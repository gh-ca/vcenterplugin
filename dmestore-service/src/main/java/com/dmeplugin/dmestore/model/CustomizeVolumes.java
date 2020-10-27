package com.dmeplugin.dmestore.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author wangxiangyong
 **/
public class CustomizeVolumes {
    @JsonProperty("availability_zone")
    private String availabilityZone;
    /**
     * 0：自动，1：高性能层，2：性能层，3：容量层 默认值：自动
     **/
    @JsonProperty("initial_distribute_policy")
    private String initialDistributePolicy;

    /**
     * OA、OB，默认为空代表自动
     **/
    @JsonProperty("owner_controller")
    private String ownerController;
    @JsonProperty("pool_raw_id")
    private String poolRawId;
    /**
     * 预取策略，影响磁盘读取。取值范围 0: 不预取，1：固定预取，2：可变预取，3：智能预取 默认值：智能预取
     **/
    @JsonProperty("prefetch_policy")
    private String prefetchPolicy;
    @JsonProperty("prefetch_value")
    private String prefetchValue;
    @JsonProperty("storage_id")
    private String storageId;
    private CustomizeVolumeTuningForCreate tuning;
    @JsonProperty("volume_specs")
    private List<ServiceVolumeBasicParams> volumeSpecs;

    public String getAvailabilityZone() {
        return availabilityZone;
    }

    public void setAvailabilityZone(String availabilityZone) {
        this.availabilityZone = availabilityZone;
    }

    public String getInitialDistributePolicy() {
        return initialDistributePolicy;
    }

    public void setInitialDistributePolicy(String initialDistributePolicy) {
        this.initialDistributePolicy = initialDistributePolicy;
    }

    public String getOwnerController() {
        return ownerController;
    }

    public void setOwnerController(String ownerController) {
        this.ownerController = ownerController;
    }

    public String getPoolRawId() {
        return poolRawId;
    }

    public void setPoolRawId(String poolRawId) {
        this.poolRawId = poolRawId;
    }

    public String getPrefetchPolicy() {
        return prefetchPolicy;
    }

    public void setPrefetchPolicy(String prefetchPolicy) {
        this.prefetchPolicy = prefetchPolicy;
    }

    public String getPrefetchValue() {
        return prefetchValue;
    }

    public void setPrefetchValue(String prefetchValue) {
        this.prefetchValue = prefetchValue;
    }

    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }

    public CustomizeVolumeTuningForCreate getTuning() {
        return tuning;
    }

    public void setTuning(CustomizeVolumeTuningForCreate tuning) {
        this.tuning = tuning;
    }

    public List<ServiceVolumeBasicParams> getVolumeSpecs() {
        return volumeSpecs;
    }

    public void setVolumeSpecs(List<ServiceVolumeBasicParams> volumeSpecs) {
        this.volumeSpecs = volumeSpecs;
    }
}
