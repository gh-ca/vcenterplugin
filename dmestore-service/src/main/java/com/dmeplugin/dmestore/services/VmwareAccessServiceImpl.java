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

    @Autowired
    private Gson gson = new Gson();


    @Override
    public List<Map<String, String>> listHosts() throws Exception {
        List<Map<String, String>> lists = null;
        try {
            //取得vcenter中的所有host。
            String listStr = VCSDKUtils.getAllHosts();
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
    public List<Map<String,String>> listClusters() throws Exception{
        List<Map<String, String>> lists = null;
        try {
            //取得vcenter中的所有host。
            String listStr = VCSDKUtils.getAllClusters();
            LOG.info("host listClusters==" + listStr);
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
}
