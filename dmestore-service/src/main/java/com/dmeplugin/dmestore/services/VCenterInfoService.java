package com.dmeplugin.dmestore.services;



import com.dmeplugin.dmestore.entity.VCenterInfo;
import com.dmeplugin.dmestore.exception.DmeSqlException;

import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.sql.SQLException;

import java.util.Map;

public interface VCenterInfoService {

    int addVcenterInfo(VCenterInfo vCenterInfo) throws DmeSqlException;

    int saveVcenterInfo(VCenterInfo vCenterInfo) throws DmeSqlException;



    Map<String, Object> findVcenterInfo() throws DmeSqlException;

    VCenterInfo getVcenterInfo() throws DmeSqlException;



}
