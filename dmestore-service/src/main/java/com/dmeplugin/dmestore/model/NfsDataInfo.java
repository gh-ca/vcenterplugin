package com.dmeplugin.dmestore.model;

public class NfsDataInfo {
    //列表字段（基本视图）
    String name;    //名称
    String status;  //状态
    Double capacity;  //总容量 单位GB
    Double freeSpace; //空闲容量 单位GB
    Double reserveCapacity; //置备容量  capacity+uncommitted-freeSpace 单位GB
    String device; //设备
    String logicPort; //逻辑端口
    String shareIp; //share ip
    String sharePath; //share path
    String fs; //fs

    //列表字段（性能视图）：
    int OPS; //OPS
    int bandwidth;   //带宽 单位MB/s

    int readResponseTime;   //读响应时间 单位ms
    int writeResponseTime; //写响应时间 单位ms
}
