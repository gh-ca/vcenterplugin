package com.dmeplugin.dmestore.model;

import java.util.List;

/**
 * @author wangxiangyong
 **/
public class CustomizeVolumes {
    private String availability_zone;
    private String initial_distribute_policy;
    private String owner_controller;
    private String pool_raw_id;
    private String prefetch_policy;
    private String prefetch_value;
    //必须
    private String storage_id;
    private CustomizeVolumeTuningForCreate tuning;
    private List<ServiceVolumeBasicParams> volume_specs;

    public String getAvailability_zone() {
        return availability_zone;
    }

    public void setAvailability_zone(String availability_zone) {
        this.availability_zone = availability_zone;
    }

    public String getInitial_distribute_policy() {
        return initial_distribute_policy;
    }

    public void setInitial_distribute_policy(String initial_distribute_policy) {
        this.initial_distribute_policy = initial_distribute_policy;
    }

    public String getOwner_controller() {
        return owner_controller;
    }

    public void setOwner_controller(String owner_controller) {
        this.owner_controller = owner_controller;
    }

    public String getPool_raw_id() {
        return pool_raw_id;
    }

    public void setPool_raw_id(String pool_raw_id) {
        this.pool_raw_id = pool_raw_id;
    }

    public String getPrefetch_policy() {
        return prefetch_policy;
    }

    public void setPrefetch_policy(String prefetch_policy) {
        this.prefetch_policy = prefetch_policy;
    }

    public String getPrefetch_value() {
        return prefetch_value;
    }

    public void setPrefetch_value(String prefetch_value) {
        this.prefetch_value = prefetch_value;
    }

    public String getStorage_id() {
        return storage_id;
    }

    public void setStorage_id(String storage_id) {
        this.storage_id = storage_id;
    }

    public CustomizeVolumeTuningForCreate getTuning() {
        return tuning;
    }

    public void setTuning(CustomizeVolumeTuningForCreate tuning) {
        this.tuning = tuning;
    }

    public List<ServiceVolumeBasicParams> getVolume_specs() {
        return volume_specs;
    }

    public void setVolume_specs(List<ServiceVolumeBasicParams> volume_specs) {
        this.volume_specs = volume_specs;
    }
}
