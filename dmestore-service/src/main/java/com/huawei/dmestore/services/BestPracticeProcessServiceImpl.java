package com.huawei.dmestore.services;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.dao.BestPracticeCheckDao;
import com.huawei.dmestore.dao.DmeVmwareRalationDao;
import com.huawei.dmestore.entity.DmeVmwareRelation;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.DmeSqlException;
import com.huawei.dmestore.exception.VcenterException;
import com.huawei.dmestore.model.*;
import com.huawei.dmestore.services.bestpractice.BestPracticeService;
import com.huawei.dmestore.services.bestpractice.JumboFrameMTUImpl;
import com.huawei.dmestore.services.bestpractice.VmfsDatastoreSpaceUtilizationImpl;
import com.huawei.dmestore.utils.StringUtil;
import com.huawei.dmestore.utils.VCSDKUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * BestPracticeProcessServiceImpl
 *
 * @author wangxiangyong
 * @since 2020-11-30
 **/
public class BestPracticeProcessServiceImpl implements BestPracticeProcessService {
    private static final Logger log = LoggerFactory.getLogger(BestPracticeProcessServiceImpl.class);

    private static final int PAGE_SIZE = 100;

    private Gson gson = new Gson();

    private VCSDKUtils vcsdkUtils;

    private BestPracticeCheckDao bestPracticeCheckDao;

    private List<BestPracticeService> bestPracticeServices;

    private VmfsDatastoreSpaceUtilizationImpl vmfsDatastoreSpaceUtilization;

    private DmeVmwareRalationDao dmeVmwareRalationDao;

    public List<BestPracticeService> getBestPracticeServices() {
        return bestPracticeServices;
    }

    public void setBestPracticeServices(List<BestPracticeService> bestPracticeServices) {
        this.bestPracticeServices = bestPracticeServices;
    }

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
    }

    public BestPracticeCheckDao getBestPracticeCheckDao() {
        return bestPracticeCheckDao;
    }

    public void setBestPracticeCheckDao(BestPracticeCheckDao bestPracticeCheckDao) {
        this.bestPracticeCheckDao = bestPracticeCheckDao;
    }

    public void setVmfsDatastoreSpaceUtilization(VmfsDatastoreSpaceUtilizationImpl vmfsDatastoreSpaceUtilization) {
        this.vmfsDatastoreSpaceUtilization = vmfsDatastoreSpaceUtilization;
    }

    public void setDmeVmwareRalationDao(DmeVmwareRalationDao dmeVmwareRalationDao) {
        this.dmeVmwareRalationDao = dmeVmwareRalationDao;
    }

    @Override
    public List<BestPracticeCheckRecordBean> getCheckRecord(String type, String objectId) throws DmeException {
        List<String> objectIds = null;

        // 先检查，后获取值
        if (StringUtil.isNotBlank(type)) {
            objectIds = new ArrayList<>();
            if (type.equals("host")) {
                objectIds.add(objectId);
            } else if (type.equals("cluster")) {
                // 查询集群下的所有主机信息
                String hostsOnCluster = vcsdkUtils.getHostsOnCluster(objectId);
                if (StringUtil.isNotBlank(hostsOnCluster)) {
                    List<Map<String, String>> hostList = gson.fromJson(hostsOnCluster,
                        new TypeToken<List<Map<String, String>>>() { }.getType());
                    for (int index = 0; index < hostList.size(); index++) {
                        String tempHostObjectId = hostList.get(index).get("hostId");
                        objectIds.add(tempHostObjectId);
                    }
                }
            }
        }
        List<BestPracticeCheckRecordBean> list = new ArrayList<>();
        for (BestPracticeService bestPracticeService : bestPracticeServices) {
            BestPracticeCheckRecordBean bean = new BestPracticeCheckRecordBean();
            bean.setHostSetting(bestPracticeService.getHostSetting());
            bean.setLevel(bestPracticeService.getLevel());
            bean.setRecommendValue(String.valueOf(bestPracticeService.getRecommendValue()));
            bean.setRepairAction(bestPracticeService.repairAction());
            try {
                if (objectIds != null && objectIds.size() > 0) {
                    List<BestPracticeBean> tempList = new ArrayList<>();
                    List<BestPracticeBean> hostBeanTemp = bestPracticeCheckDao.getRecordBeanByHostSetting(
                        bestPracticeService.getHostSetting(), objectIds);
                    tempList.addAll(hostBeanTemp);
                    bean.setHostList(tempList);
                    bean.setCount(tempList.size());
                } else if(objectIds != null && objectIds.size() == 0){
                    //集群下面没有主机
                    bean.setHostList(new ArrayList<>());
                    bean.setCount(0);
                } else {
                    List<BestPracticeBean> hostBean = bestPracticeCheckDao.getRecordBeanByHostSetting(
                        bestPracticeService.getHostSetting(), (String)null);
                    bean.setHostList(hostBean);
                    bean.setCount(hostBean.size());
                }
            } catch (SQLException ex) {
                continue;
            }
            list.add(bean);
        }

        // 添加vmfs记录信息
        list.add(getVmfsRecord(objectIds));

        return list;
    }

    @Override
    public List<BestPracticeBean> getCheckRecordBy(String hostSetting, int pageNo, int pageSize) throws DmeException {
        List<BestPracticeBean> list = new ArrayList<>();
        String vmfsHostSetting = vmfsDatastoreSpaceUtilization.getHostSetting();

        // vmfs单独处理
        if (hostSetting.equals(vmfsHostSetting)) {
            try {
                list = bestPracticeCheckDao.getRecordByPage(vmfsHostSetting, pageNo, pageSize);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return list;
        }

        for (BestPracticeService bestPracticeService : bestPracticeServices) {
            String setting = bestPracticeService.getHostSetting();
            if (hostSetting.equals(setting)) {
                try {
                    list = bestPracticeCheckDao.getRecordByPage(hostSetting, pageNo, pageSize);
                } catch (SQLException ex) {
                    throw new DmeException(ex.getMessage());
                }
                break;
            }
        }

        return list;
    }

    @Override
    public void check(String objectId) throws VcenterException {
        log.info("====best practice check start====");
        String hostsStr;
        if (objectId != null) {
            hostsStr = vcsdkUtils.findHostById(objectId);
        } else {
            hostsStr = vcsdkUtils.getAllHosts();
        }
        Map<String, List<BestPracticeBean>> checkMap = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        JsonArray hostArray = gson.fromJson(hostsStr, JsonArray.class);
        List<String> unConnectedIds = new ArrayList<>();
        if (null != hostArray) {
            for (int index = 0; index < hostArray.size(); index++) {
                // 对每一项进行检查
                JsonObject hostObject = hostArray.get(index).getAsJsonObject();
                String hostName = hostObject.get("hostName").getAsString();
                String hostObjectId = hostObject.get("objectId").getAsString();

                // 连接断开的主机跳过
                if (!vcsdkUtils.isHostConnected(hostObjectId)) {
                    unConnectedIds.add(hostObjectId);
                    continue;
                }
                for (BestPracticeService bestPracticeService : bestPracticeServices) {
                    try {
                        String hostSetting = bestPracticeService.getHostSetting();
                        if (!checkMap.containsKey(hostSetting)) {
                            checkMap.put(hostSetting, new ArrayList<>());
                        }
                        boolean isCheck = bestPracticeService.check(vcsdkUtils, hostObjectId);
                        if (!isCheck) {
                            BestPracticeBean bean = new BestPracticeBean();
                            bean.setHostSetting(hostSetting);
                            bean.setRecommendValue(
                                String.valueOf(bestPracticeService.getRecommendValue(vcsdkUtils, hostObjectId)));
                            bean.setLevel(bestPracticeService.getLevel());
                            bean.setNeedReboot(String.valueOf(bestPracticeService.needReboot()));
                            Object currentValue = bestPracticeService.getCurrentValue(vcsdkUtils, hostObjectId);
                            String actualValue = String.valueOf(currentValue);
                            bean.setActualValue(actualValue);
                            bean.setHostObjectId(hostObjectId);
                            bean.setHostName(hostName);
                            bean.setAutoRepair(String.valueOf(bestPracticeService.autoRepair()));
                            checkMap.get(hostSetting).add(bean);
                        }
                    } catch (Exception ex) {
                        // 报错，跳过当前项检查
                        log.error("{} check failed! The host may not support this operation!hostSetting={}", hostName,
                            bestPracticeService.getHostSetting(), ex);
                    }
                }
            }
        }

        // VMFS最佳实践检查
        Map<String, List<BestPracticeBean>> vmfsMap = checkVmfs(unConnectedIds, null);
        checkMap.putAll(vmfsMap);

        // 将无法连通的主机或者存储从最佳实践表中移除
        bestPracticeCheckDao.deleteByHostIds(unConnectedIds);

        // 保存到数据库
        bachDbProcess(checkMap);
        log.info("====best practice check end====");
    }

    @Override
    public void checkVmfs(List<String> objectIds) throws VcenterException {
        log.info("====VMFS best practice check begin====");
        List<String> unConnectedIds = new ArrayList<>();
        Map<String, List<BestPracticeBean>> vmfsMap = checkVmfs(unConnectedIds, objectIds);
        // 将无法连通的主机或者存储从最佳实践表中移除
        bestPracticeCheckDao.deleteByHostIds(unConnectedIds);

        // 保存到数据库
        bachDbProcess(vmfsMap);
        log.info("====VMFS best practice check end====");
    }

    @Override
    public void checkByCluster(String clusterObjectId) throws VcenterException {
        // 查询集群下的所有主机信息
        String hostsOnCluster = vcsdkUtils.getHostsOnCluster(clusterObjectId);
        if (StringUtil.isNotBlank(hostsOnCluster)) {
            List<Map<String, String>> hostList = gson.fromJson(hostsOnCluster,
                new TypeToken<List<Map<String, String>>>() { }.getType());
            for (int index = 0; index < hostList.size(); index++) {
                String tempHostObjectId = hostList.get(index).get("hostId");
                check(tempHostObjectId);
            }
        }
    }

    private void bachDbProcess(Map<String, List<BestPracticeBean>> map) {
        if (map == null || map.size() == 0) {
            return;
        }
        map.forEach((hostSetting, bestPracticeBeans) -> {
            // 本地全量查询
            List<String> localHostNames = new ArrayList<>();
            try {
                localHostNames = bestPracticeCheckDao.getHostNameByHostsetting(hostSetting);
            } catch (SQLException e) {
                log.error(e.getMessage());
            }

            List<BestPracticeBean> newList = new ArrayList<>();
            List<BestPracticeBean> upList = new ArrayList<>();
            if (bestPracticeBeans != null && bestPracticeBeans.size() > 0) {
                for (BestPracticeBean bestPracticeBean : bestPracticeBeans) {
                    String hostName = bestPracticeBean.getHostName();
                    if (localHostNames.contains(hostName)) {
                        upList.add(bestPracticeBean);
                        localHostNames.remove(hostName);
                    } else {
                        newList.add(bestPracticeBean);
                    }
                }
            }

            // 更新
            if (!upList.isEmpty()) {
                bestPracticeCheckDao.update(upList);
            }

            // 新增
            if (!newList.isEmpty()) {
                bestPracticeCheckDao.save(newList);
            }

            // 删除
            if (!localHostNames.isEmpty()) {
                bestPracticeCheckDao.deleteByHostNameAndHostsetting(localHostNames, hostSetting);
            }
        });
    }

    @Override
    public String getVirtualNicList(List<String> hostObjIds) {
        return vcsdkUtils.getVirtualNicList(hostObjIds, JumboFrameMTUImpl.RECOMMEND_VALUE);
    }

    @Override
    public void upVirtualNicList(List<UpHostVnicRequestBean> beans) {
        vcsdkUtils.updateVirtualNicList(beans, JumboFrameMTUImpl.RECOMMEND_VALUE);
    }

    @Override
    public List<BestPracticeUpResultResponse> updateByCluster(String clusterObjectId) throws DmeException {
        // 查询集群下的所有主机信息
        List<BestPracticeUpResultResponse> bestPracticeUpResultResponses = new ArrayList<>();
        String hostsOnCluster = vcsdkUtils.getHostsOnCluster(clusterObjectId);
        if (StringUtil.isNotBlank(hostsOnCluster)) {
            List<Map<String, String>> hostList = gson.fromJson(hostsOnCluster,
                new TypeToken<List<Map<String, String>>>() { }.getType());
            List<String> objectIds = new ArrayList<>();
            for (int index = 0; index < hostList.size(); index++) {
                objectIds.add(hostList.get(index).get("hostId"));
            }
            return update(objectIds);
        }
        return bestPracticeUpResultResponses;
    }

    @Override
    public List<BestPracticeUpResultResponse> update(List<String> objectIds) throws DmeSqlException {
        return update(objectIds, null, false);
    }

    @Override
    public List<BestPracticeUpResultResponse> update(List<String> objectIds, String hostSetting, boolean byTask)
        throws DmeSqlException {
        // 获取对应的service
        List<BestPracticeService> services = new ArrayList<>();
        if (hostSetting == null) {
            services.add(vmfsDatastoreSpaceUtilization);
            services.addAll(bestPracticeServices);
        } else {
            if (hostSetting.equals(vmfsDatastoreSpaceUtilization.getHostSetting())) {
                services.add(vmfsDatastoreSpaceUtilization);
            } else {
                for (BestPracticeService bestPracticeService : bestPracticeServices) {
                    if (bestPracticeService.getHostSetting().equals(hostSetting)) {
                        services.add(bestPracticeService);
                        break;
                    }
                }
            }
        }

        // 从本地数据库查询需要实施最佳实践的主机信息
        Map<String, Map<String, String>> hostMap = getHostMap(objectIds, hostSetting);
        List<BestPracticeUpResultResponse> responses = new ArrayList<>();

        List<BestPracticeLog> logList = new ArrayList<>();
        List<String> disconnectedIds = new ArrayList<>();
        for (Map.Entry<String, Map<String, String>> entry : hostMap.entrySet()) {
            String objectId = entry.getKey();
            String objName = entry.getValue().get(BestPracticeCheckDao.HOST_NAME);
            boolean isVmfs = objectId.contains("Datastore");
            if (isVmfs && !vcsdkUtils.isAccessibleOfDatastore(objectId)) {
                disconnectedIds.add(objectId);
                continue;
            }

            // 连接断开的主机跳过
            if (!isVmfs && !vcsdkUtils.isHostConnected(objectId)) {
                disconnectedIds.add(objectId);
                continue;
            }
            BestPracticeUpResultResponse response = new BestPracticeUpResultResponse();
            List<BestPracticeUpResultBase> baseList = new ArrayList();
            response.setHostObjectId(objectId);
            response.setHostName(objName);
            boolean isNeedReboot = false;
            for (BestPracticeService service : services) {
                // 能自动修复的才进行实施操作
                if (!service.autoRepair()) {
                    continue;
                }

                //如果是通过任务发起的更新，修复动作为手动的检查项不执行最佳实践操作
                if (byTask && service.repairAction().equals("0")) {
                    continue;
                }

                // 如果是vmfs自动扩容最佳实践实施，只调用vmfs的更新即可
                if (isVmfs && !(service instanceof VmfsDatastoreSpaceUtilizationImpl)) {
                    continue;
                }

                BestPracticeUpResultBase base = new BestPracticeUpResultBase();
                base.setHostObjectId(objectId);
                base.setHostSetting(service.getHostSetting());
                base.setNeedReboot(service.needReboot());

                String violationValue = null;
                boolean checkFlag = false;
                boolean repairResult = false;
                String repairMessage = "success";
                try {
                    checkFlag = service.check(vcsdkUtils, objectId);
                    log.info("check! before best practice update! checkFlag={}, objectId={}, hostSetting={}", checkFlag,
                        objectId, service.getHostSetting());
                    if (!checkFlag) {
                        // 违规值需要在更新前获取
                        violationValue = service.getCurrentValue(vcsdkUtils, objectId) + "";

                        service.update(vcsdkUtils, objectId);
                        // 更新成功后，只要有一项是需要重启的则该主机需要重启后生效
                        if (service.needReboot()) {
                            isNeedReboot = true;
                        }

                        repairResult = true;
                    }
                    base.setUpdateResult(true);
                } catch (Exception ex) {
                    base.setUpdateResult(false);
                    repairMessage = ex.getMessage();
                    base.setMessage(repairMessage);
                    repairResult = false;
                    log.error("best practice update {} {} recommend value failed!errMsg:{}", objectId, hostSetting, ex);
                }
                baseList.add(base);

                // 日志bean构建
                try {
                    if (!checkFlag) {
                        BestPracticeLog bestPracticeLog = new BestPracticeLog();
                        bestPracticeLog.setHostsetting(service.getHostSetting());
                        bestPracticeLog.setId("-1");
                        bestPracticeLog.setObjectId(objectId);
                        bestPracticeLog.setObjectName(objName);
                        bestPracticeLog.setRecommandValue(service.getRecommendValue(vcsdkUtils, objectId) + "");
                        bestPracticeLog.setViolationValue(violationValue);
                        bestPracticeLog.setRepairType(byTask ? "1" : "0");
                        bestPracticeLog.setRepairResult(repairResult);
                        bestPracticeLog.setMessage(repairMessage);
                        bestPracticeLog.setRepairTime(new Date(System.currentTimeMillis()));
                        logList.add(bestPracticeLog);
                    }
                } catch (Exception exception) {
                    log.error("build BestPracticeLog error!", exception);
                }
            }
            response.setResult(baseList);
            response.setNeedReboot(isNeedReboot);
            responses.add(response);
        }

        // 将成功修改了最佳实践值的记录从表中删除
        bestPracticeCheckDao.deleteBy(responses);
        bestPracticeCheckDao.deleteByHostIds(disconnectedIds);
        bestPracticeCheckDao.saveRepairLog(logList);

        return responses;
    }

    private Map<String, Map<String, String>> getHostMap(List<String> objectIds, String hostSetting) throws DmeSqlException {
        Map<String, Map<String, String>> hostMap = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        if (objectIds == null || objectIds.size() == 0) {
            // 获取本地所有
            int pageNo = 0;
            try {
                while (true) {
                    Map<String, Map<String, String>> hostMapTemp = bestPracticeCheckDao.getAllHostIds(pageNo++,
                        PAGE_SIZE, hostSetting);
                    hostMap.putAll(hostMapTemp);

                    if (hostMapTemp.size() != PAGE_SIZE) {
                        break;
                    }
                }
            } catch (SQLException e) {
                throw new DmeSqlException(e.getMessage());
            }
        } else {
            try {
                // 从本地数据库查询指定的主机信息
                hostMap = bestPracticeCheckDao.getByHostIds(objectIds, hostSetting);
            } catch (SQLException e) {
                throw new DmeSqlException(e.getMessage());
            }
        }
        return hostMap;
    }

    private Map<String, List<BestPracticeBean>> checkVmfs(List<String> unConnectedIds, List<String> objIds) {
        Map<String, List<BestPracticeBean>> vmfsMap = new HashMap();
        List<BestPracticeBean> list = new ArrayList<>();
        try {
            List<DmeVmwareRelation> dvrlistTemp = dmeVmwareRalationDao.getDmeVmwareRelation(DmeConstants.STORE_TYPE_VMFS);
            List<DmeVmwareRelation> dvrlist;
            if(objIds != null && objIds.size()> 0){
                dvrlist = dvrlistTemp.stream().filter(re ->objIds.contains(re.getStoreId())).collect(Collectors.toList());
            }else {
                dvrlist = dvrlistTemp;
            }
            dvrlist.forEach(dmeVmwareRelation -> {
                String hostSetting = vmfsDatastoreSpaceUtilization.getHostSetting();
                String objId = dmeVmwareRelation.getStoreId();
                if (!vcsdkUtils.isAccessibleOfDatastore(objId)) {
                    unConnectedIds.add(objId);
                } else {
                    try {
                        boolean isCheck = vmfsDatastoreSpaceUtilization.check(vcsdkUtils, objId);
                        if (!isCheck) {
                            BestPracticeBean bean = new BestPracticeBean();
                            bean.setHostSetting(hostSetting);
                            bean.setRecommendValue(
                                vmfsDatastoreSpaceUtilization.getRecommendValue(vcsdkUtils, objId) + "");
                            bean.setLevel(vmfsDatastoreSpaceUtilization.getLevel());
                            bean.setNeedReboot(String.valueOf(vmfsDatastoreSpaceUtilization.needReboot()));
                            Object currentValue = vmfsDatastoreSpaceUtilization.getCurrentValue(vcsdkUtils, objId);
                            String actualValue = String.valueOf(currentValue);
                            bean.setActualValue(actualValue);
                            bean.setHostObjectId(objId);
                            bean.setHostName(dmeVmwareRelation.getStoreName());
                            bean.setAutoRepair(String.valueOf(vmfsDatastoreSpaceUtilization.autoRepair()));
                            list.add(bean);
                        }
                    } catch (Exception ex) {
                        log.error("vmfs best practice check error! vmfs name={}", dmeVmwareRelation.getStoreName(), ex);
                    }
                }
            });
        } catch (DmeSqlException ex) {
            log.error("vmfs best practice check sql error!", ex);
        }

        vmfsMap.put(vmfsDatastoreSpaceUtilization.getHostSetting(), list);
        return vmfsMap;
    }

    private BestPracticeCheckRecordBean getVmfsRecord(List<String> hostObjIds) {
        BestPracticeCheckRecordBean bean = new BestPracticeCheckRecordBean();
        String hostSetting = vmfsDatastoreSpaceUtilization.getHostSetting();
        bean.setHostSetting(hostSetting);
        bean.setLevel(vmfsDatastoreSpaceUtilization.getLevel());
        bean.setRepairAction(vmfsDatastoreSpaceUtilization.repairAction());
        try {
            bean.setRecommendValue(vmfsDatastoreSpaceUtilization.getRecommendValue(vcsdkUtils, null) + "");
        } catch (Exception exception) {
            log.error("vmfs best practice check get recommend value error!", exception);
        }

        try {
            // 根据主机ID
            if (hostObjIds != null && hostObjIds.size() > 0) {
                List<String> dsIds = new ArrayList<>();
                for (String hostObjId : hostObjIds) {
                    dsIds.addAll(vcsdkUtils.getDatastoreByHostObjIdAndType(hostObjId, DmeConstants.STORE_TYPE_VMFS));
                }
                List<BestPracticeBean> temp = new ArrayList<>(16);
                List<BestPracticeBean> hostBeanTemp = bestPracticeCheckDao.getRecordBeanByHostSetting(hostSetting, dsIds);
                temp.addAll(hostBeanTemp);
                bean.setHostList(temp);
                bean.setCount(temp.size());
            } else if(hostObjIds != null && hostObjIds.size() == 0){
                bean.setHostList(new ArrayList<>());
                bean.setCount(0);
            } else {
                List<BestPracticeBean> hostBean = bestPracticeCheckDao.getRecordBeanByHostSetting(hostSetting, (String)null);
                bean.setHostList(hostBean);
                bean.setCount(hostBean.size());
            }
        } catch (SQLException throwables) {
            log.error("vmfs best practice get records error!", throwables);
        }

        return bean;
    }

    @Override
    public BestPracticeRecommand getRecommand(String hostsetting) throws DmeSqlException {
        boolean legalSetting = false;
        BestPracticeService service = vmfsDatastoreSpaceUtilization;
        for (BestPracticeService _service : bestPracticeServices) {
            if (hostsetting.equals(_service.getHostSetting())) {
                legalSetting = true;
                service = _service;
                break;
            }
        }

        if (legalSetting || hostsetting.equals(vmfsDatastoreSpaceUtilization.getHostSetting())) {
            return bestPracticeCheckDao.getRecommand(hostsetting, String.valueOf(service.getRecommendValue()));
        } else {
            throw new DmeSqlException(hostsetting + " hostsetting is not supported");
        }
    }

    @Override
    public int recommandUp(String filedName, String filedValue, BestPracticeRecommandUpReq req) throws DmeSqlException {
        return bestPracticeCheckDao.updateRecommendByFiled(filedName, filedValue, req);
    }

    @Override
    public void saveLog(List<BestPracticeLog> logList) {
        bestPracticeCheckDao.saveRepairLog(logList);
    }

    @Override
    public List<BestPracticeLog> getLog(String hostsetting, String objectId, int pageNo, int pageSize)
        throws DmeSqlException {
        return bestPracticeCheckDao.getRepariLogByPage(hostsetting, objectId, pageNo, pageSize);
    }
}
