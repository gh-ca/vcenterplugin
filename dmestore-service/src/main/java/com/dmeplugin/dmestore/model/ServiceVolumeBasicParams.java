package com.dmeplugin.dmestore.model;

/**
 * @author wangxiangyong
 **/
public class ServiceVolumeBasicParams {
    private String name;
    private Integer capacity;
    private Integer count;
    private Integer startSuffix;
    private String unit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public int getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getStartSuffix() {
        return startSuffix;
    }

    public void setStartSuffix(Integer startSuffix) {
        this.startSuffix = startSuffix;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
