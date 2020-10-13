package com.dmeplugin.dmestore.services;

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
    public Map<String, Object> queryVmfsStatistic(Map<String, Object> params) throws Exception {
        //通过存储ID查卷ID 实际获取卷的性能数据
        return queryVolumeStatistic(params);

        /* String obj_type_id = "1125921381679104";//SYS_Lun  ?SYS_XXXXXX
        params.put("obj_type_id", obj_type_id);
        return queryStatisticByObjType("vmfs", params);*/
    }

    @Override
    public Map<String, Object> queryVmfsStatisticCurrent(Map<String, Object> params) throws Exception {
        Map<String, String> idInstancdIdMap = new HashMap<>();
        List<String> instanceIds = new ArrayList<>();
        Map<String, Object> remap = new HashMap<>();
        remap.put("code", 503);
        remap.put("message", "queryStatistic failed!");
        remap.put("data", "queryStatistic failed!");
        Object indicatorIds = params.get("indicator_ids");
        List<String> ids = (List<String>)params.get("obj_ids");
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
            indicatorIds = initVolumeIndicator();
            params.put("indicator_ids", indicatorIds);
        }
        String obj_type_id = "1125921381679104";//SYS_Lun
        params.put("obj_type_id", obj_type_id);
//        params.put("range", RANGE_LAST_5_MINUTE);
//        params.put("interval", INTERVAL_ONE_MINUTE);
        params.put("range", RANGE_LAST_1_DAY);
        params.put("interval", INTERVAL_MINUTE);
        return queryStatisticByObjType("VMFS volume", params, idInstancdIdMap);
    }

    @Override
    public Map<String, Object> queryNfsStatistic(Map<String, Object> params) throws Exception {
        return queryFsStatistic(params);
    }

    @Override
    public Map<String, Object> queryNfsStatisticCurrent(Map<String, Object> params) throws Exception {
        Map<String, Object> remap = new HashMap<>();
        remap.put("code", 503);
        remap.put("message", "queryStatistic failed!");
        remap.put("data", "queryStatistic failed!");
        Object obj_ids = params.get("obj_ids");
        Object indicatorIds = params.get("indicator_ids");
        if (null == indicatorIds) {
            indicatorIds = initFsIndicator();
            params.put("indicator_ids", indicatorIds);
        }
        String obj_type_id = "1126179079716864";//SYS_StorageFileSystem
        params.put("obj_type_id", obj_type_id);
        params.put("range", RANGE_LAST_1_DAY);
        params.put("interval", INTERVAL_MINUTE);
        Map<String, Object> resp = queryStatisticByObjType("NFS", params, null);
        String code = resp.get("code").toString();
        if ("200".equals(code)) {
            Object data = resp.get("data");
            data = getCurrentStatistic(data);
            remap.put("code", 200);
            remap.put("data", data);
        }
        return remap;
    }

    //查询卷的性能
    @Override
    public Map<String, Object> queryVolumeStatistic(Map<String, Object> params) throws Exception {
        Map<String, String> idInstancdIdMap = new HashMap<>();
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
        String obj_type_id = "1125921381679104";//SYS_Lun
        params.put("obj_type_id", obj_type_id);
        if (null == indicatorIds) {
            indicatorIds = initVolumeIndicator();
            params.put("indicator_ids", indicatorIds);
        }
        return queryStatisticByObjType("volume", params, idInstancdIdMap);
    }

    //查询FS的性能(NFS)
    @Override
    public Map<String, Object> queryFsStatistic(Map<String, Object> params) throws Exception {
        Object indicatorIds = params.get("indicator_ids");
        String obj_type_id = "1125904201809920";//SYS_???  使用存储设备? SYS_StorDevice 1125904201809920
        params.put("obj_type_id", obj_type_id);
        if (null == indicatorIds) {
            indicatorIds = initFsIndicator();
            params.put("indicator_ids", indicatorIds);
        }
        return queryStatisticByObjType("fs", params, null);
    }

    @Override
    public Map<String, Object> queryServiceLevelStatistic(Map<String, Object> params) throws Exception {
        Object indicatorIds = params.get("indicator_ids");
        String obj_type_id = "1126174784749568";//SYS_DjTier
        params.put("obj_type_id", obj_type_id);
        if (null == indicatorIds) {
            indicatorIds = initServiceLevelIndicator();
            params.put("indicator_ids", indicatorIds);
        }
        return queryStatisticByObjType("serviceLevel", params, null);
    }

    @Override
    public Map<String, Object> queryServiceLevelLunStatistic(Map<String, Object> params) throws Exception {
        Map<String, String> idInstanceIdMap = new HashMap<>();
        List<String> instanceIds = new ArrayList<>();
        String obj_type_id = "1125921381679104";//SYS_LUN
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
        params.put("obj_type_id", obj_type_id);
        if (null == indicatorIds) {
            indicatorIds = initServiceLevelLunIndicator();
            params.put("indicator_ids", indicatorIds);
        }
        return queryStatisticByObjType("serviceLevel Lun", params, idInstanceIdMap);
    }

    @Override
    public Map<String, Object> queryServiceLevelStoragePoolStatistic(Map<String, Object> params) throws Exception {
        Map<String, String> idInstanceIdMap = new HashMap<>();
        List<String> instanceIds = new ArrayList<>();
        String obj_type_id = "1125912791744512";//SYS_StoragePool
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
        params.put("obj_type_id", obj_type_id);
        if (null == indicatorIds) {
            indicatorIds = initServiceLevelStoragePoolIndicator();
            params.put("indicator_ids", indicatorIds);
        }
        return queryStatisticByObjType("serviceLevel StroagePool", params, idInstanceIdMap);
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

    //query statistic by objType
    private Map<String, Object> queryStatisticByObjType(String objectType, Map<String, Object> params, Map<String, String> idInstanceIdMap) throws Exception {
        Map<String, Object> resmap = new HashMap<>();
        resmap.put("code", 200);
        resmap.put("message", "query" + objectType + "Statistic success!");
        resmap.put("data", params);

        ResponseEntity responseEntity;
        JsonElement statisticElement;
        //statisticElement中 data 为空 状态码又是200的情况 暂按正常来处理
        //{"status_code":200,"error_code":-60,"error_msg":"ES: Query DB return empty","data":{}}
        //{"status_code":200,"error_code":0,"error_msg":"Successful.","data":{"1282FFE20AA03E4EAC9A814C687B780A":{....}}
        try {
            responseEntity = queryStatistic(params);
            if (null != responseEntity && 200 == responseEntity.getStatusCodeValue()) {
                Object body = responseEntity.getBody();
                String bodyStr = body.toString();
                //将性能数据结果中的instanceId转换为参数传递的id
                if (null != idInstanceIdMap && idInstanceIdMap.size() > 0) {
                    for (Map.Entry<String, String> entry : idInstanceIdMap.entrySet()) {
                        String id = entry.getKey();
                        String instanceId = entry.getValue();
                        if (!StringUtils.isEmpty(instanceId) && !StringUtils.isEmpty(id)) {
                            bodyStr = bodyStr.replace(instanceId, id);
                        }
                    }
                }
                JsonObject bodyJson = new JsonParser().parse(bodyStr).getAsJsonObject();
                statisticElement = bodyJson.get("data");
                Map<String, Object> objectMap = convertMap(statisticElement);
                resmap.put("data", objectMap);
                if (null == objectMap || objectMap.size() == 0) {
                    resmap.put("code", 503);
                    resmap.put("message", objectType + " Statistic error:" + bodyJson.get("error_msg").getAsString());
                    log.error("query" + objectType + "Statistic error:", bodyJson.get("error_msg").getAsString());
                }
            } else {
                resmap.put("code", 503);
                resmap.put("message", "query" + objectType + "Statistic error!");
                log.error("query" + objectType + "Statistic error,the params is:{}", gson.toJson(params));
            }
        } catch (Exception e) {
            resmap.put("code", 503);
            resmap.put("message", "query" + objectType + "Statistic exception!");
            log.error("query" + objectType + "Statistic exception.", e);
        }
        return resmap;
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


        //参数预处理 效验赋值 处理资源对象类型指标 指标id name转换等 目前要求按API格式传参,暂不效验
        //parseParams(); ......

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
                String resource_category = dataJson.get("resource_category").getAsString();
                objtypeIdNampMap.put(objId, resource_category);
                objtypeNameIdMap.put(resource_category, objId);
            }
        }
    }

    // query obj_type indicators
    private void queryIndicatorsOfObjetype(Map<String, Object> params) throws Exception {
        String apiUrl = OBJ_TYPE_INDICATORS_QUERY;
        String objtypeId = params.get("obj_type_id").toString();
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

    //vmfs当前性能数据
    private JsonObject vmfsStatisticCurrentMimic(List<String> volumeIds) {
        JsonObject dataObject = new JsonObject();
        int counterValue_iops = 80;//IOPS指标名称
        int counterValue_bandwidth = 1000;//带宽
        int counterValue_readResponseTime = 10;//读响应时间
        int counterValue_writeResponseTime = 20;//写响应时间

        for (String volumeId : volumeIds) {
            JsonObject statisticObject = new JsonObject();
            statisticObject.addProperty(DmeIndicatorConstants.COUNTER_ID_VMFS_THROUGHPUT, String.valueOf(counterValue_iops++));
            statisticObject.addProperty(DmeIndicatorConstants.COUNTER_ID_VMFS_BANDWIDTH, String.valueOf(counterValue_bandwidth++));
            statisticObject.addProperty(DmeIndicatorConstants.COUNTER_ID_VMFS_READRESPONSETIME, String.valueOf(counterValue_readResponseTime++));
            statisticObject.addProperty(DmeIndicatorConstants.COUNTER_ID_VMFS_WRITERESPONSETIME, String.valueOf(counterValue_writeResponseTime++));
            dataObject.add(volumeId, statisticObject);
        }
        return dataObject;
    }

    //性能查询条件
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
        //时间范围为时间段 而未设置具体时间 则默认一年
        if (RANG_BEGIN_END_TIME.equals(rang)) {
            if (0 == endTime) {
                endTime = System.currentTimeMillis();
                params.put("end_time", endTime);
            }
            if (0 == beginTime) {
                beginTime = endTime - 365 * 24 * 60 * 60 * 1000;
                params.put("begin_time", beginTime);
            }

        }
        if (StringUtils.isEmpty(interval)) {
            switch (rang) {
                case RANGE_LAST_5_MINUTE:
                    interval = INTERVAL_ONE_MINUTE;
                    break;
                case RANGE_LAST_1_HOUR:
                    interval = INTERVAL_MINUTE;
                    break;
                case RANGE_LAST_1_DAY:
                    interval = INTERVAL_MINUTE;
                    break;
                case RANGE_LAST_1_WEEK:
                    interval = INTERVAL_HALF_HOUR;
                    break;
                case RANGE_LAST_1_MONTH:
                    interval = INTERVAL_HOUR;
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
            }
            params.put("interval", interval);
        }
        return params;
    }

    //nfs的默认指标集合 目前取的DME存储设备的指标
    private List<String> initFsIndicator() {
        List<String> indicators = new ArrayList<>();
        indicators.add(DmeIndicatorConstants.COUNTER_ID_FS_READTHROUGHPUT);//readThroughput
        indicators.add(DmeIndicatorConstants.COUNTER_ID_FS_WRITETHROUGHPUT);//writeThroughput
        indicators.add(DmeIndicatorConstants.COUNTER_ID_FS_READBANDWIDTH);//readBandwidth
        indicators.add(DmeIndicatorConstants.COUNTER_ID_FS_WRITEBANDWIDTH);//writeBandwidth
        indicators.add(DmeIndicatorConstants.COUNTER_ID_FS_RESPONSETIME);//responseTime 平均IO响应时间
        return indicators;
    }

    //volume默认指标集合
    private List<String> initVolumeIndicator() {
        List<String> indicators = new ArrayList<>();
        indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_READTHROUGHPUT);//readThroughput 读IOPS
        indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_WRITETHROUGHPUT);//writeThroughput
        indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_READBANDWIDTH);//readBandwidth
        indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_WRITEBANDWIDTH);//writeBandwidth
        indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_READRESPONSETIME);//readResponseTime
        indicators.add(DmeIndicatorConstants.COUNTER_ID_VMFS_WRITERESPONSETIME);//writeResponseTime
        return indicators;
    }

    //serviceLevel默认指标集合
    private List<String> initServiceLevelIndicator() {
        List<String> indicators = new ArrayList<>();
        indicators.add(DmeIndicatorConstants.COUNTER_ID_SERVICELECVEL_MAXRESPONSETIME);//maxResponseTime
        indicators.add(DmeIndicatorConstants.COUNTER_ID_SERVICELEVEL_BANDWIDTHTIB);//bandwidthTiB
        indicators.add(DmeIndicatorConstants.COUNTER_ID_SERVICELEVEL_THROUGHPUTTIB);//throughputTiB
        return indicators;
    }

    //serivceLevelLun默认指标集合
    private List<String> initServiceLevelLunIndicator() {
        List<String> indicators = new ArrayList<>();
        indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_RESPONSETIME);//ResponseTime
        indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_BANDWIDTH);//bandwidthTiB
        indicators.add(DmeIndicatorConstants.COUNTER_ID_VOLUME_THROUGHPUT);//throughput
        return indicators;
    }

    //serviceLevelStoragepool 默认指标集合
    private List<String> initServiceLevelStoragePoolIndicator() {
        List<String> indicators = new ArrayList<>();
        indicators.add(DmeIndicatorConstants.COUNTER_ID_STORAGEPOOL_THROUGHPUT);//IOPS
        return indicators;
    }

    //消息转换  提取实时性能数据
    private JsonObject getCurrentStatistic(Object object) {
        JsonObject data = new JsonObject();
        JsonObject dataJson = new JsonParser().parse(object.toString()).getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> volumeSet = dataJson.getAsJsonObject().entrySet();
        for (Map.Entry<String, JsonElement> volume : volumeSet) {
            JsonObject countRes = new JsonObject();
            String volume_id = volume.getKey();
            JsonObject counterObj = volume.getValue().getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> counterSet = counterObj.getAsJsonObject().entrySet();
            for (Map.Entry<String, JsonElement> countere : counterSet) {
                String counter_id = countere.getKey();
                JsonObject counterjson = countere.getValue().getAsJsonObject();
                JsonArray series = counterjson.getAsJsonArray("series");
                for (JsonElement elment : series) {
                    JsonObject serieJson = elment.getAsJsonObject();
                    Set<Map.Entry<String, JsonElement>> serieJsonSet = serieJson.getAsJsonObject().entrySet();
                    for (Map.Entry<String, JsonElement> serie : serieJsonSet) {
                        String value = serie.getValue().getAsString();
                        countRes.addProperty(counter_id, value);
                        break;
                    }
                    break;
                }
            }
            data.add(volume_id, countRes);
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
                //Map<String,Object> objectValueMap = new HashMap<>();
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
}
