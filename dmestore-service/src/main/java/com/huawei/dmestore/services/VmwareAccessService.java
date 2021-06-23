package com.huawei.dmestore.services;

import com.huawei.dmestore.entity.DmeVmwareRelation;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.DmeSqlException;
import com.huawei.dmestore.model.ClusterTree;

import java.util.List;
import java.util.Map;

/**
 * VmwareAccessService
 *
 * @author yy
 * @since 2020-09-15
 **/
public interface VmwareAccessService {
    /**
     * Access hosts
     *
     * @return List
     * @throws DmeException when error
     */
    List<Map<String, String>> listHosts() throws DmeException;

    /**
     * Access hosts
     *
     * @param dataStoreObjectId dataStore ObjectId
     * @return List
     * @throws DmeException DmeException
     */
    List<Map<String, String>> getHostsByDsObjectId(String dataStoreObjectId) throws DmeException;

    /**
     * Access clusters
     *
     * @return List
     * @throws DmeException when error
     */
    List<Map<String, String>> listClusters() throws DmeException;

    /**
     * Access clusters
     *
     * @param dataStoreObjectId dataStore ObjectId
     * @return List
     * @throws DmeException when error
     */
    List<Map<String, String>> getClustersByDsObjectId(String dataStoreObjectId) throws DmeException;

    /**
     * Access datastore
     *
     * @param hostObjectId host ObjectId
     * @param dataStoreType dataStore Type
     * @return List
     * @throws DmeException when error
     */
    List<Map<String, String>> getDataStoresByHostObjectId(String hostObjectId, String dataStoreType)
        throws DmeException;

    /**
     * Access datastore
     *
     * @param clusterObjectId cluster ObjectId
     * @param dataStoreType dataStore Type
     * @return List
     * @throws DmeException when error
     */
    List<Map<String, String>> getDataStoresByClusterObjectId(String clusterObjectId, String dataStoreType)
        throws DmeException;

    /**
     * Get host's vmKernel IP,only provisioning provisioning
     *
     * @param hostObjectId host Object Id
     * @return List
     * @throws DmeException when error
     */
    List<Map<String, String>> getVmKernelIpByHostObjectId(String hostObjectId) throws DmeException;

    /**
     * Access datastore
     *
     * @param hostObjectId host ObjectId
     * @param dataStoreType dataStore Type
     * @return List
     * @throws DmeException when error
     */
    List<Map<String, String>> getMountDataStoresByHostObjectId(String hostObjectId, String dataStoreType)
        throws DmeException;

    /**
     * Access datastore
     *
     * @param clusterObjectId cluster ObjectId
     * @param dataStoreType dataStore Type
     * @return List
     * @throws DmeException DmeException
     */
    List<Map<String, String>> getMountDataStoresByClusterObjectId(String clusterObjectId, String dataStoreType)
        throws DmeException;

    /**
     * getDmeVmwareRelationByDsId
     *
     * @param storeId storeId
     * @return DmeVmwareRelation
     * @throws DmeSqlException DmeSqlException
     */
    DmeVmwareRelation getDmeVmwareRelationByDsId(String storeId) throws DmeSqlException;
    /**
      * @Description:  get cluster tree
      * @Param @param null
      * @return @return
      * @throws DmeException
      * @author yc
      * @Date 2021/5/11 14:21
     */
    List<ClusterTree> listclustersReturnTree() throws DmeException;
    /**
      * @Description: 挂载页面查询可挂载的主机和集群信息
      * @Param @param null
      * @return @return
      * @throws
      * @author yc
      * @Date 2021/5/14 9:47
     */
    List<ClusterTree> getClustersAndHostsByDsObjectIdNew(String dataStoreObjectId) throws DmeException;
    /**
      * @Description: 查询独立主机
      * @Param @param null
      * @return @return
      * @throws
      * @author yc
      * @Date 2021/6/7 16:53
     */
    List<ClusterTree>  listHostsAndClusterReturnTree() throws DmeException;

}
