package com.dmeplugin.dmestore.model;

public class VmfsDataInfo {
    //列表字段（基本视图）
    String name;    //名称
    String status;  //状态
    Double capacity;  //总容量 单位GB
    Double freeSpace; //空闲容量 单位GB
    Double reserveCapacity; //置备容量  capacity+uncommitted-freeSpace 单位GB
    String device; //设备
    String serviceLevelName;    //服务等级
    Boolean vmfsProtected;  //保护状态

    //列表字段（性能视图）：
    int maxIops; //QoS上限
    int minIops; //QoS下限
    int maxBandwidth;   //带宽上限 单位MB/s
    int minBandwidth;   //带宽下限 单位MB/s

    int latency; //时延 单位ms

}
