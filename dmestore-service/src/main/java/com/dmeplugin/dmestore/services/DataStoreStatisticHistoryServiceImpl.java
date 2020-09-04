package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.utils.RestUtils;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @Description: TODO
 * @ClassName: DataStoreStatisticHistoryServiceImpl
 * @Company: GH-CA
 * @author: liuxh
 * @create: 2020-09-03
 **/
public class DataStoreStatisticHistoryServiceImpl implements DataStoreStatisticHistroyService {
    private static final Logger log = LoggerFactory.getLogger(DataStoreStatisticHistroyService.class);
    @Autowired
    private Gson gson;
    private static String dmeHostUrl;

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

        String hostUrl = "https://" + params.get("hostIp") + ":" + params.get("hostPort");
        dmeHostUrl = hostUrl;

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

    //query statistic
    private ResponseEntity queryStatistic(Map<String, Object> params) throws Exception {
        ResponseEntity responseEntity;
        String apiUrl = "/rest/metrics/v1/data-svc/history-data/action/query";
        String objTypeId = params.get("objTypeId").toString();
        Object indicatorIds = params.get("indicatorIds");
        Object objIds = params.get("obj_ids");
        String interval = params.get("interval").toString();
        String range = params.get("range").toString();
        String beginTime = params.get("beginTiem").toString();
        String endTime = params.get("endTime").toString();

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

        responseEntity = getApi(apiUrl, HttpMethod.POST, requestbody.toString());

        return responseEntity;
    }

    // query obj_types
    private void queryObjtypes() throws Exception {
        String apiUrl = "/rest/metrics/v1/mgr-svc/obj-types";
        ResponseEntity responseEntity = getApi(apiUrl, HttpMethod.GET, null);
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
        String apiUrl = "/rest/metrics/v1/mgr-svc/obj-types/{obj-type-id}/indicators";
        String objtypeId = params.get("obj_type_id").toString();
        apiUrl = apiUrl.replace("{obj-type-id}", objtypeId);
        if (apiUrl.indexOf("{obj-type-id}") > 0) {
            log.error("DataStoreStatistic query,the url is error, required \"obj-type-id\"!{}", apiUrl);
            return;
        }
        ResponseEntity responseEntity = getApi(apiUrl, HttpMethod.GET, null);

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
        String apiUrl = "/rest/metrics/v1/mgr-svc/indicators";
        ResponseEntity responseEntity = getApi(apiUrl, HttpMethod.POST, null);

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

    private ResponseEntity getApi(String url, HttpMethod method, String requestBody) throws Exception {
        ResponseEntity responseEntity = null;

        RestUtils restUtils = new RestUtils();
        RestTemplate restTemplate = restUtils.getRestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        responseEntity = restTemplate.exchange(dmeHostUrl + url, method, entity, String.class);
        log.info("servicelevel url:{},response:{}", url, gson.toJson(responseEntity));
        return responseEntity;
    }
}
