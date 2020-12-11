package com.huawei.dmestore.services;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.dao.DmeVmwareRalationDao;
import com.huawei.dmestore.entity.DmeVmwareRelation;
import com.huawei.dmestore.entity.VCenterInfo;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.DmeSqlException;
import com.huawei.dmestore.model.Storage;
import com.huawei.dmestore.model.TaskDetailInfo;
import com.huawei.dmestore.model.VmfsDataInfo;
import com.huawei.dmestore.model.VmfsDatastoreVolumeDetail;
import com.huawei.dmestore.constant.DmeIndicatorConstants;
import com.huawei.dmestore.utils.RestUtils;
import com.huawei.dmestore.utils.ToolUtils;
import com.huawei.dmestore.utils.VCSDKUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @ClassName: VmfsAccessServiceImpl
 * @Company: GH-CA
 * @author: yy
 * @create: 2020-09-02
 **/
public class VmfsAccessServiceImpl implements VmfsAccessService {
    private static final Logger LOG = LoggerFactory.getLogger(VmfsAccessServiceImpl.class);

    private Gson gson = new Gson();

    private DmeVmwareRalationDao dmeVmwareRalationDao;

    private DmeAccessService dmeAccessService;

    private DataStoreStatisticHistoryService dataStoreStatisticHistoryService;

    private DmeStorageService dmeStorageService;

    private TaskService taskService;

    private VCSDKUtils vcsdkUtils;

    private VCenterInfoService vCenterInfoService;

    public void setvCenterInfoService(VCenterInfoService vCenterInfoService) {
        this.vCenterInfoService = vCenterInfoService;
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
            List<DmeVmwareRelation> dvrlist = dmeVmwareRalationDao.getDmeVmwareRelation(ToolUtils.STORE_TYPE_VMFS);
            if (dvrlist != null && dvrlist.size() > 0) {
                // 整理数据
                Map<String, DmeVmwareRelation> dvrMap = getDvrMap(dvrlist);
                // 取得所有的存储设备
                List<Storage> storagemap = dmeStorageService.getStorages();
                // 整理数据
                Map<String, String> stoNameMap = getStorNameMap(storagemap);
                // 取得vcenter中的所有vmfs存储。
                String listStr = vcsdkUtils.getAllVmfsDataStoreInfos(ToolUtils.STORE_TYPE_VMFS);
                if (!StringUtils.isEmpty(listStr)) {
                    JsonArray jsonArray = new JsonParser().parse(listStr).getAsJsonArray();
                    if (jsonArray != null && jsonArray.size() > 0) {
                        relists = new ArrayList<>();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject jo = jsonArray.get(i).getAsJsonObject();

                            String vmwareStoreobjectid = ToolUtils.jsonToStr(jo.get("objectid"));
                            if (!StringUtils.isEmpty(vmwareStoreobjectid)) {
                                // 对比数据库关系表中的数据，只显示关系表中的数据
                                if (dvrMap != null && dvrMap.get(vmwareStoreobjectid) != null) {
                                    VmfsDataInfo vmfsDataInfo = new VmfsDataInfo();
                                    double capacity = ToolUtils.getDouble(jo.get("capacity")) / ToolUtils.GI;
                                    double freeSpace = ToolUtils.getDouble(jo.get("freeSpace")) / ToolUtils.GI;
                                    double uncommitted = ToolUtils.getDouble(jo.get("uncommitted")) / ToolUtils.GI;

                                    vmfsDataInfo.setName(ToolUtils.jsonToStr(jo.get("name")));
                                    vmfsDataInfo.setCapacity(capacity);
                                    vmfsDataInfo.setFreeSpace(freeSpace);
                                    vmfsDataInfo.setReserveCapacity(capacity + uncommitted - freeSpace);
                                    vmfsDataInfo.setObjectid(ToolUtils.jsonToStr(jo.get("objectid")));

                                    DmeVmwareRelation dvr = dvrMap.get(vmwareStoreobjectid);
                                    String volumeId = dvr.getVolumeId();
                                    // 这里由于DME系统中的卷太多。是分页查询，所以需要vmfs一个个的去查DME系统中的卷。
                                    String detailedVolumeUrl = DmeConstants.DME_VOLUME_BASE_URL + "/" + volumeId;
                                    try {
                                        ResponseEntity responseEntity = dmeAccessService.access(detailedVolumeUrl,
                                            HttpMethod.GET, null);
                                        if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                                            JsonObject voljson = new JsonParser().parse(
                                                responseEntity.getBody().toString()).getAsJsonObject();
                                            JsonObject vjson2 = voljson.getAsJsonObject("volume");

                                            vmfsDataInfo.setVolumeId(ToolUtils.jsonToStr(vjson2.get("id")));
                                            vmfsDataInfo.setVolumeName(ToolUtils.jsonToStr(vjson2.get("name")));
                                            // 状态：creating：创建中；normal：正常；mapping：映射中；unmapping：解除映射中；deleting：删除中；error：错误；expanding：扩容中
                                            vmfsDataInfo.setStatus(ToolUtils.jsonToStr(vjson2.get("status")));
                                            vmfsDataInfo.setServiceLevelName(
                                                ToolUtils.jsonToStr(vjson2.get("service_level_name")));
                                            // 卷的保护状态， true表示卷已经被保护，false表示卷未被保护
                                            vmfsDataInfo.setVmfsProtected(ToolUtils.jsonToBoo(vjson2.get("protected")));
                                            vmfsDataInfo.setWwn(ToolUtils.jsonToStr(vjson2.get("volume_wwn")));

                                            String storageId = ToolUtils.jsonToStr(vjson2.get("storage_id"));
                                            vmfsDataInfo.setDeviceId(storageId);
                                            vmfsDataInfo.setDevice(stoNameMap == null ? "" : stoNameMap.get(storageId));

                                            if (vjson2 != null && !ToolUtils.jsonIsNull(vjson2.get("tuning"))) {
                                                JsonObject tuning = vjson2.getAsJsonObject("tuning");
                                                if (tuning != null && !ToolUtils.jsonIsNull(tuning.get("smartqos"))) {
                                                    JsonObject smartqos = tuning.getAsJsonObject("smartqos");
                                                    if (smartqos != null) {
                                                        vmfsDataInfo.setMaxIops(
                                                            ToolUtils.jsonToInt(smartqos.get("maxiops"), null));
                                                        vmfsDataInfo.setMinIops(
                                                            ToolUtils.jsonToInt(smartqos.get("miniops"), null));
                                                        vmfsDataInfo.setMaxBandwidth(
                                                            ToolUtils.jsonToInt(smartqos.get("maxbandwidth"), null));
                                                        vmfsDataInfo.setMinBandwidth(
                                                            ToolUtils.jsonToInt(smartqos.get("minbandwidth"), null));
                                                        vmfsDataInfo.setLatency(
                                                            ToolUtils.jsonToFloat(smartqos.get("latency"), null));
                                                    }
                                                }
                                            }
                                            relists.add(vmfsDataInfo);
                                        }
                                    } catch (Exception e) {
                                        LOG.error(
                                            "DME link error url:" + detailedVolumeUrl + ",error:" + e.getMessage());
                                    }
                                }
                            }
                        }
                    }
                } else {
                    LOG.info("list vmfs return empty");
                }
            }
        } catch (Exception e) {
            LOG.error("list vmfs error:", e);
            throw new DmeException(e.getMessage());
        }
        LOG.info("relists===" + (relists == null ? "null" : relists.size()));
        return relists;
    }

    @Override
    public List<VmfsDataInfo> listVmfsPerformance(List<String> wwns) throws DmeException {
        List<VmfsDataInfo> relists = null;
        try {
            if (wwns != null && wwns.size() > 0) {
                Map<String, Object> params = new HashMap<>(16);
                params.put("obj_ids", wwns);
                Map<String, Object> remap = dataStoreStatisticHistoryService.queryVmfsStatisticCurrent(params);
                if (remap != null && remap.size() > 0) {
                    try {
                        JsonObject dataJson = new JsonParser().parse(remap.toString()).getAsJsonObject();
                        if (dataJson != null) {
                            relists = new ArrayList<>();
                            for (String wwn : wwns) {
                                JsonObject statisticObject = dataJson.getAsJsonObject(wwn);
                                if (statisticObject != null) {
                                    VmfsDataInfo vmfsDataInfo = new VmfsDataInfo();
                                    vmfsDataInfo.setVolumeId(wwn);
                                    vmfsDataInfo.setWwn(wwn);
                                    vmfsDataInfo.setIops(ToolUtils.jsonToFloat(
                                        ToolUtils.getStatistcValue(statisticObject,
                                            DmeIndicatorConstants.COUNTER_ID_VOLUME_THROUGHPUT, "max"), null));
                                    vmfsDataInfo.setBandwidth(ToolUtils.jsonToFloat(
                                        ToolUtils.getStatistcValue(statisticObject,
                                            DmeIndicatorConstants.COUNTER_ID_VOLUME_BANDWIDTH, "max"), null));
                                    vmfsDataInfo.setReadResponseTime(ToolUtils.jsonToFloat(
                                        ToolUtils.getStatistcValue(statisticObject,
                                            DmeIndicatorConstants.COUNTER_ID_VOLUME_READRESPONSETIME, "max"), null));
                                    vmfsDataInfo.setWriteResponseTime(ToolUtils.jsonToFloat(
                                        ToolUtils.getStatistcValue(statisticObject,
                                            DmeIndicatorConstants.COUNTER_ID_VOLUME_WRITERESPONSETIME, "max"), null));
                                    vmfsDataInfo.setLatency(ToolUtils.jsonToFloat(
                                        ToolUtils.getStatistcValue(statisticObject,
                                            DmeIndicatorConstants.COUNTER_ID_VOLUME_RESPONSETIME, "max"), null));
                                    relists.add(vmfsDataInfo);
                                }
                            }
                        }
                    } catch (Exception e) {
                        LOG.warn("查询vmfs实时性能数据listVmfsPerformance异常", e);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("list vmfs performance error:", e);
            throw new DmeException(e.getMessage());
        }
        LOG.debug("listVmfsPerformance relists===" + (relists == null ? "null" : relists.size()));
        return relists;
    }

    @Override
    public void createVmfs(Map<String, Object> params) throws DmeException {
        if (params != null) {
            // param str host: 主机  param str cluster: 集群
            String objHostId = "";
            // 判断主机或主机组在DME中是否存在
            // 如果主机或主机不存在就创建并得到主机或主机组ID
            objHostId = checkOrcreateToHostorHostGroup(params);
            if (!StringUtils.isEmpty(objHostId)) {
                // 创建DME卷
                // 判断服务等级是否存在  service_level_id
                String taskId = "";
                if (null != params.get(DmeConstants.SERVICELEVELID)) {
                    taskId = createVmfsByServiceLevel(params, objHostId);
                } else {  // 非服务化的创建
                    // taskId = createVmfsByUnServiceLevel(params, objHostId);
                    taskId = createVmfsByUnServiceLevelNew(params, objHostId);
                }
                LOG.info("createVmfs taskId====" + taskId);
                if (!StringUtils.isEmpty(taskId)) {
                    // 查询看创建任务是否完成。
                    List<String> taskIds = new ArrayList<>();
                    taskIds.add(taskId);
                    boolean createFlag = taskService.checkTaskStatus(taskIds);
                    if (createFlag) {
                        // DME创建完成
                        // 查找刚才创建好的卷
                        String dmeHostId = null;
                        String demHostGroupId = null;
                        if (null != params.get(DmeConstants.HOST)) {
                            dmeHostId = objHostId;
                        } else if (null != params.get(DmeConstants.CLUSTER)) {
                            demHostGroupId = objHostId;
                        }
                        List<Map<String, Object>> volumelist = getVolumeByName(
                            ToolUtils.getStr(params.get("volumeName")), dmeHostId, demHostGroupId,
                            ToolUtils.getStr(params.get("service_level_id")),
                            ToolUtils.getStr(params.get("storage_id")), ToolUtils.getStr(params.get("pool_raw_id")));

                        // 创建了几个卷，就创建几个VMFS，用卷的wwn去找到lun
                        if (volumelist != null && volumelist.size() > 0) {
                            VCenterInfo vCenterInfo = null;
                            if (!StringUtils.isEmpty(params.get(DmeConstants.SERVICELEVELID))) {
                                vCenterInfo = vCenterInfoService.getVcenterInfo();
                            }
                            for (Map<String, Object> volumemap : volumelist) {
                                // 创建vmware中的vmfs存储。
                                params.put("volume_wwn", volumemap.get("volume_wwn"));
                                params.put("volume_name", volumemap.get("volume_name"));
                                String dataStoreStr = createVmfsOnVmware(params);
                                if (!StringUtils.isEmpty(dataStoreStr)) {
                                    Map<String, Object> dataStoreMap = gson.fromJson(dataStoreStr,
                                        new TypeToken<Map<String, Object>>() { }.getType());
                                    if (dataStoreMap != null) {
                                        // 将DME卷与vmfs的关系保存数据库
                                        // 因为可以同时创建几个卷，无法在此得到对应关系，所以此处不再保存关系信息
                                        saveDmeVmwareRalation(volumemap, dataStoreMap);
                                        // 关联服务等级
                                        if (!StringUtils.isEmpty(params.get("service_level_id"))) {
                                            String serviceLevelName = ToolUtils.getStr(
                                                params.get("service_level_name"));
                                            vcsdkUtils.attachTag(
                                                ToolUtils.getStr(dataStoreMap.get("type")),
                                                ToolUtils.getStr(dataStoreMap.get("id")), serviceLevelName,
                                                vCenterInfo);
                                        }
                                    }
                                } else {
                                    throw new DmeException("vmware create vmfs error:" + params.get("volume_name"));
                                }
                            }
                        }
                    } else {
                        TaskDetailInfo taskinfo = taskService.queryTaskById(taskId);
                        if (taskinfo != null) {
                            throw new DmeException(
                                "DME create vmfs volume error(task status info:" + "name:" + taskinfo.getTaskName()
                                    + ";status:" + taskinfo.getStatus() + ";" + "progress:" + taskinfo.getProgress()
                                    + ";detail:" + taskinfo.getDetail() + ")!");
                        } else {
                            throw new DmeException("DME create vmfs volume error(task status is failure)!");
                        }
                    }
                } else {
                    throw new DmeException("DME create vmfs volume error(task is null)!");
                }
            } else {
                throw new DmeException("DME find or create host error!");
            }
        } else {
            throw new DmeException("Parameter exception:" + params);
        }
    }

    private String createVmfsOnVmware(Map<String, Object> params) throws DmeException {
        // 在vmware创建存储
        String dataStoreStr = "";
        try {
            if (params != null) {
                // 创建vmware中的vmfs存储。 cluster host
                String hostObjectId = ToolUtils.getStr(params.get("hostId"));
                String clusterObjectId = ToolUtils.getStr(params.get("clusterId"));
                String datastoreName = ToolUtils.getStr(params.get("name"));
                int vmfsMajorVersion = ToolUtils.getInt(params.get("version"));
                int blockSize = ToolUtils.getInt(params.get("blockSize"));
                int unmapGranularity = ToolUtils.getInt(params.get("spaceReclamationGranularity"));
                String unmapPriority = ToolUtils.getStr(params.get("spaceReclamationPriority"));
                int capacity = ToolUtils.getInt(params.get("capacity"));

                String existVolumeWwn = ToolUtils.getStr(params.get("volume_wwn"));
                String existVolumeName = ToolUtils.getStr(params.get("volume_name"));
                String volumeName = ToolUtils.getStr(params.get("volumeName"));
                existVolumeName = existVolumeName.replaceAll(volumeName, "");
                // 根据后缀序号改变VMFS的名称
                datastoreName = datastoreName + existVolumeName;
                // 从主机或集群中找出最接近capacity的LUN
                Map<String, Object> hsdmap = null;
                if (null != params.get(DmeConstants.HOST)) {
                    hsdmap = vcsdkUtils.getLunsOnHost(hostObjectId, capacity, existVolumeWwn);
                } else if (null != params.get(DmeConstants.CLUSTER)) {
                    hsdmap = vcsdkUtils.getLunsOnCluster(clusterObjectId, capacity, existVolumeWwn);
                }

                // 创建
                dataStoreStr = vcsdkUtils.createVmfsDataStore(hsdmap, capacity, datastoreName, vmfsMajorVersion,
                    blockSize, unmapGranularity, unmapPriority);

                // 如果创建成功，在集群中的其他主机上扫描并挂载datastore
                if (!StringUtils.isEmpty(clusterObjectId)) {
                    vcsdkUtils.scanDataStore(clusterObjectId, null);
                }
            }
        } catch (Exception e) {
            LOG.error("createVmfsOnVmware error:" + e.toString());
            throw new DmeException(e.getMessage());
        }
        return dataStoreStr;
    }

    private String createVmfsByServiceLevel(Map<String, Object> params, String objhostid) throws DmeException {
        // 通过服务等级创建卷，返回任务ID
        String taskId = "";
        try {
            if (null != params && null != params.get(DmeConstants.SERVICELEVELID)) {
                Map<String, Object> requestbody = null;
                // 判断该集群下有多少主机，如果主机在DME不存在就需要创建
                requestbody = new HashMap<>();
                List<Map<String, Object>> volumes = new ArrayList<>();
                Map<String, Object> svbp = new HashMap<>();
                svbp.put("name", ToolUtils.getStr(params.get("volumeName")));
                svbp.put("capacity", ToolUtils.getInt(params.get("capacity")));
                svbp.put("count", ToolUtils.getInt(params.get("count")));

                volumes.add(svbp);

                requestbody.put("volumes", volumes);
                requestbody.put("service_level_id", ToolUtils.getStr(params.get("service_level_id")));

                Map<String, Object> mapping = new HashMap<>();
                if (!StringUtils.isEmpty(params.get(DmeConstants.HOST))) {
                    mapping.put("host_id", objhostid);
                } else {
                    mapping.put("hostgroup_id", objhostid);
                }
                requestbody.put("mapping", mapping);

                ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_VOLUME_BASE_URL, HttpMethod.POST,
                    gson.toJson(requestbody));
                if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_202) {
                    JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString())
                        .getAsJsonObject();
                    if (null != jsonObject && !ToolUtils.jsonIsNull(jsonObject.get(DmeConstants.TASKID))) {
                        taskId = ToolUtils.jsonToStr(jsonObject.get("task_id"));
                        LOG.info("createVmfsByServiceLevel task_id====" + taskId);
                    }
                } else {
                    throw new Exception("create volume error:" + responseEntity.getBody().toString());
                }
            }
        } catch (Exception e) {
            LOG.error("createVmfsByServiceLevel error:", e);
            throw new DmeException("create volume error:" + e.toString());
        }
        return taskId;
    }

    private String createVmfsByUnServiceLevel(Map<String, Object> params, String objhostid) throws DmeException {
        // 通过非服务化创建卷，返回任务ID
        String taskId = "";
        try {
            if (null != params && null != params.get(DmeConstants.STORAGEID)) {
                Map<String, Object> requestbody = new HashMap<>();
                // 判断该集群下有多少主机，如果主机在DME不存在就需要创建
                Map<String, Object> cv = new HashMap<>();
                cv.put("pool_raw_id", ToolUtils.getStr(params.get("pool_raw_id")));
                cv.put("storage_id", ToolUtils.getStr(params.get("storage_id")));

                Map<String, Object> tuning = new HashMap<>();
                tuning.put("alloctype", ToolUtils.getStr(params.get("alloctype")));
                tuning.put("workload_type_id", ToolUtils.getInt(params.get("workload_type_id"), null));

                Map<String, Object> smartqos = new HashMap<>();
                smartqos.put("control_policy", ToolUtils.getStr(params.get("control_policy")));
                smartqos.put("latency", ToolUtils.getInt(params.get("latency"), null));
                smartqos.put("maxbandwidth", ToolUtils.getInt(params.get("maxbandwidth"), null));
                smartqos.put("maxiops", ToolUtils.getInt(params.get("maxiops"), null));
                smartqos.put("minbandwidth", ToolUtils.getInt(params.get("minbandwidth"), null));
                smartqos.put("miniops", ToolUtils.getInt(params.get("miniops"), null));
                smartqos.put("name", ToolUtils.getStr(params.get("qosname")));

                if (!StringUtils.isEmpty(params.get(DmeConstants.CONTROLPOLICY))) {
                    tuning.put("smartqos", smartqos);
                }

                if (!StringUtils.isEmpty(params.get(DmeConstants.ALLOCTYPE)) || !StringUtils.isEmpty(
                    params.get("workload_type_id")) || !StringUtils.isEmpty(params.get("control_policy"))) {
                    cv.put("tuning", tuning);
                }

                List<Map<String, Object>> volumeSpecs = new ArrayList<>();
                Map<String, Object> vs = new HashMap<>();
                vs.put("name", ToolUtils.getStr(params.get("volumeName")));
                vs.put("capacity", ToolUtils.getInt(params.get("capacity")));
                vs.put("count", ToolUtils.getInt(params.get("count")));
                volumeSpecs.add(vs);

                cv.put("volume_specs", volumeSpecs);

                requestbody.put("customize_volumes", cv);

                Map<String, Object> mapping = new HashMap<>();
                if (!StringUtils.isEmpty(params.get(DmeConstants.HOST))) {
                    mapping.put("host_id", objhostid);
                } else {
                    mapping.put("hostgroup_id", objhostid);
                }
                requestbody.put("mapping", mapping);

                ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_CREATE_VOLUME_UNLEVEL_URL,
                    HttpMethod.POST, gson.toJson(requestbody));
                if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_202) {
                    JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString())
                        .getAsJsonObject();
                    if (null != jsonObject && !ToolUtils.jsonIsNull(jsonObject.get(DmeConstants.TASKID))) {
                        taskId = ToolUtils.jsonToStr(jsonObject.get("task_id"));
                        LOG.info("createVmfsUNServiceLevel task_id====" + taskId);
                    }
                } else {
                    throw new DmeException(
                        "create UNService Level volume error:" + responseEntity.getBody().toString());
                }
            } else {
                throw new DmeException("create UNService Level volume parameters error.");
            }
        } catch (Exception e) {
            LOG.error("createVmfsUNServiceLevel error:", e);
            throw new DmeException("create UNService Level volume error:" + e.toString());
        }
        return taskId;
    }

    private String createVmfsByUnServiceLevelNew(Map<String, Object> params, String objhostid) throws DmeException {
        // 通过非服务化创建卷，返回任务ID
        String taskId = "";
        try {
            if (null != params && null != params.get(DmeConstants.STORAGEID)) {
                Map<String, Object> requestbody = new HashMap<>();
                // 判断该集群下有多少主机，如果主机在DME不存在就需要创建
                requestbody.put("pool_id", ToolUtils.getStr(params.get("pool_raw_id")));
                requestbody.put("storage_id", ToolUtils.getStr(params.get("storage_id")));

                Map<String, Object> tuning = new HashMap<>();
                tuning.put("alloctype", ToolUtils.getStr(params.get("alloctype")));
                tuning.put("workload_type_raw_id", ToolUtils.getInt(params.get("workload_type_id"), null));

                Map<String, Object> smartqos = new HashMap<>();
                smartqos.put("control_policy", ToolUtils.getStr(params.get("control_policy")));
                smartqos.put("latency", ToolUtils.getInt(params.get("latency"), null));
                smartqos.put("maxbandwidth", ToolUtils.getInt(params.get("maxbandwidth"), null));
                smartqos.put("maxiops", ToolUtils.getInt(params.get("maxiops"), null));
                smartqos.put("minbandwidth", ToolUtils.getInt(params.get("minbandwidth"), null));
                smartqos.put("miniops", ToolUtils.getInt(params.get("miniops"), null));
                smartqos.put("name", ToolUtils.getStr(params.get("qosname")));

                if (!StringUtils.isEmpty(params.get(DmeConstants.CONTROLPOLICY))) {
                    tuning.put("smart_qos", smartqos);
                }

                if (!StringUtils.isEmpty(params.get(DmeConstants.ALLOCTYPE)) || !StringUtils.isEmpty(
                    params.get("workload_type_id")) || !StringUtils.isEmpty(params.get("control_policy"))) {
                    requestbody.put("tuning", tuning);
                }

                List<Map<String, Object>> volumeSpecs = new ArrayList<>();
                Map<String, Object> vs = new HashMap<>();
                vs.put("name", ToolUtils.getStr(params.get("volumeName")));
                vs.put("capacity", ToolUtils.getInt(params.get("capacity")));
                vs.put("count", ToolUtils.getInt(params.get("count")));
                volumeSpecs.add(vs);
                requestbody.put("lun_specs", volumeSpecs);

                Map<String, Object> mapping = new HashMap<>();
                if (!StringUtils.isEmpty(params.get(DmeConstants.HOST))) {
                    mapping.put("host_id", objhostid);
                } else {
                    mapping.put("hostgroup_id", objhostid);
                }
                requestbody.put("mapping", mapping);

                ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_CREATE_VOLUME_UNLEVEL_URL,
                    HttpMethod.POST, gson.toJson(requestbody));
                if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_202) {
                    JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString())
                        .getAsJsonObject();
                    if (null != jsonObject && !ToolUtils.jsonIsNull(jsonObject.get(DmeConstants.TASKID))) {
                        taskId = ToolUtils.jsonToStr(jsonObject.get("task_id"));
                        LOG.info("createVmfsUNServiceLevel task_id====" + taskId);
                    }
                } else {
                    throw new DmeException(
                        "create UNService Level volume error:" + responseEntity.getBody().toString());
                }
            } else {
                throw new DmeException("create UNService Level volume parameters error.");
            }
        } catch (Exception e) {
            LOG.error("createVmfsUNServiceLevel error:", e);
            throw new DmeException("create UNService Level volume error:" + e.toString());
        }
        return taskId;
    }

    private String checkToHost(String hostId) throws DmeException {
        // 判断vcenter主机在DME中是否存在
        String objId = "";
        try {
            // param str host: 主机  param str cluster: 集群
            // 判断主机或主机组在DME中是否存在
            if (!StringUtils.isEmpty(hostId)) {
                // 通过主机的objectid查到主机上所有的hba的wwn或者iqn
                List<Map<String, Object>> hbas = vcsdkUtils.getHbasByHostObjectId(hostId);
                if (hbas != null && hbas.size() > 0) {
                    List<String> wwniqns = new ArrayList<>();
                    for (Map<String, Object> hba : hbas) {
                        wwniqns.add(ToolUtils.getStr(hba.get("name")));
                    }
                    // 取出DME所有主机
                    List<Map<String, Object>> hostlist = dmeAccessService.getDmeHosts(null);
                    if (hostlist != null && hostlist.size() > 0) {
                        for (Map<String, Object> hostmap : hostlist) {
                            if (hostmap != null && hostmap.get("id") != null) {
                                // 通过主机ID查到对应的主机的启动器
                                String demHostId = ToolUtils.getStr(hostmap.get("id"));
                                // 得到主机的启动器
                                List<Map<String, Object>> initiators = dmeAccessService.getDmeHostInitiators(demHostId);
                                if (initiators != null && initiators.size() > 0) {
                                    for (Map<String, Object> inimap : initiators) {
                                        String portName = ToolUtils.getStr(inimap.get("port_name"));
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
                    }
                } else {
                    throw new DmeException(hostId + " The host did not find a valid HbA");
                }
            }
        } catch (Exception e) {
            LOG.error("checkToHost error:", e);
            throw new DmeException(e.getMessage());
        }
        return objId;
    }

    @Override
    public String checkOrCreateToHost(String hostIp, String hostId) throws DmeException {
        // 判断主机在DME中是否存在 如果主机不存在就创建并得到主机ID
        String objId = "";
        try {
            // param str host: 主机  param str cluster: 集群
            // 判断主机或主机组在DME中是否存在
            if (!StringUtils.isEmpty(hostId)) {
                // 通过主机的objectid查到主机上所有的hba的wwn或者iqn
                List<Map<String, Object>> hbas = vcsdkUtils.getHbasByHostObjectId(hostId);
                if (hbas != null && hbas.size() > 0) {
                    List<String> wwniqns = new ArrayList<>();
                    for (Map<String, Object> hba : hbas) {
                        wwniqns.add(ToolUtils.getStr(hba.get("name")));
                    }

                    // 取出所有主机
                    List<Map<String, Object>> hostlist = dmeAccessService.getDmeHosts(null);
                    if (hostlist != null && hostlist.size() > 0) {
                        for (Map<String, Object> hostmap : hostlist) {
                            if (hostmap != null && hostmap.get("id") != null) {
                                // 通过主机ID查到对应的主机的启动器
                                String demHostId = ToolUtils.getStr(hostmap.get("id"));
                                // 得到主机的启动器
                                List<Map<String, Object>> initiators = dmeAccessService.getDmeHostInitiators(demHostId);
                                if (initiators != null && initiators.size() > 0) {
                                    for (Map<String, Object> inimap : initiators) {
                                        String portName = ToolUtils.getStr(inimap.get("port_name"));
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
                    }
                    // 如果主机或主机不存在就创建并得到主机或主机组ID
                    if (StringUtils.isEmpty(objId)) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("host", hostIp);
                        params.put("hostId", hostId);
                        Map<String, Object> hostmap = dmeAccessService.createHost(params);
                        if (null != hostmap && null != hostmap.get(DmeConstants.ID)) {
                            objId = hostmap.get("id").toString();
                        }
                    }
                } else {
                    throw new Exception(hostIp + " The host did not find a valid HbA");
                }
            }
        } catch (Exception e) {
            LOG.error("checkOrCreateToHost error:", e);
            throw new DmeException(e.getMessage());
        }
        return objId;
    }

    private String checkOrCreateToHostGroup(String clusterObjectId) throws DmeException {
        // 如果主机组不存在就创建并得到主机组ID 创建前要检查集群下的所有主机是否在DME中存在，只能通过id来创建主机组，如果集群有中文，dme中会创建失败
        String objId = "";
        try {
            // param str host: 主机  param str cluster: 集群
            // 如果主机或主机不存在就创建并得到主机或主机组ID 如果主机组不存在就需要创建,创建前要检查集群下的所有主机是否在DME中存在
            String clustername = vcsdkUtils.getVcConnectionHelper().objectId2Mor(clusterObjectId).getValue();
            if (!StringUtils.isEmpty(clusterObjectId)) {
                List<String> objIds = new ArrayList<>();
                // 检查集群对应的主机组在DME中是否存在
                List<Map<String, Object>> hostgrouplist = dmeAccessService.getDmeHostGroups(clustername);
                if (hostgrouplist != null && hostgrouplist.size() > 0) {
                    for (Map<String, Object> hostgroupmap : hostgrouplist) {
                        if (hostgroupmap != null && hostgroupmap.get("name") != null) {
                            if (clustername.equals(hostgroupmap.get("name").toString())) {
                                String tmpObjId = ToolUtils.getStr(hostgroupmap.get("id"));
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
                    // 取得集群下的所有主机
                    String vmwarehosts = vcsdkUtils.getHostsOnCluster(clusterObjectId);
                    if (!StringUtils.isEmpty(vmwarehosts)) {
                        List<Map<String, String>> vmwarehostlists = gson.fromJson(vmwarehosts,
                            new TypeToken<List<Map<String, String>>>() { }.getType());
                        if (vmwarehostlists != null && vmwarehostlists.size() > 0) {
                            // 分别检查每一个主机是否存在，如果不存在就创建
                            List<String> hostlists = new ArrayList<>();
                            for (Map<String, String> hostmap : vmwarehostlists) {
                                String tmpHostId = checkOrCreateToHost(ToolUtils.getStr(hostmap.get("hostName")),
                                    ToolUtils.getStr(hostmap.get("hostId")));
                                if (!StringUtils.isEmpty(tmpHostId)) {
                                    hostlists.add(tmpHostId);
                                }
                            }
                            // 在DME中创建主机组
                            if (hostlists.size() > 0) {
                                Map<String, Object> params = new HashMap<>();
                                params.put("cluster",
                                    vcsdkUtils.getVcConnectionHelper().objectId2Mor(clusterObjectId).getValue());
                                params.put("hostids", hostlists);
                                Map<String, Object> hostmap = dmeAccessService.createHostGroup(params);
                                if (null != hostmap && null != hostmap.get(DmeConstants.ID)) {
                                    objId = ToolUtils.getStr(hostmap.get("id"));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("checkOrCreateToHostGroup error:", e);
            throw new DmeException(e.getMessage());
        }
        return objId;
    }

    private String checkToHostGroup(String clusterObjectId) throws DmeException {
        // 检查集群下的所有主机是否在DME中存在
        String objId = "";
        try {
            // param str host: 主机  param str cluster: 集群
            // 如果主机或主机不存在就创建并得到主机或主机组ID 如果主机组不存在就需要创建,创建前要检查集群下的所有主机是否在DME中存在
            String clustername = vcsdkUtils.getVcConnectionHelper().objectId2Mor(clusterObjectId).getValue();
            if (!StringUtils.isEmpty(clustername)) {
                List<String> objIds = new ArrayList<>();
                // 检查集群对应的主机组在DME中是否存在
                List<Map<String, Object>> hostgrouplist = dmeAccessService.getDmeHostGroups(clustername);
                if (hostgrouplist != null && hostgrouplist.size() > 0) {
                    for (Map<String, Object> hostgroupmap : hostgrouplist) {
                        if (hostgroupmap != null && hostgroupmap.get("name") != null) {
                            if (clustername.equals(hostgroupmap.get("name").toString())) {
                                String tmpObjId = ToolUtils.getStr(hostgroupmap.get("id"));
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

                // 检查集群下的所有主机是否在DME中存在
                if (StringUtils.isEmpty(objId)) {
                    // 取得集群下的所有主机
                    String vmwarehosts = vcsdkUtils.getHostsOnCluster(clusterObjectId);
                    if (!StringUtils.isEmpty(vmwarehosts)) {
                        List<Map<String, String>> vmwarehostlists = gson.fromJson(vmwarehosts,
                            new TypeToken<List<Map<String, String>>>() { }.getType());
                        if (vmwarehostlists != null && vmwarehostlists.size() > 0) {
                            // 分别检查每一个主机是否存在，如果不存在就创建
                            List<String> hostlists = new ArrayList<>();
                            for (Map<String, String> hostmap : vmwarehostlists) {
                                String tmpHostId = checkToHost(ToolUtils.getStr(hostmap.get("hostId")));
                                if (!StringUtils.isEmpty(tmpHostId)) {
                                    hostlists.add(tmpHostId);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("checkOrCreateToHostGroup error:", e);
            throw new DmeException(e.getMessage());
        }
        return objId;
    }

    private String checkHostInHostGroup(String vmwareClusterObjectId, String dmeHostGroupId) throws DmeException {
        String objId = "";
        try {
            // 得到集群下所有的主机的hba
            if (!StringUtils.isEmpty(vmwareClusterObjectId)) {
                List<Map<String, Object>> hbas = vcsdkUtils.getHbasByClusterObjectId(vmwareClusterObjectId);
                if (hbas != null && hbas.size() > 0) {
                    // 整理hba名称
                    List<String> wwniqns = new ArrayList<>();
                    for (Map<String, Object> hba : hbas) {
                        wwniqns.add(ToolUtils.getStr(hba.get("name")));
                    }

                    // 得到DME中主机组下所有的主机对应的hba，并进行对比
                    List<Map<String, Object>> initiators = null;
                    List<Map<String, Object>> dmehosts = dmeAccessService.getDmeHostInHostGroup(dmeHostGroupId);
                    if (dmehosts != null && dmehosts.size() > 0) {
                        initiators = new ArrayList<>();
                        for (Map<String, Object> dmehost : dmehosts) {
                            // 得到主机的启动器
                            if (dmehost != null && dmehost.get("id") != null) {
                                String demHostId = ToolUtils.getStr(dmehost.get("id"));
                                List<Map<String, Object>> subinitiators = dmeAccessService.getDmeHostInitiators(
                                    demHostId);
                                if (subinitiators != null && subinitiators.size() > 0) {
                                    initiators.addAll(subinitiators);
                                }
                            }
                        }
                        // 整理启动器
                        if (initiators.size() > 0) {
                            List<String> initiatorName = new ArrayList<>();
                            for (Map<String, Object> inimap : initiators) {
                                if (inimap != null && inimap.get("port_name") != null) {
                                    String portName = ToolUtils.getStr(inimap.get("port_name"));
                                    initiatorName.add(portName);
                                }
                            }

                            // 对比集群中的主机hba与主机组中的启动器是否一致
                            boolean checkHbaInHostGroup = ToolUtils.compare(wwniqns, initiatorName);
                            if (checkHbaInHostGroup) {
                                objId = dmeHostGroupId;
                            }
                        } else {
                            LOG.error("DME initiators In host is null");
                        }
                    } else {
                        LOG.error("DME Host In HostGroup is null");
                    }
                } else {
                    LOG.error("vmware Cluster hbas is null");
                }
            } else {
                LOG.error("vmware Cluster Object Id is null");
            }
        } catch (Exception e) {
            LOG.error("checkHostInHostGroup error:", e);
            throw new DmeException(e.getMessage());
        }
        return objId;
    }

    private String checkOrcreateToHostorHostGroup(Map<String, Object> params) throws DmeException {
        // 根据参数选择检查主机或主机组的方法
        String objId = "";
        try {
            // param str host: 主机  param str cluster: 集群
            if (null != params && null != params.get(DmeConstants.HOST)) {
                objId = checkOrCreateToHost(ToolUtils.getStr(params.get("host")),
                    ToolUtils.getStr(params.get("hostId")));
            } else if (null != params && null != params.get(DmeConstants.CLUSTER)) {
                objId = checkOrCreateToHostGroup(ToolUtils.getStr(params.get("clusterId")));
            }
        } catch (Exception e) {
            LOG.error("checkOrcreateToHostorHostGroup error:", e);
            throw new DmeException(e.getMessage());
        }
        return objId;
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
        if (!StringUtils.isEmpty(poolRawId)) {
            listVolumeUrl = listVolumeUrl + "&pool_raw_id=" + poolRawId;
        }
        try {
            ResponseEntity responseEntity = dmeAccessService.access(listVolumeUrl, HttpMethod.GET, null);
            String s = new Gson().toJson(responseEntity);
            if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();

                if (jsonObject != null && jsonObject.get(DmeConstants.VOLUMES) != null) {
                    volumelist = new ArrayList<>();
                    JsonArray jsonArray = jsonObject.getAsJsonArray("volumes");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject vjson = jsonArray.get(i).getAsJsonObject();
                        if (vjson != null) {
                            Map<String, Object> remap = new HashMap<>();
                            remap.put("volume_id", ToolUtils.jsonToStr(vjson.get("id")));
                            remap.put("volume_name", ToolUtils.jsonToStr(vjson.get("name")));
                            remap.put("volume_wwn", ToolUtils.jsonToStr(vjson.get("volume_wwn")));
                            volumelist.add(remap);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("DME link error url:" + listVolumeUrl + ",error:" + e.getMessage());
        }
        LOG.info("listVolumeUrl===" + (volumelist == null ? "null" : volumelist.size()));
        return volumelist;
    }

    private void saveDmeVmwareRalation(Map<String, Object> volumeMap, Map<String, Object> dataStoreMap) {
        // 保存卷与vmfs的关联关系
        try {
            if (volumeMap == null || volumeMap.get(DmeConstants.VOLUMEID) == null) {
                LOG.error("save Dme and Vmware's vmfs Ralation error: volume data is null");
                return;
            }
            if (dataStoreMap == null || dataStoreMap.get(DmeConstants.ID) == null) {
                LOG.error("save Dme and Vmware's vmfs Ralation error: dataStore data is null");
                return;
            }
            List<DmeVmwareRelation> rallist = new ArrayList<>();

            DmeVmwareRelation dvr = new DmeVmwareRelation();
            dvr.setStoreId(ToolUtils.getStr(dataStoreMap.get("objectId")));
            dvr.setStoreName(ToolUtils.getStr(dataStoreMap.get("name")));
            dvr.setVolumeId(ToolUtils.getStr(volumeMap.get("volume_id")));
            dvr.setVolumeName(ToolUtils.getStr(volumeMap.get("volume_name")));
            dvr.setVolumeWwn(ToolUtils.getStr(volumeMap.get("volume_wwn")));
            dvr.setStoreType(ToolUtils.STORE_TYPE_VMFS);

            rallist.add(dvr);
            dmeVmwareRalationDao.save(rallist);
            LOG.info("save DmeVmwareRalation success");
        } catch (Exception e) {
            LOG.error("save DmeVmwareRalation error:", e);
        }
    }

    @Override
    public void mountVmfs(Map<String, Object> params) throws DmeException {
        if (params != null) {
            //  modify 20201021 传递参数中取消host/cluster  此处通过hostId/clusterId补充
            String hostObjId = ToolUtils.getStr(params.get("hostId"));
            String clusterObjId = ToolUtils.getStr(params.get("clusterId"));
            if (!StringUtils.isEmpty(hostObjId)) {
                String hostName = vcsdkUtils.getHostName(hostObjId);
                params.put("host", hostName);
            }
            if (!StringUtils.isEmpty(clusterObjId)) {
                String clusterName = vcsdkUtils.getClusterName(clusterObjId);
                params.put("cluster", clusterName);
            }
            // param str host: 主机  param str cluster: 集群  dataStoreObjectIds
            String objhostid = "";
            // 判断主机或主机组在DME中是否存在
            // 如果主机或主机不存在就创建并得到主机或主机组ID
            objhostid = checkOrcreateToHostorHostGroup(params);
            if (!StringUtils.isEmpty(objhostid)) {
                // 挂载卷
                String taskId = "";
                // 通过存储的objectid查询卷id
                if (params.get(DmeConstants.DATASTOREOBJECTIDS) != null) {
                    List<String> dataStoreObjectIds = (List<String>) params.get("dataStoreObjectIds");
                    if (dataStoreObjectIds != null && dataStoreObjectIds.size() > 0) {
                        List<String> volumeIds = new ArrayList<>();
                        List<String> dataStoreNames = new ArrayList<>();
                        for (String dsObjectId : dataStoreObjectIds) {
                            DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dsObjectId);
                            if (dvr != null) {
                                volumeIds.add(dvr.getVolumeId());
                                dataStoreNames.add(dvr.getStoreName());
                            }
                        }
                        if (volumeIds.size() > 0) {
                            params.put("volumeIds", volumeIds);
                            params.put("dataStoreNames", dataStoreNames);
                        }
                    }
                }

                LOG.info("mountvmfs==" + gson.toJson(params));

                if (params.get(DmeConstants.HOST) != null) {
                    // 将卷挂载到主机DME
                    taskId = mountVmfsToHost(params, objhostid);
                } else {
                    // 将卷挂载到集群DME
                    taskId = mountVmfsToHostGroup(params, objhostid);
                }
                LOG.info("taskId====" + taskId);
                // 查询看创建任务是否完成。
                if (!StringUtils.isEmpty(taskId)) {
                    List<String> taskIds = new ArrayList<>();
                    taskIds.add(taskId);
                    boolean mountFlag = taskService.checkTaskStatus(taskIds);
                    if (mountFlag) {
                        // DME创建完成
                        // 调用vCenter在主机上扫描卷和Datastore
                        vcsdkUtils.scanDataStore(ToolUtils.getStr(params.get("clusterId")),
                            ToolUtils.getStr(params.get("hostId")));
                        // 如果是需要扫描LUN来挂载，则需要执行下面的方法，dataStoreNames
                        if (params.get(DmeConstants.DATASTORENAMES) != null) {
                            List<String> dataStoreNames = (List<String>) params.get("dataStoreNames");
                            if (dataStoreNames != null && dataStoreNames.size() > 0) {
                                for (String dataStoreName : dataStoreNames) {
                                    Map<String, Object> dsmap = new HashMap<>();
                                    dsmap.put("name", dataStoreName);

                                    vcsdkUtils.mountVmfsOnCluster(gson.toJson(dsmap),
                                        ToolUtils.getStr(params.get("clusterId")),
                                        ToolUtils.getStr(params.get("hostId")));
                                }
                            }
                        }
                    } else {
                        throw new DmeException("DME mount vmfs volume error(task status)!");
                    }
                } else {
                    throw new DmeException("DME mount vmfs volume error(task is null)!");
                }
            } else {
                throw new DmeException("DME find or create host error!");
            }
        } else {
            throw new DmeException("Parameter exception:" + params);
        }
    }

    private String mountVmfsToHost(Map<String, Object> params, String objhostid) {
        // 将卷挂载到主机DME
        String taskId = "";
        try {
            if (params != null && params.get(DmeConstants.VOLUMEIDS) != null) {
                Map<String, Object> requestbody = null;
                // 判断该集群下有多少主机，如果主机在DME不存在就需要创建
                requestbody = new HashMap<>();
                List<String> volumeIds = (List<String>) params.get("volumeIds");

                requestbody.put("volume_ids", volumeIds);
                requestbody.put("host_id", objhostid);
                ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_HOST_MAPPING_URL,
                    HttpMethod.POST, gson.toJson(requestbody));
                if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_202) {
                    JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString())
                        .getAsJsonObject();
                    if (jsonObject != null && jsonObject.get(DmeConstants.TASKID) != null) {
                        taskId = ToolUtils.jsonToStr(jsonObject.get("task_id"));
                        LOG.info("mountVmfsToHost task_id====" + taskId);
                    }
                }
            } else {
                LOG.error("mountVmfsToHost error:volumeIds is null");
            }
        } catch (Exception e) {
            LOG.error("mountVmfsToHost error:", e);
        }
        return taskId;
    }

    private String mountVmfsToHostGroup(Map<String, Object> params, String objhostid) {
        // 将卷挂载到集群DME
        String taskId = "";
        try {
            if (params != null && params.get(DmeConstants.VOLUMEIDS) != null) {
                Map<String, Object> requestbody = null;
                // 判断该集群下有多少主机，如果主机在DME不存在就需要创建
                requestbody = new HashMap<>();
                List<String> volumeIds = (List<String>) params.get("volumeIds");

                requestbody.put("volume_ids", volumeIds);
                requestbody.put("hostgroup_id", objhostid);

                ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.MOUNT_VOLUME_TO_HOSTGROUP_URL,
                    HttpMethod.POST, gson.toJson(requestbody));
                if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_202) {
                    JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString())
                        .getAsJsonObject();
                    if (jsonObject != null && jsonObject.get(DmeConstants.TASKID) != null) {
                        taskId = ToolUtils.jsonToStr(jsonObject.get("task_id"));
                        LOG.info("mountVmfsToHostGroup task_id====" + taskId);
                    }
                }
            } else {
                LOG.error("mountVmfsToHost error:volumeIds is null");
            }
        } catch (Exception e) {
            LOG.error("mountVmfsToHostGroup error:", e);
        }
        return taskId;
    }

    @Override
    public List<VmfsDatastoreVolumeDetail> volumeDetail(String storageObjectId) throws DmeException {
        List<VmfsDatastoreVolumeDetail> list = new ArrayList<>();
        // 根据存储ID获取磁盘ID
        List<String> volumeIds = dmeVmwareRalationDao.getVolumeIdsByStorageId(storageObjectId);
        for (String volumeId : volumeIds) {
            // 调用DME接口获取卷详情
            String url = DmeConstants.DME_VOLUME_BASE_URL + "/" + volumeId;
            ResponseEntity<String> responseEntity;
            responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() / 100 != 2) {
                LOG.error("查询卷信息失败！错误信息:{}", responseEntity.getBody());
                throw new DmeException(responseEntity.getBody());
            }

            String responseBody = responseEntity.getBody();
            JsonObject volume = gson.fromJson(responseBody, JsonObject.class).getAsJsonObject("volume");
            if (volume.isJsonNull()) {
                continue;
            }

            VmfsDatastoreVolumeDetail volumeDetail = new VmfsDatastoreVolumeDetail();
            // basic info
            volumeDetail.setWwn(volume.get("volume_wwn").getAsString());
            volumeDetail.setName(volume.get("name").getAsString());
            volumeDetail.setServiceLevel(ToolUtils.jsonToStr(volume.get("service_level_name"), null));
            if (!volume.get("storage_id").isJsonNull()) {
                String storageId = volume.get("storage_id").getAsString();
                // 根据存储ID查询存储信息
                url = DmeConstants.DME_STORAGE_DETAIL_URL.replace("{storage_id}", storageId);
                responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
                if (responseEntity.getStatusCodeValue() / 100 == 2) {
                    JsonObject storeageDetail = gson.fromJson(responseEntity.getBody(), JsonObject.class);
                    volumeDetail.setStorage(storeageDetail.get("name").getAsString());
                }
            }

            if (!volume.get("pool_raw_id").isJsonNull()) {
                String poolId = volume.get("pool_raw_id").getAsString();
                url = String.format(DmeConstants.DME_RESOURCE_INSTANCE_LIST, "SYS_StoragePool");
                // 构造查询body
                String constraint = String.format(
                    "{\"constraint\": [{\"simple\": {\"name\": \"poolId\",\"value\": \"%s\"}}]}", poolId);
                url = url + "?condition={json}";
                responseEntity = dmeAccessService.accessByJson(url, HttpMethod.GET, constraint);
                if (responseEntity.getStatusCodeValue() / 100 == 2) {
                    JsonObject o = gson.fromJson(responseEntity.getBody(), JsonObject.class);
                    if (o.get("totalNum").getAsInt() > 0) {
                        JsonArray objList = o.getAsJsonArray("objList");
                        volumeDetail.setStoragePool(objList.get(0).getAsJsonObject().get("name").getAsString());
                    }
                }
            }

            JsonObject tuning = volume.getAsJsonObject("tuning");
            if (!tuning.isJsonNull()) {
                // SmartTier
                volumeDetail.setSmartTier(tuning.get("smarttier").getAsString());
                // Tunning
                volumeDetail.setDudeplication(ToolUtils.jsonToBoo(tuning.get("dedupe_enabled")));
                volumeDetail.setProvisionType(ToolUtils.jsonToStr(tuning.get("alloctype"), null));
                volumeDetail.setCompression(ToolUtils.jsonToBoo(tuning.get("compression_enabled")));
                // 应用类型
                volumeDetail.setApplicationType(ToolUtils.jsonToStr(tuning.get("workload_type_id"), null));

                // Qos Policy
                if (!tuning.get("smartqos").isJsonNull()) {
                    JsonObject smartqos = tuning.getAsJsonObject("smartqos");
                    volumeDetail.setControlPolicy(ToolUtils.jsonToStr(smartqos.get("control_policy"), null));
                    // TODO
                    volumeDetail.setTrafficControl("--");
                }
            }
            list.add(volumeDetail);
        }

        return list;
    }

    @Override
    public boolean scanVmfs() throws DmeException {
        String listStr = vcsdkUtils.getAllVmfsDataStoreInfos(ToolUtils.STORE_TYPE_VMFS);
        if (StringUtils.isEmpty(listStr)) {
            LOG.info("===list vmfs datastore return empty");
            return false;
        }
        JsonArray jsonArray = new JsonParser().parse(listStr).getAsJsonArray();
        List<DmeVmwareRelation> relationList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject vmfsDatastore = jsonArray.get(i).getAsJsonObject();
            String storeType = ToolUtils.STORE_TYPE_VMFS;
            String vmfsDatastoreId = vmfsDatastore.get("objectid").getAsString();
            String vmfsDatastoreName = vmfsDatastore.get("name").getAsString();
            JsonArray wwnArray = vmfsDatastore.getAsJsonArray("vmfsWwnList");
            if (null == wwnArray || wwnArray.size() == 0) {
                continue;
            }

            for (int j = 0; j < wwnArray.size(); j++) {
                String wwn = wwnArray.get(j).getAsString();
                // 根据wwn从DME中查询卷信息
                String volumeUrlByName = DmeConstants.DME_VOLUME_BASE_URL + "?volume_wwn=" + wwn;
                ResponseEntity<String> responseEntity = dmeAccessService.access(volumeUrlByName, HttpMethod.GET, null);
                if (responseEntity.getStatusCodeValue() / 100 != 2) {
                    LOG.info(" Query DME volume failed! errorMsg:{}", responseEntity.toString());
                    continue;
                }
                JsonObject jsonObject = gson.fromJson(responseEntity.getBody(), JsonObject.class);
                JsonElement volumesElement = jsonObject.get("volumes");
                if (!ToolUtils.jsonIsNull(volumesElement)) {
                    JsonArray volumeArray = volumesElement.getAsJsonArray();
                    if (volumeArray.size() > 0) {
                        JsonObject volumeObject = volumeArray.get(0).getAsJsonObject();
                        String volumeId = ToolUtils.jsonToOriginalStr(volumeObject.get("id"));
                        String volumeName = ToolUtils.jsonToOriginalStr(volumeObject.get("name"));
                        String volumeWwn = ToolUtils.jsonToOriginalStr(volumeObject.get("volume_wwn"));

                        DmeVmwareRelation relation = new DmeVmwareRelation();
                        relation.setStoreId(vmfsDatastoreId);
                        relation.setStoreName(vmfsDatastoreName);
                        relation.setVolumeId(volumeId);
                        relation.setVolumeName(volumeName);
                        relation.setVolumeWwn(volumeWwn);
                        relation.setStoreType(storeType);
                        relation.setState(1);

                        relationList.add(relation);
                    }
                }
            }
        }

        if (relationList.size() > 0) {
            // 数据库处理
            return dmeVmwareRelationDbProcess(relationList, ToolUtils.STORE_TYPE_VMFS);
        }
        return true;
    }

    private boolean dmeVmwareRelationDbProcess(List<DmeVmwareRelation> relationList, String storeType)
        throws DmeSqlException {
        // 本地全量查询
        List<String> localWwns = dmeVmwareRalationDao.getAllWwnByType(storeType);

        List<DmeVmwareRelation> newList = new ArrayList<>();
        List<DmeVmwareRelation> upList = new ArrayList<>();
        for (DmeVmwareRelation o : relationList) {
            String wwn = o.getVolumeWwn();
            if (localWwns.contains(wwn)) {
                upList.add(o);
                localWwns.remove(wwn);
            } else {
                newList.add(o);
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
        if (!localWwns.isEmpty()) {
            dmeVmwareRalationDao.deleteByWwn(localWwns);
        }
        return true;
    }

    /**
     * @param params dataStoreObjectIds hostId hostGroupId由调用处传递过来
     * @throws Exception
     */
    @Override
    public void unmountVmfs(Map<String, Object> params) throws DmeException {
        List<String> taskIds = new ArrayList<>();
        List<String> dataStoreObjectIds = null;
        String hostObjId = "";
        String clusterObjId = "";
        // 获取vmfs关联的dme侧volume 并提取volumeId
        if (null != params && null != params.get(DmeConstants.DATASTOREOBJECTIDS)) {
            dataStoreObjectIds = (List<String>) params.get("dataStoreObjectIds");
            if (dataStoreObjectIds != null && dataStoreObjectIds.size() > 0) {
                List<String> volumeIds = new ArrayList<>();
                List<String> dataStoreNames = new ArrayList<>();
                for (String dsObjectId : dataStoreObjectIds) {
                    DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dsObjectId);
                    if (null == dvr) {
                        //  如果数据库中不存 则重新扫描一次
                        scanVmfs();
                    }
                    dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dsObjectId);
                    if (dvr != null) {
                        volumeIds.add(dvr.getVolumeId());
                        dataStoreNames.add(dvr.getStoreName());
                    }
                }
                if (volumeIds.size() > 0) {
                    params.put("volumeIds", volumeIds);
                    params.put("dataStoreNames", dataStoreNames);
                }
            } else {
                throw new DmeException("unmount volume params dataStoreObjectIds is null!");
            }
        }
        // 通过vcenter的hostId 获取dme侧的hostId 或者hostGroupId 分别删除
        if (null != params && null != params.get(DmeConstants.VOLUMEIDS)) {
            if (null != params.get("hostId")) {
                hostObjId = ToolUtils.getStr(params.get("hostId"));
                Map<String, Object> hostMap = getDmeHostByHostObjeId(hostObjId);
                if (null != hostMap && hostMap.size() > 0) {
                    params.put("host_id", ToolUtils.getStr(hostMap.get("id")));
                    String taskId = unmountHostGetTaskId(params);
                    if (!StringUtils.isEmpty(taskId)) {
                        taskIds.add(taskId);
                    }
                }
            }
            if (null != params.get("clusterId")) {
                clusterObjId = ToolUtils.getStr(params.get("clusterId"));
                Map<String, Object> hostGroupMap = getDmeHostGroupByClusterObjId(clusterObjId);
                if (null != hostGroupMap && hostGroupMap.size() > 0) {
                    params.put("hostGroup_id", ToolUtils.getStr(hostGroupMap.get("hostGroupId")));
                    String taskId = unmountHostGroupGetTaskId(params);
                    if (!StringUtils.isEmpty(taskId)) {
                        taskIds.add(taskId);
                    }
                }
            }
        }

        //  获取卸载的任务完成后的状态 (默认超时时间10分钟)
        boolean unmountFlag = false;
        if (taskIds.size() > 0) {
            unmountFlag = taskService.checkTaskStatus(taskIds);
        }

        // 判断是否卸载vmfs上的全部主机或集群 若是 则删除dme侧的卷，目前卸载参数中的主机/集群只支持单个,所以vmfs只挂载了一个主机或集群dme侧就直接删除卷
        boolean volumeDeleteFalg = false;
        if (null != params.get("hostId")) {
            for (String dsObj : dataStoreObjectIds) {
                List<Map<String, Object>> hosts = getHostsByStorageId(dsObj);
                if (null != hosts && hosts.size() == 1) {
                    volumeDelete(params);
                    volumeDeleteFalg = true;
                    break;
                }
            }
        }
        if (!volumeDeleteFalg && null != params.get("clusterId")) {
            for (String dsObjId : dataStoreObjectIds) {
                List<Map<String, Object>> hostGroups = getHostGroupsByStorageId(dsObjId);
                if (null != hostGroups && hostGroups.size() == 1) {
                    volumeDelete(params);
                    volumeDeleteFalg = true;
                    break;
                }
            }
        }
        if (!unmountFlag) {
            throw new DmeException(
                "unmount volume precondition unmount host and hostGroup error(task status),taskIds:(" + gson.toJson(
                    taskIds) + ")!");
        } else {
            // 如果是需要扫描LUN来卸载，则需要执行下面的方法，dataStoreNames
            if (params.get(DmeConstants.DATASTORENAMES) != null) {
                List<String> dataStoreNames = (List<String>) params.get("dataStoreNames");
                // 
                if (dataStoreNames != null && dataStoreNames.size() > 0) {
                    for (String dataStoreName : dataStoreNames) {
                        Map<String, Object> dsmap = new HashMap<>();
                        dsmap.put("name", dataStoreName);
                        vcsdkUtils.unmountVmfsOnHostOrCluster(gson.toJson(dsmap), clusterObjId, hostObjId);
                    }
                }
            }
        }
        // 若卸载vmfs上的全部主机或集群 最后重新扫描 此步会自动删除vmfs
        if (volumeDeleteFalg) {
            vcsdkUtils.scanDataStore(clusterObjId, hostObjId);
        }
    }

    // 删除前的卸载
    public List<String> unmountVmfsAll(Map<String, Object> params) throws Exception {
        List<String> taskIds = new ArrayList<>();
        // 获取vmfs关联的dme侧volume 并提取volumeId
        if (null != params && null != params.get(DmeConstants.DATASTOREOBJECTIDS)) {
            List<String> dataStoreObjectIds = (List<String>) params.get("dataStoreObjectIds");
            List<String> dataStorageIds = new ArrayList<>();
            if (dataStoreObjectIds != null && dataStoreObjectIds.size() > 0) {
                List<String> volumeIds = new ArrayList<>();
                List<String> dataStoreNames = new ArrayList<>();
                for (String dsObjectId : dataStoreObjectIds) {
                    // 如果dsObject包含虚拟机 则不能删除
                    boolean hasVmflag = vcsdkUtils.hasVmOnDatastore(dsObjectId);
                    if (hasVmflag) {
                        LOG.info("vmfs delete,the vmfs:{} contain vm,can not delete!!!", dsObjectId);
                        continue;
                    }
                    DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dsObjectId);
                    if (null == dvr) {
                        scanVmfs();
                        dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dsObjectId);
                    }
                    if (dvr != null) {
                        volumeIds.add(dvr.getVolumeId());
                        dataStoreNames.add(dvr.getStoreName());
                        dataStorageIds.add(dsObjectId);
                    }
                }
                if (volumeIds.size() > 0) {
                    params.put("volumeIds", volumeIds);
                    params.put("dataStoreNames", dataStoreNames);
                }
                // 没有满足条件的datastore 直接返回taskids(size=0)
                if (dataStorageIds.size() == 0) {
                    LOG.info("vmfs delete,all vmfs contain vm,can not delete!!!");
                    // 抛出异常?
                    return taskIds;
                }
            }

            // 获取vcenter关联的所有hostId, dataStoreObjectIds只有一个值
            for (String dsObjectId : dataStorageIds) {
                List<String> unmountTaskIds = unmountVmfs(dsObjectId, params);
                if (null != unmountTaskIds && unmountTaskIds.size() > 0) {
                    taskIds.addAll(unmountTaskIds);
                }
            }
        }

        // vcenter侧卸载 调用HostStorageSystemMo的rescanVmfs()方法
       /* String hostId = params.get("host_id").toString();
        Map<String, Object> hostInfoMap = dmeAccessService.getDmeHost(hostId);
        String hostIp = hostInfoMap.get("ip").toString();
        vcsdkUtils.hostRescanVmfs(hostIp);*/

        return taskIds;
    }

    // 删除前的卸载 返回taskIds
    private List<String> unmountVmfs(String dsObjId, Map<String, Object> params) throws Exception {
        List<String> taskIds = new ArrayList<>();
        // 获取vmfs关联的dme侧volume 并提取volumeId
        List<String> hostObjIds = new ArrayList<>();
        List<String> volumeIds = new ArrayList<>();
        DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dsObjId);
        LOG.info("getVolumeId==" + dvr.getVolumeId());
        if (dvr != null) {
            volumeIds.add(dvr.getVolumeId());
        }

        // 没有指定集群 查询存储关联的所有集群
        List<Map<String, Object>> vcClusters = getHostGroupsByStorageId(dsObjId);
        if (volumeIds.size() > 0 && null != vcClusters && vcClusters.size() > 0) {
            for (Map<String, Object> vcCluster : vcClusters) {
                String hostGroupObjectId = ToolUtils.getStr(vcCluster.get("hostGroupId"));
                if (!StringUtils.isEmpty(hostGroupObjectId)) {
                    Map<String, Object> hostGroupMap = getDmeHostGroupByClusterObjId(hostGroupObjectId);
                    // vcenter的集群和dem的host的启动器一样
                    if (null != hostGroupMap && hostGroupMap.size() > 0) {
                        String hostGroupIdDmeId = ToolUtils.getStr(hostGroupMap.get("hostGroupId"));
                        // 是否关联vm
                        if (dataStoreVmRelateHostOrCluster(dsObjId, null, hostGroupObjectId)) {
                            continue;
                        }
                        Map<String, Object> tempParams = new HashMap<>();
                        tempParams.put("hostGroup_id", hostGroupIdDmeId);
                        tempParams.put("volumeIds", volumeIds);
                        String taskId = unmountHostGroupGetTaskId(tempParams);
                        taskIds.add(taskId);
                    }
                }
            }
        }

        // 没有指定主机 查询datastore下的主机 并过滤与vm有关联的
        List<Map<String, Object>> vcHosts = getHostsByStorageId(dsObjId);
        if (volumeIds.size() > 0 && null != vcHosts && vcHosts.size() > 0) {
            for (Map<String, Object> vcHost : vcHosts) {
                String hostObjectId = ToolUtils.getStr(vcHost.get("hostId"));
                if (!StringUtils.isEmpty(hostObjectId)) {
                    Map<String, Object> hostMap = getDmeHostByHostObjeId(hostObjectId);
                    // vcenter的host和dem的host的启动器一样
                    if (null != hostMap && hostMap.size() > 0) {
                        hostObjIds.add(hostObjectId);
                        String hostId = ToolUtils.getStr(hostMap.get("id"));
                        // 是否关联vm
                        if (dataStoreVmRelateHostOrCluster(dsObjId, hostObjectId, null)) {
                            continue;
                        }
                        Map<String, Object> tempParams = new HashMap<>();
                        tempParams.put("host_id", hostId);
                        tempParams.put("volumeIds", volumeIds);
                        String taskId = unmountHostGetTaskId(tempParams);
                        // 若主机组已卸载,获取主机的卸载任务信息会有对象不存在的提示,标记任务为失败。获取任务结果的目的是等待卸载任务执行完成，不关心任务成功失败
                        taskIds.add(taskId);
                    }
                }
            }
        }

        // 提取datastore的hostid
        if (hostObjIds.size() > 0) {
            Object object = params.get("hostObjIds");
            if (null != object) {
                try {
                    List<String> hostObjIdList = (List<String>) object;
                    if (null != hostObjIdList && hostObjIdList.size() > 0) {
                        hostObjIds.addAll(hostObjIdList);
                    }
                } catch (Exception e) {
                    LOG.warn("unmountVmfs get hostObjectid error!", e);
                }
            }
            params.put("hostObjIds", hostObjIds);
        }

        // 删除前的卸载 vcenter侧不扫描,执行删除时再扫描
        // host scan
        return taskIds;
    }

    @Override
    public void deleteVmfs(Map<String, Object> params) throws DmeException {
        // 先调卸载的接口 卸载是卸载所有所有主机和集群(dem侧主机,主机组)
        try {
            List<String> unmountTaskIds = unmountVmfsAll(params);
            if (null != unmountTaskIds && unmountTaskIds.size() > 0) {
                // 检测任务等待卸载完成后再删除,不用判断是否卸载成功
                taskService.checkTaskStatus(unmountTaskIds);
            }
        } catch (Exception e) {
            LOG.error("delete volume precondition unmapping host and hostGroup error!");
            throw new DmeException("delete volume precondition unmapping host and hostGroup error!");
        }
        // DME侧卸载卷
        String taskId = volumeDeleteGetTaskId(params);
        boolean dmeDeleteFlag = false;
        if (!StringUtils.isEmpty(taskId)) {
            dmeDeleteFlag = taskService.checkTaskStatus(Arrays.asList(taskId));
        }
        // vcenter侧 扫描
        List<String> hostObjIds = (List<String>) params.get("hostObjIds");
        if (null != hostObjIds && hostObjIds.size() > 0) {
            // 过滤重复的hostObjId
            hostObjIds = new ArrayList<>(new HashSet(hostObjIds));
            for (String hostObjId : hostObjIds) {
                vcsdkUtils.scanDataStore(null, hostObjId);
            }
        } else {
            // dme侧已删除(卸载) hostObjectIds参数为空，此时通过dsObjectId查询hostObjId,再扫描一次
            List<String> dataStoreObjectIds = (List<String>) params.get("dataStoreObjectIds");
            List<Map<String, String>> lists = new ArrayList<>();
            if (null != dataStoreObjectIds && dataStoreObjectIds.size() > 0) {
                for (String dsObjId : dataStoreObjectIds) {
                    String listStr = vcsdkUtils.getMountHostsByDsObjectId(dsObjId);
                    if (!StringUtils.isEmpty(listStr)) {
                        lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() { }.getType());
                    }
                    if (null != lists && lists.size() > 0) {
                        for (Map<String, String> hostMap : lists) {
                            String hostObjId = hostMap.get("hostId");
                            if (!StringUtils.isEmpty(hostObjId)) {
                                vcsdkUtils.scanDataStore(null, hostObjId);
                            }
                        }
                    }
                }
            }
        }
        if (!dmeDeleteFlag) {
            throw new DmeException("delete volume precondition unmount host and hostGroup error(task status)!");
        }
        // 重新扫描关联关系 更新数据库
        scanVmfs();
    }

    private ResponseEntity queryVmfsById(String volumeId) throws Exception {
        // 查询指定vmfs
        String url = DmeConstants.DME_VOLUME_BASE_URL + "/" + volumeId;
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() / DmeConstants.HTTPS_STATUS_CHECK_FLAG
                != DmeConstants.HTTPS_STATUS_SUCCESS_PRE) {
                LOG.error("查询指定卷信息失败!错误信息:{}", responseEntity.getBody());
                return null;
            }
        } catch (Exception ex) {
            LOG.error("查询卷信息异常", ex);
            return null;
        }
        return responseEntity;
    }

    // DME侧从主机卸载卷
    private String unmountHostGetTaskId(Map<String, Object> params) {
        String taskId = "";
        try {
            ResponseEntity responseEntity = hostUnmapping(params);
            taskId = getTaskId(responseEntity);
        } catch (DmeException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return taskId;
    }

    // DME侧删除磁盘 获取任务ID
    private String volumeDeleteGetTaskId(Map<String, Object> params) {
        String taskId = "";
        try {
            ResponseEntity responseEntity = volumeDelete(params);
            taskId = getTaskId(responseEntity);
        } catch (DmeException e) {
            e.printStackTrace();
        }
        return taskId;
    }

    private ResponseEntity hostUnmapping(Map<String, Object> params) throws DmeException {
        String hostId = ToolUtils.getStr(params.get("host_id"));
        Object volumeIds = params.get("volumeIds");
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("host_id", hostId);
        requestbody.put("volume_ids", volumeIds);
        ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_HOST_UNMAPPING_URL, HttpMethod.POST,
            gson.toJson(requestbody));
        return responseEntity;
    }

    private ResponseEntity hostGroupUnmapping(Map<String, Object> params) throws DmeException {
        String hostGroupId = ToolUtils.getStr(params.get("hostGroup_id"));
        Object volumeIds = params.get("volumeIds");
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("hostgroup_id", hostGroupId);
        requestbody.put("volume_ids", volumeIds);
        ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.HOSTGROUP_UNMAPPING, HttpMethod.POST,
            gson.toJson(requestbody));
        return responseEntity;
    }

    private ResponseEntity volumeDelete(Map<String, Object> params) throws DmeException {
        Object volumeIds = params.get("volumeIds");
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("volume_ids", volumeIds);
        ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_VOLUME_DELETE_URL, HttpMethod.POST,
            gson.toJson(requestbody));
        return responseEntity;
    }

    private Map<String, DmeVmwareRelation> getDvrMap(List<DmeVmwareRelation> dvrlist) {
        // 整理关系表数据
        Map<String, DmeVmwareRelation> remap = null;
        try {
            if (dvrlist != null && dvrlist.size() > 0) {
                remap = new HashMap<>();
                for (DmeVmwareRelation dvr : dvrlist) {
                    remap.put(dvr.getStoreId(), dvr);
                }
            }
        } catch (Exception e) {
            LOG.error("error:", e);
        }
        return remap;
    }

    private Map<String, String> getStorNameMap(List<Storage> list) {
        // 整理存储信息
        Map<String, String> remap = null;
        try {

            if (list != null && list.size() > 0) {
                remap = new HashMap<>();
                for (Storage sr : list) {
                    remap.put(sr.getId(), sr.getName());
                }
            }
        } catch (Exception e) {
            LOG.error("error:", e);
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
        if (null != responseEntity) {
            if (RestUtils.RES_STATE_I_202 == responseEntity.getStatusCodeValue()) {
                try {
                    Object hostGroupBody = responseEntity.getBody();
                    if (null != hostGroupBody) {
                        JsonObject hostJson = new JsonParser().parse(hostGroupBody.toString()).getAsJsonObject();
                        taskId = ToolUtils.jsonToStr(hostJson.get("task_id"));
                    }
                } catch (JsonSyntaxException e) {
                    LOG.warn("从结构体:{}中获取任务ID失败!", null == responseEntity ? "" : gson.toJson(responseEntity));
                }
            }
        }
        return taskId;
    }

    @Override
    public List<Map<String, Object>> getHostsByStorageId(String storageId) throws DmeException {
        List<Map<String, Object>> hostMapList = new ArrayList<>(16);
        //  先查询vcenter侧的主机
        List<Map<String, String>> hosts = null;
        String listStr = vcsdkUtils.getHostsByDsObjectId(storageId, true);

        // 获取已挂载的集群，找到对应的主机，用于排除主机
        List<Map<String, Object>> clustermaps = getHostGroupsByStorageId(storageId);
        Map<String, String> excludehostmap = new HashMap<>();
        for (Map<String, Object> clustermap : clustermaps) {
            String clusterid = String.valueOf(clustermap.get("hostGroupId"));
            String vmwarehosts = vcsdkUtils.getHostsOnCluster(clusterid);
            if (!StringUtils.isEmpty(vmwarehosts)) {
                List<Map<String, String>> vmwarehostlists = gson.fromJson(vmwarehosts,
                    new TypeToken<List<Map<String, String>>>() { }.getType());

                for (Map<String, String> vmwarehostmap : vmwarehostlists) {
                    excludehostmap.put(vmwarehostmap.get("hostId"), "true");
                }
            }
        }
        if (!StringUtils.isEmpty(listStr)) {
            hosts = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() { }.getType());
            // vcenter侧主机启动器是否和dem侧主机启动器一致
            for (Map<String, String> host : hosts) {
                String hostId = ToolUtils.getStr(host.get("hostId"));
                // 排除已挂载在集群中的主机
                if (null == excludehostmap.get(hostId)) {
                    String hostNmme = ToolUtils.getStr(host.get("hostName"));
                    String initiatorId = checkToHost(hostId);
                    if (!StringUtils.isEmpty(initiatorId)) {
                        Map<String, Object> tempMap = new HashMap<>();
                        tempMap.put("hostId", hostId);
                        tempMap.put("hostName", hostNmme);
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

        List<Map<String, String>> clusters;
        // 取得vcenter中的所有cluster
        DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(storageId);
        if (null == dvr) {
            throw new DmeException("存储关联关系为空");
        }
        List<String> hostgroupids = getDmeAttachHostGroupByVolumeId(dvr.getVolumeId());
        Map<String, String> mappeddmegroups = new HashMap<>();
        for (String hostgroupid : hostgroupids) {
            Map<String, Object> hostgroupmap = dmeAccessService.getDmeHostGroup(hostgroupid);
            mappeddmegroups.put(String.valueOf(hostgroupmap.get("name")), "has");
        }
        String listStr = vcsdkUtils.getMountClustersByDsObjectId(storageId, mappeddmegroups);
        if (!StringUtils.isEmpty(listStr)) {
            clusters = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() { }.getType());
            // vcenter侧主机启动器是否和dem侧主机启动器一致
            for (Map<String, String> cluster : clusters) {
                String clusterId = ToolUtils.getStr(cluster.get("clusterId"));
                String clusterNmme = ToolUtils.getStr(cluster.get("clusterName"));
                String initiatorId = checkToHostGroup(clusterId);
                if (!StringUtils.isEmpty(initiatorId)) {
                    Map<String, Object> tempMap = new HashMap<>();
                    tempMap.put("hostGroupId", clusterId);
                    tempMap.put("hostGroupName", clusterNmme);
                    hostGroupMapList.add(tempMap);
                }
            }
        }
        return hostGroupMapList;
    }

    private List<String> getDmeAttachHostGroupByVolumeId(String volumeId) throws DmeException {
        String url;
        List<String> groupids = new ArrayList<>();
        url = DmeConstants.DME_VOLUME_BASE_URL + "/" + volumeId;
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        int code = responseEntity.getStatusCodeValue();
        if (code != HttpStatus.OK.value()) {
            throw new DmeException("search host id error");
        }
        String object = responseEntity.getBody();
        JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
        JsonObject volume = jsonObject.get("volume").getAsJsonObject();
        JsonArray attachments = volume.get("attachments").getAsJsonArray();
        for (JsonElement jsonElement : attachments) {
            JsonObject element = jsonElement.getAsJsonObject();
            String attachedHostGroupId = ToolUtils.jsonToStr(element.get("attached_host_group"));
            if (!"".equalsIgnoreCase(attachedHostGroupId)) {
                groupids.add(attachedHostGroupId);
            }
        }

        return groupids;
    }

    // 通过vcenter的主机ID 查询dme侧的主机信息
    private Map<String, Object> getDmeHostByHostObjeId(String hostObjId) throws DmeException {
        Map<String, Object> hostInfo = new HashMap<>();
        Map<String, Object> hbaMap = vcsdkUtils.getHbaByHostObjectId(hostObjId);
        if (null != hbaMap && hbaMap.size() > 0) {
            String initiatorName = ToolUtils.getStr(hbaMap.get("name"));
            // 取出DME所有主机
            List<Map<String, Object>> hostlist = dmeAccessService.getDmeHosts(null);
            if (hostlist != null && hostlist.size() > 0) {
                for (Map<String, Object> hostmap : hostlist) {
                    if (hostmap != null && hostmap.get("id") != null) {
                        // 通过主机ID查到对应的主机的启动器
                        String demHostId = ToolUtils.getStr(hostmap.get("id"));
                        // 得到主机的启动器
                        List<Map<String, Object>> initiators = dmeAccessService.getDmeHostInitiators(demHostId);
                        if (initiators != null && initiators.size() > 0) {
                            for (Map<String, Object> inimap : initiators) {
                                String portName = ToolUtils.getStr(inimap.get("port_name"));
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
            }
        }
        return hostInfo;
    }

    // 通过vcenter的集群ID 查询dme侧的主机组信息
    private Map<String, Object> getDmeHostGroupByClusterObjId(String clusterObjId) throws DmeException {
        Map<String, Object> hostGroupMap = new HashMap<>();
        String initiatorId = checkToHostGroup(clusterObjId);
        if (!StringUtils.isEmpty(initiatorId)) {
            hostGroupMap.put("hostGroupId", initiatorId);
        }
        return hostGroupMap;
    }

    public boolean isVmfs(String objectId) throws DmeSqlException {
        List<DmeVmwareRelation> dvrlist = dmeVmwareRalationDao.getDmeVmwareRelation(ToolUtils.STORE_TYPE_VMFS);
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
        try {
            // 从关系表中取得DME卷与vcenter存储的对应关系
            List<DmeVmwareRelation> dvrlist = dmeVmwareRalationDao.getDmeVmwareRelation(ToolUtils.STORE_TYPE_VMFS);
            if (dvrlist != null && dvrlist.size() > 0) {
                // 整理数据
                Map<String, DmeVmwareRelation> dvrMap = getDvrMap(dvrlist);

                // 取得所有的存储设备
                List<Storage> storagemap = dmeStorageService.getStorages();
                // 整理数据
                Map<String, String> stoNameMap = getStorNameMap(storagemap);
                // 取得vcenter中的所有vmfs存储。
                String listStr = vcsdkUtils.getAllVmfsDataStoreInfos(ToolUtils.STORE_TYPE_VMFS);
                if (!StringUtils.isEmpty(listStr)) {
                    JsonArray jsonArray = new JsonParser().parse(listStr).getAsJsonArray();
                    if (jsonArray != null && jsonArray.size() > 0) {
                        relists = new ArrayList<>();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject jo = jsonArray.get(i).getAsJsonObject();

                            String vmwareStoreobjectid = ToolUtils.jsonToStr(jo.get("objectid"));
                            if (!StringUtils.isEmpty(vmwareStoreobjectid) && vmwareStoreobjectid.equals(dsObjId)) {
                                // 对比数据库关系表中的数据，只显示关系表中的数据
                                if (dvrMap != null && dvrMap.get(vmwareStoreobjectid) != null) {
                                    VmfsDataInfo vmfsDataInfo = new VmfsDataInfo();
                                    double capacity = ToolUtils.getDouble(jo.get("capacity")) / ToolUtils.GI;
                                    double freeSpace = ToolUtils.getDouble(jo.get("freeSpace")) / ToolUtils.GI;
                                    double uncommitted = ToolUtils.getDouble(jo.get("uncommitted")) / ToolUtils.GI;

                                    vmfsDataInfo.setName(ToolUtils.jsonToStr(jo.get("name")));
                                    vmfsDataInfo.setCapacity(capacity);
                                    vmfsDataInfo.setFreeSpace(freeSpace);
                                    vmfsDataInfo.setReserveCapacity(capacity + uncommitted - freeSpace);
                                    vmfsDataInfo.setObjectid(ToolUtils.jsonToStr(jo.get("objectid")));

                                    DmeVmwareRelation dvr = dvrMap.get(vmwareStoreobjectid);
                                    String volumeId = dvr.getVolumeId();
                                    LOG.info("volumeId==" + volumeId);
                                    // 这里由于DME系统中的卷太多。是分页查询，所以需要vmfs一个个的去查DME系统中的卷。
                                    String detailedVolumeUrl = DmeConstants.DME_VOLUME_BASE_URL + "/" + volumeId;
                                    try {
                                        ResponseEntity responseEntity = dmeAccessService.access(detailedVolumeUrl,
                                            HttpMethod.GET, null);
                                        if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
                                            JsonObject voljson = new JsonParser().parse(
                                                responseEntity.getBody().toString()).getAsJsonObject();
                                            JsonObject vjson2 = voljson.getAsJsonObject("volume");

                                            vmfsDataInfo.setVolumeId(ToolUtils.jsonToStr(vjson2.get("id")));
                                            vmfsDataInfo.setVolumeName(ToolUtils.jsonToStr(vjson2.get("name")));
                                            vmfsDataInfo.setStatus(ToolUtils.jsonToStr(vjson2.get("status")));
                                            vmfsDataInfo.setServiceLevelName(
                                                ToolUtils.jsonToStr(vjson2.get("service_level_name")));
                                            vmfsDataInfo.setVmfsProtected(ToolUtils.jsonToBoo(vjson2.get("protected")));
                                            vmfsDataInfo.setWwn(ToolUtils.jsonToStr(vjson2.get("volume_wwn")));

                                            String storageId = ToolUtils.jsonToStr(vjson2.get("storage_id"));
                                            vmfsDataInfo.setDeviceId(storageId);
                                            vmfsDataInfo.setDevice(stoNameMap == null ? "" : stoNameMap.get(storageId));

                                            if (vjson2 != null && !ToolUtils.jsonIsNull(vjson2.get("tuning"))) {
                                                JsonObject tuning = vjson2.getAsJsonObject("tuning");
                                                if (tuning != null && !ToolUtils.jsonIsNull(tuning.get("smartqos"))) {
                                                    JsonObject smartqos = tuning.getAsJsonObject("smartqos");
                                                    if (smartqos != null) {
                                                        vmfsDataInfo.setMaxIops(
                                                            ToolUtils.jsonToInt(smartqos.get("maxiops"), null));
                                                        vmfsDataInfo.setMinIops(
                                                            ToolUtils.jsonToInt(smartqos.get("miniops"), null));
                                                        vmfsDataInfo.setMaxBandwidth(
                                                            ToolUtils.jsonToInt(smartqos.get("maxbandwidth"), null));
                                                        vmfsDataInfo.setMinBandwidth(
                                                            ToolUtils.jsonToInt(smartqos.get("minbandwidth"), null));
                                                        vmfsDataInfo.setLatency(
                                                            ToolUtils.jsonToFloat(smartqos.get("latency"), null));
                                                    }
                                                }
                                            }
                                            relists.add(vmfsDataInfo);
                                        }
                                    } catch (Exception e) {
                                        LOG.error(
                                            "DME link error url:" + detailedVolumeUrl + ",error:" + e.getMessage());
                                    }
                                }
                            }
                        }
                    }
                } else {
                    LOG.info("Vmfs listStr=null");
                }
            }
        } catch (Exception e) {
            LOG.error("query vmfs error:", e);
            throw e;
        }
        LOG.info("relists===" + (relists == null ? "null" : relists.size()));
        return relists;
    }

    @Override
    public Boolean queryDatastoreByName(String name) {
        Boolean flag = true;
        try {
            String dataStoreName = dmeVmwareRalationDao.getDataStoreByName(name);
            if (!StringUtils.isEmpty(dataStoreName)) {
                flag = false;
            }
        } catch (DmeSqlException e) {
            e.printStackTrace();
        }
        return flag;
    }

    // 查询dataStrore上的vm 看是否关联了指定的主机或集群
    private boolean dataStoreVmRelateHostOrCluster(String dsObjid, String hostObjId, String clusterObjId) {
        // 1 查询datastore上的vm
        // 2 vm是否关联了hostObjId 或 clusterObjId (objId转id?)
        return false;
    }
}
