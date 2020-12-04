package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @since  2020/10/15 16:28
 */
public class CapacityAutonegotiation {
    /**
     * capacitymodeoff .
     */
    public static String capacitymodeoff = "off";
    /**
     * capacitymodeauto .
     */
    public static String capacitymodeauto = "grow_shrink";

    /**
     * autoSizeEnable .
     */
    private Boolean autoSizeEnable;

    /**
     * getAutoSizeEnable .
     * @return Boolean .
     */
    public Boolean getAutoSizeEnable() {
        return autoSizeEnable;
    }

    /**
     * setAutoSizeEnable .
     * @param param .
     */
    public void setAutoSizeEnable(final Boolean param) {
        this.autoSizeEnable = param;
    }
}
