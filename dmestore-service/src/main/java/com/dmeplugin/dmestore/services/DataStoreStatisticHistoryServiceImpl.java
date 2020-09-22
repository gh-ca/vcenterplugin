package com.dmeplugin.dmestore.services;

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

    private final String STATISTIC_QUERY = "/rest/metrics/v1/data-svc/history-data/action/query";
    private final String OBJ_TYPES_LIST = "/rest/metrics/v1/mgr-svc/obj-types";
    private final String INDICATORS_LIST = "/rest/metrics/v1/mgr-svc/indicators";
    private final String OBJ_TYPE_INDICATORS_QUERY = "/rest/metrics/v1/mgr-svc/obj-types/{obj-type-id}/indicators";

    public static final String COUNTER_NAME_IOPS = "throughput";//IOPS指标名称
    public static final String COUNTER_NAME_BANDWIDTH = "bandwidth";//带宽
    public static final String COUNTER_NAME_READPESPONSETIME = "readResponseTime";//读响应时间
    public static final String COUNTER_NAME_WRITERESPONSETIME = "writeResponseTime";//写响应时间

    //性能指标 id和name的映射关系
    private static Map<String, String> indicatorNameIdMap = new HashMap<>();
    private static Map<String, String> indicatorIdNameMap = new HashMap<>();

    //资源对象类型 id和name的映射关系
    private static Map<String, String> objtypeIdNampMap = new HashMap<>();
    private static Map<String, String> objtypeNameIdMap = new HashMap<>();

    //资源对象类型支持指标 对象类型id和指标id集合关系
    private static Map<String, List<String>> objTypeCountersMap = new HashMap<>();

    @Override
    public Map<String, Object> queryVmfsStatistic(Map<String, Object> params) {
        Map<String, Object> remap = new HashMap<>();
        remap.put("code", 200);
        remap.put("message", "queryStatistic success!");
        remap.put("data", params);

        ResponseEntity responseEntity;
        Object statisticObj;
        try {
            responseEntity = queryStatistic(params);
            if (null != responseEntity && 200 == responseEntity.getStatusCodeValue()) {
                Object body = responseEntity.getBody();
                JsonObject bodyJson = new JsonParser().parse(body.toString()).getAsJsonObject();
                statisticObj = bodyJson.get("data").getAsJsonObject();
                remap.put("data", statisticObj);
            } else {
                remap.put("code", 503);
                remap.put("message", "queryStatistic error!");
                log.error("queryStatistic error,the params is:{}", gson.toJson(params));
            }
        } catch (Exception e) {
            remap.put("code", 503);
            remap.put("message", "queryStatistic exception!");
            log.error("queryStatistic exception.", e);
        }
        return remap;
    }

    @Override
    public Map<String, Object> queryVmfsStatisticCurrent(Map<String, Object> params) throws Exception {
        Map<String, Object> remap = new HashMap<>();
        Object obj_ids = params.get("obj_ids");
        Object indicatorIds = params.get("indicator_ids");
        //以下为模拟响应报文的处理
        /*if (null != obj_ids) {
            List<String> volumeIds = (List<String>) obj_ids;
            JsonObject dataJson = vmfsStatisticCurrentMimic(volumeIds);
            remap.put("code", 200);
            remap.put("message", "queryStatistic success!");
            remap.put("data", dataJson);
        }*/

        //以下为实际消息的处理
        if (null == indicatorIds) {
            indicatorIds = initVolumeIndicator();
            params.put("indicator_ids", indicatorIds);
        }
        params.put("range", RANGE_LAST_5_MINUTE);
        params.put("interval", INTERVAL_ONE_MINUTE);
        Map<String, Object> resp = queryStatisticByObjType("VMFS volume", params);
        String code = resp.get("code").toString();
        if ("200".equals(code)) {
            Object data = remap.get("data");
            data = getCurrentStatistic(data);
            remap.put("data", data);
        }
        return remap;
    }

    @Override
    public Map<String, Object> queryNfsStatistic(Map<String, Object> params) throws Exception {
        return queryFsStatistic(params);
    }

    @Override
    public Map<String, Object> queryNfsStatisticCurrent(Map<String, Object> params) throws Exception {
        Map<String, Object> remap = new HashMap<>();

        Object obj_ids = params.get("obj_ids");
        Object indicatorIds = params.get("indicator_ids");
        //以下为模拟响应报文的处理
       /* if (null != obj_ids) {
            List<String> volumeIds = (List<String>) obj_ids;
            JsonObject dataJson = vmfsStatisticCurrentMimic(volumeIds);
            remap.put("code", 200);
            remap.put("message", "queryStatistic fs success!");
            remap.put("data", dataJson);
        }*/

        //以下为实际响应报文处理
        if (null == indicatorIds) {
            indicatorIds = initFsIndicator();
            params.put("indicator_ids", indicatorIds);
        }
        params.put("range", RANGE_LAST_5_MINUTE);
        params.put("interval", INTERVAL_ONE_MINUTE);
        Map<String, Object> resp = queryStatisticByObjType("NFS", params);
        String code = resp.get("code").toString();
        if ("200".equals(code)) {
            Object data = remap.get("data");
            data = getCurrentStatistic(data);
            remap.put("data", data);
        }
        return remap;
    }

    //查询卷的性能
    @Override
    public Map<String, Object> queryVolumeStatistic(Map<String, Object> params) throws Exception {
        String obj_type_id = "1125921381679104";//SYS_Lun
        params.put("obj_type_id", obj_type_id);
        return queryStatisticByObjType("volume", params);
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
        return queryStatisticByObjType("fs", params);
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
        return queryStatisticByObjType("serviceLevel", params);
    }

    //query statistic by objType
    private Map<String, Object> queryStatisticByObjType(String objectType, Map<String, Object> params) throws Exception {
        Map<String, Object> resmap = new HashMap<>();
        resmap.put("code", 200);
        resmap.put("message", "query" + objectType + "Statistic success!");
        resmap.put("data", params);

        ResponseEntity responseEntity;
        Object statisticObj;
        try {
            responseEntity = queryStatistic(params);
            if (null != responseEntity && 200 == responseEntity.getStatusCodeValue()) {
                Object body = responseEntity.getBody();
                JsonObject bodyJson = new JsonParser().parse(body.toString()).getAsJsonObject();
                statisticObj = bodyJson.get("data").getAsJsonObject();
                resmap.put("data", statisticObj);
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
        String beginTime = params.get("begin_time").toString();
        String endTime = params.get("end_time").toString();

        //参数预处理 效验赋值 处理资源对象类型指标 指标id name转换等 目前要求按API格式传参,暂不效验
        //parseParams(); ......

        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("obj_type_id", objTypeId);
        requestbody.put("indicator_ids", indicatorIds);
        requestbody.put("obj_ids", objIds);
        requestbody.put("interval", interval);
        requestbody.put("range", range);
        requestbody.put("begin_time", beginTime);
        requestbody.put("end_time", endTime);
        responseEntity = dmeAccessService.access(STATISTIC_QUERY, HttpMethod.POST, requestbody.toString());

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
            statisticObject.addProperty(COUNTER_NAME_IOPS, String.valueOf(counterValue_iops++));
            statisticObject.addProperty(COUNTER_NAME_BANDWIDTH, String.valueOf(counterValue_bandwidth++));
            statisticObject.addProperty(COUNTER_NAME_READPESPONSETIME, String.valueOf(counterValue_readResponseTime++));
            statisticObject.addProperty(COUNTER_NAME_WRITERESPONSETIME, String.valueOf(counterValue_writeResponseTime++));
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
        if (0 == endTime) {
            endTime = System.currentTimeMillis();
            params.put("end_time", endTime);
        }
        if (0 == beginTime) {
            switch (rang) {
                case RANGE_LAST_5_MINUTE:
                    beginTime = endTime - 5 * 60 * 1000;
                    break;
                case RANGE_LAST_1_HOUR:
                    beginTime = endTime - 60 * 60 * 1000;
                    break;
                case RANGE_LAST_1_DAY:
                    beginTime = endTime - 24 * 60 * 60 * 1000;
                    break;
                case RANGE_LAST_1_WEEK:
                    beginTime = endTime - 7 * 24 * 60 * 60 * 1000;
                    break;
                case RANGE_LAST_1_MONTH:
                    beginTime = endTime - 30 * 24 * 60 * 60 * 1000;
                    break;
                case RANGE_LAST_1_QUARTER:
                    beginTime = endTime - 3 * 30 * 24 * 60 * 60 * 1000;
                    break;
                case RANGE_HALF_1_YEAR:
                    beginTime = endTime - 6 * 30 * 24 * 60 * 60 * 1000;
                    break;
                case RANGE_LAST_1_YEAR:
                    beginTime = endTime - 365 * 24 * 60 * 60 * 1000;
                    break;
            }
            params.put("begin_time", beginTime);
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
        indicators.add("1125904201875461");//readThroughput
        indicators.add("1125904201875462");//writeThroughput
        indicators.add("1125904201875459");//readBandwidth
        indicators.add("1125904201875460");//writeBandwidth
        indicators.add("1125904201875464");//responseTime 平均IO响应时间
        return indicators;
    }

    //volume默认指标集合
    private List<String> initVolumeIndicator() {
        List<String> indicators = new ArrayList<>();
        indicators.add("1125921381744648");//readThroughput 读IOPS
        indicators.add("1125921381744649");//writeThroughput
        indicators.add("1125921381744646");//readBandwidth
        indicators.add("1125921381744647");//writeBandwidth
        indicators.add("1125921381744656");//readResponseTime
        indicators.add("1125921381744656");//writeResponseTime
        return indicators;
    }

    //serviceLevel默认指标集合
    private List<String> initServiceLevelIndicator() {
        List<String> indicators = new ArrayList<>();
        indicators.add("1126174784815111");//maxResponseTime
        indicators.add("1126174784815118");//bandwidthTiB
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
}
