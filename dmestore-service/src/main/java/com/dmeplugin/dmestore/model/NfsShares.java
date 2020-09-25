package com.dmeplugin.dmestore.model;

import java.io.Serializable;
/**
 * @author lianq
 * @ClassName: NfsShare
 * @Company: GH-CA
 * @create 2020-09-03
 */
public class NfsShares implements Serializable {

    private String name; //名称
    private String share_path; //共享路径
    private String storage_id; //存储设备id
    private String tier_name; //服务等级
    private String owning_dtree_name;//所属dtree
    private String fs_name; //所属文件系统名字在
    private String owning_dtree_id; //所属dtreeid

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShare_path() {
        return share_path;
    }

    public void setShare_path(String share_path) {
        this.share_path = share_path;
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

    public String getOwning_dtree_name() {
        return owning_dtree_name;
    }

    public void setOwning_dtree_name(String owning_dtree_name) {
        this.owning_dtree_name = owning_dtree_name;
    }

    public String getFs_name() {
        return fs_name;
    }

    public void setFs_name(String fs_name) {
        this.fs_name = fs_name;
    }

    public String getOwning_dtree_id() {
        return owning_dtree_id;
    }

    public void setOwning_dtree_id(String owning_dtree_id) {
        this.owning_dtree_id = owning_dtree_id;
    }
}
