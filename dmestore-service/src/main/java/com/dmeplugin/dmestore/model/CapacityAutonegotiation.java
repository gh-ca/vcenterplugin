package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @className CapacityAutonegotiation
 * @description TODO
 * @date 2020/10/15 16:28
 */
public class CapacityAutonegotiation {

    public static String capacitymodeoff = "off";

    public static String capacitymodeauto = "grow_shrink";

    private Boolean autoSizeEnable;

    public Boolean getAutoSizeEnable() {
        return autoSizeEnable;
    }

    public void setAutoSizeEnable(Boolean autoSizeEnable) {
        this.autoSizeEnable = autoSizeEnable;
    }
}
