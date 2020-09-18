package com.dmeplugin.dmestore.services;


import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

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

    private VCSDKUtils vcsdkUtils;

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
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
            //取得vcenter中的所有host。
            String listStr = vcsdkUtils.getDataStoresByHostObjectId(hostObjectId, dataStoreType);
            LOG.info("host getDataStoresByHostObjectId==" + listStr);
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() {
                }.getType());
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
            //取得vcenter中的所有host。
            String listStr = vcsdkUtils.getDataStoresByClusterObjectId(clusterObjectId, dataStoreType);
            LOG.info("host getDataStoresByClusterObjectId==" + listStr);
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() {
                }.getType());
            }
        } catch (Exception e) {
            LOG.error("get DataStores By ClusterObjectId error:", e);
            throw e;
        }
        LOG.info("getDataStoresByClusterObjectId===" + (lists == null ? "null" : (lists.size() + "==" + gson.toJson(lists))));
        return lists;
    }



}
