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
    private Double capacity;

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
}
