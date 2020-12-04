package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DMEException;

public interface PluginRegisterService {
    void installService(String vcenterIp, String vcenterPort, String vcenterUsername, String vcenterPassword, String dmeIp,
                        String dmePort, String dmeUsername, String dmePassword) throws DMEException;

    void uninstallService();
}
