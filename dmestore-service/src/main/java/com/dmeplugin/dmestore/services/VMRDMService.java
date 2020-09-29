package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.DMEHostInfo;
import com.dmeplugin.dmestore.model.VmRDMCreateBean;

import java.util.List;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName VMRDMService.java
 * @Description TODO
 * @createTime 2020年09月09日 10:58:00
 */
public interface VMRDMService {
    void createRDM(String vm_objectId, String host_objectId, VmRDMCreateBean createBean) throws Exception;

    List<DMEHostInfo> getAllDmeHost() throws Exception;
}
