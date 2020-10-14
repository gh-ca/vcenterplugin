package com.dmeplugin.dmestore.model;
/**
 * @Description: TODO
 * @ClassName: VmfsDataInfo
 * @Company: GH-CA
 * @author: yy
 * @create: 2020-09-02
 **/
public class VmfsDataInfo {
    /**
     * vmware中跳转用唯一id
     **/
    String objectid;
    /**
     * 名称 vmware中vmfs的名称
     **/
    String name;
    /**
     * dme中状态
     **/
    String status;
    /**
     * vmware中总容量 单位GB
     **/
    Double capacity;
    /**
     * vmware中空闲容量 单位GB
     **/
    Double freeSpace;
    /**
     * vmware中置备容量  capacity+uncommitted-freeSpace 单位GB
     **/
    Double reserveCapacity;
    /**
     * dme中存储设备ID
     **/
    String deviceId;
    /**
     * dme中存储设备名称
     **/
    String device;
    /**
     * dme中服务等级
     **/
    String serviceLevelName;
    /**
     * dme中保护状态
     **/
    Boolean vmfsProtected;
    /**
     * dme中QoS上限
     **/
    Integer maxIops;
    /**
     * dme中QoS下限
     **/
    Integer minIops;
    /**
     * dme中iops 性能统计数据
     **/
    Float iops;
    /**
     * dme中带宽上限 单位MB/s
     **/
    Integer maxBandwidth;
    /**
     * dme中带宽下限 单位MB/s
     **/
    Integer minBandwidth;
    /**
     * dme中bandwidth 性能统计数据
     **/
    Float bandwidth;
    /**
     * dme中读响应时间 单位ms  性能统计数据
     **/
    Float readResponseTime;
    /**
     * dme中写响应时间 单位ms   性能统计数据
     **/
    Float writeResponseTime;
    /**
     * dme中时延 单位ms
     **/
    Integer latency;
    /**
     * dme中卷ID
     **/
    String volumeId;
    /**
     * dme中卷名称
     **/
    String volumeName;
    /**
     * dme中wwn
     **/
    String wwn;

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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public Float getIops() {
        return iops;
    }

    public void setIops(Float iops) {
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

    public Float getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(Float bandwidth) {
        this.bandwidth = bandwidth;
    }

    public Float getReadResponseTime() {
        return readResponseTime;
    }

    public void setReadResponseTime(Float readResponseTime) {
        this.readResponseTime = readResponseTime;
    }

    public Float getWriteResponseTime() {
        return writeResponseTime;
    }

    public void setWriteResponseTime(Float writeResponseTime) {
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

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }

    public String getWwn() {
        return wwn;
    }

    public void setWwn(String wwn) {
        this.wwn = wwn;
    }
}
