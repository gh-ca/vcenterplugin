package com.huawei.dmestore.services;

import com.google.gson.*;
import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.constant.DmeIndicatorConstants;
import com.huawei.dmestore.dao.DmeVmwareRalationDao;
import com.huawei.dmestore.entity.DmeVmwareRelation;
import com.huawei.dmestore.entity.VCenterInfo;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.DmeSqlException;
import com.huawei.dmestore.exception.VcenterException;
import com.huawei.dmestore.model.*;
import com.huawei.dmestore.utils.*;

import com.google.gson.reflect.TypeToken;

import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * VmfsAccessServiceImpl
 *
 * @author yy
 * @since 2020-09-09
 **/
public class VmfsAccessServiceImpl implements VmfsAccessService {
    private static final Logger LOG = LoggerFactory.getLogger(VmfsAccessServiceImpl.class);


    private static final String VOLUME_IDS = "volume_ids";

    private static final String VOLUME_ID = "volume_id";

    private static final String HOST_OBJ_IDS = "hostObjIds";

    private static final String HOST_GROUP_ID = "hostGroupId";

    private static final String HOST_GROUP_ID1 = "hostgroup_id";

    private static final int DIVISOR_100 = 100;

    private static final int HTTP_SUCCESS = 2;

    private static final int SIZE = 20;

    private static final String DATASTORE_NAMES = "dataStoreNames";

    private static final String OBJECTID = "objectId";

    private static final String CAPACITY = "capacity";

    private static final String COUNT = "count";

    private static final String VOLUMEIDS = "volumeIds";

    private static final String HOST_NAME = "hostName";

    private static final String PORT_NAME = "port_name";

    private static final String NAME_FIELD = "name";

    private static final String ID_FIELD = "id";

    private static final String VOLUME_FIELD = "volume";

    private static final String SERVICE_LEVEL_NAME = "service_level_name";

    private static final String VOLUME_WWN = "volume_wwn";

    private static final String STORAGE_ID = "storage_id";

    private static final String HOSTID = "hostId";

    private static final String HOSTIDS = "hostIds";

    private static final String STORAGEID_VOLUMEIDS = "storageId_volumeIds";

    private static final String HOST = "host";

    private static final String CLUSTER = "cluster";

    private static final String CLUSTER_ID = "clusterId";

    private static final String TUNING = "tuning";

    private static final String SMARTQOS = "smartqos";

    private static final String MAXIOPS = "maxiops";

    private static final String MINIOPS = "miniops";

    private static final String MAXBANDWIDTH = "maxbandwidth";

    private static final String MINBANDWIDTH = "minbandwidth";

    private static final String LATENCY = "latency";

    private static final String MAX = "max";

    private static final String VOLUMENAME = "volumeName";

    private static final String VOLUME_NAME = "volume_name";

    private static final String SERVICE_LEVEL_ID = "service_level_id";

    private static final String POOL_RAW_ID = "pool_raw_id";

    private static final String HOST_ID = "host_id";

    private static final String MAPPING = "mapping";

    private static final String TASK_ID = "task_id";

    private static final String CONTROL_POLICY = "control_policy";

    private static final String ALLOCTYPE = "alloctype";

    private static final String SMARTTIER = "smartTier";

    private static final String WORKLOAD_TYPE_ID = "workload_type_id";

    private static final String DATASTORE_OBJECT_IDS = "dataStoreObjectIds";

    private static final String CHOOSEDEVICE = "chooseDevice";

    private static final String TYPE = "type";

    private final String TASKTYPE = "Unmap LUN";

    private static final String FIEL_SEPARATOR = "/";

    private static final String HOST_GROUP_VIEW_TYPE = "host_group";

    private static final int DEFAULT_LEN = 16;

    private static final String CLUSTER_NAME = "clusterName";

    private static final String IP_FIELD = "ip";
    /**
     * 轮询任务状态的超值时间，这里设置超长，避免创建超多的lun超时
     */
    private final long longTaskTimeOut = 30 * 60 * 1000;

    private final String CONNECTIVITY_NORMAL = "normal";

    private final String LANGUAGE_CN = "CN";

    private final String LANGUAGE_EN = "EN";

    private ThreadPoolTaskExecutor threadPoolExecutor;

    private Gson gson = new Gson();

    private DmeVmwareRalationDao dmeVmwareRalationDao;

    private DmeAccessService dmeAccessService;

    private DataStoreStatisticHistoryService dataStoreStatisticHistoryService;

    private DmeStorageService dmeStorageService;

    @Autowired
    private VmwareAccessServiceImpl vmwareAccessService;

    private TaskService taskService;

    private VCSDKUtils vcsdkUtils;

    private VCenterInfoService vcenterinfoservice;

    public ThreadPoolTaskExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void setThreadPoolExecutor(ThreadPoolTaskExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public void setVcenterinfoservice(VCenterInfoService vcenterinfoservice) {
        this.vcenterinfoservice = vcenterinfoservice;
    }

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
    }

    @Override
    public List<VmfsDataInfo> listVmfs() throws DmeException {
        List<VmfsDataInfo> relists = null;
        try {
            // 从关系表中取得DME卷与vcenter存储的对应关系
            List<DmeVmwareRelation> dvrlist = dmeVmwareRalationDao.getDmeVmwareRelation(DmeConstants.STORE_TYPE_VMFS);
            if (dvrlist == null || dvrlist.size() == 0) {
                return relists;
            }

            // 整理数据
            Map<String, DmeVmwareRelation> dvrMap = getDvrMap(dvrlist);

            // 取得所有的存储设备
            List<Storage> storagemap = dmeStorageService.getStorages(null);

            // 整理数据
            Map<String, String> stoNameMap = getStorNameMap(storagemap);

            Map<String, VmfsDataInfo> volIds = new HashMap<>();

            // 取得vcenter中的所有vmfs存储。
            long start = System.currentTimeMillis();
            String listStr = vcsdkUtils.getAllVmfsDataStoreInfos(DmeConstants.STORE_TYPE_VMFS);
            LOG.info("取得vcenter中的所有vmfs存储时间：{}ms", System.currentTimeMillis() - start);
            if (!StringUtils.isEmpty(listStr)) {
                JsonArray jsonArray = new JsonParser().parse(listStr).getAsJsonArray();
                if (jsonArray != null && jsonArray.size() > 0) {
                    relists = new CopyOnWriteArrayList<>();
                    for (int index = 0; index < jsonArray.size(); index++) {
                        JsonObject jo = jsonArray.get(index).getAsJsonObject();

                        String vmwareStoreobjectid = ToolUtils.jsonToStr(jo.get(OBJECTID));
                        if (dvrMap != null && dvrMap.get(vmwareStoreobjectid) != null) {
                            VmfsDataInfo vmfsDataInfo = new VmfsDataInfo();
                            double capacity = ToolUtils.getDouble(jo.get(CAPACITY)) / ToolUtils.GI;
                            double freeSpace = ToolUtils.getDouble(jo.get("freeSpace")) / ToolUtils.GI;
                            double uncommitted = ToolUtils.getDouble(jo.get("uncommitted")) / ToolUtils.GI;

                            vmfsDataInfo.setName(ToolUtils.jsonToStr(jo.get(NAME_FIELD)));
                            vmfsDataInfo.setAlarmState(ToolUtils.jsonToStr(jo.get("alarmState")));
                            vmfsDataInfo.setCapacity(capacity);
                            vmfsDataInfo.setFreeSpace(freeSpace);
                            vmfsDataInfo.setReserveCapacity(capacity + uncommitted - freeSpace);
                            vmfsDataInfo.setObjectid(ToolUtils.jsonToStr(jo.get(OBJECTID)));

                            DmeVmwareRelation dvr = dvrMap.get(vmwareStoreobjectid);
                            volIds.put(dvr.getVolumeId(), vmfsDataInfo);
                        }
                    }
                    Iterator<String> iterator = volIds.keySet().iterator();
                    int k = 0;
                    Map<String, VmfsDataInfo> tm = new HashMap<>();
                    long start1 = System.currentTimeMillis();
                    getVmfsSync(volIds, relists, stoNameMap);
                    LOG.info("vmfs list response：{}ms", System.currentTimeMillis() - start1);
                }
            } else {
                LOG.info("list vmfs return empty");
            }
        } catch (DmeException e) {
            LOG.error("list vmfs error:", e);
            throw new DmeException(e.getMessage());
        }
        return relists;
    }

    public synchronized void getVmfsSync(Map<String, VmfsDataInfo> volIds, List<VmfsDataInfo> vmfsDataInfos, Map<String, String> stoNameMap) throws DmeException {
        Map<String, Object> requestbody = new HashMap<>();
        int total = 0;
        int pageno = 1;
        int allpageno = 1;
        CountDownLatch countDownLatch = new CountDownLatch(volIds.size());
        List<JsonObject> volumeobjectlist = new ArrayList<>();
        while (pageno <= allpageno) {
            requestbody.put("page_no", pageno);
            requestbody.put("page_size", 1000);

            String volumeUrlByName = DmeConstants.DME_VOLUME_BASE_URL;

            ResponseEntity<String> responseEntity = dmeAccessService.access(volumeUrlByName, HttpMethod.GET, gson.toJson(requestbody));
            if (responseEntity.getStatusCodeValue() / DIVISOR_100 != HTTP_SUCCESS) {
                LOG.info(" Query DME volume failed! errorMsg:{}", responseEntity.toString());
            } else {
                JsonObject jsonObject = gson.fromJson(responseEntity.getBody(), JsonObject.class);
                JsonElement volumesElement = jsonObject.get("volumes");
                if (!ToolUtils.jsonIsNull(volumesElement)) {
                    JsonArray volumeArray = volumesElement.getAsJsonArray();
                    for (JsonElement volumeObjectelement : volumeArray) {
                        JsonObject volumeObject = volumeObjectelement.getAsJsonObject();
                        volumeobjectlist.add(volumeObject);
                    }
                }
                JsonObject nfsjson = gson.fromJson(responseEntity.getBody(), JsonObject.class);
                total = ToolUtils.jsonToInt(nfsjson.get("total"));
                allpageno = total / 1000;
                if (total % 1000 != 0)
                    allpageno += 1;
                pageno++;
            }
        }


        for (Map.Entry<String, VmfsDataInfo> entry : volIds.entrySet()) {
            threadPoolExecutor.execute(() -> {
                getVmfsDetailFromDme(vmfsDataInfos, stoNameMap, entry.getValue(), entry.getKey(), volumeobjectlist);
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getVmfsDetailFromDme(List<VmfsDataInfo> relists, Map<String, String> stoNameMap,
                                      VmfsDataInfo vmfsDataInfo, String volumeid, List<JsonObject> volumeobjectlist) {
        // String detailedVolumeUrl = DmeConstants.DME_VOLUME_BASE_URL + FIEL_SEPARATOR + volumeId;
        try {
            //ResponseEntity responseEntity = dmeAccessService.access(detailedVolumeUrl, HttpMethod.GET, null);
            // if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
            // JsonObject voljson = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
            for (JsonObject jsonObject : volumeobjectlist) {
                //JsonObject vjson2 = new JsonObject();//voljson.getAsJsonObject(VOLUME_FIELD);
                if (volumeid.equalsIgnoreCase(ToolUtils.jsonToStr(jsonObject.get(ID_FIELD)))) {

                    vmfsDataInfo.setVolumeId(ToolUtils.jsonToStr(jsonObject.get(ID_FIELD)));
                    vmfsDataInfo.setVolumeName(ToolUtils.jsonToStr(jsonObject.get(NAME_FIELD)));
                    vmfsDataInfo.setStatus(ToolUtils.jsonToStr(jsonObject.get("status")));
                    vmfsDataInfo.setServiceLevelName(ToolUtils.jsonToStr(jsonObject.get(SERVICE_LEVEL_NAME)));
                    vmfsDataInfo.setVmfsProtected(ToolUtils.jsonToBoo(jsonObject.get("protected")));
                    vmfsDataInfo.setWwn(ToolUtils.jsonToStr(jsonObject.get(VOLUME_WWN)));
                    String storageId = ToolUtils.jsonToStr(jsonObject.get(STORAGE_ID));
                    vmfsDataInfo.setDeviceId(storageId);
                    vmfsDataInfo.setDevice(stoNameMap == null ? "" : stoNameMap.get(storageId));

                    parseTuning(vmfsDataInfo, jsonObject);
                    if (vmfsDataInfo != null) {
                        relists.add(vmfsDataInfo);
                    }
                    break;
                }
            }


            // }
        } catch (DmeException e) {
            LOG.error("get volume from dme error!errMsg={}", e.getMessage());
        }
    }

    private void parseTuning(VmfsDataInfo vmfsDataInfo, JsonObject vjson2) throws DmeException {
        if (vjson2 != null && !ToolUtils.jsonIsNull(vjson2.get(TUNING))) {
            JsonObject tuning = vjson2.getAsJsonObject(TUNING);
            if (tuning != null && !ToolUtils.jsonIsNull(tuning.get(SMARTQOS))) {
                JsonObject smartqos = tuning.getAsJsonObject(SMARTQOS);
                boolean dorado = false;
                if (smartqos != null) {
                    vmfsDataInfo.setMaxIops(ToolUtils.jsonToInt(smartqos.get(MAXIOPS), null));
                    vmfsDataInfo.setMinIops(ToolUtils.jsonToInt(smartqos.get(MINIOPS), null));
                    vmfsDataInfo.setMaxBandwidth(ToolUtils.jsonToInt(smartqos.get(MAXBANDWIDTH), null));
                    vmfsDataInfo.setMinBandwidth(ToolUtils.jsonToInt(smartqos.get(MINBANDWIDTH), null));
                    String storageModel = getStorageModelByWwn(vmfsDataInfo.getWwn());
                    Float latency = ToolUtils.jsonToFloat(smartqos.get(LATENCY), null);
                    if (!StringUtils.isEmpty(storageModel)) {
                        StorageTypeShow storageTypeShow = ToolUtils.getStorageTypeShow(storageModel);
                        if (storageTypeShow.getDorado() && latency != null) {
                            vmfsDataInfo.setLatency(latency / 1000);
                        } else {
                            vmfsDataInfo.setLatency(latency);
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<VmfsDataInfo> listVmfsPerformance(List<String> wwns) throws DmeException {
        List<VmfsDataInfo> relists = null;
        try {
            if (wwns != null && wwns.size() > 0) {
                Map<String, Object> params = new HashMap<>(DEFAULT_LEN);
                params.put("obj_ids", wwns);
                Map<String, Object> remap = dataStoreStatisticHistoryService.queryVmfsStatisticCurrent(params);
                if (remap != null && remap.size() > 0) {
                    JsonObject dataJson = new JsonParser().parse(remap.toString()).getAsJsonObject();
                    if (dataJson != null) {
                        relists = new ArrayList<>();
                        for (String wwn : wwns) {
                            JsonObject statisticObject = dataJson.getAsJsonObject(wwn);
                            VmfsDataInfo vmfsDataInfo = new VmfsDataInfo();
                            vmfsDataInfo.setVolumeId(wwn);
                            vmfsDataInfo.setWwn(wwn);
                            vmfsDataInfo.setIops(ToolUtils.jsonToFloat(ToolUtils.getStatistcValue(statisticObject,
                                    DmeIndicatorConstants.COUNTER_ID_VOLUME_THROUGHPUT, MAX), null));
                            vmfsDataInfo.setBandwidth(ToolUtils.jsonToFloat(ToolUtils.getStatistcValue(statisticObject,
                                    DmeIndicatorConstants.COUNTER_ID_VOLUME_BANDWIDTH, MAX), null));
                            vmfsDataInfo.setReadResponseTime(ToolUtils.jsonToFloat(
                                    ToolUtils.getStatistcValue(statisticObject,
                                            DmeIndicatorConstants.COUNTER_ID_VOLUME_READRESPONSETIME, MAX), null));
                            vmfsDataInfo.setWriteResponseTime(ToolUtils.jsonToFloat(
                                    ToolUtils.getStatistcValue(statisticObject,
                                            DmeIndicatorConstants.COUNTER_ID_VOLUME_WRITERESPONSETIME, MAX), null));
                            vmfsDataInfo.setLatency(ToolUtils.jsonToFloat(ToolUtils.getStatistcValue(statisticObject,
                                    DmeIndicatorConstants.COUNTER_ID_VOLUME_RESPONSETIME, MAX), null));
                            relists.add(vmfsDataInfo);
                        }
                    }
                }
            }
        } catch (DmeException e) {
            LOG.error("list vmfs performance error:{}", e);
            throw new DmeException(e.getMessage());
        }
        return relists;
    }

    public Map<String, List<Map<String, Object>>> getAllInitionator() throws DmeException {
        Map<String, List<Map<String, Object>>> hostinitionators = new HashMap<>();
        // 取出所有主机
        LOG.info("get all getAllInitionator");
        List<Map<String, Object>> hostlist = dmeAccessService.getDmeHosts(null);
        if (hostlist != null && hostlist.size() > 0) {
            for (Map<String, Object> hostmap : hostlist) {
                if (hostmap == null || hostmap.get(ID_FIELD) == null) {
                    continue;
                }

                // 通过主机ID查到对应的主机的启动器
                String demHostId = ToolUtils.getStr(hostmap.get(ID_FIELD));

                // 得到主机的启动器
                List<Map<String, Object>> initiators = dmeAccessService.getDmeHostInitiators(demHostId);
                if (initiators != null && initiators.size() > 0) {
                    hostinitionators.put(demHostId, initiators);
                }
            }
        }
        LOG.info("get all getAllInitionator size=" + hostinitionators.size());
        return hostinitionators;
    }

    @Override
    public List<Map<String, String>> createVmfs(Map<String, Object> params) throws DmeException {
        if (params == null) {
            throw new DmeException("create vmfs params is null");
        }

        String objHostId = "";
        String dmeHostId = "";
        String demHostGroupId = "";

        List<Map<String, Object>> volumelist = new ArrayList<>();
        List<String> volumeIds = new ArrayList<>();
        boolean isCreated = false;
        boolean isMappling = false;
        Map<String, List<Map<String, Object>>> allinitionators = getAllInitionator();
        try {
            // 根据服务等级ID获取对应的存储设备ID
            Object storageId = params.get(STORAGE_ID);
            if (storageId == null && params.get(DmeConstants.SERVICELEVELID) != null) {
                storageId =
                        dmeStorageService.getStorageByServiceLevelId(String.valueOf(params.get(DmeConstants.SERVICELEVELID)));
                params.put(STORAGE_ID, storageId);
            }


            LOG.info("create vms checkOrCreateToHostorHostGroup2");
            String objectid = checkOrCreateToHostorHostGroupnotest(params, allinitionators);
            List<Map<String, String>> hostIds = new ArrayList<>();
            if (params != null && params.get(DmeConstants.HOST) != null) {
                // 根据获取到的dme主机，检查主机连通性
                hostIds = estimateConnectivityOfHostOrHostgroup(ToolUtils.getStr(params.get(STORAGE_ID)), objectid, null);
            } else if (params != null && params.get(DmeConstants.CLUSTER) != null) {
                // 检查主机组连通性
                hostIds = estimateConnectivityOfHostOrHostgroup(ToolUtils.getStr(params.get(STORAGE_ID)), null, objectid);
            }
            if (hostIds.size() == 0) {
                Map<String, String> map = new HashMap<>();
                map.put(CONNECTIVITY_NORMAL, objectid);
                hostIds.add(map);
            }
            if (hostIds.size() != 0) {
                for (Map<String, String> map : hostIds) {
                    objHostId = map.get(CONNECTIVITY_NORMAL);
                    if (StringUtils.isEmpty(objHostId)) {
                        LOG.info("start rollback createvmfs");
                        rollBack(volumeIds, dmeHostId, demHostGroupId, isCreated, isMappling);
                        return hostIds;
                    } else {
                        break;
                    }
                }
            }

            // 创建Lun
            String taskId = createLun(params);


            //获取lun
            if (!StringUtils.isEmpty(taskId)) {
                // 创建Lun结果判断
                Set<String> taskIds = new HashSet<>();
                taskIds.add(taskId);
                isCreated = taskService.checkTaskStatusLarge(taskIds, longTaskTimeOut);
                LOG.info("create vms isCreated" + isCreated);
                // 查询看创建任务是否完成。
                if (isCreated) {
                    if (volumelist.size() == 0) {
                        volumelist = getVolumeByName(ToolUtils.getStr(params.get(VOLUMENAME)),
                                null, null, ToolUtils.getStr(params.get(SERVICE_LEVEL_ID)),
                                ToolUtils.getStr(params.get(STORAGE_ID)), ToolUtils.getStr(params.get(POOL_RAW_ID)));
                    }
                    LOG.info("create vms volumelist size=" + volumelist.size());
                    volumeIds = getVolumeId(volumelist);
                    Set<String> umaplist = new HashSet<>();
                    List<String> lunids = new ArrayList<>();

                    for (String volumeId : volumeIds) {
                        if (!StringUtils.isEmpty(volumeId)) {
                            params.put(VOLUME_ID, volumeId);
                        }

                        // 映射主机或者主机组
                        LOG.info("create vms 映射主机或者主机组");
                        lunids.add(volumeId);
                        if (params.get(DmeConstants.HOST) != null && lunids.size() == volumeIds.size()) {
                            dmeHostId = objHostId;
                            taskId = lunMappingToHostOrHostgroup(lunids, dmeHostId, null);
                        } else if (params.get(DmeConstants.CLUSTER) != null) {
                            lunids.clear();
                            lunids.add(volumeId);
                            demHostGroupId = objHostId;
                            taskId = lunMappingToHostOrHostgroup(lunids, null, demHostGroupId);
                        }
                        umaplist.add(taskId);
                    }
                    LOG.info("start mapping task checking");
                    isMappling = taskService.checkTaskStatusLarge(umaplist, longTaskTimeOut);
                    if (isCreated && isMappling) {
                        // 创建了几个卷，就创建几个VMFS，用卷的wwn去找到lun
                        if (volumelist != null && volumelist.size() > 0) {
                            createOnVmware(params, volumelist);
                        }
                    } else {
                        TaskDetailInfo taskinfo = taskService.queryTaskById(taskId);
                        if (taskinfo != null) {
                            throw new DmeException(
                                    "DME create vmfs volume error(task status info:" + "name:" + taskinfo.getTaskName() + ";status:"
                                            + taskinfo.getStatus() + ";" + "progress:" + taskinfo.getProgress() + ";detail:"
                                            + taskinfo.getDetail() + ")!");
                        } else {
                            throw new DmeException("DME create vmfs volume error(task status is failure)!");
                        }
                    }
                } else {
                    throw new DmeException("DME create vmfs volume error(task status is failure)!");
                }
            }
        } catch (DmeException e) {
            LOG.info("start rollback createvmfs");
            rollBack(volumeIds, dmeHostId, demHostGroupId, isCreated, isMappling);
            throw new DmeException("create vmfs failed!", e.getMessage());
        }

        return new ArrayList<>();
    }

    private void rollBack(List<String> volumeIds, String dmeHostId, String demHostGroupId, Boolean isCreated, Boolean isMapping) throws DmeException {
        ResponseEntity responseEntity = null;
        String taskId = "";
        if (volumeIds.size() != 0) {
            Map<String, Object> requestParam = new HashMap<>();
            //解除映射，删除已创建的卷
            requestParam.put(VOLUMEIDS, volumeIds);
            if (isCreated && isMapping) {
                if (!StringUtils.isEmpty(dmeHostId) && isMapping) {
                    requestParam.put(HOST_ID, dmeHostId);
                    responseEntity = hostUnmapping(requestParam);
                }
                if (!StringUtils.isEmpty(demHostGroupId) && isMapping) {
                    requestParam.put(HOST_GROUP_ID1, demHostGroupId);
                    responseEntity = hostGroupUnmapping(requestParam);
                }
                if (responseEntity.getStatusCodeValue() == HttpStatus.ACCEPTED.value()) {
                    taskId = ToolUtils.jsonToStr(
                            new JsonParser().parse(ToolUtils.getStr(responseEntity.getBody())).getAsJsonObject()
                                    .get("task_id"));
                }
            }
            Set<String> taskIds = new HashSet<>();
            taskIds.add(taskId);
            if (taskId.equals("") || taskService.checkTaskStatusLarge(taskIds, longTaskTimeOut)) {
                volumeDelete(requestParam);
                if (!StringUtils.isEmpty(demHostGroupId)) {
                    deleteHostgroup(demHostGroupId);
                }
            }
        }
    }

    private ResponseEntity deleteHostgroup(String hostgroupId) throws DmeException {
        String url = DmeConstants.DELETE_ORIENTED_HOSTGROUP_URL.replace("{hostgroup_id}", hostgroupId);
        return dmeAccessService.access(url, HttpMethod.DELETE, null);
    }

    private String lunMappingToHostOrHostgroup(List<String> volumeIds, String hostId, String clusterId)
            throws DmeException {

        String url = "";
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(clusterId)) {
            url = DmeConstants.MOUNT_VOLUME_TO_HOSTGROUP_URL;
            params.put(HOST_GROUP_ID1, clusterId);
        }
        if (!StringUtils.isEmpty(hostId)) {
            url = DmeConstants.DME_HOST_MAPPING_URL;
            params.put(HOST_ID, hostId);
        }
        String taskId = "";
        params.put(VOLUME_IDS, volumeIds);
        LOG.info("lun mapping to host or hostgroup request params:{}", gson.toJson(params));
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, gson.toJson(params));
        if (responseEntity.getStatusCodeValue() == HttpStatus.ACCEPTED.value()) {
            JsonObject object = new JsonParser().parse(responseEntity.getBody()).getAsJsonObject();
            taskId = ToolUtils.jsonToStr(object.get("task_id"));

        }
        return taskId;
    }

    private String createLun(Map<String, Object> params) throws DmeException {
        // 创建 lun
        String taskId = "";
        if (params.get(DmeConstants.SERVICELEVELID) != null) {
            taskId = createVmfsByServiceLevel2(params);
        } else {
            taskId = createVmfsByUnServiceLevelNew2(params);
        }
        return taskId;
    }


    private List<String> getVolumeId(List<Map<String, Object>> volumeByName) {
        List<String> volumeIds = new ArrayList<>();
        for (Map<String, Object> map : volumeByName) {
            String volumeId = ToolUtils.getStr(map.get(VOLUME_ID));
            volumeIds.add(volumeId);
        }
        return volumeIds;
    }

    private String getDmeHostNameById(String hostId) throws DmeException {
        String hostName = "";
        String getHostUrl = DmeConstants.GET_DME_HOST_URL.replace("{host_id}", hostId);
        ResponseEntity responseEntity = dmeAccessService.access(getHostUrl, HttpMethod.GET, null);
        if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
            JsonObject vjson = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
            hostName = ToolUtils.jsonToStr(vjson.get(NAME_FIELD));

        }
        return hostName;
    }

    private void createOnVmware(Map<String, Object> params, List<Map<String, Object>> volumelist) throws DmeException {
        VCenterInfo vcenterinfo = null;
        if (!StringUtils.isEmpty(params.get(DmeConstants.SERVICELEVELID))) {
            vcenterinfo = vcenterinfoservice.getVcenterInfo();
        }
        final VCenterInfo vcentertemp = vcenterinfo;
        //创建前先扫描hba，避免每次循环扫描

        String hostObjectId = ToolUtils.getStr(params.get(HOSTID));
        String clusterObjectId = ToolUtils.getStr(params.get(CLUSTER_ID));
        if (params.get(DmeConstants.HOST) != null) {
            vcsdkUtils.rescanHbaByHostObjectId(hostObjectId);
        } else if (params.get(DmeConstants.CLUSTER) != null) {
            vcsdkUtils.rescanHbaByClusterObjectId(clusterObjectId);
        }

        CountDownLatch countDownLatch = new CountDownLatch(volumelist.size());
        for (Map<String, Object> volumemap : volumelist) {
            // threadPoolExecutor.submit(()->{
            Map<String, Object> paramstemp = new HashMap<>(params);
            try {
                // 创建vmware中的vmfs存储。
                paramstemp.put(VOLUME_WWN, volumemap.get(VOLUME_WWN));
                paramstemp.put(VOLUME_NAME, volumemap.get(VOLUME_NAME));
                String dataStoreStr = createVmfsOnVmware(paramstemp);
                if (!StringUtils.isEmpty(dataStoreStr)) {
                    Map<String, Object> dataStoreMap = gson.fromJson(dataStoreStr,
                            new TypeToken<Map<String, Object>>() {
                            }.getType());
                    if (dataStoreMap != null) {
                        // 将DME卷与vmfs的关系保存数据库,因为可以同时创建几个卷，无法在此得到对应关系，所以此处不再保存关系信息
                        saveDmeVmwareRalation(volumemap, dataStoreMap);

                        // 关联服务等级
                        if (!StringUtils.isEmpty(paramstemp.get(SERVICE_LEVEL_ID))) {
                            String serviceLevelName = ToolUtils.getStr(paramstemp.get(SERVICE_LEVEL_NAME));
                            vcsdkUtils.attachTag(ToolUtils.getStr(dataStoreMap.get(TYPE)),
                                    ToolUtils.getStr(dataStoreMap.get(ID_FIELD)), serviceLevelName, vcentertemp);
                        }
                    }
                } else {
                    throw new DmeException("vmware create vmfs error:" + params.get(VOLUME_NAME));
                }
            } catch (Exception e) {
                LOG.info("vmware create vmfs error:" + params.get(VOLUME_NAME));
            }
            //   countDownLatch.countDown();
            //  });
        }
        /*try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        /*for (Map<String, Object> volumemap : volumelist) {
            // 创建vmware中的vmfs存储。
            params.put(VOLUME_WWN, volumemap.get(VOLUME_WWN));
            params.put(VOLUME_NAME, volumemap.get(VOLUME_NAME));
            String dataStoreStr = createVmfsOnVmware(params);
            if (!StringUtils.isEmpty(dataStoreStr)) {
                Map<String, Object> dataStoreMap = gson.fromJson(dataStoreStr,
                    new TypeToken<Map<String, Object>>() { }.getType());
                if (dataStoreMap != null) {
                    // 将DME卷与vmfs的关系保存数据库,因为可以同时创建几个卷，无法在此得到对应关系，所以此处不再保存关系信息
                    saveDmeVmwareRalation(volumemap, dataStoreMap);

                    // 关联服务等级
                    if (!StringUtils.isEmpty(params.get(SERVICE_LEVEL_ID))) {
                        String serviceLevelName = ToolUtils.getStr(params.get(SERVICE_LEVEL_NAME));
                        vcsdkUtils.attachTag(ToolUtils.getStr(dataStoreMap.get("type")),
                            ToolUtils.getStr(dataStoreMap.get(ID_FIELD)), serviceLevelName, vcenterinfo);
                    }
                }
            } else {
                throw new DmeException("vmware create vmfs error:" + params.get(VOLUME_NAME));
            }
        }*/
    }

    private String createVmfsOnVmware(Map<String, Object> params) throws DmeException {
        // 在vmware创建存储
        String dataStoreStr = "";
        LOG.info("create vmfs on vmware begin !");
        try {
            if (params != null) {
                // 创建vmware中的vmfs存储。 cluster host
                String hostObjectId = ToolUtils.getStr(params.get(HOSTID));
                String clusterObjectId = ToolUtils.getStr(params.get(CLUSTER_ID));
                String datastoreName = ToolUtils.getStr(params.get(NAME_FIELD));
                int capacity = ToolUtils.getInt(params.get(CAPACITY));
                String existVolumeWwn = ToolUtils.getStr(params.get(VOLUME_WWN));
                String existVolumeName = ToolUtils.getStr(params.get(VOLUME_NAME));
                String volumeName = ToolUtils.getStr(params.get(VOLUMENAME));
                existVolumeName = existVolumeName.replaceAll(volumeName, "");

                // 根据后缀序号改变VMFS的名称
                datastoreName = datastoreName + existVolumeName;

                // 从主机或集群中找出最接近capacity的LUN
                Map<String, Object> hsdmap = null;
                if (params.get(DmeConstants.HOST) != null) {
                    hsdmap = vcsdkUtils.getLunsOnHost(hostObjectId, capacity, existVolumeWwn);
                } else if (params.get(DmeConstants.CLUSTER) != null) {
                    hsdmap = vcsdkUtils.getLunsOnCluster(clusterObjectId, capacity, existVolumeWwn);
                }

                int vmfsMajorVersion = ToolUtils.getInt(params.get("version"));
                int unmapGranularity = ToolUtils.getInt(params.get("spaceReclamationGranularity"));
                int blockSize = ToolUtils.getInt(params.get("blockSize"));
                String unmapPriority = ToolUtils.getStr(params.get("spaceReclamationPriority"));
                dataStoreStr = vcsdkUtils.createVmfsDataStore(hsdmap, capacity, datastoreName, vmfsMajorVersion,
                        blockSize, unmapGranularity, unmapPriority);

                // 如果创建成功，在集群中的其他主机上扫描并挂载datastore
                if (!StringUtils.isEmpty(clusterObjectId)) {
                    vcsdkUtils.scanDataStore(clusterObjectId, null);
                }
            }
        } catch (DmeException e) {
            LOG.error("createVmfsOnVmware error:{}", e.toString());
            throw new DmeException(e.getMessage());
        }
        return dataStoreStr;
    }

    private String createVmfsByServiceLevel2(Map<String, Object> params) throws DmeException {
        // 通过服务等级创建卷，返回任务ID
        String taskId = "";
        try {
            if (params != null && params.get(DmeConstants.SERVICELEVELID) != null) {
                Map<String, Object> svbp = new HashMap<>();
                svbp.put(NAME_FIELD, ToolUtils.getStr(params.get(VOLUMENAME)));
                svbp.put(CAPACITY, ToolUtils.getInt(params.get(CAPACITY)));
                svbp.put(COUNT, ToolUtils.getInt(params.get(COUNT)));
                List<Map<String, Object>> volumes = new ArrayList<>();
                volumes.add(svbp);
                Map<String, Object> requestbody = new HashMap<>();
                requestbody.put("volumes", volumes);
                requestbody.put(SERVICE_LEVEL_ID, ToolUtils.getStr(params.get(SERVICE_LEVEL_ID)));

                ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_VOLUME_BASE_URL,
                        HttpMethod.POST, gson.toJson(requestbody));
                if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_202) {
                    JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString())
                            .getAsJsonObject();
                    if (jsonObject != null && !ToolUtils.jsonIsNull(jsonObject.get(DmeConstants.TASKID))) {
                        taskId = ToolUtils.jsonToStr(jsonObject.get(TASK_ID));
                    }
                } else {
                    throw new DmeException(responseEntity.getBody().toString());
                }
            }
        } catch (DmeException e) {
            LOG.error("createVmfsByServiceLevel error:", e);
            throw new DmeException("create volume error:" + e.toString());
        }
        return taskId;
    }

    private String createVmfsByUnServiceLevelNew2(Map<String, Object> params) throws DmeException {
        // 通过非服务化创建卷，返回任务ID
        String taskId = "";
        try {
            if (params != null && params.get(DmeConstants.STORAGEID) != null) {
                Map<String, Object> requestbody = initCreateBody2(params);
                ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_CREATE_VOLUME_UNLEVEL_URL,
                        HttpMethod.POST, gson.toJson(requestbody));
                if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_202) {
                    JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString())
                            .getAsJsonObject();
                    if (jsonObject != null && !ToolUtils.jsonIsNull(jsonObject.get(DmeConstants.TASKID))) {
                        taskId = ToolUtils.jsonToStr(jsonObject.get(TASK_ID));
                    }
                } else {
                    throw new DmeException(
                            "create UNService Level volume error:" + responseEntity.getBody().toString());
                }
            } else {
                throw new DmeException("create UNService Level volume parameters error.");
            }
        } catch (DmeException e) {
            LOG.error("createVmfsUNServiceLevel error:", e);
            throw new DmeException("create UNService Level volume error:" + e.toString());
        }
        return taskId;
    }

    private Map<String, Object> initCreateBody(Map<String, Object> params, String objhostid) {
        Map<String, Object> requestbody = new HashMap<>();

        // 判断该集群下有多少主机，如果主机在DME不存在就需要创建
        requestbody.put("pool_id", ToolUtils.getStr(params.get(POOL_RAW_ID)));
        requestbody.put(STORAGE_ID, ToolUtils.getStr(params.get(STORAGE_ID)));

        Map<String, Object> tuning = new HashMap<>();
        tuning.put("alloction_type", ToolUtils.getStr(params.get(ALLOCTYPE)));
        tuning.put("workload_type_raw_id", ToolUtils.getInt(params.get(WORKLOAD_TYPE_ID), null));
        String smartTier = ToolUtils.getStr(params.get(SMARTTIER));
        if (!StringUtils.isEmpty(smartTier)) {
            tuning.put("smart_tier", DmeConstants.SMART_TIER.get(smartTier));
        }
        if (params.get("qosFlag") != null && (Boolean) params.get("qosFlag") == true) {
            Map<String, Object> smartqos = new HashMap<>();
            smartqos.put(LATENCY, ToolUtils.getInt(params.get(LATENCY), null));
            smartqos.put("max_bandwidth", ToolUtils.getInt(params.get(MAXBANDWIDTH), null));
            smartqos.put("max_iops", ToolUtils.getInt(params.get(MAXIOPS), null));
            smartqos.put("min_bandwidth", ToolUtils.getInt(params.get(MINBANDWIDTH), null));
            smartqos.put("min_iops", ToolUtils.getInt(params.get(MINIOPS), null));
            if (!StringUtils.isEmpty(params.get(DmeConstants.CONTROLPOLICY))) {
                smartqos.put(DmeConstants.CONTROLPOLICY, params.get(DmeConstants.CONTROLPOLICY));
            }
            smartqos.put("enabled", true);
            tuning.put("smart_qos", smartqos);
        }
        if (!StringUtils.isEmpty(params.get(DmeConstants.ALLOCTYPE)) || !StringUtils.isEmpty(
                params.get(WORKLOAD_TYPE_ID)) || !StringUtils.isEmpty(params.get(CONTROL_POLICY))) {
            requestbody.put(TUNING, tuning);
        }

        Map<String, Object> vs = new HashMap<>();
        vs.put(NAME_FIELD, ToolUtils.getStr(params.get(VOLUMENAME)));
        vs.put(CAPACITY, ToolUtils.getInt(params.get(CAPACITY)));
        vs.put(COUNT, ToolUtils.getInt(params.get(COUNT)));
        List<Map<String, Object>> volumeSpecs = new ArrayList<>();
        volumeSpecs.add(vs);
        requestbody.put("lun_specs", volumeSpecs);

        Map<String, Object> mapping = new HashMap<>();
        if (!StringUtils.isEmpty(params.get(DmeConstants.HOST))) {
            mapping.put(HOST_ID, objhostid);
        } else {
            mapping.put(HOST_GROUP_ID1, objhostid);
        }
        requestbody.put(MAPPING, mapping);
        return requestbody;
    }

    private Map<String, Object> initCreateBody2(Map<String, Object> params) {
        Map<String, Object> requestbody = new HashMap<>();

        // 判断该集群下有多少主机，如果主机在DME不存在就需要创建
        requestbody.put("pool_id", ToolUtils.getStr(params.get(POOL_RAW_ID)));
        requestbody.put(STORAGE_ID, ToolUtils.getStr(params.get(STORAGE_ID)));

        Map<String, Object> tuning = new HashMap<>();
        tuning.put("alloction_type", ToolUtils.getStr(params.get(ALLOCTYPE)));
        tuning.put("workload_type_raw_id", ToolUtils.getInt(params.get(WORKLOAD_TYPE_ID), null));
        String smartTier = ToolUtils.getStr(params.get(SMARTTIER));
        if (!StringUtils.isEmpty(smartTier)) {
            tuning.put("smart_tier", DmeConstants.SMART_TIER.get(smartTier));
        }
        if (params.get("qosFlag") != null && (Boolean) params.get("qosFlag") == true) {
            Map<String, Object> smartqos = new HashMap<>();
            smartqos.put(LATENCY, ToolUtils.getInt(params.get(LATENCY), null));
            smartqos.put("max_bandwidth", ToolUtils.getInt(params.get(MAXBANDWIDTH), null));
            smartqos.put("max_iops", ToolUtils.getInt(params.get(MAXIOPS), null));
            smartqos.put("min_bandwidth", ToolUtils.getInt(params.get(MINBANDWIDTH), null));
            smartqos.put("min_iops", ToolUtils.getInt(params.get(MINIOPS), null));
            if (!StringUtils.isEmpty(params.get(DmeConstants.CONTROLPOLICY))) {
                smartqos.put(DmeConstants.CONTROLPOLICY, params.get(DmeConstants.CONTROLPOLICY));
            }
            smartqos.put("enabled", true);
            tuning.put("smart_qos", smartqos);
        }
        if (!StringUtils.isEmpty(params.get(DmeConstants.ALLOCTYPE)) || !StringUtils.isEmpty(
                params.get(WORKLOAD_TYPE_ID)) || !StringUtils.isEmpty(params.get(CONTROL_POLICY))) {
            requestbody.put(TUNING, tuning);
        }

        Map<String, Object> vs = new HashMap<>();
        vs.put(NAME_FIELD, ToolUtils.getStr(params.get(VOLUMENAME)));
        vs.put(CAPACITY, ToolUtils.getInt(params.get(CAPACITY)));
        vs.put(COUNT, ToolUtils.getInt(params.get(COUNT)));
        List<Map<String, Object>> volumeSpecs = new ArrayList<>();
        volumeSpecs.add(vs);
        requestbody.put("lun_specs", volumeSpecs);

        return requestbody;
    }

    private String checkToHostAllInitiators(String vmwareHostObjId, Map<String, List<Map<String, Object>>> allinitionators) throws DmeException {
        if (StringUtils.isEmpty(vmwareHostObjId)) {
            return "";
        }

        // 判断vcenter主机在DME中是否存在
        String objId = "";
        try {
            // 通过主机的objectid查到主机上所有的hba的wwn或者iqn
            List<Map<String, Object>> hbas = vcsdkUtils.getHbasByHostObjectId(vmwareHostObjId);
            LOG.info("==hbas 0f host on vcenter==", gson.toJson(hbas));
            if (hbas == null || hbas.size() == 0) {
                throw new DmeException(vmwareHostObjId + " The host did not find a valid Hba");
            }
            List<String> wwniqns = new ArrayList<>();
            for (Map<String, Object> hba : hbas) {
                wwniqns.add(ToolUtils.getStr(hba.get(NAME_FIELD)));
            }
            LOG.info("==hostlist on vcenter==", gson.toJson(wwniqns));
            // 取出DME所有主机
            List<Map<String, Object>> dmeHostlist = dmeAccessService.getDmeHosts(null);
            if (dmeHostlist == null || dmeHostlist.size() == 0) {
                LOG.error("list dme hosts failed!dmeHostlist is null");
                return "";
            }
            for (Map<String, Object> hostmap : dmeHostlist) {
                if (hostmap != null && hostmap.get(ID_FIELD) != null) {
                    String demHostId = ToolUtils.getStr(hostmap.get(ID_FIELD));

                    // 得到主机的启动器
                    List<Map<String, Object>> initiators = allinitionators.get(demHostId);
                    LOG.info("==initiators 0f host on vcenter==", gson.toJson(initiators));
                    if (initiators != null && initiators.size() > 0) {
                        for (Map<String, Object> inimap : initiators) {
                            String portName = ToolUtils.getStr(inimap.get(PORT_NAME));
                            if (wwniqns.contains(portName)) {
                                objId = demHostId;
                                break;
                            }
                        }
                    }
                }

                // 如果已经找到的主机就不再循环
                if (!StringUtils.isEmpty(objId)) {
                    break;
                }
            }
        } catch (DmeException ex) {
            LOG.error("checkToHost error:", ex);
            throw new DmeException(ex.getMessage());
        }
        return objId;
    }

    private String checkToHostAllInitiators2(String vmwareHostObjId, Map<String, List<Map<String, Object>>> allinitionators,
                                             List<String> hostIds) throws DmeException {
        if (StringUtils.isEmpty(vmwareHostObjId)) {
            return "";
        }

        // 判断vcenter主机在DME中是否存在
        String objId = "";
        try {
            // 通过主机的objectid查到主机上所有的hba的wwn或者iqn
            List<Map<String, Object>> hbas = vcsdkUtils.getHbasByHostObjectId(vmwareHostObjId);
            LOG.info("==hbas 0f host on vcenter==", gson.toJson(hbas));
            if (hbas == null || hbas.size() == 0) {
                throw new DmeException(vmwareHostObjId + " The host did not find a valid Hba");
            }
            List<String> wwniqns = new ArrayList<>();
            for (Map<String, Object> hba : hbas) {
                wwniqns.add(ToolUtils.getStr(hba.get(NAME_FIELD)));
            }
            LOG.info("==hostlist on vcenter==", gson.toJson(wwniqns));
            // 取出DME所有主机
            //List<Map<String, Object>> dmeHostlist = dmeAccessService.getDmeHosts(null);
            if (CollectionUtils.isEmpty(hostIds)) {
                LOG.error("volume mapped hosts params error!dme hosts is null");
                return "";
            }
            for (String demHostId : hostIds) {
                List<Map<String, Object>> initiators = allinitionators.get(demHostId);
                LOG.info("==initiators 0f host on vcenter==", gson.toJson(initiators));
                if (initiators != null && initiators.size() > 0) {
                    for (Map<String, Object> inimap : initiators) {
                        String portName = ToolUtils.getStr(inimap.get(PORT_NAME));
                        if (wwniqns.contains(portName)) {
                            objId = demHostId;
                            break;
                        }
                    }
                }
            }
        } catch (DmeException ex) {
            LOG.error("checkToHost error:", ex);
            throw new DmeException(ex.getMessage());
        }
        return objId;
    }

    private String checkToHost(String vmwareHostObjId) throws DmeException {
        if (StringUtils.isEmpty(vmwareHostObjId)) {
            return "";
        }

        // 判断vcenter主机在DME中是否存在
        String objId = "";
        try {
            // 通过主机的objectid查到主机上所有的hba的wwn或者iqn
            List<Map<String, Object>> hbas = vcsdkUtils.getHbasByHostObjectId(vmwareHostObjId);
            LOG.info("==hbas 0f host on vcenter==", gson.toJson(hbas));
            if (hbas == null || hbas.size() == 0) {
                throw new DmeException(vmwareHostObjId + " The host did not find a valid Hba");
            }
            List<String> wwniqns = new ArrayList<>();
            for (Map<String, Object> hba : hbas) {
                wwniqns.add(ToolUtils.getStr(hba.get(NAME_FIELD)));
            }
            LOG.info("==hostlist on vcenter==", gson.toJson(wwniqns));
            // 取出DME所有主机
            List<Map<String, Object>> dmeHostlist = dmeAccessService.getDmeHosts(null);
            if (dmeHostlist == null || dmeHostlist.size() == 0) {
                LOG.error("list dme hosts failed!dmeHostlist is null");
                return "";
            }
            for (Map<String, Object> hostmap : dmeHostlist) {
                if (hostmap != null && hostmap.get(ID_FIELD) != null) {
                    String demHostId = ToolUtils.getStr(hostmap.get(ID_FIELD));

                    // 得到主机的启动器
                    List<Map<String, Object>> initiators = dmeAccessService.getDmeHostInitiators(demHostId);
                    LOG.info("==initiators 0f host on vcenter==", gson.toJson(initiators));
                    if (initiators != null && initiators.size() > 0) {
                        for (Map<String, Object> inimap : initiators) {
                            String portName = ToolUtils.getStr(inimap.get(PORT_NAME));
                            if (wwniqns.contains(portName)) {
                                objId = demHostId;
                                break;
                            }
                        }
                    }
                }

                // 如果已经找到的主机就不再循环
                if (!StringUtils.isEmpty(objId)) {
                    break;
                }
            }
        } catch (DmeException ex) {
            LOG.error("checkToHost error:", ex);
            throw new DmeException(ex.getMessage());
        }
        return objId;
    }

    @Override
    public String checkOrCreateToHost(String hostIp, String hostId, Map<String, List<Map<String, Object>>> allinitionators) throws DmeException {
        // 判断主机在DME中是否存在 如果主机不存在就创建并得到主机ID
        String objId = "";
        try {
            if (StringUtils.isEmpty(hostId)) {
                return "";
            }

            // 通过主机的objectid查到主机上所有的hba的wwn或者iqn
            List<Map<String, Object>> hbas = vcsdkUtils.getHbasByHostObjectId(hostId);
            if (hbas == null || hbas.size() == 0) {
                throw new DmeException("The host did not find a valid Hba");
            }
            List<String> wwniqns = new ArrayList<>();
            for (Map<String, Object> hba : hbas) {
                wwniqns.add(ToolUtils.getStr(hba.get(NAME_FIELD)));
            }

            // 取出所有主机
            //List<Map<String, Object>> hostlist = dmeAccessService.getDmeHosts(null);
            /*if (hostlist != null && hostlist.size() > 0) {
                for (Map<String, Object> hostmap : hostlist) {
                    if (hostmap == null || hostmap.get(ID_FIELD) == null) {
                        continue;
                    }

                    // 通过主机ID查到对应的主机的启动器
                    String demHostId = ToolUtils.getStr(hostmap.get(ID_FIELD));

                    // 得到主机的启动器
                    List<Map<String, Object>> initiators = dmeAccessService.getDmeHostInitiators(demHostId);
                    if (initiators != null && initiators.size() > 0) {
                        for (Map<String, Object> inimap : initiators) {
                            String portName = ToolUtils.getStr(inimap.get(PORT_NAME));
                            if (wwniqns.contains(portName)) {
                                objId = demHostId;
                                break;
                            }
                        }
                    }

                    // 如果已经找到的主机就不再循环
                    if (!StringUtils.isEmpty(objId)) {
                        break;
                    }
                }
            }*/

            for (String dmehostid : allinitionators.keySet()) {
                List<Map<String, Object>> hostinitionators = allinitionators.get(dmehostid);
                if (hostinitionators != null && hostinitionators.size() > 0) {
                    for (Map<String, Object> inimap : hostinitionators) {
                        String portName = ToolUtils.getStr(inimap.get(PORT_NAME));
                        if (wwniqns.contains(portName)) {
                            objId = dmehostid;
                            break;
                        }
                    }
                }

            }


            // 如果主机或主机不存在就创建并得到主机或主机组ID
            if (StringUtils.isEmpty(objId)) {
                objId = createHostOnDme(hostIp, hostId);
            }
            if (StringUtils.isEmpty(objId)) {
                throw new DmeException("create host error");
            }
        } catch (DmeException ex) {
            LOG.error("checkOrCreateToHost error:", ex);
            throw new DmeException(ex.getMessage());
        }
        return objId;
    }

    @Override
    public List<Map<String, String>> estimateConnectivityOfHostOrHostgroup(String storageId, String hostId,
                                                                           String hostgroupId) throws DmeException {

        Map<String, Object> requestBody = new HashMap<>();
        if (StringUtils.isEmpty(storageId)) {
            LOG.error("estimate connectivity of host or hostgroup storageid param error!", storageId);
            throw new DmeException("estimate connectivity of host or hostgroup , storageid param error!");
        }
        requestBody.put("storage_id", storageId);
        if (!StringUtils.isEmpty(hostId)) {
            //检查主机连通性参数
            ArrayList<String> hostIds = new ArrayList<>();
            hostIds.add(hostId);
            requestBody.put("host_ids", hostIds);
        }
        if (!StringUtils.isEmpty(hostgroupId)) {
            //检查主机组连通性参数
            requestBody.put("hostgroup_id", hostgroupId);
        }

        String url = DmeConstants.DME_ESTIMATE_CONNECTIVITY;
        Map<String, Object> param = new HashMap<>();
        LOG.info("check connectivity of host or horstgroup on dme ! {" + gson.toJson(requestBody) + "}");
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, gson.toJson(requestBody));
        List<Map<String, String>> results = new ArrayList<>();
        if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
            String body = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
            //JsonArray jsonArray = jsonObject.get("result_list").getAsJsonArray();
            JsonArray jsonArray = jsonObject.get("results").getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                String id = ToolUtils.jsonToStr(element.get("host_id"));
                String status = ToolUtils.jsonToStr(element.get("status"));
                String resultMessage = ToolUtils.jsonToStr(element.get("result_message"));
                // 连通性异常主机结果统计
                Map<String, String> result = new HashMap<>();
                if (status.equalsIgnoreCase("NOT_CONNECT")) {
                    //if (status.equalsIgnoreCase("FAILED")) {
                    String hostName = getDmeHostNameById(id);
                    //result.put(hostName, resultMessage);
                    result.put(hostName, "启动器处于离线状态！");
                }
                if (result.size() != 0) {
                    results.add(result);
                }
            }
        } else {
            throw new DmeException("check connectivity of host or horstgroup on dme failed!");
        }
        return results;
    }

    private Map<String, String> getOrientedHostInfo(String hostName) throws VcenterException {
        Map<String, String> remap = new HashMap<>();
        String hosts = vcsdkUtils.getAllHosts();
        List<Map<String, String>> list = gson.fromJson(hosts, List.class);
        for (Map<String, String> map : list) {
            if (map.get(hostName) != null) {
                remap.put(HOSTID, map.get(HOSTID));
                break;
            }
        }
        return remap;
    }

    private void addHostToHosts(String hostGroupId, String hostId) throws DmeException {

        Map<String, Object> reqMap = new HashMap<>();
        String url = DmeConstants.PUT_ADD_HOST_TO_HOSTS.replace("{hostgroup_id}", hostGroupId);
        List<String> reqbody = new ArrayList<>();
        reqbody.add(hostId);
        reqMap.put("host_ids", reqbody);
        reqMap.put("sync_to_storage", true);
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.PUT, gson.toJson(reqMap));
        if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
            LOG.info("add host to host group success !");
        } else {
            LOG.error("add host to host group fail !");
        }

    }

    private String createHostOnDme(String hostIp, String hostId) throws DmeException {
        String hostObjId = "";
        Map<String, Object> params = new HashMap<>();
        params.put(HOST, hostIp);
        params.put(HOSTID, hostId);
        Map<String, Object> hostmap = dmeAccessService.createHost(params);
        if (hostmap != null && hostmap.get(DmeConstants.ID) != null) {
            hostObjId = hostmap.get(ID_FIELD).toString();
        }
        return hostObjId;
    }

    private String checkOrCreateToHostGroup(String clusterObjectId) throws DmeException {
        // 如果主机组不存在就创建并得到主机组ID 创建前要检查集群下的所有主机是否在DME中存在，只能通过id来创建主机组，如果集群有中文，dme中会创建失败
        String objId = "";
        try {
            if (StringUtils.isEmpty(clusterObjectId)) {
                return "";
            }
            // param str host: 主机  param str cluster: 集群
            // 如果主机或主机不存在就创建并得到主机或主机组ID 如果主机组不存在就需要创建,创建前要检查集群下的所有主机是否在DME中存在
            String clustername = vcsdkUtils.getVcConnectionHelper().objectId2Mor(clusterObjectId).getValue();
            List<String> objIds = new ArrayList<>();

            // 检查集群对应的主机组在DME中是否存在
            List<Map<String, Object>> hostgrouplist = dmeAccessService.getDmeHostGroups(clustername);
            LOG.info("query host group of dme", hostgrouplist.toString());
            if (hostgrouplist != null && hostgrouplist.size() > 0) {
                for (Map<String, Object> hostgroupmap : hostgrouplist) {
                    if (hostgroupmap != null && hostgroupmap.get(NAME_FIELD) != null) {
                        if (clustername.equals(hostgroupmap.get(NAME_FIELD).toString())) {
                            String tmpObjId = ToolUtils.getStr(hostgroupmap.get(ID_FIELD));
                            objIds.add(tmpObjId);
                        }
                    }
                }
            }

            // 如果主机组id存在，就判断该主机组下的所有主机与集群下的主机是否一到处，如果不一致，不管是多还是少都算不一致，都需要重新创建主机组
            if (objIds != null && objIds.size() > 0) {
                for (String tmpObjId : objIds) {
                    objId = checkHostInHostGroup(clusterObjectId, tmpObjId);
                    if (!StringUtils.isEmpty(objId)) {
                        break;
                    }
                }
            }

            // 如果主机组不存在就需要创建,创建前要检查集群下的所有主机是否在DME中存在
            if (StringUtils.isEmpty(objId)) {
                objId = getOrCreateHostGroupId(clusterObjectId);
            }
        } catch (DmeException e) {
            LOG.error("checkOrCreateToHostGroup error:", e);
            throw new DmeException(e.getMessage());
        }
        return objId;
    }

    private String getOrCreateHostGroupId(String clusterObjectId) throws DmeException {
        String objId = "";

        // 取得集群下的所有主机
        String vmwarehosts = vcsdkUtils.getHostsOnCluster(clusterObjectId);
        if (!StringUtils.isEmpty(vmwarehosts)) {
            List<Map<String, String>> vmwarehostlists = gson.fromJson(vmwarehosts,
                    new TypeToken<List<Map<String, String>>>() {
                    }.getType());
            if (vmwarehostlists != null && vmwarehostlists.size() > 0) {
                // 分别检查每一个主机是否存在，如果不存在就创建
                Map<String, List<Map<String, Object>>> allinitionators = getAllInitionator();
                List<String> hostlists = new ArrayList<>();
                for (Map<String, String> hostmap : vmwarehostlists) {
                    String tmpHostId = checkOrCreateToHost(ToolUtils.getStr(hostmap.get(HOST_NAME)),
                            ToolUtils.getStr(hostmap.get(HOSTID)), allinitionators);
                    if (!StringUtils.isEmpty(tmpHostId)) {
                        hostlists.add(tmpHostId);
                    }
                }

                // 在DME中创建主机组
                if (hostlists.size() == 0) {
                    return objId;
                }
                Map<String, Object> params = new HashMap<>();
                params.put("cluster", vcsdkUtils.getVcConnectionHelper().objectId2Mor(clusterObjectId).getValue());
                params.put("hostids", hostlists);
                Map<String, Object> hostmap = dmeAccessService.createHostGroup(params);
                if (hostmap != null && hostmap.get(DmeConstants.ID) != null) {
                    objId = ToolUtils.getStr(hostmap.get(ID_FIELD));
                }
            }
        } else {
            new DmeException("the cluster has no host");
        }
        return objId;
    }

    private String getOrCreateHostGroupId2(String clusterObjectId, String volumeId)
            throws DmeException {

        String objId = "";

        String subVolumeId = ToolUtils.handleString(volumeId);

        // 取得集群下的所有主机
        String vmwarehosts = vcsdkUtils.getHostsOnCluster(clusterObjectId);
        if (!StringUtils.isEmpty(vmwarehosts)) {
            List<Map<String, String>> vmwarehostlists = gson.fromJson(vmwarehosts,
                    new TypeToken<List<Map<String, String>>>() {
                    }.getType());
            if (vmwarehostlists != null && vmwarehostlists.size() > 0) {
                // 分别检查每一个主机是否存在，如果不存在就创建
                List<String> hostlists = new ArrayList<>();
                Map<String, List<Map<String, Object>>> allinitionators = getAllInitionator();
                for (Map<String, String> hostmap : vmwarehostlists) {
                    String tmpHostId = checkOrCreateToHost(ToolUtils.getStr(hostmap.get(HOST_NAME)),
                            ToolUtils.getStr(hostmap.get(HOSTID)), allinitionators);
                    if (!StringUtils.isEmpty(tmpHostId)) {
                        hostlists.add(tmpHostId);
                    }
                }

                // 在DME中创建主机组
                if (hostlists.size() == 0) {
                    return objId;
                }
                Map<String, Object> params = new HashMap<>();
                params.put("cluster",
                        vcsdkUtils.getVcConnectionHelper().objectId2Mor(clusterObjectId).getValue() + "-" + subVolumeId);
                params.put("hostids", hostlists);
                Map<String, Object> hostmap = dmeAccessService.createHostGroup(params);
                if (hostmap != null && hostmap.get(DmeConstants.ID) != null) {
                    objId = ToolUtils.getStr(hostmap.get(ID_FIELD));
                }
            }
        } else {
            new DmeException("the cluster has no host");
        }
        return objId;
    }

    private String checkToHostGroup(String clusterObjectId) throws DmeException {
        String objId = "";

        // 检查集群下的所有主机是否在DME中存在
        try {
            // param str host: 主机  param str cluster: 集群
            // 如果主机或主机不存在就创建并得到主机或主机组ID 如果主机组不存在就需要创建,创建前要检查集群下的所有主机是否在DME中存在
            String clustername = vcsdkUtils.getVcConnectionHelper().objectId2Mor(clusterObjectId).getValue();
            if (!StringUtils.isEmpty(clustername)) {
                List<String> dmeHostGroupIds = new ArrayList<>();

                // 检查集群对应的主机组在DME中是否存在
                List<Map<String, Object>> hostgrouplist = dmeAccessService.getDmeHostGroups(clustername);
                if (hostgrouplist != null && hostgrouplist.size() > 0) {
                    for (Map<String, Object> hostgroupmap : hostgrouplist) {
                        if (clustername.equals(hostgroupmap.get(NAME_FIELD).toString())) {
                            String tmpObjId = ToolUtils.getStr(hostgroupmap.get(ID_FIELD));
                            dmeHostGroupIds.add(tmpObjId);
                        }
                    }
                }

                // 如果主机组id存在，就判断该主机组下的所有主机与集群下的主机是否一到处，如果不一致，不管是多还是少都算不一致，都需要重新创建主机组
                if (dmeHostGroupIds != null && dmeHostGroupIds.size() > 0) {
                    for (String dmeHostGroupId : dmeHostGroupIds) {
                        objId = checkHostInHostGroup(clusterObjectId, dmeHostGroupId);
                        if (!StringUtils.isEmpty(objId)) {
                            break;
                        }
                    }
                }

                // 检查集群下的所有主机是否在DME中存在
                if (!StringUtils.isEmpty(objId)) {
                    return objId;
                }

                // 取得集群下的所有主机
                String vmwarehosts = vcsdkUtils.getHostsOnCluster(clusterObjectId);
                if (!StringUtils.isEmpty(vmwarehosts)) {
                    List<Map<String, String>> vmwarehostlists = gson.fromJson(vmwarehosts,
                            new TypeToken<List<Map<String, String>>>() {
                            }.getType());

                    // 分别检查每一个主机是否存在，如果不存在就创建
                    List<String> hostlists = new ArrayList<>();
                    for (Map<String, String> hostmap : vmwarehostlists) {
                        String tmpHostId = checkToHost(ToolUtils.getStr(hostmap.get(HOSTID)));
                        if (!StringUtils.isEmpty(tmpHostId)) {
                            hostlists.add(tmpHostId);
                        }
                    }
                }
            }
        } catch (VcenterException e) {
            LOG.error("checkOrCreateToHostGroup error:", e);
            throw new DmeException(e.getMessage());
        }
        return objId;
    }

    private Map<String, String> getHostGroupMappingOrientedVolume(String clusterObjectId, List<String> volumeIds) throws DmeException {
        Map<String, String> hostgroupIDAndVolemeID = new HashMap<>();
        try {
            // 获取到主机组列表
            String clustername = vcsdkUtils.getVcConnectionHelper().objectId2Mor(clusterObjectId).getValue();
            if (!StringUtils.isEmpty(clustername)) {
                List<String> dmeHostGroupIds = new ArrayList<>();

                // 检查集群对应的主机组在DME中是否存在
                List<Map<String, Object>> hostgrouplist = dmeAccessService.getDmeHostGroups(clustername);
                if (hostgrouplist != null && hostgrouplist.size() > 0) {
                    for (Map<String, Object> hostgroupmap : hostgrouplist) {
                        String hostgroupName = ToolUtils.getStr(hostgroupmap.get(NAME_FIELD));
                        if (hostgroupName.contains(clustername)) {
                            for (String volume : volumeIds) {
                                String subVolume1 = ToolUtils.handleStringBegin(volume);
                                String subVolume2 = ToolUtils.handleStringEnd(volume);
                                if (hostgroupName.contains(subVolume1) || hostgroupName.contains(subVolume2)) {
                                    hostgroupIDAndVolemeID.put(ToolUtils.getStr(hostgroupmap.get(ID_FIELD)), volume);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (VcenterException e) {
            LOG.error("get volume mapping oriented hostgroup failed!", e);
            throw new DmeException(e.getMessage());
        }
        return hostgroupIDAndVolemeID;
    }

    private String checkHostInHostGroup(String vmwareClusterObjectId, String dmeHostGroupId) throws DmeException {
        String objId = "";
        try {
            if (StringUtils.isEmpty(vmwareClusterObjectId)) {
                LOG.error("vmware Cluster Object Id is null");
                return objId;
            }

            // 得到集群下所有的主机的hba
            List<Map<String, Object>> hbas = vcsdkUtils.getHbasByClusterObjectId(vmwareClusterObjectId);
            LOG.info("host hbas info on vcenter !", hbas.toString());
            if (hbas == null || hbas.size() == 0) {
                LOG.error("vmware Cluster hbas is null");
                return objId;
            }
            List<String> wwniqns = new ArrayList<>();
            for (Map<String, Object> hba : hbas) {
                wwniqns.add(ToolUtils.getStr(hba.get(NAME_FIELD)));
            }
            LOG.info("wwn of host group !", wwniqns.toString() + "wwn size:" + wwniqns.size());
            // 查询dme中指定主机组
            List<Map<String, Object>> dmehosts = dmeAccessService.getDmeHostInHostGroup(dmeHostGroupId);
            LOG.info("wwn of dme hosts ！", dmehosts.toString() + "host size:" + dmehosts.size());
            if (dmehosts != null && dmehosts.size() > 0) {
                List<Map<String, Object>> initiators = new ArrayList<>();
                for (Map<String, Object> dmehost : dmehosts) {
                    // 得到主机的启动器
                    if (dmehost != null && dmehost.get(ID_FIELD) != null) {
                        String demHostId = ToolUtils.getStr(dmehost.get(ID_FIELD));
                        List<Map<String, Object>> subinitiators = dmeAccessService.getDmeHostInitiators(demHostId);
                        LOG.info("initiators of host on dme！",
                                gson.toJson(initiators) + "host size:" + initiators.size());
                        if (subinitiators != null && subinitiators.size() > 0) {
                            initiators.addAll(subinitiators);
                        }
                    }
                }
                if (initiators.size() > 0) {
                    List<String> initiatorName = new ArrayList<>();
                    for (Map<String, Object> inimap : initiators) {
                        if (inimap != null && inimap.get(PORT_NAME) != null) {
                            String portName = ToolUtils.getStr(inimap.get(PORT_NAME));
                            initiatorName.add(portName);
                        }
                    }
                    boolean isCheckHbaInHostGroup = ToolUtils.compare(wwniqns, initiatorName);
                    LOG.info("==result of dme/vcenter wwn compare==", isCheckHbaInHostGroup);
                    if (isCheckHbaInHostGroup) {
                        objId = dmeHostGroupId;
                    }
                }
            }
        } catch (DmeException e) {
            LOG.error("checkHostInHostGroup error:", e);
            throw new DmeException(e.getMessage());
        }
        return objId;
    }

    private List<Map<String, String>> checkOrCreateToHostorHostGroup2(Map<String, Object> params, Map<String, List<Map<String, Object>>> allinitionators) throws DmeException {
        // 根据参数选择检查主机或主机组的方法
        String objId = "";
        List<Map<String, String>> hostIds = new ArrayList<>();
        try {
            // param str host: 主机  param str cluster: 集群
            if (params != null && params.get(DmeConstants.HOST) != null) {
                objId = checkOrCreateToHost(ToolUtils.getStr(params.get(HOST)), ToolUtils.getStr(params.get(HOSTID)), allinitionators);
                // 根据获取到的dme主机，检查主机连通性
                hostIds = estimateConnectivityOfHostOrHostgroup(ToolUtils.getStr(params.get(STORAGE_ID)), objId, null);
            } else if (params != null && params.get(DmeConstants.CLUSTER) != null) {
                objId = getOrCreateHostGroupId2(ToolUtils.getStr(params.get(CLUSTER_ID)), ToolUtils.getStr(params.get(VOLUME_ID)));
                // 检查主机组连通性
                hostIds = estimateConnectivityOfHostOrHostgroup(ToolUtils.getStr(params.get(STORAGE_ID)), null, objId);
            }

            // 连通性正常的主机或者主机组id
            if (hostIds.size() == 0) {
                Map<String, String> map = new HashMap<>();
                map.put(CONNECTIVITY_NORMAL, objId);
                hostIds.add(map);
            }
        } catch (DmeException e) {
            LOG.error("checkOrcreateToHostorHostGroup error:", e);
            throw new DmeException(e.getMessage());
        }
        return hostIds;
    }

    private String checkOrCreateToHostorHostGroupnotest(Map<String, Object> params, Map<String, List<Map<String, Object>>> allinitionators) throws DmeException {
        // 根据参数选择检查主机或主机组的方法
        String objId = "";
        List<Map<String, String>> hostIds = new ArrayList<>();
        try {
            // param str host: 主机  param str cluster: 集群
            if (params != null && params.get(DmeConstants.HOST) != null) {
                objId = checkOrCreateToHost(ToolUtils.getStr(params.get(HOST)), ToolUtils.getStr(params.get(HOSTID)), allinitionators);
                // 根据获取到的dme主机，检查主机连通性
                //hostIds = estimateConnectivityOfHostOrHostgroup(ToolUtils.getStr(params.get(STORAGE_ID)), objId, null);
            } else if (params != null && params.get(DmeConstants.CLUSTER) != null) {
                objId = getOrCreateHostGroupId2(ToolUtils.getStr(params.get(CLUSTER_ID)), ToolUtils.getStr(params.get(VOLUME_ID)));
                // 检查主机组连通性
                //hostIds = estimateConnectivityOfHostOrHostgroup(ToolUtils.getStr(params.get(STORAGE_ID)), null, objId);
            }

            // 连通性正常的主机或者主机组id
            /*if (hostIds.size() == 0) {
                Map<String, String> map = new HashMap<>();
                map.put(CONNECTIVITY_NORMAL, objId);
                hostIds.add(map);
            }*/
        } catch (DmeException e) {
            LOG.error("checkOrcreateToHostorHostGroup error:", e);
            throw new DmeException(e.getMessage());
        }
        return objId;
    }

    private String getVolumeIdByStoreId(String storeId) throws DmeSqlException {
        String volumeId = "";
        try {
            if (!StringUtils.isEmpty(storeId)) {
                volumeId = dmeVmwareRalationDao.getVolumeIdlByStoreId(storeId);
            }
        } catch (DmeSqlException e) {
            throw new DmeSqlException("get volume id from relation list fail!");
        }
        return volumeId;
    }

    private String getStorageIdByVolumeId(String volumeId) throws DmeException {
        String storageId = "";
        if (!StringUtils.isEmpty(volumeId)) {
            String url = DmeConstants.DME_VOLUME_BASE_URL + "/" + volumeId;
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
                String body = responseEntity.getBody();
                JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
                storageId = ToolUtils.jsonToStr(jsonObject.get("volume").getAsJsonObject().get("storage_id"));
            }
        }
        return storageId;
    }

    private List<Map<String, Object>> getVolumeByName(String volumeName, String hostId, String hostGroupId,
                                                      String serviceLevelId, String storageId, String poolRawId) {
        // 根据卷名称,主机id,主机组id,服务等级id,存储设备ID，存储池ID 查询DME卷的信息
        List<Map<String, Object>> volumelist = null;
        String listVolumeUrl = DmeConstants.DME_VOLUME_BASE_URL + "?name=" + volumeName;
        if (!StringUtils.isEmpty(hostId)) {
            listVolumeUrl = listVolumeUrl + "&host_id=" + hostId;
        }
        if (!StringUtils.isEmpty(hostGroupId)) {
            listVolumeUrl = listVolumeUrl + "&hostgroup_id=" + hostGroupId;
        }
        if (!StringUtils.isEmpty(serviceLevelId)) {
            listVolumeUrl = listVolumeUrl + "&service_level_id=" + serviceLevelId;
        }
        if (!StringUtils.isEmpty(storageId)) {
            listVolumeUrl = listVolumeUrl + "&storage_id=" + storageId;
        }
        // 按照这个参数去查询存储池可用查到，如果按照这个参数的返回的结果去查询就查不到
        if (!StringUtils.isEmpty(poolRawId)) {
            listVolumeUrl = listVolumeUrl + "&id=" + poolRawId;
        }
        try {
            LOG.info("getallvolume url=" + listVolumeUrl);
            ResponseEntity responseEntity = dmeAccessService.access(listVolumeUrl, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                volumelist = new ArrayList<>();
                JsonArray jsonArray = jsonObject.getAsJsonArray(DmeConstants.VOLUMES);
                for (int index = 0; index < jsonArray.size(); index++) {
                    JsonObject vjson = jsonArray.get(index).getAsJsonObject();
                    if (vjson != null) {
                        Map<String, Object> remap = new HashMap<>();
                        remap.put("storage_id", storageId);
                        remap.put(VOLUME_ID, ToolUtils.jsonToStr(vjson.get(ID_FIELD)));
                        remap.put(VOLUME_NAME, ToolUtils.jsonToStr(vjson.get(NAME_FIELD)));
                        remap.put(VOLUME_WWN, ToolUtils.jsonToStr(vjson.get(VOLUME_WWN)));
                        volumelist.add(remap);
                    }
                }
            }
        } catch (DmeException e) {
            LOG.error("url:{},error msg:{}", listVolumeUrl, e.getMessage());
        }
        return volumelist;
    }

    private List<Map<String, Object>> getVolumeByNameNew(String volumeName, String hostId, String hostGroupId,
                                                      String serviceLevelId, String storageId, String poolRawId) {
        // 根据卷名称,主机id,主机组id,服务等级id,存储设备ID，存储池ID 查询DME卷的信息
        List<Map<String, Object>> volumelist = null;
        String listVolumeUrl = DmeConstants.DME_VOLUME_BASE_URL + "?name=" + volumeName;
        if (!StringUtils.isEmpty(hostId)) {
            listVolumeUrl = listVolumeUrl + "&host_id=" + hostId;
        }
        if (!StringUtils.isEmpty(hostGroupId)) {
            listVolumeUrl = listVolumeUrl + "&hostgroup_id=" + hostGroupId;
        }
        if (!StringUtils.isEmpty(serviceLevelId)) {
            listVolumeUrl = listVolumeUrl + "&service_level_id=" + serviceLevelId;
        }
        if (!StringUtils.isEmpty(storageId)) {
            listVolumeUrl = listVolumeUrl + "&storage_id=" + storageId;
        }
        // 按照这个参数去查询存储池可用查到，如果按照这个参数的返回的结果去查询就查不到
        if (!StringUtils.isEmpty(poolRawId)) {
            listVolumeUrl = listVolumeUrl + "&id=" + poolRawId;
        }
        try {
            LOG.info("getallvolume url=" + listVolumeUrl);
            ResponseEntity responseEntity = dmeAccessService.access(listVolumeUrl, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                volumelist = new ArrayList<>();
                JsonArray jsonArray = jsonObject.getAsJsonArray(DmeConstants.VOLUMES);
                for (int index = 0; index < jsonArray.size(); index++) {
                    JsonObject vjson = jsonArray.get(index).getAsJsonObject();
                    if (vjson != null) {
                        Map<String, Object> remap = new HashMap<>();
                        remap.put("storage_id", storageId);
                        remap.put(VOLUME_ID, ToolUtils.jsonToStr(vjson.get(ID_FIELD)));
                        remap.put(VOLUME_NAME, ToolUtils.jsonToStr(vjson.get(NAME_FIELD)));
                        remap.put(VOLUME_WWN, ToolUtils.jsonToStr(vjson.get(VOLUME_WWN)));
                        remap.put("created_at", ToolUtils.jsonToStr(vjson.get("created_at")));
                        volumelist.add(remap);
                    }
                }
            }
        } catch (DmeException e) {
            LOG.error("url:{},error msg:{}", listVolumeUrl, e.getMessage());
        }
        return volumelist;
    }


    private void saveDmeVmwareRalation(Map<String, Object> volumeMap, Map<String, Object> dataStoreMap)
            throws DmeException {
        // 保存卷与vmfs的关联关系
        if (volumeMap == null || volumeMap.get(DmeConstants.VOLUMEID) == null) {
            LOG.error("save Dme and Vmware's vmfs Ralation error: volume data is null");
            return;
        }
        if (dataStoreMap == null || dataStoreMap.get(DmeConstants.ID) == null) {
            LOG.error("save Dme and Vmware's vmfs Ralation error: dataStore data is null");
            return;
        }
        DmeVmwareRelation dvr = new DmeVmwareRelation();
        dvr.setStoreId(ToolUtils.getStr(dataStoreMap.get(OBJECTID)));
        dvr.setStoreName(ToolUtils.getStr(dataStoreMap.get(NAME_FIELD)));
        dvr.setVolumeId(ToolUtils.getStr(volumeMap.get(VOLUME_ID)));
        dvr.setVolumeName(ToolUtils.getStr(volumeMap.get(VOLUME_NAME)));
        dvr.setVolumeWwn(ToolUtils.getStr(volumeMap.get(VOLUME_WWN)));
        dvr.setStoreType(DmeConstants.STORE_TYPE_VMFS);
        // 服务等级方式创建vmfs 不需要判断存储类型
        String storageId = ToolUtils.getStr(volumeMap.get("storage_id"));
        if (!StringUtils.isEmpty(storageId)) {
            dvr.setStorageDeviceId(storageId);
            dvr.setStorageType(getStorageModel(storageId));
        }
        List<DmeVmwareRelation> rallist = new ArrayList<>();
        rallist.add(dvr);
        dmeVmwareRalationDao.save(rallist);
        LOG.info("save DmeVmwareRalation success");
    }

    @Override
    public List<Map<String, String>> mountVmfs(Map<String, Object> params) throws DmeException {
        if (params == null || params.size() == 0) {
            throw new DmeException("mount vmfs error, params is null");
        }
        String hostObjId = ToolUtils.getStr(params.get(HOSTID));
        String clusterObjId = ToolUtils.getStr(params.get(CLUSTER_ID));
        if (!StringUtils.isEmpty(hostObjId)) {
            String hostName = vcsdkUtils.getHostName(hostObjId);
            params.put(HOST, hostName);
        }
        String clusterName = "";
        if (!StringUtils.isEmpty(clusterObjId)) {
            clusterName = vcsdkUtils.getClusterName(clusterObjId);
            params.put("cluster", clusterName);
        }

        String objhostid = "";
        String dmeHostgroupId = "";
        List<Map<String, String>> maps = new ArrayList<>();

        try {
            // 通过存储的objectid查询卷id
            if (params.get(DATASTORE_OBJECT_IDS) != null) {
                List<String> dataStoreObjectIds = (List<String>) params.get(DATASTORE_OBJECT_IDS);
                if (dataStoreObjectIds != null && dataStoreObjectIds.size() > 0) {
                    getVolumIdFromLocal2(params, dataStoreObjectIds);
                }
            }
            // param str host: 主机  param str cluster: 集群  dataStoreObjectIds
            // 判断主机或主机组在DME中是否存在, 如果主机或主机不存在就创建并得到主机或主机组ID
            // 接入主机连通性检查
            Map<String, List<String>> storageIdMaps = (Map<String, List<String>>) params.get(STORAGEID_VOLUMEIDS);
            Map<String, List<Map<String, Object>>> allinitionators = getAllInitionator();


            if (storageIdMaps != null && storageIdMaps.size() != 0) {
                for (Map.Entry<String, List<String>> entry : storageIdMaps.entrySet()) {
                    params.put(STORAGE_ID, entry.getKey());
                    for (String volumeId : entry.getValue()) {
                        params.put(VOLUME_ID, volumeId);
                        maps = checkOrCreateToHostorHostGroup2(params, allinitionators);
                        if (maps.size() != 0) {
                            for (Map<String, String> map : maps) {
                                objhostid = map.get(CONNECTIVITY_NORMAL);
                                if (StringUtils.isEmpty(objhostid)) {
                                    return maps;
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            String taskId = "";
            if (params.get(DmeConstants.HOST) != null) {
                // 将卷挂载到主机DME
                LOG.info("mount Vmfs to host begin!");
                taskId = mountVmfsToHost(params, objhostid);
                LOG.info("mount Vmfs to host end!taskId={}", taskId);
            } else {
                // 将卷挂载到集群DME
                LOG.info("mount Vmfs to host group begin!");
                dmeHostgroupId = objhostid;
                taskId = mountVmfsToHostGroup(params, dmeHostgroupId);
                LOG.info("mount Vmfs to host group end!taskId={}", taskId);
            }
            if (StringUtils.isEmpty(taskId)) {
                throw new DmeException("DME mount vmfs volume error(task is null)!");
            }
            //todo 问题单修改；挂载需要展示详情
            //d.根据任务id判断创建Lun的任务状态,并且返回该任务id下的所有明细信息
            List<TaskDetailInfoNew> taskDetailInfoNewList = taskService.getTaskInfo(taskId, longTaskTimeOut);
            LOG.info(gson.toJson(taskDetailInfoNewList));
            //首先获取主任务信息
            TaskDetailInfoNew mainTask = taskService.getMainTaskInfo(taskId, taskDetailInfoNewList);
            if (StringUtils.isEmpty(mainTask)){
                throw new DmeException("get main task info error");
            }else if (mainTask.getStatus()>4){
                throw new DmeException(mainTask.getDetailEn());
            }
            String mainId = mainTask.getId();
            if (StringUtils.isEmpty(mainId)){
                throw new DmeException("get task info error");
            }
            String id = getMapMainChildernId(mainId,taskDetailInfoNewList);
            List<TaskDetailInfoNew> createTaskInfo = getMapInfos(id,taskDetailInfoNewList);
            if (CollectionUtils.isEmpty(createTaskInfo)) {
                throw new DmeException(mainTask.getDetailEn());
            }
            List<String> taskIds = new ArrayList<>();
            taskIds.add(taskId);
            boolean isMounted = taskService.checkTaskStatus(taskIds);
            if (isMounted) {
                LOG.info("vmware mount Vmfs begin!params={}", gson.toJson(params));
                mountVmfsOnVmware(params);
                LOG.info("vmware mount Vmfs end!");
            } else {
                LOG.info("DME mount Vmfs failed!taskId={}", taskId);
                throw new DmeException("DME mount vmfs volume error(task status)!" + mainTask.getDetailEn());
            }
        } catch (DmeException e) {
            // rollback
            if (!StringUtils.isEmpty(dmeHostgroupId)) {
                deleteHostgroup(dmeHostgroupId);
            }
            throw new DmeException("DME mount vmfs error:", e.getMessage());
        }

        return new ArrayList<>();
    }

    private void getVolumIdFromLocal2(Map<String, Object> params, List<String> dataStoreObjectIds)
            throws DmeSqlException {
        List<String> volumeIds = new ArrayList<>();
        List<String> dataStoreNames = new ArrayList<>();
        Map<String, List<String>> storageIds = new HashMap<>();

        for (String dsObjectId : dataStoreObjectIds) {
            DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dsObjectId);
            if (dvr != null) {
                String storageDeviceId = dvr.getStorageDeviceId();
                if (storageIds.get(storageDeviceId) != null) {
                    storageIds.get(storageDeviceId).add(dvr.getVolumeId());
                } else {
                    List<String> volumeIdList = new ArrayList<>();
                    volumeIdList.add(dvr.getVolumeId());
                    storageIds.put(storageDeviceId, volumeIdList);
                }
                volumeIds.add(dvr.getVolumeId());
                dataStoreNames.add(dvr.getStoreName());
            }
        }
        if (volumeIds.size() > 0) {
            params.put(VOLUMEIDS, volumeIds);
            params.put(DATASTORE_NAMES, dataStoreNames);
            params.put(STORAGEID_VOLUMEIDS, storageIds);
        }
    }

    private void mountVmfsOnVmware(Map<String, Object> params) throws VcenterException {
        // 调用vCenter在主机上扫描卷和Datastore
        vcsdkUtils.scanDataStore(ToolUtils.getStr(params.get(CLUSTER_ID)), ToolUtils.getStr(params.get(HOSTID)));

        // 如果是需要扫描LUN来挂载，则需要执行下面的方法，dataStoreNames
        if (params.get(DmeConstants.DATASTORENAMES) != null) {
            List<String> dataStoreNames = (List<String>) params.get(DATASTORE_NAMES);
            if (dataStoreNames != null && dataStoreNames.size() > 0) {
                for (String dataStoreName : dataStoreNames) {
                    Map<String, Object> dsmap = new HashMap<>();
                    dsmap.put(NAME_FIELD, dataStoreName);

                    vcsdkUtils.mountVmfsOnCluster(gson.toJson(dsmap), ToolUtils.getStr(params.get(CLUSTER_ID)),
                            ToolUtils.getStr(params.get(HOSTID)));
                }
            }
        }
    }

    private String mountVmfsToHost(Map<String, Object> params, String objhostid) {
        // 将卷挂载到主机DME
        String taskId = "";
        try {
            if (params != null && params.get(DmeConstants.VOLUMEIDS) != null) {
                Map<String, Object> requestbody = new HashMap<>();
                List<String> volumeIds = (List<String>) params.get(VOLUMEIDS);

                requestbody.put(VOLUME_IDS, volumeIds);
                requestbody.put(HOST_ID, objhostid);
                ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_HOST_MAPPING_URL,
                        HttpMethod.POST, gson.toJson(requestbody));
                if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_202) {
                    JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString())
                            .getAsJsonObject();
                    if (jsonObject != null && jsonObject.get(DmeConstants.TASKID) != null) {
                        taskId = ToolUtils.jsonToStr(jsonObject.get(TASK_ID));
                    }
                }
            } else {
                LOG.error("mountVmfsToHost error:volumeIds is null");
            }
        } catch (DmeException e) {
            LOG.error("mountVmfsToHost error:", e);
        }
        return taskId;
    }

    private String mountVmfsToHostGroup(Map<String, Object> params, String objhostid) {
        // 将卷挂载到集群DME
        String taskId = "";
        try {
            if (params != null && params.get(DmeConstants.VOLUMEIDS) != null) {
                Map<String, Object> requestbody = new HashMap<>();
                List<String> volumeIds = (List<String>) params.get(VOLUMEIDS);

                requestbody.put(VOLUME_IDS, volumeIds);
                requestbody.put(HOST_GROUP_ID1, objhostid);

                ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.MOUNT_VOLUME_TO_HOSTGROUP_URL,
                        HttpMethod.POST, gson.toJson(requestbody));
                if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_202) {
                    JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString())
                            .getAsJsonObject();
                    if (jsonObject != null && jsonObject.get(DmeConstants.TASKID) != null) {
                        taskId = ToolUtils.jsonToStr(jsonObject.get(TASK_ID));
                        LOG.info("mountVmfsToHostGroup task_id===={}", taskId);
                    }
                }
            } else {
                LOG.error("mountVmfsToHost error:volumeIds is null");
            }
        } catch (DmeException e) {
            LOG.error("mountVmfsToHostGroup error:", e);
        }
        return taskId;
    }

    @Override
    public List<VmfsDatastoreVolumeDetail> volumeDetail(String storageObjectId) throws DmeException {
        List<VmfsDatastoreVolumeDetail> list = new ArrayList<>();
        List<String> volumeIds = dmeVmwareRalationDao.getVolumeIdsByStorageId(storageObjectId);
        LOG.error("get volume detail! volumeIds={}", gson.toJson(volumeIds));
        String storageId = "";
        String version = "";
        String model = "";
        for (String volumeId : volumeIds) {
            // 调用DME接口获取卷详情
            String url = DmeConstants.DME_VOLUME_BASE_URL + FIEL_SEPARATOR + volumeId;
            LOG.info("get volume detail! url={}", url);
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() / DIVISOR_100 != HTTP_SUCCESS) {
                LOG.info("get volume detail failed! response={}", gson.toJson(responseEntity));
                throw new DmeException(responseEntity.getBody());
            }
            String responseBody = responseEntity.getBody();
            JsonObject volume = gson.fromJson(responseBody, JsonObject.class).getAsJsonObject(VOLUME_FIELD);
            if (volume.isJsonNull()) {
                continue;
            }
            LOG.info("volume detail! dme response={}", volume.toString());
            VmfsDatastoreVolumeDetail volumeDetail = new VmfsDatastoreVolumeDetail();
            volumeDetail.setWwn(volume.get(VOLUME_WWN).getAsString());
            volumeDetail.setName(volume.get(NAME_FIELD).getAsString());
            volumeDetail.setServiceLevel(ToolUtils.jsonToStr(volume.get(SERVICE_LEVEL_NAME), null));
            if (!volume.get(STORAGE_ID).isJsonNull()) {
                storageId = volume.get(STORAGE_ID).getAsString();

                // 根据存储ID查询存储信息
                url = DmeConstants.DME_STORAGE_DETAIL_URL.replace("{storage_id}", storageId);
                responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
                if (responseEntity.getStatusCodeValue() / DIVISOR_100 == HTTP_SUCCESS) {
                    JsonObject storeageDetail = gson.fromJson(responseEntity.getBody(), JsonObject.class);
                    volumeDetail.setStorage(storeageDetail.get(NAME_FIELD).getAsString());
                    version = ToolUtils.jsonToStr(storeageDetail.get("product_version"));
                    model = ToolUtils.jsonToStr(storeageDetail.get("model"));
                }
            }
            String storageType = model + " " + version;
            StorageTypeShow storageTypeShow = new StorageTypeShow();
            if (!StringUtils.isEmpty(model)&& !StringUtils.isEmpty(version)) {
                storageTypeShow = ToolUtils.getStorageTypeShow(storageType);
            }

            if (!volume.get(POOL_RAW_ID).isJsonNull()) {
                parseStoragePool(volume.get(POOL_RAW_ID).getAsString(), volumeDetail, storageId);
            }

            JsonObject tuning = volume.getAsJsonObject(TUNING);
            if (!tuning.isJsonNull()) {
                parseVolumeTuning(volumeDetail, tuning,storageTypeShow);
            }
            // 存储应用类型
            if (!StringUtils.isEmpty(volumeDetail.getApplicationType())) {
                volumeDetail.setApplicationType(getWorkLoadNameById(storageId, volumeDetail.getApplicationType()));
            }
            list.add(volumeDetail);
        }
        replaceSpecChar(list);
        return list;
    }

    /**
     * @return @return
     * @throws
     * @Description: 替换卷页面展示的&问题
     * @Param @param null
     * @author yc
     * @Date 2021/4/20 18:29
     */
    private void replaceSpecChar(List<VmfsDatastoreVolumeDetail> list) {

        for (VmfsDatastoreVolumeDetail volumeDetail : list) {
            if (null != volumeDetail) {
                if (!StringUtils.isEmpty(volumeDetail.getApplicationType())) {
                    volumeDetail.setApplicationType(volumeDetail.getApplicationType().replace("&amp;", "&"));
                }
            }
        }
    }

    private String getWorkLoadNameById(String storageId, String workLoadType) throws DmeException {
        String name = "";
        if (!StringUtils.isEmpty(storageId) && !StringUtils.isEmpty(workLoadType)) {
            String workloadsUrl = DmeConstants.GET_WORKLOADS_URL.replace("{storage_id}", storageId);
            ResponseEntity responseEntity = dmeAccessService.access(workloadsUrl, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString())
                        .getAsJsonObject();
                if (jsonObject != null && jsonObject.get(DmeConstants.DATAS) != null) {
                    JsonArray jsonArray = jsonObject.getAsJsonArray(DmeConstants.DATAS);
                    for (int index = 0; index < jsonArray.size(); index++) {
                        JsonObject vjson = jsonArray.get(index).getAsJsonObject();
                        if (!StringUtils.isEmpty(workLoadType) && workLoadType.equals(ToolUtils.jsonToStr(vjson.get("id")))) {
                            name = ToolUtils.jsonToStr(vjson.get("name"));
                            break;
                        }
                    }
                }
            } else {
                LOG.error("getWorkLoads error url:{},error:{}", workloadsUrl);
            }
        }
        return name;
    }


    private void parseStoragePool(String poolId, VmfsDatastoreVolumeDetail volumeDetail, String storageDeviceId) throws DmeException {
        String poolName = "";
        try {
            poolName = dmeStorageService.getStorageByPoolRawId(poolId, storageDeviceId);
        } catch (DmeException ex) {
            LOG.info("query datastore pool failed!{}", ex.getMessage());
        }
        LOG.info("query datastore pool success!poolName={}", poolName);
        volumeDetail.setStoragePool(poolName);
    }

    private void parseVolumeTuning(VmfsDatastoreVolumeDetail volumeDetail, JsonObject tuning,StorageTypeShow storageTypeShow) {
        // SmartTier
        volumeDetail.setSmartTier(ToolUtils.jsonToStr(tuning.get("smarttier")));

        // Tunning
        volumeDetail.setDudeplication(ToolUtils.jsonToBoo(tuning.get("dedupe_enabled")));
        volumeDetail.setProvisionType(ToolUtils.jsonToStr(tuning.get(ALLOCTYPE), null));
        volumeDetail.setCompression(ToolUtils.jsonToBoo(tuning.get("compression_enabled")));

        // 应用类型
        volumeDetail.setApplicationType(ToolUtils.jsonToStr(tuning.get(WORKLOAD_TYPE_ID), null));

        // Qos Policy
        if (!tuning.get(SMARTQOS).isJsonNull()) {
            JsonObject smartqos = tuning.getAsJsonObject(SMARTQOS);
            SmartQos smartQos = new SmartQos();
            Integer maxbandwidth = ToolUtils.jsonToInt(smartqos.get("maxbandwidth"), 0);
            Integer minbandwidth = ToolUtils.jsonToInt(smartqos.get("minbandwidth"), 0);
            LOG.info("storage type param{}",storageTypeShow);
            if (storageTypeShow.getDorado()) {
                // 2表示保护上限 和  保护下线
                smartQos.setControlPolicy("2");
                volumeDetail.setControlPolicy("2");
            } else {
                smartQos.setControlPolicy(ToolUtils.jsonToStr(smartqos.get(CONTROL_POLICY), null));
                volumeDetail.setControlPolicy(ToolUtils.jsonToStr(smartqos.get(CONTROL_POLICY), null));
            }
            smartQos.setMaxbandwidth(maxbandwidth);
            smartQos.setMinbandwidth(minbandwidth);
            smartQos.setMaxiops(ToolUtils.jsonToInt(smartqos.get("maxiops"), 0));
            smartQos.setMiniops(ToolUtils.jsonToInt(smartqos.get("miniops"), 0));
            smartQos.setLatency(ToolUtils.jsonToInt(smartqos.get("latency"), 0));
            volumeDetail.setSmartQos(smartQos);
        }
    }

    @Override
    public boolean scanVmfs() throws DmeException {
        String listStr = vcsdkUtils.getAllVmfsDataStoreInfos(DmeConstants.STORE_TYPE_VMFS);
        if (StringUtils.isEmpty(listStr)) {
            LOG.info("===list vmfs datastore return empty");
            return false;
        }
        JsonArray jsonArray = new JsonParser().parse(listStr).getAsJsonArray();
        List<DmeVmwareRelation> relationList = new CopyOnWriteArrayList<>();
        List<Ob> pa = new ArrayList<>();
        Map<String, String> storageIds = new ConcurrentHashMap<>();
        for (int index = 0; index < jsonArray.size(); index++) {
            JsonObject vmfsDatastore = jsonArray.get(index).getAsJsonObject();
            String vmfsDatastoreId = vmfsDatastore.get(OBJECTID).getAsString();
            String vmfsDatastoreName = vmfsDatastore.get(NAME_FIELD).getAsString();
            JsonArray wwnArray = vmfsDatastore.getAsJsonArray("vmfsWwnList");

            if (wwnArray == null || wwnArray.size() == 0) {
                continue;
            }

            List<String> wwns = new ArrayList<>();
            for (int dex = 0; dex < wwnArray.size(); dex++) {
                String wwn = wwnArray.get(dex).getAsString();
                if (StringUtil.isBlank(wwn)) {
                    continue;
                }
                wwns.add(wwn);
            }
            Ob p = new Ob(wwns, vmfsDatastoreId, vmfsDatastoreName);
            pa.add(p);
        }

        List<Ob> tm = new ArrayList<>();
        int k = 0;
        List<Ob> ps = splitOb(pa);
        long start1 = System.currentTimeMillis();
        getRelationSync(ps, SIZE, storageIds, relationList);
        LOG.info("调用vmfs volume_wwn接口时间：{}ms", System.currentTimeMillis() - start1);

        if (relationList.size() > 0) {
            // 数据库处理
            return dmeVmwareRelationDbProcess(relationList, DmeConstants.STORE_TYPE_VMFS);
        }
        return true;
    }


    private List<Ob> splitOb(List<Ob> obs) {
        List<Ob> r = new CopyOnWriteArrayList<>();
        for (Ob ob : obs) {
            if (ob.wwns.size() > SIZE) {
                List<String> wns = new ArrayList<>();
                for (int i = 1; i <= ob.wwns.size(); i++) {
                    wns.add(ob.wwns.get(i - 1));
                    if (i % SIZE == 0) {
                        Ob o = new Ob(wns, ob.vmfsDatastoreId, ob.vmfsDatastoreName);
                        r.add(o);
                        wns = new ArrayList<>();
                    }
                }
                if (wns.size() > 0) {
                    Ob o = new Ob(wns, ob.vmfsDatastoreId, ob.vmfsDatastoreName);
                    r.add(o);
                }
            } else {
                r.add(ob);
            }
        }

        return r;
    }

    static class Ob {
        List<String> wwns;
        String storeType = DmeConstants.STORE_TYPE_VMFS;
        String vmfsDatastoreId;
        String vmfsDatastoreName;

        private Ob(List<String> wwns, String vmfsDatastoreId, String vmfsDatastoreName) {
            this.wwns = wwns;
            this.vmfsDatastoreId = vmfsDatastoreId;
            this.vmfsDatastoreName = vmfsDatastoreName;
        }

    }

    public synchronized void getRelationSync(List<Ob> obs, int size, Map<String, String> storageIds, List<DmeVmwareRelation> relationList) throws DmeException {
        JsonArray volumeList = new JsonArray();
        int offset = 0;
        getVolumesRecursion(offset, volumeList);
        if (null != volumeList && volumeList.size() > 0) {
            for (JsonElement volumeObjectelement : volumeList) {
                JsonObject volumeObject = volumeObjectelement.getAsJsonObject();
                for (Ob ob : obs) {
                    for (String wwn : ob.wwns) {
                        String storageId = ToolUtils.jsonToOriginalStr(volumeObject.get("storage_id"));
                        String dmewwn = ToolUtils.jsonToOriginalStr(volumeObject.get("volume_wwn"));
                        if (wwn.equalsIgnoreCase(dmewwn)) {
                            //根据存储Id 获取存储型号
                            String storageModel = "";
                            if (storageIds.get(storageId) == null) {
                                storageModel = getStorageModel(storageId);
                                storageIds.put(storageId, storageModel);
                            } else {
                                storageModel = storageIds.get(storageId);
                            }
                            DmeVmwareRelation relation = getDmeVmwareRelation(ob.storeType, ob.vmfsDatastoreId, ob.vmfsDatastoreName,
                                    volumeObject, storageModel, storageId);
                            relationList.add(relation);
                        }
                    }
                }
            }
        }
    }

    private JsonArray getVolumesRecursion(int offset, JsonArray volumeList) throws DmeException {
        int limit = 1000;
        String volumeUrlByName = DmeConstants.DME_VOLUME_BASE_URL + "?offset=" + offset;
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(volumeUrlByName, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() / DIVISOR_100 != HTTP_SUCCESS) {
                LOG.info(" Query DME volume failed! errorMsg:{}", responseEntity.toString());
            } else {
                JsonObject jsonObject = gson.fromJson(responseEntity.getBody(), JsonObject.class);
                JsonElement volumesElement = jsonObject.get("volumes");
                int count = jsonObject.get("count").getAsInt();
                offset += limit;
                if (!ToolUtils.jsonIsNull(volumesElement)) {
                    JsonArray volumeArray = volumesElement.getAsJsonArray();
                    volumeList.addAll(volumeArray);
                }
                if (count > offset) {
                    getVolumesRecursion(offset, volumeList);
                }
            }
        } catch (DmeException e) {
            LOG.warn("List volumes error", e);
            throw new DmeException("List volumes error", e.getMessage());
        }
        return volumeList;
    }

    private DmeVmwareRelation getDmeVmwareRelation(String storeType, String vmfsDatastoreId, String vmfsDatastoreName,
                                                   JsonObject volumeObject, String storageModel, String deviceId) {
        String volumeId = ToolUtils.jsonToOriginalStr(volumeObject.get(ID_FIELD));
        String volumeName = ToolUtils.jsonToOriginalStr(volumeObject.get(NAME_FIELD));
        String volumeWwn = ToolUtils.jsonToOriginalStr(volumeObject.get(VOLUME_WWN));

        DmeVmwareRelation relation = new DmeVmwareRelation();
        relation.setStoreId(vmfsDatastoreId);
        relation.setStoreName(vmfsDatastoreName);
        relation.setVolumeId(volumeId);
        relation.setVolumeName(volumeName);
        relation.setVolumeWwn(volumeWwn);
        relation.setStoreType(storeType);
        relation.setStorageType(storageModel);
        relation.setStorageDeviceId(deviceId);
        relation.setState(1);
        return relation;
    }

    private boolean dmeVmwareRelationDbProcess(List<DmeVmwareRelation> relationList, String storeType)
            throws DmeSqlException {
        // 本地全量查询
        List<String> localWwns = dmeVmwareRalationDao.getAllWwnByType(storeType);
        List<String> storeIds = dmeVmwareRalationDao.getAllStorageIdByType(storeType);
        List<DmeVmwareRelation> newList = new ArrayList<>();
        List<DmeVmwareRelation> upList = new ArrayList<>();
        for (DmeVmwareRelation relation : relationList) {
            String wwn = relation.getVolumeWwn();
            String storeId = relation.getStoreId();
            if (localWwns.contains(wwn) && storeIds.contains(storeId)) {
                upList.add(relation);
                localWwns.remove(wwn);
                storeIds.remove(storeId);
            } else {
                newList.add(relation);
            }
        }

        // 更新
        if (!upList.isEmpty()) {
            dmeVmwareRalationDao.update(upList, storeType);
        }

        // 新增
        if (!newList.isEmpty()) {
            dmeVmwareRalationDao.save(newList);
        }

        // 删除
        if (!localWwns.isEmpty() && !storeIds.isEmpty()) {
            dmeVmwareRalationDao.deleteByWwn(localWwns);
        }
        return true;
    }

    @Override
    public Map<String, Object> unmountVmfs(Map<String, Object> params) throws DmeException {
        Map<String, Object> response = new HashMap<>();
        List<String> taskIds = new ArrayList<>();
        List<String> dataStoreObjectIds = null;
        List<String> hostObjIds = new ArrayList<>();
        String clusterObjId = "";
        List<String> volumeIds = new ArrayList<>();
        Map<String, String> volumeIdToStoreName = new HashMap<>();
        List<String> errorStoreName = new ArrayList<>();
        //计数操作对象数量
        int count = 0;
        //失败对象计数
        int failCount = 0;
        // 获取存储对应的卷Id并过滤绑掉虚拟机的存储
        try {
            if (null != params && null != params.get(DATASTORE_OBJECT_IDS)) {
                dataStoreObjectIds = (List<String>) params.get(DATASTORE_OBJECT_IDS);
                count = dataStoreObjectIds.size();
                if (dataStoreObjectIds.size() > 0) {
                    List<String> dataStoreNames = new ArrayList<>();
                    List<Map<String, String>> boundVmfs = new ArrayList<>();
                    for (String dsObjectId : dataStoreObjectIds) {
                        DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dsObjectId);
                        if (dvr == null) {
                            scanVmfs();
                            dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dsObjectId);
                        }
                        boolean isFoundVm = vcsdkUtils.hasVmOnDatastore(dsObjectId);
                        if (isFoundVm) {
                            LOG.error("the vmfs {} contain vm,can not unmount!!!", dvr.getStoreName());
                            Map<String, String> boundedMap = new HashMap<>();
                            boundedMap.put(dvr.getStoreName(),"vCenter error:the vmfs contain vm,can not unmount!");
                            boundVmfs.add(boundedMap);
                            failCount++;
                            continue;
                        }
                        if (dvr != null) {
                            volumeIds.add(dvr.getVolumeId());
                            dataStoreNames.add(dvr.getStoreName());
                        }
                    }
                    if (!CollectionUtils.isEmpty(boundVmfs)) {
                        response.put("bounded", boundVmfs);
                    }
                    if (volumeIds.size() > 0) {
                        params.put(VOLUMEIDS, volumeIds);
                        params.put(DATASTORE_NAMES, dataStoreNames);
                    } else {
                        throw new DmeException("The object has already been deleted or contain virtual machine rdm !");
                    }
                } else {
                    throw new DmeException("The object has already been deleted or has not been completely created or contain virtual machine rdm ,please synchronize vmfs and try again later!");
                }
            }


            List<String> dmeHostIds = new ArrayList<>();
            List<Map<String, List<String>>> needUnmapped = new ArrayList<>();
            if (params != null && volumeIds.size() != 0) {
                if (params.get(HOSTIDS) != null && !"".equals(params.get(HOSTIDS))) {
                    //hostObjIds.add(ToolUtils.getStr(params.get(HOSTIDS)));
                    hostObjIds = (List<String>) params.get(HOSTIDS);
                }
                if (!StringUtils.isEmpty(ToolUtils.getStr(params.get(CLUSTER_ID)))) {
                    clusterObjId = ToolUtils.getStr(params.get(CLUSTER_ID));
                    String hosts = vcsdkUtils.getHostsOnCluster(clusterObjId);
                    if (!StringUtils.isEmpty(hosts)) {
                        List<Map<String, String>> list = gson.fromJson(hosts, List.class);
                        for (Map<String, String> map : list) {
                            String hostId = map.get(HOSTID);
                            if (!StringUtils.isEmpty(hostId)) {
                                hostObjIds.add(hostId);
                            }
                        }
                    }
                }
            }
            // vcenter卸载存储
            if (params.get(DmeConstants.DATASTORENAMES) != null) {
                List<String> dataStoreNames = (List<String>) params.get(DATASTORE_NAMES);
                if (dataStoreNames != null && dataStoreNames.size() > 0) {
                    List<Map<String, String>> vcErrors = new ArrayList<>();
                    for (String dataStoreName : dataStoreNames) {
                        Map<String, Object> dsmap = new HashMap<>();
                        dsmap.put(NAME_FIELD, dataStoreName);
                        if (!CollectionUtils.isEmpty(hostObjIds)) {
                            Map<String, String> vcError = vcsdkUtils.unmountVmfsOnHost(gson.toJson(dsmap), hostObjIds);
                            if (!CollectionUtils.isEmpty(vcError)) {
                                vcErrors.add(vcError);
                                errorStoreName.add(dataStoreName);
                                failCount++;
                            }
                        }
                    }
                    if (!CollectionUtils.isEmpty(vcErrors)) {
                        response.put("vcError", vcErrors);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(hostObjIds)) {
                Map<String, Map<String, Object>> hostMap = getDmeHostByHostObjeId2(hostObjIds);
                if (!CollectionUtils.isEmpty(hostMap)) {
                    for (Map.Entry<String, Map<String, Object>> entry : hostMap.entrySet()) {
                        dmeHostIds.add(entry.getKey());
                        // volume映射给主机 解除映射；volume映射给主机组 不解除映射
                    }
                    needUnmapped = isNeedUnmapping(volumeIds, dmeHostIds);
                } else {
                    LOG.error("the corresponding exsi host was not found in DME");
                }
            }
            // 解除Lun映射   List<Map<String, List<String>>> needUnmapped
            if (!CollectionUtils.isEmpty(needUnmapped)) {
                Map<String, List<String>> unMappingVolumeParams = handleMap(needUnmapped);
                for (Map.Entry<String, List<String>> entry : unMappingVolumeParams.entrySet()) {
                    Map<String, Object> unmap = new HashMap<>();
                    unmap.put(HOST_ID, entry.getKey());
                    unmap.put(VOLUMEIDS, entry.getValue());
                    LOG.info("start to unmap the host's lun.");
                    String taskId = unmountHostGetTaskId2(unmap);
                    taskIds.add(taskId);
                }
            }
            // 获取卸载的任务完成后的状态,默认超时时间10分钟
            if (!CollectionUtils.isEmpty(taskIds)) {
                // 检测任务等待卸载完成后再删除,不用判断是否卸载成功
                List<Map<String, String>> dmeErrors = new ArrayList<>();
                for (String taskId : taskIds) {
                    TaskDetailInfoNew taskDetailInfoNew = taskService.queryTaskByIdReturnMainTask(taskId, longTaskTimeOut);
                    Map<String, String> dmeError = new HashMap<>();
                    if (taskDetailInfoNew != null && taskDetailInfoNew.getStatus() != 3) {
                        // 任务部分成功/失败 取得Lun名称
                        List<String> name = taskService.getFailNameFromCreateTask(TASKTYPE, taskId, longTaskTimeOut);
                        for (String name1:name) {
                            if (!CollectionUtils.isEmpty(errorStoreName)&&!errorStoreName.contains(name1)) {
                                failCount++;
                            }
                        }
                        if (!CollectionUtils.isEmpty(name) && ToolUtils.getStr(params.get("language")).equals(LANGUAGE_CN)) {
                            dmeError.put(name.toString().replace("[", "").replace("]", ""), "DME 错误: " + taskDetailInfoNew.getDetailCn());
                        }
                        if (!CollectionUtils.isEmpty(name)&&ToolUtils.getStr(params.get("language")).equals(LANGUAGE_EN)) {
                            dmeError.put(name.toString().replace("[", "").replace("]", ""), "DME ERROR: "+taskDetailInfoNew.getDetailEn());
                        }
                    }
                    if (dmeError.size() != 0) {
                        dmeErrors.add(dmeError);
                    }
                }
                if (dmeErrors.size() != 0) {
                    response.put("dmeError", dmeErrors);
                }
            }
            if (count > failCount) {
                params.put("success", true);
            }
            if (!CollectionUtils.isEmpty(hostObjIds)) {
                for (String hostId : hostObjIds) {
                    vcsdkUtils.scanDataStore(null, hostId);
                }
            }
        } catch (DmeException | JsonSyntaxException e) {
            throw new DmeException(e.getMessage());
        }
        return response;
    }

    private List<Map<String, String>> findOrientedVolumeMapping(String volumeId) throws DmeException {

        List<Map<String, String>> response = new ArrayList<>();
        String url = DmeConstants.DME_QUERY_ONE_VOLUME.replace("{volume_id}", volumeId);
        ResponseEntity<String> entity = dmeAccessService.access(url, HttpMethod.GET, null);
        if (entity.getStatusCodeValue() == HttpStatus.OK.value()) {
            String body = entity.getBody();

            if (StringUtils.isEmpty(body)) {
                throw new DmeException("query oriented volume is null!{}", volumeId);
            }
            JsonObject volume = new JsonParser().parse(entity.getBody()).getAsJsonObject().get("volume").getAsJsonObject();
            LOG.info("query oriented volume ,{}", volume);
            boolean attached = ToolUtils.jsonToBoo(volume.get("attached"));
            if (!attached) {
                throw new DmeException("Lun attached status error!{}", volumeId);
            }
            JsonArray attachments = volume.get("attachments").getAsJsonArray();
            LOG.info("query oriented volume attachments,{}", attachments);
            if (attachments.size() < 1) {
                LOG.info("query oriented volume attachments is null!{}", volumeId);
            } else {
                for (JsonElement jsonElement : attachments) {
                    JsonObject attachmentsAsJsonObject = jsonElement.getAsJsonObject();
                    Map<String, String> volumeAttachments = new HashMap<>();
                    volumeAttachments.put(ID_FIELD, ToolUtils.jsonToStr(attachmentsAsJsonObject.get(ID_FIELD)));
                    volumeAttachments.put(VOLUME_ID, ToolUtils.jsonToStr(attachmentsAsJsonObject.get(VOLUME_ID)));
                    volumeAttachments.put(HOST_ID, ToolUtils.jsonToStr(attachmentsAsJsonObject.get(HOST_ID)));
                    volumeAttachments.put("attached_at", ToolUtils.jsonToStr(attachmentsAsJsonObject.get("attached_at")));
                    volumeAttachments.put("attached_host_group", ToolUtils.jsonToStr(attachmentsAsJsonObject.get("attached_host_group")));
                    response.add(volumeAttachments);
                }
            }
        }
        return response;
    }

    private List<Map<String, List<String>>> isNeedUnmapping(List<String> volumeIds, List<String> dmeHostIds) throws DmeException {
        // 返回需要解除映射的LunId及其对应的主机ID
        List<Map<String, List<String>>> isNeeds = new ArrayList<>();
        Map<String, List<String>> volumeMappedHost = new HashMap<>();
        for (String volumeId : volumeIds) {
            List<String> hostIds = new ArrayList<>();
            List<Map<String, String>> orientedVolume = findOrientedVolumeMapping(volumeId);
            if (!CollectionUtils.isEmpty(orientedVolume)) {
                for (Map<String, String> map : orientedVolume) {
                    String hostId1 = "";
                    String temp = "";
                    String hostgroupId = map.get("attached_host_group");
                    if (StringUtils.isEmpty(hostgroupId)) {
                        temp = map.get(HOST_ID);
                    }
                    if (!StringUtils.isEmpty(temp) && dmeHostIds.contains(temp)) {
                        hostId1 = temp;
                    }
                    if (!StringUtils.isEmpty(hostId1)) {
                        hostIds.add(hostId1);
                    }
                }
                if (!CollectionUtils.isEmpty(hostIds)) {
                    // 保留需要解除映射的LunId 及对应的主机Id
                    LOG.info("the host that needs to be unmapped was found.size={}", hostIds.size());
                    volumeMappedHost.put(volumeId, hostIds);
                }
            }
        }
        if (!CollectionUtils.isEmpty(volumeMappedHost)) {
            isNeeds.add(volumeMappedHost);
        }
        return isNeeds;
    }


    private boolean findMappingVolumeToHostgroup(List<String> volumeIds, Map<String, Object> hostMap) throws DmeException {
        boolean flag = false;
        Map<String, Object> hostOfHostgroup = new HashMap<>();
        List<String> hostgroupIds = new ArrayList<>();
        for (String volume : volumeIds) {
            // 主机组名称子字符串查找对应主机组名称
            String subHostgroup = ToolUtils.handleStringBegin(volume);
            String subHostgroup2 = ToolUtils.handleStringEnd(volume);
            List<Map<String, Object>> hostgroups = (List<Map<String, Object>>) hostMap.get("hostGroups");
            for (int i = 0; i < hostgroups.size(); i++) {
                String hostgroupName = ToolUtils.getStr(hostgroups.get(i).get(NAME_FIELD));
                // 获取所有需要移除主机的主机组id
                if (hostgroupName.contains(subHostgroup) || hostgroupName.contains(subHostgroup2)) {
                    hostgroupIds.add(ToolUtils.getStr(hostgroups.get(i).get(ID_FIELD)));
                    break;
                }
            }
        }
        // 非主机组映射卷所映射的主机不需要从主机组种移除
        hostOfHostgroup.put(ToolUtils.getStr(hostMap.get(ID_FIELD)), hostgroupIds);
        if (hostgroupIds.size() != 0 && hostOfHostgroup.size() != 0) {
            flag = removeHostFromHostgroup(hostOfHostgroup);
        }
        return flag;
    }

    private boolean removeHostFromHostgroup(Map<String, Object> hostOfHostgroup) throws DmeException {
        boolean flag = false;
        List<String> hostIds = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        if (hostOfHostgroup != null && hostOfHostgroup.size() != 0) {
            for (Map.Entry<String, Object> entry : hostOfHostgroup.entrySet()) {
                List<String> hostgroupIds = (List<String>) entry.getValue();
                for (String hostgroupId : hostgroupIds) {
                    String url = DmeConstants.PUT_REMOVE_HOST_FROM_HOSTGROUP.replace("{hostgroup_id}", hostgroupId);
                    hostIds.add(entry.getKey());
                    params.put("host_ids", hostIds);
                    params.put("sync_to_storage", true);
                    ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.PUT, gson.toJson(params));
                    if (responseEntity.getStatusCodeValue() != HttpStatus.OK.value()) {
                        throw new DmeException("remove host from hostgroup failed!{}",
                                entry.getKey() + " from " + hostgroupId);
                    }
                    flag = isRemoveHostOfHostgroup(hostgroupId);
                }
            }
        }
        return flag;
    }

    private Boolean isRemoveHostOfHostgroup(String hostgroupId) throws DmeException {
        boolean flag = false;
        Map<String, Object> dmeHostGroup = dmeAccessService.getDmeHostGroup(hostgroupId);
        int hostCount = ToolUtils.getInt(dmeHostGroup.get("host_count"));
        if (hostCount == 0) {
            Map<String, Object> hostMap = new HashMap<>();
            hostMap.put(HOST_GROUP_ID1, hostgroupId);
            String taskId = removeHostgroupGetTaskId(hostMap);
            List<String> list = new ArrayList<>();
            list.add(taskId);
            flag = taskService.checkTaskStatus(list);
        }
        if (hostCount != 0) {
            flag = true;
        }
        return flag;
    }

    private List<String> unmountVmfsByDatastores(List<String> dsObjIds, Map<String, Object> params) throws DmeException {
        List<String> taskIds = new ArrayList<>();
        Map<String, List<String>> unionmap = new HashMap<>();
        Map<String, List<Map<String, Object>>> allinitionators = getAllInitionator();
        for (String dsObjId : dsObjIds) {


            // 获取vmfs关联的dme侧volume 并提取volumeId
            List<String> hostObjIds = new ArrayList<>();
            List<String> volumeIds = new ArrayList<>();
            DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dsObjId);
            if (dvr != null) {
                volumeIds.add(dvr.getVolumeId());
            }

            // 没有指定集群 查询存储关联的所有集群
            List<Map<String, Object>> vcClusters = getHostGroupsByStorageId2(dsObjId);
            if (volumeIds.size() > 0 && vcClusters != null && vcClusters.size() > 0) {
                for (Map<String, Object> vcCluster : vcClusters) {
                    String hostGroupObjectId = ToolUtils.getStr(vcCluster.get(HOST_GROUP_ID));
                    if (StringUtils.isEmpty(hostGroupObjectId)) {
                        continue;
                    }
                    // 通过主机组ID,volume id 获取到要卸载的主机组 并执行卸载
                    Map<String, String> hostGroupMappingOrientedVolume =
                            getHostGroupMappingOrientedVolume(hostGroupObjectId, volumeIds);

                    if (hostGroupMappingOrientedVolume != null && hostGroupMappingOrientedVolume.size() != 0) {
                        for (Map.Entry<String, String> entry : hostGroupMappingOrientedVolume.entrySet()) {
                            String hostGroupIdDmeId = entry.getKey();
                            if (dataStoreVmRelateHostOrCluster(dsObjId, null, hostGroupObjectId)) {
                                continue;
                            }
                            Map<String, Object> tempParams = new HashMap<>();
                            tempParams.put(HOST_GROUP_ID1, hostGroupIdDmeId);
                            tempParams.put(VOLUMEIDS, volumeIds);
                            String taskId = unmountHostGroupGetTaskId(tempParams);
                            taskIds.add(taskId);
                        }
                    }
                }
            }

            // 没有指定主机 下查询datastore的主机 并过滤与vm有关联的
            List<Map<String, Object>> vcHosts = getHostsByStorageIdAllinitiators(dsObjId, allinitionators);
            if (volumeIds.size() > 0 && vcHosts != null && vcHosts.size() > 0) {
                Map<String, List<String>> hostvolumemap = unionUnmountHostFromDme(dsObjId, hostObjIds, volumeIds, vcHosts, allinitionators);
                for (String temphostid : hostvolumemap.keySet()) {
                    if (unionmap.get(temphostid) != null) {
                        List<String> tempvolumeids = unionmap.get(temphostid);
                        tempvolumeids.addAll(hostvolumemap.get(temphostid));
                        unionmap.put(temphostid, tempvolumeids);
                    } else {
                        unionmap.put(temphostid, hostvolumemap.get(temphostid));
                    }
                }
            }

            // 提取datastore的hostid
            if (hostObjIds.size() > 0) {
                Object object = params.get(HOST_OBJ_IDS);
                if (object != null) {
                    List<String> hostObjIdList = (List<String>) object;
                    if (hostObjIdList != null && hostObjIdList.size() > 0) {
                        hostObjIds.addAll(hostObjIdList);
                    }
                }
                params.put(HOST_OBJ_IDS, hostObjIds);
            }
        }
        for (String temphostid : unionmap.keySet()) {
            Map<String, Object> tempParams = new HashMap<>();
            tempParams.put(HOST_ID, temphostid);
            tempParams.put(VOLUMEIDS, unionmap.get(temphostid));
            String taskId = unmountHostGetTaskId(tempParams);
            taskIds.add(taskId);
        }

        //taskIds.addAll(unmountHostFromDme(dsObjId, hostObjIds, volumeIds, vcHosts));

        // 删除前的卸载 vcenter侧不扫描,执行删除时再扫描
        // host scan
        return taskIds;
    }

    private List<String> unmountVmfsByDatastores2(Map<String, Object> params) throws DmeException, InterruptedException {

        List<String> taskIds = new ArrayList<>();
        List<String> objHostIds = new ArrayList<>();
        List<String> dsObjIds = (List<String>) params.get(DATASTORE_OBJECT_IDS);
        Map<String, List<String>> volumeMappedHost = new HashMap<>();
        Map<String, List<String>> volumeMappedHostgroup = new HashMap<>();
        Map<String, List<Map<String, Object>>> allinitionators = getAllInitionator();
        DmeVmwareRelation dvr = null;
        for (String dsObjId : dsObjIds) {
            // 获取vmfs关联的dme侧volume 并提取volumeId
            List<String> hostObjIds = new ArrayList<>();
            List<String> volumeIds = new ArrayList<>();
            dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dsObjId);
            if (dvr == null) {
                LOG.error("dme vmware relation table data or store id error,please synchronize vmfs datastore!");
                throw new DmeException("dme vmware relation table data or store id error,please synchronize vmfs datastore!");
            } else {
                volumeIds.add(dvr.getVolumeId());
            }

            //查找Lun映射的所有集群
            List<Map<String, String>> volumeRientedHost = findOrientedVolumeMapping(dvr.getVolumeId());
            if (!CollectionUtils.isEmpty(volumeRientedHost)) {
                //存在映射的主机组 解除映射
                List<String> hostgroupIds = new ArrayList<>();
                for (Map<String, String> hostGroup : volumeRientedHost) {
                    String hostgroupId = hostGroup.get("attached_host_group");
                    if (!StringUtils.isEmpty(hostgroupId) && !hostgroupIds.contains(hostgroupId)) {
                        hostgroupIds.add(hostgroupId);
                    }
                }
                if (!CollectionUtils.isEmpty(hostgroupIds)) {
                    volumeMappedHostgroup.put(dvr.getVolumeId(), hostgroupIds);
                }
            }
            //查询Lun所有映射的主机（解除Lun与主机的关联）
            List<String> hostIds = new ArrayList<>();
            //List<Map<String, String>> volumeRientedHost = findOrientedVolumeMapping(dvr.getVolumeId());
            if (!CollectionUtils.isEmpty(volumeRientedHost)) {
                for (Map<String, String> map : volumeRientedHost) {
                    // 排除以主机组映射的情况
                    String hostId = map.get(HOST_ID);
                    if (StringUtils.isEmpty(map.get("attached_host_group")) && !StringUtils.isEmpty(hostId)) {
                        hostIds.add(hostId);
                    }
                }
                List<Map<String, String>> vcenterHosts = new ArrayList<>();
                if (!CollectionUtils.isEmpty(hostIds)) {
                    vcenterHosts = checkVolumeMappedHost(dsObjId, allinitionators, hostIds);
                    hostIds.clear();
                }
                if (!CollectionUtils.isEmpty(vcenterHosts)) {
                    for (Map<String, String> map : vcenterHosts) {
                        if (!objHostIds.contains(map.get(HOST_OBJ_IDS))) {
                            objHostIds.add(map.get(HOST_OBJ_IDS));
                        }
                        if (!StringUtils.isEmpty(map.get(HOSTID))) {
                            hostIds.add(map.get(HOSTID));
                        }
                    }
                    volumeMappedHost.put(dvr.getVolumeId(), hostIds);
                }
            }
        }

        //vcenter侧移除存储
        for (String storeId : dsObjIds) {
            // vcenter侧移除存储
            if (!vcsdkUtils.deleteVmfsDataStore(storeId)) {
                LOG.error("vcenter remove vmfs datastore error,please try again later!", storeId);
                continue;
            }
        }
        // 解除Lun映射（主机/主机组）
        if (!CollectionUtils.isEmpty(volumeMappedHostgroup)) {
            LOG.info("The host group begins to unmap,storageId{}",dvr.getStorageDeviceId());
            unMappingLunToHostGroup(taskIds, volumeMappedHostgroup, dvr.getStorageDeviceId());
            LOG.info("The host group end to unmap,storageId");
        }
        if (!CollectionUtils.isEmpty(volumeMappedHost)) {
            unMappingLunToHost(taskIds, volumeMappedHost);
        }

        return taskIds;
    }

    private List<String> unMappingLunToHostGroup(List<String> taskIds, Map<String, List<String>> volumeMappedHostgroup, String storageId) throws DmeException, InterruptedException {
        Map<String, List<String>> unmappingParams = convertMap(volumeMappedHostgroup);
        for (Map.Entry<String, List<String>> entry : unmappingParams.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put(HOST_GROUP_ID1, entry.getKey());
            map.put(VOLUMEIDS, entry.getValue());
            if (!CollectionUtils.isEmpty(map)) {
                String taskId = unmountHostGroupGetTaskId(map);
                taskIds.add(taskId);
                // 等待数据同步到存储，否则映射视图没有同步
                Thread.sleep(2000);
                // 满足删除主机组条件（无Lun映射）删除主机组
                LOG.info("主机组解除Lun映射任务状态：{}", taskService.checkTaskStatus(taskIds));
                Boolean isDelete = isDeleteHostgroups(storageId, entry.getKey(), HOST_GROUP_VIEW_TYPE);
                if (taskService.checkTaskStatus(taskIds) && isDelete) {
                    LOG.info("Start deleting host group:isDelete{}", isDelete);
                    removeHostgroupGetTaskId(map);
                    LOG.info("over delete host group");
                }
            }
        }
        return taskIds;
    }

    private List<String> unMappingLunToHost(List<String> taskIds, Map<String, List<String>> volumeMappedHost) throws DmeException {
        Map<String, List<String>> unmappingParams = convertMap(volumeMappedHost);
        Map<String, Object> tempParams = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : unmappingParams.entrySet()) {
            tempParams.put(HOST_ID, entry.getKey());
            tempParams.put(VOLUMEIDS, entry.getValue());
            String taskId = unmountHostGetTaskId(tempParams);
            taskIds.add(taskId);
        }
        return taskIds;
    }


    private List<Map<String, String>> checkVolumeMappedHost(String storeId, Map<String, List<Map<String, Object>>> allinitionators,
                                                            List<String> hostIds) throws DmeException {
        List<Map<String, String>> hostMapList = new ArrayList<>();
        // 获取vcenter存储对应挂载的主机
        String listStr = vcsdkUtils.getHostsByDsObjectId(storeId, true);

        if (!StringUtils.isEmpty(listStr)) {
            List<Map<String, String>> hosts = gson.fromJson(listStr,
                    new TypeToken<List<Map<String, String>>>() {
                    }.getType());

            // vcenter侧主机启动器是否和dem侧主机启动器一致
            for (Map<String, String> host : hosts) {
                String objHostId = ToolUtils.getStr(host.get(HOSTID));
                String hostName = ToolUtils.getStr(host.get(HOST_NAME));
                String dmeHostId = "";
                if (!StringUtils.isEmpty(objHostId)) {
                    dmeHostId = checkToHostAllInitiators2(objHostId, allinitionators, hostIds);
                    Map<String, String> tempMap = new HashMap<>();
                    if (!StringUtils.isEmpty(dmeHostId)) {
                        tempMap.put(HOST_OBJ_IDS, objHostId);
                        tempMap.put(HOST_NAME, hostName);
                        tempMap.put(HOSTID, dmeHostId);
                        hostMapList.add(tempMap);
                    }
                }

            }
        }
        return hostMapList;
    }

    private Map<String, List<String>> convertMap(Map<String, List<String>> param1) {
        Map<String, List<String>> param2 = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : param1.entrySet()) {
            for (String hostOrGroupId : entry.getValue()) {
                if (param2.get(hostOrGroupId) == null) {
                    List<String> volumeIds = new ArrayList<>();
                    volumeIds.add(entry.getKey());
                    param2.put(hostOrGroupId, volumeIds);
                } else {
                    param2.get(hostOrGroupId).add(entry.getKey());
                }
            }
        }
        return param2;

    }

    private Map<String, List<String>> handleMap(List<Map<String, List<String>>> param1) {
        Map<String, List<String>> result = new HashMap<>();
        List<String> list = new ArrayList<>();
        for (Map<String, List<String>> map : param1) {
            Map<String, List<String>> volumeToHost = convertMap(map);
            for (Map.Entry<String, List<String>> entry : volumeToHost.entrySet()) {
                if (result.get(entry.getKey()) == null) {
                    result.put(entry.getKey(), entry.getValue());
                } else {
                    for (String volumeId : entry.getValue()) {
                        if (!result.get(entry.getKey()).contains(volumeId)) {
                            result.get(entry.getKey()).add(volumeId);
                        }
                    }
                }
            }
        }
        return result;
    }

    private Boolean isDeleteHostgroups(String storageId, String hostgroupId, String type) throws DmeException {

        Map<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("request_id", hostgroupId);
        params.put("storage_id", storageId);

        LOG.info("Start query host group mapping view,requset body:{}", gson.toJson(params));
        ResponseEntity<String> access = dmeAccessService.access(DmeConstants.QUERY_HOSTGROUP_MAPPING_VIEW_URL, HttpMethod.POST, gson.toJson(params));
        if (access.getStatusCodeValue() != HttpStatus.OK.value()) {
            throw new DmeException(String.valueOf(access.getStatusCodeValue()), "query host group mapping view failed !");
        }
        LOG.info("over deleting host group,requset body:{}", access.getBody());
        JsonArray jsonArray = new JsonParser().parse(access.getBody()).getAsJsonArray();
        LOG.info("number of host group mapping views：{}" ,jsonArray.size());
        if (jsonArray.size() < 1) {
            return true;
        }
        return false;
    }


    private List<String> unmountVmfs(String dsObjId, Map<String, Object> params) throws DmeException {
        List<String> taskIds = new ArrayList<>();
        Map<String, List<Map<String, Object>>> allinitionators = getAllInitionator();
        // 获取vmfs关联的dme侧volume 并提取volumeId
        List<String> hostObjIds = new ArrayList<>();
        List<String> volumeIds = new ArrayList<>();
        DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dsObjId);
        if (dvr != null) {
            volumeIds.add(dvr.getVolumeId());
        }

        // 没有指定集群 查询存储关联的所有集群
        List<Map<String, Object>> vcClusters = getHostGroupsByStorageId2(dsObjId);
        if (volumeIds.size() > 0 && vcClusters != null && vcClusters.size() > 0) {
            for (Map<String, Object> vcCluster : vcClusters) {
                String hostGroupObjectId = ToolUtils.getStr(vcCluster.get(HOST_GROUP_ID));
                if (StringUtils.isEmpty(hostGroupObjectId)) {
                    continue;
                }
                // 通过主机组ID,volume id 获取到要卸载的主机组 并执行卸载
                Map<String, String> hostGroupMappingOrientedVolume =
                        getHostGroupMappingOrientedVolume(hostGroupObjectId, volumeIds);

                if (hostGroupMappingOrientedVolume != null && hostGroupMappingOrientedVolume.size() != 0) {
                    for (Map.Entry<String, String> entry : hostGroupMappingOrientedVolume.entrySet()) {
                        String hostGroupIdDmeId = entry.getKey();
                        if (dataStoreVmRelateHostOrCluster(dsObjId, null, hostGroupObjectId)) {
                            continue;
                        }
                        Map<String, Object> tempParams = new HashMap<>();
                        tempParams.put(HOST_GROUP_ID1, hostGroupIdDmeId);
                        tempParams.put(VOLUMEIDS, volumeIds);
                        String taskId = unmountHostGroupGetTaskId(tempParams);
                        taskIds.add(taskId);
                    }
                }
            }
        }

        // 没有指定主机 下查询datastore的主机 并过滤与vm有关联的
        List<Map<String, Object>> vcHosts = getHostsByStorageId2(dsObjId);
        if (volumeIds.size() > 0 && vcHosts != null && vcHosts.size() > 0) {
            taskIds.addAll(unmountHostFromDme(dsObjId, hostObjIds, volumeIds, vcHosts, allinitionators));
        }

        // 提取datastore的hostid
        if (hostObjIds.size() > 0) {
            Object object = params.get(HOST_OBJ_IDS);
            if (object != null) {
                List<String> hostObjIdList = (List<String>) object;
                if (hostObjIdList != null && hostObjIdList.size() > 0) {
                    hostObjIds.addAll(hostObjIdList);
                }
            }
            params.put(HOST_OBJ_IDS, hostObjIds);
        }

        // 删除前的卸载 vcenter侧不扫描,执行删除时再扫描
        // host scan
        return taskIds;
    }

    private Map<String, List<String>> unionUnmountHostFromDme(String dsObjId, List<String> hostObjIds, List<String> volumeIds,
                                                              List<Map<String, Object>> vcHosts, Map<String, List<Map<String, Object>>> allinitiators) throws DmeException {
        Map<String, List<String>> retParams = new HashMap<>();
        //List<String> taskIds = new ArrayList<>();
        for (Map<String, Object> vcHost : vcHosts) {

            String hostObjectId = ToolUtils.getStr(vcHost.get(HOSTID));
            if (!StringUtils.isEmpty(hostObjectId)) {
                Map<String, Object> hostMap = getDmeHostByHostObjeId(hostObjectId, allinitiators);

                // vcenter的host和dem的host的启动器一样
                if (hostMap != null && hostMap.size() > 0) {
                    hostObjIds.add(hostObjectId);
                    String hostId = ToolUtils.getStr(hostMap.get(ID_FIELD));

                   /* // 是否关联vm
                    if (vcsdkUtils.hasVmOnDatastore(dsObjId)) {
                        continue;
                    }*/
                    retParams.put(hostId, volumeIds);
                }
            }
        }

        return retParams;
    }

    private List<String> unmountHostFromDme(String dsObjId, List<String> hostObjIds, List<String> volumeIds,
                                            List<Map<String, Object>> vcHosts, Map<String, List<Map<String, Object>>> allinitiators) throws DmeException {
        List<String> taskIds = new ArrayList<>();
        for (Map<String, Object> vcHost : vcHosts) {
            String hostObjectId = ToolUtils.getStr(vcHost.get(HOSTID));
            if (!StringUtils.isEmpty(hostObjectId)) {
                Map<String, Object> hostMap = getDmeHostByHostObjeId(hostObjectId, allinitiators);

                // vcenter的host和dem的host的启动器一样
                if (hostMap != null && hostMap.size() > 0) {
                    hostObjIds.add(hostObjectId);
                    String hostId = ToolUtils.getStr(hostMap.get(ID_FIELD));

                    // 是否关联vm
                    if (dataStoreVmRelateHostOrCluster(dsObjId, hostObjectId, null)) {
                        continue;
                    }
                    Map<String, Object> tempParams = new HashMap<>();
                    tempParams.put(HOST_ID, hostId);
                    tempParams.put(VOLUMEIDS, volumeIds);
                    String taskId = unmountHostGetTaskId(tempParams);
                    taskIds.add(taskId);
                }
            }
        }

        return taskIds;
    }

    // 删除前的卸载
    public List<String> unmountVmfsAll(Map<String, Object> params) throws DmeException, InterruptedException {
        List<String> taskIds = new ArrayList<>();

        // 获取vmfs关联的dme侧volume 并提取volumeId
        if (params != null && params.get(DmeConstants.DATASTOREOBJECTIDS) != null) {
            List<String> dataStoreObjectIds = (List<String>) params.get(DATASTORE_OBJECT_IDS);
            List<String> dataStorageIds = new ArrayList<>();
            if (!CollectionUtils.isEmpty(dataStoreObjectIds)) {
                List<String> volumeIds = new ArrayList<>();
                List<String> dataStoreNames = new ArrayList<>();
                List<String> bounds = new ArrayList<>();
                for (String dsObjectId : dataStoreObjectIds) {
                    DmeVmwareRelation dvr = null;
                    try {
                        dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dsObjectId);
                    } catch (DmeSqlException e) {
                        LOG.info("getDmeVmwareRelationByDsId exception!dsId={}, msg={}", dsObjectId, e.getMessage());
                    }
                    if (dvr == null) {
                        scanVmfs();
                        dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dsObjectId);
                    }
                    // 如果dsObject包含虚拟机 则不能删除
                    boolean isFoundVm = vcsdkUtils.hasVmOnDatastore(dsObjectId);
                    if (isFoundVm) {
                        bounds.add(dvr.getStoreName());
                        LOG.info("vmfs delete,the vmfs:{} contain vm,can not delete!!!", dsObjectId);
                        continue;
                    }

                    if (dvr != null) {
                        volumeIds.add(dvr.getVolumeId());
                        dataStoreNames.add(dvr.getStoreName());
                        dataStorageIds.add(dsObjectId);
                    }
                }
                if (!CollectionUtils.isEmpty(volumeIds)) {
                    params.put(VOLUMEIDS, volumeIds);
                    params.put(DATASTORE_NAMES, dataStoreNames);
                    params.put(DATASTORE_OBJECT_IDS, dataStorageIds);
                }else {
                    throw new DmeException("The object has already been deleted or has not been completely created or contain virtual machine rdm !");
                }
            }
            // dme 侧解除关联
            List<String> unmountTaskIds = unmountVmfsByDatastores2(params);
            if (unmountTaskIds != null && unmountTaskIds.size() > 0) {
                taskIds.addAll(unmountTaskIds);
            }
        }
        return taskIds;
    }

    /**
     * language CN EN
     * @param params include dataStoreObjectIds（list）
     * @throws DmeException
     */
    @Override
    public void deleteVmfs(Map<String, Object> params) throws DmeException {
        // 先调卸载的接口 卸载是卸载所有所有主机和集群(dme侧主机,主机组)
        try {
            List<String> unmountTaskIds = unmountVmfsAll(params);
            if (!CollectionUtils.isEmpty(unmountTaskIds)) {
                // 检测任务等待卸载完成后再删除,不用判断是否卸载成功
                for (String taskId : unmountTaskIds) {
                    TaskDetailInfoNew taskDetailInfoNew = taskService.queryTaskByIdReturnMainTask(taskId, longTaskTimeOut);
                    if (taskDetailInfoNew != null && taskDetailInfoNew.getStatus() != 3) {
                        if (ToolUtils.getStr(params.get("language")).equals(LANGUAGE_CN)) {
                            throw new DmeException("vcenter移除数据存储成功!" + taskDetailInfoNew.getDetailCn());
                        } else {
                            throw new DmeException("vcenter remove datastore success!" + taskDetailInfoNew.getDetailEn());
                        }
                    }
                }
            }
        } catch (DmeException | InterruptedException e) {
            throw new DmeException(e.getMessage());
        }

        // vcenter侧 扫描
        List<String> hostObjIds = (List<String>) params.get(HOST_OBJ_IDS);
        if (!CollectionUtils.isEmpty(hostObjIds)) {
            // 过滤重复的hostObjId
            hostObjIds = new ArrayList<>(new HashSet(hostObjIds));
            for (String hostObjId : hostObjIds) {
                vcsdkUtils.scanDataStore(null, hostObjId);
            }
        }
        // 重新扫描关联关系 更新数据库
        scanVmfs();
    }

    // DME侧从主机卸载卷
    private String unmountHostGetTaskId(Map<String, Object> params) {
        String taskId = "";
        try {
            ResponseEntity responseEntity = hostUnmapping(params);
            taskId = getTaskId(responseEntity);
        } catch (DmeException e) {
            LOG.error(e.getMessage());
        }
        return taskId;
    }

    private String unmountHostGetTaskId2(Map<String, Object> params) {
        String taskId = "";
        try {
            ResponseEntity responseEntity = hostUnmapping(params);
            taskId = getTaskId(responseEntity);
        } catch (DmeException e) {
            LOG.error(e.getMessage());
        }
        return taskId;
    }

    // DME侧从主机组卸载卷
    private String unmountHostGroupGetTaskId(Map<String, Object> params) {
        String taskId = "";
        try {
            ResponseEntity responseEntity = hostGroupUnmapping(params);
            taskId = getTaskId(responseEntity);
        } catch (DmeException e) {
            LOG.error(e.getMessage());
        }
        return taskId;
    }

    private String removeHostgroupGetTaskId(Map<String, Object> params) {
        String taskId = "";
        try {
            ResponseEntity responseEntity = removeHostgroup(params);
            taskId = getTaskId(responseEntity);
        } catch (DmeException e) {
            LOG.error(e.getMessage());
        }
        return taskId;
    }

    private ResponseEntity removeHostgroup(Map<String, Object> params) throws DmeException {
        String hostGroupId = ToolUtils.getStr(params.get(HOST_GROUP_ID1));
        Map<String, Object> requestbody = new HashMap<>();
        String url = DmeConstants.HOSTGROUP_REMOVE.replace("{hostgroup_id}", hostGroupId);
        LOG.info("Request the interface to delete the host group,request url:{}",url);
        ResponseEntity responseEntity = dmeAccessService.access(url, HttpMethod.DELETE, null);
        LOG.info("Delete host group interface response data:{}", responseEntity +""+responseEntity.getStatusCode());
        return responseEntity;
    }

    // DME侧删除磁盘 获取任务ID
    private String volumeDeleteGetTaskId(Map<String, Object> params) {
        String taskId = "";
        try {
            ResponseEntity responseEntity = volumeDelete(params);
            taskId = getTaskId(responseEntity);
        } catch (DmeException e) {
            LOG.error(e.getMessage());
        }
        return taskId;
    }

    private ResponseEntity hostUnmapping(Map<String, Object> params) throws DmeException {
        String hostId = ToolUtils.getStr(params.get(HOST_ID));
        Object volumeIds = params.get(VOLUMEIDS);
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put(HOST_ID, hostId);
        requestbody.put(VOLUME_IDS, volumeIds);
        ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_HOST_UNMAPPING_URL, HttpMethod.POST,
                gson.toJson(requestbody));
        return responseEntity;
    }

    private ResponseEntity hostGroupUnmapping(Map<String, Object> params) throws DmeException {
        String hostGroupId = ToolUtils.getStr(params.get(HOST_GROUP_ID1));
        Object volumeIds = params.get(VOLUMEIDS);
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put(HOST_GROUP_ID1, hostGroupId);
        requestbody.put(VOLUME_IDS, volumeIds);
        LOG.info("host group unmapping request body:{}", gson.toJson(requestbody));
        ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.HOSTGROUP_UNMAPPING, HttpMethod.POST,
                gson.toJson(requestbody));
        return responseEntity;
    }

    private ResponseEntity volumeDelete(Map<String, Object> params) throws DmeException {
        Object volumeIds = params.get(VOLUMEIDS);
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put(VOLUME_IDS, volumeIds);
        ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_VOLUME_DELETE_URL, HttpMethod.POST,
                gson.toJson(requestbody));
        return responseEntity;
    }

    private Map<String, DmeVmwareRelation> getDvrMap(List<DmeVmwareRelation> dvrlist) {
        // 整理关系表数据
        Map<String, DmeVmwareRelation> remap = null;
        if (dvrlist != null && dvrlist.size() > 0) {
            remap = new HashMap<>();
            for (DmeVmwareRelation dvr : dvrlist) {
                remap.put(dvr.getStoreId(), dvr);
            }
        }
        return remap;
    }

    private Map<String, String> getStorNameMap(List<Storage> list) {
        // 整理存储信息
        Map<String, String> remap = null;
        if (list != null && list.size() > 0) {
            remap = new HashMap<>();
            for (Storage sr : list) {
                remap.put(sr.getId(), sr.getName());
            }
        }

        return remap;
    }

    public void setDmeVmwareRalationDao(DmeVmwareRalationDao dmeVmwareRalationDao) {
        this.dmeVmwareRalationDao = dmeVmwareRalationDao;
    }

    public void setDmeAccessService(DmeAccessService dmeAccessService) {
        this.dmeAccessService = dmeAccessService;
    }

    public void setDataStoreStatisticHistoryService(DataStoreStatisticHistoryService dataStoreStatisticHistoryService) {
        this.dataStoreStatisticHistoryService = dataStoreStatisticHistoryService;
    }

    public void setDmeStorageService(DmeStorageService dmeStorageService) {
        this.dmeStorageService = dmeStorageService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    private String getTaskId(ResponseEntity responseEntity) {
        String taskId = "";
        if (responseEntity != null) {
            if (RestUtils.RES_STATE_I_202 == responseEntity.getStatusCodeValue()) {
                Object hostGroupBody = responseEntity.getBody();
                if (hostGroupBody != null) {
                    JsonObject hostJson = new JsonParser().parse(hostGroupBody.toString()).getAsJsonObject();
                    taskId = ToolUtils.jsonToStr(hostJson.get(TASK_ID));
                }
            }
        }
        return taskId;
    }

    @Override
    public List<Map<String, Object>> getHostsByStorageId(String storageId) throws DmeException {
        List<Map<String, Object>> hostMapList = new ArrayList<>(DEFAULT_LEN);

        // 先查询vcenter侧的主机
        String listStr = vcsdkUtils.getHostsByDsObjectId(storageId, true);

        // 获取已挂载的集群，找到对应的主机，用于排除主机
        List<Map<String, Object>> clustermaps = getHostGroupsByStorageId(storageId);
        Map<String, String> excludehostmap = new HashMap<>();
        for (Map<String, Object> clustermap : clustermaps) {
            String clusterid = String.valueOf(clustermap.get(HOST_GROUP_ID));
            String vmwarehosts = vcsdkUtils.getHostsOnCluster(clusterid);
            if (!StringUtils.isEmpty(vmwarehosts)) {
                List<Map<String, String>> vmwarehostlists = gson.fromJson(vmwarehosts,
                        new TypeToken<List<Map<String, String>>>() {
                        }.getType());

                for (Map<String, String> vmwarehostmap : vmwarehostlists) {
                    excludehostmap.put(vmwarehostmap.get(HOSTID), "true");
                }
            }
        }
        if (!StringUtils.isEmpty(listStr)) {
            List<Map<String, String>> hosts = gson.fromJson(listStr,
                    new TypeToken<List<Map<String, String>>>() {
                    }.getType());

            // vcenter侧主机启动器是否和dem侧主机启动器一致
            for (Map<String, String> host : hosts) {
                String hostId = ToolUtils.getStr(host.get(HOSTID));

                // 排除已挂载在集群中的主机
                if (excludehostmap.get(hostId) == null) {
                    String hostNmme = ToolUtils.getStr(host.get(HOST_NAME));
                    String initiatorId = checkToHost(hostId);
                    if (!StringUtils.isEmpty(initiatorId)) {
                        Map<String, Object> tempMap = new HashMap<>();
                        tempMap.put(HOSTID, hostId);
                        tempMap.put(HOST_NAME, hostNmme);
                        hostMapList.add(tempMap);
                    }
                }
            }
        }
        return hostMapList;
    }

    @Override
    public List<Map<String, Object>> getHostsByStorageId2(String storeId) throws DmeException {
        List<Map<String, Object>> hostMapList = new ArrayList<>(DEFAULT_LEN);

        // 先查询vcenter侧的主机
        String listStr = vcsdkUtils.getHostsByDsObjectId(storeId, true);

        // 获取已挂载的集群，找到对应的主机，用于排除主机
        List<Map<String, Object>> clustermaps = getHostGroupsByStorageId2(storeId);
        Map<String, String> excludehostmap = new HashMap<>();
        for (Map<String, Object> clustermap : clustermaps) {
            String clusterid = String.valueOf(clustermap.get(HOST_GROUP_ID));
            String vmwarehosts = vcsdkUtils.getHostsOnCluster(clusterid);
            if (!StringUtils.isEmpty(vmwarehosts)) {
                List<Map<String, String>> vmwarehostlists = gson.fromJson(vmwarehosts,
                        new TypeToken<List<Map<String, String>>>() {
                        }.getType());

                for (Map<String, String> vmwarehostmap : vmwarehostlists) {
                    excludehostmap.put(vmwarehostmap.get(HOSTID), "true");
                }
            }
        }
        if (!StringUtils.isEmpty(listStr)) {
            List<Map<String, String>> hosts = gson.fromJson(listStr,
                    new TypeToken<List<Map<String, String>>>() {
                    }.getType());

            // vcenter侧主机启动器是否和dem侧主机启动器一致
            for (Map<String, String> host : hosts) {
                String hostId = ToolUtils.getStr(host.get(HOSTID));

                // 排除已挂载在集群中的主机
                if (excludehostmap.get(hostId) == null) {
                    String hostNmme = ToolUtils.getStr(host.get(HOST_NAME));
                    String initiatorId = checkToHost(hostId);
                    if (!StringUtils.isEmpty(initiatorId)) {
                        Map<String, Object> tempMap = new HashMap<>();
                        tempMap.put(HOSTID, hostId);
                        tempMap.put(HOST_NAME, hostNmme);
                        hostMapList.add(tempMap);
                    }
                }
            }
        }
        return hostMapList;
    }

    public List<Map<String, Object>> getHostsByStorageIdAllinitiators(String storeId, Map<String, List<Map<String, Object>>> allinitiators) throws DmeException {
        List<Map<String, Object>> hostMapList = new ArrayList<>(DEFAULT_LEN);

        Map<String, String> hostinitonatormap = new HashMap<>();
        // 先查询vcenter侧的主机
        String listStr = vcsdkUtils.getHostsByDsObjectId(storeId, true);

        // 获取已挂载的集群，找到对应的主机，用于排除主机
        List<Map<String, Object>> clustermaps = getHostGroupsByStorageId2(storeId);
        Map<String, String> excludehostmap = new HashMap<>();
        for (Map<String, Object> clustermap : clustermaps) {
            String clusterid = String.valueOf(clustermap.get(HOST_GROUP_ID));
            String vmwarehosts = vcsdkUtils.getHostsOnCluster(clusterid);
            if (!StringUtils.isEmpty(vmwarehosts)) {
                List<Map<String, String>> vmwarehostlists = gson.fromJson(vmwarehosts,
                        new TypeToken<List<Map<String, String>>>() {
                        }.getType());

                for (Map<String, String> vmwarehostmap : vmwarehostlists) {
                    excludehostmap.put(vmwarehostmap.get(HOSTID), "true");
                }
            }
        }
        if (!StringUtils.isEmpty(listStr)) {
            List<Map<String, String>> hosts = gson.fromJson(listStr,
                    new TypeToken<List<Map<String, String>>>() {
                    }.getType());

            // vcenter侧主机启动器是否和dem侧主机启动器一致
            for (Map<String, String> host : hosts) {
                String hostId = ToolUtils.getStr(host.get(HOSTID));

                // 排除已挂载在集群中的主机
                if (excludehostmap.get(hostId) == null) {
                    String hostName = ToolUtils.getStr(host.get(HOST_NAME));
                    String initiatorId = "";
                    if (hostinitonatormap.get(hostId) == null) {
                        initiatorId = checkToHostAllInitiators(hostId, allinitiators);
                        hostinitonatormap.put(hostId, initiatorId);
                    } else {
                        initiatorId = hostinitonatormap.get(hostId);
                    }

                    if (!StringUtils.isEmpty(initiatorId)) {
                        Map<String, Object> tempMap = new HashMap<>();
                        tempMap.put(HOSTID, hostId);
                        tempMap.put(HOST_NAME, hostName);
                        hostMapList.add(tempMap);
                    }
                }
            }
        }
        return hostMapList;
    }


    @Override
    public List<Map<String, Object>> getHostGroupsByStorageId(String storageId) throws DmeException {
        List<Map<String, Object>> hostGroupMapList = new ArrayList<>();

        // 取得vcenter中的所有cluster
        DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(storageId);
        if (dvr == null) {
            throw new DmeException("存储关联关系为空");
        }
        List<String> hostgroupids = getDmeAttachHostGroupByVolumeId(dvr.getVolumeId());
        Map<String, String> mappeddmegroups = new HashMap<>();
        for (String hostgroupid : hostgroupids) {
            Map<String, Object> hostgroupmap = dmeAccessService.getDmeHostGroup(hostgroupid);
            mappeddmegroups.put(String.valueOf(hostgroupmap.get(NAME_FIELD)), "has");
        }
        String listStr = vcsdkUtils.getMountClustersByDsObjectId(storageId, mappeddmegroups);
        if (!StringUtils.isEmpty(listStr)) {
            List<Map<String, String>> clusters = gson.fromJson(listStr,
                    new TypeToken<List<Map<String, String>>>() {
                    }.getType());

            // vcenter侧主机启动器是否和dem侧主机启动器一致
            for (Map<String, String> cluster : clusters) {
                String clusterId = ToolUtils.getStr(cluster.get(CLUSTER_ID));
                String clusterNmme = ToolUtils.getStr(cluster.get("clusterName"));
                String initiatorId = checkToHostGroup(clusterId);
                if (!StringUtils.isEmpty(initiatorId)) {
                    Map<String, Object> tempMap = new HashMap<>();
                    tempMap.put(HOST_GROUP_ID, clusterId);
                    tempMap.put("hostGroupName", clusterNmme);
                    hostGroupMapList.add(tempMap);
                }
            }
        }
        return hostGroupMapList;
    }

    @Override
    public List<Map<String, Object>> getHostGroupsByStorageId2(String storeId) throws DmeException {
        List<Map<String, Object>> hostGroupMapList = new ArrayList<>();

        // 取得vcenter中的所有cluster
        DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(storeId);
        if (dvr == null) {
            throw new DmeException("存储关联关系为空");
        }
        // DME 主机组
        List<String> hostgroupids = getDmeAttachHostGroupByVolumeId(dvr.getVolumeId());
        Map<String, String> mappeddmegroups = new HashMap<>();
        for (String hostgroupid : hostgroupids) {
            Map<String, Object> hostgroupmap = dmeAccessService.getDmeHostGroup(hostgroupid);
            mappeddmegroups.put(String.valueOf(hostgroupmap.get(NAME_FIELD)), "has");
        }
        String listStr = vcsdkUtils.getMountClustersByDsObjectId(storeId, mappeddmegroups);
        if (!StringUtils.isEmpty(listStr)) {
            List<Map<String, String>> clusters = gson.fromJson(listStr,
                    new TypeToken<List<Map<String, String>>>() {
                    }.getType());

            // 通过名字判断主机组是否一致
            for (Map<String, String> cluster : clusters) {
                String clusterId = ToolUtils.getStr(cluster.get(CLUSTER_ID));
                String clusterNmme = ToolUtils.getStr(cluster.get("clusterName"));
                List<String> volumeIds = new ArrayList<>();
                volumeIds.add(dvr.getVolumeId());
                /*Map<String, String> hostGroupMappingOrientedVolume =
                    getHostGroupMappingOrientedVolume(clusterId, volumeIds);*/
                if (hostgroupids.size() != 0) {
                    Map<String, Object> tempMap = new HashMap<>();
                    tempMap.put(HOST_GROUP_ID, clusterId);
                    tempMap.put("hostGroupName", clusterNmme);
                    hostGroupMapList.add(tempMap);
                }
            }
        }
        return hostGroupMapList;
    }

    private List<Map<String, String>> getHostgroupsByVolumeId(String volumeId) throws DmeException {
        List<Map<String, String>> hostGroupIds = new ArrayList<>();
        List<String> hostgroupids = getDmeAttachHostGroupByVolumeId(volumeId);
        for (String hostgroupid : hostgroupids) {
            Map<String, String> mappeddmegroups = new HashMap<>();
            Map<String, Object> hostgroupmap = dmeAccessService.getDmeHostGroup(hostgroupid);
            mappeddmegroups.put(String.valueOf(hostgroupmap.get(ID_FIELD)), String.valueOf(hostgroupmap.get(NAME_FIELD)));
            hostGroupIds.add(mappeddmegroups);
        }
        return hostGroupIds;
    }

    private List<String> getDmeAttachHostGroupByVolumeId(String volumeId) throws DmeException {
        String url;
        List<String> groupids = new ArrayList<>();
        url = DmeConstants.DME_VOLUME_BASE_URL + FIEL_SEPARATOR + volumeId;
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        int code = responseEntity.getStatusCodeValue();
        if (code != HttpStatus.OK.value()) {
            throw new DmeException("search host id error");
        }
        String object = responseEntity.getBody();
        JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
        JsonObject volume = jsonObject.get(VOLUME_FIELD).getAsJsonObject();
        JsonArray attachments = volume.get("attachments").getAsJsonArray();
        for (JsonElement jsonElement : attachments) {
            JsonObject element = jsonElement.getAsJsonObject();
            String attachedHostGroupId = ToolUtils.jsonToStr(element.get("attached_host_group"));
            if (!"".equalsIgnoreCase(attachedHostGroupId) && !groupids.contains(attachedHostGroupId)) {
                groupids.add(attachedHostGroupId);
            }
        }

        return groupids;
    }

    // 通过vcenter的主机ID 查询dme侧的主机信息
    private Map<String, Object> getDmeHostByHostObjeId(String hostObjId, Map<String, List<Map<String, Object>>> allinitiators) throws DmeException {
        Map<String, Object> hostInfo = new HashMap<>();
        List<Map<String, Object>> hbalists = vcsdkUtils.getHbaByHostObjectId(hostObjId);
        if (hbalists == null || hbalists.size() == 0) {
            return hostInfo;
        }
        for (Map<String, Object> hbaMap:hbalists){
            String initiatorName = ToolUtils.getStr(hbaMap.get(NAME_FIELD));
            // 取出DME所有主机
            List<Map<String, Object>> hostlist = dmeAccessService.getDmeHosts(null);
            if (hostlist == null || hostlist.size() == 0) {
                return hostInfo;
            }
            for (Map<String, Object> hostmap : hostlist) {
                if (hostmap != null && hostmap.get(ID_FIELD) != null) {
                    // 通过主机ID查到对应的主机的启动器
                    String demHostId = ToolUtils.getStr(hostmap.get(ID_FIELD));

                    // 得到主机的启动器
                    //List<Map<String, Object>> initiators = dmeAccessService.getDmeHostInitiators(demHostId);
                    List<Map<String, Object>> initiators = allinitiators.get(demHostId);
                    if (initiators != null && initiators.size() > 0) {
                        for (Map<String, Object> inimap : initiators) {
                            String portName = ToolUtils.getStr(inimap.get(PORT_NAME));
                            if (initiatorName.equals(portName)) {
                                hostInfo = hostmap;
                                break;
                            }
                        }
                    }
                }
                // 如果已经找到的主机就不再循环
                if (hostInfo.size() > 0) {
                    break;
                }
            }
            // 如果已经找到的主机就不再循环
            if (hostInfo.size() > 0) {
                break;
            }
        }
        return hostInfo;
    }

    // 通过vcenter的主机ID 查询dme侧的主机信息
    private Map<String, Map<String, Object>> getDmeHostByHostObjeId2(List<String> hostObjIds) throws DmeException {

        Map<String, Object> hostInfo = new HashMap<>();
        Map<String, Map<String, Object>> hostMap = new HashMap<>();
        // 查询所有启动器
        Map<String, List<Map<String, Object>>> allinitionators = getAllInitionator();
        if (CollectionUtils.isEmpty(allinitionators)) {
            return hostMap;
        }
        for (String hostObjId : hostObjIds) {
            List<Map<String, Object>> hbalists = vcsdkUtils.getHbaByHostObjectId(hostObjId);
            for (Map<String, Object> hbaMap : hbalists) {
                if (CollectionUtils.isEmpty(hbaMap)) {
                    LOG.error("query host hba is null!{}", hostObjId);
                    continue;
                }
                String initiatorName = ToolUtils.getStr(hbaMap.get(NAME_FIELD));
                for (Map.Entry<String, List<Map<String, Object>>> entry : allinitionators.entrySet()) {
                    String dmehostId = "";
                    for (Map<String, Object> map : entry.getValue()) {
                        String portName = ToolUtils.getStr(map.get(PORT_NAME));
                        if (initiatorName.equalsIgnoreCase(portName)) {
                            hostInfo = map;
                            dmehostId = entry.getKey();
                            LOG.info("the esxi host corresponding to vcenter was found in DME.");
                            break;
                        }
                    }
                    if (!StringUtils.isEmpty(dmehostId)) {
                        hostMap.put(dmehostId, hostInfo);
                        LOG.info("the esxi host corresponding to vcenter was found in DME{}", gson.toJson(hostMap));
                        break;
                    }
                }
                if (!CollectionUtils.isEmpty(hostMap)) {
                    break;
                }
            }
        }
        return hostMap;
    }

    public boolean isVmfs(String objectId) throws DmeSqlException {
        List<DmeVmwareRelation> dvrlist = dmeVmwareRalationDao.getDmeVmwareRelation(DmeConstants.STORE_TYPE_VMFS);
        for (DmeVmwareRelation dmeVmwareRelation : dvrlist) {
            if (dmeVmwareRelation.getStoreId().equalsIgnoreCase(objectId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<VmfsDataInfo> queryVmfs(String dsObjId) throws Exception {
        List<VmfsDataInfo> relists = null;
        List<DmeVmwareRelation> dvrlist;
        try {
            // 从关系表中取得DME卷与vcenter存储的对应关系
            dvrlist = dmeVmwareRalationDao.getDmeVmwareRelation(DmeConstants.STORE_TYPE_VMFS);
        } catch (DmeSqlException ex) {
            LOG.error("query vmfs error:", ex);
            throw ex;
        }

        if (dvrlist.size() == 0) {
            LOG.info("Vmfs listStr=null");
            return relists;
        }

        // 整理数据
        Map<String, DmeVmwareRelation> dvrMap = getDvrMap(dvrlist);

        // 取得所有的存储设备
        List<Storage> storagemap = dmeStorageService.getStorages(null);

        // 整理数据
        Map<String, String> stoNameMap = getStorNameMap(storagemap);

        // 取得vcenter中的所有vmfs存储。
        String listStr = vcsdkUtils.getAllVmfsDataStoreInfos(DmeConstants.STORE_TYPE_VMFS);
        if (StringUtils.isEmpty(listStr)) {
            return relists;
        }
        JsonArray jsonArray = new JsonParser().parse(listStr).getAsJsonArray();
        if (jsonArray != null && jsonArray.size() > 0) {
            relists = new ArrayList<>();
            for (int index = 0; index < jsonArray.size(); index++) {
                JsonObject jo = jsonArray.get(index).getAsJsonObject();

                String vmwareStoreobjectid = ToolUtils.jsonToStr(jo.get(OBJECTID));
                if (!StringUtils.isEmpty(vmwareStoreobjectid) && vmwareStoreobjectid.equals(dsObjId)) {
                    // 对比数据库关系表中的数据，只显示关系表中的数据
                    if (dvrMap != null && dvrMap.get(vmwareStoreobjectid) != null) {
                        VmfsDataInfo vmfsDataInfo = new VmfsDataInfo();
                        double capacity = ToolUtils.getDouble(jo.get(CAPACITY)) / ToolUtils.GI;
                        double freeSpace = ToolUtils.getDouble(jo.get("freeSpace")) / ToolUtils.GI;
                        double uncommitted = ToolUtils.getDouble(jo.get("uncommitted")) / ToolUtils.GI;

                        vmfsDataInfo.setName(ToolUtils.jsonToStr(jo.get(NAME_FIELD)));
                        vmfsDataInfo.setCapacity(capacity);
                        vmfsDataInfo.setFreeSpace(freeSpace);
                        vmfsDataInfo.setReserveCapacity(capacity + uncommitted - freeSpace);
                        vmfsDataInfo.setObjectid(ToolUtils.jsonToStr(jo.get(OBJECTID)));

                        DmeVmwareRelation dvr = dvrMap.get(vmwareStoreobjectid);
                        String volumeId = dvr.getVolumeId();

                        // 这里由于DME系统中的卷太多。是分页查询，所以需要vmfs一个个的去查DME系统中的卷。
                        getVolumeDetail(relists, stoNameMap, vmfsDataInfo, volumeId);
                    }
                }
            }
        }
        return relists;
    }

    private void getVolumeDetail(List<VmfsDataInfo> relists, Map<String, String> stoNameMap, VmfsDataInfo vmfsDataInfo,
                                 String volumeId) {
        String detailedVolumeUrl = DmeConstants.DME_VOLUME_BASE_URL + FIEL_SEPARATOR + volumeId;
        try {
            ResponseEntity responseEntity = dmeAccessService.access(detailedVolumeUrl, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                JsonObject voljson = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                JsonObject vjson2 = voljson.getAsJsonObject(VOLUME_FIELD);

                vmfsDataInfo.setVolumeId(ToolUtils.jsonToStr(vjson2.get(ID_FIELD)));
                vmfsDataInfo.setVolumeName(ToolUtils.jsonToStr(vjson2.get(NAME_FIELD)));
                vmfsDataInfo.setStatus(ToolUtils.jsonToStr(vjson2.get("status")));
                vmfsDataInfo.setServiceLevelName(ToolUtils.jsonToStr(vjson2.get(SERVICE_LEVEL_NAME)));
                vmfsDataInfo.setVmfsProtected(ToolUtils.jsonToBoo(vjson2.get("protected")));
                vmfsDataInfo.setWwn(ToolUtils.jsonToStr(vjson2.get(VOLUME_WWN)));

                String storageId = ToolUtils.jsonToStr(vjson2.get(STORAGE_ID));
                vmfsDataInfo.setDeviceId(storageId);
                vmfsDataInfo.setDevice(stoNameMap == null ? "" : stoNameMap.get(storageId));

                parseTuning(vmfsDataInfo, vjson2);
                relists.add(vmfsDataInfo);
            }
        } catch (DmeException e) {
            LOG.error("DME link error url:{},error:{}", detailedVolumeUrl, e.getMessage());
        }
    }

    @Override
    public boolean queryDatastoreByName(String name) {
        boolean isFlag = true;
        try {
            String dataStoreName = dmeVmwareRalationDao.getDataStoreByName(name);
            if (!StringUtils.isEmpty(dataStoreName)) {
                isFlag = false;
            }
        } catch (DmeSqlException e) {
            isFlag = false;
        }
        return isFlag;
    }

    // 查询dataStrore上的vm 看是否关联了指定的主机或集群
    private boolean dataStoreVmRelateHostOrCluster(String dsObjid, String hostObjId, String clusterObjId) {
        // 1 查询datastore上的vm
        // 2 vm是否关联了hostObjId 或 clusterObjId (objId转id?)
        return false;
    }

    private String getStorageModel(String storageId) throws DmeException {
        StorageDetail storageDetail = dmeStorageService.getStorageDetail(storageId);
        return storageDetail.getModel() + " " + storageDetail.getProductVersion();
    }

    private String getStorageModelByWwn(String wwn) throws DmeSqlException {
        return dmeVmwareRalationDao.getStorageModelByWwn(wwn);
    }


    //  ________________________________________________________________________________________________________________________________________________________________________________

    /**
     * @throws DmeException
     * @Description: 创建Vmfs, 支持多主机，多集群，集群和主机的搭配创建
     * @author yc
     * @Date 2021/5/13 17:23
     */
    @Override
    public CreateVmfsResponse createVmfsNew(Map<String, Object> params) throws DmeException {
        //1.参数准备
        if (CollectionUtils.isEmpty(params)) {
            throw new DmeException("create vmfs params is null");
        }
        //根据前端入参判断前端选择的创建方式
        Object ss = params.get(CHOOSEDEVICE);
        if (StringUtils.isEmpty(params.get(CHOOSEDEVICE))) {
            throw new DmeException("create vmfs the params is error");
        }
        // 根据服务等级ID获取对应的存储设备ID
        Object storageId = params.get(STORAGE_ID);
        if (StringUtils.isEmpty(storageId) && params.get(DmeConstants.SERVICELEVELID) != null) {
            storageId =
                    dmeStorageService.getStorageByServiceLevelId(String.valueOf(params.get(DmeConstants.SERVICELEVELID)));
            params.put(STORAGE_ID, storageId);
        }
        List<Map<String, String>> chooseDevicelists = gson.fromJson(gson.toJsonTree(ss),
                new TypeToken<List<Map<String, String>>>() {
                }.getType());
        //获取vmfs的创建方式
        HashSet<String> deviceTypeSet = getDeviceTypeSet(chooseDevicelists);
        //获取启动器
        List<String> connectionFailList = new ArrayList<>();
        int failnum = 0;
        int successnum = 0;
        String descriptionEN = null;
        String descriptionCN = null;
        //todo 将主机创建和主机组的创建独立开来，针对于主机，创建vmfs之前进行联通性校验，校验通过的继续，不通过的返回不通过的主机名称和状态
        //2.1根据主机创建vmfs
        if (deviceTypeSet.contains("host")) {
            //a.首先检测主机的联通性
            Map<String, List<Map<String, Object>>> allinitionators = getAllInitionator();
            Map<String, List<String>> hostStatusMap = checkHostNew(params, allinitionators, chooseDevicelists,null);
            //b.组装检测联通性失败的主机信息
            if (!CollectionUtils.isEmpty(hostStatusMap.get("failHost"))) {
                hostStatusMap.get("failHost").forEach(hostid -> {
                    String hostname = getDmeHostNameByIdNew(hostid);
                    connectionFailList.add(hostname);
                });
            }
            if (CollectionUtils.isEmpty(hostStatusMap.get("nomalHost"))) {
                throw new DmeException("DME create vmfs volume fail(all connection of hosts is lossing)!");
            }
            //c.创建Lun
            String taskId = createLun(params);
            if (StringUtils.isEmpty(taskId)) {
                throw new DmeException("create vmfs error,taskid is empty");
            }
            //d.根据任务id判断创建Lun的任务状态
            TasksResultObject tasksResult = taskService.checkTaskStatusNew(taskId, longTaskTimeOut);
            LOG.info("create vms isCreated" + tasksResult.toString());
            if (StringUtils.isEmpty(tasksResult)) {
                throw new DmeException("DME create vmfs volume error(task status is failure)!");
            }
            //e.获取卷数据信息集合
            List<Map<String, Object>> volumelist = getVolumeByName(ToolUtils.getStr(params.get(VOLUMENAME)),
                    null, null, ToolUtils.getStr(params.get(SERVICE_LEVEL_ID)),
                    ToolUtils.getStr(params.get(STORAGE_ID)), ToolUtils.getStr(params.get(POOL_RAW_ID)));
            LOG.info("create vms volumelist size=" + volumelist.size());
            //f.映射主机
            List<String> volumeIdList = new ArrayList<>();
            volumelist.forEach(volumeMap -> {
                volumeIdList.add(ToolUtils.getStr(volumeMap.get("volume_id")));
            });
            //TODO 将lun映射给主机的时候，接口支持将多个lun映射给同一台主机，这样就会出现一个争议（如果部分成功这个失败的lun就不能删除），如果单个循环lun去映射单个主机，就会出现批量创建时的效率问题
            // (最后的结论，多个lun映射给多个主机，一个不成功直接认为隐射失败)
            List<String> objHostIds = hostStatusMap.get("nomalHost");
            Map<String, Map<String, List<String>>> mappingResultMapNew = lunMappingToHostOrHostgroupForCreate(volumeIdList, objHostIds, null);
            //todo 20210623修改逻辑获取所有主机映射的volumid的交集（映射成功的），其他为非成功的
            //获取全部映射成功的volumid集合
            List<String> allMappedVolumids = getAllMappedVolumids(mappingResultMapNew,"hostMapped");
            //组装全部映射成功的volum集合
            List<Map<String, Object>> allMappedvolumelist = getAllMappedVolums(volumelist, allMappedVolumids);
            List<String> successList = createOnVmware2New(params, allMappedvolumelist);
            successnum = successList.size();
            //解除每台主机已经映射但没有挂载成功的lun
            unmappingLunFromEveryHost(mappingResultMapNew,successList,"hostMapped");
            //删除没有创建成功的lun
            volumeIdList.removeAll(successList);
            failnum = volumeIdList.size();
            if (!CollectionUtils.isEmpty(volumeIdList)) {
                deleteFailLun(volumeIdList);
            }            /*//检查映射结果，如果该lun未映射给全部主机，直接认为隐射直接删除lun
            if (!CollectionUtils.isEmpty(mappingResultMap.get("hostUnmapped"))) {
                rollBackHostNew(volumeIdList, objHostIds, null, true, true);
                failnum = volumeIdList.size();
                if (!CollectionUtils.isEmpty(mappingResultMap.get("descriptionEN"))) {
                    descriptionEN = mappingResultMap.get("descriptionEN").get(0);
                }
                if (!CollectionUtils.isEmpty(mappingResultMap.get("descriptionCN"))) {
                    descriptionCN = mappingResultMap.get("descriptionCN").get(0);
                }
            } else {
                createOnVmware2New(params, volumelist);
                successnum = volumeIdList.size();
            }*/
        }
        //2.2根据集群创建vmfs
        if (deviceTypeSet.contains("cluster")) {
            //a. 首先获取前端入参判断主机组是否已经存在，不存在就创建新的主机组，并且校验其联通性
            Map<String, List<String>> maps = checkOrCreateToHostGroupNew(params, chooseDevicelists,null);
            //b. 如果集合为空，联通性校验失败，直接抛出异常
            if (CollectionUtils.isEmpty(maps)) {
                throw new DmeException("check connectivity of hostgroup is error");
            }
            //c. 创建lun
            String taskId = createLun(params);
            if (StringUtils.isEmpty(taskId)) {
                throw new DmeException("create vmfs error,taskid is empty");
            }
            //d.根据任务id判断创建Lun的任务状态
            TasksResultObject tasksResult = taskService.checkTaskStatusNew(taskId, longTaskTimeOut);
            LOG.info("create vms isCreated" + tasksResult.toString());
            if (StringUtils.isEmpty(tasksResult)) {
                throw new DmeException("DME create vmfs volume error(task status is failure)!");
            }
            //e.获取卷数据信息集合
            List<Map<String, Object>> volumelist = getVolumeByName(ToolUtils.getStr(params.get(VOLUMENAME)),
                    null, null, ToolUtils.getStr(params.get(SERVICE_LEVEL_ID)),
                    ToolUtils.getStr(params.get(STORAGE_ID)), ToolUtils.getStr(params.get(POOL_RAW_ID)));
            LOG.info("create vms volumelist size=" + volumelist.size());
            //f.将创建的lun映射给主机组
            List<String> volumeIdList = new ArrayList<>();
            volumelist.forEach(volumeMap -> {
                volumeIdList.add(ToolUtils.getStr(volumeMap.get("volume_id")));
            });
            List<String> hostGroupIds = maps.get("nomalCluster");
            String createFlag = maps.get("flag").get(0);
            boolean isCreate = ("1".equalsIgnoreCase(createFlag));
           /* if (createFlag.equalsIgnoreCase("0")) {
                isCreate = false;
            } else {
                isCreate = false;
            }*/
            Map<String, Map<String, List<String>>> mappingResultMapNew = lunMappingToHostOrHostgroupForCreate(volumeIdList, null, hostGroupIds);
            //获取全部映射成功的volumid集合
            List<String> allMappedVolumids = getAllMappedVolumids(mappingResultMapNew,"clusterMapped");
            //组装全部映射成功的volum集合
            List<Map<String, Object>> allMappedvolumelist = getAllMappedVolums(volumelist, allMappedVolumids);
            List<String> successList = createOnVmware2New(params, allMappedvolumelist);
            successnum = successList.size();
            //解除每台主机已经映射但没有挂载成功的lun
            unmappingLunFromEveryHostGroup(mappingResultMapNew,successList,"clusterMapped");
            //删除没有创建成功的lun
            volumeIdList.removeAll(successList);
            if (!CollectionUtils.isEmpty(volumeIdList)) {
                deleteFailLun(volumeIdList);
            }
            failnum = volumeIdList.size();
            //需要删除创建的主机组
            if (successnum == 0 && isCreate && !CollectionUtils.isEmpty(hostGroupIds)){
                for (String demHostGroupId : hostGroupIds) {
                    deleteHostgroup(demHostGroupId);
                }
            }




          /*  //g. 检查隐射结果，如果隐射失败，直接删除lun
            //检查映射结果，如果该lun未映射给全部主机，直接认为隐射直接删除lun
            if (!CollectionUtils.isEmpty(mappingResultMap.get("clusterUnmapped"))) {
                rollBackHostNew(volumeIdList, null, hostGroupIds, isCreate, true);
                failnum = volumeIdList.size();
                if (!CollectionUtils.isEmpty(mappingResultMap.get("descriptionEN"))) {
                    descriptionEN = mappingResultMap.get("descriptionEN").get(0);
                }
                if (!CollectionUtils.isEmpty(mappingResultMap.get("descriptionCN"))) {
                    descriptionCN = mappingResultMap.get("descriptionCN").get(0);
                }
            } else {
                //h. 创建创建数据存储=
                createOnVmware2New(params, volumelist);
                successnum = volumeIdList.size();
            }*/
        }
        return new CreateVmfsResponse(successnum, failnum, connectionFailList, descriptionEN, descriptionCN);
    }
//____________________________________________________________________________________________________________________________________________________________
    /**
     * @throws DmeException
     * @Description: 创建Vmfs, 支持多主机，多集群，集群和主机的搭配创建
     * @author yc
     * @Date 2021/5/13 17:23
     */
    @Override
    public CreateVmfsResponse02 createVmfsNew1(Map<String, Object> params) throws DmeException {
        //1.参数准备
        if (CollectionUtils.isEmpty(params)) {
            throw new DmeException("params is null");
        }
        //根据前端入参判断前端选择的创建方式
        Object ss = params.get(CHOOSEDEVICE);
        if (StringUtils.isEmpty(params.get(CHOOSEDEVICE))) {
            throw new DmeException("params is error");
        }
        // 根据服务等级ID获取对应的存储设备ID
        Object storageId = params.get(STORAGE_ID);
        if (StringUtils.isEmpty(storageId) && params.get(DmeConstants.SERVICELEVELID) != null) {
            storageId =
                    dmeStorageService.getStorageByServiceLevelId(String.valueOf(params.get(DmeConstants.SERVICELEVELID)));
            params.put(STORAGE_ID, storageId);
        }
        List<Map<String, String>> chooseDevicelists = gson.fromJson(gson.toJsonTree(ss),
                new TypeToken<List<Map<String, String>>>() {
                }.getType());
        //获取vmfs的创建方式
        HashSet<String> deviceTypeSet = getDeviceTypeSet(chooseDevicelists);
        //获取启动器
        List<String> connectionFailList = new ArrayList<>();
        int failnum = 0;
        int successnum = 0;
        int partsuccessnum = 0;
        List<String> desc = new ArrayList<>();
        //todo 将主机创建和主机组的创建独立开来，针对于主机，创建vmfs之前进行联通性校验，校验通过的继续，不通过的返回不通过的主机名称和状态
        //2.1根据主机创建vmfs
        if (deviceTypeSet.contains("host")) {
            //a.首先检测主机的联通性
            Map<String, List<Map<String, Object>>> allinitionators = getAllInitionator();
            //准备dmehostid和vcenterhostid映射对象
            Map<String,String> objid2hostId = new HashMap<>();
            Map<String, List<String>> hostStatusMap = checkHostNew(params, allinitionators, chooseDevicelists, objid2hostId);
            //b.组装检测联通性失败的主机信息
            if (!CollectionUtils.isEmpty(hostStatusMap.get("failHost"))) {
                hostStatusMap.get("failHost").forEach(hostid -> {
                    String hostname = getDmeHostNameByIdNew(hostid);
                    connectionFailList.add(hostname);
                });
            }
            if (CollectionUtils.isEmpty(hostStatusMap.get("nomalHost"))) {
                return new CreateVmfsResponse02(successnum, ToolUtils.getInt(params.get("count")), connectionFailList, partsuccessnum);
            }
            //c.创建Lun
            String taskId = createLun(params);
            if (StringUtils.isEmpty(taskId)) {
                throw new DmeException("taskid is empty");
            }
            //d.根据任务id判断创建Lun的任务状态,并且返回该任务id下的所有明细信息
            List<TaskDetailInfoNew> taskDetailInfoNewList = taskService.getTaskInfo(taskId, longTaskTimeOut);
            LOG.info(gson.toJson(taskDetailInfoNewList));
            //首先获取主任务信息
            TaskDetailInfoNew mainTask = taskService.getMainTaskInfo(taskId, taskDetailInfoNewList);
            if (StringUtils.isEmpty(mainTask)){
                throw new DmeException("get main task info error");
            }else if (mainTask.getStatus()>4){
                throw new DmeException(mainTask.getDetailEn());
            }
            String mainId = mainTask.getId();
            if (StringUtils.isEmpty(mainId)){
                throw new DmeException("get task info error");
            }
            String id = getCreateMainChildernId(mainId,taskDetailInfoNewList);
            List<TaskDetailInfoNew> createTaskInfo = getCreateInfos(id,taskDetailInfoNewList);
            if (CollectionUtils.isEmpty(createTaskInfo)) {
                throw new DmeException(mainTask.getDetailEn());
            }
            //获取lun名称和对应的创建时间
            Map<String,String> lunNameAndTimeMap = getLunNameAndTimeMap(createTaskInfo);
            if (CollectionUtils.isEmpty(lunNameAndTimeMap)) {
                throw new DmeException("get task info error");
            }
            //循环根据lun名称获取lun信息
            List<Map<String, Object>> volumelistLst = new ArrayList<>();
            for (String lunName : lunNameAndTimeMap.keySet()) {
                Map<String, Object> volumInfo = null;
                List<Map<String, Object>> volumelist = getVolumeByNameNew(lunName,
                        null, null, ToolUtils.getStr(params.get(SERVICE_LEVEL_ID)),
                        ToolUtils.getStr(params.get(STORAGE_ID)), ToolUtils.getStr(params.get(POOL_RAW_ID)));
                if (!CollectionUtils.isEmpty(volumelist) && volumelist.size() == 1 ){
                    volumInfo = volumelist.get(0);
                }else if (!CollectionUtils.isEmpty(volumelist) && volumelist.size() > 1){
                    for (Map<String, Object> map : volumelist) {
                        if(!CollectionUtils.isEmpty(map) &&
                                ToolUtils.getDate(ToolUtils.getStr(lunNameAndTimeMap.get(lunName))).compareTo(ToolUtils.getDate(ToolUtils.getStr( map.get("created_at"))))==0){
                            volumInfo = map;
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(volumInfo)){
                    volumelistLst.add(volumInfo);
                }
            }
            if (CollectionUtils.isEmpty(volumelistLst)) {
                throw new DmeException("create vms error");
            }
            //f.映射主机
            List<String> volumeIdListTemp = new ArrayList<>();
            volumelistLst.forEach(volumeMap -> {
                volumeIdListTemp.add(ToolUtils.getStr(volumeMap.get("volume_id")));
            });
            List<String> volumeIdList = volumeIdListTemp.stream().filter(Objects::nonNull).collect(Collectors.toList());
            LOG.info("create vms volumelist size=" + volumeIdList.size());
            if (CollectionUtils.isEmpty(volumeIdList)) {
                throw new DmeException("get volumid error!");
            }
            List<String> objHostIds = hostStatusMap.get("nomalHost");
            Map<String, List<String>> mappingResultMapNew =  new HashMap<>();
            Map<String, List<String>> volumeidMapToHostOrGroup = new HashMap<>();
            List<String> successList = new ArrayList<>();
            mappingResultMapNew = lunMappingToHostOrHostgroupForCreate02(volumeIdList, objHostIds, null, volumelistLst, desc);
            //根据映射的并集去创建存储，挂载映射成功的主机
            //准volumeid和主机的对应关系
            volumeidMapToHostOrGroup = getVolumeidMapToHostOrGroup(mappingResultMapNew);
            Map<String, List<String>>  info  = getLogInfo(volumeidMapToHostOrGroup,chooseDevicelists,volumelistLst,objid2hostId);
            LOG.info(gson.toJson(info));
            List<String> mappedFail = getFailVolumid(volumeIdList, volumeidMapToHostOrGroup);
            if (!CollectionUtils.isEmpty(mappedFail)){
                deleteFailLun02(volumeIdList);
            }
            Map<String,String> dataStoreStrMap = new HashMap<>();
            successList = createOnVmware2New02(volumeidMapToHostOrGroup,mappingResultMapNew, volumelistLst, params, objid2hostId, dataStoreStrMap, HOST);
            LOG.info("create vmfs succesdList: size:"+successList.size()+", info:"+gson.toJson(successList));
            unmappingLunFromEveryHost02(volumeidMapToHostOrGroup, successList,HOST);
            if (!CollectionUtils.isEmpty(volumeidMapToHostOrGroup)) {
                deleteFailLun02(new ArrayList<>(volumeidMapToHostOrGroup.keySet()));
            }
            //挂载主机1.先扫描所有的主机上的存储信息2.执行挂载
            List<String> vcHostIds = getVcHost(objHostIds,objid2hostId);
            if (!CollectionUtils.isEmpty(vcHostIds) && !CollectionUtils.isEmpty(successList)) {
                for (String host : vcHostIds) {
                    vcsdkUtils.scanDataStore(null, host);
                    try {
                        TimeUnit.SECONDS.sleep(4);
                    }catch (Exception e){
                        LOG.error("scan datastore error");
                    }
                }
            }
            for (String volumid : successList) {
                String dataStoreStr = dataStoreStrMap.get(volumid);
                Map<String, Object> dataStoreMap = gson.fromJson(dataStoreStr,
                        new TypeToken<Map<String, Object>>() {
                        }.getType());
                String objid = ToolUtils.getStr(dataStoreMap.get("objectId"));
                List<Map<String, String>> mounted = vcsdkUtils.getHostsByDsObjectIdNew(objid, true);
                if (mounted.size()== chooseDevicelists.size()){
                    successnum+=1;
                }

            }
            int createNo = successList.size();
            if (!CollectionUtils.isEmpty(connectionFailList)){
                successnum = 0;
            }
            LOG.info("succesdList:"+gson.toJson(successList));
            partsuccessnum = createNo - successnum;
            failnum = ToolUtils.getInt(params.get("count")) - createNo;

        }
        //2.2根据集群创建vmfs
        if (deviceTypeSet.contains("cluster")) {
            //a. 首先获取前端入参判断主机组是否已经存在，不存在就创建新的主机组，并且校验其联通性
            Map<String,String> objid2hostId = new HashMap<>();
            Map<String, List<String>> maps = checkOrCreateToHostGroupNew(params, chooseDevicelists,objid2hostId);
            //b. 如果集合为空，联通性校验失败，直接抛出异常
            if (CollectionUtils.isEmpty(maps)) {
                throw new DmeException("check connectivity of hostgroup is error");
            }
            //c. 创建lun
            String taskId = createLun(params);
            if (StringUtils.isEmpty(taskId)) {
                throw new DmeException("taskid is empty");
            }
            //d.根据任务id判断创建Lun的任务状态,并且返回该任务id下的所有明细信息
            List<TaskDetailInfoNew> taskDetailInfoNewList = taskService.getTaskInfo(taskId, longTaskTimeOut);
            LOG.info(gson.toJson(taskDetailInfoNewList));

            //首先获取主任务信息
            TaskDetailInfoNew mainTask = taskService.getMainTaskInfo(taskId, taskDetailInfoNewList);
            if (StringUtils.isEmpty(mainTask)){
                throw new DmeException("get main task info error");
            }else if (mainTask.getStatus()>4){
                throw new DmeException(mainTask.getDetailEn());
            }
            String mainId = mainTask.getId();
            if (StringUtils.isEmpty(mainId)){
                throw new DmeException("get task info error");
            }
            String id = getCreateMainChildernId(mainId,taskDetailInfoNewList);
            List<TaskDetailInfoNew> createTaskInfo = getCreateInfos(id,taskDetailInfoNewList);
            if (CollectionUtils.isEmpty(createTaskInfo)) {
                throw new DmeException(mainTask.getDetailEn());
            }
            //获取lun名称和对应的创建时间
            Map<String,String> lunNameAndTimeMap = getLunNameAndTimeMap(createTaskInfo);
            if (CollectionUtils.isEmpty(lunNameAndTimeMap)) {
                throw new DmeException("get task info error");
            }
            //循环根据lun名称获取lun信息
            List<Map<String, Object>> volumelistLst = new ArrayList<>();
            for (String lunName : lunNameAndTimeMap.keySet()) {
                Map<String, Object> volumInfo = null;
                List<Map<String, Object>> volumelist = getVolumeByNameNew(lunName,
                        null, null, ToolUtils.getStr(params.get(SERVICE_LEVEL_ID)),
                        ToolUtils.getStr(params.get(STORAGE_ID)), ToolUtils.getStr(params.get(POOL_RAW_ID)));
                if (!CollectionUtils.isEmpty(volumelist) && volumelist.size() == 1 ){
                    volumInfo = volumelist.get(0);
                }else if (!CollectionUtils.isEmpty(volumelist) && volumelist.size() > 1){
                    for (Map<String, Object> map : volumelist) {
                        if(!CollectionUtils.isEmpty(map) &&
                                ToolUtils.getStr(lunNameAndTimeMap.get(lunName)).equalsIgnoreCase(ToolUtils.getStr( map.get("created_at")))){
                            volumInfo = map;
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(volumInfo)){
                    volumelistLst.add(volumInfo);
                }
            }
            LOG.info("create vms volumelist size=" + volumelistLst.size());
            if (CollectionUtils.isEmpty(volumelistLst)) {
                throw new DmeException("create vms error");
            }
            List<String> hostGroupIds = maps.get("nomalCluster");
            String createFlag = maps.get("flag").get(0);
            boolean isCreate = ("1".equalsIgnoreCase(createFlag));
            List<String> volumeIdListTemp = new ArrayList<>();
            volumelistLst.forEach(volumeMap -> {
                volumeIdListTemp.add(ToolUtils.getStr(volumeMap.get("volume_id")));
            });
            List<String> volumeIdList = volumeIdListTemp.stream().filter(Objects::nonNull).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(volumeIdList)) {
                throw new DmeException("get volumid error!");
            }
            Map<String, List<String>> mappingResultMapNew =  new HashMap<>();
            Map<String, List<String>> volumeidMapToHostOrGroup = new HashMap<>();
            List<String> successList = new ArrayList<>();
            mappingResultMapNew = lunMappingToHostOrHostgroupForCreate02(volumeIdList, null, hostGroupIds, volumelistLst, desc);
            volumeidMapToHostOrGroup = getVolumeidMapToHostOrGroup(mappingResultMapNew);
            Map<String, List<String>>  info  = getLogInfo(volumeidMapToHostOrGroup,chooseDevicelists,volumelistLst,objid2hostId);
            LOG.info(gson.toJson(info));
            List<String> mappedFail = getFailVolumid(volumeIdList, volumeidMapToHostOrGroup);
            if (!CollectionUtils.isEmpty(mappedFail)){
                deleteFailLun02(volumeIdList);
            }
            Map<String,String> dataStoreStrMap = new HashMap<>();
            successList = createOnVmware2New02(volumeidMapToHostOrGroup, mappingResultMapNew,volumelistLst, params, objid2hostId,dataStoreStrMap, CLUSTER);
            unmappingLunFromEveryHost02(volumeidMapToHostOrGroup, successList,CLUSTER);
            LOG.info(gson.toJson(successList));
            LOG.info(gson.toJson(volumeidMapToHostOrGroup));

            if (!CollectionUtils.isEmpty(volumeidMapToHostOrGroup)) {
                LOG.info(gson.toJson(new ArrayList<>(volumeidMapToHostOrGroup.keySet())));
                deleteFailLun02(new ArrayList<>(volumeidMapToHostOrGroup.keySet()));
            }
            successnum = successList.size();
           // partsuccessnum = volumelistLst.size()-successnum-volumeidMapToHostOrGroup.size();
            failnum = ToolUtils.getInt(params.get("count")) - successnum;

            //需要删除创建的主机组
            if (successnum == 0 && isCreate && !CollectionUtils.isEmpty(hostGroupIds)){
                for (String demHostGroupId : hostGroupIds) {
                    deleteHostgroup(demHostGroupId);
                }
            }
        }
        return new CreateVmfsResponse02(successnum, failnum, connectionFailList, partsuccessnum, desc);
    }

    private Map<String, List<String>> getLogInfo(Map<String, List<String>> mappingResultMapNew, List<Map<String, String>> chooseDevicelists,
                                                 List<Map<String, Object>> volumelistLst,Map<String,String> objid2hostId ) {
        Map<String, List<String>> info = new HashMap<>();
        if (!CollectionUtils.isEmpty(mappingResultMapNew)){
            for (String volid : mappingResultMapNew.keySet()){
                List<String> kls =  new ArrayList<>();
                String k = null;
                if (!CollectionUtils.isEmpty(volumelistLst)){
                    //根据隐射成功的volumName获取volumid
                    for (Map<String, Object> volumMap : volumelistLst) {
                        if (!CollectionUtils.isEmpty(volumMap) && volid.equalsIgnoreCase(ToolUtils.getStr(volumMap.get(VOLUME_ID)))) {
                            k = ToolUtils.getStr(volumMap.get(VOLUME_NAME));
                        }
                    }
                }


                List<String> hoss = mappingResultMapNew.get(volid);
                if (!CollectionUtils.isEmpty(hoss)){
                    for (String obs : hoss){
                        String hos = objid2hostId.get(obs);
                        for (Map<String, String> choos : chooseDevicelists){
                            if (hos.equalsIgnoreCase(choos.get("deviceId"))){
                                kls.add(choos.get("deviceName"));
                            }
                        }
                    }

                }
                info.put(k,kls);
            }
        }
        return info;
    }

    private List<String> getVcHost(List<String> objHostIds, Map<String, String> objid2hostId) {
        List<String> vchost = new ArrayList<>();
        if (!CollectionUtils.isEmpty(objHostIds) && !CollectionUtils.isEmpty(objid2hostId)){
            for (String objhost : objHostIds) {
                if (!StringUtils.isEmpty(objid2hostId.get(objhost)))
                vchost.add(objid2hostId.get(objhost));
            }
        }
        return vchost;
    }

    private List<String> getFailVolumid(List<String> volumeIdList, Map<String, List<String>> volumeidMapToHostOrGroup) {
        if (!CollectionUtils.isEmpty(volumeIdList) && !CollectionUtils.isEmpty(volumeidMapToHostOrGroup)){
            ArrayList<String> mappedList = new ArrayList<>(volumeidMapToHostOrGroup.keySet());
            volumeIdList.removeAll(mappedList);
            return volumeIdList;
        }else if (CollectionUtils.isEmpty(volumeidMapToHostOrGroup)){
            return volumeIdList;
        }
        return null;
    }

    private Map<String, String> getLunNameAndTimeMap(List<TaskDetailInfoNew> createTaskInfo) {
        Map <String, String> lunNameAndTimeMap = new HashMap<>();
        for (TaskDetailInfoNew taskinfo : createTaskInfo) {
            String  name_en = null;
            long create_time = 0;
            if (!StringUtils.isEmpty(taskinfo)){
                name_en = taskinfo.getNameEn();
                create_time = taskinfo.getCreateTime();
            }
            if (!StringUtils.isEmpty(name_en)){
                String[] rs = name_en.split(" ");
                if (rs.length == 3){
                    String lunName = rs[2];
                    if (!StringUtils.isEmpty(lunName)){
                        lunNameAndTimeMap.put(lunName,ToolUtils.getStr(create_time));
                    }
                }
            }
        }
        return lunNameAndTimeMap;
    }

    private String getCreateMainChildernId(String mainId,List<TaskDetailInfoNew> taskDetailInfoNewList) throws DmeException {
        List<TaskDetailInfoNew> mainTasks = null;
        //首先获取主任务信息
        if (!CollectionUtils.isEmpty(taskDetailInfoNewList)){
            mainTasks =  taskDetailInfoNewList.stream().filter(TaskDetailInfoNew -> (!mainId.equalsIgnoreCase(TaskDetailInfoNew.getId())
                    && mainId.equalsIgnoreCase(TaskDetailInfoNew.getParentId()) && TaskDetailInfoNew.getNameEn().contains("Create LUN"))).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(mainTasks) || mainTasks.size()>1){
            throw new DmeException("get main task info error");
        }
        return mainTasks.get(0).getId();
    }


    private String getMapMainChildernId(String mainId,List<TaskDetailInfoNew> taskDetailInfoNewList) throws DmeException {
        List<TaskDetailInfoNew> mainTasks = null;
        //首先获取主任务信息
        if (!CollectionUtils.isEmpty(taskDetailInfoNewList)){
            mainTasks =  taskDetailInfoNewList.stream().filter(TaskDetailInfoNew -> (!mainId.equalsIgnoreCase(TaskDetailInfoNew.getId())
                    && mainId.equalsIgnoreCase(TaskDetailInfoNew.getParentId()) && TaskDetailInfoNew.getNameEn().contains("Map LUN"))).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(mainTasks) || mainTasks.size()>1){
            throw new DmeException("get main task info error");
        }
        return mainTasks.get(0).getId();
    }

    private List<TaskDetailInfoNew> getCreateInfos(String id, List<TaskDetailInfoNew> taskDetailInfoNewList) {
        List<TaskDetailInfoNew> createTasks = null;
        if (!CollectionUtils.isEmpty(taskDetailInfoNewList)){
            createTasks =  taskDetailInfoNewList.stream().filter(TaskDetailInfoNew -> (id.equalsIgnoreCase(TaskDetailInfoNew.getParentId())
                    && TaskDetailInfoNew.getNameEn().contains("Create LUN") && TaskDetailInfoNew.getStatus() == 3)).collect(Collectors.toList());
        }
        return createTasks;
    }

    private List<TaskDetailInfoNew> getMapInfos(String id, List<TaskDetailInfoNew> taskDetailInfoNewList) {
        List<TaskDetailInfoNew> createTasks = null;
        if (!CollectionUtils.isEmpty(taskDetailInfoNewList)){
            createTasks =  taskDetailInfoNewList.stream().filter(TaskDetailInfoNew -> (id.equalsIgnoreCase(TaskDetailInfoNew.getParentId())
                    && TaskDetailInfoNew.getNameEn().contains("Map LUN") && TaskDetailInfoNew.getStatus() == 3)).collect(Collectors.toList());
        }
        return createTasks;
    }

    private List<String> getMappedLunName(List<TaskDetailInfoNew> taskDetailInfoNewList) {
        List<String> mappedLunName = new ArrayList<>();
        if (!CollectionUtils.isEmpty(taskDetailInfoNewList)) {
            for (TaskDetailInfoNew taskDetailInfoNew : taskDetailInfoNewList) {
                String name_en = null;
                if (!StringUtils.isEmpty(taskDetailInfoNew)) {
                    name_en = taskDetailInfoNew.getNameEn();
                }
                if (!StringUtils.isEmpty(name_en)) {
                    String[] rs = name_en.split(" ");
                    if (rs.length == 3) {
                        String lunName = rs[2];
                        if (!StringUtils.isEmpty(lunName)) {
                            mappedLunName.add(lunName);
                        }
                    }
                }

            }
        }
        return mappedLunName;
    }
    private  Map<String, List<String>>    getVolumeidMapToHostOrGroup( Map<String, List<String>>  mappingResultMapNew) {
        //获取所有映射的并集
        Map<String, List<String>> VolumeidMapToHostOrGroup = new HashMap<>();
        List<String> allMappedVolumids = new ArrayList<>();
        if (!CollectionUtils.isEmpty(mappingResultMapNew)){
            for (String hostOrClusterId : mappingResultMapNew.keySet()){
                List<String> mappedVolumids = mappingResultMapNew.get(hostOrClusterId);
                allMappedVolumids.addAll(mappedVolumids);
            }
            for (String volumeid : allMappedVolumids){
                List<String> hostOrClus = new ArrayList<>();
                Set<String> hostSet = new HashSet<>();
                for (String hostOrClusterId : mappingResultMapNew.keySet()){
                    List<String> mappedVolumids = mappingResultMapNew.get(hostOrClusterId);
                    if (mappedVolumids.contains(volumeid)){
                        hostSet.add(hostOrClusterId);
                    }
                    hostOrClus = new ArrayList<>(hostSet);
                }
                VolumeidMapToHostOrGroup.put(volumeid,hostOrClus);
            }
        }
        return VolumeidMapToHostOrGroup;
    }

    private List<String> createOnVmware2New02 (Map<String, List<String>> volumeidMapToHostOrGroup,
                                               Map<String, List<String>> mappingResultMapNew,
                                               List<Map<String, Object>> volumelistLst ,Map<String, Object> params,
                                               Map<String,String> objid2hostId ,
                                               Map<String,String> dataStoreStrMap ,String type) {
        VCenterInfo vcenterinfo = null;
        List<String> successed = new ArrayList<>();
        try {
            if (!StringUtils.isEmpty(params.get(DmeConstants.SERVICELEVELID))) {
                vcenterinfo = vcenterinfoservice.getVcenterInfo();
            }
        }catch (Exception e){
            return successed;
        }
        final VCenterInfo vcentertemp = vcenterinfo;
        //创建前先扫描hba，避免每次循环扫描
        Object ss = params.get(CHOOSEDEVICE);

        List<Map<String, String>> chooseDevicelists = gson.fromJson(gson.toJsonTree(ss),
                new TypeToken<List<Map<String, String>>>() {
                }.getType());
        //获取主机信息
        Set<String> hostIds = mappingResultMapNew.keySet();
        List<String> hostObjectIds = new ArrayList<>();
        for (String hostid : hostIds) {
            hostObjectIds.add(objid2hostId.get(hostid));
        }
        //获取集群信息
        List<String> clusterObjectIds = getClusterIdList(chooseDevicelists);
        String clusterid = null;
        if ( !CollectionUtils.isEmpty(clusterObjectIds)){
            clusterid = clusterObjectIds.get(0);
        }
        try {
            if (HOST.equalsIgnoreCase(type)) {
                for (String hostObjectId : hostObjectIds) {
                    if (!StringUtils.isEmpty(hostObjectId)) {
                        vcsdkUtils.rescanHbaByHostObjectId(hostObjectId);
                    }
                }
            }
            if (CLUSTER.equalsIgnoreCase(type) && !StringUtils.isEmpty(clusterid)) {
                vcsdkUtils.rescanHbaByClusterObjectId(clusterid);
            }
        } catch (Exception e) {
            return successed;
        }
        Map<String, Object> paramstemp = new HashMap<>(params);
        //数据处理
        Map<String, Map<String, Object>> volume2InfoMap = new HashMap<String, Map<String, Object>>();
        if(!CollectionUtils.isEmpty(volumeidMapToHostOrGroup) && !CollectionUtils.isEmpty(volumelistLst)){
            for (String volumid : volumeidMapToHostOrGroup.keySet()) {
                for (Map<String, Object> volumemap :volumelistLst) {
                    if (!StringUtils.isEmpty(volumid) && volumid.equalsIgnoreCase(ToolUtils.getStr(volumemap.get(VOLUME_ID)))) {
                        volume2InfoMap.put(volumid, volumemap);
                    }
                }
            }
        }
        for (String volumeId: volumeidMapToHostOrGroup.keySet()) {

                // 创建vmware中的vmfs存储。
                paramstemp.put(VOLUME_WWN, volume2InfoMap.get(volumeId).get(VOLUME_WWN));
                paramstemp.put(VOLUME_NAME, volume2InfoMap.get(volumeId).get(VOLUME_NAME));
                String dataStoreStr= null;
                try {
                    if (HOST.equalsIgnoreCase(type)) {
                        dataStoreStr = createVmfsOnVmwareNew02(paramstemp, volumeidMapToHostOrGroup.get(volumeId).get(0), null, objid2hostId);
                    }
                    if (CLUSTER.equalsIgnoreCase(type)) {
                        dataStoreStr = createVmfsOnVmwareNew02(paramstemp, null, clusterid, objid2hostId);
                    }

                    if (!StringUtils.isEmpty(dataStoreStr)) {
                        dataStoreStrMap.put(volumeId,dataStoreStr);
                        Map<String, Object> dataStoreMap = gson.fromJson(dataStoreStr,
                                new TypeToken<Map<String, Object>>() {
                                }.getType());
                        if (dataStoreMap != null) {
                            // 将DME卷与vmfs的关系保存数据库,因为可以同时创建几个卷，无法在此得到对应关系，所以此处不再保存关系信息
                            saveDmeVmwareRalation(volume2InfoMap.get(volumeId), dataStoreMap);
                            // 关联服务等级
                            if (!StringUtils.isEmpty(paramstemp.get(SERVICE_LEVEL_ID))) {
                                String serviceLevelName = ToolUtils.getStr(paramstemp.get(SERVICE_LEVEL_NAME));
                                vcsdkUtils.attachTag(ToolUtils.getStr(dataStoreMap.get(TYPE)),
                                        ToolUtils.getStr(dataStoreMap.get(ID_FIELD)), serviceLevelName, vcentertemp);
                            }
                        }
                        successed.add(volumeId);
                        if (HOST.equalsIgnoreCase(type)) {
                            List<String> hosts = volumeidMapToHostOrGroup.get(volumeId);
                            hosts.remove(0);
                            volumeidMapToHostOrGroup.put(volumeId,hosts);
                        }
                    } else {
                        LOG.error("vmware create vmfs error:" + params.get(VOLUME_NAME));
                    }
                }catch (Exception e){
                    LOG.error(volume2InfoMap.get(volumeId).get(VOLUME_NAME)+": create vmfs error") ;
                }
        }
        return successed;
    }

    private String createVmfsOnVmwareNew02(Map<String, Object> params, String hostObjectId,
                                         String clusterObjectId,Map<String,String> objid2hostId ) throws DmeException {
        String dataStoreStr = "";
        LOG.info("create vmfs on vmware begin !");
        try {
            if (params != null) {
                // 创建vmware中的vmfs存储。 cluster host
                String datastoreName = ToolUtils.getStr(params.get(NAME_FIELD));
                int capacity = ToolUtils.getInt(params.get(CAPACITY));
                String existVolumeWwn = ToolUtils.getStr(params.get(VOLUME_WWN));
                String existVolumeName = ToolUtils.getStr(params.get(VOLUME_NAME));
                String volumeName = ToolUtils.getStr(params.get(VOLUMENAME));
                existVolumeName = existVolumeName.replaceAll(volumeName, "");

                // 根据后缀序号改变VMFS的名称
                datastoreName = datastoreName + existVolumeName;

                // 从主机或集群中找出最接近capacity的LUN
                Map<String, Object> hsdmap = new HashMap<>();
                ArrayList<Map<String, Object>> hsdmapList = new ArrayList<Map<String, Object>>();
                if (!StringUtils.isEmpty(hostObjectId)) {
                    hsdmap = vcsdkUtils.getLunsOnHost(objid2hostId.get(hostObjectId), capacity, existVolumeWwn);
                }
                if (!StringUtils.isEmpty(clusterObjectId)) {
                    hsdmap = vcsdkUtils.getLunsOnCluster(clusterObjectId, capacity, existVolumeWwn);
                }

                int vmfsMajorVersion = ToolUtils.getInt(params.get("version"));
                int unmapGranularity = ToolUtils.getInt(params.get("spaceReclamationGranularity"));
                int blockSize = ToolUtils.getInt(params.get("blockSize"));
                String unmapPriority = ToolUtils.getStr(params.get("spaceReclamationPriority"));
                dataStoreStr = vcsdkUtils.createVmfsDataStore(hsdmap, capacity, datastoreName, vmfsMajorVersion,
                        blockSize, unmapGranularity, unmapPriority);

                // 如果创建成功，扫描集群中的其他主机
                if (!StringUtils.isEmpty(clusterObjectId)) {
                    vcsdkUtils.scanDataStore(clusterObjectId, null);
                }
            }
        } catch (Exception e) {
            LOG.error("createVmfsOnVmware error:{}", e.toString());
            throw new DmeException(e.getMessage());
        }
        return dataStoreStr;
    }

    private void deleteFailLun02(List<String> volumelist) {
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put(VOLUME_IDS, volumelist);
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = dmeAccessService.access(DmeConstants.DME_VOLUME_DELETE_URL, HttpMethod.POST, gson.toJson(requestbody));
            dmeAccessService.access(DmeConstants.DME_VOLUME_DELETE_URL, HttpMethod.POST, gson.toJson(requestbody));
        }catch (Exception e){
            LOG.error(e.getMessage());
        }
        if (!StringUtils.isEmpty(responseEntity) && responseEntity.getStatusCodeValue() == HttpStatus.ACCEPTED.value()) {
            JsonObject object = new JsonParser().parse(responseEntity.getBody()).getAsJsonObject();
            String taskId = ToolUtils.jsonToStr(object.get("task_id"));
            taskService.checkTaskStatusNew(taskId,longTaskTimeOut);
        }
    }

    private void unmappingLunFromEveryHost02(Map<String, List<String>> volumeidMapToHostOrGroup, List<String> successList,String type) {
        if (!CollectionUtils.isEmpty(volumeidMapToHostOrGroup)) {
            if (!CollectionUtils.isEmpty(successList)) {
                for (String vomid : successList) {
                    volumeidMapToHostOrGroup.remove(vomid);
                }
            }
            for (String volume : volumeidMapToHostOrGroup.keySet()) {
                List<String> hostids = null;
                List<String> hostGroupids = null;
                if (HOST.equalsIgnoreCase(type)) {
                     hostids = volumeidMapToHostOrGroup.get(volume);
                }
                if (CLUSTER.equalsIgnoreCase(type)){
                    hostGroupids = volumeidMapToHostOrGroup.get(volume);
                }
                if (!CollectionUtils.isEmpty(hostids)) {
                    for (String hostid : hostids) {
                        if (!StringUtils.isEmpty(hostid) && !StringUtils.isEmpty(volume)) {
                            Map<String, Object> requestParam = new HashMap<>();
                            requestParam.put(VOLUMEIDS, Arrays.asList(volume));
                            requestParam.put(HOST_ID, hostid);
                            LOG.info("unmapping lun "+gson.toJson(requestParam));
                            try {
                                hostUnmapping(requestParam);
                            } catch (Exception e) {
                                LOG.error(e.getMessage());
                            }
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(hostGroupids)) {
                    for (String hostGroupid : hostGroupids) {
                        if (!StringUtils.isEmpty(hostGroupid) && !StringUtils.isEmpty(volume)) {
                            Map<String, Object> requestParam = new HashMap<>();
                            requestParam.put(VOLUMEIDS, Arrays.asList(volume));
                            requestParam.put(HOST_GROUP_ID1, hostGroupid);
                            LOG.info("unmapping lun "+gson.toJson(requestParam));
                            try {
                                hostGroupUnmapping(requestParam);
                            } catch (Exception e) {
                                LOG.error(e.getMessage());
                            }
                        }
                    }
                }
            }
        }
    }
    private void unmappingLunFromEveryHostGroup02(Map<String, List<String>> volumeidMapToHostOrGroup, List<String> successList){
        if (!CollectionUtils.isEmpty(volumeidMapToHostOrGroup) && !CollectionUtils.isEmpty(successList)){
            for (String vomid : successList){
                volumeidMapToHostOrGroup.remove(vomid);
            }
            for (String volume : volumeidMapToHostOrGroup.keySet()) {
                List<String> hostGroupIds = volumeidMapToHostOrGroup.get(volume);
                if (!CollectionUtils.isEmpty(hostGroupIds)) {
                    for (String hostGroupId : hostGroupIds){
                        if (!StringUtils.isEmpty(hostGroupId) && !StringUtils.isEmpty(volume)){
                            Map<String, Object> requestParam = new HashMap<>();
                            requestParam.put(VOLUMEIDS, Arrays.asList(volume));
                            requestParam.put(HOST_GROUP_ID1, hostGroupId);
                            try {
                                hostUnmapping(requestParam);
                            } catch (Exception e) {
                                LOG.error(e.getMessage());
                            }
                        }
                    }
                }
            }
        }
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------




    private void deleteFailLun(List<String> volumelist) {
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put(VOLUME_IDS, volumelist);
        try {
            dmeAccessService.access(DmeConstants.DME_VOLUME_DELETE_URL, HttpMethod.POST, gson.toJson(requestbody));
        }catch (Exception e){
            LOG.error(e.getMessage());
        }
    }

    private void unmappingLunFromEveryHost(Map<String, Map<String, List<String>>> mappingResultMapNew, List<String> successList,String type){
        if (!CollectionUtils.isEmpty(mappingResultMapNew) && !CollectionUtils.isEmpty(successList)){
            for (String hostid : mappingResultMapNew.keySet()) {
                Map<String, List<String>> mappResult = mappingResultMapNew.get(hostid);
                if (!CollectionUtils.isEmpty(mappResult)) {
                    List<String> mapped = mappResult.get(type);
                    mapped.removeAll(successList);
                    if (!CollectionUtils.isEmpty(mapped)) {
                        Map<String, Object> requestParam = new HashMap<>();
                        requestParam.put(VOLUMEIDS, mapped);
                        requestParam.put(HOST_ID, hostid);
                        try {
                            hostUnmapping(requestParam);
                        } catch (Exception e) {
                            LOG.error(e.getMessage());
                        }
                    }
                }
            }
        }
    }
    private void unmappingLunFromEveryHostGroup(Map<String, Map<String, List<String>>> mappingResultMapNew, List<String> successList,String type){
        if (!CollectionUtils.isEmpty(mappingResultMapNew) && !CollectionUtils.isEmpty(successList)){
            for (String hostGroupId : mappingResultMapNew.keySet()) {
                Map<String, List<String>> mappResult = mappingResultMapNew.get(hostGroupId);
                if (!CollectionUtils.isEmpty(mappResult)) {
                    List<String> mapped = mappResult.get(type);
                    mapped.removeAll(successList);
                    if (!CollectionUtils.isEmpty(mapped)) {
                        Map<String, Object> requestParam = new HashMap<>();
                        requestParam.put(VOLUMEIDS, mapped);
                        requestParam.put(HOST_GROUP_ID1, hostGroupId);
                        try {
                            hostGroupUnmapping(requestParam);
                        } catch (Exception e) {
                            LOG.error(e.getMessage());
                        }
                    }
                }
            }
        }
    }

    private List<String> getAllMappedVolumids(Map<String, Map<String, List<String>>> mappingResultMapNew,String type) {
        List<String> allMappedVolumids = null;
        if (!CollectionUtils.isEmpty(mappingResultMapNew)){
            for (String hostOrClusterId : mappingResultMapNew.keySet()){
                List<String> mappedVolumids = mappingResultMapNew.get(hostOrClusterId).get(type);
                if (CollectionUtils.isEmpty(allMappedVolumids)){
                    allMappedVolumids = mappedVolumids;
                }else {
                    allMappedVolumids.retainAll(mappedVolumids);
                }
            }
        }
        return allMappedVolumids;
    }
    private List<Map<String, Object>> getAllMappedVolums(List<Map<String, Object>> volumelist, List<String> allMappedVolumids) throws DmeException {
        List<Map<String, Object>> allMappedVolums = new ArrayList<>();
        if (CollectionUtils.isEmpty(allMappedVolumids)){
            throw new DmeException("no effective volum") ;
        }
        for (String volumid : allMappedVolumids){
            for (Map<String,Object> volumInfo : volumelist) {
                if (!CollectionUtils.isEmpty(volumInfo) && (volumid.equalsIgnoreCase(ToolUtils.getStr(volumInfo.get(VOLUME_ID))))){
                    allMappedVolums.add(volumInfo);
                }
            }
        }
        return allMappedVolums;
    }

    /**
     * @throws DmeException
     * @Description: 检验主机是否属于多个主机组,
     * @Param chooseDevicelists
     * @author yc
     * @Date 2021/5/20 16:39
     */
    private void checkHostsBelongOnecluster(List<Map<String, String>> chooseDevicelists) throws DmeException {
        //首先获取主机列表
        try {
            Map<String, String> hostObjectIdMap = getHostNameAndHostIdList(chooseDevicelists);
            List<String> hostIds = new ArrayList<>(hostObjectIdMap.keySet());
            HashSet<String> clusterSet = new HashSet<>(getClusterIdsByHostId(hostIds));
            if (!CollectionUtils.isEmpty(clusterSet) && clusterSet.size() > 1) {
                throw new DmeException("create vmfs error: not accept more cluster the host in");
            }
        } catch (Exception e) {
            throw new DmeException(e.getMessage());
        }
    }

    /**
     * @return deviceTypeSet
     * @throws DmeException
     * @Description: 根据前端入参判断前端选择的创建VFMS的组合方式
     * @Param Map<String, Object> params
     * @author yc
     * @Date 2021/5/13 14:52
     */
    private HashSet<String> getDeviceTypeSet(List<Map<String, String>> chooseDevicelists) throws DmeException {
        HashSet<String> deviceTypeSet = new HashSet<String>();
        if (!CollectionUtils.isEmpty(chooseDevicelists)) {
            deviceTypeSet = getDeviceType(chooseDevicelists);
        }
        if (CollectionUtils.isEmpty(deviceTypeSet) || deviceTypeSet.size() > 1) {
            throw new DmeException("create vmfs error: params is error");
        }
        return deviceTypeSet;
    }

    /**
     * @return deviceTypeSet
     * @throws DmeException
     * @Description: 根据前端入参判断前端选择的创建VFMS的组合方式
     * @Param Map<String, Object> params
     * @author yc
     * @Date 2021/5/13 14:52
     */
    private HashSet<String> getDeviceTypeSet2(List<Map<String, String>> chooseDevicelists) throws DmeException {
        HashSet<String> deviceTypeSet = new HashSet<String>();
        if (!CollectionUtils.isEmpty(chooseDevicelists)) {
            deviceTypeSet = getDeviceType(chooseDevicelists);
        }
        if (CollectionUtils.isEmpty(deviceTypeSet) || deviceTypeSet.size() > 2) {
            throw new DmeException("create vmfs params is error");
        }
        return deviceTypeSet;
    }

    /**
     * @return hostName
     * @throws DmeException
     * @Description: 根据主机id获取主机名称
     * @Param hostId
     * @author yc
     * @Date 2021/5/28 11:33
     */
    private String getDmeHostNameByIdNew(String hostId) {
        String hostName = "";
        ResponseEntity responseEntity;
        String getHostUrl = DmeConstants.GET_DME_HOST_URL.replace("{host_id}", hostId);
        try {
            responseEntity = dmeAccessService.access(getHostUrl, HttpMethod.GET, null);
        } catch (Exception e) {
            LOG.error("request of getDmeHostNameByIdNew time out");
            return hostName;
        }
        if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
            JsonObject vjson = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
            hostName = ToolUtils.jsonToStr(vjson.get(NAME_FIELD));
        }
        return hostName;
    }

    /**
     * @return @return
     * @throws
     * @Description: 回滚函数
     * @Param @param null
     * @author yc
     * @Date 2021/5/13 17:22
     */
    private void rollBackHostNew(List<String> volumeIds, List<String> dmeHostIds, List<String> demHostGroupIds,
                                 Boolean isCreated, Boolean isMapping) throws DmeException {
        ResponseEntity responseEntity = null;
        String taskId = "";
        if (!CollectionUtils.isEmpty(volumeIds)) {
            Map<String, Object> requestParam = new HashMap<>();
            //解除映射，删除已创建的卷
            requestParam.put(VOLUMEIDS, volumeIds);
            if (isMapping) {
                if (!CollectionUtils.isEmpty(dmeHostIds)) {
                    for (String dmeHostId : dmeHostIds) {
                        requestParam.put(HOST_ID, dmeHostId);
                        responseEntity = hostUnmapping(requestParam);
                    }
                }
                if (!CollectionUtils.isEmpty(demHostGroupIds)) {
                    for (String demHostGroupId : demHostGroupIds) {
                        requestParam.put(HOST_GROUP_ID1, demHostGroupId);
                        responseEntity = hostGroupUnmapping(requestParam);
                    }
                }
                if (responseEntity.getStatusCodeValue() == HttpStatus.ACCEPTED.value()) {
                    taskId = ToolUtils.jsonToStr(
                            new JsonParser().parse(ToolUtils.getStr(responseEntity.getBody())).getAsJsonObject()
                                    .get("task_id"));
                }
            }
            Set<String> taskIds = new HashSet<>();
            taskIds.add(taskId);
            if (taskId.equals("") || taskService.checkTaskStatusLarge(taskIds, longTaskTimeOut)) {
                volumeDelete(requestParam);
                if (!CollectionUtils.isEmpty(demHostGroupIds) && isCreated) {
                    for (String demHostGroupId : demHostGroupIds) {
                        deleteHostgroup(demHostGroupId);
                    }
                }
            }
        }
    }

    /**
     * @throws DmeException
     * @Description: 在虚拟机上创建存储
     * @Param params, volumemaps
     * @author yc
     * @Date 2021/5/31 16:10
     */
    private List<String> createOnVmware2New(Map<String, Object> params, List<Map<String, Object>> volumemaps) throws DmeException {
        VCenterInfo vcenterinfo = null;
        List<String> successed = new ArrayList<>();
        if (!StringUtils.isEmpty(params.get(DmeConstants.SERVICELEVELID))) {
            vcenterinfo = vcenterinfoservice.getVcenterInfo();
        }
        final VCenterInfo vcentertemp = vcenterinfo;
        //创建前先扫描hba，避免每次循环扫描
        Object ss = params.get(CHOOSEDEVICE);

        List<Map<String, String>> chooseDevicelists = gson.fromJson(gson.toJsonTree(ss),
                new TypeToken<List<Map<String, String>>>() {
                }.getType());
        //获取主机信息
        Map<String, String> hostObjectIdMap = getHostNameAndHostIdList(chooseDevicelists);
        Set<String> hostObjectIds = hostObjectIdMap.keySet();
        //获取集群信息
        List<String> clusterObjectIds = getClusterIdList(chooseDevicelists);
        if (!CollectionUtils.isEmpty(hostObjectIds)) {
            for (String hostObjectId : hostObjectIds) {
                vcsdkUtils.rescanHbaByHostObjectId(hostObjectId);
            }
        }
        if (!CollectionUtils.isEmpty(clusterObjectIds)) {
            for (String clusterObjectId : clusterObjectIds) {
                vcsdkUtils.rescanHbaByClusterObjectId(clusterObjectId);
            }
        }
        Map<String, Object> paramstemp = new HashMap<>(params);
        for (Map<String, Object> volumemap : volumemaps) {

            try {
                // 创建vmware中的vmfs存储。
                paramstemp.put(VOLUME_WWN, volumemap.get(VOLUME_WWN));
                paramstemp.put(VOLUME_NAME, volumemap.get(VOLUME_NAME));
                String dataStoreStr = createVmfsOnVmwareNew(paramstemp, hostObjectIdMap, clusterObjectIds);
                if (!StringUtils.isEmpty(dataStoreStr)) {
                    Map<String, Object> dataStoreMap = gson.fromJson(dataStoreStr,
                            new TypeToken<Map<String, Object>>() {
                            }.getType());
                    if (dataStoreMap != null) {
                        // 将DME卷与vmfs的关系保存数据库,因为可以同时创建几个卷，无法在此得到对应关系，所以此处不再保存关系信息
                        saveDmeVmwareRalation(volumemap, dataStoreMap);
                        // 关联服务等级
                        if (!StringUtils.isEmpty(paramstemp.get(SERVICE_LEVEL_ID))) {
                            String serviceLevelName = ToolUtils.getStr(paramstemp.get(SERVICE_LEVEL_NAME));
                            vcsdkUtils.attachTag(ToolUtils.getStr(dataStoreMap.get(TYPE)),
                                    ToolUtils.getStr(dataStoreMap.get(ID_FIELD)), serviceLevelName, vcentertemp);
                        }
                    }
                    //将vmfs挂载到其他主机上
                    if (hostObjectIds.size() > 1) {
                        mountVmfsOnVmwareNew(params, chooseDevicelists);
                    }
                    successed.add(ToolUtils.getStr(volumemap.get(VOLUME_ID)));
                } else {
                    throw new DmeException("vmware create vmfs error:" + params.get(VOLUME_NAME));
                }

            } catch (Exception e) {
                LOG.info("vmware create vmfs error:" + params.get(VOLUME_NAME));
            }
        }
        return successed;
    }

    /**
     * @return Map<String, List < String>>
     * @throws DmeException
     * @Description: 检查主机组是否存在，不存在就创建主机组
     * @Param params, deviceTypeSet, chooseDevicelists
     * @author yc
     * @Date 2021/6/1 15:19
     */
    private Map<String, List<String>> checkOrCreateToHostGroupNew(Map<String, Object> params,
                                                                  List<Map<String, String>> chooseDevicelists,
                                                                  Map<String,String> objid2hostId) throws DmeException {
        //去重后根据前端入参的方式进行逻辑判断
        Map<String, List<String>> hostIds = new HashMap<>();
        int createFlag = 0;
        try {
            //单纯选择主机组的方式创建
            //todo 重新改造原有方法（检查主机组连通性）
            //检查主机组是否存在，不存在就创建新的主机组
            List<String> clusters = getClusterIdList(chooseDevicelists);
            Map<String, String> clusterMap = getClusterNameAndClusterIdList(chooseDevicelists);
            if (!CollectionUtils.isEmpty(clusters) && clusters.size() > 1) {
                throw new DmeException("create vmfs error : params is error");
            }
            if (!CollectionUtils.isEmpty(clusterMap) && clusterMap.size() > 1) {
                throw new DmeException("create vmfs error : params is error");
            }
            //校验主机组是否存在，存在，先检查连通性进行映射，不存在就创建新的主机组再进行隐射
            //todo 20210629x修改查找主机组的方法
           // HostGroupAndClusterConsistency checkResult = checkConsistencyAndGetclusterId(clusters.get(0),null);
            //修改后
            HostGroupAndClusterConsistency checkResult = getTakeCareHostGroup(clusterMap);
            if (StringUtils.isEmpty(checkResult) || StringUtils.isEmpty(checkResult.getHostGroupId())) {
                checkResult = checkConsistencyAndGetclusterId(clusters.get(0),null);
            }
            String hostGroupId = null;
            if (!StringUtils.isEmpty(checkResult) && !StringUtils.isEmpty(checkResult.getHostGroupId())) {
                hostGroupId = checkResult.getHostGroupId();
            } else {
                //需要创建主机组
                List<String> objIds = createHostGroupIdNew(clusters,null);
                createFlag = 1;
                if (!CollectionUtils.isEmpty(objIds)) {
                    hostGroupId = objIds.get(0);
                }
            }
            objid2hostId.put(hostGroupId,clusters.get(0));
            List<String> failGroupHostIdList = new ArrayList<>();
            // 检查主机组连通性
            if (!StringUtils.isEmpty(hostGroupId)) {
                failGroupHostIdList.addAll(estimateConnectivityOfHostOrHostgroupNew02(ToolUtils.getStr(params.get(STORAGE_ID)), null, hostGroupId));
            }
            if (CollectionUtils.isEmpty(failGroupHostIdList)) {
                hostIds.put("nomalCluster", Arrays.asList(hostGroupId));
            }
            hostIds.put("flag", Arrays.asList(Integer.toString(createFlag)));
        } catch (DmeException e) {
            LOG.error("checkOrcreateToHostorHostGroup error:", e);
            throw new DmeException(e.getMessage());
        }
        return hostIds;
    }

    private HostGroupAndClusterConsistency getTakeCareHostGroup(Map<String, String> clusterMap) throws DmeException {
        if (!CollectionUtils.isEmpty(clusterMap)) {
            String hostGroupName = clusterMap.get(new ArrayList<>(clusterMap.keySet()).get(0));
            List<Map<String, Object>> dmeHostGroups = dmeAccessService.getDmeHostGroups(hostGroupName);
            if (!CollectionUtils.isEmpty(dmeHostGroups)) {
                for (Map<String, Object> map : dmeHostGroups) {
                    if (hostGroupName.equalsIgnoreCase(ToolUtils.getStr(map.get(NAME_FIELD)))) {
                        String hostGroupId = ToolUtils.getStr(map.get(ID_FIELD));
                        return new HostGroupAndClusterConsistency(true, hostGroupId);
                    }
                }
            }
        }
        return null;
    }


    /**
     * @return Map<String, List < String>>
     * @throws DmeException
     * @Description: 检查主机组是否存在，不存在就创建主机组
     * @Param params, deviceTypeSet, chooseDevicelists
     * @author yc
     * @Date 2021/6/1 15:19
     */
    private List<Map<String, List<String>>> checkOrCreateToHostGroupNew2(Map<String, Object> params,

                                                                         List<Map<String, String>> chooseDevicelists,
                                                                         String hostGroupVolumid) throws DmeException {
        List<Map<String, List<String>>> temps = new ArrayList<>();
        if(StringUtils.isEmpty(hostGroupVolumid)){
            throw new DmeException("mount vmfs params is error");
        }
        //去重后根据前端入参的方式进行逻辑判断
        int createFlag = 0;
        try {
            //单纯选择主机组的方式创建
            //todo 重新改造原有方法（检查主机组连通性）
            //检查主机组是否存在，不存在就创建新的主机组
            List<String> clusters = getClusterIdList(chooseDevicelists);
            Map<String, String> clusterMap = getClusterNameAndClusterIdList(chooseDevicelists);
            if (CollectionUtils.isEmpty(clusters)) {
                throw new DmeException("mount vmfs params is error");
            }
            //校验主机组是否存在，存在，先检查连通性进行映射，不存在就创建新的主机组再进行隐射
            for (String clusterid : clusters) {
                Map<String, List<String>> hostIds = new HashMap<>();
                List<String> failHostName = new ArrayList<>();
                //HostGroupAndClusterConsistency checkResult = checkConsistencyAndGetclusterId(clusterid,hostGroupVolumid);
                //修改后
                Map<String, String> clusterTakeCareMap = new HashMap<>();
                clusterTakeCareMap.put(clusterid,clusterMap.get(clusterid));
                HostGroupAndClusterConsistency checkResult = getTakeCareHostGroup(clusterTakeCareMap);
                if (StringUtils.isEmpty(checkResult) || StringUtils.isEmpty(checkResult.getHostGroupId())) {
                    checkResult = checkConsistencyAndGetclusterId(clusterid,null);
                }
                String hostGroupId = null;
                if (!StringUtils.isEmpty(checkResult) && !StringUtils.isEmpty(checkResult.getHostGroupId())) {
                    hostGroupId = checkResult.getHostGroupId();
                } else {
                    //需要创建主机组
                    List<String> objIds = createHostGroupIdNew(Arrays.asList(clusterid),hostGroupVolumid);
                    createFlag = 1;
                    if (!CollectionUtils.isEmpty(objIds)) {
                        hostGroupId = objIds.get(0);
                    }
                }
                List<String> failGroupHostIdList = new ArrayList<>();
                // 检查主机组连通性
                if (!StringUtils.isEmpty(hostGroupId)) {
                    failGroupHostIdList.addAll(estimateConnectivityOfHostOrHostgroupNew(ToolUtils.getStr(params.get(STORAGE_ID)), null, hostGroupId,failHostName));
                }
                if (CollectionUtils.isEmpty(failGroupHostIdList)) {
                    hostIds.put("nomalCluster", Arrays.asList(hostGroupId));
                }
                if (!CollectionUtils.isEmpty(failHostName)) {
                    hostIds.put("failHostname", failHostName);
                }
                hostIds.put("flag", Arrays.asList(Integer.toString(createFlag)));
                temps.add(hostIds);
            }
        } catch (DmeException e) {
            LOG.error("checkOrcreateToHostorHostGroup error:", e);
            throw new DmeException(e.getMessage());
        }
        return temps;
    }
    /**
     * @throws DmeException
     * @Description: 检验主机是否属于多个主机组,
     * @Param chooseDevicelists
     * @author yc
     * @Date 2021/5/20 16:39
     */
    public List<String> getClusterIdsByHostId(List<String> hostIds) throws VcenterException {
        //首先判断入参
        ArrayList<String> clusterIds = new ArrayList<String>();
        if (CollectionUtils.isEmpty(hostIds))
            return clusterIds;
        List<String> clusteridList = vcsdkUtils.getAllClusterIds();
        //循环主机id,判断主机所属的主机组
        for (String hostid : hostIds) {
            for (String clusterid : clusteridList) {
                List<String> hosts = vcsdkUtils.getHostidsOnCluster(clusterid);
                if (!CollectionUtils.isEmpty(hosts) && hosts.contains(hostid)) {
                    clusterIds.add(clusterid);
                }
            }
        }
        return clusterIds;
    }


    /**
     * @return deviceTypeSet
     * @Description: 根据前端入参判断用户选择是通过主机方式创建还是根据主机组方式创建，或者进行混合创建
     * @Param chooseDevicelists
     * @author yc
     * @Date 2021/5/12 11:44
     */
    private HashSet<String> getDeviceType(List<Map<String, String>> chooseDevicelists) {
        HashSet<String> deviceTypeSet = new HashSet<String>();
        for (Map<String, String> chooseMap : chooseDevicelists) {
            deviceTypeSet.add(StringUtils.trimAllWhitespace(chooseMap.get("deviceType")));
        }
        return deviceTypeSet;
    }

    /**
     * @Description: 获取参数中的主机名称和Id列表
     * @Param params
     * @author yc
     * @Date 2021/5/12 14:37
     */
    private Map<String, String> getHostNameAndHostIdList(List<Map<String, String>> chooseDevicelists) {
        HashMap<String, String> hostMap = new HashMap<String, String>();
        for (Map<String, String> chooseMap : chooseDevicelists) {
            if ("host".equalsIgnoreCase(StringUtils.trimAllWhitespace(chooseMap.get("deviceType")))) {
                hostMap.put(StringUtils.trimAllWhitespace(chooseMap.get("deviceId")), StringUtils.trimAllWhitespace(chooseMap.get("deviceName")));
            }
        }
        return hostMap;

    }

    /**
     * @Description: 获取参数中的主机组id列表
     * @Param params
     * @author yc
     * @Date 2021/5/12 14:39
     */
    private List<String> getClusterIdList(List<Map<String, String>> chooseDevicelists) {
        ArrayList<String> clusterList = new ArrayList<String>();
        for (Map<String, String> chooseMap : chooseDevicelists) {
            if ("cluster".equalsIgnoreCase(StringUtils.trimAllWhitespace(chooseMap.get("deviceType")))) {
                clusterList.add(StringUtils.trimAllWhitespace(chooseMap.get("deviceId")));
            }
        }
        return clusterList;
    }

    /**
     * @Description: 获取参数中的集群名称和Id列表
     * @Param params
     * @author yc
     * @Date 2021/5/12 14:37
     */
    private Map<String, String> getClusterNameAndClusterIdList(List<Map<String, String>> chooseDevicelists) {
        HashMap<String, String> hostMap = new HashMap<String, String>();
        for (Map<String, String> chooseMap : chooseDevicelists) {
            if ("cluster".equalsIgnoreCase(StringUtils.trimAllWhitespace(chooseMap.get("deviceType")))) {
                hostMap.put(StringUtils.trimAllWhitespace(chooseMap.get("deviceId")), StringUtils.trimAllWhitespace(chooseMap.get("deviceName")));
            }
        }
        return hostMap;

    }
    /**
     * @return hostIds
     * @throws DmeException
     * @Description: 检查主机或者主机是否存在，不存在就创建新的主机
     * @Param params, allinitionators, volumeId, deviceTypeSet
     * @author yc
     * @Date 2021/5/13 14:54
     */
    private Map<String, List<String>> checkHostNew(Map<String, Object> params,
                                                   Map<String, List<Map<String, Object>>> allinitionators,
                                                   List<Map<String, String>> chooseDevicelists ,Map<String,String> objid2hostId) throws DmeException {
        List<String> hostIdList = new ArrayList<>();
        Map<String, List<String>> hostIds = new HashMap<>();

        try {
            //检查以主机的方式创建，多主机是否属于同一主机组，不支持多主机组的形式
            if (chooseDevicelists.size() > 1) {
                checkHostsBelongOnecluster(chooseDevicelists);
            }
            //检查主机是否存在，不存在就创建新的主机
            Map<String, String> hostIdMaps = getHostNameAndHostIdList(chooseDevicelists);
            List<String> objIds = checkOrCreateToHostNew(hostIdMaps, allinitionators, objid2hostId);
            // 根据获取到的dme主机，检查主机连通性结果，返回联通性异常的主机
            hostIdList = estimateConnectivityOfHostOrHostgroupNew(ToolUtils.getStr(params.get(STORAGE_ID)), objIds, null,null);
            // 连通性正常的主机id
            if (!CollectionUtils.isEmpty(hostIdList)) {
                hostIds.put("failHost", hostIdList);
                objIds.removeAll(hostIdList);
            }
            hostIds.put("nomalHost", objIds);
        } catch (DmeException e) {
            LOG.error("checkOrcreateToHostorHostGroup error:", e);
            throw new DmeException(e.getMessage());
        }
        return hostIds;
    }
    /**
     * @return hostIds
     * @throws DmeException
     * @Description: 检查主机或者主机是否存在，不存在就创建新的主机
     * @Param params, allinitionators, volumeId, deviceTypeSet
     * @author yc
     * @Date 2021/5/13 14:54
     */
    private Map<String, List<String>> checkHostNewNoCheckCluster(Map<String, Object> params,
                                                   Map<String, List<Map<String, Object>>> allinitionators,
                                                   List<Map<String, String>> chooseDevicelists) throws DmeException {
        List<String> hostIdList = new ArrayList<>();
        Map<String, List<String>> hostIds = new HashMap<>();
        Map<String,String> objid2hostId = new HashMap<>();
        List<String> failHostName = new ArrayList<>();

        try {
            //检查主机是否存在，不存在就创建新的主机
            Map<String, String> hostIdMaps = getHostNameAndHostIdList(chooseDevicelists);
            List<String> objIds = checkOrCreateToHostNew(hostIdMaps, allinitionators,objid2hostId);
            // 根据获取到的dme主机，检查主机连通性结果，返回联通性异常的主机
            hostIdList = estimateConnectivityOfHostOrHostgroupNew(ToolUtils.getStr(params.get(STORAGE_ID)), objIds, null,failHostName);
            // 连通性正常的主机id
            if (!CollectionUtils.isEmpty(hostIdList)) {
                hostIds.put("failHost", hostIdList);
                objIds.removeAll(hostIdList);
            }
            if(!CollectionUtils.isEmpty(failHostName)){
                hostIds.put("failHostName", failHostName);
            }
            hostIds.put("nomalHost", objIds);
        } catch (DmeException e) {
            LOG.error("checkOrcreateToHostorHostGroup error:", e);
            throw new DmeException(e.getMessage());
        }
        return hostIds;
    }
    /**
     * @Description: 检查主机是否存在，不存在就创建新的主机
     * @author yc
     * @Date 2021/5/12 14:55
     */
    private List<String> checkOrCreateToHostNew(Map<String, String> hostNameAndHostIdList,
                                                Map<String, List<Map<String,
                                                        Object>>> allinitionators,Map<String,String> objid2hostId) throws DmeException {
        ArrayList<String> objIds = new ArrayList<String>();
        if (CollectionUtils.isEmpty(hostNameAndHostIdList)) {
            return null;
        }
        Set<String> hostIds = hostNameAndHostIdList.keySet();
        try {
            //获取各个主机的适配器集合
            for (String hostId : hostIds) {
                String objId = "";
                List<Map<String, Object>> hbas = vcsdkUtils.getHbasByHostObjectId(hostId);
                if (CollectionUtils.isEmpty(hbas)) {
                    throw new DmeException("The" + hostNameAndHostIdList.get(hostId) + " did not find a valid Hba");
                }
                List<String> wwniqns = new ArrayList<>();
                for (Map<String, Object> hba : hbas) {
                    wwniqns.add(ToolUtils.getStr(hba.get(NAME_FIELD)));
                }
                for (String dmehostid : allinitionators.keySet()) {
                    List<Map<String, Object>> hostinitionators = allinitionators.get(dmehostid);
                    if (hostinitionators != null && hostinitionators.size() > 0) {
                        for (Map<String, Object> inimap : hostinitionators) {
                            String portName = ToolUtils.getStr(inimap.get(PORT_NAME));
                            if (wwniqns.contains(portName)) {
                                objId = dmehostid;
                                break;
                            }
                        }
                    }
                }
                // 如果主机\不存在就创建并得到主机
                if (StringUtils.isEmpty(objId)) {
                    objId = createHostOnDme(hostNameAndHostIdList.get(hostId), hostId);
                    objIds.add(objId);
                } else {
                    objIds.add(objId);
                }
                if (!StringUtils.isEmpty(objid2hostId)) {
                    objid2hostId.put(objId, hostId);
                }
            }
        } catch (DmeException ex) {
            LOG.error("checkOrCreateToHost error:", ex);
            throw new DmeException(ex.getMessage());
        }
        return objIds;

    }

    /**
     * @return List<String>
     * @throws DmeException
     * @Description: 创建主机组新方法
     * @Param clusterIdList
     * @author yc
     * @Date 2021/6/2 11:09
     */
    private List<String> createHostGroupIdNew(List<String> clusterIdList,String hostGroupVolumid) throws DmeException {

        ArrayList<String> objIds = new ArrayList<String>();
        String radomStr = new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date(System.currentTimeMillis())).toString();
        // 取得集群下的所有主机
        for (String clusterObjectId : clusterIdList) {
            String objId = "";
            String vmwarehosts = vcsdkUtils.getHostsOnCluster(clusterObjectId);
            if (StringUtils.isEmpty(vmwarehosts)) {
                throw new DmeException("the cluster has no host");
            }

            List<Map<String, String>> vmwarehostlists = gson.fromJson(vmwarehosts,
                    new TypeToken<List<Map<String, String>>>() {
                    }.getType());
            if (vmwarehostlists != null && vmwarehostlists.size() > 0) {
                // 分别检查每一个主机是否存在，如果不存在就创建
                List<String> hostlists = new ArrayList<>();
                Map<String, List<Map<String, Object>>> allinitionators = getAllInitionator();
                //todo 需要从检查联通性的主机中把已经挂载的剔除
                //todo 20210621 修改挂载集群时映射主机组时需要剔除已经隐射的主机
                if (!StringUtils.isEmpty(hostGroupVolumid)) {
                    List<String> mappedHostid = new ArrayList<>();
                    if (!StringUtils.isEmpty(hostGroupVolumid)) {
                        List<Attachment> attachmentList = findMappedHostsAndClusters(hostGroupVolumid);
                        if (!CollectionUtils.isEmpty(attachmentList)) {
                            for (Attachment attach : attachmentList) {
                                if (!StringUtils.isEmpty(StringUtil.dealQuotationMarks(attach.getHostId()))) {
                                    mappedHostid.add(StringUtil.dealQuotationMarks(attach.getHostId()));
                                }
                            }
                        }
                    }
                    //从集群中的主机中剔除已经映射的主机
                    vmwarehostlists = deleteMappedHost(vmwarehostlists, mappedHostid,allinitionators);
                }
                for (Map<String, String> hostmap : vmwarehostlists) {
                    String tmpHostId = checkOrCreateToHost(ToolUtils.getStr(hostmap.get(HOST_NAME)),
                            ToolUtils.getStr(hostmap.get(HOSTID)), allinitionators);
                    if (!StringUtils.isEmpty(tmpHostId)) {
                        hostlists.add(tmpHostId);
                    }
                }
                // 在DME中创建主机组
                //todo 从主机集合中剔除已经映射的主机
                if (!CollectionUtils.isEmpty(hostlists)) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("cluster",
                            vcsdkUtils.getVcConnectionHelper().objectId2Mor(clusterObjectId).getValue() + "-C" + radomStr);
                    params.put("hostids", hostlists);
                    Map<String, Object> hostmap = dmeAccessService.createHostGroup(params);
                    if (hostmap != null && hostmap.get(DmeConstants.ID) != null) {
                        objId = ToolUtils.getStr(hostmap.get(ID_FIELD));
                    }
                }
            }
            objIds.add(objId);
        }
        return objIds;
    }

    /**
     * @Description: 检查主机或者主机组的联通性
     * @author yc
     * @Date 2021/5/12 15:49
     */
    private List<String> estimateConnectivityOfHostOrHostgroupNew(String storageId, List<String> objIds, String hostgroupId ,List<String> failHostName) throws DmeException {
        Map<String, Object> requestBody = new HashMap<>();
        if (StringUtils.isEmpty(storageId)) {
            LOG.error("estimate connectivity of host or hostgroup storageid param error!", storageId);
            throw new DmeException("estimate connectivity of host or hostgroup , storageid param error!");
        }
        requestBody.put("storage_id", storageId);
        if (!CollectionUtils.isEmpty(objIds)) {
            //检查主机连通性参数
            requestBody.put("host_ids", objIds);
        }
        if (!StringUtils.isEmpty(hostgroupId)) {
            //检查主机组连通性参数
            requestBody.put("hostgroup_id", hostgroupId);
        }
        String url = DmeConstants.DME_ESTIMATE_CONNECTIVITY;
        Map<String, Object> param = new HashMap<>();
        LOG.info("check connectivity of host or horstgroup on dme ! {" + gson.toJson(requestBody) + "}");
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, gson.toJson(requestBody));
        LOG.info("check connectivity return : " +gson.toJson(responseEntity));
        List<String> resultMapList = new ArrayList<>();
        if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
            String body = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("results").getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                String id = ToolUtils.jsonToStr(element.get("host_id"));
                String name = ToolUtils.jsonToStr(element.get("host_name"));
                String status = ToolUtils.jsonToStr(element.get("status"));
                // 连通性异常主机结果统计
                if (status.equalsIgnoreCase("NOT_CONNECT")) {
                    resultMapList.add(id);
                    if (null != failHostName) {
                        failHostName.add(name);
                    }
                }
            }
        } else {
            throw new DmeException("check connectivity of host or horstgroup on dme failed!"+responseEntity.getStatusCodeValue());
        }
        return resultMapList;
    }

    /**
     * @Description: 检查主机或者主机组的联通性
     * @author yc
     * @Date 2021/5/12 15:49
     */
    private List<String> estimateConnectivityOfHostOrHostgroupNew02(String storageId, List<String> objIds, String hostgroupId) throws DmeException {
        Map<String, Object> requestBody = new HashMap<>();
        if (StringUtils.isEmpty(storageId)) {
            LOG.error("estimate connectivity of host or hostgroup storageid param error!", storageId);
            throw new DmeException("estimate connectivity of host or hostgroup , storageid param error!");
        }
        requestBody.put("storage_id", storageId);
        if (!CollectionUtils.isEmpty(objIds)) {
            //检查主机连通性参数
            requestBody.put("host_ids", objIds);
        }
        if (!StringUtils.isEmpty(hostgroupId)) {
            //检查主机组连通性参数
            requestBody.put("hostgroup_id", hostgroupId);
        }
        String url = DmeConstants.DME_ESTIMATE_CONNECTIVITY;
        Map<String, Object> param = new HashMap<>();
        LOG.info("check connectivity of host or horstgroup on dme ! {" + gson.toJson(requestBody) + "}");
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, gson.toJson(requestBody));
        List<String> resultMapList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(objIds) && responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
            String body = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("results").getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                String id = ToolUtils.jsonToStr(element.get("host_id"));
                String status = ToolUtils.jsonToStr(element.get("status"));
                // 连通性异常主机结果统计
                if (status.equalsIgnoreCase("NOT_CONNECT")) {
                    resultMapList.add(id);
                }
            }
        }else if (!StringUtils.isEmpty(hostgroupId) && responseEntity.getStatusCodeValue() == HttpStatus.OK.value() ){
            String body = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("results").getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                String id = ToolUtils.jsonToStr(element.get("host_id"));
                String hostName = ToolUtils.jsonToStr(element.get("host_name"));
                String status = ToolUtils.jsonToStr(element.get("status"));
                // 连通性异常主机结果统计
                if (status.equalsIgnoreCase("NOT_CONNECT")) {
                    throw new DmeException("check connectivity of hostgroup on dme failed! name : "+hostName);
                }
            }
        }else {
            throw new DmeException("check connectivity of host or hostgroup on dme failed!"+responseEntity.getStatusCodeValue() );
        }
        return resultMapList;
    }

    /**
     * @Description: 检查主机组是否存在，不存在就创建主机组
     * @author yc
     * @Date 2021/5/12 16:17
     */
    private List<String> getOrCreateHostGroupIdNew(List<String> clusterIdList, String volumeId) throws DmeException {

        ArrayList<String> objIds = new ArrayList<String>();
        String subVolumeId = ToolUtils.handleString(volumeId);
        // 取得集群下的所有主机
        for (String clusterObjectId : clusterIdList) {
            String objId = "";
            String vmwarehosts = vcsdkUtils.getHostsOnCluster(clusterObjectId);
            if (StringUtils.isEmpty(vmwarehosts)) {
                new DmeException("the cluster has no host");
            }

            List<Map<String, String>> vmwarehostlists = gson.fromJson(vmwarehosts,
                    new TypeToken<List<Map<String, String>>>() {
                    }.getType());
            if (vmwarehostlists != null && vmwarehostlists.size() > 0) {
                // 分别检查每一个主机是否存在，如果不存在就创建
                List<String> hostlists = new ArrayList<>();
                Map<String, List<Map<String, Object>>> allinitionators = getAllInitionator();
                for (Map<String, String> hostmap : vmwarehostlists) {
                    String tmpHostId = checkOrCreateToHost(ToolUtils.getStr(hostmap.get(HOST_NAME)),
                            ToolUtils.getStr(hostmap.get(HOSTID)), allinitionators);
                    if (!StringUtils.isEmpty(tmpHostId)) {
                        hostlists.add(tmpHostId);
                    }
                }
                // 在DME中创建主机组
                if (!CollectionUtils.isEmpty(hostlists)) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("cluster",
                            vcsdkUtils.getVcConnectionHelper().objectId2Mor(clusterObjectId).getValue() + "-" + subVolumeId);
                    params.put("hostids", hostlists);
                    Map<String, Object> hostmap = dmeAccessService.createHostGroup(params);
                    if (hostmap != null && hostmap.get(DmeConstants.ID) != null) {
                        objId = ToolUtils.getStr(hostmap.get(ID_FIELD));
                    }
                }
            }
            objIds.add(objId);
        }
        return objIds;
    }

    /**
     * @return taskIds
     * @Description: 将lun映射给主机和主机组并且检查映射状态
     * @Param volumeIds, hostIds, clusterIds
     * @author yc
     * @Date 2021/5/13 15:29
     */
    private Map<String, List<String>> lunMappingToHostOrHostgroupNew(List<String> volumeIds, List<String> hostIds, List<String> clusterIds) {
        Map<String, Object> params = new HashMap<>();
        Map<String, List<String>> mappingResultMap = new HashMap<>();
        List<String> mappedList = new ArrayList<>();
        List<String> unMappedList = new ArrayList<>();
        ResponseEntity<String> responseEntity = null;
        String descriptionEn = null;
        String descriptionCn = null;

        if (!CollectionUtils.isEmpty(clusterIds)) {
            String url = DmeConstants.MOUNT_VOLUME_TO_HOSTGROUP_URL;
            for (String clusterId : clusterIds) {
                params.put(HOST_GROUP_ID1, clusterId);
                params.put(VOLUME_IDS, volumeIds);
                LOG.info("lun mapping to host or hostgroup request params:{}", gson.toJson(params));
                try {
                    responseEntity = dmeAccessService.access(url, HttpMethod.POST, gson.toJson(params));
                } catch (Exception e) {
                    unMappedList.add(clusterId);
                }
                if (!StringUtils.isEmpty(responseEntity) && responseEntity.getStatusCodeValue() == HttpStatus.ACCEPTED.value()) {
                    JsonObject object = new JsonParser().parse(responseEntity.getBody()).getAsJsonObject();
                    String taskId = ToolUtils.jsonToStr(object.get("task_id"));
                    TaskDetailInfoNew result = taskService.queryTaskByIdReturnMainTask(taskId, longTaskTimeOut);
                    if (3 == result.getStatus() && 100 == result.getProgress()) {
                        mappedList.add(clusterId);
                    } else {
                        unMappedList.add(clusterId);
                        descriptionEn = result.getDetailEn();
                        descriptionCn = result.getDetailCn();
                    }
                } else {
                    unMappedList.add(clusterId);
                    descriptionEn = "request lun mapping to hostgroup return error!";
                    descriptionCn = "请求DME，将Lun映射给主机组，DME返回失败！";
                }
                //将联通性检测失败的主机组也放至映射失败的集合中
                mappingResultMap.put("clusterMapped", mappedList);
                mappingResultMap.put("clusterUnmapped", unMappedList);
            }
        } else if (!CollectionUtils.isEmpty(hostIds)) {
            String url = DmeConstants.DME_HOST_MAPPING_URL;
            for (String hostId : hostIds) {
                params.put(HOST_ID, hostId);
                params.put(VOLUME_IDS, volumeIds);
                LOG.info("lun mapping to host or hostgroup request params:{}", gson.toJson(params));
                try {
                    responseEntity = dmeAccessService.access(url, HttpMethod.POST, gson.toJson(params));
                }catch (Exception e){
                    unMappedList.add(hostId);
                }
                if (!StringUtils.isEmpty(responseEntity) && responseEntity.getStatusCodeValue() == HttpStatus.ACCEPTED.value()) {
                    JsonObject object = new JsonParser().parse(responseEntity.getBody()).getAsJsonObject();
                    String taskId = ToolUtils.jsonToStr(object.get("task_id"));
                    TaskDetailInfoNew result = taskService.queryTaskByIdReturnMainTask(taskId, longTaskTimeOut);
                    if (3 == result.getStatus() && 100 == result.getProgress()) {
                        mappedList.add(hostId);
                    } else {
                        unMappedList.add(hostId);
                        descriptionEn = result.getDetailEn();
                        descriptionCn = result.getDetailCn();
                    }
                } else {
                    unMappedList.add(hostId);
                    descriptionEn = "request lun mapping to host return error!";
                    descriptionCn = "请求DME，将Lun映射给主机，DME返回失败！";

                }
                //将联通性检测失败的主机也放至映射失败的集合中
                mappingResultMap.put("hostMapped", mappedList);
                mappingResultMap.put("hostUnmapped", unMappedList);
            }
        }
        if (!StringUtils.isEmpty(descriptionEn)) {
            mappingResultMap.put("descriptionEN", Arrays.asList(descriptionEn));
            mappingResultMap.put("descriptionCN", Arrays.asList(descriptionCn));
        }
        return mappingResultMap;
    }

    private Map<String,Map<String, List<String>>>  lunMappingToHostOrHostgroupForCreate(List<String> volumeIds, List<String> hostIds, List<String> clusterIds)
            throws DmeException {
       Map<String, Object> params = new HashMap<>();
        ResponseEntity<String> responseEntity = null;

       /*
        Map<String, List<String>> mappingResultMap = new HashMap<>();
        List<String> mappedList = new ArrayList<>();
        List<String> unMappedList = new ArrayList<>();
        ResponseEntity<String> responseEntity = null;
        String descriptionEn = null;
        String descriptionCn = null;*/
        Map<String,Map<String, List<String>>> resultMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(clusterIds)) {
            String url = DmeConstants.MOUNT_VOLUME_TO_HOSTGROUP_URL;
            for (String clusterId : clusterIds) {
                Map<String,List<String>> mappedResult = new HashMap<>();
                List<String> mappedList = new ArrayList<>();
                List<String> unMappedList = new ArrayList<>();
                for (String volumid :volumeIds ) {
                    params.put(HOST_GROUP_ID1, clusterId);
                    params.put(VOLUME_IDS, Arrays.asList(volumid));
                    LOG.info("lun mapping to host or hostgroup request params:{}", gson.toJson(params));
                    try {
                        responseEntity = dmeAccessService.access(url, HttpMethod.POST, gson.toJson(params));
                    } catch (Exception e) {
                        unMappedList.add(volumid);
                    }
                    if (!StringUtils.isEmpty(responseEntity) && responseEntity.getStatusCodeValue() == HttpStatus.ACCEPTED.value()) {
                        JsonObject object = new JsonParser().parse(responseEntity.getBody()).getAsJsonObject();
                        String taskId = ToolUtils.jsonToStr(object.get("task_id"));
                        TaskDetailInfoNew result = taskService.queryTaskByIdReturnMainTask(taskId, longTaskTimeOut);
                        if (3 == result.getStatus() && 100 == result.getProgress()) {
                            mappedList.add(volumid);
                        } else {
                            unMappedList.add(volumid);
                           /* descriptionEn = result.getDetailEn();
                            descriptionCn = result.getDetailCn();*/
                        }
                    } else {
                        unMappedList.add(volumid);
                        /*descriptionEn = "request lun mapping to hostgroup return error!";
                        descriptionCn = "请求DME，将Lun映射给主机组，DME返回失败！";*/
                    }
                    //将联通性检测失败的主机组也放至映射失败的集合中
                    mappedResult.put("clusterMapped", mappedList);
                    mappedResult.put("clusterUnmapped", unMappedList);
                }
                resultMap.put(clusterId,mappedResult);
            }

        } else if (!CollectionUtils.isEmpty(hostIds)) {
            String url = DmeConstants.DME_HOST_MAPPING_URL;
            for (String hostId : hostIds) {
                Map<String, List<String>> mappedResult = new HashMap<>();
                List<String> mappedList = new ArrayList<>();
                List<String> unMappedList = new ArrayList<>();
                for (String volumid : volumeIds) {
                    params.put(HOST_ID, hostId);
                    params.put(VOLUME_IDS, Arrays.asList(volumid));
                    LOG.info("lun mapping to host or hostgroup request params:{}", gson.toJson(params));
                    responseEntity = dmeAccessService.access(url, HttpMethod.POST, gson.toJson(params));
                    if (!StringUtils.isEmpty(responseEntity) && responseEntity.getStatusCodeValue() == HttpStatus.ACCEPTED.value()) {
                        JsonObject object = new JsonParser().parse(responseEntity.getBody()).getAsJsonObject();
                        String taskId = ToolUtils.jsonToStr(object.get("task_id"));
                        TaskDetailInfoNew result = taskService.queryTaskByIdReturnMainTask(taskId, longTaskTimeOut);
                        if (3 == result.getStatus() && 100 == result.getProgress()) {
                            mappedList.add(volumid);
                        } else {
                            unMappedList.add(volumid);
                           /* descriptionEn = result.getDetailEn();
                            descriptionCn = result.getDetailCn();*/
                        }
                    } else {
                        unMappedList.add(volumid);
                       /* descriptionEn = "request lun mapping to host return error!";
                        descriptionCn = "请求DME，将Lun映射给主机，DME返回失败！";*/

                    }
                    //将联通性检测失败的主机也放至映射失败的集合中
                    mappedResult.put("hostMapped", mappedList);
                    mappedResult.put("hostUnmapped", unMappedList);
                }
                resultMap.put(hostId,mappedResult);
            }
        }
        /*if (!StringUtils.isEmpty(descriptionEn)) {
            mappingResultMap.put("descriptionEN", Arrays.asList(descriptionEn));
            mappingResultMap.put("descriptionCN", Arrays.asList(descriptionCn));
        }*/
        return resultMap;
    }

    private Map<String,List<String>>  lunMappingToHostOrHostgroupForCreate02(List<String> volumeIds, List<String> hostIds,
                                                                                          List<String> clusterIds,
                                                                             List<Map<String, Object>> volumelistLst, List<String> desc) {
        Map<String, Object> params = new HashMap<>();
        ResponseEntity<String> responseEntity = null;

        Map<String,List<String>> resultMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(clusterIds)) {
            String url = DmeConstants.MOUNT_VOLUME_TO_HOSTGROUP_URL;
            for (String clusterId : clusterIds) {
                Map<String,List<String>> mappedResult = new HashMap<>();
                List<String> mappedList = new ArrayList<>();
                Set<String> mappedSet = new HashSet<>();
                params.put(HOST_GROUP_ID1, clusterId);
                    params.put(VOLUME_IDS, volumeIds);
                    LOG.info("lun mapping to host or hostgroup request params:{}", gson.toJson(params));
                    try {
                        responseEntity = dmeAccessService.access(url, HttpMethod.POST, gson.toJson(params));
                    } catch (Exception e) {
                        LOG.error(clusterId+":map lun to hostgroup error"+responseEntity);
                    }
                    if (!StringUtils.isEmpty(responseEntity) && responseEntity.getStatusCodeValue() == HttpStatus.ACCEPTED.value()) {
                        JsonObject object = new JsonParser().parse(responseEntity.getBody()).getAsJsonObject();
                        String taskId = ToolUtils.jsonToStr(object.get("task_id"));
                        try {
                            List<TaskDetailInfoNew> taskDetailInfoNewList = taskService.getTaskInfo(taskId, longTaskTimeOut);
                            LOG.info(gson.toJson(taskDetailInfoNewList));
                            //首先获取主任务信息
                            TaskDetailInfoNew mainTask = taskService.getMainTaskInfo(taskId, taskDetailInfoNewList);
                            if (StringUtils.isEmpty(mainTask)) {
                                throw new DmeException("map vmfs error:get main task info error");
                            } else if (mainTask.getStatus() > 4) {
                                throw new DmeException("map vmfs error" + mainTask.getDetailEn());
                            }
                            String mainId = mainTask.getId();
                            if (StringUtils.isEmpty(mainId)) {
                                throw new DmeException("map vmfs error:get task info error");
                            }
                            String id = getMapMainChildernId(mainId, taskDetailInfoNewList);
                            List<TaskDetailInfoNew> mapTaskInfo = getMapInfos(id, taskDetailInfoNewList);
                            if (CollectionUtils.isEmpty(mapTaskInfo)) {
                                if (!StringUtils.isEmpty(mainTask.getDetailEn())){
                                desc.add(mainTask.getDetailEn());}
                                throw new DmeException("map vmfs error:get task info error");
                            }
                            List<String> mappedLunName = getMappedLunName(mapTaskInfo);
                            if (CollectionUtils.isEmpty(mappedLunName)) {
                                throw new DmeException("map vmfs error:get task info error");
                            }
                            //根据隐射成功的volumName获取volumid
                            for (Map<String, Object> volumMap : volumelistLst) {
                                if (!CollectionUtils.isEmpty(volumMap) && mappedLunName.contains(ToolUtils.getStr(volumMap.get(VOLUME_NAME)))) {
                                    mappedSet.add(ToolUtils.getStr(volumMap.get(VOLUME_ID)));
                                    mappedList = new ArrayList<>(mappedSet);
                                }
                            }
                        }catch (Exception e){
                            LOG.error(e.getMessage());
                        }
                    }
                resultMap.put(clusterId,mappedList);
            }

        } else if (!CollectionUtils.isEmpty(hostIds)) {
            String url = DmeConstants.DME_HOST_MAPPING_URL;
            for (String hostId : hostIds) {
                Map<String, List<String>> mappedResult = new HashMap<>();
                List<String> mappedList = new ArrayList<>();
                Set<String> mappedSet = new HashSet<>();
                params.put(HOST_ID, hostId);
                    params.put(VOLUME_IDS, volumeIds);
                    LOG.info("lun mapping to host or hostgroup request params:{}", gson.toJson(params));
                    try{
                    responseEntity = dmeAccessService.access(url, HttpMethod.POST, gson.toJson(params));
                    } catch (Exception e) {
                        LOG.error(hostId+":map lun to host error"+responseEntity);
                    }
                    if (!StringUtils.isEmpty(responseEntity) && responseEntity.getStatusCodeValue() == HttpStatus.ACCEPTED.value()) {
                        JsonObject object = new JsonParser().parse(responseEntity.getBody()).getAsJsonObject();
                        String taskId = ToolUtils.jsonToStr(object.get("task_id"));
                        try {
                            List<TaskDetailInfoNew> taskDetailInfoNewList = taskService.getTaskInfo(taskId, longTaskTimeOut);
                            LOG.info(gson.toJson(taskDetailInfoNewList));
                            //首先获取主任务信息
                            TaskDetailInfoNew mainTask = taskService.getMainTaskInfo(taskId, taskDetailInfoNewList);
                            if (StringUtils.isEmpty(mainTask)) {
                                throw new DmeException("map vmfs error:get main task info error");
                            } else if (mainTask.getStatus() > 4) {
                                throw new DmeException("map vmfs error" + mainTask.getDetailEn());
                            }
                            String mainId = mainTask.getId();
                            if (StringUtils.isEmpty(mainId)) {
                                throw new DmeException("map vmfs error:get task info error");
                            }
                            String id = getMapMainChildernId(mainId, taskDetailInfoNewList);
                            List<TaskDetailInfoNew> mapTaskInfo = getMapInfos(id, taskDetailInfoNewList);
                            if (CollectionUtils.isEmpty(mapTaskInfo)) {
                                if (!StringUtils.isEmpty(mainTask.getDetailEn())){
                                    desc.add(mainTask.getDetailEn());}
                                throw new DmeException("map vmfs error:get task info error");
                            }
                            List<String> mappedLunName = getMappedLunName(mapTaskInfo);
                            if (CollectionUtils.isEmpty(mappedLunName)) {
                                throw new DmeException("map vmfs error:get task info error");
                            }
                            //根据隐射成功的volumName获取volumid
                            for (Map<String, Object> volumMap : volumelistLst) {
                                if (!CollectionUtils.isEmpty(volumMap) && mappedLunName.contains(ToolUtils.getStr(volumMap.get(VOLUME_NAME)))) {
                                    mappedSet.add(ToolUtils.getStr(volumMap.get(VOLUME_ID)));
                                    mappedList = new ArrayList<>(mappedSet);
                                }
                            }
                        }catch (Exception e){
                            LOG.error(e.getMessage());
                        }
                }
                resultMap.put(hostId,mappedList);
            }
        }
        return resultMap;
    }
    /**
     * @return @return
     * @throws
     * @Description: 回滚函数
     * @Param @param null
     * @author yc
     * @Date 2021/5/13 17:22
     */
    private void rollBackNew(String volumeId, List<String> dmeHostIds, List<String> demHostGroupIds, Boolean isCreated, Boolean isMapping) throws DmeException {
        ResponseEntity responseEntity = null;
        String taskId = "";
        if (!StringUtils.isEmpty(volumeId)) {
            Map<String, Object> requestParam = new HashMap<>();
            //解除映射，删除已创建的卷
            requestParam.put(VOLUMEIDS, Arrays.asList(volumeId));
            if (isCreated && isMapping) {
                if (!CollectionUtils.isEmpty(dmeHostIds) && isMapping) {
                    for (String dmeHostId : dmeHostIds) {
                        requestParam.put(HOST_ID, dmeHostId);
                        responseEntity = hostUnmapping(requestParam);
                    }
                }
                if (!CollectionUtils.isEmpty(demHostGroupIds) && isMapping) {
                    for (String demHostGroupId : demHostGroupIds) {
                        requestParam.put(HOST_GROUP_ID1, demHostGroupId);
                        responseEntity = hostGroupUnmapping(requestParam);
                    }
                }
                if (responseEntity.getStatusCodeValue() == HttpStatus.ACCEPTED.value()) {
                    taskId = ToolUtils.jsonToStr(
                            new JsonParser().parse(ToolUtils.getStr(responseEntity.getBody())).getAsJsonObject()
                                    .get("task_id"));
                }
            }
            Set<String> taskIds = new HashSet<>();
            taskIds.add(taskId);
            if (taskId.equals("") || taskService.checkTaskStatusLarge(taskIds, longTaskTimeOut)) {
                volumeDelete(requestParam);
                if (!CollectionUtils.isEmpty(demHostGroupIds)) {
                    for (String demHostGroupId : demHostGroupIds) {
                        deleteHostgroup(demHostGroupId);
                    }
                }
            }
        }
    }

    /**
     * @return @return
     * @throws
     * @Description: vmfs挂载新方法，支持批量选择主机或者主机组
     * @Param @param null
     * @author yc
     * @Date 2021/5/14 10:41
     */
    @Override
    public MountVmfsReturn mountVmfsNew(Map<String, Object> params) throws DmeException {
        //校验参数不能为空
        //todo 增加逻辑（如果存储设备是以主机创建，挂载时就只支持按主机挂载）
        if (CollectionUtils.isEmpty(params)) {
            throw new DmeException("mount vmfs error, params is null");
        }
        //获取前端页面入参
        Object ss = params.get(CHOOSEDEVICE);
        //校验前端选择的主机或者主机组不能为空
        if (StringUtils.isEmpty(params.get(CHOOSEDEVICE))) {
            throw new DmeException("mount vmfs the params is error");
        }
        List<Map<String, String>> chooseDevicelists = gson.fromJson(gson.toJsonTree(ss),
                new TypeToken<List<Map<String, String>>>() {
                }.getType());

        if (CollectionUtils.isEmpty(chooseDevicelists)) {
            throw new DmeException("mount vmfs the params is error");
        }
        HashSet<String> deviceTypeSet = getDeviceTypeSet2(chooseDevicelists);
        //根据前端的存储获取存储的创建方式以及每个存储所对应的卷信息
        HashMap<String, Map<String, String>> dataStorageMap = new HashMap<String, Map<String, String>>();
        String dataStoreObjectId = null;
        String hostVolumid = null;
        String hostGroupVolumid = null;

        HashSet<String> createTypeSet = new HashSet<String>();
        if (params.get(DATASTORE_OBJECT_IDS) != null) {
            List<String> dataStoreObjectIds = (List<String>) params.get(DATASTORE_OBJECT_IDS);
            if (!CollectionUtils.isEmpty(dataStoreObjectIds) && dataStoreObjectIds.size() == 1) {
                dataStoreObjectId = dataStoreObjectIds.get(0);
            } else {
                throw new DmeException("mount vmfs the params is error");
            }
            if (StringUtils.isEmpty(dataStoreObjectId)) {
                throw new DmeException("mount vmfs the params is error");
            }
            getDataStorageInfo(dataStorageMap, dataStoreObjectId);
        }
        //对前端入参逻辑进行校验

        String type = dataStorageMap.get(dataStoreObjectId).get(TYPE);
        if (StringUtils.isEmpty(type)) {
            throw new DmeException("get method creating vmfs error ");
        }
        createTypeSet.add(type);
        if (HOST.equalsIgnoreCase(type)) {
            hostVolumid = dataStorageMap.get(dataStoreObjectId).get("volumeId");
        } else {
            hostGroupVolumid = dataStorageMap.get(dataStoreObjectId).get("volumeId");
        }
        Map<String, String> hostIdToIp = getHostNameAndHostIdList(chooseDevicelists);
        List<String> hostIds = new ArrayList<>(hostIdToIp.keySet());
        List<String> clusterIds = getClusterIdList(chooseDevicelists);
        Set<String> needMountIpSet = new HashSet<>();

        //获取集群下的主机数量;
        if (!CollectionUtils.isEmpty(clusterIds)) {
            for (String clusterid : clusterIds) {
                List<Map<String, String>> hosts = vcsdkUtils.getHostsOnClusterNew(clusterid);
                if (!CollectionUtils.isEmpty(hosts)){
                    for (Map<String, String> hostinfo : hosts){
                        if (!StringUtils.isEmpty(hostinfo.get(HOST_NAME))){
                            needMountIpSet.add(hostinfo.get(HOST_NAME));
                        }
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(hostIdToIp)) {
            for (String hostId : hostIdToIp.keySet()) {
                if (!StringUtils.isEmpty(hostIdToIp.get(hostId))) {
                    needMountIpSet.add(hostIdToIp.get(hostId));
                }
            }
        }
        List<String> needMountIps = new ArrayList<>(needMountIpSet);
        int num = needMountIps.size();
        List<String> mountedIps = new ArrayList<>();
        //获取存储已经挂载的主机
        List<Map<String, String>> mounted = vcsdkUtils.getHostsByDsObjectIdNew(ToolUtils.getStr(params.get(DATASTORE_OBJECT_IDS)), true);
        if (!CollectionUtils.isEmpty(mounted)){
            for (Map<String, String> hostinfo : mounted){
                if (!StringUtils.isEmpty(hostinfo.get(HOST_NAME))){
                    mountedIps.add(hostinfo.get(HOST_NAME));
                }
            }
        }
        String desc = "";
        List<String> connectionFail = new ArrayList<>();
        //1.前端入参的选择的挂载方式为主机
        if (deviceTypeSet.size() == 1 && deviceTypeSet.contains(HOST)) {
            //1.1如果存储的创建方式为主机，检查主机连通性，将原有的lun映射给新的主机
            if (!StringUtils.isEmpty(hostVolumid)) {
                Map<String, List<Map<String, Object>>> allinitionators = getAllInitionator();
                Map<String, Object> paraMap = new HashMap<>();
                paraMap.put(STORAGE_ID, dataStorageMap.get(dataStoreObjectId).get("storageId"));
                Map<String, List<String>> hostStatusMap1 = checkHostNewNoCheckCluster(paraMap, allinitionators, chooseDevicelists);
                List<String> objHostIds = hostStatusMap1.get("nomalHost");
                if (CollectionUtils.isEmpty(objHostIds)){
                    desc = "the connectivity of host is error";
                }
                if (!CollectionUtils.isEmpty(hostStatusMap1.get("failHostName"))){
                    connectionFail.addAll(hostStatusMap1.get("failHostName"));
                }
                List<String> unmappingAndNomalHostids = getUnmappingAndNomalHost(hostVolumid, objHostIds);
                Map<String, List<String>> mappingResult1 = lunMappingToHostOrHostgroupNew(Arrays.asList(hostVolumid), unmappingAndNomalHostids, null);
                if (!StringUtils.isEmpty(mappingResult1.get("descriptionEN"))){
                    if (StringUtils.isEmpty(desc)){
                        desc = mappingResult1.get("descriptionEN").toString();
                    }else {
                        desc = desc + ";" + mappingResult1.get("descriptionEN").toString();
                    }
                }
                List<String> mappedHostid = mappingResult1.get("hostMapped");
                if (!CollectionUtils.isEmpty(objHostIds) && CollectionUtils.isEmpty(unmappingAndNomalHostids)) {
                    mappedHostid = objHostIds;
                }
                if (!CollectionUtils.isEmpty(mappedHostid)) {
                    try {
                        for (String hostId : hostIds) {
                            Map<String, Object> dsmap = new HashMap<>();
                            dsmap.put(NAME_FIELD, dataStorageMap.get(dataStoreObjectId).get("dataStoreName"));
                            vcsdkUtils.mountVmfsOnClusterNew(gson.toJson(dsmap), null, hostId, dataStoreObjectId);
                        }
                    } catch (Exception e) {
                        //如果抛出异常，需要解除映射
                        HashMap<String, Object> requestParam = new HashMap<>();
                        requestParam.put(VOLUMEIDS, Arrays.asList(hostVolumid));
                        for (String dmeHostId : mappedHostid) {
                            requestParam.put(HOST_ID, dmeHostId);
                            try {
                                ResponseEntity responseEntity = hostUnmapping(requestParam);
                                LOG.info("mountVmfsNew rollback:" + responseEntity);
                            }catch (Exception ex) {
                                LOG.error("mountVmfsNew rollback:" + ex.getMessage());
                            }
                        }
                    }
                }
                //1.2如果存储的创建方式为集群，需要判断前端入参的主机是否包含在已经映射的集群中，包含的话，将主机加入主机组，继续挂载；不包含，将主机以单主机的方式映射
            } else if (!StringUtils.isEmpty(hostGroupVolumid)) {
                if (!CollectionUtils.isEmpty(hostIds)) {
                    //根据非独立出来的主机重新组装对象
                    List<Map<String, String>> chooseDeviceNew = buildChooseDevice(chooseDevicelists, hostIds);
                    Map<String, List<Map<String, Object>>> allinitionators = getAllInitionator();
                    Map<String, Object> paraMap = new HashMap<>();
                    paraMap.put(STORAGE_ID, dataStorageMap.get(dataStoreObjectId).get("storageId"));
                    Map<String, List<String>> hostStatusMap1 = checkHostNewNoCheckCluster(paraMap, allinitionators, chooseDeviceNew);
                    List<String> objHostIds = hostStatusMap1.get("nomalHost");
                    if (CollectionUtils.isEmpty(objHostIds)){
                        desc = "the connectivity of host is error";
                    }
                    if (!CollectionUtils.isEmpty(hostStatusMap1.get("failHostName"))){
                        connectionFail.addAll(hostStatusMap1.get("failHostName"));
                    }
                    //增加查询lun对应的已经映射的主机，映射之前从连通性正常的主机组中移除已经映射的主机
                    List<String> unmappingAndNomalHostids = getUnmappingAndNomalHost(hostGroupVolumid, objHostIds);
                    //todo 增加将主机加入主机组的操作，如果有什么问题无法处理
                    desc = addHostToHostGroup(hostIds,hostGroupVolumid,dataStoreObjectId,hostIdToIp,unmappingAndNomalHostids,allinitionators);
                    Map<String, List<String>> mappingResult1 = lunMappingToHostOrHostgroupNew(Arrays.asList(hostGroupVolumid), unmappingAndNomalHostids, null);
                    if (!StringUtils.isEmpty(mappingResult1.get("descriptionEN"))){
                        if (StringUtils.isEmpty(desc)){
                            desc = mappingResult1.get("descriptionEN").toString();
                        }else {
                            desc = desc + ";" + mappingResult1.get("descriptionEN").toString();
                        }
                    }
                    List<String> mapingHostid = mappingResult1.get("hostMapped");
                    if (!CollectionUtils.isEmpty(objHostIds) && CollectionUtils.isEmpty(unmappingAndNomalHostids)) {
                        mapingHostid = objHostIds;
                    }
                    if (!CollectionUtils.isEmpty(mapingHostid)) {
                        try {
                            for (String hostId : hostIds) {
                                Map<String, Object> dsmap = new HashMap<>();
                                dsmap.put(NAME_FIELD, dataStorageMap.get(dataStoreObjectId).get("dataStoreName"));
                                vcsdkUtils.mountVmfsOnClusterNew(gson.toJson(dsmap), null, hostId, dataStoreObjectId);
                            }
                        } catch (Exception e) {
                            //如果抛出异常，需要解除映射
                            HashMap<String, Object> requestParam = new HashMap<>();
                            requestParam.put(VOLUMEIDS, Arrays.asList(hostGroupVolumid));
                            for (String dmeHostId : mapingHostid) {
                                requestParam.put(HOST_ID, dmeHostId);
                                try {
                                    ResponseEntity responseEntity = hostUnmapping(requestParam);
                                    LOG.info("mountVmfsNew rollback:" + responseEntity);
                                }catch (Exception es){
                                    LOG.error("mountVmfsNew rollback:" + es.getMessage());
                                }
                            }
                        }
                    }
                }
            }
        }
        //2.前端入参的选择的挂载方式为集群
        if (deviceTypeSet.size() == 1 && deviceTypeSet.contains(CLUSTER)) {
            //2.1如果存储的创建方式为主机，直接抛出异常
            if (!StringUtils.isEmpty(hostVolumid)) {
                throw new DmeException("vmfs was created with host not allow to mount the cluster");
            }
            //2.2如果存储的创建方式为集群，将lun隐射为新的主机组,然后将存储挂载在集群上；
            if (!CollectionUtils.isEmpty(clusterIds) && !StringUtils.isEmpty(hostGroupVolumid)) {
                //2.2.1检查集群的联通性
                HashMap<String, Object> param = new HashMap<String, Object>();
                param.put(STORAGE_ID, dataStorageMap.get(dataStoreObjectId).get("storageId"));
                List<Map<String, List<String>>> maps = checkOrCreateToHostGroupNew2(param, chooseDevicelists, hostGroupVolumid);
                List<String> hostGroupIds = new ArrayList<>();
                for (Map<String, List<String>> map : maps) {
                    if (!CollectionUtils.isEmpty(map.get("nomalCluster"))) {
                        hostGroupIds.addAll(map.get("nomalCluster"));
                    }
                    if (!CollectionUtils.isEmpty(map.get("failHostname"))) {
                        connectionFail.addAll(map.get("failHostname"));
                    }
                }
                //增加将已经映射的主机组从主机组中移除
                List<String> mappedHostGroupId = getMappedHostGroupIds(hostGroupVolumid);
                if (!CollectionUtils.isEmpty(mappedHostGroupId)){
                    hostGroupIds.removeAll(mappedHostGroupId);
                }
                //maps.stream().forEach(map -> hostGroupIds.addAll(map.get("nomalCluster")));
                if (!CollectionUtils.isEmpty(hostGroupIds)) {
                    //将lun映射给主机组，然后挂载集群
                    for (String hostGroupId : hostGroupIds) {
                        String taskid = lunMappingToHostOrHostgroup(Arrays.asList(hostGroupVolumid), null, hostGroupId);
                        TaskDetailInfoNew result = taskService.queryTaskByIdReturnMainTask(taskid, longTaskTimeOut);
                        if (3 < result.getStatus() && !StringUtils.isEmpty(result.getDetailEn())){
                            if (StringUtils.isEmpty(desc)){
                                desc = result.getDetailEn();
                            }else {
                                desc = desc + ";" +result.getDetailEn();
                            }
                        }
                    }
                    Map<String, Object> dsmap = new HashMap<>();
                    dsmap.put(NAME_FIELD, dataStorageMap.get(dataStoreObjectId).get("dataStoreName"));
                    for (String cluster : clusterIds) {
                        try {
                            vcsdkUtils.mountVmfsOnClusterNew(gson.toJson(dsmap), cluster, null, dataStoreObjectId);
                        }catch (Exception esx){
                            LOG.error(esx.getMessage());
                        }
                    }
                } else {
                    throw new DmeException("check connectivity of cluster error");
                }
            }
        }
        //3.前端入参的选择方式为集群加主机，此时只支持以集群方式创建的数据存储。
        if (deviceTypeSet.size() > 1) {
            if (!StringUtils.isEmpty(hostVolumid)) {
                throw new DmeException("vmfs was created with host not allow to mount the cluster");
            }
            //3.1处理主机集合
            if (!CollectionUtils.isEmpty(hostIds)) {
                //根据非独立出来的主机重新组装对象
                List<Map<String, String>> chooseDeviceNew = buildChooseDevice(chooseDevicelists, hostIds);
                Map<String, List<Map<String, Object>>> allinitionators = getAllInitionator();
                Map<String, Object> paraMap = new HashMap<>();
                paraMap.put(STORAGE_ID, dataStorageMap.get(dataStoreObjectId).get("storageId"));
                Map<String, List<String>> hostStatusMap1 = checkHostNewNoCheckCluster(paraMap, allinitionators, chooseDeviceNew);
                List<String> objHostIds = hostStatusMap1.get("nomalHost");
                if (CollectionUtils.isEmpty(objHostIds)){
                    desc = "the connectivity of host is error";
                }
                if (!CollectionUtils.isEmpty(hostStatusMap1.get("failHostName"))){
                    connectionFail.addAll(hostStatusMap1.get("failHostName"));
                }
                //增加查询lun对应的已经映射的主机，映射之前从连通性正常的主机组中移除已经映射的主机
                List<String> unmappingAndNomalHostids = getUnmappingAndNomalHost(hostGroupVolumid, objHostIds);
                desc = addHostToHostGroup(hostIds,hostGroupVolumid,dataStoreObjectId,hostIdToIp,unmappingAndNomalHostids,allinitionators);
                Map<String, List<String>> mappingResult1 = lunMappingToHostOrHostgroupNew(Arrays.asList(hostGroupVolumid), unmappingAndNomalHostids, null);
                if (!StringUtils.isEmpty(mappingResult1.get("descriptionEN"))){
                    if (StringUtils.isEmpty(desc)){
                        desc = mappingResult1.get("descriptionEN").toString();
                    }else {
                        desc = desc + ";" + mappingResult1.get("descriptionEN").toString();
                    }
                }
                List<String> mapingHostid = mappingResult1.get("hostMapped");
                if (!CollectionUtils.isEmpty(objHostIds) && CollectionUtils.isEmpty(unmappingAndNomalHostids)) {
                    mapingHostid = objHostIds;
                }
                if (!CollectionUtils.isEmpty(mapingHostid)) {
                    try {
                        for (String hostId : hostIds) {
                            Map<String, Object> dsmap = new HashMap<>();
                            dsmap.put(NAME_FIELD, dataStorageMap.get(dataStoreObjectId).get("dataStoreName"));
                            vcsdkUtils.mountVmfsOnClusterNew(gson.toJson(dsmap), null, hostId, dataStoreObjectId);
                        }
                    } catch (Exception e) {
                        //如果抛出异常，需要解除映射
                        HashMap<String, Object> requestParam = new HashMap<>();
                        requestParam.put(VOLUMEIDS, Arrays.asList(hostGroupVolumid));
                        for (String dmeHostId : mapingHostid) {
                            requestParam.put(HOST_ID, dmeHostId);
                            try {
                                ResponseEntity responseEntity = hostUnmapping(requestParam);
                                LOG.info("mountVmfsNew rollback:" + responseEntity);
                            }catch (Exception excep){
                                LOG.error("mountVmfsNew rollback:" + excep.getMessage());
                            }
                        }
                    }
                }
            }
            //3.2处理集群集合
            if (!CollectionUtils.isEmpty(clusterIds) && !StringUtils.isEmpty(hostGroupVolumid)) {
                //2.2.1检查集群的联通性
                HashMap<String, Object> param = new HashMap<String, Object>();
                param.put(STORAGE_ID, dataStorageMap.get(dataStoreObjectId).get("storageId"));
                List<Map<String, List<String>>> maps = checkOrCreateToHostGroupNew2(param, chooseDevicelists, hostGroupVolumid);
                List<String> hostGroupIds = new ArrayList<>();
                for (Map<String, List<String>> map : maps) {
                    if (!CollectionUtils.isEmpty(map.get("nomalCluster"))) {
                        hostGroupIds.addAll(map.get("nomalCluster"));
                    }
                    if (!CollectionUtils.isEmpty(map.get("failHostname"))) {
                        connectionFail.addAll(map.get("failHostname"));
                    }
                }
                //maps.stream().forEach(map -> hostGroupIds.addAll(map.get("nomalCluster")));
                List<String> mappedHostGroupId = getMappedHostGroupIds(hostGroupVolumid);
                if (!CollectionUtils.isEmpty(mappedHostGroupId)){
                    hostGroupIds.removeAll(mappedHostGroupId);
                }
                if (!CollectionUtils.isEmpty(hostGroupIds)) {
                    //将lun映射给主机组，然后挂载集群
                    for (String hostGroupId : hostGroupIds) {
                        String taskid = lunMappingToHostOrHostgroup(Arrays.asList(hostGroupVolumid), null, hostGroupId);
                        TaskDetailInfoNew result = taskService.queryTaskByIdReturnMainTask(taskid, longTaskTimeOut);
                        if (3 < result.getStatus() && !StringUtils.isEmpty(result.getDetailEn())){
                            if (StringUtils.isEmpty(desc)){
                                desc = result.getDetailEn();
                            }else {
                                desc = desc + ";" +result.getDetailEn();
                            }
                        }
                    }
                    Map<String, Object> dsmap = new HashMap<>();
                    dsmap.put(NAME_FIELD, dataStorageMap.get(dataStoreObjectId).get("dataStoreName"));
                    for (String cluster : clusterIds) {
                        try {
                            vcsdkUtils.mountVmfsOnClusterNew(gson.toJson(dsmap), cluster, null, dataStoreObjectId);
                        }catch (Exception esx){
                            LOG.error(esx.getMessage());
                        }
                    }
                } else {
                    throw new DmeException("check connectivity of cluster error");
                }
            }
        }
        vcsdkUtils.refreshDatastore(dataStoreObjectId);
        try {
            TimeUnit.SECONDS.sleep(3);
        }catch (Exception e){
            LOG.error("scan datastore error");
        }
        List<Map<String, String>> mountedLst = vcsdkUtils.getHostsByDsObjectIdNew(ToolUtils.getStr(params.get(DATASTORE_OBJECT_IDS)), true);
        LOG.info("check mounted result:" + gson.toJson(mountedLst));
        List<String> lstIps = new ArrayList<>();
        if (!CollectionUtils.isEmpty(mountedLst)){
            for (Map<String, String> hostinfo : mountedLst){
                if (!StringUtils.isEmpty(hostinfo.get(HOST_NAME))){
                    lstIps.add(hostinfo.get(HOST_NAME));
                }
            }
        }
        needMountIps.addAll(mountedIps);
        needMountIps.removeAll(lstIps);
        if (CollectionUtils.isEmpty(connectionFail)){
            connectionFail = null;
        }
        if (0<needMountIps.size() && needMountIps.size()< num){
            return new MountVmfsReturn(true,needMountIps,connectionFail,desc);
        }else if (needMountIps.size() == num){
            return new MountVmfsReturn(false,null,connectionFail,desc);
        }else {
            return new MountVmfsReturn(true,null,connectionFail,desc);
        }

    }


    private List<String> getUnmappingAndNomalHost(String hostGroupVolumid, List<String> objHostIds) throws DmeException {
        //首选根据卷id查询卷已经隐射的主机id
        List<Attachment> attachmentList = findMappedHostsAndClusters(hostGroupVolumid);
        List<String> mappedHostid = new ArrayList<>();
        if (!CollectionUtils.isEmpty(attachmentList)){
            for (Attachment attach : attachmentList) {
                if (!StringUtils.isEmpty(StringUtil.dealQuotationMarks(attach.getHostId()))) {
                    mappedHostid.add(StringUtil.dealQuotationMarks(attach.getHostId()));
                }
            }
        }
        List<String> unMappedAndNomalHostIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(mappedHostid) && !CollectionUtils.isEmpty(objHostIds)){
            //将已经映射的主机id从主机中剔除
            for (String hostId : objHostIds) {
                if (!mappedHostid.contains(hostId)){
                    unMappedAndNomalHostIds.add(hostId);
                }
            }
        }else {
            unMappedAndNomalHostIds = objHostIds;
        }
        return  unMappedAndNomalHostIds;
    }

    private List<String> getMappedHostGroupIds(String hostGroupVolumid) throws DmeException {
        //首选根据卷id查询卷已经隐射的主机id
        List<Attachment> attachmentList = findMappedHostsAndClusters(hostGroupVolumid);
        List<String> mappedHostid = new ArrayList<>();
        if (!CollectionUtils.isEmpty(attachmentList)) {
            for (Attachment attach : attachmentList) {
                if (!StringUtils.isEmpty(StringUtil.dealQuotationMarks(attach.getAttachedHostGroup()))) {
                    mappedHostid.add(StringUtil.dealQuotationMarks(attach.getAttachedHostGroup()));
                }
            }
        }
        return mappedHostid;
    }

   /* private List<String> getDmeHostIdByHostId(List<String> dependentHosts, Map<String, String> hostIdToIp) throws DmeException {

        List<String> dmeHostId = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dependentHosts)) {

            for (String hostid : dependentHosts) {
                if (!CollectionUtils.isEmpty(hostIdToIp) && !StringUtils.isEmpty(hostIdToIp.get(hostid))) {
                    List<Map<String, Object>> hostResult = dmeAccessService.getDmeHosts2(hostIdToIp.get(hostid));
                    if (StringUtils.isEmpty(hostResult) || CollectionUtils.isEmpty(hostResult)) {
                        throw new DmeException("get hostid from dme return empty");
                    }
                    String hostDmeId = ToolUtils.getStr(hostResult.get(0).get(ID_FIELD));
                    if (!StringUtils.isEmpty(hostDmeId)) {
                        dmeHostId.add(hostid);
                    }
                }
            }
        }
        return dmeHostId;
    }*/

    private List<String> getIndependentHosts(List<String> hostIds, List<String> nonIndependentHosts) {
        List<String> independentHosts = new ArrayList<>();
        for (String hostid : hostIds) {
            if (!nonIndependentHosts.contains(hostid)) {
                independentHosts.add(hostid);
            }
        }
        return independentHosts;
    }


    private List<String> getValues(List<Map<String, String>> clusterTempList) {
        List<String> values = new ArrayList<>();
        if (!CollectionUtils.isEmpty(clusterTempList)){
            for (Map<String,String> map:clusterTempList) {
                if (!StringUtils.isEmpty(map.get(CLUSTER_ID)));
                values.add(map.get(CLUSTER_ID));
            }
        }
        return values;
    }

    private List<Map<String, String>> buildChooseDevice(List<Map<String, String>> chooseDevicelists, List<String> independentHosts) {
        List<Map<String, String>> chooseDeviceNewlists = new ArrayList<>();
        if (!CollectionUtils.isEmpty(chooseDevicelists) && !CollectionUtils.isEmpty(independentHosts)) {
            for (String hostid : independentHosts) {
                Map<String, String> chooseMap = new HashMap<>();
                for (Map<String, String> choose : chooseDevicelists) {
                    if (hostid.equalsIgnoreCase(choose.get("clusterId"))) {
                        chooseMap.put("deviceId", hostid);
                        chooseMap.put("deviceName", choose.get("clusterName"));
                        chooseMap.put("deviceType", "host");
                    }
                }
                chooseDeviceNewlists.add(chooseMap);
            }

        }
        return chooseDeviceNewlists;
    }

    /**
     * @Description: 获取指定lun最近映射的主机组id
     * @Param clusterVolumIds
     * @return hostgroupids
     * @throws DmeException
     * @author yc
     * @Date 2021/6/4 9:47
     */
    private String getLasterMappingHostGroup(String clusterVolumId) throws DmeException {
        //首先校验入参的集合
        String hostgroupid = null;
        if (!StringUtils.isEmpty(clusterVolumId)){
            //查询指定lun获取最近时间的主机组id
            List<Attachment> attachmentList = findMappedHostsAndClusters(clusterVolumId);
            Attachment maxdatares = attachmentList.stream().filter(attachment -> !StringUtils.isEmpty(attachment.getAttachedHostGroup())).max(Comparator.comparing(attachment1 -> attachment1.getAttachedAtDate())).get();
            if (!StringUtils.isEmpty(maxdatares) && !StringUtils.isEmpty(maxdatares.getAttachedHostGroup())){
                hostgroupid = maxdatares.getAttachedHostGroup();
            }
        }
        if(StringUtils.isEmpty(hostgroupid)){
            throw new DmeException("get hostgroupid error");
        }
        return hostgroupid;
    }
    /**
     * @Description: 根据存储设备获取卷的信息
     * @Param dataStorageMap,dataStoreObjectIds
     * @throws DmeException
     * @author yc
     * @Date 2021/6/3 10:54
     */
    private void getDataStorageInfo (HashMap<String, Map<String, String>> dataStorageMap, String dataStoreObjectId) throws DmeException {
        HashMap<String, String> dataInfo = new HashMap<String, String>();
        DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dataStoreObjectId);
        if (!StringUtils.isEmpty(dvr)) {
            String volumeId = dvr.getVolumeId();
            String dataStoreName = dvr.getStoreName();
            String storageId = dvr.getStorageDeviceId();
            if (StringUtils.isEmpty(volumeId)){
                throw new DmeException("get volumeid fail");
            }
            dataInfo.put("volumeId", volumeId);
            dataInfo.put("dataStoreName", dataStoreName);
            dataInfo.put("storageId", storageId);

            List<Attachment> attachmentList = findMappedHostsAndClusters(volumeId);
            //获取映射数据中时间最早的映射方式
            if (CollectionUtils.isEmpty(attachmentList)){
                throw new DmeException("query lun info error");
            }
            Attachment mindatares = attachmentList.stream().min(Comparator.comparing(attachment -> attachment.getAttachedAtDate())).get();
            //判断时间最小的映射方式（如果为主机组，就返回‘cluster’,如果为主机返回‘host’,其他报错）
            if (!StringUtils.isEmpty(mindatares.getAttachedHostGroup()) && !mindatares.getAttachedHostGroup().equalsIgnoreCase("null")) {
                dataInfo.put(TYPE, CLUSTER);
            }else if (!StringUtils.isEmpty(mindatares.getHostId()) && !mindatares.getHostId().equalsIgnoreCase("null")){
                dataInfo.put(TYPE, HOST);
            }else {
                throw new DmeException("get vmfs create method error ,the dataStoreObjectId = "+dataStoreObjectId);
            }
        }
        dataStorageMap.put(dataStoreObjectId,dataInfo);
    }
    /**
     * @Description: 获取各个主机和其对应主机组的映射关系
     * @Param hostIds
     * @return List<String>
     * @throws VcenterException
     * @author yc
     * @Date 2021/5/20 16:26
     */
    private Map<String,String> gethostToclusterMap(List<String> hostIds) throws VcenterException {
        //首先判断入参
        Map<String,String> hostToclusterMap = new HashMap<>();
        if (CollectionUtils.isEmpty(hostIds))
            return hostToclusterMap;
        List<String> clusteridList = vcsdkUtils.getAllClusterIds();
        //循环主机id,判断主机所属的主机组
        for (String hostid : hostIds) {
            for (String clusterid : clusteridList) {
                List<String> hosts = vcsdkUtils.getHostidsOnCluster(clusterid);
                if (!CollectionUtils.isEmpty(hosts) && hosts.contains(hostid)) {
                    hostToclusterMap.put(hostid,clusterid);
                }
            }
        }
        return hostToclusterMap;
    }
    /**
      * @Description:  将vfms挂载至虚拟机的新方法
      * @Param params,chooseDevicelists
      * @throws  VcenterException
      * @author yc
      * @Date 2021/5/14 15:24
     */
    private void mountVmfsOnVmwareNew(Map<String, Object> params, List<Map<String, String>> chooseDevicelists) throws VcenterException {

        //根据入参将主机和主机组区分开，分别调用vCenter在主机上扫描卷和Datastore
        for (Map<String, String> map : chooseDevicelists) {
            if ("host".equalsIgnoreCase(map.get("deviceType"))) {
                vcsdkUtils.scanDataStore(null, ToolUtils.getStr(map.get("deviceId")));
            }
            if ("cluster".equalsIgnoreCase(map.get("deviceType"))) {
                vcsdkUtils.scanDataStore(ToolUtils.getStr(map.get("deviceId")), null);
            }
        }
        // 如果是需要扫描LUN来挂载，则需要执行下面的方法，dataStoreNames
        if (params.get(DmeConstants.DATASTORENAMES) != null) {
            List<String> dataStoreNames = (List<String>) params.get(DATASTORE_NAMES);
            if (dataStoreNames != null && dataStoreNames.size() > 0) {
                for (String dataStoreName : dataStoreNames) {
                    Map<String, Object> dsmap = new HashMap<>();
                    dsmap.put(NAME_FIELD, dataStoreName);
                    for (Map<String, String> map : chooseDevicelists) {
                        if ("host".equalsIgnoreCase(map.get("deviceType"))) {
                            vcsdkUtils.scanDataStore(null, ToolUtils.getStr(map.get("deviceId")));
                            vcsdkUtils.mountVmfsOnCluster(gson.toJson(dsmap), null,
                                    ToolUtils.getStr(map.get("deviceId")));
                        }
                        if ("cluster".equalsIgnoreCase(map.get("deviceType"))) {
                            vcsdkUtils.mountVmfsOnCluster(gson.toJson(dsmap), ToolUtils.getStr(map.get("deviceId")),
                                    null);
                        }
                    }

                }
            }
        }
    }

    /**
      * @Description: 根据storageid查询已经挂载的主机组信息并以树的结果返回
      * @Param storageId
      * @return List<ClusterTree>
      * @throws DmeException
      * @author yc
      * @Date 2021/5/14 15:40
     */
    @Override
    public List<ClusterTree> getMountedHostGroupsAndHostReturnTree(String dataStoreObjectId) throws Exception {

        List<ClusterTree> clusterTreeList = new ArrayList<>();
        List<Map<String, String>> clusterList = new ArrayList<>();
        //首先根据存储id获取存储下已经挂载的主机信息
        List<String> hostObjectIds= vcsdkUtils.getAllMountedHostId(dataStoreObjectId);
        //判断主机是否属于集群，如果主机属于集群需要获取集群的信息，将主机添加至集群中
        try {
            String clusterListStr = vcsdkUtils.getAllClusters();
            if (!StringUtils.isEmpty(clusterListStr)) {
                clusterList = gson.fromJson(clusterListStr, new TypeToken<List<Map<String, String>>>() {
                }.getType());
            }
        } catch (Exception e) {
            LOG.error("get all mounted hosts and clusters by  datastoreobjectid error:", e);
            throw new DmeException("get all mounted hosts and clusters by  datastoreobjectid error:" + e.getMessage());
        }
        Map<String,List<String>> temp = new HashMap<>();
        HashSet<String> hostIdSet = new HashSet<>(hostObjectIds);
        if (!CollectionUtils.isEmpty(hostObjectIds) && !CollectionUtils.isEmpty(clusterList)) {
                for (Map<String, String> clusterMap : clusterList) {
                    List<String> hosts = vcsdkUtils.getHostidsOnCluster(clusterMap.get(CLUSTER_ID));
                    List<String> hostMapTemp = new ArrayList<>();
                    for (String hostid : hostObjectIds) {
                    if (!CollectionUtils.isEmpty(hosts) && hosts.contains(hostid)){
                        hostMapTemp.add(hostid);
                        temp.put(clusterMap.get(CLUSTER_ID),hostMapTemp);
                        hostIdSet.remove(hostid);
                    }
                }
            }
        }
        //组装树数据
        if (!CollectionUtils.isEmpty(hostIdSet)){
            for (String hostId : hostIdSet) {
                ClusterTree hostTree = new ClusterTree();
                hostTree.setClusterId(hostId);
                hostTree.setClusterName(vcsdkUtils.getHostName(hostId));
                if (!StringUtils.isEmpty(hostTree.getClusterId())){
                    clusterTreeList.add(hostTree);
                }
            }
        }
        if (!CollectionUtils.isEmpty(temp)){
            for (String clusterId: temp.keySet()) {
               if (!CollectionUtils.isEmpty(temp.get(clusterId))){
                List<ClusterTree> childTemp  = new ArrayList<>();
                    for (String hostId : temp.get(clusterId)) {
                        ClusterTree hostTree = new ClusterTree();
                        hostTree.setClusterId(hostId);
                        hostTree.setClusterName(vcsdkUtils.getHostName(hostId));
                        childTemp.add(hostTree);
                    }
                   ClusterTree clusterTree = new ClusterTree();
                   clusterTree.setClusterId(clusterId);
                   clusterTree.setClusterName(vcsdkUtils.getClusterNameByClusterId(clusterId));
                   clusterTree.setChildren(childTemp);
                   clusterTreeList.add(clusterTree);
                }
            }
        }



        return clusterTreeList;
    }

    /**
     * @throws DmeException
     * @Description: 卸载vmfs新接口
     * @Param params
     * @author yc
     * @Date 2021/5/14 17:57
     */
    /**
    @Override
    public void unmountVmfsNew(Map<String, Object> params) throws DmeException {
        //参数非空校验
        if (CollectionUtils.isEmpty(params)) {
            throw new DmeException("unmount volume params is null!");
        }
        //解析前端入参
        Object ss = params.get("chooseDevice");
        //校验前端选择的主机或者主机组不能为空
        if (StringUtils.isEmpty(params.get("chooseDevice"))) {
            throw new DmeException("create vmfs the params is error");
        }
        List<Map<String, String>> chooseDevicelists = gson.fromJson(gson.toJsonTree(ss),
                new TypeToken<List<Map<String, String>>>() {
                }.getType());
        if (CollectionUtils.isEmpty(chooseDevicelists)) {
            throw new DmeException("create vmfs the params is error");
        }
        //将前端参数按主机组和主机信息进行区分
        List<String> hostIds = new ArrayList<>();
        List<String> clusterIds = new ArrayList<>();
        for (Map<String, String> chooseMap : chooseDevicelists) {
            if ("host".equalsIgnoreCase(chooseMap.get("deviceType"))) {
                if (!StringUtils.isEmpty(chooseMap.get("deviceId"))) {
                    hostIds.add(chooseMap.get("deviceId"));
                }
            }
            if ("cluster".equalsIgnoreCase(chooseMap.get("deviceType"))) {
                if (!StringUtils.isEmpty(chooseMap.get("deviceId"))) {
                    clusterIds.add(chooseMap.get("deviceId"));
                }
            }
        }
        //获取vmfs对应的存储数据信息
        List<String> dataStoreObjectIds = new ArrayList<>();
        String volumeId = null;
        String dataStoreName = null;
        //List<String> volumeIds = new ArrayList<>();
        if (!StringUtils.isEmpty(params.get(DATASTORE_OBJECT_IDS))) {
            dataStoreObjectIds = (List<String>) params.get(DATASTORE_OBJECT_IDS);
            if (CollectionUtils.isEmpty(dataStoreObjectIds)) {
                throw new DmeException("unmount volume params dataStoreObjectIds is null!");
            }
           // List<String> dataStoreNames = new ArrayList<>();
            for (String dsObjectId : dataStoreObjectIds) {
                boolean isFoundVm = vcsdkUtils.hasVmOnDatastore(dsObjectId);
                if (isFoundVm) {
                    LOG.info("vmfs unmount,the vmfs:{} contain vm,can not unmount!!!", dsObjectId);
                    continue;
                }
                DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dsObjectId);
                if (dvr == null) {
                    scanVmfs();
                    dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dsObjectId);
                }
                if (dvr != null) {
                    volumeId = dvr.getVolumeId();
                    dataStoreName = dvr.getStoreName();
                }
            }
            //找到对应的lun数据信息
            if (!StringUtils.isEmpty(volumeId)) {
                params.put(VOLUMEIDS, volumeId);
                params.put(DATASTORE_NAMES, dataStoreName);
            }
        }
        List<String> taskIds = new ArrayList<>();
        boolean isUnmounted = false;
      //  if (!CollectionUtils.isEmpty(volumeIds)) {
            if (!CollectionUtils.isEmpty(hostIds) && !StringUtils.isEmpty(volumeId)) {
                for (String hostObjId : hostIds) {
                    Map<String, Object> hostMap = getDmeHostByHostObjeId2(hostObjId);
                    if (hostMap != null && hostMap.size() > 0) {
                        params.put(HOST_ID, ToolUtils.getStr(hostMap.get(ID_FIELD)));
                        //根据volumeid查询指定lun获取该lun映射的主机和主机组信息
                        //如果该lun映射给的主机包含卸载主机，先解除lun和主机的映射关系，如果该lun只包含一个主机先接触隐射，然后删除lun,如果lun映射的主机不包含需要卸载的主机，返回成功，
                        List<Attachment> attachmentList= findMappedHostsAndClusters(volumeId);
                        //获取
                        isUnmounted = findMappingVolumeToHostgroup(volumeIds, hostMap);
                        if (!isUnmounted) {
                            String taskId = unmountHostGetTaskId2(params);
                            if (!StringUtils.isEmpty(taskId)) {
                                taskIds.add(taskId);
                            }
                        }
                    }
                }
            }
            if (!CollectionUtils.isEmpty(clusterIds)) {
                for (String clusterObjId : clusterIds) {
                    // 根据cluster获取dme侧目标主机组
                    Map<String, String> hostGroupMappingOrientedVolume =
                            getHostGroupMappingOrientedVolume(clusterObjId, volumeIds);
                    if (!CollectionUtils.isEmpty(hostGroupMappingOrientedVolume)) {
                        List<String> volumeids = new ArrayList<>();
                        for (Map.Entry<String, String> entry : hostGroupMappingOrientedVolume.entrySet()) {
                            volumeids.add(entry.getValue());
                            params.put(HOST_GROUP_ID1, entry.getKey());
                            params.put(VOLUME_IDS, volumeids);
                            String taskId = unmountHostGroupGetTaskId(params);
                            if (!StringUtils.isEmpty(taskId)) {
                                taskIds.add(taskId);
                            }
                            boolean isUnmappingHostgroup = taskService.checkTaskStatus(taskIds);
                            if (isUnmappingHostgroup) {
                                taskId = removeHostgroupGetTaskId(params);
                            }
                            if (!StringUtils.isEmpty(taskId)) {
                                taskIds.add(taskId);
                            }
                        }
                    }
                }
            }



    // 获取卸载的任务完成后的状态,默认超时时间10分钟
        if (taskIds.size() > 0) {
            isUnmounted = taskService.checkTaskStatus(taskIds);
        }
        //根据前端入参的主机和主机组集合，首先判断如果主机正好构成主机组信息，直接删除主机组信息，如果是主机组的部分主机，直接删除主机信息
        //判断是否卸载vmfs上的全部主机或集群 若是 则删除dme侧的卷，目前卸载参数中的主机/集群只支持单个,所以vmfs只挂载了一个主机或集群dme侧就直接删除卷
        boolean isDeleteHostFalg = false;
        boolean isDeleteClusterFalg = false;
        if (!CollectionUtils.isEmpty(hostIds)) {
            for (String dsObj : dataStoreObjectIds) {
                List<Map<String, Object>> hosts = getHostsByStorageId2(dsObj);
                if (hosts != null && hosts.size() == 0) {
                    isDeleteHostFalg = true;
                    break;
                }
            }
        }
        if (!CollectionUtils.isEmpty(clusterIds)) {
            for (String dsObjId : dataStoreObjectIds) {
                List<Map<String, Object>> hostGroups = getHostGroupsByStorageId2(dsObjId);
                if (hostGroups != null && hostGroups.size() == 0) {
                    isDeleteClusterFalg = true;
                    break;
                }
            }
        }
        if (!isUnmounted) {
            throw new DmeException(
                    "unmount volume precondition unmount host and hostGroup error(task status),taskIds:(" + gson.toJson(
                            taskIds) + ")!");
        } else {
            // 如果是需要扫描LUN来卸载，则需要执行下面的方法，dataStoreNames
            if (params.get(DmeConstants.DATASTORENAMES) != null) {
                List<String> dataStoreNames = (List<String>) params.get(DATASTORE_NAMES);
                if (dataStoreNames != null && dataStoreNames.size() > 0) {
                    for (String dataStoreName : dataStoreNames) {
                        Map<String, Object> dsmap = new HashMap<>();
                        dsmap.put(NAME_FIELD, dataStoreName);
                        vcsdkUtils.unmountVmfsOnHostOrClusterNew(gson.toJson(dsmap), clusterIds, hostIds);
                    }
                }
            }
        }
        // 若卸载vmfs上的全部主机或集群 最后重新扫描 此步会自动删除vmfs
        if (isDeleteHostFalg && isDeleteClusterFalg) {
            vcsdkUtils.scanDataStoreNew(clusterIds, hostIds);
        }
    }*/
    /**
      * @Description: 创建vmfs新方法-支持多主机或集群
      * @Param params, volumelist
      * @throws DmeException
      * @author yc
      * @Date 2021/5/20 14:13
     */
    private void createOnVmwareNew(Map<String, Object> params, Map<String, Object> volumemap) throws DmeException {
        VCenterInfo vcenterinfo = null;
        if (!StringUtils.isEmpty(params.get(DmeConstants.SERVICELEVELID))) {
            vcenterinfo = vcenterinfoservice.getVcenterInfo();
        }
        final VCenterInfo vcentertemp = vcenterinfo;
        //创建前先扫描hba，避免每次循环扫描
        Object ss = params.get("chooseDevice");

        List<Map<String, String>> chooseDevicelists = gson.fromJson(gson.toJsonTree(ss),
                new TypeToken<List<Map<String, String>>>() {
                }.getType());
        Map<String, String> hostObjectIdMap = getHostNameAndHostIdList(chooseDevicelists);
        Set<String> hostObjectIds = hostObjectIdMap.keySet();
        List<String> clusterObjectIds = getClusterIdList(chooseDevicelists);
        if (!CollectionUtils.isEmpty(hostObjectIds)) {
            for (String hostObjectId : hostObjectIds) {
                vcsdkUtils.rescanHbaByHostObjectId(hostObjectId);
            }
        }
        if (!CollectionUtils.isEmpty(clusterObjectIds)) {
            for (String clusterObjectId : clusterObjectIds) {
                vcsdkUtils.rescanHbaByClusterObjectId(clusterObjectId);
            }
        }
            Map<String, Object> paramstemp = new HashMap<>(params);
            try {
                // 创建vmware中的vmfs存储。
                paramstemp.put(VOLUME_WWN, volumemap.get(VOLUME_WWN));
                paramstemp.put(VOLUME_NAME, volumemap.get(VOLUME_NAME));
                String dataStoreStr = createVmfsOnVmwareNew(paramstemp, hostObjectIdMap, clusterObjectIds);
                if (!StringUtils.isEmpty(dataStoreStr)) {
                    Map<String, Object> dataStoreMap = gson.fromJson(dataStoreStr,
                            new TypeToken<Map<String, Object>>() {
                            }.getType());
                    if (dataStoreMap != null) {
                        // 将DME卷与vmfs的关系保存数据库,因为可以同时创建几个卷，无法在此得到对应关系，所以此处不再保存关系信息
                        saveDmeVmwareRalation(volumemap, dataStoreMap);
                        // 关联服务等级
                        if (!StringUtils.isEmpty(paramstemp.get(SERVICE_LEVEL_ID))) {
                            String serviceLevelName = ToolUtils.getStr(paramstemp.get(SERVICE_LEVEL_NAME));
                            vcsdkUtils.attachTag(ToolUtils.getStr(dataStoreMap.get("type")),
                                    ToolUtils.getStr(dataStoreMap.get(ID_FIELD)), serviceLevelName, vcentertemp);
                        }
                    }
                    List<String> volumeIds = new ArrayList<>();
                    String volumeId = ToolUtils.getStr(volumemap.get(VOLUME_ID));
                    volumeIds.add(volumeId);
                    //将vmfs挂载到其他主机上
                    if (hostObjectIds.size() > 1){
                        //ArrayList<String> hostObjectIdList = new ArrayList<>(hostObjectIds);
                        //List<String> hostObjectIdListnew = Arrays.asList(hostObjectIdList.remove(0));
                        /*for (String  objhostid : hostObjectIdListnew) {
                            Map<String, Object> tempparams = new HashMap<>();
                            tempparams.put(VOLUMEIDS,volumeIds);*/
                            mountVmfsOnVmwareNew(params, chooseDevicelists);

                            //taskids.add( mountVmfsToHost(tempparams,  objhostid) );
                       // }
                    }
                } else {
                    throw new DmeException("vmware create vmfs error:" + params.get(VOLUME_NAME));
                }
            } catch (Exception e) {
                LOG.info("vmware create vmfs error:" + params.get(VOLUME_NAME));
            }
    }
    /**
      * @Description: 在虚拟机上创建vmfs新方法
      * @Param params,hostObjectIdMap,clusterObjectIds
      * @return String
      * @throws DmeException
      * @author yc
      * @Date 2021/5/20 14:10
     */
    private String createVmfsOnVmwareNew(Map<String, Object> params, Map<String, String> hostObjectIdMap,
                                         List<String> clusterObjectIds) throws DmeException {
        String dataStoreStr = "";
        LOG.info("create vmfs on vmware begin !");
        try {
            if (params != null) {
                // 创建vmware中的vmfs存储。 cluster host
                List<String> hostObjectIds = new ArrayList<>(hostObjectIdMap.keySet());
                String datastoreName = ToolUtils.getStr(params.get(NAME_FIELD));
                int capacity = ToolUtils.getInt(params.get(CAPACITY));
                String existVolumeWwn = ToolUtils.getStr(params.get(VOLUME_WWN));
                String existVolumeName = ToolUtils.getStr(params.get(VOLUME_NAME));
                String volumeName = ToolUtils.getStr(params.get(VOLUMENAME));
                existVolumeName = existVolumeName.replaceAll(volumeName, "");

                // 根据后缀序号改变VMFS的名称
                datastoreName = datastoreName + existVolumeName;

                // 从主机或集群中找出最接近capacity的LUN
                Map<String, Object> hsdmap = new HashMap<>();
                ArrayList<Map<String, Object>> hsdmapList = new ArrayList<Map<String, Object>>();
                if (!CollectionUtils.isEmpty(hostObjectIds)) {
                    hsdmap = vcsdkUtils.getLunsOnHost(hostObjectIds.get(0), capacity, existVolumeWwn);
                }
                if (!CollectionUtils.isEmpty(clusterObjectIds)) {
                    hsdmap = vcsdkUtils.getLunsOnCluster(clusterObjectIds.get(0), capacity, existVolumeWwn);
                }

                int vmfsMajorVersion = ToolUtils.getInt(params.get("version"));
                int unmapGranularity = ToolUtils.getInt(params.get("spaceReclamationGranularity"));
                int blockSize = ToolUtils.getInt(params.get("blockSize"));
                String unmapPriority = ToolUtils.getStr(params.get("spaceReclamationPriority"));
                dataStoreStr = vcsdkUtils.createVmfsDataStore(hsdmap, capacity, datastoreName, vmfsMajorVersion,
                        blockSize, unmapGranularity, unmapPriority);

                // 如果创建成功，扫描集群中的其他主机
                if (!CollectionUtils.isEmpty(clusterObjectIds)) {
                    vcsdkUtils.scanDataStore(clusterObjectIds.get(0), null);
                }
            }
        } catch (DmeException e) {
            LOG.error("createVmfsOnVmware error:{}", e.toString());
            throw new DmeException(e.getMessage());
        }
        return dataStoreStr;
    }
    /**
      * @Description: 获取各个主机所在的主机组id
      * @Param hostIds
      * @return List<String>
      * @throws VcenterException
      * @author yc
      * @Date 2021/5/20 16:26
     */
    public List<String> getClusterNamesByHostId(List<String> hostIds) throws VcenterException {
        //首先判断入参
        ArrayList<String> clusterIds = new ArrayList<String>();
        if (CollectionUtils.isEmpty(hostIds))
            return clusterIds;
        List<String> clusteridList = vcsdkUtils.getAllClusterIds();
        //循环主机id,判断主机所属的主机组
        for (String hostid : hostIds)
            for (String clusterid : clusteridList) {
                List<String> hosts = vcsdkUtils.getHostidsOnCluster(clusterid);
                if (!CollectionUtils.isEmpty(hosts) && hosts.contains(hostid))
                    clusterIds.add(clusterid);
            }
        return clusterIds;
    }

    /**
      * @Description: 检查映射结果，并且分别统计成功和失败的主机或者主机组
      * @Param taskMap
      * @return @return
      * @throws
      * @author yc
      * @Date 2021/5/21 16:39
     */
    private Map<String, List<String>> checkTaskStatusMappingToHostOrClustre(Map<String, String> taskMap,
                                                                            List<String> objHostIds, List<String> clusterIds) throws DmeException {
        if (CollectionUtils.isEmpty(taskMap)) {
            throw new DmeException("create vmfs: check mapping result,the parmas is error");
        }
        Map<String, List<String>> mappingResultMap = new HashMap<>();
        List<String> mappedList = new ArrayList<>();
        List<String> unMappedList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(objHostIds)) {
            //检查主机集合的映射结果
            for (String hostId : taskMap.keySet()) {
                TaskDetailInfo result = taskService.queryTaskById(taskMap.get(hostId));
                if (3 == result.getStatus() && 100 == result.getProgress()) {
                    mappedList.add(hostId);
                } else {
                    unMappedList.add(hostId);
                }
            }
            //将联通性检测失败的主机也放至映射失败的集合中
            List<String> failList = objHostIds.stream().filter(id -> !taskMap.keySet().contains(id)).collect(Collectors.toList());
            unMappedList.addAll(failList);
            mappingResultMap.put("hostMapped", mappedList);
            mappingResultMap.put("hostUnmapped", unMappedList);
        } else if (!CollectionUtils.isEmpty(clusterIds)) {
            //检查主机组集合的映射结果
            for (String clusterId : taskMap.keySet()) {
                TaskDetailInfo result = taskService.queryTaskById(taskMap.get(clusterId));
                if (3 == result.getStatus() && 100 == result.getProgress()) {
                    mappedList.add(clusterId);
                } else {
                    unMappedList.add(clusterId);
                }
            }
            //将联通性检测失败的主机组也放至映射失败的集合中
            List<String> failList = clusterIds.stream().filter(id -> !taskMap.keySet().contains(id)).collect(Collectors.toList());
            unMappedList.addAll(failList);
            mappingResultMap.put("clusterMapped", mappedList);
            mappingResultMap.put("clusterUnmapped", unMappedList);
        }
        return mappingResultMap;
    }

    /**
     * @Description: 根据存储获取映射的lun信息
     * @Param volumeId
     * @return List<Attachment>
     * @throws DmeException
     * @author yc
     * @Date 2021/6/2 16:39
     */
    private List<Attachment> findMappedHostsAndClusters(String volumeId) throws DmeException {
        if (StringUtils.isEmpty(volumeId)) {
            throw new DmeException("findMappedHostsAndClusters error:the param is empty");
        }
        //调用dme查询指定lun的详细信息
        String url = DmeConstants.DME_VOLUME_BASE_URL + "/" + volumeId;
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);

        } catch (Exception e) {
            throw new DmeException("findMappedHostsAndClusters error :response " + e.getMessage());
        }
        if (responseEntity.getStatusCodeValue() != HttpStatus.OK.value()) {
            LOG.error("queryTaskById failed!taskId={},errorMsg:{}", volumeId, responseEntity.getBody());
            throw new DmeException("findMappedHostsAndClusters error :response error");
        }
        //解析响应结果，获取lun所映射的主机和主机组信息
        JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
        //String response = responseEntity.getBody();
        JsonObject volumeDetail = new JsonObject();
        if (!StringUtils.isEmpty(jsonObject)) {
            volumeDetail = jsonObject.get("volume").getAsJsonObject();
        }
        List<Attachment> attachmentList = new ArrayList<>();

        JsonArray attachments = volumeDetail.get("attachments").getAsJsonArray();
        for (JsonElement jsonElement1 : attachments) {
            Attachment attachment = new Attachment();
            JsonObject json1 = jsonElement1.getAsJsonObject();
            attachment.setId(StringUtil.dealQuotationMarks(ToolUtils.getStr(json1.get(ID_FIELD))));
            attachment.setVolumeId(StringUtil.dealQuotationMarks(ToolUtils.getStr(json1.get("volume_id"))));
            attachment.setHostId(StringUtil.dealQuotationMarks(ToolUtils.getStr(json1.get(HOST_ID))));
            attachment.setAttachedAt(StringUtil.dealQuotationMarks(ToolUtils.getStr(json1.get("attached_at"))));
            attachment.setAttachedHostGroup(StringUtil.dealQuotationMarks(ToolUtils.getStr(json1.get("attached_host_group"))));
            attachment.setAttachedAtDate(ToolUtils.getDate(ToolUtils.getStr(json1.get("attached_at"))));
            attachmentList.add(attachment);
        }

        return attachmentList;
    }

    /**
     * @return @return
     * @throws
     * @Description: 向指定主机组中添加主机（主机支持批量）
     * @Param hostGroupId, hostIds
     * @author yc
     * @Date 2021/6/4 10:04
     */
    private void addHostsToHostGroupbatch(String hostGroupId, List<String> hostIds) throws DmeException {
        //校验参数
        if (StringUtils.isEmpty(hostGroupId) || CollectionUtils.isEmpty(hostIds) || hostIds.size() > 100) {
            throw new DmeException("add hosts to host group error:the param is empty");
        }

        Map<String,Object> reqMap = new HashMap<>();
        String url = DmeConstants.PUT_ADD_HOST_TO_HOSTS.replace("{hostgroup_id}", StringUtil.dealQuotationMarks(hostGroupId));
        reqMap.put("host_ids", hostIds);
        reqMap.put("sync_to_storage", true);
        ResponseEntity<String> responseEntity;
        try {
            responseEntity  = dmeAccessService.access(url, HttpMethod.PUT, gson.toJson(reqMap));
        }catch (Exception e){
            throw new DmeException("add hosts to host group error:"+e.getMessage());
        }
        if (responseEntity.getStatusCodeValue() != HttpStatus.OK.value()) {
            throw new DmeException(responseEntity.getBody());
        }
    }
    /**
     * @return @boolean
     * @throws DmeException
     * @Description: 将主机移出主机组
     * @Param hostGroupId, hostIds
     * @author yc
     * @Date 2021/6/4 18:08
     */
    private boolean removeHostFromHostgroupNew(@NotNull String hostGroupId, @NotNull List<String> hostIds) throws DmeException {

        Map<String, Object> params = new HashMap<>();

        String url = DmeConstants.PUT_REMOVE_HOST_FROM_HOSTGROUP.replace("{hostgroup_id}", hostGroupId);
        params.put("host_ids", hostIds);
        params.put("sync_to_storage", true);
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = dmeAccessService.access(url, HttpMethod.PUT, gson.toJson(params));
        } catch (Exception e) {
            throw new DmeException("request DME remove host from hostgroup failed!" + e.getMessage());
        }
        if (responseEntity.getStatusCodeValue() != HttpStatus.OK.value()) {
            throw new DmeException("remove host from hostgroup failed!{}",
                    hostIds + " from " + hostGroupId);
        }
        return isRemoveHostOfHostgroup(hostGroupId);

    }
    /**
     * @return @return
     * @throws
     * @Description: 检查主机组和集群的一致性
     * @Param @param null
     * @author yc
     * @Date 2021/5/31 17:21
     */
    public HostGroupAndClusterConsistency checkConsistencyAndGetclusterId(String clusterId,String hostGroupVolumid) throws DmeException {
        //校验集群id(clusterId)不能为空
        if (StringUtils.isEmpty(clusterId)) {
            throw new DmeException("check consistency error : the clusterId is empty!");
        }
        //首先根据集群id获取集群信息
        String prefix = vcsdkUtils.getVcConnectionHelper().objectId2Mor(clusterId).getValue();
        List<Map<String, Object>> dmeHostGroupList = new ArrayList<>();
        List<Map<String, Object>> dmeHostGroups = dmeAccessService.getDmeHostGroups(prefix);
        //根据集群的名称和主机数量查询主机，对查询出的主机组进行筛选
        String vmwarehosts = vcsdkUtils.getHostsOnCluster(clusterId);
        if (StringUtils.isEmpty(vmwarehosts)) {
            new DmeException("the cluster has no host");
        }
        List<Map<String, String>> vmwarehostlists = gson.fromJson(vmwarehosts,
                new TypeToken<List<Map<String, String>>>() {
                }.getType());
        //todo 20210621 修改挂载集群时映射主机组时需要剔除已经隐射的主机
        List<String> mappedHostid = new ArrayList<>();
        if(!StringUtils.isEmpty(hostGroupVolumid)) {
            Map<String, List<Map<String, Object>>> allHabs = getAllInitionator();
            List<Attachment> attachmentList = findMappedHostsAndClusters(hostGroupVolumid);
            if (!CollectionUtils.isEmpty(attachmentList)) {
                for (Attachment attach : attachmentList) {
                    if (!StringUtils.isEmpty(StringUtil.dealQuotationMarks(attach.getHostId()))) {
                        mappedHostid.add(StringUtil.dealQuotationMarks(attach.getHostId()));
                    }
                }
            }
            //从集群中的主机中剔除已经映射的主机
            vmwarehostlists = deleteMappedHost(vmwarehostlists,mappedHostid,allHabs);
        }

        //循环dme上的集群的主机组，获取和集群中主机数量相等的主机组；
        if (!CollectionUtils.isEmpty(dmeHostGroups)) {
            List<Map<String, String>> finalVmwarehostlists = vmwarehostlists;
            dmeHostGroupList = dmeHostGroups.stream().filter(hostGroupMap -> (int) hostGroupMap.get("host_count") == finalVmwarehostlists.size()).collect(Collectors.toList());
        }
        //校验DME中的启动器是否包含vcenter,如果包含默认为两者为同一集群
        //1.根据集群中主机的获取主机对应的启动器（Vcenter侧）
        Map<String, List<Map<String, Object>>> hbas = new HashMap<>();
        for (Map<String,String > hostMap : vmwarehostlists ){
            List<Map<String, Object>> hosthbas = vcsdkUtils.getHbasByHostObjectId(hostMap.get(HOSTID));
            hbas.put(hostMap.get(HOSTID),hosthbas);
           // Map<String, Object> sss = vcsdkUtils.getHbaByHostObjectId(hostMap.get(HOSTID));

        }
       /* Map<String, List<Map<String, Object>>> hbas = vcsdkUtils.getHbasByClusterObjectId2(clusterId);//获取name\
        if (!StringUtils.isEmpty(hostGroupVolumid)) {
            //todo 20210621 修改挂载集群时映射主机组时需要剔除已经隐射的主机
            deleteMappedHostHabs(mappedHostid, hbas,);
        }*/
        ArrayList<String> hbaList = new ArrayList<String>();
        if (!CollectionUtils.isEmpty(hbas)) {
            //准备一个Boolean类型的set用于判断启动器相等判断的最后结果
            for (String hostId : hbas.keySet()) {
                if (!CollectionUtils.isEmpty(hbas.get(hostId))) {
                    hbas.get(hostId).forEach(hba -> {
                        hbaList.add((String) hba.get("name"));
                    });
                }
            }
        }
        List<String> hbaListTemp = hbaList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        //2.Dme侧获取启动器
        if (!CollectionUtils.isEmpty(dmeHostGroupList)) {
            for (Map<String, Object> dmeHostGroupMap : dmeHostGroupList) {
                //1.查询主机数量相等的主机组中的主机，根据主机获取主机对应的启动器(DME侧)
                String hostGroupId = null;
                List<String> initiators = new ArrayList<>();
                if (!CollectionUtils.isEmpty(dmeHostGroupMap)) {
                    hostGroupId = (String) dmeHostGroupMap.get(ID_FIELD);
                }
                //2.根据主机组id获取主机，并且获取主机的启动器
                if (!StringUtils.isEmpty(hostGroupId)) {
                    List<Map<String, Object>> hostList = dmeAccessService.getDmeHostInHostGroup(hostGroupId);
                    //2.1.根据主机获取主机的所有启动器
                    if (!CollectionUtils.isEmpty(hostList)) {
                        for (Map<String, Object> dmehost : hostList) {
                            // 得到主机的启动器
                            if (!CollectionUtils.isEmpty(dmehost)) {
                                String demHostId = ToolUtils.getStr(dmehost.get(ID_FIELD));
                                List<Map<String, Object>> subinitiators = dmeAccessService.getDmeHostInitiators(demHostId);
                                LOG.info("initiators of host on dme！",
                                        gson.toJson(initiators) + "host size:" + initiators.size());
                                if (!CollectionUtils.isEmpty(subinitiators)) {
                                    subinitiators.stream().forEach(s -> {
                                        initiators.add((String) s.get(PORT_NAME));
                                    });
                                }
                            }
                        }
                    }
                }
                List<String> initiatorsTemp = initiators.stream().filter(Objects::nonNull).collect(Collectors.toList());
                if (initiatorsTemp.containsAll(hbaListTemp)) {
                    return new HostGroupAndClusterConsistency(true, hostGroupId);
                }
            }
        }
        return new HostGroupAndClusterConsistency(false);
    }

    private void deleteMappedHostHabs(List<String> mappedHostid, Map<String, List<Map<String, Object>>> hbas)  throws  DmeException{
        //根据已经映射的hostid查询主机对应的ip地址，
        List<String> ipList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(mappedHostid)){
            for (String hostid : mappedHostid) {
                Map<String, Object> dmeHostImfo = dmeAccessService.getDmeHost(hostid);
                if (!StringUtils.isEmpty(dmeHostImfo) && !StringUtils.isEmpty(dmeHostImfo.get(IP_FIELD))){
                    ipList.add(ToolUtils.getStr(dmeHostImfo.get(IP_FIELD)));
                }
            }
        }
        Map<String, List<Map<String, Object>>> mappedHbas = new HashMap<>();
        //获取vcenter侧的所有主机信息，获取主机ip相同的
        List<Map<String, String>> allHost = vcsdkUtils.getAllHosts2();
        if (!CollectionUtils.isEmpty(allHost)){
            for (Map<String,String> hostInfo : allHost) {
                if (!CollectionUtils.isEmpty(hostInfo) && ipList.contains( hostInfo.get("hostName"))){
                    hbas.remove(hostInfo.get("hostName"));
                }
            }
        }
    }

    private List<Map<String, String>> deleteMappedHost(List<Map<String, String>> vmwarehostlists, List<String> mappedHostid,
                                                       Map<String, List<Map<String, Object>>> allHabs)  throws  DmeException{
        //循环vcenter端的主机列表，查找主机ip所对应的dme端的hostid
        List<Map<String, String>> vmwareHosts = new ArrayList<>();
        if (!CollectionUtils.isEmpty(vmwarehostlists)){
            for (Map <String,String> hostMap : vmwarehostlists) {
                if (!CollectionUtils.isEmpty(hostMap) && !StringUtils.isEmpty(hostMap.get(HOSTID))){
                    String hostResult = getDmeHostId(hostMap.get(HOSTID),allHabs);
                    if (StringUtils.isEmpty(hostResult)){
                        throw new DmeException("get hostid from dme return empty");
                    }
                    if (!mappedHostid.contains(hostResult)){
                        vmwareHosts.add(hostMap);
                    }
                }
            }
        }
        return vmwareHosts;

    }
    private Map<String,String> deleteMappedHost2(List<String> targList, List<String> mappedHostid,Map<String, List<Map<String, Object>>> allHbas )  throws  DmeException{
        //循环vcenter端的主机列表，查找主机ip所对应的dme端的hostid
        Map<String,String> vmwareHosts = new HashMap<>();
        if (!CollectionUtils.isEmpty(targList)){
            for (String hostid : targList) {
                if (!StringUtils.isEmpty(hostid)){
                    String hostResult = getDmeHostId(hostid,allHbas);
                    if (StringUtils.isEmpty(hostResult)){
                        throw new DmeException("get hostid from dme return empty");
                    }
                    if (!mappedHostid.contains(hostResult)){
                        vmwareHosts.put(hostid,hostResult);
                    }
                }
            }
        }
        return vmwareHosts;

    }

    private String getDmeHostId(String hostid, Map<String, List<Map<String, Object>>> allHbas) throws DmeException {
        if (StringUtils.isEmpty(hostid)) {
            throw new DmeException("get dme host id error");
        }
        List<Map<String, Object>> hbas = vcsdkUtils.getHbasByHostObjectId(hostid);
        if (CollectionUtils.isEmpty(hbas)) {
            throw new DmeException("The" + hostid + " did not find a valid Hba");
        }
        List<String> wwniqns = new ArrayList<>();
        for (Map<String, Object> hba : hbas) {
            wwniqns.add(ToolUtils.getStr(hba.get(NAME_FIELD)));
        }
        for (String dmehostid : allHbas.keySet()) {
            List<Map<String, Object>> hostinitionators = allHbas.get(dmehostid);
            if (hostinitionators != null && hostinitionators.size() > 0) {
                for (Map<String, Object> inimap : hostinitionators) {
                    String portName = ToolUtils.getStr(inimap.get(PORT_NAME));
                    if (wwniqns.contains(portName)) {
                        return dmehostid;

                    }
                }
            }
        }
        return null;
    }
    /**
     * @return hostIds
     * @throws DmeException
     * @Description: 检查主机或者主机组是否存在，不存在就创建新的主机或者主机组
     * @Param params, allinitionators, volumeId, deviceTypeSet
     * @author yc
     * @Date 2021/5/13 14:54
     */
    private Map<String, List<String>> checkOrCreateToHostorHostGroupNew(Map<String, Object> params,
                                                                        Map<String, List<Map<String, Object>>> allinitionators,
                                                                        String volumeId, HashSet<String> deviceTypeSet,
                                                                        List<Map<String, String>> chooseDevicelists) throws DmeException {
        // 根据前端入参，确定主机和主机组的组合方式（chooseDevice）
        //去重后根据前端入参的方式进行逻辑判断
        //1.单纯选择主机的方式创建
        Map<String, String> hostIdMap = new HashMap<>();
        Map<String, List<String>> hostIds = new HashMap<>();

        try {
            if (deviceTypeSet.contains("host")) {
                //检查以主机的方式创建，多主机是否属于同一主机组，不支持多主机组的形式
                checkHostsBelongOnecluster(chooseDevicelists);
                //检查主机是否存在，不存在就创建新的主机
                Map<String, String> hostIdMaps = getHostNameAndHostIdList(chooseDevicelists);
                List<String> objIds = checkOrCreateToHostNew(hostIdMaps, allinitionators,null);
                // 根据获取到的dme主机，检查主机连通性
                List<String> failHostIdList = estimateConnectivityOfHostOrHostgroupNew(ToolUtils.getStr(params.get(STORAGE_ID)), objIds, null,null);
                // 连通性正常的主机或者主机组id
                if (CollectionUtils.isEmpty(failHostIdList)) {
                    hostIds.put("nomalHost", objIds);
                }
            }
            //2.单纯选择主机组的方式创建
            else if (deviceTypeSet.contains("cluster")) {
                //todo 重新改造原有方法（检查主机组连通性）
                //检查主机组是否存在，不存在就创建新的主机组
                List<String> clusters = getClusterIdList(chooseDevicelists);
                if (!CollectionUtils.isEmpty(clusters) && clusters.size() >1 ){
                    throw new DmeException("create vmfs params is error");
                }
                List<String> objIds = getOrCreateHostGroupIdNew(clusters, volumeId);
                List<String> failGroupHostIdList = new ArrayList<>();
                // 检查主机组连通性
                if (!CollectionUtils.isEmpty(objIds)) {
                    for (String hostGroupId : objIds) {
                        failGroupHostIdList.addAll(estimateConnectivityOfHostOrHostgroupNew(ToolUtils.getStr(params.get(STORAGE_ID)), null, hostGroupId,null));
                    }
                }
                if (CollectionUtils.isEmpty(hostIdMap)) {
                    hostIds.put("nomalCluster", objIds);
                }
            }
            else {
                throw new DmeException("create vmfs deviceType is error");
            }

        } catch (DmeException e) {
            LOG.error("checkOrcreateToHostorHostGroup error:", e);
            throw new DmeException(e.getMessage());
        }
        return hostIds;
    }
    /**
     * @Description: 查询存储设备的创建方式
     * @Param dataStoreObjectIds
     * @return String
     * @throws
     * @author yc
     * @Date 2021/6/2 15:31
     */
    @Override
    public String queryCreationMethodByDatastore(String dataStoreObjectId) throws DmeException {

        //根据存储设备获取设备上映射的卷信息
        String result = null;
        if(StringUtils.isEmpty(dataStoreObjectId)){
            throw new DmeException("query creation method by datastore error,param is empty");
        }
        String volumeId = null;
        DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dataStoreObjectId);
        if (!StringUtils.isEmpty(dvr)) {
            volumeId = dvr.getVolumeId();
        }
        if (StringUtils.isEmpty(volumeId)){
            throw new DmeException("get volumeid fail");
        }
        //查询指定lun，获取映射信息
        List<Attachment> attachmentList = findMappedHostsAndClusters(volumeId);
        //获取映射数据中时间最早的映射方式
        if (CollectionUtils.isEmpty(attachmentList)){
            throw new DmeException("query lun info error");
        }
        Attachment mindatares = attachmentList.stream().min(Comparator.comparing(attachment -> attachment.getAttachedAtDate())).get();
        //判断时间最小的映射方式（如果为主机组，就返回‘cluster’,如果为主机返回‘host’,其他报错）
        if (!StringUtils.isEmpty(mindatares.getAttachedHostGroup()) && !mindatares.getAttachedHostGroup().equalsIgnoreCase("null")){
            result = CLUSTER;
        } else if (!StringUtils.isEmpty(mindatares.getHostId()) && !mindatares.getHostId().equalsIgnoreCase("null")){
            result = HOST;
        }
        if(StringUtils.isEmpty(result)){
            throw new DmeException("query vmfs create method error");
        }
        return result;
    }

    @Override
    public List<Map<String, String>> queryMountableVmfsByClusterId(String clusterObjectId, String dataStoreType)  throws DmeException {
        List<Map<String, String>> rst;
        try {
             rst = vcsdkUtils.getNotMountedDatastorage(clusterObjectId, dataStoreType);
        }catch (VcenterException e){
            throw new DmeException(e.getMessage());
        }
        return  rst;
    }

    private String addHostToHostGroup(List<String> hostIds, String hostGroupVolumid, String dataStoreObjectId, Map<String,
            String> hostIdToIp,List<String> unmappingAndNomalHostids,Map<String, List<Map<String, Object>>> allinitionators ) {
        List<Map<String, String>> clusterList = null;
        String desc = "";
        try {
             clusterList = vcsdkUtils.getClustersByDsObjectIdNew(dataStoreObjectId);

            HashMap<String, List<String>> hostToClu = new HashMap<>();
            if (!CollectionUtils.isEmpty(hostIds) && !CollectionUtils.isEmpty(clusterList)) {
                for (Map<String, String> clusterinfo : clusterList) {
                    if (!CollectionUtils.isEmpty(clusterinfo) && !StringUtils.isEmpty(clusterinfo.get(CLUSTER_ID))) {
                        String hostInfoStr = vcsdkUtils.getHostsOnCluster(clusterinfo.get(CLUSTER_ID));
                        List<Map<String, String>> hostInfoList = null;
                        if (!StringUtils.isEmpty(hostInfoStr)) {
                            hostInfoList = gson.fromJson(hostInfoStr, new TypeToken<List<Map<String, String>>>() {
                            }.getType());
                        }
                        List<String> hostIdList = new ArrayList<>();
                        if (!CollectionUtils.isEmpty(hostInfoList)) {
                            for (Map<String, String> hostInfo : hostInfoList) {
                                if (!CollectionUtils.isEmpty(hostInfo) && !StringUtils.isEmpty(hostInfo.get(HOSTID))) {
                                    hostIdList.add(hostInfo.get(HOSTID));
                                }
                            }
                        }
                        Set<String> hostObjList = new HashSet<>();
                        for (String hostObjid : hostIds) {
                            if (!StringUtils.isEmpty(hostObjid) && hostIdList.contains(hostObjid)) {
                                hostObjList.add(hostObjid);
                            }
                        }
                        if (!CollectionUtils.isEmpty(hostObjList)) {
                            hostToClu.put(clusterinfo.get(CLUSTER_ID), new ArrayList<>(hostObjList));
                        }
                    }
                }
            }
            if (CollectionUtils.isEmpty(hostToClu)) {
                return desc;
            }
            List<Attachment> attachmentList = findMappedHostsAndClusters(hostGroupVolumid);
            Map<String, Map<String, Object>> mappedHostgroupinfoMap = new HashMap<>();
            Map<String, Attachment> HostgroupnameToDataMap = new HashMap<>();

            if (!CollectionUtils.isEmpty(attachmentList)) {
                for (Attachment attach : attachmentList) {
                    if (!StringUtils.isEmpty(attach) && !StringUtils.isEmpty(StringUtil.dealQuotationMarks(attach.getAttachedHostGroup()))) {
                        Map<String, Object> dmeHostGroupInfo = dmeAccessService.getDmeHostGroup(StringUtil.dealQuotationMarks(attach.getAttachedHostGroup()));
                        if (!CollectionUtils.isEmpty(dmeHostGroupInfo)) {
                            mappedHostgroupinfoMap.put(ToolUtils.getStr(dmeHostGroupInfo.get(NAME_FIELD)), dmeHostGroupInfo);
                            HostgroupnameToDataMap.put(ToolUtils.getStr(dmeHostGroupInfo.get(NAME_FIELD)), attach);
                        }
                    }
                }
            }
            Map<String, List<String>> hostIdsToHostGroupId = new HashMap<>();
            if (!CollectionUtils.isEmpty(mappedHostgroupinfoMap) && !CollectionUtils.isEmpty(HostgroupnameToDataMap)) {
                for (String hostGrouName : mappedHostgroupinfoMap.keySet()) {
                    for (String clusterid : hostToClu.keySet()) {
                        if (!StringUtils.isEmpty(clusterid)) {
                            if (hostGrouName.equals(vcsdkUtils.getClusterName(clusterid))) {
                                hostIdsToHostGroupId.put(StringUtil.dealQuotationMarks(ToolUtils.getStr(mappedHostgroupinfoMap.get(hostGrouName).get("id"))), hostToClu.get(clusterid));
                            } else {
                                String prefix = vcsdkUtils.getVcConnectionHelper().objectId2Mor(clusterid).getValue();
                                List<Attachment> HostgroupnameToDataMapnew = new ArrayList<>();
                                for (String hostGrouName2 : HostgroupnameToDataMap.keySet()) {
                                    if (hostGrouName2.startsWith(prefix)) {
                                        HostgroupnameToDataMapnew.add(HostgroupnameToDataMap.get(hostGrouName2));
                                    }
                                    Attachment maxdatares = HostgroupnameToDataMapnew.stream().filter(attachment -> !StringUtils.isEmpty(attachment.getAttachedHostGroup())).max(Comparator.comparing(attachment1 -> attachment1.getAttachedAtDate())).get();
                                    hostIdsToHostGroupId.put(StringUtil.dealQuotationMarks(ToolUtils.getStr(maxdatares.getAttachedHostGroup())), hostToClu.get(clusterid));
                                }
                            }
                        }
                    }
                }
            }
            HashSet<String> hoss = new HashSet<String>();
            HashMap<String, List<String>> hostIdsToHostGroupId2 = new HashMap<>();
            if (!CollectionUtils.isEmpty(hostIdsToHostGroupId)) {
                for (String hostgroupId : hostIdsToHostGroupId.keySet()) {
                    List<String> hostobjids = hostIdsToHostGroupId.get(hostgroupId);
                    if (!CollectionUtils.isEmpty(hostobjids)) {
                        //获取各个主机的适配器集合
                        List<String> hostIdList = new ArrayList<>();
                        for (String hostId : hostobjids) {
                            String objId = "";
                            List<Map<String, Object>> hbas = vcsdkUtils.getHbasByHostObjectId(hostId);
                            if (CollectionUtils.isEmpty(hbas)) {
                                throw new DmeException("The" + hostId + " did not find a valid Hba");
                            }
                            List<String> wwniqns = new ArrayList<>();
                            for (Map<String, Object> hba : hbas) {
                                wwniqns.add(ToolUtils.getStr(hba.get(NAME_FIELD)));
                            }
                            for (String dmehostid : allinitionators.keySet()) {
                                List<Map<String, Object>> hostinitionators = allinitionators.get(dmehostid);
                                if (hostinitionators != null && hostinitionators.size() > 0) {
                                    for (Map<String, Object> inimap : hostinitionators) {
                                        String portName = ToolUtils.getStr(inimap.get(PORT_NAME));
                                        if (wwniqns.contains(portName)) {
                                            objId = dmehostid;
                                            break;
                                        }
                                    }
                                }
                            }
                            // 如果主机\不存在就创建并得到主机
                            if (StringUtils.isEmpty(objId)) {
                                objId = createHostOnDme(hostIdToIp.get(hostId), hostId);
                            }
                            if (!StringUtils.isEmpty(objId)) {
                                hostIdList.add(objId);
                                hoss.add(objId);
                            }
                        }
                        if (!CollectionUtils.isEmpty(hostIdList))
                            hostIdsToHostGroupId2.put(hostgroupId, hostIdList);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(hoss)){
                unmappingAndNomalHostids.removeAll(new ArrayList<>(hoss));
            }
            if (!CollectionUtils.isEmpty(hostIdsToHostGroupId2)) {
                for (String Hostgroupid : hostIdsToHostGroupId2.keySet()) {
                    if (!StringUtils.isEmpty(Hostgroupid) && !CollectionUtils.isEmpty(hostIdsToHostGroupId2.get(Hostgroupid))) {
                        addHostsToHostGroupbatch(Hostgroupid, hostIdsToHostGroupId2.get(Hostgroupid));
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            if (!StringUtils.isEmpty(e.getMessage())) {
                try {
                    JsonObject vjson = new JsonParser().parse(e.getMessage()).getAsJsonObject();
                    desc = StringUtil.dealQuotationMarks(ToolUtils.getStr(vjson.get("error_msg")));
                }catch (Exception ess){
                    desc = e.getMessage();
                }
            }
        }

        return desc;
    }
}
