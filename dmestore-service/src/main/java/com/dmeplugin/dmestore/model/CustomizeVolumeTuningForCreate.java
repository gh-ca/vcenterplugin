package com.dmeplugin.dmestore.model;

/**
 * @Author wangxiangyong
 * @Description //TODO 
 * @Date 17:47 2020/9/27
 * @Param 
 * @Return 
 **/
public class CustomizeVolumeTuningForCreate {
    private SmartQos smartqos;
    private Boolean compression_enabled;
    private Boolean dedupe_enabled;
    //数据迁移策略，取值范围 0：不迁移，1：自动迁移，2：向高性能层迁移，3：向低性能层迁移  默认值：不迁移
    private Integer smarttier;
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

    public Integer getSmarttier() {
        return smarttier;
    }

    public void setSmarttier(Integer smarttier) {
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
