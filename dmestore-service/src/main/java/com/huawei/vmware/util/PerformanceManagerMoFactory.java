package com.huawei.vmware.util;

import com.huawei.vmware.mo.DatastoreMo;
import com.huawei.vmware.mo.PerformanceManagerMo;
import com.vmware.vim25.ManagedObjectReference;

/**
 * DatastoreMOFactory
 *
 * @author lianq
 * @ClassName: DatastoreMOFactory
 * @since 2020-12-09
 */
public class PerformanceManagerMoFactory {
    private static PerformanceManagerMoFactory performanceManagerFactory;

    private PerformanceManagerMoFactory() {
    }

    /**
     * getInstance
     *
     * @return DatastoreMOFactory
     */
    public static PerformanceManagerMoFactory getInstance() {
        if (performanceManagerFactory == null) {
            synchronized (PerformanceManagerMoFactory.class) {
                if (performanceManagerFactory == null) {
                    performanceManagerFactory = new PerformanceManagerMoFactory();
                }
            }
        }
        return performanceManagerFactory;
    }

    /**
     * build
     *
     * @param context context
     * @param morDatastore morDatastore
     * @return DatastoreMO
     * @throws Exception Exception
     */
    public PerformanceManagerMo build(VmwareContext context, ManagedObjectReference morDatastore) throws Exception {
        return new PerformanceManagerMo(context, morDatastore);
    }
}
