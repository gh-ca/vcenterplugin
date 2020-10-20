package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.NfsDataStoreFsAttr;
import com.dmeplugin.dmestore.model.NfsDataStoreLogicPortAttr;
import com.dmeplugin.dmestore.model.NfsDataStoreShareAttr;
import com.dmeplugin.dmestore.model.NfsDataInfo;

import java.util.List;
import java.util.Map;

/**
 * @author wangxiangyong
 **/
public interface DmeNFSAccessService {

    /**
     * NFS DataStore share属性获取
     * @author wangxy
     * @date 14:15 2020/10/15
     * @param storageObjectId vCenter存储ID
     * @throws Exception
     * @return com.dmeplugin.dmestore.model.NFSDataStoreShareAttr
     **/
    NfsDataStoreShareAttr getNfsDatastoreShareAttr(String storageObjectId) throws Exception;

    /**
     * NFS DataStore 逻辑端口属性获取
     * @author wangxy
     * @date 14:16 2020/10/15
     * @param storageObjectId vCenter存储ID
     * @throws Exception
     * @return com.dmeplugin.dmestore.model.NFSDataStoreLogicPortAttr
     **/
    NfsDataStoreLogicPortAttr getNfsDatastoreLogicPortAttr(String storageObjectId) throws Exception;

    /**
     * NFS DataStore FileSystem属性获取
     * @author wangxy
     * @date 14:16 2020/10/15
     * @param storageObjectId vCenter存储ID
     * @throws Exception
     * @return java.util.List<com.dmeplugin.dmestore.model.NFSDataStoreFSAttr>
     **/
    List<NfsDataStoreFsAttr> getNfsDatastoreFsAttr(String storageObjectId) throws Exception;

    /**
     * 扫描NFS 存储DataSotre 与share fs logicPort的关系
     *
     * @return
     * @throws Exception
     */
    boolean scanNfs() throws Exception;

    /**
     * List nfs
     *
     * @return List<NfsDataInfo>
     * @throws Exception when error
     */
    List<NfsDataInfo> listNfs() throws Exception;

    /**
     * List nfs Performance
     *
     * @param fsIds fs id
     * @return List<NfsDataInfo>
     * @throws Exception when error
     */
    List<NfsDataInfo> listNfsPerformance(List<String> fsIds) throws Exception;

    /**
     * Mount nfs,params中包含了 include:
     * dataStoreObjectId: datastore的object id
     * dataStoreName: datastore名称  必
     * list<map<str,str>> hosts: 主机objectid,主机名称 必 （主机与集群二选一）
     * list<map<str,str>>  clusters: 集群objectid,集群名称 必（主机与集群二选一）
     * str mountType: 挂载模式（只读或读写）  readOnly/readWrite
     *
     * @param params: include dataStoreName,hosts,clusters,mountType
     * @return: ResponseBodyBean
     */
    void mountNfs(Map<String, Object> params) throws Exception;

    /**
     * unmount nfs,params中包含了 include:
     * dataStoreObjectId: datastore的object id
     * hostId: 主机hostId 必 （主机与集群二选一）
     * clusterId: 集群clusterId 必（主机与集群二选一）
     * @param params: include dataStoreName,hostId,clusterId
     * @return: ResponseBodyBean
     */
    void unmountNfs(Map<String, Object> params) throws Exception;

    /**
     * delete nfs,params中包含了 include:
     * dataStoreObjectId: datastore的object id
     * @param params: include dataStoreObjectId
     * @return: ResponseBodyBean
     */
    void deleteNfs(Map<String, Object> params) throws Exception;

    /**
     * 通过nfs storageId查询DME侧关联的主机信息
     * @return 返回主机列表，单个主机的信息以map方式存储属性和属性值
     * @throws Exception
     */
    List<Map<String, Object>> getHostsMountDataStoreByDsObjectId(String storageId) throws Exception;

    /**
     * 通过nfs storageId查询DME侧关联的主机组信息
     * @param storageId
     * @return 返回主机组列表，单个主机组的信息以map方式存储属性和属性值
     * @throws Exception
     */
    List<Map<String, Object>> getClusterMountDataStoreByDsObjectId(String storageId) throws Exception;

}
