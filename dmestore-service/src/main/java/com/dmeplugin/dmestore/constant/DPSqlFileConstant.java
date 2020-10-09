package com.dmeplugin.dmestore.constant;

public class DPSqlFileConstant {

  public static final String DP_DME_ACCESS_INFO = "DP_DME_ACCESS_INFO"; //DME访问信息表
  public static final String DP_DME_TASK_INFO = "DP_DME_TASK_INFO"; //DME任务表
  public static final String DP_DME_VMWARE_RELATION = "DP_DME_VMWARE_RELATION"; //DME与VMWARE对应关系表

  public static final String[] ALL_TABLES = {DPSqlFileConstant.DP_DME_ACCESS_INFO,
          DPSqlFileConstant.DP_DME_VMWARE_RELATION,DPSqlFileConstant.DP_DME_TASK_INFO, DPSqlFileConstant.HW_BEST_PRACTICE_CHECK};

  public static final String DP_DME_ACCESS_INFO_SQL = "DROP TABLE IF EXISTS \"DP_DME_ACCESS_INFO\";\n" +
          "CREATE TABLE DP_DME_ACCESS_INFO\n" +
          "(\n" +
          "    id integer PRIMARY KEY AUTO_INCREMENT NOT NULL,\n" +
          "    hostIp VARCHAR(255),\n" +
          "    hostPort int,\n" +
          "    userName VARCHAR(255),\n" +
          "    password VARCHAR(1024),\n" +
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

  public static final String DP_DME_TASK_INFO_SQL = "DROP TABLE IF EXISTS \"DP_DME_TASK_INFO\";\n" +
          "CREATE TABLE DP_DME_TASK_INFO\n" +
          "(\n" +
          "    id integer PRIMARY KEY AUTO_INCREMENT NOT NULL,\n" +
          "    CLASS_NAME VARCHAR(255),\n" +
          "    CRON VARCHAR(255),\n" +
          "    JOB_NAME VARCHAR(255),\n" +
          "    METHOD VARCHAR(255)\n" +
          ");";

  public static final String DP_DME_TASK_DATA_SYNCSERVICELEVEL_SQL = "INSERT INTO DP_DME_TASK_INFO (ID, CLASS_NAME, CRON, JOB_NAME, METHOD) " +
          "VALUES (1, 'com.dmeplugin.dmestore.task.BackgroundSyncServiceLevelTask', '0 0 0/6 * * ?', 'syncServiceLevel', 'syncServiceLevel');";

  public static final String DP_DME_TASK_DATA_SYNCBESTPRACTISE_SQL = "INSERT INTO DP_DME_TASK_INFO (ID, CLASS_NAME, CRON, JOB_NAME, METHOD) " +
          "VALUES (2, 'com.dmeplugin.dmestore.task.BackgroundCheckBestPractiseTask', '0 0 2 * * ?', 'syncCheckBestPractise', 'syncCheckBestPractise');";

  public static final String DP_DME_TASK_DATA_SCANDATASTORE_SQL = "INSERT INTO DP_DME_TASK_INFO (ID, CLASS_NAME, CRON, JOB_NAME, METHOD) " +
          "VALUES (3, 'com.dmeplugin.dmestore.task.BackgroundScanDatastoreTask', '0 0 0/6 * * ?', 'scanDatastore', 'scanDatastore');";

  public static final String HW_BEST_PRACTICE_CHECK = "HW_BEST_PRACTICE_CHECK";
  public static final String HW_BEST_PRACTICE_CHECK_SQL ="DROP TABLE IF EXISTS \"HW_BEST_PRACTICE_CHECK\";\n" +
          "CREATE TABLE \"HW_BEST_PRACTICE_CHECK\" (\n" +
          "\"ID\"  integer PRIMARY KEY AUTO_INCREMENT NOT NULL,\n" +
          "\"HOST_ID\"  nvarchar(255),\n" +
          "\"HOST_NAME\"  nvarchar(255),\n" +
          "\"HOST_SETTING\"  nvarchar(255),\n" +
          "\"RECOMMEND_VALUE\"  nvarchar(50) NOT NULL,\n" +
          "\"ACTUAL_VALUE\"  nvarchar(50),\n" +
          "\"HINT_LEVEL\"  nvarchar(20),\n" +
          "\"NEED_REBOOT\"  nvarchar(10),\n" +
          "\"AUTO_REPAIR\"  nvarchar(10),\n" +
          "\"CREATE_TIME\"  datetime\n" +
          ");";

}
