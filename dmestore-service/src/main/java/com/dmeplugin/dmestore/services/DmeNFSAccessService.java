package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.NFSDataStoreFSAttr;
import com.dmeplugin.dmestore.model.NFSDataStorePortAttr;
import com.dmeplugin.dmestore.model.NFSDataStoreShareAttr;

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
      * @Description  NFS DataStore share属性获取
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
    NFSDataStorePortAttr  getNFSDatastoreLogicPortAttr(Map<String, String> params) throws Exception;

    /**
     * @Author wangxiangyong
     * @Description NFS DataStore FS属性获取
     * @Date 10:32 2020/9/4
     * @Param []
     * @Return com.dmeplugin.dmestore.model.NFSDataStoreFSAttr
     **/
    List<NFSDataStoreFSAttr> getNFSDatastoreFSAttr(String storage_id) throws Exception;
}
