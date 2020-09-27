package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.dao.BestPracticeCheckDao;
import com.dmeplugin.dmestore.model.BestPracticeBean;
import com.dmeplugin.dmestore.model.BestPracticeCheckRecordBean;
import com.dmeplugin.dmestore.model.BestPracticeUpResultBase;
import com.dmeplugin.dmestore.model.BestPracticeUpResultResponse;
import com.dmeplugin.dmestore.services.bestpractice.BestPracticeService;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.dmeplugin.vmware.util.VmwareContext;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName BestPracticeProcessServiceImpl
 * @Description TODO
 * @Author wangxiangyong
 * @Date 2020/9/15 16:41
 * @Version V1.0
 **/
public class BestPracticeProcessServiceImpl implements BestPracticeProcessService {
    private static final Logger log = LoggerFactory.getLogger(BestPracticeProcessServiceImpl.class);

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
    public List<BestPracticeCheckRecordBean> getCheckRecord() throws Exception {
        List<BestPracticeCheckRecordBean> list = new ArrayList<>();
        for (BestPracticeService bestPracticeService : bestPracticeServices) {
            BestPracticeCheckRecordBean bean = new BestPracticeCheckRecordBean();
            bean.setHostSetting(bestPracticeService.getHostSetting());
            bean.setLevel(bestPracticeService.getLevel());
            bean.setRecommendValue(String.valueOf(bestPracticeService.getRecommendValue()));
            try {
                List<BestPracticeBean> hostBean = bestPracticeCheckDao.getRecordBeanByHostsetting(bestPracticeService.getHostSetting());
                bean.setHostList(hostBean);
                bean.setCount(hostBean.size());
            } catch (Exception ex) {
                continue;
            }
            if (bean.getCount() > 0) {
                list.add(bean);
            }
        }
        return list;
    }

    @Override
    public List<BestPracticeBean> getCheckRecordBy(String hostSetting, int pageNo, int pageSize) throws Exception {
        List<BestPracticeBean> list = new ArrayList<>();
        for (BestPracticeService bestPracticeService : bestPracticeServices) {
            String s = bestPracticeService.getHostSetting();
            if (hostSetting.equals(s)) {
                try {
                    list = bestPracticeCheckDao.getRecordByPage(hostSetting, pageNo, pageSize);
                } catch (Exception ex) {
                    throw new Exception(ex.getMessage());
                }
                break;
            }
        }
        return list;
    }

    @Override
    public void check(String objectId) throws Exception {
        String hostsStr;
        if (null != objectId) {
            hostsStr = vcsdkUtils.findHostById(objectId);
        } else {
            hostsStr = vcsdkUtils.getAllHosts();
        }

        JsonArray hostArray = gson.fromJson(hostsStr, JsonArray.class);

        Map<String, List<BestPracticeBean>> checkMap = new HashMap<>();
        for (int i = 0; i < hostArray.size(); i++) {
            JsonObject hostObject = hostArray.get(i).getAsJsonObject();
            String _hostName = hostObject.get("hostName").getAsString();
            String _objectId = hostObject.get("objectId").getAsString();
            //对每一项进行检查
            for (BestPracticeService bestPracticeService : bestPracticeServices) {
                try {
                    boolean checkFlag = bestPracticeService.check(vcsdkUtils, _objectId);
                    if (!checkFlag) {
                        BestPracticeBean bean = new BestPracticeBean();
                        String hostSetting = bestPracticeService.getHostSetting();
                        bean.setHostSetting(hostSetting);
                        bean.setRecommendValue(String.valueOf(bestPracticeService.getRecommendValue()));
                        bean.setLevel(bestPracticeService.getLevel());
                        bean.setNeedReboot(String.valueOf(bestPracticeService.needReboot()));
                        bean.setActualValue(String.valueOf(bestPracticeService.getCurrentValue(vcsdkUtils, _objectId)));
                        bean.setHostObjectId(_objectId);
                        bean.setHostName(_hostName);
                        bean.setAutoRepair(String.valueOf(bestPracticeService.autoRepair()));

                        //根据检查项进行归类
                        if (!checkMap.containsKey(hostSetting)) {
                            List<BestPracticeBean> failList = new ArrayList<>();
                            failList.add(bean);
                            checkMap.put(hostSetting, failList);
                        } else {
                            checkMap.get(hostSetting).add(bean);
                        }
                    }
                } catch (Exception ex) {
                    //报错，跳过当前项检查
                    log.error("{} check failed! hostSetting={}", _hostName, bestPracticeService.getHostSetting());
                    continue;
                }
            }
        }

        if (checkMap.size() > 0) {
            //保存到数据库
            bachDBProcess(checkMap);
        }
    }

    private void bachDBProcess(Map<String, List<BestPracticeBean>> map) {
        map.forEach((k, v) -> {
            //本地全量查询
            List<String> localHostNames = null;
            try {
                localHostNames = bestPracticeCheckDao.getHostNameByHostsetting(k);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            List<BestPracticeBean> newList = new ArrayList<>();
            List<BestPracticeBean> upList = new ArrayList<>();
            for (BestPracticeBean o : v) {
                String hostName = o.getHostName();
                if (localHostNames.contains(hostName)) {
                    upList.add(o);
                    localHostNames.remove(hostName);
                } else {
                    newList.add(o);
                }
            }

            //更新
            if (!upList.isEmpty()) {
                //bestPracticeCheckDao.update(upList);
            }

            //新增
            if (!newList.isEmpty()) {
                bestPracticeCheckDao.save(newList);
            }
            //删除
            if (!localHostNames.isEmpty()) {
                bestPracticeCheckDao.deleteByHostNameAndHostsetting(localHostNames, k);
            }
        });
    }

    @Override
    public List<BestPracticeUpResultResponse> update(List<String> objectIds) throws Exception {
        return update(objectIds, null);
    }

    @Override
    public List<BestPracticeUpResultResponse> updateByCluster(String clusterobjectid) throws Exception {
        String vmwarehosts= vcsdkUtils.getHostsOnCluster(clusterobjectid);
        if (!StringUtils.isEmpty(vmwarehosts)) {
            List<Map<String, String>> vmwarehostlists = gson.fromJson(vmwarehosts, new TypeToken<List<Map<String, String>>>() {
            }.getType());
            List<String> hostlists = new ArrayList<>();
            if (vmwarehostlists != null && vmwarehostlists.size() > 0) {
                //分别检查每一个主机是否存在，如果不存在就创建

                for (Map<String, String> hostmap : vmwarehostlists) {
                   hostlists.add(hostmap.get("hostId"));
                }
            }
            return update(hostlists);
        }
        return new ArrayList<>();
    }

    @Override
    public List<BestPracticeUpResultResponse> update(List<String> objectIds, String hostSetting) throws Exception {
        List<BestPracticeService> services = new ArrayList<>();
        //获取对应的service
        for (BestPracticeService bestPracticeService : bestPracticeServices) {
            if (null != hostSetting) {
                if (bestPracticeService.getHostSetting().equals(hostSetting)) {
                    services.add(bestPracticeService);
                    break;
                }
            } else {
                services.addAll(bestPracticeServices);
                break;
            }
        }

        if (null == objectIds || objectIds.size() == 0) {
            objectIds = new ArrayList<>();
            int pageNo = 0;
            int pageSize = 100;
            //获取本地所有
            while (true){
                List<String> hostIdList = bestPracticeCheckDao.getAllHostIds(pageNo++,  pageSize);
                objectIds.addAll(hostIdList);

                if(hostIdList.size() != pageSize){
                    break;
                }
            }
        }

        List<BestPracticeUpResultResponse> responses = new ArrayList<>();
        List<String> successList = new ArrayList<>();
        for (String objectId : objectIds) {
            BestPracticeUpResultResponse response = new BestPracticeUpResultResponse();
            List<BestPracticeUpResultBase> baseList = new ArrayList();
            response.setHostObjectId(objectId);
            boolean needReboot = false;
            for (BestPracticeService service : services) {
                BestPracticeUpResultBase base = new BestPracticeUpResultBase();
                base.setHostObjectId(objectId);
                base.setHostSetting(service.getHostSetting());
                base.setNeedReboot(service.needReboot());
                try {
                    service.update(vcsdkUtils, objectId);
                    base.setUpdateResult(true);
                    //更新成功后，只要有一项是需要重启的则该主机需要重启后生效
                    if (service.needReboot()) {
                        needReboot = true;
                    }
                    successList.add(objectId);
                } catch (Exception ex) {
                    base.setUpdateResult(false);
                    log.error("best practice update {} {} recommend value failed!errMsg:{}", objectId, hostSetting, ex.getMessage());
                    ex.printStackTrace();
                    continue;
                }
                baseList.add(base);
            }
            response.setNeedReboot(needReboot);
            response.setResult(baseList);

            responses.add(response);
        }

        //将成功修改了最佳实践值的记录从表中删除
        bestPracticeCheckDao.deleteBy(responses);

        return responses;
    }
}
