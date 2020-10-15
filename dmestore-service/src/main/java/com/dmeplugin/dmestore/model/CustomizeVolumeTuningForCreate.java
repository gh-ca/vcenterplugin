package com.dmeplugin.dmestore.model;

/**
 * @author wangxiangyong
 **/
public class CustomizeVolumeTuningForCreate {
    private SmartQos smartqos;
    private Boolean compression_enabled;
    private Boolean dedupe_enabled;
    private String smarttier;
    private String alloctype;
    private Integer workload_type_id;

    public SmartQos getSmartqos() {
        return smartqos;
    }

    public void setSmartqos(SmartQos smartqos) {
        this.smartqos = smartqos;
    }

    public Boolean getCompression_enabled() {
        return compression_enabled;
    }

    public void setCompression_enabled(Boolean compression_enabled) {
        this.compression_enabled = compression_enabled;
    }

    public Boolean getDedupe_enabled() {
        return dedupe_enabled;
    }

    public void setDedupe_enabled(Boolean dedupe_enabled) {
        this.dedupe_enabled = dedupe_enabled;
    }

    public String getSmarttier() {
        return smarttier;
    }

    public void setSmarttier(String smarttier) {
        this.smarttier = smarttier;
    }

    public String getAlloctype() {
        return alloctype;
    }

    public void setAlloctype(String alloctype) {
        this.alloctype = alloctype;
    }

    public Integer getWorkload_type_id() {
        return workload_type_id;
    }

    public void setWorkload_type_id(Integer workload_type_id) {
        this.workload_type_id = workload_type_id;
    }
}
