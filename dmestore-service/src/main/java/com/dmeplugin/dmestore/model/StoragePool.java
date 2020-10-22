package com.dmeplugin.dmestore.model;


/**
 * @author lianq
 * @ClassName: StoragePool
 * @Company: GH-CA
 * @create 2020-09-03
 */
public class StoragePool   {
    /**
     *空闲容量
    **/
    private Double freeCapacity;
    /**
     *名称
    **/
    private String name;
    /**
     *存储池id
    **/
    private String id;
    /**
     *运行状态
    **/
    private String runningStatus;
    /**
     *健康状态
    **/
    private String healthStatus;
    /**
     *总容量
    **/
    private Double totalCapacity;
    /**
     *已用容量
    **/
    private Double consumedCapacity;
    /**
     *已用容量百分比(容量利用率)
    **/
    private String consumedCapacityPercentage;
    private String storagePoolId;
    private String storageInstanceId;
    private String storageDeviceId;
    /**
     *订阅率 = 订阅容量/总容量
    **/
    private Double subscriptionRate;
    /**
     *类型（块）file 或者 block
    **/
    private String mediaType;
    /**
     *0-无效，1-RAID 10，2-RAID 5，3-RAID 0，4-RAID 1，5-RAID 6，6-RAID 50，7-RAID 3。
     /**
     *RAID级别
    **/
    private String tier0RaidLv;
    private String tier1RaidLv;
    private String tier2RaidLv;
    /**
     *存储设备id
    **/
    private String storageId;
    /**
     *存储池上创建LUN或者文件系统时的可用容量 单位MB
    **/
    private Double dataSpace;
    /**
     *时延 iops 带宽 公用存储设备的
     *订阅容量
    **/
    private Double subscribedCapacity;
    /**
     *去重容量
     **/
    private Double dedupedCapacity;
    /**
     *压缩容量
     **/
    private Double compressedCapacity;
    /**
     *保护容量
     **/
    private Double protectionCapacity;
    /**
     *硬盘类型
    **/
    private String physicalType;
    /**
     *存储池所处硬盘id
    **/
    private String diskPoolId;

    /**
     *存储池所在存储中的ID
     **/
    private String poolId;
    /**
     *服务等级
     **/
    private String serviceLevelName;

    /**
     *iops
     **/
    private Float maxIops;

    /**
     *带宽
     **/
    private Float maxBandwidth;

    /**
     *时延
     **/
    private Float maxLatency;

    public Double getFreeCapacity() {
        return freeCapacity;
    }

    public void setFreeCapacity(Double freeCapacity) {
        this.freeCapacity = freeCapacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(String runningStatus) {
        this.runningStatus = runningStatus;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public Double getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Double totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Double getConsumedCapacity() {
        return consumedCapacity;
    }

    public void setConsumedCapacity(Double consumedCapacity) {
        this.consumedCapacity = consumedCapacity;
    }

    public String getConsumedCapacityPercentage() {
        return consumedCapacityPercentage;
    }

    public void setConsumedCapacityPercentage(String consumedCapacityPercentage) {
        this.consumedCapacityPercentage = consumedCapacityPercentage;
    }

    public String getStoragePoolId() {
        return storagePoolId;
    }

    public void setStoragePoolId(String storagePoolId) {
        this.storagePoolId = storagePoolId;
    }

    public String getStorageInstanceId() {
        return storageInstanceId;
    }

    public void setStorageInstanceId(String storageInstanceId) {
        this.storageInstanceId = storageInstanceId;
    }

    public String getStorageDeviceId() {
        return storageDeviceId;
    }

    public void setStorageDeviceId(String storageDeviceId) {
        this.storageDeviceId = storageDeviceId;
    }

    public Double getSubscriptionRate() {
        return subscriptionRate;
    }

    public void setSubscriptionRate(Double subscriptionRate) {
        this.subscriptionRate = subscriptionRate;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getTier0RaidLv() {
        return tier0RaidLv;
    }

    public void setTier0RaidLv(String tier0RaidLv) {
        this.tier0RaidLv = tier0RaidLv;
    }

    public String getTier1RaidLv() {
        return tier1RaidLv;
    }

    public void setTier1RaidLv(String tier1RaidLv) {
        this.tier1RaidLv = tier1RaidLv;
    }

    public String getTier2RaidLv() {
        return tier2RaidLv;
    }

    public void setTier2RaidLv(String tier2RaidLv) {
        this.tier2RaidLv = tier2RaidLv;
    }

    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }

    public Double getDataSpace() {
        return dataSpace;
    }

    public void setDataSpace(Double dataSpace) {
        this.dataSpace = dataSpace;
    }

    public Double getSubscribedCapacity() {
        return subscribedCapacity;
    }

    public void setSubscribedCapacity(Double subscribedCapacity) {
        this.subscribedCapacity = subscribedCapacity;
    }

    public String getPhysicalType() {
        return physicalType;
    }

    public void setPhysicalType(String physicalType) {
        this.physicalType = physicalType;
    }

    public String getDiskPoolId() {
        return diskPoolId;
    }

    public void setDiskPoolId(String diskPoolId) {
        this.diskPoolId = diskPoolId;
    }

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

    public String getServiceLevelName() {
        return serviceLevelName;
    }

    public void setServiceLevelName(String serviceLevelName) {
        this.serviceLevelName = serviceLevelName;
    }

    public Float getMaxIops() {
        return maxIops;
    }

    public void setMaxIops(Float maxIops) {
        this.maxIops = maxIops;
    }

    public Float getMaxBandwidth() {
        return maxBandwidth;
    }

    public void setMaxBandwidth(Float maxBandwidth) {
        this.maxBandwidth = maxBandwidth;
    }

    public Float getMaxLatency() {
        return maxLatency;
    }

    public void setMaxLatency(Float maxLatency) {
        this.maxLatency = maxLatency;
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

    public Double getProtectionCapacity() {
        return protectionCapacity;
    }

    public void setProtectionCapacity(Double protectionCapacity) {
        this.protectionCapacity = protectionCapacity;
    }
}
