package com.huawei.dmestore.model;

/**
 * BaseSmartQos
 *
 * @author lianqiang
 * @since 2020-09-09
 */
public class BaseSmartQos {
    /**
     * latencyUnit .
     */
    private String name;

    /**
     * latency .
     */
    private int latency;

    /**
     * maxbandwidth .
     */
    private int maxbandwidth;

    /**
     * maxiops .
     */
    private int maxiops;

    /**
     * minbandwidth .
     */
    private int minbandwidth;

    /**
     * miniops .
     */
    private int miniops;

    /**
     * enabled .
     */
    private boolean enabled;

    /**
     * latencyUnit .
     */
    private String latencyUnit;

    /**
     * getLatencyUnit.
     *
     * @return .
     */
    public String getLatencyUnit() {
        return latencyUnit;
    }

    /**
     * setLatencyUnit .
     *
     * @param param .
     */
    public void setLatencyUnit(final String param) {
        this.latencyUnit = param;
    }

    /**
     * getEnabled .
     *
     * @return .
     */
    public boolean getEnabled() {
        return enabled;
    }

    /**
     * setLatencyUnit .
     *
     * @param param .
     */
    public void isEnabled(final boolean param) {
        this.enabled = param;
    }

    /**
     * getName .
     *
     * @return .
     */
    public String getName() {
        return name;
    }

    /**
     * setLatencyUnit .
     *
     * @param param .
     */
    public void setName(final String param) {
        this.name = param;
    }

    /**
     * getLatency .
     *
     * @return .
     */
    public int getLatency() {
        return latency;
    }

    /**
     * setLatencyUnit .
     *
     * @param param .
     */
    public void setLatency(final int param) {
        this.latency = param;
    }

    /**
     * getMaxbandwidth .
     *
     * @return .
     */
    public int getMaxbandwidth() {
        return maxbandwidth;
    }

    /**
     * setLatencyUnit .
     *
     * @param param .
     */
    public void setMaxbandwidth(final int param) {
        this.maxbandwidth = param;
    }

    /**
     * getMaxiops .
     *
     * @return .
     */
    public int getMaxiops() {
        return maxiops;
    }

    /**
     * setLatencyUnit .
     *
     * @param param .
     */
    public void setMaxiops(final int param) {
        this.maxiops = param;
    }

    /**
     * getMinbandwidth .
     *
     * @return .
     */
    public int getMinbandwidth() {
        return minbandwidth;
    }

    /**
     * setLatencyUnit .
     *
     * @param param .
     */
    public void setMinbandwidth(final int param) {
        this.minbandwidth = param;
    }

    /**
     * getMiniops .
     *
     * @return .
     */
    public int getMiniops() {
        return miniops;
    }

    /**
     * setLatencyUnit .
     *
     * @param param .
     */
    public void setMiniops(final int param) {
        this.miniops = param;
    }
}
