package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.ResponseBodyBean;

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
     * @return
     */
    Map<String,Object> createNfsDatastore(Map<String, Object> params);

    /**
     * 更新NFS存储
     * @param params
     * @return
     */
    Map<String,Object> updateNfsDatastore(Map<String, Object> params);

    /**
     * NFS存储扩容和缩容
     * @param params
     * @return
     */
    ResponseBodyBean changeNfsCapacity(Map<String, Object> params);
}
