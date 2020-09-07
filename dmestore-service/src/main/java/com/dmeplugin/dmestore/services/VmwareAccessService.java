package com.dmeplugin.dmestore.services;

import java.util.List;
import java.util.Map;


/**
 * Created by hyuan on 2017/5/10.
 */

public interface VmwareAccessService {
    /**
     * vmware host list
     */
    List<Map<String,String>> listHosts() throws Exception;

    /**
     * vmware host list
     */
    List<Map<String,String>> listClusters() throws Exception;


}
