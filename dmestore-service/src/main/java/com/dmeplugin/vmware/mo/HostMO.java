// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package com.dmeplugin.vmware.mo;

import com.dmeplugin.vmware.util.ClusterMOFactory;
import com.dmeplugin.vmware.util.Pair;
import com.dmeplugin.vmware.util.VmwareClient;
import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;

public class HostMO extends BaseMO implements VmwareHypervisorHost {
    private static final Logger logger = LoggerFactory.getLogger(HostMO.class);

    private static final String CLUSTER_COMPUTE_RESOURCE = "ClusterComputeResource";

    Map<String, VirtualMachineMO> vmCache = new HashMap<>();

    private ClusterMOFactory clusterMOFactory = ClusterMOFactory.getInstance();

    public HostMO() {
    }

    public HostMO(VmwareContext context, ManagedObjectReference morHost) {
        super(context, morHost);
    }

    public HostMO(VmwareContext context, String morType, String morValue) {
        super(context, morType, morValue);
    }

    public HostMO(VmwareContext context, String hostName) throws Exception {
        super(context, null);
        mor = this.context.getVimClient().getDecendentMoRef(this.context.getRootFolder(), "HostSystem", hostName);
        if (mor == null) {
            logger.error("Unable to locate host " + hostName);
        }
    }

    public HostConfigManager getHostConfigManager() throws Exception {
        return (HostConfigManager) context.getVimClient().getDynamicProperty(mor, "configManager");
    }

    public List<VirtualNicManagerNetConfig> getHostVirtualNicManagerNetConfig() throws Exception {
        return context.getVimClient().getDynamicProperty(mor, "config.virtualNicManagerInfo.netConfig");
    }

    public HostNetworkInfo getHostNetworkInfo() throws Exception {
        VmwareClient vmwareClient = context.getVimClient();
        HostNetworkInfo hostNetworkInfo = vmwareClient.getDynamicProperty(mor, "config.network");
        return hostNetworkInfo;
    }

    @Override
    public String getHyperHostName() throws Exception {
        return getName();
    }

    @Override
    public ClusterDasConfigInfo getDasConfig() throws Exception {
        ManagedObjectReference morParent = getParentMor();
        if (CLUSTER_COMPUTE_RESOURCE.equals(morParent.getType())) {
            ClusterMO clusterMo = clusterMOFactory.build(context, morParent);
            return clusterMo.getDasConfig();
        }

        return null;
    }

    @Override
    public boolean isHaEnabled() throws Exception {
        ManagedObjectReference morParent = getParentMor();
        if (CLUSTER_COMPUTE_RESOURCE.equals(morParent.getType())) {
            ClusterMO clusterMo = clusterMOFactory.build(context, morParent);
            return clusterMo.isHaEnabled();
        }

        return false;
    }

    @Override
    public void setRestartPriorityForVm(VirtualMachineMO vmMo, String priority) throws Exception {
        ManagedObjectReference morParent = getParentMor();
        if (CLUSTER_COMPUTE_RESOURCE.equals(morParent.getType())) {
            ClusterMO clusterMo = new ClusterMO(context, morParent);
            clusterMo.setRestartPriorityForVm(vmMo, priority);
        }
    }

    public HostStorageSystemMO getHostStorageSystemMo() throws Exception {
        return new HostStorageSystemMO(context,
            context.getVimClient().getDynamicProperty(mor, "configManager.storageSystem"));
    }

    public HostDatastoreSystemMO getHostDatastoreSystemMo() throws Exception {
        return new HostDatastoreSystemMO(context,
            context.getVimClient().getDynamicProperty(mor, "configManager.datastoreSystem"));
    }

    public HostAdvanceOptionMO getHostAdvanceOptionMo() throws Exception {
        HostConfigManager configMgr = getHostConfigManager();
        return new HostAdvanceOptionMO(context, configMgr.getAdvancedOption());
    }

    public HostKernelModuleSystemMO getHostKernelModuleSystemMo() throws Exception {
        HostConfigManager configMgr = getHostConfigManager();
        return new HostKernelModuleSystemMO(context, configMgr.getKernelModuleSystem());
    }

    public IscsiManagerMO getIscsiManagerMo() throws Exception {
        HostConfigManager configMgr = getHostConfigManager();
        return new IscsiManagerMO(context, configMgr.getIscsiManager());
    }

    public HostNetworkSystemMO getHostNetworkSystemMo() throws Exception {
        HostConfigManager configMgr = getHostConfigManager();
        return new HostNetworkSystemMO(context, configMgr.getNetworkSystem());
    }

    @Override
    public ManagedObjectReference getHyperHostDatacenter() throws Exception {
        Pair<DatacenterMO, String> dcPair = DatacenterMO.getOwnerDatacenter(getContext(), getMor());
        assert dcPair != null;
        return dcPair.first().getMor();
    }

    @Override
    public ManagedObjectReference getHyperHostOwnerResourcePool() throws Exception {
        ManagedObjectReference morComputerResource = context.getVimClient().getDynamicProperty(mor, "parent");
        return (ManagedObjectReference) context.getVimClient().getDynamicProperty(morComputerResource, "resourcePool");
    }

    @Override
    public ManagedObjectReference getHyperHostCluster() throws Exception {
        ManagedObjectReference morParent = context.getVimClient().getDynamicProperty(mor, "parent");
        if (CLUSTER_COMPUTE_RESOURCE.equalsIgnoreCase(morParent.getType())) {
            return morParent;
        }

        assert false;
        throw new Exception("Standalone host is not supported");
    }

    public AboutInfo getHostAboutInfo() throws Exception {
        return (AboutInfo) context.getVimClient().getDynamicProperty(mor, "config.product");
    }

    public VmwareHostType getHostType() throws Exception {
        AboutInfo aboutInfo = getHostAboutInfo();
        String flag = "VMware ESXi";
        if (flag.equals(aboutInfo.getName())) {
            return VmwareHostType.ESXi;
        } else if (flag.equals(aboutInfo.getName())) {
            return VmwareHostType.ESX;
        }

        throw new Exception("Unrecognized VMware host type " + aboutInfo.getName());
    }

    public String getHostName() throws Exception {
        return (String) context.getVimClient().getDynamicProperty(mor, "name");
    }

    @Override
    public synchronized List<VirtualMachineMO> listVmsOnHyperHost(String vmName) throws Exception {
        List<VirtualMachineMO> vms = new ArrayList<>();
        if (vmName != null && !vmName.isEmpty()) {
            vms.add(findVmOnHyperHost(vmName));
        } else {
            loadVmCache();
            vms.addAll(vmCache.values());
        }
        return vms;
    }

    @Override
    public synchronized VirtualMachineMO findVmOnHyperHost(String vmName) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("find VM " + vmName + " on host");
        }

        VirtualMachineMO vmMo = vmCache.get(vmName);
        if (vmMo != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("VM " + vmName + " found in host cache");
            }
            return vmMo;
        }

        logger.info("VM " + vmName + " not found in host cache");
        loadVmCache();

        return vmCache.get(vmName);
    }

    private boolean isUserVmInternalCsName(String vmInternalCsName) {
        String pattern = "^[i][-][0-9]+[-][0-9]+[-]";
        Pattern p = Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(vmInternalCsName);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }

    private void loadVmCache() throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("load VM cache on host");
        }

        vmCache.clear();

        int key = getCustomFieldKey("VirtualMachine", CustomFieldConstants.CLOUD_VM_INTERNAL_NAME);
        if (key == 0) {
            logger.warn("Custom field " + CustomFieldConstants.CLOUD_VM_INTERNAL_NAME + " is not registered ?!");
        }

        ObjectContent[] ocs = getVmPropertiesOnHyperHost(new String[] {"name", "value[" + key + "]"});
        if (ocs != null && ocs.length > 0) {
            for (ObjectContent oc : ocs) {
                List<DynamicProperty> props = oc.getPropSet();
                if (props != null) {
                    String vmVcenterName = null;
                    String vmInternalCsName = null;
                    for (DynamicProperty prop : props) {
                        if ("name".equals(prop.getName())) {
                            vmVcenterName = prop.getVal().toString();
                        } else if (prop.getName().startsWith("value[")) {
                            if (prop.getVal() != null) {
                                vmInternalCsName = ((CustomFieldStringValue) prop.getVal()).getValue();
                            }
                        }
                    }
                    String vmName;
                    if (vmInternalCsName != null && isUserVmInternalCsName(vmInternalCsName)) {
                        vmName = vmInternalCsName;
                    } else {
                        vmName = vmVcenterName;
                    }

                    if (logger.isTraceEnabled()) {
                        logger.trace("put " + vmName + " into host cache");
                    }

                    vmCache.put(vmName, new VirtualMachineMO(context, oc.getObj()));
                }
            }
        }
    }

    @Override
    public ObjectContent[] getVmPropertiesOnHyperHost(String[] propertyPaths) throws Exception {
        PropertySpec pSpec = new PropertySpec();
        pSpec.setType("VirtualMachine");
        pSpec.getPathSet().addAll(Arrays.asList(propertyPaths));

        TraversalSpec host2VmTraversal = new TraversalSpec();
        host2VmTraversal.setType("HostSystem");
        host2VmTraversal.setPath("vm");
        host2VmTraversal.setName("host2VmTraversal");

        ObjectSpec oSpec = new ObjectSpec();
        oSpec.setObj(mor);
        oSpec.setSkip(Boolean.TRUE);
        oSpec.getSelectSet().add(host2VmTraversal);

        PropertyFilterSpec pfSpec = new PropertyFilterSpec();
        pfSpec.getPropSet().add(pSpec);
        pfSpec.getObjectSet().add(oSpec);
        List<PropertyFilterSpec> pfSpecArr = new ArrayList<>();
        pfSpecArr.add(pfSpec);

        List<ObjectContent> properties = context.getService()
            .retrieveProperties(context.getPropertyCollector(), pfSpecArr);

        if (logger.isTraceEnabled()) {
            logger.trace("vCenter API trace - retrieveProperties() done");
        }
        return properties.toArray(new ObjectContent[properties.size()]);
    }

    @Override
    public ObjectContent[] getDatastorePropertiesOnHyperHost(String[] propertyPaths) throws Exception {
        PropertySpec pSpec = new PropertySpec();
        pSpec.setType("Datastore");
        pSpec.getPathSet().addAll(Arrays.asList(propertyPaths));

        TraversalSpec host2DatastoreTraversal = new TraversalSpec();
        host2DatastoreTraversal.setType("HostSystem");
        host2DatastoreTraversal.setPath("datastore");
        host2DatastoreTraversal.setName("host2DatastoreTraversal");

        ObjectSpec oSpec = new ObjectSpec();
        oSpec.setObj(mor);
        oSpec.setSkip(Boolean.TRUE);
        oSpec.getSelectSet().add(host2DatastoreTraversal);

        PropertyFilterSpec pfSpec = new PropertyFilterSpec();
        pfSpec.getPropSet().add(pSpec);
        pfSpec.getObjectSet().add(oSpec);
        List<PropertyFilterSpec> pfSpecArr = new ArrayList<PropertyFilterSpec>();
        pfSpecArr.add(pfSpec);

        List<ObjectContent> properties = context.getService()
            .retrieveProperties(context.getPropertyCollector(), pfSpecArr);
        return properties.toArray(new ObjectContent[properties.size()]);
    }

    public List<Pair<ManagedObjectReference, String>> getDatastoreMountsOnHost() throws Exception {
        List<Pair<ManagedObjectReference, String>> mounts = new ArrayList<Pair<ManagedObjectReference, String>>();

        ObjectContent[] ocs = getDatastorePropertiesOnHyperHost(
            new String[] {String.format("host[\"%s\"].mountInfo.path", mor.getValue())});
        if (ocs != null) {
            for (ObjectContent oc : ocs) {
                Pair<ManagedObjectReference, String> mount = new Pair<ManagedObjectReference, String>(oc.getObj(),
                    oc.getPropSet().get(0).getVal().toString());
                mounts.add(mount);
            }
        }
        return mounts;
    }

    public ManagedObjectReference getExistingDataStoreOnHost(boolean vmfsDatastore, String hostAddress, int hostPort,
        String path, HostDatastoreSystemMO hostDatastoreSystemMo) {
        List<ManagedObjectReference> morArray;
        try {
            morArray = hostDatastoreSystemMo.getDatastores();
        } catch (Exception e) {
            logger.info("Failed to retrieve list of Managed Object References");
            return null;
        }
        if (morArray.size() > 0) {
            int i;
            for (i = 0; i < morArray.size(); i++) {
                NasDatastoreInfo nasDadastore;
                try {
                    nasDadastore = hostDatastoreSystemMo.getNasDatastoreInfo(morArray.get(i));
                    if (nasDadastore != null) {
                        if (nasDadastore.getNas().getRemoteHost().equalsIgnoreCase(hostAddress) && nasDadastore.getNas()
                            .getRemotePath()
                            .equalsIgnoreCase(path)) {
                            return morArray.get(i);
                        }
                    }
                } catch (Exception e) {
                    logger.info("Encountered exception when retrieving nas datastore info");
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public ManagedObjectReference mountDatastore(boolean vmfsDatastore, String poolHostAddress, int poolHostPort,
        String poolPath, String poolUuid) throws Exception {
        HostDatastoreSystemMO hostDatastoreSystemMo = getHostDatastoreSystemMo();
        ManagedObjectReference morDatastore = hostDatastoreSystemMo.findDatastore(poolUuid);
        if (morDatastore == null) {
            if (!vmfsDatastore) {
                try {
                    morDatastore = hostDatastoreSystemMo.createNfsDatastore(poolHostAddress, poolHostPort, poolPath,
                        poolUuid, null, null, null);
                } catch (AlreadyExistsFaultMsg e) {
                    return getExistingDataStoreOnHost(vmfsDatastore, poolHostAddress, poolHostPort, poolPath,
                        hostDatastoreSystemMo);
                } catch (Exception e) {
                    throw new Exception("Creation of NFS datastore on vCenter failed.");
                }
                if (morDatastore == null) {
                    String msg = "Unable to create NFS datastore. host: " + poolHostAddress + ", port: " + poolHostPort
                        + ", path: " + poolPath + ", uuid: " + poolUuid;
                    logger.error(msg);
                    throw new Exception(msg);
                }
            } else {
                morDatastore = context.getDatastoreMorByPath(poolPath);
                if (morDatastore == null) {
                    String msg = "Unable to create VMFS datastore.";
                    throw new Exception(msg);
                }

                DatastoreMO dsMo = new DatastoreMO(context, morDatastore);
                dsMo.setCustomFieldValue(CustomFieldConstants.CLOUD_UUID, poolUuid);
            }
        }

        if (logger.isTraceEnabled()) {
            logger.trace("vCenter API trace - mountDatastore() done(successfully)");
        }

        return morDatastore;
    }

    @Override
    public void unmountDatastore(String uuid) throws Exception {
        HostDatastoreSystemMO hostDatastoreSystemMo = getHostDatastoreSystemMo();
        if (!hostDatastoreSystemMo.deleteDatastore(uuid)) {
            String msg = "Unable to unmount datastore. uuid: " + uuid;
            logger.error(msg);

            if (logger.isTraceEnabled()) {
                logger.trace("vCenter API trace - unmountDatastore() done(failed)");
            }
            throw new Exception(msg);
        }
    }

    @Override
    public VmwareHypervisorHostNetworkSummary getHyperHostNetworkSummary(String managementPortGroup) throws Exception {
        VmwareHypervisorHostNetworkSummary summary = new VmwareHypervisorHostNetworkSummary();

        if (getHostType() == VmwareHostType.ESXi) {
            List<VirtualNicManagerNetConfig> netConfigs = context.getVimClient()
                .getDynamicProperty(mor, "config.virtualNicManagerInfo.netConfig");
            assert netConfigs != null;

            String dvPortGroupKey;
            String portGroup;
            for (VirtualNicManagerNetConfig netConfig : netConfigs) {
                if ("management".equals(netConfig.getNicType())) {
                    for (HostVirtualNic nic : netConfig.getCandidateVnic()) {
                        portGroup = nic.getPortgroup();
                        if (portGroup == null || portGroup.isEmpty()) {
                            dvPortGroupKey = nic.getSpec().getDistributedVirtualPort().getPortgroupKey();
                            portGroup = getNetworkName(dvPortGroupKey);
                        }
                        if (portGroup.equalsIgnoreCase(managementPortGroup)) {
                            summary.setHostIp(nic.getSpec().getIp().getIpAddress());
                            summary.setHostNetmask(nic.getSpec().getIp().getSubnetMask());
                            summary.setHostMacAddress(nic.getSpec().getMac());
                            return summary;
                        }
                    }
                }
            }
        } else {
            List<HostVirtualNic> hostNics = context.getVimClient()
                .getDynamicProperty(mor, "config.network.consoleVnic");

            if (hostNics != null) {
                for (HostVirtualNic vnic : hostNics) {
                    if (vnic.getPortgroup().equals(managementPortGroup)) {
                        summary.setHostIp(vnic.getSpec().getIp().getIpAddress());
                        summary.setHostNetmask(vnic.getSpec().getIp().getSubnetMask());
                        summary.setHostMacAddress(vnic.getSpec().getMac());
                        return summary;
                    }
                }
            }
        }
        throw new Exception("Unable to find management port group " + managementPortGroup);
    }

    @Override
    public String getRecommendedDiskController(String guestOsId) throws Exception {
        ManagedObjectReference morParent = getParentMor();
        if (CLUSTER_COMPUTE_RESOURCE.equals(morParent.getType())) {
            ClusterMO clusterMo = new ClusterMO(context, morParent);
            return clusterMo.getRecommendedDiskController(guestOsId);
        }
        return null;
    }

    public List<ManagedObjectReference> getHostNetworks() throws Exception {
        return context.getVimClient().getDynamicProperty(mor, "network");
    }

    public String getNetworkName(String netMorVal) throws Exception {
        String networkName = "";
        List<ManagedObjectReference> hostNetworks = getHostNetworks();
        for (ManagedObjectReference hostNetwork : hostNetworks) {
            if (hostNetwork.getValue().equals(netMorVal)) {
                networkName = context.getVimClient().getDynamicProperty(hostNetwork, "name");
                break;
            }
        }
        return networkName;
    }

}
