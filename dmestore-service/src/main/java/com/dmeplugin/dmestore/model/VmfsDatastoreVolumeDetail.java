package com.dmeplugin.dmestore.model;

/**
 * @Author Administrator
 * @Description vmfs Datastore劵详情
 * @Date 16:01 2020/9/2
 * @Param
 * @Return
 **/
public class VmfsDatastoreVolumeDetail {
    private String name;
    private String wwn;
    private QosPolicy qosPolice;
    private String smartTier;
    private String evolutionaryInfo;
    private String device;
    private String storePool;
    private String serviceLevel;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWwn() {
        return wwn;
    }

    public void setWwn(String wwn) {
        this.wwn = wwn;
    }

    public QosPolicy getQosPolice() {
        return qosPolice;
    }

    public void setQosPolice(QosPolicy qosPolice) {
        this.qosPolice = qosPolice;
    }

    public String getSmartTier() {
        return smartTier;
    }

    public void setSmartTier(String smartTier) {
        this.smartTier = smartTier;
    }

    public String getEvolutionaryInfo() {
        return evolutionaryInfo;
    }

    public void setEvolutionaryInfo(String evolutionaryInfo) {
        this.evolutionaryInfo = evolutionaryInfo;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getStorePool() {
        return storePool;
    }

    public void setStorePool(String storePool) {
        this.storePool = storePool;
    }

    public String getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(String serviceLevel) {
        this.serviceLevel = serviceLevel;
    }
}
