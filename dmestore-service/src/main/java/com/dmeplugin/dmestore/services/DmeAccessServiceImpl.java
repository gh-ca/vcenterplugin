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
            System.out.println("params==" + (params==null?"null":params.toString()));
            if (params != null) {
                //判断与服务器的连接
                ResponseEntity responseEntity = login(params);
                if(responseEntity.getStatusCodeValue()==200) {
                    //连接成功后，数据入库
                    try {
                        Gson tmpgson = new Gson();
                        DmeInfo dmeInfo = new Gson().fromJson(params.toString(), DmeInfo.class);
                        System.out.println("dmeInfo==" + tmpgson.toJson(dmeInfo));
                        int re = dmeInfoDao.addDmeInfo(dmeInfo);
                        System.out.println("re==" + re);
                    } catch (Exception ex) {
                        remap.put("code", 503);
                        remap.put("message", "连接信息保存失败:" + ex.getMessage());
                    }
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

        HttpHeaders headers = getHeaders();

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        responseEntity = restTemplate.exchange(dmeHostUrl+url, method, entity, String.class);
        System.out.println(url+"==responseEntity=="+responseEntity);
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

            HttpHeaders headers = getHeaders();

            Map<String,Object> requestbody = new HashMap<>();
            requestbody.put("grantType","password");
            requestbody.put("userName",params.get("userName"));
            requestbody.put("value",params.get("password"));

            String loginUrl = "/rest/plat/smapp/v1/sessions";
            String hostUrl = "https://"+params.get("hostIp")+":"+params.get("hostPort");

            HttpEntity<String> entity = new HttpEntity<>(requestbody.toString(), headers);
            responseEntity = restTemplate.exchange(hostUrl+loginUrl
                    , HttpMethod.PUT, entity, String.class);

            System.out.println("responseEntity=="+responseEntity);
            if(responseEntity.getStatusCodeValue()==200){
                JsonArray jsonArray = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonArray();
                System.out.println("jsonArray=="+jsonArray);
                JsonObject jsonObject = jsonArray.get(1).getAsJsonObject();
                if(jsonObject!=null && jsonObject.get("accessSession")!=null){
                    dmeToken = jsonObject.get("accessSession").getAsString();
                    System.out.println("dmeToken==="+dmeToken);
                    dmeHostUrl = hostUrl;
                    System.out.println("dmeHostUrl==="+dmeHostUrl);
                }
            }

        }

        return responseEntity;
    }

    private HttpHeaders getHeaders() throws Exception{
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        if(dmeToken!=null && !dmeToken.equals("")) {
            headers.set("X-Auth-Token",dmeToken);
        }
        return headers;
    }

    public void setDmeInfoDao(DmeInfoDao dmeInfoDao) {
        this.dmeInfoDao = dmeInfoDao;
    }
}
