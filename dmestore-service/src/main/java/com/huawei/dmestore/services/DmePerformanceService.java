package com.huawei.dmestore.services;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.huawei.dmestore.exception.DmeException;

import java.util.Map;

public interface DmePerformanceService {

    String getPerformanceDataStore(String storageId) throws DmeException;

    Map<String, Object> getPerformance(Map<String, Object> params) throws  DmeException;

    Map<String, Object> getPerformanceCurrent(Map<String, Object> params) throws DmeException;
}