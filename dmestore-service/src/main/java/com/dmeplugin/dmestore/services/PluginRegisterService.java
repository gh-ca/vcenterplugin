package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DmeException;

public interface PluginRegisterService {
    void installService(String vcenterIp, String vcenterPort, String vcenterUsername, String vcenterPassword, String dmeIp,
                        String dmePort, String dmeUsername, String dmePassword) throws DmeException;

    void uninstallService();
}
