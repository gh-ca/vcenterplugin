package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        } else {
            throw new Exception("Mount nfs parameter exception:" + params);
        }
    }


}
