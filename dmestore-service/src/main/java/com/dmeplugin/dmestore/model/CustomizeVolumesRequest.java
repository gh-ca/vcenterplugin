package com.dmeplugin.dmestore.model;

/**
 * @ClassName CustomizeVolumesRequest
 * @Description 非服务等级创建卷请求bean
 * @Author wangxiangyong
 * @Date 2020/9/27 16:14
 * @Version V1.0
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
