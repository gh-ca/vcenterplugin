package com.dmeplugin.dmestore.model;

/**
 * @author wangxiangyong
 **/
public class CustomizeVolumesRequest {
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
