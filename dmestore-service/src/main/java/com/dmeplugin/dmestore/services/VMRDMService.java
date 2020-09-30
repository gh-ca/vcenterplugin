package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.VmRDMCreateBean;
import com.vmware.vim25.DatastoreSummary;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName VMRDMService.java
 * @Description TODO
 * @createTime 2020年09月09日 10:58:00
 */
public interface VMRDMService {
    void createRDM(String data_store_name, String vm_objectId, String host_id, VmRDMCreateBean createBean) throws Exception;

    List<Map<String, Object>> getAllDmeHost() throws Exception;

    List<DatastoreSummary> getDatastoreMountsOnHost(String host_id) throws Exception;
}
