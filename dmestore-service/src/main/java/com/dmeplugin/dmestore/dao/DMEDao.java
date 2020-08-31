package com.dmeplugin.dmestore.dao;


import com.dmeplugin.dmestore.constant.SqlFileConstant;
import com.dmeplugin.dmestore.entity.DME;
import com.dmeplugin.dmestore.utils.VersionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DMEDao extends H2DataBaseDao {

  public DME getDMEById(int id) throws SQLException {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      ps = con.prepareStatement("select * from HW_ESIGHT_HOST where id = ?");
      ps.setInt(1, id);
      rs = ps.executeQuery();
      if (rs.next()) {
        return buildDME(rs);
      }
    } catch (SQLException e) {
      LOGGER.error("Failed to get dme by Id: " + e.getMessage());
      throw e;
    } finally {
      closeConnection(con, ps, rs);
    }
    return null;
  }

  public DME getDMEByIp(String ip) throws SQLException {
    checkIp(ip);

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      ps = con.prepareStatement("select * from HW_ESIGHT_HOST where HOST_IP = ?");
      ps.setString(1, ip);
      rs = ps.executeQuery();
      if (rs.next()) {
        return buildDME(rs);
      }
    } catch (SQLException e) {
      LOGGER.error("Failed to get dme by IP: " + e.getMessage());
      throw e;
    } finally {
      closeConnection(con, ps, rs);
    }
    return null;

  }

  public List<DME> getDMEList(String ip, int pageNo, int pageSize) throws SQLException {
    checkNullIp(ip);

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      StringBuffer sql = new StringBuffer();
      sql.append("select * from HW_ESIGHT_HOST");
      if (ip != null && !ip.isEmpty()) {
        sql.append(" where HOST_IP like ?");
      }

      if (pageSize > 0) {
        sql.append(" limit ? offset ?");
      }
      ps = con.prepareStatement(sql.toString());

      int i = 1;

      if (ip != null && !ip.isEmpty()) {
        ps.setString(i++, "%" + ip + "%");
      }

      if (pageSize > 0) {
        ps.setInt(i++, pageSize);
        ps.setInt(i++, (pageNo - 1) * pageSize);
      }
      rs = ps.executeQuery();
      List<DME> dmeList = new ArrayList<>();
      while (rs.next()) {
        DME dme = buildDME(rs);
        dme.setLoginPwd(null);
        dmeList.add(dme);
      }
      return dmeList;
    } catch (SQLException e) {
      LOGGER.error("Failed to get dme list: " + e.getMessage());
      throw e;
    } finally {
      closeConnection(con, ps, rs);
    }

  }

  public List<DME> getDMEListWithPwd(String ip, int pageNo, int pageSize)
      throws SQLException {
    checkNullIp(ip);

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      StringBuffer sql = new StringBuffer();
      sql.append("select * from HW_ESIGHT_HOST");
      if (ip != null && !ip.isEmpty()) {
        sql.append(" where HOST_IP like ?");
      }

      if (pageSize > 0) {
        sql.append(" limit ? offset ?");
      }
      ps = con.prepareStatement(sql.toString());

      int i = 1;

      if (ip != null && !ip.isEmpty()) {
        ps.setString(i++, "%" + ip + "%");
      }

      if (pageSize > 0) {
        ps.setInt(i++, pageSize);
        ps.setInt(i++, (pageNo - 1) * pageSize);
      }
      rs = ps.executeQuery();
      List<DME> dmeList = new ArrayList<>();
      while (rs.next()) {
        DME dme = buildDME(rs);
        dmeList.add(dme);
      }
      return dmeList;
    } catch (SQLException e) {
      LOGGER.error("Failed to get dme list with password: " + e.getMessage());
      throw e;
    } finally {
      closeConnection(con, ps, rs);
    }

  }

  public List<DME> getAllDMEs() throws SQLException {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      ps = con.prepareStatement("SELECT * FROM " + SqlFileConstant.HW_ESIGHT_HOST);
      rs = ps.executeQuery();
      List<DME> dmeList = new ArrayList<>();
      while (rs.next()) {
        dmeList.add(buildDME(rs));
      }
      return dmeList;
    } catch (SQLException e) {
      LOGGER.error("Failed to get dmes: " + e.getMessage());
      throw e;
    } finally {
      closeConnection(con, ps, rs);
    }

  }

  private DME buildDME(ResultSet rs) throws SQLException {
    DME dme = new DME();
    dme.setHostIp(rs.getString("HOST_IP"));
    dme.setAliasName(rs.getString("ALIAS_NAME"));
    dme.setLoginAccount(rs.getString("LOGIN_ACCOUNT"));
    dme.setLoginPwd(rs.getString("LOGIN_PWD"));
    dme.setId(rs.getInt("ID"));
    dme.setHostPort(rs.getInt("HOST_PORT"));
    dme.setSystemId(rs.getString("SYSTEM_ID"));
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    dme.setCreateTime(sdf.format(rs.getTimestamp("CREATE_TIME")));
    dme.setLastModify(sdf.format(rs.getTimestamp("LAST_MODIFY_TIME")));
    dme.setReservedInt1(rs.getString("RESERVED_INT1")); // HA状态
    dme.setReservedInt2(rs.getString("RESERVED_INT2")); // 保活状态
    dme.setHaProvider(rs.getInt("HA_PROVIDER"));
    dme.setAlarmDefinition(rs.getInt("ALARM_DEFINITION"));
    DME.decryptedPassword(dme);
    return dme;
  }

  public int getDMEListCount(String ip) throws SQLException {
    checkNullIp(ip);

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    int count = 0;
    try {
      con = getConnection();
      StringBuffer sql = new StringBuffer();
      sql.append("select count(*) as count from HW_ESIGHT_HOST");
      if (ip != null && !ip.isEmpty()) {
        sql.append(" where HOST_IP like ?");
      }
      ps = con.prepareStatement(sql.toString());

      int i = 1;
      if (ip != null && !ip.isEmpty()) {
        ps.setString(i, "%" + ip + "%");
      }

      rs = ps.executeQuery();
      if (rs.next()) {
        count = rs.getInt("count");
      }

    } catch (SQLException e) {
      LOGGER.error("Failed to get dme list count: " + e.getMessage());
      throw e;
    } finally {
      closeConnection(con, ps, rs);
    }
    return count;
  }

  public int saveDME(DME dme) throws SQLException {
    checkDME(dme);

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      ps = con.prepareStatement(
          "insert into HW_ESIGHT_HOST (HOST_IP,ALIAS_NAME,HOST_PORT,SYSTEM_ID,LOGIN_ACCOUNT,LOGIN_PWD,RESERVED_STR1,CREATE_TIME, LAST_MODIFY_TIME) values (?,?,?,?,?,?,?,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)");
      ps.setString(1, dme.getHostIp());
      ps.setString(2, dme.getAliasName());
      ps.setInt(3, dme.getHostPort());
      ps.setString(4, dme.getSystemId());
      ps.setString(5, dme.getLoginAccount());
      ps.setString(6, dme.getLoginPwd());
      ps.setString(7, VersionUtils.getVersion());

      int re = ps.executeUpdate();
      if (re > 0) {
        rs = ps.getGeneratedKeys();
        if (rs.next()) {
          int deptno = rs.getInt(1);
          LOGGER.info("save dme info successful,dme id:" + deptno);
        }
      }
      return re;
    } catch (SQLException e) {
      LOGGER.error("Failed to save dme: " + e.getMessage());
      throw e;
    } finally {
      closeConnection(con, ps, rs);
    }
  }

  public int updateDME(DME dme) throws SQLException {
    checkDME(dme);

    Connection con = null;
    PreparedStatement ps = null;
    try {
      con = getConnection();
      ps = con.prepareStatement(
          "update HW_ESIGHT_HOST set HOST_IP = ? , ALIAS_NAME = ?, HOST_PORT = ? , SYSTEM_ID = ? , LOGIN_ACCOUNT = ? , LOGIN_PWD = ? , LAST_MODIFY_TIME = CURRENT_TIMESTAMP where ID = ?");
      ps.setString(1, dme.getHostIp());
      ps.setString(2, dme.getAliasName());
      ps.setInt(3, dme.getHostPort());
      ps.setString(4, dme.getSystemId());
      ps.setString(5, dme.getLoginAccount());
      ps.setString(6, dme.getLoginPwd());
      ps.setInt(7, dme.getId());
      return ps.executeUpdate();
    } catch (SQLException e) {
      LOGGER.error("Failed to update dme: " + e.getMessage());
      throw e;
    } finally {
      closeConnection(con, ps, null);
    }
  }

  public int deleteDME(List<Integer> id) throws SQLException {
    checkIds(id);

    if (id.isEmpty()) {
      return 0;
    }

    Connection con = null;
    PreparedStatement ps = null;
    try {
      con = getConnection();
      StringBuffer sql = new StringBuffer();
      sql.append("delete from HW_ESIGHT_HOST where ID in (");
      for (int i = 0; i < id.size(); i++) {
        sql.append("?,");
      }
      sql.deleteCharAt(sql.length() - 1);
      sql.append(")");
      ps = con.prepareStatement(sql.toString());
      for (int i = 0; i < id.size(); i++) {
        ps.setInt(i + 1, id.get(i));
      }

      int result = ps.executeUpdate();

      closeConnection(null, ps, null);

      // remove relations
      // delete tasks
      StringBuffer tsql = new StringBuffer();
      tsql.append("delete from HW_ESIGHT_TASK where HW_ESIGHT_HOST_ID in (");
      for (int i = 0; i < id.size(); i++) {
        tsql.append("?,");
      }
      tsql.deleteCharAt(tsql.length() - 1);
      tsql.append(")");
      ps = con.prepareStatement(tsql.toString());
      for (int i = 0; i < id.size(); i++) {
        ps.setInt(i + 1, id.get(i));
      }
      result += ps.executeUpdate();

      return result;
    } catch (SQLException e) {
      LOGGER.error("Failed to delete dme: " + e.getMessage());
      throw e;
    } finally {
      closeConnection(con, ps, null);
    }
  }

  public boolean updateSystemKeepAliveStatus(String ip, String status) throws SQLException {
    Connection con = null;
    PreparedStatement ps = null;
    try {
      con = getConnection();
      ps = con.prepareStatement(
          "update HW_ESIGHT_HOST set RESERVED_INT2 = ? where HOST_IP = ?");
      ps.setString(1, status);
      ps.setString(2, ip);
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      LOGGER.error("Failed to update SKA status: " + e.getMessage());
      throw e;
    } finally {
      closeConnection(con, ps, null);
    }
  }

  public boolean updateHAStatus(String ip, String status) throws SQLException {
    Connection con = null;
    PreparedStatement ps = null;
    try {
      con = getConnection();
      ps = con.prepareStatement(
          "update HW_ESIGHT_HOST set RESERVED_INT1 = ? where HOST_IP = ?");
      ps.setString(1, status);
      ps.setString(2, ip);
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      LOGGER.error("Failed to update HA status: " + e.getMessage());
      throw e;
    } finally {
      closeConnection(con, ps, null);
    }
  }

  public boolean updateHAProvider(int haProviderStatus) throws SQLException {
    Connection con = null;
    PreparedStatement ps = null;
    try {
      con = getConnection();
      ps = con.prepareStatement(
          "update HW_ESIGHT_HOST set HA_PROVIDER = ?");
      ps.setInt(1, haProviderStatus);
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      LOGGER.error("Failed to update HA provider: " + e.getMessage());
      throw e;
    } finally {
      closeConnection(con, ps, null);
    }
  }

  public boolean updateAlarmDefinition(int alarmDefinitionStatus) throws SQLException {
    Connection con = null;
    PreparedStatement ps = null;
    try {
      con = getConnection();
      ps = con.prepareStatement(
          "update HW_ESIGHT_HOST set ALARM_DEFINITION = ?");
      ps.setInt(1, alarmDefinitionStatus);
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      LOGGER.error("Failed to update alarm definitions: " + e.getMessage());
      throw e;
    } finally {
      closeConnection(con, ps, null);
    }
  }

  private void checkNullIp(String ip) throws SQLException {
    if (ip != null && ip.length() > 255) {
      throw new SQLException("parameter ip is not correct");
    }
  }

  private void checkIp(String ip) throws SQLException {
    if (ip == null || ip.length() > 255) {
      throw new SQLException("parameter ip is not correct");
    }
  }

  private void checkSystemId(String systemId) throws SQLException {
    if (systemId == null || systemId.length() > 50) {
      throw new SQLException("parameter systemId is not correct");
    }
  }

  private void checkIds(List<Integer> ids) throws SQLException {
    if (ids == null || ids.size() > 1000) {
      throw new SQLException("parameter ids is not correct");
    }
  }

  private void checkAliasName(String aliasName) throws SQLException {
    if (aliasName == null) {
      aliasName = "";
    }
    if (aliasName != null && aliasName.length() > 255) {
      throw new SQLException("parameter aliasName is not correct");
    }
    String regEx = "[a-zA-Z0-9_\\-\\.]{1,100}";
    Matcher matcher = Pattern.compile(regEx).matcher(aliasName);
    if (aliasName != null && aliasName.length() > 0 && !matcher.matches()) {
      throw new SQLException("parameter aliasName is not correct");
    }
  }

  private void checkLoginAccount(String loginAccount) throws SQLException {
    if (loginAccount == null || loginAccount.length() > 255) {
      throw new SQLException("parameter loginAccount is not correct");
    }
  }

  private void checkLoginPwd(String loginPwd) throws SQLException {
    if (loginPwd == null || loginPwd.length() > 255) {
      throw new SQLException("parameter loginPwd is not correct");
    }
  }

  private void checkDME(DME dme) throws SQLException {
    checkIp(dme.getHostIp());
    checkAliasName(dme.getAliasName());
    checkLoginAccount(dme.getLoginAccount());
    checkLoginPwd(dme.getLoginPwd());
    checkSystemId(dme.getSystemId());
  }
}
