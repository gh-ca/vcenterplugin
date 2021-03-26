package com.huawei.dmestore.services;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.dao.BestPracticeCheckDao;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.DmeSqlException;
import com.huawei.dmestore.exception.VcenterException;
import com.huawei.dmestore.model.BestPracticeBean;
import com.huawei.dmestore.model.BestPracticeCheckRecordBean;
import com.huawei.dmestore.model.BestPracticeUpResultBase;
import com.huawei.dmestore.model.BestPracticeUpResultResponse;
import com.huawei.dmestore.services.bestpractice.BestPracticeService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<BestPracticeCheckRecordBean> getCheckRecord(String type, String objectId) throws DmeException {
        List<String> objectIds = null;

        // 先检查，后获取值
        if (StringUtil.isNotBlank(type)) {
            objectIds = new ArrayList<>();
            if (type.equals("host")) {
                //check(objectId);
                objectIds.add(objectId);
            } else if (type.equals("cluster")) {
                // 查询集群下的所有主机信息
                String hostsOnCluster = vcsdkUtils.getHostsOnCluster(objectId);
                if (StringUtil.isNotBlank(hostsOnCluster)) {
                    List<Map<String, String>> hostList = gson.fromJson(hostsOnCluster,
                        new TypeToken<List<Map<String, String>>>() { }.getType());
                    for (int index = 0; index < hostList.size(); index++) {
                        String tempHostObjectId = hostList.get(index).get("hostId");
                        //check(tempHostObjectId);
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
            try {
                if (objectIds != null && objectIds.size() > 0) {
                    List<BestPracticeBean> tempList = new ArrayList<>();
                    for (String id : objectIds) {
                        List<BestPracticeBean> hostBeanTemp = bestPracticeCheckDao.getRecordBeanByHostsetting(
                            bestPracticeService.getHostSetting(), id);
                        tempList.addAll(hostBeanTemp);
                    }
                    bean.setHostList(tempList);
                    bean.setCount(tempList.size());
                } else {
                    List<BestPracticeBean> hostBean = bestPracticeCheckDao.getRecordBeanByHostsetting(
                        bestPracticeService.getHostSetting(), null);
                    bean.setHostList(hostBean);
                    bean.setCount(hostBean.size());
                }
            } catch (SQLException ex) {
                continue;
            }
            list.add(bean);
        }
        return list;
    }

    @Override
    public List<BestPracticeBean> getCheckRecordBy(String hostSetting, int pageNo, int pageSize) throws DmeException {
        List<BestPracticeBean> list = new ArrayList<>();
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
        JsonArray hostArray = gson.fromJson(hostsStr, JsonArray.class);
        if (null != hostArray) {
            Map<String, List<BestPracticeBean>> checkMap = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
            List<String> unConnectedIds = new ArrayList<>();
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
                            bean.setRecommendValue(String.valueOf(bestPracticeService.getRecommendValue()));
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
                        log.error("{} check failed! hostSetting={}, errorMsg={}", hostName,
                            bestPracticeService.getHostSetting(), ex.getMessage());
                    }
                }
            }

            if (checkMap.size() > 0) {
                // 保存到数据库
                bachDbProcess(checkMap);
            }

            bestPracticeCheckDao.deleteByHostIds(unConnectedIds);
        }
        log.info("====best practice check end====");
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
        return update(objectIds, null);
    }

    @Override
    public List<BestPracticeUpResultResponse> update(List<String> objectIds, String hostSetting)
        throws DmeSqlException {
        // 获取对应的service
        List<BestPracticeService> services = new ArrayList<>();
        for (BestPracticeService bestPracticeService : bestPracticeServices) {
            if (hostSetting != null) {
                if (bestPracticeService.getHostSetting().equals(hostSetting)) {
                    services.add(bestPracticeService);
                    break;
                }
            } else {
                services.addAll(bestPracticeServices);
                break;
            }
        }

        // 从本地数据库查询需要实施最佳实践的主机信息
        Map<String, String> hostMap = getHostMap(objectIds);
        List<BestPracticeUpResultResponse> responses = new ArrayList<>();
        List<String> successList = new ArrayList<>();
        List<String> disconnectedIds = new ArrayList<>();
        for (Map.Entry<String, String> entry : hostMap.entrySet()) {
            String objectId = entry.getKey();
            // 连接断开的主机跳过
            if (!vcsdkUtils.isHostConnected(objectId)) {
                disconnectedIds.add(objectId);
                continue;
            }
            String hostName = entry.getValue();
            BestPracticeUpResultResponse response = new BestPracticeUpResultResponse();
            List<BestPracticeUpResultBase> baseList = new ArrayList();
            response.setHostObjectId(objectId);
            response.setHostName(hostName);
            boolean isNeedReboot = false;
            for (BestPracticeService service : services) {
                // 能自动修复的才进行实施操作
                if (!service.autoRepair()) {
                    continue;
                }
                BestPracticeUpResultBase base = new BestPracticeUpResultBase();
                base.setHostObjectId(objectId);
                base.setHostSetting(service.getHostSetting());
                base.setNeedReboot(service.needReboot());
                try {
                    boolean checkFlag = service.check(vcsdkUtils, objectId);
                    if (!checkFlag) {
                        service.update(vcsdkUtils, objectId);
                        // 更新成功后，只要有一项是需要重启的则该主机需要重启后生效
                        if (service.needReboot()) {
                            isNeedReboot = true;
                        }
                    }
                    base.setUpdateResult(true);
                    successList.add(objectId);
                } catch (Exception ex) {
                    base.setUpdateResult(false);
                    log.error("best practice update {} {} recommend value failed!errMsg:{}", objectId, hostSetting,
                        ex.getMessage());
                }
                baseList.add(base);
            }
            response.setResult(baseList);
            response.setNeedReboot(isNeedReboot);
            responses.add(response);
        }

        // 将成功修改了最佳实践值的记录从表中删除
        bestPracticeCheckDao.deleteBy(responses);
        bestPracticeCheckDao.deleteByHostIds(disconnectedIds);

        return responses;
    }

    private Map<String, String> getHostMap(List<String> objectIds) throws DmeSqlException {
        Map<String, String> hostMap = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        if (objectIds == null || objectIds.size() == 0) {
            // 获取本地所有
            int pageNo = 0;
            try {
                while (true) {
                    Map<String, String> hostMapTemp = bestPracticeCheckDao.getAllHostIds(pageNo++, PAGE_SIZE);
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
                hostMap = bestPracticeCheckDao.getByHostIds(objectIds);
            } catch (SQLException e) {
                throw new DmeSqlException(e.getMessage());
            }
        }
        return hostMap;
    }
}
