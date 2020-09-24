package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @ClassName: Storage
 * @Company: GH-CA
 * @create 2020-09-03
 */
public class Storage  {

    private String id;//存储设备ID。
    private String name; //名称
    private String ip;  //ip地址
    private String status; //状态
    private String synStatus; //状态
    private String vendor; //厂商
    private String model; //型号
    private Double usedCapacity;//已用容量
    private Double totalCapacity;//裸容量
    private Double totalEffectiveCapacity;
    private Double freeEffectiveCapacity;//空闲容量
    private Double maxCpuUtilization;//CPU利用率
    private Double maxIops;//iops
    private Double maxBandwidth;//带宽
    private Double maxLatency;//时延
    private String[] azIds; //可用分区
    private String sn;//设备序列号
    private String version;//版本
    private String product_version;
    private Double total_pool_capacity;//总容量
    private Double subscription_capacity; //订阅容量

    //容量利用率 = 已用容量/总容量
    //订阅率 =订阅容量/总容量
    // TODO OPS未找到
    public Double getTotal_pool_capacity() {
        return total_pool_capacity;
    }

    public void setTotal_pool_capacity(Double total_pool_capacity) {
        this.total_pool_capacity = total_pool_capacity;
    }

    public Double getSubscription_capacity() {
        return subscription_capacity;
    }

    public void setSubscription_capacity(Double subscription_capacity) {
        this.subscription_capacity = subscription_capacity;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProduct_version() {
        return product_version;
    }

    public void setProduct_version(String product_version) {
        this.product_version = product_version;
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

}
