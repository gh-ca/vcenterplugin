package com.dmeplugin.dmestore.services;


import com.dmeplugin.dmestore.dao.DmeInfoDao;
import com.dmeplugin.dmestore.entity.DmeInfo;
import com.dmeplugin.dmestore.utils.RestUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DmeAccessServiceImpl implements DmeAccessService {

    private static final Logger LOG = LoggerFactory.getLogger(DmeAccessServiceImpl.class);

    private DmeInfoDao dmeInfoDao;

    @Autowired
    private Gson gson;

    private static String dmeToken;
    private static String dmeHostUrl;

    @Override
    public Map<String, Object> accessDme(Map<String, Object> params) {
        Map<String, Object> remap = new HashMap<>();
        remap.put("code", 200);
        remap.put("message", "连接成功");
        remap.put("data", params);
        try {
            LOG.info("params==" + (params==null?"null":gson.toJson(params)));
            if (params != null) {
                //判断与服务器的连接
                ResponseEntity responseEntity = login(params);
                if(responseEntity.getStatusCodeValue()==200) {
                    //连接成功后，数据入库
                    DmeInfo dmeInfo = new Gson().fromJson(gson.toJson(params), DmeInfo.class);
                    LOG.info("dmeInfo==" + gson.toJson(dmeInfo));
                    int re = dmeInfoDao.addDmeInfo(dmeInfo);
                    LOG.info("re==" + re);
                }else{
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
        Map<String, Object> remap = new HashMap<>();
        remap.put("code", 200);
        remap.put("message", "连接成功");
//    remap.put("data", params);
        return remap;
    }

    @Override
    public ResponseEntity access(String url, HttpMethod method, String requestBody) throws Exception{
        ResponseEntity responseEntity = null;

        RestUtils restUtils = new RestUtils();
        RestTemplate restTemplate = restUtils.getRestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        responseEntity = restTemplate.exchange(dmeHostUrl+url, method, entity, String.class);
        LOG.info(url+"==responseEntity=="+responseEntity);
        if(responseEntity.getStatusCodeValue()==403 ||
                responseEntity.getStatusCodeValue()==401 ){
            //查询数据库
            //
            Map<String, Object> params = new HashMap<>();
            login(params);
            responseEntity = restTemplate.exchange(dmeHostUrl+url, method, entity, String.class);
        }
        return responseEntity;
    }

    private ResponseEntity login(Map<String, Object> params) throws Exception{
        ResponseEntity responseEntity = null;
        if(params!=null && params.get("hostIp")!=null) {
            RestUtils restUtils = new RestUtils();
            RestTemplate restTemplate = restUtils.getRestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);

            JsonObject requestbody = new JsonObject();
            requestbody.addProperty("grantType","password");
            requestbody.addProperty("userName",params.get("userName").toString());
            requestbody.addProperty("value",params.get("password").toString());

            String loginUrl = "/rest/plat/smapp/v1/sessions";
            String hostUrl = "https://"+params.get("hostIp")+":"+params.get("hostPort");

            HttpEntity<String> entity = new HttpEntity<>(requestbody.getAsString(), headers);
            responseEntity = restTemplate.exchange(hostUrl+loginUrl
                    , HttpMethod.PUT, entity, String.class);
            LOG.info("responseEntity=="+responseEntity);
            if(responseEntity.getStatusCodeValue()==200){
                JsonArray jsonArray = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonArray();
                LOG.info("jsonArray=="+jsonArray);
                JsonObject jsonObject = jsonArray.get(1).getAsJsonObject();
                if(jsonObject!=null && jsonObject.get("accessSession")!=null){
                    dmeToken = jsonObject.get("accessSession").getAsString();
                    LOG.info("dmeToken==="+dmeToken);
                    dmeHostUrl = hostUrl;
                    LOG.info("dmeHostUrl==="+dmeHostUrl);
                }
            }

        }

        return responseEntity;
    }

    public void setDmeInfoDao(DmeInfoDao dmeInfoDao) {
        this.dmeInfoDao = dmeInfoDao;
    }
}
