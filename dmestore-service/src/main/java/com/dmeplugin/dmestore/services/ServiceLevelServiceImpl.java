package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.entity.VCenterInfo;
import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.exception.VcenterException;
import com.dmeplugin.dmestore.model.*;
import com.dmeplugin.dmestore.utils.CipherUtils;
import com.dmeplugin.dmestore.utils.ToolUtils;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.dmeplugin.vmware.autosdk.SessionHelper;
import com.google.gson.*;
import com.vmware.cis.tagging.TagModel;
import com.vmware.pbm.PbmProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @Description: TODO
 * @ClassName: ServiceLevelServiceImpl
 * @Company: GH-CA
 * @author: liuxh
 * @create: 2020-09-02
 **/
public class ServiceLevelServiceImpl implements ServiceLevelService {
    private static final Logger log = LoggerFactory.getLogger(ServiceLevelServiceImpl.class);

    public static Map<String, Map<String, Object>> serviceLevelInstance = new HashMap<>();

    private Gson gson = new Gson();
    private DmeAccessService dmeAccessService;
    private DmeRelationInstanceService dmeRelationInstanceService;
    private DmeStorageService dmeStorageService;
    private VCenterInfoService vCenterInfoService;
    private VCSDKUtils vcsdkUtils;

    private SessionHelper sessionHelper;

    private static final String POLICY_DESC = "policy created by dme";

    private final String LIST_SERVICE_LEVEL_URL = "/rest/service-policy/v1/service-levels";
    private final String QUERY_SERVICE_LEVEL_VOLUME_URL = "/rest/blockservice/v1/volumes?service_level_id={serviceLevelId}";

    public DmeAccessService getDmeAccessService() {
        return dmeAccessService;
    }

    public void setDmeAccessService(DmeAccessService dmeAccessService) {
        this.dmeAccessService = dmeAccessService;
    }

    public DmeRelationInstanceService getDmeRelationInstanceService() {
        return dmeRelationInstanceService;
    }

    public void setDmeRelationInstanceService(DmeRelationInstanceService dmeRelationInstanceService) {
        this.dmeRelationInstanceService = dmeRelationInstanceService;
    }

    public DmeStorageService getDmeStorageService() {
        return dmeStorageService;
    }

    public void setDmeStorageService(DmeStorageService dmeStorageService) {
        this.dmeStorageService = dmeStorageService;
    }

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
    }

    public VCenterInfoService getvCenterInfoService() {
        return vCenterInfoService;
    }

    public void setvCenterInfoService(VCenterInfoService vCenterInfoService) {
        this.vCenterInfoService = vCenterInfoService;
    }

    @Override
    public List<SimpleServiceLevel> listServiceLevel(Map<String, Object> params) throws DMEException {

        ResponseEntity responseEntity;
        List<SimpleServiceLevel> slis;
        try {
            responseEntity = dmeAccessService.access(LIST_SERVICE_LEVEL_URL, HttpMethod.GET, null);
            int code = responseEntity.getStatusCodeValue();
            if (HttpStatus.OK.value() != code) {
                throw new DMEException("503","list serviceLevel response error!");
            }
            Object object = responseEntity.getBody();
            slis = convertBean(object);
        } catch (Exception e) {
            log.error("list serviceLevel error", e);
            throw new DMEException("503",e.getMessage());
        }
        return slis;
    }

    @Override
    public void updateVmwarePolicy() throws DMEException {
        try {
            sessionHelper = new SessionHelper();
            VCenterInfo vCenterInfo = vCenterInfoService.getVcenterInfo();
            if (null != vCenterInfo) {
                sessionHelper.login(vCenterInfo.getHostIp(), String.valueOf(vCenterInfo.getHostPort()), vCenterInfo.getUserName(), CipherUtils.decryptString(vCenterInfo.getPassword()));

                String categoryid = vcsdkUtils.getCategoryId(sessionHelper);
                List<TagModel> tagModels = vcsdkUtils.getAllTagsByCategoryId(categoryid, sessionHelper);
                List<PbmProfile> pbmProfiles = vcsdkUtils.getAllSelfPolicyInallcontext();
                ResponseEntity responseEntity = dmeAccessService.access(LIST_SERVICE_LEVEL_URL, HttpMethod.GET, null);
                Object object = responseEntity.getBody();
                JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("service-levels").getAsJsonArray();
                List<TagModel> alreadyhasList = new ArrayList<>();
                List<PbmProfile> alreadyhasPolicyList = new ArrayList<>();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject object1 = new JsonParser().parse(jsonElement.toString()).getAsJsonObject();
                    String name = object1.get("name").getAsString();
                    //tag是否存在判断
                    boolean alreadyhas = false;
                    for (TagModel tagModel : tagModels) {
                        if (tagModel.getName().equalsIgnoreCase(name)) {
                            alreadyhasList.add(tagModel);
                            alreadyhas = true;
                            break;
                        }
                    }

                    //虚拟机存储策略判断
                    boolean alreadyhasPolicy = false;
                    for (PbmProfile profile : pbmProfiles) {
                        if (profile.getName().equalsIgnoreCase(name)) {
                            alreadyhasPolicyList.add(profile);
                            alreadyhasPolicy = true;
                            break;
                        }
                    }
                    if (!alreadyhas) {
                        //创建tag
                        vcsdkUtils.createTag(name, sessionHelper);
                    }

                    if (!alreadyhasPolicy) {
                        //创建虚拟机存储策略
                        vcsdkUtils.createPbmProfileInAllContext(VCSDKUtils.CATEGORY_NAME, name);
                    }
                }
                tagModels.removeAll(alreadyhasList);
                pbmProfiles.removeAll(alreadyhasPolicyList);
                //删除多余的tag，虚拟机存储策略
                //先删除虚拟机存储策略
                vcsdkUtils.removePbmProfileInAllContext(pbmProfiles);
                vcsdkUtils.removeAllTags(tagModels, sessionHelper);
                log.info("后台更新服务等级策略完成");
            } else {
                throw new VcenterException("数据库中没有vcenter信息");
            }
        } catch (Exception e) {
            log.error("list serviceLevel error", e);
            throw new DMEException("503","update service level error"+e.getMessage());
        }
    }

    /**
     * convert the api responseBody to SimpleServiceLevel Bean list
     * @author wangxy
     * @date 11:21 2020/11/13
     * @param object
     * @return java.util.List<com.dmeplugin.dmestore.model.SimpleServiceLevel>
     **/
    private List<SimpleServiceLevel> convertBean(Object object) {
        List<SimpleServiceLevel> ssls = new ArrayList<>();
        JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
        JsonArray jsonArray = jsonObject.get("service-levels").getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            SimpleServiceLevel ssl = new SimpleServiceLevel();
            try {
                JsonObject object1 = new JsonParser().parse(jsonElement.toString()).getAsJsonObject();
                String id = ToolUtils.jsonToOriginalStr(object1.get("id"));
                String name = ToolUtils.jsonToOriginalStr(object1.get("name"));
                String descriptionn = ToolUtils.jsonToStr(object1.get("description"));
                String type = ToolUtils.jsonToOriginalStr(object1.get("type"));
                String protocol = ToolUtils.jsonToOriginalStr(object1.get("protocol"));
                double totalCapacity = ToolUtils.jsonToDou(object1.get("total_capacity"));
                double usedCapcity = ToolUtils.jsonToDou(object1.get("used_capacity"));
                double freeCapacity = ToolUtils.jsonToDou(object1.get("free_capacity"));
                ssl.setId(id);
                ssl.setName(name);
                ssl.setDescription(descriptionn);
                ssl.setType(type);
                ssl.setProtocol(protocol);
                ssl.setTotalCapacity(totalCapacity);
                ssl.setFreeCapacity(freeCapacity);
                ssl.setUsedCapacity(usedCapcity);
                JsonElement capObj = object1.get("capabilities");
                if (!ToolUtils.jsonIsNull(capObj)) {
                    SimpleCapabilities scb = new SimpleCapabilities();
                    JsonObject capJsonObj = capObj.getAsJsonObject();
                    String resourceType = ToolUtils.jsonToOriginalStr(capJsonObj.get("resource_type"));
                    boolean compression = ToolUtils.jsonToBoo(capJsonObj.get("compression"));
                    scb.setResourceType(resourceType);
                    scb.setCompression(compression);

                    //报文中暂未出现此属性,暂不处理
                    JsonElement smarttierObj = capJsonObj.get("smarttier");
                    if (!ToolUtils.jsonIsNull(smarttierObj)) {
                        CapabilitiesSmarttier cbs = new CapabilitiesSmarttier();
                        JsonObject smartJsonObj = smarttierObj.getAsJsonObject();
                        int policy = ToolUtils.jsonToInt(smartJsonObj.get("policy"), null);
                        boolean enabled = ToolUtils.jsonToBoo(smartJsonObj.get("enabled"));
                        cbs.setEnabled(enabled);
                        cbs.setPolicy(policy);
                        scb.setSmarttier(cbs);
                    }

                    JsonElement qosObject = capJsonObj.get("qos");
                    if (!ToolUtils.jsonIsNull(qosObject)) {
                        CapabilitiesQos cq = new CapabilitiesQos();
                        JsonObject qosJsonObj = qosObject.getAsJsonObject();
                        boolean enabled = ToolUtils.jsonToBoo(qosJsonObj.get("enabled"));
                        cq.setEnabled(enabled);
                        JsonElement qosParamObj = qosJsonObj.get("qos_param");
                        if (!ToolUtils.jsonIsNull(qosParamObj)) {
                            QosParam qp = new QosParam();
                            JsonObject qosParamJsonObj = qosParamObj.getAsJsonObject();
                            String latencyUnit = ToolUtils.jsonToStr(qosParamJsonObj.get("latencyUnit"));
                            int latnecy = ToolUtils.jsonToInt(qosParamJsonObj.get("latency"));
                            int minBandWidth = ToolUtils.jsonToInt(qosParamJsonObj.get("minBandWidth"));
                            int minIops = ToolUtils.jsonToInt(qosParamJsonObj.get("minIOPS"));
                            int maxBandWidth = ToolUtils.jsonToInt(qosParamJsonObj.get("maxBandWidth"));
                            int maxIops = ToolUtils.jsonToInt(qosParamJsonObj.get("maxIOPS"));
                            qp.setLatency(latnecy);
                            qp.setLatencyUnit(latencyUnit);
                            qp.setMinBandWidth(minBandWidth);
                            qp.setMinIOPS(minIops);
                            qp.setMaxBandWidth(maxBandWidth);
                            qp.setMaxIOPS(maxIops);
                            cq.setQosParam(qp);
                        }
                        scb.setQos(cq);
                    }
                    ssl.setCapabilities(scb);
                }
                ssls.add(ssl);
            } catch (Exception e) {
                log.warn("servicelevel convert error:", e);
            }
        }
        return ssls;
    }

    /**
     * 扫描服务等级 发现服务等级下的存储池,磁盘,(存储端口)
     * @author wangxy
     * @date 11:21 2020/11/13
     * @param serivceLevelId
     * @throws DMEException
     * @return java.util.List<com.dmeplugin.dmestore.model.StoragePool>
     **/
    @Override
    public List<StoragePool> getStoragePoolInfosByServiceLevelId(String serivceLevelId) throws DMEException {
        List<StoragePool> storagePools = new ArrayList<>();
        // servicLevelId对应的serviceLevelInstanceId
        Map<String, Map<String, Object>> serviceLevelMap = dmeRelationInstanceService.getServiceLevelInstance();
        if(null!= serviceLevelMap && serviceLevelMap.size()>0){
            try {
                String serviceLevelInstanceId = serviceLevelMap.get(serivceLevelId).get("resId").toString();
                if(!StringUtils.isEmpty(serviceLevelInstanceId)){
                    serivceLevelId = serviceLevelInstanceId;
                }
            } catch (Exception e) {
                log.warn("获取服务等级关联的存储池,查询服务等级instanceId异常,servcieLevelId:"+serivceLevelId);
            }
        }

        // 1 获取serviceLevelId下的StoragePool实例集合
        List<String> storagePoolInstanceIds = getStoragePoolIdsByServiceLevelId(serivceLevelId);

        // 2 通过storagePoolInstanceId获取storagePoolId和storageDeviceId信息
        List<StoragePool> sps = new ArrayList<>();
        if (null != storagePoolInstanceIds && storagePoolInstanceIds.size() > 0) {
            for (String instanceId : storagePoolInstanceIds) {
                Object object = dmeRelationInstanceService.queryInstanceByInstanceNameId("SYS_StoragePool", instanceId);
                StoragePool sp = convertInstanceToStoragePool(object);
                sps.add(sp);
            }
        }

        // 3 通过storageDeviceId和storagePoolId获取storagePool信息(这里先获取存储设备下的所有存储池,再通过storagePoolId过滤)
        if (sps.size() > 0) {
            Map<String, Set<String>> storageDevicePoolIds = new HashMap<>();
            for (StoragePool sp : sps) {
                String storageDeviceId = sp.getStorageDeviceId();
                String poolInstanceId = sp.getStorageInstanceId();
                if (null == storageDevicePoolIds.get(storageDeviceId)) {
                    Set<String> poolIds = new HashSet<>();
                    poolIds.add(poolInstanceId);
                    storageDevicePoolIds.put(storageDeviceId, poolIds);
                } else {
                    storageDevicePoolIds.get(storageDeviceId).add(poolInstanceId);
                }
            }
            for (Map.Entry<String, Set<String>> entry : storageDevicePoolIds.entrySet()) {
                String storageDevcieId = entry.getKey();
                Set<String> storagePoolIds = entry.getValue();
                List<StoragePool> storageDevicePools = getStoragePoolInfosByStorageIdStoragePoolIds(storageDevcieId, new ArrayList<>(storagePoolIds));
                if (null != storageDevicePoolIds && storageDevicePoolIds.size() > 0) {
                    storagePools.addAll(storageDevicePools);
                } else {
                    log.warn("存储设备:{}下的存储池集合:{}的信息为空", storageDevcieId, gson.toJson(storageDevicePools));
                }
            }
        }
        return storagePools;
    }

    @Override
    public List<Volume> getVolumeInfosByServiceLevelId(String serviceLevelId) throws DMEException {
        List<Volume> volumes = new ArrayList<>();
        String url = QUERY_SERVICE_LEVEL_VOLUME_URL.replace("{serviceLevelId}", serviceLevelId);
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        int code = responseEntity.getStatusCodeValue();
        if (code != HttpStatus.OK.value()) {
            return volumes;
        }
        Object object = responseEntity.getBody();
        if (object != null) {
            Map<String, Map<String, Object>> sysLunMap = dmeRelationInstanceService.getLunInstance();
            JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
            JsonArray array = jsonObject.get("volumes").getAsJsonArray();

            for (JsonElement je : array) {
                JsonObject element = je.getAsJsonObject();
                Volume volume = new Volume();
                volume.setId(ToolUtils.jsonToStr(element.get("id")));
                volume.setName(ToolUtils.jsonToStr(element.get("name")));
                volume.setStatus(ToolUtils.jsonToStr(element.get("status")));
                volume.setAttached(ToolUtils.jsonToBoo(element.get("attached")));
                volume.setAlloctype(ToolUtils.jsonToStr(element.get("alloctype")));
                volume.setServiceLevelName(ToolUtils.jsonToStr(element.get("service_level_name")));
                volume.setStorageId(ToolUtils.jsonToStr(element.get("storage_id")));
                volume.setPoolRawId(ToolUtils.jsonToStr(element.get("pool_raw_id")));
                volume.setCapacityUsage(ToolUtils.jsonToStr(element.get("capacity_usage")));
                volume.setProtectionStatus(ToolUtils.jsonToBoo(element.get("protectionStatus")));
                volume.setCapacity(ToolUtils.jsonToInt(element.get("capacity"), 0));

                String wwn = ToolUtils.jsonToStr(element.get("volume_wwn"));
                String instanceId = getInstancePropertyValue(sysLunMap, wwn, "wwn");
                if (!StringUtils.isEmpty(instanceId)) {
                    volume.setInstanceId(instanceId);
                }
                volumes.add(volume);
            }
        }
        return volumes;
    }

    private String getInstancePropertyValue(Map<String, Map<String, Object>> instanceMap, String id, String name) {
        String value = "";
        if (null != instanceMap) {
            Map<String, Object> instance = instanceMap.get(id);
            if (null != instance) {
                Object obj = instance.get(name);
                if (null != obj) {
                    value = obj.toString();
                }
            }
        }
        return value;
    }

    private List<StoragePool> getStoragePoolInfosByStorageIdStoragePoolIds(String storageDeviceId, List<String> storagePoolIds) throws DMEException {
        List<StoragePool> sps = new ArrayList<>();
        List<StoragePool> storagePools = dmeStorageService.getStoragePools(storageDeviceId, "all");

            for (StoragePool sp : storagePools) {
                String poolId = sp.getStorageInstanceId();
                if (storagePoolIds.contains(poolId)) {
                    sps.add(sp);
                }
            }
        return sps;
    }

    /**
     * 服务等级 发现服务等级下的存储池 serviceLevelId和sourceInstanceId一样?
     * @author wangxy
     * @date 11:19 2020/11/13
     * @param serviceLevelId
     * @throws DMEException
     * @return java.util.List<java.lang.String>
     **/
    public List<String> getStoragePoolIdsByServiceLevelId(String serviceLevelId) throws DMEException {
        String relatinName = "M_DjTierContainsStoragePool";
        return getContainIdsByRelationNameLevelId(relatinName, serviceLevelId);
    }

    /**
     * 服务等级 发现服务等级下的卷实例ID
     * @author wangxy
     * @date 11:20 2020/11/13
     * @param serviceLevelId
     * @throws DMEException
     * @return java.util.List<java.lang.String>
     **/
    public List<String> getVolumeIdsByServiceLivelId(String serviceLevelId) throws DMEException {
        String relationName = "M_DjTierContainsLun";
        return getContainIdsByRelationNameLevelId(relationName, serviceLevelId);
    }

    private List<String> getContainIdsByRelationNameLevelId(String relationName, String serviceLevelId) throws DMEException {
        Set<String> ids = new HashSet<>();
        List<RelationInstance> ris = dmeRelationInstanceService.queryRelationByRelationNameConditionSourceInstanceId(relationName, serviceLevelId);
        if (null != ris && ris.size() > 0) {
            for (RelationInstance ri : ris) {
                ids.add(ri.getTargetInstanceId());
            }
        }
        return new ArrayList<>(ids);
    }

    /**
     * 将instance转换为storagepool信息
     * @author wangxy
     * @date 11:20 2020/11/13
     * @param instanceObj
     * @return com.dmeplugin.dmestore.model.StoragePool
     **/
    private StoragePool convertInstanceToStoragePool(Object instanceObj) {
        StoragePool sp = new StoragePool();
        JsonObject jsonObject = new JsonParser().parse(instanceObj.toString()).getAsJsonObject();
        String name = ToolUtils.jsonToStr(jsonObject.get("name"));
        // runningStatus?
        String status = ToolUtils.jsonToStr(jsonObject.get("status"));
        // SYS_StorageDisk中取磁盘类型?
        String type = ToolUtils.jsonToStr(jsonObject.get("type"));
        String poolId = ToolUtils.jsonToStr(jsonObject.get("poolId"));
        String storageDeviceId = ToolUtils.jsonToStr(jsonObject.get("storageDeviceId"));
        String storagePoolInstanceId = ToolUtils.jsonToStr(jsonObject.get("id"));

        sp.setName(name);
        sp.setStoragePoolId(poolId);
        sp.setStorageInstanceId(storagePoolInstanceId);
        sp.setStorageDeviceId(storageDeviceId);

        return sp;
    }
}
