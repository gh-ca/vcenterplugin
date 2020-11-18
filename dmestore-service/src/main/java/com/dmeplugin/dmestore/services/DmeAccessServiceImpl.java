package com.dmeplugin.dmestore.services;


import com.dmeplugin.dmestore.dao.DmeInfoDao;
import com.dmeplugin.dmestore.dao.ScheduleDao;
import com.dmeplugin.dmestore.entity.DmeInfo;
import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.task.ScheduleSetting;
import com.dmeplugin.dmestore.utils.RestUtils;
import com.dmeplugin.dmestore.utils.ToolUtils;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

    private TaskService taskService;

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
    public void accessDme(Map<String, Object> params) throws DMEException {
        try {
            if (params != null) {
                //判断与服务器的连接
                ResponseEntity responseEntity = login(params);
                if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                    //连接成功后，数据入库
                    try {
                        DmeInfo dmeInfo = new Gson().fromJson(params.toString(), DmeInfo.class);
                        int re = dmeInfoDao.addDmeInfo(dmeInfo);
                        LOG.info("accessDme re==" + re);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        throw new DMEException("503","连接信息保存失败:" + ex.getMessage());
                    }
                } else {
                    throw new DMEException("503","连接信息保存失败:" + responseEntity.toString());
                }
            }
        } catch (Exception e) {
            throw new DMEException("503","连接信息保存失败:" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> refreshDme() throws DMEException {
        try {
            //判断与服务器的连接
            ResponseEntity responseEntity = access(REFRES_STATE_URL, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() != RestUtils.RES_STATE_I_200) {
                throw new DMEException("503","更新连接状态失败:" + responseEntity.toString());
            }

        } catch (Exception e) {
            throw new DMEException("503", "更新连接状态失败:" + e.toString());
        }

        Map<String, Object> params = new HashMap<>(16);
        params.put("hostIp", dmeHostIp);
        params.put("hostPort", dmeHostPort);

        return params;
    }

    @Override
    public ResponseEntity<String> access(String url, HttpMethod method, String requestBody) throws DMEException {
        ResponseEntity<String> responseEntity;

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
            responseEntity = new ResponseEntity(e.getStatusCode());

        }
        LOG.info(url + "==responseEntity==" + (responseEntity == null ? "null" : responseEntity.getStatusCodeValue()));
        if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_401 ||
                responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_403) {
            //如果token失效，重新登录
            dmeToken = null;
            LOG.info("token失效，重新登录，获取token");
            iniLogin();
            //得到新token后，重新执行上次任务
            LOG.info("得到新token成功，重新执行上次任务！url={}", url);
            headers = getHeaders();
            entity = new HttpEntity<>(requestBody, headers);
            responseEntity = restTemplate.exchange(url, method, entity, String.class);
        }

        if(responseEntity.getStatusCodeValue()/100 != 2){
            LOG.info("{} {}==执行失败，response={}, requestBody={}", url, method.name(), gson.toJson(responseEntity), gson.toJson(requestBody));
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> accessByJson(String url, HttpMethod method, String jsonBody) throws DMEException {
        ResponseEntity<String> responseEntity;

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
        LOG.info(url + "==accessByJson responseEntity==" + (responseEntity == null ? "null" : responseEntity.getStatusCodeValue()));
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

        if(responseEntity.getStatusCodeValue()/100 != 2){
            LOG.info("{} {}==执行失败，response={}, jsonBody={}", url, method.name(), gson.toJson(responseEntity), jsonBody);
        }
        return responseEntity;
    }

    private synchronized ResponseEntity login(Map<String, Object> params) throws DMEException {
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

            String hostUrl = "https://" + params.get("hostIp") + ":" + params.get("hostPort");

            HttpEntity<String> entity = new HttpEntity<>(gson.toJson(requestbody), headers);
            String s = gson.toJson(entity);
            responseEntity = restTemplate.exchange(hostUrl + LOGIN_DME_URL
                    , HttpMethod.PUT, entity, String.class);

            if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                if (jsonObject != null && jsonObject.get(DmeConstants.ACCESSSESSION) != null) {
                    dmeToken = jsonObject.get("accessSession").getAsString();
                    dmeHostUrl = hostUrl;
                    dmeHostIp = params.get("hostIp").toString();
                    dmeHostPort = Integer.parseInt(params.get("hostPort").toString());
                }
            }else {
                LOG.info("hostUrl:{},userName={},password={}鉴权失败！", hostUrl, params.get("userName"), params.get("password"));
            }
        }

        return responseEntity;
    }

    private HttpHeaders getHeaders()   {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (!StringUtils.isEmpty(dmeToken)) {
            headers.set("X-Auth-Token", dmeToken);
        }
        return headers;
    }

    private synchronized void iniLogin() throws DMEException {
        if (StringUtils.isEmpty(dmeToken)) {
            //查询数据库
            DmeInfo dmeInfo = dmeInfoDao.getDmeInfo();
            if (dmeInfo != null && dmeInfo.getHostIp() != null) {
                Map<String, Object> params = new HashMap<>(16);
                params.put("hostIp", dmeInfo.getHostIp());
                params.put("hostPort", dmeInfo.getHostPort());
                params.put("userName", dmeInfo.getUserName());
                params.put("password", dmeInfo.getPassword());
                //登录
                login(params);
            } else {
                throw new DMEException("目前没有DME接入信息");
            }
        }
    }

    @Override
    public List<Map<String, Object>> getWorkLoads(String storageId) throws DMEException {
        List<Map<String, Object>> relists = null;
        try {
            if (!StringUtils.isEmpty(storageId)) {
                String workloadsUrl = GET_WORKLOADS_URL.replace("{storage_id}", storageId);
                try {
                    ResponseEntity responseEntity = access(workloadsUrl, HttpMethod.GET, null);
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
                    throw new DMEException("503","DME link error url:" + workloadsUrl + ",error:" + e.toString());
                }
            }
        } catch (Exception e) {
            LOG.error("get WorkLoads error:", e);
            throw new DMEException("503","get WorkLoads error:"+e.getMessage());
        }
        LOG.info("getWorkLoads relists===" + (relists == null ? "null" : relists.size()));
        return relists;
    }

    @Override
    public List<Map<String, Object>> getDmeHosts(String hostIp) throws DMEException {
        List<Map<String, Object>> relists = null;
        String getHostsUrl = GET_DME_HOSTS_URL;
        try {
            Map<String, Object> requestbody = new HashMap<>(16);
            if (!StringUtils.isEmpty(hostIp)) {
                requestbody.put("ip", hostIp);
            }
            ResponseEntity responseEntity = access(getHostsUrl, HttpMethod.POST, (requestbody == null ? null : gson.toJson(requestbody)));
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
            throw new DMEException(e.getMessage());
        }
        LOG.info("getDmeHosts relists===" + (relists == null ? "null" : (relists.size())));
        return relists;
    }

    @Override
    public List<Map<String, Object>> getDmeHostInitiators(String hostId) throws DMEException {
        List<Map<String, Object>> relists = null;
        String getHostsInitiatorUrl = GET_DME_HOSTS_INITIATORS_URL;
        getHostsInitiatorUrl = getHostsInitiatorUrl.replace("{host_id}", hostId);
        try {
            ResponseEntity responseEntity = access(getHostsInitiatorUrl, HttpMethod.GET, null);
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
            throw new DMEException(e.getMessage());
        }
        LOG.info("getHostsInitiator relists===" + (relists == null ? "null" : (relists.size())));
        return relists;
    }

    @Override
    public List<Map<String, Object>> getDmeHostGroups(String hostGroupName) throws DMEException {
        List<Map<String, Object>> relists = null;
        String getHostGroupsUrl = GET_DME_HOSTGROUPS_URL;
        try {
            Map<String, Object> requestbody = null;
            if (!StringUtils.isEmpty(hostGroupName)) {
                requestbody = new HashMap<>(16);
                requestbody.put("name", hostGroupName);
            }
            ResponseEntity responseEntity = access(getHostGroupsUrl, HttpMethod.POST, (requestbody == null ? null : gson.toJson(requestbody)));
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
            throw new DMEException(e.getMessage());
        }
        LOG.info("getDmeHostgroups relists===" + (relists == null ? "null" : relists.size()));
        return relists;
    }

    @Override
    public Map<String, Object> createHost(Map<String, Object> params) throws DMEException {
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
                ResponseEntity responseEntity = access(createHostUrl, HttpMethod.POST, gson.toJson(requestbody));
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
            throw new DMEException(e.getMessage());
        }
        LOG.info("createHost hostmap===" + (hostmap == null ? "null" : hostmap.size()));
        return hostmap;
    }

    @Override
    public Map<String, Object> createHostGroup(Map<String, Object> params) throws DMEException {
        Map<String, Object> hostgroupmap = null;
        String createHostGroupUrl = CREATE_DME_HOSTGROUP_URL;
        try {
            Map<String, Object> requestbody;
            if (params != null && params.get(DmeConstants.CLUSTER) != null && params.get(DmeConstants.HOSTIDS) != null) {
                //判断该集群下有多少主机，如果主机在DME不存在就需要创建
                requestbody = new HashMap<>(16);
                requestbody.put("name", params.get("cluster").toString());
                requestbody.put("host_ids", params.get("hostids"));

                ResponseEntity responseEntity = access(createHostGroupUrl, HttpMethod.POST, gson.toJson(requestbody));
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
            throw new DMEException(e.getMessage());
        }
        LOG.info("createHostGroup hostmap===" + (hostgroupmap == null ? "null" : (hostgroupmap.size())));
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

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public Map<String, Object> getDmeHost(String hostId) throws DMEException {
        Map<String, Object> map = new HashMap<>(16);
        String getHostUrl = GET_DME_HOST_URL;
        getHostUrl = getHostUrl.replace("{host_id}", hostId);
        try {
            ResponseEntity responseEntity = access(getHostUrl, HttpMethod.GET, null);
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
                }
            }
        } catch (Exception e) {
            LOG.error("DME link error url:" + getHostUrl + ",error:" + e.toString());
            throw new DMEException(e.getMessage());
        }
        LOG.info("getDmeHost relists===size={}", map.size());
        return map;
    }

    @Override
    public void scanDatastore(String storageType) throws DMEException {
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
    public void configureTaskTime(Integer taskId,String taskCron) throws DMEException {
        try {
            if(!StringUtils.isEmpty(taskId) && !StringUtils.isEmpty(taskCron)) {
                int re = scheduleDao.updateTaskTime(taskId,taskCron);
                if(re>0){
                    scheduleSetting.refreshTasks(taskId,taskCron);
                }
            }else{
                throw new DMEException("configure Task Time error:taskId or taskCorn is null");
            }
        } catch (Exception e) {
            LOG.error("configure Task Time error:" + e.toString());
            throw new DMEException(e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getDmeHostGroup(String hostGroupId) throws DMEException {
        Map<String, Object> map = new HashMap<>(16);
        String getHostGroupUrl = GET_DME_HOSTGROUP_URL;
        getHostGroupUrl = getHostGroupUrl.replace("{hostgroup_id}", hostGroupId);
        try {
            ResponseEntity responseEntity = access(getHostGroupUrl, HttpMethod.GET, null);
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
            throw new DMEException(e.getMessage());
        }
        return map;
    }

    @Override
    public List<Map<String,Object>> getDmeHostInHostGroup(String hostGroupId) throws DMEException {
        List<Map<String,Object>> list = null;
        String getHostInHostGroupUrl = GET_DME_HOSTS_IN_HOSTGROUP_URL;
        getHostInHostGroupUrl = getHostInHostGroupUrl.replace("{hostgroup_id}", hostGroupId);
        try {
            Map<String, Object> requestbody = new HashMap<>(16);
            ResponseEntity responseEntity = access(getHostInHostGroupUrl, HttpMethod.POST, gson.toJson(requestbody));
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
            throw new DMEException(e.getMessage());
        }
        LOG.info("getDmeHostInHostGroup relists===" + (null == list? null : list.size()));
        return list;
    }

    @Override
    public void deleteVolumes(List<String> ids) throws DMEException {
        String url = DmeConstants.DME_VOLUME_DELETE_URL;
        JsonObject body = new JsonObject();
        JsonArray array = new JsonParser().parse(gson.toJson(ids)).getAsJsonArray();
        body.add("volume_ids", array);
        ResponseEntity<String> responseEntity = access(url, HttpMethod.POST, body.toString());
        if(responseEntity.getStatusCodeValue()/DmeConstants.HTTPS_STATUS_CHECK_FLAG != DmeConstants.HTTPS_STATUS_SUCCESS_PRE){
            throw new DMEException(responseEntity.getBody());
        }

        JsonObject task = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        String taskId = task.get("task_id").getAsString();
        JsonObject taskDetail = taskService.queryTaskByIdUntilFinish(taskId);
        if (taskDetail.get(DmeConstants.TASK_DETAIL_STATUS_FILE).getAsInt() != DmeConstants.TASK_SUCCESS) {
            LOG.error("delete volumes failed!task status={}", task.get("status").getAsInt());
            throw new DMEException(task.get("detail_cn").getAsString());
        }
    }

    @Override
    public void unMapHost(String hostId, List<String> ids) throws DMEException {
        String url = DmeConstants.DME_HOST_UNMAPPING_URL;
        JsonObject body = new JsonObject();
        JsonArray array = new JsonParser().parse(gson.toJson(ids)).getAsJsonArray();
        body.add("volume_ids", array);
        body.addProperty("host_id", hostId);
        ResponseEntity<String> responseEntity = access(url, HttpMethod.POST, body.toString());
        if(responseEntity.getStatusCodeValue()/DmeConstants.HTTPS_STATUS_CHECK_FLAG != DmeConstants.HTTPS_STATUS_SUCCESS_PRE){
            throw new DMEException(responseEntity.getBody());
        }

        JsonObject task = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        String taskId = task.get("task_id").getAsString();
        JsonObject taskDetail = taskService.queryTaskByIdUntilFinish(taskId);
        if (taskDetail.get(DmeConstants.TASK_DETAIL_STATUS_FILE).getAsInt() != DmeConstants.TASK_SUCCESS) {
            LOG.error("host unmapping failed!task status={}", task.get("status").getAsInt());
            throw new DMEException(task.get("detail_cn").getAsString());
        }
    }

    @Override
    public void hostMapping(String hostId, List<String> volumeIds) throws DMEException {
        String url = DmeConstants.DME_HOST_MAPPING_URL;
        JsonObject body = new JsonObject();
        body.addProperty("host_id", hostId);
        JsonArray volumeIdArray = gson.fromJson(gson.toJson(volumeIds), JsonArray.class);
        body.add("volume_ids", volumeIdArray);
        ResponseEntity<String> responseEntity = access(url, HttpMethod.POST, body.toString());
        if (responseEntity.getStatusCodeValue() / DmeConstants.HTTPS_STATUS_CHECK_FLAG != DmeConstants.HTTPS_STATUS_SUCCESS_PRE) {
            LOG.error("host mapping failed!errorMsg:{}", responseEntity.getBody());
            throw new DMEException(responseEntity.getBody());
        }
        JsonObject task = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        String taskId = task.get("task_id").getAsString();
        JsonObject taskDetail = taskService.queryTaskByIdUntilFinish(taskId);
        if (taskDetail.get(DmeConstants.TASK_DETAIL_STATUS_FILE).getAsInt() != DmeConstants.TASK_SUCCESS) {
            LOG.error("host mapping failed!task status={}", task.get("status").getAsInt());
            throw new DMEException(task.get("detail_cn").getAsString());
        }
    }
}
