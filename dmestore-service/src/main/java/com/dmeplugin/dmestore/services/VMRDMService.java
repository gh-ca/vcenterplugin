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
    void createRDM(String datastore_name, String vm_name, String host_id, VmRDMCreateBean createBean) throws Exception;
}
