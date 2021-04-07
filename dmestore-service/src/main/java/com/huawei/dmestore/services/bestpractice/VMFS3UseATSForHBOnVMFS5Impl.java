package com.huawei.dmestore.services.bestpractice;

import com.huawei.dmestore.utils.VCSDKUtils;
import com.huawei.vmware.mo.HostMo;
import com.huawei.vmware.util.VmwareContext;

import com.vmware.vim25.AboutInfo;
import com.vmware.vim25.ManagedObjectReference;

import java.util.HashMap;
import java.util.Map;

/**
 * VMFS3UseATSForHBOnVMFS5Impl
 *
 * @author wangxiangyong
 * @since 2020-11-30
 **/
public class VMFS3UseATSForHBOnVMFS5Impl extends BaseBestPracticeService implements BestPracticeService {
    private static final Map<String, Long> recommendMap = new HashMap() {{
        put("6.5up", 1L);
        put("6.5low", 0L);
    }};

    @Override
    public String getHostSetting() {
        return "VMFS3.UseATSForHBOnVMFS5";
    }

    @Override
    public Object getRecommendValue() {
        return "0|1";
    }

    @Override
    public Object getRecommendValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        String key = getRecommendValueKey(vcsdkUtils, objectId);
        return recommendMap.get(key);
    }

    @Override
    public Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        return super.getCurrentValue(vcsdkUtils, objectId, getHostSetting());
    }

    @Override
    public String getLevel() {
        return "Critical";
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
        return super.check(vcsdkUtils, objectId, getHostSetting(),
            recommendMap.get(getRecommendValueKey(vcsdkUtils, objectId)));
    }

    @Override
    public void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        super.update(vcsdkUtils, objectId, getHostSetting(),
            recommendMap.get(getRecommendValueKey(vcsdkUtils, objectId)));
    }

    private String getRecommendValueKey(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        String recommendValueKey = "6.5low";
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMo hostMo = this.getHostMoFactory().build(context, mor);
        AboutInfo aboutInfo = hostMo.getHostAboutInfo();
        String apiVersion = aboutInfo.getApiVersion();
        try {
            String esxiApiVersion = apiVersion.substring(0, 3);
            // ESXI6.5及以后版本，推荐开启，也就是如果是低于6.5的版本直接认为检测通过。
            if (Float.valueOf(esxiApiVersion) >= Float.valueOf("6.5")) {
                recommendValueKey = "6.5up";
            }
        } catch (Exception ex) {
            logger.error("get host recommend value key error!host_ip={},apiVersion={}", hostMo.getName(), apiVersion);
            throw ex;
        }

        return recommendValueKey;
    }
}
