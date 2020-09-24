package com.dmeplugin.dmestore.services;


import com.dmeplugin.dmestore.dao.DmeVmwareRalationDao;
import com.dmeplugin.dmestore.entity.DmeVmwareRelation;
import com.dmeplugin.dmestore.model.Storage;
import com.dmeplugin.dmestore.model.VmfsDataInfo;
import com.dmeplugin.dmestore.model.VmfsDatastoreVolumeDetail;
import com.dmeplugin.dmestore.model.Volume;
import com.dmeplugin.dmestore.utils.ToolUtils;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.*;

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

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
    }

    private final String LIST_VOLUME_URL = "/rest/blockservice/v1/volumes";
    private final String HOST_UNMAPAPING = "/rest/blockservice/v1/volumes/host-unmapping";
    private final String HOSTGROUP_UNMAPPING = "/rest/blockservice/v1/volumes/hostgroup-unmapping";
    private final String VOLUME_DELETE = "/rest/blockservice/v1/volumes/delete";
    private final String CREATE_VOLUME_URL = "/rest/blockservice/v1/volumes";
    private final String CREATE_VOLUME_UNSERVICE_URL = "/rest/blockservice/v1/volumes";
    private final String MOUNT_VOLUME_TO_HOST_URL = "/rest/blockservice/v1/volumes/host-mapping";
    private final String MOUNT_VOLUME_TO_HOSTGROUP_URL = "/rest/blockservice/v1/volumes/hostgroup-mapping";


    @Override
    public List<VmfsDataInfo> listVmfs() throws Exception {
        List<VmfsDataInfo> relists = null;
        try {
            //从关系表中取得DME卷与vcenter存储的对应关系
            List<DmeVmwareRelation> dvrlist = dmeVmwareRalationDao.getDmeVmwareRelation(ToolUtils.STORE_TYPE_VMFS);
            LOG.info("dvrlist==" + gson.toJson(dvrlist));
            if (dvrlist != null && dvrlist.size() > 0) {
                //整理数据
                Map<String, DmeVmwareRelation> dvrMap = getDvrMap(dvrlist);
                //取得所有的存储设备
                Map<String, Object> storagemap = dmeStorageService.getStorages();
                //整理数据
                Map<String, String> stoNameMap = getStorNameMap(storagemap);
                LOG.info("stoNameMap===" + gson.toJson(stoNameMap));
                //取得vcenter中的所有vmfs存储。
                String listStr = vcsdkUtils.getAllVmfsDataStoreInfos(ToolUtils.STORE_TYPE_VMFS);
                LOG.info("Vmfs listStr==" + listStr);
                if (!StringUtils.isEmpty(listStr)) {
                    JsonArray jsonArray = new JsonParser().parse(listStr).getAsJsonArray();
                    if (jsonArray != null && jsonArray.size() > 0) {
                        relists = new ArrayList<>();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject jo = jsonArray.get(i).getAsJsonObject();
                            //LOG.info("jo==" + jo.toString());
                            String vmwareStoreobjectid = ToolUtils.jsonToStr(jo.get("objectid"));
                            if (!StringUtils.isEmpty(vmwareStoreobjectid)) {
                                //对比数据库关系表中的数据，只显示关系表中的数据
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
                                    String volid = dvr.getVolumeId();
                                    LOG.info("volid==" + volid);
                                    //然后通过vmfs中的url值去DME系统中查询对应wwn的卷信息。
                                    ///rest/blockservice/v1/volumes?volume_wwn=wwn
                                    //这里由于DME系统中的卷太多。是分页查询，所以需要vmfs一个个的去查DME系统中的卷。
                                    //而每次查询DME中的卷都需要调用两次，分别是查卷列表接口，查卷详细接口。
                                    String detailedVolumeUrl = LIST_VOLUME_URL + "/" + volid;
                                    try {
                                        ResponseEntity responseEntity = dmeAccessService.access(detailedVolumeUrl, HttpMethod.GET, null);
                                        LOG.info("volid responseEntity==" + responseEntity.toString());
                                        if (responseEntity.getStatusCodeValue() == 200) {
                                            JsonObject voljson = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                                            //LOG.info("volid voljson==" + voljson.toString());
                                            JsonObject vjson2 = voljson.getAsJsonObject("volume");

                                            vmfsDataInfo.setVolumeId(ToolUtils.jsonToStr(vjson2.get("id")));
                                            vmfsDataInfo.setStatus(ToolUtils.jsonToStr(vjson2.get("status")));
                                            vmfsDataInfo.setServiceLevelName(ToolUtils.jsonToStr(vjson2.get("service_level_name")));
                                            vmfsDataInfo.setVmfsProtected(ToolUtils.jsonToBoo(vjson2.get("protected")));

                                            String storageId = ToolUtils.jsonToStr(vjson2.get("storage_id"));
                                            vmfsDataInfo.setDeviceId(storageId);
                                            vmfsDataInfo.setDevice(stoNameMap == null ? "" : stoNameMap.get(storageId));

                                            if (vjson2 != null && vjson2.get("tuning") != null) {
                                                JsonObject tuning = vjson2.getAsJsonObject("tuning");
                                                if (tuning != null && tuning.get("smartqos") != null) {
                                                    JsonObject smartqos = tuning.getAsJsonObject("smartqos");
                                                    if (smartqos != null) {
                                                        vmfsDataInfo.setMaxIops(ToolUtils.jsonToInt(smartqos.get("maxiops"), null));
                                                        vmfsDataInfo.setMinIops(ToolUtils.jsonToInt(smartqos.get("miniops"), null));
                                                        vmfsDataInfo.setMaxBandwidth(ToolUtils.jsonToInt(smartqos.get("maxbandwidth"), null));
                                                        ;
                                                        vmfsDataInfo.setMinBandwidth(ToolUtils.jsonToInt(smartqos.get("minbandwidth"), null));
                                                        vmfsDataInfo.setLatency(ToolUtils.jsonToInt(smartqos.get("latency"), null));
                                                    }
                                                }
                                            }

                                            relists.add(vmfsDataInfo);
                                        }
                                    } catch (Exception e) {
                                        LOG.error("DME link error url:" + detailedVolumeUrl + ",error:" + e.getMessage());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("list vmfs error:", e);
            throw e;
        }
        LOG.info("relists===" + (relists == null ? "null" : (relists.size() + "==" + gson.toJson(relists))));
        return relists;
    }

    @Override
    public List<VmfsDataInfo> listVmfsPerformance(List<String> volumeIds) throws Exception {
        List<VmfsDataInfo> relists = null;
        try {
            if (volumeIds != null && volumeIds.size() > 0) {
                Map<String, Object> params = new HashMap<>();
                params.put("obj_ids", volumeIds);
                Map<String, Object> remap = dataStoreStatisticHistoryService.queryVmfsStatisticCurrent(params);
                LOG.info("remap===" + gson.toJson(remap));
                if (remap != null && remap.get("data") != null) {
                    JsonObject dataJson = (JsonObject) remap.get("data");
                    if (dataJson != null) {
                        relists = new ArrayList<>();
                        for (String volumeId : volumeIds) {
                            JsonObject statisticObject = dataJson.getAsJsonObject(volumeId);
                            if (statisticObject != null) {
                                VmfsDataInfo vmfsDataInfo = new VmfsDataInfo();
                                vmfsDataInfo.setVolumeId(volumeId);
                                vmfsDataInfo.setIops(ToolUtils.jsonToInt(statisticObject.get(DataStoreStatisticHistoryServiceImpl.COUNTER_NAME_IOPS), null));
                                vmfsDataInfo.setBandwidth(ToolUtils.jsonToDou(statisticObject.get(DataStoreStatisticHistoryServiceImpl.COUNTER_NAME_BANDWIDTH), null));
                                vmfsDataInfo.setReadResponseTime(ToolUtils.jsonToInt(statisticObject.get(DataStoreStatisticHistoryServiceImpl.COUNTER_NAME_READPESPONSETIME), null));
                                vmfsDataInfo.setWriteResponseTime(ToolUtils.jsonToInt(statisticObject.get(DataStoreStatisticHistoryServiceImpl.COUNTER_NAME_WRITERESPONSETIME), null));
                                relists.add(vmfsDataInfo);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("list vmfs performance error:", e);
            throw e;
        }
        LOG.info("listVmfsPerformance relists===" + (relists == null ? "null" : (relists.size() + "==" + gson.toJson(relists))));
        return relists;
    }

    @Override
    public void createVmfs(Map<String, Object> params) throws Exception {
        if (params != null) {
            //param str host: 主机  param str cluster: 集群
            String objhostid = "";
            //判断主机或主机组在DME中是否存在
            //如果主机或主机不存在就创建并得到主机或主机组ID
            objhostid = checkOrcreateToHostorHostGroup(params);
            LOG.info("objhostid====" + objhostid);
            if (!StringUtils.isEmpty(objhostid)) {
                //创建DME卷
                //判断服务等级是否存在  service_level_id
                String taskId = "";
                if (params.get("service_level_id") != null) {
                    taskId = createVmfsByServiceLevel(params, objhostid);
                } else {  //非服务化的创建
                    taskId = createVmfsByUNServiceLevel(params, objhostid);

                }
                LOG.info("taskId====" + taskId);
                if (!StringUtils.isEmpty(taskId)) {
                    //查询看创建任务是否完成。
                    List<String> taskIds = new ArrayList<>();
                    taskIds.add(taskId);
                    boolean createFlag = taskService.checkTaskStatus(taskIds);
                    if (createFlag) { //DME创建完成
                        //创建vmware中的vmfs存储。
                        String dataStoreStr = createVmfsOnVmware(params);
                        LOG.info("Vmfs dataStoreStr==" + dataStoreStr);
                        if (!StringUtils.isEmpty(dataStoreStr)) {
                            Map<String, Object> dataStoreMap = gson.fromJson(dataStoreStr, new TypeToken<Map<String, Object>>() {
                            }.getType());
                            if (dataStoreMap != null) {
                                //通过卷的名称去DME查询卷的信息
                                Map<String, Object> volumeMap = getVolumeByName(ToolUtils.getStr(params.get("volumeName")));
                                //将DME卷与vmfs的关系保存数据库
                                saveDmeVmwareRalation(volumeMap, dataStoreMap);
                                //关联服务等级
                                if (params.get("service_level_id") != null) {
                                    String serviceLevelName = ToolUtils.getStr(params.get("service_level_name"));
                                    String attachTagStr = vcsdkUtils.attachTag(ToolUtils.getStr(dataStoreMap.get("type")), ToolUtils.getStr(dataStoreMap.get("id")), serviceLevelName);
                                    LOG.info("Vmfs attachTagStr==" + attachTagStr);
                                }
                            }
                        }
                    } else {
                        throw new Exception("DME create vmfs volume error(task status)!");
                    }
                } else {
                    throw new Exception("DME create vmfs volume error(task is null)!");
                }
            } else {
                throw new Exception("DME find or create host error!");
            }
        } else {
            throw new Exception("Parameter exception:" + params);
        }
    }

    //在vmware创建存储
    private String createVmfsOnVmware(Map<String, Object> params) {
        String dataStoreStr = "";
        try {
            if (params != null) {
                //创建vmware中的vmfs存储。 cluster host
                String hostName = ToolUtils.getStr(params.get("host"));
                String hostObjectId = ToolUtils.getStr(params.get("hostId"));
                String clusterName = ToolUtils.getStr(params.get("cluster"));
                String clusterObjectId = ToolUtils.getStr(params.get("clusterId"));
                String datastoreName = ToolUtils.getStr(params.get("name"));
                int vmfsMajorVersion = ToolUtils.getInt(params.get("version"));
                int blockSize = ToolUtils.getInt(params.get("blockSize"));
                int unmapGranularity = ToolUtils.getInt(params.get("spaceReclamationGranularity"));
                String unmapPriority = ToolUtils.getStr(params.get("spaceReclamationPriority"));
                int capacity = ToolUtils.getInt(params.get("capacity"));
                //从主机或集群中找出最接近capacity的LUN
                Map<String, Object> hsdmap = null;
                if (params != null && params.get("host") != null) {
                    hsdmap = vcsdkUtils.getLunsOnHost(hostObjectId, capacity);
                } else if (params != null && params.get("cluster") != null) {
                    hsdmap = vcsdkUtils.getLunsOnCluster(clusterObjectId, capacity);
                }

                //创建
                dataStoreStr = vcsdkUtils.createVmfsDataStore(hsdmap, capacity, datastoreName,
                        vmfsMajorVersion, blockSize, unmapGranularity, unmapPriority);

                //如果创建成功，在集群中的其他主机上扫描并挂载datastore
                vcsdkUtils.mountVmfsOnCluster(dataStoreStr, clusterObjectId, hostObjectId);
            }
        } catch (Exception e) {
            LOG.error("createVmfsByServiceLevel error:", e);
        }
        return dataStoreStr;
    }

    //通过服务等级创建卷，返回任务ID
    private String createVmfsByServiceLevel(Map<String, Object> params, String objhostid) {
        String taskId = "";
        try {
            if (params != null && params.get("service_level_id") != null) {
                Map<String, Object> requestbody = null;
                //判断该集群下有多少主机，如果主机在DME不存在就需要创建
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
                if (!StringUtils.isEmpty(params.get("host"))) {
                    mapping.put("host_id", objhostid);
                } else {
                    mapping.put("hostgroup_id", objhostid);
                }
                requestbody.put("mapping", mapping);
                LOG.info("ByServiceLevel requestbody==" + gson.toJson(requestbody));

                LOG.info("create ByServiceLevel vmfs_url===" + CREATE_VOLUME_URL);
                ResponseEntity responseEntity = dmeAccessService.access(CREATE_VOLUME_URL, HttpMethod.POST, requestbody.toString());

                LOG.info("create ByServiceLevel vmfs responseEntity==" + responseEntity.toString());
                if (responseEntity.getStatusCodeValue() == 202) {
                    JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                    if (jsonObject != null && jsonObject.get("task_id") != null) {
                        taskId = ToolUtils.jsonToStr(jsonObject.get("task_id"));
                        LOG.info("createVmfsByServiceLevel task_id====" + taskId);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("createVmfsByServiceLevel error:", e);
        }
        return taskId;
    }

    //通过非服务化创建卷，返回任务ID
    private String createVmfsByUNServiceLevel(Map<String, Object> params, String objhostid) {
        String taskId = "";
        try {
            if (params != null && params.get("storage_id") != null) {
                Map<String, Object> requestbody = null;
                //判断该集群下有多少主机，如果主机在DME不存在就需要创建
                requestbody = new HashMap<>();
                Map<String, Object> cv = new HashMap<>();
                cv.put("pool_raw_id", ToolUtils.getStr(params.get("pool_raw_id")));
                cv.put("storage_id", ToolUtils.getStr(params.get("storage_id")));

                Map<String, Object> tuning = new HashMap<>();
                tuning.put("alloctype", ToolUtils.getStr(params.get("alloctype")));
                tuning.put("workload_type_id", ToolUtils.getInt(params.get("workload_type_id")));

                Map<String, Object> smartqos = new HashMap<>();
                smartqos.put("control_policy", ToolUtils.getStr(params.get("control_policy")));
                smartqos.put("latency", ToolUtils.getInt(params.get("latency")));
                smartqos.put("maxbandwidth", ToolUtils.getInt(params.get("maxbandwidth")));
                smartqos.put("maxiops", ToolUtils.getInt(params.get("maxiops")));
                smartqos.put("minbandwidth", ToolUtils.getInt(params.get("minbandwidth")));
                smartqos.put("miniops", ToolUtils.getInt(params.get("miniops")));
                smartqos.put("name", ToolUtils.getStr(params.get("qosname")));

                tuning.put("smartqos", smartqos);

                cv.put("tuning", tuning);

                List<Map<String, Object>> volumeSpecs = new ArrayList<>();
                Map<String, Object> vs = new HashMap<>();
                vs.put("name", ToolUtils.getStr(params.get("volumeName")));
                vs.put("capacity", ToolUtils.getInt(params.get("capacity")));
                vs.put("count", ToolUtils.getInt(params.get("count")));
                volumeSpecs.add(vs);

                cv.put("volume_specs", volumeSpecs);

                requestbody.put("customize_volumes", cv);

                Map<String, Object> mapping = new HashMap<>();
                if (!StringUtils.isEmpty(params.get("host"))) {
                    mapping.put("host_id", objhostid);
                } else {
                    mapping.put("hostgroup_id", objhostid);
                }
                requestbody.put("mapping", mapping);
                LOG.info("ByServiceLevel requestbody==" + gson.toJson(requestbody));


                LOG.info("create UNServiceLevel vmfs_url===" + CREATE_VOLUME_UNSERVICE_URL);
                ResponseEntity responseEntity = dmeAccessService.access(CREATE_VOLUME_UNSERVICE_URL, HttpMethod.POST, requestbody.toString());
                LOG.info("create UNServiceLevel vmfs responseEntity==" + responseEntity.toString());
                if (responseEntity.getStatusCodeValue() == 202) {
                    JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                    if (jsonObject != null && jsonObject.get("task_id") != null) {
                        taskId = ToolUtils.jsonToStr(jsonObject.get("task_id"));
                        LOG.info("createVmfsUNServiceLevel task_id====" + taskId);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("createVmfsUNServiceLevel error:", e);
        }
        return taskId;
    }

    //判断主机在DME中是否存在 如果主机不存在就创建并得到主机ID
    private String checkOrcreateToHost(String hostIp) throws Exception {
        String objId = "";
        try {
            //param str host: 主机  param str cluster: 集群
            //判断主机或主机组在DME中是否存在
            if (!StringUtils.isEmpty(hostIp)) {
                List<Map<String, Object>> hostlist = dmeAccessService.getDmeHosts(hostIp);
                if (hostlist != null && hostlist.size() > 0) {
                    for (Map<String, Object> hostmap : hostlist) {
                        if (hostmap != null && hostmap.get("ip") != null) {
                            if (hostIp.equals(hostmap.get("ip").toString())) {
                                objId = hostmap.get("id").toString();
                                break;
                            }
                        }
                    }
                }
                LOG.info("check host id==" + objId);
                //如果主机或主机不存在就创建并得到主机或主机组ID
                if (StringUtils.isEmpty(objId)) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("host", hostIp);
                    Map<String, Object> hostmap = dmeAccessService.createHost(params);
                    if (hostmap != null && hostmap.get("id") != null) {
                        objId = hostmap.get("id").toString();
                    }
                    LOG.info("create host id==" + objId);
                }
            }
        } catch (Exception e) {
            LOG.error("checkOrcreateToHost error:", e);
            throw e;
        }
        return objId;
    }

    //如果主机组不存在就创建并得到主机组ID 创建前要检查集群下的所有主机是否在DME中存在
    private String checkOrcreateToHostGroup(String clusterName, String clusterObjectId) throws Exception {
        String objId = "";
        try {
            //param str host: 主机  param str cluster: 集群
            //如果主机或主机不存在就创建并得到主机或主机组ID 如果主机组不存在就需要创建,创建前要检查集群下的所有主机是否在DME中存在
            if (!StringUtils.isEmpty(clusterName)) {
                //检查集群对应的主机组在DME中是否存在
                List<Map<String, Object>> hostgrouplist = dmeAccessService.getDmeHostGroups(clusterName);
                if (hostgrouplist != null && hostgrouplist.size() > 0) {
                    for (Map<String, Object> hostgroupmap : hostgrouplist) {
                        if (hostgroupmap != null && hostgroupmap.get("name") != null) {
                            if (clusterName.equals(hostgroupmap.get("name").toString())) {
                                objId = ToolUtils.getStr(hostgroupmap.get("id"));
                                break;
                            }
                        }
                    }
                }
                LOG.info("check host group id==" + objId);
                //如果主机组不存在就需要创建,创建前要检查集群下的所有主机是否在DME中存在
                if (StringUtils.isEmpty(objId)) {
                    //取得集群下的所有主机
                    String vmwarehosts = vcsdkUtils.getHostsOnCluster(clusterObjectId);
                    LOG.info("vmwarehosts==" + vmwarehosts);
                    if (!StringUtils.isEmpty(vmwarehosts)) {
                        List<Map<String, String>> vmwarehostlists = gson.fromJson(vmwarehosts, new TypeToken<List<Map<String, String>>>() {
                        }.getType());
                        if (vmwarehostlists != null && vmwarehostlists.size() > 0) {
                            //分别检查每一个主机是否存在，如果不存在就创建
                            List<String> hostlists = new ArrayList<>();
                            for (Map<String, String> hostmap : vmwarehostlists) {
                                LOG.info("checkOrcreateToHost====" + hostmap.get("hostName"));
                                String tmpHostId = checkOrcreateToHost(ToolUtils.getStr(hostmap.get("hostName")));
                                if (!StringUtils.isEmpty(tmpHostId)) {
                                    hostlists.add(tmpHostId);
                                }
                            }
                            LOG.info("hostlists====" + hostlists);
                            //在DME中创建主机组
                            if (hostlists.size() > 0) {
                                Map<String, Object> params = new HashMap<>();
                                params.put("cluster", clusterName);
                                params.put("hostids", hostlists);
                                Map<String, Object> hostmap = dmeAccessService.createHostGroup(params);
                                if (hostmap != null && hostmap.get("id") != null) {
                                    objId = ToolUtils.getStr(hostmap.get("id"));
                                }
                            }
                        }
                    }
                    LOG.info("create host group id==" + objId);
                }
            }
        } catch (Exception e) {
            LOG.error("checkOrcreateToHostGroup error:", e);
            throw e;
        }
        return objId;
    }

    //根据参数选择检查主机或主机组的方法
    private String checkOrcreateToHostorHostGroup(Map<String, Object> params) throws Exception {
        String objId = "";
        try {
            //param str host: 主机  param str cluster: 集群
            if (params != null && params.get("host") != null) {
                objId = checkOrcreateToHost(ToolUtils.getStr(params.get("host")));
            } else if (params != null && params.get("cluster") != null) {
                objId = checkOrcreateToHostGroup(ToolUtils.getStr(params.get("cluster")), ToolUtils.getStr(params.get("clusterId")));
            }
        } catch (Exception e) {
            LOG.error("checkOrcreateToHostorHostGroup error:", e);
            throw e;
        }
        return objId;
    }

    //根据卷名称查询DME卷的信息
    private Map<String, Object> getVolumeByName(String volumeName) {
        Map<String, Object> remap = null;
        String volumeUrlByName = LIST_VOLUME_URL + "?name=" + volumeName;
        try {
            ResponseEntity responseEntity = dmeAccessService.access(volumeUrlByName, HttpMethod.GET, null);
            LOG.info("getVolumeByName responseEntity==" + responseEntity.toString());
            if (responseEntity.getStatusCodeValue() == 200) {
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                //LOG.info("listVmfs jsonObject==" + jsonObject.toString());
                if (jsonObject != null && jsonObject.get("volumes") != null) {
                    JsonArray jsonArray = jsonObject.getAsJsonArray("volumes");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject vjson = jsonArray.get(i).getAsJsonObject();
                        if (vjson != null && vjson.get("name") != null && ToolUtils.jsonToStr(vjson.get("name")).equals(volumeName)) {
                            remap = new HashMap<>();
                            remap.put("volume_id", ToolUtils.jsonToStr(vjson.get("id")));
                            remap.put("volume_name", ToolUtils.jsonToStr(vjson.get("name")));
                            remap.put("volume_wwn", ToolUtils.jsonToStr(vjson.get("volume_wwn")));
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("DME link error url:" + volumeUrlByName + ",error:" + e.getMessage());
        }
        LOG.info("remap===" + (remap == null ? "null" : (remap.size() + "==" + gson.toJson(remap))));
        return remap;
    }

    //保存卷与vmfs的关联关系
    private void saveDmeVmwareRalation(Map<String, Object> volumeMap, Map<String, Object> dataStoreMap) {
        try {
            if (volumeMap == null || volumeMap.get("volume_id") == null) {
                LOG.error("save Dme and Vmware's vmfs Ralation error: volume data is null");
                return;
            }
            if (dataStoreMap == null || dataStoreMap.get("id") == null) {
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
    public void mountVmfs(Map<String, Object> params) throws Exception {
        if (params != null) {
            //param str host: 主机  param str cluster: 集群  dataStoreObjectIds
            String objhostid = "";
            //判断主机或主机组在DME中是否存在
            //如果主机或主机不存在就创建并得到主机或主机组ID
            objhostid = checkOrcreateToHostorHostGroup(params);
            LOG.info("objhostid====" + objhostid);
            if (!StringUtils.isEmpty(objhostid)) {
                //挂载卷
                String taskId = "";
                //通过存储的objectid查询卷id
                if (params.get("dataStoreObjectIds") != null) {
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

                if (params.get("host") != null) {
                    //将卷挂载到主机DME
                    taskId = mountVmfsToHost(params, objhostid);
                } else {
                    //将卷挂载到集群DME
                    taskId = mountVmfsToHostGroup(params, objhostid);
                }
                LOG.info("taskId====" + taskId);
                //查询看创建任务是否完成。
                if (!StringUtils.isEmpty(taskId)) {
                    List<String> taskIds = new ArrayList<>();
                    taskIds.add(taskId);
                    boolean mountFlag = taskService.checkTaskStatus(taskIds);
                    if (mountFlag) { //DME创建完成
                        //调用vCenter在主机上扫描卷和Datastore
                        vcsdkUtils.scanDataStore(ToolUtils.getStr(params.get("clusterId")), ToolUtils.getStr(params.get("hostId")));
                        //如果是需要扫描LUN来挂载，则需要执行下面的方法，dataStoreNames
                        if (params.get("dataStoreNames") != null) {
                            List<String> dataStoreNames = (List<String>) params.get("dataStoreNames");
                            //
                            if (dataStoreNames != null && dataStoreNames.size() > 0) {
                                for (String dataStoreName : dataStoreNames) {
                                    Map<String, Object> dsmap = new HashMap<>();
                                    dsmap.put("name", dataStoreName);

                                    vcsdkUtils.mountVmfsOnCluster(gson.toJson(dsmap), ToolUtils.getStr(params.get("clusterId")), ToolUtils.getStr(params.get("hostId")));
                                }
                            }
                        }
                    } else {
                        throw new Exception("DME mount vmfs volume error(task status)!");
                    }
                } else {
                    throw new Exception("DME mount vmfs volume error(task is null)!");
                }
            } else {
                throw new Exception("DME find or create host error!");
            }
        } else {
            throw new Exception("Parameter exception:" + params);
        }
    }

    //将卷挂载到主机DME
    private String mountVmfsToHost(Map<String, Object> params, String objhostid) {
        String taskId = "";
        try {
            if (params != null && params.get("volumeIds") != null) {
                Map<String, Object> requestbody = null;
                //判断该集群下有多少主机，如果主机在DME不存在就需要创建
                requestbody = new HashMap<>();
                List<String> volumeIds = (List<String>) params.get("volumeIds");

                requestbody.put("volume_ids", volumeIds);
                requestbody.put("host_id", objhostid);

                LOG.info("mountVmfsToHost requestbody==" + gson.toJson(requestbody));

                LOG.info("mountVmfsToHost URL===" + MOUNT_VOLUME_TO_HOST_URL);
                ResponseEntity responseEntity = dmeAccessService.access(MOUNT_VOLUME_TO_HOST_URL, HttpMethod.POST, requestbody.toString());

                LOG.info("mountVmfsToHost vmfs responseEntity==" + responseEntity.toString());
                if (responseEntity.getStatusCodeValue() == 202) {
                    JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                    if (jsonObject != null && jsonObject.get("task_id") != null) {
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

    //将卷挂载到集群DME
    private String mountVmfsToHostGroup(Map<String, Object> params, String objhostid) {
        String taskId = "";
        try {
            if (params != null && params.get("volumeIds") != null) {
                Map<String, Object> requestbody = null;
                //判断该集群下有多少主机，如果主机在DME不存在就需要创建
                requestbody = new HashMap<>();
                List<String> volumeIds = (List<String>) params.get("volumeIds");

                requestbody.put("volume_ids", volumeIds);
                requestbody.put("hostgroup_id", objhostid);

                LOG.info("mountVmfsToHostGroup requestbody==" + gson.toJson(requestbody));

                LOG.info("mountVmfsToHostGroup URL===" + MOUNT_VOLUME_TO_HOSTGROUP_URL);
                ResponseEntity responseEntity = dmeAccessService.access(MOUNT_VOLUME_TO_HOSTGROUP_URL, HttpMethod.POST, requestbody.toString());

                LOG.info("mountVmfsToHostGroup vmfs responseEntity==" + responseEntity.toString());
                if (responseEntity.getStatusCodeValue() == 202) {
                    JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                    if (jsonObject != null && jsonObject.get("task_id") != null) {
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


    /**
     * @Author wangxiangyong
     * @Description /vmfs datastore 卷详情查询
     * @Date 14:46 2020/9/3
     * @Param [storage_objectId]
     * @Return com.dmeplugin.dmestore.model.VmfsDatastoreVolumeDetail
     **/
    @Override
    public List<VmfsDatastoreVolumeDetail> volumeDetail(String storage_objectId) throws Exception {
        List<VmfsDatastoreVolumeDetail> list = new ArrayList<>();
        //根据存储ID获取磁盘ID
        List<String> volumeIds = dmeVmwareRalationDao.getVolumeIdsByStorageId(storage_objectId);
        for (String volumeId : volumeIds) {
            //调用DME接口获取卷详情
            String url = LIST_VOLUME_URL + "/" + volumeId;
            ResponseEntity<String> responseEntity;
            responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() / 100 != 2) {
                LOG.error("查询卷信息失败！错误信息:{}", responseEntity.getBody());
                throw new Exception(responseEntity.getBody());
            }

            String responseBody = responseEntity.getBody();
            JsonObject volume = gson.fromJson(responseBody, JsonObject.class).getAsJsonObject("volume");

            VmfsDatastoreVolumeDetail volumeDetail = new VmfsDatastoreVolumeDetail();
            //basic info
            volumeDetail.setWwn(volume.get("volume_wwn").getAsString());
            volumeDetail.setName(volume.get("name").getAsString());
            volumeDetail.setServiceLevel(volume.get("service_level_name").getAsString());
            //TODO
            volumeDetail.setStorage(volume.get("storage_id").getAsString());
            volumeDetail.setStoragePool(volume.get("pool_raw_id").getAsString());

            JsonObject tuning = volume.getAsJsonObject("tuning");
            //SmartTier
            volumeDetail.setSmartTier(tuning.get("smarttier").getAsString());
            //Tunning
            volumeDetail.setDudeplication(tuning.get("dedupe_enabled").getAsBoolean());
            volumeDetail.setProvisionType(tuning.get("alloctype").getAsString());
            volumeDetail.setCompression(tuning.get("compression_enabled").getAsBoolean());
            //TODO
            volumeDetail.setApplicationType("--");

            JsonObject smartqos = tuning.getAsJsonObject("smartqos");
            //Qos Policy
            if (null != smartqos) {
                volumeDetail.setControlPolicy(smartqos.get("control_policy").getAsString());
                //TODO
                volumeDetail.setTrafficControl("--");
            }

            list.add(volumeDetail);
        }

        return list;
    }

    /**
     * @Author Administrator
     * @Description 扫描vmfs datastore并和卷建立关系
     * @Date 16:49 2020/9/7
     * @Param []
     * @Return boolean
     **/
    @Override
    public boolean scanVmfs() throws Exception {
        String listStr = vcsdkUtils.getAllVmfsDataStoreInfos(ToolUtils.STORE_TYPE_VMFS);
        LOG.info("===list vmfs datastore success====\n{}", listStr);
        if (StringUtils.isEmpty(listStr)) {
            return false;
        }
        //TODO
        JsonArray jsonArray = new JsonParser().parse(listStr).getAsJsonArray();
        List<DmeVmwareRelation> relationList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject vmfsDatastore = jsonArray.get(i).getAsJsonObject();
            String storeType = ToolUtils.STORE_TYPE_VMFS;
            //TODO 暂时认为是从url中获取到wwn信息
            String vmfsDatastoreUrl = vmfsDatastore.get("url").getAsString();
            String vmfsDatastoreId = vmfsDatastore.get("objectid").getAsString();
            String vmfsDatastoreName = vmfsDatastore.get("name").getAsString();
            //拆分wwn TODO
            String wwn = vmfsDatastoreUrl.split("volumes/")[1];
            //根据wwn从DME中查询卷信息
            String volumeUrlByWwn = LIST_VOLUME_URL + "?volume_wwn=" + wwn;
            ResponseEntity<String> responseEntity = dmeAccessService.access(volumeUrlByWwn, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() / 100 != 2) {
                LOG.info(" Query DME volume failed! errorMsg:{}", responseEntity.toString());
                continue;
            }
            JsonObject jsonObject = gson.fromJson(responseEntity.getBody(), JsonObject.class);
            JsonObject volume = jsonObject.getAsJsonArray("volumes").get(0).getAsJsonObject();
            String volumeId = volume.get("id").getAsString();
            String volumeName = volume.get("name").getAsString();
            String volumeWwn = volume.get("volume_wwn").getAsString();

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

        if (relationList.size() > 0) {
            //数据库处理
            return dmeVmwareRelationDBProcess(relationList, ToolUtils.STORE_TYPE_VMFS);
        }
        return true;
    }

    private boolean dmeVmwareRelationDBProcess(List<DmeVmwareRelation> relationList, String storeType) throws Exception {
        //本地全量查询
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

        //更新
        if (!upList.isEmpty()) {
            dmeVmwareRalationDao.update(upList);
        }

        //新增
        if (!newList.isEmpty()) {
            dmeVmwareRalationDao.save(newList);
        }
        //删除
        if (!localWwns.isEmpty()) {
            dmeVmwareRalationDao.deleteByWwn(localWwns);
        }
        return true;
    }


    /**
     * @param params volume_id host_id hostGroup_id由调用处传递过来，而不是在此处自己查询？
     * @throws Exception
     */
    @Override
    public void unmountVmfs(Map<String, Object> params) throws Exception {
        List<String> taskIds = new ArrayList<>();
        //unmount前的效验 wether attached is ture
        String volumeId = params.get("{volume_id").toString();
        ResponseEntity responseVmfs = queryVmfsById(volumeId);
        if (null != responseVmfs) {
            Object body = responseVmfs.getBody();
            JsonObject bodyJson = new JsonParser().parse(body.toString()).getAsJsonObject();
            JsonObject volumJson = bodyJson.get("volume").getAsJsonObject();
            boolean attached = volumJson.get("attached").getAsBoolean();
            if (attached) {
                JsonArray attachments = volumJson.getAsJsonArray("attachments");
                for (JsonElement attachment : attachments) {
                    //String volume_id = attachment.getAsJsonObject().get("volume_id").getAsString();//attachment中的volume_id和param中的volume_id应该是一致的
                    String host_id = attachment.getAsJsonObject().get("host_id").getAsString();
                    String hostgroup_id = attachment.getAsJsonObject().get("attached_host_group").getAsString();
                    List<String> volume_ids = Arrays.asList(volumeId);

                    params.put("volume_ids", volume_ids);
                    if (!StringUtils.isEmpty(host_id)) {
                        params.put("host_id", host_id);
                    }
                    if (!StringUtils.isEmpty(hostgroup_id)) {
                        params.put("hostgroup_id", hostgroup_id);
                    }
                    break;// break? volume 与host 及hostgroup的关系应该是1:1
                }
            }
            //按需分别卸载host 和 hostgroup
            boolean unmappingHostFlag = true;
            boolean unmappingHostgroupFlag = true;
            if (null != params.get("hostgroup_id")) {
                ResponseEntity responseHostGroupUnmaaping = hostGroupUnmapping(params);
                if (202 != responseHostGroupUnmaaping.getStatusCodeValue()) {
                    unmappingHostgroupFlag = false;
                } else {
                    String taskId = getTaskId(responseHostGroupUnmaaping);
                    taskIds.add(taskId);
                }
            }
            if (null != params.get("host_id")) {
                ResponseEntity responseHostUnmapping = hostUnmapping(params);
                if (202 != responseHostUnmapping.getStatusCodeValue()) {
                    unmappingHostFlag = false;
                } else {
                    String taskId = getTaskId(responseHostUnmapping);
                    taskIds.add(taskId);
                }
            }
            //判断是否卸载成功 ，卸载失败 抛出错误提示
            if (!(unmappingHostFlag && unmappingHostgroupFlag)) {
                throw new Exception("unmount volume precondition unmount host and hostGroup error(response code)!");
            }
        }

        // 获取卸载的任务完成后的状态 (默认超时时间10分钟)
        boolean unmountFlag = taskService.checkTaskStatus(taskIds);

        if (!unmountFlag) {
            throw new Exception("unmount volume precondition unmount host and hostGroup error(task status)!");
        }

        //vcenter侧卸载 调用HostStorageSystemMo的rescanVmfs()方法
        String hostId = params.get("host_id").toString();
        Map<String, Object> hostInfoMap = dmeAccessService.getDmeHost(hostId);
        String hostIp = hostInfoMap.get("ip").toString();
        //String hostName = hostInfoMap.get("name").toString(); //使用hostName而不是hostIp?
        vcsdkUtils.hostRescanVmfs(hostIp);
    }

    @Override
    public void deleteVmfs(Map<String, Object> params) throws Exception {
        //先调卸载的接口
        try {
            unmountVmfs(params);
        } catch (Exception e) {
            LOG.error("delete volume precondition unmapping host and hostGroup error!");
            throw new Exception("delete volume precondition unmapping host and hostGroup error!");
        }
        //删除vmfs

        String taskId;
        Object volume_ids = params.get("volume_ids");
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("volume_ids", volume_ids);
        ResponseEntity responseEntity = dmeAccessService.access(VOLUME_DELETE, HttpMethod.POST, requestbody.toString());
        if (202 != responseEntity.getStatusCodeValue()) {
            throw new Exception("delete volume error!");
        } else {
            taskId = getTaskId(responseEntity);
        }

        boolean dmeDeleteFlag = taskService.checkTaskStatus(Arrays.asList(taskId));

        if (!dmeDeleteFlag) {
            throw new Exception("delete volume precondition unmount host and hostGroup error(task status)!");
        }
        //vcenter侧删除
        String dataStoreName = params.get("dataStoreName").toString();
        boolean deleteFlag = vcsdkUtils.deleteVmfsDataStore(dataStoreName);
        if (deleteFlag) {
            LOG.info("delete vmfs:{} success!", dataStoreName);
        }


    }

    //查询指定vmfs
    private ResponseEntity queryVmfsById(String volume_id) throws Exception {
        String url = LIST_VOLUME_URL + "/" + volume_id;
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() / 100 != 2) {
                LOG.error("查询指定卷信息失败!错误信息:{}", responseEntity.getBody());
                return null;
            }
        } catch (Exception ex) {
            LOG.error("查询卷信息异常", ex);
            return null;
        }
        return responseEntity;
    }


    private ResponseEntity hostUnmapping(Map<String, Object> params) throws Exception {
        String hostId = params.get("hostId").toString();
        Object volumeIds = params.get("volumeIds");

        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("host_id", hostId);
        requestbody.put("volume_ids", volumeIds);
        ResponseEntity responseEntity = dmeAccessService.access(HOST_UNMAPAPING, HttpMethod.POST, requestbody.toString());
        return responseEntity;
    }

    private ResponseEntity hostGroupUnmapping(Map<String, Object> params) throws Exception {
        String hostGroupId = params.get("hostGroupId").toString();
        Object volumeIds = params.get("volumeIds");
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("host_id", hostGroupId);
        requestbody.put("volume_ids", volumeIds);
        ResponseEntity responseEntity = dmeAccessService.access(HOSTGROUP_UNMAPPING, HttpMethod.POST, requestbody.toString());
        return responseEntity;

    }

    //整理关系表数据
    private Map<String, DmeVmwareRelation> getDvrMap(List<DmeVmwareRelation> dvrlist) {
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

    //整理存储信息
    private Map<String, String> getStorNameMap(Map<String, Object> storagemap) {
        Map<String, String> remap = null;
        try {
            if (storagemap != null && storagemap.get("data") != null) {
                List<Storage> list = (List<Storage>) storagemap.get("data");
                if (list != null && list.size() > 0) {
                    remap = new HashMap<>();
                    for (Storage sr : list) {
                        remap.put(sr.getId(), sr.getName());
                    }
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
        Object hostGroupBody = responseEntity.getBody();
        JsonObject hostJson = new JsonParser().parse(hostGroupBody.toString()).getAsJsonObject();
        String taskId = hostJson.get("task_id").getAsString();
        return taskId;
    }

    @Override
    public List<Map<String, Object>> getHostsByStorageId(String storageId) throws Exception {
        List<Map<String, Object>> hostMapList = new ArrayList<>();
        //通过vmware storageId 查询DME存储的关联关系 提取DME的存储ID
        List<String> dmeStorageIds = getDmeStorageIdsByStorageId(storageId);

        //通过dmeStorageIds 查主机ID
        Set<String> hostIds = new HashSet<>();
       if(dmeStorageIds.size() >0){
           List<Volume> volumes = getVolumesByDmeStorageIds(new ArrayList<>(dmeStorageIds));
           if(null != volumes && volumes.size() >0){
               for(Volume volume: volumes){
                   List<String> hostIdList = volume.getHostIds();
                   if(null != hostIdList && hostIdList.size()>0){
                       hostIds.addAll(hostIdList);
                   }
               }
           }
       }

        // 通过主机Id获取主机的信息
        if(hostIds.size() > 0){
            for(String hostId : hostIds){
                Map<String,Object> hostMap = dmeAccessService.getDmeHost(hostId);
                hostMapList.add(hostMap);
            }
        }
        return hostMapList;
    }

    @Override
    public List<Map<String, Object>> getHostGroupsByStorageId(String storageId) throws Exception {
        List<Map<String, Object>> hostMapList = new ArrayList<>();
        //通过vmware storageId 查询DME存储的关联关系 提取DME的存储ID
        List<String> dmeStorageIds = getDmeStorageIdsByStorageId(storageId);

        //通过dmeStorageIds 查主机组ID
        Set<String> hostgroupIds = new HashSet<>();
        if(dmeStorageIds.size() >0){
            List<Volume> volumes = getVolumesByDmeStorageIds(new ArrayList<>(dmeStorageIds));
            if(null != volumes && volumes.size() >0){
                for(Volume volume: volumes){
                    List<String> hostGroupIdList = volume.getHostGroupIds();
                    if(null != hostGroupIdList && hostGroupIdList.size()>0){
                        hostgroupIds.addAll(hostGroupIdList);
                    }
                }
            }
        }

        // 通过主机组Id获取主机组的信息
        if(hostgroupIds.size() > 0){
            for(String hostGroupId : hostgroupIds){
                Map<String,Object> hostGroupMap = dmeAccessService.getDmeHostGroup(hostGroupId);
                hostMapList.add(hostGroupMap);
            }
        }
        return hostMapList;
    }

    //通过v魔法师DATa StorageId查询关联的dmeStorageIds集合(即卷id集合)
    private List<String> getDmeStorageIdsByStorageId(String storageId) throws Exception{
        Set<String> dmeStorageIds = new HashSet<>();
        List<DmeVmwareRelation> relations = dmeVmwareRalationDao.getDmeVmwareRelationsByStorageIds(Arrays.asList(storageId));
        if (null != relations && relations.size() > 0) {
            for (DmeVmwareRelation relation : relations) {
                String dmeStorageId = relation.getVolumeId();
                dmeStorageIds.add(dmeStorageId);
            }
        }
        return new ArrayList<>(dmeStorageIds);
    }


    //通过dmeStorageIds查询卷信息
    private List<Volume> getVolumesByDmeStorageIds(List<String> dmeStorageIds) throws Exception{
        List<Volume> volumes = new ArrayList<>();
        for (String id : dmeStorageIds) {
            Map<String, Object> volumeResp = dmeStorageService.getVolume(id);
            if ("200".equals(volumeResp.get("code").toString())) {
                Volume volume = (Volume)volumeResp.get("data");
                volumes.add(volume);
            }
        }
        return volumes;
    }


}
