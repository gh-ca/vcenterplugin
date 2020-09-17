package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.mvc.VmfsOperationController;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lianq
 * @className NfsOperationServiceImpl
 * @description TODO
 * @date 2020/9/16 16:25
 */
public class NfsOperationServiceImpl implements NfsOperationService {

    public static final Logger LOG = LoggerFactory.getLogger(VmfsOperationController.class);

    @Autowired
    private VCSDKUtils vcsdkUtils;

    @Override
    public Map<String, Object> createNfsDatastore(Map<String, String> params) {
        /**
         * params (String serverHost, Integer logicPort, String exportPath, String nfsName ,String accessMode,String mountHost )
         */
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "create nfs datastore success !");

        if (params == null || params.size() == 0) {
            resMap.put("code", 403);
            resMap.put("msg", "params error , please check your params !");
            return resMap;
        }
        String serverHost = params.get("serverHost");
        String port = params.get("logicPort");
        String exportPath = params.get("exportPath");
        String nfsName = params.get("nfsName");
        String accessMode = params.get("accessMode");
        String mountHost = params.get("mountHost");
        if (StringUtils.isEmpty(serverHost)||StringUtils.isEmpty(port)||StringUtils.isEmpty(exportPath)
                ||StringUtils.isEmpty(nfsName)||StringUtils.isEmpty(accessMode)||StringUtils.isEmpty(mountHost)) {
            resMap.put("code", 403);
            resMap.put("msg", "params error , please check your params !");
            return resMap;
        }
        //VCSDKUtils vcsdkUtils = new VCSDKUtils();
        Integer logicPort = Integer.valueOf(port);
        String result = vcsdkUtils.createNfsDatastore(serverHost, logicPort, exportPath, nfsName, accessMode, mountHost);
        if ("failed".equals(result)) {
            resMap.put("code", 403);
            resMap.put("msg", "create nfs datastore error!");
            return resMap;
        }
        return resMap;
    }
}
