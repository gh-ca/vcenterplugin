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

    private String serviceLevel;
    private String targetStore;
    private String storePool;
    private Object policy;

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

    public String getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(String serviceLevel) {
        this.serviceLevel = serviceLevel;
    }

    public String getTargetStore() {
        return targetStore;
    }

    public void setTargetStore(String targetStore) {
        this.targetStore = targetStore;
    }

    public String getStorePool() {
        return storePool;
    }

    public void setStorePool(String storePool) {
        this.storePool = storePool;
    }

    public Object getPolicy() {
        return policy;
    }

    public void setPolicy(Object policy) {
        this.policy = policy;
    }
}
