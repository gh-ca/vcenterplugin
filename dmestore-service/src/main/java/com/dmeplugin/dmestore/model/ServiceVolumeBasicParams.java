package com.dmeplugin.dmestore.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author wangxiangyong
 **/
public class ServiceVolumeBasicParams {
    private String name;
    private Integer capacity;
    private Integer count;
    @JsonProperty("start_suffix")
    private Integer startSuffix;

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
}
