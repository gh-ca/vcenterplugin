package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @ClassName: CapabilitiesQos
 * @Company: GH-CA
 * @create 2020-09-03
 */
public class CapabilitiesQos {

    private SmartQos smartQos;

    private QosParam qosParam;

    private Boolean enabled;

    public SmartQos getSmartQos() {
        return smartQos;
    }

    public QosParam getQosParam() {
        return qosParam;
    }

    public void setQosParam(QosParam qosParam) {
        this.qosParam = qosParam;
    }

    public void setSmartQos(SmartQos smartQos) {
        this.smartQos = smartQos;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
