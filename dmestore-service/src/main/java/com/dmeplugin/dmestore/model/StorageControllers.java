package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @className StorageDisk
 * @description TODO
 * @date 2020/9/7 16:46
 */
public class StorageControllers {

    private String name;
    private String status;
    private String softVer;
    private String cpuInfo;

    private Float cpuUsage;
    private Float iops;
    private Float ops;
    private Float lantency;
    private Float bandwith;

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

    public String getSoftVer() {
        return softVer;
    }

    public void setSoftVer(String softVer) {
        this.softVer = softVer;
    }

    public String getCpuInfo() {
        return cpuInfo;
    }

    public void setCpuInfo(String cpuInfo) {
        this.cpuInfo = cpuInfo;
    }

    public Float getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Float cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Float getIops() {
        return iops;
    }

    public void setIops(Float iops) {
        this.iops = iops;
    }

    public Float getOps() {
        return ops;
    }

    public void setOps(Float ops) {
        this.ops = ops;
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
}
