package com.dmeplugin.dmestore.model;

/**
 * @ClassName DTree
 * @Description TODO
 * @Author wangxiangyong
 * @Date 2020/9/2 17:03
 * @Version V1.0
 **/
public class DTree {
    private String device_name;
    private String fs_id;
    private String fs_name;
    private String id;
    private String name;
    private boolean quota_switch;
    private String security_style;
    private String storage_id;
    private String tier_name;

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getFs_id() {
        return fs_id;
    }

    public void setFs_id(String fs_id) {
        this.fs_id = fs_id;
    }

    public String getFs_name() {
        return fs_name;
    }

    public void setFs_name(String fs_name) {
        this.fs_name = fs_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isQuota_switch() {
        return quota_switch;
    }

    public void setQuota_switch(boolean quota_switch) {
        this.quota_switch = quota_switch;
    }

    public String getSecurity_style() {
        return security_style;
    }

    public void setSecurity_style(String security_style) {
        this.security_style = security_style;
    }

    public String getStorage_id() {
        return storage_id;
    }

    public void setStorage_id(String storage_id) {
        this.storage_id = storage_id;
    }

    public String getTier_name() {
        return tier_name;
    }

    public void setTier_name(String tier_name) {
        this.tier_name = tier_name;
    }
}
