package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @className FailoverGroup
 * @description TODO
 * @date 2020/10/14 15:32
 */
public class FailoverGroup {

    //漂移组类型，枚举（system、VLAN、customized）
    private String failoverGroupType;
    //漂移组id
    private String id;
    //漂移组名称
    private String name;

    public String getFailoverGroupType() {
        return failoverGroupType;
    }

    public void setFailoverGroupType(String failoverGroupType) {
        this.failoverGroupType = failoverGroupType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
