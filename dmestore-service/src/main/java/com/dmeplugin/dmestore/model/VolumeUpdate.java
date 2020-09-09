package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @className VolumeUpdate
 * @description TODO
 * @date 2020/9/9 11:46
 */
public class VolumeUpdate {

     private String name;
     private String owner_controller;
     private String prefetch_policy;
     private String prefetch_value;
     private CustomizeVolumeTuning tuning;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner_controller() {
        return owner_controller;
    }

    public void setOwner_controller(String owner_controller) {
        this.owner_controller = owner_controller;
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

    public CustomizeVolumeTuning getTuning() {
        return tuning;
    }

    public void setTuning(CustomizeVolumeTuning tuning) {
        this.tuning = tuning;
    }
}
