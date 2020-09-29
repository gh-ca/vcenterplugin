package com.dmeplugin.dmestore.model;


/**
 * @author lianq
 * @className StorageDetail
 * @description TODO
 * @date 2020/9/4 10:11
 */
public class StorageDetail {

    private String id;
    private String name;//名称
    private String ip;//ip地址
    private String status;//状态 运行状态 0-离线 1-正常 2-故障 9-未管理。
    private String synStatus;//同步状态 同步状态 0-未同步 1-同步中 2-同步完成。
    private String sn;//设备序列号。
    private String vendor;//厂商
    private String model;//产品型号
    private Double usedCapacity;//已用容量 （单位:MB）
    private Double totalCapacity; //裸容量（单位:MB）。
    private Double totalEffectiveCapacity;//可得容量 （总容量）
    private Double freeEffectiveCapacity;//空闲容量
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

    //补充字段
    private String productVersion;//固件版本
    private Integer maintenance_start; //维护开始时间
    private Integer maintenance_overtime;//维护结束时间
    private String patch_version;//补订版本
    private String warning; //警告
    private String event;//事件

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getPatch_version() {
        return patch_version;
    }

    public void setPatch_version(String patch_version) {
        this.patch_version = patch_version;
    }

    public Integer getMaintenance_start() {
        return maintenance_start;
    }

    public void setMaintenance_start(Integer maintenance_start) {
        this.maintenance_start = maintenance_start;
    }

    public Integer getMaintenance_overtime() {
        return maintenance_overtime;
    }

    public void setMaintenance_overtime(Integer maintenance_overtime) {
        this.maintenance_overtime = maintenance_overtime;
    }

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
