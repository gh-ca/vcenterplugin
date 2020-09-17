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
    String RANG_LAST_5_MINUTE = "LAST_5_MINUTE";
    String RANG_LAST_1_HOUR = "LAST_1_HOUR";
    String RANG_LAST_1_DAY = "LAST_1_DAY";
    String RANG_LAST_1_WEEK = "LAST_1_WEEK";
    String RANG_LAST_1_MONTH = "LAST_1_MONTH";
    String RANG_LAST_1_QUARTER = "LAST_1_QUARTER";
    String RANG_HALF_1_YEAR = "HALF_1_YEAR";
    String RANG_LAST_1_YEAR = "LAST_1_YEAR";
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
     * @param params
     * @return
     */
    Map<String, Object> queryVmfsStatistic(Map<String, Object> params);

    /**
     * 获取VMFS当前性能数据值
     *
     * @param params
     * @return
     */
    Map<String, Object> queryVmfsStatisticCurrent(Map<String, Object> params);

    /**
     * 获取NFS历史性能数据值
     *
     * @param params
     * @return
     */
    Map<String, Object> queryNfsStatistic(Map<String, Object> params);

    /**
     * 获取NFS当前性能数据值
     *
     * @param params
     * @return
     */
    Map<String, Object> queryNfsStatisticCurrent(Map<String, Object> params);
}
