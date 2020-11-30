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
import com.dmeplugin.vmware.util.Pair;
import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HypervisorHostHelper {
    private static final Logger s_logger = LoggerFactory.getLogger(HypervisorHostHelper.class);
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
                            vmNameInvCenter = (String) objProp.getVal();
                        } else if (objProp.getName().contains(instanceNameCustomField)) {
                            if (objProp.getVal() != null) {
                                vmInternalCsName = ((CustomFieldStringValue) objProp.getVal()).getValue();
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

    public static String getVcenterApiVersion(VmwareContext serviceContext) throws Exception {
        String vcApiVersion = null;
        if (serviceContext != null) {
            vcApiVersion = serviceContext.getServiceContent().getAbout().getApiVersion();
        }
        return vcApiVersion;
    }

    public enum PvlanType {
        /**
         * promiscuous
         **/
        promiscuous,
        isolated,
        community;
    }

    public static VirtualMachineMO createWorkerVm(VmwareHypervisorHost hyperHost, DatastoreMO dsMo, String vmName) throws Exception {
        ManagedObjectReference morCluster = hyperHost.getHyperHostCluster();
        if (morCluster != null) {
            hyperHost = new ClusterMO(hyperHost.getContext(), morCluster);
        }

        VirtualMachineMO workingVm = null;
        VirtualMachineConfigSpec vmConfig = new VirtualMachineConfigSpec();
        vmConfig.setName(vmName);
        vmConfig.setMemoryMB((long) 4);
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

    public static String removeOvfNetwork(final String ovfString) {
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

        VirtualMachineImportSpec importSpec = (VirtualMachineImportSpec) ovfImportResult.getImportSpec();
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


    public static int getOsDiskFromOvfConf(VirtualMachineConfigSpec config, String deviceLocation) {
        List<VirtualDeviceConfigSpec> deviceConfigList = config.getDeviceChange();
        int controllerKey = 0;
        int deviceSeqNumber = 0;
        int controllerNumber = 0;
        int deviceNodeNumber = 0;
        int controllerCount = 0;
        String[] virtualNodeInfo = deviceLocation.split(":");

        if (deviceLocation.startsWith("scsi")) {
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
            controllerNumber = Integer.parseInt(virtualNodeInfo[0].substring(3));
            deviceNodeNumber = Integer.parseInt(virtualNodeInfo[1]);
            controllerCount = 0;

            for (VirtualDeviceConfigSpec deviceConfig : deviceConfigList) {
                VirtualDevice device = deviceConfig.getDevice();
                if (device instanceof VirtualIDEController) {
                    if (controllerNumber == controllerCount) {
                        controllerKey = device.getKey();
                        break;
                    }
                    controllerCount++;
                }
            }
        }
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
                paramVal = (String) option.getValue();
                break;
            }
        }
        return paramVal;
    }

}
