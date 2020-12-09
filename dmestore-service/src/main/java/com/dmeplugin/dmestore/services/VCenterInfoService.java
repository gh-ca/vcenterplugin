package com.dmeplugin.dmestore.services;



import com.dmeplugin.dmestore.entity.VCenterInfo;
import com.dmeplugin.dmestore.exception.DmeSqlException;

import java.util.Map;

public interface VCenterInfoService {

    int addVcenterInfo(VCenterInfo vCenterInfo) throws DmeSqlException;

    int saveVcenterInfo(VCenterInfo vCenterInfo) throws DmeSqlException;



    Map<String, Object> findVcenterInfo() throws DmeSqlException;

    VCenterInfo getVcenterInfo() throws DmeSqlException;



}
