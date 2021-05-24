
package com.huawei.dmestore.model;

import java.util.List;

/**
 * Volume.
 *
 * @author lianq
 * @ClassName: Volume
 * @Company: GH-CA
 * @since 2020-12-08
 */
public class Volume {
    /**
     * 卷的唯一标识 .
     */
    private String id;
    /**
     * 名称.
     */
    private String name;
    /**
     * 状态.
     */
    private String status;
    /**
     * 状态.
     */
    private boolean attached;
    /**
     * 分配类型.
     */
    private String alloctype;
    /**
     * 服务等级.
     */
    private String serviceLevelName;
    /**
     * 存储设备id.
     */
    private String storageId;
    /**
     * 存储池id.
     */
    private String poolRawId;
    /**
     * 容量利用率.
     */
    private String capacityUsage;
    /**
     * 保护状态.
     */
    private boolean protectionStatus;
    /**
     * hostIds.
     */
    private List<String> hostIds;
    /**
     * hostGroupIds.
     */
    private List<String> hostGroupIds;
    /**
     * 存储池名称.
     */
    private String storagePoolName;
    /**
     * 总容量 单位GB.
     */
    private int capacity;
    /**
     * 关联的datastore.
     */
    private String datastores;
    /**
     * volume对应的instanceId.
     */
    private String instanceId;

    /**
     * wwn标识.
     */
    private String wwn;
    /**
     * iops .
     */
    private float iops;
    /**
     * lantency .
     */
    private float lantency;
    /**
     * bandwith .
     */
    private float bandwith;

    /**
     * getId .
     *
     * @return String .
     */
    public String getId() {
        return id;
    }

    /**
     * setId .
     *
     * @param param .
     */
    public void setId(final String param) {
        this.id = param;
    }

    /**
     * getName .
     *
     * @return String .
     */
    public String getName() {
        return name;
    }

    /**
     * setName .
     *
     * @param param .
     */
    public void setName(final String param) {
        this.name = param;
    }

    /**
     * getStatus .
     *
     * @return String .
     */
    public String getStatus() {
        return status;
    }

    /**
     * setStatus .
     *
     * @param param .
     */
    public void setStatus(final String param) {
        this.status = param;
    }

    /**
     * getAttached .
     *
     * @return boolean .
     */
    public boolean getAttached() {
        return attached;
    }

    /**
     * setAttached .
     *
     * @param param .
     */
    public void setAttached(final boolean param) {
        this.attached = param;
    }

    /**
     * getAlloctype .
     *
     * @return String .
     */
    public String getAlloctype() {
        return alloctype;
    }

    /**
     * setAlloctype .
     *
     * @param param .
     */
    public void setAlloctype(final String param) {
        this.alloctype = param;
    }

    /**
     * getServiceLevelName .
     *
     * @return String .
     */
    public String getServiceLevelName() {
        return serviceLevelName;
    }

    /**
     * setServiceLevelName .
     *
     * @param param .
     */
    public void setServiceLevelName(final String param) {
        this.serviceLevelName = param;
    }

    /**
     * getStorageId .
     *
     * @return String .
     */
    public String getStorageId() {
        return storageId;
    }

    /**
     * setStorageId .
     *
     * @param param .
     */
    public void setStorageId(final String param) {
        this.storageId = param;
    }

    /**
     * getPoolRawId .
     *
     * @return String .
     */
    public String getPoolRawId() {
        return poolRawId;
    }

    /**
     * setPoolRawId .
     *
     * @param param .
     */
    public void setPoolRawId(final String param) {
        this.poolRawId = param;
    }

    /**
     * getCapacityUsage .
     *
     * @return String .
     */
    public String getCapacityUsage() {
        return capacityUsage;
    }

    /**
     * setCapacityUsage .
     *
     * @param param .
     */
    public void setCapacityUsage(final String param) {
        this.capacityUsage = param;
    }

    /**
     * getProtectionStatus .
     *
     * @return String .
     */
    public boolean getProtectionStatus() {
        return protectionStatus;
    }

    /**
     * setProtectionStatus .
     *
     * @param param .
     */
    public void setProtectionStatus(final boolean param) {
        this.protectionStatus = param;
    }

    /**
     * getHostIds .
     *
     * @return String .
     */
    public List<String> getHostIds() {
        return hostIds;
    }

    /**
     * setHostIds .
     *
     * @param param .
     */
    public void setHostIds(final List<String> param) {
        this.hostIds = param;
    }

    /**
     * gegetHostGroupIdstId .
     *
     * @return String .
     */
    public List<String> getHostGroupIds() {
        return hostGroupIds;
    }

    /**
     * setHostGroupIds .
     *
     * @param param .
     */
    public void setHostGroupIds(final List<String> param) {
        this.hostGroupIds = param;
    }

    /**
     * getStoragePoolName .
     *
     * @return String .
     */
    public String getStoragePoolName() {
        return storagePoolName;
    }

    /**
     * setStoragePoolName .
     *
     * @param param .
     */
    public void setStoragePoolName(final String param) {
        this.storagePoolName = param;
    }

    /**
     * getCapacity .
     *
     * @return int .
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * setId .
     *
     * @param param .
     */
    public void setCapacity(final int param) {
        this.capacity = param;
    }

    /**
     * getDatastores .
     *
     * @return String .
     */
    public String getDatastores() {
        return datastores;
    }

    /**
     * setId .
     *
     * @param param .
     */
    public void setDatastores(final String param) {
        this.datastores = param;
    }

    /**
     * getInstanceId .
     *
     * @return String .
     */
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * setInstanceId .
     *
     * @param param .
     */
    public void setInstanceId(final String param) {
        this.instanceId = param;
    }

    /**
     * getId .
     *
     * @return float .
     */
    public float getIops() {
        return iops;
    }

    /**
     * setIops .
     *
     * @param param .
     */
    public void setIops(final float param) {
        this.iops = param;
    }

    /**
     * getLantency .
     *
     * @return float .
     */
    public float getLantency() {
        return lantency;
    }

    /**
     * setLantency .
     *
     * @param param .
     */
    public void setLantency(final float param) {
        this.lantency = param;
    }

    /**
     * getBandwith .
     *
     * @return float .
     */
    public float getBandwith() {
        return bandwith;
    }

    /**
     * setBandwith .
     *
     * @param param .
     */
    public void setBandwith(final float param) {
        this.bandwith = param;
    }

    /**
     * getWwn .
     *
     * @return String .
     */
    public String getWwn() {
        return wwn;
    }

    /**
     * setWwn .
     *
     * @param param .
     */
    public void setWwn(final String param) {
        this.wwn = param;
    }
}
