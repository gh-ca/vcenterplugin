package com.huawei.dmestore.services.bestpractice;

import com.huawei.dmestore.utils.VCSDKUtils;
import com.huawei.vmware.mo.HostMo;
import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.AboutInfo;
import com.vmware.vim25.ManagedObjectReference;

/**
 * LunQueueDepthForQlogicImpl
 *
 * @author wangxiangyong
 * @since 2020-11-30
 **/
public class LunQueueDepthForQlogicImpl extends BaseBestPracticeService implements BestPracticeService {
    @Override
    public String getHostSetting() {
        return "LUN Queue Depth for Qlogic";
    }

    @Override
    public Object getRecommendValue() {
        return "512";
    }

    @Override
    public Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        return super.getCurrentModuleOption(vcsdkUtils, objectId, getModuleName());
    }

    @Override
    public String getLevel() {
        return "Warning";
    }

    @Override
    public boolean needReboot() {
        return true;
    }

    @Override
    public boolean autoRepair() {
        return true;
    }

    @Override
    public boolean check(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        // ESXI6.7的版本才执行这一项检查。
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMo hostMo = this.getHostMoFactory().build(context, mor);
        AboutInfo aboutInfo = hostMo.getHostAboutInfo();
        String apiVersion = aboutInfo.getApiVersion();
        try {
            String esxiApiVersion = apiVersion.substring(0, 3);
            if (Float.valueOf(esxiApiVersion) != Float.valueOf("6.7")) {
                return true;
            }
        } catch (Exception ex) {
            logger.error("get host recommend value key error!host_ip={},apiVersion={}", hostMo.getName(), apiVersion);
            throw ex;
        }
        return super.checkModuleOption(vcsdkUtils, objectId, getModuleName(), getRecommendValue());
    }

    @Override
    public void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        super.updateModuleOption(vcsdkUtils, objectId,  getModuleName(), getOptionName(), getRecommendValue());
    }

    private String getModuleName() {
        return "qlnativefc";
    }

    private String getOptionName() {
        return "ql2xmaxqdepth";
    }
}
