package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.entity.VCenterInfo;
import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.model.EthPortInfo;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author wangxiangyong
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
    public void configureIscsi(Map<String, Object> params) throws DMEException {
        if (params != null) {
            if (null == params.get(DmeConstants.ETHPORTS)) {
                LOG.error("configure Iscsi error:ethPorts is null.");
                throw new DMEException("configure Iscsi error:ethPorts is null.");
            }
            if (null == params.get(DmeConstants.VMKERNEL)) {
                LOG.error("configure Iscsi error:vmKernel is null.");
                throw new DMEException("configure Iscsi error:vmKernel is null.");
            }
            if (StringUtils.isEmpty(params.get(DmeConstants.HOSTOBJECTID))) {
                LOG.error("configure Iscsi error:host ObjectId is null.");
                throw new DMEException("configure Iscsi error:host ObjectId is null.");
            }
            List<Map<String, Object>> ethPorts = (List<Map<String, Object>>) params.get("ethPorts");
            Map<String, String> vmKernel = (Map<String, String>) params.get("vmKernel");
            String hostObjectId = params.get("hostObjectId").toString();
            LOG.info("params==" + gson.toJson(params));
            vcsdkUtils.configureIscsi(hostObjectId, vmKernel, ethPorts);
        } else {
            throw new DMEException("configure Iscsi parameter exception:" + params);
        }
    }

    @Override
    public List<EthPortInfo> testConnectivity(Map<String, Object> params) throws DMEException {
        List<EthPortInfo> relists = null;
        try {
            if (params != null) {
                if (null == params.get(DmeConstants.ETHPORTS)) {
                    LOG.error("test connectivity error:ethPorts is null.");
                    throw new DMEException("test connectivity error:ethPorts is null.");
                }
                if (StringUtils.isEmpty(params.get(DmeConstants.HOSTOBJECTID))) {
                    LOG.error("test connectivity error:host ObjectId is null.");
                    throw new DMEException("test connectivity error:host ObjectId is null.");
                }
                List<Map<String, Object>> ethPorts = (List<Map<String, Object>>) params.get("ethPorts");
                String hostObjectId = params.get("hostObjectId").toString();
                Map<String, String> vmKernel = null;
                if (null != params.get(DmeConstants.VMKERNEL)) {
                    vmKernel = (Map<String, String>) params.get("vmKernel");
                }
                LOG.info("params==" + gson.toJson(params));
                LOG.info("ethPorts==" + gson.toJson(ethPorts));
                LOG.info("vmKernel==" + gson.toJson(vmKernel));

                VCenterInfo vCenterInfo = vCenterInfoService.getVcenterInfo();
                LOG.info("vCenterInfo==" + gson.toJson(vCenterInfo));
                if (null != vCenterInfo) {
                    String conStr = vcsdkUtils.testConnectivity(hostObjectId, ethPorts, vmKernel, vCenterInfo);
                    if (!StringUtils.isEmpty(conStr)) {
                        relists = gson.fromJson(conStr, new TypeToken<List<EthPortInfo>>() {
                        }.getType());
                    }
                }
            } else {
                throw new Exception("test connectivity parameter exception:" + params);
            }
        } catch (Exception e) {
            LOG.error("test connectivity error:", e);
            throw new DMEException(e.getMessage());
        }
        LOG.info("test connectivity relists===" +
            (relists == null ? "null" : (relists.size() + "==" + gson.toJson(relists))));
        return relists;
    }


}
