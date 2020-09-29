package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.EthPortInfo;

import java.util.List;
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
     * List<Map<String, Object>> ethPorts: 选择的以太网端口列表
     *
     * @param params: params include:hostObjectId,vmKernel,ethPorts
     * @return: ResponseBodyBean
     * @throws Exception when error
     */
    void configureIscsi(Map<String, Object> params) throws Exception;

    /**
     * Test Connectivity:
     * String hostObjectId：host object id
     * List<Map<String, Object>> ethPorts: 要测试的以太网端口列表
     * Map<String, String> vmKernel: 虚拟网卡信息
     *
     * @param params: params include:hostObjectId,ethPorts，vmKernel
     * @return: ResponseBodyBean
     * @throws Exception when error
     */
    List<EthPortInfo> testConnectivity(Map<String, Object> params) throws Exception;
}
