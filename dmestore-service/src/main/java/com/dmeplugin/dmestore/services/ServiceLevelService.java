package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DmeException;
import com.dmeplugin.dmestore.model.SimpleServiceLevel;
import com.dmeplugin.dmestore.model.StoragePool;
import com.dmeplugin.dmestore.model.Volume;

import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @ClassName: ServiceLevelService
 * @Company: GH-CA
 * @author: liuxh
 * @create: 2020-09-02
 **/
public interface ServiceLevelService {
 //服务等级列表
    List<SimpleServiceLevel> listServiceLevel(Map<String, Object> params) throws DmeException;


     //服务等级关联的storagePool
    List<StoragePool> getStoragePoolInfosByServiceLevelId(String serviceLevelId) throws DmeException;

    List<Volume> getVolumeInfosByServiceLevelId(String serviceLevelId) throws DmeException;

    void updateVmwarePolicy() throws DmeException;
}
