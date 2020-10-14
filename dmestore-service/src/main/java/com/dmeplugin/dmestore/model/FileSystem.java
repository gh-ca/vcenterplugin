package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @ClassName: FileSystem
 * @Company: GH-CA
 * @create 2020-09-03
 */
public class FileSystem  {

    //id
    private String id;
    //名称
    private String name;
    //状态
    private String health_status;
    //分配策略
    private String alloc_type;
    //容量使用率
    private Integer capacity_usage_ratio;
    //存储池名字
    private String storage_pool_name;
    //nfs
    private Integer nfs_count;
    //cifs
    private Integer cifs_count;
    //dtree
    private Integer dtree_count;
    //总容量
    private Double capacity;
    //文件系统在存储池上已经分配的容量
    private Double allocate_quota_in_pool;
    //可用空间
    private Double available_capacity;
    //Thin文件系统能缩容的最小空间。单位GB，保留三位小数
    private Double min_size_fs_capacity;
    //存储设备Id
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
