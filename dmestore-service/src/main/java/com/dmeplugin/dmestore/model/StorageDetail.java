package com.dmeplugin.dmestore.model;


/**
 * @author lianq
 * @className StorageDetail
 * @description TODO
 * @date 2020/9/4 10:11
 */
public class StorageDetail {

    private String id;
    private String name;
    private String ip;
    private String status;
    private String synStatus;
    private String sn;
    private String vendor;
    private String model;
    private String productVersion;
    private Double usedCapacity;
    private Double totalCapacity;
    private Double totalEffectiveCapacity;
    private Double freeEffectiveCapacity;
    private String location;
    private String[] azIds;
    private String storagePool;
    private String volume;
    private String fileSystem;
    private String dTrees;
    private String nfsShares;
    private String bandPorts;
    private String logicPorts;
    private String storageControllers;
    private String storageDisks;

    public String getStoragePool() {
        return storagePool;
    }

    public void setStoragePool(String storagePool) {
        this.storagePool = storagePool;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getFileSystem() {
        return fileSystem;
    }

    public void setFileSystem(String fileSystem) {
        this.fileSystem = fileSystem;
    }

    public String getdTrees() {
        return dTrees;
    }

    public void setdTrees(String dTrees) {
        this.dTrees = dTrees;
    }

    public String getNfsShares() {
        return nfsShares;
    }

    public void setNfsShares(String nfsShares) {
        this.nfsShares = nfsShares;
    }

    public String getBandPorts() {
        return bandPorts;
    }

    public void setBandPorts(String bandPorts) {
        this.bandPorts = bandPorts;
    }

    public String getLogicPorts() {
        return logicPorts;
    }

    public void setLogicPorts(String logicPorts) {
        this.logicPorts = logicPorts;
    }

    public String getStorageControllers() {
        return storageControllers;
    }

    public void setStorageControllers(String storageControllers) {
        this.storageControllers = storageControllers;
    }

    public String getStorageDisks() {
        return storageDisks;
    }

    public void setStorageDisks(String storageDisks) {
        this.storageDisks = storageDisks;
    }

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

    public String getSynStatus() {
        return synStatus;
    }

    public void setSynStatus(String synStatus) {
        this.synStatus = synStatus;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProductVersion() {
        return productVersion;
    }

    public void setProductVersion(String productVersion) {
        this.productVersion = productVersion;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String[] getAzIds() {
        return azIds;
    }

    public void setAzIds(String[] azIds) {
        this.azIds = azIds;
    }

    public Double getUsedCapacity() {
        return usedCapacity;
    }

    public void setUsedCapacity(Double usedCapacity) {
        this.usedCapacity = usedCapacity;
    }

    public Double getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Double totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Double getTotalEffectiveCapacity() {
        return totalEffectiveCapacity;
    }

    public void setTotalEffectiveCapacity(Double totalEffectiveCapacity) {
        this.totalEffectiveCapacity = totalEffectiveCapacity;
    }

    public Double getFreeEffectiveCapacity() {
        return freeEffectiveCapacity;
    }

    public void setFreeEffectiveCapacity(Double freeEffectiveCapacity) {
        this.freeEffectiveCapacity = freeEffectiveCapacity;
    }

}
