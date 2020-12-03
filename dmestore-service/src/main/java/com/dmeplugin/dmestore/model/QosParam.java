package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @className QosParam
 * @description TODO
 * @date 2020/9/15 14:57
 */
public class QosParam {

    private Boolean enabled;
    //responTime;
    private Integer latency;
    //Unit of response time eg:ms s
    private String latencyUnit;
    private Integer minBandWidth;
    private Integer minIOPS;
    private Integer maxBandWidth;
    private Integer maxIOPS;
    private SmartQos smartQos;

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

    public SmartQos getSmartQos() {
        return smartQos;
    }

    public void setSmartQos(SmartQos smartQos) {
        this.smartQos = smartQos;
    }

    @Override
    public String toString() {
        return "QosParam{" +
            "enabled=" + enabled +
            ", latency=" + latency +
            ", latencyUnit='" + latencyUnit + '\'' +
            ", minBandWidth=" + minBandWidth +
            ", minIOPS=" + minIOPS +
            ", maxBandWidth=" + maxBandWidth +
            ", maxIOPS=" + maxIOPS +
            ", smartQos=" + smartQos +
            '}';
    }
}
