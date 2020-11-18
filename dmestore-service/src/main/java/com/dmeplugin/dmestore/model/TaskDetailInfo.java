package com.dmeplugin.dmestore.model;

import java.util.List;

/**
 * @Description: TODO
 * @ClassName: TaskDetailInfo
 * @Company: GH-CA
 * @author: liuxh
 * @create: 2020-09-08
 **/
public class TaskDetailInfo {
    // taskID
    String id;
    // task name;
    String taskName;
    int status;
    int progress;
    String ownerName;
    long createTiem;
    long startTime;
    long endTime;
    String detail;
    List<TaskDetailResource> resources;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public long getCreateTiem() {
        return createTiem;
    }

    public void setCreateTiem(long createTiem) {
        this.createTiem = createTiem;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<TaskDetailResource> getResources() {
        return resources;
    }

    public void setResources(List<TaskDetailResource> resources) {
        this.resources = resources;
    }
}
