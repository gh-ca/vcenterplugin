package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @ClassName: FileSystem
 * @Company: GH-CA
 * @create 2020-09-03
 */
public class FileSystem  {

    //id
    private String id;
    //名称
    private String name;
    //状态
    private String healthStatus;
    //分配策略
    private String allocType;
    //容量使用率
    private Integer capacityUsageRatio;
    //存储池名字
    private String storagePoolName;
    //nfs
    private Integer nfsCount;
    //cifs
    private Integer cifsCount;
    //dtree
    private Integer dtreeCount;
    //总容量
    private Double capacity;
    //文件系统在存储池上已经分配的容量
    private Double allocateQuotaInPool;
    //可用空间
    private Double availableCapacity;
    //Thin文件系统能缩容的最小空间。单位GB，保留三位小数
    private Double minSizeFsCapacity;
    //存储设备Id
    private String storageId;

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

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getAllocType() {
        return allocType;
    }

    public void setAllocType(String allocType) {
        this.allocType = allocType;
    }

    public Integer getCapacityUsageRatio() {
        return capacityUsageRatio;
    }

    public void setCapacityUsageRatio(Integer capacityUsageRatio) {
        this.capacityUsageRatio = capacityUsageRatio;
    }

    public String getStoragePoolName() {
        return storagePoolName;
    }

    public void setStoragePoolName(String storagePoolName) {
        this.storagePoolName = storagePoolName;
    }

    public Integer getNfsCount() {
        return nfsCount;
    }

    public void setNfsCount(Integer nfsCount) {
        this.nfsCount = nfsCount;
    }

    public Integer getCifsCount() {
        return cifsCount;
    }

    public void setCifsCount(Integer cifsCount) {
        this.cifsCount = cifsCount;
    }

    public Integer getDtreeCount() {
        return dtreeCount;
    }

    public void setDtreeCount(Integer dtreeCount) {
        this.dtreeCount = dtreeCount;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public Double getAllocateQuotaInPool() {
        return allocateQuotaInPool;
    }

    public void setAllocateQuotaInPool(Double allocateQuotaInPool) {
        this.allocateQuotaInPool = allocateQuotaInPool;
    }

    public Double getAvailableCapacity() {
        return availableCapacity;
    }

    public void setAvailableCapacity(Double availableCapacity) {
        this.availableCapacity = availableCapacity;
    }

    public Double getMinSizeFsCapacity() {
        return minSizeFsCapacity;
    }

    public void setMinSizeFsCapacity(Double minSizeFsCapacity) {
        this.minSizeFsCapacity = minSizeFsCapacity;
    }

    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }
}
