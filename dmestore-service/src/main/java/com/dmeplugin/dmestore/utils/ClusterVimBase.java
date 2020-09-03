package com.dmeplugin.dmestore.utils;

import com.vmware.vim25.ComputeResourceSummary;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ClusterVimBase {
    private ConnectedVimServiceBase connectedVimServiceBase;

    public ConnectedVimServiceBase getConnectedVimServiceBase() {
        return connectedVimServiceBase;
    }

    public void setConnectedVimServiceBase(ConnectedVimServiceBase connectedVimServiceBase) {
        this.connectedVimServiceBase = connectedVimServiceBase;
    }

    public ClusterVimBase(ConnectedVimServiceBase connectedVimServiceBase) {
        this.connectedVimServiceBase = connectedVimServiceBase;
    }

    /**
     * 获取cluster的 mor列表
     */
    public Map<String, ManagedObjectReference> getClusters()
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        //查找hosts
        Map<String, ManagedObjectReference> clusters = connectedVimServiceBase.getMOREFs.inFolderByType(connectedVimServiceBase.serviceContent.getRootFolder(),
                "ClusterComputeResource");
        return clusters;
    }

    /**
     * 根据名称获取cluster mor
     */
    public ManagedObjectReference getClusterByName(String clusterName)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        //查找hosts
        ManagedObjectReference clusters = connectedVimServiceBase.getMOREFs.inFolderByType(connectedVimServiceBase.serviceContent.getRootFolder(),
                "ClusterComputeResource").get(clusterName);
        return clusters;
    }

    /**
     * 获取所有cluster的summary
     */
    public List<ComputeResourceSummary> getAllClusterSummary() throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        List<ComputeResourceSummary> clustersummarylist=new ArrayList<>();
        Map<String, ManagedObjectReference> hosts=getClusters();

        Collection<Map<String, Object>> restcollection=connectedVimServiceBase.getMOREFs.entityProps(new ArrayList<>(hosts.values()),new String[]{"summary"}).values();

        for (Map<String, Object> clustermap: restcollection){
            ComputeResourceSummary clustersummary= (ComputeResourceSummary) clustermap.get("summary");
            clustersummarylist.add(clustersummary);
        }
        return clustersummarylist;
    }

    /**
     * 按名称查找cluster的summary
     */
    public ComputeResourceSummary getClusterSummaryByName(String clustername) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        ManagedObjectReference cluster=getClusterByName(clustername);

        ComputeResourceSummary restcollection= (ComputeResourceSummary) connectedVimServiceBase.getMOREFs.entityProps(cluster,new String[]{"summary"}).get("summary");


        return restcollection;
    }
}
