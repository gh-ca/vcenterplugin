package com.dmeplugin.dmestore.model;
/**
 * @author lianq
 * @ClassName: SmartQos
 * @Company: GH-CA
 * @create 2020-09-03
 */
public class SmartQos {

    private String name;
    //控制策略,0：保护IO下限，1：控制IO上限
    private Integer controlPolicy;
    private Integer latency;
    private Integer maxbandwidth;
    private Integer maxiops;
    private Integer minbandwidth;
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
