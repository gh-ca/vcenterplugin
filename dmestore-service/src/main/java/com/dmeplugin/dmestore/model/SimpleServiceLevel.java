package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @ClassName: SimpleServiceLevel
 * @Company: GH-CA
 * @create 2020-09-03
 */
public class SimpleServiceLevel{

    private SimpleCapabilities capabilities;
    private String id;
    private String name;
    private String description;
    private String type;
    private String protocol;
    private Double totalCapacity;
    private Double freeCapacity;
    private Double usedCapacity;

    public SimpleCapabilities getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(SimpleCapabilities capabilities) {
        this.capabilities = capabilities;
    }

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

    public Double getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Double totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Double getFreeCapacity() {
        return freeCapacity;
    }

    public void setFreeCapacity(Double freeCapacity) {
        this.freeCapacity = freeCapacity;
    }

    public Double getUsedCapacity() {
        return usedCapacity;
    }

    public void setUsedCapacity(Double usedCapacity) {
        this.usedCapacity = usedCapacity;
    }
}
