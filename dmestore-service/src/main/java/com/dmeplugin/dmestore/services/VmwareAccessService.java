package com.dmeplugin.dmestore.services;

import java.util.List;
import java.util.Map;


/**
 * @Description: TODO
 * @ClassName: VmwareAccessService
 * @Company: GH-CA
 * @author: yy
 * @create: 2020-09-02
 **/
public interface VmwareAccessService {
    /**
     * vmware host list
     */
    List<Map<String,String>> listHosts() throws Exception;

    /**
     * vmware host list
     */
    List<Map<String,String>> getHostsByDsName(String dataStoreName) throws Exception;

    /**
     * vmware Cluster list
     */
    List<Map<String,String>> listClusters() throws Exception;

    /**
     * vmware Cluster list
     */
    List<Map<String,String>> getClustersByDsName(String dataStoreName) throws Exception;

    /**
     * vmware dataStore list
     */
    List<Map<String,String>> getDataStoresByHostName(String hostName, String dataStoreType) throws Exception;

    /**
     * vmware dataStore list
     */
    List<Map<String,String>> getDataStoresByClusterName(String clusterName, String dataStoreType) throws Exception;



}
