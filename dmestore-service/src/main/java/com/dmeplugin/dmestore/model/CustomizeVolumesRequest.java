package com.dmeplugin.dmestore.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author wangxiangyong
 **/
public class CustomizeVolumesRequest {
    @JsonProperty("customize_volumes")
    private CustomizeVolumes customizeVolumes;
    private ServiceVolumeMapping mapping;

    public CustomizeVolumes getCustomizeVolumes() {
        return customizeVolumes;
    }

    public void setCustomizeVolumes(CustomizeVolumes customizeVolumes) {
        this.customizeVolumes = customizeVolumes;
    }

    public ServiceVolumeMapping getMapping() {
        return mapping;
    }

    public void setMapping(ServiceVolumeMapping mapping) {
        this.mapping = mapping;
    }
}
