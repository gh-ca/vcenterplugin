package com.dmeplugin.dmestore.model;

/**
 * @author wangxiangyong
 **/
public class BestPracticeUpResultBase {
    private String hostSetting;
    private boolean needReboot;
    private String hostObjectId;
    private boolean updateResult;

    public String getHostSetting() {
        return hostSetting;
    }

    public void setHostSetting(String hostSetting) {
        this.hostSetting = hostSetting;
    }

    public boolean getNeedReboot() {
        return needReboot;
    }

    public void setNeedReboot(boolean needReboot) {
        this.needReboot = needReboot;
    }

    public String getHostObjectId() {
        return hostObjectId;
    }

    public void setHostObjectId(String hostObjectId) {
        this.hostObjectId = hostObjectId;
    }

    public boolean getUpdateResult() {
        return updateResult;
    }

    public void setUpdateResult(boolean updateResult) {
        this.updateResult = updateResult;
    }
}
