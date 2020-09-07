package com.dmeplugin.dmestore.services;

import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

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
    @Autowired
    private Gson gson;
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
    public Map<String, Object> queryVmfsStatisticCurrent(Map<String, Object> params) {
        Map<String, Object> remap = new HashMap<>();

        //以下为模拟响应报文的处理
        Object obj_ids = params.get("obj_ids");
        if (null != obj_ids) {
            List<String> volumeIds = (List<String>) obj_ids;
            JsonObject dataJson = vmfsStatisticCurrentMimic(volumeIds);
            remap.put("code", 200);
            remap.put("message", "queryStatistic success!");
            remap.put("data", dataJson);
        }

        //以下为实际消息的处理


        return remap;
    }

    //query statistic
    private ResponseEntity queryStatistic(Map<String, Object> params) throws Exception {
        ResponseEntity responseEntity;
        String objTypeId = params.get("obj_type_id").toString();
        Object indicatorIds = params.get("indicator_ids");
        Object objIds = params.get("obj_ids");
        String interval = params.get("interval").toString();
        String range = params.get("range").toString();
        String beginTime = params.get("begin_time").toString();
        String endTime = params.get("end_time").toString();

        //参数预处理 效验赋值 处理资源对象类型指标 指标id name转换等
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
}
