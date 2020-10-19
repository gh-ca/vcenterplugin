package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @ClassName: SimpleCapabilities
 * @Company: GH-CA
 * @create 2020-09-03
 */
public class SimpleCapabilities {

    private String resourceType;
    private Boolean compression;
    private Boolean deduplication;

    private CapabilitiesSmarttier smarttier;
    private CapabilitiesIopriority iopriority;
    private CapabilitiesQos qos;

    public CapabilitiesIopriority getIopriority() {
        return iopriority;
    }

    public void setIopriority(CapabilitiesIopriority iopriority) {
        this.iopriority = iopriority;
    }

    public CapabilitiesQos getQos() {
        return qos;
    }

    public void setQos(CapabilitiesQos qos) {
        this.qos = qos;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Boolean getCompression() {
        return compression;
    }

    public void setCompression(Boolean compression) {
        this.compression = compression;
    }

    public Boolean getDeduplication() {
        return deduplication;
    }

    public void setDeduplication(Boolean deduplication) {
        this.deduplication = deduplication;
    }

    public CapabilitiesSmarttier getSmarttier() {
        return smarttier;
    }

    public void setSmarttier(CapabilitiesSmarttier smarttier) {
        this.smarttier = smarttier;
    }

}
