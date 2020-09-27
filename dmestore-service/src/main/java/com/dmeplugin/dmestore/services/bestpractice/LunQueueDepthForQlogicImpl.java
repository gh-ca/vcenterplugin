package com.dmeplugin.dmestore.services.bestpractice;

import com.dmeplugin.dmestore.utils.VCSDKUtils;

/**
 * @ClassName LunQueueDepthForQlogicImpl
 * @Description 板卡队列深度 For QLogic
 * @Author wangxiangyong
 * @Date 2020/9/15 10:26
 * @Version V1.0
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
    public Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception{
        return super.getCurrentModuleOption(vcsdkUtils, objectId, getOptionName());
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
        return super.checkModuleOption(vcsdkUtils, objectId, getOptionName(), getRecommendValue());
    }

    @Override
    public void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception{
        super.updateModuleOption(vcsdkUtils, objectId, getOptionName(), getRecommendValue());
    }

    private String getOptionName(){
        return "ql2xmaxqdepth";
    }
}
