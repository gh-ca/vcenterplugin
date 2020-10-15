package com.dmeplugin.dmestore.services.bestpractice;

import com.dmeplugin.dmestore.utils.VCSDKUtils;

/**
 * @author wangxiangyong
 **/
public class NMPPathSwitchPolicyImpl extends BaseBestPracticeService implements BestPracticeService {
    @Override
    public String getHostSetting() {
        return "NMP path switch policy";
    }

    @Override
    public Object getRecommendValue() {
        return new Integer(9000);
    }

    @Override
    public Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception{
        return super.getCurrentValue(vcsdkUtils, objectId, (Integer)getRecommendValue());
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
        return super.check(vcsdkUtils, objectId, getRecommendValue());
    }

    @Override
    public void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception{
        super.update(vcsdkUtils, objectId);
    }
}
