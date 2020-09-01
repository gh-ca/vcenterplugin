package com.dmeplugin.dmestore.utils;


import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

/**
 * Created by yuanqi on 2020/8/31.
 */
public class RestUtils {
    public static void main( String[] args ){
//        SSLContext sslContext = new SSLContextBuilder().
//                loadTrustMaterial(TrustSelfSignedStrategy.INSTANCE).
//                build();
//
//        HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
//
//        // X509HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
//        SSLConnectionSocketFactory systemSocketFactory = new SSLConnectionSocketFactory(
//                sslContext,
//                hostnameVerifier);
//        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(systemSocketFactory).setMaxConnTotal(200).setMaxConnPerRoute(20).build();
//
//        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//
//        requestFactory.setHttpClient(httpClient);
//        requestFactory.setReadTimeout(185000);
        RestTemplate template = new RestTemplate();
    }
}
