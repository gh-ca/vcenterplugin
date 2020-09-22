package com.dmeplugin.dmestore.services;

import java.util.Map;

/**
 * @ClassName HostAccessService
 * @Description TODO
 * @Author wangxiangyong
 * @Date 2020/9/4 10:18
 * @Version V1.0
 **/
public interface HostAccessService {

    /**
     * Configure the host with iSCSI, params include:
     * String hostObjectId：host object id
     * Map<String, String> vmKernel：  选择的vmkernel
     * List<EthPortInfo> ethPorts: 选择的以太网端口列表
     *
     * @param params: params include:hostObjectId,vmKernel,ethPorts
     * @return: ResponseBodyBean
     * @throws Exception when error
     */
    void configureIscsi(Map<String, Object> params) throws Exception;
}
