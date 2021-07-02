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

    public MountVmfsReturn(boolean flag) {
        this.flag = flag;
    }

    public MountVmfsReturn(boolean flag, List<String> failedHost) {
        this.flag = flag;
        this.failedHost = failedHost;
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
}
