package com.dmeplugin.dmestore.model;

import java.util.List;

/**
 * @ClassName BestPracticeReq
 * @Description 最佳实践检查请求参数bean
 * @Author wangxiangyong
 * @Date 2020/9/2 17:59
 * @Version V1.0
 **/
public class BestPracticeUpdateByTypeRequest {
    private String hostSetting;
    private List<String>  hostObjectIds;

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
