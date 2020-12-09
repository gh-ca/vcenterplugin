package com.dmeplugin.vmware.util;

import com.dmeplugin.vmware.mo.DiskControllerType;
import com.vmware.vim25.GuestOsDescriptor;

/**
 * @author Administrator
 */
public class VmwareHelper {
    public static final int MAX_SCSI_CONTROLLER_COUNT = 4;
    public static final int MAX_ALLOWED_DEVICES_SCSI_CONTROLLER = 16;
    public static final int MAX_SUPPORTED_DEVICES_SCSI_CONTROLLER = MAX_ALLOWED_DEVICES_SCSI_CONTROLLER - 1;

    public static boolean isReservedScsiDeviceNumber(int deviceNumber) {
        return (deviceNumber % VmwareHelper.MAX_ALLOWED_DEVICES_SCSI_CONTROLLER) == 7;
    }

    public static String getRecommendedDiskControllerFromDescriptor(GuestOsDescriptor guestOsDescriptor)
        throws Exception {
        String recommendedController = guestOsDescriptor.getRecommendedDiskController();
        if (DiskControllerType.getType(recommendedController) == DiskControllerType.pvscsi) {
            recommendedController = DiskControllerType.lsilogic.toString();
        }

        return recommendedController;
    }

}
