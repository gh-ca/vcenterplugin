package com.dmeplugin.dmestore.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author wangxiangyong
 */
public class SmartQosForRdmCreate extends BaseSmartQos {
    /**
     * 控制策略， 0：保护IO下限，1：控制IO上限
     **/
    @JsonProperty("control_policy")
    private String controlPolicy;

    public String getControlPolicy() {
        return controlPolicy;
    }

    public void setControlPolicy(String controlPolicy) {
        this.controlPolicy = controlPolicy;
    }
}
