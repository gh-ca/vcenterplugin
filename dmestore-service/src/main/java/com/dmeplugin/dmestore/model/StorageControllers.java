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
}
