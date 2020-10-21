package com.dmeplugin.dmestore.services;



import com.dmeplugin.dmestore.entity.VCenterInfo;
import com.dmeplugin.dmestore.exception.DmeSqlException;

import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.sql.SQLException;

import java.util.Map;

public interface VCenterInfoService {

    int addVCenterInfo(VCenterInfo vCenterInfo) throws DmeSqlException;

    int saveVCenterInfo(VCenterInfo vCenterInfo) throws DmeSqlException;



    Map<String, Object> findVCenterInfo() throws DmeSqlException;

    VCenterInfo getVCenterInfo() throws DmeSqlException;



}
