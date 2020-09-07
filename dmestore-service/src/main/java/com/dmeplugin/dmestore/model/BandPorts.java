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
   private String health_status;
   private String running_status;
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

    public String getHealth_status() {
        return health_status;
    }

    public void setHealth_status(String health_status) {
        this.health_status = health_status;
    }

    public String getRunning_status() {
        return running_status;
    }

    public void setRunning_status(String running_status) {
        this.running_status = running_status;
    }

    public String getMtu() {
        return mtu;
    }

    public void setMtu(String mtu) {
        this.mtu = mtu;
    }
}
