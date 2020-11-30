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

import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.*;

import java.util.List;


public class HostStorageSystemMO extends BaseMO {
    public HostStorageSystemMO(VmwareContext context, ManagedObjectReference morHostDatastore) {
        super(context, morHostDatastore);
    }

    public HostStorageDeviceInfo getStorageDeviceInfo() throws Exception {
        return (HostStorageDeviceInfo) context.getVimClient().getDynamicProperty(mor, "storageDeviceInfo");
    }

    public HostFileSystemVolumeInfo getHostFileSystemVolumeInfo() throws Exception {
        return context.getVimClient().getDynamicProperty(mor, "fileSystemVolumeInfo");
    }

    public void rescanHba(String iScsiHbaDevice) throws Exception {
        context.getService().rescanHba(mor, iScsiHbaDevice);
    }

    public void rescanVmfs() throws Exception {
        context.getService().rescanVmfs(mor);
    }

    public void refreshStorageSystem() throws Exception {
        context.getService().refreshStorageSystem(mor);
    }

    public void mountVmfsVolume(String datastoreUuid) throws Exception {
        context.getService().mountVmfsVolume(mor, datastoreUuid);
    }

    public void unmountVmfsVolume(String datastoreUuid) throws Exception {
        context.getService().unmountVmfsVolume(mor, datastoreUuid);
    }

    public void setMultipathLunPolicy(String lunId, HostMultipathInfoLogicalUnitPolicy hostMultipathInfoLogicalUnitPolicy) throws Exception {
        context.getService().setMultipathLunPolicy(mor, lunId, hostMultipathInfoLogicalUnitPolicy);
    }

    public void addInternetScsiSendTargets(String iScsiHbaDevice, List<HostInternetScsiHbaSendTarget> targets) throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
        context.getService().addInternetScsiSendTargets(mor, iScsiHbaDevice, targets);
    }
}
