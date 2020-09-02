package com.dmeplugin.dmestore.model;

/**
 * @Description: TODO
 * @ClassName: ServerLevelnfo
 * @Company: GH-CA
 * @author: liuxh
 * @create: 2020-09-01
 **/
public class ServiceLevelInfo {
    private String id;
    private String name;
    private String description;
    private String type;// the value:file block
    private String protocol;// the value:FC iSCSI
    private double totalCapacity;// total capacity,unit GB
    private double usedCapacity;// used capacity
    private double freeCapacity;// free capacity
    private int latency;// the response time
    private String latencyUnit;// defalut value 'ms'
    private int minBandWidth;
    private int maxBandWidth;
    private int minIOPS;
    private int maxIOPS;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public double getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(double totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public double getUsedCapacity() {
        return usedCapacity;
    }

    public void setUsedCapacity(double usedCapacity) {
        this.usedCapacity = usedCapacity;
    }

    public double getFreeCapacity() {
        return freeCapacity;
    }

    public void setFreeCapacity(double freeCapacity) {
        this.freeCapacity = freeCapacity;
    }

    public int getLatency() {
        return latency;
    }

    public int getMinBandWidth() {
        return minBandWidth;
    }

    public void setMinBandWidth(int minBandWidth) {
        this.minBandWidth = minBandWidth;
    }

    public int getMaxBandWidth() {
        return maxBandWidth;
    }

    public void setMaxBandWidth(int maxBandWidth) {
        this.maxBandWidth = maxBandWidth;
    }

    public int getMinIOPS() {
        return minIOPS;
    }

    public void setMinIOPS(int minIOPS) {
        this.minIOPS = minIOPS;
    }

    public int getMaxIOPS() {
        return maxIOPS;
    }

    public void setMaxIOPS(int maxIOPS) {
        this.maxIOPS = maxIOPS;
    }

    public void setLatency(int latency) {
        this.latency = latency;
    }

    public String getLatencyUnit() {
        return latencyUnit;
    }

    public void setLatencyUnit(String latencyUnit) {
        this.latencyUnit = latencyUnit;
    }
}
