package com.huawei.dmestore.services.bestpractice;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.huawei.dmestore.utils.VCSDKUtils;
import com.huawei.vmware.mo.HostMoObj;
import com.huawei.vmware.mo.HostStorageSystemMoObj;
import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.*;

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
        List<HostMultipathInfoLogicalUnit> lunList = getLuns(vcsdkUtils, objectId);
        JsonArray array = new JsonArray();
        for (HostMultipathInfoLogicalUnit lun : lunList) {
            HostMultipathInfoLogicalUnitPolicy policy = lun.getPolicy();
            String policyStr = policy.getPolicy();
            if (!policyStr.equals(getRecommendValue())) {
                JsonObject object = new JsonObject();
                object.addProperty("name", "naa." + lun.getId().substring(10, 42));
                object.addProperty("value", policyStr);
                array.add(object);
            }
        }

        return array.toString();
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
        List<HostMultipathInfoLogicalUnit> lunList = getLuns(vcsdkUtils, objectId);
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
        HostMoObj hostMo = this.getHostMoFactory().build(context, mor);
        HostStorageSystemMoObj hostStorageSystemMo = hostMo.getHostStorageSystemMo();
        List<HostMultipathInfoLogicalUnit> lunList = getLuns(vcsdkUtils, objectId);
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

    private List<HostMultipathInfoLogicalUnit> getLuns(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMoObj hostMo = this.getHostMoFactory().build(context, mor);
        HostStorageSystemMoObj hostStorageSystemMo = hostMo.getHostStorageSystemMo();
        HostStorageDeviceInfo deviceInfo = hostStorageSystemMo.getStorageDeviceInfo();
        HostMultipathInfo hostMultipathInfo = deviceInfo.getMultipathInfo();
        List<HostMultipathInfoLogicalUnit> lunList = hostMultipathInfo.getLun();
        return lunList;
    }
}
