package com.dmeplugin.dmestore.model;


import java.util.Arrays;

/**
 * @author lianq
 * @className StorageDetail
 * @description TODO
 * @date 2020/9/4 10:11
 */
public class StorageDetail {

    private String id;
    //名称
    private String name;
    //ip地址
    private String ip;
    //状态 运行状态 0-离线 1-正常 2-故障 9-未管理。
    private String status;
    //同步状态 同步状态 0-未同步 1-同步中 2-同步完成。
    private String synStatus;
    //设备序列号。
    private String sn;
    //厂商
    private String vendor;
    //产品型号
    private String model;
    //已用容量 （单位:MB）
    private Double usedCapacity;
    //裸容量（单位:MB）。
    private Double totalCapacity;
    //可得容量 （总容量）
    private Double totalEffectiveCapacity;
    //空闲容量
    private Double freeEffectiveCapacity;

    //订阅容量
    private Double subscriptionCapacity;

    //容量分布
    //保护容量
    private Double protectionCapacity;
    //文件系统
    private Double fileCapacity;
    //卷
    private Double blockCapacity;

    /**
     *去重容量
     **/
    private Double dedupedCapacity;
    /**
     *压缩容量
     **/
    private Double compressedCapacity;

    /**
     *节省后容量
     **/
    private Double optimizeCapacity;

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
    //固件版本
    private String productVersion;
    //警告
    private String warning;
    //事件
    private String event;

    /**
     *位置
     **/
    private String location;
    /**
     *补丁版本
     **/
    private String patchVersion;
    /**
     *维保开始时间
     **/
    private String maintenanceStart;
    /**
     *维保结束时间
     **/
    private String maintenanceOvertime;

    public String getPatchVersion() {
        return patchVersion;
    }

    public void setPatchVersion(String patchVersion) {
        this.patchVersion = patchVersion;
    }

    public String getMaintenanceStart() {
        return maintenanceStart;
    }

    public void setMaintenanceStart(String maintenanceStart) {
        this.maintenanceStart = maintenanceStart;
    }

    public String getMaintenanceOvertime() {
        return maintenanceOvertime;
    }

    public void setMaintenanceOvertime(String maintenanceOvertime) {
        this.maintenanceOvertime = maintenanceOvertime;
    }

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


    public Double getSubscriptionCapacity() {
        return subscriptionCapacity;
    }

    public void setSubscriptionCapacity(Double subscriptionCapacity) {
        this.subscriptionCapacity = subscriptionCapacity;
    }

    public Double getProtectionCapacity() {
        return protectionCapacity;
    }

    public void setProtectionCapacity(Double protectionCapacity) {
        this.protectionCapacity = protectionCapacity;
    }

    public Double getFileCapacity() {
        return fileCapacity;
    }

    public void setFileCapacity(Double fileCapacity) {
        this.fileCapacity = fileCapacity;
    }

    public Double getBlockCapacity() {
        return blockCapacity;
    }

    public void setBlockCapacity(Double blockCapacity) {
        this.blockCapacity = blockCapacity;
    }

    public Double getDedupedCapacity() {
        return dedupedCapacity;
    }

    public void setDedupedCapacity(Double dedupedCapacity) {
        this.dedupedCapacity = dedupedCapacity;
    }

    public Double getCompressedCapacity() {
        return compressedCapacity;
    }

    public void setCompressedCapacity(Double compressedCapacity) {
        this.compressedCapacity = compressedCapacity;
    }

    public Double getOptimizeCapacity() {
        return optimizeCapacity;
    }

    public void setOptimizeCapacity(Double optimizeCapacity) {
        this.optimizeCapacity = optimizeCapacity;
    }

    @Override
    public String toString() {
        return "StorageDetail{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", ip='" + ip + '\'' +
            ", status='" + status + '\'' +
            ", synStatus='" + synStatus + '\'' +
            ", sn='" + sn + '\'' +
            ", vendor='" + vendor + '\'' +
            ", model='" + model + '\'' +
            ", usedCapacity=" + usedCapacity +
            ", totalCapacity=" + totalCapacity +
            ", totalEffectiveCapacity=" + totalEffectiveCapacity +
            ", freeEffectiveCapacity=" + freeEffectiveCapacity +
            ", subscriptionCapacity=" + subscriptionCapacity +
            ", protectionCapacity=" + protectionCapacity +
            ", fileCapacity=" + fileCapacity +
            ", blockCapacity=" + blockCapacity +
            ", dedupedCapacity=" + dedupedCapacity +
            ", compressedCapacity=" + compressedCapacity +
            ", optimizeCapacity=" + optimizeCapacity +
            ", azIds=" + Arrays.toString(azIds) +
            ", storagePool='" + storagePool + '\'' +
            ", volume='" + volume + '\'' +
            ", fileSystem='" + fileSystem + '\'' +
            ", dTrees='" + dTrees + '\'' +
            ", nfsShares='" + nfsShares + '\'' +
            ", bandPorts='" + bandPorts + '\'' +
            ", logicPorts='" + logicPorts + '\'' +
            ", storageControllers='" + storageControllers + '\'' +
            ", storageDisks='" + storageDisks + '\'' +
            ", productVersion='" + productVersion + '\'' +
            ", warning='" + warning + '\'' +
            ", event='" + event + '\'' +
            ", location='" + location + '\'' +
            ", patchVersion='" + patchVersion + '\'' +
            ", maintenanceStart='" + maintenanceStart + '\'' +
            ", maintenanceOvertime='" + maintenanceOvertime + '\'' +
            '}';
    }
}
