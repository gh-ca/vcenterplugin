package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @className StorageDisk
 * @description TODO
 * @date 2020/9/7 19:33
 */
public class StorageDisk {

    private String name;
    private String status;
    private String capacity;

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

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }
}
