package com.huawei.vmware.util;

import com.huawei.vmware.mo.DatacenterMO;

/**
 * DatacenterMOFactory
 *
 * @author lianq
 * @ClassName: DatacenterMOFactory
 * @since 2020-12-09
 */
public class DatacenterMOFactory {

    private static DatacenterMOFactory datacenterMOFactory;

    private DatacenterMOFactory() {
    }

    /**
     * getInstance
     *
     * @return DatacenterMOFactory
     */
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

    /**
     * build
     *
     * @param context context
     * @param dcName  dcName
     * @return DatacenterMO
     * @throws Exception Exception
     */
    public DatacenterMO build(VmwareContext context, String dcName) throws Exception {
        return new DatacenterMO(context, dcName);
    }
}
