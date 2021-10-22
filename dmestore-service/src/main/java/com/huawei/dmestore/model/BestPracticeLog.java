package com.huawei.dmestore.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * BestPracticeLog .
 *
 * @author wangxiangyong .
 * @since 2021-07-23
 **/
public class BestPracticeLog implements Serializable {
    private String id;

    private String objectName;

    private String objectId;

    private String hostsetting;

    private String violationValue;

    private String recommandValue;

    private String repairType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date repairTime;

    private boolean repairResult;

    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getViolationValue() {
        return violationValue;
    }

    public void setViolationValue(String violationValue) {
        this.violationValue = violationValue;
    }

    public String getRecommandValue() {
        return recommandValue;
    }

    public void setRecommandValue(String recommandValue) {
        this.recommandValue = recommandValue;
    }

    public String getRepairType() {
        return repairType;
    }

    public void setRepairType(String repairType) {
        this.repairType = repairType;
    }

    public Date getRepairTime() {
        return repairTime;
    }

    public void setRepairTime(Date repairTime) {
        this.repairTime = repairTime;
    }

    public boolean getRepairResult() {
        return repairResult;
    }

    public void setRepairResult(boolean repairResult) {
        this.repairResult = repairResult;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHostsetting() {
        return hostsetting;
    }

    public void setHostsetting(String hostsetting) {
        this.hostsetting = hostsetting;
    }
}
