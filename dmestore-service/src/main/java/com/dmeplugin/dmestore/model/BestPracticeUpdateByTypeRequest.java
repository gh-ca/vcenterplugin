package com.dmeplugin.dmestore.model;

import java.util.List;

/**
 * @author wangxiangyong
 **/
public class BestPracticeUpdateByTypeRequest {
    /**
     * hostSetting .
     */
    private String hostSetting;
    /**
     * hostObjectIds .
     */
    private List<String> hostObjectIds;

    /**
     * getHostSetting .
     * @return String .
     */
    public String getHostSetting() {
        return hostSetting;
    }
    /**
     * setHostObjectIds .
     * @param param .
     */
    public void setHostSetting(final String param) {
        this.hostSetting = param;
    }
    /**
     * getHostObjectIds .
     * @return List<String> .
     */
    public List<String> getHostObjectIds() {
        return hostObjectIds;
    }

    /**
     * setHostObjectIds .
     * @param param .
     */
    public void setHostObjectIds(final List<String> param) {
        this.hostObjectIds = param;
    }

    @Override
    public final String toString() {
        return "BestPracticeUpdateByTypeRequest{"
            + "hostSetting='" + hostSetting + '\''
            + ", hostObjectIds=" + hostObjectIds
            + '}';
    }
}
