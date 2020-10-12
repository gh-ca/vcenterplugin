package com.dmeplugin.dmestore.services;



import com.dmeplugin.dmestore.entity.VCenterInfo;

import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.sql.SQLException;

import java.util.Map;

public interface VCenterInfoService {

    int addVCenterInfo(VCenterInfo vCenterInfo) throws SQLException;

    int saveVCenterInfo(VCenterInfo vCenterInfo, HttpSession session) throws SQLException;



    Map<String, Object> findVCenterInfo() throws SQLException;

    VCenterInfo getVCenterInfo() throws SQLException;



}
