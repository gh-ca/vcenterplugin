package com.dmeplugin.dmestore.model;

import java.io.Serializable;
/**
 * @author lianq
 * @ClassName: FileSystem
 * @Company: GH-CA
 * @create 2020-09-03
 */
public class FileSystem  {


    private String id; //id
    private String name;//����
    private String health_status; //״̬
    private String alloc_type; //��������
    private Integer capacity_usage_ratio; //����������
    private String storage_pool_name;//�����洢������
    private Integer nfs_count; //nfs
    private Integer cifs_count;//cifs
    private Integer dtree_count; //dtree
    private Double capacity;//������


    private Double allocate_quota_in_pool;
    private Double available_capacity;
    private Double min_size_fs_capacity;
    private String storage_id;


    private String id;
    private String name;
    private String health_status;
    private String alloc_type;
    private Integer capacity_usage_ratio;
    private String storage_pool_name;// �ļ�ϵͳ���ڴ洢�ص�����
    private Integer nfs_count;
    private Integer cifs_count;
    private Integer dtree_count;
    private Double capacity;

    //�����ֶ�
    private Double allocate_quota_in_pool; //�ļ�ϵͳ�ڴ洢�����Ѿ����������
    private Double available_capacity; //�ļ�ϵͳ�������õ�����
    private Double min_size_fs_capacity;//�ļ�ϵͳ�����ݵ���С�ռ�;
    private String storage_id; //�ļ�ϵͳ�����洢�豸


    private String id; //id
    private String name;//����
    private String health_status; //״̬
    private String alloc_type; //��������
    private Integer capacity_usage_ratio; //����������
    private String storage_pool_name;//�����洢������
    private Integer nfs_count; //nfs
    private Integer cifs_count;//cifs
    private Integer dtree_count; //dtree
    private Double capacity;//������


    private Double allocate_quota_in_pool;
    private Double available_capacity;
    private Double min_size_fs_capacity;
    private String storage_id;


    public Double getAllocate_quota_in_pool() {
        return allocate_quota_in_pool;
    }

    public void setAllocate_quota_in_pool(Double allocate_quota_in_pool) {
        this.allocate_quota_in_pool = allocate_quota_in_pool;
    }

    public Double getAvailable_capacity() {
        return available_capacity;
    }

    public void setAvailable_capacity(Double available_capacity) {
        this.available_capacity = available_capacity;
    }

    public Double getMin_size_fs_capacity() {
        return min_size_fs_capacity;
    }

    public void setMin_size_fs_capacity(Double min_size_fs_capacity) {
        this.min_size_fs_capacity = min_size_fs_capacity;
    }

    public String getStorage_id() {
        return storage_id;
    }

    public void setStorage_id(String storage_id) {
        this.storage_id = storage_id;
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

    public String getHealth_status() {
        return health_status;
    }

    public void setHealth_status(String health_status) {
        this.health_status = health_status;
    }

    public String getAlloc_type() {
        return alloc_type;
    }

    public void setAlloc_type(String alloc_type) {
        this.alloc_type = alloc_type;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public Integer getCapacity_usage_ratio() {
        return capacity_usage_ratio;
    }

    public void setCapacity_usage_ratio(Integer capacity_usage_ratio) {
        this.capacity_usage_ratio = capacity_usage_ratio;
    }

    public String getStorage_pool_name() {
        return storage_pool_name;
    }

    public void setStorage_pool_name(String storage_pool_name) {
        this.storage_pool_name = storage_pool_name;
    }

    public Integer getNfs_count() {
        return nfs_count;
    }

    public void setNfs_count(Integer nfs_count) {
        this.nfs_count = nfs_count;
    }

    public Integer getCifs_count() {
        return cifs_count;
    }

    public void setCifs_count(Integer cifs_count) {
        this.cifs_count = cifs_count;
    }

    public Integer getDtree_count() {
        return dtree_count;
    }

    public void setDtree_count(Integer dtree_count) {
        this.dtree_count = dtree_count;
    }
}
