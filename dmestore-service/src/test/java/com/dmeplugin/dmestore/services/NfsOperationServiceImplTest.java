package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.dao.DmeVmwareRalationDao;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

/**
 * @author lianq
 * @className NfsOperationServiceImplTest
 * @description TODO
 * @date 2020/11/13 16:36
 */
public class NfsOperationServiceImplTest {

    @Mock
    DmeAccessService dmeAccessService;
    @Mock
    TaskService taskService;
    @Mock
    DmeStorageService dmeStorageService;
    Gson gson = new Gson();
    @Mock
    VCSDKUtils vcsdkUtils;
    @Mock
    DmeVmwareRalationDao dmeVmwareRalationDao;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
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