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


}
