package com.dmeplugin.dmestore.exception;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className DMEExceptionTest
 * @description TODO
 * @date 2020/12/3 10:12
 */
public class DMEExceptionTest {

    @InjectMocks
    DMEException dmeException = new DMEException();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        dmeException = new DMEException("321", "321");
    }

    @Test
    public void getMessage() {
        dmeException.getMessage();
    }

    @Test
    public void getCode() {
        dmeException.getCode();

    }

    @Test
    public void setCode() {
        dmeException.setCode("321");

    }

    @Test
    public void setMessage() {
        dmeException = new DMEException("321");
        dmeException.setMessage("321");

    }
}