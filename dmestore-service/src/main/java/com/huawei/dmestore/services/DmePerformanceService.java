package com.huawei.dmestore.services;

import com.huawei.dmestore.exception.DmeException;

public interface DmePerformanceService {

    String getPerformanceDataStore(String storageId) throws DmeException;
}