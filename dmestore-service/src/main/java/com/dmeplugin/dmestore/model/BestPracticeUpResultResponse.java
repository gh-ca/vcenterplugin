package com.dmeplugin.dmestore.model;

import java.util.List;

/**
 * @author wangxiangyong
 *
 **/
public class BestPracticeUpResultResponse {
    private String hostObjectId;
    private String hostName;
    private List<BestPracticeUpResultBase> result;
    private boolean needReboot;

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

    public List<BestPracticeUpResultBase> getResult() {
        return result;
    }

    public void setResult(List<BestPracticeUpResultBase> result) {
        this.result = result;
    }

    public boolean getNeedReboot() {
        return needReboot;
    }

    public void setNeedReboot(boolean needReboot) {
        this.needReboot = needReboot;
    }
}
