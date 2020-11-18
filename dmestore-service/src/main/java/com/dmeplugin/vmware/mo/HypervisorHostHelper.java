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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import com.dmeplugin.dmestore.exception.VcenterException;
import com.dmeplugin.dmestore.utils.StringUtil;
import com.dmeplugin.vmware.util.Pair;
import com.dmeplugin.vmware.util.VmwareContext;
import com.dmeplugin.vmware.util.VmwareHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;


import com.vmware.vim25.OvfCreateDescriptorParams;
import com.vmware.vim25.OvfCreateDescriptorResult;
import com.vmware.vim25.AlreadyExistsFaultMsg;
import com.vmware.vim25.BoolPolicy;
import com.vmware.vim25.CustomFieldStringValue;
import com.vmware.vim25.ClusterConfigInfoEx;
import com.vmware.vim25.DatacenterConfigInfo;
import com.vmware.vim25.DVPortSetting;
import com.vmware.vim25.DVPortgroupConfigInfo;
import com.vmware.vim25.DVPortgroupConfigSpec;
import com.vmware.vim25.DVSSecurityPolicy;
import com.vmware.vim25.DVSTrafficShapingPolicy;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.HostNetworkSecurityPolicy;
import com.vmware.vim25.HostNetworkTrafficShapingPolicy;
import com.vmware.vim25.HostPortGroup;
import com.vmware.vim25.HostPortGroupSpec;
import com.vmware.vim25.HostVirtualSwitch;
import com.vmware.vim25.LocalizedMethodFault;
import com.vmware.vim25.LongPolicy;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.OptionValue;
import com.vmware.vim25.OvfCreateImportSpecParams;
import com.vmware.vim25.OvfCreateImportSpecResult;
import com.vmware.vim25.OvfFileItem;
import com.vmware.vim25.OvfFile;
import com.vmware.vim25.ParaVirtualSCSIController;
import com.vmware.vim25.VMwareDVSConfigSpec;
import com.vmware.vim25.VMwareDVSPortSetting;
import com.vmware.vim25.VMwareDVSPvlanConfigSpec;
import com.vmware.vim25.VMwareDVSPvlanMapEntry;
import com.vmware.vim25.VirtualBusLogicController;
import com.vmware.vim25.VirtualController;
import com.vmware.vim25.VirtualDevice;
import com.vmware.vim25.VirtualDisk;
import com.vmware.vim25.VirtualDeviceConfigSpec;
import com.vmware.vim25.VirtualDeviceConfigSpecOperation;
import com.vmware.vim25.VirtualIDEController;
import com.vmware.vim25.VirtualLsiLogicController;
import com.vmware.vim25.VirtualLsiLogicSASController;
import com.vmware.vim25.VirtualMachineConfigSpec;
import com.vmware.vim25.VirtualMachineFileInfo;
import com.vmware.vim25.VirtualMachineGuestOsIdentifier;
import com.vmware.vim25.VirtualMachineVideoCard;
import com.vmware.vim25.VirtualSCSIController;
import com.vmware.vim25.VirtualSCSISharing;
import com.vmware.vim25.VirtualMachineImportSpec;
import com.vmware.vim25.VmwareDistributedVirtualSwitchPvlanSpec;
import com.vmware.vim25.VmwareDistributedVirtualSwitchTrunkVlanSpec;
import com.vmware.vim25.VmwareDistributedVirtualSwitchVlanIdSpec;
import com.vmware.vim25.VmwareDistributedVirtualSwitchVlanSpec;
import java.io.FileWriter;
import java.util.UUID;

public class HypervisorHostHelper {
    private static final Logger s_logger = LoggerFactory.getLogger(HypervisorHostHelper.class);
    private static final int DEFAULT_LOCK_TIMEOUT_SECONDS = 600;
    // make vmware-base loosely coupled with cloud-specific stuff, duplicate VLAN.UNTAGGED constant here
    private static final String UNTAGGED_VLAN_NAME = "untagged";
    private static final String VMDK_PACK_DIR = "ova";
    private static final String OVA_OPTION_KEY_BOOTDISK = "cloud.ova.bootdisk";

    public static VirtualMachineMO findVmFromObjectContent(VmwareContext context, ObjectContent[] ocs, String name, String instanceNameCustomField) {

        if (ocs != null && ocs.length > 0) {
            for (ObjectContent oc : ocs) {
                String vmNameInvCenter = null;
                String vmInternalCsName = null;
                List<DynamicProperty> objProps = oc.getPropSet();
                if (objProps != null) {
                    for (DynamicProperty objProp : objProps) {
                        if ("name".equals(objProp.getName())) {
                            vmNameInvCenter = (String)objProp.getVal();
                        } else if (objProp.getName().contains(instanceNameCustomField)) {
                            if (objProp.getVal() != null) {
                                vmInternalCsName = ((CustomFieldStringValue)objProp.getVal()).getValue();
                            }
                        }

                        if ((vmNameInvCenter != null && name.equalsIgnoreCase(vmNameInvCenter)) || (vmInternalCsName != null && name.equalsIgnoreCase(vmInternalCsName))) {
                            VirtualMachineMO vmMo = new VirtualMachineMO(context, oc.getObj());
                            return vmMo;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static ManagedObjectReference findDatastoreWithBackwardsCompatibility(VmwareHypervisorHost hyperHost, String uuidName) throws Exception {
        ManagedObjectReference morDs = hyperHost.findDatastore(uuidName.replace("-", ""));
        if (morDs == null) {
            morDs = hyperHost.findDatastore(uuidName);
        }

        return morDs;
    }

    public static String getSecondaryDatastoreUuid(String storeUrl) {
        return UUID.nameUUIDFromBytes(storeUrl.getBytes()).toString();
    }

    public static DatastoreMO getHyperHostDatastoreMo(VmwareHypervisorHost hyperHost, String datastoreName) throws Exception {
        ObjectContent[] ocs = hyperHost.getDatastorePropertiesOnHyperHost(new String[] {"name"});
        if (ocs != null && ocs.length > 0) {
            for (ObjectContent oc : ocs) {
                List<DynamicProperty> objProps = oc.getPropSet();
                if (objProps != null) {
                    for (DynamicProperty objProp : objProps) {
                        if (objProp.getVal().toString().equals(datastoreName)) {
                            return new DatastoreMO(hyperHost.getContext(), oc.getObj());
                        }
                    }
                }
            }
        }
        return null;
    }

    public static String getPublicNetworkNamePrefix(String vlanId) {
        if (UNTAGGED_VLAN_NAME.equalsIgnoreCase(vlanId)) {
            return "cloud.public.untagged";
        } else {
            return "cloud.public." + vlanId + ".";
        }
    }

    public static String composeCloudNetworkName(String prefix, String vlanId, String svlanId, Integer networkRateMbps, String vSwitchName) {
        StringBuffer sb = new StringBuffer(prefix);
        if (vlanId == null || UNTAGGED_VLAN_NAME.equalsIgnoreCase(vlanId)) {
            sb.append(".untagged");
        } else {
            sb.append(".").append(vlanId);
            if (svlanId != null) {
                sb.append(".").append("s" + svlanId);
            }

        }

        if (networkRateMbps != null && networkRateMbps.intValue() > 0) {
            sb.append(".").append(String.valueOf(networkRateMbps));
        } else {
            sb.append(".0");
        }
        sb.append(".").append(VersioningContants.PORTGROUP_NAMING_VERSION);
        sb.append("-").append(vSwitchName);

        return sb.toString();
    }

    public static Map<String, String> getValidatedVsmCredentials(VmwareContext context) throws Exception {
        Map<String, String> vsmCredentials = context.getStockObject("vsmcredentials");
        String msg;
        if (vsmCredentials == null || vsmCredentials.size() != 3) {
            msg = "Failed to retrieve required credentials of Nexus VSM from database.";
            s_logger.error(msg);
            throw new Exception(msg);
        }

        String vsmIp = vsmCredentials.containsKey("vsmip") ? vsmCredentials.get("vsmip") : null;
        String vsmUserName = vsmCredentials.containsKey("vsmusername") ? vsmCredentials.get("vsmusername") : null;
        String vsmPassword = vsmCredentials.containsKey("vsmpassword") ? vsmCredentials.get("vsmpassword") : null;
        if (vsmIp == null || vsmIp.isEmpty() || vsmUserName == null || vsmUserName.isEmpty() || vsmPassword == null || vsmPassword.isEmpty()) {
            msg = "Detected invalid credentials for Nexus 1000v.";
            s_logger.error(msg);
            throw new Exception(msg);
        }
        return vsmCredentials;
    }




    public static String getVcenterApiVersion(VmwareContext serviceContext) throws Exception {
        String vcApiVersion = null;
        if (serviceContext != null) {
            vcApiVersion = serviceContext.getServiceContent().getAbout().getApiVersion();
        }
        return vcApiVersion;
    }

    public static boolean isFeatureSupportedInVcenterApiVersion(String vCenterApiVersion, String minVcenterApiVersionForFeature) {
        return vCenterApiVersion.compareTo(minVcenterApiVersionForFeature) >= 0 ? true : false;
    }

    private static void setupPvLanPair(DistributedVirtualSwitchMO dvSwitchMo, ManagedObjectReference morDvSwitch, Integer vid, Integer spvlanid, String pvlanType) throws Exception {
        s_logger.debug(String.format("Setting up PVLAN on dvSwitch %s with the following information: %s %s %s", dvSwitchMo.getName(), vid, spvlanid, pvlanType));
        Map<Integer, HypervisorHostHelper.PvlanType> vlanmap = dvSwitchMo.retrieveVlanPvlan(vid, spvlanid, morDvSwitch);
        if (!vlanmap.isEmpty()) {
            // Then either vid or pvlanid or both are already being used. Check how.
            // First the primary pvlan id.
            if (vlanmap.containsKey(vid) && !vlanmap.get(vid).equals(HypervisorHostHelper.PvlanType.promiscuous)) {
                // This VLAN ID is already setup as a non-promiscuous vlan id on the DVS. Throw an exception.
                String msg = "Specified primary PVLAN ID " + vid + " is already in use as a " + vlanmap.get(vid).toString() + " VLAN on the DVSwitch";
                s_logger.error(msg);
                throw new Exception(msg);
            }
            // Next the secondary pvlan id.
            if (spvlanid.equals(vid)) {
                if (vlanmap.containsKey(spvlanid) && !vlanmap.get(spvlanid).equals(HypervisorHostHelper.PvlanType.promiscuous)) {
                    String msg = "Specified secondary PVLAN ID " + spvlanid + " is already in use as a " + vlanmap.get(spvlanid).toString() + " VLAN in the DVSwitch";
                    s_logger.error(msg);
                    throw new Exception(msg);
                }
            }
        }

        // First create a DVSconfig spec.
        VMwareDVSConfigSpec dvsSpec = new VMwareDVSConfigSpec();
        // Next, add the required primary and secondary vlan config specs to the dvs config spec.

        if (!vlanmap.containsKey(vid)) {
            VMwareDVSPvlanConfigSpec ppvlanConfigSpec = createDvPortPvlanConfigSpec(vid, vid, PvlanType.promiscuous, PvlanOperation.add);
            dvsSpec.getPvlanConfigSpec().add(ppvlanConfigSpec);
        }
        if (!vid.equals(spvlanid) && !vlanmap.containsKey(spvlanid)) {
            PvlanType selectedType = StringUtil.isNotBlank(pvlanType) ? PvlanType.fromStr(pvlanType) : PvlanType.isolated;
            VMwareDVSPvlanConfigSpec spvlanConfigSpec = createDvPortPvlanConfigSpec(vid, spvlanid, selectedType, PvlanOperation.add);
            dvsSpec.getPvlanConfigSpec().add(spvlanConfigSpec);
        }

        if (dvsSpec.getPvlanConfigSpec().size() > 0) {
            // We have something to configure on the DVS... so send it the command.
            // When reconfiguring a vmware DVSwitch, we need to send in the configVersion in the spec.
            // Let's retrieve this switch's configVersion first.
            String dvsConfigVersion = dvSwitchMo.getDvsConfigVersion(morDvSwitch);
            dvsSpec.setConfigVersion(dvsConfigVersion);

            // Reconfigure the dvs using this spec.
            try {
                dvSwitchMo.updateVmWareDvSwitchGetTask(morDvSwitch, dvsSpec);
            } catch (AlreadyExistsFaultMsg e) {
                s_logger.info("Specified vlan id (" + vid + ") private vlan id (" + spvlanid + ") tuple already configured on VMWare DVSwitch");
                // Do nothing, good if the tuple's already configured on the dvswitch.
            } catch (Exception e) {
                // Rethrow the exception
                s_logger.error("Failed to configure vlan/pvlan tuple on VMware DVSwitch: " + vid + "/" + spvlanid + ", failure message: ", e);
                throw e;
            }
        }

    }


    public static boolean isSpecMatch(DVPortgroupConfigInfo currentDvPortgroupInfo, DVPortgroupConfigSpec newDvPortGroupSpec) {
        String dvPortGroupName = newDvPortGroupSpec.getName();
        s_logger.debug("Checking if configuration of dvPortGroup [" + dvPortGroupName + "] has changed.");
        boolean specMatches = true;
        DVSTrafficShapingPolicy currentTrafficShapingPolicy;
        currentTrafficShapingPolicy = currentDvPortgroupInfo.getDefaultPortConfig().getInShapingPolicy();

        assert (currentTrafficShapingPolicy != null);

        LongPolicy oldAverageBandwidthPolicy = currentTrafficShapingPolicy.getAverageBandwidth();
        LongPolicy oldBurstSizePolicy = currentTrafficShapingPolicy.getBurstSize();
        LongPolicy oldPeakBandwidthPolicy = currentTrafficShapingPolicy.getPeakBandwidth();
        BoolPolicy oldIsEnabledPolicy = currentTrafficShapingPolicy.getEnabled();
        Long oldAverageBandwidth = null;
        Long oldBurstSize = null;
        Long oldPeakBandwidth = null;
        Boolean oldIsEnabled = null;

        if (oldAverageBandwidthPolicy != null) {
            oldAverageBandwidth = oldAverageBandwidthPolicy.getValue();
        }
        if (oldBurstSizePolicy != null) {
            oldBurstSize = oldBurstSizePolicy.getValue();
        }
        if (oldPeakBandwidthPolicy != null) {
            oldPeakBandwidth = oldPeakBandwidthPolicy.getValue();
        }
        if (oldIsEnabledPolicy != null) {
            oldIsEnabled = oldIsEnabledPolicy.isValue();
        }

        DVSTrafficShapingPolicy newTrafficShapingPolicyInbound = newDvPortGroupSpec.getDefaultPortConfig().getInShapingPolicy();
        LongPolicy newAverageBandwidthPolicy = newTrafficShapingPolicyInbound.getAverageBandwidth();
        LongPolicy newBurstSizePolicy = newTrafficShapingPolicyInbound.getBurstSize();
        LongPolicy newPeakBandwidthPolicy = newTrafficShapingPolicyInbound.getPeakBandwidth();
        BoolPolicy newIsEnabledPolicy = newTrafficShapingPolicyInbound.getEnabled();
        Long newAverageBandwidth = null;
        Long newBurstSize = null;
        Long newPeakBandwidth = null;
        Boolean newIsEnabled = null;
        if (newAverageBandwidthPolicy != null) {
            newAverageBandwidth = newAverageBandwidthPolicy.getValue();
        }
        if (newBurstSizePolicy != null) {
            newBurstSize = newBurstSizePolicy.getValue();
        }
        if (newPeakBandwidthPolicy != null) {
            newPeakBandwidth = newPeakBandwidthPolicy.getValue();
        }
        if (newIsEnabledPolicy != null) {
            newIsEnabled = newIsEnabledPolicy.isValue();
        }

        if (!oldIsEnabled.equals(newIsEnabled)) {
            s_logger.info("Detected change in state of shaping policy (enabled/disabled) [" + newIsEnabled + "]");
            specMatches = false;
        }

        if (oldIsEnabled || newIsEnabled) {
            if (oldAverageBandwidth != null && !oldAverageBandwidth.equals(newAverageBandwidth)) {
                s_logger.info("Average bandwidth setting in new shaping policy doesn't match the existing setting.");
                specMatches = false;
            } else if (oldBurstSize != null && !oldBurstSize.equals(newBurstSize)) {
                s_logger.info("Burst size setting in new shaping policy doesn't match the existing setting.");
                specMatches = false;
            } else if (oldPeakBandwidth != null && !oldPeakBandwidth.equals(newPeakBandwidth)) {
                s_logger.info("Peak bandwidth setting in new shaping policy doesn't match the existing setting.");
                specMatches = false;
            }
        }

        boolean oldAutoExpandSetting = currentDvPortgroupInfo.isAutoExpand();
        boolean autoExpandEnabled = newDvPortGroupSpec.isAutoExpand();
        if (oldAutoExpandSetting != autoExpandEnabled) {
            specMatches = false;
        }
        if (!autoExpandEnabled) {
            // Allow update of number of dvports per dvPortGroup is auto expand is not enabled.
            int oldNumPorts = currentDvPortgroupInfo.getNumPorts();
            int newNumPorts = newDvPortGroupSpec.getNumPorts();
            if (oldNumPorts < newNumPorts) {
                s_logger.info("Need to update the number of dvports for dvPortGroup :[" + dvPortGroupName +
                            "] from existing number of dvports " + oldNumPorts + " to " + newNumPorts);
                specMatches = false;
            } else if (oldNumPorts > newNumPorts) {
                s_logger.warn("Detected that new number of dvports [" + newNumPorts + "] in dvPortGroup [" + dvPortGroupName +
                        "] is less than existing number of dvports [" + oldNumPorts + "]. Attempt to update this dvPortGroup may fail!");
                specMatches = false;
            }
        }

        VMwareDVSPortSetting currentPortSetting = ((VMwareDVSPortSetting)currentDvPortgroupInfo.getDefaultPortConfig());
        VMwareDVSPortSetting newPortSetting = ((VMwareDVSPortSetting)newDvPortGroupSpec.getDefaultPortConfig());
        if ((currentPortSetting.getSecurityPolicy() == null && newPortSetting.getSecurityPolicy() != null) ||
                (currentPortSetting.getSecurityPolicy() != null && newPortSetting.getSecurityPolicy() == null)) {
            specMatches = false;
        }
        if (currentPortSetting.getSecurityPolicy() != null && newPortSetting.getSecurityPolicy() != null) {
            if (currentPortSetting.getSecurityPolicy().getAllowPromiscuous() != null &&
                    newPortSetting.getSecurityPolicy().getAllowPromiscuous() != null &&
                    newPortSetting.getSecurityPolicy().getAllowPromiscuous().isValue() != null &&
                    !newPortSetting.getSecurityPolicy().getAllowPromiscuous().isValue().equals(currentPortSetting.getSecurityPolicy().getAllowPromiscuous().isValue())) {
                specMatches = false;
            }
            if (currentPortSetting.getSecurityPolicy().getForgedTransmits() != null &&
                    newPortSetting.getSecurityPolicy().getForgedTransmits() != null &&
                    newPortSetting.getSecurityPolicy().getForgedTransmits().isValue() != null &&
                    !newPortSetting.getSecurityPolicy().getForgedTransmits().isValue().equals(currentPortSetting.getSecurityPolicy().getForgedTransmits().isValue())) {
                specMatches = false;
            }
            if (currentPortSetting.getSecurityPolicy().getMacChanges() != null &&
                    newPortSetting.getSecurityPolicy().getMacChanges() != null &&
                    newPortSetting.getSecurityPolicy().getMacChanges().isValue() != null &&
                    !newPortSetting.getSecurityPolicy().getMacChanges().isValue().equals(currentPortSetting.getSecurityPolicy().getMacChanges().isValue())) {
                specMatches = false;
            }
        }

        VmwareDistributedVirtualSwitchVlanSpec oldVlanSpec = currentPortSetting.getVlan();
        VmwareDistributedVirtualSwitchVlanSpec newVlanSpec = newPortSetting.getVlan();

        int oldVlanId, newVlanId;
        if (oldVlanSpec instanceof VmwareDistributedVirtualSwitchPvlanSpec && newVlanSpec instanceof VmwareDistributedVirtualSwitchPvlanSpec) {
            VmwareDistributedVirtualSwitchPvlanSpec oldpVlanSpec = (VmwareDistributedVirtualSwitchPvlanSpec) oldVlanSpec;
            VmwareDistributedVirtualSwitchPvlanSpec newpVlanSpec = (VmwareDistributedVirtualSwitchPvlanSpec) newVlanSpec;
            oldVlanId = oldpVlanSpec.getPvlanId();
            newVlanId = newpVlanSpec.getPvlanId();
        } else if (oldVlanSpec instanceof VmwareDistributedVirtualSwitchTrunkVlanSpec && newVlanSpec instanceof VmwareDistributedVirtualSwitchTrunkVlanSpec) {
            VmwareDistributedVirtualSwitchTrunkVlanSpec oldpVlanSpec = (VmwareDistributedVirtualSwitchTrunkVlanSpec) oldVlanSpec;
            VmwareDistributedVirtualSwitchTrunkVlanSpec newpVlanSpec = (VmwareDistributedVirtualSwitchTrunkVlanSpec) newVlanSpec;
            oldVlanId = oldpVlanSpec.getVlanId().get(0).getStart();
            newVlanId = newpVlanSpec.getVlanId().get(0).getStart();
        } else if (oldVlanSpec instanceof VmwareDistributedVirtualSwitchVlanIdSpec && newVlanSpec instanceof VmwareDistributedVirtualSwitchVlanIdSpec) {
            VmwareDistributedVirtualSwitchVlanIdSpec oldVlanIdSpec = (VmwareDistributedVirtualSwitchVlanIdSpec) oldVlanSpec;
            VmwareDistributedVirtualSwitchVlanIdSpec newVlanIdSpec = (VmwareDistributedVirtualSwitchVlanIdSpec) newVlanSpec;
            oldVlanId = oldVlanIdSpec.getVlanId();
            newVlanId = newVlanIdSpec.getVlanId();
        } else {
            s_logger.debug("Old and new vlan spec type mismatch found for [" + dvPortGroupName + "] has changed. Old spec type is: " + oldVlanSpec.getClass() + ", and new spec type is:" + newVlanSpec.getClass());
            return false;
        }

        if (oldVlanId != newVlanId) {
            s_logger.info("Detected that new VLAN [" + newVlanId + "] of dvPortGroup [" + dvPortGroupName +
                        "] is different from current VLAN [" + oldVlanId + "]");
            specMatches = false;
        }

        return specMatches;
    }

    public static ManagedObjectReference waitForDvPortGroupReady(DatacenterMO dataCenterMo, String dvPortGroupName, long timeOutMs) throws Exception {
        ManagedObjectReference morDvPortGroup = null;

        // if DvPortGroup is just created, we may fail to retrieve it, we
        // need to retry
        long startTick = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTick <= timeOutMs) {
            morDvPortGroup = dataCenterMo.getDvPortGroupMor(dvPortGroupName);
            if (morDvPortGroup != null) {
                break;
            }

            s_logger.info("Waiting for dvPortGroup " + dvPortGroupName + " to be ready");
            Thread.sleep(1000);
        }
        return morDvPortGroup;
    }

    public static boolean isSpecMatch(DVPortgroupConfigInfo configInfo, Integer vid, DVSTrafficShapingPolicy shapingPolicy) {
        DVSTrafficShapingPolicy currentTrafficShapingPolicy;
        currentTrafficShapingPolicy = configInfo.getDefaultPortConfig().getInShapingPolicy();

        assert (currentTrafficShapingPolicy != null);

        LongPolicy averageBandwidth = currentTrafficShapingPolicy.getAverageBandwidth();
        LongPolicy burstSize = currentTrafficShapingPolicy.getBurstSize();
        LongPolicy peakBandwidth = currentTrafficShapingPolicy.getPeakBandwidth();
        BoolPolicy isEnabled = currentTrafficShapingPolicy.getEnabled();

        if (!isEnabled.equals(shapingPolicy.getEnabled())) {
            return false;
        }

        if (averageBandwidth != null && !averageBandwidth.equals(shapingPolicy.getAverageBandwidth())) {
            if (s_logger.isInfoEnabled()) {
                s_logger.info("Average bandwidth setting in shaping policy doesn't match with existing setting.");
            }
            return false;
        } else if (burstSize != null && !burstSize.equals(shapingPolicy.getBurstSize())) {
            if (s_logger.isInfoEnabled()) {
                s_logger.info("Burst size setting in shaping policy doesn't match with existing setting.");
            }
            return false;
        } else if (peakBandwidth != null && !peakBandwidth.equals(shapingPolicy.getPeakBandwidth())) {
            if (s_logger.isInfoEnabled()) {
                s_logger.info("Peak bandwidth setting in shaping policy doesn't match with existing setting.");
            }
            return false;
        }

        return true;
    }

    public static DVPortgroupConfigSpec createDvPortGroupSpec(String dvPortGroupName, DVPortSetting portSetting, boolean autoExpandSupported) {
        DVPortgroupConfigSpec spec = new DVPortgroupConfigSpec();
        spec.setName(dvPortGroupName);
        spec.setDefaultPortConfig(portSetting);
        spec.setPortNameFormat("vnic<portIndex>");
        spec.setType("earlyBinding");
        spec.setAutoExpand(autoExpandSupported);
        return spec;
    }

    public static VMwareDVSPortSetting createVmwareDvPortSettingSpec(DVSTrafficShapingPolicy shapingPolicy, DVSSecurityPolicy secPolicy,
                                                                     VmwareDistributedVirtualSwitchVlanSpec vlanSpec) {
        VMwareDVSPortSetting dvsPortSetting = new VMwareDVSPortSetting();
        dvsPortSetting.setVlan(vlanSpec);
        dvsPortSetting.setSecurityPolicy(secPolicy);
        dvsPortSetting.setInShapingPolicy(shapingPolicy);
        dvsPortSetting.setOutShapingPolicy(shapingPolicy);
        return dvsPortSetting;
    }

    public static DVSTrafficShapingPolicy getDvsShapingPolicy(Integer networkRateMbps) {
        DVSTrafficShapingPolicy shapingPolicy = new DVSTrafficShapingPolicy();
        BoolPolicy isEnabled = new BoolPolicy();
        if (networkRateMbps == null || networkRateMbps.intValue() <= 0) {
            isEnabled.setValue(false);
            shapingPolicy.setEnabled(isEnabled);
            return shapingPolicy;
        }
        LongPolicy averageBandwidth = new LongPolicy();
        LongPolicy peakBandwidth = new LongPolicy();
        LongPolicy burstSize = new LongPolicy();

        isEnabled.setValue(true);
        averageBandwidth.setValue(networkRateMbps.intValue() * 1024L * 1024L);
        // We chose 50% higher allocation than average bandwidth.
        // TODO(sateesh): Also let user specify the peak coefficient
        peakBandwidth.setValue((long)(averageBandwidth.getValue() * 1.5));
        // TODO(sateesh): Also let user specify the burst coefficient
        burstSize.setValue(5 * averageBandwidth.getValue() / 8);

        shapingPolicy.setEnabled(isEnabled);
        shapingPolicy.setAverageBandwidth(averageBandwidth);
        shapingPolicy.setPeakBandwidth(peakBandwidth);
        shapingPolicy.setBurstSize(burstSize);

        return shapingPolicy;
    }

    public static VmwareDistributedVirtualSwitchPvlanSpec createDvPortPvlanIdSpec(int pvlanId) {
        VmwareDistributedVirtualSwitchPvlanSpec pvlanIdSpec = new VmwareDistributedVirtualSwitchPvlanSpec();
        pvlanIdSpec.setPvlanId(pvlanId);
        return pvlanIdSpec;
    }

    public enum PvlanOperation {
        /**
         * add
         **/
        add,
        edit,
        remove
    }

    public enum PvlanType {
        /**
         * promiscuous
         **/
        promiscuous,
        isolated,
        community;

        public static PvlanType fromStr(String val) {
            if (StringUtil.isBlank(val)) {
                return null;
            } else if ("promiscuous".equalsIgnoreCase(val)) {
                return promiscuous;
            } else if ("community".equalsIgnoreCase(val)) {
                return community;
            } else if ("isolated".equalsIgnoreCase(val)) {
                return isolated;
            }
            return null;
        }
    }

    public static VMwareDVSPvlanConfigSpec createDvPortPvlanConfigSpec(int vlanId, int secondaryVlanId, PvlanType pvlantype, PvlanOperation operation) {
        VMwareDVSPvlanConfigSpec pvlanConfigSpec = new VMwareDVSPvlanConfigSpec();
        VMwareDVSPvlanMapEntry map = new VMwareDVSPvlanMapEntry();
        map.setPvlanType(pvlantype.toString());
        map.setPrimaryVlanId(vlanId);
        map.setSecondaryVlanId(secondaryVlanId);
        pvlanConfigSpec.setPvlanEntry(map);

        pvlanConfigSpec.setOperation(operation.toString());
        return pvlanConfigSpec;
    }






    private static boolean isSpecMatch(HostPortGroupSpec spec, Integer vlanId, HostNetworkSecurityPolicy securityPolicy, HostNetworkTrafficShapingPolicy shapingPolicy) {
        // check VLAN configuration
        if (vlanId != null) {
            if (vlanId.intValue() != spec.getVlanId()) {
                return false;
            }
        } else {
            if (spec.getVlanId() != 0) {
                return false;
            }
        }

        // check security policy for the portgroup
        HostNetworkSecurityPolicy secPolicyInSpec = null;
        if (spec.getPolicy() != null) {
            secPolicyInSpec = spec.getPolicy().getSecurity();
        }

        if ((secPolicyInSpec != null && securityPolicy == null) || (secPolicyInSpec == null && securityPolicy != null)) {
            return false;
        }

        if (secPolicyInSpec != null && securityPolicy != null
                && ((securityPolicy.isAllowPromiscuous() != null && !securityPolicy.isAllowPromiscuous().equals(secPolicyInSpec.isAllowPromiscuous()))
                    || (securityPolicy.isForgedTransmits() != null && !securityPolicy.isForgedTransmits().equals(secPolicyInSpec.isForgedTransmits()))
                    || (securityPolicy.isMacChanges() != null && securityPolicy.isMacChanges().equals(secPolicyInSpec.isMacChanges())))) {
            return false;
        }

        // check traffic shaping configuration
        HostNetworkTrafficShapingPolicy policyInSpec = null;
        if (spec.getPolicy() != null) {
            policyInSpec = spec.getPolicy().getShapingPolicy();
        }

        if ((policyInSpec != null && shapingPolicy == null) || (policyInSpec == null && shapingPolicy != null)) {
            return false;
        }

        if (policyInSpec == null && shapingPolicy == null) {
            return true;
        }

        // so far policyInSpec and shapingPolicy should both not be null
        if (policyInSpec.isEnabled() == null || !policyInSpec.isEnabled().booleanValue()) {
            return false;
        }

        if (policyInSpec.getAverageBandwidth() == null || policyInSpec.getAverageBandwidth().longValue() != shapingPolicy.getAverageBandwidth().longValue()) {
            return false;
        }

        if (policyInSpec.getPeakBandwidth() == null || policyInSpec.getPeakBandwidth().longValue() != shapingPolicy.getPeakBandwidth().longValue()) {
            return false;
        }

        return policyInSpec.getBurstSize() != null && policyInSpec.getBurstSize().longValue() == shapingPolicy.getBurstSize().longValue();
    }

    private static void createNvpPortGroup(HostMO hostMo, HostVirtualSwitch vSwitch, String networkName, HostNetworkTrafficShapingPolicy shapingPolicy) throws Exception {
        /**
         * No portgroup created yet for this nic
         * We need to find an unused vlan and create the pg
         * The vlan is limited to this vSwitch and the NVP vAPP,
         * so no relation to the other vlans in use in CloudStack.
         */
        String vSwitchName = vSwitch.getName();

        // Find all vlanids that we have in use
        List<Integer> usedVlans = new ArrayList<Integer>();
        for (HostPortGroup pg : hostMo.getHostNetworkInfo().getPortgroup()) {
            HostPortGroupSpec hpgs = pg.getSpec();
            if (vSwitchName.equals(hpgs.getVswitchName())) {
                usedVlans.add(hpgs.getVlanId());
            }
        }

        // Find the first free vlanid
        int nvpVlanId = 0;
        for (nvpVlanId = 1; nvpVlanId < 4095; nvpVlanId++) {
            if (!usedVlans.contains(nvpVlanId)) {
                break;
            }
        }
        if (nvpVlanId == 4095) {
            throw new InvalidParameterException("No free vlan numbers on " + vSwitchName + " to create a portgroup for nic " + networkName);
        }

        // Strict security policy
        HostNetworkSecurityPolicy secPolicy = new HostNetworkSecurityPolicy();
        secPolicy.setAllowPromiscuous(Boolean.FALSE);
        secPolicy.setForgedTransmits(Boolean.FALSE);
        secPolicy.setMacChanges(Boolean.FALSE);

        // Create a portgroup with the uuid of the nic and the vlanid found above
        hostMo.createPortGroup(vSwitch, networkName, nvpVlanId, secPolicy, shapingPolicy);
    }

    public static ManagedObjectReference waitForNetworkReady(HostMO hostMo, String networkName, long timeOutMs) throws Exception {

        ManagedObjectReference morNetwork = null;

        // if portGroup is just created, getNetwork may fail to retrieve it, we
        // need to retry
        long startTick = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTick <= timeOutMs) {
            morNetwork = hostMo.getNetworkMor(networkName);
            if (morNetwork != null) {
                break;
            }

            s_logger.info("Waiting for network " + networkName + " to be ready");
            Thread.sleep(1000);
        }

        return morNetwork;
    }

    public static boolean createBlankVm(VmwareHypervisorHost host, String vmName, String vmInternalCsName, int cpuCount, int cpuSpeedMhz, int cpuReservedMhz,
                                        boolean limitCpuUse, int memoryMb, int memoryReserveMb, String guestOsIdentifier, ManagedObjectReference morDs, boolean snapshotDirToParent,
                                        Pair<String, String> controllerInfo, Boolean systemVm) throws Exception {

        if (s_logger.isInfoEnabled()) {
            s_logger.info("Create blank VM. cpuCount: " + cpuCount + ", cpuSpeed(MHz): " + cpuSpeedMhz + ", mem(Mb): " + memoryMb);
        }

        VirtualDeviceConfigSpec controllerSpec = null;
        // VM config basics
        VirtualMachineConfigSpec vmConfig = new VirtualMachineConfigSpec();
        vmConfig.setName(vmName);
        if (vmInternalCsName == null) {
            vmInternalCsName = vmName;
        }

        VmwareHelper.setBasicVmConfig(vmConfig, cpuCount, cpuSpeedMhz, cpuReservedMhz, memoryMb, memoryReserveMb, guestOsIdentifier, limitCpuUse);

        String recommendedController = host.getRecommendedDiskController(guestOsIdentifier);
        String newRootDiskController = controllerInfo.first();
        String newDataDiskController = controllerInfo.second();
        if (DiskControllerType.getType(controllerInfo.first()) == DiskControllerType.osdefault) {
            newRootDiskController = recommendedController;
        }
        if (DiskControllerType.getType(controllerInfo.second()) == DiskControllerType.osdefault) {
            newDataDiskController = recommendedController;
        }

        Pair<String, String> updatedControllerInfo = new Pair<String, String>(newRootDiskController, newDataDiskController);
        String scsiDiskController = HypervisorHostHelper.getScsiController(updatedControllerInfo, recommendedController);
        // If there is requirement for a SCSI controller, ensure to create those.
        if (scsiDiskController != null) {
        int busNum = 0;
            int maxControllerCount = VmwareHelper.MAX_SCSI_CONTROLLER_COUNT;
            if (systemVm) {
                maxControllerCount = 1;
            }
            while (busNum < maxControllerCount) {
            VirtualDeviceConfigSpec scsiControllerSpec = new VirtualDeviceConfigSpec();
                scsiControllerSpec = getControllerSpec(DiskControllerType.getType(scsiDiskController).toString(), busNum);

            vmConfig.getDeviceChange().add(scsiControllerSpec);
            busNum++;
            }
        }

        if (guestOsIdentifier.startsWith("darwin")) { //Mac OS
            s_logger.debug("Add USB Controller device for blank Mac OS VM " + vmName);

            //For Mac OS X systems, the EHCI+UHCI controller is enabled by default and is required for USB mouse and keyboard access.
            VirtualDevice usbControllerDevice = VmwareHelper.prepareUsbControllerDevice();
            VirtualDeviceConfigSpec usbControllerSpec = new VirtualDeviceConfigSpec();
            usbControllerSpec.setDevice(usbControllerDevice);
            usbControllerSpec.setOperation(VirtualDeviceConfigSpecOperation.ADD);

            vmConfig.getDeviceChange().add(usbControllerSpec);
        }

        VirtualMachineFileInfo fileInfo = new VirtualMachineFileInfo();
        DatastoreMO dsMo = new DatastoreMO(host.getContext(), morDs);
        fileInfo.setVmPathName(String.format("[%s]", dsMo.getName()));
        vmConfig.setFiles(fileInfo);

        VirtualMachineVideoCard videoCard = new VirtualMachineVideoCard();
        videoCard.setControllerKey(100);
        videoCard.setUseAutoDetect(true);

        VirtualDeviceConfigSpec videoDeviceSpec = new VirtualDeviceConfigSpec();
        videoDeviceSpec.setDevice(videoCard);
        videoDeviceSpec.setOperation(VirtualDeviceConfigSpecOperation.ADD);

        vmConfig.getDeviceChange().add(videoDeviceSpec);

        ClusterMO clusterMo = new ClusterMO(host.getContext(), host.getHyperHostCluster());
        DatacenterMO dataCenterMo = new DatacenterMO(host.getContext(), host.getHyperHostDatacenter());
        setVmHardwareVersion(vmConfig, clusterMo, dataCenterMo);

        if (host.createVm(vmConfig)) {
            // Here, when attempting to find the VM, we need to use the name
            // with which we created it. This is the only such place where
            // we need to do this. At all other places, we always use the
            // VM's internal cloudstack generated name. Here, we cannot use
            // the internal name because we can set the internal name into the
            // VM's custom field CLOUD_VM_INTERNAL_NAME only after we create
            // the VM.
            VirtualMachineMO vmMo = host.findVmOnHyperHost(vmName);
            assert (vmMo != null);

            vmMo.setCustomFieldValue(CustomFieldConstants.CLOUD_VM_INTERNAL_NAME, vmInternalCsName);

            int ideControllerKey = -1;
            while (ideControllerKey < 0) {
                ideControllerKey = vmMo.tryGetIdeDeviceControllerKey();
                if (ideControllerKey >= 0) {
                    break;
                }

                s_logger.info("Waiting for IDE controller be ready in VM: " + vmInternalCsName);
                Thread.sleep(1000);
            }

            return true;
        }
        return false;
    }

    /**
     * Set the VM hardware version based on the information retrieved by the cluster and datacenter:
     * - If the cluster hardware version is set, then it is set to this hardware version on vmConfig
     * - If the cluster hardware version is not set, check datacenter hardware version. If it is set, then it is set to vmConfig
     * - In case both cluster and datacenter hardware version are not set, hardware version is not set to vmConfig
     */
    protected static void setVmHardwareVersion(VirtualMachineConfigSpec vmConfig, ClusterMO clusterMo, DatacenterMO datacenterMo) throws Exception {
        ClusterConfigInfoEx clusterConfigInfo = clusterMo != null ? clusterMo.getClusterConfigInfo() : null;
        String clusterHardwareVersion = clusterConfigInfo != null ? clusterConfigInfo.getDefaultHardwareVersionKey() : null;
        if (StringUtil.isNotBlank(clusterHardwareVersion)) {
            s_logger.debug("Cluster hardware version found: " + clusterHardwareVersion + ". Creating VM with this hardware version");
            vmConfig.setVersion(clusterHardwareVersion);
        } else {
            DatacenterConfigInfo datacenterConfigInfo = datacenterMo != null ? datacenterMo.getDatacenterConfigInfo() : null;
            String datacenterHardwareVersion = datacenterConfigInfo != null ? datacenterConfigInfo.getDefaultHardwareVersionKey() : null;
            if (StringUtil.isNotBlank(datacenterHardwareVersion)) {
                s_logger.debug("Datacenter hardware version found: " + datacenterHardwareVersion + ". Creating VM with this hardware version");
                vmConfig.setVersion(datacenterHardwareVersion);
            }
        }
    }

    private static VirtualDeviceConfigSpec getControllerSpec(String diskController, int busNum) {
        VirtualDeviceConfigSpec controllerSpec = new VirtualDeviceConfigSpec();
        VirtualController controller = null;

        if (diskController.equalsIgnoreCase(DiskControllerType.ide.toString())) {
           controller = new VirtualIDEController();
        } else if (DiskControllerType.pvscsi == DiskControllerType.getType(diskController)) {
            controller = new ParaVirtualSCSIController();
        } else if (DiskControllerType.lsisas1068 == DiskControllerType.getType(diskController)) {
            controller = new VirtualLsiLogicSASController();
        } else if (DiskControllerType.buslogic == DiskControllerType.getType(diskController)) {
            controller = new VirtualBusLogicController();
        } else if (DiskControllerType.lsilogic == DiskControllerType.getType(diskController)) {
            controller = new VirtualLsiLogicController();
        }

        if (!diskController.equalsIgnoreCase(DiskControllerType.ide.toString())) {
            ((VirtualSCSIController)controller).setSharedBus(VirtualSCSISharing.NO_SHARING);
        }

        controller.setBusNumber(busNum);
        controller.setKey(busNum - VmwareHelper.MAX_SCSI_CONTROLLER_COUNT);

        controllerSpec.setDevice(controller);
        controllerSpec.setOperation(VirtualDeviceConfigSpecOperation.ADD);

        return controllerSpec;
    }
    public static VirtualMachineMO createWorkerVm(VmwareHypervisorHost hyperHost, DatastoreMO dsMo, String vmName) throws Exception {

        // Allow worker VM to float within cluster so that we will have better chance to
        // create it successfully
        ManagedObjectReference morCluster = hyperHost.getHyperHostCluster();
        if (morCluster != null) {
            hyperHost = new ClusterMO(hyperHost.getContext(), morCluster);
        }

        VirtualMachineMO workingVm = null;
        VirtualMachineConfigSpec vmConfig = new VirtualMachineConfigSpec();
        vmConfig.setName(vmName);
        vmConfig.setMemoryMB((long)4);
        vmConfig.setNumCPUs(1);
        vmConfig.setGuestId(VirtualMachineGuestOsIdentifier.OTHER_GUEST.value());
        VirtualMachineFileInfo fileInfo = new VirtualMachineFileInfo();
        fileInfo.setVmPathName(dsMo.getDatastoreRootPath());
        vmConfig.setFiles(fileInfo);

        VirtualLsiLogicController scsiController = new VirtualLsiLogicController();
        scsiController.setSharedBus(VirtualSCSISharing.NO_SHARING);
        scsiController.setBusNumber(0);
        scsiController.setKey(1);
        VirtualDeviceConfigSpec scsiControllerSpec = new VirtualDeviceConfigSpec();
        scsiControllerSpec.setDevice(scsiController);
        scsiControllerSpec.setOperation(VirtualDeviceConfigSpecOperation.ADD);

        vmConfig.getDeviceChange().add(scsiControllerSpec);
        if (hyperHost.createVm(vmConfig)) {
            // Ugly work-around, it takes time for newly created VM to appear
            for (int i = 0; i < 10 && workingVm == null; i++) {
                workingVm = hyperHost.findVmOnHyperHost(vmName);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    s_logger.debug("[ignored] interupted while waiting to config vm.");
                }
            }
        }

        if (workingVm != null) {
            workingVm.setCustomFieldValue(CustomFieldConstants.CLOUD_WORKER, "true");
            String workerTag = String.format("%d-%s", System.currentTimeMillis(), hyperHost.getContext().getStockObject("noderuninfo"));
            workingVm.setCustomFieldValue(CustomFieldConstants.CLOUD_WORKER_TAG, workerTag);
        }
        return workingVm;
    }


    public static String removeOvfNetwork(final String ovfString)  {
        if (ovfString == null || ovfString.isEmpty()) {
            return ovfString;
        }
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final Document doc = factory.newDocumentBuilder().parse(new ByteArrayInputStream(ovfString.getBytes()));
            final DocumentTraversal traversal = (DocumentTraversal) doc;
            final NodeIterator iterator = traversal.createNodeIterator(doc.getDocumentElement(), NodeFilter.SHOW_ELEMENT, null, true);
            for (Node n = iterator.nextNode(); n != null; n = iterator.nextNode()) {
                final Element e = (Element) n;
                if ("NetworkSection".equals(e.getTagName())) {
                    if (e.getParentNode() != null) {
                        e.getParentNode().removeChild(e);
                    }
                } else if ("rasd:Connection".equals(e.getTagName())) {
                    if (e.getParentNode() != null && e.getParentNode().getParentNode() != null) {
                        e.getParentNode().getParentNode().removeChild(e.getParentNode());
                    }
                }
            }
            final DOMSource domSource = new DOMSource(doc);
            final StringWriter writer = new StringWriter();
            final StreamResult result = new StreamResult(writer);
            final TransformerFactory tf = TransformerFactory.newInstance();
            final Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
        } catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {
            s_logger.warn("Unexpected exception caught while removing network elements from OVF:", e);
        }
        return ovfString;
    }

    public static List<Pair<String, Boolean>> readOvf(VmwareHypervisorHost host, String ovfFilePath, DatastoreMO dsMo) throws Exception {
        List<Pair<String, Boolean>> ovfVolumeInfos = new ArrayList<Pair<String, Boolean>>();
        List<String> files = new ArrayList<String>();

        ManagedObjectReference morRp = host.getHyperHostOwnerResourcePool();
        assert (morRp != null);
        ManagedObjectReference morHost = host.getMor();
        String importEntityName = UUID.randomUUID().toString();
        OvfCreateImportSpecParams importSpecParams = new OvfCreateImportSpecParams();
        importSpecParams.setHostSystem(morHost);
        importSpecParams.setLocale("US");
        importSpecParams.setEntityName(importEntityName);
        importSpecParams.setDeploymentOption("");

        String ovfDescriptor = removeOvfNetwork(HttpNfcLeaseMO.readOvfContent(ovfFilePath));
        VmwareContext context = host.getContext();
        OvfCreateImportSpecResult ovfImportResult = context.getService().createImportSpec(context.getServiceContent().getOvfManager(), ovfDescriptor, morRp, dsMo.getMor(),
                importSpecParams);

        if (ovfImportResult == null) {
            String msg = "createImportSpec() failed. ovfFilePath: " + ovfFilePath;
            s_logger.error(msg);
            throw new Exception(msg);
        }

        if (!ovfImportResult.getError().isEmpty()) {
            for (LocalizedMethodFault fault : ovfImportResult.getError()) {
                s_logger.error("createImportSpec error: " + fault.getLocalizedMessage());
            }
            throw new VcenterException("Failed to create an import spec from " + ovfFilePath + ". Check log for details.");
        }

        if (!ovfImportResult.getWarning().isEmpty()) {
            for (LocalizedMethodFault fault : ovfImportResult.getError()) {
                s_logger.warn("createImportSpec warning: " + fault.getLocalizedMessage());
            }
        }

        VirtualMachineImportSpec importSpec = (VirtualMachineImportSpec)ovfImportResult.getImportSpec();
        if (importSpec == null) {
            String msg = "createImportSpec() failed to create import specification for OVF template at " + ovfFilePath;
            s_logger.error(msg);
            throw new Exception(msg);
        }

        File ovfFile = new File(ovfFilePath);
        for (OvfFileItem ovfFileItem : ovfImportResult.getFileItem()) {
            String absFile = ovfFile.getParent() + File.separator + ovfFileItem.getPath();
            files.add(absFile);
        }


       int osDiskSeqNumber = 0;
       VirtualMachineConfigSpec config = importSpec.getConfigSpec();
       String paramVal = getOvfParamValue(config);
       if (paramVal != null && !paramVal.isEmpty()) {
           try {
               osDiskSeqNumber = getOsDiskFromOvfConf(config, paramVal);
           } catch (Exception e) {
               osDiskSeqNumber = 0;
           }
       }

        int diskCount = 0;
        int deviceCount = 0;
        List<VirtualDeviceConfigSpec> deviceConfigList = config.getDeviceChange();
        for (VirtualDeviceConfigSpec deviceSpec : deviceConfigList) {
            Boolean osDisk = false;
            VirtualDevice device = deviceSpec.getDevice();
            if (device instanceof VirtualDisk) {
                if ((osDiskSeqNumber == 0 && diskCount == 0) || osDiskSeqNumber == deviceCount) {
                    osDisk = true;
                }
                Pair<String, Boolean> ovfVolumeInfo = new Pair<String, Boolean>(files.get(diskCount), osDisk);
                ovfVolumeInfos.add(ovfVolumeInfo);
                diskCount++;
            }
            deviceCount++;
        }
        return ovfVolumeInfos;
    }

    public static void createOvfFile(VmwareHypervisorHost host, String diskFileName, String ovfName, String datastorePath, String templatePath, long diskCapacity, long fileSize,
            ManagedObjectReference morDs) throws Exception {
        VmwareContext context = host.getContext();
        ManagedObjectReference morOvf = context.getServiceContent().getOvfManager();
        VirtualMachineMO workerVmMo = HypervisorHostHelper.createWorkerVm(host, new DatastoreMO(context, morDs), ovfName);
        if (workerVmMo == null) {
            throw new Exception("Unable to find just-created worker VM");
        }

        String[] disks = {datastorePath + File.separator + diskFileName};
        try {
            VirtualMachineConfigSpec vmConfigSpec = new VirtualMachineConfigSpec();
            VirtualDeviceConfigSpec deviceConfigSpec = new VirtualDeviceConfigSpec();

            // Reconfigure worker VM with datadisk
            VirtualDevice device = VmwareHelper.prepareDiskDevice(workerVmMo, null, -1, disks, morDs, -1, 1);
            deviceConfigSpec.setDevice(device);
            deviceConfigSpec.setOperation(VirtualDeviceConfigSpecOperation.ADD);
            vmConfigSpec.getDeviceChange().add(deviceConfigSpec);
            workerVmMo.configureVm(vmConfigSpec);

            // Write OVF descriptor file
            OvfCreateDescriptorParams ovfDescParams = new OvfCreateDescriptorParams();
            String deviceId = File.separator + workerVmMo.getMor().getValue() + File.separator + "VirtualIDEController0:0";
            OvfFile ovfFile = new OvfFile();
            ovfFile.setPath(diskFileName);
            ovfFile.setDeviceId(deviceId);
            ovfFile.setSize(fileSize);
            ovfFile.setCapacity(diskCapacity);
            ovfDescParams.getOvfFiles().add(ovfFile);
            OvfCreateDescriptorResult ovfCreateDescriptorResult = context.getService().createDescriptor(morOvf, workerVmMo.getMor(), ovfDescParams);

            String ovfPath = templatePath + File.separator + ovfName + ".ovf";
            try {
                FileWriter out = new FileWriter(ovfPath);
                out.write(ovfCreateDescriptorResult.getOvfDescriptor());
                out.close();
            } catch (Exception e) {
                throw e;
            }
        } finally {
            workerVmMo.detachAllDisks();
            workerVmMo.destroy();
        }
    }

    public static int getOsDiskFromOvfConf(VirtualMachineConfigSpec config, String deviceLocation) {
        List<VirtualDeviceConfigSpec> deviceConfigList = config.getDeviceChange();
        int controllerKey = 0;
        int deviceSeqNumber = 0;
        int controllerNumber = 0;
        int deviceNodeNumber = 0;
        int controllerCount = 0;
        String[] virtualNodeInfo = deviceLocation.split(":");

        if (deviceLocation.startsWith("scsi")) {
            // get substring excluding prefix scsi
           controllerNumber = Integer.parseInt(virtualNodeInfo[0].substring(4));
           deviceNodeNumber = Integer.parseInt(virtualNodeInfo[1]);

           for (VirtualDeviceConfigSpec deviceConfig : deviceConfigList) {
               VirtualDevice device = deviceConfig.getDevice();
               if (device instanceof VirtualSCSIController) {
                   if (controllerNumber == controllerCount) {
                       controllerKey = device.getKey();
                       break;
                   }
                   controllerCount++;
               }
           }
       } else {
            // get substring excluding prefix ide
           controllerNumber = Integer.parseInt(virtualNodeInfo[0].substring(3));
           deviceNodeNumber = Integer.parseInt(virtualNodeInfo[1]);
           controllerCount = 0;

           for (VirtualDeviceConfigSpec deviceConfig : deviceConfigList) {
               VirtualDevice device = deviceConfig.getDevice();
               if (device instanceof VirtualIDEController) {
                   if (controllerNumber == controllerCount) {
                       // Only 2 IDE controllers supported and they will have bus numbers 0 and 1
                       controllerKey = device.getKey();
                       break;
                   }
                   controllerCount++;
               }
           }
       }
       // Get devices on this controller at specific device node.
       for (VirtualDeviceConfigSpec deviceConfig : deviceConfigList) {
           VirtualDevice device = deviceConfig.getDevice();
           if (device instanceof VirtualDisk) {
               if (controllerKey == device.getControllerKey() && deviceNodeNumber == device.getUnitNumber()) {
                   break;
               }
               deviceSeqNumber++;
           }
       }
       return deviceSeqNumber;
   }

   public static String getOvfParamValue(VirtualMachineConfigSpec config) {
       String paramVal = "";
       List<OptionValue> options = config.getExtraConfig();
       for (OptionValue option : options) {
           if (OVA_OPTION_KEY_BOOTDISK.equalsIgnoreCase(option.getKey())) {
               paramVal = (String)option.getValue();
               break;
           }
       }
       return paramVal;
   }


    public static String getScsiController(Pair<String, String> controllerInfo, String recommendedController) {
        String rootDiskController = controllerInfo.first();
        String dataDiskController = controllerInfo.second();

        // If "osdefault" is specified as controller type, then translate to actual recommended controller.
        if (VmwareHelper.isControllerOsRecommended(rootDiskController)) {
            rootDiskController = recommendedController;
        }
        if (VmwareHelper.isControllerOsRecommended(dataDiskController)) {
            dataDiskController = recommendedController;
        }
        //If any of the controller provided is SCSI then return it's sub-type.
        String scsiDiskController = null;
        if (isIdeController(rootDiskController) && isIdeController(dataDiskController)) {
            //Default controllers would exist
            return null;
        } else if (isIdeController(rootDiskController) || isIdeController(dataDiskController)) {
            // Only one of the controller types is IDE. Pick the other controller type to create controller.
            if (isIdeController(rootDiskController)) {
                scsiDiskController = dataDiskController;
            } else {
                scsiDiskController = rootDiskController;
            }
        } else if (DiskControllerType.getType(rootDiskController) != DiskControllerType.getType(dataDiskController)) {
            // Both ROOT and DATA controllers are SCSI controllers but different sub-types, then prefer ROOT controller
            scsiDiskController = rootDiskController;
        } else {
            // Both are SCSI controllers.
            scsiDiskController = rootDiskController;
        }
        return scsiDiskController;
    }

    public static boolean isIdeController(String controller) {
        return DiskControllerType.getType(controller) == DiskControllerType.ide;
    }

}
