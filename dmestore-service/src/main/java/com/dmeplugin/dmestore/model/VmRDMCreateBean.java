package com.dmeplugin.dmestore.model;

/**
 * @ClassName VmRDMCreateBean
 * @Description TODO
 * @Author wangxiangyong
 * @Date 2020/9/2 16:48
 * @Version V1.0
 **/
public class VmRDMCreateBean {
    private String name;
    private int size;
    private String count;
    private String serviceLevelId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getServiceLevelId() {
        return serviceLevelId;
    }

    public void setServiceLevelId(String serviceLevelId) {
        this.serviceLevelId = serviceLevelId;
    }
}
