package com.dmeplugin.dmestore.model;

/**
 * @ClassName VmRDMCreateBean
 * @Description DME卷创建请求Bean
 * @Author wangxiangyong
 * @Date 2020/9/2 16:48
 * @Version V1.0
 **/
public class VmRDMCreateBean {
    private CreateVolumesRequest createVolumesRequest;
    private CustomizeVolumesRequest customizeVolumesRequest;

    public CreateVolumesRequest getCreateVolumesRequest() {
        return createVolumesRequest;
    }

    public void setCreateVolumesRequest(CreateVolumesRequest createVolumesRequest) {
        this.createVolumesRequest = createVolumesRequest;
    }

    public CustomizeVolumesRequest getCustomizeVolumesRequest() {
        return customizeVolumesRequest;
    }

    public void setCustomizeVolumesRequest(CustomizeVolumesRequest customizeVolumesRequest) {
        this.customizeVolumesRequest = customizeVolumesRequest;
    }
}
