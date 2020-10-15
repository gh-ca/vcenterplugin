package com.dmeplugin.dmestore.model;


/**
 * @author lianq
 * @ClassName: StoragePool
 * @Company: GH-CA
 * @create 2020-09-03
 */
public class StoragePool   {
    //空闲容量
    private Double free_capacity;
    //名称
    private String name;
    //存储池id
    private String id;
    //运行状态
    private String running_status;
    //健康状态
    private String health_status;
    //总容量
    private Double total_capacity;
    //已用容量
    private Double consumed_capacity;
    //已用容量百分比(容量利用率)
    private String consumed_capacity_percentage;
    private String storage_pool_id;
    private String storage_instance_id;
    private String storage_device_id;
    //订阅率 = 订阅容量/总容量
    private Double subscription_rate;
    //类型（块）
    private String media_type;
    //0-无效，1-RAID 10，2-RAID 5，3-RAID 0，4-RAID 1，5-RAID 6，6-RAID 50，7-RAID 3。
    //RAID级别
    private String tier0_raid_lv;
    private String tier1_raid_lv;
    private String tier2_raid_lv;
    //存储设备id
    private String storage_id;
    //存储池上创建LUN或者文件系统时的可用容量 单位MB
    private Double data_space;
    //时延 iops 带宽 公用存储设备的
    //订阅容量
    private Double subscribed_capacity;
    //硬盘类型
    private String physicalType;
    //存储池所处硬盘id
    private String diskPoolId;

    public String getDiskPoolId() {
        return diskPoolId;
    }

    public void setDiskPoolId(String diskPoolId) {
        this.diskPoolId = diskPoolId;
    }

    public String getPhysicalType() {
        return physicalType;
    }

    public void setPhysicalType(String physicalType) {
        this.physicalType = physicalType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getSubscribed_capacity() {
        return subscribed_capacity;
    }

    public Double getSubscription_rate() {
        return subscription_rate;
    }

    public void setSubscription_rate(Double subscription_rate) {
        this.subscription_rate = subscription_rate;
    }

    public void setSubscribed_capacity(Double subscribed_capacity) {
        this.subscribed_capacity = subscribed_capacity;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


}
