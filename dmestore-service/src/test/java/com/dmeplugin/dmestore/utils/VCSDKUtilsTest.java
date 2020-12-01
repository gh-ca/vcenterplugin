package com.dmeplugin.dmestore.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.dmeplugin.dmestore.entity.VCenterInfo;
import com.dmeplugin.dmestore.exception.VcenterException;
import com.dmeplugin.vmware.VCConnectionHelper;
import com.dmeplugin.vmware.autosdk.SessionHelper;
import com.dmeplugin.vmware.autosdk.TaggingWorkflow;
import com.dmeplugin.vmware.mo.ClusterMO;
import com.dmeplugin.vmware.mo.DatastoreMO;
import com.dmeplugin.vmware.mo.HostDatastoreSystemMO;
import com.dmeplugin.vmware.mo.HostMO;
import com.dmeplugin.vmware.mo.HostStorageSystemMO;
import com.dmeplugin.vmware.mo.IscsiManagerMO;
import com.dmeplugin.vmware.mo.RootFsMO;
import com.dmeplugin.vmware.mo.VirtualMachineMO;
import com.dmeplugin.vmware.util.ClusterMOFactory;
import com.dmeplugin.vmware.util.DatastoreMOFactory;
import com.dmeplugin.vmware.util.HostMOFactory;
import com.dmeplugin.vmware.util.Pair;
import com.dmeplugin.vmware.util.PbmUtil;
import com.dmeplugin.vmware.util.RootFsMOFactory;
import com.dmeplugin.vmware.util.SessionHelperFactory;
import com.dmeplugin.vmware.util.TaggingWorkflowFactory;
import com.dmeplugin.vmware.util.VirtualMachineMOFactory;
import com.dmeplugin.vmware.util.VmwareClient;
import com.dmeplugin.vmware.util.VmwareContext;
import com.google.gson.Gson;
import com.vmware.cis.tagging.CategoryModel;
import com.vmware.cis.tagging.CategoryTypes;
import com.vmware.cis.tagging.TagModel;
import com.vmware.pbm.PbmPortType;
import com.vmware.pbm.PbmProfile;
import com.vmware.pbm.PbmProfileId;
import com.vmware.pbm.PbmProfileResourceType;
import com.vmware.pbm.PbmServiceInstanceContent;
import com.vmware.vapi.std.DynamicID;
import com.vmware.vim.binding.vmodl.list;
import com.vmware.vim.binding.vmodl.map;
import com.vmware.vim.vmomi.core.types.VmodlContext;
import com.vmware.vim25.DatastoreHostMount;
import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.HostDiskDimensionsLba;
import com.vmware.vim25.HostDiskPartitionSpec;
import com.vmware.vim25.HostFibreChannelHba;
import com.vmware.vim25.HostFileSystemMountInfo;
import com.vmware.vim25.HostFileSystemVolumeInfo;
import com.vmware.vim25.HostHostBusAdapter;
import com.vmware.vim25.HostInternetScsiHba;
import com.vmware.vim25.HostIpConfig;
import com.vmware.vim25.HostMountInfo;
import com.vmware.vim25.HostNasVolume;
import com.vmware.vim25.HostScsiDisk;
import com.vmware.vim25.HostStorageDeviceInfo;
import com.vmware.vim25.HostVirtualNic;
import com.vmware.vim25.HostVirtualNicSpec;
import com.vmware.vim25.HostVmfsVolume;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.IscsiPortInfo;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.NasDatastoreInfo;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.VirtualNicManagerNetConfig;
import com.vmware.vim25.VmfsDatastoreExpandSpec;
import com.vmware.vim25.VmfsDatastoreInfo;
import com.vmware.vim25.VmfsDatastoreOption;
import com.vmware.vim25.VmfsDatastoreSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.result.FlashAttributeResultMatchers;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
    @Mock
    private SessionHelperFactory sessionHelperFactory;
    @Mock
    private TaggingWorkflowFactory taggingWorkflowFactory;
    @Mock
    private VirtualMachineMOFactory virtualMachineMOFactory;
    private Gson gson = new Gson();
    private PbmUtil pbmUtil;
    private static VmodlContext context;

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
    Map<String, Object> hsdmap;
    VCenterInfo vCenterInfo;
    SessionHelper sessionHelper;
    TaggingWorkflow taggingWorkflow;
    VirtualMachineMO virtualMachineMo;
    List<PbmProfile> pbmprofiles;
    PbmProfile pbmProfile;
    PbmServiceInstanceContent spbmsc ;
    PbmPortType pbmService ;

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
        datastoreSummary = new DatastoreSummary();
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

        hsdmap = new HashMap<>();
        hsdmap.put("host", hostMO);
        hsdmap.put("hostScsiDisk", hostScsiDisk);

        vCenterInfo = new VCenterInfo();
        vCenterInfo.setHostIp("321");
        vCenterInfo.setHostPort(321);
        vCenterInfo.setUserName("321");
        vCenterInfo.setPassword("321");

        sessionHelper = mock(SessionHelper.class);
        taggingWorkflow = mock(TaggingWorkflow.class);
        virtualMachineMo = mock(VirtualMachineMO.class);

        pbmprofiles = spy(ArrayList.class);
        pbmProfile = spy(PbmProfile.class);
        pbmProfile.setDescription("policy created by dme");
        pbmprofiles.add(pbmProfile);
        spbmsc = spy(PbmServiceInstanceContent.class);
        pbmService = spy(PbmPortType.class);
        spbmsc.setProfileManager(managedObjectReference);
        context = spy(VmodlContext.class);

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
        when(clusterMO.getName()).thenReturn("321");
        when(rootFsMo.getAllDatastoreOnRootFs()).thenReturn(list);
        when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
        when(datastoreMO.getSummary()).thenReturn(datastoreSummary);
        when(datastoreSummary.getType()).thenReturn("321");
        when(datastoreSummary.getCapacity()).thenReturn(321l);
        when(datastoreSummary.getFreeSpace()).thenReturn(32l);
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
    public void getUnmoutHostsOnCluster() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("321", "321");
        List<Map<String, String>> l1 = new ArrayList<>();
        l1.add(map);
        getHostsOnCluster();
        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(clusterMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(clusterMO);
        when(clusterMO.getClusterHosts()).thenReturn(list);


        vcsdkUtils.getUnmoutHostsOnCluster("321", l1);
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
    public void getLunsOnCluster() throws Exception {

        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(clusterMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(clusterMO);
        when(clusterMO.getClusterHosts()).thenReturn(list);
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


        vcsdkUtils.getLunsOnCluster("321", 321, "321");
    }

    @Test
    public void createVmfsDataStore() throws Exception {
        Map<String, Object> hsdmap = new HashMap<>();
        hsdmap.put("host", hostMO);
        hsdmap.put("hostScsiDisk", hostScsiDisk);
        when(hostMO.getHostDatastoreSystemMo()).thenReturn(hostDatastoreSystemMO);
        when(hostDatastoreSystemMO.createVmfsDatastore("321", hostScsiDisk, 321,
            321, 1073741824, 321, "321")).thenReturn(managedObjectReference);
        when(datastoreMOFactory.build(hostMO.getContext(), managedObjectReference)).thenReturn(datastoreMO);
        when(vcConnectionHelper.mor2ObjectId(datastoreMO.getMor(), vmwareContext.getServerAddress())).thenReturn("321");
        when(datastoreMO.getContext()).thenReturn(vmwareContext);
        when(vmwareContext.getServerAddress()).thenReturn("321");
        when(datastoreMO.getName()).thenReturn("321");
        when(datastoreMO.getMor()).thenReturn(managedObjectReference);
        when(managedObjectReference.getValue()).thenReturn("321");
        when(datastoreMO.getSummary()).thenReturn(datastoreSummary);
        when(hostMO.getName()).thenReturn("321");
        vcsdkUtils.createVmfsDataStore(hsdmap, 321, "321", 321, 321,
            321, "321");
    }

    @Test
    public void attachTag() throws Exception {
        when(sessionHelperFactory.build()).thenReturn(sessionHelper);
        when(taggingWorkflowFactory.build(sessionHelper)).thenReturn(taggingWorkflow);
        List<String> list = spy(ArrayList.class);
        list.add("321");
        when(taggingWorkflow.listTags()).thenReturn(list);
        TagModel tagModel = new TagModel();
        tagModel.setName("321");
        when(taggingWorkflow.getTag("321")).thenReturn(tagModel);
        vcsdkUtils.attachTag("321", "321", "321", vCenterInfo);
    }

    @Test
    public void deleteVmfsDataStore() throws Exception {

        VmwareContext[] vmodlContexts = {vmwareContext};
        when(vcConnectionHelper.getAllContext()).thenReturn(vmodlContexts);
        when(rootFsMOFactory.build(vmwareContext, vmwareContext.getRootFolder())).thenReturn(rootFsMo);
        when(rootFsMo.getAllHostOnRootFs()).thenReturn(list);
        when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
        when(hostMO.getHostDatastoreSystemMo()).thenReturn(hostDatastoreSystemMO);
        when(hostDatastoreSystemMO.findDatastore("321")).thenReturn(managedObjectReference);
        when(hostDatastoreSystemMO.deleteDatastore("321")).thenReturn(true);
        vcsdkUtils.deleteVmfsDataStore("321");

    }

    @Test
    public void mountVmfsOnCluster() throws Exception {
        Map<String, Object> dsmap = new HashMap<>();
        dsmap.put("name", "321");
        dsmap.put("hostName", "321");
        for (int i = 0; i <2 ; i++) {
            if (i == 0) {
                when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
                when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
                when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
                when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
                when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
                when(clusterMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(clusterMO);
                when(clusterMO.getClusterHosts()).thenReturn(list);
                when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
                vcsdkUtils.mountVmfsOnCluster(gson.toJson(dsmap), "321", "");
            }
            if (i == 1) {
                when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
                when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
                vcsdkUtils.mountVmfsOnCluster(gson.toJson(dsmap), "", "321");
            }
        }
    }

    @Test
    public void unmountVmfsOnHostOrCluster() throws Exception {
        Map<String, Object> dsmap = new HashMap<>();
        dsmap.put("name", "321");
        dsmap.put("hostName", "321");
        for (int i = 0; i <2 ; i++) {
            if (i == 0) {
                when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
                when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
                when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
                when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
                when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
                when(clusterMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(clusterMO);
                when(clusterMO.getClusterHosts()).thenReturn(list);
                when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
                when(hostMO.getName()).thenReturn("321");
                vcsdkUtils.unmountVmfsOnHostOrCluster(gson.toJson(dsmap),"321","");
            }
            if (i == 1) {
                when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
                when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
                when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
                vcsdkUtils.unmountVmfsOnHostOrCluster(gson.toJson(dsmap),"","321");
            }
        }
    }

    @Test
    public void mountVmfs() throws Exception {

        when(hostMO.getHostStorageSystemMo()).thenReturn(hostStorageSystemMO);
        HostFileSystemVolumeInfo hostFileSystemVolumeInfo = spy(HostFileSystemVolumeInfo.class);
        HostFileSystemMountInfo hostFileSystemMountInfo = spy(HostFileSystemMountInfo.class);
        HostMountInfo hostMountInfo = spy(HostMountInfo.class);
        hostFileSystemMountInfo.setMountInfo(hostMountInfo);
        HostVmfsVolume hostVmfsVolume = spy(HostVmfsVolume.class);
        hostVmfsVolume.setName("321");
        hostVmfsVolume.setUuid("321");
        hostFileSystemMountInfo.setVolume(hostVmfsVolume);
        hostFileSystemVolumeInfo.getMountInfo().add(hostFileSystemMountInfo);
        when(hostStorageSystemMO.getHostFileSystemVolumeInfo()).thenReturn(hostFileSystemVolumeInfo);
        vcsdkUtils.mountVmfs("321", hostMO);
    }

    @Test
    public void unmountVmfs() throws Exception {

        when(hostMO.getName()).thenReturn("321");
        when(hostMO.getHostStorageSystemMo()).thenReturn(hostStorageSystemMO);
        HostFileSystemVolumeInfo hostFileSystemVolumeInfo = spy(HostFileSystemVolumeInfo.class);
        HostFileSystemMountInfo hostFileSystemMountInfo = spy(HostFileSystemMountInfo.class);
        HostMountInfo hostMountInfo = spy(HostMountInfo.class);
        hostFileSystemMountInfo.setMountInfo(hostMountInfo);
        HostVmfsVolume hostVmfsVolume = spy(HostVmfsVolume.class);
        hostVmfsVolume.setName("321");
        hostVmfsVolume.setUuid("321");
        hostFileSystemMountInfo.setVolume(hostVmfsVolume);
        hostFileSystemVolumeInfo.getMountInfo().add(hostFileSystemMountInfo);
        when(hostStorageSystemMO.getHostFileSystemVolumeInfo()).thenReturn(hostFileSystemVolumeInfo);
        vcsdkUtils.unmountVmfs("321", hostMO);

    }

    @Test
    public void scanDataStore() throws Exception {
        for (int i = 0; i <2 ; i++) {
            if (i == 0) {
                when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
                when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
                when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
                when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
                when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
                when(clusterMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(clusterMO);
                when(clusterMO.getClusterHosts()).thenReturn(list);
                when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
                when(hostMO.getName()).thenReturn("321");
                vcsdkUtils.scanDataStore("321", "");
            }
            if (i == 1) {
                when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
                when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
                when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
                vcsdkUtils.scanDataStore("", "321");
            }
        }

    }

    @Test
    public void createDisk() throws Exception {

        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(virtualMachineMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(virtualMachineMo);
        vcsdkUtils.createDisk("321", "321", "321", 21);
    }

    @Test
    public void getDatastoreMountsOnHost() throws Exception {

        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(virtualMachineMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(virtualMachineMo);
        when(virtualMachineMo.getRunningHost()).thenReturn(hostMO);
        when(hostMO.getDatastoreMountsOnHost()).thenReturn(list);
        when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
        when(vcConnectionHelper.mor2ObjectId(datastoreMO.getMor(), vmwareContext.getServerAddress())).thenReturn("321");
        DatastoreSummary datastoreSummary = new DatastoreSummary();//spy(DatastoreSummary.class);
        datastoreSummary.setMultipleHostAccess(true);
        datastoreSummary.setUrl("321");
        datastoreSummary.setType("VMFS");
        datastoreSummary.setName("321");
        datastoreSummary.setDatastore(managedObjectReference);
        datastoreSummary.setAccessible(true);
        when(datastoreMO.getSummary()).thenReturn(datastoreSummary);
        //when(datastoreSummary.getType()).thenReturn("VMFS");
        vcsdkUtils.getDatastoreMountsOnHost("321");

    }
    @Test
    public void mountNfs() throws Exception {
        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
        when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
        when(hostMO.getHostStorageSystemMo()).thenReturn(hostStorageSystemMO);
        NasDatastoreInfo datastoreInfo = spy(NasDatastoreInfo.class);
        HostNasVolume hostNasVolume = spy(HostNasVolume.class);
        hostNasVolume.setRemoteHost("321");
        hostNasVolume.setRemotePath("321");
        hostNasVolume.setSecurityType("321");
        hostNasVolume.setType("321");
        datastoreInfo.setNas(hostNasVolume);
        when(datastoreMO.getName()).thenReturn("321");
        when(datastoreMO.getInfo()).thenReturn(datastoreInfo);
        when(hostMO.getHostDatastoreSystemMo()).thenReturn(hostDatastoreSystemMO);
        when(hostMO.getName()).thenReturn("321");
        when(datastoreMO.getName()).thenReturn("321");
        vcsdkUtils.mountNfs("321", "321", "321", "321");
    }

    @Test
    public void unmountNfsOnHost() throws Exception {
        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
        vcsdkUtils.unmountNfsOnHost("321", "321");

    }

    @Test
    public void unmountNfsOnCluster() throws Exception {
        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(rootFsMOFactory.build(vmwareContext, vmwareContext.getRootFolder())).thenReturn(rootFsMo);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
        when(rootFsMo.findClusterById("321")).thenReturn(clusterMO);
        when(clusterMO.getName()).thenReturn("321");
        vcsdkUtils.unmountNfsOnCluster("321", "321");
    }

    @Test
    public void testUnmountNfsOnHost() throws Exception {
        when(hostMO.getHostStorageSystemMo()).thenReturn(hostStorageSystemMO);
        when(datastoreMO.getName()).thenReturn("321");
        when(hostMO.getHostDatastoreSystemMo()).thenReturn(hostDatastoreSystemMO);
        vcsdkUtils.unmountNfsOnHost(datastoreMO, hostMO, "321");

    }

    @Test
    public void getVmKernelIpByHostObjectId() throws Exception {

        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
        List<VirtualNicManagerNetConfig> nics = spy(ArrayList.class);
        VirtualNicManagerNetConfig virtualNicManagerNetConfig = spy(VirtualNicManagerNetConfig.class);
        virtualNicManagerNetConfig.setNicType("vSphereProvisioning");
        virtualNicManagerNetConfig.getSelectedVnic().add("321");
        HostVirtualNic hostVirtualNic = spy(HostVirtualNic.class);
        hostVirtualNic.setDevice("321");
        hostVirtualNic.setKey("321");
        hostVirtualNic.setPort("321");
        hostVirtualNic.setPortgroup("321");
        HostVirtualNicSpec hostVirtualNicSpec = spy(HostVirtualNicSpec.class);
        HostIpConfig hostIpConfig = spy(HostIpConfig.class);
        hostIpConfig.setIpAddress("321");
        hostVirtualNicSpec.setIp(hostIpConfig);
        hostVirtualNicSpec.setMac("321");
        hostVirtualNic.setSpec(hostVirtualNicSpec);
        virtualNicManagerNetConfig.getCandidateVnic().add(hostVirtualNic);
        nics.add(virtualNicManagerNetConfig);
        when(hostMO.getHostVirtualNicManagerNetConfig()).thenReturn(nics);
        vcsdkUtils.getVmKernelIpByHostObjectId("321");

    }

    @Test
    public void getAllSelfPolicyInallcontext() throws Exception {
        VmwareContext[] vmodlContexts = {vmwareContext};
        when(vcConnectionHelper.getAllContext()).thenReturn(vmodlContexts);
        when(vmwareContext.getPbmServiceContent()).thenReturn(spbmsc);
        when(vmwareContext.getPbmService()).thenReturn(pbmService);
        PbmProfileResourceType pbmProfileResourceType = spy(PbmProfileResourceType.class);
        pbmProfileResourceType.setResourceType("STORAGE");
        List<PbmProfileId> pbmProfileIds = spy(ArrayList.class);
        PbmProfileId pbmProfileId = spy(PbmProfileId.class);
        pbmProfileId.setUniqueId("321");
        String param = "{\"resourceType\":\"STORAGE\"}";
        when(pbmService.pbmQueryProfile(managedObjectReference, pbmProfileResourceType, null))
            .thenReturn(pbmProfileIds);
        when(vmwareContext.getPbmService()).thenReturn(pbmService);
        when(pbmService.pbmRetrieveContent(spbmsc.getProfileManager(), pbmProfileIds)).thenReturn(pbmprofiles);
        vcsdkUtils.getAllSelfPolicyInallcontext();
    }

    @Test
    public void getAllTagsByCategoryId() throws Exception {

        when(taggingWorkflowFactory.build(sessionHelper)).thenReturn(taggingWorkflow);
        List<String> list = spy(ArrayList.class);
        list.add("321");
        when(taggingWorkflow.listTagsForCategory("321")).thenReturn(list);
        TagModel tagModel = new TagModel();
        tagModel.setName("321");
        when(taggingWorkflow.getTag("321")).thenReturn(tagModel);
        vcsdkUtils.getAllTagsByCategoryId("321",sessionHelper);
    }

    @Test
    public void getCategoryId() throws Exception {
        when(taggingWorkflowFactory.build(sessionHelper)).thenReturn(taggingWorkflow);
        List<String> list = spy(ArrayList.class);
        list.add("321");
        when(taggingWorkflow.listTagCategory()).thenReturn(list);
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setName("DME Service Level");
        categoryModel.setId("");
        CategoryTypes.CreateSpec createSpec = new CategoryTypes.CreateSpec();
        taggingWorkflow.createTagCategory(createSpec);
        when(taggingWorkflow.getTagCategory("321")).thenReturn(categoryModel);
        createSpec.setName("DME Service Level");
        createSpec.setDescription("DME Service Level");
        createSpec.setCardinality(CategoryModel.Cardinality.SINGLE);
        Set<String> associableTypes = spy(HashSet.class); // empty hash set
        associableTypes.add("Datastore");
        createSpec.setAssociableTypes(associableTypes);
        when(taggingWorkflow.createTagCategory(createSpec)).thenReturn("321");
        vcsdkUtils.getCategoryId(sessionHelper);
    }

    @Test
    public void createPbmProfileInAllContext() throws Exception {
        VmwareContext[] vmodlContexts = {vmwareContext};
        when(vcConnectionHelper.getAllContext()).thenReturn(vmodlContexts);
        vcsdkUtils.createPbmProfileInAllContext("321", "321");
    }

    @Test
    public void createTag() throws Exception {
        when(taggingWorkflowFactory.build(sessionHelper)).thenReturn(taggingWorkflow);
        getCategoryId();
        vcsdkUtils.createTag("321", sessionHelper);

    }

    @Test
    public void removePbmProfileInAllContext() throws Exception {
        VmwareContext[] vmodlContexts = {vmwareContext};
        when(vcConnectionHelper.getAllContext()).thenReturn(vmodlContexts);
        when(vmwareContext.getPbmServiceContent()).thenReturn(spbmsc);
        when(vmwareContext.getPbmService()).thenReturn(pbmService);
        vcsdkUtils.removePbmProfileInAllContext(pbmprofiles);
    }

    @Test
    public void removeAllTags() throws Exception {
        List<TagModel> tagModels = spy(ArrayList.class);
        TagModel tagModel = new TagModel();
        tagModel.setName("321");
        tagModels.add(tagModel);
        when(taggingWorkflowFactory.build(sessionHelper)).thenReturn(taggingWorkflow);
        vcsdkUtils.removeAllTags(tagModels,sessionHelper);
    }

    @Test
    public void configureIscsi() throws Exception {
        Map<String, String> vmKernel = spy(HashMap.class);
        vmKernel.put("device", "321");
        List<Map<String, Object>> ethPorts = spy(ArrayList.class);
        Map<String, Object> ethPort = spy(HashMap.class);
        ethPort.put("321", "321");
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
        boundVmKernel();
        addIscsiSendTargets();
        when(hostMO.getName()).thenReturn("321");
        vcsdkUtils.configureIscsi("321", vmKernel, ethPorts);
    }

    @Test
    public void boundVmKernel() throws Exception {
        Map<String, String> vmKernel = spy(HashMap.class);
        vmKernel.put("device", "321");
        IscsiManagerMO iscsiManagerMo = mock(IscsiManagerMO.class);
        for (int i = 0; i <2 ; i++) {
            if (i == 0) {
                when(hostMO.getIscsiManagerMo()).thenReturn(iscsiManagerMo);
                List<IscsiPortInfo> iscsiPortInfos = spy(ArrayList.class);
                IscsiPortInfo iscsiPortInfo = spy(IscsiPortInfo.class);
                iscsiPortInfo.setVnicDevice("321");
                iscsiPortInfos.add(iscsiPortInfo);
                when(iscsiManagerMo.queryBoundVnics("321")).thenReturn(iscsiPortInfos);
                vcsdkUtils.boundVmKernel(hostMO, vmKernel, "321");
            }
            if (i==1) {
                when(hostMO.getIscsiManagerMo()).thenReturn(iscsiManagerMo);
                List<IscsiPortInfo> iscsiPortInfos = spy(ArrayList.class);
                IscsiPortInfo iscsiPortInfo = spy(IscsiPortInfo.class);
                iscsiPortInfo.setVnicDevice("456");
                when(iscsiManagerMo.queryBoundVnics("321")).thenReturn(iscsiPortInfos);
                when(hostMO.getIscsiManagerMo()).thenReturn(iscsiManagerMo);
                vcsdkUtils.boundVmKernel(hostMO, vmKernel, "321");
            }
        }
    }

    @Test
    public void addIscsiSendTargets() throws Exception {
        List<Map<String, Object>> ethPorts = spy(ArrayList.class);
        Map<String, Object> ethPort = spy(HashMap.class);
        ethPort.put("mgmtIp", "321");
        ethPorts.add(ethPort);
        when(hostMO.getHostStorageSystemMo()).thenReturn(hostStorageSystemMO);
        when(hostMO.getHostStorageSystemMo()).thenReturn(hostStorageSystemMO);
        vcsdkUtils.addIscsiSendTargets(hostMO, ethPorts, "321");
    }

    @Test
    public void deleteNfs() throws Exception {

        List<String> hostObjIds = spy(ArrayList.class);
        hostObjIds.add("321");
        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(rootFsMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(rootFsMo);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
        when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
        when(hostMO.getName()).thenReturn("321");
        testDeleteNfs();
        vcsdkUtils.deleteNfs("321", hostObjIds);
    }

    @Test
    public void testDeleteNfs() throws Exception {
        when(hostMO.getHostStorageSystemMo()).thenReturn(hostStorageSystemMO);
        NasDatastoreInfo nasDatastoreInfo = spy(NasDatastoreInfo.class);
        nasDatastoreInfo.setName("321");
        when(datastoreMO.getInfo()).thenReturn(nasDatastoreInfo);
        when(hostMO.getHostDatastoreSystemMo()).thenReturn(hostDatastoreSystemMO);
        when(hostMO.getName()).thenReturn("321");
        when(datastoreMO.getName()).thenReturn("321");
        vcsdkUtils.deleteNfs(datastoreMO, hostMO, "321");
    }

    @Test
    public void getHbaByHostObjectId() throws Exception {
        for (int i = 0; i <2 ; i++) {
            if (i == 0) {
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
                hostHostBusAdapter.setIScsiName("321");
                hostBusAdapter.add(hostHostBusAdapter);
                when(storageDeviceInfo.getHostBusAdapter()).thenReturn(hostBusAdapter);
                vcsdkUtils.getHbaByHostObjectId("321");

            }
            if (i == 1) {
                when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
                when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
                when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
                when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
                when(hostMO.getHostStorageSystemMo()).thenReturn(hostStorageSystemMO);
                HostStorageDeviceInfo storageDeviceInfo = spy(HostStorageDeviceInfo.class);
                when(hostStorageSystemMO.getStorageDeviceInfo()).thenReturn(storageDeviceInfo);
                List<HostHostBusAdapter> hostBusAdapter = spy(ArrayList.class);
                HostFibreChannelHba hostHostBusAdapter = new HostFibreChannelHba();
                hostHostBusAdapter.setDevice("321");
                hostHostBusAdapter.setNodeWorldWideName(21l);
                hostBusAdapter.add(hostHostBusAdapter);
                when(storageDeviceInfo.getHostBusAdapter()).thenReturn(hostBusAdapter);
                vcsdkUtils.getHbaByHostObjectId("321");
            }
        }
    }

    @Test
    public void getHbasByHostObjectId() throws Exception {

        for (int i = 0; i <2 ; i++) {
            if (i == 0) {
                when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
                when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
                when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
                when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
                when(hostMO.getHostStorageSystemMo()).thenReturn(hostStorageSystemMO);
                when(hostMO.getHostStorageSystemMo()).thenReturn(hostStorageSystemMO);
                HostStorageDeviceInfo storageDeviceInfo = spy(HostStorageDeviceInfo.class);
                when(hostStorageSystemMO.getStorageDeviceInfo()).thenReturn(storageDeviceInfo);
                List<HostHostBusAdapter> hostBusAdapter = spy(ArrayList.class);
                HostInternetScsiHba hostHostBusAdapter = new HostInternetScsiHba();
                hostHostBusAdapter.setDevice("321");
                hostHostBusAdapter.setIScsiName("321");
                hostBusAdapter.add(hostHostBusAdapter);
                when(storageDeviceInfo.getHostBusAdapter()).thenReturn(hostBusAdapter);
                vcsdkUtils.getHbasByHostObjectId("321");
            }
            if (i == 1) {
                when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
                when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
                when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
                when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
                when(hostMO.getHostStorageSystemMo()).thenReturn(hostStorageSystemMO);
                HostStorageDeviceInfo storageDeviceInfo = spy(HostStorageDeviceInfo.class);
                when(hostStorageSystemMO.getStorageDeviceInfo()).thenReturn(storageDeviceInfo);
                List<HostHostBusAdapter> hostBusAdapter = spy(ArrayList.class);
                HostFibreChannelHba hostHostBusAdapter = new HostFibreChannelHba();
                hostHostBusAdapter.setDevice("321");
                hostHostBusAdapter.setNodeWorldWideName(21l);
                hostBusAdapter.add(hostHostBusAdapter);
                when(storageDeviceInfo.getHostBusAdapter()).thenReturn(hostBusAdapter);
                vcsdkUtils.getHbasByHostObjectId("321");
            }
        }

    }

    @Test
    public void getHbasByClusterObjectId() throws Exception {
        for (int i = 0; i <2 ; i++) {
            if (i == 0) {
                when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
                when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
                when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
                when(clusterMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(clusterMO);
                when(clusterMO.getClusterHosts()).thenReturn(list);
                when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
                when(hostMO.getHostStorageSystemMo()).thenReturn(hostStorageSystemMO);
                HostStorageDeviceInfo storageDeviceInfo = spy(HostStorageDeviceInfo.class);
                when(hostStorageSystemMO.getStorageDeviceInfo()).thenReturn(storageDeviceInfo);
                List<HostHostBusAdapter> hostBusAdapter = spy(ArrayList.class);
                HostInternetScsiHba hostHostBusAdapter = new HostInternetScsiHba();
                hostHostBusAdapter.setDevice("321");
                hostHostBusAdapter.setIScsiName("321");
                hostBusAdapter.add(hostHostBusAdapter);
                when(storageDeviceInfo.getHostBusAdapter()).thenReturn(hostBusAdapter);
                vcsdkUtils.getHbasByClusterObjectId("321");
            }
            if (i == 1) {
                when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
                when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
                when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
                when(clusterMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(clusterMO);
                when(clusterMO.getClusterHosts()).thenReturn(list);
                when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
                when(hostMO.getHostStorageSystemMo()).thenReturn(hostStorageSystemMO);
                HostStorageDeviceInfo storageDeviceInfo = spy(HostStorageDeviceInfo.class);
                when(hostStorageSystemMO.getStorageDeviceInfo()).thenReturn(storageDeviceInfo);
                List<HostHostBusAdapter> hostBusAdapter = spy(ArrayList.class);
                HostFibreChannelHba hostHostBusAdapter = new HostFibreChannelHba();
                hostHostBusAdapter.setDevice("321");
                hostHostBusAdapter.setNodeWorldWideName(21l);
                hostBusAdapter.add(hostHostBusAdapter);
                when(storageDeviceInfo.getHostBusAdapter()).thenReturn(hostBusAdapter);
                vcsdkUtils.getHbasByClusterObjectId("321");
            }
        }
    }

    @Test
    public void testConnectivity() throws VcenterException {
        List<Map<String, Object>> ethPorts = spy(ArrayList.class);
        Map<String, Object> ethPort = spy(HashMap.class);
        ethPort.put("mgmtIp", "321");
        ethPorts.add(ethPort);
        Map<String, String> vmKernel = spy(HashMap.class);
        vmKernel.put("device", "321");
        when(VmodlContext
            .initContext(new String[] {"com.vmware.vim.binding.vim", "com.vmware.vim.binding.vmodl.reflect"})).thenReturn(context);
        vcsdkUtils.testConnectivity("321", ethPorts, vmKernel, vCenterInfo);
    }

    @Test
    public void xmlFormat() throws ParserConfigurationException {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
            "\t<returnsms>\n" +
            "\t\t\t <returnstatus>Success</returnstatus>\n" +
            "\t\t\t <message>ok</message>\n" +
            "\t\t\t <remainpoint>9118</remainpoint>\n" +
            "\t\t\t <taskID>4197401</taskID>\n" +
            "\t\t\t <successCounts>1</successCounts>\n" +
            "\t </returnsms>";
        vcsdkUtils.xmlFormat(xml);
    }

    @Test
    public void hasVmOnDatastore() throws Exception {
        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
        List<ManagedObjectReference> vms = spy(ArrayList.class);
        vms.add(managedObjectReference);
        when(datastoreMO.getVm()).thenReturn(vms);
        vcsdkUtils.hasVmOnDatastore("321");
    }

    @Test
    public void getHostName() throws Exception {
        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(hostMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(hostMO);
        when(hostMO.getName()).thenReturn("321");
        vcsdkUtils.getHostName("321");

    }

    @Test
    public void getClusterName() throws Exception {
        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(clusterMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(clusterMO);
        when(clusterMO.getName()).thenReturn("321");
        vcsdkUtils.getClusterName("321");
    }

    @Test
    public void getDataStoreName() throws Exception {
        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
        when(datastoreMO.getName()).thenReturn("321");
        vcsdkUtils.getDataStoreName("321");
    }

    @Test
    public void refreshDatastore() throws Exception {
        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(datastoreMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(datastoreMO);
        vcsdkUtils.refreshDatastore("321");
    }

    @Test
    public void refreshStorageSystem() throws Exception {
        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        vcsdkUtils.refreshStorageSystem("321");
    }

    @Test
    public void getHostByVmObjectId() throws Exception {
        when(vcConnectionHelper.objectId2Serverguid("321")).thenReturn("321");
        when(vcConnectionHelper.getServerContext("321")).thenReturn(vmwareContext);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(virtualMachineMOFactory.build(vmwareContext, managedObjectReference)).thenReturn(virtualMachineMo);
        when(virtualMachineMo.getRunningHost()).thenReturn(hostMO);
        when(vcConnectionHelper.objectId2Mor("321")).thenReturn(managedObjectReference);
        when(hostMO.getName()).thenReturn("321");
        vcsdkUtils.getHostByVmObjectId("321");
    }
}