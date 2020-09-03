package com.dmeplugin.dmestore.entity;

public class CustomizeVolumeTuning {

    private SmartQos smartQos;
    private Boolean compressionEnabled;
    private Boolean dedupeEnabled;
    //数据迁移策略，取值范围 0：不迁移，1：自动迁移，2：向高性能层迁移，3：向低性能层迁移  默认值：不迁移
    private Integer smarttier = 0;

    public SmartQos getSmartQos() {
        return smartQos;
    }

    public void setSmartQos(SmartQos smartQos) {
        this.smartQos = smartQos;
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

    public Integer getSmarttier() {
        return smarttier;
    }

    public void setSmarttier(Integer smarttier) {
        this.smarttier = smarttier;
    }

    @Override
    public String toString() {
        return "CustomizeVolumeTuning{" +
                "smartQos=" + smartQos +
                ", compressionEnabled=" + compressionEnabled +
                ", dedupeEnabled=" + dedupeEnabled +
                ", smarttier=" + smarttier +
                '}';
    }
}
