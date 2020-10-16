package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @className FileSystemTurning
 * @description TODO
 * @date 2020/10/15 15:48
 */
public class FileSystemTurning {

    //重复数据删除。默认关闭
    private Boolean deduplication_enabled;
    //数据压缩。默认关闭
    private Boolean compression_enabled;
    //文件系统分配类型
    private String allocation_type;
    private SmartQos smartQos;

    public String getAllocation_type() {
        return allocation_type;
    }

    public void setAllocation_type(String allocation_type) {
        this.allocation_type = allocation_type;
    }

    public Boolean getDeduplication_enabled() {
        return deduplication_enabled;
    }

    public void setDeduplication_enabled(Boolean deduplication_enabled) {
        this.deduplication_enabled = deduplication_enabled;
    }

    public Boolean getCompression_enabled() {
        return compression_enabled;
    }

    public void setCompression_enabled(Boolean compression_enabled) {
        this.compression_enabled = compression_enabled;
    }

    public SmartQos getSmartQos() {
        return smartQos;
    }

    public void setSmartQos(SmartQos smartQos) {
        this.smartQos = smartQos;
    }
}
