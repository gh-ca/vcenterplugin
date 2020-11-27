package com.dmeplugin.dmestore.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.dmeplugin.dmestore.exception.VcenterException;
import com.dmeplugin.vmware.VCConnectionHelper;
import com.dmeplugin.vmware.mo.ClusterMO;
import com.dmeplugin.vmware.mo.DatastoreMO;
import com.dmeplugin.vmware.mo.HostDatastoreSystemMO;
import com.dmeplugin.vmware.mo.HostMO;
import com.dmeplugin.vmware.mo.HostStorageSystemMO;
import com.dmeplugin.vmware.mo.RootFsMO;
import com.dmeplugin.vmware.util.ClusterMOFactory;
import com.dmeplugin.vmware.util.DatastoreMOFactory;
import com.dmeplugin.vmware.util.HostMOFactory;
import com.dmeplugin.vmware.util.Pair;
import com.dmeplugin.vmware.util.RootFsMOFactory;
import com.dmeplugin.vmware.util.VmwareClient;
import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim.binding.vmodl.list;
import com.vmware.vim.binding.vmodl.map;
import com.vmware.vim.vmomi.core.types.VmodlContext;
import com.vmware.vim25.DatastoreHostMount;
import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.HostDiskDimensionsLba;
import com.vmware.vim25.HostDiskPartitionSpec;
import com.vmware.vim25.HostFibreChannelHba;
import com.vmware.vim25.HostHostBusAdapter;
import com.vmware.vim25.HostInternetScsiHba;
import com.vmware.vim25.HostMountInfo;
import com.vmware.vim25.HostScsiDisk;
import com.vmware.vim25.HostStorageDeviceInfo;
import com.vmware.vim25.HostVmfsVolume;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.VmfsDatastoreExpandSpec;
import com.vmware.vim25.VmfsDatastoreInfo;
import com.vmware.vim25.VmfsDatastoreOption;
import com.vmware.vim25.VmfsDatastoreSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.result.FlashAttributeResultMatchers;

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
    private RootFsMOFactory rootFsMOFactory ;
    @Mock
    private DatastoreMOFactory datastoreMOFactory ;
    @Mock
    private HostMOFactory hostMOFactory;
    @Mock
    private ClusterMOFactory clusterMOFactory;

    @InjectMocks
    VCSDKUtils vcsdkUtils = new VCSDKUtils();;
    VmwareContext vmwareContext;
    ManagedObjectReference managedObjectReference;
    RootFsMO rootFsMo;
    DatastoreMO datastoreMO;
    HostMO hostMO;
    ClusterMO clusterMO;
    VmwareClient vimClient;
    List<Pair<ManagedObjectReference, String>> list;
    Pair<ManagedObjectReference, String> pair;
    List<DatastoreHostMount> dhms;
    DatastoreHostMount datastoreHostMount;
    HostMountInfo hostMountInfo;
    DatastoreSummary datastoreSummary;
    HostDatastoreSystemMO hostDatastoreSystemMO;
    List<VmfsDatastoreOption> vmfsDatastoreOptions;
    VmfsDatastoreOption vmfsDatastoreOption;
    VmfsDatastoreInfo vmfsDatastoreInfo;
    HostVmfsVolume hostVmfsVolume;
    VmfsDatastoreExpandSpec vmfsDatastoreExpandSpec;
    HostStorageSystemMO hostStorageSystemMO;
    List<HostScsiDisk> hostScsiDisks;
    HostScsiDisk hostScsiDisk;
    HostDiskDimensionsLba capacity;
    HostDiskDimensionsLba hostDiskDimensionsLba;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        vmwareContext = mock(VmwareContext.class);
        managedObjectReference = spy(ManagedObjectReference.class);
        managedObjectReference.setValue("321");
        rootFsMo = mock(RootFsMO.class);
        datastoreMO = mock(DatastoreMO.class);
        datastoreMO.setCustomFieldValue("321", "321");
        hostMO = mock(HostMO.class);
        clusterMO = mock(ClusterMO.class);
        vimClient = mock(VmwareClient.class);
        list = new ArrayList<>();
        pair = new Pair<>(managedObjectReference, "321");
        list.add(pair);

        dhms =new ArrayList<>();
        datastoreHostMount = new DatastoreHostMount();
        hostMountInfo = new HostMountInfo();
        hostMountInfo.setMounted(true);
        datastoreHostMount.setMountInfo(hostMountInfo);
        datastoreHostMount.setKey(managedObjectReference);
        datastoreHostMount.getKey().setValue("321");
        dhms.add(datastoreHostMount);

        datastoreSummary = spy(DatastoreSummary.class);
        datastoreSummary.setMultipleHostAccess(true);
        datastoreSummary.setUrl("321");
        datastoreSummary.setType("321");
        datastoreSummary.setName("321");
        datastoreSummary.setDatastore(managedObjectReference);
        datastoreSummary.setAccessible(true);

        hostDatastoreSystemMO = mock(HostDatastoreSystemMO.class);
        vmfsDatastoreOptions = new ArrayList<>();
        vmfsDatastoreOption = spy(VmfsDatastoreOption.class);
        VmfsDatastoreSpec vmfsDatastoreSpec = spy(VmfsDatastoreSpec.class);
        vmfsDatastoreOption.setSpec(vmfsDatastoreSpec);
        vmfsDatastoreInfo = spy(VmfsDatastoreInfo.class);
        hostVmfsVolume = spy(HostVmfsVolume.class);
        vmfsDatastoreInfo.setVmfs(hostVmfsVolume);
        hostVmfsVolume.setUuid("321");
        hostVmfsVolume.setBlockSize(1);
        vmfsDatastoreOptions.add(vmfsDatastoreOption);
        vmfsDatastoreExpandSpec = spy(VmfsDatastoreExpandSpec.class);
        HostDiskPartitionSpec hostDiskPartitionSpec = spy(HostDiskPartitionSpec.class);
        hostDiskPartitionSpec.setTotalSectors(321l);
        vmfsDatastoreExpandSpec.setPartition(hostDiskPartitionSpec);
        hostStorageSystemMO = mock(HostStorageSystemMO.class);

        hostScsiDisks = new ArrayList<>();
        hostScsiDisk = spy(HostScsiDisk.class);
        hostScsiDisk.setCanonicalName("naa.321");
        capacity = hostScsiDisk.getCapacity();
        hostScsiDisk.setUuid("321");
        hostScsiDisk.setDevicePath("321");
        hostScsiDisk.setDeviceName("321");
        hostScsiDisk.setLocalDisk(true);
        hostDiskDimensionsLba = new HostDiskDimensionsLba();
        hostDiskDimensionsLba.setBlockSize(321);
        hostDiskDimensionsLba.setBlock(321l);
        hostScsiDisk.setCapacity(hostDiskDimensionsLba);
        hostScsiDisks.add(hostScsiDisk);

    }

    @Test
    public void getAllVmfsDataStoreInfos() throws Exception {
        VmwareContext[] vmodlContexts = {vmwareContext};
        when(vcConnectionHelper.getAllContext()).thenReturn(vmodlContexts);
        when(rootFsMOFactory.build(vmwareContext, vmwareContext.getRootFolder())).thenReturn(rootFsMo);
        when(rootFsMo.getAllDatastoreOnRootFs()).thenReturn(list);
        when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
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
        Boolean flag = false;
        for (int i = 0; i <2 ; i++) {
            if (i == 0) {
                when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
                when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
                when(rootFsMOFactory.build(vmwareContext, vmwareContext.getRootFolder())).thenReturn(rootFsMo);
                when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
                when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
                when(datastoreMO.getHostMounts()).thenReturn(dhms);
                when(rootFsMo.getAllHostOnRootFs()).thenReturn(list);
                when(hostMO.getMor()).thenReturn(managedObjectReference);
                when(managedObjectReference.getValue()).thenReturn("");
                when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
                when(vcConnectionHelper.mor2ObjectId(datastoreMO.getMor(), vmwareContext.getServerAddress()))
                    .thenReturn("321");
                when(vmwareContext.getVimClient()).thenReturn(vimClient);
                when(vimClient.getDynamicProperty(managedObjectReference, "name")).thenReturn("321");
                vcsdkUtils.getHostsByDsObjectId("321",flag);
            }else {
                flag = true;
                when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
                when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
                when(rootFsMOFactory.build(vmwareContext, vmwareContext.getRootFolder())).thenReturn(rootFsMo);
                when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
                when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
                when(datastoreMO.getHostMounts()).thenReturn(dhms);
                when(rootFsMo.getAllHostOnRootFs()).thenReturn(list);
                when(hostMO.getMor()).thenReturn(managedObjectReference);
                when(managedObjectReference.getValue()).thenReturn("321");
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
    public void getMountHostsByDsObjectId() throws VcenterException {
       // vcsdkUtils.getMountHostsByDsObjectId("321");
    }

    @Test
    public void getClustersByDsObjectId() throws Exception {
        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(rootFsMOFactory.build(vmwareContext, vmwareContext.getRootFolder())).thenReturn(rootFsMo);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(datastoreMO.getHostMounts()).thenReturn(dhms);
        when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
        when(rootFsMo.getAllClusterOnRootFs()).thenReturn(list);
        when(clusterMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(clusterMO);
        when(clusterMO.getClusterHosts()).thenReturn(list);
        when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
        ManagedObjectReference mor = spy(ManagedObjectReference.class);
        mor.setValue("456");
        when(hostMO.getMor()).thenReturn(mor);
        when(managedObjectReference.getValue()).thenReturn("321");
        when(vcConnectionHelper.mor2ObjectId(datastoreMO.getMor(), vmwareContext.getServerAddress())).thenReturn("321");
        vcsdkUtils.getClustersByDsObjectId("321");
    }

    @Test
    public void getMountClustersByDsObjectId() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("321", "321");
        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(rootFsMOFactory.build(vmwareContext, vmwareContext.getRootFolder())).thenReturn(rootFsMo);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
        when(datastoreMO.getHostMounts()).thenReturn(dhms);
        when(rootFsMo.getAllClusterOnRootFs()).thenReturn(list);
        when(clusterMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(clusterMO);
        when(clusterMO.getClusterHosts()).thenReturn(list);
        when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
        when(hostMO.getMor()).thenReturn(managedObjectReference);
        when(managedObjectReference.getValue()).thenReturn("321");
        when(vcConnectionHelper.mor2ObjectId(datastoreMO.getMor(), vmwareContext.getServerAddress())).thenReturn("321");
        vcsdkUtils.getMountClustersByDsObjectId("321", map);
    }

    @Test
    public void getDataStoresByHostObjectId() throws Exception {

        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(rootFsMOFactory.build(vmwareContext, vmwareContext.getRootFolder())).thenReturn(rootFsMo);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        ManagedObjectReference mor = spy(ManagedObjectReference.class);
        mor.setValue("456");
        when(hostMO.getMor()).thenReturn(mor);
        when(hostMOFactory.build(vmwareContext, mor)).thenReturn(hostMO);
        when(hostMO.getMor()).thenReturn(managedObjectReference);
        when(managedObjectReference.getValue()).thenReturn("321");
        when(rootFsMo.getAllDatastoreOnRootFs()).thenReturn(list);
        when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
        when(datastoreMO.getMor()).thenReturn(managedObjectReference);
        when(datastoreMO.getSummary()).thenReturn(datastoreSummary);
        when(datastoreMO.getHostMounts()).thenReturn(dhms);
        when(vcConnectionHelper.mor2ObjectId(datastoreMO.getMor(), vmwareContext.getServerAddress())).thenReturn("321");
        when(hostMO.getMor()).thenReturn(managedObjectReference);
        when(managedObjectReference.getValue()).thenReturn("321");

        vcsdkUtils.getDataStoresByHostObjectId("321", "321");
    }

    @Test
    public void getMountDataStoresByHostObjectId() throws Exception {
        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(rootFsMOFactory.build(vmwareContext, vmwareContext.getRootFolder())).thenReturn(rootFsMo);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
        when(hostMO.getMor()).thenReturn(managedObjectReference);
        when(managedObjectReference.getValue()).thenReturn("321");
        when(rootFsMo.getAllDatastoreOnRootFs()).thenReturn(list);
        when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
        when(datastoreMO.getSummary()).thenReturn(datastoreSummary);
        when(datastoreMO.getHostMounts()).thenReturn(dhms);
        when(vcConnectionHelper.mor2ObjectId(datastoreMO.getMor(), vmwareContext.getServerAddress())).thenReturn("321");
        when(datastoreMO.getMor()).thenReturn(managedObjectReference);
        when(managedObjectReference.getValue()).thenReturn("321");
        vcsdkUtils.getMountDataStoresByHostObjectId("321","321");
    }

    @Test
    public void getDataStoresByClusterObjectId() throws Exception {

        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(rootFsMOFactory.build(vmwareContext, vmwareContext.getRootFolder())).thenReturn(rootFsMo);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(clusterMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(clusterMO);
        when(clusterMO.getClusterHosts()).thenReturn(list);
        when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
        ManagedObjectReference mor = spy(ManagedObjectReference.class);
        mor.setValue("456");
        when(hostMO.getMor()).thenReturn(mor);
        when(managedObjectReference.getValue()).thenReturn("456");
        when(rootFsMo.getAllDatastoreOnRootFs()).thenReturn(list);
        when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
        when(datastoreMO.getSummary()).thenReturn(datastoreSummary);
        when(datastoreMO.getHostMounts()).thenReturn(dhms);
        when(vcConnectionHelper.mor2ObjectId(datastoreMO.getMor(), vmwareContext.getServerAddress())).thenReturn("321");
        when(datastoreMO.getMor()).thenReturn(managedObjectReference);
        when(managedObjectReference.getValue()).thenReturn("321");
        vcsdkUtils.getDataStoresByClusterObjectId("321", "321");
    }

    @Test
    public void getMountDataStoresByClusterObjectId() throws Exception {
        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(rootFsMOFactory.build(vmwareContext, vmwareContext.getRootFolder())).thenReturn(rootFsMo);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(clusterMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(clusterMO);
        when(clusterMO.getClusterHosts()).thenReturn(list);
        when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
        when(hostMO.getMor()).thenReturn(managedObjectReference);
        when(managedObjectReference.getValue()).thenReturn("321");
        when(rootFsMo.getAllDatastoreOnRootFs()).thenReturn(list);
        when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
        when(datastoreMO.getSummary()).thenReturn(datastoreSummary);
        when(datastoreMO.getHostMounts()).thenReturn(dhms);
        when(datastoreMO.getMor()).thenReturn(managedObjectReference);
        when(managedObjectReference.getValue()).thenReturn("321");
        when(vcConnectionHelper.mor2ObjectId(datastoreMO.getMor(), vmwareContext.getServerAddress())).thenReturn("321");
        vcsdkUtils.getMountDataStoresByClusterObjectId("321", "321");
    }

    @Test
    public void getHostsOnCluster() throws Exception {

        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(clusterMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(clusterMO);
        when(clusterMO.getClusterHosts()).thenReturn(list);
        when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
        when(vcConnectionHelper.mor2ObjectId(hostMO.getMor(), vmwareContext.getServerAddress())).thenReturn("321");
        vcsdkUtils.getHostsOnCluster("321");
    }

    @Test
    //todo 调其他方法 留后面弄
    public void getUnmoutHostsOnCluster() throws VcenterException {
        Map<String, String> map = new HashMap<>();
        map.put("321", "321");
        List<Map<String, String>> list = new ArrayList<>();
        list.add(map);






        vcsdkUtils.getUnmoutHostsOnCluster("321", list);
    }

    @Test
    public void testGetHostsOnCluster() throws Exception {
        for (int i = 0; i <2 ; i++) {
            if (i == 0) {
                when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
                when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
                when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
                when(clusterMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(clusterMO);
                when(clusterMO.getClusterHosts()).thenReturn(list);
                vcsdkUtils.getHostsOnCluster("321", "");
            }
            if (i == 1) {
                when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
                when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
                when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
                when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
                when(hostMO.getHyperHostCluster()).thenReturn(managedObjectReference);
                when(clusterMOFactory.build(hostMO.getContext(), managedObjectReference)).thenReturn(clusterMO);
                when(clusterMO.getName()).thenReturn("321");
                when(clusterMO.getClusterHosts()).thenReturn(list);
                vcsdkUtils.getHostsOnCluster("", "321");
            }
        }
    }

    @Test
    public void renameDataStore() throws Exception {

        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
        vcsdkUtils.renameDataStore("321","321");
    }

    @Test
    public void expandVmfsDatastore() throws Exception {
        VmwareContext[] vmodlContexts = {vmwareContext};
        when(vcConnectionHelper.getAllContext()).thenReturn(vmodlContexts);
        when(rootFsMOFactory.build(vmwareContext, vmwareContext.getRootFolder())).thenReturn(rootFsMo);
        when(rootFsMo.getAllHostOnRootFs()).thenReturn(list);
        when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
        when(hostMO.getHostDatastoreSystemMo()).thenReturn(hostDatastoreSystemMO);
        when(hostDatastoreSystemMO.findDatastore("321")).thenReturn(managedObjectReference);
        when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
        when(hostDatastoreSystemMO.queryVmfsDatastoreExpandOptions(datastoreMO)).thenReturn(vmfsDatastoreOptions);
        when(hostDatastoreSystemMO.getDatastoreInfo(managedObjectReference)).thenReturn(vmfsDatastoreInfo);
        when(vmfsDatastoreOption.getSpec()).thenReturn(vmfsDatastoreExpandSpec);
        when(datastoreMO.getHostMounts()).thenReturn(dhms);
        when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
        when(hostMO.getHostStorageSystemMo()).thenReturn(hostStorageSystemMO);
        vcsdkUtils.expandVmfsDatastore("321", 21, "321");

    }

    @Test
    public void recycleVmfsCapacity() throws Exception {
        VmwareContext[] vmodlContexts = {vmwareContext};
        when(vcConnectionHelper.getAllContext()).thenReturn(vmodlContexts);
        when(rootFsMOFactory.build(vmwareContext, vmwareContext.getRootFolder())).thenReturn(rootFsMo);
        when(rootFsMo.getAllHostOnRootFs()).thenReturn(list);
        when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
        when(hostMO.getHostDatastoreSystemMo()).thenReturn(hostDatastoreSystemMO);
        when(hostDatastoreSystemMO.findDatastore("321")).thenReturn(managedObjectReference);
        when(hostDatastoreSystemMO.getDatastoreInfo(managedObjectReference)).thenReturn(vmfsDatastoreInfo);

        vcsdkUtils.recycleVmfsCapacity("321");
    }

    @Test
    public void createNfsDatastore() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("321", "321");
        List<Map<String, String>> list = new ArrayList<>();
        list.add(map);
        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
        when(hostMO.getHostDatastoreSystemMo()).thenReturn(hostDatastoreSystemMO);
        when(vcConnectionHelper.mor2ObjectId(datastoreMO.getMor(), vmwareContext.getServerAddress())).thenReturn("321");

        vcsdkUtils.createNfsDatastore("321","321","321","321",list,"321","321");
    }

    @Test
    public void hostRescanVmfs() throws Exception {

        VmwareContext[] vmodlContexts = {vmwareContext};
        when(vcConnectionHelper.getAllContext()).thenReturn(vmodlContexts);
        when(rootFsMOFactory.build(vmwareContext, vmwareContext.getRootFolder())).thenReturn(rootFsMo);
        when(rootFsMo.getAllHostOnRootFs()).thenReturn(list);
        when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
        when(hostMO.getHostName()).thenReturn("321");
        when(hostMO.getHostStorageSystemMo()).thenReturn(hostStorageSystemMO);
        vcsdkUtils.hostRescanVmfs("321");
    }

    @Test
    public void hostRescanHba() throws Exception {
        for (int i = 0; i <2 ; i++) {
            if (i == 0) {
                VmwareContext[] vmodlContexts = {vmwareContext};
                when(vcConnectionHelper.getAllContext()).thenReturn(vmodlContexts);
                when(rootFsMOFactory.build(vmwareContext, vmwareContext.getRootFolder())).thenReturn(rootFsMo);
                when(rootFsMo.getAllHostOnRootFs()).thenReturn(list);
                when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
                when(hostMO.getHostName()).thenReturn("321");
                when(hostMO.getHostStorageSystemMo()).thenReturn(hostStorageSystemMO);
                HostStorageDeviceInfo storageDeviceInfo = spy(HostStorageDeviceInfo.class);
                when(hostStorageSystemMO.getStorageDeviceInfo()).thenReturn(storageDeviceInfo);
                List<HostHostBusAdapter> hostBusAdapter = spy(ArrayList.class);
                HostInternetScsiHba hostHostBusAdapter = new HostInternetScsiHba();
                hostHostBusAdapter.setDevice("321");
                hostBusAdapter.add(hostHostBusAdapter);
                when(storageDeviceInfo.getHostBusAdapter()).thenReturn(hostBusAdapter);
                vcsdkUtils.hostRescanHba("321");
            }
            if (i == 1) {
                VmwareContext[] vmodlContexts = {vmwareContext};
                when(vcConnectionHelper.getAllContext()).thenReturn(vmodlContexts);
                when(rootFsMOFactory.build(vmwareContext, vmwareContext.getRootFolder())).thenReturn(rootFsMo);
                when(rootFsMo.getAllHostOnRootFs()).thenReturn(list);
                when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
                when(hostMO.getHostName()).thenReturn("321");
                when(hostMO.getHostStorageSystemMo()).thenReturn(hostStorageSystemMO);
                HostStorageDeviceInfo storageDeviceInfo = spy(HostStorageDeviceInfo.class);
                when(hostStorageSystemMO.getStorageDeviceInfo()).thenReturn(storageDeviceInfo);
                List<HostHostBusAdapter> hostBusAdapter = spy(ArrayList.class);
                HostFibreChannelHba hostHostBusAdapter = new HostFibreChannelHba();
                hostHostBusAdapter.setDevice("321");
                hostBusAdapter.add(hostHostBusAdapter);
                when(storageDeviceInfo.getHostBusAdapter()).thenReturn(hostBusAdapter);
                vcsdkUtils.hostRescanHba("321");
            }
        }

    }

    @Test
    public void getLunsOnHost() throws Exception {

        VmwareContext[] vmodlContexts = {vmwareContext};
        when(vcConnectionHelper.getAllContext()).thenReturn(vmodlContexts);
        when(rootFsMOFactory.build(vmwareContext, vmwareContext.getRootFolder())).thenReturn(rootFsMo);
        when(rootFsMo.getAllHostOnRootFs()).thenReturn(list);
        when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
        when(hostMO.getName()).thenReturn("321");
        when(hostMO.getHostDatastoreSystemMo()).thenReturn(hostDatastoreSystemMO);
        when(hostDatastoreSystemMO.queryAvailableDisksForVmfs()).thenReturn(hostScsiDisks);
        vcsdkUtils.getLunsOnHost("321");
    }

    @Test
    public void testGetLunsOnHost() throws Exception {

        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
        when(hostMO.getHostStorageSystemMo()).thenReturn(hostStorageSystemMO);
        HostStorageDeviceInfo storageDeviceInfo = spy(HostStorageDeviceInfo.class);
        when(hostStorageSystemMO.getStorageDeviceInfo()).thenReturn(storageDeviceInfo);
        List<HostHostBusAdapter> hostBusAdapter = spy(ArrayList.class);
        HostInternetScsiHba hostHostBusAdapter = new HostInternetScsiHba();
        hostHostBusAdapter.setDevice("321");
        hostBusAdapter.add(hostHostBusAdapter);
        when(storageDeviceInfo.getHostBusAdapter()).thenReturn(hostBusAdapter);
        when(hostMO.getHostDatastoreSystemMo()).thenReturn(hostDatastoreSystemMO);
        when(hostDatastoreSystemMO.queryAvailableDisksForVmfs()).thenReturn(hostScsiDisks);
        vcsdkUtils.getLunsOnHost("321",321,"321");
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