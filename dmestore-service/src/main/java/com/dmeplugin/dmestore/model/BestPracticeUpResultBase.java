package com.dmeplugin.dmestore.model;

/**
 * @ClassName BestPracticeUpResultBase
 * @Description TODO
 * @Author wangxiangyong
 * @Date 2020/9/3 9:47
 * @Version V1.0
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
