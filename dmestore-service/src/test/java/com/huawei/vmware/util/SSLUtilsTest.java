package com.huawei.vmware.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.security.NoSuchAlgorithmException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import javax.net.ssl.SSLContext;

/**
 * @author lianq
 * @className SSLUtilsTest
 * @description TODO
 * @date 2020/12/2 10:37
 */
public class SSLUtilsTest {

    @InjectMocks
    SSLUtils sslUtils;
    SSLContext sslContext;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        sslContext = SSLContext.getDefault();
    }

    @Test
    public void getSupportedProtocols() {
        String[] strings = {"SSLv3"};
        sslUtils.getSupportedProtocols(strings);
    }

    @Test
    public void getSupportedCiphers() throws NoSuchAlgorithmException {
//        SSLSocketFactory socketFactory = new SecureSSLSocketFactory(sslContext);
//        SSLSocketFactory socketFactory1 = sslContext.getSocketFactory();
//        when(sslContext.getSocketFactory()).thenReturn(socketFactory);
//        String[] availableCiphers = spy(String[].class);
//        when(socketFactory.getSupportedCipherSuites()).thenReturn(availableCiphers);
        sslUtils.getSupportedCiphers();
    }

    @Test
    public void getSslContext() throws NoSuchAlgorithmException {
        sslUtils.getSslContext();
    }
}