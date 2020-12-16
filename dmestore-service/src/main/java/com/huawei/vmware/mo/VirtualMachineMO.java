package com.huawei.vmware.mo;

import com.huawei.vmware.util.Pair;
import com.huawei.vmware.util.VmwareContext;
import com.huawei.vmware.util.VmwareHelper;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.VirtualDevice;
import com.vmware.vim25.VirtualDeviceConfigSpec;
import com.vmware.vim25.VirtualDeviceConfigSpecFileOperation;
import com.vmware.vim25.VirtualDeviceConfigSpecOperation;
import com.vmware.vim25.VirtualDisk;
import com.vmware.vim25.VirtualDiskFlatVer2BackingInfo;
import com.vmware.vim25.VirtualDiskMode;
import com.vmware.vim25.VirtualDiskRawDiskMappingVer1BackingInfo;
import com.vmware.vim25.VirtualDiskType;
import com.vmware.vim25.VirtualLsiLogicController;
import com.vmware.vim25.VirtualMachineConfigSpec;
import com.vmware.vim25.VirtualMachineFileInfo;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.VirtualSCSIController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * VirtualMachineMO
 *
 * @author xxxx
 * @since 2020-12-11
 **/
public class VirtualMachineMO extends BaseMO {
    private static final Logger logger = LoggerFactory.getLogger(VirtualMachineMO.class);
    private final String configDeviceStr = "config.hardware.device";
    private final int unit = 1024;
    private final int failResult = -1;

    /**
     * VirtualMachineMO
     *
     * @param context context
     * @param morVm   morVm
     */
    public VirtualMachineMO(VmwareContext context, ManagedObjectReference morVm) {
        super(context, morVm);
    }

    /**
     * getOwnerDatacenter
     *
     * @return Pair
     * @throws Exception Exception
     */
    public Pair<DatacenterMO, String> getOwnerDatacenter() throws Exception {
        return DatacenterMO.getOwnerDatacenter(context, mor);
    }

    /**
     * getRunningHost
     *
     * @return HostMO
     * @throws Exception Exception
     */
    public HostMO getRunningHost() throws Exception {
        VirtualMachineRuntimeInfo runtimeInfo = getRuntimeInfo();
        return new HostMO(context, runtimeInfo.getHost());
    }

    /**
     * getRuntimeInfo
     *
     * @return VirtualMachineRuntimeInfo
     * @throws Exception Exception
     */
    public VirtualMachineRuntimeInfo getRuntimeInfo() throws Exception {
        return (VirtualMachineRuntimeInfo) context.getVimClient().getDynamicProperty(mor, "runtime");
    }

    /**
     * getFileInfo
     *
     * @return VirtualMachineFileInfo
     * @throws Exception Exception
     */
    public VirtualMachineFileInfo getFileInfo() throws Exception {
        return (VirtualMachineFileInfo) context.getVimClient().getDynamicProperty(mor, "config.files");
    }

    @Override
    public ManagedObjectReference getParentMor() throws Exception {
        return (ManagedObjectReference) context.getVimClient().getDynamicProperty(mor, "parent");
    }

    /**
     * createDisk
     *
     * @param vmdkDatastorePath vmdkDatastorePath
     * @param diskType          diskType
     * @param diskMode          diskMode
     * @param rdmDeviceName     rdmDeviceName
     * @param sizeInMb          sizeInMb
     * @param morDs             morDs
     * @param controllerKey     controllerKey
     * @throws Exception Exception
     */
    public void createDisk(String vmdkDatastorePath, VirtualDiskType diskType, VirtualDiskMode diskMode,
                           String rdmDeviceName, long sizeInMb, ManagedObjectReference morDs, int controllerKey)
        throws Exception {
        assert vmdkDatastorePath != null;
        assert morDs != null;

        // 2020-11-06 wangxiangyong 修改为SCSI
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
        newDisk.setCapacityInKB(sizeInMb * unit);

        VirtualDeviceConfigSpec deviceConfigSpec = new VirtualDeviceConfigSpec();
        deviceConfigSpec.setDevice(newDisk);
        deviceConfigSpec.setFileOperation(VirtualDeviceConfigSpecFileOperation.CREATE);
        deviceConfigSpec.setOperation(VirtualDeviceConfigSpecOperation.ADD);

        VirtualMachineConfigSpec reConfigSpec = new VirtualMachineConfigSpec();
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

    /**
     * getVmdkFileInfo
     *
     * @param vmdkDatastorePath vmdkDatastorePath
     * @return Pair
     * @throws Exception Exception
     */
    public Pair<VmdkFileDescriptor, byte[]> getVmdkFileInfo(String vmdkDatastorePath) throws Exception {
        Pair<DatacenterMO, String> dcPair = getOwnerDatacenter();
        String url = context.composeDatastoreBrowseUrl(dcPair.second(), vmdkDatastorePath);
        byte[] content = context.getResourceContent(url);
        VmdkFileDescriptor descriptor = new VmdkFileDescriptor();
        descriptor.parse(content);

        Pair<VmdkFileDescriptor, byte[]> result = new Pair<>(descriptor, content);

        return result;
    }

    /**
     * getScsiDeviceControllerKeyNoException
     *
     * @return int
     * @throws Exception Exception
     */
    public int getScsiDeviceControllerKeyNoException() throws Exception {
        List<VirtualDevice> devices = context.getVimClient().getDynamicProperty(mor, configDeviceStr);

        if (devices != null && devices.size() > 0) {
            for (VirtualDevice device : devices) {
                if (device instanceof VirtualSCSIController
                    && isValidScsiDiskController((VirtualSCSIController) device)) {
                    return device.getKey();
                }
            }
        }

        return failResult;
    }

    /**
     * isValidScsiDiskController
     *
     * @param scsiDiskController scsiDiskController
     * @return boolean
     */
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

    /**
     * getLsiLogicControllerKey
     *
     * @return int
     * @throws Exception Exception
     */
    public int getLsiLogicControllerKey() throws Exception {
        List<VirtualDevice> devices = context.getVimClient().getDynamicProperty(mor, configDeviceStr);

        if (devices != null && devices.size() > 0) {
            for (VirtualDevice device : devices) {
                if (device instanceof VirtualLsiLogicController) {
                    return device.getKey();
                }
            }
        }

        assert false;
        throw new Exception("IDE Controller Not Found");
    }

    /**
     * getNextDeviceNumber
     *
     * @param controllerKey controllerKey
     * @return int
     * @throws Exception Exception
     */
    public int getNextDeviceNumber(int controllerKey) throws Exception {
        List<VirtualDevice> devices = context.getVimClient().getDynamicProperty(mor, configDeviceStr);

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
