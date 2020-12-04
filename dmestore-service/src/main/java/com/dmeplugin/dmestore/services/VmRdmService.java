package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.model.VmRdmCreateBean;

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
     * @param dataStoreObjectId vCenter存储名称对象ID
     * @param vmObjectId    vCenter虚拟机ID
     * @param createBean    DME卷创建基础信息
     * @return void
     **/
    void createRdm(String dataStoreObjectId, String vmObjectId, VmRdmCreateBean createBean) throws DMEException;

    /**
     * @author wangxy
     * @description 获取DME主机信息
     * @date 17:43 2020/10/13
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    List<Map<String, Object>> getAllDmeHost() throws DMEException;

    /**
     * 获取虚拟机对应主机的存储信息
     * @author Administrator
     * @description 根据主机绑定的存储查询
     * @date 17:36 2020/10/13
     * @param vmObjectId vCenter虚拟机objectId
     * @return java.util.List<Object>
     * @throw DMEException
     **/
    List<Object> getDatastoreMountsOnHost(String vmObjectId) throws DMEException;
}
