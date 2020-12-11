package com.huawei.vmware.util;

import com.huawei.vmware.mo.RootFsMO;
import com.vmware.vim25.ManagedObjectReference;

/**
 * RootFsMOFactory
 *
 * @author lianq
 * @ClassName: RootFsMOFactory
 * @since 2020-12-09
 */
public class RootFsMOFactory {
    private static RootFsMOFactory rootFsMOFactory;

    private RootFsMOFactory() {
    }

    /**
     * getInstance
     *
     * @return RootFsMOFactory
     */
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

    /**
     * build
     *
     * @param context context
     * @param mor     mor
     * @return RootFsMO
     */
    public RootFsMO build(VmwareContext context, ManagedObjectReference mor) {
        return new RootFsMO(context, mor);
    }
}
