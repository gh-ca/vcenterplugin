package com.dmeplugin.dmestore.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author lianq
 * @className VolumeUpdate
 * @description TODO
 * @date 2020/9/9 11:46
 */
public class VolumeUpdate {

     private String name;
    @JsonProperty("owner_controller")
     private String ownerController;
    @JsonProperty("prefetch_policy")
     private String prefetchPolicy;
    @JsonProperty("prefetch_value")
     private String prefetchValue;
     private CustomizeVolumeTuning tuning;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerController() {
        return ownerController;
    }

    public void setOwnerController(String ownerController) {
        this.ownerController = ownerController;
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

    public CustomizeVolumeTuning getTuning() {
        return tuning;
    }

    public void setTuning(CustomizeVolumeTuning tuning) {
        this.tuning = tuning;
    }

    @Override
    public String toString() {
        return "VolumeUpdate{" +
            "name='" + name + '\'' +
            ", ownerController='" + ownerController + '\'' +
            ", prefetchPolicy='" + prefetchPolicy + '\'' +
            ", prefetchValue='" + prefetchValue + '\'' +
            ", tuning=" + tuning +
            '}';
    }
}
