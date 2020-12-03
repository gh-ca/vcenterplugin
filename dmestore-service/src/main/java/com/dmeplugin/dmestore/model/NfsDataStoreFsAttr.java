package com.dmeplugin.dmestore.model;

import java.io.Serializable;

/**
 * @author wangxiangyong
 **/
public class NfsDataStoreFsAttr implements Serializable {
    private String name;
    private String description;
    private String device;
    private String storagePoolName;
    private String provisionType;
    private String applicationScenario;
    private Boolean dataDeduplication;
    private Boolean dateCompression;
    private String controller;
    private String fileSystemId;

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getStoragePoolName() {
        return storagePoolName;
    }

    public void setStoragePoolName(String storagePoolName) {
        this.storagePoolName = storagePoolName;
    }

    public String getProvisionType() {
        return provisionType;
    }

    public void setProvisionType(String provisionType) {
        this.provisionType = provisionType;
    }

    public String getApplicationScenario() {
        return applicationScenario;
    }

    public void setApplicationScenario(String applicationScenario) {
        this.applicationScenario = applicationScenario;
    }

    public Boolean getDataDeduplication() {
        return dataDeduplication;
    }

    public void setDataDeduplication(Boolean dataDeduplication) {
        this.dataDeduplication = dataDeduplication;
    }

    public Boolean getDateCompression() {
        return dateCompression;
    }

    public void setDateCompression(Boolean dateCompression) {
        this.dateCompression = dateCompression;
    }

    public String getFileSystemId() {
        return fileSystemId;
    }

    public void setFileSystemId(String fileSystemId) {
        this.fileSystemId = fileSystemId;
    }

    @Override
    public String toString() {
        return "NfsDataStoreFsAttr{" +
            "name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", device='" + device + '\'' +
            ", storagePoolName='" + storagePoolName + '\'' +
            ", provisionType='" + provisionType + '\'' +
            ", applicationScenario='" + applicationScenario + '\'' +
            ", dataDeduplication=" + dataDeduplication +
            ", dateCompression=" + dateCompression +
            ", controller='" + controller + '\'' +
            ", fileSystemId='" + fileSystemId + '\'' +
            '}';
    }
}
