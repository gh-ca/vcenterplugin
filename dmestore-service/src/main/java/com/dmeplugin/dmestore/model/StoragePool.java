package com.dmeplugin.dmestore.model;

import java.util.PrimitiveIterator;

/**
 * @author lianq
 * @ClassName: StoragePool
 * @Company: GH-CA
 * @create 2020-09-03
 */
public class StoragePool   {

    private Double free_capacity;
    private String health_status;
    private Double lun_subscribed_capacity;
    private String name;
    private String parent_type;
    private String running_status;
    private Double total_capacity;
    private Double fs_subscribed_capacity;
    private Double consumed_capacity;
    private String consumed_capacity_percentage;
    private String consumed_capacity_threshold;
    private String storage_pool_id;

    public String getStorage_pool_id() {
        return storage_pool_id;
    }

    public void setStorage_pool_id(String storage_pool_id) {
        this.storage_pool_id = storage_pool_id;
    }

    public Double getFree_capacity() {
        return free_capacity;
    }

    public void setFree_capacity(Double free_capacity) {
        this.free_capacity = free_capacity;
    }

    public String getHealth_status() {
        return health_status;
    }

    public void setHealth_status(String health_status) {
        this.health_status = health_status;
    }

    public Double getLun_subscribed_capacity() {
        return lun_subscribed_capacity;
    }

    public void setLun_subscribed_capacity(Double lun_subscribed_capacity) {
        this.lun_subscribed_capacity = lun_subscribed_capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent_type() {
        return parent_type;
    }

    public void setParent_type(String parent_type) {
        this.parent_type = parent_type;
    }

    public String getRunning_status() {
        return running_status;
    }

    public void setRunning_status(String running_status) {
        this.running_status = running_status;
    }

    public Double getTotal_capacity() {
        return total_capacity;
    }

    public void setTotal_capacity(Double total_capacity) {
        this.total_capacity = total_capacity;
    }

    public Double getFs_subscribed_capacity() {
        return fs_subscribed_capacity;
    }

    public void setFs_subscribed_capacity(Double fs_subscribed_capacity) {
        this.fs_subscribed_capacity = fs_subscribed_capacity;
    }

    public Double getConsumed_capacity() {
        return consumed_capacity;
    }

    public void setConsumed_capacity(Double consumed_capacity) {
        this.consumed_capacity = consumed_capacity;
    }

    public String getConsumed_capacity_percentage() {
        return consumed_capacity_percentage;
    }

    public void setConsumed_capacity_percentage(String consumed_capacity_percentage) {
        this.consumed_capacity_percentage = consumed_capacity_percentage;
    }

    public String getConsumed_capacity_threshold() {
        return consumed_capacity_threshold;
    }

    public void setConsumed_capacity_threshold(String consumed_capacity_threshold) {
        this.consumed_capacity_threshold = consumed_capacity_threshold;
    }
}
