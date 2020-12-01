package com.dmeplugin.vmware.mo;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.dmeplugin.vmware.util.ClusterMOFactory;
import com.dmeplugin.vmware.util.VmwareClient;
import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.ClusterDasConfigInfo;
import com.vmware.vim25.HostConfigManager;
import com.vmware.vim25.HostNetworkInfo;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.VirtualNicManagerNetConfig;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName HostMOTest.java
 * @Description TODO
 * @createTime 2020年12月01日 10:36:00
 */
public class HostMOTest {
    @Mock
    private VmwareContext context;
    @Mock
    private ManagedObjectReference mor;
    @Mock
    private ClusterMOFactory clusterMOFactory;
    @InjectMocks
    private HostMO hostMo = new HostMO();

    private  VmwareClient vmwareClient;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        vmwareClient = mock(VmwareClient.class);
        when(context.getVimClient()).thenReturn(vmwareClient);
    }

    @Test
    public void getHostConfigManager() throws Exception {
        HostConfigManager configManager = spy(HostConfigManager.class);
        when(vmwareClient.getDynamicProperty(mor, "configManager")).thenReturn(configManager);
        hostMo.getHostConfigManager();
    }

    @Test
    public void getHostVirtualNicManagerNetConfig() throws Exception {
        List<VirtualNicManagerNetConfig> list = new ArrayList<>();
        when(vmwareClient.getDynamicProperty(mor, "config.virtualNicManagerInfo.netConfig")).thenReturn(list);
        hostMo.getHostVirtualNicManagerNetConfig();
    }

    @Test
    public void getHostNetworkInfo() throws Exception {
        HostNetworkInfo hostNetworkInfo = new HostNetworkInfo();
        when(vmwareClient.getDynamicProperty(mor, "config.network")).thenReturn(hostNetworkInfo);
        hostMo.getHostNetworkInfo();
    }

    @Test
    public void getHyperHostName() throws Exception {
        when(vmwareClient.getDynamicProperty(mor, "name")).thenReturn("aa");
        hostMo.getHyperHostName();
    }

    @Test
    public void getDasConfig() throws Exception {
        ManagedObjectReference mor = mock(ManagedObjectReference.class);
        mor.setType("ClusterComputeResource");
        when(vmwareClient.getDynamicProperty(mor, "parent")).thenReturn(mor);
        hostMo.getDasConfig();
    }

    @Test
    public void isHaEnabled() throws Exception {
        ManagedObjectReference parent = mock(ManagedObjectReference.class);
        mor.setType("ClusterComputeResource");
        when(vmwareClient.getDynamicProperty(mor, "parent")).thenReturn(parent);
        hostMo.isHaEnabled();
    }

    @Test
    public void setRestartPriorityForVm() throws Exception {
        VirtualMachineMO vmMo = mock(VirtualMachineMO.class);
        ManagedObjectReference parent = mock(ManagedObjectReference.class);
        when(vmwareClient.getDynamicProperty(mor, "parent")).thenReturn(parent);
        String priority = "1";
        hostMo.setRestartPriorityForVm(vmMo, priority);
    }

    @Test
    public void getHostStorageSystemMo() throws Exception {
        hostMo.getHostStorageSystemMo();
    }

    @Test
    public void getHostDatastoreSystemMo() throws Exception{
        hostMo.getHostDatastoreSystemMo();
    }

    @Test
    public void getHostAdvanceOptionMo() throws Exception{
        HostConfigManager hostConfigManager = mock(HostConfigManager.class);
        when(vmwareClient.getDynamicProperty(mor, "configManager")).thenReturn(hostConfigManager);
        hostMo.getHostAdvanceOptionMo();
    }

    @Test
    public void getHostKernelModuleSystemMo() throws Exception{
        HostConfigManager hostConfigManager = mock(HostConfigManager.class);
        when(vmwareClient.getDynamicProperty(mor, "configManager")).thenReturn(hostConfigManager);
        hostMo.getHostKernelModuleSystemMo();
    }

    @Test
    public void getIscsiManagerMo() throws Exception{
        HostConfigManager hostConfigManager = mock(HostConfigManager.class);
        when(vmwareClient.getDynamicProperty(mor, "configManager")).thenReturn(hostConfigManager);
        hostMo.getIscsiManagerMo();
    }

    @Test
    public void getHostNetworkSystemMo() throws Exception{
        HostConfigManager hostConfigManager = mock(HostConfigManager.class);
        when(vmwareClient.getDynamicProperty(mor, "configManager")).thenReturn(hostConfigManager);
        when(hostConfigManager.getNetworkSystem()).thenReturn(mock(ManagedObjectReference.class));
        hostMo.getHostNetworkSystemMo();
    }

    @Test
    public void getHyperHostDatacenter() throws Exception{
    }

    @Test
    public void getHyperHostOwnerResourcePool() throws Exception{
    }

    @Test
    public void getHyperHostCluster() throws Exception{
    }

    @Test
    public void getHostAboutInfo() throws Exception{
    }

    @Test
    public void getHostType() throws Exception{
    }

    @Test
    public void getHostName() throws Exception{
    }

    @Test
    public void listVmsOnHyperHost() throws Exception{
    }

    @Test
    public void findVmOnHyperHost() throws Exception{
    }

    @Test
    public void getVmPropertiesOnHyperHost() throws Exception{
    }

    @Test
    public void getDatastorePropertiesOnHyperHost() throws Exception{
    }

    @Test
    public void getDatastoreMountsOnHost() throws Exception{
    }

    @Test
    public void getExistingDataStoreOnHost() throws Exception{
    }

    @Test
    public void mountDatastore() throws Exception{
    }

    @Test
    public void unmountDatastore() throws Exception{
    }

    @Test
    public void getHyperHostNetworkSummary() throws Exception{
    }

    @Test
    public void getRecommendedDiskController() throws Exception{
    }

    @Test
    public void getHostNetworks() throws Exception{
    }

    @Test
    public void getNetworkName() throws Exception{
    }
}