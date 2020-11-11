package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.services.NfsOperationService;
import com.dmeplugin.dmestore.services.NfsOperationServiceImpl;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author lianq
 * @className NfsOperationControllerTest
 * @description TODO
 * @date 2020/11/11 10:38
 */
public class NfsOperationControllerTest {

    private NfsOperationService nfsOperationService;
    private Gson gson=new Gson();

    @Before
    public void setUp() throws Exception {
        nfsOperationService = new NfsOperationServiceImpl();
    }

    @Test
    public void createNfsDatastore() {

    }

    @Test
    public void updateNfsDatastore() {
    }

    @Test
    public void changeNfsCapacity() {
    }

    @Test
    public void getEditNfsStore() {
    }


}