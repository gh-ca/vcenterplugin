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
    private List<String> desc;

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

    public List<String> getDesc() {
        return desc;
    }

    public void setDesc(List<String> desc) {
        this.desc = desc;
    }

    public CreateVmfsResponse02(int successNo, int failNo, List<String> connectionResult, int partialSuccess) {
        this.successNo = successNo;
        this.failNo = failNo;
        this.connectionResult = connectionResult;
        this.partialSuccess = partialSuccess;
    }

    public CreateVmfsResponse02(int successNo, int failNo, List<String> connectionResult, int partialSuccess, List<String> desc) {
        this.successNo = successNo;
        this.failNo = failNo;
        this.connectionResult = connectionResult;
        this.partialSuccess = partialSuccess;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "CreateVmfsResponse02{" +
                "successNo=" + successNo +
                ", failNo=" + failNo +
                ", connectionResult=" + connectionResult +
                ", partialSuccess=" + partialSuccess +
                ", desc=" + desc +
                '}';
    }
}
