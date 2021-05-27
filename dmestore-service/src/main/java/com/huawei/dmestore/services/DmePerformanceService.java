package com.huawei.dmestore.services;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.huawei.dmestore.exception.DmeException;

import java.util.Map;

/**
 * vmware性能接口
 */
public interface DmePerformanceService {
    /**
     * 获取存储的使用情况 countId 268，disk.used.latest	KB
     * @param storageId
     * @return
     * @throws DmeException
     */
    String getPerformanceDataStore(String storageId) throws DmeException;

    Map<String, Object> getPerformance(Map<String, Object> params) throws  DmeException;

    /**
     * 获取vmware指定对象指定指标的当前性能数据
     * @param params obj_ids，indicator_ids
     * @return
     * @throws DmeException
     */
    Map<String, Object> getPerformanceCurrent(Map<String, Object> params) throws DmeException;
}