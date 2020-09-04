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
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DmeAccessServiceImpl implements DmeAccessService {

    private static final Logger LOG = LoggerFactory.getLogger(DmeAccessServiceImpl.class);

    private DmeInfoDao dmeInfoDao;

    @Autowired
    private Gson gson = new Gson();

    private static String dmeToken;
    private static String dmeHostUrl;
    private static String dmeHostIp;
    private static Integer dmeHostPort;

    private final String LOGIN_DME_URL = "/rest/plat/smapp/v1/sessions";
    private final String REFRES_STATE_URL = "/rest/blockservice/v1/volumes?limit=1";

    @Override
    public Map<String, Object> accessDme(Map<String, Object> params) {
        Map<String, Object> remap = new HashMap<>();
        remap.put("code", 200);
        remap.put("message", "连接成功");
        remap.put("data", params);
        try {
            LOG.info("params==" + (params == null ? "null" : gson.toJson(params)));
            if (params != null) {
                //判断与服务器的连接
                ResponseEntity responseEntity = login(params);
                if (responseEntity.getStatusCodeValue() == 200) {
                    //连接成功后，数据入库
                    try {
                        DmeInfo dmeInfo = new Gson().fromJson(params.toString(), DmeInfo.class);
                        LOG.info("dmeInfo==" + gson.toJson(dmeInfo));
                        int re = dmeInfoDao.addDmeInfo(dmeInfo);
                        LOG.info("re==" + re);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        remap.put("code", 503);
                        remap.put("message", "连接信息保存失败:" + ex.getMessage());
                    }
                } else {
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
        remap.put("message", "更新连接状态成功");
        try {
            //判断与服务器的连接
            ResponseEntity responseEntity = access(REFRES_STATE_URL, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() != 200) {
                remap.put("code", 503);
                remap.put("message", "更新连接状态失败:" + responseEntity.toString());
            }

        } catch (Exception e) {
            remap.put("code", 503);
            remap.put("message", "更新连接状态失败:" + e.getMessage());
        }

        Map<String, Object> params = new HashMap<>();
        params.put("hostIp", dmeHostIp);
        params.put("hostPort", dmeHostPort);
        remap.put("data", params);

        return remap;
    }

    @Override
    public ResponseEntity<String> access(String url, HttpMethod method, String requestBody) throws Exception {
        ResponseEntity<String> responseEntity = null;

        if (dmeToken == null || dmeToken.equals("")) {
            //如果token为空，就自动登录，获取token
            iniLogin();
        }

        RestUtils restUtils = new RestUtils();
        RestTemplate restTemplate = restUtils.getRestTemplate();

        HttpHeaders headers = getHeaders();

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        if (url.indexOf("http") < 0) {
            url = dmeHostUrl + url;
        }
        responseEntity = restTemplate.exchange(url, method, entity, String.class);
        LOG.info(url + "==responseEntity==" + responseEntity);
        if (responseEntity.getStatusCodeValue() == 403 ||
                responseEntity.getStatusCodeValue() == 401) {
            //如果token失败，重新登录
            iniLogin();
            //得到新token后，重新执行上次任务
            responseEntity = restTemplate.exchange(dmeHostUrl + url, method, entity, String.class);
        }
        return responseEntity;
    }

    private ResponseEntity login(Map<String, Object> params) throws Exception {
        ResponseEntity responseEntity = null;
        if (params != null && params.get("hostIp") != null) {
            RestUtils restUtils = new RestUtils();
            RestTemplate restTemplate = restUtils.getRestTemplate();

            HttpHeaders headers = getHeaders();

            Map<String, Object> requestbody = new HashMap<>();
            requestbody.put("grantType", "password");
            requestbody.put("userName", params.get("userName"));
            requestbody.put("value", params.get("password"));
            LOG.info("requestbody=="+gson.toJson(requestbody));

            String hostUrl = "https://" + params.get("hostIp") + ":" + params.get("hostPort");
            LOG.info("hostUrl=="+hostUrl);

            HttpEntity<String> entity = new HttpEntity<>(requestbody.toString(), headers);
            responseEntity = restTemplate.exchange(hostUrl + LOGIN_DME_URL
                    , HttpMethod.PUT, entity, String.class);

            LOG.info("responseEntity==" + responseEntity);
            if (responseEntity.getStatusCodeValue() == 200) {
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                LOG.info("jsonObject==" + jsonObject);
                if (jsonObject != null && jsonObject.get("accessSession") != null) {
                    dmeToken = jsonObject.get("accessSession").getAsString();
                    LOG.info("dmeToken===" + dmeToken);
                    dmeHostUrl = hostUrl;
                    dmeHostIp = params.get("hostIp").toString();
                    dmeHostPort = Integer.parseInt(params.get("hostPort").toString());
                    LOG.info("dmeHostUrl==" + dmeHostUrl + "==dmeHostIp==" + dmeHostIp + "==dmeHostPort==" + dmeHostPort);
                }
            }

        }

        return responseEntity;
    }

    private HttpHeaders getHeaders() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (dmeToken != null && !dmeToken.equals("")) {
            headers.set("X-Auth-Token", dmeToken);
        }
        return headers;
    }

    private void iniLogin() throws Exception {
        //查询数据库
        DmeInfo dmeInfo = dmeInfoDao.getDmeInfo();
        LOG.info("dmeinfo==" + gson.toJson(dmeInfo));
        Map<String, Object> params = new HashMap<>();
        params.put("hostIp", dmeInfo.getHostIp());
        params.put("hostPort", dmeInfo.getHostPort());
        params.put("userName", dmeInfo.getUserName());
        params.put("password", dmeInfo.getPassword());
        LOG.info("params==" + gson.toJson(params));
        //登录
        login(params);
    }

    public void setDmeInfoDao(DmeInfoDao dmeInfoDao) {
        this.dmeInfoDao = dmeInfoDao;
    }
}
