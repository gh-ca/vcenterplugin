package com.dmeplugin.dmestore.model;

/**
 * @author wangxiangyong
 **/
public class VmRdmCreateBean {
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
