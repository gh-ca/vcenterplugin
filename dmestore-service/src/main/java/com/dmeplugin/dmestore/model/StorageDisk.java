package com.dmeplugin.dmestore.model;

import java.util.List;

/**
 * @author lianq
 * @className StorageDisk
 * @description TODO
 * @date 2020/9/7 19:33
 */
public class StorageDisk {

    private String name;
    private String status;
    private Double capacity;
    private String poolId;
    private String storageDeviceId;
    //硬盘域
    private List<DiskPool> diskPools;
    //补充字段
    /**
     * 类型
     */
    private String physicalType;
    /**
     * 角色
     */
    private String logicalType;
    /**
     * 磁盘域
     */
    private String diskDomain;
    /**
     * 转速
     */
    private Long speed;

    /**
     * 性能
     */
    private String performance;


    public List<DiskPool> getDiskPools() {
        return diskPools;
    }

    public void setDiskPools(List<DiskPool> diskPools) {
        this.diskPools = diskPools;
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

    public String getLogicalType() {
        return logicalType;
    }

    public void setLogicalType(String logicalType) {
        this.logicalType = logicalType;
    }

    public String getDiskDomain() {
        return diskDomain;
    }

    public void setDiskDomain(String diskDomain) {
        this.diskDomain = diskDomain;
    }

    public Long getSpeed() {
        return speed;
    }

    public void setSpeed(Long speed) {
        this.speed = speed;
    }

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public String getPhysicalType() {
        return physicalType;
    }

    public void setPhysicalType(String physicalType) {
        this.physicalType = physicalType;
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

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }
}
