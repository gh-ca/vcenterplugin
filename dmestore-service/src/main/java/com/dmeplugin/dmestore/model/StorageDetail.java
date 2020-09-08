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
    private Object storagePool;
    private Object volume;
    private Object fileSystem;
    private Object dTrees;
    private Object nfsShares;
    private Object bandPorts;
    private Object logicPorts;
    private Object storageControllers;
    private Object storageDisks;

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

    public Object getStoragePool() {
        return storagePool;
    }

    public void setStoragePool(Object storagePool) {
        this.storagePool = storagePool;
    }

    public Object getVolume() {
        return volume;
    }

    public void setVolume(Object volume) {
        this.volume = volume;
    }

    public Object getFileSystem() {
        return fileSystem;
    }

    public void setFileSystem(Object fileSystem) {
        this.fileSystem = fileSystem;
    }

    public Object getdTrees() {
        return dTrees;
    }

    public void setdTrees(Object dTrees) {
        this.dTrees = dTrees;
    }

    public Object getNfsShares() {
        return nfsShares;
    }

    public void setNfsShares(Object nfsShares) {
        this.nfsShares = nfsShares;
    }

    public Object getBandPorts() {
        return bandPorts;
    }

    public void setBandPorts(Object bandPorts) {
        this.bandPorts = bandPorts;
    }

    public Object getLogicPorts() {
        return logicPorts;
    }

    public void setLogicPorts(Object logicPorts) {
        this.logicPorts = logicPorts;
    }

    public Object getStorageControllers() {
        return storageControllers;
    }

    public void setStorageControllers(Object storageControllers) {
        this.storageControllers = storageControllers;
    }

    public Object getStorageDisks() {
        return storageDisks;
    }

    public void setStorageDisks(Object storageDisks) {
        this.storageDisks = storageDisks;
    }
}
