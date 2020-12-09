package com.dmeplugin.vmware.util;

import com.dmeplugin.vmware.mo.DatastoreMO;
import com.vmware.vim25.ManagedObjectReference;

/**
 * @author lianq
 * @className DatastoreMOFactory
 * @description TODO
 * @date 2020/11/26 14:44
 */
public class DatastoreMOFactory {

    private static DatastoreMOFactory datastoreMOFactory;

    private DatastoreMOFactory() {
    }

    public static DatastoreMOFactory getInstance() {
        if (datastoreMOFactory == null) {
            synchronized (DatastoreMOFactory.class) {
                if (datastoreMOFactory == null) {
                    datastoreMOFactory = new DatastoreMOFactory();
                }
            }
        }
        return datastoreMOFactory;
    }

    public DatastoreMO build(VmwareContext context, ManagedObjectReference morDatastore) throws Exception {
        return new DatastoreMO(context, morDatastore);
    }
}
