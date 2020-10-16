package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @className FileSystemTurning
 * @description TODO
 * @date 2020/10/15 15:48
 */
public class FileSystemTurning {

    //重复数据删除。默认关闭
    private Boolean deduplicationEnabled;
    //数据压缩。默认关闭
    private Boolean compressionEnabled;
    //文件系统分配类型
    private String allocationType;
    private SmartQos smartQos;

    public Boolean getDeduplicationEnabled() {
        return deduplicationEnabled;
    }

    public void setDeduplicationEnabled(Boolean deduplicationEnabled) {
        this.deduplicationEnabled = deduplicationEnabled;
    }

    public Boolean getCompressionEnabled() {
        return compressionEnabled;
    }

    public void setCompressionEnabled(Boolean compressionEnabled) {
        this.compressionEnabled = compressionEnabled;
    }

    public String getAllocationType() {
        return allocationType;
    }

    public void setAllocationType(String allocationType) {
        this.allocationType = allocationType;
    }

    public SmartQos getSmartQos() {
        return smartQos;
    }

    public void setSmartQos(SmartQos smartQos) {
        this.smartQos = smartQos;
    }
}
