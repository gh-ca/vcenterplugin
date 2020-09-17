package com.dmeplugin.dmestore.services;

import java.util.Map;

/**
 * @author lianq
 * @className NfsOperationService
 * @description TODO
 * @date 2020/9/16 16:25
 */
public interface NfsOperationService {

    Map<String,Object> createNfsDatastore(Map<String, String> params);
}
