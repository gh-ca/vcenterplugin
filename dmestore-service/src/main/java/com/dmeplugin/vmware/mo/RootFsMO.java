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
        HostMO objmo = null;
        List<Pair<ManagedObjectReference, String>> objs = getAllHostOnRootFs();
        if (objs != null && objs.size() > 0) {
            for (Pair<ManagedObjectReference, String> obj : objs) {
                HostMO submo = new HostMO(_context, obj.first());
                if(name.equals(submo.getName())){
                    objmo = submo;
                    break;
                }
            }
        }
        return objmo;
    }

    public ClusterMO findCluster(String name) throws Exception {
        ClusterMO objmo = null;
        List<Pair<ManagedObjectReference, String>> objs = getAllClusterOnRootFs();
        if (objs != null && objs.size() > 0) {
            for (Pair<ManagedObjectReference, String> obj : objs) {
                ClusterMO submo = new ClusterMO(_context, obj.first());
                if(name.equals(submo.getName())){
                    objmo = submo;
                    break;
                }
            }
        }
        return objmo;
    }

    public DatastoreMO findDataStore(String name) throws Exception {
        DatastoreMO objmo = null;
        List<Pair<ManagedObjectReference, String>> objs = getAllDatastoreOnRootFs();
        if (objs != null && objs.size() > 0) {
            for (Pair<ManagedObjectReference, String> obj : objs) {
                DatastoreMO submo = new DatastoreMO(_context, obj.first());
                if(name.equals(submo.getName())){
                    objmo = submo;
                    break;
                }
            }
        }
        return objmo;
    }
}
