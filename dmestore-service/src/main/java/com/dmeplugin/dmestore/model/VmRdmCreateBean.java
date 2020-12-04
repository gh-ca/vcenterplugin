package com.dmeplugin.dmestore.model;

/**
 * @author wangxiangyong
 **/
public class VmRdmCreateBean {
    /**
     * createVolumesRequest .
     */
    private CreateVolumesRequest createVolumesRequest;
    /**
     * customizeVolumesRequest .
     */
    private CustomizeVolumesRequest customizeVolumesRequest;

    /**
     *  getCreateVolumesRequest .
     * @return CreateVolumesRequest .
     */
    public CreateVolumesRequest getCreateVolumesRequest() {
        return createVolumesRequest;
    }

    /**
     * setCreateVolumesRequest .
     * @param param .
     */
    public void setCreateVolumesRequest(final CreateVolumesRequest param) {
        this.createVolumesRequest = param;
    }
    /**
     *  getCustomizeVolumesRequest .
     * @return CustomizeVolumesRequest .
     */
    public CustomizeVolumesRequest getCustomizeVolumesRequest() {
        return customizeVolumesRequest;
    }

    /**
     * setCustomizeVolumesRequest .
     *
     * @param param .
     */
    public void setCustomizeVolumesRequest(
        final CustomizeVolumesRequest param) {
        this.customizeVolumesRequest = param;
    }
}
