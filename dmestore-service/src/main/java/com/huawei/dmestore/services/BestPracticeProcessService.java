package com.huawei.dmestore.services;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.DmeSqlException;
import com.huawei.dmestore.exception.VcenterException;
import com.huawei.dmestore.model.BestPracticeBean;
import com.huawei.dmestore.model.BestPracticeCheckRecordBean;
import com.huawei.dmestore.model.BestPracticeUpResultResponse;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * BestPracticeProcessService
 *
 * @author wangxiangyong
 * @since 2020-11-30
 **/
public interface BestPracticeProcessService {
    List<BestPracticeCheckRecordBean> getCheckRecord(String type, String objectId) throws DmeException;

    List<BestPracticeBean> getCheckRecordBy(String hostSetting, int pageNo, int pageSize) throws DmeException;

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

    /**
     * 根据集群ID实施最佳实践
     *
     * @author wangxy
     * @param clusterObjectId 集群对象ID
     * @return List
     * @throws DmeException 异常
     */
    List<BestPracticeUpResultResponse> updateByCluster(String clusterObjectId) throws DmeException;
}
