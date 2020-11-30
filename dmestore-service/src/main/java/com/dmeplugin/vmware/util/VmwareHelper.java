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

package com.dmeplugin.vmware.util;

import com.dmeplugin.vmware.mo.DiskControllerType;
import com.vmware.vim25.GuestOsDescriptor;


public class VmwareHelper {
    public static final int MAX_SCSI_CONTROLLER_COUNT = 4;
    public static final int MAX_ALLOWED_DEVICES_SCSI_CONTROLLER = 16;
    public static final int MAX_SUPPORTED_DEVICES_SCSI_CONTROLLER = MAX_ALLOWED_DEVICES_SCSI_CONTROLLER - 1;

    public static boolean isReservedScsiDeviceNumber(int deviceNumber) {
        return (deviceNumber % VmwareHelper.MAX_ALLOWED_DEVICES_SCSI_CONTROLLER) == 7;
    }

    public static String getRecommendedDiskControllerFromDescriptor(GuestOsDescriptor guestOsDescriptor) throws Exception {
        String recommendedController;
        recommendedController = guestOsDescriptor.getRecommendedDiskController();
        if (DiskControllerType.getType(recommendedController) == DiskControllerType.pvscsi) {
            recommendedController = DiskControllerType.lsilogic.toString();
        }

        return recommendedController;
    }

}
