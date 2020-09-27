package com.dmeplugin.dmestore.services.bestpractice;

import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.dmeplugin.vmware.util.VmwareContext;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName BestPracticeService.java
 * @Description TODO
 * @createTime 2020年09月15日 10:06:00
 */
public interface BestPracticeService {
    /**
     * @Author Administrator
     * @Description 检查项
     * @Date 10:15 2020/9/15
     * @Param []
     * @Return java.lang.String
     **/
    String getHostSetting();

    /**
     * @Author Administrator
     * @Description 推荐值
     * @Date 10:15 2020/9/15
     * @Param []
     * @Return java.lang.Object
     **/
    Object getRecommendValue();

    /**
     * @Author Administrator
     * @Description 当前默认值
     * @Date 15:40 2020/9/15
     * @Param [context, hostName]
     * @Return java.lang.Object
     **/
    Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception;

    /**
     * @Author Administrator
     * @Description 最近实践警告级别
     * @Date 10:16 2020/9/15
     * @Param
     * @Return
     **/
    String getLevel();

    /**
     * @Author Administrator
     * @Description 是否需要重启
     * @Date 10:17 2020/9/15
     * @Param []
     * @Return boolean
     **/
    boolean needReboot();

    /**
     * @Author Administrator
     * @Description 可自动修复
     * @Date 9:44 2020/9/16
     * @Return boolean
     **/
    boolean autoRepair();

    boolean check(VCSDKUtils vcsdkUtils, String objectId) throws Exception;

    void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception;
}
