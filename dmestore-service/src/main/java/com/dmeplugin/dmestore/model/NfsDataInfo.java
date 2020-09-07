package com.dmeplugin.dmestore.model;

public class NfsDataInfo {
    //列表字段（基本视图）
    String name;    //名称
    String status;  //状态
    Double capacity;  //总容量 单位GB
    Double freeSpace; //空闲容量 单位GB
    Double reserveCapacity; //置备容量  capacity+uncommitted-freeSpace 单位GB
    String device; //设备
    String logicPort; //逻辑端口
    String shareIp; //share ip
    String sharePath; //share path
    String fs; //fs

    //列表字段（性能视图）：
    Integer OPS; //OPS
    Double bandwidth;   //带宽 单位MB/s

    Integer readResponseTime;   //读响应时间 单位ms
    Integer writeResponseTime; //写响应时间 单位ms

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

    public String getLogicPort() {
        return logicPort;
    }

    public void setLogicPort(String logicPort) {
        this.logicPort = logicPort;
    }

    public String getShareIp() {
        return shareIp;
    }

    public void setShareIp(String shareIp) {
        this.shareIp = shareIp;
    }

    public String getSharePath() {
        return sharePath;
    }

    public void setSharePath(String sharePath) {
        this.sharePath = sharePath;
    }

    public String getFs() {
        return fs;
    }

    public void setFs(String fs) {
        this.fs = fs;
    }

    public Integer getOPS() {
        return OPS;
    }

    public void setOPS(Integer OPS) {
        this.OPS = OPS;
    }

    public Double getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(Double bandwidth) {
        this.bandwidth = bandwidth;
    }

    public Integer getReadResponseTime() {
        return readResponseTime;
    }

    public void setReadResponseTime(Integer readResponseTime) {
        this.readResponseTime = readResponseTime;
    }

    public Integer getWriteResponseTime() {
        return writeResponseTime;
    }

    public void setWriteResponseTime(Integer writeResponseTime) {
        this.writeResponseTime = writeResponseTime;
    }
}
