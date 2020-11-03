package com.dmeplugin.dmestore.model;

/**
 * @author wangxiangyong
 **/
public class CustomizeVolumeTuningForCreate {
    private SmartQosForRdmCreate smartqos;
    private Boolean compressionEnabled;
    private Boolean dedupeEnabled;
    private String smarttier;
    /**
     * 卷分配类型，取值范围 thin，thick
     **/
    private String alloctype;
    private Integer workloadTypeId;

    public SmartQosForRdmCreate getSmartqos() {
        return smartqos;
    }

    public void setSmartqos(SmartQosForRdmCreate smartqos) {
        this.smartqos = smartqos;
    }

    public Boolean getCompressionEnabled() {
        return compressionEnabled;
    }

    public void setCompressionEnabled(Boolean compressionEnabled) {
        this.compressionEnabled = compressionEnabled;
    }

    public Boolean getDedupeEnabled() {
        return dedupeEnabled;
    }

    public void setDedupeEnabled(Boolean dedupeEnabled) {
        this.dedupeEnabled = dedupeEnabled;
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

    public Integer getWorkloadTypeId() {
        return workloadTypeId;
    }

    public void setWorkloadTypeId(Integer workloadTypeId) {
        this.workloadTypeId = workloadTypeId;
    }
}
