package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @className QosParam
 * @description TODO
 * @date 2020/9/15 14:57
 */
public class QosParam {

    private Boolean enabled;
    private QosPolicy qosPolicy;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public QosPolicy getQosPolicy() {

        return qosPolicy;
    }

    public void setQosPolicy(QosPolicy qosPolicy) {
        this.qosPolicy = qosPolicy;
    }
}
