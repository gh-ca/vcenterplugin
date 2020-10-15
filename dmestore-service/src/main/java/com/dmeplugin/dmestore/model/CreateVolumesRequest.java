package com.dmeplugin.dmestore.model;

import java.util.List;

/**
 * @author wangxiangyong
 **/
public class CreateVolumesRequest {
    private String service_level_id;
    private List<ServiceVolumeBasicParams> volumes;
    private ServiceVolumeMapping mapping;

    public String getService_level_id() {
        return service_level_id;
    }

    public void setService_level_id(String service_level_id) {
        this.service_level_id = service_level_id;
    }

    public List<ServiceVolumeBasicParams> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<ServiceVolumeBasicParams> volumes) {
        this.volumes = volumes;
    }

    public ServiceVolumeMapping getMapping() {
        return mapping;
    }

    public void setMapping(ServiceVolumeMapping mapping) {
        this.mapping = mapping;
    }
}
