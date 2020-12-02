package com.dmeplugin.dmestore.utils;

import static org.junit.Assert.*;

import com.dmeplugin.dmestore.exception.DMEException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className RestUtilsTest
 * @description TODO
 * @date 2020/12/1 20:17
 */
public class RestUtilsTest {

    @InjectMocks
    RestUtils restUtils = new RestUtils();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getRestTemplate() throws DMEException {
        restUtils.getRestTemplate();
    }
}