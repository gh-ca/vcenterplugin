package com.dmeplugin.dmestore.services;

import java.util.Map;

public interface PluginRegisterService {
    public Map<String, Object> installService(String vcenterIP,String vcenterPort,String vcenterUsername,String vcenterPassword,String dmeIp,
                                              String dmePort,String dmeUsername,String dmePassword);

    public void uninstallService();
}
