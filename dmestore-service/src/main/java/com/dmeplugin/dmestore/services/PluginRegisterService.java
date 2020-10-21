package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DMEException;

public interface PluginRegisterService {
    public void installService(String vcenterIP, String vcenterPort, String vcenterUsername, String vcenterPassword, String dmeIp,
                               String dmePort, String dmeUsername, String dmePassword) throws DMEException;

    public void uninstallService();
}
