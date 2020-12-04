package com.dmeplugin.dmestore.services.bestpractice;

import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.dmeplugin.vmware.util.DatastoreMOFactory;
import com.dmeplugin.vmware.util.HostMOFactory;
import com.dmeplugin.vmware.util.VmwareContext;

/**
 * @author admin
 */
public interface BestPracticeService {

    /**
     * 检查项名称
     *
     * @param
     * @return java.lang.String
     * @throws
     * @author wangxy
     * @date 9:56 2020/10/27
     **/
    String getHostSetting();

    /**
     * 推荐值
     *
     * @param
     * @return java.lang.Object
     * @throws
     * @author wangxy
     * @date 9:57 2020/10/27
     **/
    Object getRecommendValue();

    /**
     * 当前值
     *
     * @param vcsdkUtils
     * @param objectId
     * @return java.lang.Object
     * @throws Exception
     * @author wangxy
     * @date 9:58 2020/10/27
     **/
    Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception;

    /**
     * 提示级别
     *
     * @param
     * @return java.lang.String
     * @throws
     * @author wangxy
     * @date 9:58 2020/10/27
     **/
    String getLevel();

    /**
     * 是否需要重启
     *
     * @param
     * @return boolean
     * @throws
     * @author wangxy
     * @date 9:58 2020/10/27
     **/
    boolean needReboot();

    /**
     * 是否可以自动修复
     *
     * @param
     * @return boolean
     * @throws
     * @author wangxy
     * @date 9:59 2020/10/27
     **/
    boolean autoRepair();

    /**
     * 最佳实践检查
     *
     * @param vcsdkUtils
     * @param objectId
     * @return boolean
     * @throws Exception
     * @author wangxy
     * @date 9:59 2020/10/27
     **/
    boolean check(VCSDKUtils vcsdkUtils, String objectId) throws Exception;

    /**
     * 最佳实践实施
     *
     * @param vcsdkUtils
     * @param objectId
     * @throws Exception void
     * @author wangxy
     * @date 9:59 2020/10/27
     **/
    void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception;

    /**
     * getHostMoFactory
     *
     * @author wangxy
     * @date 9:59 2020/10/27
     **/
    default HostMOFactory getHostMoFactory() {
        return null;
    }

    /**
     * getDatastoreMoFactory
     *
     * @author wangxy
     * @date 9:59 2020/10/27
     **/
    default DatastoreMOFactory getDatastoreMoFactory() {
        return null;
    }
}
