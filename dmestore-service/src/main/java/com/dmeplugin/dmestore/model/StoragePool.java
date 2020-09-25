package com.dmeplugin.dmestore.model;


/**
 * @author lianq
 * @ClassName: StoragePool
 * @Company: GH-CA
 * @create 2020-09-03
 */
public class StoragePool   {

    private Double free_capacity;//空闲容量
    private Double lun_subscribed_capacity; //LUN的配置容量（LUN订阅容量）
    private String name;//名称
    private String parent_type;
    private String running_status;//运行状态
    private String health_status;//健康状态
    private Double total_capacity;//总容量
    private Double fs_subscribed_capacity; //订阅容量(文件系统订阅容量)
    private Double consumed_capacity;//已用容量
    private String consumed_capacity_percentage;//已用容量百分比(容量利用率)
    private String consumed_capacity_threshold;
    private String storage_pool_id;
    private String storage_instance_id;
    private String storage_device_id;
    private String storage_name;
    //订阅率 = 订阅容量/总容量
    private Double lun_subscription_rate;
    private Double fs_subscription_rate;
    //补充字段
    private String media_type;//类型（块）
    private String tier0_disk_type;//硬盘类型
    private String tier0_raid_lv; //RAID级别
    private String tier1_disk_type;//硬盘类型
    private String tier1_raid_lv;//RAID级别
    private String tier2_disk_type;//硬盘类型
    private String tier2_raid_lv;//RAID级别
    private String storage_id; //存储设备id
    private Double data_space; //存储池上创建LUN或者文件系统时的可用容量 单位MB
    //时延 iops 带宽 公用存储设备的

    public Double getLun_subscription_rate() {
        return lun_subscription_rate;
    }

    public void setLun_subscription_rate(Double lun_subscription_rate) {
        this.lun_subscription_rate = lun_subscription_rate;
    }

    public Double getFs_subscription_rate() {
        return fs_subscription_rate;
    }

    public void setFs_subscription_rate(Double fs_subscription_rate) {
        this.fs_subscription_rate = fs_subscription_rate;
    }

    public Double getData_space() {
        return data_space;
    }

    public void setData_space(Double data_space) {
        this.data_space = data_space;
    }

    public String getTier0_raid_lv() {
        return tier0_raid_lv;
    }

    public void setTier0_raid_lv(String tier0_raid_lv) {
        this.tier0_raid_lv = tier0_raid_lv;
    }

    public String getTier1_raid_lv() {
        return tier1_raid_lv;
    }

    public void setTier1_raid_lv(String tier1_raid_lv) {
        this.tier1_raid_lv = tier1_raid_lv;
    }

    public String getTier2_raid_lv() {
        return tier2_raid_lv;
    }

    public void setTier2_raid_lv(String tier2_raid_lv) {
        this.tier2_raid_lv = tier2_raid_lv;
    }

    public String getStorage_id() {
        return storage_id;
    }

    public void setStorage_id(String storage_id) {
        this.storage_id = storage_id;
    }

    public String getTier0_disk_type() {
        return tier0_disk_type;
    }

    public void setTier0_disk_type(String tier0_disk_type) {
        this.tier0_disk_type = tier0_disk_type;
    }

    public String getTier1_disk_type() {
        return tier1_disk_type;
    }

    public void setTier1_disk_type(String tier1_disk_type) {
        this.tier1_disk_type = tier1_disk_type;
    }

    public String getTier2_disk_type() {
        return tier2_disk_type;
    }

    public void setTier2_disk_type(String tier2_disk_type) {
        this.tier2_disk_type = tier2_disk_type;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

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

    public String getStorage_instance_id() {
        return storage_instance_id;
    }

    public void setStorage_instance_id(String storage_instance_id) {
        this.storage_instance_id = storage_instance_id;
    }

    public String getStorage_device_id() {
        return storage_device_id;
    }

    public void setStorage_device_id(String storage_device_id) {
        this.storage_device_id = storage_device_id;
    }

    public String getStorage_name() {
        return storage_name;
    }

    public void setStorage_name(String storage_name) {
        this.storage_name = storage_name;
    }

}
