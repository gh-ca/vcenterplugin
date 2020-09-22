package com.dmeplugin.dmestore.services;


import com.dmeplugin.dmestore.dao.DmeVmwareRalationDao;
import com.dmeplugin.dmestore.entity.DmeVmwareRelation;
import com.dmeplugin.dmestore.utils.ToolUtils;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @ClassName: VmwareAccessServiceImpl
 * @Company: GH-CA
 * @author: yy
 * @create: 2020-09-02
 **/
public class VmwareAccessServiceImpl implements VmwareAccessService {

    private static final Logger LOG = LoggerFactory.getLogger(VmwareAccessServiceImpl.class);

    private Gson gson = new Gson();

    private DmeVmwareRalationDao dmeVmwareRalationDao;

    private VCSDKUtils vcsdkUtils;

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
    }

    public void setDmeVmwareRalationDao(DmeVmwareRalationDao dmeVmwareRalationDao) {
        this.dmeVmwareRalationDao = dmeVmwareRalationDao;
    }

    @Override
    public List<Map<String, String>> listHosts() throws Exception {
        List<Map<String, String>> lists = null;
        try {
            //取得vcenter中的所有host。
            String listStr = vcsdkUtils.getAllHosts();
            LOG.info("host listStr==" + listStr);
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() {
                }.getType());
            }
        } catch (Exception e) {
            LOG.error("list hosts error:", e);
            throw e;
        }
        LOG.info("listHosts===" + (lists == null ? "null" : (lists.size() + "==" + gson.toJson(lists))));
        return lists;
    }

    @Override
    public List<Map<String,String>> getHostsByDsObjectId(String dataStoreObjectId) throws Exception {
        List<Map<String, String>> lists = null;
        try {
            //取得vcenter中的所有host。
            String listStr = vcsdkUtils.getHostsByDsObjectId(dataStoreObjectId);
            LOG.info("host getHostsByDsObjectId==" + listStr);
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() {
                }.getType());
            }
        } catch (Exception e) {
            LOG.error("get Hosts By DsObjectId error:", e);
            throw e;
        }
        LOG.info("getHostsByDsObjectId===" + (lists == null ? "null" : (lists.size() + "==" + gson.toJson(lists))));
        return lists;
    }

    @Override
    public List<Map<String,String>> listClusters() throws Exception{
        List<Map<String, String>> lists = null;
        try {
            //取得vcenter中的所有host。
            String listStr = vcsdkUtils.getAllClusters();
            LOG.info("listClusters==" + listStr);
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() {
                }.getType());
            }
        } catch (Exception e) {
            LOG.error("list listClusters error:", e);
            throw e;
        }
        LOG.info("listClusters===" + (lists == null ? "null" : (lists.size() + "==" + gson.toJson(lists))));
        return lists;
    }

    @Override
    public List<Map<String,String>> getClustersByDsObjectId(String dataStoreObjectId) throws Exception {
        List<Map<String, String>> lists = null;
        try {
            //取得vcenter中的所有host。
            String listStr = vcsdkUtils.getClustersByDsObjectId(dataStoreObjectId);
            LOG.info("host getClustersByDsObjectId==" + listStr);
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() {
                }.getType());
            }
        } catch (Exception e) {
            LOG.error("get Clusters By DsObjectId error:", e);
            throw e;
        }
        LOG.info("getClustersByDsObjectId===" + (lists == null ? "null" : (lists.size() + "==" + gson.toJson(lists))));
        return lists;
    }

    @Override
    public List<Map<String,String>> getDataStoresByHostObjectId(String hostObjectId, String dataStoreType) throws Exception {
        List<Map<String, String>> lists = null;
        try {
            //根据存储类型，取得vcenter中的所有存储。
            String listStr = vcsdkUtils.getDataStoresByHostObjectId(hostObjectId, dataStoreType);
            LOG.info("host getDataStoresByHostObjectId==" + listStr);
            if (!StringUtils.isEmpty(listStr)) {
                lists = new ArrayList<>();
                List<Map<String, String>> tmplists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() {
                }.getType());
                //根据dataStoreType取得数据库对应存储信息，然后以数据库信息为准过滤存储 objectId
                List<String> dvrlist = dmeVmwareRalationDao.getAllStorageIdByType(dataStoreType);
                LOG.info("dvrlist==" + gson.toJson(dvrlist));
                if (dvrlist != null && dvrlist.size() > 0) {
                    for(Map<String, String> dsmap:tmplists){
                        if(dsmap!=null && dsmap.get("objectId")!=null && dvrlist.contains(ToolUtils.getStr(dsmap.get("objectId")))){
                            lists.add(dsmap);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("get DataStores By HostObjectId error:", e);
            throw e;
        }
        LOG.info("getDataStoresByHostObjectId===" + (lists == null ? "null" : (lists.size() + "==" + gson.toJson(lists))));
        return lists;
    }

    @Override
    public List<Map<String,String>> getDataStoresByClusterObjectId(String clusterObjectId, String dataStoreType) throws Exception {
        List<Map<String, String>> lists = null;
        try {
            //根据存储类型，取得vcenter中的所有存储。
            String listStr = vcsdkUtils.getDataStoresByClusterObjectId(clusterObjectId, dataStoreType);
            LOG.info("host getDataStoresByClusterObjectId==" + listStr);
            if (!StringUtils.isEmpty(listStr)) {
                lists = new ArrayList<>();
                List<Map<String, String>> tmplists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() {
                }.getType());
                //根据dataStoreType取得数据库对应存储信息，然后以数据库信息为准过滤存储 objectId
                List<String> dvrlist = dmeVmwareRalationDao.getAllStorageIdByType(dataStoreType);
                LOG.info("dvrlist==" + gson.toJson(dvrlist));
                if (dvrlist != null && dvrlist.size() > 0) {
                    for(Map<String, String> dsmap:tmplists){
                        if(dsmap!=null && dsmap.get("objectId")!=null && dvrlist.contains(ToolUtils.getStr(dsmap.get("objectId")))){
                            lists.add(dsmap);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("get DataStores By ClusterObjectId error:", e);
            throw e;
        }
        LOG.info("getDataStoresByClusterObjectId===" + (lists == null ? "null" : (lists.size() + "==" + gson.toJson(lists))));
        return lists;
    }

    @Override
    public List<Map<String,String>> getVmKernelIpByHostObjectId(String hostObjectId) throws Exception{
        List<Map<String, String>> lists = null;
        try {
            //根据存储类型，取得vcenter中的所有存储。
            String listStr = vcsdkUtils.getVmKernelIpByHostObjectId(hostObjectId);
            LOG.info("host getVmKernelIpByHostObjectId==" + listStr);
            if (!StringUtils.isEmpty(listStr)) {
                lists = new ArrayList<>();
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() {
                }.getType());
            }
        } catch (Exception e) {
            LOG.error("get vmkernel ip by hostobjectid error:", e);
            throw e;
        }
        LOG.info("getVmKernelIpByHostObjectId===" + (lists == null ? "null" : (lists.size() + "==" + gson.toJson(lists))));
        return lists;
    }

}
