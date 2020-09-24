package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.NFSDataStoreFSAttr;
import com.dmeplugin.dmestore.model.NFSDataStoreLogicPortAttr;
import com.dmeplugin.dmestore.model.NFSDataStoreShareAttr;
import com.dmeplugin.dmestore.model.NfsDataInfo;

import java.util.List;
import java.util.Map;

/**
 * @ClassName DmeNFSAccessService
 * @Description TODO
 * @Author wangxiangyong
 * @Date 2020/9/4 10:18
 * @Version V1.0
 **/
public interface DmeNFSAccessService {

    /**
     * @Author wangxiangyong
     * @Description NFS DataStore share属性获取
     * @Date 10:21 2020/9/4
     * @Param []
     * @Return com.dmeplugin.dmestore.model.NFSDataStoreShareAttr
     **/
    NFSDataStoreShareAttr getNFSDatastoreShareAttr(String nfs_share_id) throws Exception;

    /**
     * @Author wangxiangyong
     * @Description NFS DataStore 逻辑端口属性获取
     * @Date 10:31 2020/9/4
     * @Param [params]
     * @Return com.dmeplugin.dmestore.model.NFSDataStorePortAttr
     **/
    NFSDataStoreLogicPortAttr getNFSDatastoreLogicPortAttr(String logic_port_id) throws Exception;

    /**
     * @Author wangxiangyong
     * @Description NFS DataStore FS属性获取
     * @Date 10:32 2020/9/4
     * @Param []
     * @Return com.dmeplugin.dmestore.model.NFSDataStoreFSAttr
     **/
    List<NFSDataStoreFSAttr> getNFSDatastoreFSAttr(String storage_id) throws Exception;

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
     * Mount nfs,params中包含了 include:
     * dataStoreObjectId: datastore的object id
     * hostId: 主机hostId 必 （主机与集群二选一）
     * clusterId: 集群clusterId 必（主机与集群二选一）
     * @param params: include dataStoreName,hostId,clusterId
     * @return: ResponseBodyBean
     */
    void unmountNfs(Map<String, Object> params) throws Exception;
}
