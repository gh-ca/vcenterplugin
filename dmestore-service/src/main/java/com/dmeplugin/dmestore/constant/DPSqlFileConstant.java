package com.dmeplugin.dmestore.constant;

public class DPSqlFileConstant {

  public static final String DP_DME_ACCESS_INFO = "DP_DME_ACCESS_INFO";

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
          "    STATE int DEFAULT 1\n" +
          ");";

}
