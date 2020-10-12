package com.dmeplugin.dmestore.dao;



import com.dmeplugin.dmestore.constant.DPSqlFileConstant;
import com.dmeplugin.dmestore.entity.VCenterInfo;
import com.dmeplugin.dmestore.exception.DataBaseException;
import com.dmeplugin.dmestore.services.DmeConstants;

import java.sql.*;
import java.util.*;

/**
 * @Description: TODO
 * @ClassName: VCenterInfoDao
 * @Company: GH-CA
 * @author: lgq
 * @create: 2020-09-02
 **/
public class VCenterInfoDao extends H2DataBaseDao {

  public int addVCenterInfo(VCenterInfo vCenterInfo) throws SQLException {
    checkVCenterInfo(vCenterInfo);
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      ps = con.prepareStatement(
          "INSERT INTO " + DPSqlFileConstant.DP_DME_VCENTER_INFO + " (HOST_IP,USER_NAME," +
              "PASSWORD,STATE,CREATE_TIME,PUSH_EVENT,PUSH_EVENT_LEVEL,HOST_PORT) VALUES (?,?,?,?,CURRENT_TIMESTAMP,?,?,?)");
      ps.setString(1, vCenterInfo.getHostIp());
      ps.setString(2, vCenterInfo.getUserName());
      ps.setString(3, vCenterInfo.getPassword());
      ps.setBoolean(4, vCenterInfo.isState());
      ps.setBoolean(5, vCenterInfo.isPushEvent());
      ps.setInt(6, vCenterInfo.getPushEventLevel());
      ps.setInt(7, vCenterInfo.getHostPort());
      int row = ps.executeUpdate();
      rs = ps.getGeneratedKeys();
      if (rs.next()) {
        vCenterInfo.setId(rs.getInt(1));
      }
      return row;
    } catch (SQLException e) {
      LOGGER.error("Failed to add vCenter info: " + e.getMessage());
      throw e;
    } finally {
      closeConnection(con, ps, rs);
    }
  }

  public int updateVCenterInfo(VCenterInfo vCenterInfo) throws SQLException {
    checkVCenterInfo(vCenterInfo);
    checkID(vCenterInfo.getId());
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      ps = con.prepareStatement("UPDATE " + DPSqlFileConstant.DP_DME_VCENTER_INFO
          + " SET HOST_IP=?,USER_NAME=?,PASSWORD=?,STATE=?,PUSH_EVENT=?,PUSH_EVENT_LEVEL=?,HOST_PORT=? WHERE ID=?");
      ps.setString(1, vCenterInfo.getHostIp());
      ps.setString(2, vCenterInfo.getUserName());
      ps.setString(3, vCenterInfo.getPassword());
      ps.setBoolean(4, vCenterInfo.isState());
      ps.setBoolean(5, vCenterInfo.isPushEvent());
      ps.setInt(6, vCenterInfo.getPushEventLevel());
      ps.setInt(7, vCenterInfo.getHostPort());
      ps.setInt(8, vCenterInfo.getId());
      return ps.executeUpdate();
    } catch (SQLException e) {
      LOGGER.error("Failed to update vCenter info: " + e.getMessage());
      throw e;
    } finally {
      closeConnection(con, ps, rs);
    }
  }

  public VCenterInfo getVCenterInfo() throws SQLException {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      ps = con.prepareStatement("SELECT * FROM " + DPSqlFileConstant.DP_DME_VCENTER_INFO
          + " ORDER BY CREATE_TIME DESC LIMIT 1");
      rs = ps.executeQuery();
      if (rs.next()) {
        VCenterInfo vCenterInfo = new VCenterInfo();
        vCenterInfo.setId(rs.getInt("ID"));
        vCenterInfo.setHostIp(rs.getString("HOST_IP"));
        vCenterInfo.setHostPort(rs.getInt("HOST_PORT"));
        vCenterInfo.setUserName(rs.getString("USER_NAME"));
        vCenterInfo.setPassword(rs.getString("PASSWORD"));
        vCenterInfo.setCreateTime(rs.getTimestamp("CREATE_TIME"));
        vCenterInfo.setState(rs.getBoolean("STATE"));
        vCenterInfo.setPushEvent(rs.getBoolean("PUSH_EVENT"));
        vCenterInfo.setPushEventLevel(rs.getInt("PUSH_EVENT_LEVEL"));
        return vCenterInfo;
      }
    } catch (DataBaseException | SQLException e) {
      LOGGER.error("Failed to get vCenter info: " + e.getMessage());
      throw new SQLException(e);
    } finally {
      closeConnection(con, ps, rs);
    }
    return null;
  }

  public boolean disableVCenterInfo() throws SQLException {
    Connection con = null;
    PreparedStatement ps = null;
    try {
      con = getConnection();
      ps = con.prepareStatement("UPDATE " + DPSqlFileConstant.DP_DME_VCENTER_INFO + " SET state=FALSE ");
      return ps.executeUpdate() > 0;
    } catch (DataBaseException | SQLException e) {
      LOGGER.error("Failed to disable vCenter info: " + e.getMessage());
      throw new SQLException(e);
    } finally {
      closeConnection(con, ps, null);
    }
  }

  public boolean disablePushEvent() throws SQLException {
    Connection con = null;
    PreparedStatement ps = null;
    try {
      con = getConnection();
      ps = con
          .prepareStatement("UPDATE " + DPSqlFileConstant.DP_DME_VCENTER_INFO + " SET PUSH_EVENT=FALSE ");
      return ps.executeUpdate() > 0;
    } catch (DataBaseException | SQLException e) {
      LOGGER.error("Failed to disable push event: " + e.getMessage());
      throw new SQLException(e);
    } finally {
      closeConnection(con, ps, null);
    }
  }







  private void checkIp(String ip) throws SQLException {
    if (ip == null || ip.length() > DmeConstants.MAXLEN) {
      throw new SQLException("parameter ip is not correct");
    }
  }

  private void checkUserName(String userName) throws SQLException {
    if (userName == null || userName.length() > DmeConstants.MAXLEN) {
      throw new SQLException("parameter userName is not correct");
    }
  }

  private void checkPassword(String password) throws SQLException {
    if (password == null || password.length() > DmeConstants.MAXLEN) {
      throw new SQLException("parameter password is not correct");
    }
  }

  private void checkID(int id) throws SQLException {
    if (id < 1) {
      throw new SQLException("parameter is is not correct");
    }

  }

  private void checkVCenterInfo(VCenterInfo vCenterInfo) throws SQLException {
    checkIp(vCenterInfo.getHostIp());
    checkUserName(vCenterInfo.getUserName());
    checkPassword(vCenterInfo.getPassword());
  }
}
