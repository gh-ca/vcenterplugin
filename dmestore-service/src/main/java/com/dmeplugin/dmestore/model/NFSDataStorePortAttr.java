package com.dmeplugin.dmestore.model;

/**
 * @ClassName NFSDataStorePortAttr
 * @Description NFS DataStore端口属性
 * @Author wangxiangyong
 * @Date 2020/9/2 16:56
 * @Version V1.0
 **/
public class NFSDataStorePortAttr {
    //名称、IP、状态、运行状态、Active port、Current Port、Failover Group
    private String name;
    private String ip;
    private String status;
    private String runningStatus;
    private String activePort;
    private String currentPort;
    private String failoverGroup;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(String runningStatus) {
        this.runningStatus = runningStatus;
    }

    public String getActivePort() {
        return activePort;
    }

    public void setActivePort(String activePort) {
        this.activePort = activePort;
    }

    public String getCurrentPort() {
        return currentPort;
    }

    public void setCurrentPort(String currentPort) {
        this.currentPort = currentPort;
    }

    public String getFailoverGroup() {
        return failoverGroup;
    }

    public void setFailoverGroup(String failoverGroup) {
        this.failoverGroup = failoverGroup;
    }
}
