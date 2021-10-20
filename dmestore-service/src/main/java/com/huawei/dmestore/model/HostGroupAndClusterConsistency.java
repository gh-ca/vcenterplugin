package com.huawei.dmestore.model;

/**
 * @author yc
 * @Title:
 * @Description:
 * @date 2021/5/3111:23
 */
public class HostGroupAndClusterConsistency {
    private boolean consistency; //true 一致； false 不一致
    private String hostGroupId; //主机组id

    public HostGroupAndClusterConsistency() { }

    public HostGroupAndClusterConsistency(boolean consistency) {
        this.consistency = consistency;
    }

    public HostGroupAndClusterConsistency(String hostGroupId) {
        this.hostGroupId = hostGroupId;
    }

    public boolean isConsistency() {
        return consistency;
    }

    public void setConsistency(boolean consistency) {
        this.consistency = consistency;
    }

    public String getHostGroupId() {
        return hostGroupId;
    }

    public void setHostGroupId(String hostGroupId) {
        this.hostGroupId = hostGroupId;
    }

    public HostGroupAndClusterConsistency(boolean consistency, String hostGroupId) {
        this.consistency = consistency;
        this.hostGroupId = hostGroupId;
    }
}
