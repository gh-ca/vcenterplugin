package com.dmeplugin.dmestore.entity;

import java.io.Serializable;

public class Volume implements Serializable {

    //卷列表，添加列：关联的Datastore

    //卷新名称，只能包含字母、数字、“.”、“_”、“-”和中文字符，长度为1～31个字符。服务化和非服务化发放卷均可修改
    private String name;

    //归属控制器
    private String ownerController;

    //预取策略，影响磁盘读取。取值范围 0: 不预取，1：固定预取，2：可变预取，3：智能预取 默认值：智能预取。仅非服务化卷支持修改
    private String prefetchPolicy;

    //预取策略值,下发了PrefetchPolicy且其值为固定或可变预取，需要下发。固定预取取值范围为01024KB；可变预取取值范围为01024倍。仅非服务化卷支持修改
    private Integer prefetchValue;

    //非服务化卷
    private CustomizeVolumeTuning tuning;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerController() {
        return ownerController;
    }

    public void setOwnerController(String ownerController) {
        this.ownerController = ownerController;
    }

    public String getPrefetchPolicy() {
        return prefetchPolicy;
    }

    public void setPrefetchPolicy(String prefetchPolicy) {
        this.prefetchPolicy = prefetchPolicy;
    }

    public Integer getPrefetchValue() {
        return prefetchValue;
    }

    public void setPrefetchValue(Integer prefetchValue) {
        this.prefetchValue = prefetchValue;
    }

    public CustomizeVolumeTuning getTuning() {
        return tuning;
    }

    public void setTuning(CustomizeVolumeTuning tuning) {
        this.tuning = tuning;
    }

    @Override
    public String toString() {
        return "VolumeInfo{" +
                "name='" + name + '\'' +
                ", ownerController='" + ownerController + '\'' +
                ", prefetchPolicy='" + prefetchPolicy + '\'' +
                ", prefetchValue=" + prefetchValue +
                ", tuning=" + tuning +
                '}';
    }
}
