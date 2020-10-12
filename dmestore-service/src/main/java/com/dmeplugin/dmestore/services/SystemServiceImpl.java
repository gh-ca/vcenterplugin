package com.dmeplugin.dmestore.services;


import com.dmeplugin.dmestore.constant.DPSqlFileConstant;
import com.dmeplugin.dmestore.dao.SystemDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class SystemServiceImpl implements SystemService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SystemServiceImpl.class);

  private SystemDao systemDao;

  private VCenterInfoService vCenterInfoService;

  @Override
  public void initDB() {
    try {

      // 20200904 add table DP_DME_ACCESS_INFO
      systemDao.checkExistAndCreateTable(DPSqlFileConstant.DP_DME_ACCESS_INFO,
              DPSqlFileConstant.DP_DME_ACCESS_INFO_SQL);
      // 20200904 add table DP_DME_VMWARE_RELATION
      systemDao.checkExistAndCreateTable(DPSqlFileConstant.DP_DME_VMWARE_RELATION,
              DPSqlFileConstant.DP_DME_VMWARE_RELATION_SQL);

      //20200916 add table HW_BEST_PRACTICE_CHECK
      systemDao.checkExistAndCreateTable(DPSqlFileConstant.DP_DME_BEST_PRACTICE_CHECK,
              DPSqlFileConstant.DP_DME_BEST_PRACTICE_CHECK_SQL);

      // 20200922 add table DP_DME_TASK_INFO
      systemDao.checkExistAndCreateTable(DPSqlFileConstant.DP_DME_TASK_INFO,
              DPSqlFileConstant.DP_DME_TASK_INFO_SQL);

      // 20201012 add table DP_DME_VCENTER_INFO
      systemDao.checkExistAndCreateTable(DPSqlFileConstant.DP_DME_VCENTER_INFO,
              DPSqlFileConstant.DP_DME_VCENTER_INFO_SQL);

      LOGGER.info("creating table over...");

      systemDao.initData(DPSqlFileConstant.DP_DME_TASK_DATA_SYNCSERVICELEVEL_SQL);

      systemDao.initData(DPSqlFileConstant.DP_DME_TASK_DATA_SYNCBESTPRACTISE_SQL);

      systemDao.initData(DPSqlFileConstant.DP_DME_TASK_DATA_SCANDATASTORE_SQL);

      LOGGER.info("init data over...");
    } catch (Exception e) {
      LOGGER.error("Failed to init DB: " + e.getMessage());
    }
  }

  @Override
  public boolean isTableExists(String tableName) {
    try {
      return systemDao.checkTable(tableName);
    } catch (SQLException e) {
      LOGGER.error("Cannot check table exist " + e.getMessage());
      return false;
    }
  }

  @Override
  public boolean isColumnExists(String tableName, String columnName) {
    try {
      return systemDao.isColumnExists(tableName, columnName);
    } catch (SQLException e) {
      LOGGER.error("Cannot check column exist: " + e.getMessage());
      return false;
    }
  }

  public void cleanData() {
    LOGGER.info("Clean data from all tables...");
    systemDao.cleanAllData();
  }

  public void setSystemDao(SystemDao systemDao) {
    this.systemDao = systemDao;
  }

  public void setvCenterInfoService(VCenterInfoService vCenterInfoService) {
    this.vCenterInfoService = vCenterInfoService;
  }
}
