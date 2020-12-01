package com.dmeplugin.dmestore.dao;

import com.dmeplugin.dmestore.constant.DPSqlFileConstant;
import com.dmeplugin.dmestore.entity.DmeInfo;
import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.exception.DataBaseException;
import com.dmeplugin.dmestore.services.DmeConstants;
import com.dmeplugin.dmestore.utils.CipherUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Description: TODO
 * @ClassName: DmeInfoDao
 * @Company: GH-CA
 * @author: yy
 * @create: 2020-09-02
 **/
public class DmeInfoDao extends H2DataBaseDao {

    public int addDmeInfo(DmeInfo dmeInfo) throws Exception {
        checkDmeInfo(dmeInfo);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(
                "INSERT INTO " + DPSqlFileConstant.DP_DME_ACCESS_INFO + " (hostIp,hostPort,username,password) "
                    + "VALUES (?,?,?,?)");
            ps.setString(1, dmeInfo.getHostIp());
            ps.setInt(2, dmeInfo.getHostPort());
            ps.setString(3, dmeInfo.getUserName());
            ps.setString(4, CipherUtils.encryptString(dmeInfo.getPassword()));
            int row = ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                dmeInfo.setId(rs.getInt(1));
            }
            return row;
        } catch (Exception e) {
            LOGGER.error("Failed to add vCenter info: " + e.toString());
            throw e;
        } finally {
            closeConnection(con, ps, rs);
        }
    }

    public DmeInfo getDmeInfo() throws DMEException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        DmeInfo dmeInfo = null;
        try {
            con = getConnection();
            ps = con.prepareStatement("SELECT * FROM " + DPSqlFileConstant.DP_DME_ACCESS_INFO + " WHERE state=1");
            rs = ps.executeQuery();
            if (rs.next()) {
                dmeInfo = new DmeInfo();
                dmeInfo.setId(rs.getInt("ID"));
                dmeInfo.setHostIp(rs.getString("HOSTIP"));
                dmeInfo.setHostPort(rs.getInt("HOSTPORT"));
                dmeInfo.setUserName(rs.getString("USERNAME"));
                dmeInfo.setPassword(CipherUtils.decryptString(rs.getString("PASSWORD")));
                dmeInfo.setCreateTime(rs.getTimestamp("CREATETIME"));
                dmeInfo.setUpdateTime(rs.getTimestamp("UPDATETIME"));
                dmeInfo.setState(rs.getInt("STATE"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Failed to get dme access info: " + e.toString());
            throw new DMEException("503", e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return dmeInfo;
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

    private void checkDmeInfo(DmeInfo dmeInfo) throws SQLException {
        checkIp(dmeInfo.getHostIp());
        checkUserName(dmeInfo.getUserName());
        checkPassword(dmeInfo.getPassword());
    }
}
