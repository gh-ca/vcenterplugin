package com.dmeplugin.dmestore.model;

import java.util.List;

/**
 * @author wangxiangyong
 **/
public class BestPracticeUpdateByTypeRequest {
    private String hostSetting;

    private List<String> hostObjectIds;

    public String getHostSetting() {
        return hostSetting;
    }

    public void setHostSetting(String hostSetting) {
        this.hostSetting = hostSetting;
    }

    public List<String> getHostObjectIds() {
        return hostObjectIds;
    }

    public void setHostObjectIds(List<String> hostObjectIds) {
        this.hostObjectIds = hostObjectIds;
    }
}
