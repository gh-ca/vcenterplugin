package com.dmeplugin.dmestore.model;
/**
 * @author lianq
 * @ClassName: CustomizeVolumeTuning
 * @Company: GH-CA
 * @create 2020-09-03
 */
public class CustomizeVolumeTuning {

    private QosPolicy smartQos;
    private Boolean compression_enabled;
    private Boolean dedupe_enabled;
    //数据迁移策略，取值范围 0：不迁移，1：自动迁移，2：向高性能层迁移，3：向低性能层迁移  默认值：不迁移
    private Integer smarttier;

    public QosPolicy getSmartQos() {
        return smartQos;
    }

    public void setSmartQos(QosPolicy smartQos) {
        this.smartQos = smartQos;
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
}
