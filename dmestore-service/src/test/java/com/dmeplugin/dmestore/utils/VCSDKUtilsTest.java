package com.dmeplugin.dmestore.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.dmeplugin.dmestore.exception.VcenterException;
import com.dmeplugin.vmware.VCConnectionHelper;
import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim.vmomi.core.types.VmodlContext;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className VCSDKUtilsTest
 * @description TODO
 * @date 2020/11/25 15:51
 */
public class VCSDKUtilsTest {

    @Mock
    private VCConnectionHelper vcConnectionHelper;

    @InjectMocks
    VCSDKUtils vcsdkUtils = new VCSDKUtils();;
    VmwareContext vmwareContext;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        vmwareContext = mock(VmwareContext.class);
    }

    @Test
    public void getAllVmfsDataStoreInfos() throws Exception {
        VmwareContext[] vmodlContexts = {vmwareContext};
        when(vcConnectionHelper.getAllContext()).thenReturn(vmodlContexts);
        vcsdkUtils.getAllVmfsDataStoreInfos("321");
    }

    @Test
    public void getDataStoreSummaryByObjectId() {
    }

    @Test
    public void getAllHosts() {
    }

    @Test
    public void findHostById() {
    }

    @Test
    public void getAllClusters() {
    }

    @Test
    public void getHostsByDsObjectId() {
    }

    @Test
    public void getMountHostsByDsObjectId() {
    }

    @Test
    public void getClustersByDsObjectId() {
    }

    @Test
    public void getMountClustersByDsObjectId() {
    }

    @Test
    public void getDataStoresByHostObjectId() {
    }

    @Test
    public void getMountDataStoresByHostObjectId() {
    }

    @Test
    public void getDataStoresByClusterObjectId() {
    }

    @Test
    public void getMountDataStoresByClusterObjectId() {
    }

    @Test
    public void getHostsOnCluster() {
    }

    @Test
    public void getUnmoutHostsOnCluster() {
    }

    @Test
    public void testGetHostsOnCluster() {
    }

    @Test
    public void renameDataStore() {
    }

    @Test
    public void getCapacityOnVmfs() {
    }

    @Test
    public void expandVmfsDatastore() {
    }

    @Test
    public void recycleVmfsCapacity() {
    }

    @Test
    public void createNfsDatastore() {
    }

    @Test
    public void hostRescanVmfs() {
    }

    @Test
    public void hostRescanHba() {
    }

    @Test
    public void getLunsOnHost() {
    }

    @Test
    public void testGetLunsOnHost() {
    }

    @Test
    public void getHbaDeviceByHost() {
    }

    @Test
    public void getLunsOnCluster() {
    }

    @Test
    public void getObjectLuns() {
    }

    @Test
    public void createVmfsDataStore() {
    }

    @Test
    public void attachTag() {
    }

    @Test
    public void deleteVmfsDataStore() {
    }

    @Test
    public void mountVmfsOnCluster() {
    }

    @Test
    public void unmountVmfsOnHostOrCluster() {
    }

    @Test
    public void mountVmfs() {
    }

    @Test
    public void unmountVmfs() {
    }

    @Test
    public void scanDataStore() {
    }

    @Test
    public void createDisk() {
    }

    @Test
    public void getDatastoreMountsOnHost() {
    }

    @Test
    public void getDatastoreByVmObject() {
    }

    @Test
    public void mountNfsOnCluster() {
    }

    @Test
    public void mountNfs() {
    }

    @Test
    public void unmountNfsOnHost() {
    }

    @Test
    public void unmountNfsOnCluster() {
    }

    @Test
    public void testUnmountNfsOnHost() {
    }

    @Test
    public void testUnmountNfsOnCluster() {
    }

    @Test
    public void getVmKernelIpByHostObjectId() {
    }

    @Test
    public void getAllSelfPolicyInallcontext() {
    }

    @Test
    public void getAllTagsByCategoryId() {
    }

    @Test
    public void getCategoryId() {
    }

    @Test
    public void createPbmProfileInAllContext() {
    }

    @Test
    public void createTag() {
    }

    @Test
    public void removePbmProfileInAllContext() {
    }

    @Test
    public void removeAllTags() {
    }

    @Test
    public void configureIscsi() {
    }

    @Test
    public void boundVmKernel() {
    }

    @Test
    public void addIscsiSendTargets() {
    }

    @Test
    public void deleteNfs() {
    }

    @Test
    public void testDeleteNfs() {
    }

    @Test
    public void testGetHostsByDsObjectId() {
    }

    @Test
    public void getHbaByHostObjectId() {
    }

    @Test
    public void getHbasByHostObjectId() {
    }

    @Test
    public void getHbasByClusterObjectId() {
    }

    @Test
    public void testConnectivity() {
    }

    @Test
    public void xmlFormat() {
    }

    @Test
    public void hasVmOnDatastore() {
    }

    @Test
    public void getHostName() {
    }

    @Test
    public void getClusterName() {
    }

    @Test
    public void getDataStoreName() {
    }

    @Test
    public void refreshDatastore() {
    }

    @Test
    public void refreshStorageSystem() {
    }

    @Test
    public void getHostByVmObjectId() {
    }
}