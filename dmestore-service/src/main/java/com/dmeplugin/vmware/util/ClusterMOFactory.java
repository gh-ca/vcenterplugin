package com.dmeplugin.vmware.util;

import com.dmeplugin.vmware.mo.ClusterMO;
import com.vmware.vim25.ManagedObjectReference;

/**
 * ClusterMOFactory
 *
 * @author lianq
 * @ClassName: ClusterMOFactory
 * @since 2020-12-09
 */
public class ClusterMOFactory {
    private static ClusterMOFactory clusterMOFactory;

    private ClusterMOFactory() {
    }

    /**
     * getInstance
     *
     * @return ClusterMOFactory
     */
    public static ClusterMOFactory getInstance() {
        if (clusterMOFactory == null) {
            synchronized (ClusterMOFactory.class) {
                if (clusterMOFactory == null) {
                    clusterMOFactory = new ClusterMOFactory();
                }
            }
        }
        return clusterMOFactory;
    }

    /**
     * build
     *
     * @param context context
     * @param morCluster morCluster
     * @return ClusterMO
     * @throws Exception Exception
     */
    public ClusterMO build(VmwareContext context, ManagedObjectReference morCluster) throws Exception {
        return new ClusterMO(context, morCluster);
    }
}
