package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.entity.VCenterInfo;
import com.dmeplugin.dmestore.utils.CipherUtils;
import com.dmeplugin.dmestore.utils.RestUtils;
import com.dmeplugin.vmware.VCConnectionHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
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
    public Map<String, Object> installService(String vcenterIP,String vcenterPort,String vcenterUsername,String vcenterPassword,String dmeIp,
                                              String dmePort,String dmeUsername,String dmePassword) {

        Map<String, Object> remap=new HashMap<>(16);
        try {
            //保存vcenter信息,如有已有vcenter信息，需要更新
            VCenterInfo vCenterInfo = new VCenterInfo();
            vCenterInfo.setHostIp(vcenterIP);
            vCenterInfo.setUserName(vcenterUsername);
            vCenterInfo.setPassword(CipherUtils.encryptString(vcenterPassword));
            vCenterInfo.setHostPort(Integer.parseInt(vcenterPort));
            vCenterInfoService.saveVCenterInfo(vCenterInfo);

            vcConnectionHelper.setServerurl("https://" + vcenterIP + ":" + vcenterPort + "/sdk");
            vcConnectionHelper.setUsername(vcenterUsername);
            vcConnectionHelper.setPassword(vcenterPassword);

            if (!"".equalsIgnoreCase(dmeIp)) {
                //调用接口，创建dme连接信息,如有已有dme信息，需要更新
                Map params = new HashMap(16);
                params.put("hostIp", dmeIp);
                params.put("hostPort", dmePort);
                params.put("userName", dmeUsername);
                params.put("password", dmePassword);
                remap = dmeAccessService.accessDme(params);

            }

        }catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return remap;
    }

    @Override
    public void uninstallService() {
        systemService.cleanData();
    }
}
