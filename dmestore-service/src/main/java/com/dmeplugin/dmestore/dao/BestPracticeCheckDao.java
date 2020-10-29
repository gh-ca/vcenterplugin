package com.dmeplugin.dmestore.dao;


import com.dmeplugin.dmestore.exception.DataBaseException;
import com.dmeplugin.dmestore.model.BestPracticeBean;
import com.dmeplugin.dmestore.model.BestPracticeUpResultBase;
import com.dmeplugin.dmestore.model.BestPracticeUpResultResponse;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: wangxiangyong
 **/
public class BestPracticeCheckDao extends H2DataBaseDao {

    public void save(List<BestPracticeBean> list) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = getConnection();
            String sql = "insert into DP_DME_BEST_PRACTICE_CHECK(HOST_ID,HOST_NAME,HOST_SETTING,RECOMMEND_VALUE,ACTUAL_VALUE,HINT_LEVEL,NEED_REBOOT,AUTO_REPAIR,CREATE_TIME) values(?,?,?,?,?,?,?,?,?)";
            pstm = con.prepareStatement(sql);
            //不自动提交
            con.setAutoCommit(false);
            for (BestPracticeBean bean : list) {
                pstm.setString(1, bean.getHostObjectId());
                pstm.setString(2, bean.getHostName());
                pstm.setString(3, bean.getHostSetting());
                pstm.setString(4, bean.getRecommendValue());
                pstm.setCharacterStream(5, new StringReader(bean.getActualValue()));
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

    public List<String> getHostNameByHostsetting(String hostSetting) throws SQLException {
        List<String> lists = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT HOST_NAME from DP_DME_BEST_PRACTICE_CHECK where 1=1 ";
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

    public Map<String, String> getAllHostIds(int pageNo, int pageSize) throws SQLException {
        Map<String, String> map = new HashMap<>(16);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT HOST_ID,HOST_NAME from DP_DME_BEST_PRACTICE_CHECK where 1=1 ";
            if (pageNo > 0 && pageSize > 0) {
                int offset = (pageNo - 1) * pageSize;
                sql = sql + " OFFSET " + offset + " ROWS FETCH FIRST " + pageSize + " ROWS ONLY";
            }
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("HOST_ID"), rs.getString("HOST_NAME"));
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("getAllHostIds Failed! " + e.getMessage());
            throw new SQLException(e);
        } finally {
            closeConnection(con, ps, rs);
        }
        return map;
    }

    public Map<String, String> getByHostIds(List<String> ids) throws SQLException {
        Map<String, String> map = new HashMap<>(16);
        if(null == ids || ids.size() == 0){
            return map;
        }
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT HOST_ID,HOST_NAME from DP_DME_BEST_PRACTICE_CHECK where 1=1 AND HOST_ID in(%s)";
            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 0; i < ids.size(); i++){
                stringBuilder.append("'").append(ids.get(i)).append("'");
                if(i < ids.size() -1){
                    stringBuilder.append(",");
                }
            }
            sql = String.format(sql, stringBuilder.toString());
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("HOST_ID"), rs.getString("HOST_NAME"));
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("getAllHostIds Failed! " + e.getMessage());
            throw new SQLException(e);
        } finally {
            closeConnection(con, ps, rs);
        }
        return map;
    }

    public List<BestPracticeBean> getRecordByPage(String hostSetting, int pageNo, int pageSize) throws SQLException {
        int offset = (pageNo - 1) * pageSize;
        List<BestPracticeBean> lists = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT HOST_ID,HOST_NAME,HOST_SETTING,RECOMMEND_VALUE,ACTUAL_VALUE,HINT_LEVEL,NEED_REBOOT,AUTO_REPAIR from DP_DME_BEST_PRACTICE_CHECK where 1=1 ";
            if (!StringUtils.isEmpty(hostSetting)) {
                sql = sql + " and HOST_SETTING='" + hostSetting + "'";
            }

            sql = sql + " OFFSET " + offset + " ROWS FETCH FIRST " + pageSize + " ROWS ONLY";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                BestPracticeBean bean = getBestPracticeBean(rs);
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

    public List<BestPracticeBean> getRecordBeanByHostsetting(String hostSetting) throws SQLException {
        List<BestPracticeBean> lists = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT HOST_ID,HOST_NAME,HOST_SETTING,RECOMMEND_VALUE,ACTUAL_VALUE,HINT_LEVEL,NEED_REBOOT,AUTO_REPAIR from DP_DME_BEST_PRACTICE_CHECK where 1=1 ";
            if (!StringUtils.isEmpty(hostSetting)) {
                sql = sql + " and HOST_SETTING='" + hostSetting + "'";
            }
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                BestPracticeBean bean = getBestPracticeBean(rs);
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

    private BestPracticeBean getBestPracticeBean(ResultSet rs) throws SQLException {
        BestPracticeBean bean = new BestPracticeBean();
        bean.setHostObjectId(rs.getString("HOST_ID"));
        bean.setHostName(rs.getString("HOST_NAME"));
        bean.setHostSetting(rs.getString("HOST_SETTING"));
        bean.setRecommendValue(rs.getString("RECOMMEND_VALUE"));
        bean.setActualValue(clobStreamToStr(rs.getClob("ACTUAL_VALUE")));
        bean.setLevel(rs.getString("HINT_LEVEL"));
        bean.setNeedReboot(rs.getString("NEED_REBOOT"));
        bean.setAutoRepair(rs.getString("AUTO_REPAIR"));
        return bean;
    }

    private String clobStreamToStr(Clob clob) {
        try {
            InputStream input = clob.getAsciiStream();
            int len = (int) clob.length();
            byte[] by = new byte[len];
            int i;
            while (-1 != (i = input.read(by, 0, by.length))) {
                input.read(by, 0, i);
            }
            return new String(by);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void deleteBy(List<BestPracticeUpResultResponse> list) {
        if (null == list || list.size() == 0) {
            return;
        }

        Connection con = null;
        Statement stm = null;
        try {
            con = getConnection();
            String sql = "delete from DP_DME_BEST_PRACTICE_CHECK where host_id='%s' and host_setting='%s'";
            stm = con.createStatement();
            con.setAutoCommit(false);
            for (BestPracticeUpResultResponse response : list) {
                List<BestPracticeUpResultBase> baseList = response.getResult();
                for (BestPracticeUpResultBase base : baseList) {
                    //只有更新成功的才执行删除
                    if(base.getUpdateResult()){
                        String sqlTemp = String.format(sql, base.getHostObjectId(), base.getHostSetting());
                        stm.addBatch(sqlTemp);
                    }
                }
            }
            stm.executeBatch();
            con.commit();
        } catch (Exception ex) {
            try {
                //回滚
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            closeConnection(con, stm, null);
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
            String sql = "delete from DP_DME_BEST_PRACTICE_CHECK where host_name=? and host_setting=?";
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

    public void update(List<BestPracticeBean> list) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = getConnection();
            String sql = "UPDATE DP_DME_BEST_PRACTICE_CHECK SET ACTUAL_VALUE=?,CREATE_TIME=? where HOST_NAME=? and HOST_SETTING=?";
            pstm = con.prepareStatement(sql);
            //不自动提交
            con.setAutoCommit(false);
            for (BestPracticeBean bean : list) {
                pstm.setCharacterStream(1, new StringReader(bean.getActualValue()));
                pstm.setDate(2, new Date(System.currentTimeMillis()));
                pstm.setString(3, bean.getHostName());
                pstm.setString(4, bean.getHostSetting());
                pstm.addBatch();
            }
            pstm.executeUpdate();
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
