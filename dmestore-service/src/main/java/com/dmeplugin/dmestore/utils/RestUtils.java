package com.dmeplugin.dmestore.utils;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.conn.ssl.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * @Description: TODO
 * @ClassName: RestUtils
 * @Company: GH-CA
 * @author: yy
 * @create: 2020-09-02
 **/
public class RestUtils {
    public final static String RESPONSE_STATE_CODE = "code";
    public final static String RESPONSE_STATE_200 = "200";
    public final static int RES_STATE_I_200 = 200;
    public final static int RES_STATE_I_202 = 202;
    public final static int RES_STATE_I_401 = 401;
    public final static int RES_STATE_I_403 = 403;

    public RestTemplate getRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        HostnameVerifier hostnameVerifier = (s, sslSession) -> true;

        //HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
       /* SSLConnectionSocketFactory systemSocketFactory = new SSLConnectionSocketFactory(
                builder.build(),
                hostnameVerifier);*/
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).setMaxConnTotal(200).setMaxConnPerRoute(20).build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);
        requestFactory.setReadTimeout(3000);
        requestFactory.setConnectTimeout(3000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        return restTemplate;
    }

    public static void main( String[] args ) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        RestUtils restUtils = new RestUtils();
        RestTemplate restTemplate = restUtils.getRestTemplate();

        Gson gson = new Gson();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity responseEntity  = restTemplate.exchange("https://10.143.132.231:20886/vsrespoolcommon/getrestypelist"
                , HttpMethod.POST, entity, String.class);
        System.out.println(gson.toJson(responseEntity));
        System.out.println(responseEntity.toString());
        System.out.println("re==="+responseEntity.getStatusCode());
        System.out.println("re==="+responseEntity.getStatusCodeValue());
        System.out.println("re==="+responseEntity.getBody());
        JsonArray jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonArray();
        System.out.println("re==="+jsonObject);
    }
}
