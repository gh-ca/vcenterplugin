package com.huawei.dmestore.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yc
 * @Title:  ClusterTree
 * @Description: be used encapsulation cluster tree
 * @date 2021/5/1114:15
 */
public class ClusterTree implements Serializable {
    private static final long serialVersionUID = 6756705875820791027L;
    private String clusterId;
    private String clusterName;
    private boolean flag = true;
    private List<ClusterTree> children = new ArrayList<>();

    public ClusterTree(String clusterId, String clusterName, List<ClusterTree> children) {
        this.clusterId = clusterId;
        this.clusterName = clusterName;
        this.children = children;
    }

    public ClusterTree() {

    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public List<ClusterTree> getChildren() {
        return children;
    }

    public void setChildren(List<ClusterTree> children) {
        this.children = children;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }


    @Override
    public String toString() {
        return "ClusterTree{" +
                "clusterId='" + clusterId + '\'' +
                ", clusterName='" + clusterName + '\'' +
                ", flag=" + flag +
                ", children=" + children +
                '}';
    }
}
