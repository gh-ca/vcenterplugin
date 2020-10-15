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
    private boolean dataDeduplication;
    private boolean dateCompression;
    private String controller;

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

    public boolean getDataDeduplication() {
        return dataDeduplication;
    }

    public void setDataDeduplication(boolean dataDeduplication) {
        this.dataDeduplication = dataDeduplication;
    }

    public boolean getDateCompression() {
        return dateCompression;
    }

    public void setDateCompression(boolean dateCompression) {
        this.dateCompression = dateCompression;
    }
}
