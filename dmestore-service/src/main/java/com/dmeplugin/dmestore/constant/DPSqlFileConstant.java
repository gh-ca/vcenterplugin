package com.dmeplugin.dmestore.constant;

public class DPSqlFileConstant {

  public static final String DP_DME_ACCESS_INFO = "DP_DME_ACCESS_INFO"; //DME访问信息表
  public static final String DP_DME_VMWARE_RELATION = "DP_DME_VMWARE_RELATION"; //DME与VMWARE对应关系表

  public static final String DP_DME_ACCESS_INFO_SQL = "DROP TABLE IF EXISTS \"DP_DME_ACCESS_INFO\";\n" +
          "CREATE TABLE DP_DME_ACCESS_INFO\n" +
          "(\n" +
          "    id integer PRIMARY KEY AUTO_INCREMENT NOT NULL,\n" +
          "    hostIp VARCHAR(255),\n" +
          "    hostPort int,\n" +
          "    userName VARCHAR(255),\n" +
          "    password VARCHAR(255),\n" +
          "    createTime datetime DEFAULT NOW(),\n" +
          "    updateTime datetime DEFAULT NOW(),\n" +
          "    state int DEFAULT 1\n" +
          ");";

  public static final String DP_DME_VMWARE_RELATION_SQL = "DROP TABLE IF EXISTS \"DP_DME_VMWARE_RELATION\";\n" +
          "CREATE TABLE DP_DME_VMWARE_RELATION\n" +
          "(\n" +
          "    id integer PRIMARY KEY AUTO_INCREMENT NOT NULL,\n" +
          "    store_id VARCHAR(255),\n" +
          "    store_name VARCHAR(255),\n" +
          "    volume_id VARCHAR(255),\n" +
          "    volume_name VARCHAR(255),\n" +
          "    volume_wwn VARCHAR(255),\n" +
          "    volume_share VARCHAR(255),\n" +
          "    volume_fs VARCHAR(255),\n" +
          "    storage_device_id VARCHAR(255),\n" +
          "    share_id VARCHAR(255),\n" +
          "    share_name VARCHAR(255),\n" +
          "    fs_id VARCHAR(255),\n" +
          "    fs_name VARCHAR(255),\n" +
          "    logicport_id VARCHAR(255),\n" +
          "    logicport_name VARCHAR(255),\n" +
          "    store_type VARCHAR(255),\n" +
          "    createTime datetime DEFAULT NOW(),\n" +
          "    updateTime datetime DEFAULT NOW(),\n" +
          "    STATE int DEFAULT 1\n" +
          ");";

}
