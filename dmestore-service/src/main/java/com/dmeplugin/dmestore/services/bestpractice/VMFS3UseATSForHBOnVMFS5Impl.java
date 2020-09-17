package com.dmeplugin.dmestore.services.bestpractice;

import com.dmeplugin.vmware.util.VmwareContext;

/**
 * @ClassName VMFS3UseATSForHBOnVMFS5Impl
 * @Description TODO
 * @Author wangxiangyong
 * @Date 2020/9/15 10:26
 * @Version V1.0
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
    public Object getCurrentValue(VmwareContext context, String hostName) throws Exception{
        return super.getCurrentValue(context, hostName, getHostSetting());
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
    public boolean check(VmwareContext context, String hostName) throws Exception {
        return super.check(context, hostName, getHostSetting(), getRecommendValue());
    }

    @Override
    public void update(VmwareContext context, String hostName) throws Exception{
        super.update(context, hostName, getHostSetting(), getRecommendValue());
    }
}
