package com.huawei.dmestore.model;

import java.util.ArrayList;
import java.util.List;

public class EsxiInstanceTree {

    private List<String> hostIds = new ArrayList<>();
    private List<String> hostIdsOnCluster = new ArrayList<>();
    private List<String> clusterIds = new ArrayList<>();
    private List<String> hostIdsIndependence = new ArrayList<>();
    //集群信息
    private List<ClusterTree> clusters = new ArrayList<>();
    //主机信息
    private List<ClusterTree> hosts = new ArrayList<>();
    public EsxiInstanceTree() {
    }

    public List<String> getHostIds() {
        return hostIds;
    }

    public void setHostIds(List<String> hostIds) {
        this.hostIds = hostIds;
    }

    public List<String> getHostIdsOnCluster() {
        return hostIdsOnCluster;
    }

    public void setHostIdsOnCluster(List<String> hostIdsOnCluster) {
        this.hostIdsOnCluster = hostIdsOnCluster;
    }

    public List<String> getClusterIds() {
        return clusterIds;
    }

    public void setClusterIds(List<String> clusterIds) {
        this.clusterIds = clusterIds;
    }

    public List<String> getHostIdsIndependence() {
        return hostIdsIndependence;
    }

    public void setHostIdsIndependence(List<String> hostIdsIndependence) {
        this.hostIdsIndependence = hostIdsIndependence;
    }

    public List<ClusterTree> getClusters() {
        return clusters;
    }

    public void setClusters(List<ClusterTree> clusters) {
        this.clusters = clusters;
    }

    public List<ClusterTree> getHosts() {
        return hosts;
    }

    public void setHosts(List<ClusterTree> hosts) {
        this.hosts = hosts;
    }

    @Override
    public String toString() {
        return "EsxiInstanceTree{" +
                "hostIds=" + hostIds +
                ", hostIdsOnCluster=" + hostIdsOnCluster +
                ", clusterIds=" + clusterIds +
                ", hostIdsIndependence=" + hostIdsIndependence +
                ", clusters=" + clusters +
                ", hosts=" + hosts +
                '}';
    }
}
