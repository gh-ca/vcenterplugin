package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.entity.VCenterInfo;
import com.dmeplugin.dmestore.exception.DmeSqlException;

import java.util.Map;

/**
 * VCenterInfoService
 *
 * @author lianqiang
 * @since 2020-09-15
 **/
public interface VCenterInfoService {
    int addVcenterInfo(VCenterInfo info) throws DmeSqlException;

    int saveVcenterInfo(VCenterInfo info) throws DmeSqlException;

    Map<String, Object> findVcenterInfo() throws DmeSqlException;

    VCenterInfo getVcenterInfo() throws DmeSqlException;
}
