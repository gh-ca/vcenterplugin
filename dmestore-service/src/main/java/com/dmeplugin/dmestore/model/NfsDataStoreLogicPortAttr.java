package com.dmeplugin.dmestore.model;

import java.io.Serializable;

/**
 * @author wangxiangyong
 **/
    public class NfsDataStoreLogicPortAttr implements Serializable{
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
