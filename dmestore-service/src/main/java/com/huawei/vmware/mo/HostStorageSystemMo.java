package com.huawei.vmware.mo;

import com.huawei.vmware.util.VmwareContext;

import com.vmware.vim25.*;

import java.util.List;

/**
 * HostStorageSystemMO
 *
 * @author Administrator
 * @since 2020-12-11
 */
public class HostStorageSystemMo extends BaseMo {
    /**
     * HostStorageSystemMO
     *
     * @param context context
     * @param morHostDatastore morHostDatastore
     */
    public HostStorageSystemMo(VmwareContext context, ManagedObjectReference morHostDatastore) {
        super(context, morHostDatastore);
    }

    /**
     * getStorageDeviceInfo
     *
     * @return HostStorageDeviceInfo
     * @throws Exception Exception
     */
    public HostStorageDeviceInfo getStorageDeviceInfo() throws Exception {
        return (HostStorageDeviceInfo) context.getVimClient().getDynamicProperty(mor, "storageDeviceInfo");
    }

    /**
     * getHostFileSystemVolumeInfo
     *
     * @return HostFileSystemVolumeInfo
     * @throws Exception Exception
     */
    public HostFileSystemVolumeInfo getHostFileSystemVolumeInfo() throws Exception {
        return context.getVimClient().getDynamicProperty(mor, "fileSystemVolumeInfo");
    }

    /**
     * rescanHba
     *
     * @param isCsiHbaDevice isCsiHbaDevice
     * @throws Exception Exception
     */
    public void rescanHba(String isCsiHbaDevice) throws Exception {
        context.getService().rescanHba(mor, isCsiHbaDevice);
    }

    /**
     * rescanVmfs
     *
     * @throws Exception Exception
     */
    public void rescanVmfs() throws Exception {
        context.getService().rescanVmfs(mor);
    }

    /**
     * refreshStorageSystem
     *
     * @throws Exception Exception
     */
    public void refreshStorageSystem() throws Exception {
        context.getService().refreshStorageSystem(mor);
    }

    /**
     * mountVmfsVolume
     *
     * @param datastoreUuid datastoreUuid
     * @throws Exception Exception
     */
    public void mountVmfsVolume(String datastoreUuid) throws Exception {
        context.getService().mountVmfsVolume(mor, datastoreUuid);
    }

    /**
     * unmountVmfsVolume
     *
     * @param datastoreUuid datastoreUuid
     * @throws Exception Exception
     */
    public void unmountVmfsVolume(String datastoreUuid) throws Exception {
        context.getService().unmountVmfsVolume(mor, datastoreUuid);
    }

    /**
     * unmapVmfsVolumeExTask
     *
     * @param vmfsUuids vmfsUuids
     * @throws Exception Exception
     */
    public void unmapVmfsVolumeExTask(List<String> vmfsUuids) throws Exception {
        context.getService().unmapVmfsVolumeExTask(mor, vmfsUuids);
    }

    /**
     * setMultipathLunPolicy
     *
     * @param lunId lunId
     * @param hostMultipathInfoLogicalUnitPolicy hostMultipathInfoLogicalUnitPolicy
     * @throws Exception Exception
     */
    public void setMultipathLunPolicy(String lunId,
        HostMultipathInfoLogicalUnitPolicy hostMultipathInfoLogicalUnitPolicy) throws Exception {
        context.getService().setMultipathLunPolicy(mor, lunId, hostMultipathInfoLogicalUnitPolicy);
    }

    /**
     * addInternetScsiSendTargets
     *
     * @param iscsiHbaDevice iscsiHbaDevice
     * @param targets targets
     * @throws HostConfigFaultFaultMsg HostConfigFaultFaultMsg
     * @throws NotFoundFaultMsg NotFoundFaultMsg
     * @throws RuntimeFaultFaultMsg RuntimeFaultFaultMsg
     */
    public void addInternetScsiSendTargets(String iscsiHbaDevice, List<HostInternetScsiHbaSendTarget> targets)
        throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
        context.getService().addInternetScsiSendTargets(mor, iscsiHbaDevice, targets);
    }

    /**
     * updateVmfsUnmapPriority
     *
     * @param vmfsUuid vmfsUuid
     * @param unmapPriority unmapPriority
     * @throws HostConfigFaultFaultMsg HostConfigFaultFaultMsg
     * @throws NotFoundFaultMsg NotFoundFaultMsg
     * @throws RuntimeFaultFaultMsg RuntimeFaultFaultMsg
     */
    public void updateVmfsUnmapPriority(String vmfsUuid, String unmapPriority) throws RuntimeFaultFaultMsg {
        context.getService().updateVmfsUnmapPriority(mor, vmfsUuid, unmapPriority);
    }

    public List<HostDiskPartitionInfo> retrieveDiskPartitionInfo(List<String> uids) throws Exception {
        return context.getService().retrieveDiskPartitionInfo(mor, uids);
    }
}
