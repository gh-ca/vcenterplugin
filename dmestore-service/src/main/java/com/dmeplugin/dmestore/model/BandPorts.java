package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @className BandPort
 * @description TODO
 * @date 2020/9/7 15:35
 */
public class BandPorts {

   private String id;
   private String name;
   private String healthStatus;
   private String runningStatus;
   private String mtu;

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

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(String runningStatus) {
        this.runningStatus = runningStatus;
    }

    public String getMtu() {
        return mtu;
    }

    public void setMtu(String mtu) {
        this.mtu = mtu;
    }
}
