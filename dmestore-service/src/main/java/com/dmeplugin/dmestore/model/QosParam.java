package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @className QosParam
 * @description TODO
 * @date 2020/9/15 14:57
 */
public class QosParam {

    private Boolean enabled;
    private SmartQos SmartQos;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public com.dmeplugin.dmestore.model.SmartQos getSmartQos() {
        return SmartQos;
    }

    public void setSmartQos(com.dmeplugin.dmestore.model.SmartQos smartQos) {
        SmartQos = smartQos;
    }
}
