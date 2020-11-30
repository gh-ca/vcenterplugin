package com.dmeplugin.vmware.util;

import com.dmeplugin.vmware.autosdk.SessionHelper;
import com.dmeplugin.vmware.mo.HostMO;
import com.vmware.vim25.ManagedObjectReference;

/**
 * @author lianq
 * @className SessionHelperFactory
 * @description TODO
 * @date 2020/11/30 10:47
 */
public class SessionHelperFactory {
    private static SessionHelperFactory sessionHelperFactory;

    private SessionHelperFactory() {
    }

    public static SessionHelperFactory getInstance() {
        if (sessionHelperFactory == null) {
            synchronized (SessionHelperFactory.class) {
                if (sessionHelperFactory == null) {
                    sessionHelperFactory = new SessionHelperFactory();
                }
            }
        }
        return sessionHelperFactory;
    }

    public SessionHelper build() throws Exception {
        return new SessionHelper();
    }
}
