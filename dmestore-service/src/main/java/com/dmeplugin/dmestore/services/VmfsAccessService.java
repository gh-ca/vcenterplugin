package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.VmfsDataInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by hyuan on 2017/5/10.
 */

public interface VmfsAccessService {
    /**
     * vmfs list
     */
    List<VmfsDataInfo> listVmfs() throws Exception;

    /**
     * create vmfs
     */
    void createVmfs(Map<String, Object> params) throws Exception;

    /**
     * mount vmfs
     */
    void mountVmfs(Map<String, Object> params) throws Exception;

}
