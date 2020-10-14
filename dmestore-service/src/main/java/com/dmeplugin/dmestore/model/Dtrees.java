package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @className DtreeList
 * @description TODO
 * @date 2020/9/7 9:42
 */
public class Dtrees {

    private String name;
    //所属文件系统名称
    private String fs_name;
    //配额
    private boolean quota_switch;
    //安全模式
    private String security_style;
    //服务等级名称
    private String tier_name;
    //nfs
    private Integer nfs_count;
    private Integer cifs_count;


    public Integer getCifs_count() {
        return cifs_count;
    }

    public void setCifs_count(Integer cifs_count) {
        this.cifs_count = cifs_count;
    }

    public String getFs_name() {
        return fs_name;
    }

    public void setFs_name(String fs_name) {
        this.fs_name = fs_name;
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

    public String getTier_name() {
        return tier_name;
    }

    public void setTier_name(String tier_name) {
        this.tier_name = tier_name;
    }

    public Integer getNfs_count() {
        return nfs_count;
    }

    public void setNfs_count(Integer nfs_count) {
        this.nfs_count = nfs_count;
    }
}
