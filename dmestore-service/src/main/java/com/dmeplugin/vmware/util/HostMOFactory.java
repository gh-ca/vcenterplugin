package com.dmeplugin.vmware.util;

import com.dmeplugin.vmware.mo.HostMO;
import com.vmware.vim25.ManagedObjectReference;

/**
 * @author lianq
 * @className HostMOFactory
 * @description TODO
 * @date 2020/11/26 15:44
 */
public class HostMOFactory {
    private static HostMOFactory hostMOFactory;

    private HostMOFactory() {
    }

    public static HostMOFactory getInstance() {
        if (hostMOFactory == null) {
            synchronized (HostMOFactory.class) {
                if (hostMOFactory == null) {
                    hostMOFactory = new HostMOFactory();
                }
            }
        }
        return hostMOFactory;
    }

    public HostMO build(VmwareContext context, ManagedObjectReference morHost) throws Exception {
        return new HostMO(context, morHost);
    }
}
