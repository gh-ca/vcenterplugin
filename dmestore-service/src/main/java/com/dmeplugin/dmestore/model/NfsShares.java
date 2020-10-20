package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @ClassName: NfsShare
 * @Company: GH-CA
 * @create 2020-09-03
 */
public class NfsShares {

    //名称
    private String name;
    //共享路径
    private String sharePath;
    //存储设备id
    private String storageId;
    //服务等级
    private String tierName;
    //所属dtree
    private String owningDtreeName;
    //所属文件系统名字在
    private String fsName;
    //所属dtreeid
    private String owningDtreeId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSharePath() {
        return sharePath;
    }

    public void setSharePath(String sharePath) {
        this.sharePath = sharePath;
    }

    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public String getOwningDtreeName() {
        return owningDtreeName;
    }

    public void setOwningDtreeName(String owningDtreeName) {
        this.owningDtreeName = owningDtreeName;
    }

    public String getFsName() {
        return fsName;
    }

    public void setFsName(String fsName) {
        this.fsName = fsName;
    }

    public String getOwningDtreeId() {
        return owningDtreeId;
    }

    public void setOwningDtreeId(String owningDtreeId) {
        this.owningDtreeId = owningDtreeId;
    }
}
