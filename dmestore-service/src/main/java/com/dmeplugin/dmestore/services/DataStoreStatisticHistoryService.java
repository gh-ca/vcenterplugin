package com.dmeplugin.dmestore.services;

import java.util.Map;

/**
 * @Description: TODO
 * @ClassName: DataSotreStatisticHistroyService
 * @Company: GH-CA
 * @author: liuxh
 * @create: 2020-09-03
 **/
public interface DataStoreStatisticHistoryService {
    String RANGE_LAST_5_MINUTE = "LAST_5_MINUTE";
    String RANGE_LAST_1_HOUR = "LAST_1_HOUR";
    String RANGE_LAST_1_DAY = "LAST_1_DAY";
    String RANGE_LAST_1_WEEK = "LAST_1_WEEK";
    String RANGE_LAST_1_MONTH = "LAST_1_MONTH";
    String RANGE_LAST_1_QUARTER = "LAST_1_QUARTER";
    String RANGE_HALF_1_YEAR = "HALF_1_YEAR";
    String RANGE_LAST_1_YEAR = "LAST_1_YEAR";
    String RANG_BEGIN_END_TIME = "BEGIN_END_TIME";

    String INTERVAL_ONE_MINUTE = "ONE_MINUTE";
    String INTERVAL_MINUTE = "MINUTE";
    String INTERVAL_HALF_HOUR = "HALF_HOUR";
    String INTERVAL_HOUR = "HOUR";
    String INTERVAL_DAY = "DAY";
    String INTERVAL_WEEK = "WEEK";
    String INTERVAL_MONTH = "MONTH";

    /**
     * 获取VMFS历史性能数据
     *
     * @param params key required: obj_ids, indicator_ids, range
     * @return
     */
    Map<String, Object> queryVmfsStatistic(Map<String, Object> params) throws Exception;

    /**
     * 获取VMFS当前性能数据值
     *
     * @param params key required: obj_ids
     * @return
     */
    Map<String, Object> queryVmfsStatisticCurrent(Map<String, Object> params) throws Exception;

    /**
     * 获取NFS历史性能数据值
     *
     * @param params key required: obj_ids, indicator_ids, range
     * @return
     */
    Map<String, Object> queryNfsStatistic(Map<String, Object> params) throws Exception;

    /**
     * 获取NFS当前性能数据
     *
     * @param params key required: obj_ids
     * @return
     */
    Map<String, Object> queryNfsStatisticCurrent(Map<String, Object> params) throws Exception;

    /**
     * 查询磁盘(卷)历史性能数据
     *
     * @param params key required: obj_ids, indicator_ids, range
     * @return
     */
    Map<String, Object> queryVolumeStatistic(Map<String, Object> params) throws Exception;

    /**
     * 查询fileSystem历史性能数据(Nfsdatasotrage)
     *
     * @param params key required: obj_ids, indicator_ids, range
     * @return
     */
    Map<String, Object> queryFsStatistic(Map<String, Object> params) throws Exception;

    /**
     * 查询服务等级历史性能数据
     *
     * @param params key required: obj_ids, indicator_ids, range
     * @return
     * @throws Exception
     */
    Map<String, Object> queryServiceLevelStatistic(Map<String, Object> params) throws Exception;

    /**
     * 查询服务等级卷历史性能数据
     *
     * @param params key required: obj_ids, indicator_ids, range
     * @return
     * @throws Exception
     */
    Map<String, Object> queryServiceLevelLunStatistic(Map<String, Object> params) throws Exception;

    /**
     * 查询服务等级存储池历史性能数据
     *
     * @param params key required: obj_ids, indicator_ids, range
     * @return
     * @throws Exception
     */
    Map<String, Object> queryServiceLevelStoragePoolStatistic(Map<String, Object> params) throws Exception;

    /**
     * 查询存储池历史性能数据
     *
     * @param params key required: obj_ids, indicator_ids, range
     * @return
     * @throws Exception
     */
    Map<String, Object> queryStoragePoolStatistic(Map<String, Object> params) throws Exception;

    /**
     * 查询存储池当前性能数据
     *
     * @param params key required: obj_ids, indicator_ids, range
     * @return
     * @throws Exception
     */
    Map<String, Object> queryStoragePoolCurrentStatistic(Map<String, Object> params) throws Exception;

    /**
     * 查询存储设备历史性能数据
     *
     * @param params key required: obj_ids, indicator_ids, range
     * @return
     * @throws Exception
     */
    Map<String, Object> queryStorageDevcieStatistic(Map<String, Object> params) throws Exception;

    /**
     * 查询存储设备当前性能数据
     *
     * @param params key required: obj_ids, indicator_ids, range
     * @return
     * @throws Exception
     */
    Map<String, Object> queryStorageDevcieCurrentStatistic(Map<String, Object> params) throws Exception;

    /**
     * 查询指定资源类型的历史性能数据
     *
     * @param resType 实例类型;  params key required: obj_ids, indicator_ids, range
     * @return
     * @throws Exception
     */
    Map<String, Object> queryHistoryStatistic(String resType, Map<String, Object> params) throws Exception;

    /**
     * 查询指定资源类型的当前性能数据
     *
     * @param resType 实例类型;  params key required: obj_ids, indicator_ids, range
     * @return
     * @throws Exception
     */
    Map<String, Object> queryCurrentStatistic(String resType, Map<String, Object> params) throws Exception;
}
