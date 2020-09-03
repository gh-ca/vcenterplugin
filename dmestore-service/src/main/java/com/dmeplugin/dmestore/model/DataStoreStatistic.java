package com.dmeplugin.dmestore.model;

/**
 * @Description: TODO
 * @ClassName: DataStoreStatistic
 * @Company: GH-CA
 * @author: liuxh
 * @create: 2020-09-03
 **/
public class DataStoreStatistic {
    float readThroughput; // IOPS
    float writeThroughput; // IOPS
    float maxThroughput; // IOPS
    float throughput; // IOPS
    float readBandwidth; // bandwidth
    float writeBandwidth; // bandwidth
    float maxBandwidth; // bandwidth
    float bandwidth; // bandwidth
    float readResponseTime; // latency
    float writeResponseTime; // latency
    float maxResponseTime; // latency
    float responseTime; // lantecy

    public float getReadThroughput() {
        return readThroughput;
    }

    public void setReadThroughput(float readThroughput) {
        this.readThroughput = readThroughput;
    }

    public float getWriteThroughput() {
        return writeThroughput;
    }

    public void setWriteThroughput(float writeThroughput) {
        this.writeThroughput = writeThroughput;
    }

    public float getMaxThroughput() {
        return maxThroughput;
    }

    public void setMaxThroughput(float maxThroughput) {
        this.maxThroughput = maxThroughput;
    }

    public float getThroughput() {
        return throughput;
    }

    public void setThroughput(float throughput) {
        this.throughput = throughput;
    }

    public float getReadBandwidth() {
        return readBandwidth;
    }

    public void setReadBandwidth(float readBandwidth) {
        this.readBandwidth = readBandwidth;
    }

    public float getWriteBandwidth() {
        return writeBandwidth;
    }

    public void setWriteBandwidth(float writeBandwidth) {
        this.writeBandwidth = writeBandwidth;
    }

    public float getMaxBandwidth() {
        return maxBandwidth;
    }

    public void setMaxBandwidth(float maxBandwidth) {
        this.maxBandwidth = maxBandwidth;
    }

    public float getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(float bandwidth) {
        this.bandwidth = bandwidth;
    }

    public float getReadResponseTime() {
        return readResponseTime;
    }

    public void setReadResponseTime(float readResponseTime) {
        this.readResponseTime = readResponseTime;
    }

    public float getWriteResponseTime() {
        return writeResponseTime;
    }

    public void setWriteResponseTime(float writeResponseTime) {
        this.writeResponseTime = writeResponseTime;
    }

    public float getMaxResponseTime() {
        return maxResponseTime;
    }

    public void setMaxResponseTime(float maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
    }

    public float getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(float responseTime) {
        this.responseTime = responseTime;
    }
}
