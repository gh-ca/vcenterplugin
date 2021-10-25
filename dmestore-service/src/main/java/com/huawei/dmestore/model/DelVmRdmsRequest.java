package com.huawei.dmestore.model;

/**
 * @author wangxiangyong
 * @Description: TODO
 * @date 2021/8/13 11:15
 */
public class DelVmRdmsRequest {
    private String diskObjectId;
    private String lunWwn;

    public String getDiskObjectId() {
        return diskObjectId;
    }

    public void setDiskObjectId(String diskObjectId) {
        this.diskObjectId = diskObjectId;
    }

    public String getLunWwn() {
        return lunWwn;
    }

    public void setLunWwn(String lunWwn) {
        this.lunWwn = lunWwn;
    }

}
