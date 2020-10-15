package com.dmeplugin.dmestore.services;


import com.dmeplugin.dmestore.model.EthPortInfo;
import com.dmeplugin.dmestore.model.Storage;

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

    /**
     * 获取逻辑端口列表
     * @param storageId
     * @return
     */
    Map<String, Object> getLogicPorts(String storageId);

    /**
     * 获取卷列表
     * @param storageId
     * @return
     */
    Map<String, Object> getVolumes(String storageId);

    /**
     * 获取文件系统列表
     * @param storageId
     * @return
     */
    Map<String, Object> getFileSystems(String storageId);

    /**
     * 获取Dtree列表
     * @param storageId
     * @return
     */
    Map<String, Object> getDTrees(String storageId);

    /**
     * 获取share列表
     * @param storageId
     * @return
     */
    Map<String, Object> getNfsShares(String storageId);

    /**
     * 获取绑定端口列表
     * @param storageId
     * @return
     */
    Map<String, Object> getBandPorts(String storageId);

    /**
     * 获取控制器列表
     * @param storageDeviceId
     * @return
     */
    Map<String, Object> getStorageControllers(String storageDeviceId);

    /**
     * 获取存储硬盘列表
     * @param storageDeviceId
     * @return
     */
    Map<String, Object> getStorageDisks(String storageDeviceId);

    /**
     * 获取StorageEthPorts列表
     * @param storageSn
     * @return
     * @throws Exception
     */
    List<EthPortInfo> getStorageEthPorts(String storageSn) throws Exception;

    /**
     * 获取指定卷
     * @param volumeId
     * @return
     */
    Map<String, Object> getVolume(String volumeId);

    /**
     * 获取存储端口列表
     * @param storageDeviceId
     * @param portType
     * @return
     */
    Map<String,Object> getStoragePort(String storageDeviceId,String portType);

    /**
     * 获取漂移组列表
     * @param storage_id
     * @return
     */
    Map<String,Object> getFailoverGroups(String storage_id);

    /**
     * Access storage performance
     *
     * @param storageIds storage id
     * @return: ResponseBodyBean
     */
    List<Storage> listStoragePerformance(List<String> storageIds) throws Exception;
}
