package com.huawei.dmestore.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.huawei.dmestore.utils.StringUtil;
import com.vmware.vim.binding.vmodl.list;
import com.vmware.vim.binding.vmodl.map;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.dao.DmeInfoDao;
import com.huawei.dmestore.dao.ScheduleDao;
import com.huawei.dmestore.dao.SystemDao;
import com.huawei.dmestore.entity.DmeInfo;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.DmeSqlException;
import com.huawei.dmestore.task.ScheduleSetting;
import com.huawei.dmestore.utils.RestUtils;
import com.huawei.dmestore.utils.ToolUtils;
import com.huawei.dmestore.utils.VCSDKUtils;

import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * DmeAccessServiceImpl
 *
 * @author yy
 * @since 2020-09-03
 **/
public class DmeAccessServiceImpl implements DmeAccessService {
    private static final Logger LOG = LoggerFactory.getLogger(DmeAccessServiceImpl.class);

    private static final String NULL_STRING = "null";

    private static final String HOST_IP = "hostIp";

    private static final String HOST_PORT = "hostPort";

    private static final String USER_NAME = "userName";

    private static final String PASSWORD = "password";

    private static final String TASK_ID = "task_id";

    private static final String VOLUME_IDS = "volume_ids";

    private static final String HOST_COUNT = "host_count";

    private static final String PORT_NAME = "port_name";

    private static final String PROTOCOL = "protocol";

    private static final String STATUS = "status";

    private static final String ID_FIELD = "id";

    private static final String NAME_FIELD = "name";

    private static final String TYPE_FIELD = "type";

    private static final String IP_FIELD = "ip";

    private static final String PROJECT_ID_FIELD = "project_id";

    private static final String DISPLAY_STATUS_FIELD = "display_status";

    private static final String MANAGED_STATUS_FIELD = "managed_status";

    private static final String OS_STATUS_FIELD = "os_status";

    private static final String OVERALL_STATUS_FIELD = "overall_status";

    private static final String OS_TYPE_FIELD = "os_type";

    private static final String INITIATOR_COUNT_FIELD = "initiator_count";

    private static final String ACCESS_MODE_FIELD = "access_mode";

    private static final String MULTIPATH_TYPE = "multipath_type";

    private static final String THIRD_PARTY = "third_party";

    private static final String PATH_TYPE = "path_type";

    private static final String OPTIMAL_PATH = "optimal_path";

    private static final String FAILOVER_MODE = "failover_mode";

    private static final String COMMON_ALUA = "common_alua";

    private static String dmeToken;

    private static String dmeHostUrl;

    private static String dmeHostIp;

    private static int dmeHostPort;

    private DmeInfoDao dmeInfoDao;

    private SystemDao systemDao;

    private ScheduleDao scheduleDao;

    private VmfsAccessService vmfsAccessService;

    private DmeNFSAccessService dmeNfsAccessService;

    private TaskService taskService;

    private VCSDKUtils vcsdkUtils;

    @Autowired
    private ScheduleSetting scheduleSetting;

    private Gson gson = new Gson();

    private RestUtils restUtils = new RestUtils();


    private static Lock lock = new ReentrantLock();

    @Override
    public void accessDme(Map<String, Object> params) throws DmeException {
        if (params != null) {
            // 判断与服务器的连接
            ResponseEntity responseEntity = login(params);
            if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                // 连接成功后，数据入库
                try {
                    //DmeInfo dmeInfo = new Gson().fromJson(params.toString(), DmeInfo.class);
                    DmeInfo dmeInfo = new DmeInfo();
                    dmeInfo.setHostIp(ToolUtils.getStr(params.get("hostIp")));
                    dmeInfo.setHostPort(ToolUtils.getInt(params.get("hostPort")));
                    dmeInfo.setUserName(ToolUtils.getStr(params.get("userName")));
                    dmeInfo.setPassword(ToolUtils.getStr(params.get("password")));

                    dmeInfoDao.addDmeInfo(dmeInfo);
                } catch (DmeException ex) {
                    throw new DmeException(DmeConstants.ERROR_CODE_503, "连接信息保存失败:" + ex.getMessage());
                }
            } else {
                throw new DmeException(DmeConstants.ERROR_CODE_503, "连接信息保存失败:" + responseEntity.toString());
            }
        }
    }

    @Override
    public Map<String, Object> refreshDme() {
        LOG.info("====refreshDme begin=====");
        Map<String, Object> params = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        params.put("code", DmeConstants.HTTPS_STATUS_SUCCESS_200);
        try {
            DmeInfo dmeInfo = dmeInfoDao.getDmeInfo();
            if (dmeInfo != null && dmeInfo.getHostIp() != null) {
                params.put(HOST_IP, dmeInfo.getHostIp());
                params.put(HOST_PORT, dmeInfo.getHostPort());
            } else {
                params.put("code", DmeConstants.HTTPS_STATUS_SUCCESS_200);
                params.put("message", "DME未接入");
                return params;
            }

            // 判断与服务器的连接
            ResponseEntity responseEntity = access(DmeConstants.REFRES_STATE_URL, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() != RestUtils.RES_STATE_I_200) {
                params.put("code", DmeConstants.ERROR_CODE_503);
                params.put("message", "更新连接状态失败:" + responseEntity.toString());
            }
        } catch (DmeException e) {
            params.put("code", DmeConstants.ERROR_CODE_503);
            params.put("message", "更新连接状态失败:" + e.getMessage());
        } catch (RestClientException e) {
            params.put("code", DmeConstants.ERROR_CODE_503);
            params.put("message", "连接失败:" + e.getMessage());
        }

        // 成功返回200
        LOG.info("====refreshDme end=====response:{}", gson.toJson(params));
        return params;
    }

    @Override
    public void disconnectDme() throws DmeSqlException {
        dmeToken = null;
        dmeHostUrl = null;
        systemDao.cleanAllData(true);
    }

    @Override
    public ResponseEntity<String> access(String requestUrl, HttpMethod method, String requestBody) throws DmeException {
        String url = requestUrl;
        if (StringUtils.isEmpty(dmeToken)) {
            // 如果token为空，就自动登录，获取token
            LOG.info("token is null，automatic login to get token");
            iniLogin();
        }

        RestTemplate restTemplate = restUtils.getRestTemplate();

        HttpHeaders headers = getHeaders();

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        if (url.indexOf(DmeConstants.HTTP) < 0) {
            url = dmeHostUrl + url;
        }
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange(url, method, entity, String.class);
        } catch (HttpClientErrorException e) {
            LOG.error("HttpClientErrorException:{}", e.toString());
            responseEntity = new ResponseEntity(e.getStatusCode());
        }
        if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_401
                || responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_403) {
            LOG.info("token invalid, login again!");
            dmeToken = null;
            iniLogin();
            LOG.info("get the new token successfully, reexecute the previous task！url={}", url);
            headers = getHeaders();
            entity = new HttpEntity<>(requestBody, headers);
            responseEntity = restTemplate.exchange(url, method, entity, String.class);
        }

        LOG.info("{},retutn code={}", url, responseEntity.getStatusCodeValue());
        if (!String.valueOf(responseEntity.getStatusCodeValue()).startsWith("2")) {
            LOG.info("{},{}==failed!，response={}", url, method.name(), gson.toJson(responseEntity));
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> accessByJson(String requestUrl, HttpMethod method, String jsonBody)
            throws DmeException {
        String url = requestUrl;
        if (StringUtils.isEmpty(dmeToken)) {
            iniLogin();
        }
        RestTemplate restTemplate = restUtils.getRestTemplate();
        HttpHeaders headers = getHeaders();
        ResponseEntity<String> responseEntity;
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        if (url.indexOf(DmeConstants.HTTP) < 0) {
            url = dmeHostUrl + url;
        }
        try {
            responseEntity = restTemplate.exchange(url, method, entity, String.class, jsonBody);
        } catch (HttpClientErrorException e) {
            LOG.error("HttpClientErrorException:{}", e.toString());
            responseEntity = new ResponseEntity<String>(e.getStatusCode());
        }
        LOG.info("{},==accessByJson responseEntity=={}", url.replace("{json}", jsonBody),
                responseEntity == null ? NULL_STRING : responseEntity.getStatusCodeValue());
        if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_401
                || responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_403) {
            dmeToken = null;
            iniLogin();
            headers = getHeaders();
            entity = new HttpEntity<>(null, headers);
            responseEntity = restTemplate.exchange(url, method, entity, String.class, jsonBody);
        }

        if (!String.valueOf(responseEntity.getStatusCodeValue()).startsWith("2")) {
            LOG.info("{},{}==execution，response={}, jsonBody={}", url, method.name(), gson.toJson(responseEntity),
                    jsonBody);
        }
        return responseEntity;
    }

    private ResponseEntity login(Map<String, Object> params) throws DmeException {
        ResponseEntity responseEntity;
        try {
            lock.lock();
            responseEntity = null;
            dmeToken = null;
            if (params != null && params.get(DmeConstants.HOSTIP) != null) {
                HttpHeaders headers = getHeaders();
                Map<String, Object> requestbody = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
                requestbody.put("grantType", PASSWORD);
                requestbody.put(USER_NAME, params.get(USER_NAME));
                requestbody.put("value", params.get(PASSWORD));
                String hostUrl = "https://" + params.get(HOST_IP) + ":" + params.get(HOST_PORT);
                HttpEntity<String> entity = new HttpEntity<>(gson.toJson(requestbody), headers);
                RestTemplate restTemplate = restUtils.getRestTemplate();
                String url = hostUrl + DmeConstants.LOGIN_DME_URL;
                responseEntity = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
                LOG.info("login end!url={},return={}", url, gson.toJson(responseEntity));
                if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                    JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                    if (jsonObject != null && jsonObject.get(DmeConstants.ACCESSSESSION) != null) {
                        dmeToken = jsonObject.get("accessSession").getAsString();
                        dmeHostUrl = hostUrl;
                        dmeHostIp = params.get(HOST_IP).toString();
                        dmeHostPort = Integer.parseInt(params.get(HOST_PORT).toString());
                    }
                } else {
                    LOG.info("url:{},userName={},password={},authentication failed！", url, params.get(USER_NAME),
                            params.get(PASSWORD));
                }
            }
        } finally {
            lock.unlock();
        }

        return responseEntity;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        // 调整传递参数header 解决传递中文乱码问题
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        if (!StringUtils.isEmpty(dmeToken)) {
            headers.set("X-Auth-Token", dmeToken);
        }
        return headers;
    }

    private void iniLogin() throws DmeException {
        try {
            lock.lock();
            if (StringUtils.isEmpty(dmeToken)) {
                // 查询数据库
                DmeInfo dmeInfo = dmeInfoDao.getDmeInfo();
                if (dmeInfo != null && dmeInfo.getHostIp() != null) {
                    Map<String, Object> params = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
                    params.put(HOST_IP, dmeInfo.getHostIp());
                    params.put(HOST_PORT, dmeInfo.getHostPort());
                    params.put(USER_NAME, dmeInfo.getUserName());
                    params.put(PASSWORD, dmeInfo.getPassword());

                    // 登录
                    login(params);
                } else {
                    throw new DmeException("目前没有DME接入信息");
                }
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<Map<String, Object>> getWorkLoads(String storageId) throws DmeException {
        List<Map<String, Object>> relists = null;
        try {
            if (!StringUtils.isEmpty(storageId)) {
                String workloadsUrl = DmeConstants.GET_WORKLOADS_URL.replace("{storage_id}", storageId);
                try {
                    ResponseEntity responseEntity = access(workloadsUrl, HttpMethod.GET, null);
                    if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                        JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString())
                                .getAsJsonObject();
                        if (jsonObject != null && jsonObject.get(DmeConstants.DATAS) != null) {
                            JsonArray jsonArray = jsonObject.getAsJsonArray(DmeConstants.DATAS);
                            relists = new ArrayList<>();
                            for (int index = 0; index < jsonArray.size(); index++) {
                                JsonObject vjson = jsonArray.get(index).getAsJsonObject();
                                Map<String, Object> map = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
                                map.put("block_size", ToolUtils.jsonToStr(vjson.get("block_size")));
                                map.put("create_type", ToolUtils.jsonToStr(vjson.get("create_type")));
                                map.put("enable_compress", ToolUtils.jsonToBoo(vjson.get("enable_compress")));
                                map.put("enable_dedup", ToolUtils.jsonToBoo(vjson.get("enable_dedup")));
                                map.put(ID_FIELD, ToolUtils.jsonToStr(vjson.get(ID_FIELD)));
                                map.put(NAME_FIELD, ToolUtils.jsonToStr(vjson.get(NAME_FIELD)));
                                map.put(TYPE_FIELD, ToolUtils.jsonToStr(vjson.get(TYPE_FIELD)));

                                relists.add(map);
                            }
                        }
                    }else {
                        LOG.error("getWorkLoads error url:{},error:{}", workloadsUrl,responseEntity.getStatusCodeValue());
                    }
                } catch (DmeException e) {
                    LOG.error("getWorkLoads error url:{},error:{}", workloadsUrl, e.toString());
                    throw new DmeException(DmeConstants.ERROR_CODE_503,
                            "DME link error url:" + workloadsUrl + ",error:" + e.toString());
                }
            }
        } catch (DmeException e) {
            LOG.error("get WorkLoads error:", e);
            throw new DmeException(DmeConstants.ERROR_CODE_503, "get WorkLoads error:" + e.getMessage());
        }
        List<Map<String, Object>> newList = replaceSpecialChar(relists);
        return newList;
    }

    @Override
    public List<Map<String, Object>> getDmeHosts(String hostIp) throws DmeException {
        List<Map<String, Object>> relists = null;
        String getHostsUrl = DmeConstants.DME_HOST_SUMMARY_URL;
        try {
            Map<String, Object> requestbody = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
            if (!StringUtils.isEmpty(hostIp)) {
                requestbody.put(IP_FIELD, hostIp);
            }
            ResponseEntity responseEntity = access(getHostsUrl, HttpMethod.POST, gson.toJson(requestbody));
            if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                JsonArray jsonArray = jsonObject.getAsJsonArray(DmeConstants.HOSTS);
                if (jsonArray != null && jsonArray.size() > 0) {
                    relists = new ArrayList<>();
                    for (int index = 0; index < jsonArray.size(); index++) {
                        JsonObject vjson = jsonArray.get(index).getAsJsonObject();
                        Map<String, Object> map = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
                        map.put(ID_FIELD, ToolUtils.jsonToStr(vjson.get(ID_FIELD)));
                        map.put(PROJECT_ID_FIELD, ToolUtils.jsonToStr(vjson.get(PROJECT_ID_FIELD)));
                        map.put(NAME_FIELD, ToolUtils.jsonToStr(vjson.get(NAME_FIELD)));
                        map.put(IP_FIELD, ToolUtils.jsonToStr(vjson.get(IP_FIELD)));
                        map.put(DISPLAY_STATUS_FIELD, ToolUtils.jsonToStr(vjson.get(DISPLAY_STATUS_FIELD)));
                        map.put(MANAGED_STATUS_FIELD, ToolUtils.jsonToStr(vjson.get(MANAGED_STATUS_FIELD)));
                        map.put(OS_STATUS_FIELD, ToolUtils.jsonToStr(vjson.get(OS_STATUS_FIELD)));
                        map.put(OVERALL_STATUS_FIELD, ToolUtils.jsonToStr(vjson.get(OVERALL_STATUS_FIELD)));
                        map.put(OS_TYPE_FIELD, ToolUtils.jsonToStr(vjson.get(OS_TYPE_FIELD)));
                        map.put(INITIATOR_COUNT_FIELD, ToolUtils.jsonToInt(vjson.get(INITIATOR_COUNT_FIELD), null));
                        map.put(ACCESS_MODE_FIELD, ToolUtils.jsonToStr(vjson.get(ACCESS_MODE_FIELD)));
                        JsonArray hostgroups = vjson.getAsJsonArray("hostGroups");
                        if (hostgroups != null && hostgroups.size() > 0) {
                            List<Map<String, Object>> hglists = new ArrayList<>();
                            for (int indext2 = 0; indext2 < hostgroups.size(); indext2++) {
                                Map<String, Object> hgmap = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
                                hgmap.put(ID_FIELD, ToolUtils.jsonToStr(vjson.get(ID_FIELD)));
                                hgmap.put(NAME_FIELD, ToolUtils.jsonToStr(vjson.get(NAME_FIELD)));
                                hglists.add(hgmap);
                            }
                            map.put("hostGroups", hglists);
                        }
                        relists.add(map);
                    }
                }
            }
        } catch (DmeException e) {
            throw new DmeException(e.getMessage());
        }
        return relists;
    }

    @Override
    public List<Map<String, Object>> getDmeHostInitiators(String hostId) throws DmeException {
        List<Map<String, Object>> relists = null;
        String url = DmeConstants.GET_DME_HOSTS_INITIATORS_URL.replace("{host_id}", hostId);
        try {
            ResponseEntity responseEntity = access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                if (jsonObject != null && jsonObject.get(DmeConstants.INITIATORS) != null) {
                    JsonArray jsonArray = jsonObject.getAsJsonArray("initiators");
                    if (jsonArray != null && jsonArray.size() > 0) {
                        relists = new ArrayList<>();
                        for (int index = 0; index < jsonArray.size(); index++) {
                            JsonObject vjson = jsonArray.get(index).getAsJsonObject();
                            Map<String, Object> map = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
                            map.put(ID_FIELD, ToolUtils.jsonToStr(vjson.get(ID_FIELD)));
                            map.put(PORT_NAME, ToolUtils.jsonToStr(vjson.get(PORT_NAME)));
                            map.put(STATUS, ToolUtils.jsonToStr(vjson.get(STATUS)));
                            map.put(PROTOCOL, ToolUtils.jsonToStr(vjson.get(PROTOCOL)));

                            relists.add(map);
                        }
                    }
                }
            }
        } catch (DmeException e) {
            LOG.error("url:{},error:{}", url, e.toString());
            throw new DmeException(e.getMessage());
        }
        return relists;
    }

    @Override
    public List<Map<String, Object>> getDmeHostGroups(String hostGroupName) throws DmeException {
        LOG.info("search oriented host begin ！");
        List<Map<String, Object>> relists = null;
        String getHostGroupsUrl = DmeConstants.GET_DME_HOSTGROUPS_URL;
        try {
            Map<String, Object> requestbody = null;
            if (!StringUtils.isEmpty(hostGroupName)) {
                requestbody = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
                requestbody.put(NAME_FIELD, hostGroupName);
            }
            LOG.info("search oriented host group requestBody{}!", gson.toJson(requestbody));
            ResponseEntity responseEntity = access(getHostGroupsUrl, HttpMethod.POST,
                    requestbody == null ? null : gson.toJson(requestbody));
            LOG.info("search oriented host group responseBody{}!", gson.toJson(responseEntity));
            if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                JsonArray jsonArray = jsonObject.getAsJsonArray(DmeConstants.HOSTGROUPS);
                relists = new ArrayList<>();
                for (int index = 0; index < jsonArray.size(); index++) {
                    JsonObject object = jsonArray.get(index).getAsJsonObject();
                    Map<String, Object> map = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
                    map.put(ID_FIELD, ToolUtils.jsonToStr(object.get(ID_FIELD)));
                    // 主机名称
                    map.put(NAME_FIELD, ToolUtils.jsonToStr(object.get(NAME_FIELD)));
                    map.put(HOST_COUNT, ToolUtils.jsonToInt(object.get(HOST_COUNT), 0));
                    map.put("source_type", ToolUtils.jsonToStr(object.get("source_type")));
                    map.put(MANAGED_STATUS_FIELD, ToolUtils.jsonToStr(object.get(MANAGED_STATUS_FIELD)));
                    map.put(PROJECT_ID_FIELD, ToolUtils.jsonToStr(object.get(PROJECT_ID_FIELD)));

                    relists.add(map);
                }
            }
        } catch (DmeException e) {
            LOG.error("url:{},error:{}", getHostGroupsUrl, e.toString());
            throw new DmeException(e.getMessage());
        }
        LOG.info("getDmeHostgroups relists==={}", relists == null ? NULL_STRING : relists.toString());
        return relists;
    }

    @Override
    public Map<String, Object> createHost(Map<String, Object> params) throws DmeException {
        Map<String, Object> hostmap = null;
        String createHostUrl = DmeConstants.CREATE_DME_HOST_URL;
        try {
            if (params != null && params.get(DmeConstants.HOST) != null) {
                // 得到主机的hba信息
                List<Map<String, Object>> initiators = new ArrayList<>();
                List<Map<String, Object>> hbalists = vcsdkUtils.getHbaByHostObjectId(ToolUtils.getStr(params.get("hostId")));
                for (Map<String, Object> hbamap : hbalists) {
                    Map<String, Object> initiator = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
                    initiator.put(PROTOCOL, ToolUtils.getStr(hbamap.get(TYPE_FIELD)));
                    initiator.put(PORT_NAME, ToolUtils.getStr(hbamap.get(NAME_FIELD)));
                    initiators.add(initiator);
                }
                Map<String, Object> requestbody = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
                requestbody.put(ACCESS_MODE_FIELD, "NONE");
                requestbody.put(TYPE_FIELD, "VMWAREESX");
                //requestbody.put(IP_FIELD, params.get("host"));
                requestbody.put("host_name", params.get("host"));
                requestbody.put("initiator", initiators);
                //接入主机三方路径配置，需要配套参数。path_type 优选路径 ，failover_mode 通用ALUA
                // v5配置多路径参数有效，V6设备自适应设置参数无效。
                requestbody.put(MULTIPATH_TYPE, THIRD_PARTY);
                // 优选路径
                requestbody.put(PATH_TYPE, OPTIMAL_PATH);
                // 通用ALUA
                requestbody.put(FAILOVER_MODE, COMMON_ALUA);
                LOG.info("Create logical host request parameters for exsi host on DME:{}", gson.toJson(requestbody));
                ResponseEntity responseEntity = access(createHostUrl, HttpMethod.POST, gson.toJson(requestbody));
                if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                    JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString())
                            .getAsJsonObject();
                    hostmap = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
                    hostmap.put(ID_FIELD, ToolUtils.jsonToStr(jsonObject.get(DmeConstants.ID)));
                    hostmap.put(IP_FIELD, ToolUtils.jsonToStr(jsonObject.get(IP_FIELD)));
                    hostmap.put(ACCESS_MODE_FIELD, ToolUtils.jsonToStr(jsonObject.get(ACCESS_MODE_FIELD)));
                    hostmap.put(TYPE_FIELD, ToolUtils.jsonToStr(jsonObject.get(TYPE_FIELD)));
                    hostmap.put("port", ToolUtils.jsonToInt(jsonObject.get("port"), 0));
                }
            }
        } catch (DmeException e) {
            LOG.error("DME link error url:{},error:", createHostUrl, e.toString());
            throw new DmeException(e.getMessage());
        }
        LOG.info("createHost hostmap==={}", hostmap == null ? NULL_STRING : hostmap.size());
        return hostmap;
    }

    @Override
    public Map<String, Object> createHostGroup(Map<String, Object> params) throws DmeException {
        Map<String, Object> hostgroupmap = null;
        String createHostGroupUrl = DmeConstants.CREATE_DME_HOSTGROUP_URL;
        try {
            Map<String, Object> requestbody;
            if (params != null && params.get(DmeConstants.CLUSTER) != null
                    && params.get(DmeConstants.HOSTIDS) != null) {
                // 判断该集群下有多少主机，如果主机在DME不存在就需要创建
                requestbody = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
                requestbody.put(NAME_FIELD, params.get("cluster").toString());
                requestbody.put("host_ids", params.get("hostids"));

                ResponseEntity responseEntity = access(createHostGroupUrl, HttpMethod.POST, gson.toJson(requestbody));
                if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                    JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString())
                            .getAsJsonObject();
                    if (jsonObject != null && jsonObject.get(DmeConstants.ID) != null) {
                        hostgroupmap = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
                        hostgroupmap.put(ID_FIELD, ToolUtils.jsonToStr(jsonObject.get(ID_FIELD)));
                        hostgroupmap.put(NAME_FIELD, ToolUtils.jsonToStr(jsonObject.get(NAME_FIELD)));
                    }
                }else {
                    String desc = "";
                    try {
                        JsonObject vjson = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                        desc = StringUtil.dealQuotationMarks(ToolUtils.getStr(vjson.get("error_msg")));
                    }catch (Exception ess){
                        desc = "";
                    }
                    if (!StringUtils.isEmpty(desc)) {
                        throw new DmeException("create hostgroup error,the DME return " + responseEntity.getStatusCode() + "," + desc);
                    }else {
                        throw new DmeException("create hostgroup error,the DME return " + responseEntity.getStatusCode());
                    }
                }
            }
        } catch (DmeException e) {
            LOG.error("DME link error url:{},error:", createHostGroupUrl, e.toString());
            throw new DmeException(e.getMessage());
        }
        LOG.info("createHostGroup hostmap==={}", hostgroupmap == null ? NULL_STRING : (hostgroupmap.size()));
        return hostgroupmap;
    }

    public SystemDao getSystemDao() {
        return systemDao;
    }

    public void setSystemDao(SystemDao systemDao) {
        this.systemDao = systemDao;
    }

    public void setDmeInfoDao(DmeInfoDao dmeInfoDao) {
        this.dmeInfoDao = dmeInfoDao;
    }

    public void setVmfsAccessService(VmfsAccessService vmfsAccessService) {
        this.vmfsAccessService = vmfsAccessService;
    }

    public void setDmeNfsAccessService(DmeNFSAccessService dmeNfsAccessService) {
        this.dmeNfsAccessService = dmeNfsAccessService;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
    }

    public void setScheduleDao(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public Map<String, Object> getDmeHost(String hostId) throws DmeException {
        Map<String, Object> map = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        String getHostUrl = DmeConstants.GET_DME_HOST_URL.replace("{host_id}", hostId);
        try {
            ResponseEntity responseEntity = access(getHostUrl, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                JsonObject vjson = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                map.put(ID_FIELD, ToolUtils.jsonToStr(vjson.get(ID_FIELD)));
                map.put(NAME_FIELD, ToolUtils.jsonToStr(vjson.get(NAME_FIELD)));
                map.put(IP_FIELD, ToolUtils.jsonToStr(vjson.get(IP_FIELD)));
                map.put(DISPLAY_STATUS_FIELD, ToolUtils.jsonToStr(vjson.get(DISPLAY_STATUS_FIELD)));
                map.put(MANAGED_STATUS_FIELD, ToolUtils.jsonToStr(vjson.get(MANAGED_STATUS_FIELD)));
                map.put(OS_STATUS_FIELD, ToolUtils.jsonToStr(vjson.get(OS_STATUS_FIELD)));
                map.put(OVERALL_STATUS_FIELD, ToolUtils.jsonToStr(vjson.get(OVERALL_STATUS_FIELD)));
                map.put(OS_TYPE_FIELD, ToolUtils.jsonToStr(vjson.get(OS_TYPE_FIELD)));
                map.put(INITIATOR_COUNT_FIELD, ToolUtils.jsonToInt(vjson.get(INITIATOR_COUNT_FIELD), null));
                map.put(ACCESS_MODE_FIELD, ToolUtils.jsonToStr(vjson.get(ACCESS_MODE_FIELD)));
            }
        } catch (DmeException e) {
            LOG.error("DME link error url:{},error:{}", getHostUrl, e.toString());
            throw new DmeException(e.getMessage());
        }
        LOG.info("getDmeHost relists===size={}", map.size());
        return map;
    }

    @Override
    public void scanDatastore(String storageType) throws DmeException {
        LOG.info("scanDatastore storageType={}", storageType);
        if (!StringUtils.isEmpty(storageType)) {
            if (storageType.equalsIgnoreCase(DmeConstants.STORE_TYPE_VMFS)) {
                LOG.info("scan VMFS Datastore start");
                vmfsAccessService.scanVmfs();
                LOG.info("scan VMFS Datastore end");
            } else if (storageType.equalsIgnoreCase(ToolUtils.STORE_TYPE_NFS)) {
                LOG.info("scan NFS Datastore start");
                dmeNfsAccessService.scanNfs();
                LOG.info("scan NFS Datastore end");
            } else if (storageType.equalsIgnoreCase(ToolUtils.STORE_TYPE_ALL)) {
                try {
                    // 扫描vmfs
                    LOG.info("scan VMFS Datastore start");
                    vmfsAccessService.scanVmfs();
                    LOG.info("scan VMFS Datastore end");
                } catch (DmeException e) {
                    LOG.error("scan VMFS failed！error:{}", e.getMessage());
                }
                try {
                    // 扫描nfs
                    LOG.info("scan NFS Datastore start");
                    dmeNfsAccessService.scanNfs();
                    LOG.info("scan NFS Datastore end");
                } catch (DmeException e) {
                    LOG.error("scan NFS failed！error:{}", e.getMessage());
                }
            }
        }
    }

    @Override
    public void configureTaskTime(Integer taskId, String taskCron) throws DmeException {
        try {
            if (!StringUtils.isEmpty(taskId) && !StringUtils.isEmpty(taskCron)) {
                int re = scheduleDao.updateTaskTime(taskId, taskCron);
                if (re > 0) {
                    scheduleSetting.refreshTasks(taskId, taskCron);
                }
            } else {
                throw new DmeException("configure Task Time error:taskId or taskCorn is null");
            }
        } catch (SQLException e) {
            throw new DmeException(e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getDmeHostGroup(String hostGroupId) throws DmeException {
        Map<String, Object> map = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        String getHostGroupUrl = DmeConstants.GET_DME_HOSTGROUP_URL.replace("{hostgroup_id}", hostGroupId);
        try {
            ResponseEntity responseEntity = access(getHostGroupUrl, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                JsonObject vjson = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                if (vjson != null) {
                    map.put(ID_FIELD, ToolUtils.jsonToStr(vjson.get(ID_FIELD)));
                    map.put(NAME_FIELD, ToolUtils.jsonToStr(vjson.get(NAME_FIELD)));
                    map.put(HOST_COUNT, ToolUtils.jsonToStr(vjson.get(HOST_COUNT)));
                    map.put(PROJECT_ID_FIELD, ToolUtils.jsonToStr(vjson.get(PROJECT_ID_FIELD)));
                }
            }
        } catch (DmeException e) {
            throw new DmeException(e.getMessage());
        }
        return map;
    }

    @Override
    public List<Map<String, Object>> getDmeHostInHostGroup(String hostGroupId) throws DmeException {
        List<Map<String, Object>> list = null;
        String getHostInHostGroupUrl = DmeConstants.GET_DME_HOSTS_IN_HOSTGROUP_URL.replace("{hostgroup_id}",
                hostGroupId);
        try {
            Map<String, Object> requestbody = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
            // 查询指定主机组
            ResponseEntity responseEntity = access(getHostInHostGroupUrl, HttpMethod.POST, gson.toJson(requestbody));
            if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                list = new ArrayList<>();
                JsonObject datajson = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                JsonArray hostsja = datajson.getAsJsonArray(DmeConstants.HOSTS);
                if (hostsja != null && hostsja.size() > 0) {
                    for (int index = 0; index < hostsja.size(); index++) {
                        JsonObject hostjs = hostsja.get(index).getAsJsonObject();
                        Map<String, Object> hostmap = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
                        hostmap.put(ID_FIELD, ToolUtils.jsonToStr(hostjs.get(ID_FIELD)));
                        hostmap.put(NAME_FIELD, ToolUtils.jsonToStr(hostjs.get(NAME_FIELD)));
                        hostmap.put(HOST_COUNT, ToolUtils.jsonToStr(hostjs.get(IP_FIELD)));
                        // list.size 可以代表主机组中主机的数量
                        list.add(hostmap);
                    }
                }
            }
        } catch (DmeException e) {
            LOG.error("DME link error url:{},error:{}", getHostInHostGroupUrl, e.getMessage());
            throw new DmeException(e.getMessage());
        }
        return list;
    }

    @Override
    public void deleteVolumes(List<String> ids, String language) throws DmeException {
        String url = DmeConstants.DME_VOLUME_DELETE_URL;
        JsonObject body = new JsonObject();
        JsonArray array = new JsonParser().parse(gson.toJson(ids)).getAsJsonArray();
        body.add(VOLUME_IDS, array);
        ResponseEntity<String> responseEntity = access(url, HttpMethod.POST, body.toString());
        if (responseEntity.getStatusCodeValue() / DmeConstants.HTTPS_STATUS_CHECK_FLAG
                != DmeConstants.HTTPS_STATUS_SUCCESS_PRE) {
            throw new DmeException(responseEntity.getBody());
        }

        JsonObject task = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        String taskId = task.get(TASK_ID).getAsString();
        JsonObject taskDetail = taskService.queryTaskByIdUntilFinish(taskId);
        if (taskDetail.get(DmeConstants.TASK_DETAIL_STATUS_FILE).getAsInt() != DmeConstants.TASK_SUCCESS) {
            String errMessage = DmeConstants.LANGUAGE_CN.equals(language)?
                taskDetail.get(DmeConstants.TASK_DETAIL_CN).getAsString() :
                taskDetail.get(DmeConstants.TASK_DETAIL_EN).getAsString();
            LOG.error("delete volumes failed!task status={},errMsg={}", task.get(STATUS).getAsInt(), errMessage);
            throw new DmeException(errMessage);
        }
    }

    @Override
    public void unMapHost(String hostId, List<String> ids, String language) throws DmeException {
        String url = DmeConstants.DME_HOST_UNMAPPING_URL;
        JsonObject body = new JsonObject();
        JsonArray array = new JsonParser().parse(gson.toJson(ids)).getAsJsonArray();
        body.add(VOLUME_IDS, array);
        body.addProperty("host_id", hostId);
        ResponseEntity<String> responseEntity = access(url, HttpMethod.POST, body.toString());
        if (responseEntity.getStatusCodeValue() / DmeConstants.HTTPS_STATUS_CHECK_FLAG
                != DmeConstants.HTTPS_STATUS_SUCCESS_PRE) {
            throw new DmeException(responseEntity.getBody());
        }

        JsonObject task = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        String taskId = task.get(TASK_ID).getAsString();
        JsonObject taskDetail = taskService.queryTaskByIdUntilFinish(taskId);
        if (taskDetail.get(DmeConstants.TASK_DETAIL_STATUS_FILE).getAsInt() != DmeConstants.TASK_SUCCESS) {
            // 错误信息构建
            String errMessage = DmeConstants.LANGUAGE_CN.equals(language)?
                taskDetail.get(DmeConstants.TASK_DETAIL_CN).getAsString() :
                taskDetail.get(DmeConstants.TASK_DETAIL_EN).getAsString();
            LOG.error("host unmapping failed!task status={}，errMsg={}", task.get(STATUS).getAsInt(), errMessage);
            throw new DmeException(errMessage);
        }
    }

    @Override
    public void hostMapping(String hostId, List<String> volumeIds, String language) throws DmeException {
        String url = DmeConstants.DME_HOST_MAPPING_URL;
        JsonObject body = new JsonObject();
        body.addProperty("host_id", hostId);
        JsonArray volumeIdArray = gson.fromJson(gson.toJson(volumeIds), JsonArray.class);
        body.add(VOLUME_IDS, volumeIdArray);
        ResponseEntity<String> responseEntity = access(url, HttpMethod.POST, body.toString());
        if (responseEntity.getStatusCodeValue() / DmeConstants.HTTPS_STATUS_CHECK_FLAG
                != DmeConstants.HTTPS_STATUS_SUCCESS_PRE) {
            LOG.error("host mapping failed!errorMsg:{}", responseEntity.getBody());
            throw new DmeException(responseEntity.getBody());
        }
        JsonObject task = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        String taskId = task.get(TASK_ID).getAsString();
        JsonObject taskDetail = taskService.queryTaskByIdUntilFinish(taskId);
        if (taskDetail.get(DmeConstants.TASK_DETAIL_STATUS_FILE).getAsInt() != DmeConstants.TASK_SUCCESS) {
            // 错误信息构建
            String errMessage = DmeConstants.LANGUAGE_CN.equals(language)?
                taskDetail.get(DmeConstants.TASK_DETAIL_CN).getAsString():taskDetail.get(DmeConstants.TASK_DETAIL_EN).getAsString();
            LOG.error("host mapping failed!task status={},errMsg:{}", task.get(STATUS).getAsInt(), errMessage);
            throw new DmeException(errMessage);
        }
    }

    /**
     * @return @return
     * @throws
     * @Description: /
     * @Param @param null
     * @author yc
     * @Date 2021/4/20 16:20
     */
    private   List<Map<String, Object>> replaceSpecialChar(List<Map<String, Object>> list) {
        List<Map<String, Object>> objList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(list)) {
            for (Map<String, Object> map : list) {
                Map<String, Object> objMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(map)){
                    Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry entry = (Map.Entry) it.next();
                        Object key = entry.getKey();
                        Object value = entry.getValue();
                        if ((!StringUtils.isEmpty(value)) && ToolUtils.getStr(value).contains("&amp;")) {
                            value = ToolUtils.getStr(value).replace("&amp;", "&");
                        }
                        objMap.put(ToolUtils.getStr(key), ToolUtils.getStr(value));
                    }
                }
                objList.add(objMap);
            }
        }
        return objList;
    }
}
