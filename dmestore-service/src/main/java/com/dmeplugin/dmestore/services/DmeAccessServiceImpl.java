package com.dmeplugin.dmestore.services;


import com.dmeplugin.dmestore.dao.DmeInfoDao;
import com.dmeplugin.dmestore.dao.ScheduleDao;
import com.dmeplugin.dmestore.entity.DmeInfo;
import com.dmeplugin.dmestore.model.DMEHostInfo;
import com.dmeplugin.dmestore.task.ScheduleSetting;
import com.dmeplugin.dmestore.utils.RestUtils;
import com.dmeplugin.dmestore.utils.ToolUtils;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @Description: TODO
 * @ClassName: DmeAccessServiceImpl
 * @Company: GH-CA
 * @author: yy
 * @create: 2020-09-02
 **/
public class DmeAccessServiceImpl implements DmeAccessService {

    private static final Logger LOG = LoggerFactory.getLogger(DmeAccessServiceImpl.class);

    private DmeInfoDao dmeInfoDao;

    private ScheduleDao scheduleDao;

    private VmfsAccessService vmfsAccessService;

    private DmeNFSAccessService dmeNfsAccessService;

    private VCSDKUtils vcsdkUtils;

    @Autowired
    private ScheduleSetting scheduleSetting;

    private Gson gson = new Gson();

    private static String dmeToken;
    private static String dmeHostUrl;
    private static String dmeHostIp;
    private static Integer dmeHostPort;

    private final String LOGIN_DME_URL = "/rest/plat/smapp/v1/sessions";
    private final String REFRES_STATE_URL = "/rest/blockservice/v1/volumes?limit=1";
    private final String GET_WORKLOADS_URL = "/rest/storagemgmt/v1/storages/{storage_id}/workloads";
    private final String GET_DME_HOSTS_URL = "/rest/hostmgmt/v1/hosts/summary";
    private final String GET_DME_HOSTGROUPS_URL = "/rest/hostmgmt/v1/hostgroups/summary";
    private final String CREATE_DME_HOST_URL = "/rest/hostmgmt/v1/hosts";
    private final String CREATE_DME_HOSTGROUP_URL = "/rest/hostmgmt/v1/hostgroups";
    private final String GET_DME_HOST_URL = "/rest/hostmgmt/v1/hosts/{host_id}/summary";
    private final String GET_DME_HOSTGROUP_URL = "/rest/hostmgmt/v1/hostgroups/{hostgroup_id}/summary";

    private final String GET_DME_HOSTS_INITIATORS_URL = "/rest/hostmgmt/v1/hosts/{host_id}/initiators";
    private final String GET_DME_HOSTS_IN_HOSTGROUP_URL = "/rest/hostmgmt/v1/hostgroups/{hostgroup_id}/hosts/list";


    @Override
    public Map<String, Object> accessDme(Map<String, Object> params) {
        Map<String, Object> remap = new HashMap<>(16);
        remap.put("code", 200);
        remap.put("message", "连接成功");
        remap.put("data", params);
        try {
            LOG.info("params==" + (params == null ? "null" : gson.toJson(params)));
            if (params != null) {
                //判断与服务器的连接
                ResponseEntity responseEntity = login(params);
                if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                    //连接成功后，数据入库
                    try {
                        DmeInfo dmeInfo = new Gson().fromJson(params.toString(), DmeInfo.class);
                        LOG.info("dmeInfo==" + gson.toJson(dmeInfo));
                        int re = dmeInfoDao.addDmeInfo(dmeInfo);
                        LOG.info("re==" + re);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        remap.put("code", 503);
                        remap.put("message", "连接信息保存失败:" + ex.getMessage());
                    }
                } else {
                    remap.put("code", 503);
                    remap.put("message", "连接失败:" + responseEntity.toString());
                }
            }
        } catch (Exception e) {
            remap.put("code", 503);
            remap.put("message", "连接失败:" + e.getMessage());
        }

        return remap;
    }

    @Override
    public Map<String, Object> refreshDme() {
        Map<String, Object> remap = new HashMap<>(16);
        remap.put("code", 200);
        remap.put("message", "更新连接状态成功");
        try {
            //判断与服务器的连接
            ResponseEntity responseEntity = access(REFRES_STATE_URL, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() != RestUtils.RES_STATE_I_200) {
                remap.put("code", 503);
                remap.put("message", "更新连接状态失败:" + responseEntity.toString());
            }

        } catch (Exception e) {
            remap.put("code", 503);
            remap.put("message", "更新连接状态失败:" + e.toString());
        }

        Map<String, Object> params = new HashMap<>(16);
        params.put("hostIp", dmeHostIp);
        params.put("hostPort", dmeHostPort);
        remap.put("data", params);

        return remap;
    }

    @Override
    public ResponseEntity<String> access(String url, HttpMethod method, String requestBody) throws Exception {
        ResponseEntity<String> responseEntity = null;

        if (StringUtils.isEmpty(dmeToken)) {
            //如果token为空，就自动登录，获取token
            LOG.info("token为空，自动登录，获取token");
            iniLogin();
        }

        RestUtils restUtils = new RestUtils();
        RestTemplate restTemplate = restUtils.getRestTemplate();

        HttpHeaders headers = getHeaders();

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        if (url.indexOf(DmeConstants.HTTP) < 0) {
            url = dmeHostUrl + url;
        }
        try {
            responseEntity = restTemplate.exchange(url, method, entity, String.class);
        }catch (HttpClientErrorException e){
            LOG.error("HttpClientErrorException:"+e.toString());
            responseEntity = new ResponseEntity<String>(e.getStatusCode());

        }
        LOG.info(url + "==responseEntity==" + (responseEntity == null ? "null" : responseEntity.getStatusCodeValue()));
        if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_401 ||
                responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_403) {
            //如果token失效，重新登录
            dmeToken = null;
            LOG.info("token失效，重新登录，获取token");
            iniLogin();
            //得到新token后，重新执行上次任务
            LOG.info("得到新token后，重新执行上次任务，dmeToken==" + dmeToken);
            headers = getHeaders();
            entity = new HttpEntity<>(requestBody, headers);
            responseEntity = restTemplate.exchange(url, method, entity, String.class);
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> accessByJson(String url, HttpMethod method, String jsonBody) throws Exception{
        ResponseEntity<String> responseEntity = null;

        if (StringUtils.isEmpty(dmeToken)) {
            //如果token为空，就自动登录，获取token
            LOG.info("token为空，自动登录，获取token");
            iniLogin();
        }

        RestUtils restUtils = new RestUtils();
        RestTemplate restTemplate = restUtils.getRestTemplate();

        HttpHeaders headers = getHeaders();

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        if (url.indexOf(DmeConstants.HTTP) < 0) {
            url = dmeHostUrl + url;
        }
        try{
            responseEntity = restTemplate.exchange(url, method, entity, String.class, jsonBody);
        }catch (HttpClientErrorException e){
            LOG.error("HttpClientErrorException:"+e.toString());
            responseEntity = new ResponseEntity<String>(e.getStatusCode());

        }
        LOG.info(url + "==responseEntity==" + (responseEntity == null ? "null" : responseEntity.getStatusCodeValue()));
        if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_401 ||
                responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_403) {
            //如果token失效，重新登录
            dmeToken = null;
            LOG.info("token失效，重新登录，获取token");
            iniLogin();
            //得到新token后，重新执行上次任务
            LOG.info("得到新token后，重新执行上次任务，dmeToken==" + dmeToken);
            headers = getHeaders();
            entity = new HttpEntity<>(null, headers);
            responseEntity = restTemplate.exchange(url, method, entity, String.class, jsonBody);
        }
        return responseEntity;
    }

    private synchronized ResponseEntity login(Map<String, Object> params) throws Exception {
        ResponseEntity responseEntity = null;
        dmeToken = null;
        if (params != null && params.get(DmeConstants.HOSTIP) != null) {
            RestUtils restUtils = new RestUtils();
            RestTemplate restTemplate = restUtils.getRestTemplate();

            HttpHeaders headers = getHeaders();

            Map<String, Object> requestbody = new HashMap<>(16);
            requestbody.put("grantType", "password");
            requestbody.put("userName", params.get("userName"));
            requestbody.put("value", params.get("password"));
            LOG.info("requestbody==" + gson.toJson(requestbody));

            String hostUrl = "https://" + params.get("hostIp") + ":" + params.get("hostPort");
            LOG.info("hostUrl==" + hostUrl);

            HttpEntity<String> entity = new HttpEntity<>(gson.toJson(requestbody), headers);
            responseEntity = restTemplate.exchange(hostUrl + LOGIN_DME_URL
                    , HttpMethod.PUT, entity, String.class);

            LOG.info("responseEntity==" + responseEntity);
            if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                LOG.info("jsonObject==" + jsonObject);
                if (jsonObject != null && jsonObject.get(DmeConstants.ACCESSSESSION) != null) {
                    dmeToken = jsonObject.get("accessSession").getAsString();
                    LOG.info("dmeToken===" + dmeToken);
                    dmeHostUrl = hostUrl;
                    dmeHostIp = params.get("hostIp").toString();
                    dmeHostPort = Integer.parseInt(params.get("hostPort").toString());
                    LOG.info("dmeHostUrl==" + dmeHostUrl + "==dmeHostIp==" + dmeHostIp + "==dmeHostPort==" + dmeHostPort);
                }
            }
        }

        return responseEntity;
    }

    private HttpHeaders getHeaders() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (!StringUtils.isEmpty(dmeToken)) {
            headers.set("X-Auth-Token", dmeToken);
        }
        LOG.info("headers==" + gson.toJson(headers));
        return headers;
    }

    private synchronized void iniLogin() throws Exception {
        if (StringUtils.isEmpty(dmeToken)) {
            //查询数据库
            DmeInfo dmeInfo = dmeInfoDao.getDmeInfo();
            LOG.info("dmeinfo==" + gson.toJson(dmeInfo));
            if (dmeInfo != null && dmeInfo.getHostIp() != null) {
                Map<String, Object> params = new HashMap<>(16);
                params.put("hostIp", dmeInfo.getHostIp());
                params.put("hostPort", dmeInfo.getHostPort());
                params.put("userName", dmeInfo.getUserName());
                params.put("password", dmeInfo.getPassword());
                LOG.info("params==" + gson.toJson(params));
                //登录
                login(params);
            } else {
                throw new Exception("目前没有DME接入信息");
            }
        }
    }

    @Override
    public List<Map<String, Object>> getWorkLoads(String storageId) throws Exception {
        List<Map<String, Object>> relists = null;
        try {
            if (!StringUtils.isEmpty(storageId)) {
                String workloadsUrl = GET_WORKLOADS_URL.replace("{storage_id}", storageId);
                LOG.info("workloadsUrl===" + workloadsUrl);
                try {
                    ResponseEntity responseEntity = access(workloadsUrl, HttpMethod.GET, null);
                    LOG.info("getWorkLoads responseEntity==" + responseEntity.toString());
                    if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                        JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                        if (jsonObject != null && jsonObject.get(DmeConstants.DATAS) != null) {
                            JsonArray jsonArray = jsonObject.getAsJsonArray("datas");
                            if (jsonArray != null && jsonArray.size() > 0) {
                                relists = new ArrayList<>();
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JsonObject vjson = jsonArray.get(i).getAsJsonObject();
                                    if (vjson != null) {
                                        Map<String, Object> map = new HashMap<>(16);
                                        map.put("block_size", ToolUtils.jsonToStr(vjson.get("block_size")));
                                        map.put("create_type", ToolUtils.jsonToStr(vjson.get("create_type")));
                                        map.put("enable_compress", ToolUtils.jsonToBoo(vjson.get("enable_compress")));
                                        map.put("enable_dedup", ToolUtils.jsonToBoo(vjson.get("enable_dedup")));
                                        map.put("id", ToolUtils.jsonToStr(vjson.get("id")));
                                        map.put("name", ToolUtils.jsonToStr(vjson.get("name")));
                                        map.put("type", ToolUtils.jsonToStr(vjson.get("type")));

                                        relists.add(map);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    LOG.error("DME link error url:" + workloadsUrl + ",error:" + e.toString());
                }
            }
        } catch (Exception e) {
            LOG.error("get WorkLoads error:", e);
            throw e;
        }
        LOG.info("getWorkLoads relists===" + (relists == null ? "null" : (relists.size() + "==" + gson.toJson(relists))));
        return relists;
    }

    @Override
    public List<Map<String, Object>> getDmeHosts(String hostIp) throws Exception {
        List<Map<String, Object>> relists = null;
        String getHostsUrl = GET_DME_HOSTS_URL;
        try {
            Map<String, Object> requestbody = new HashMap<>(16);
            if (!StringUtils.isEmpty(hostIp)) {
                requestbody.put("ip", hostIp);
            }
            LOG.info("requestbody==" + gson.toJson(requestbody));
            LOG.info("getDmeHosts_url===" + getHostsUrl);
            ResponseEntity responseEntity = access(getHostsUrl, HttpMethod.POST, (requestbody == null ? null : gson.toJson(requestbody)));
            LOG.info("getDmeHosts responseEntity==" + responseEntity.toString());
            if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                if (jsonObject != null && jsonObject.get(DmeConstants.HOSTS) != null) {
                    JsonArray jsonArray = jsonObject.getAsJsonArray("hosts");
                    if (jsonArray != null && jsonArray.size() > 0) {
                        relists = new ArrayList<>();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject vjson = jsonArray.get(i).getAsJsonObject();
                            if (vjson != null) {
                                Map<String, Object> map = new HashMap<>(16);
                                map.put("id", ToolUtils.jsonToStr(vjson.get("id")));
                                map.put("project_id", ToolUtils.jsonToStr(vjson.get("project_id")));
                                map.put("name", ToolUtils.jsonToStr(vjson.get("name")));
                                map.put("ip", ToolUtils.jsonToStr(vjson.get("ip")));
                                map.put("display_status", ToolUtils.jsonToStr(vjson.get("display_status")));
                                map.put("managed_status", ToolUtils.jsonToStr(vjson.get("managed_status")));
                                map.put("os_status", ToolUtils.jsonToStr(vjson.get("os_status")));
                                map.put("overall_status", ToolUtils.jsonToStr(vjson.get("overall_status")));
                                map.put("os_type", ToolUtils.jsonToStr(vjson.get("os_type")));
                                map.put("initiator_count", ToolUtils.jsonToInt(vjson.get("initiator_count"), null));
                                map.put("access_mode", ToolUtils.jsonToStr(vjson.get("access_mode")));
                                JsonArray hostgroups = vjson.getAsJsonArray("hostGroups");
                                if (hostgroups != null && hostgroups.size() > 0) {
                                    List<Map<String, Object>> hglists = new ArrayList<>();
                                    for (int y = 0; y < hostgroups.size(); y++) {
                                        Map<String, Object> hgmap = new HashMap<>(16);
                                        hgmap.put("id", ToolUtils.jsonToStr(vjson.get("id")));
                                        hgmap.put("name", ToolUtils.jsonToStr(vjson.get("name")));
                                        hglists.add(hgmap);
                                    }
                                    map.put("hostGroups", hglists);
                                }
                                relists.add(map);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("DME link error url:" + getHostsUrl + ",error:" + e.toString());
            throw e;
        }
        LOG.info("getDmeHosts relists===" + (relists == null ? "null" : (relists.size() + "==" + gson.toJson(relists))));
        return relists;
    }

    @Override
    public List<Map<String, Object>> getDmeHostInitiators(String hostId) throws Exception{
        List<Map<String, Object>> relists = null;
        String getHostsInitiatorUrl = GET_DME_HOSTS_INITIATORS_URL;
        getHostsInitiatorUrl = getHostsInitiatorUrl.replace("{host_id}", hostId);
        try {
            LOG.info("getHostsInitiatorUrl===" + getHostsInitiatorUrl);
            ResponseEntity responseEntity = access(getHostsInitiatorUrl, HttpMethod.GET, null);
            LOG.info("getDmeHostInitiators responseEntity==" + responseEntity.toString());
            if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                if (jsonObject != null && jsonObject.get(DmeConstants.INITIATORS) != null) {
                    JsonArray jsonArray = jsonObject.getAsJsonArray("initiators");
                    if (jsonArray != null && jsonArray.size() > 0) {
                        relists = new ArrayList<>();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject vjson = jsonArray.get(i).getAsJsonObject();
                            if (vjson != null) {
                                Map<String, Object> map = new HashMap<>(16);
                                map.put("id", ToolUtils.jsonToStr(vjson.get("id")));
                                map.put("port_name", ToolUtils.jsonToStr(vjson.get("port_name")));
                                map.put("status", ToolUtils.jsonToStr(vjson.get("status")));
                                map.put("protocol", ToolUtils.jsonToStr(vjson.get("protocol")));

                                relists.add(map);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("DME link error url:" + getHostsInitiatorUrl + ",error:" + e.toString());
            throw e;
        }
        LOG.info("getHostsInitiator relists===" + (relists == null ? "null" : (relists.size() + "==" + gson.toJson(relists))));
        return relists;
    }

    @Override
    public List<Map<String, Object>> getDmeHostGroups(String hostGroupName) throws Exception {
        List<Map<String, Object>> relists = null;
        String getHostGroupsUrl = GET_DME_HOSTGROUPS_URL;
        try {
            Map<String, Object> requestbody = null;
            if (!StringUtils.isEmpty(hostGroupName)) {
                requestbody = new HashMap<>(16);
                requestbody.put("name", hostGroupName);
                LOG.info("requestbody==" + gson.toJson(requestbody));
            }
            LOG.info("gethostgroups_url===" + getHostGroupsUrl);
            ResponseEntity responseEntity = access(getHostGroupsUrl, HttpMethod.POST, (requestbody == null ? null : gson.toJson(requestbody)));
            LOG.info("getDmeHostgroups responseEntity==" + responseEntity.toString());
            if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                if (jsonObject != null && jsonObject.get(DmeConstants.HOSTGROUPS) != null) {
                    JsonArray jsonArray = jsonObject.getAsJsonArray("hostgroups");
                    if (jsonArray != null && jsonArray.size() > 0) {
                        relists = new ArrayList<>();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject vjson = jsonArray.get(i).getAsJsonObject();
                            if (vjson != null) {
                                Map<String, Object> map = new HashMap<>(16);
                                map.put("id", ToolUtils.jsonToStr(vjson.get("id")));
                                map.put("name", ToolUtils.jsonToStr(vjson.get("name")));
                                map.put("host_count", ToolUtils.jsonToInt(vjson.get("ip"), 0));
                                map.put("source_type", ToolUtils.jsonToStr(vjson.get("source_type")));
                                map.put("managed_status", ToolUtils.jsonToStr(vjson.get("managed_status")));
                                map.put("project_id", ToolUtils.jsonToStr(vjson.get("project_id")));

                                relists.add(map);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("DME link error url:" + getHostGroupsUrl + ",error:" + e.toString());
            throw e;
        }
        LOG.info("getDmeHostgroups relists===" + (relists == null ? "null" : (relists.size() + "==" + gson.toJson(relists))));
        return relists;
    }

    @Override
    public Map<String, Object> createHost(Map<String, Object> params) throws Exception {
        Map<String, Object> hostmap = null;
        String createHostUrl = CREATE_DME_HOST_URL;
        try {
            Map<String, Object> requestbody = null;
            if (params != null && params.get(DmeConstants.HOST) != null) {
                //得到主机的hba信息
                Map<String,Object> hbamap = vcsdkUtils.getHbaByHostObjectId(ToolUtils.getStr(params.get("hostId")));

                requestbody = new HashMap<>(16);
                requestbody.put("access_mode", "NONE");
                requestbody.put("type", "VMWAREESX");
                requestbody.put("ip", params.get("host"));
                requestbody.put("host_name", params.get("host"));
                List<Map<String,Object>> initiators = new ArrayList<>();
                Map<String,Object> initiator = new HashMap<>(16);
                initiator.put("protocol",ToolUtils.getStr(hbamap.get("type")));
                initiator.put("port_name",ToolUtils.getStr(hbamap.get("name")));
                initiators.add(initiator);
                requestbody.put("initiator",initiators);
                LOG.info("requestbody==" + gson.toJson(requestbody));

                LOG.info("createHost_url===" + createHostUrl);
                ResponseEntity responseEntity = access(createHostUrl, HttpMethod.POST, gson.toJson(requestbody));
                LOG.info("getDmeHostgroups responseEntity==" + responseEntity.toString());
                if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                    JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                    if (jsonObject != null && jsonObject.get(DmeConstants.ID) != null) {
                        hostmap = new HashMap<>(16);
                        hostmap.put("id", ToolUtils.jsonToStr(jsonObject.get("id")));
                        hostmap.put("ip", ToolUtils.jsonToStr(jsonObject.get("ip")));
                        hostmap.put("access_mode", ToolUtils.jsonToStr(jsonObject.get("access_mode")));
                        hostmap.put("type", ToolUtils.jsonToStr(jsonObject.get("type")));
                        hostmap.put("port", ToolUtils.jsonToInt(jsonObject.get("port"), 0));
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("DME link error url:" + createHostUrl + ",error:" + e.toString());
            throw e;
        }
        LOG.info("createHost hostmap===" + (hostmap == null ? "null" : (hostmap.size() + "==" + gson.toJson(hostmap))));
        return hostmap;
    }

    @Override
    public Map<String, Object> createHostGroup(Map<String, Object> params) throws Exception {
        Map<String, Object> hostgroupmap = null;
        String createHostGroupUrl = CREATE_DME_HOSTGROUP_URL;
        try {
            Map<String, Object> requestbody = null;
            if (params != null && params.get(DmeConstants.CLUSTER) != null && params.get(DmeConstants.HOSTIDS) != null) {
                //判断该集群下有多少主机，如果主机在DME不存在就需要创建
                requestbody = new HashMap<>(16);
                requestbody.put("name", params.get("cluster").toString());
                requestbody.put("host_ids", (List<String>) params.get("hostids"));
                LOG.info("requestbody==" + gson.toJson(requestbody));

                LOG.info("createHostGroup_url===" + createHostGroupUrl);
                ResponseEntity responseEntity = access(createHostGroupUrl, HttpMethod.POST, gson.toJson(requestbody));
                LOG.info("getDmeHostgroups responseEntity==" + responseEntity.toString());
                if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                    JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                    if (jsonObject != null && jsonObject.get(DmeConstants.ID) != null) {
                        hostgroupmap = new HashMap<>(16);
                        hostgroupmap.put("id", ToolUtils.jsonToStr(jsonObject.get("id")));
                        hostgroupmap.put("name", ToolUtils.jsonToStr(jsonObject.get("name")));
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("DME link error url:" + createHostGroupUrl + ",error:" + e.toString());
            throw e;
        }
        LOG.info("createHostGroup hostmap===" + (hostgroupmap == null ? "null" : (hostgroupmap.size() + "==" + gson.toJson(hostgroupmap))));
        return hostgroupmap;
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

    @Override
    public Map<String, Object> getDmeHost(String hostId) throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        String getHostUrl = GET_DME_HOST_URL;
        getHostUrl = getHostUrl.replace("{host_id}", hostId);
        try {
            LOG.info("getDmeHost_url===" + getHostUrl);
            ResponseEntity responseEntity = access(getHostUrl, HttpMethod.GET, null);
            LOG.info("getDmeHost responseEntity==" + responseEntity.toString());
            if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                JsonObject vjson = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                if (vjson != null) {
                    map.put("id", ToolUtils.jsonToStr(vjson.get("id")));
                    map.put("name", ToolUtils.jsonToStr(vjson.get("name")));
                    map.put("ip", ToolUtils.jsonToStr(vjson.get("ip")));
                    map.put("display_status", ToolUtils.jsonToStr(vjson.get("display_status")));
                    map.put("managed_status", ToolUtils.jsonToStr(vjson.get("managed_status")));
                    map.put("os_status", ToolUtils.jsonToStr(vjson.get("os_status")));
                    map.put("overall_status", ToolUtils.jsonToStr(vjson.get("overall_status")));
                    map.put("os_type", ToolUtils.jsonToStr(vjson.get("os_type")));
                    map.put("initiator_count", ToolUtils.jsonToInt(vjson.get("initiator_count"), null));
                    map.put("access_mode", ToolUtils.jsonToStr(vjson.get("access_mode")));
                    JsonArray hostgroups = vjson.getAsJsonArray("hostGroups");
                    /*if (hostgroups != null && hostgroups.size() > 0) {
                        List<Map<String, Object>> hglists = new ArrayList<>();
                        for (int y = 0; y < hostgroups.size(); y++) {
                            Map<String, Object> hgmap = new HashMap<>(16);
                            hgmap.put("id", ToolUtils.jsonToStr(vjson.get("id")));
                            hgmap.put("name", ToolUtils.jsonToStr(vjson.get("name")));
                            hglists.add(hgmap);
                        }
                        map.put("hostGroups", hglists);
                    }*/
                }
            }
        } catch (Exception e) {
            LOG.error("DME link error url:" + getHostUrl + ",error:" + e.toString());
            throw e;
        }
        LOG.info("getDmeHost relists===" + (gson.toJson(map)));
        return map;
    }

    @Override
    public void scanDatastore(String storageType) throws Exception{
        if(!StringUtils.isEmpty(storageType)){
            if(storageType.equals(ToolUtils.STORE_TYPE_VMFS)){
                LOG.info("scan VMFS Datastore start");
                vmfsAccessService.scanVmfs();
                LOG.info("scan VMFS Datastore end");
            }else if(storageType.equals(ToolUtils.STORE_TYPE_NFS)){
                LOG.info("scan NFS Datastore start");
                dmeNfsAccessService.scanNfs();
                LOG.info("scan NFS Datastore end");
            }else if(storageType.equals(ToolUtils.STORE_TYPE_ALL)){
                //扫描vmfs
                try {
                    LOG.info("scan VMFS Datastore start");
                    vmfsAccessService.scanVmfs();
                    LOG.info("scan VMFS Datastore end");
                }catch (Exception e){
                    e.printStackTrace();
                }
                //扫描nfs
                try {
                    LOG.info("scan NFS Datastore start");
                    dmeNfsAccessService.scanNfs();
                    LOG.info("scan NFS Datastore end");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void configureTaskTime(Integer taskId,String taskCron) throws Exception{
        try {
            if(!StringUtils.isEmpty(taskId) && !StringUtils.isEmpty(taskCron)) {
                int re = scheduleDao.updateTaskTime(taskId,taskCron);
                if(re>0){
                    scheduleSetting.refreshTasks(taskId,taskCron);
                }
            }else{
                throw new Exception("configure Task Time error:taskId or taskCorn is null");
            }
        } catch (Exception e) {
            LOG.error("configure Task Time error:" + e.toString());
            throw e;
        }
    }

    @Override
    public Map<String, Object> getDmeHostGroup(String hostGroupId) throws Exception{
        Map<String, Object> map = new HashMap<>(16);
        String getHostGroupUrl = GET_DME_HOSTGROUP_URL;
        getHostGroupUrl = getHostGroupUrl.replace("{hostgroup_id}", hostGroupId);
        try {
            LOG.info("getDmeHostGroup_url===" + getHostGroupUrl);
            ResponseEntity responseEntity = access(getHostGroupUrl, HttpMethod.GET, null);
            LOG.info("getDmeHostGroup responseEntity==" + responseEntity.toString());
            if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                JsonObject vjson = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                if (vjson != null) {
                    map.put("id", ToolUtils.jsonToStr(vjson.get("id")));
                    map.put("name", ToolUtils.jsonToStr(vjson.get("name")));
                    map.put("host_count", ToolUtils.jsonToStr(vjson.get("host_count")));
                    map.put("project_id", ToolUtils.jsonToStr(vjson.get("project_id")));
                }
            }
        } catch (Exception e) {
            LOG.error("DME link error url:" + getHostGroupUrl + ",error:" + e.getMessage());
            throw e;
        }
        LOG.info("getDmeHostGroup relists===" + (gson.toJson(map)));
        return map;
    }

    @Override
    public List<Map<String,Object>> getDmeHostInHostGroup(String hostGroupId) throws Exception {
        List<Map<String,Object>> list = null;
        String getHostInHostGroupUrl = GET_DME_HOSTS_IN_HOSTGROUP_URL;
        getHostInHostGroupUrl = getHostInHostGroupUrl.replace("{hostgroup_id}", hostGroupId);
        try {
            Map<String, Object> requestbody = new HashMap<>(16);
            LOG.info("getHostInHostGroup_Url===" + getHostInHostGroupUrl);
            ResponseEntity responseEntity = access(getHostInHostGroupUrl, HttpMethod.POST, gson.toJson(requestbody));
            LOG.info("getDmeHostGroup responseEntity==" + responseEntity.toString());
            if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                list = new ArrayList<>();
                JsonObject datajson = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                if (datajson != null && datajson.get(DmeConstants.HOSTS)!=null) {
                    JsonArray hostsja = datajson.getAsJsonArray("hosts");
                    if(hostsja!=null && hostsja.size()>0) {
                        for(int i=0;i<hostsja.size();i++) {
                            JsonObject hostjs = hostsja.get(i).getAsJsonObject();
                            Map<String,Object> hostmap = new HashMap<>(16);
                            hostmap.put("id", ToolUtils.jsonToStr(hostjs.get("id")));
                            hostmap.put("name", ToolUtils.jsonToStr(hostjs.get("name")));
                            hostmap.put("host_count", ToolUtils.jsonToStr(hostjs.get("ip")));
                            list.add(hostmap);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("DME link error url:" + getHostInHostGroupUrl + ",error:" + e.getMessage());
            throw e;
        }
        LOG.info("getDmeHostInHostGroup relists===" + (gson.toJson(list)));
        return list;
    }

    @Override
    public void deleteVolumes(List<String> ids) throws Exception {
        String url = DmeConstants.DME_VOLUME_DELETE_URL;
        JsonObject body = new JsonObject();
        JsonArray array = new JsonParser().parse(gson.toJson(ids)).getAsJsonArray();
        body.add("volume_ids", array);
        ResponseEntity<String> responseEntity = access(url, HttpMethod.POST, body.toString());
        if(responseEntity.getStatusCodeValue()/100 != 2){
            throw new Exception(responseEntity.getBody());
        }
    }

    @Override
    public void unMapHost(String host_id, List<String> ids) throws Exception {
        String url = DmeConstants.DME_HOST_UNMAPPING_URL;
        JsonObject body = new JsonObject();
        JsonArray array = new JsonParser().parse(gson.toJson(ids)).getAsJsonArray();
        body.add("volume_ids", array);
        body.addProperty("host_id", host_id);
        ResponseEntity<String> responseEntity = access(url, HttpMethod.POST, body.toString());
        if(responseEntity.getStatusCodeValue()/100 != 2){
            throw new Exception(responseEntity.getBody());
        }
    }
}
