package com.dmeplugin.dmestore.services;

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
    Map<String, Object> listServiceLevel(Map<String, Object> params);


     //服务等级关联的storagePool
    List<StoragePool> getStoragePoolInfosByServiceLevelId(String serviceLevelId) throws Exception;

    List<Volume> getVolumeInfosByServiceLevelId(String serviceLevelId) throws Exception;
    
    void updateVmwarePolicy();
}
