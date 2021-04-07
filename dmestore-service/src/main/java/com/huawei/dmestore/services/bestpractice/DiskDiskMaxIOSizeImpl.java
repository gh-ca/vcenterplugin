package com.huawei.dmestore.services.bestpractice;

import com.huawei.dmestore.utils.VCSDKUtils;
import com.huawei.vmware.mo.HostMo;
import com.huawei.vmware.util.VmwareContext;

import com.vmware.vim25.AboutInfo;
import com.vmware.vim25.ManagedObjectReference;

import java.util.HashMap;
import java.util.Map;

/**
 * DiskDiskMaxIOSizeImpl
 *
 * @author wangxiangyong
 * @since 2020-11-30
 **/
public class DiskDiskMaxIOSizeImpl extends BaseBestPracticeService implements BestPracticeService {
    private static final Map<String, Long> recommendMap = new HashMap() {{
        put("6.7up", 32767L);
        put("6.7low", 1024L);
    }};

    @Override
    public String getHostSetting() {
        return "Disk.DiskMaxIOSize";
    }

    @Override
    public Object getRecommendValue() {
        // ESXi6.7U1以前的版本默认值为1024，ESXi6.7U1以及之后的版本默认值为32767
        return "1024|32767";
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
        return super.check(vcsdkUtils, objectId, getHostSetting(),
            recommendMap.get(getRecommendValueKey(vcsdkUtils, objectId)));
    }

    private boolean isSupportDefaultValue(String bulidNum) {
        String[] isNotSupports = new String[] {"10176752", "9484548", "8941472", "9214924", "8169962"};
        for (String str : isNotSupports) {
            if (str.equals(bulidNum)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        super.update(vcsdkUtils, objectId, getHostSetting(),
            recommendMap.get(getRecommendValueKey(vcsdkUtils, objectId)));
    }

    private String getRecommendValueKey(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        // ESXi版本检测，6.7U1以前的版本才建议进行修改
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMo hostMo = this.getHostMoFactory().build(context, mor);
        AboutInfo aboutInfo = hostMo.getHostAboutInfo();
        String esxiApiVersion = aboutInfo.getApiVersion().substring(0, 2);
        String bulidNum = aboutInfo.getBuild();
        String recommendValueKey = "6.7low";
        if (Float.valueOf(esxiApiVersion) > Float.valueOf("6.5")) {
            if (isSupportDefaultValue(bulidNum)) {
                recommendValueKey = "6.7up";
            }
        }
        return recommendValueKey;
    }
}
