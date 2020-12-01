package com.dmeplugin.vmware.mo;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.dmeplugin.vmware.util.ClusterMOFactory;
import com.dmeplugin.vmware.util.VmwareClient;
import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.AboutInfo;
import com.vmware.vim25.ClusterDasConfigInfo;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.HostConfigManager;
import com.vmware.vim25.HostIpConfig;
import com.vmware.vim25.HostNasVolume;
import com.vmware.vim25.HostNetworkInfo;
import com.vmware.vim25.HostVirtualNic;
import com.vmware.vim25.HostVirtualNicSpec;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.NasDatastoreInfo;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.VimPortType;
import com.vmware.vim25.VirtualNicManagerNetConfig;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Mock
    Map<String, VirtualMachineMO> vmCache;

    @InjectMocks
    private HostMO hostMo;

    private VmwareClient vmwareClient;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        vmwareClient = mock(VmwareClient.class);
        when(context.getVimClient()).thenReturn(vmwareClient);
    }

    @Test
    public void getHostConfigManager() throws Exception {
        HostConfigManager configManager = mock(HostConfigManager.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("configManager"))).thenReturn(configManager);
        hostMo.getHostConfigManager();
    }

    @Test
    public void getHostVirtualNicManagerNetConfig() throws Exception {
        List<VirtualNicManagerNetConfig> list = new ArrayList<>();
        when(vmwareClient.getDynamicProperty(anyObject(), eq("config.virtualNicManagerInfo.netConfig"))).thenReturn(list);
        hostMo.getHostVirtualNicManagerNetConfig();
    }

    @Test
    public void getHostNetworkInfo() throws Exception {
        HostNetworkInfo hostNetworkInfo = mock(HostNetworkInfo.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("config.network"))).thenReturn(hostNetworkInfo);
        hostMo.getHostNetworkInfo();
    }

    @Test
    public void getHyperHostName() throws Exception {
        when(vmwareClient.getDynamicProperty(anyObject(), eq("name"))).thenReturn("aa");
        hostMo.getHyperHostName();
    }

    @Test
    public void getDasConfig() throws Exception {
        ManagedObjectReference parent = mock(ManagedObjectReference.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("parent"))).thenReturn(parent);
        when(parent.getType()).thenReturn("ClusterComputeResource");
        ClusterMO clusterMO = mock(ClusterMO.class);
        when(clusterMOFactory.build(context, parent)).thenReturn(clusterMO);
        when(clusterMO.getDasConfig()).thenReturn(mock(ClusterDasConfigInfo.class));
        hostMo.getDasConfig();
    }

    @Test
    public void isHaEnabled() throws Exception {
        ManagedObjectReference parent = mock(ManagedObjectReference.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("parent"))).thenReturn(parent);
        when(parent.getType()).thenReturn("ClusterComputeResource");
        ClusterMO clusterMO = mock(ClusterMO.class);
        when(clusterMOFactory.build(context, parent)).thenReturn(clusterMO);
        when(clusterMO.isHaEnabled()).thenReturn(true);
        hostMo.isHaEnabled();
    }

    @Test
    public void setRestartPriorityForVm() throws Exception {
        String priority = "cca";
        ManagedObjectReference parent = mock(ManagedObjectReference.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("parent"))).thenReturn(parent);
        when(parent.getType()).thenReturn("ClusterComputeResource");
        ClusterMO clusterMO = mock(ClusterMO.class);
        when(clusterMOFactory.build(context, parent)).thenReturn(clusterMO);
        when(clusterMO.isHaEnabled()).thenReturn(true);
        VirtualMachineMO vmMo = mock(VirtualMachineMO.class);
        doNothing().when(clusterMO).setRestartPriorityForVm(anyObject(), eq(priority));
        hostMo.setRestartPriorityForVm(vmMo, priority);
    }

    @Test
    public void getHostStorageSystemMo() throws Exception {
        hostMo.getHostStorageSystemMo();
    }

    @Test
    public void getHostDatastoreSystemMo() throws Exception {
        hostMo.getHostDatastoreSystemMo();
    }

    @Test
    public void getHostAdvanceOptionMo() throws Exception {
        HostConfigManager hostConfigManager = mock(HostConfigManager.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("configManager"))).thenReturn(hostConfigManager);
        when(hostConfigManager.getAdvancedOption()).thenReturn(mock(ManagedObjectReference.class));
        hostMo.getHostAdvanceOptionMo();
    }

    @Test
    public void getHostKernelModuleSystemMo() throws Exception {
        HostConfigManager hostConfigManager = mock(HostConfigManager.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("configManager"))).thenReturn(hostConfigManager);
        when(hostConfigManager.getKernelModuleSystem()).thenReturn(mock(ManagedObjectReference.class));
        hostMo.getHostKernelModuleSystemMo();
    }

    @Test
    public void getIscsiManagerMo() throws Exception {
        HostConfigManager hostConfigManager = mock(HostConfigManager.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("configManager"))).thenReturn(hostConfigManager);
        when(hostConfigManager.getIscsiManager()).thenReturn(mock(ManagedObjectReference.class));
        hostMo.getIscsiManagerMo();
    }

    @Test
    public void getHostNetworkSystemMo() throws Exception {
        HostConfigManager hostConfigManager = mock(HostConfigManager.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("configManager"))).thenReturn(hostConfigManager);
        when(hostConfigManager.getNetworkSystem()).thenReturn(mock(ManagedObjectReference.class));
        hostMo.getHostNetworkSystemMo();
    }

    @Test
    public void getHyperHostDatacenter() throws Exception {
        when(vmwareClient.getDynamicProperty(mor, "name")).thenReturn("cc");
        hostMo.getHyperHostName();
    }

    @Test
    public void getHyperHostOwnerResourcePool() throws Exception {
        when(vmwareClient.getDynamicProperty(mor, "parent")).thenReturn(mock(ManagedObjectReference.class));
        hostMo.getHyperHostOwnerResourcePool();
    }

    @Test
    public void getHyperHostCluster() throws Exception {
        ManagedObjectReference parent = mock(ManagedObjectReference.class);
        parent.setType("ClusterComputeResource");
        when(vmwareClient.getDynamicProperty(mor, "parent")).thenReturn(parent);
        hostMo.getHyperHostCluster();
    }

    @Test
    public void getHostAboutInfo() throws Exception {
        when(vmwareClient.getDynamicProperty(mor, "config.product")).thenReturn(mock(AboutInfo.class));
        hostMo.getHostAboutInfo();
    }

    @Test
    public void getHostType() throws Exception {
        AboutInfo aboutInfo = mock(AboutInfo.class);
        aboutInfo.setName("VMware ESXi");
        when(vmwareClient.getDynamicProperty(mor, "config.product")).thenReturn(aboutInfo);
        hostMo.getHostType();
    }

    @Test
    public void getHostName() throws Exception {
        when(vmwareClient.getDynamicProperty(mor, "name")).thenReturn("cc");
        hostMo.getHostName();
    }

    @Test
    public void listVmsOnHyperHost() throws Exception {
        String vmName = "12";
        ServiceContent serviceContent = mock(ServiceContent.class);
        when(context.getServiceContent()).thenReturn(serviceContent);
        when(serviceContent.getCustomFieldsManager()).thenReturn(mock(ManagedObjectReference.class));
        VimPortType vimPortType = mock(VimPortType.class);
        when(context.getService()).thenReturn(vimPortType);
        List<ObjectContent> properties = new ArrayList();
        ObjectContent content = mock(ObjectContent.class);
        properties.add(content);
        when(vimPortType.retrieveProperties(anyObject(), anyObject())).thenReturn(properties);
        List<DynamicProperty> props = new ArrayList<>();
        DynamicProperty dynamicProperty = new DynamicProperty();
        dynamicProperty.setName("name");
        dynamicProperty.setVal(vmName);
        props.add(dynamicProperty);
        when(content.getPropSet()).thenReturn(props);
        hostMo.listVmsOnHyperHost(vmName);
    }

    @Test
    public void findVmOnHyperHost() throws Exception {
        String vmName = "12";
        try {
            hostMo.findVmOnHyperHost(vmName);
        } catch (Exception ex) {

        }
    }

    @Test
    public void getVmPropertiesOnHyperHost() throws Exception {
        String[] propertyPaths = {"11", "22"};
        List<ObjectContent> properties = new ArrayList();
        ObjectContent content = mock(ObjectContent.class);
        properties.add(content);
        VimPortType vimPortType = mock(VimPortType.class);
        when(context.getService()).thenReturn(vimPortType);
        when(vimPortType.retrieveProperties(anyObject(), anyObject())).thenReturn(properties);
        hostMo.getVmPropertiesOnHyperHost(propertyPaths);
    }

    @Test
    public void getDatastorePropertiesOnHyperHost() throws Exception {
        String[] propertyPaths = {"33", "44"};
        List<ObjectContent> properties = new ArrayList();
        ObjectContent content = mock(ObjectContent.class);
        properties.add(content);
        VimPortType vimPortType = mock(VimPortType.class);
        when(context.getService()).thenReturn(vimPortType);
        when(vimPortType.retrieveProperties(anyObject(), anyObject())).thenReturn(properties);
        hostMo.getDatastorePropertiesOnHyperHost(propertyPaths);
    }

    @Test
    public void getDatastoreMountsOnHost() throws Exception {
        when(mor.getValue()).thenReturn("cc");
        List<ObjectContent> properties = new ArrayList();
        ObjectContent content = spy(ObjectContent.class);
        content.setObj(spy(ManagedObjectReference.class));
        List<DynamicProperty> list = new ArrayList<>();
        DynamicProperty dynamicProperty = spy(DynamicProperty.class);
        dynamicProperty.setVal("kk");
        list.add(dynamicProperty);
        when(content.getPropSet()).thenReturn(list);
        properties.add(content);
        VimPortType vimPortType = mock(VimPortType.class);
        when(context.getService()).thenReturn(vimPortType);
        when(vimPortType.retrieveProperties(anyObject(), anyObject())).thenReturn(properties);

        hostMo.getDatastoreMountsOnHost();
    }

    @Test
    public void getExistingDataStoreOnHost() throws Exception {
        String hostAddress = "122";
        String path = "12";
        HostDatastoreSystemMO hostDatastoreSystemMo = new HostDatastoreSystemMO(context, mor);
        List<ManagedObjectReference> morArray = new ArrayList<>();
        ManagedObjectReference managedObjectReference = spy(ManagedObjectReference.class);
        morArray.add(managedObjectReference);
        when(hostDatastoreSystemMo.getDatastores()).thenReturn(morArray);
        NasDatastoreInfo nasDatastoreInfo = spy(NasDatastoreInfo.class);
        when(hostDatastoreSystemMo.getNasDatastoreInfo(managedObjectReference)).thenReturn(nasDatastoreInfo);
        HostNasVolume hostNasVolume = spy(HostNasVolume.class);
        when(nasDatastoreInfo.getNas()).thenReturn(hostNasVolume);
        when(hostNasVolume.getRemoteHost()).thenReturn(hostAddress);
        when(hostNasVolume.getRemotePath()).thenReturn(path);

        hostMo.getExistingDataStoreOnHost(hostAddress, path, hostDatastoreSystemMo);
    }

    @Test
    public void mountDatastore() throws Exception {
        boolean vmfsDatastore = false;
        String poolHostAddress = "10.143.132.11";
        int poolHostPort = 22;
        String poolPath = "123";
        String poolUuid = "121";
        hostMo.mountDatastore(vmfsDatastore, poolHostAddress, poolHostPort, poolPath, poolUuid);
    }

    @Test
    public void unmountDatastore() throws Exception {
        String uuid = "123";
        hostMo.unmountDatastore(uuid);
    }

    @Test
    public void getHyperHostNetworkSummary() throws Exception {
        String managementPortGroup = "123";
        AboutInfo aboutInfo = spy(AboutInfo.class);
        aboutInfo.setName("VMware ESXi");
        when(vmwareClient.getDynamicProperty(anyObject(), eq("config.product"))).thenReturn(aboutInfo);
        List<VirtualNicManagerNetConfig> netConfigList = new ArrayList<>();
        VirtualNicManagerNetConfig netConfig = spy(VirtualNicManagerNetConfig.class);
        netConfigList.add(netConfig);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("config.virtualNicManagerInfo.netConfig"))).thenReturn(
            netConfigList);
        when(netConfig.getNicType()).thenReturn("management");
        List<HostVirtualNic> nicList = new ArrayList<>();
        HostVirtualNic nic = spy(HostVirtualNic.class);
        nicList.add(nic);
        when(netConfig.getCandidateVnic()).thenReturn(nicList);
        when(nic.getPortgroup()).thenReturn(managementPortGroup);
        HostVirtualNicSpec spec = mock(HostVirtualNicSpec.class);
        when(nic.getSpec()).thenReturn(spec);
        HostIpConfig hostIpConfig = mock(HostIpConfig.class);
        when(spec.getIp()).thenReturn(hostIpConfig);
        when(hostIpConfig.getIpAddress()).thenReturn("192.168.1.5");
        when(hostIpConfig.getSubnetMask()).thenReturn("192.168.1.1");
        when(spec.getMac()).thenReturn("192:168:1:1");

        hostMo.getHyperHostNetworkSummary(managementPortGroup);
    }

    @Test
    public void getRecommendedDiskController() throws Exception {
        String guestOsId = "11";
        ManagedObjectReference parent = mock(ManagedObjectReference.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("parent"))).thenReturn(parent);
        when(parent.getType()).thenReturn("ClusterComputeResource");
        ClusterMO clusterMO = mock(ClusterMO.class);
        when(clusterMOFactory.build(context, parent)).thenReturn(clusterMO);
        when(clusterMO.getRecommendedDiskController(guestOsId)).thenReturn("aa");
        try {
            hostMo.getRecommendedDiskController(guestOsId);
        } catch (Exception ex) {
        }
    }

    @Test
    public void getHostNetworks() throws Exception {
        List<ManagedObjectReference> list = new ArrayList<>();
        ManagedObjectReference managedObjectReference = mock(ManagedObjectReference.class);
        list.add(managedObjectReference);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("network"))).thenReturn(list);

        hostMo.getHostNetworks();
    }

    @Test
    public void getNetworkName() throws Exception {
        String netMorVal = "123";
        List<ManagedObjectReference> list = new ArrayList<>();
        ManagedObjectReference managedObjectReference = mock(ManagedObjectReference.class);
        list.add(managedObjectReference);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("network"))).thenReturn(list);
        when(managedObjectReference.getValue()).thenReturn(netMorVal);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("name"))).thenReturn("sss");
        hostMo.getNetworkName(netMorVal);
    }
}