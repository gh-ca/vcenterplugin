package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DmeException;

import java.util.Map;

/**
 * @author lianq
 * @className NfsOperationService
 * @description TODO
 * @date 2020/9/16 16:25
 */
public interface NfsOperationService {

    /**
     * 创建nfs存储
     * @param params
     */
    void createNfsDatastore(Map<String, Object> params) throws DmeException;

    /**
     * 更新NFS存储
     * @param params
     * @throws DmeException
     */
    void updateNfsDatastore(Map<String, Object> params) throws DmeException;

    /**
     * NFS存储扩容和缩容
     * @param params
     */
    void changeNfsCapacity(Map<String, Object> params) throws DmeException;

    /**
     *
     * @param storeObjectId
     * @return
     * @throws DmeException
     */
    Map<String,Object> getEditNfsStore(String storeObjectId) throws DmeException;
}
