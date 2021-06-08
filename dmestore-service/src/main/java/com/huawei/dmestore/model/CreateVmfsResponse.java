package com.huawei.dmestore.model;

import java.io.Serializable;
import java.util.List;
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
    private List<String> connectionResult ;
    private String descriptionEN;
    private String descriptionCN;

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

    public CreateVmfsResponse(int successNo, int failNo, List<String>  connectionResult) {
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

    public List<String>  getConnectionResult() {
        return connectionResult;
    }

    public void setConnectionResult(List<String>  connectionResult) {
        this.connectionResult = connectionResult;
    }

    public String getDescriptionEN() {
        return descriptionEN;
    }

    public void setDescriptionEN(String descriptionEN) {
        this.descriptionEN = descriptionEN;
    }

    public String getDescriptionCN() {
        return descriptionCN;
    }

    public void setDescriptionCN(String descriptionCN) {
        this.descriptionCN = descriptionCN;
    }

    public CreateVmfsResponse(int successNo, int failNo, List<String>  connectionResult, String descriptionEN, String descriptionCN) {
        this.successNo = successNo;
        this.failNo = failNo;
        this.connectionResult = connectionResult;
        this.descriptionEN = descriptionEN;
        this.descriptionCN = descriptionCN;
    }
}
