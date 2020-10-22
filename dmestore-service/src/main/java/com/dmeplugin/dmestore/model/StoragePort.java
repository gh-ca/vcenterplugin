package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @className Port
 * @description 补充端口类型得列表数据
 * @date 2020/10/14 14:27
 */
public class StoragePort {
    //id
    private String id;
    //原始id
    private String nativeId;
    //最后一次修改时间
    private Long last_Modified;
    //最后监控时间
    private Long lastMonitorTime;
    //监控状态
    private String dataStatus;
    //名称
    private String name;
    //端口id
    private String portId;
    //端口名称: P2
    private String portName;
    //位置: CTE0.B0.P2
    private String location;
    //连接状态
    private String connectStatus;
    //状态
    private String status;
    //端口类型
    private String portType;
    //MAC地址
    private String mac;
    //IPv4地址
    private String mgmtIp;
    //IPv4掩码
    private String ipv4Mask;
    //IPv6地址
    private String mgmtIpv6;
    //IPv6掩码
    private String ipv6Mask;
    //iSCSI名称
    private String iscsiName;
    //绑定ID
    private String bondId;
    //绑定名称
    private String bondName;
    //wwn
    private String wwn;
    //光模块状态
    private String sfpStatus;
    //逻辑类型
    private String logicalType;
    //启动器个数
    private Integer numOfInitiators;
    //端口速率
    private Integer speed;
    //端口最大速率
    private Integer maxSpeed;
    //存储设备ID
    private String storageDeviceId;

    private Float iops;
    private Float lantency;
    private Float bandwith;
    private Float useage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNativeId() {
        return nativeId;
    }

    public void setNativeId(String nativeId) {
        this.nativeId = nativeId;
    }

    public Long getLast_Modified() {
        return last_Modified;
    }

    public void setLast_Modified(Long last_Modified) {
        this.last_Modified = last_Modified;
    }

    public Long getLastMonitorTime() {
        return lastMonitorTime;
    }

    public void setLastMonitorTime(Long lastMonitorTime) {
        this.lastMonitorTime = lastMonitorTime;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getConnectStatus() {
        return connectStatus;
    }

    public void setConnectStatus(String connectStatus) {
        this.connectStatus = connectStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPortType() {
        return portType;
    }

    public void setPortType(String portType) {
        this.portType = portType;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMgmtIp() {
        return mgmtIp;
    }

    public void setMgmtIp(String mgmtIp) {
        this.mgmtIp = mgmtIp;
    }

    public String getIpv4Mask() {
        return ipv4Mask;
    }

    public void setIpv4Mask(String ipv4Mask) {
        this.ipv4Mask = ipv4Mask;
    }

    public String getMgmtIpv6() {
        return mgmtIpv6;
    }

    public void setMgmtIpv6(String mgmtIpv6) {
        this.mgmtIpv6 = mgmtIpv6;
    }

    public String getIpv6Mask() {
        return ipv6Mask;
    }

    public void setIpv6Mask(String ipv6Mask) {
        this.ipv6Mask = ipv6Mask;
    }

    public String getIscsiName() {
        return iscsiName;
    }

    public void setIscsiName(String iscsiName) {
        this.iscsiName = iscsiName;
    }

    public String getBondId() {
        return bondId;
    }

    public void setBondId(String bondId) {
        this.bondId = bondId;
    }

    public String getBondName() {
        return bondName;
    }

    public void setBondName(String bondName) {
        this.bondName = bondName;
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

    public String getLogicalType() {
        return logicalType;
    }

    public void setLogicalType(String logicalType) {
        this.logicalType = logicalType;
    }

    public Integer getNumOfInitiators() {
        return numOfInitiators;
    }

    public void setNumOfInitiators(Integer numOfInitiators) {
        this.numOfInitiators = numOfInitiators;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getStorageDeviceId() {
        return storageDeviceId;
    }

    public void setStorageDeviceId(String storageDeviceId) {
        this.storageDeviceId = storageDeviceId;
    }

    public Float getIops() {
        return iops;
    }

    public void setIops(Float iops) {
        this.iops = iops;
    }

    public Float getLantency() {
        return lantency;
    }

    public void setLantency(Float lantency) {
        this.lantency = lantency;
    }

    public Float getBandwith() {
        return bandwith;
    }

    public void setBandwith(Float bandwith) {
        this.bandwith = bandwith;
    }

    public Float getUseage() {
        return useage;
    }

    public void setUseage(Float useage) {
        this.useage = useage;
    }
}
