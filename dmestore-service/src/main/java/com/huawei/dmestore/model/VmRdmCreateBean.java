package com.huawei.dmestore.model;

/**
 * VmRdmCreateBean
 *
 * @author wangxiangyong
 * @since 2020-12-08
 **/
public class VmRdmCreateBean {
    /**
     * language .
     */
    private String language;

    /**
     * createVolumesRequest .
     */
    private CreateVolumesRequest createVolumesRequest;
    /**
     * customizeVolumesRequest .
     */
    private CustomizeVolumesRequest customizeVolumesRequest;

    /**
     * compatibilityMode 兼容模式,取值：virtualMode、physicalMode
     */
    private String compatibilityMode;

    /**
     * getCreateVolumesRequest .
     *
     * @return CreateVolumesRequest .
     */
    public CreateVolumesRequest getCreateVolumesRequest() {
        return createVolumesRequest;
    }

    /**
     * setCreateVolumesRequest .
     *
     * @param param .
     */
    public void setCreateVolumesRequest(final CreateVolumesRequest param) {
        this.createVolumesRequest = param;
    }

    /**
     * getCustomizeVolumesRequest .
     *
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
    public void setCustomizeVolumesRequest(final CustomizeVolumesRequest param) {
        this.customizeVolumesRequest = param;
    }

    public String getCompatibilityMode() {
        return compatibilityMode;
    }

    public void setCompatibilityMode(String compatibilityMode) {
        this.compatibilityMode = compatibilityMode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
