package com.dmeplugin.dmestore.services.bestpractice;

import com.dmeplugin.dmestore.exception.DmeException;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.dmeplugin.vmware.util.DatastoreMOFactory;
import com.dmeplugin.vmware.util.HostMOFactory;

/**
 * BestPracticeService 最佳实践实现接口类
 *
 * @author wangxiangyong
 * @since 2020-11-30
 **/
public interface BestPracticeService {
    /**
     * 检查项名称
     *
     * @author wangxy
     * @return java.lang.String
     */
    String getHostSetting();

    /**
     * 建议值
     *
     * @author wangxy
     * @return java.lang.Object
     */
    Object getRecommendValue();

    /**
     * 当前值
     *
     * @author wangxy
     * @param vcsdkUtils vCenter工具类
     * @param objectId 存储/主机ID
     * @return Object
     * @throws DmeException 异常
     */
    Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception;

    /**
     * 提示级别
     *
     * @author wangxy
     * @return java.lang.String
     */
    String getLevel();

    /**
     * 是否需要重启
     *
     * @author wangxy
     * @return boolean
     */
    boolean needReboot();

    /**
     * 是否可以自动恢复
     *
     * @author wangxy
     * @return boolean
     */
    boolean autoRepair();

    /**
     * 最佳实践检查接口
     *
     * @author wangxy
     * @param vcsdkUtils vCenter工具类
     * @param objectId 存储/主机ID
     * @return boolean
     * @throws Exception 异常
     */
    boolean check(VCSDKUtils vcsdkUtils, String objectId) throws Exception;

    /**
     * 最佳实践实施
     *
     * @author wangxy
     * @param vcsdkUtils vCenter工具类
     * @param objectId 存储/主机ID
     * @throws Exception 异常
     */
    void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception;

    /**
     * HostMOFactory
     *
     * @author wangxy
     * @return HostMOFactory
     */
    default HostMOFactory getHostMoFactory() {
        return HostMOFactory.getInstance();
    }

    /**
     * DatastoreMOFactory
     *
     * @author wangxy
     * @return DatastoreMOFactory
     */
    default DatastoreMOFactory getDatastoreMoFactory() {
        return DatastoreMOFactory.getInstance();
    }
}
