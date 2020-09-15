package com.dmeplugin.dmestore.dao;


import com.dmeplugin.dmestore.constant.DPSqlFileConstant;
import com.dmeplugin.dmestore.entity.DmeVmwareRelation;
import com.dmeplugin.dmestore.exception.DataBaseException;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * @ClassName: DmeVmwareRalationDao
 * @Company: GH-CA
 * @author: yy
 * @create: 2020-09-02
 **/
public class DmeVmwareRalationDao extends H2DataBaseDao {

    public List<DmeVmwareRelation> getDmeVmwareRelation(String storeType) throws SQLException {

        List<DmeVmwareRelation> lists = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT * FROM " + DPSqlFileConstant.DP_DME_VMWARE_RELATION
                    + " WHERE state=1 ";
            if (!StringUtils.isEmpty(storeType)) {
                sql = sql + " and STORE_TYPE='" + storeType + "'";
            }

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                DmeVmwareRelation dvr = new DmeVmwareRelation();
                dvr.setId(rs.getInt("ID"));
                dvr.setStoreId(rs.getString("STORE_ID"));
                dvr.setStoreName(rs.getString("STORE_NAME"));
                dvr.setVolumeId(rs.getString("VOLUME_ID"));
                dvr.setVolumeName(rs.getString("VOLUME_NAME"));
                dvr.setVolumeWwn(rs.getString("VOLUME_WWN"));
                dvr.setVolumeShare(rs.getString("VOLUME_SHARE"));
                dvr.setVolumeFs(rs.getString("VOLUME_FS"));
                dvr.setStoreType(rs.getString("STORE_TYPE"));
                dvr.setCreateTime(rs.getTimestamp("CREATETIME"));
                dvr.setUpdateTime(rs.getTimestamp("UPDATETIME"));
                dvr.setState(rs.getInt("STATE"));
                dvr.setShareId(rs.getString("SHARE_ID"));
                dvr.setFsId(rs.getString("FS_ID"));
                dvr.setFsName(rs.getString("FS_NAME"));
                dvr.setLogicPortId(rs.getString("LOGICPORT_ID"));
                dvr.setLogicPortName(rs.getString("LOGICPORT_NAME"));
                lists.add(dvr);
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("Failed to get dme access info: " + e.getMessage());
            throw new SQLException(e);
        } finally {
            closeConnection(con, ps, rs);
        }
        return lists;
    }


    public List<String> getAllWwnByType(String storeType) throws SQLException {
        List<String> lists = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT VOLUME_WWN FROM " + DPSqlFileConstant.DP_DME_VMWARE_RELATION
                    + " WHERE state=1 ";
            if (!StringUtils.isEmpty(storeType)) {
                sql = sql + " and STORE_TYPE='" + storeType + "'";
            }

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                lists.add(rs.getString("VOLUME_WWN"));
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("Failed to get dme access info: " + e.getMessage());
            throw new SQLException(e);
        } finally {
            closeConnection(con, ps, rs);
        }
        return lists;
    }

    public List<String> getAllStorageIdByType(String storeType) throws SQLException {
        List<String> lists = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT STORE_ID FROM " + DPSqlFileConstant.DP_DME_VMWARE_RELATION
                    + " WHERE state=1 ";
            if (!StringUtils.isEmpty(storeType)) {
                sql = sql + " and STORE_TYPE='" + storeType + "'";
            }

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                lists.add(rs.getString("STORE_ID"));
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("Failed to get dme store info: " + e.getMessage());
            throw new SQLException(e);
        } finally {
            closeConnection(con, ps, rs);
        }
        return lists;
    }

    public void update(List<DmeVmwareRelation> list) {

    }

    public void save(List<DmeVmwareRelation> list) {
        if (null == list || list.size() == 0) {
            return;
        }

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = getConnection();
            String sql = "insert into DP_DME_VMWARE_RELATION(STORE_ID,STORE_NAME,VOLUME_ID,VOLUME_NAME,VOLUME_WWN,VOLUME_SHARE,VOLUME_FS,STORE_TYPE,SHARE_ID,SHARE_NAME,FS_ID,FS_NAME,LOGICPORT_ID,LOGIC_PORT_NAME) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstm = con.prepareStatement(sql);
            //不自动提交
            con.setAutoCommit(false);
            for (DmeVmwareRelation relation : list) {
                pstm.setString(1, relation.getStoreId());
                pstm.setString(2, relation.getStoreName());
                pstm.setString(3, relation.getVolumeId());
                pstm.setString(4, relation.getVolumeName());
                pstm.setString(5, relation.getVolumeWwn());
                pstm.setString(6, relation.getVolumeShare());
                pstm.setString(7, relation.getVolumeFs());
                pstm.setString(8, relation.getStoreType());
                pstm.setString(9, relation.getShareId());
                pstm.setString(10, relation.getShareName());
                pstm.setString(11, relation.getFsId());
                pstm.setString(12, relation.getFsName());
                pstm.setString(13, relation.getLogicPortId());
                pstm.setString(14, relation.getLogicPortName());
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

    public void deleteByWwn(List<String> list) {
        if (null == list || list.size() == 0) {
            return;
        }

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = getConnection();
            String sql = "delete from DP_DME_VMWARE_RELATION where VOLUME_WWN=?";
            pstm = con.prepareStatement(sql);
            con.setAutoCommit(false);
            for (String wwn : list) {
                pstm.setString(1, wwn);

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

    public void deleteByStorageId(List<String> list) {
        if (null == list || list.size() == 0) {
            return;
        }
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = getConnection();
            String sql = "delete from DP_DME_VMWARE_RELATION where STORE_ID=?";
            pstm = con.prepareStatement(sql);
            con.setAutoCommit(false);
            for (String wwn : list) {
                pstm.setString(1, wwn);
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
