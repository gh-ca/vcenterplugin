package com.huawei.dmestore.services.bestpractice;

import com.huawei.dmestore.utils.VCSDKUtils;
import com.huawei.vmware.mo.HostMo;
import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.AboutInfo;
import com.vmware.vim25.ManagedObjectReference;

/**
 * VMFS3UseATSForHBOnVMFS5Impl
 *
 * @author wangxiangyong
 * @since 2020-11-30
 **/
public class VMFS3UseATSForHBOnVMFS5Impl extends BaseBestPracticeService implements BestPracticeService {
    @Override
    public String getHostSetting() {
        return "VMFS3.UseATSForHBOnVMFS5";
    }

    @Override
    public Object getRecommendValue() {
        return 0L;
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
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMo hostMo = this.getHostMoFactory().build(context, mor);
        AboutInfo aboutInfo = hostMo.getHostAboutInfo();
        String esxiApiVersion = aboutInfo.getApiVersion();
        // ESXI6.5及以后版本，推荐开启，也就是如果是低于6.5的版本直接认为检测通过。
        if(Float.valueOf(esxiApiVersion) < Float.valueOf("6.5")){
            return true;
        }
        return super.check(vcsdkUtils, objectId, getHostSetting(), getRecommendValue());
    }

    @Override
    public void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        super.update(vcsdkUtils, objectId, getHostSetting(), getRecommendValue());
    }
}
