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
    private Double total_capacity;
    private Double free_capacity;
    private Double used_capacity;

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

    public Double getTotal_capacity() {
        return total_capacity;
    }

    public void setTotal_capacity(Double total_capacity) {
        this.total_capacity = total_capacity;
    }

    public Double getFree_capacity() {
        return free_capacity;
    }

    public void setFree_capacity(Double free_capacity) {
        this.free_capacity = free_capacity;
    }

    public Double getUsed_capacity() {
        return used_capacity;
    }

    public void setUsed_capacity(Double used_capacity) {
        this.used_capacity = used_capacity;
    }
}
