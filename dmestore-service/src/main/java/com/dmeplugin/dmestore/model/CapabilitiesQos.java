package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @ClassName: CapabilitiesQos
 * @Company: GH-CA
 * @create 2020-09-03
 */
public class CapabilitiesQos {

    private QosParam qosParam;
    private Boolean enabled;

    public QosParam getQosParam() {
        return qosParam;
    }

    public void setQosParam(QosParam qosParam) {
        this.qosParam = qosParam;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
