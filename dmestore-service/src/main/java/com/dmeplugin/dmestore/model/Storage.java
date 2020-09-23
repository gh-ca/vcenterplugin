package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @ClassName: Storage
 * @Company: GH-CA
 * @create 2020-09-03
 */
public class Storage  {

    private String id;
    private String name;
    private String ip;
    private String status;
    private String synStatus;
    private String vendor;
    private String model;
    private String productVersion;
    private Double usedCapacity;
    private Double totalCapacity;
    private Double totalEffectiveCapacity;
    private Double freeEffectiveCapacity;
    private Double maxCpuUtilization;
    private Double maxIops;
    private Double maxBandwidth;
    private Double maxLatency;
    private String[] azIds;
    //补充字段
    private String sn;

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
}
