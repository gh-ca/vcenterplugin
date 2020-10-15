package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.BestPracticeBean;
import com.dmeplugin.dmestore.model.BestPracticeCheckRecordBean;
import com.dmeplugin.dmestore.model.BestPracticeUpResultResponse;

import java.util.List;

/**
 * @author wangxiangyong
 */
public interface BestPracticeProcessService {
    List<BestPracticeCheckRecordBean> getCheckRecord() throws Exception;

    List<BestPracticeBean> getCheckRecordBy(String hostSetting, int pageNo, int pageSize) throws Exception;

    void check(String objectId) throws Exception;

    /**
     * @Author Administrator
     * @Description 更新主机的所有检查项
     * @Date 11:06 2020/9/23
     * @Param [objectIds, hostSetting]
     * @Return void
     **/
    List<BestPracticeUpResultResponse> update(List<String> objectIds) throws Exception;

    List<BestPracticeUpResultResponse> updateByCluster(String clusterobjectid) throws Exception;

    /**
     * @Author Administrator
     * @Description 更新主机的某一检查项
     * @Date 11:06 2020/9/23
     * @Param [objectIds, hostSetting]
     * @Return void
     **/
    List<BestPracticeUpResultResponse> update(List<String> objectIds, String hostSetting) throws Exception;
}
