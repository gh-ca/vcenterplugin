package com.dmeplugin.dmestore.model;

/**
 * @author wangxiangyong
 **/
public class CustomizeVolumesRequest {
    private CustomizeVolumes customize_volumes;
    private ServiceVolumeMapping mapping;

    public CustomizeVolumes getCustomize_volumes() {
        return customize_volumes;
    }

    public void setCustomize_volumes(CustomizeVolumes customize_volumes) {
        this.customize_volumes = customize_volumes;
    }

    public ServiceVolumeMapping getMapping() {
        return mapping;
    }

    public void setMapping(ServiceVolumeMapping mapping) {
        this.mapping = mapping;
    }
}
