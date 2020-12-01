package com.dmeplugin.vmware.mo;

import com.dmeplugin.vmware.util.Pair;
import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;

import java.util.List;

public class RootFsMO extends BaseMO {
    public RootFsMO(VmwareContext context, ManagedObjectReference mor) {
        super(context, mor);
    }
    /**
     * 获取host主机列表
     */
    public List<Pair<ManagedObjectReference, String>> getAllHostOnRootFs()
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        //查找hosts
        List<Pair<ManagedObjectReference, String>> hosts = context.inFolderByType(context.getRootFolder(),
                "HostSystem");
        return hosts;
    }

    /**
     * 获取集群列表
     */
    public List<Pair<ManagedObjectReference, String>> getAllClusterOnRootFs()
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        //查找clusters
        List<Pair<ManagedObjectReference, String>> clusters = context.inFolderByType(context.getRootFolder(),
                "ClusterComputeResource");
        return clusters;
    }

    /**
     * 获取datastore列表
     */
    public List<Pair<ManagedObjectReference, String>> getAllDatastoreOnRootFs()
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        //查找datastores
        List<Pair<ManagedObjectReference, String>> hosts = context.inFolderByType(context.getRootFolder(),
                "Datastore");
        return hosts;
    }

    public HostMO findHostById(String id) throws Exception {
        HostMO objmo = null;
        List<Pair<ManagedObjectReference, String>> objs = getAllHostOnRootFs();
        if (objs != null && objs.size() > 0) {
            for (Pair<ManagedObjectReference, String> obj : objs) {
                HostMO submo = new HostMO(context, obj.first());
                if (id.equals(submo.getMor().getValue())) {
                    objmo = submo;
                    break;
                }
            }
        }
        return objmo;
    }

    public ClusterMO findClusterById(String id) throws Exception {
        ClusterMO objmo = null;
        List<Pair<ManagedObjectReference, String>> objs = getAllClusterOnRootFs();
        if (objs != null && objs.size() > 0) {
            for (Pair<ManagedObjectReference, String> obj : objs) {
                ClusterMO submo = new ClusterMO(context, obj.first());
                if (id.equals(submo.getMor().getValue())) {
                    objmo = submo;
                    break;
                }
            }
        }
        return objmo;
    }
}
