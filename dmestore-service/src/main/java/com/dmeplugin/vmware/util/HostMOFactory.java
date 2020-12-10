package com.dmeplugin.vmware.util;

import com.dmeplugin.vmware.mo.HostMO;
import com.vmware.vim25.ManagedObjectReference;

/**
 * HostMOFactory
 *
 * @author lianq
 * @ClassName: HostMOFactory
 * @since 2020-12-09
 */
public class HostMOFactory {
    private static HostMOFactory hostMOFactory;

    private HostMOFactory() {
    }

    /**
     * getInstance
     *
     * @return HostMOFactory
     */
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

    /**
     * build
     *
     * @param context context
     * @param morHost morHost
     * @return HostMO
     * @throws Exception Exception
     */
    public HostMO build(VmwareContext context, ManagedObjectReference morHost) throws Exception {
        return new HostMO(context, morHost);
    }

    /**
     * build
     *
     * @param context  context
     * @param hostName hostName
     * @return HostMO
     * @throws Exception
     */
    public HostMO build(VmwareContext context, String hostName) throws Exception {
        return new HostMO(context, hostName);
    }
}
