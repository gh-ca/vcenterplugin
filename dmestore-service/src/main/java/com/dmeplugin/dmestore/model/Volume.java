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
    private String service_level_name;
    //存储设备id
    private String storage_id;
    //存储池id
    private String pool_raw_id;
    //容量利用率
    private String capacity_usage;
    //保护状态
    private Boolean protectionStatus;
    private List<String> hostIds;
    private List<String> hostGroupIds;
    //存储池名称
    private String storage_pool_name;
    //总容量 单位GB
    private Integer capacity;
    //关联的datastore
    private String datastores;
    //volume对应的instanceId
    private String instanceId;


    //todo 时延 iops 带宽 公用存储设备的？

    public String getDatastores() {
        return datastores;
    }

    public void setDatastores(String datastores) {
        this.datastores = datastores;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getStorage_pool_name() {
        return storage_pool_name;
    }

    public void setStorage_pool_name(String storage_pool_name) {
        this.storage_pool_name = storage_pool_name;
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

    public String getService_level_name() {
        return service_level_name;
    }

    public void setService_level_name(String service_level_name) {
        this.service_level_name = service_level_name;
    }

    public String getStorage_id() {
        return storage_id;
    }

    public void setStorage_id(String storage_id) {
        this.storage_id = storage_id;
    }

    public String getPool_raw_id() {
        return pool_raw_id;
    }

    public void setPool_raw_id(String pool_raw_id) {
        this.pool_raw_id = pool_raw_id;
    }

    public String getCapacity_usage() {
        return capacity_usage;
    }

    public void setCapacity_usage(String capacity_usage) {
        this.capacity_usage = capacity_usage;
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

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
