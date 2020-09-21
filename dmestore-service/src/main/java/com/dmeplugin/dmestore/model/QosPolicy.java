package com.dmeplugin.dmestore.model;

/**
 * @ClassName QosPolicy
 * @Description TODO
 * @Author wangxiangyong
 * @Date 2020/9/2 17:34
 * @Version V1.0
 **/
public class QosPolicy {

    private String latency;
    private String min_bandwidth;
    private String min_iops;

    private String max_bandwidth;
    private String max_iops;
    //for list service level
    private String latencyUnit;
    //for update
    private Object control_policy;

    public String getLatencyUnit() {
        return latencyUnit;
    }

    public void setLatencyUnit(String latencyUnit) {
        this.latencyUnit = latencyUnit;
    }

    public String getLatency() {
        return latency;
    }

    public void setLatency(String latency) {
        this.latency = latency;
    }

    public String getMax_bandwidth() {
        return max_bandwidth;
    }

    public void setMax_bandwidth(String max_bandwidth) {
        this.max_bandwidth = max_bandwidth;
    }

    public String getMax_iops() {
        return max_iops;
    }

    public void setMax_iops(String max_iops) {
        this.max_iops = max_iops;
    }

    public String getMin_bandwidth() {
        return min_bandwidth;
    }

    public void setMin_bandwidth(String min_bandwidth) {
        this.min_bandwidth = min_bandwidth;
    }

    public String getMin_iops() {
        return min_iops;
    }

    public void setMin_iops(String min_iops) {
        this.min_iops = min_iops;
    }

    public Object getControl_policy() {
        return control_policy;
    }

    public void setControl_policy(Object control_policy) {
        this.control_policy = control_policy;
    }
}
