package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.VmRDMCreateBean;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName VMRDMService.java
 * @Description TODO
 * @createTime 2020年09月09日 10:58:00
 */
public interface VMRDMService {
    void createRDM(String datastore_objectId, String vm_objectId, String host_objectId, VmRDMCreateBean createBean) throws Exception;
}
