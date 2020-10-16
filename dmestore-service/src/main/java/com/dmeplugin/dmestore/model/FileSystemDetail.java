package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @className VolumeDetail
 * @description TODO
 * @date 2020/10/15 15:41
 */
public class FileSystemDetail {

    private String id;
    private String name;
    private FileSystemTurning fileSystemTurning;
    //自动调整容量开关。 false: 关闭；true：打开。默认打开
    private CapacityAutonegotiation capacityAutonegotiation;

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

    public FileSystemTurning getFileSystemTurning() {
        return fileSystemTurning;
    }

    public void setFileSystemTurning(FileSystemTurning fileSystemTurning) {
        this.fileSystemTurning = fileSystemTurning;
    }

    public CapacityAutonegotiation getCapacityAutonegotiation() {
        return capacityAutonegotiation;
    }

    public void setCapacityAutonegotiation(CapacityAutonegotiation capacityAutonegotiation) {
        this.capacityAutonegotiation = capacityAutonegotiation;
    }
}
