package com.dmeplugin.dmestore.services.bestpractice;

import com.dmeplugin.dmestore.utils.VCSDKUtils;

/**
 * @author wangxiangyong
 **/
public class DataMoverHardwareAcceleratedInitImpl extends BaseBestPracticeService implements BestPracticeService {
    @Override
    public String getHostSetting() {
        return "DataMover.HardwareAcceleratedInit";
    }

    @Override
    public Object getRecommendValue() {
        return 1L;
    }

    @Override
    public Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception{
        return super.getCurrentValue(vcsdkUtils, objectId, getHostSetting());
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
    public boolean check(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        return super.check(vcsdkUtils, objectId, getHostSetting(), getRecommendValue());
    }

    @Override
    public void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception{
        super.update(vcsdkUtils, objectId, getHostSetting(), getRecommendValue());
    }
}
