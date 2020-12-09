package com.dmeplugin.dmestore.dao;

import com.dmeplugin.dmestore.constant.DPSqlFileConstant;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class SystemDao extends H2DataBaseDao {

    public boolean checkTable(String sqlFile) throws SQLException {
        Connection con = null;
        ResultSet resultSet = null;
        boolean tableExist;
        try {
            con = getConnection();
            resultSet = con.getMetaData().getTables(null, null, sqlFile, null);
            tableExist = resultSet.next();
        } catch (SQLException e) {
            LOGGER.error("Failed to check table: " + e.getMessage());
            throw e;
        } finally {
            closeConnection(con, null, resultSet);
        }

        return tableExist;
    }

    /**
     * 判断表是否存在，不存在则创建表
     *
     * @param tableName 表名
     * @param createTableSql 创建表的SQL
     */
    public void checkExistAndCreateTable(String tableName, String createTableSql) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            rs = con.getMetaData().getTables(null, null, tableName, null);
            if (!rs.next()) {
                ps = con.prepareStatement(createTableSql);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to check exist and create table: " + e.getMessage());
            throw e;
        } finally {
            closeConnection(con, ps, rs);
        }
    }

    public void initData(String datasql) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(datasql);
            ps.executeUpdate();
        } catch (Exception e) {
            LOGGER.error("Failed to initData: " + e.getMessage());
            throw e;
        } finally {
            closeConnection(con, ps, rs);
        }
    }

    public boolean isColumnExists(String tableName, String columnName) throws SQLException {
        Connection con = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT * FROM " + tableName + " LIMIT 1";
            ps1 = con.prepareStatement(sql);
            rs = ps1.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                if (resultSetMetaData.getColumnName(i + 1).equalsIgnoreCase(columnName)) {
                    return true;
                }
            }
            return false;
        } finally {
            closeConnection(con, rs, ps1);
        }
    }

    public void cleanAllData() {
        Connection con = null;
        PreparedStatement ps1 = null;
        try {
            con = getConnection();
            for (String table : DPSqlFileConstant.ALL_TABLES) {
                try {
                    ps1 = con.prepareStatement("DELETE FROM " + table);
                    ps1.execute();
                    ps1.close();
                    ps1 = null;
                } catch (SQLException e) {
                    LOGGER.error("Cannot delete data from " + table);
                }
            }
        } finally {
            closeConnection(con, ps1, null);
        }
    }

}
