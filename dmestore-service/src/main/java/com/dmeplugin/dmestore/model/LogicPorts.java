package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @className LogicPort
 * @description TODO
 * @date 2020/9/7 15:40
 */
public class LogicPorts {
    private String id;
    private String name;
    private String runningStatus;
    private String operationalStatus;
    private String mgmtIp;
    private String mgmtIpv6;
    private String homePortId;
    private String homePortName;
    private String currentPortId;
    private String currentPortName;
    private String role;
    private String ddnsStatus;
    private String supportProtocol;
    private String managementAccess;
    private String vstoreId;
    private String vstoreName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(String runningStatus) {
        this.runningStatus = runningStatus;
    }

    public String getOperationalStatus() {
        return operationalStatus;
    }

    public void setOperationalStatus(String operationalStatus) {
        this.operationalStatus = operationalStatus;
    }

    public String getMgmtIp() {
        return mgmtIp;
    }

    public void setMgmtIp(String mgmtIp) {
        this.mgmtIp = mgmtIp;
    }

    public String getMgmtIpv6() {
        return mgmtIpv6;
    }

    public void setMgmtIpv6(String mgmtIpv6) {
        this.mgmtIpv6 = mgmtIpv6;
    }

    public String getHomePortId() {
        return homePortId;
    }

    public void setHomePortId(String homePortId) {
        this.homePortId = homePortId;
    }

    public String getHomePortName() {
        return homePortName;
    }

    public void setHomePortName(String homePortName) {
        this.homePortName = homePortName;
    }

    public String getCurrentPortId() {
        return currentPortId;
    }

    public void setCurrentPortId(String currentPortId) {
        this.currentPortId = currentPortId;
    }

    public String getCurrentPortName() {
        return currentPortName;
    }

    public void setCurrentPortName(String currentPortName) {
        this.currentPortName = currentPortName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDdnsStatus() {
        return ddnsStatus;
    }

    public void setDdnsStatus(String ddnsStatus) {
        this.ddnsStatus = ddnsStatus;
    }

    public String getSupportProtocol() {
        return supportProtocol;
    }

    public void setSupportProtocol(String supportProtocol) {
        this.supportProtocol = supportProtocol;
    }

    public String getManagementAccess() {
        return managementAccess;
    }

    public void setManagementAccess(String managementAccess) {
        this.managementAccess = managementAccess;
    }

    public String getVstoreId() {
        return vstoreId;
    }

    public void setVstoreId(String vstoreId) {
        this.vstoreId = vstoreId;
    }

    public String getVstoreName() {
        return vstoreName;
    }

    public void setVstoreName(String vstoreName) {
        this.vstoreName = vstoreName;
    }
}
