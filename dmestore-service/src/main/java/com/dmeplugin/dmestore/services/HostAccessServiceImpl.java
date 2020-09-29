package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.entity.VCenterInfo;
import com.dmeplugin.dmestore.model.EthPortInfo;
import com.dmeplugin.dmestore.utils.CipherUtils;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @ClassName HostAccessServiceImpl
 * @Description TODO
 * @Author wangxiangyong
 * @Date 2020/9/4 10:33
 * @Version V1.0
 **/
public class HostAccessServiceImpl implements HostAccessService {
    private static final Logger LOG = LoggerFactory.getLogger(HostAccessServiceImpl.class);

    private Gson gson = new Gson();

    private VCSDKUtils vcsdkUtils;

    private VCenterInfoService vCenterInfoService;

    public void setvCenterInfoService(VCenterInfoService vCenterInfoService) {
        this.vCenterInfoService = vCenterInfoService;
    }

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
    }

    @Override
    public void configureIscsi(Map<String, Object> params) throws Exception {
        if (params != null) {
            if(params.get("ethPorts")==null){
                LOG.error("configure Iscsi error:ethPorts is null.");
                throw new Exception("configure Iscsi error:ethPorts is null.");
            }
            if(params.get("vmKernel")==null){
                LOG.error("configure Iscsi error:vmKernel is null.");
                throw new Exception("configure Iscsi error:vmKernel is null.");
            }
            if(StringUtils.isEmpty(params.get("hostObjectId"))){
                LOG.error("configure Iscsi error:host ObjectId is null.");
                throw new Exception("configure Iscsi error:host ObjectId is null.");
            }
            List<Map<String, Object>> ethPorts = (List<Map<String, Object>>)params.get("ethPorts");
            Map<String, String> vmKernel = (Map<String, String>)params.get("vmKernel");
            String hostObjectId = params.get("hostObjectId").toString();
            LOG.info("params=="+gson.toJson(params));
            vcsdkUtils.configureIscsi(hostObjectId,vmKernel,ethPorts);
        } else {
            throw new Exception("configure Iscsi parameter exception:" + params);
        }
    }

    @Override
    public List<EthPortInfo> testConnectivity(Map<String, Object> params) throws Exception {
        List<EthPortInfo> relists = null;
        try {
            if (params != null) {
                if(params.get("ethPorts")==null){
                    LOG.error("test connectivity error:ethPorts is null.");
                    throw new Exception("test connectivity error:ethPorts is null.");
                }
                if(StringUtils.isEmpty(params.get("hostObjectId"))){
                    LOG.error("test connectivity error:host ObjectId is null.");
                    throw new Exception("test connectivity error:host ObjectId is null.");
                }
                List<Map<String, Object>> ethPorts = (List<Map<String, Object>>)params.get("ethPorts");
                String hostObjectId = params.get("hostObjectId").toString();
                Map<String, String> vmKernel = (Map<String, String>)params.get("vmKernel");
                LOG.info("params=="+gson.toJson(params));
                LOG.info("ethPorts=="+gson.toJson(ethPorts));
                LOG.info("vmKernel=="+gson.toJson(vmKernel));

                VCenterInfo vCenterInfo= vCenterInfoService.getVCenterInfo();
                LOG.info("vCenterInfo=="+gson.toJson(vCenterInfo));
                if (null!=vCenterInfo) {
                    String conStr = vcsdkUtils.testConnectivity(hostObjectId,ethPorts,vmKernel,vCenterInfo);
                    if(!StringUtils.isEmpty(conStr)){
                        relists = gson.fromJson(conStr, new TypeToken<List<EthPortInfo>>() {}.getType());
                    }
                }
            } else {
                throw new Exception("test connectivity parameter exception:" + params);
            }
        } catch (Exception e) {
            LOG.error("test connectivity error:", e);
            throw e;
        }
        LOG.info("test connectivity relists===" + (relists == null ? "null" : (relists.size() + "==" + gson.toJson(relists))));
        return relists;
    }


}
