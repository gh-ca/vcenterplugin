package com.dmeplugin.vmware;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;

import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vise.usersession.UserSessionService;
import com.vmware.vise.vim.data.VimObjectReferenceService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lianq
 * @className VCConnectionHelperTest
 * @description TODO
 * @date 2020/12/3 15:29
 */
public class VCConnectionHelperTest {

    @Autowired
    VCConnectionHelper vcConnectionHelper;

    UserSessionService userSessionService;
    VimObjectReferenceService vimObjectReferenceService;
    ManagedObjectReference mor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userSessionService = spy(UserSessionService.class);
        vimObjectReferenceService = spy(VimObjectReferenceService.class);
        mor = spy(ManagedObjectReference.class);
    }

    @Test
    public void getUserSessionService() {
        vcConnectionHelper.getUserSessionService();
    }

    @Test
    public void setUserSessionService() {
        vcConnectionHelper.setUserSessionService(userSessionService);

    }

    @Test
    public void getVimObjectReferenceService() {
        vcConnectionHelper.getVimObjectReferenceService();

    }

    @Test
    public void setVimObjectReferenceService() {
        vcConnectionHelper.setVimObjectReferenceService(vimObjectReferenceService);

    }

    @Test
    public void getServerurl() {
        vcConnectionHelper.getServerurl();

    }

    @Test
    public void setServerurl() {
        vcConnectionHelper.setServerurl("321");

    }

    @Test
    public void getUsername() {
        vcConnectionHelper.getUsername();

    }

    @Test
    public void setUsername() {
        vcConnectionHelper.setUsername("321");

    }

    @Test
    public void getPassword() {
        vcConnectionHelper.getPassword();

    }

    @Test
    public void setPassword() {
        vcConnectionHelper.setPassword("321");

    }

    @Test
    public void getServerport() {
        vcConnectionHelper.getServerport();

    }

    @Test
    public void setServerport() {
        vcConnectionHelper.setServerport(321);

    }

    @Test
    public void getServerContext() throws Exception {
        vcConnectionHelper.getServerContext("321:321321");

    }

    @Test
    public void objectId2Mor() {
        vcConnectionHelper.objectId2Mor("321:321:321:321");

    }

    @Test
    public void objectId2Serverguid() {
        vcConnectionHelper.objectId2Serverguid("321:321:321:321");

    }

    @Test
    public void mor2ObjectId() {
        vcConnectionHelper.mor2ObjectId(mor, "321:213:321:321");

    }
}