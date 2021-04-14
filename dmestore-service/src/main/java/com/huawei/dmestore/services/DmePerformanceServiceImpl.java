package com.huawei.dmestore.services;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.utils.VCSDKUtils;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;

public class DmePerformanceServiceImpl implements DmePerformanceService {

    private VCSDKUtils vcsdkUtils;
    @Override
    public String getPerformanceDataStore(String storageId) throws DmeException {
        return vcsdkUtils.queryPerf(storageId);
    }

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
    }
}