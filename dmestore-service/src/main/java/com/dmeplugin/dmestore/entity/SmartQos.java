package com.dmeplugin.dmestore.entity;

public class SmartQos {

    //QoS名称
    private String name;
    //控制策略,0：保护IO下限，1：控制IO上限
    private Integer controlPolicy;
    //时延
    private Integer latency;
    //最大带宽
    private Integer maxbandwidth;
    //最大iops
    private Integer maxiops;
    //最小带宽
    private Integer minbandwidth;
    //最小iops
    private Integer miniops;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getControlPolicy() {
        return controlPolicy;
    }

    public void setControlPolicy(Integer control_policy) {
        this.controlPolicy = control_policy;
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

    @Override
    public String toString() {
        return "SmartQos{" +
                "name='" + name + '\'' +
                ", control_policy=" + controlPolicy +
                ", latency=" + latency +
                ", maxbandwidth=" + maxbandwidth +
                ", maxiops=" + maxiops +
                ", minbandwidth=" + minbandwidth +
                ", miniops=" + miniops +
                '}';
    }
}
