package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @ClassName: Storage
 * @Company: GH-CA
 * @create 2020-09-03
 */
public class Storage  {

    //存储设备ID。
    private String id;
    //名称
    private String name;
    //ip地址
    private String ip;
    //状态
    private String status;
    //状态
    private String synStatus;
    //厂商
    private String vendor;
    //型号
    private String model;
    //已用容量
    private Double usedCapacity;
    //裸容量
    private Double totalCapacity;
    private Double totalEffectiveCapacity;
    //空闲容量
    private Double freeEffectiveCapacity;
    //CPU利用率
    private Double maxCpuUtilization;
    //iops
    private Double maxIops;
    //带宽
    private Double maxBandwidth;
    //时延
    private Double maxLatency;
    //可用分区
    private String[] azIds;
    //设备序列号
    private String sn;
    //版本
    private String version;
    private String productVersion;
    //总容量
    private Double totalPoolCapacity;
    //订阅容量
    private Double subscriptionCapacity;

    //容量利用率 = 已用容量/总容量
    private Double capacityUtilization;

    //订阅率 =订阅容量/总容量
    private Double subscriptionRate;

    /**
     *位置
     **/
    private String location;
    /**
     *补丁版本
    **/
    private String patchVersion;
    /**
     *维保开始时间
     **/
    private String maintenanceStart;
    /**
     *维保结束时间
     **/
    private String maintenanceOvertime;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPatchVersion() {
        return patchVersion;
    }

    public void setPatchVersion(String patchVersion) {
        this.patchVersion = patchVersion;
    }

    public String getMaintenanceStart() {
        return maintenanceStart;
    }

    public void setMaintenanceStart(String maintenanceStart) {
        this.maintenanceStart = maintenanceStart;
    }

    public String getMaintenanceOvertime() {
        return maintenanceOvertime;
    }

    public void setMaintenanceOvertime(String maintenanceOvertime) {
        this.maintenanceOvertime = maintenanceOvertime;
    }

    public Double getCapacityUtilization() {
        return capacityUtilization;
    }

    public void setCapacityUtilization(Double capacityUtilization) {
        this.capacityUtilization = capacityUtilization;
    }



    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSynStatus() {
        return synStatus;
    }

    public void setSynStatus(String synStatus) {
        this.synStatus = synStatus;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getUsedCapacity() {
        return usedCapacity;
    }

    public void setUsedCapacity(Double usedCapacity) {
        this.usedCapacity = usedCapacity;
    }

    public Double getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Double totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Double getTotalEffectiveCapacity() {
        return totalEffectiveCapacity;
    }

    public void setTotalEffectiveCapacity(Double totalEffectiveCapacity) {
        this.totalEffectiveCapacity = totalEffectiveCapacity;
    }

    public Double getFreeEffectiveCapacity() {
        return freeEffectiveCapacity;
    }

    public void setFreeEffectiveCapacity(Double freeEffectiveCapacity) {
        this.freeEffectiveCapacity = freeEffectiveCapacity;
    }

    public Double getMaxCpuUtilization() {
        return maxCpuUtilization;
    }

    public void setMaxCpuUtilization(Double maxCpuUtilization) {
        this.maxCpuUtilization = maxCpuUtilization;
    }

    public Double getMaxIops() {
        return maxIops;
    }

    public void setMaxIops(Double maxIops) {
        this.maxIops = maxIops;
    }

    public Double getMaxBandwidth() {
        return maxBandwidth;
    }

    public void setMaxBandwidth(Double maxBandwidth) {
        this.maxBandwidth = maxBandwidth;
    }

    public Double getMaxLatency() {
        return maxLatency;
    }

    public void setMaxLatency(Double maxLatency) {
        this.maxLatency = maxLatency;
    }

    public Object getAzIds() {
        return azIds;
    }

    public void setAzIds(String[] azIds) {
        this.azIds = azIds;
    }

    public String getProductVersion() {
        return productVersion;
    }

    public void setProductVersion(String productVersion) {
        this.productVersion = productVersion;
    }

    public Double getTotalPoolCapacity() {
        return totalPoolCapacity;
    }

    public void setTotalPoolCapacity(Double totalPoolCapacity) {
        this.totalPoolCapacity = totalPoolCapacity;
    }

    public Double getSubscriptionCapacity() {
        return subscriptionCapacity;
    }

    public void setSubscriptionCapacity(Double subscriptionCapacity) {
        this.subscriptionCapacity = subscriptionCapacity;
    }

    public Double getSubscriptionRate() {
        return subscriptionRate;
    }

    public void setSubscriptionRate(Double subscriptionRate) {
        this.subscriptionRate = subscriptionRate;
    }
}
