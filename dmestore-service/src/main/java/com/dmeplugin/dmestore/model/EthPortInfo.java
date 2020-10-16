package com.dmeplugin.dmestore.model;
/**
 * @author yy
 * @className EthPortInfo
 * @description TODO
 * @date 2020/9/3 17:43
 */
public class EthPortInfo {
    /**
     * DME Storage 1.0.RC1 系统资源北向模型 01.pdf 存储端口
    **/
    String ownerType;
    /**
     * IPv4掩码
     **/
    String ipv4Mask;
    String logicalType;
    String storageDeviceId;
    /**
     * 端口名称
     **/
    String portName;
    String ownerId;
    /**
     * 端口ID
     **/
    String portId;
    /**
     * 绑定名称
     **/
    String bondName;
    /**
     * MAC地址
     **/
    String mac;
    /**
     * IPv6地址
     **/
    String mgmtIpv6;
    /**
     * iSCSI名称
     **/
    String iscsiName;
    String ownerName;
    /**
     * 最后监控时间
     **/
    Long lastMonitorTime;
    /**
     * IPv4地址
     **/
    String mgmtIp;
    String confirmStatus;
    /**
     * CMDB 实例 ID
     **/
    String id;
    /**
     * 最后修改时间
     **/
    Long lastModified;
    /**
     * 连接状态
     **/
    String connectStatus;
    Integer classId;
    /**
     * 监控状态
     **/
    String dataStatus;
    /**
     * Mbit/s
     **/
    Integer maxSpeed;
    String resId;
    Boolean local;
    /**
     * 端口类型
     **/
    String portType;
    String className;
    Integer numberOfInitiators;
    /**
     * 绑定ID
     **/
    String bondId;
    String regionId;
    /**
     * 名称
     **/
    String name;
    /**
     * 位置
     **/
    String location;
    /**
     * 原始ID
     **/
    String nativeId;
    String dataSource;
    /**
     * IPv6掩码
     **/
    String ipv6Mask;
    /**
     * 状态
     **/
    String status;
    /**
     * Mbit/s
     **/
    Integer speed;
    /**
     * WWN
     **/
    String wwn;  
    /**
     * 光模块状态
     **/
    String sfpStatus;

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getIpv4Mask() {
        return ipv4Mask;
    }

    public void setIpv4Mask(String ipv4Mask) {
        this.ipv4Mask = ipv4Mask;
    }

    public String getLogicalType() {
        return logicalType;
    }

    public void setLogicalType(String logicalType) {
        this.logicalType = logicalType;
    }

    public String getStorageDeviceId() {
        return storageDeviceId;
    }

    public void setStorageDeviceId(String storageDeviceId) {
        this.storageDeviceId = storageDeviceId;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
    }

    public String getBondName() {
        return bondName;
    }

    public void setBondName(String bondName) {
        this.bondName = bondName;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMgmtIpv6() {
        return mgmtIpv6;
    }

    public void setMgmtIpv6(String mgmtIpv6) {
        this.mgmtIpv6 = mgmtIpv6;
    }

    public String getIscsiName() {
        return iscsiName;
    }

    public void setIscsiName(String iscsiName) {
        this.iscsiName = iscsiName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getLastMonitorTime() {
        return lastMonitorTime;
    }

    public void setLastMonitorTime(Long lastMonitorTime) {
        this.lastMonitorTime = lastMonitorTime;
    }

    public String getMgmtIp() {
        return mgmtIp;
    }

    public void setMgmtIp(String mgmtIp) {
        this.mgmtIp = mgmtIp;
    }

    public String getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(String confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    public String getConnectStatus() {
        return connectStatus;
    }

    public void setConnectStatus(String connectStatus) {
        this.connectStatus = connectStatus;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public Boolean getLocal() {
        return local;
    }

    public void setLocal(Boolean local) {
        local = local;
    }

    public String getPortType() {
        return portType;
    }

    public void setPortType(String portType) {
        this.portType = portType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getNumberOfInitiators() {
        return numberOfInitiators;
    }

    public void setNumberOfInitiators(Integer numberOfInitiators) {
        this.numberOfInitiators = numberOfInitiators;
    }

    public String getBondId() {
        return bondId;
    }

    public void setBondId(String bondId) {
        this.bondId = bondId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNativeId() {
        return nativeId;
    }

    public void setNativeId(String nativeId) {
        this.nativeId = nativeId;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getIpv6Mask() {
        return ipv6Mask;
    }

    public void setIpv6Mask(String ipv6Mask) {
        this.ipv6Mask = ipv6Mask;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public String getWwn() {
        return wwn;
    }

    public void setWwn(String wwn) {
        this.wwn = wwn;
    }

    public String getSfpStatus() {
        return sfpStatus;
    }

    public void setSfpStatus(String sfpStatus) {
        this.sfpStatus = sfpStatus;
    }
}
