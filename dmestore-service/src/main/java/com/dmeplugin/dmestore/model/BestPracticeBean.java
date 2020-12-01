package com.dmeplugin.dmestore.model;

/**
 * @author wangxiangyong
 **/
public class BestPracticeBean {
    private String hostSetting;

    private String recommendValue;

    private String level;

    private String actualValue;

    private String needReboot;

    private String hostObjectId;

    private String hostName;

    private String autoRepair;

    public String getHostSetting() {
        return hostSetting;
    }

    public void setHostSetting(String hostSetting) {
        this.hostSetting = hostSetting;
    }

    public String getRecommendValue() {
        return recommendValue;
    }

    public void setRecommendValue(String recommendValue) {
        this.recommendValue = recommendValue;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getActualValue() {
        return actualValue;
    }

    public void setActualValue(String actualValue) {
        this.actualValue = actualValue;
    }

    public String getNeedReboot() {
        return needReboot;
    }

    public void setNeedReboot(String needReboot) {
        this.needReboot = needReboot;
    }

    public String getHostObjectId() {
        return hostObjectId;
    }

    public void setHostObjectId(String hostObjectId) {
        this.hostObjectId = hostObjectId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getAutoRepair() {
        return autoRepair;
    }

    public void setAutoRepair(String autoRepair) {
        this.autoRepair = autoRepair;
    }
}
