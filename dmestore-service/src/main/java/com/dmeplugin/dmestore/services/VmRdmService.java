package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.VmRdmCreateBean;
import com.vmware.vim25.DatastoreSummary;

import java.util.List;
import java.util.Map;

/**
 * @Author admin
 * @version 1.0.0
 * @ClassName VMRDMService.java
 * @Description RDM创建相关接口
 * @createTime 2020年09月09日 10:58:00
 */
public interface VmRdmService {

    /**
     * @author wangxy
     * @description vCenter 虚拟机RDM创建
     * @date 17:37 2020/10/13
     * @param dataStoreName vCenter存储名称
     * @param vmObjectId    vCenter虚拟机ID
     * @param hostId        DME主机ID
     * @param createBean    DME卷创建基础信息
     * @return void
     **/
    void createRdm(String dataStoreName, String vmObjectId, String hostId, VmRdmCreateBean createBean) throws Exception;

    /**
     * @author wangxy
     * @description 获取DME主机信息
     * @date 17:43 2020/10/13
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    List<Map<String, Object>> getAllDmeHost() throws Exception;

    /**
     * @author Administrator
     * @description 根据主机绑定的存储查询
     * @date 17:36 2020/10/13
     * @param hostId DME主机ID
     * @return java.util.List<com.vmware.vim25.DatastoreSummary>
     **/
    List<DatastoreSummary> getDatastoreMountsOnHost(String hostId) throws Exception;
}
