package com.dmeplugin.dmestore.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.dmeplugin.dmestore.exception.VcenterException;
import com.dmeplugin.vmware.VCConnectionHelper;
import com.dmeplugin.vmware.mo.DatastoreMO;
import com.dmeplugin.vmware.mo.HostMO;
import com.dmeplugin.vmware.mo.RootFsMO;
import com.dmeplugin.vmware.util.DatastoreMOFactory;
import com.dmeplugin.vmware.util.HostMOFactory;
import com.dmeplugin.vmware.util.Pair;
import com.dmeplugin.vmware.util.RootFsMOFactory;
import com.dmeplugin.vmware.util.VmwareClient;
import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim.binding.vmodl.list;
import com.vmware.vim.vmomi.core.types.VmodlContext;
import com.vmware.vim25.DatastoreHostMount;
import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.HostMountInfo;
import com.vmware.vim25.ManagedObjectReference;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lianq
 * @className VCSDKUtilsTest
 * @description TODO
 * @date 2020/11/25 15:51
 */
public class VCSDKUtilsTest {

    @Mock
    private VCConnectionHelper vcConnectionHelper;
    @Mock
    private RootFsMOFactory rootFsMOFactory;
    @Mock
    private DatastoreMOFactory datastoreMOFactory;
    @Mock
    private HostMOFactory hostMOFactory;

    @InjectMocks
    VCSDKUtils vcsdkUtils = new VCSDKUtils();;
    VmwareContext vmwareContext;
    ManagedObjectReference managedObjectReference;
    RootFsMO rootFsMo;
    DatastoreMO datastoreMO;
    HostMO hostMO;
    VmwareClient vimClient;
    List<Pair<ManagedObjectReference, String>> list;
    Pair<ManagedObjectReference, String> pair;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        vmwareContext = mock(VmwareContext.class);
        managedObjectReference = spy(ManagedObjectReference.class);
        rootFsMo = mock(RootFsMO.class);
        datastoreMO = mock(DatastoreMO.class);
        hostMO = mock(HostMO.class);
        vimClient = mock(VmwareClient.class);
        list = new ArrayList<>();
        pair = new Pair<>(managedObjectReference, "321");
        list.add(pair);
    }

    @Test
    public void getAllVmfsDataStoreInfos() throws Exception {
        VmwareContext[] vmodlContexts = {vmwareContext};
        when(vcConnectionHelper.getAllContext()).thenReturn(vmodlContexts);
        when(rootFsMOFactory.build(vmwareContext, vmwareContext.getRootFolder())).thenReturn(rootFsMo);
        when(rootFsMo.getAllDatastoreOnRootFs()).thenReturn(list);
        when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
        DatastoreSummary datastoreSummary = spy(DatastoreSummary.class);
        datastoreSummary.setMultipleHostAccess(true);
        datastoreSummary.setUrl("321");
        datastoreSummary.setType("321");
        datastoreSummary.setName("321");
        datastoreSummary.setDatastore(managedObjectReference);
        when(vmwareContext.getVimClient()).thenReturn(vimClient);
        when(vimClient.getDynamicProperty(managedObjectReference, "summary")).thenReturn(datastoreSummary);
        when(vcConnectionHelper.mor2ObjectId(datastoreMO.getMor(), vmwareContext.getServerAddress())).thenReturn("321");
        vcsdkUtils.getAllVmfsDataStoreInfos("321");
    }

    @Test
    public void getDataStoreSummaryByObjectId() throws Exception {
        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
        vcsdkUtils.getDataStoreSummaryByObjectId("321");

    }

    @Test
    public void getAllHosts() throws Exception {
        VmwareContext[] vmodlContexts = {vmwareContext};
        when(vcConnectionHelper.getAllContext()).thenReturn(vmodlContexts);
        when(rootFsMOFactory.build(vmwareContext, vmwareContext.getRootFolder())).thenReturn(rootFsMo);
        when(rootFsMo.getAllHostOnRootFs()).thenReturn(list);
        when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
        when(vcConnectionHelper.mor2ObjectId(hostMO.getMor(), vmwareContext.getServerAddress())).thenReturn("321");
        when(vmwareContext.getVimClient()).thenReturn(vimClient);
        when(vimClient.getDynamicProperty(managedObjectReference, "name")).thenReturn("321");
        vcsdkUtils.getAllHosts();
    }

    @Test
    public void findHostById() throws Exception {
        VmwareContext[] vmodlContexts = {vmwareContext};
        when(vcConnectionHelper.getAllContext()).thenReturn(vmodlContexts);
        when(rootFsMOFactory.build(vmwareContext, vmwareContext.getRootFolder())).thenReturn(rootFsMo);
        when(rootFsMo.findHostById("321")).thenReturn(hostMO);
        when(vmwareContext.getVimClient()).thenReturn(vimClient);
        when(vimClient.getDynamicProperty(managedObjectReference, "name")).thenReturn("321");
        vcsdkUtils.findHostById("321");
    }

    @Test
    public void getAllClusters() throws Exception {

        VmwareContext[] vmodlContexts = {vmwareContext};
        when(vcConnectionHelper.getAllContext()).thenReturn(vmodlContexts);
        when(rootFsMOFactory.build(vmwareContext, vmwareContext.getRootFolder())).thenReturn(rootFsMo);
        when(rootFsMo.getAllClusterOnRootFs()).thenReturn(list);
        when(vcConnectionHelper.mor2ObjectId(hostMO.getMor(), vmwareContext.getServerAddress())).thenReturn("321");
        when(vmwareContext.getVimClient()).thenReturn(vimClient);
        when(vimClient.getDynamicProperty(managedObjectReference, "name")).thenReturn("321");
        vcsdkUtils.getAllClusters();
    }

    @Test
    public void getHostsByDsObjectId() throws Exception {
        Boolean flag = true;
        for (int i = 0; i < 2; i++) {
            List<DatastoreHostMount> dhms = new ArrayList<>();
            DatastoreHostMount datastoreHostMount = new DatastoreHostMount();
            HostMountInfo hostMountInfo = new HostMountInfo();
            hostMountInfo.setMounted(true);
            datastoreHostMount.setMountInfo(hostMountInfo);
            managedObjectReference.setValue("321");
            datastoreHostMount.setKey(managedObjectReference);
            datastoreHostMount.getKey().setValue("321");
            dhms.add(datastoreHostMount);
            if (i == 0) {
                when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
                when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
                when(rootFsMOFactory.build(vmwareContext, vmwareContext.getRootFolder())).thenReturn(rootFsMo);
                when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
                when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
                when(datastoreMO.getHostMounts()).thenReturn(dhms);
                when(rootFsMo.getAllHostOnRootFs()).thenReturn(list);
                when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
                when(vcConnectionHelper.mor2ObjectId(datastoreMO.getMor(), vmwareContext.getServerAddress()))
                    .thenReturn("321");
                when(vmwareContext.getVimClient()).thenReturn(vimClient);
                when(vimClient.getDynamicProperty(managedObjectReference, "name")).thenReturn("321");
                vcsdkUtils.getHostsByDsObjectId("321",flag);
            }else {
                when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
                when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
                when(rootFsMOFactory.build(vmwareContext, vmwareContext.getRootFolder())).thenReturn(rootFsMo);
                when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
                when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
                when(datastoreMO.getHostMounts()).thenReturn(dhms);
                when(rootFsMo.getAllHostOnRootFs()).thenReturn(list);
                when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
                when(vcConnectionHelper.mor2ObjectId(datastoreMO.getMor(), vmwareContext.getServerAddress()))
                    .thenReturn("321");
                when(vmwareContext.getVimClient()).thenReturn(vimClient);
                when(vimClient.getDynamicProperty(managedObjectReference, "name")).thenReturn("321");
                vcsdkUtils.getHostsByDsObjectId("321",flag);
            }
        }

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