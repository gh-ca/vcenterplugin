package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @className QosParam
 * @description TODO
 * @date 2020/9/15 14:57
 */
public class QosParam {

    private Boolean enabled;
    private QosPolicy qosPolicy;
    private Integer latency;//responTime;
    private String latencyUnit;//Unit of response time eg:ms s
    private Integer minBandWidth;
    private Integer minIOPS;
    private Integer maxBandWidth;
    private Integer maxIOPS;

    public Integer getLatency() {
        return latency;
    }

    public void setLatency(Integer latency) {
        this.latency = latency;
    }

    public String getLatencyUnit() {
        return latencyUnit;
    }

    public void setLatencyUnit(String latencyUnit) {
        this.latencyUnit = latencyUnit;
    }

    public Integer getMinBandWidth() {
        return minBandWidth;
    }

    public void setMinBandWidth(Integer minBandWidth) {
        this.minBandWidth = minBandWidth;
    }

    public Integer getMinIOPS() {
        return minIOPS;
    }

    public void setMinIOPS(Integer minIOPS) {
        this.minIOPS = minIOPS;
    }

    public Integer getMaxBandWidth() {
        return maxBandWidth;
    }

    public void setMaxBandWidth(Integer maxBandWidth) {
        this.maxBandWidth = maxBandWidth;
    }

    public Integer getMaxIOPS() {
        return maxIOPS;
    }

    public void setMaxIOPS(Integer maxIOPS) {
        this.maxIOPS = maxIOPS;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public com.dmeplugin.dmestore.model.SmartQos getSmartQos() {
        return SmartQos;
    }

    public void setSmartQos(com.dmeplugin.dmestore.model.SmartQos smartQos) {
        SmartQos = smartQos;
    }
}
