package com.dmeplugin.dmestore.model;

/**
 * @ClassName BestPracticeBean
 * @Description TODO
 * @Author wangxiangyong
 * @Date 2020/9/3 9:47
 * @Version V1.0
 **/
public class BestPracticeBean {
    private String hostSetting;
    private String recommendValue;
    private String level;

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
}
