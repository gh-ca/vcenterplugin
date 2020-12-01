package com.dmeplugin.dmestore.model;

import java.util.List;

/**
 * @author wangxiangyong
 **/
public class NfsDataStoreShareAttr {
    private String fsName;

    private String name;

    private String sharePath;

    private String description;

    private String owningDtreeId;

    private String owningDtreeName;

    private String deviceName;

    private List<AuthClient> authClientList;

    public String getFsName() {
        return fsName;
    }

    public void setFsName(String fsName) {
        this.fsName = fsName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSharePath() {
        return sharePath;
    }

    public void setSharePath(String sharePath) {
        this.sharePath = sharePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwningDtreeId() {
        return owningDtreeId;
    }

    public void setOwningDtreeId(String owningDtreeId) {
        this.owningDtreeId = owningDtreeId;
    }

    public String getOwningDtreeName() {
        return owningDtreeName;
    }

    public void setOwningDtreeName(String owningDtreeName) {
        this.owningDtreeName = owningDtreeName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public List<AuthClient> getAuthClientList() {
        return authClientList;
    }

    public void setAuthClientList(List<AuthClient> authClientList) {
        this.authClientList = authClientList;
    }

    @Override
    public String toString() {
        return "NfsDataStoreShareAttr{" + "fsName='" + fsName + '\'' + ", name='" + name + '\'' + ", sharePath='"
            + sharePath + '\'' + ", description='" + description + '\'' + ", owningDtreeId='" + owningDtreeId + '\''
            + ", owningDtreeName='" + owningDtreeName + '\'' + ", deviceName='" + deviceName + '\''
            + ", authClientList=" + authClientList + '}';
    }
}
