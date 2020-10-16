package com.dmeplugin.dmestore.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author wangxiangyong
 **/
public class ServiceVolumeMapping {
    @JsonProperty("host_id")
    private String hostId;
    @JsonProperty("hostgroup_id")
    private String hostgroupId;

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getHostgroupId() {
        return hostgroupId;
    }

    public void setHostgroupId(String hostgroupId) {
        this.hostgroupId = hostgroupId;
    }
}
