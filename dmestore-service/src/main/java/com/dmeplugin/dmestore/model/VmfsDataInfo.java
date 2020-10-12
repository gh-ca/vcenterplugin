package com.dmeplugin.dmestore.model;

public class VmfsDataInfo {
    //列表字段（基本视图）
    String objectid; //vmware中跳转用唯一id
    String name;    //名称 vmware中vmfs的名称
    String status;  //dme中状态

    Double capacity;  //vmware中总容量 单位GB
    Double freeSpace; //vmware中空闲容量 单位GB
    Double reserveCapacity; //vmware中置备容量  capacity+uncommitted-freeSpace 单位GB

    String deviceId; //dme中存储设备ID
    String device; //dme中存储设备名称
    String serviceLevelName;    //dme中服务等级
    Boolean vmfsProtected;  //dme中保护状态

    //列表字段（性能视图）：
    Integer maxIops; //dme中QoS上限
    Integer minIops; //dme中QoS下限
    Integer iops; //dme中iops 性能统计数据

    Integer maxBandwidth;   //dme中带宽上限 单位MB/s
    Integer minBandwidth;   //dme中带宽下限 单位MB/s
    Double bandwidth; //dme中bandwidth 性能统计数据

    Integer readResponseTime;   //dme中读响应时间 单位ms  性能统计数据
    Integer writeResponseTime; //dme中写响应时间 单位ms   性能统计数据
    Integer latency; //dme中时延 单位ms

    String volumeId; //dme中卷ID
    String volumeName; //dme中卷名称
    String wwn; //dme中wwn

    public String getWwn() {
        return wwn;
    }

    public void setWwn(String wwn) {
        this.wwn = wwn;
    }

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }

    public String getObjectid() {
        return objectid;
    }

    public void setObjectid(String objectid) {
        this.objectid = objectid;
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

    public Integer getMaxIops() {
        return maxIops;
    }

    public void setMaxIops(Integer maxIops) {
        this.maxIops = maxIops;
    }

    public Integer getMinIops() {
        return minIops;
    }

    public void setMinIops(Integer minIops) {
        this.minIops = minIops;
    }

    public Integer getIops() {
        return iops;
    }

    public void setIops(Integer iops) {
        this.iops = iops;
    }

    public Integer getMaxBandwidth() {
        return maxBandwidth;
    }

    public void setMaxBandwidth(Integer maxBandwidth) {
        this.maxBandwidth = maxBandwidth;
    }

    public Integer getMinBandwidth() {
        return minBandwidth;
    }

    public void setMinBandwidth(Integer minBandwidth) {
        this.minBandwidth = minBandwidth;
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

    public Integer getLatency() {
        return latency;
    }

    public void setLatency(Integer latency) {
        this.latency = latency;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
