package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.ServiceLevelInfo;
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
 * @ClassName: ServiceLevelServiceImpl
 * @Company: GH-CA
 * @author: liuxh
 * @create: 2020-09-02
 **/
public class ServiceLevelServiceImpl implements ServiceLevelService {
    private static final Logger log = LoggerFactory.getLogger(ServiceLevelServiceImpl.class);

    @Autowired
    private Gson gson;
    private static String dmeHostUrl;

    @Override
    public Map<String, Object> listServiceLevel(Map<String, Object> params) {
        Map<String, Object> remap = new HashMap<>();
        remap.put("code", 200);
        remap.put("message", "list serviceLevel success!");
        remap.put("data", params);

        String apiUrl = "/rest/service-policy/v1/service-levels";
        String hostUrl = "https://" + params.get("hostIp") + ":" + params.get("hostPort");
        dmeHostUrl = hostUrl;

        ResponseEntity responseEntity;
        List<ServiceLevelInfo> slis;
        try {
            responseEntity = listServiceLevel(apiUrl, HttpMethod.GET);
            int code = responseEntity.getStatusCodeValue();
            if (200 != code) {
                remap.put("code", 503);
                remap.put("message", "list serviceLevel error!");
                return remap;
            }
            Object object = responseEntity.getBody();
            slis = convertBean(object);
            remap.put("data", slis);
        } catch (Exception e) {
            log.error("list serviceLevel error", e);
            String message = e.getMessage();
            remap.put("code", 503);
            remap.put("message", message);
            return remap;
        }
        return remap;
    }

    private ResponseEntity listServiceLevel(String url, HttpMethod method) throws Exception {
        ResponseEntity responseEntity = null;

        RestUtils restUtils = new RestUtils();
        RestTemplate restTemplate = restUtils.getRestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        responseEntity = restTemplate.exchange(dmeHostUrl + url, method, entity, String.class);
        log.info("servicelevel url:{},response:{}", url, gson.toJson(responseEntity));
        if (403 == responseEntity.getStatusCodeValue() || 401 == responseEntity.getStatusCodeValue()) {
            //认证失效,调用登陆接口重新认证,重新执行调用接口
            //Map<String, Object> params = new HashMap<>();
            //login(params);
            responseEntity = restTemplate.exchange(dmeHostUrl + url, method, entity, String.class);
        }
        return responseEntity;
    }

    // convert the api responseBody to ServiceLevelInfo Bean list
    private List<ServiceLevelInfo> convertBean(Object object) {
        List<ServiceLevelInfo> slis = new ArrayList<>();

        JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
        JsonArray jsonArray = jsonObject.get("service-levels").getAsJsonArray();
        for(JsonElement jsonElement : jsonArray){
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
            } catch (Exception e){
                log.warn("servicelevel convert error:",e);
            }
        }
        return slis;
    }

    /*public static void main(String[] args) {
        String str = "{\"service-levels\":[{\"id\": \"UUID\",\"name\": \"service-level_block\",\"description\": \"block service-level for dj\",\"type\":\"BLOCK\",\"protocol\": \"FC\",\"total_capacity\": 200,\"free_capacity\": 200,\"used_capacity\":100,\"capabilities\": {\"resource_type\": \"thin\",\"compression\": true,\"deduplication\": true,\"smarttier\": {\"policy\": \"1\",\"enabled\": true},\"qos\": {\"enabled\": true,\"qos_param\":{\"latency\":\"10\",\"latencyUnit\": \"ms\",\"minBandWidth\": 1000,\"minIOPS\": 1000}}}}]}";
        ServiceLevelServiceImpl sls = new ServiceLevelServiceImpl();
        sls.convertBean(str);
    }*/
}
