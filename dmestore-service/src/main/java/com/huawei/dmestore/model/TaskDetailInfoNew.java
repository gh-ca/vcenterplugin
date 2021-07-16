package com.huawei.dmestore.model;

import java.util.List;

/**
 * @author yc
 * @Title:
 * @Description:
 * @date 2021/5/2114:10
 */
public class TaskDetailInfoNew {
   private String  id;
   private String  nameEn;
   private String  nameCn;
   private String  description;
   private String  parentId;
   private Integer  seqNo;
   private Integer  status;
   private Integer  progress;
   private String  ownerName;
   private String  ownerId;
   private long  createTime;
   private long  startTime;
   private long  endTime;
   private String  detailEn;
   private String  detailCn;
   private List<TaskDetailResource> resources;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getDetailEn() {
        return detailEn;
    }

    public void setDetailEn(String detailEn) {
        this.detailEn = detailEn;
    }

    public String getDetailCn() {
        return detailCn;
    }

    public void setDetailCn(String detailCn) {
        this.detailCn = detailCn;
    }

    public List<TaskDetailResource> getResources() {
        return resources;
    }

    public void setResources(List<TaskDetailResource> resources) {
        this.resources = resources;
    }

    public TaskDetailInfoNew() {
    }
}
