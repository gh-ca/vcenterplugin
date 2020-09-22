package com.dmeplugin.dmestore.model;

public class EthPortInfo {
    //DME Storage 1.0.RC1 系统资源北向模型 01.pdf 存储端口
    String ownerType;
    String ipv4Mask;  //IPv4掩码
    String logicalType;
    String storageDeviceId;
    String portName;    //端口名称
    String ownerId;
    String portId;  //端口ID
    String bondName; //绑定名称
    String mac;  //MAC地址
    String mgmtIpv6; //IPv6地址
    String iscsiName; //iSCSI名称
    String ownerName;
    Long lastMonitorTime; //最后监控时间
    String mgmtIp;  //IPv4地址
    String confirmStatus;
    String id;  //CMDB 实例 ID
    Long lastModified; //最后修改时间
    String connectStatus; //连接状态
    Integer classId;
    String dataStatus; //监控状态
    Integer maxSpeed; //Mbit/s
    String resId;
    Boolean local;
    String portType; //端口类型
    String className;
    Integer numberOfInitiators;
    String bondId;  //绑定ID
    String regionId;
    String name;  //名称
    String location; //位置
    String nativeId; //原始ID
    String dataSource;
    String ipv6Mask; //IPv6掩码
    String status; //状态
    Integer speed;  //Mbit/s
    String wwn;  //WWN
    String sfpStatus; //光模块状态

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
