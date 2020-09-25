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

    Map<String,Object> createNfsDatastore(Map<String, Object> params);

    Map<String,Object> updateNfsDatastore(Map<String, Object> params);

    ResponseBodyBean changeNfsCapacity(Map<String, Object> params);
}
