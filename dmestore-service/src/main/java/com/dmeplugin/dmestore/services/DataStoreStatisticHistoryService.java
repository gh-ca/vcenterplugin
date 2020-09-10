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
    /**
     * 获取VMFS历史性能数据
     * @param params
     * @return
     */
    Map<String, Object> queryVmfsStatistic(Map<String, Object> params);

    /**
     * 获取VMFS当前性能数据值
     * @param params
     * @return
     */
    Map<String, Object> queryVmfsStatisticCurrent(Map<String, Object> params);

    /**
     * 获取NFS历史性能数据值
     * @param params
     * @return
     */
    Map<String, Object> queryNfsStatistic(Map<String, Object> params);

    /**
     * 获取NFS当前性能数据值
     * @param params
     * @return
     */
    Map<String, Object> queryNfsStatisticCurrent(Map<String, Object> params);
}
