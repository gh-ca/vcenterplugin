package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.model.RelationInstance;
import com.dmeplugin.dmestore.services.bestpractice.DmeIndicatorConstants;
import com.dmeplugin.dmestore.utils.ToolUtils;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @Description: TODO
 * @ClassName: DataStoreStatisticHistoryServiceImpl
 * @Company: GH-CA
 * @author: liuxh
 * @create: 2020-09-03
 **/
public class DataStoreStatisticHistoryServiceImpl implements DataStoreStatisticHistoryService {
    private static final Logger log = LoggerFactory.getLogger(DataStoreStatisticHistoryService.class);

    private Gson gson = new Gson();
    @Autowired
    private DmeAccessService dmeAccessService;
    @Autowired
    private DmeRelationInstanceService dmeRelationInstanceService;

    private final String STATISTIC_QUERY = "/rest/metrics/v1/data-svc/history-data/action/query";
    private final String OBJ_TYPES_LIST = "/rest/metrics/v1/mgr-svc/obj-types";
    private final String INDICATORS_LIST = "/rest/metrics/v1/mgr-svc/indicators";
    private final String OBJ_TYPE_INDICATORS_QUERY = "/rest/metrics/v1/mgr-svc/obj-types/{obj-type-id}/indicators";

    //性能指标 id和name的映射关系
    private static Map<String, String> indicatorNameIdMap = new HashMap<>();
    private static Map<String, String> indicatorIdNameMap = new HashMap<>();

    //资源对象类型 id和name的映射关系
    private static Map<String, String> objtypeIdNampMap = new HashMap<>();
    private static Map<String, String> objtypeNameIdMap = new HashMap<>();

    //资源对象类型支持指标 对象类型id和指标id集合关系
    private static Map<String, List<String>> objTypeCountersMap = new HashMap<>();

    @Override
    public Map<String, Object> queryVmfsStatistic(Map<String, Object> params) throws DMEException {
        //通过存储ID查卷ID 实际获取卷的性能数据
        return queryVolumeStatistic(params);
    }

    @Override
    public Map<String, Object> queryVmfsStatisticCurrent(Map<String, Object> params) throws DMEException {
        Map<String, String> idInstancdIdMap = new HashMap<>(16);
        List<String> instanceIds = new ArrayList<>();
        Object indicatorIds = params.get("indicator_ids");
        List<String> ids = (List<String>) params.get("obj_ids");
        //ids若为wwn的集合则转换为对应的instanceId集合,也有可能ids直接就是volume的instanceId集合
        if (ids.size() > 0) {
            Map<String, Map<String, Object>> sysLunMap = dmeRelationInstanceService.getLunInstance();
            for (String id : ids) {
                try {
                    String instanceId = sysLunMap.get(id).get("resId").toString();
                    if (!StringUtils.isEmpty(instanceId)) {
                        idInstancdIdMap.put(id, instanceId);
                        instanceIds.add(instanceId);
                    }
                } catch (Exception e) {
                    log.warn("查询vmfs性能,通过wwn查询instanceId异常,wwn:" + id);
                }
            }
            if (instanceIds.size() > 0) {
                params.put("obj_ids", instanceIds);
            }
        }
        if (null == indicatorIds) {
            indicatorIds = initVolumeIndicator(true);
            params.put("indicator_ids", indicatorIds);
        }
        //SYS_Lun
        String objTypeId = "1125921381679104";
        params.put("obj_type_id", objTypeId);
        return queryHistoryStatistic("queryVmfsStatisticCurrent", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryNfsStatistic(Map<String, Object> params) throws DMEException {
        return queryFsStatistic(params);
    }

    @Override
    public Map<String, Object> queryNfsStatisticCurrent(Map<String, Object> params) throws DMEException {
        Object indicatorIds = params.get("indicator_ids");
        if (null == indicatorIds) {
            indicatorIds = initFsIndicator(true);
            params.put("indicator_ids", indicatorIds);
        }
        //SYS_StorageFileSystem
        String objTypeId = "1126179079716864";
        params.put("obj_type_id", objTypeId);
        return queryHistoryStatistic("queryNfsStatisticCurrent", params, null);
    }

    @Override
    public Map<String, Object> queryVolumeStatistic(Map<String, Object> params) throws DMEException {
        Map<String, String> idInstancdIdMap = initParamVolume(params, false);
        return queryHistoryStatistic("volume", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryControllerStatistic(Map<String, Object> params) throws DMEException {
        Map<String, String> idInstancdIdMap = initParamController(params, false);
        return queryHistoryStatistic("controller", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryStoragePortStatistic(Map<String, Object> params) throws DMEException {
        Map<String, String> idInstancdIdMap = initParamStoragePort(params, false);
        return queryHistoryStatistic("storagePort", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryStorageDiskStatistic(Map<String, Object> params) throws DMEException {
        Map<String, String> idInstancdIdMap = initParamStorageDisk(params, false);
        return queryHistoryStatistic("storageDisk", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryFsStatistic(Map<String, Object> params) throws DMEException {
        Map<String, String> idInstancdIdMap = initParamFs(params, false);
        return queryHistoryStatistic("storageFileSystem", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryServiceLevelStatistic(Map<String, Object> params) throws DMEException {
        Map<String, String> idInstancdIdMap = initParamServiceLevel(params);
        return queryHistoryStatistic("serviceLevel", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryServiceLevelLunStatistic(Map<String, Object> params) throws DMEException {
        Map<String, String> idInstanceIdMap = new HashMap<>(16);
        List<String> instanceIds = new ArrayList<>();
        //SYS_LUN
        String objTypeId = "1125921381679104";
        String relationName = "M_DjTierContainsLun";
        Object indicatorIds = params.get("indicator_ids");
        Object objIds = params.get("obj_ids");
        List<String> ids = getObjIds(objIds);
        if (ids.size() > 0) {
            for (String id : ids) {
                //查询serviceLevel关联的lun实例ID
                String targetInstanceId = getTargetInstanceIdByRelationNameSourceId(relationName, id);
                if (!StringUtils.isEmpty(targetInstanceId)) {
                    idInstanceIdMap.put(id, targetInstanceId);
                    instanceIds.add(targetInstanceId);
                }
            }
            if (instanceIds.size() > 0) {
                params.put("obj_ids", instanceIds);
            }
        }
        params.put("obj_type_id", objTypeId);
        if (null == indicatorIds) {
            indicatorIds = initServiceLevelLunIndicator();
            params.put("indicator_ids", indicatorIds);
        }
        return queryHistoryStatistic("serviceLevel Lun", params, idInstanceIdMap);
    }

    @Override
    public Map<String, Object> queryServiceLevelStoragePoolStatistic(Map<String, Object> params) throws DMEException {
        Map<String, String> idInstanceIdMap = new HashMap<>(16);
        List<String> instanceIds = new ArrayList<>();
        //SYS_StoragePool
        String objTypeId = "1125912791744512";
        String relationName = "M_DjTierContainsStoragePool";
        Object indicatorIds = params.get("indicator_ids");
        Object objIds = params.get("obj_ids");
        List<String> ids = getObjIds(objIds);
        if (ids.size() > 0) {
            for (String id : ids) {
                //查询serviceLevel关联的StoragePool的实例ID
                String targetInstanceId = getTargetInstanceIdByRelationNameSourceId(relationName, id);
                if (!StringUtils.isEmpty(targetInstanceId)) {
                    idInstanceIdMap.put(id, targetInstanceId);
                    instanceIds.add(targetInstanceId);
                }
            }
            if (instanceIds.size() > 0) {
                params.put("obj_ids", instanceIds);
            }
        }
        params.put("obj_type_id", objTypeId);
        if (null == indicatorIds) {
            indicatorIds = initServiceLevelStoragePoolIndicator();
            params.put("indicator_ids", indicatorIds);
        }
        return queryHistoryStatistic("serviceLevel StroagePool", params, idInstanceIdMap);
    }

    @Override
    public Map<String, Object> queryStoragePoolStatistic(Map<String, Object> params) throws DMEException {
        Map<String, String> idInstancdIdMap = initParamStoragePool(params);
        return queryHistoryStatistic("queryStoragePoolStatistic", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryStorageDevcieStatistic(Map<String, Object> params) throws DMEException {
        Map<String, String> idInstancdIdMap = initParamStorageDevice(params);
        return queryHistoryStatistic("queryStorageDevcieStatistic", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryStorageDevcieCurrentStatistic(Map<String, Object> params) throws DMEException {
        Map<String, String> idInstancdIdMap = initParamStorageDevice(params);
        return queryCurrentStatistic("StorageDevcie", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryStoragePoolCurrentStatistic(Map<String, Object> params) throws DMEException {
        Map<String, String> idInstancdIdMap = initParamStoragePool(params);
        return queryCurrentStatistic("StoragePool", params, idInstancdIdMap);
    }

    public Map<String, Object> queryVolumeCurrentStatistic(Map<String, Object> params) throws DMEException {
        Map<String, String> idInstancdIdMap = initParamVolume(params, true);
        return queryCurrentStatistic("volume", params, idInstancdIdMap);
    }

    public Map<String, Object> queryServiceLevelCurrentStatistic(Map<String, Object> params) throws DMEException {
        Map<String, String> idInstancdIdMap = initParamServiceLevel(params);
        return queryCurrentStatistic("volume", params, idInstancdIdMap);
    }

    public Map<String, Object> queryFsCurrentStatistic(Map<String, Object> params) throws DMEException {
        Map<String, String> idInstancdIdMap = initParamFs(params, true);
        return queryCurrentStatistic("volume", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryControllerCurrentStatistic(Map<String, Object> params) throws DMEException {
        Map<String, String> idInstancdIdMap = initParamController(params, true);
        return queryCurrentStatistic("controller", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryStoragePortCurrentStatistic(Map<String, Object> params) throws DMEException {
        Map<String, String> idInstancdIdMap = initParamStoragePort(params, true);
        return queryCurrentStatistic("storageport", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryStorageDiskCurrentStatistic(Map<String, Object> params) throws DMEException {
        Map<String, String> idInstancdIdMap = initParamStorageDisk(params, true);
        return queryCurrentStatistic("storagedisk", params, idInstancdIdMap);
    }


    @Override
    public Map<String, Object> queryHistoryStatistic(String relationOrInstance, Map<String, Object> params) throws DMEException {
        Map<String, Object> resultMap = new HashMap<>(16);
        if (!StringUtils.isEmpty(relationOrInstance)) {
            switch (relationOrInstance) {
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_STORAGEDEVICE:
                    resultMap = queryStorageDevcieStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_STORAGEPOOL:
                    resultMap = queryStoragePoolStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_LUN:
                    resultMap = queryVolumeStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_SERVICELEVEL:
                    resultMap = queryServiceLevelStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_FILESYSTEM:
                    resultMap = queryFsStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_CONTROLLER:
                    resultMap = queryControllerStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_STORAGEPORT:
                    resultMap = queryStoragePortStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_STORAGEDISK:
                    resultMap = queryStorageDiskStatistic(params);
                    break;
                default:
                    log.error("query " + relationOrInstance + " statistic error, non-supported relation and instance.the params is:{}", gson.toJson(params));
                    throw new DMEException("503", "query " + relationOrInstance + " statistic error, non-supported relation and instance.the params is:{}" + gson.toJson(params));
            }

        }
        return resultMap;
    }

    @Override
    public Map<String, Object> queryCurrentStatistic(String relationOrInstance, Map<String, Object> params) throws DMEException {
        Map<String, Object> resultMap = new HashMap<>(16);
        if (!StringUtils.isEmpty(relationOrInstance)) {
            switch (relationOrInstance) {
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_STORAGEDEVICE:
                    resultMap = queryStorageDevcieCurrentStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_STORAGEPOOL:
                    resultMap = queryStoragePoolCurrentStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_LUN:
                    resultMap = queryVolumeCurrentStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_SERVICELEVEL:
                    resultMap = queryServiceLevelCurrentStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_FILESYSTEM:
                    resultMap = queryFsCurrentStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_CONTROLLER:
                    resultMap = queryControllerCurrentStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_STORAGEPORT:
                    resultMap = queryStoragePortCurrentStatistic(params);
                    break;
                case DmeIndicatorConstants.RESOURCE_TYPE_NAME_STORAGEDISK:
                    resultMap = queryStorageDiskCurrentStatistic(params);
                    break;
                default:
                    resultMap.put("code", 503);
                    resultMap.put("message", "query " + relationOrInstance + " current statistic error, non-supported relation and instance!");
                    log.error("query " + relationOrInstance + " current statistic error, non-supported relation and instance.the params is:{}", gson.toJson(params));
                    throw new DMEException(gson.toJson(resultMap));
            }
        }
        return resultMap;
    }

    //预处理存储设备参数
    private Map<String, String> initParamStorageDevice(Map<String, Object> params) {
        if (null == params || params.size() == 0) {
            return null;
        }
        Map<String, String> idInstancdIdMap = new HashMap<>(16);
        List<String> instanceIds = new ArrayList<>();
        List<String> ids = (List<String>) params.get("obj_ids");
        Object indicatorIds = params.get("indicator_ids");
        Map<String, Map<String, Object>> instanceMap = dmeRelationInstanceService.getStorageDeviceInstance();
        for (String id : ids) {
            try {
                String instanceId = instanceMap.get(id).get("resId").toString();
                if (!StringUtils.isEmpty(instanceId)) {
                    idInstancdIdMap.put(id, instanceId);
                    instanceIds.add(instanceId);
                }
            } catch (Exception e) {
                log.warn("查询存储设备性能,通过storageDeviceId查询instanceId异常,storageDeviceId:" + id);
            }
        }
        if (instanceIds.size() > 0) {
            params.put("obj_ids", instanceIds);
        }
        if (null == indicatorIds) {
            indicatorIds = initStorageDeviceIndicator();
            params.put("indicator_ids", indicatorIds);
        }
        //SYS_StorDevice
        String objTypeId = "1125904201809920";
        params.put("obj_type_id", objTypeId);
        return idInstancdIdMap;
    }

    //预处理存储池参数
    private Map<String, String> initParamStoragePool(Map<String, Object> params) {
        if (null == params || params.size() == 0) {
            return null;
        }
        Map<String, String> idInstancdIdMap = new HashMap<>(16);
        List<String> instanceIds = new ArrayList<>();
        List<String> ids = (List<String>) params.get("obj_ids");
        Object indicatorIds = params.get("indicator_ids");
        Map<String, Map<String, Object>> sysLunMap = dmeRelationInstanceService.getStoragePoolInstance();
        for (String id : ids) {
            try {
                String instanceId = sysLunMap.get(id).get("resId").toString();
                if (!StringUtils.isEmpty(instanceId)) {
                    idInstancdIdMap.put(id, instanceId);
                    instanceIds.add(instanceId);
                }
            } catch (Exception e) {
                log.warn("查询存储池性能,通过storagePoolId查询instanceId异常,storagePoolId:" + id);
            }
        }
        if (instanceIds.size() > 0) {
            params.put("obj_ids", instanceIds);
        }
        if (null == indicatorIds) {
            indicatorIds = initStoragePoolIndicator();
            params.put("indicator_ids", indicatorIds);
        }
        //SYS_StoragePool
        String objTypeId = "1125912791744512";
        params.put("obj_type_id", objTypeId);
        return idInstancdIdMap;
    }

    //预处理卷参数
    private Map<String, String> initParamVolume(Map<String, Object> params, boolean isCurrent) {
        if (null == params || params.size() == 0) {
            return null;
        }
        Map<String, String> idInstancdIdMap = new HashMap<>(16);
        List<String> instanceIds = new ArrayList<>();
        Object indicatorIds = params.get("indicator_ids");
        Object objIds = params.get("obj_ids");
        List<String> ids = getObjIds(objIds);
        //ids若为wwn的集合则转换为对应的instanceId集合,也有可能ids直接就是volume的instanceId集合
        if (ids.size() > 0) {
            Map<String, Map<String, Object>> sysLunMap = dmeRelationInstanceService.getLunInstance();
            for (String id : ids) {
                try {
                    String instanceId = sysLunMap.get(id).get("resId").toString();
                    if (!StringUtils.isEmpty(instanceId)) {
                        idInstancdIdMap.put(id, instanceId);
                        instanceIds.add(instanceId);
                    }
                } catch (Exception e) {
                    log.warn("查询磁盘性能,通过wwn查询instanceId异常,wwn:" + id);
                }
            }
            if (instanceIds.size() > 0) {
                params.put("obj_ids", instanceIds);
            }
        }
        //SYS_Lun
        String objTypeId = "1125921381679104";
        params.put("obj_type_id", objTypeId);
        if (null == indicatorIds) {
            indicatorIds = initVolumeIndicator(isCurrent);
            params.put("indicator_ids", indicatorIds);
        }
        return idInstancdIdMap;
    }

    //预处理服务等级参数(params中的obj_ids如果是instanceId 则不用做转换处理)
    private Map<String, String> initParamServiceLevel(Map<String, Object> params) {
        Map<String, String> idInstanceIdMap = new HashMap<>();
        Object indicatorIds = params.get("indicator_ids");
        //SYS_DjTier
        String objTypeId = "1126174784749568";
        params.put("obj_type_id", objTypeId);
        if (null == indicatorIds) {
            indicatorIds = initServiceLevelIndicator();
            params.put("indicator_ids", indicatorIds);
        }
        return idInstanceIdMap;
    }

    //预处理文件系统参数(params中的obj_ids如果是instanceId 则不用做转换处理)
    private Map<String, String> initParamFs(Map<String, Object> params, boolean isCurrent) {
        Map<String, String> idInstanceIdMap = new HashMap<>();
        Object indicatorIds = params.get("indicator_ids");
        //SYS_StorageFileSystem
        String objTypeId = "1126179079716864";
        params.put("obj_type_id", objTypeId);
        if (null == indicatorIds) {
            indicatorIds = initFsIndicator(isCurrent);
            params.put("indicator_ids", indicatorIds);
        }
        return idInstanceIdMap;
    }

    //预处理控制器参数(params中的obj_ids如果是instanceId 则不用做转换处理)
    private Map<String, String> initParamController(Map<String, Object> params, boolean isCurrent) {
        Map<String, String> idInstanceIdMap = new HashMap<>();
        Object indicatorIds = params.get("indicator_ids");
        //SYS_Contorller
        String objTypeId = "1125908496777216";
        params.put("obj_type_id", objTypeId);
        if (null == indicatorIds) {
            indicatorIds = initControllerIndicator(isCurrent);
            params.put("indicator_ids", indicatorIds);
        }
        return idInstanceIdMap;
    }

    //预处理存储端口参数(params中的obj_ids如果是instanceId 则不用做转换处理)
    private Map<String, String> initParamStoragePort(Map<String, Object> params, boolean isCurrent) {
        Map<String, String> idInstanceIdMap = new HashMap<>();
        Object indicatorIds = params.get("indicator_ids");
        //SYS_StoragePort
        String objTypeId = "1125925676646400";
        params.put("obj_type_id", objTypeId);
        if (null == indicatorIds) {
            indicatorIds = initStoragePortIndicator(isCurrent);
            params.put("indicator_ids", indicatorIds);
        }
        return idInstanceIdMap;
    }

    //预处理存储硬盘参数(params中的obj_ids如果是instanceId 则不用做转换处理)
    private Map<String, String> initParamStorageDisk(Map<String, Object> params, boolean isCurrent) {
        Map<String, String> idInstanceIdMap = new HashMap<>();
        Object indicatorIds = params.get("indicator_ids");
        //SYS_StorageDisk
        String objTypeId = "1125917086711808";
        params.put("obj_type_id", objTypeId);
        if (null == indicatorIds) {
            indicatorIds = initStorageDiskIndicator(isCurrent);
            params.put("indicator_ids", indicatorIds);
        }
        return idInstanceIdMap;
    }

    //查询sourceId下relationName对应的关系
    private RelationInstance getInstance(String relationName, String sourceId) {
        RelationInstance relationInstance = null;
        try {
            List<RelationInstance> instances = dmeRelationInstanceService.queryRelationByRelationNameConditionSourceInstanceId(relationName, sourceId);
            if (instances.size() > 0) {
                relationInstance = instances.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return relationInstance;
    }

    //通过resourceId 查询resourceInstanceId,再查询resourceInstanceId关联的关系类型的targetInstanceId
    private String getTargetInstanceIdByRelationNameSourceId(String relationName, String resourceId) {
        String sourceInstanceId = "";
        String targetInstanceId = "";
        //获取sourceInstanceId
        Map<String, Map<String, Object>> serviceLevelInstance = dmeRelationInstanceService.getServiceLevelInstance();
        if (null != serviceLevelInstance) {
            Map<String, Object> slInstance = serviceLevelInstance.get(resourceId);
            if (null != slInstance) {
                Object sourceIdObj = slInstance.get("resId");
                if (null != sourceIdObj) {
                    sourceInstanceId = sourceIdObj.toString();
                }
            }
        }
        //获取targetInstanceId
        if (!StringUtils.isEmpty(sourceInstanceId)) {
            RelationInstance instance = getInstance(relationName, sourceInstanceId);
            if (null != instance) {
                targetInstanceId = instance.getTargetInstanceId();
            }
        }
        return targetInstanceId;
    }

    //从ObjectIds中提取id
    private List<String> getObjIds(Object objIds) {
        List<String> objectIds = new ArrayList<>();
        if (null != objIds) {
            JsonArray objIdJsonArray = new JsonParser().parse(objIds.toString()).getAsJsonArray();
            for (JsonElement element : objIdJsonArray) {
                try {
                    String id = element.getAsString();
                    objectIds.add(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return objectIds;
    }

    //query statistic by objType(methodName)
    private Map<String, Object> queryHistoryStatistic(String relationOrInstance, Map<String, Object> params, Map<String, String> idInstanceIdMap) throws DMEException {
        Map<String, Object> resultmap = new HashMap<>();

        ResponseEntity responseEntity;
        JsonElement statisticElement;
        List<List<String>> objIdGroup = groupObjIds(params);
        if (null != objIdGroup && objIdGroup.size() > 0) {
            for (List<String> objids : objIdGroup) {
                params.put("obj_ids", objids);
                try {
                    responseEntity = queryStatistic(params);
                    if (null != responseEntity && 200 == responseEntity.getStatusCodeValue()) {
                        Object body = responseEntity.getBody();
                        String bodyStr = body.toString();
                        bodyStr = replace(bodyStr, idInstanceIdMap);
                        JsonObject bodyJson = new JsonParser().parse(bodyStr).getAsJsonObject();
                        statisticElement = bodyJson.get("data");
                        if (ToolUtils.jsonIsNull(statisticElement)) {
                            log.error("query " + relationOrInstance + "objid: " + gson.toJson(objids) + "Statistic is null:", bodyJson.get("error_msg").getAsString());
                            continue;
                        }
                        Map<String, Object> objectMap = convertMap(statisticElement);
                        resultmap.putAll(objectMap);
                        if (objectMap.size() == 0) {
                            log.error("query " + relationOrInstance + "Statistic error:", bodyJson.get("error_msg").getAsString());
                        }
                    }else{
                        log.error("query " + relationOrInstance + " statistic error,the params is:{}", gson.toJson(params));
                        throw new DMEException("503", responseEntity.getBody().toString());
                    }
                } catch (Exception e) {
                    log.error("query " + relationOrInstance + " statistic exception.", e);
                    throw new DMEException("503", e.getMessage());
                }
            }
        }
        return resultmap;
    }

    private Map<String, Object> queryCurrentStatistic(String relationOrInstance, Map<String, Object> params, Map<String, String> idInstanceIdMap) throws DMEException {
        Map<String, Object> resultmap = new HashMap<>();

        String label = "max";
        ResponseEntity responseEntity;
        JsonElement statisticElement;
        List<List<String>> objIdGroup = groupObjIds(params);
        if (null != objIdGroup && objIdGroup.size() > 0) {
            for (List<String> objids : objIdGroup) {
                params.put("obj_ids", objids);
                try {
                    responseEntity = queryStatistic(params);
                    if (null != responseEntity && 200 == responseEntity.getStatusCodeValue()) {
                        Object body = responseEntity.getBody();
                        String bodyStr = body.toString();
                        bodyStr = replace(bodyStr, idInstanceIdMap);
                        JsonObject bodyJson = new JsonParser().parse(bodyStr).getAsJsonObject();
                        statisticElement = bodyJson.get("data");
                        if (ToolUtils.jsonIsNull(statisticElement)) {
                            log.error("query " + relationOrInstance + "objid: " + gson.toJson(objids) + "currentStatistic is null:", bodyJson.get("error_msg").getAsString());
                            continue;
                        }
                        Map<String, Object> objectMap = convertMap(statisticElement, label);
                        resultmap.putAll(objectMap);
                        if (null == objectMap || objectMap.size() == 0) {
                            log.error("query " + relationOrInstance + " current statistic error:", bodyJson.get("error_msg").getAsString());
                        }
                    } else {
                        log.error("query " + relationOrInstance + " current statistic error,the params is:{}", gson.toJson(params));
                    }
                } catch (Exception e) {
                    log.error("query " + relationOrInstance + " current statistic exception.", e);
                }
            }
        }
        return resultmap;
    }

    //query statistic
    private ResponseEntity queryStatistic(Map<String, Object> params) throws Exception {
        ResponseEntity responseEntity;
        String objTypeId = params.get("obj_type_id").toString();
        Object indicatorIds = params.get("indicator_ids");
        Object objIds = params.get("obj_ids");
        params = initParams(params);

        String interval = params.get("interval").toString();
        String range = params.get("range").toString();

        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("obj_type_id", objTypeId);
        requestbody.put("indicator_ids", indicatorIds);
        requestbody.put("obj_ids", objIds);
        requestbody.put("interval", interval);
        requestbody.put("range", range);
        if (RANG_BEGIN_END_TIME.equals(range)) {
            String beginTime = ToolUtils.getStr(params.get("begin_time"));
            String endTime = ToolUtils.getStr(params.get("end_time"));
            requestbody.put("begin_time", beginTime);
            requestbody.put("end_time", endTime);
        }
        responseEntity = dmeAccessService.access(STATISTIC_QUERY, HttpMethod.POST, gson.toJson(requestbody));
        return responseEntity;
    }

    // query obj_types
    private void queryObjtypes() throws Exception {
        ResponseEntity responseEntity = dmeAccessService.access(OBJ_TYPES_LIST, HttpMethod.GET, null);
        if (null != responseEntity && 200 == responseEntity.getStatusCodeValue()) {
            Object body = responseEntity.getBody();
            JsonObject bodyJson = new JsonParser().parse(body.toString()).getAsJsonObject();
            JsonArray dataJsonArray = bodyJson.get("data").getAsJsonArray();
            for (JsonElement element : dataJsonArray) {
                JsonObject dataJson = new JsonParser().parse(element.toString()).getAsJsonObject();
                String objId = dataJson.get("obj_type_id").getAsString();
                String resourceCategory = dataJson.get("resource_category").getAsString();
                objtypeIdNampMap.put(objId, resourceCategory);
                objtypeNameIdMap.put(resourceCategory, objId);
            }
        }
    }

    // query obj_type indicators
    private void queryIndicatorsByObjetypeId(String objtypeId) throws Exception {
        String apiUrl = OBJ_TYPE_INDICATORS_QUERY;
        apiUrl = apiUrl.replace("{obj-type-id}", objtypeId);
        if (apiUrl.indexOf("{obj-type-id}") > 0) {
            log.error("DataStoreStatistic query,the url is error, required \"obj-type-id\"!{}", apiUrl);
            return;
        }
        ResponseEntity responseEntity = dmeAccessService.access(apiUrl, HttpMethod.GET, null);

        if (null != responseEntity && 200 == responseEntity.getStatusCodeValue()) {
            Object body = responseEntity.getBody();
            JsonObject bodyJson = new JsonParser().parse(body.toString()).getAsJsonObject();
            JsonObject dataJson = bodyJson.get("data").getAsJsonObject();
            JsonArray ids = dataJson.get("indicator_ids").getAsJsonArray();
            List<String> indicatorIds = new ArrayList<>();
            for (JsonElement element : ids) {
                String id = element.toString();
                indicatorIds.add(id);
            }
            objTypeCountersMap.put(objtypeId, indicatorIds);
        }
    }

    // query indicators
    private void queryIndicators() throws Exception {
        ResponseEntity responseEntity = dmeAccessService.access(INDICATORS_LIST, HttpMethod.POST, null);
        if (null != responseEntity && 200 == responseEntity.getStatusCodeValue()) {
            Object body = responseEntity.getBody();
            JsonObject bodyJson = new JsonParser().parse(body.toString()).getAsJsonObject();
            JsonObject dataJson = bodyJson.get("data").getAsJsonObject();
            Iterator iter = dataJson.entrySet().iterator();
            while (iter.hasNext()) {
                String counterId = iter.toString();
                JsonObject counterJson = dataJson.get(counterId).getAsJsonObject();
                String counterName = counterJson.get("indicator_name").getAsString();
                indicatorIdNameMap.put(counterId, counterName);
                indicatorNameIdMap.put(counterName, counterId);
            }
        }
    }

    //性能查询条件参数初始化
    private Map<String, Object> initParams(Map<String, Object> params) {
        String rang = ToolUtils.getStr(params.get("range"));
        String interval = ToolUtils.getStr(params.get("interval"));
        long beginTime = ToolUtils.getLong(params.get("begin_time"));
        long endTime = ToolUtils.getLong(params.get("end_time"));

        //设置默认值
        if (StringUtils.isEmpty(rang)) {
            rang = RANGE_LAST_1_DAY;
            params.put("range", rang);
        }
        //时间范围为时间段 而未设置具体时间 则默认一天
        if (RANG_BEGIN_END_TIME.equals(rang)) {
            if (0 == endTime) {
                endTime = System.currentTimeMillis();
                params.put("end_time", endTime);
            }
            if (0 == beginTime) {
                beginTime = endTime - 24 * 60 * 60 * 1000;
                params.put("begin_time", beginTime);
            }
        }
        if (StringUtils.isEmpty(interval)) {
            switch (rang) {
                case RANGE_LAST_5_MINUTE:
                    interval = INTERVAL_ONE_MINUTE;
                    break;
                case RANGE_LAST_1_HOUR:
                    interval = INTERVAL_ONE_MINUTE;
                    break;
                    //INTERVAL_ONE_MINUTE查不到值，会报错
                case RANGE_LAST_1_DAY:
                    interval = INTERVAL_ONE_MINUTE;
                    break;
                case RANGE_LAST_1_WEEK:
                    interval = INTERVAL_HALF_HOUR;
                    break;
                case RANGE_LAST_1_MONTH:
                    interval = INTERVAL_HALF_HOUR;
                    break;
                case RANGE_LAST_1_QUARTER:
                    interval = INTERVAL_DAY;
                    break;
                case RANGE_HALF_1_YEAR:
                    interval = INTERVAL_DAY;
                    break;
                case RANGE_LAST_1_YEAR:
                    interval = INTERVAL_DAY;
                    break;
                case RANG_BEGIN_END_TIME:
                    interval = INTERVAL_ONE_MINUTE;
                    break;
                default:
            }
            params.put("interval", interval);
        }
        return params;
    }

    //nfs的默认指标集合 目前取的DME存储设备的指标
    private List<String> initFsIndicator(boolean wetherCurrent) {
        List<String> indicators = new ArrayList<>();
        if (wetherCurrent) {
            indicators.add(DmeIndicatorConstants.COUNTER_ID_FS_THROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_FS_BANDWIDTH);
        } else {
            indicators.add(DmeIndicatorConstants.COUNTER_ID_FS_READTHROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_FS_WRITETHROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_FS_READBANDWIDTH);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_FS_WRITEBANDWIDTH);
        }
        indicators.add(DmeIndicatorConstants.COUNTER_ID_FS_READRESPONSETIME);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_FS_WRITERESPONSETIME);

        return indicators;
    }

    //volume默认指标集合
    private List<String> initVolumeIndicator(boolean isCurrent) {
        List<String> indicators = new ArrayList<>();
        if (isCurrent) {
            indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_THROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_BANDWIDTH);
        } else {
            indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_READTHROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_WRITETHROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_READBANDWIDTH);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_WRITEBANDWIDTH);
        }
        indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_READRESPONSETIME);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_WRITERESPONSETIME);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_RESPONSETIME);
        return indicators;
    }

    //serviceLevel默认指标集合
    private List<String> initServiceLevelIndicator() {
        List<String> indicators = new ArrayList<>();
        indicators.add(DmeIndicatorConstants.COUNTER_ID_SERVICELECVEL_MAXRESPONSETIME);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_SERVICELEVEL_BANDWIDTHTIB);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_SERVICELEVEL_THROUGHPUTTIB);
        return indicators;
    }

    //serivceLevelLun默认指标集合
    private List<String> initServiceLevelLunIndicator() {
        List<String> indicators = new ArrayList<>();
        indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_RESPONSETIME);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_BANDWIDTH);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_THROUGHPUT);
        return indicators;
    }

    //serviceLevelStoragepool 默认指标集合
    private List<String> initServiceLevelStoragePoolIndicator() {
        List<String> indicators = new ArrayList<>();
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPOOL_THROUGHPUT);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPOOL_BANDWIDTH);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPOOL_RESPONSETIME);
        return indicators;
    }

    //Storagepool 默认指标集合
    private List<String> initStoragePoolIndicator() {
        List<String> indicators = new ArrayList<>();
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPOOL_THROUGHPUT);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPOOL_RESPONSETIME);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPOOL_BANDWIDTH);
        return indicators;
    }

    //StorageDevice 默认指标集合
    private List<String> initStorageDeviceIndicator() {
        List<String> indicators = new ArrayList<>();
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORDEVICE_CPUUSAGE);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORDEVICE_RESPONSETIME);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORDEVICE_BANDWIDTH);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORDEVICE_READTHROUGHPUT);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORDEVICE_WRITETHROUGHPUT);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORDEVICE_READBANDWIDTH);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORDEVICE_WRITEBANDWIDTH);
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORDEVICE_THROUGHPUT);
        return indicators;
    }

    //Controller 默认指标集合
    private List<String> initControllerIndicator(boolean isCurrent) {
        List<String> indicators = new ArrayList<>();
        if (isCurrent) {
            indicators.add(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_THROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_BANDWIDTH);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_CPUUSAGE);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_RESPONSETIME);
        } else {
            indicators.add(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_READTHROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_WRITETHROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_READBANDWIDTH);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_WRITEBANDWIDTH);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_READRESPONSETIME);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_WRITERESPONSETIME);
        }
        return indicators;
    }

    //StoragePort 默认指标集合
    private List<String> initStoragePortIndicator(boolean isCurrent) {
        List<String> indicators = new ArrayList<>();
        if (isCurrent) {
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_THROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_BANDWIDTH);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_UTILITY);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_RESPONSETIME);
        } else {
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_READTHROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_WRITETHROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_READBANDWIDTH);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_WRITEBANDWIDTH);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_READRESPONSETIME);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_WRITERESPONSETIME);
        }
        return indicators;
    }

    //StorageDisk 默认指标集合
    private List<String> initStorageDiskIndicator(boolean isCurrent) {
        List<String> indicators = new ArrayList<>();
        if (isCurrent) {
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEDISK_READTHROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEDISK_BANDWIDTH);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEDISK_UTILITY);
        } else {
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEDISK_READTHROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEDISK_WRITETHROUGHPUT);
            indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEDISK_BANDWIDTH);
        }
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEDISK_RESPONSETIME);
        return indicators;
    }

    //消息转换  提取实时性能数据
    private JsonObject getCurrentStatistic(Object object) {
        JsonObject data = new JsonObject();
        JsonObject dataJson = new JsonParser().parse(object.toString()).getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> volumeSet = dataJson.getAsJsonObject().entrySet();
        for (Map.Entry<String, JsonElement> volume : volumeSet) {
            JsonObject countRes = new JsonObject();
            String volumeId = volume.getKey();
            JsonObject counterObj = volume.getValue().getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> counterSet = counterObj.getAsJsonObject().entrySet();
            for (Map.Entry<String, JsonElement> countere : counterSet) {
                String counterId = countere.getKey();
                JsonObject counterjson = countere.getValue().getAsJsonObject();
                JsonArray series = counterjson.getAsJsonArray("series");
                for (JsonElement elment : series) {
                    JsonObject serieJson = elment.getAsJsonObject();
                    Set<Map.Entry<String, JsonElement>> serieJsonSet = serieJson.getAsJsonObject().entrySet();
                    for (Map.Entry<String, JsonElement> serie : serieJsonSet) {
                        String value = serie.getValue().getAsString();
                        countRes.addProperty(counterId, value);
                        break;
                    }
                    break;
                }
            }
            data.add(volumeId, countRes);
        }
        return data;
    }

    //消息转换 object---map
    private Map<String, Object> convertMap(JsonElement jsonElement) {
        Map<String, Object> objectMap = new HashMap<>();
        if (!ToolUtils.jsonIsNull(jsonElement)) {
            Set<Map.Entry<String, JsonElement>> objectSet = jsonElement.getAsJsonObject().entrySet();
            for (Map.Entry<String, JsonElement> objectEntry : objectSet) {
                String objectId = objectEntry.getKey();
                JsonElement objectElement = objectEntry.getValue();
                if (!ToolUtils.jsonIsNull(objectElement)) {
                    Set<Map.Entry<String, JsonElement>> objectValueSet = objectElement.getAsJsonObject().entrySet();
                    Map<String, Object> indicatorMap = new HashMap<>();
                    for (Map.Entry<String, JsonElement> indicaterEntry : objectValueSet) {
                        Map<String, Object> indicatorValueMap = new HashMap<>();
                        String indicatoerId = indicaterEntry.getKey();
                        JsonElement indicatorElement = indicaterEntry.getValue();
                        JsonObject indicatorValueObject = indicatorElement.getAsJsonObject();
                        JsonArray seriesArray = indicatorValueObject.get("series").getAsJsonArray();
                        if (null != seriesArray && seriesArray.size() > 0) {
                            List<Map<String, String>> seriesList = new ArrayList<>();
                            for (JsonElement serieCellElemt : seriesArray) {
                                Map<String, String> seriesMap = new HashMap<>();
                                if (!ToolUtils.jsonIsNull(serieCellElemt)) {
                                    Set<Map.Entry<String, JsonElement>> cellSet = serieCellElemt.getAsJsonObject().entrySet();
                                    for (Map.Entry<String, JsonElement> cellEntry : cellSet) {
                                        String time = cellEntry.getKey();
                                        String value = cellEntry.getValue().getAsString();
                                        seriesMap.put(time, value);
                                        seriesList.add(seriesMap);
                                        break;
                                    }
                                }
                            }
                            indicatorValueMap.put("series", seriesList);
                        }
                        JsonElement minElement = indicatorValueObject.get("min");
                        if (!ToolUtils.jsonIsNull(minElement)) {
                            Map<String, String> minValueMap = new HashMap<>();
                            Set<Map.Entry<String, JsonElement>> minSet = minElement.getAsJsonObject().entrySet();
                            for (Map.Entry<String, JsonElement> minEntry : minSet) {
                                String time = minEntry.getKey();
                                String value = minEntry.getValue().getAsString();
                                minValueMap.put(time, value);
                                break;
                            }
                            indicatorValueMap.put("min", minValueMap);
                        }
                        JsonElement maxElement = indicatorValueObject.get("max");
                        if (!ToolUtils.jsonIsNull(maxElement)) {
                            Map<String, String> maxValueMap = new HashMap<>();
                            Set<Map.Entry<String, JsonElement>> maxSet = maxElement.getAsJsonObject().entrySet();
                            for (Map.Entry<String, JsonElement> maxEntry : maxSet) {
                                String time = maxEntry.getKey();
                                String value = maxEntry.getValue().getAsString();
                                maxValueMap.put(time, value);
                                break;
                            }
                            indicatorValueMap.put("max", maxValueMap);
                        }
                        JsonElement avgElement = indicatorValueObject.get("avg");
                        if (!ToolUtils.jsonIsNull(avgElement)) {
                            Map<String, String> avgMap = new HashMap<>();
                            Set<Map.Entry<String, JsonElement>> avgSet = avgElement.getAsJsonObject().entrySet();
                            for (Map.Entry<String, JsonElement> avgEntry : avgSet) {
                                String time = avgEntry.getKey();
                                String value = avgEntry.getValue().getAsString();
                                avgMap.put(time, value);
                                break;
                            }
                            indicatorValueMap.put("avg", avgMap);
                        }
                        indicatorMap.put(indicatoerId, indicatorValueMap);
                    }
                    objectMap.put(objectId, indicatorMap);
                }
            }
        }
        return objectMap;
    }

    //消息转换 object-map 提取指定的标签
    private Map<String, Object> convertMap(JsonElement jsonElement, String label) {
        Map<String, Object> objectMap = new HashMap<>();
        if (!ToolUtils.jsonIsNull(jsonElement)) {
            Set<Map.Entry<String, JsonElement>> objectSet = jsonElement.getAsJsonObject().entrySet();
            for (Map.Entry<String, JsonElement> objectEntry : objectSet) {
                String objectId = objectEntry.getKey();
                JsonElement objectElement = objectEntry.getValue();
                if (!ToolUtils.jsonIsNull(objectElement)) {
                    Set<Map.Entry<String, JsonElement>> objectValueSet = objectElement.getAsJsonObject().entrySet();
                    Map<String, Object> indicatorMap = new HashMap<>();
                    for (Map.Entry<String, JsonElement> indicaterEntry : objectValueSet) {
                        String indicatoerId = indicaterEntry.getKey();
                        JsonElement indicatorElement = indicaterEntry.getValue();
                        JsonObject indicatorValueObject = indicatorElement.getAsJsonObject();
                        JsonElement maxElement = indicatorValueObject.get(label);
                        if (!ToolUtils.jsonIsNull(maxElement)) {
                            Set<Map.Entry<String, JsonElement>> maxSet = maxElement.getAsJsonObject().entrySet();
                            for (Map.Entry<String, JsonElement> maxEntry : maxSet) {
                                String value = ToolUtils.jsonToStr(maxEntry.getValue());
                                indicatorMap.put(indicatoerId, value);
                                break;
                            }
                        }
                    }
                    objectMap.put(objectId, indicatorMap);
                }
            }
        }
        return objectMap;
    }

    //将性能数据结果中的instanceId转换为参数传递的id
    private String replace(String result, Map<String, String> idInstanceIdMap) {
        if (!StringUtils.isEmpty(result) && null != idInstanceIdMap && idInstanceIdMap.size() > 0) {
            for (Map.Entry<String, String> entry : idInstanceIdMap.entrySet()) {
                String id = entry.getKey();
                String instanceId = entry.getValue();
                if (!StringUtils.isEmpty(instanceId) && !StringUtils.isEmpty(id)) {
                    result = result.replace(instanceId, id);
                }
            }
        }
        return result;
    }

    //将对象分组:当对象数*指标数 > 100 时分组，数量太多会造成查询出错，先50
    private List<List<String>> groupObjIds(Map<String, Object> params) {
        int maxObjIndicator = 50;
        List<List<String>> objGroup = new ArrayList<>();
        List<String> objIds = (List<String>) params.get("obj_ids");
        List<String> indicatorIds = (List<String>) params.get("indicator_ids");
        int objSize = objIds.size();
        int indicatorSize = indicatorIds.size();
        if (objSize * indicatorSize > maxObjIndicator) {
            int length = maxObjIndicator / indicatorSize;
            int count = 0;
            List<String> list = null;
            for (String objId : objIds) {
                if (0 == count % length) {
                    list = new ArrayList<>();
                    objGroup.add(list);
                }
                list.add(objId);
                count++;
            }
        } else {
            objGroup.add(objIds);
        }
        return objGroup;
    }
}
