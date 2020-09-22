package com.dmeplugin.vmware;

import com.dmeplugin.dmestore.entity.VCenterInfo;
import com.dmeplugin.dmestore.services.VCenterInfoService;
import com.dmeplugin.dmestore.utils.CipherUtils;
import com.dmeplugin.vmware.util.TestVmwareContextFactory;
import com.dmeplugin.vmware.util.VmwareContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SpringBootConnectionHelper extends VCConnectionHelper{

    private static final Logger log = LoggerFactory.getLogger(SpringBootConnectionHelper.class);
    private VCenterInfoService vCenterInfoService;

    public VCenterInfoService getvCenterInfoService() {
        return vCenterInfoService;
    }

    public void setvCenterInfoService(VCenterInfoService vCenterInfoService) {
        this.vCenterInfoService = vCenterInfoService;
    }


    @Override
    public VmwareContext getServerContext(String serverguid) throws Exception {
        try {
            VCenterInfo vCenterInfo= vCenterInfoService.getVCenterInfo();
            if (null!=vCenterInfo) {
                this.setServerurl(vCenterInfo.getHostIp());
                this.setServerport(vCenterInfo.getHostPort());
                this.setUsername(vCenterInfo.getUserName());
                this.setPassword(CipherUtils.decryptString(vCenterInfo.getPassword()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取本地vcenter信息出错");
        }
        return TestVmwareContextFactory.getContext(getServerurl(),getServerport(),getUsername(),getPassword());
    }

    @Override
    public VmwareContext[] getAllContext() throws Exception {
        try {
            VCenterInfo vCenterInfo= vCenterInfoService.getVCenterInfo();
            if (null!=vCenterInfo) {
                this.setServerurl(vCenterInfo.getHostIp());
                this.setServerport(vCenterInfo.getHostPort());
                this.setUsername(vCenterInfo.getUserName());
                this.setPassword(CipherUtils.decryptString(vCenterInfo.getPassword()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        VmwareContext vmwareContext=TestVmwareContextFactory.getContext(getServerurl(),getServerport(),getUsername(),getPassword());
        List<VmwareContext> vmwareContextList=new ArrayList<>();
        vmwareContextList.add(vmwareContext);
        return vmwareContextList.toArray(new VmwareContext[0]);
    }
}
