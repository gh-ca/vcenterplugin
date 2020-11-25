package com.dmeplugin.dmestore.utils;

import com.dmeplugin.dmestore.exception.DMEException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.conn.ssl.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import java.io.IOException;
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
@Component
public class RestUtils {
    public final static String RESPONSE_STATE_CODE = "code";

    public final static String RESPONSE_STATE_200 = "200";

    public final static int RES_STATE_I_200 = 200;

    public final static int RES_STATE_I_202 = 202;

    public final static int RES_STATE_I_401 = 401;

    public final static int RES_STATE_I_403 = 403;

    @Bean
    public RestTemplate getRestTemplate() throws DMEException {
        RestTemplate restTemplate;
        try {
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
            HostnameVerifier hostnameVerifier = (s, sslSession) -> true;
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext,
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .setMaxConnTotal(200)
                .setMaxConnPerRoute(20)
                .build();
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);
            requestFactory.setReadTimeout(10000);
            requestFactory.setConnectTimeout(3000);
            restTemplate = new RestTemplate(requestFactory);
            restTemplate.setErrorHandler(new CustomErrorHandler());

        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
            throw new DMEException("初始化resttemplate错误");
        }
        return restTemplate;
    }

    class CustomErrorHandler implements ResponseErrorHandler {

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return true;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {

        }
    }
}
