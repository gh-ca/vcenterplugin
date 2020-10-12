package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.VmRdmCreateBean;
import com.vmware.vim25.DatastoreSummary;

import java.util.List;
import java.util.Map;

/**
 * @Author admin
 * @version 1.0.0
 * @ClassName VMRDMService.java
 * @Description TODO
 * @createTime 2020年09月09日 10:58:00
 */
public interface VmRdmService {
    /**
     * @Author Administrator
     * @Description vCenter虚拟机创建Rdm
     * @Date 10:29 2020/10/12
     * @Param [dataStoreName, vmObjectId, hostId, createBean]
     * @Return void
     **/
    void createRdm(String dataStoreName, String vmObjectId, String hostId, VmRdmCreateBean createBean) throws Exception;

    /**
     * @Author Administrator
     * @Description 获取所有DME主机
     * @Date 10:32 2020/10/12
     * @Param []
     * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    List<Map<String, Object>> getAllDmeHost() throws Exception;

    /**
     * @Author Administrator
     * @Description 获取vCenter主机已关联的VMFS存储
     * @Date 10:36 2020/10/12
     * @Param [hostId]
     * @Return java.util.List<com.vmware.vim25.DatastoreSummary>
     **/
    List<DatastoreSummary> getDatastoreMountsOnHost(String hostId) throws Exception;
}
