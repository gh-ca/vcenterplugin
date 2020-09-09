package com.dmeplugin.dmestore.services;

import java.util.Map;

/**
 * @author lianq
 * @className VmfsOperationService
 * @description TODO
 * @date 2020/9/9 10:21
 */
public interface VmfsOperationService {

    Map<String, Object> updateVMFS(String volume_id, Map<String, Object> params);
}
