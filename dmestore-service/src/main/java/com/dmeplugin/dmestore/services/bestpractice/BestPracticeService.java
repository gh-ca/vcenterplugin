package com.dmeplugin.dmestore.services.bestpractice;

import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.dmeplugin.vmware.util.HostMOFactory;
import com.dmeplugin.vmware.util.VmwareContext;

/**
 * @author admin
 */
public interface BestPracticeService {

    /**
     * 检查项名称
     * @author wangxy
     * @date 9:56 2020/10/27
     * @param
     * @throws
     * @return java.lang.String
     **/
    String getHostSetting();

    /**
     * 推荐值
     * @author wangxy
     * @date 9:57 2020/10/27
     * @param
     * @throws
     * @return java.lang.Object
     **/
    Object getRecommendValue();

    /**
     * 当前值
     * @author wangxy
     * @date 9:58 2020/10/27
     * @param vcsdkUtils
     * @param objectId
     * @throws Exception
     * @return java.lang.Object
     **/
    Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception;

    /**
     * 提示级别
     * @author wangxy
     * @date 9:58 2020/10/27
     * @param
     * @throws
     * @return java.lang.String
     **/
    String getLevel();

    /**
     * 是否需要重启
     * @author wangxy
     * @date 9:58 2020/10/27
     * @param
     * @throws
     * @return boolean
     **/
    boolean needReboot();

    /**
     * 是否可以自动修复
     * @author wangxy
     * @date 9:59 2020/10/27
     * @param
     * @throws
     * @return boolean
     **/
    boolean autoRepair();

    /**
     * 最佳实践检查
     * @author wangxy
     * @date 9:59 2020/10/27
     * @param vcsdkUtils
     * @param objectId
     * @throws Exception
     * @return boolean
     **/
    boolean check(VCSDKUtils vcsdkUtils, String objectId) throws Exception;

    /**
     * 最佳实践实施
     * @author wangxy
     * @date 9:59 2020/10/27
     * @param vcsdkUtils
     * @param objectId
     * @throws Exception
     * @return void
     **/
    void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception;

    /**
     * 最佳实践实施
     * @author wangxy
     * @date 9:59 2020/10/27
     * @throws Exception
     * @return void
     **/
    default HostMOFactory getHostMoFactory() throws Exception{
        return HostMOFactory.getInstance();
    }
}
