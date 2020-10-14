package com.dmeplugin.dmestore.services;

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
     * @param volume_id
     * @param params
     * @return
     */
    Map<String, Object> updateVMFS(String volume_id, Map<String, Object> params);

    /**
     * vmfs存储扩容
     * @param volumes
     * @return
     */
    Map<String,Object> expandVMFS(List<Map<String,String>> volumes);

    /**
     * vmfs存储空间回收
     * @param vmfsUuids
     * @return
     */
    Map<String,Object> recycleVmfsCapacity(List<String> vmfsUuids);

    /**
     * 更新vmfs存储
     * @param params
     * @return
     */
    Map<String,Object> updateVmfsServiceLevel(Map<String, Object> params);

    /**
     * 获取服务等级列表
     * @param params
     * @return
     */
    Map<String,Object> listServiceLevelVMFS(Map<String, Object> params);
}
