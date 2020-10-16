package com.dmeplugin.dmestore.services.bestpractice;

import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.dmeplugin.vmware.util.VmwareContext;

/**
 * @author admin
 */
public interface BestPracticeService {

    String getHostSetting();

    Object getRecommendValue();

    Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception;

    String getLevel();

    boolean needReboot();

    boolean autoRepair();

    boolean check(VCSDKUtils vcsdkUtils, String objectId) throws Exception;

    void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception;
}
