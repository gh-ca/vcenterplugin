package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.EthPortInfo;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.Gson;
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
            LOG.info("ethPorts=="+gson.toJson(ethPorts));
            vcsdkUtils.configureIscsi(hostObjectId,vmKernel,ethPorts);
        } else {
            throw new Exception("configure Iscsi parameter exception:" + params);
        }
    }


}
