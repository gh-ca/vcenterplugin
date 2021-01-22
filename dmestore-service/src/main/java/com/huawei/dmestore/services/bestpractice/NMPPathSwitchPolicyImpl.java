package com.huawei.dmestore.services.bestpractice;

import com.huawei.dmestore.utils.VCSDKUtils;
import com.huawei.vmware.mo.HostMO;
import com.huawei.vmware.mo.HostStorageSystemMO;
import com.huawei.vmware.util.VmwareContext;

import com.vmware.vim25.HostMultipathInfo;
import com.vmware.vim25.HostMultipathInfoLogicalUnit;
import com.vmware.vim25.HostMultipathInfoLogicalUnitPolicy;
import com.vmware.vim25.HostStorageDeviceInfo;
import com.vmware.vim25.ManagedObjectReference;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * NMPPathSwitchPolicyImpl
 *
 * @author wangxiangyong
 * @since 2020-11-30
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
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = this.getHostMoFactory().build(context, mor);
        HostStorageSystemMO hostStorageSystemMo = hostMo.getHostStorageSystemMo();
        List<HostMultipathInfoLogicalUnit> lunList = hostStorageSystemMo.getStorageDeviceInfo()
            .getMultipathInfo()
            .getLun();
        for (HostMultipathInfoLogicalUnit lun : lunList) {
            HostMultipathInfoLogicalUnitPolicy policy = lun.getPolicy();
            String policyStr = policy.getPolicy();
            if (!policyStr.equals(getRecommendValue())) {
                return policyStr;
            }
        }

        return "--";
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
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = this.getHostMoFactory().build(context, mor);
        HostStorageSystemMO hostStorageSystemMo = hostMo.getHostStorageSystemMo();
        HostStorageDeviceInfo deviceInfo = hostStorageSystemMo.getStorageDeviceInfo();
        HostMultipathInfo hostMultipathInfo = deviceInfo.getMultipathInfo();
        List<HostMultipathInfoLogicalUnit> lunList = hostMultipathInfo.getLun();
        for (HostMultipathInfoLogicalUnit lun : lunList) {
            HostMultipathInfoLogicalUnitPolicy psp = lun.getPolicy();
            String pspPolicy = psp.getPolicy();

            // 多路径选路策略，集中式存储选择VMW_SATP_ALUA, VMW_PSP_RR
            if (!pspPolicy.equals(getRecommendValue())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        if (check(vcsdkUtils, objectId)) {
            return;
        }
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = this.getHostMoFactory().build(context, mor);
        HostStorageSystemMO hostStorageSystemMo = hostMo.getHostStorageSystemMo();
        List<HostMultipathInfoLogicalUnit> lunList = hostStorageSystemMo.getStorageDeviceInfo()
            .getMultipathInfo()
            .getLun();
        if (lunList != null && lunList.size() > 0) {
            ExecutorService executor = Executors.newFixedThreadPool(lunList.size());
            for (HostMultipathInfoLogicalUnit lun : lunList) {
                executor.execute(() -> {
                    HostMultipathInfoLogicalUnitPolicy policy = lun.getPolicy();
                    String pspPolicy = policy.getPolicy();

                    // 多路径选路策略，集中式存储选择VMW_SATP_ALUA, VMW_PSP_RR
                    if (!pspPolicy.equals(getRecommendValue())) {
                        policy.setPolicy((String) getRecommendValue());
                        try {
                            hostStorageSystemMo.setMultipathLunPolicy(lun.getId(), policy);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                });
            }
        }
    }
}
