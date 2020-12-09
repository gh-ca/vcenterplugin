package com.dmeplugin.vmware.util;

import com.dmeplugin.vmware.mo.DatacenterMO;
import com.dmeplugin.vmware.mo.DatastoreMO;
import com.vmware.vim25.ManagedObjectReference;

/**
 * @author lianq
 * @className DatacenterMOFactory
 * @description TODO
 * @date 2020/12/2 17:11
 */
public class DatacenterMOFactory {

    private static DatacenterMOFactory datacenterMOFactory;

    private DatacenterMOFactory() {
    }

    public static DatacenterMOFactory getInstance() {
        if (datacenterMOFactory == null) {
            synchronized (DatacenterMOFactory.class) {
                if (datacenterMOFactory == null) {
                    datacenterMOFactory = new DatacenterMOFactory();
                }
            }
        }
        return datacenterMOFactory;
    }

    public DatacenterMO build(VmwareContext context, String dcName) throws Exception {
        return new DatacenterMO(context, dcName);
    }
}
