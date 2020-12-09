package com.dmeplugin.dmestore.dao;

import com.dmeplugin.dmestore.constant.DPSqlFileConstant;
import com.dmeplugin.dmestore.entity.VCenterInfo;
import com.dmeplugin.dmestore.exception.DataBaseException;
import com.dmeplugin.dmestore.exception.DmeSqlException;
import com.dmeplugin.dmestore.constant.DmeConstants;

import java.sql.*;

/**
 * @Description: TODO
 * @ClassName: VCenterInfoDao
 * @Company: GH-CA
 * @author: lgq
 * @create: 2020-09-02
 **/
public class VCenterInfoDao extends H2DataBaseDao {
    public int addVcenterInfo(VCenterInfo vCenterInfo) throws DmeSqlException {
        checkVcenterInfo(vCenterInfo);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement("INSERT INTO " + DPSqlFileConstant.DP_DME_VCENTER_INFO + " (HOST_IP,USER_NAME,"
                + "PASSWORD,STATE,CREATE_TIME,PUSH_EVENT,PUSH_EVENT_LEVEL,HOST_PORT) VALUES (?,?,?,?,CURRENT_TIMESTAMP,?,?,?)");
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
            throw new DmeSqlException(e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
    }

    public int updateVcenterInfo(VCenterInfo vCenterInfo) throws DmeSqlException {
        checkVcenterInfo(vCenterInfo);
        checkId(vCenterInfo.getId());
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
            throw new DmeSqlException(e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
    }

    public VCenterInfo getVcenterInfo() throws DmeSqlException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(
                "SELECT * FROM " + DPSqlFileConstant.DP_DME_VCENTER_INFO + " ORDER BY CREATE_TIME DESC LIMIT 1");
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
            throw new DmeSqlException(e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return null;
    }

    private void checkIp(String ip) throws DmeSqlException {
        if (ip == null || ip.length() > DmeConstants.MAXLEN) {
            throw new DmeSqlException("parameter ip is not correct");
        }
    }

    private void checkUserName(String userName) throws DmeSqlException {
        if (userName == null || userName.length() > DmeConstants.MAXLEN) {
            throw new DmeSqlException("parameter userName is not correct");
        }
    }

    private void checkPassword(String password) throws DmeSqlException {
        if (password == null || password.length() > DmeConstants.MAXLEN) {
            throw new DmeSqlException("parameter password is not correct");
        }
    }

    private void checkId(int id) throws DmeSqlException {
        if (id < 1) {
            throw new DmeSqlException("parameter is is not correct");
        }

    }

    private void checkVcenterInfo(VCenterInfo vCenterInfo) throws DmeSqlException {
        checkIp(vCenterInfo.getHostIp());
        checkUserName(vCenterInfo.getUserName());
        checkPassword(vCenterInfo.getPassword());
    }
}
