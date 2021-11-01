package com.huawei.dmestore.services;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.DmeSqlException;
import com.huawei.dmestore.exception.VcenterException;
import com.huawei.dmestore.model.BestPracticeBean;
import com.huawei.dmestore.model.BestPracticeCheckRecordBean;
import com.huawei.dmestore.model.BestPracticeLog;
import com.huawei.dmestore.model.BestPracticeRecommand;
import com.huawei.dmestore.model.BestPracticeRecommandUpReq;
import com.huawei.dmestore.model.BestPracticeUpResultResponse;
import com.huawei.dmestore.model.UpHostVnicRequestBean;


import java.util.List;
import java.util.Map;

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

    void checkVmfs(List<String> objectIds) throws VcenterException;

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
     * @param byTask true:通过任务发起 false:手动点击
     * @return List
     * @throws DmeSqlException 异常
     * @author wangxy
     */
    List<BestPracticeUpResultResponse> update(List<String> objectIds, String hostSetting, boolean byTask) throws DmeSqlException;

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

    /**
     * 获取最佳实践推荐值
     *
     * @param hostsetting 检查项标签
     * @return BestPracticeRecommand
     * @throws DmeSqlException 异常
     * @author wangxy
     */
    BestPracticeRecommand getRecommand(String hostsetting) throws DmeSqlException;


    /**
     * 更新最佳实践推荐值
     *
     * @param filedName 查询字段名称
     * @param filedValue 查询字段值
     * @param req 推荐值、动作map
     * @return int 影响条数
     * @throws DmeSqlException 异常
     * @author wangxy
     */
    int recommandUp(String filedName, String filedValue, BestPracticeRecommandUpReq req) throws DmeSqlException;

    /**
     * 最佳实践实施日志保存
     *
     * @param logs List<BestPracticeLog> 日志集合
     * @author wangxy
     */
    void saveLog(List<BestPracticeLog> logs);

    /**
     * 最佳实践实施日志查询
     *
     * @param hostsetting 检查项
     * @param objectId 对象ID
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return List<BestPracticeLog> 日志集合
     * @throws DmeSqlException 异常
     * @author wangxy
     */
    List<BestPracticeLog> getLog(String hostsetting, String objectId,int pageNo, int pageSize) throws DmeSqlException;

}
