package com.huawei.dmestore.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * BestPracticeUpResultResponse .
 *
 * @author wangxiangyong .
 * @since 2020-12-01
 **/
public class BestPracticeRecommand implements Serializable {
    private String id;

    private String hostsetting;

    private String recommandValue;

    private String repairAction;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateRecommendTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateRepairTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHostsetting() {
        return hostsetting;
    }

    public void setHostsetting(String hostsetting) {
        this.hostsetting = hostsetting;
    }

    public String getRecommandValue() {
        return recommandValue;
    }

    public void setRecommandValue(String recommandValue) {
        this.recommandValue = recommandValue;
    }

    public String getRepairAction() {
        return repairAction;
    }

    public void setRepairAction(String repairAction) {
        this.repairAction = repairAction;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateRecommendTime() {
        return updateRecommendTime;
    }

    public void setUpdateRecommendTime(Date updateRecommendTime) {
        this.updateRecommendTime = updateRecommendTime;
    }

    public Date getUpdateRepairTime() {
        return updateRepairTime;
    }

    public void setUpdateRepairTime(Date updateRepairTime) {
        this.updateRepairTime = updateRepairTime;
    }
}
