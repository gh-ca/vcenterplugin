package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DMEException;

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
    List<Map<String,String>> listHosts() throws DMEException;

    /**
     * Access hosts
     *
     * @param  dataStoreObjectId dataStore ObjectId
     * @return ResponseBodyBean List<Map<String, String>> include hostId hostName
     * @throws Exception when error
     */
    List<Map<String,String>> getHostsByDsObjectId(String dataStoreObjectId) throws DMEException;

    /**
     * Access clusters
     *
     * @return ResponseBodyBean List<Map<String, String>> include clusterId clusterName
     * @throws Exception when error
     */
    List<Map<String,String>> listClusters() throws DMEException;

    /**
     * Access clusters
     *
     * @param  dataStoreObjectId dataStore ObjectId
     * @return ResponseBodyBean List<Map<String, String>> include clusterId clusterObjectId
     * @throws Exception when error
     */
    List<Map<String,String>> getClustersByDsObjectId(String dataStoreObjectId) throws DMEException;

    /**
     * Access datastore
     *
     * @param  hostObjectId host ObjectId
     * @param  dataStoreType dataStore Type
     * @return ResponseBodyBean List<Map<String, String>> include id name status type capacity freeSpace
     * @throws Exception when error
     */
    List<Map<String,String>> getDataStoresByHostObjectId(String hostObjectId, String dataStoreType) throws DMEException;

    /**
     * Access datastore
     *
     * @param  clusterObjectId cluster ObjectId
     * @param  dataStoreType dataStore Type
     * @return ResponseBodyBean List<Map<String, String>> include id name status type capacity freeSpace
     * @throws Exception when error
     */
    List<Map<String,String>> getDataStoresByClusterObjectId(String clusterObjectId, String dataStoreType) throws DMEException;

    /**
     * Get host's vmKernel IP,only provisioning provisioning
     *
     * @param  hostObjectId host Object Id
     * @return ResponseBodyBean List<Map<String, String>> include device portgroup ipAddress key mac
     * @throws Exception when error
     */
    List<Map<String,String>> getVmKernelIpByHostObjectId(String hostObjectId) throws DMEException;
}
