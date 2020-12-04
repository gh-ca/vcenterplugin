package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.model.SimpleServiceLevel;

import java.util.List;
import java.util.Map;

/**
 * @author lianq
 * @className VmfsOperationService
 * @description TODO
 * @date 2020/9/9 10:21
 */
public interface VmfsOperationService {

    /**
     * 更新VMFS存储
     * @param volumeId
     * @param params
     */
    void updateVmfs(String volumeId, Map<String, Object> params) throws DMEException;

    /**
     * vmfs存储扩容
     * @param volumes
     */
    void expandVmfs(Map<String,String> volumes) throws DMEException;

    /**
     * vmfs存储空间回收
     * @param vmfsUuids
     */
    void recycleVmfsCapacity(List<String> vmfsUuids) throws DMEException;

    /**
     * vmfs存储空间回收
     * @param vmfsObjIds
     */
    void recycleVmfsCapacityByDataStoreIds(List<String> vmfsObjIds) throws DMEException;

    /**
     * 更新vmfs存储
     * @param params
     */
    void updateVmfsServiceLevel(Map<String, Object> params) throws DMEException;

    /**
     * 获取服务等级列表
     * @param params
     * @return
     */
    List<SimpleServiceLevel> listServiceLevelVmfs(Map<String, Object> params) throws DMEException;
}
