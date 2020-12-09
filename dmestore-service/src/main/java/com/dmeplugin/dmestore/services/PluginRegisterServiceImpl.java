package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.entity.VCenterInfo;
import com.dmeplugin.dmestore.exception.DmeException;
import com.dmeplugin.dmestore.utils.CipherUtils;
import com.dmeplugin.vmware.VCConnectionHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class PluginRegisterServiceImpl implements PluginRegisterService{

    @Autowired
    private DmeAccessService dmeAccessService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private VCenterInfoService vCenterInfoService;


    @Autowired
    private VCConnectionHelper vcConnectionHelper;

    @Override
    public void installService(String vcenterIp, String vcenterPort, String vcenterUsername, String vcenterPassword, String dmeIp,
                               String dmePort, String dmeUsername, String dmePassword) throws DmeException {

        //Map<String, Object> remap=new HashMap<>(16);
        try {
            //保存vcenter信息,如有已有vcenter信息，需要更新
            VCenterInfo vCenterInfo = new VCenterInfo();
            vCenterInfo.setHostIp(vcenterIp);
            vCenterInfo.setUserName(vcenterUsername);
            vCenterInfo.setPassword(CipherUtils.encryptString(vcenterPassword));
            vCenterInfo.setHostPort(Integer.parseInt(vcenterPort));
            vCenterInfoService.saveVcenterInfo(vCenterInfo);

            vcConnectionHelper.setServerurl("https://" + vcenterIp + ":" + vcenterPort + "/sdk");
            vcConnectionHelper.setUsername(vcenterUsername);
            vcConnectionHelper.setPassword(vcenterPassword);

            if (!"".equalsIgnoreCase(dmeIp)) {
                //调用接口，创建dme连接信息,如有已有dme信息，需要更新
                Map params = new HashMap(16);
                params.put("hostIp", dmeIp);
                params.put("hostPort", dmePort);
                params.put("userName", dmeUsername);
                params.put("password", dmePassword);
                dmeAccessService.accessDme(params);

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new DmeException("503",e.getMessage());
        }
       // return remap;
    }

    @Override
    public void uninstallService() {
        systemService.cleanData();
    }
}
