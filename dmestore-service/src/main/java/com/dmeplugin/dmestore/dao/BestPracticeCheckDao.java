package com.dmeplugin.dmestore.dao;


import com.dmeplugin.dmestore.exception.DataBaseException;
import com.dmeplugin.dmestore.model.BestPracticeBean;
import com.dmeplugin.dmestore.model.BestPracticeUpResultBase;
import com.dmeplugin.dmestore.model.BestPracticeUpResultResponse;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * @ClassName: BestPracticeCheckDao
 * @Company: GH-CA
 * @author: wangxiangyong
 * @create: 2020-09-16
 **/
public class BestPracticeCheckDao extends H2DataBaseDao {

    public void save(List<BestPracticeBean> list) {
        if (null == list || list.size() == 0) {
            return;
        }

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = getConnection();
            String sql = "insert into HW_BEST_PRACTICE_CHECK(HOST_ID,HOST_NAME,HOST_SETTING,RECOMMEND_VALUE,ACTUAL_VALUE,HINT_LEVEL,NEED_REBOOT,AUTO_REPAIR,CREATE_TIME) values(?,?,?,?,?,?,?,?,?)";
            pstm = con.prepareStatement(sql);
            //不自动提交
            con.setAutoCommit(false);
            for (BestPracticeBean bean : list) {
                pstm.setString(1, bean.getHostObjectId());
                pstm.setString(2, bean.getHostName());
                pstm.setString(3, bean.getHostSetting());
                pstm.setString(4, bean.getRecommendValue());
                pstm.setString(5, bean.getActualValue());
                pstm.setString(6, bean.getLevel());
                pstm.setString(7, bean.getNeedReboot());
                pstm.setString(8, bean.getAutoRepair());
                pstm.setDate(9, new Date(System.currentTimeMillis()));
                pstm.addBatch();
            }
            pstm.executeBatch();
            con.commit();
        } catch (Exception ex) {
            try {
                //回滚
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            closeConnection(con, pstm, null);
        }
    }

    public List<String> getHostNameByHostsetting(String hostSetting) throws SQLException{
        List<String> lists = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT HOST_NAME from HW_BEST_PRACTICE_CHECK where 1=1 ";
            if (!StringUtils.isEmpty(hostSetting)) {
                sql = sql + " and HOST_SETTING='" + hostSetting + "'";
            }
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                lists.add(rs.getString("HOST_NAME"));
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("Failed to get host check info! " + e.getMessage());
            throw new SQLException(e);
        } finally {
            closeConnection(con, ps, rs);
        }
        return lists;
    }

    public List<BestPracticeBean> getRecordByPage(String hostSetting, int pageNo, int pageSize) throws SQLException{
        int offset = (pageNo - 1) * pageSize;
        List<BestPracticeBean> lists = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT HOST_ID,HOST_NAME,HOST_SETTING,RECOMMEND_VALUE,ACTUAL_VALUE,HINT_LEVEL,NEED_REBOOT,AUTO_REPAIR from HW_BEST_PRACTICE_CHECK where 1=1 ";
            if (!StringUtils.isEmpty(hostSetting)) {
                sql = sql + " and HOST_SETTING='" + hostSetting + "'";
            }

            sql = sql + " OFFSET " + offset + " ROWS FETCH FIRST " + pageSize + " ROWS ONLY";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                BestPracticeBean bean = new BestPracticeBean();
                bean.setHostObjectId(rs.getString("HOST_ID"));
                bean.setHostName(rs.getString("HOST_NAME"));
                bean.setHostSetting(rs.getString("HOST_SETTING"));
                bean.setRecommendValue(rs.getString("RECOMMEND_VALUE"));
                bean.setActualValue(rs.getString("ACTUAL_VALUE"));
                bean.setLevel(rs.getString("HINT_LEVEL"));
                bean.setNeedReboot(rs.getString("NEED_REBOOT"));
                bean.setAutoRepair(rs.getString("AUTO_REPAIR"));
                lists.add(bean);
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("Failed to get host check info by host_setting! " + e.getMessage());
            throw new SQLException(e);
        } finally {
            closeConnection(con, ps, rs);
        }
        return lists;
    }

    public List<BestPracticeBean> getRecordBeanByHostsetting(String hostSetting) throws SQLException{
        List<BestPracticeBean> lists = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT HOST_ID,HOST_NAME,HOST_SETTING,RECOMMEND_VALUE,ACTUAL_VALUE,HINT_LEVEL,NEED_REBOOT,AUTO_REPAIR from HW_BEST_PRACTICE_CHECK where 1=1 ";
            if (!StringUtils.isEmpty(hostSetting)) {
                sql = sql + " and HOST_SETTING='" + hostSetting + "'";
            }
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                BestPracticeBean bean = new BestPracticeBean();
                bean.setHostObjectId(rs.getString("HOST_ID"));
                bean.setHostName(rs.getString("HOST_NAME"));
                bean.setHostSetting(rs.getString("HOST_SETTING"));
                bean.setRecommendValue(rs.getString("RECOMMEND_VALUE"));
                bean.setActualValue(rs.getString("ACTUAL_VALUE"));
                bean.setLevel(rs.getString("HINT_LEVEL"));
                bean.setNeedReboot(rs.getString("NEED_REBOOT"));
                bean.setAutoRepair(rs.getString("AUTO_REPAIR"));
                lists.add(bean);
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("Failed to get host check info by host_setting! " + e.getMessage());
            throw new SQLException(e);
        } finally {
            closeConnection(con, ps, rs);
        }
        return lists;
    }

    public void deleteBy(List<BestPracticeUpResultResponse> list) {
        if (null == list || list.size() == 0) {
            return;
        }

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = getConnection();
            String sql = "delete from HW_BEST_PRACTICE_CHECK where host_id=? and host_setting=?";
            pstm = con.prepareStatement(sql);
            con.setAutoCommit(false);
            for (BestPracticeUpResultResponse response : list) {
                List<BestPracticeUpResultBase> baseList = response.getResult();
                for(BestPracticeUpResultBase base : baseList){
                    pstm.setString(1, base.getHostObjectId());
                    pstm.setString(2, base.getHostSetting());
                }
                pstm.addBatch();
            }
            pstm.executeBatch();
            con.commit();
        } catch (Exception ex) {
            try {
                //回滚
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            closeConnection(con, pstm, null);
        }
    }


    public void deleteByHostNameAndHostsetting(List<String> list, String hostSetting) {
        if (null == list || list.size() == 0) {
            return;
        }

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = getConnection();
            String sql = "delete from HW_BEST_PRACTICE_CHECK where host_name=? and host_setting=?";
            pstm = con.prepareStatement(sql);
            con.setAutoCommit(false);
            for (String hostName : list) {
                pstm.setString(1, hostName);
                pstm.setString(2, hostSetting);

                pstm.addBatch();
            }
            pstm.executeBatch();
            con.commit();
        } catch (Exception ex) {
            try {
                //回滚
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            closeConnection(con, pstm, null);
        }
    }


}
