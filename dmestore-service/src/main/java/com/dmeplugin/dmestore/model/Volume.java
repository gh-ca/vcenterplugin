package com.dmeplugin.dmestore.model;

import java.util.List;

/**
 * @author lianq
 * @ClassName: Volume
 * @Company: GH-CA
 * @create 2020-09-03
 */
public class Volume  {

    private String id;
    private String name;
    private String status;
    private Boolean attached;
    private String alloctype;
    private String service_level_name;
    private String storage_id;
    private String pool_raw_id;
    private String capacity_usage;
    private Boolean protectionStatus;
    private List<String> hostIds;
    private List<String> hostGroupIds;

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
}
