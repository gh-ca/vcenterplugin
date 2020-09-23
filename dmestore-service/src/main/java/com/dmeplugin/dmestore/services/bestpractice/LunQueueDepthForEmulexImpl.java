package com.dmeplugin.dmestore.services.bestpractice;

import com.dmeplugin.vmware.util.VmwareContext;

/**
 * @ClassName LunQueueDepthForEmulexImpl
 * @Description 板卡队列深度 For Emulex
 * @Author wangxiangyong
 * @Date 2020/9/15 10:26
 * @Version V1.0
 **/
public class LunQueueDepthForEmulexImpl extends BaseBestPracticeService implements BestPracticeService {
    @Override
    public String getHostSetting() {
        return "LUN Queue Depth for Emulex";
    }

    @Override
    public Object getRecommendValue() {
        return "512";
    }

    @Override
    public Object getCurrentValue(VmwareContext context, String objectId) throws Exception{
        return super.getCurrentModuleOption(context, objectId, getOptionName());
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
    public boolean check(VmwareContext context, String objectId) throws Exception {
        return super.checkModuleOption(context, objectId, getOptionName(), getRecommendValue());
    }

    @Override
    public void update(VmwareContext context, String objectId) throws Exception{
        super.updateModuleOption(context, objectId, getOptionName(), getRecommendValue());
    }

    private String getOptionName(){
        return "lpfc0_lun_queue_depth";
    }
}
