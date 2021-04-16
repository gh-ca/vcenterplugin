package com.huawei.dmestore.services;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.DmeSqlException;
import com.huawei.dmestore.exception.VcenterException;
import com.huawei.dmestore.model.BestPracticeBean;
import com.huawei.dmestore.model.BestPracticeCheckRecordBean;
import com.huawei.dmestore.model.BestPracticeUpResultResponse;
import com.huawei.dmestore.model.UpHostVnicRequestBean;


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

    void checkByCluster(String clusterObjectId) throws VcenterException;

    /**
     * 最佳实践实施
     *
     * @param objectIds 主机对象ID
     * @return List
     * @throws DmeSqlException 异常
     * @author wangxy
     */
    List<BestPracticeUpResultResponse> update(List<String> objectIds) throws DmeSqlException;

    /**
     * 最佳实践实施
     *
     * @param objectIds   主机对象ID
     * @param hostSetting 检查对象类型
     * @return List
     * @throws DmeSqlException 异常
     * @author wangxy
     */
    List<BestPracticeUpResultResponse> update(List<String> objectIds, String hostSetting) throws DmeSqlException;

    /**
     * 根据集群ID实施最佳实践
     *
     * @param clusterObjectId 集群对象ID
     * @return List
     * @throws DmeException 异常
     * @author wangxy
     */
    List<BestPracticeUpResultResponse> updateByCluster(String clusterObjectId) throws DmeException;


    /**
     * 获取主机VMkernel适配器设备信息
     *
     * @param hostObjIds 主机对象集合
     * @return String
     * @author wangxy
     */
    String getVirtualNicList(List<String> hostObjIds);

    /**
     * 更新主机VMkernel适配器设备信息
     *
     * @param beans 主机对象集合
     * @author wangxy
     */
    void upVirtualNicList(List<UpHostVnicRequestBean> beans);
}
