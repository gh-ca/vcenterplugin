package com.dmeplugin.dmestore.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * @author lianq
 * @ClassName: Storage
 * @Company: GH-CA
 * @create 2020-09-03
 */
public class Storage implements Serializable {

    private String id;
    private String name;
    private String ip;
    private String status;
    private String synStatus;
    private String vendor;
    private String model;
    private Long usedCapacity;
    private Long totalCapacity;
    private Long totalEffectiveCapacity;
    private Long freeEffectiveCapacity;
    private Long maxCpuUtilization;
    private Long maxIops;
    private Long maxBandwidth;
    private Long maxLatency;

    private List<String> azIds =new ArrayList<>();//

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

    public Long getUsedCapacity() {
        return usedCapacity;
    }

    public void setUsedCapacity(Long usedCapacity) {
        this.usedCapacity = usedCapacity;
    }

    public Long getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Long totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Long getTotalEffectiveCapacity() {
        return totalEffectiveCapacity;
    }

    public void setTotalEffectiveCapacity(Long totalEffectiveCapacity) {
        this.totalEffectiveCapacity = totalEffectiveCapacity;
    }

    public Long getFreeEffectiveCapacity() {
        return freeEffectiveCapacity;
    }

    public void setFreeEffectiveCapacity(Long freeEffectiveCapacity) {
        this.freeEffectiveCapacity = freeEffectiveCapacity;
    }

    public Long getMaxCpuUtilization() {
        return maxCpuUtilization;
    }

    public void setMaxCpuUtilization(Long maxCpuUtilization) {
        this.maxCpuUtilization = maxCpuUtilization;
    }

    public Long getMaxIops() {
        return maxIops;
    }

    public void setMaxIops(Long maxIops) {
        this.maxIops = maxIops;
    }

    public Long getMaxBandwidth() {
        return maxBandwidth;
    }

    public void setMaxBandwidth(Long maxBandwidth) {
        this.maxBandwidth = maxBandwidth;
    }

    public Long getMaxLatency() {
        return maxLatency;
    }

    public void setMaxLatency(Long maxLatency) {
        this.maxLatency = maxLatency;
    }

    public List<String> getAzIds() {
        return azIds;
    }

    public void setAzIds(List<String> azIds) {
        this.azIds = azIds;
    }

    @Override
    public String toString() {
        return "StorageInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", status='" + status + '\'' +
                ", synStatus='" + synStatus + '\'' +
                ", vendor='" + vendor + '\'' +
                ", model='" + model + '\'' +
                ", usedCapacity=" + usedCapacity +
                ", totalCapacity=" + totalCapacity +
                ", totalEffectiveCapacity=" + totalEffectiveCapacity +
                ", freeEffectiveCapacity=" + freeEffectiveCapacity +
                ", maxCpuUtilization=" + maxCpuUtilization +
                ", maxIops=" + maxIops +
                ", maxBandwidth=" + maxBandwidth +
                ", maxLatency=" + maxLatency +
                ", azIds=" + azIds +
                '}';
    }
}
