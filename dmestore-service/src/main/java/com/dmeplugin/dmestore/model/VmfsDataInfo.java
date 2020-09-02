package com.dmeplugin.dmestore.model;

public class VmfsDataInfo {
    //列表字段（基本视图）
    String name;    //名称
    String status;  //状态

    Double capacity;  //总容量 单位GB
    Double freeSpace; //空闲容量 单位GB
    Double reserveCapacity; //置备容量  capacity+uncommitted-freeSpace 单位GB

    String device; //设备
    String serviceLevelName;    //服务等级
    Boolean vmfsProtected;  //保护状态

    //列表字段（性能视图）：
    int maxIops; //QoS上限
    int minIops; //QoS下限
    int maxBandwidth;   //带宽上限 单位MB/s
    int minBandwidth;   //带宽下限 单位MB/s

    int latency; //时延 单位ms

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

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public Double getFreeSpace() {
        return freeSpace;
    }

    public void setFreeSpace(Double freeSpace) {
        this.freeSpace = freeSpace;
    }

    public Double getReserveCapacity() {
        return reserveCapacity;
    }

    public void setReserveCapacity(Double reserveCapacity) {
        this.reserveCapacity = reserveCapacity;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getServiceLevelName() {
        return serviceLevelName;
    }

    public void setServiceLevelName(String serviceLevelName) {
        this.serviceLevelName = serviceLevelName;
    }

    public Boolean getVmfsProtected() {
        return vmfsProtected;
    }

    public void setVmfsProtected(Boolean vmfsProtected) {
        this.vmfsProtected = vmfsProtected;
    }

    public int getMaxIops() {
        return maxIops;
    }

    public void setMaxIops(int maxIops) {
        this.maxIops = maxIops;
    }

    public int getMinIops() {
        return minIops;
    }

    public void setMinIops(int minIops) {
        this.minIops = minIops;
    }

    public int getMaxBandwidth() {
        return maxBandwidth;
    }

    public void setMaxBandwidth(int maxBandwidth) {
        this.maxBandwidth = maxBandwidth;
    }

    public int getMinBandwidth() {
        return minBandwidth;
    }

    public void setMinBandwidth(int minBandwidth) {
        this.minBandwidth = minBandwidth;
    }

    public int getLatency() {
        return latency;
    }

    public void setLatency(int latency) {
        this.latency = latency;
    }
}
