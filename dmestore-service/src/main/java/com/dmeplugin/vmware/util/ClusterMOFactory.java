package com.dmeplugin.vmware.util;

import com.dmeplugin.vmware.mo.ClusterMO;
import com.dmeplugin.vmware.mo.DatastoreMO;
import com.vmware.vim25.ManagedObjectReference;

/**
 * @author lianq
 * @className ClusterMOFactory
 * @description TODO
 * @date 2020/11/26 16:46
 */
public class ClusterMOFactory {
    private static ClusterMOFactory clusterMOFactory;

    private ClusterMOFactory() {
    }

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

    public ClusterMO build(VmwareContext context, ManagedObjectReference morCluster) throws Exception {
        return new ClusterMO(context, morCluster);
    }
}
