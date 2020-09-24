package com.dmeplugin.dmestore.model;

import java.util.List;

/**
 * @ClassName BestPracticeUpResultBase
 * @Description TODO
 * @Author wangxiangyong
 * @Date 2020/9/3 9:47
 * @Version V1.0
 **/
public class BestPracticeUpResultResponse {
    private String hostObjectId;
    private List<BestPracticeUpResultBase> result;
    private boolean needReboot;

    public String getHostObjectId() {
        return hostObjectId;
    }

    public void setHostObjectId(String hostObjectId) {
        this.hostObjectId = hostObjectId;
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
