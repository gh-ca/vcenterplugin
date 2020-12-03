package com.dmeplugin.dmestore.model;

/**
 * xxx
 *
 * @author ghca
 *
 * @since 2020-12-01
 *
 **/
public class CapabilitiesIopriority {
    /**
     * IO优先级，默认false
     */
    private Boolean enabled;
    /**
     * IO优先级枚举值, 取值范围：1：低；2：中；3：高
     */
    private Integer policy;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getPolicy() {
        return policy;
    }

    public void setPolicy(Integer policy) {
        this.policy = policy;
    }

    @Override
    public String toString() {
        return "CapabilitiesIopriority{" +
            "enabled=" + enabled +
            ", policy=" + policy +
            '}';
    }
}
