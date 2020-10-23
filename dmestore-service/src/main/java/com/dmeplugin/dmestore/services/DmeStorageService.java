package com.dmeplugin.dmestore.services;


import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.model.*;

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
    List<Storage> getStorages() throws DMEException;
    /**
     * 获取存储详情
     * @param storageId
     * @return
     */
    StorageDetail getStorageDetail(String storageId) throws DMEException;

    /**
     * 获取存储池列表
     * @param storageId 存储设备id
     * @param media_type 存储池类型 file block all(默认)
     * @return
     */
    List<StoragePool> getStoragePools(String storageId , String media_type) throws DMEException;

    /**
     * 获取逻辑端口列表
     * @param storageId
     * @return
     */
    List<LogicPorts> getLogicPorts(String storageId) throws DMEException;

    /**
     * 获取卷列表
     * @param storageId
     * @return
     */
    List<Volume> getVolumes(String storageId) throws DMEException;

    /**
     * 获取文件系统列表
     * @param storageId
     * @return
     */
    List<FileSystem> getFileSystems(String storageId) throws DMEException;

    /**
     * 获取Dtree列表
     * @param storageId
     * @return
     */
    List<Dtrees> getDTrees(String storageId) throws DMEException;

    /**
     * 获取share列表
     * @param storageId
     * @return
     */
    List<NfsShares> getNfsShares(String storageId) throws DMEException;

    /**
     * 获取绑定端口列表
     * @param storageId
     * @return
     */
    List<BandPorts> getBandPorts(String storageId) throws DMEException;

    /**
     * 获取控制器列表
     * @param storageDeviceId
     * @return
     */
    List<StorageControllers> getStorageControllers(String storageDeviceId) throws DMEException;

    /**
     * 获取存储硬盘列表
     * @param storageDeviceId
     * @return
     */
    List<StorageDisk> getStorageDisks(String storageDeviceId) throws DMEException;

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
    List<StoragePort> getStoragePort(String storageDeviceId, String portType) throws DMEException;

    /**
     * 获取漂移组列表
     * @param storage_id
     * @return
     */
    List<FailoverGroup> getFailoverGroups(String storage_id) throws DMEException;

    FileSystemDetail getFileSystemDetail(String file_system_id) throws DMEException;
    /**
     * Access storage performance
     *
     * @param storageIds storage id
     * @return: ResponseBodyBean
     */
    List<Storage> listStoragePerformance(List<String> storageIds) throws DMEException;

    /**
     * Access storage pool performance
     *
     * @param storagePoolIds storage pool res Id
     * @return: ResponseBodyBean
     */
    List<StoragePool> listStoragePoolPerformance(List<String> storagePoolIds) throws DMEException;

    /**
     *
     * @param storageControllerIds  controllerid
     * @return
     * @throws DMEException
     */
    public List<StorageControllers> listStorageControllerPerformance(List<String> storageControllerIds) throws DMEException;

    /**
     *
     * @param storageDiskIds  diskid
     * @return
     * @throws DMEException
     */
    public List<StorageDisk> listStorageDiskPerformance(List<String> storageDiskIds) throws DMEException;

    /**
     *
     * @param storagePortIds portid
     * @return
     * @throws DMEException
     */
    public List<StoragePort> listStoragePortPerformance(List<String> storagePortIds) throws DMEException;

    /**
     *
     * @param volumeIds volumeid
     * @return
     * @throws DMEException
     */
    public List<Volume> listVolumesPerformance(List<String> volumeId) throws DMEException;
}
