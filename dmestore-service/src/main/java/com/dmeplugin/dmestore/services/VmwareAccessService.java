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
     * Access hosts
     *
     * @return ResponseBodyBean List<Map<String, String>> include hostId hostName
     * @throws Exception when error
     */
    List<Map<String,String>> listHosts() throws Exception;

    /**
     * Access hosts
     *
     * @param  dataStoreName dataStore Name
     * @return ResponseBodyBean List<Map<String, String>> include hostId hostName
     * @throws Exception when error
     */
    List<Map<String,String>> getHostsByDsName(String dataStoreName) throws Exception;

    /**
     * Access clusters
     *
     * @return ResponseBodyBean List<Map<String, String>> include clusterId clusterName
     * @throws Exception when error
     */
    List<Map<String,String>> listClusters() throws Exception;

    /**
     * Access clusters
     *
     * @param  dataStoreName dataStore Name
     * @return ResponseBodyBean List<Map<String, String>> include clusterId clusterName
     * @throws Exception when error
     */
    List<Map<String,String>> getClustersByDsName(String dataStoreName) throws Exception;

    /**
     * Access datastore
     *
     * @param  hostName host Name
     * @param  dataStoreType dataStore Type
     * @return ResponseBodyBean List<Map<String, String>> include id name status type capacity freeSpace
     * @throws Exception when error
     */
    List<Map<String,String>> getDataStoresByHostName(String hostName, String dataStoreType) throws Exception;

    /**
     * Access datastore
     *
     * @param  clusterName cluster Name
     * @param  dataStoreType dataStore Type
     * @return ResponseBodyBean List<Map<String, String>> include id name status type capacity freeSpace
     * @throws Exception when error
     */
    List<Map<String,String>> getDataStoresByClusterName(String clusterName, String dataStoreType) throws Exception;



}
