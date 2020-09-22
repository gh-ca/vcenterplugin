package com.dmeplugin.dmestore.services;

import java.util.Map;

/**
 * @author lianq
 * @className DmeStorageService
 * @description TODO
 * @date 2020/9/3 17:48
 */
public interface DmeStorageService {

    Map<String, Object> getStorages();

    Map<String,Object> getStorageDetail(String storageId);

    Map<String, Object> getStoragePools(String storageId);

    Map<String, Object> getVolume(String volumeId);
}
