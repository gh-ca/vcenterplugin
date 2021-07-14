package com.huawei.dmestore.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author yc
 * @Title:
 * @Description:
 * @date 2021/7/115:20
 */
public class MountVmfsReturn implements Serializable {
    private static final long serialVersionUID = -977833815757933935L;
    private boolean flag;
    private List<String> failedHost;
    private List<String> connectionFail;
    private String description;
    public MountVmfsReturn(boolean flag) {
        this.flag = flag;
    }

    public MountVmfsReturn(boolean flag, List<String> failedHost) {
        this.flag = flag;
        this.failedHost = failedHost;
    }

    public MountVmfsReturn(boolean flag, List<String> failedHost, String description) {
        this.flag = flag;
        this.failedHost = failedHost;
        this.description = description;
    }

    public MountVmfsReturn(boolean flag, List<String> failedHost, List<String> connectionFail, String description) {
        this.flag = flag;
        this.failedHost = failedHost;
        this.connectionFail = connectionFail;
        this.description = description;
    }

    public MountVmfsReturn(boolean flag, String description, List<String> connectionFail) {
        this.flag = flag;
        this.connectionFail = connectionFail;
        this.description = description;
    }

    public MountVmfsReturn(boolean flag, String description) {
        this.flag = flag;
        this.description = description;
    }



    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public List<String> getFailedHost() {
        return failedHost;
    }

    public void setFailedHost(List<String> failedHost) {
        this.failedHost = failedHost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getConnectionFail() {
        return connectionFail;
    }

    public void setConnectionFail(List<String> connectionFail) {
        this.connectionFail = connectionFail;
    }
}
