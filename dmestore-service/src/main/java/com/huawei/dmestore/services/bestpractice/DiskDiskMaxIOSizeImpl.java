package com.huawei.dmestore.services.bestpractice;

import com.huawei.dmestore.utils.VCSDKUtils;
import com.huawei.vmware.mo.HostMo;
import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.AboutInfo;
import com.vmware.vim25.ManagedObjectReference;

/**
 * DiskDiskMaxIOSizeImpl
 *
 * @author wangxiangyong
 * @since 2020-11-30
 **/
public class DiskDiskMaxIOSizeImpl extends BaseBestPracticeService implements BestPracticeService {
    private static final long RECOMMEND_VALUE = 1024L;

    @Override
    public String getHostSetting() {
        return "Disk.DiskMaxIOSize";
    }

    @Override
    public Object getRecommendValue() {
        return RECOMMEND_VALUE;
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
        // ESXi版本检测，6.7U1以前的版本才建议进行修改
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMo hostMo = this.getHostMoFactory().build(context, mor);
        AboutInfo aboutInfo = hostMo.getHostAboutInfo();
        String esxiApiVersion = aboutInfo.getApiVersion();
        String bulidNum = aboutInfo.getBuild();
        if(Float.valueOf(esxiApiVersion) > Float.valueOf("6.5")){
            if(isSupportDefaultValue(bulidNum)){
                return true;
            }
        }
        return super.check(vcsdkUtils, objectId, getHostSetting(), getRecommendValue());
    }

    private boolean isSupportDefaultValue(String bulidNum){
        String[] isNotSupports = new String[]{"10176752","9484548","8941472","9214924","8169962"};
        for(String str : isNotSupports){
            if(str.equals(bulidNum)){
                return false;
            }
        }

        return true;
    }

    @Override
    public void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        super.update(vcsdkUtils, objectId, getHostSetting(), getRecommendValue());
    }
}
