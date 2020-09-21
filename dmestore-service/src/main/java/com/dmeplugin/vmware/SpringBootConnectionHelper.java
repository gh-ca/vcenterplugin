package com.dmeplugin.vmware;

import com.dmeplugin.dmestore.entity.VCenterInfo;
import com.dmeplugin.dmestore.services.VCenterInfoService;
import com.dmeplugin.dmestore.utils.CipherUtils;
import com.dmeplugin.vmware.util.TestVmwareContextFactory;
import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.ManagedObjectReference;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SpringBootConnectionHelper extends VCConnectionHelper{

    private VCenterInfoService vCenterInfoService;

    public SpringBootConnectionHelper(VCenterInfoService vCenterInfoService) {
        this.vCenterInfoService = vCenterInfoService;
        try {
          VCenterInfo vCenterInfo= vCenterInfoService.getVCenterInfo();
          if (null!=vCenterInfo) {
              this.setServerurl("https://" + vCenterInfo.getHostIp() + ":" + vCenterInfo.getHostPort() + "/sdk");
              this.setUsername(vCenterInfo.getUserName());
              this.setPassword(CipherUtils.decryptString(vCenterInfo.getPassword()));
          }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public VmwareContext getServerContext(String serverguid) throws Exception {
        return TestVmwareContextFactory.getContext(getServerurl(),getUsername(),getPassword());
    }

    @Override
    public VmwareContext[] getAllContext() throws Exception {
        VmwareContext vmwareContext=TestVmwareContextFactory.getContext(getServerurl(),getUsername(),getPassword());
        List<VmwareContext> vmwareContextList=new ArrayList<>();
        vmwareContextList.add(vmwareContext);
        return vmwareContextList.toArray(new VmwareContext[0]);
    }
}
