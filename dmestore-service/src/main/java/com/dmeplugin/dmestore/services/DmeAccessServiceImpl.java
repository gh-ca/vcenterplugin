package com.dmeplugin.dmestore.services;


import com.dmeplugin.dmestore.dao.DmeInfoDao;
import com.dmeplugin.dmestore.entity.DmeInfo;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class DmeAccessServiceImpl implements DmeAccessService {

    private static final Logger LOG = LoggerFactory.getLogger(DmeAccessServiceImpl.class);

    private DmeInfoDao dmeInfoDao;

    @Autowired
    private Gson gson;

    @Override
    public Map<String, Object> accessDme(Map<String, Object> params) {
        Map<String, Object> remap = new HashMap<>();
        remap.put("code", 200);
        remap.put("message", "连接成功");
        remap.put("data", params);
        try {
            if (params != null) {
                LOG.info("params==" + gson.toJson(params));
                DmeInfo dmeInfo = new Gson().fromJson(gson.toJson(params), DmeInfo.class);
                LOG.info("dmeInfo==" + gson.toJson(dmeInfo));
                int re = dmeInfoDao.addDmeInfo(dmeInfo);
                LOG.info("re==" + re);
            }
        } catch (Exception e) {
            remap.put("code", 503);
            remap.put("message", "连接失败:" + e.getMessage());
            remap.put("data", params);
        }

        return remap;
    }

    @Override
    public Map<String, Object> refreshDme() {
        Map<String, Object> remap = new HashMap<>();
        remap.put("code", 200);
        remap.put("message", "连接成功");
//    remap.put("data", params);
        return remap;
    }


}
