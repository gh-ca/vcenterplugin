package com.dmeplugin.dmestore.model;

import java.util.List;

/**
 * @author wangxiangyong
 **/
public class BestPracticeCheckRecordBean {
    private String hostSetting;
    private String recommendValue;
    private String level;
    private int count;
    private List<BestPracticeBean> hostList;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<BestPracticeBean> getHostList() {
        return hostList;
    }

    public void setHostList(List<BestPracticeBean> hostList) {
        this.hostList = hostList;
    }
}
