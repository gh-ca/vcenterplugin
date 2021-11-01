package com.huawei.dmestore.services.bestpractice;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.utils.VCSDKUtils;
import com.huawei.vmware.util.DatastoreVmwareMoFactory;
import com.huawei.vmware.util.HostVmwareFactory;

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
     * @return java.lang.String
     * @author wangxy
     */
    String getHostSetting();

    /**
     * 建议值
     *
     * @return java.lang.Object
     * @author wangxy
     */
    Object getRecommendValue();

    /**
     * 获取每个主机的建议值。个性化需求
     *
     * @param vcsdkUtils vCenter工具类
     * @param objectId 存储/主机ID
     * @return java.lang.Object
     * @author wangxy
     */
    default Object getRecommendValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        // 默认返回配置的值。如果需要特殊需求，单独实现这个方法。
        return getRecommendValue();
    }

    /**
     * 当前值
     *
     * @param vcsdkUtils vCenter工具类
     * @param objectId 存储/主机ID
     * @return Object
     * @throws DmeException 异常
     * @author wangxy
     */
    Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception;

    /**
     * 提示级别
     *
     * @return java.lang.String
     * @author wangxy
     */
    String getLevel();

    /**
     * 是否需要重启
     *
     * @return boolean
     * @author wangxy
     */
    boolean needReboot();

    /**
     * 是否可以自动恢复
     *
     * @return boolean
     * @author wangxy
     */
    boolean autoRepair();

    /**
     * 最佳实践检查接口
     *
     * @param vcsdkUtils vCenter工具类
     * @param objectId 存储/主机ID
     * @return boolean
     * @throws Exception 异常
     * @author wangxy
     */
    boolean check(VCSDKUtils vcsdkUtils, String objectId) throws Exception;

    /**
     * 最佳实践实施
     *
     * @param vcsdkUtils vCenter工具类
     * @param objectId 存储/主机ID
     * @throws Exception 异常
     * @author wangxy
     */
    void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception;

    /**
     * HostMOFactory
     *
     * @return HostMOFactory
     * @author wangxy
     */
    default HostVmwareFactory getHostMoFactory() {
        return HostVmwareFactory.getInstance();
    }

    /**
     * DatastoreMOFactory
     *
     * @return DatastoreMOFactory
     * @author wangxy
     */
    default DatastoreVmwareMoFactory getDatastoreMoFactory() {
        return DatastoreVmwareMoFactory.getInstance();
    }

    /**
     * 修复动作，默认为手动
     * 0：手动
     * 1：自动
     *
     * @return String
     *
     */
    default String repairAction() {
        return "0";
    }

}
