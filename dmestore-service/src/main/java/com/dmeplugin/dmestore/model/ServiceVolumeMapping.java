package com.dmeplugin.dmestore.model;

/**
 * @ClassName ServiceVolumeMapping
 * @Description TODO
 * @Author wangxiangyong
 * @Date 2020/9/27 16:00
 * @Version V1.0
 **/
public class ServiceVolumeMapping {
    private String host_id;
    private String hostgroup_id;

    public String getHost_id() {
        return host_id;
    }

    public void setHost_id(String host_id) {
        this.host_id = host_id;
    }

    public String getHostgroup_id() {
        return hostgroup_id;
    }

    public void setHostgroup_id(String hostgroup_id) {
        this.hostgroup_id = hostgroup_id;
    }
}