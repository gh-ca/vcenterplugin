package com.dmeplugin.dmestore.model;

/**
 * @author wangxiangyong
 **/
public class ServiceVolumeBasicParams {
    private String name;
    private Integer capacity;
    private Integer count;
    private Integer start_suffix;

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

    public int getStart_suffix() {
        return start_suffix;
    }

    public void setStart_suffix(Integer start_suffix) {
        this.start_suffix = start_suffix;
    }
}
