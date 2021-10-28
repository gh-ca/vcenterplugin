package com.huawei.dmestore.model;

import java.util.Date;

/**
 * @author yc
 * @Title:
 * @Description:
 * @date 2021/5/2518:03
 */
public class Attachment {
    private String id;
    private String volumeId;
    private String hostId;
    private String attachedAt;
    private String attachedHostGroup;
    private Date attachedAtDate;

    public Attachment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getAttachedAt() {
        return attachedAt;
    }

    public void setAttachedAt(String attachedAt) {
        this.attachedAt = attachedAt;
    }

    public String getAttachedHostGroup() {
        return attachedHostGroup;
    }

    public void setAttachedHostGroup(String attachedHostGroup) {
        this.attachedHostGroup = attachedHostGroup;
    }

    public Date getAttachedAtDate() {
        return attachedAtDate;
    }

    public void setAttachedAtDate(Date attachedAtDate) {
        this.attachedAtDate = attachedAtDate;
    }
}
