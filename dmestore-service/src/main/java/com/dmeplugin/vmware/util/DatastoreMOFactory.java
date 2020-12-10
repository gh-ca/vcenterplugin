package com.dmeplugin.vmware.util;

import com.dmeplugin.vmware.mo.DatastoreMO;
import com.vmware.vim25.ManagedObjectReference;

/**
 * DatastoreMOFactory
 *
 * @author lianq
 * @ClassName: DatastoreMOFactory
 * @since 2020-12-09
 */
public class DatastoreMOFactory {

    private static DatastoreMOFactory datastoreMOFactory;

    private DatastoreMOFactory() {
    }

    /**
     * getInstance
     *
     * @return DatastoreMOFactory
     */
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

    /**
     * build
     *
     * @param context      context
     * @param morDatastore morDatastore
     * @return DatastoreMO
     * @throws Exception Exception
     */
    public DatastoreMO build(VmwareContext context, ManagedObjectReference morDatastore) throws Exception {
        return new DatastoreMO(context, morDatastore);
    }
}
