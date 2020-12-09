package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.exception.DmeSqlException;
import com.dmeplugin.dmestore.exception.VcenterException;
import com.dmeplugin.dmestore.model.BestPracticeBean;
import com.dmeplugin.dmestore.model.BestPracticeCheckRecordBean;
import com.dmeplugin.dmestore.model.BestPracticeUpResultResponse;

import java.util.List;

/**
 * BestPracticeProcessService
 *
 * @author wangxiangyong
 * @since 2020-11-30
 **/
public interface BestPracticeProcessService {
    List<BestPracticeCheckRecordBean> getCheckRecord() throws DMEException;

    List<BestPracticeBean> getCheckRecordBy(String hostSetting, int pageNo, int pageSize) throws DMEException;

    void check(String objectId) throws VcenterException;

    /**
     * 最佳实践实施
     *
     * @author wangxy
     * @param objectIds 主机对象ID
     * @return List
     * @throws DmeSqlException 异常
     */
    List<BestPracticeUpResultResponse> update(List<String> objectIds) throws DmeSqlException;

    /**
     * 最佳实践实施
     *
     * @author wangxy
     * @param objectIds 主机对象ID
     * @param hostSetting 检查对象类型
     * @return List
     * @throws DmeSqlException 异常
     */
    List<BestPracticeUpResultResponse> update(List<String> objectIds, String hostSetting) throws DmeSqlException;
}
