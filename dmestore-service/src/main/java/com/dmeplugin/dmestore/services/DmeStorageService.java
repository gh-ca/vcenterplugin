package com.dmeplugin.dmestore.services;


import com.dmeplugin.dmestore.model.EthPortInfo;

import java.util.List;
import java.util.Map;

/**
 * @author lianq
 * @className DmeStorageService
 * @description TODO
 * @date 2020/9/3 17:48
 */
public interface DmeStorageService {

    /**
     * 存储设备列表
     * @return
     */
    Map<String, Object> getStorages();
    /**
     * 获取存储详情
     * @param storageId
     * @return
     */
    Map<String,Object> getStorageDetail(String storageId);

    /**
     * 获取存储池列表
     * @param storageId 存储设备id
     * @param media_type 存储池类型 file block all(默认)
     * @return
     */
    Map<String, Object> getStoragePools(String storageId ,String media_type);

    Map<String, Object> getLogicPorts(String storageId);

    Map<String, Object> getVolumes(String storageId);

    Map<String, Object> getFileSystems(String storageId);

    Map<String, Object> getDTrees(String storageId);

    Map<String, Object> getNfsShares(String storageId);

    Map<String, Object> getBandPorts(String storageId);

    Map<String, Object> getStorageControllers();

    Map<String, Object> getStorageDisks();

    List<EthPortInfo> getStorageEthPorts(String storageSn) throws Exception;

    Map<String, Object> getVolume(String volumeId);

    Map<String,Object> getStoragePort(String storageDeviceId,String portType);

    Map<String,Object> getFailoverGroups(String storage_id);
}
