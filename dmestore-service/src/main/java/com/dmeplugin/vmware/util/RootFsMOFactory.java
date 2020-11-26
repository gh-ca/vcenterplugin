package com.dmeplugin.vmware.util;

import com.dmeplugin.vmware.mo.RootFsMO;
import com.vmware.vim25.ManagedObjectReference;

/**
 * @author lianq
 * @className RootFsMOFactory
 * @description TODO
 * @date 2020/11/26 11:12
 */
public class RootFsMOFactory {

    private static RootFsMOFactory rootFsMOFactory;

    private RootFsMOFactory() {
    }

    public static RootFsMOFactory getInstance() {
        if (rootFsMOFactory == null) {
            synchronized (RootFsMOFactory.class) {
                if (rootFsMOFactory == null) {
                    rootFsMOFactory = new RootFsMOFactory();
                }
            }
        }
        return rootFsMOFactory;
    }

    public RootFsMO build(VmwareContext context, ManagedObjectReference mor){
        return new RootFsMO(context, mor);
    }

}
