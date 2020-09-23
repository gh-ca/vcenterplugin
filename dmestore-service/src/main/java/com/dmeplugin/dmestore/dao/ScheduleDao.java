package com.dmeplugin.dmestore.dao;

import com.dmeplugin.dmestore.constant.DPSqlFileConstant;
import com.dmeplugin.dmestore.constant.SqlFileConstant;
import com.dmeplugin.dmestore.entity.DME;
import com.dmeplugin.dmestore.entity.ScheduleConfig;
import com.dmeplugin.dmestore.task.ScheduleSetting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDao extends H2DataBaseDao {
    public List<ScheduleConfig> getScheduleList()  {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ScheduleConfig> scheduleconfiglist=new ArrayList<>();
        try {
            con = getConnection();
            ps = con.prepareStatement("SELECT * FROM " + DPSqlFileConstant.DP_DME_TASK_INFO);
            rs = ps.executeQuery();
             scheduleconfiglist = new ArrayList<>();
            while (rs.next()) {
                scheduleconfiglist.add(buildSchedule(rs));
            }

        } catch (SQLException e) {
            LOGGER.error("Failed to get dmes: " + e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return scheduleconfiglist;
    }

    private ScheduleConfig buildSchedule(ResultSet rs) throws SQLException {
        ScheduleConfig scheduleConfig = new ScheduleConfig();
        scheduleConfig.setClassName(rs.getString("CLASS_NAME"));
        scheduleConfig.setCron(rs.getString("CRON"));
        scheduleConfig.setJobName(rs.getString("JOB_NAME"));
        scheduleConfig.setMethod(rs.getString("METHOD"));
        scheduleConfig.setId(rs.getInt("ID"));
        return scheduleConfig;
    }
}
