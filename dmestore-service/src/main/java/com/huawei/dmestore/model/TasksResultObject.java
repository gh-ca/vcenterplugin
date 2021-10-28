package com.huawei.dmestore.model;

/**
 * @author yc
 * @Title:
 * @Description:
 * @date 2021/5/2110:42
 */
public class TasksResultObject {
    //为true表示全部成功，为false表示部分成功或未成功
    private boolean status;
    //成功的数量
    private long successNos;
    //失败的数量
    private long failNos;

    private int totalSize;

    private String description;

    public TasksResultObject() {

    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getSuccessNos() {
        return successNos;
    }

    public void setSuccessNos(long successNos) {
        this.successNos = successNos;
    }

    public long getFailNos() {
        return failNos;
    }

    public void setFailNos(long failNos) {
        this.failNos = failNos;
    }

    public TasksResultObject(boolean status) {
        this.status = status;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
