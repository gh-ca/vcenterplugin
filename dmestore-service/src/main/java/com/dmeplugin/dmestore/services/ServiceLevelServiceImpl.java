package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.RelationInstance;
import com.dmeplugin.dmestore.model.ServiceLevelInfo;
import com.dmeplugin.dmestore.model.StoragePool;
import com.dmeplugin.dmestore.model.Volume;
import com.dmeplugin.dmestore.utils.ToolUtils;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

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

    private Gson gson = new Gson();
    private DmeAccessService dmeAccessService;
    private DmeRelationInstanceService dmeRelationInstanceService;
    private DmeStorageService dmeStorageService;

    private final String LIST_SERVICE_LEVEL_URL = "https://localhost:26335/rest/service-policy/v1/service-levels";

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

    @Override
    public Map<String, Object> listServiceLevel(Map<String, Object> params) {
        Map<String, Object> remap = new HashMap<>();
        remap.put("code", 200);
        remap.put("message", "list serviceLevel success!");
        remap.put("data", params);

        ResponseEntity responseEntity;
        List<ServiceLevelInfo> slis;
        try {
            responseEntity = dmeAccessService.access(LIST_SERVICE_LEVEL_URL, HttpMethod.GET, null);
            int code = responseEntity.getStatusCodeValue();
            if (200 != code) {
                remap.put("code", 503);
                remap.put("message", "list serviceLevel response error!");
                return remap;
            }
            Object object = responseEntity.getBody();
            //JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
            //slis = convertBean(object);
            //remap.put("data", slis);
            remap.put("data", object);
        } catch (Exception e) {
            log.error("list serviceLevel error", e);
            String message = e.getMessage();
            remap.put("code", 503);
            remap.put("message", message);
            return remap;
        }
        return remap;
    }

    // convert the api responseBody to ServiceLevelInfo Bean list
    private List<ServiceLevelInfo> convertBean(Object object) {
        List<ServiceLevelInfo> slis = new ArrayList<>();

        JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
        JsonArray jsonArray = jsonObject.get("service-levels").getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            try {
                JsonObject object1 = new JsonParser().parse(jsonElement.toString()).getAsJsonObject();
                String id = object1.get("id").getAsString();
                String name = object1.get("name").getAsString();
                String type = object1.get("type").getAsString();
                String protocol = object1.get("protocol").getAsString();
                double totalCapacity = object1.get("total_capacity").getAsDouble();
                double freeCapacity = object1.get("free_capacity").getAsDouble();

                JsonObject qosParamObject = object1.get("capabilities").getAsJsonObject().get("qos").getAsJsonObject().get("qos_param").getAsJsonObject();
                int minIOPS = qosParamObject.get("minIOPS").getAsInt();
                int latency = qosParamObject.get("latency").getAsInt();
                String latencyUnit = qosParamObject.get("latencyUnit").getAsString();

                ServiceLevelInfo sli = new ServiceLevelInfo();
                sli.setId(id);
                sli.setName(name);
                sli.setType(type);
                sli.setProtocol(protocol);
                sli.setTotalCapacity(totalCapacity);
                sli.setFreeCapacity(freeCapacity);
                sli.setMinIOPS(minIOPS);
                sli.setLatency(latency);
                sli.setLatencyUnit(latencyUnit);

                slis.add(sli);
            } catch (Exception e) {
                log.warn("servicelevel convert error:", e);
            }
        }
        return slis;
    }

    //扫描服务等级 发现服务等级下的存储池,磁盘,(存储端口)
    @Override
    public List<StoragePool> getStoragePoolInfosByServiceLevelId(String serivceLevelId) throws Exception {
        List<StoragePool> storagePools = new ArrayList<>();
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
                String storageDeviceId = sp.getStorage_device_id();
                String poolId = sp.getStorage_pool_id();
                if (null == storageDevicePoolIds.get(storageDeviceId)) {
                    Set<String> poolIds = new HashSet<>();
                    poolIds.add(poolId);
                    storageDevicePoolIds.put(storageDeviceId, poolIds);
                } else {
                    storageDevicePoolIds.get(storageDeviceId).add(poolId);
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
    public List<Volume> getVolumeInfosByServiceLevelId(String serviceLevelId) throws Exception {
        List<Volume> volumeList = new ArrayList<>();
        //1 获取serviveLevel关联的volumeId
        List<String> volumeIds = getVolumeIdsByServiceLivelId(serviceLevelId);
        //2 根据volumeId查询olume信息
        if (null != volumeIds && volumeIds.size() > 0) {
            for (String volumeId : volumeIds) {
                Map<String, Object> map = dmeStorageService.getVolume(volumeId);
                if ("200".equals(map.get("code").toString())) {
                    Volume volume = (Volume) map.get("data");
                    volumeList.add(volume);
                }
            }
        }
        return volumeList;
    }

    private List<StoragePool> getStoragePoolInfosByStorageIdStoragePoolIds(String storageDeviceId, List<String> storagePoolIds) {
        List<StoragePool> sps = new ArrayList<>();
        Map<String, Object> resp = dmeStorageService.getStoragePools(storageDeviceId);
        String code = resp.get("code").toString();
        if ("200".equals(code)) {
            List<StoragePool> storagePools = (List<StoragePool>) resp.get("data");
            for (StoragePool sp : storagePools) {
                String poolId = sp.getStorage_pool_id();
                if (storagePoolIds.contains(poolId)) {
                    sps.add(sp);
                }
            }
        }
        return sps;
    }

    //服务等级 发现服务等级下的存储池 serviceLevelId和sourceInstanceId一样?
    public List<String> getStoragePoolIdsByServiceLevelId(String serviceLevelId) throws Exception {
        String relatinName = "M_DjTierContainsStoragePool";
        return getContainIdsByRelationNameLevelId(relatinName, serviceLevelId);
    }

    //服务等级 发现服务等级下的卷
    public List<String> getVolumeIdsByServiceLivelId(String serviceLevelId) throws Exception {
        String relationName = "M_DjTierContainsLun";
        return getContainIdsByRelationNameLevelId(relationName, serviceLevelId);
    }

    private List<String> getContainIdsByRelationNameLevelId(String relationName, String serviceLevelId) throws Exception {
        Set<String> ids = new HashSet<>();
        List<RelationInstance> ris = dmeRelationInstanceService.queryRelationByRelationNameConditionSourceInstanceId(relationName, serviceLevelId);
        if (null != ris && ris.size() > 0) {
            for (RelationInstance ri : ris) {
                ids.add(ri.getTargetInstanceId());
            }
        }
        return new ArrayList<>(ids);
    }

    //存储池的详情需要通过存储设备ID和存储池在设备上的ID联合来查询
    private void getStoragePoolDevcieIdRelationByRelationNameLevelId(String storageDeviceId, String storagePoolId) {

    }

    //查询服务等级性能
    public Object getStatisticByServiceLevelId(String serviceLevelId) {
        //1 获取服务等级下的磁盘Id

        //2 获取磁盘性能

        return null;
    }

    //将instance转换为storagepool信息
    private StoragePool convertInstanceToStoragePool(Object instanceObj) {
        StoragePool sp = new StoragePool();
        JsonObject jsonObject = new JsonParser().parse(instanceObj.toString()).getAsJsonObject();
        String name = ToolUtils.getStr(jsonObject.get("name"));
        String status = ToolUtils.getStr(jsonObject.get("status"));// runningStatus?
        String type = ToolUtils.getStr(jsonObject.get("type"));// SYS_StorageDisk中取磁盘类型?
        String poolId = ToolUtils.getStr(jsonObject.get("poolId"));
        String storageDeviceId = ToolUtils.getStr(jsonObject.get("storageDeviceId"));
        String storagePoolInstanceId = ToolUtils.getStr(jsonObject.get("id"));

        sp.setName(name);
        sp.setStorage_pool_id(poolId);
        sp.setStorage_device_id(storageDeviceId);
        sp.setStorage_instance_id(storagePoolInstanceId);

        return sp;
    }


    /*public static void main(String[] args) {
        String str = "{\"service-levels\":[{\"id\": \"UUID\",\"name\": \"service-level_block\",\"description\": \"block service-level for dj\",\"type\":\"BLOCK\",\"protocol\": \"FC\",\"total_capacity\": 200,\"free_capacity\": 200,\"used_capacity\":100,\"capabilities\": {\"resource_type\": \"thin\",\"compression\": true,\"deduplication\": true,\"smarttier\": {\"policy\": \"1\",\"enabled\": true},\"qos\": {\"enabled\": true,\"qos_param\":{\"latency\":\"10\",\"latencyUnit\": \"ms\",\"minBandWidth\": 1000,\"minIOPS\": 1000}}}}]}";
        ServiceLevelServiceImpl sls = new ServiceLevelServiceImpl();
        sls.convertBean(str);
    }*/
}
