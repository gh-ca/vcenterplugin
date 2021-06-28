package com.huawei.dmestore.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author yc
 * @Title:
 * @Description:
 * @date 2021/5/2810:41
 */
public class CreateVmfsResponse02 implements Serializable {
    private static final long serialVersionUID = 8718802610431878776L;

    private int successNo;
    private int failNo;
    private List<String> connectionResult ;
    private int partialSuccess;

    public CreateVmfsResponse02() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public List<String> getConnectionResult() {
        return connectionResult;
    }

    public void setConnectionResult(List<String> connectionResult) {
        this.connectionResult = connectionResult;
    }

    public int getPartialSuccess() {
        return partialSuccess;
    }

    public void setPartialSuccess(int partialSuccess) {
        this.partialSuccess = partialSuccess;
    }

    public CreateVmfsResponse02(int successNo, int failNo, List<String> connectionResult, int partialSuccess) {
        this.successNo = successNo;
        this.failNo = failNo;
        this.connectionResult = connectionResult;
        this.partialSuccess = partialSuccess;
    }


    @Override
    public String toString() {
        return "CreateVmfsResponse{" +
                "successNo=" + successNo +
                ", failNo=" + failNo +
                ", connectionResult=" + connectionResult +
                '}';
    }

}
