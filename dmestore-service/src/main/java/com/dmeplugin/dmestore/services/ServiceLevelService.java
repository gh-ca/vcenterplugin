package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DmeException;
import com.dmeplugin.dmestore.model.SimpleServiceLevel;
import com.dmeplugin.dmestore.model.StoragePool;
import com.dmeplugin.dmestore.model.Volume;

import java.util.List;
import java.util.Map;

/**
 * ServiceLevelService
 *
 * @author liuxh
 * @since 2020-09-15
 **/
public interface ServiceLevelService {
    List<SimpleServiceLevel> listServiceLevel(Map<String, Object> params) throws DmeException;

    List<StoragePool> getStoragePoolInfosByServiceLevelId(String serviceLevelId) throws DmeException;

    List<Volume> getVolumeInfosByServiceLevelId(String serviceLevelId) throws DmeException;

    void updateVmwarePolicy() throws DmeException;
}
