package com.dmeplugin.dmestore.model;

import java.util.List;

/**
 * @author lianq
 * @ClassName: Volume
 * @Company: GH-CA
 * @create 2020-09-03
 */
public class Volume {
    //卷的唯一标识
    private String id;
    //名称
    private String name;
    //状态
    private String status;
    private Boolean attached;
    //分配类型
    private String alloctype;
    //服务等级
    private String serviceLevelName;
    //存储设备id
    private String storageId;
    //存储池id
    private String poolRawId;
    //容量利用率
    private String capacityUsage;
    //保护状态
    private Boolean protectionStatus;
    private List<String> hostIds;
    private List<String> hostGroupIds;
    //存储池名称
    private String storagePoolName;
    //总容量 单位GB
    private Integer capacity;
    //关联的datastore
    private String datastores;
    //volume对应的instanceId
    private String instanceId;

    private Float iops;
    private Float lantency;
    private Float bandwith;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getAttached() {
        return attached;
    }

    public void setAttached(Boolean attached) {
        this.attached = attached;
    }

    public String getAlloctype() {
        return alloctype;
    }

    public void setAlloctype(String alloctype) {
        this.alloctype = alloctype;
    }

    public String getServiceLevelName() {
        return serviceLevelName;
    }

    public void setServiceLevelName(String serviceLevelName) {
        this.serviceLevelName = serviceLevelName;
    }

    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }

    public String getPoolRawId() {
        return poolRawId;
    }

    public void setPoolRawId(String poolRawId) {
        this.poolRawId = poolRawId;
    }

    public String getCapacityUsage() {
        return capacityUsage;
    }

    public void setCapacityUsage(String capacityUsage) {
        this.capacityUsage = capacityUsage;
    }

    public Boolean getProtectionStatus() {
        return protectionStatus;
    }

    public void setProtectionStatus(Boolean protectionStatus) {
        this.protectionStatus = protectionStatus;
    }

    public List<String> getHostIds() {
        return hostIds;
    }

    public void setHostIds(List<String> hostIds) {
        this.hostIds = hostIds;
    }

    public List<String> getHostGroupIds() {
        return hostGroupIds;
    }

    public void setHostGroupIds(List<String> hostGroupIds) {
        this.hostGroupIds = hostGroupIds;
    }

    public String getStoragePoolName() {
        return storagePoolName;
    }

    public void setStoragePoolName(String storagePoolName) {
        this.storagePoolName = storagePoolName;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDatastores() {
        return datastores;
    }

    public void setDatastores(String datastores) {
        this.datastores = datastores;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Float getIops() {
        return iops;
    }

    public void setIops(Float iops) {
        this.iops = iops;
    }

    public Float getLantency() {
        return lantency;
    }

    public void setLantency(Float lantency) {
        this.lantency = lantency;
    }

    public Float getBandwith() {
        return bandwith;
    }

    public void setBandwith(Float bandwith) {
        this.bandwith = bandwith;
    }
}
