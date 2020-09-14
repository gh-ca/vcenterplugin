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
    public List<Map<String,String>> getHostsByDsName(String dataStoreName) throws Exception {
        List<Map<String, String>> lists = null;
        try {
            //取得vcenter中的所有host。
            String listStr = vcsdkUtils.getHostsByDsName(dataStoreName);
            LOG.info("host getHostsByDsName==" + listStr);
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() {
                }.getType());
            }
        } catch (Exception e) {
            LOG.error("get Hosts By DsName error:", e);
            throw e;
        }
        LOG.info("getHostsByDsName===" + (lists == null ? "null" : (lists.size() + "==" + gson.toJson(lists))));
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
    public List<Map<String,String>> getClustersByDsName(String dataStoreName) throws Exception {
        List<Map<String, String>> lists = null;
        try {
            //取得vcenter中的所有host。
            String listStr = vcsdkUtils.getClustersByDsName(dataStoreName);
            LOG.info("host getClustersByDsName==" + listStr);
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() {
                }.getType());
            }
        } catch (Exception e) {
            LOG.error("get Clusters By DsName error:", e);
            throw e;
        }
        LOG.info("getClustersByDsName===" + (lists == null ? "null" : (lists.size() + "==" + gson.toJson(lists))));
        return lists;
    }

    @Override
    public List<Map<String,String>> getDataStoresByHostName(String hostName) throws Exception {
        List<Map<String, String>> lists = null;
        try {
            //取得vcenter中的所有host。
            String listStr = vcsdkUtils.getDataStoresByHostName(hostName);
            LOG.info("host getDataStoresByHostName==" + listStr);
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() {
                }.getType());
            }
        } catch (Exception e) {
            LOG.error("get DataStores By HostName error:", e);
            throw e;
        }
        LOG.info("getDataStoresByHostName===" + (lists == null ? "null" : (lists.size() + "==" + gson.toJson(lists))));
        return lists;
    }

    @Override
    public List<Map<String,String>> getDataStoresByClusterName(String clusterName) throws Exception {
        List<Map<String, String>> lists = null;
        try {
            //取得vcenter中的所有host。
            String listStr = vcsdkUtils.getDataStoresByClusterName(clusterName);
            LOG.info("host getDataStoresByClusterName==" + listStr);
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() {
                }.getType());
            }
        } catch (Exception e) {
            LOG.error("get DataStores By ClusterName error:", e);
            throw e;
        }
        LOG.info("getDataStoresByClusterName===" + (lists == null ? "null" : (lists.size() + "==" + gson.toJson(lists))));
        return lists;
    }



}
