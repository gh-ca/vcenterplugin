package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @ClassName: BaseSmartQos
 * @Company: GH-CA
 * @create 2020-09-03
 */
public class BaseSmartQos {
    protected String name;

    protected Integer latency;

    protected Integer maxbandwidth;

    protected Integer maxiops;

    protected Integer minbandwidth;

    protected Integer miniops;

    protected Boolean enabled;

    protected String latencyUnit;

    public String getLatencyUnit() {
        return latencyUnit;
    }

    public void setLatencyUnit(String latencyUnit) {
        this.latencyUnit = latencyUnit;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLatency() {
        return latency;
    }

    public void setLatency(Integer latency) {
        this.latency = latency;
    }

    public Integer getMaxbandwidth() {
        return maxbandwidth;
    }

    public void setMaxbandwidth(Integer maxbandwidth) {
        this.maxbandwidth = maxbandwidth;
    }

    public Integer getMaxiops() {
        return maxiops;
    }

    public void setMaxiops(Integer maxiops) {
        this.maxiops = maxiops;
    }

    public Integer getMinbandwidth() {
        return minbandwidth;
    }

    public void setMinbandwidth(Integer minbandwidth) {
        this.minbandwidth = minbandwidth;
    }

    public Integer getMiniops() {
        return miniops;
    }

    public void setMiniops(Integer miniops) {
        this.miniops = miniops;
    }

}
