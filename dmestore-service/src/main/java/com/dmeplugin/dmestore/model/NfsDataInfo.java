package com.dmeplugin.dmestore.model;
/**
 * @Description: TODO
 * @ClassName: NfsDataInfo
 * @Company: GH-CA
 * @author: yy
 * @create: 2020-09-02
 **/
public class NfsDataInfo {
    /**
     * 跳转用唯一id
     **/
    String objectid;
    /**
     * 名称
     **/
    String name;
    /**
     * 状态
     **/
    String status;
    /**
     * 总容量 单位GB
     **/
    Double capacity;
    /**
     * 空闲容量 单位GB
     **/
    Double freeSpace;
    /**
     * 置备容量  capacity+uncommitted-freeSpace 单位GB
     **/
    Double reserveCapacity;
    /**
     * 存储设备ID
     **/
    String deviceId;
    /**
     * 存储设备名称
     **/
    String device;
    /**
     * 逻辑端口
     **/
    String logicPort;
    /**
     * 逻辑端口 id
     **/
    String logicPortId;
    /**
     * share ip
     **/
    String shareIp;
    /**
     * share path
     **/
    String sharePath;
    /**
     * share 名称
     **/
    String share;
    /**
     * share id
     **/
    String shareId;
    /**
     * fs
     **/
    String fs;
    /**
     * fs id
     **/
    String fsId;
    /**
     * OPS
     **/
    Float ops;
    /**
     * 带宽 单位MB/s
     **/
    Float bandwidth;
    /**
     * 读响应时间 单位ms
     **/
    Float readResponseTime;
    /**
     * 写响应时间 单位ms
     **/
    Float writeResponseTime;

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

    public String getLogicPort() {
        return logicPort;
    }

    public void setLogicPort(String logicPort) {
        this.logicPort = logicPort;
    }

    public String getLogicPortId() {
        return logicPortId;
    }

    public void setLogicPortId(String logicPortId) {
        this.logicPortId = logicPortId;
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

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public String getFs() {
        return fs;
    }

    public void setFs(String fs) {
        this.fs = fs;
    }

    public String getFsId() {
        return fsId;
    }

    public void setFsId(String fsId) {
        this.fsId = fsId;
    }

    public Float getOps() {
        return ops;
    }

    public void setOps(Float ops) {
        this.ops = ops;
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
}
