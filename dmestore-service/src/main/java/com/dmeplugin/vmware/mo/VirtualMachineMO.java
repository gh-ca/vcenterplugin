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

import com.dmeplugin.vmware.util.Pair;
import com.dmeplugin.vmware.util.VmwareContext;
import com.dmeplugin.vmware.util.VmwareHelper;
import com.google.gson.Gson;
import com.vmware.vim25.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xxxx
 **/
public class VirtualMachineMO extends BaseMO {
    private static final Logger s_logger = LoggerFactory.getLogger(VirtualMachineMO.class);

    public VirtualMachineMO(VmwareContext context, ManagedObjectReference morVm) {
        super(context, morVm);
    }

    public Pair<DatacenterMO, String> getOwnerDatacenter() throws Exception {
        return DatacenterMO.getOwnerDatacenter(getContext(), getMor());
    }

    public HostMO getRunningHost() throws Exception {
        VirtualMachineRuntimeInfo runtimeInfo = getRuntimeInfo();
        return new HostMO(context, runtimeInfo.getHost());
    }

    public VirtualMachineRuntimeInfo getRuntimeInfo() throws Exception {
        return (VirtualMachineRuntimeInfo) context.getVimClient().getDynamicProperty(mor, "runtime");
    }

    public VirtualMachineFileInfo getFileInfo() throws Exception {
        return (VirtualMachineFileInfo) context.getVimClient().getDynamicProperty(mor, "config.files");
    }

    @Override
    public ManagedObjectReference getParentMor() throws Exception {
        return (ManagedObjectReference) context.getVimClient().getDynamicProperty(mor, "parent");
    }

    public void createDisk(String vmdkDatastorePath, VirtualDiskType diskType, VirtualDiskMode diskMode,
        String rdmDeviceName, long sizeInMb, ManagedObjectReference morDs, int controllerKey) throws Exception {
        assert (vmdkDatastorePath != null);
        assert (morDs != null);

        //2020-11-06 wangxiangyong 修改为SCSI
        //int ideControllerKey = getIdeDeviceControllerKey();
        int ideControllerKey = getLsiLogicControllerKey();
        if (controllerKey < 0) {
            controllerKey = ideControllerKey;
        }

        VirtualDisk newDisk = new VirtualDisk();
        if (diskType == VirtualDiskType.THIN || diskType == VirtualDiskType.PREALLOCATED
            || diskType == VirtualDiskType.EAGER_ZEROED_THICK) {

            VirtualDiskFlatVer2BackingInfo backingInfo = new VirtualDiskFlatVer2BackingInfo();
            backingInfo.setDiskMode(VirtualDiskMode.PERSISTENT.value());
            if (diskType == VirtualDiskType.THIN) {
                backingInfo.setThinProvisioned(true);
            } else {
                backingInfo.setThinProvisioned(false);
            }

            if (diskType == VirtualDiskType.EAGER_ZEROED_THICK) {
                backingInfo.setEagerlyScrub(true);
            } else {
                backingInfo.setEagerlyScrub(false);
            }

            backingInfo.setDatastore(morDs);
            backingInfo.setFileName(vmdkDatastorePath);
            newDisk.setBacking(backingInfo);
        } else if (diskType == VirtualDiskType.RDM || diskType == VirtualDiskType.RDMP) {
            VirtualDiskRawDiskMappingVer1BackingInfo backingInfo = new VirtualDiskRawDiskMappingVer1BackingInfo();
            if (diskType == VirtualDiskType.RDM) {
                backingInfo.setCompatibilityMode("virtualMode");
            } else {
                backingInfo.setCompatibilityMode("physicalMode");
            }
            backingInfo.setDeviceName(rdmDeviceName);
            if (diskType == VirtualDiskType.RDM) {
                backingInfo.setDiskMode(diskMode.value());
            }

            backingInfo.setDatastore(morDs);
            backingInfo.setFileName(vmdkDatastorePath);
            newDisk.setBacking(backingInfo);
        }

        int deviceNumber = getNextDeviceNumber(controllerKey);

        newDisk.setControllerKey(controllerKey);
        newDisk.setKey(-deviceNumber);
        newDisk.setUnitNumber(deviceNumber);
        newDisk.setCapacityInKB(sizeInMb * 1024);

        VirtualMachineConfigSpec reConfigSpec = new VirtualMachineConfigSpec();
        VirtualDeviceConfigSpec deviceConfigSpec = new VirtualDeviceConfigSpec();

        deviceConfigSpec.setDevice(newDisk);
        deviceConfigSpec.setFileOperation(VirtualDeviceConfigSpecFileOperation.CREATE);
        deviceConfigSpec.setOperation(VirtualDeviceConfigSpecOperation.ADD);

        reConfigSpec.getDeviceChange().add(deviceConfigSpec);

        ManagedObjectReference morTask = context.getService().reconfigVMTask(mor, reConfigSpec);
        boolean result = context.getVimClient().waitForTask(morTask);

        if (!result) {
            throw new Exception(
                "Unable to create disk " + vmdkDatastorePath + " due to " + TaskMO.getTaskFailureInfo(context,
                    morTask));
        }

        context.waitForTaskProgressDone(morTask);
    }

    public Pair<VmdkFileDescriptor, byte[]> getVmdkFileInfo(String vmdkDatastorePath) throws Exception {
        Pair<DatacenterMO, String> dcPair = getOwnerDatacenter();

        String url = getContext().composeDatastoreBrowseUrl(dcPair.second(), vmdkDatastorePath);
        byte[] content = getContext().getResourceContent(url);
        VmdkFileDescriptor descriptor = new VmdkFileDescriptor();
        descriptor.parse(content);

        Pair<VmdkFileDescriptor, byte[]> result = new Pair<VmdkFileDescriptor, byte[]>(descriptor, content);
        if (s_logger.isTraceEnabled()) {
            s_logger.trace("vCenter API trace - getVmdkFileInfo() done");
            s_logger.trace("VMDK file descriptor: " + new Gson().toJson(result.first()));
        }
        return result;
    }

    @Deprecated
    public void setSnapshotDirectory(String snapshotDir) throws Exception {
        VirtualMachineFileInfo fileInfo = getFileInfo();
        Pair<DatacenterMO, String> dcInfo = getOwnerDatacenter();
        String vmxUrl = context.composeDatastoreBrowseUrl(dcInfo.second(), fileInfo.getVmPathName());
        byte[] vmxContent = context.getResourceContent(vmxUrl);

        BufferedReader in = null;
        BufferedWriter out = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        boolean replaced = false;
        try {
            in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(vmxContent), "UTF-8"));
            out = new BufferedWriter(new OutputStreamWriter(bos, "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("workingDir")) {
                    replaced = true;
                    out.write(String.format("workingDir=\"%s\"", snapshotDir));
                    out.newLine();
                } else {
                    out.write(line);
                    out.newLine();
                }
            }

            if (!replaced) {
                out.newLine();
                out.write(String.format("workingDir=\"%s\"", snapshotDir));
                out.newLine();
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
        context.uploadResourceContent(vmxUrl, bos.toByteArray());
    }

    @Deprecated
    public void moveAllVmDiskFiles(DatastoreMO destDsMo, String destDsDir, boolean followDiskChain) throws Exception {
        VirtualDevice[] disks = getAllDiskDevice();
        DatacenterMO dcMo = getOwnerDatacenter().first();
        if (disks != null) {
            for (VirtualDevice disk : disks) {
                List<Pair<String, ManagedObjectReference>> vmdkFiles = getDiskDatastorePathChain((VirtualDisk) disk,
                    followDiskChain);
                for (Pair<String, ManagedObjectReference> fileItem : vmdkFiles) {
                    DatastoreMO srcDsMo = new DatastoreMO(context, fileItem.second());

                    DatastoreFile srcFile = new DatastoreFile(fileItem.first());
                    DatastoreFile destFile = new DatastoreFile(destDsMo.getName(), destDsDir, srcFile.getFileName());

                    Pair<VmdkFileDescriptor, byte[]> vmdkDescriptor = null;
                    vmdkDescriptor = getVmdkFileInfo(fileItem.first());

                    s_logger.info("Move VM disk file " + srcFile.getPath() + " to " + destFile.getPath());
                    srcDsMo.moveDatastoreFile(fileItem.first(), dcMo.getMor(), destDsMo.getMor(), destFile.getPath(),
                        dcMo.getMor(), true);

                    if (vmdkDescriptor != null) {
                        String vmdkBaseFileName = vmdkDescriptor.first().getBaseFileName();
                        String baseFilePath = srcFile.getCompanionPath(vmdkBaseFileName);
                        destFile = new DatastoreFile(destDsMo.getName(), destDsDir, vmdkBaseFileName);

                        s_logger.info("Move VM disk file " + baseFilePath + " to " + destFile.getPath());
                        srcDsMo.moveDatastoreFile(baseFilePath, dcMo.getMor(), destDsMo.getMor(), destFile.getPath(),
                            dcMo.getMor(), true);
                    }
                }
            }
        }
    }

    public int getScsiDeviceControllerKeyNoException() throws Exception {
        List<VirtualDevice> devices = context.getVimClient().getDynamicProperty(mor, "config.hardware.device");

        if (devices != null && devices.size() > 0) {
            for (VirtualDevice device : devices) {
                if (device instanceof VirtualSCSIController && isValidScsiDiskController(
                    (VirtualSCSIController) device)) {
                    return device.getKey();
                }
            }
        }

        return -1;
    }

    private boolean isValidScsiDiskController(VirtualSCSIController scsiDiskController) {
        if (scsiDiskController == null) {
            return false;
        }

        List<Integer> scsiDiskDevicesOnController = scsiDiskController.getDevice();
        if (scsiDiskDevicesOnController == null
            || scsiDiskDevicesOnController.size() >= (VmwareHelper.MAX_SUPPORTED_DEVICES_SCSI_CONTROLLER)) {
            return false;
        }

        if (scsiDiskController.getBusNumber() >= VmwareHelper.MAX_SCSI_CONTROLLER_COUNT) {
            return false;
        }

        return true;
    }

    @Deprecated
    public List<Pair<String, ManagedObjectReference>> getDiskDatastorePathChain(VirtualDisk disk, boolean followChain)
        throws Exception {
        VirtualDeviceBackingInfo backingInfo = disk.getBacking();
        if (!(backingInfo instanceof VirtualDiskFlatVer2BackingInfo)) {
            throw new Exception("Unsupported VirtualDeviceBackingInfo");
        }

        List<Pair<String, ManagedObjectReference>> pathList = new ArrayList<Pair<String, ManagedObjectReference>>();
        VirtualDiskFlatVer2BackingInfo diskBackingInfo = (VirtualDiskFlatVer2BackingInfo) backingInfo;

        if (!followChain) {
            pathList.add(new Pair<>(diskBackingInfo.getFileName(), diskBackingInfo.getDatastore()));
            return pathList;
        }

        Pair<DatacenterMO, String> dcPair = getOwnerDatacenter();
        VirtualMachineFileInfo vmFilesInfo = getFileInfo();
        DatastoreFile snapshotDirFile = new DatastoreFile(vmFilesInfo.getSnapshotDirectory());
        DatastoreFile vmxDirFile = new DatastoreFile(vmFilesInfo.getVmPathName());

        do {
            if (diskBackingInfo.getParent() != null) {
                pathList.add(new Pair<>(diskBackingInfo.getFileName(), diskBackingInfo.getDatastore()));
                diskBackingInfo = diskBackingInfo.getParent();
            } else {
                byte[] content;
                try {
                    String url = getContext().composeDatastoreBrowseUrl(dcPair.second(), diskBackingInfo.getFileName());
                    content = getContext().getResourceContent(url);
                    if (content == null || content.length == 0) {
                        break;
                    }

                    pathList.add(new Pair<>(diskBackingInfo.getFileName(), diskBackingInfo.getDatastore()));
                } catch (Exception e) {
                    DatastoreFile currentFile = new DatastoreFile(diskBackingInfo.getFileName());
                    String vmdkFullDsPath = snapshotDirFile.getCompanionPath(currentFile.getFileName());

                    String url = getContext().composeDatastoreBrowseUrl(dcPair.second(), vmdkFullDsPath);
                    content = getContext().getResourceContent(url);
                    if (content == null || content.length == 0) {
                        break;
                    }

                    pathList.add(new Pair<>(vmdkFullDsPath, diskBackingInfo.getDatastore()));
                }

                VmdkFileDescriptor descriptor = new VmdkFileDescriptor();
                descriptor.parse(content);
                if (descriptor.getParentFileName() != null && !descriptor.getParentFileName().isEmpty()) {
                    VirtualDiskFlatVer2BackingInfo parentDiskBackingInfo = new VirtualDiskFlatVer2BackingInfo();
                    parentDiskBackingInfo.setDatastore(diskBackingInfo.getDatastore());

                    String parentFileName = descriptor.getParentFileName();
                    if (parentFileName.startsWith("/")) {
                        int fileNameStartPos = parentFileName.lastIndexOf("/");
                        parentFileName = parentFileName.substring(fileNameStartPos + 1);
                        parentDiskBackingInfo.setFileName(vmxDirFile.getCompanionPath(parentFileName));
                    } else {
                        parentDiskBackingInfo.setFileName(snapshotDirFile.getCompanionPath(parentFileName));
                    }
                    diskBackingInfo = parentDiskBackingInfo;
                } else {
                    break;
                }
            }
        } while (diskBackingInfo != null);

        return pathList;
    }

    public VirtualDisk[] getAllDiskDevice() throws Exception {
        List<VirtualDisk> deviceList = new ArrayList<VirtualDisk>();
        List<VirtualDevice> devices = context.getVimClient().getDynamicProperty(mor, "config.hardware.device");
        if (devices != null && devices.size() > 0) {
            for (VirtualDevice device : devices) {
                if (device instanceof VirtualDisk) {
                    deviceList.add((VirtualDisk) device);
                }
            }
        }

        return deviceList.toArray(new VirtualDisk[0]);
    }

    public int getLsiLogicControllerKey() throws Exception {
        List<VirtualDevice> devices = context.getVimClient().getDynamicProperty(mor, "config.hardware.device");

        if (devices != null && devices.size() > 0) {
            for (VirtualDevice device : devices) {
                if (device instanceof VirtualLsiLogicController) {
                    return device.getKey();
                }
            }
        }

        assert (false);
        throw new Exception("IDE Controller Not Found");
    }

    public int getNextDeviceNumber(int controllerKey) throws Exception {
        List<VirtualDevice> devices = context.getVimClient().getDynamicProperty(mor, "config.hardware.device");

        List<Integer> existingUnitNumbers = new ArrayList<Integer>();
        int deviceNumber = 0;
        int scsiControllerKey = getScsiDeviceControllerKeyNoException();
        if (devices != null && devices.size() > 0) {
            for (VirtualDevice device : devices) {
                if (device.getControllerKey() != null && device.getControllerKey().intValue() == controllerKey) {
                    existingUnitNumbers.add(device.getUnitNumber());
                }
            }
        }
        while (true) {
            if (!existingUnitNumbers.contains(Integer.valueOf(deviceNumber))) {
                if (controllerKey != scsiControllerKey || !VmwareHelper.isReservedScsiDeviceNumber(deviceNumber)) {
                    break;
                }
            }
            ++deviceNumber;
        }
        return deviceNumber;
    }
}
