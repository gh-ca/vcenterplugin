package com.dmeplugin.dmestore.model;

/**
 * @Author Administrator
 * @Description vmfs Datastore劵详情
 * @Date 16:01 2020/9/2
 * @Param
 * @Return
 **/
public class VmfsDatastoreVolumeDetail {
    private String name;
    private String wwn;
    private String smartTier;
    private String evolutionaryInfo;
    private String device;
    private String storagePool;
    private String serviceLevel;
    private String storage;
    private boolean dudeplication;
    private String provisionType;
    private boolean compression;
    private String applicationType;
    private String controlPolicy;
    private String trafficControl;


    public boolean getCompression() {
        return compression;
    }

    public void setCompression(boolean compression) {
        this.compression = compression;
    }

    public String getProvisionType() {
        return provisionType;
    }

    public void setProvisionType(String provisionType) {
        this.provisionType = provisionType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWwn() {
        return wwn;
    }

    public void setWwn(String wwn) {
        this.wwn = wwn;
    }

    public String getSmartTier() {
        return smartTier;
    }

    public void setSmartTier(String smartTier) {
        this.smartTier = smartTier;
    }

    public String getEvolutionaryInfo() {
        return evolutionaryInfo;
    }

    public void setEvolutionaryInfo(String evolutionaryInfo) {
        this.evolutionaryInfo = evolutionaryInfo;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getStoragePool() {
        return storagePool;
    }

    public void setStoragePool(String storagePool) {
        this.storagePool = storagePool;
    }

    public String getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(String serviceLevel) {
        this.serviceLevel = serviceLevel;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public boolean getDudeplication() {
        return dudeplication;
    }

    public void setDudeplication(boolean dudeplication) {
        this.dudeplication = dudeplication;
    }

    public boolean isDudeplication() {
        return dudeplication;
    }

    public boolean isCompression() {
        return compression;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public String getControlPolicy() {
        return controlPolicy;
    }

    public void setControlPolicy(String controlPolicy) {
        this.controlPolicy = controlPolicy;
    }

    public String getTrafficControl() {
        return trafficControl;
    }

    public void setTrafficControl(String trafficControl) {
        this.trafficControl = trafficControl;
    }
}
