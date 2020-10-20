package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @className DiskPool
 * @description TODO
 * @date 2020/10/19 18:48
 */
public class DiskPool {

    private String id;
    //原始id
    private String nativeId;
    //上次修改时间
    private String lastModified;
    //最后监控时间
    private String lastMonitorTime;
    //监控状态
    private String dataStatus;
    private String name;
    //状态
    private String status;
    //运行状态
    private String runningStatus;
    //加密类型
    private String encryptDiskType;
    //总容量
    private Double totalCapacity;
    //已用容量
    private Double usedCapacity;
    //空闲容量
    private Double freeCapacity;
    //热备容量
    private Double spareCapacity;
    //已用热备容量
    private Double usedSpareCapacity;
    //在设备上的硬盘域Id
    private String poolId;
    private String storageDeviceId;
    /**
     * 使用率
     */
    private Double usageRate;

    public Double getUsageRate() {
        return usageRate;
    }

    public void setUsageRate(Double usageRate) {
        this.usageRate = usageRate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNativeId() {
        return nativeId;
    }

    public void setNativeId(String nativeId) {
        this.nativeId = nativeId;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastMonitorTime() {
        return lastMonitorTime;
    }

    public void setLastMonitorTime(String lastMonitorTime) {
        this.lastMonitorTime = lastMonitorTime;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEncryptDiskType() {
        return encryptDiskType;
    }

    public void setEncryptDiskType(String encryptDiskType) {
        this.encryptDiskType = encryptDiskType;
    }

    public Double getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Double totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Double getUsedCapacity() {
        return usedCapacity;
    }

    public void setUsedCapacity(Double usedCapacity) {
        this.usedCapacity = usedCapacity;
    }

    public Double getFreeCapacity() {
        return freeCapacity;
    }

    public void setFreeCapacity(Double freeCapacity) {
        this.freeCapacity = freeCapacity;
    }

    public Double getSpareCapacity() {
        return spareCapacity;
    }

    public void setSpareCapacity(Double spareCapacity) {
        this.spareCapacity = spareCapacity;
    }

    public Double getUsedSpareCapacity() {
        return usedSpareCapacity;
    }

    public void setUsedSpareCapacity(Double usedSpareCapacity) {
        this.usedSpareCapacity = usedSpareCapacity;
    }

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

    public String getStorageDeviceId() {
        return storageDeviceId;
    }

    public void setStorageDeviceId(String storageDeviceId) {
        this.storageDeviceId = storageDeviceId;
    }
}
