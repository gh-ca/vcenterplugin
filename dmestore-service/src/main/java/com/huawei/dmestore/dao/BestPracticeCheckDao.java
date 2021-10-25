package com.huawei.dmestore.dao;

import com.huawei.dmestore.constant.DpSqlFileConstants;
import com.huawei.dmestore.exception.DataBaseException;
import com.huawei.dmestore.exception.DmeSqlException;
import com.huawei.dmestore.model.BestPracticeBean;
import com.huawei.dmestore.model.BestPracticeLog;
import com.huawei.dmestore.model.BestPracticeRecommand;
import com.huawei.dmestore.model.BestPracticeRecommandUpReq;
import com.huawei.dmestore.model.BestPracticeUpResultBase;
import com.huawei.dmestore.model.BestPracticeUpResultResponse;

import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * BestPracticeCheckDao
 *
 * @author wangxiangyong
 * @since 2020-09-15
 **/
public class BestPracticeCheckDao extends H2DataBaseDao {
    public static final String HOST_NAME = "HOST_NAME";

    public static final String HOST_ID = "HOST_ID";

    public static final String HOST_SETTING = "HOST_SETTING";

    private static final int PARAMETER_INDEX_1 = 1;

    private static final int PARAMETER_INDEX_2 = 2;

    private static final int COLLECTIONS_DEFAULT_LEN = 16;

    private final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * save
     *
     * @param list list
     */
    public void save(List<BestPracticeBean> list) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = getConnection();
            String sql = "insert into DP_DME_BEST_PRACTICE_CHECK(HOST_ID,HOST_NAME,HOST_SETTING,RECOMMEND_VALUE,"
                + "ACTUAL_VALUE,HINT_LEVEL,NEED_REBOOT,AUTO_REPAIR,CREATE_TIME) values(?,?,?,?,?,?,?,?,?)";
            pstm = con.prepareStatement(sql);

            // 不自动提交
            con.setAutoCommit(false);
            for (BestPracticeBean bean : list) {
                pstm.setString(DpSqlFileConstants.DIGIT_1, bean.getHostObjectId());
                pstm.setString(DpSqlFileConstants.DIGIT_2, bean.getHostName());
                pstm.setString(DpSqlFileConstants.DIGIT_3, bean.getHostSetting());
                pstm.setString(DpSqlFileConstants.DIGIT_4, bean.getRecommendValue());
                pstm.setCharacterStream(DpSqlFileConstants.DIGIT_5, new StringReader(bean.getActualValue()));
                pstm.setString(DpSqlFileConstants.DIGIT_6, bean.getLevel());
                pstm.setString(DpSqlFileConstants.DIGIT_7, bean.getNeedReboot());
                pstm.setString(DpSqlFileConstants.DIGIT_8, bean.getAutoRepair());
                pstm.setDate(DpSqlFileConstants.DIGIT_9, new Date(System.currentTimeMillis()));
                pstm.addBatch();
            }
            pstm.executeBatch();
            con.commit();
        } catch (SQLException ex) {
            LOGGER.error("save bestPracticeBean error!errMsg={}", ex.getMessage());
            try {
                // 回滚
                con.rollback();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
            }
        } finally {
            closeConnection(con, pstm, null);
        }
    }

    /**
     * getHostNameByHostsetting
     *
     * @param hostSetting hostSetting
     * @return List
     * @throws SQLException SQLException
     */
    public List<String> getHostNameByHostsetting(String hostSetting) throws SQLException {
        List<String> lists = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT HOST_NAME from DP_DME_BEST_PRACTICE_CHECK where 1=1 ";
            if (!StringUtils.isEmpty(hostSetting)) {
                sql = sql + " and HOST_SETTING ='" + hostSetting + "' ";
            }
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                lists.add(rs.getString(HOST_NAME));
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("Failed to get host check info! {}", e.getMessage());
            throw new SQLException(e);
        } finally {
            closeConnection(con, ps, rs);
        }
        return lists;
    }

    /**
     * getAllHostIds
     *
     * @param pageNo pageNo
     * @param pageSize pageSize
     * @return Map
     * @throws SQLException SQLException
     */
    public Map<String, Map<String, String>> getAllHostIds(int pageNo, int pageSize, String hostSetting)
        throws SQLException {
        Map<String, Map<String, String>> map = new HashMap<>(COLLECTIONS_DEFAULT_LEN);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT HOST_ID,HOST_NAME,HOST_SETTING from DP_DME_BEST_PRACTICE_CHECK where 1=1 ";
            if (!StringUtils.isEmpty(hostSetting)) {
                sql = sql + "and HOST_SETTING='" + hostSetting + "' ";
            }
            int fetchSize = pageSize > 0 ? pageSize : 100;
            int offset = 0;
            if (pageNo > 0) {
                offset = (pageNo - 1) * pageSize;
            }
            sql = sql + " OFFSET " + offset + " ROWS FETCH FIRST " + fetchSize + " ROWS ONLY";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, String> temp = new HashMap<>();
                temp.put(HOST_NAME, rs.getString(HOST_NAME));
                temp.put(HOST_SETTING, rs.getString(HOST_SETTING));
                map.put(rs.getString(HOST_ID), temp);
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("getAllHostIds Failed! {}", e.getMessage());
            throw new SQLException(e);
        } finally {
            closeConnection(con, ps, rs);
        }
        return map;
    }

    /**
     * getByHostIds
     *
     * @param ids ids
     * @return Map
     * @throws SQLException SQLException
     */
    public Map<String, Map<String, String>> getByHostIds(List<String> ids, String hostSetting) throws SQLException {
        Map<String, Map<String, String>> map = new HashMap<>(COLLECTIONS_DEFAULT_LEN);
        if (ids == null || ids.size() == 0) {
            return map;
        }
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT HOST_ID,HOST_NAME,HOST_SETTING from DP_DME_BEST_PRACTICE_CHECK where 1=1 ";
            if (!StringUtils.isEmpty(hostSetting)) {
                sql = sql + " AND HOST_SETTING='" + hostSetting + "' ";
            }
            sql = sql + " AND HOST_ID in(";
            StringBuilder stringBuilder = new StringBuilder(sql);
            for (int index = 0; index < ids.size(); index++) {
                if (index == ids.size() - 1) {
                    stringBuilder.append("?)");
                } else {
                    stringBuilder.append("?,");
                }
            }
            ps = con.prepareStatement(stringBuilder.toString());
            for (int index = 0; index < ids.size(); index++) {
                ps.setString(index + 1, ids.get(index));
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, String> temp = new HashMap<>();
                temp.put(HOST_NAME, rs.getString(HOST_NAME));
                temp.put(HOST_SETTING, rs.getString(HOST_SETTING));
                map.put(rs.getString(HOST_ID), temp);
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("getAllHostIds Failed! {}", e.getMessage());
            throw new SQLException(e);
        } finally {
            closeConnection(con, ps, rs);
        }
        return map;
    }

    /**
     * getRecordByPage
     *
     * @param hostSetting hostSetting
     * @param pageNo pageNo
     * @param pageSize pageSize
     * @return List
     * @throws SQLException SQLException
     */
    public List<BestPracticeBean> getRecordByPage(String hostSetting, int pageNo, int pageSize) throws SQLException {
        int offset = (pageNo - 1) * pageSize;
        List<BestPracticeBean> lists = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql =
                "SELECT HOST_ID,HOST_NAME,HOST_SETTING,RECOMMEND_VALUE,ACTUAL_VALUE,HINT_LEVEL,NEED_REBOOT,AUTO_REPAIR "
                    + " from DP_DME_BEST_PRACTICE_CHECK where 1=1 ";
            if (!StringUtils.isEmpty(hostSetting)) {
                sql = sql + " and HOST_SETTING=? ";
            }
            sql = sql + " OFFSET " + offset + " ROWS FETCH FIRST " + pageSize + " ROWS ONLY";
            if (!StringUtils.isEmpty(hostSetting)) {
                ps = con.prepareStatement(sql);
                ps.setString(1, hostSetting);
            } else {
                ps = con.prepareStatement(sql);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                BestPracticeBean bean = getBestPracticeBean(rs);
                lists.add(bean);
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("Failed to get host check info by host_setting! {}", e.getMessage());
            throw new SQLException(e);
        } finally {
            closeConnection(con, ps, rs);
        }
        return lists;
    }

    public List<BestPracticeBean> getRecordBeanByHostSetting(String hostSetting, String id) throws SQLException {
        List<String> ids = null;
        if (!StringUtils.isEmpty(id)) {
            ids = new ArrayList<>();
            ids.add(id);
        }
        return getRecordBeanByHostSetting(hostSetting, ids);
    }

    /**
     * getRecordBeanByHostsetting
     *
     * @param hostSetting hostSetting
     * @param ids id
     * @return List
     * @throws SQLException SQLException
     */
    public List<BestPracticeBean> getRecordBeanByHostSetting(String hostSetting, List<String> ids) throws SQLException {
        List<BestPracticeBean> lists = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT HOST_ID,HOST_NAME,HOST_SETTING,RECOMMEND_VALUE,ACTUAL_VALUE,HINT_LEVEL,NEED_REBOOT,"
                + "AUTO_REPAIR from DP_DME_BEST_PRACTICE_CHECK where 1=1 ";
            if (!StringUtils.isEmpty(hostSetting)) {
                sql = sql + " and HOST_SETTING='" + hostSetting + "'";
                boolean isId = (ids != null && ids.size() > 0);
                if (isId) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String id : ids) {
                        stringBuilder.append("'").append(id).append("'").append(",");
                    }
                    sql = sql + " and HOST_ID in(" + stringBuilder.substring(0, stringBuilder.lastIndexOf(",")) + ")";
                }
                ps = con.prepareStatement(sql);
            } else {
                ps = con.prepareStatement(sql);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                BestPracticeBean bean = getBestPracticeBean(rs);
                lists.add(bean);
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("Failed to get host check info by host_setting! {}", e.getMessage());
            throw new SQLException(e);
        } finally {
            closeConnection(con, ps, rs);
        }
        return lists;
    }

    private BestPracticeBean getBestPracticeBean(ResultSet rs) throws SQLException {
        BestPracticeBean bean = new BestPracticeBean();
        bean.setHostObjectId(rs.getString(HOST_ID));
        bean.setHostName(rs.getString(HOST_NAME));
        bean.setHostSetting(rs.getString("HOST_SETTING"));
        bean.setRecommendValue(rs.getString("RECOMMEND_VALUE"));
        try {
            String acturalValue = clobToString(rs.getClob("ACTUAL_VALUE"));
            bean.setActualValue(acturalValue);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        bean.setLevel(rs.getString("HINT_LEVEL"));
        bean.setNeedReboot(rs.getString("NEED_REBOOT"));
        bean.setAutoRepair(rs.getString("AUTO_REPAIR"));
        return bean;
    }

    /**
     * clobToString
     *
     * @param clob clob
     * @return String
     * @throws SQLException SQLException
     * @throws IOException IOException
     */
    public String clobToString(Clob clob) throws SQLException, IOException {
        String reString = "";
        Reader is = clob.getCharacterStream();
        BufferedReader br = new BufferedReader(is);
        String str = br.readLine();
        StringBuffer sb = new StringBuffer();
        while (str != null) {
            sb.append(str);
            str = br.readLine();
        }
        reString = sb.toString();
        if (br != null) {
            br.close();
        }
        if (is != null) {
            is.close();
        }
        return reString;
    }

    /**
     * deleteBy
     *
     * @param list list
     */
    public void deleteBy(List<BestPracticeUpResultResponse> list) {
        if (list == null || list.size() == 0) {
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
                    // 只有更新成功的才执行删除
                    if (base.getUpdateResult()) {
                        String sqlTemp = String.format(sql, base.getHostObjectId(), base.getHostSetting());
                        stm.addBatch(sqlTemp);
                    }
                }
            }
            stm.executeBatch();
            con.commit();
        } catch (SQLException ex) {
            try {
                // 回滚
                con.rollback();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
            }
        } finally {
            closeConnection(con, stm, null);
        }
    }

    /**
     * deleteByHostIds
     *
     * @param ids ids
     */
    public void deleteByHostIds(List<String> ids) {
        if (ids == null || ids.size() == 0) {
            return;
        }

        Connection con = null;
        Statement stm = null;
        try {
            con = getConnection();
            String sql = "delete from DP_DME_BEST_PRACTICE_CHECK where host_id='%s'";
            stm = con.createStatement();
            con.setAutoCommit(false);
            for (String id : ids) {
                String sqlTemp = String.format(sql, id);
                stm.addBatch(sqlTemp);
            }
            stm.executeBatch();
            con.commit();
        } catch (SQLException ex) {
            try {
                // 回滚
                con.rollback();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
            }
        } finally {
            closeConnection(con, stm, null);
        }
    }

    /**
     * deleteByHostNameAndHostsetting
     *
     * @param list list
     * @param hostSetting hostSetting
     */
    public void deleteByHostNameAndHostsetting(List<String> list, String hostSetting) {
        if (list == null || list.size() == 0) {
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
                pstm.setString(DpSqlFileConstants.DIGIT_1, hostName);
                pstm.setString(DpSqlFileConstants.DIGIT_2, hostSetting);

                pstm.addBatch();
            }
            pstm.executeBatch();
            con.commit();
        } catch (SQLException ex) {
            try {
                // 回滚
                con.rollback();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
            }
        } finally {
            closeConnection(con, pstm, null);
        }
    }

    /**
     * update
     *
     * @param list list
     */
    public void update(List<BestPracticeBean> list) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = getConnection();
            String sql = "UPDATE DP_DME_BEST_PRACTICE_CHECK SET ACTUAL_VALUE=?,CREATE_TIME=? "
                + "where HOST_NAME=? and HOST_SETTING=?";
            pstm = con.prepareStatement(sql);

            // 不自动提交
            con.setAutoCommit(false);
            for (BestPracticeBean bean : list) {
                pstm.setCharacterStream(DpSqlFileConstants.DIGIT_1, new StringReader(bean.getActualValue()));
                pstm.setDate(DpSqlFileConstants.DIGIT_2, new Date(System.currentTimeMillis()));
                pstm.setString(DpSqlFileConstants.DIGIT_3, bean.getHostName());
                pstm.setString(DpSqlFileConstants.DIGIT_4, bean.getHostSetting());
                pstm.addBatch();
            }
            pstm.executeBatch();
            con.commit();
        } catch (SQLException ex) {
            try {
                // 回滚
                con.rollback();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
            }
        } finally {
            closeConnection(con, pstm, null);
        }
    }

    public BestPracticeRecommand getRecommand(String hostsetting, String recommandValue) throws DmeSqlException {
        BestPracticeRecommand recommand = getRecommandFromDb("HOST_SETTING", hostsetting);
        if (recommand != null) {
            return recommand;
        }

        // 如果没有就默认生成一条记录
        initRecommand(hostsetting, recommandValue, "0");
        return getRecommandFromDb("HOST_SETTING", hostsetting);
    }

    public int updateRecommendByFiled(String filedName, String filedValue, BestPracticeRecommandUpReq req)
        throws DmeSqlException {
        String recommandVale = req.getRecommandValue();
        String repairAction = req.getRepairAction();
        if (StringUtils.isEmpty(recommandVale) && StringUtils.isEmpty(repairAction)) {
            LOGGER.error("best practice recommend update error!All parameters are null!");
            throw new DmeSqlException("parameter error!");
        }

        BestPracticeRecommand recommand = getRecommandFromDb(filedName, String.valueOf(filedValue));
        if (null == recommand) {
            throw new DmeSqlException("recommend record not found!");
        }

        if (!StringUtils.isEmpty(recommandVale)) {
            recommand.setRecommandValue(recommandVale);
            recommand.setUpdateRecommendTime(new java.util.Date(System.currentTimeMillis()));
        }

        if (!StringUtils.isEmpty(repairAction)) {
            recommand.setRepairAction(repairAction);
            recommand.setUpdateRepairTime(new java.util.Date(System.currentTimeMillis()));
        }

        return updateRecommandFromDb(recommand);
    }

    private int updateRecommandFromDb(BestPracticeRecommand recommand) throws DmeSqlException {
        Connection con = null;
        PreparedStatement pstm = null;
        int upDateSize;
        try {
            con = getConnection();
            String sql = "UPDATE DP_DME_BEST_PRACTICE_RECOMMAND SET RECOMMEND_VALUE=?,UPDATE_RECOMMEND_TIME=?, "
                + "REPAIR_ACTION=?,UPDATE_REPAIR_TIME=? where ID=?";
            pstm = con.prepareStatement(sql);
            pstm.setString(DpSqlFileConstants.DIGIT_1, recommand.getRecommandValue());
            pstm.setString(DpSqlFileConstants.DIGIT_2, fmt.format(recommand.getUpdateRecommendTime()));
            pstm.setString(DpSqlFileConstants.DIGIT_3, recommand.getRepairAction());
            pstm.setString(DpSqlFileConstants.DIGIT_4, fmt.format(recommand.getUpdateRepairTime()));
            pstm.setString(DpSqlFileConstants.DIGIT_5, recommand.getId());
            pstm.execute();
            upDateSize = 1;
        } catch (SQLException ex) {
            throw new DmeSqlException(ex.getMessage());
        } finally {
            closeConnection(con, pstm, null);
        }

        return upDateSize;
    }

    private BestPracticeRecommand getRecommandFromDb(String filedName, String filedValue) throws DmeSqlException {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        BestPracticeRecommand recommand = null;
        try {
            con = getConnection();
            String sql = "SELECT * FROM DP_DME_BEST_PRACTICE_RECOMMAND where " + filedName + "=?";
            pstm = con.prepareStatement(sql);
            pstm.setString(PARAMETER_INDEX_1, filedValue);
            rs = pstm.executeQuery();
            if (rs.next()) {
                recommand = new BestPracticeRecommand();
                recommand.setId(rs.getString("ID"));
                recommand.setHostsetting(rs.getString("HOST_SETTING"));
                recommand.setRecommandValue(rs.getString("RECOMMEND_VALUE"));
                recommand.setRepairAction(rs.getString("REPAIR_ACTION"));
                recommand.setCreateTime(rs.getTimestamp("CREATE_TIME"));
                recommand.setUpdateRecommendTime(rs.getTimestamp("UPDATE_RECOMMEND_TIME"));
                recommand.setUpdateRepairTime(rs.getTimestamp("UPDATE_REPAIR_TIME"));
            }
        } catch (SQLException ex) {
            throw new DmeSqlException(ex.getMessage());
        } finally {
            closeConnection(con, pstm, rs);
        }

        return recommand;
    }

    private void initRecommand(String hostSetting, String recommandVale, String repairAction) {
        BestPracticeRecommand recommand = new BestPracticeRecommand();
        String uuid = UUID.randomUUID().toString();
        recommand.setId(uuid);
        recommand.setHostsetting(hostSetting);
        recommand.setRecommandValue(recommandVale);
        recommand.setRepairAction(repairAction);
        java.util.Date date = new java.util.Date(System.currentTimeMillis());
        recommand.setCreateTime(date);
        recommand.setUpdateRecommendTime(date);
        recommand.setUpdateRepairTime(date);
        saveRecommand(recommand);
    }

    private void saveRecommand(BestPracticeRecommand recommand) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = getConnection();
            String sql = "insert into DP_DME_BEST_PRACTICE_RECOMMAND(HOST_SETTING,RECOMMEND_VALUE,REPAIR_ACTION,"
                + "CREATE_TIME,UPDATE_RECOMMEND_TIME,UPDATE_REPAIR_TIME) values(?,?,?,?,?,?)";
            pstm = con.prepareStatement(sql);

            // 不自动提交
            con.setAutoCommit(false);
            pstm.setString(DpSqlFileConstants.DIGIT_1, recommand.getHostsetting());
            pstm.setString(DpSqlFileConstants.DIGIT_2, recommand.getRecommandValue());
            pstm.setString(DpSqlFileConstants.DIGIT_3, recommand.getRepairAction());
            String dateStr = fmt.format(recommand.getCreateTime());
            pstm.setString(DpSqlFileConstants.DIGIT_4, dateStr);
            pstm.setString(DpSqlFileConstants.DIGIT_5, dateStr);
            pstm.setString(DpSqlFileConstants.DIGIT_6, dateStr);
            pstm.execute();
            con.commit();
        } catch (SQLException ex) {
            LOGGER.error("save recommand vale error!errMsg={}", ex.getMessage());
            try {
                // 回滚
                con.rollback();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
            }
        } finally {
            closeConnection(con, pstm, null);
        }
    }

    public void saveRepairLog(List<BestPracticeLog> logs) {
        if (logs == null || logs.size() == 0) {
            return;
        }

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = getConnection();
            String sql = "insert into DP_DME_BEST_PRACTICE_LOG(OBJECT_NAME,OBJECT_ID,HOST_SETTING,RECOMMEND_VALUE,"
                + "VIOLATION_VALUE,REPAIR_TYPE,REPAIR_RESULT,REPAIR_TIME,MESSAGE) values(?,?,?,?,?,?,?,?,?)";
            pstm = con.prepareStatement(sql);

            // 不自动提交
            con.setAutoCommit(false);
            for (BestPracticeLog log : logs) {
                pstm.setString(DpSqlFileConstants.DIGIT_1, log.getObjectName());
                pstm.setString(DpSqlFileConstants.DIGIT_2, log.getObjectId());
                pstm.setString(DpSqlFileConstants.DIGIT_3, log.getHostsetting());
                pstm.setString(DpSqlFileConstants.DIGIT_4, log.getRecommandValue());
                pstm.setString(DpSqlFileConstants.DIGIT_5, log.getViolationValue());
                pstm.setString(DpSqlFileConstants.DIGIT_6, log.getRepairType());
                pstm.setBoolean(DpSqlFileConstants.DIGIT_7, log.getRepairResult());
                pstm.setString(DpSqlFileConstants.DIGIT_8, fmt.format(log.getRepairTime()));
                pstm.setCharacterStream(DpSqlFileConstants.DIGIT_9, new StringReader(log.getMessage()));
                pstm.addBatch();
            }
            pstm.executeBatch();
            con.commit();
        } catch (SQLException ex) {
            LOGGER.error("save best practice log error!errMsg={}", ex.getMessage());
            try {
                // 回滚
                con.rollback();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
            }
        } finally {
            closeConnection(con, pstm, null);
        }
    }

    public List<BestPracticeLog> getRepariLogByPage(String hostsetting, String objId, int pageNo, int pageSize)
        throws DmeSqlException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<BestPracticeLog> logList = new ArrayList<>();
        try {
            con = getConnection();
            String sql = "SELECT * from DP_DME_BEST_PRACTICE_LOG where 1=1 ";
            if (!StringUtils.isEmpty(hostsetting)) {
                sql = sql + " and HOST_SETTING='" + hostsetting + "' ";
            }

            if (!StringUtils.isEmpty(objId)) {
                sql = sql + " and OBJECT_ID='" + objId + "' ";
            }

            sql = sql + " order by REPAIR_TIME DESC ";

            if (pageNo > 0 && pageSize > 0) {
                int offset = (pageNo - 1) * pageSize;
                sql = sql + " OFFSET " + offset + " ROWS FETCH FIRST " + pageSize + " ROWS ONLY";
            }
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                BestPracticeLog log = new BestPracticeLog();
                log.setId(rs.getString("ID"));
                log.setObjectId(rs.getString("OBJECT_ID"));
                log.setObjectName(rs.getString("OBJECT_NAME"));
                log.setHostsetting(rs.getString("HOST_SETTING"));
                log.setRecommandValue(rs.getString("RECOMMEND_VALUE"));
                log.setViolationValue(rs.getString("VIOLATION_VALUE"));
                log.setRepairType(rs.getString("REPAIR_TYPE"));
                log.setRepairResult(rs.getBoolean("REPAIR_RESULT"));
                log.setRepairTime(rs.getTimestamp("REPAIR_TIME"));
                log.setMessage(clobToString(rs.getClob("MESSAGE")));
                logList.add(log);
            }
        } catch (DataBaseException | SQLException | IOException e) {
            LOGGER.error("getAllHostIds Failed! {}", e.getMessage());
            throw new DmeSqlException(e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return logList;
    }
}
