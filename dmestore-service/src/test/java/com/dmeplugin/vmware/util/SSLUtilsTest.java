package com.dmeplugin.vmware.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.security.NoSuchAlgorithmException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * @author lianq
 * @className SSLUtilsTest
 * @description TODO
 * @date 2020/12/2 10:37
 */
public class SSLUtilsTest {

    @InjectMocks
    SSLUtils sslUtils = new SSLUtils();
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