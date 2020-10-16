package com.dmeplugin.dmestore.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author wangxiangyong
 **/
public class CreateVolumesRequest {
    @JsonProperty("service_level_id")
    private String serviceLevelId;
    private List<ServiceVolumeBasicParams> volumes;
    private ServiceVolumeMapping mapping;

    public String getServiceLevelId() {
        return serviceLevelId;
    }

    public void setServiceLevelId(String serviceLevelId) {
        this.serviceLevelId = serviceLevelId;
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
