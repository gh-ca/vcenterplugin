package com.huawei.dmestore.model;

import java.io.Serializable;
import java.util.Map;

/**
 * @author yc
 * @Title:
 * @Description:
 * @date 2021/5/2810:41
 */
public class CreateVmfsResponse implements Serializable {
    private static final long serialVersionUID = 8718802610431878776L;

    private int successNo;
    private int failNo;
    private Map<String,String> connectionResult ;
    private String description;

    public CreateVmfsResponse() {
    }

    @Override
    public String toString() {
        return "CreateVmfsResponse{" +
                "successNo=" + successNo +
                ", failNo=" + failNo +
                ", connectionResult=" + connectionResult +
                '}';
    }

    public CreateVmfsResponse(int successNo) {
        this.successNo = successNo;
    }

    public CreateVmfsResponse(int successNo, int failNo) {
        this.successNo = successNo;
        this.failNo = failNo;
    }

    public CreateVmfsResponse(int successNo, int failNo, Map<String, String> connectionResult) {
        this.successNo = successNo;
        this.failNo = failNo;
        this.connectionResult = connectionResult;
    }

    public int getSuccessNo() {
        return successNo;
    }

    public void setSuccessNo(int successNo) {
        this.successNo = successNo;
    }

    public int getFailNo() {
        return failNo;
    }

    public void setFailNo(int failNo) {
        this.failNo = failNo;
    }

    public Map<String, String> getConnectionResult() {
        return connectionResult;
    }

    public void setConnectionResult(Map<String, String> connectionResult) {
        this.connectionResult = connectionResult;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CreateVmfsResponse(int successNo, int failNo, Map<String, String> connectionResult, String description) {
        this.successNo = successNo;
        this.failNo = failNo;
        this.connectionResult = connectionResult;
        this.description = description;
    }
}
