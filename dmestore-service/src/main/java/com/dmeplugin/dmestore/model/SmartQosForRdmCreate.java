package com.dmeplugin.dmestore.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author wangxiangyong
 */
public class SmartQosForRdmCreate extends BaseSmartQos {
    @JsonProperty("control_policy")
    private String controlPolicy;

    public String getControlPolicy() {
        return controlPolicy;
    }

    public void setControlPolicy(String controlPolicy) {
        this.controlPolicy = controlPolicy;
    }
}
