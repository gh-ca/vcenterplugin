package com.dmeplugin.dmestore.services.bestpractice;

import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.dmeplugin.vmware.mo.HostMO;
import com.dmeplugin.vmware.mo.HostStorageSystemMO;
import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.HostMultipathInfoLogicalUnit;
import com.vmware.vim25.HostMultipathInfoLogicalUnitPolicy;
import com.vmware.vim25.ManagedObjectReference;

import java.util.List;

/**
 * @author wangxiangyong
 **/
public class NMPPathSwitchPolicyImpl extends BaseBestPracticeService implements BestPracticeService {
    @Override
    public String getHostSetting() {
        return "NMP path switch policy";
    }

    @Override
    public Object getRecommendValue() {
        return "VMW_PSP_RR";
    }

    @Override
    public Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectID2MOR(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = new HostMO(context, mor);
        HostStorageSystemMO hostStorageSystemMo = hostMo.getHostStorageSystemMO();
        List<HostMultipathInfoLogicalUnit> lunList = hostStorageSystemMo.getStorageDeviceInfo().getMultipathInfo().getLun();
        for (HostMultipathInfoLogicalUnit lun : lunList) {
            HostMultipathInfoLogicalUnitPolicy policy = lun.getPolicy();
            String policyStr = policy.getPolicy();
            return policyStr;
        }

        return null;
    }

    @Override
    public String getLevel() {
        return "Major";
    }

    @Override
    public boolean needReboot() {
        return false;
    }

    @Override
    public boolean autoRepair() {
        return true;
    }

    @Override
    public boolean check(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectID2MOR(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = new HostMO(context, mor);
        HostStorageSystemMO hostStorageSystemMo = hostMo.getHostStorageSystemMO();
        List<HostMultipathInfoLogicalUnit> lunList = hostStorageSystemMo.getStorageDeviceInfo().getMultipathInfo().getLun();
        for (HostMultipathInfoLogicalUnit lun : lunList) {
            HostMultipathInfoLogicalUnitPolicy policy = lun.getPolicy();
            String policyStr = policy.getPolicy();
            //多路径选路策略，集中式存储选择VMW_SATP_ALUA, VMW_PSP_RR
            //TODO
            if(policyStr.contains("_PSP_") && !policy.equals("VMW_PSP_RR")){
                //return false;
            }

            if(policyStr.contains("_SATP_") && !policy.equals("VMW_SATP_ALUA")){
                //return false;
            }
        }

        return true;
    }

    @Override
    public void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        if (check(vcsdkUtils, objectId)) {
            return;
        }

        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectID2MOR(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = new HostMO(context, mor);
        HostStorageSystemMO hostStorageSystemMo = hostMo.getHostStorageSystemMO();
        List<HostMultipathInfoLogicalUnit> lunList = hostStorageSystemMo.getStorageDeviceInfo().getMultipathInfo().getLun();
        for (HostMultipathInfoLogicalUnit lun : lunList) {
            HostMultipathInfoLogicalUnitPolicy policy = lun.getPolicy();
            String policyStr = policy.getPolicy();
            //TODO
            //多路径选路策略，集中式存储选择VMW_SATP_ALUA, VMW_PSP_RR
            if(policyStr.equals("")){
                //policy.setPolicy("VMW_PSP_RR");
            }
            hostStorageSystemMo.setMultipathLunPolicy(lun.getId(), policy);
        }
    }
}
