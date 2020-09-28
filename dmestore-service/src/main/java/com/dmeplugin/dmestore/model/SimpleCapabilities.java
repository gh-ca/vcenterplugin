package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @ClassName: SimpleCapabilities
 * @Company: GH-CA
 * @create 2020-09-03
 */
public class SimpleCapabilities {

    private String resource_type;
    private Boolean compression;
    private Boolean deduplication;

    private CapabilitiesSmarttier smarttier;
    private QosParam qosParam;
    private CapabilitiesQos qos;

    public CapabilitiesQos getQos() {
        return qos;
    }

    public void setQos(CapabilitiesQos qos) {
        this.qos = qos;
    }

    public String getResource_type() {
        return resource_type;
    }

    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
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

    public QosParam getQosParam() {
        return qosParam;
    }

    public void setQosParam(QosParam qosParam) {
        this.qosParam = qosParam;
    }
}
