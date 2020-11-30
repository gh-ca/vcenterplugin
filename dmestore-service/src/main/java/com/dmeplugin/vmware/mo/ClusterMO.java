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

import com.dmeplugin.dmestore.exception.VcenterException;
import com.dmeplugin.dmestore.utils.StringUtil;
import com.dmeplugin.vmware.util.Pair;
import com.dmeplugin.vmware.util.VmwareContext;
import com.dmeplugin.vmware.util.VmwareHelper;
import com.google.gson.Gson;
import com.vmware.vim25.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClusterMO extends BaseMO implements VmwareHypervisorHost {
    private static final Logger s_logger = LoggerFactory.getLogger(ClusterMO.class);

    private ManagedObjectReference environmentBrowser = null;

    public ClusterMO(VmwareContext context, ManagedObjectReference morCluster) {
        super(context, morCluster);
    }

    @Override
    public String getHyperHostName() throws Exception {
        return getName();
    }

    @Override
    public ClusterDasConfigInfo getDasConfig() throws Exception {
        ClusterConfigInfoEx configInfo = getClusterConfigInfo();
        if (configInfo != null) {
            return configInfo.getDasConfig();
        }

        return null;
    }

    public ClusterConfigInfoEx getClusterConfigInfo() throws Exception {
        ClusterConfigInfoEx configInfo = context.getVimClient().getDynamicProperty(mor, "configurationEx");
        return configInfo;
    }

    @Override
    public boolean isHaEnabled() throws Exception {
        ClusterDasConfigInfo dasConfig = getDasConfig();
        if (dasConfig != null && dasConfig.isEnabled() != null && dasConfig.isEnabled().booleanValue()) {
            return true;
        }

        return false;
    }

    private String getRestartPriorityForVm(VirtualMachineMO vmMo) throws Exception {
        if (vmMo == null) {
            s_logger.debug("Failed to get restart priority for VM, invalid VM object reference");
            return null;
        }

        ManagedObjectReference vmMor = vmMo.getMor();
        if (vmMor == null || !"VirtualMachine".equals(vmMor.getType())) {
            s_logger.debug(
                "Failed to get restart priority for VM: " + vmMo.getName() + ", invalid VM object reference");
            return null;
        }

        ClusterConfigInfoEx configInfo = getClusterConfigInfo();
        if (configInfo == null) {
            s_logger.debug(
                "Failed to get restart priority for VM: " + vmMo.getName() + ", no cluster config information");
            return null;
        }

        List<ClusterDasVmConfigInfo> dasVmConfig = configInfo.getDasVmConfig();
        for (int dasVmConfigIndex = 0; dasVmConfigIndex < dasVmConfig.size(); dasVmConfigIndex++) {
            ClusterDasVmConfigInfo dasVmConfigInfo = dasVmConfig.get(dasVmConfigIndex);
            if (dasVmConfigInfo != null && dasVmConfigInfo.getKey().getValue().equals(vmMor.getValue())) {
                DasVmPriority dasVmPriority = dasVmConfigInfo.getRestartPriority();
                if (dasVmPriority != null) {
                    return dasVmPriority.value();
                } else {
                    return ClusterDasVmSettingsRestartPriority.CLUSTER_RESTART_PRIORITY.value();
                }
            }
        }

        s_logger.debug("VM: " + vmMo.getName() + " uses default restart priority in the cluster: " + getName());
        return null;
    }

    @Override
    public void setRestartPriorityForVm(VirtualMachineMO vmMo, String priority) throws Exception {
        if (vmMo == null || StringUtil.isBlank(priority)) {
            return;
        }

        if (!isHaEnabled()) {
            s_logger.debug("Couldn't set restart priority for VM: " + vmMo.getName() + ", HA disabled in the cluster");
            return;
        }

        ManagedObjectReference vmMor = vmMo.getMor();
        if (vmMor == null || !"VirtualMachine".equals(vmMor.getType())) {
            s_logger.debug(
                "Failed to set restart priority for VM: " + vmMo.getName() + ", invalid VM object reference");
            return;
        }

        String currentVmRestartPriority = getRestartPriorityForVm(vmMo);
        if (StringUtil.isNotBlank(currentVmRestartPriority) && currentVmRestartPriority.equalsIgnoreCase(priority)) {
            return;
        }

        ClusterDasVmSettings clusterDasVmSettings = new ClusterDasVmSettings();
        clusterDasVmSettings.setRestartPriority(priority);

        ClusterDasVmConfigInfo clusterDasVmConfigInfo = new ClusterDasVmConfigInfo();
        clusterDasVmConfigInfo.setKey(vmMor);
        clusterDasVmConfigInfo.setDasSettings(clusterDasVmSettings);

        ClusterDasVmConfigSpec clusterDasVmConfigSpec = new ClusterDasVmConfigSpec();
        clusterDasVmConfigSpec.setOperation(
            (StringUtil.isNotBlank(currentVmRestartPriority)) ? ArrayUpdateOperation.EDIT : ArrayUpdateOperation.ADD);
        clusterDasVmConfigSpec.setInfo(clusterDasVmConfigInfo);

        ClusterConfigSpecEx clusterConfigSpecEx = new ClusterConfigSpecEx();
        ClusterDasConfigInfo clusterDasConfigInfo = new ClusterDasConfigInfo();
        clusterConfigSpecEx.setDasConfig(clusterDasConfigInfo);
        clusterConfigSpecEx.getDasVmConfigSpec().add(clusterDasVmConfigSpec);

        ManagedObjectReference morTask = context.getService()
            .reconfigureComputeResourceTask(mor, clusterConfigSpecEx, true);

        boolean result = context.getVimClient().waitForTask(morTask);

        if (result) {
            context.waitForTaskProgressDone(morTask);
        } else {
            s_logger.error(
                "Set restart priority failed for VM: " + vmMo.getName() + " due to " + TaskMO.getTaskFailureInfo(
                    context, morTask));
        }
    }

    @Override
    public ManagedObjectReference getHyperHostDatacenter() throws Exception {
        Pair<DatacenterMO, String> dcPair = DatacenterMO.getOwnerDatacenter(getContext(), getMor());
        assert (dcPair != null);
        return dcPair.first().getMor();
    }

    @Override
    public ManagedObjectReference getHyperHostOwnerResourcePool() throws Exception {
        return (ManagedObjectReference) context.getVimClient().getDynamicProperty(getMor(), "resourcePool");
    }

    @Override
    public ManagedObjectReference getHyperHostCluster() throws Exception {
        return mor;
    }

    @Override
    public synchronized List<VirtualMachineMO> listVmsOnHyperHost(String vmName) throws Exception {
        List<VirtualMachineMO> vms = new ArrayList<>();
        List<ManagedObjectReference> hosts = context.getVimClient().getDynamicProperty(mor, "host");
        if (hosts != null && hosts.size() > 0) {
            for (ManagedObjectReference morHost : hosts) {
                HostMO hostMo = new HostMO(context, morHost);
                vms.addAll(hostMo.listVmsOnHyperHost(vmName));
            }
        }
        return vms;
    }

    @Override
    public VirtualMachineMO findVmOnHyperHost(String name) throws Exception {
        int key = getCustomFieldKey("VirtualMachine", CustomFieldConstants.CLOUD_VM_INTERNAL_NAME);
        if (key == 0) {
            s_logger.warn("Custom field " + CustomFieldConstants.CLOUD_VM_INTERNAL_NAME + " is not registered ?!");
        }

        String instanceNameCustomField = "value[" + key + "]";
        ObjectContent[] ocs = getVmPropertiesOnHyperHost(new String[] {"name", instanceNameCustomField});
        return HypervisorHostHelper.findVmFromObjectContent(context, ocs, name, instanceNameCustomField);
    }

    @Override
    public ObjectContent[] getVmPropertiesOnHyperHost(String[] propertyPaths) throws Exception {
        PropertySpec pSpec = new PropertySpec();
        pSpec.setType("VirtualMachine");
        pSpec.getPathSet().addAll(Arrays.asList(propertyPaths));

        TraversalSpec host2VmFolderTraversal = new TraversalSpec();
        host2VmFolderTraversal.setType("HostSystem");
        host2VmFolderTraversal.setPath("vm");
        host2VmFolderTraversal.setName("host2VmFolderTraversal");

        TraversalSpec cluster2HostFolderTraversal = new TraversalSpec();
        cluster2HostFolderTraversal.setType("ClusterComputeResource");
        cluster2HostFolderTraversal.setPath("host");
        cluster2HostFolderTraversal.setName("cluster2HostFolderTraversal");
        cluster2HostFolderTraversal.getSelectSet().add(host2VmFolderTraversal);

        ObjectSpec oSpec = new ObjectSpec();
        oSpec.setObj(getMor());
        oSpec.setSkip(Boolean.TRUE);
        oSpec.getSelectSet().add(cluster2HostFolderTraversal);

        PropertyFilterSpec pfSpec = new PropertyFilterSpec();
        pfSpec.getPropSet().add(pSpec);
        pfSpec.getObjectSet().add(oSpec);
        List<PropertyFilterSpec> pfSpecArr = new ArrayList<PropertyFilterSpec>();
        pfSpecArr.add(pfSpec);

        List<ObjectContent> properties = context.getService()
            .retrieveProperties(context.getPropertyCollector(), pfSpecArr);

        if (s_logger.isTraceEnabled()) {
            s_logger.trace("vCenter API trace - retrieveProperties() done");
        }
        return properties.toArray(new ObjectContent[properties.size()]);
    }

    @Override
    public ObjectContent[] getDatastorePropertiesOnHyperHost(String[] propertyPaths) throws Exception {
        if (s_logger.isTraceEnabled()) {
            s_logger.trace(
                "vCenter API trace - retrieveProperties() on Datastore properties. target MOR: " + mor.getValue()
                    + ", properties: " + new Gson().toJson(propertyPaths));
        }

        PropertySpec pSpec = new PropertySpec();
        pSpec.setType("Datastore");
        pSpec.getPathSet().addAll(Arrays.asList(propertyPaths));

        TraversalSpec cluster2DatastoreTraversal = new TraversalSpec();
        cluster2DatastoreTraversal.setType("ClusterComputeResource");
        cluster2DatastoreTraversal.setPath("datastore");
        cluster2DatastoreTraversal.setName("cluster2DatastoreTraversal");

        ObjectSpec oSpec = new ObjectSpec();
        oSpec.setObj(mor);
        oSpec.setSkip(Boolean.TRUE);
        oSpec.getSelectSet().add(cluster2DatastoreTraversal);

        PropertyFilterSpec pfSpec = new PropertyFilterSpec();
        pfSpec.getPropSet().add(pSpec);
        pfSpec.getObjectSet().add(oSpec);
        List<PropertyFilterSpec> pfSpecArr = new ArrayList<PropertyFilterSpec>();
        pfSpecArr.add(pfSpec);

        List<ObjectContent> properties = context.getService()
            .retrieveProperties(context.getPropertyCollector(), pfSpecArr);

        if (s_logger.isTraceEnabled()) {
            s_logger.trace("vCenter API trace - retrieveProperties() done");
        }
        return properties.toArray(new ObjectContent[properties.size()]);
    }

    public ObjectContent[] getHostPropertiesOnCluster(String[] propertyPaths) throws Exception {
        if (s_logger.isTraceEnabled()) {
            s_logger.trace("vCenter API trace - retrieveProperties() on Host properties. target MOR: " + mor.getValue()
                + ", properties: " + new Gson().toJson(propertyPaths));
        }

        PropertySpec pSpec = new PropertySpec();
        pSpec.setType("HostSystem");
        pSpec.getPathSet().addAll(Arrays.asList(propertyPaths));

        TraversalSpec cluster2HostTraversal = new TraversalSpec();
        cluster2HostTraversal.setType("ClusterComputeResource");
        cluster2HostTraversal.setPath("host");
        cluster2HostTraversal.setName("cluster2HostTraversal");

        ObjectSpec oSpec = new ObjectSpec();
        oSpec.setObj(mor);
        oSpec.setSkip(Boolean.TRUE);
        oSpec.getSelectSet().add(cluster2HostTraversal);

        PropertyFilterSpec pfSpec = new PropertyFilterSpec();
        pfSpec.getPropSet().add(pSpec);
        pfSpec.getObjectSet().add(oSpec);

        List<PropertyFilterSpec> pfSpecArr = new ArrayList<PropertyFilterSpec>();
        pfSpecArr.add(pfSpec);

        List<ObjectContent> properties = context.getService()
            .retrieveProperties(context.getPropertyCollector(), pfSpecArr);

        if (s_logger.isTraceEnabled()) {
            s_logger.trace("vCenter API trace - retrieveProperties() done");
        }
        return properties.toArray(new ObjectContent[properties.size()]);
    }

    @Override
    public ManagedObjectReference mountDatastore(boolean vmfsDatastore, String poolHostAddress, int poolHostPort,
        String poolPath, String poolUuid) throws Exception {

        if (s_logger.isTraceEnabled()) {
            s_logger.trace(
                "vCenter API trace - mountDatastore(). target MOR: " + mor.getValue() + ", vmfs: " + vmfsDatastore
                    + ", poolHost: " + poolHostAddress + ", poolHostPort: " + poolHostPort + ", poolPath: " + poolPath
                    + ", poolUuid: " + poolUuid);
        }

        ManagedObjectReference morDs = null;
        ManagedObjectReference morDsFirst = null;
        List<ManagedObjectReference> hosts = context.getVimClient().getDynamicProperty(mor, "host");
        if (hosts != null && hosts.size() > 0) {
            for (ManagedObjectReference morHost : hosts) {
                HostMO hostMo = new HostMO(context, morHost);
                morDs = hostMo.mountDatastore(vmfsDatastore, poolHostAddress, poolHostPort, poolPath, poolUuid);
                if (morDsFirst == null) {
                    morDsFirst = morDs;
                }

                assert (morDsFirst.getValue().equals(morDs.getValue()));
            }
        }

        if (morDs == null) {
            String msg = "Failed to mount datastore in all hosts within the cluster";
            s_logger.error(msg);

            if (s_logger.isTraceEnabled()) {
                s_logger.trace("vCenter API trace - mountDatastore() done(failed)");
            }
            throw new Exception(msg);
        }

        if (s_logger.isTraceEnabled()) {
            s_logger.trace("vCenter API trace - mountDatastore() done(successfully)");
        }

        return morDs;
    }

    @Override
    public void unmountDatastore(String poolUuid) throws Exception {
        List<ManagedObjectReference> hosts = context.getVimClient().getDynamicProperty(mor, "host");
        if (hosts != null && hosts.size() > 0) {
            for (ManagedObjectReference morHost : hosts) {
                HostMO hostMo = new HostMO(context, morHost);
                hostMo.unmountDatastore(poolUuid);
            }
        }
    }

    @Override
    public VmwareHypervisorHostNetworkSummary getHyperHostNetworkSummary(String esxServiceConsolePort)
        throws Exception {
        List<ManagedObjectReference> hosts = context.getVimClient().getDynamicProperty(mor, "host");
        if (hosts != null && hosts.size() > 0) {
            VmwareHypervisorHostNetworkSummary summary = new HostMO(context, hosts.get(0)).getHyperHostNetworkSummary(
                esxServiceConsolePort);
            return summary;
        }
        return null;
    }

    public List<Pair<ManagedObjectReference, String>> getClusterHosts() throws Exception {
        List<Pair<ManagedObjectReference, String>> hosts = new ArrayList<Pair<ManagedObjectReference, String>>();

        ObjectContent[] ocs = getHostPropertiesOnCluster(new String[] {"name"});
        if (ocs != null) {
            for (ObjectContent oc : ocs) {
                ManagedObjectReference morHost = oc.getObj();
                String name = (String) oc.getPropSet().get(0).getVal();

                hosts.add(new Pair<>(morHost, name));
            }
        }
        return hosts;
    }

    private ManagedObjectReference getEnvironmentBrowser() throws Exception {
        if (environmentBrowser == null) {
            environmentBrowser = context.getVimClient().getMoRefProp(mor, "environmentBrowser");
        }
        return environmentBrowser;
    }

    @Override
    public String getRecommendedDiskController(String guestOsId) throws Exception {
        VirtualMachineConfigOption vmConfigOption = context.getService()
            .queryConfigOption(getEnvironmentBrowser(), null, null);
        GuestOsDescriptor guestOsDescriptor = null;
        String diskController;
        List<GuestOsDescriptor> guestDescriptors = vmConfigOption.getGuestOSDescriptor();
        for (GuestOsDescriptor descriptor : guestDescriptors) {
            if (guestOsId != null && guestOsId.equalsIgnoreCase(descriptor.getId())) {
                guestOsDescriptor = descriptor;
                break;
            }
        }
        if (guestOsDescriptor != null) {
            diskController = VmwareHelper.getRecommendedDiskControllerFromDescriptor(guestOsDescriptor);
            s_logger.debug("Retrieved recommended disk controller for guest OS : " + guestOsId + " in cluster "
                + getHyperHostName() + " : " + diskController);
            return diskController;
        } else {
            String msg = "Unable to retrieve recommended disk controller for guest OS : " + guestOsId + " in cluster "
                + getHyperHostName();
            s_logger.error(msg);
            throw new VcenterException(msg);
        }
    }
}
