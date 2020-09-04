package com.dmeplugin.dmestore.dao;


import com.dmeplugin.dmestore.constant.DPSqlFileConstant;
import com.dmeplugin.dmestore.entity.DmeInfo;
import com.dmeplugin.dmestore.exception.DataBaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DmeInfoDao extends H2DataBaseDao {

    public int addDmeInfo(DmeInfo dmeInfo) throws SQLException {
        checkDmeInfo(dmeInfo);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(
                    "INSERT INTO " + DPSqlFileConstant.DP_DME_ACCESS_INFO + " (hostIp,hostPort,username,password) " +
                            "VALUES (?,?,?,?)");
            ps.setString(1, dmeInfo.getHostIp());
            ps.setInt(2, dmeInfo.getHostPort());
            ps.setString(3, dmeInfo.getUserName());
            ps.setString(4, dmeInfo.getPassword());
            int row = ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                dmeInfo.setId(rs.getInt(1));
            }
            return row;
        } catch (SQLException e) {
            LOGGER.error("Failed to add vCenter info: " + e.getMessage());
            throw e;
        } finally {
            closeConnection(con, ps, rs);
        }
    }

    public DmeInfo getDmeInfo() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement("SELECT * FROM " + DPSqlFileConstant.DP_DME_ACCESS_INFO
                    + " WHERE state=1");
            rs = ps.executeQuery();
            if (rs.next()) {
                DmeInfo dmeInfo = new DmeInfo();
                dmeInfo.setId(rs.getInt("ID"));
                dmeInfo.setHostIp(rs.getString("HOSTIP"));
                dmeInfo.setHostPort(rs.getInt("HOSTPORT"));
                dmeInfo.setUserName(rs.getString("USERNAME"));
                dmeInfo.setPassword(rs.getString("PASSWORD"));
                dmeInfo.setCreateTime(rs.getTimestamp("CREATETIME"));
                dmeInfo.setUpdateTime(rs.getTimestamp("UPDATETIME"));
                dmeInfo.setState(rs.getInt("STATE"));
                return dmeInfo;
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("Failed to get dme access info: " + e.getMessage());
            throw new SQLException(e);
        } finally {
            closeConnection(con, ps, rs);
        }
        return null;
    }


    private void checkIp(String ip) throws SQLException {
        if (ip == null || ip.length() > 255) {
            throw new SQLException("parameter ip is not correct");
        }
    }

    private void checkUserName(String userName) throws SQLException {
        if (userName == null || userName.length() > 255) {
            throw new SQLException("parameter userName is not correct");
        }
    }

    private void checkPassword(String password) throws SQLException {
        if (password == null || password.length() > 255) {
            throw new SQLException("parameter password is not correct");
        }
    }


    private void checkDmeInfo(DmeInfo dmeInfo) throws SQLException {
        checkIp(dmeInfo.getHostIp());
        checkUserName(dmeInfo.getUserName());
        checkPassword(dmeInfo.getPassword());
    }
}
