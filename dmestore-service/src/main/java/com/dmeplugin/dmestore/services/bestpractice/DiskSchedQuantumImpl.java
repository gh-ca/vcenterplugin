package com.dmeplugin.dmestore.services.bestpractice;

import com.dmeplugin.vmware.util.VmwareContext;

/**
 * @ClassName DiskSchedQuantumImpl
 * @Description TODO
 * @Author wangxiangyong
 * @Date 2020/9/15 10:26
 * @Version V1.0
 **/
public class DiskSchedQuantumImpl extends BaseBestPracticeService implements BestPracticeService {
    @Override
    public String getHostSetting() {
        return "Disk.SchedQuantum";
    }

    @Override
    public Object getRecommendValue() {
        return 64L;
    }

    @Override
    public Object getCurrentValue(VmwareContext context, String objectId) throws Exception{
        return super.getCurrentValue(context, objectId, getHostSetting());
    }

    @Override
    public String getLevel() {
        return "Warning";
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
    public boolean check(VmwareContext context, String objectId) throws Exception {
        return super.check(context, objectId, getHostSetting(), getRecommendValue());
    }

    @Override
    public void update(VmwareContext context, String objectId) throws Exception{
        super.update(context, objectId, getHostSetting(), getRecommendValue());
    }
}
