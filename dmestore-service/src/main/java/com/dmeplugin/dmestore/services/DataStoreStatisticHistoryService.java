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
    Map<String, Object> queryVmfsStatistic(Map<String, Object> params);

    Map<String, Object> queryVmfsStatisticCurrent(Map<String, Object> params);
}
