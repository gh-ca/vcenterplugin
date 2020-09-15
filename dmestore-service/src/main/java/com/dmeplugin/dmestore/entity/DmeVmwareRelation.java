package com.dmeplugin.dmestore.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: TODO
 * @ClassName: DmeVmwareRelation
 * @Company: GH-CA
 * @author: yy
 * @create: 2020-09-02
 **/
public class DmeVmwareRelation implements Serializable {

  private int id;
  private String storeId;
  private String storeName;
  private String volumeId;
  private String volumeName;
  private String volumeWwn;
  private String volumeShare;
  private String volumeFs;

  private String shareId;
  private String shareName;
  private String fsId;
  private String fsName;
  private String logicPortId;
  private String logicPortName;

  private String storeType;
  private Date createTime;
  private Date updateTime;
  private int state;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getStoreId() {
    return storeId;
  }

  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }

  public String getStoreName() {
    return storeName;
  }

  public void setStoreName(String storeName) {
    this.storeName = storeName;
  }

  public String getVolumeId() {
    return volumeId;
  }

  public void setVolumeId(String volumeId) {
    this.volumeId = volumeId;
  }

  public String getVolumeName() {
    return volumeName;
  }

  public void setVolumeName(String volumeName) {
    this.volumeName = volumeName;
  }

  public String getVolumeWwn() {
    return volumeWwn;
  }

  public void setVolumeWwn(String volumeWwn) {
    this.volumeWwn = volumeWwn;
  }

  public String getVolumeShare() {
    return volumeShare;
  }

  public void setVolumeShare(String volumeShare) {
    this.volumeShare = volumeShare;
  }

  public String getVolumeFs() {
    return volumeFs;
  }

  public void setVolumeFs(String volumeFs) {
    this.volumeFs = volumeFs;
  }

  public String getShareId() {
    return shareId;
  }

  public void setShareId(String shareId) {
    this.shareId = shareId;
  }

  public String getShareName() {
    return shareName;
  }

  public void setShareName(String shareName) {
    this.shareName = shareName;
  }

  public String getFsId() {
    return fsId;
  }

  public void setFsId(String fsId) {
    this.fsId = fsId;
  }

  public String getFsName() {
    return fsName;
  }

  public void setFsName(String fsName) {
    this.fsName = fsName;
  }

  public String getLogicPortId() {
    return logicPortId;
  }

  public void setLogicPortId(String logicPortId) {
    this.logicPortId = logicPortId;
  }

  public String getLogicPortName() {
    return logicPortName;
  }

  public void setLogicPortName(String logicPortName) {
    this.logicPortName = logicPortName;
  }

  public String getStoreType() {
    return storeType;
  }

  public void setStoreType(String storeType) {
    this.storeType = storeType;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }
}
