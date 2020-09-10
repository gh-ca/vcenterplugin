package com.dmeplugin.vmware.mo;

import com.dmeplugin.vmware.util.Pair;
import com.dmeplugin.vmware.util.VmwareContext;
import com.google.gson.Gson;
import com.vmware.vim25.*;

import java.util.*;

public class RootFsMO extends BaseMO{
    public RootFsMO(VmwareContext context, ManagedObjectReference mor) {
        super(context, mor);
    }

    public RootFsMO(VmwareContext context, String morType, String morValue) {
        super(context, morType, morValue);
    }

    public List<Pair<ManagedObjectReference, String>> getAllDatacenterOnRootFs() throws Exception {
        //查找datacenters
        List<Pair<ManagedObjectReference, String>> datacenters = _context.inFolderByType(_context.getRootFolder(),
                "Datacenter");
        return datacenters;
    }

    /**
     * 获取host主机列表
     */
    public List<Pair<ManagedObjectReference, String>> getAllHostOnRootFs()
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        //查找hosts
        List<Pair<ManagedObjectReference, String>> hosts = _context.inFolderByType(_context.getRootFolder(),
                "HostSystem");
        return hosts;
    }

    /**
     * 获取集群列表
     */
    public List<Pair<ManagedObjectReference, String>> getAllClusterOnRootFs()
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        //查找clusters
        List<Pair<ManagedObjectReference, String>> clusters = _context.inFolderByType(_context.getRootFolder(),
                "ClusterComputeResource");
        return clusters;
    }

    /**
     * 获取datastore列表
     */
    public List<Pair<ManagedObjectReference, String>> getAllDatastoreOnRootFs()
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        //查找datastores
        List<Pair<ManagedObjectReference, String>> hosts = _context.inFolderByType(_context.getRootFolder(),
                "Datastore");
        return hosts;
    }

    public HostMO findHost(String name) throws Exception {
        HostMO objHostMO = null;
        List<Pair<ManagedObjectReference, String>> hosts = getAllHostOnRootFs();
        if (hosts != null && hosts.size() > 0) {
            for (Pair<ManagedObjectReference, String> host : hosts) {
                HostMO hostMo = new HostMO(_context, host.first());
                if(name.equals(hostMo.getName())){
                    objHostMO = hostMo;
                    break;
                }
            }
        }
        return objHostMO;
    }

    public ClusterMO findCluster(String name) throws Exception {
        ClusterMO objClusterMO = null;
        List<Pair<ManagedObjectReference, String>> clusters = getAllClusterOnRootFs();
        if (clusters != null && clusters.size() > 0) {
            for (Pair<ManagedObjectReference, String> cluster : clusters) {
                ClusterMO clusterMO = new ClusterMO(_context, cluster.first());
                if(name.equals(clusterMO.getName())){
                    objClusterMO = clusterMO;
                    break;
                }
            }
        }
        return objClusterMO;
    }
}
