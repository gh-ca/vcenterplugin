package com.huawei.dmestore.services;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.dao.DmeVmwareRalationDao;
import com.huawei.dmestore.entity.DmeVmwareRelation;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.VcenterException;
import com.huawei.dmestore.exception.VcenterRuntimeException;
import com.huawei.dmestore.model.*;
import com.huawei.dmestore.utils.ToolUtils;
import com.huawei.dmestore.utils.VCSDKUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import sun.rmi.runtime.Log;

/**
 * NfsOperationServiceImpl
 *
 * @author lianqiang
 * @since 2020-09-15
 **/
public class NfsOperationServiceImpl implements NfsOperationService {
    private static final Logger LOG = LoggerFactory.getLogger(NfsOperationServiceImpl.class);

    private static final String RESULT_FAIL = "failed";

    private static final String RESULT_SUCCESS = "success";

    private static final String CODE_403 = "403";

    private static final String CODE_503 = "503";

    private static final String CODE_400 = "400";

    private static final String FILESYSTEM_SPECS = "filesystem_specs";

    private static final String NAME_FIELD = "name";

    private static final String STORAGE_ID = "storage_id";

    private static final String TUNING = "tuning";

    private static final String QOS_POLICY = "qos_policy";

    private static final String CAPACITY_AUTONEGOTIATION = "capacity_autonegotiation";

    private static final String TASK_ID = "task_id";

    private static final String NFS_NAME = "nfsName";

    private static final String CAPACITY = "capacity";

    private static final String DATA = "data";

    private static final String FILE_SYSTEM_ID = "{file_system_id}";

    private static final String THIN_FIELD = "thin";

    private static final String OPERATOR = "operator";

    private static final String LOGOP = "logOp";

    private static final String EQUAL_FIELD = "equal";

    private static final String VALUE_FIELD = "value";

    private static final String SIMPLE = "simple";

    private static final String AND_FIELD = "and";

    private static final String NFS_SHARE_ID_STR = "{nfs_share_id}";

    private static final String NFS_SHARE_ID = "nfs_share_id";

    private final long longTaskTimeOut = 30 * 60 * 1000;

    private static final int DIGIT_100 = 100;

    private static final int DIGIT_2 = 2;

    private static final int DIGIT_16 = 16;

    private DmeAccessService dmeAccessService;

    private TaskService taskService;

    private DmeStorageService dmeStorageService;

    private Gson gson = new Gson();

    private VCSDKUtils vcsdkUtils;

    private DmeVmwareRalationDao dmeVmwareRalationDao;

    public DmeVmwareRalationDao getDmeVmwareRalationDao() {
        return dmeVmwareRalationDao;
    }

    public void setDmeVmwareRalationDao(DmeVmwareRalationDao dmeVmwareRalationDao) {
        this.dmeVmwareRalationDao = dmeVmwareRalationDao;
    }

    public DmeAccessService getDmeAccessService() {
        return dmeAccessService;
    }

    public void setDmeAccessService(DmeAccessService dmeAccessService) {
        this.dmeAccessService = dmeAccessService;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public DmeStorageService getDmeStorageService() {
        return dmeStorageService;
    }

    public void setDmeStorageService(DmeStorageService dmeStorageService) {
        this.dmeStorageService = dmeStorageService;
    }

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
    }

    @Override
    public void createNfsDatastore(Map<String, Object> params) throws DmeException {
        LOG.info("createNfsDatastore params={}", gson.toJson(params));
        if (params == null || params.size() == 0) {
            throw new DmeException(CODE_403, "params error , please check your params !");
        }
        String fsId = "";
        String shareId = "";
        String shareClientName = "";
        String storageId = "";
        try {
            // 创建fs
            Map<String, Object> fsMap = new HashMap<>();
            Map<String, Object> nfsShareMap = new HashMap<>();
            Object filesystemSpecs = params.get(FILESYSTEM_SPECS);
            if (StringUtils.isEmpty(filesystemSpecs)) {
                throw new DmeException(CODE_403, "filesystem_specs is null");
            }

            String fsName = "";
            JsonArray jsonArray1 = gson.fromJson(gson.toJson(filesystemSpecs), JsonArray.class);
            for (JsonElement jsonElement : jsonArray1) {
                JsonObject element = jsonElement.getAsJsonObject();
                fsName = ToolUtils.jsonToStr(element.get(NAME_FIELD));
            }
            fsMap.put(FILESYSTEM_SPECS, gson.toJson(filesystemSpecs));
            String poolRawId = (String) params.get("pool_raw_id");
            storageId = (String) params.get(STORAGE_ID);
            if (StringUtils.isEmpty(poolRawId) || StringUtils.isEmpty(storageId)) {
                throw new DmeException(CODE_403, "pool_raw_id or storage_id is null!");
            }
            fsMap.put("pool_raw_id", poolRawId);
            fsMap.put(STORAGE_ID, storageId);
            Object tuning = params.get(TUNING);
            if (!StringUtils.isEmpty(tuning)) {
                Map<String, Object> tuningMap = gson.fromJson(tuning.toString(), Map.class);
                Object qoPolicy = params.get(QOS_POLICY);
                if (qoPolicy != null ) {
                    tuningMap.put(QOS_POLICY, qoPolicy);
                }
                fsMap.put(TUNING, tuningMap);
            }
            Object capacityAutonegotiation = params.get(CAPACITY_AUTONEGOTIATION);
            if (!StringUtils.isEmpty(capacityAutonegotiation)) {
                fsMap.put(CAPACITY_AUTONEGOTIATION, capacityAutonegotiation);
            }
            List<Map<String, String>> mounts = new ArrayList<>();
            Map<String, String> mount = new HashMap<>();
            List<Object> reqNfsShareClientArrayAdditions = new ArrayList<>();
            Object nfsShareClientAddition = params.get("nfs_share_client_addition");
            List<Map<String, Object>> nfsShareClientArrayAddition = (List<Map<String, Object>>) nfsShareClientAddition;
            String shareName = "";
            Map<String, Object> createNfsShareParams = new HashMap<>();
            if (nfsShareClientAddition != null) {
                Map<String, Object> reqNfsShareClientArrayAddition = new HashMap<>();
                for (int index = 0; index < nfsShareClientArrayAddition.size(); index++) {
                    Map<String, Object> shareClientHostMap = nfsShareClientArrayAddition.get(index);
                    if (shareClientHostMap != null && shareClientHostMap.size() > 0
                            && shareClientHostMap.get("objectId") != null) {
                        // 默认值
                        reqNfsShareClientArrayAddition.put(NAME_FIELD, shareClientHostMap.get(NAME_FIELD));
                        shareClientName = (String) shareClientHostMap.get(NAME_FIELD);
                        reqNfsShareClientArrayAddition.put("permission", params.get("accessModeDme"));
                        reqNfsShareClientArrayAddition.put("write_mode", "synchronization");
                        reqNfsShareClientArrayAddition.put("permission_constraint", "no_all_squash");
                        reqNfsShareClientArrayAddition.put("root_permission_constraint", "no_root_squash");
                        reqNfsShareClientArrayAddition.put("source_port_verification", "insecure");
                        mount.put((String) shareClientHostMap.get("objectId"),
                                (String) shareClientHostMap.get(NAME_FIELD));
                    }
                    reqNfsShareClientArrayAdditions.add(reqNfsShareClientArrayAddition);
                }
                mounts.add(mount);
                Object createNfsShareParam = params.get("create_nfs_share_param");
                if (!StringUtils.isEmpty(createNfsShareParam)) {
                    createNfsShareParams = gson.fromJson(gson.toJson(createNfsShareParam), Map.class);
                    createNfsShareParams.put("nfs_share_client_addition", reqNfsShareClientArrayAdditions);
                    shareName = (String) createNfsShareParams.get(NAME_FIELD);
                    Object showSnapshotEnable = params.get("show_snapshot_enable");
                    if (!StringUtils.isEmpty(showSnapshotEnable)) {
                        createNfsShareParams.put("show_snapshot_enable", gson.toJson(showSnapshotEnable));
                    }
                    nfsShareMap.put("create_nfs_share_param", createNfsShareParams);
                }
            }
            String storagePoolId = (String) params.get("storage_pool_id");
            String taskId = createFileSystem(fsMap, storagePoolId);
            if (StringUtils.isEmpty(taskId)) {
                throw new DmeException(CODE_403, "dme create FileSystem failed, task id is null");
            }
            List<String> taskIds = new ArrayList<>();
            taskIds.add(taskId);
            if (taskService.checkTaskStatus(taskIds)) {
                fsId = getFsIdByName(fsName);
            } else {
                // 查询任务详细信息，获取报错信息
                TaskDetailInfo taskDetailInfo = taskService.queryTaskById(taskId);
                throw new DmeException(CODE_403, "create FileSystem fail!" + taskDetailInfo.getDetail());
            }
            if (!"".equals(fsId)) {
                createNfsShareParams.put("fs_id", fsId);
                LOG.info("DME create nfs share request params:{}", gson.toJson(nfsShareMap));
                if (createNfsShare(nfsShareMap)) {
                    // 查询shareId
                    if (StringUtils.isEmpty(shareName)) {
                        shareName = "/"+fsName;
                    }
                    shareId = getShareIdByName(shareName, fsName);
                }
            }
            String serverHost = "";
            String logicPortName = "";
            String currentPortId = String.valueOf(params.get("current_port_id"));
            Map<String, String> logicMap = getMgmtByStorageId(storageId, currentPortId);
            if (logicMap != null && logicMap.size() != 0) {
                serverHost = logicMap.get("mgmt");
                logicPortName = logicMap.get("logicPortName");
            }
            String exportPath = (String) params.get("exportPath");
            String type = (String) params.get("type");
            String securityType = ToolUtils.getStr(params.get("securityType"), "");
            String accessMode = (String) params.get("accessMode");

            if (StringUtils.isEmpty(serverHost) || StringUtils.isEmpty(exportPath) || StringUtils.isEmpty(accessMode)
                    || mounts.size() == 0) {
                throw new DmeException(CODE_403, "params error , please check your params !");
            }
            String nfsName = (String) params.get(NFS_NAME);
            String result = vcsdkUtils.createNfsDatastore(serverHost, exportPath, nfsName, accessMode, mounts, type,
                    securityType);
            if (RESULT_FAIL.equals(result)) {
                throw new VcenterRuntimeException(CODE_403, "create nfs datastore error!");
            }
            // 判断存储类型
            String storageModel = getStorageModel(ToolUtils.getStr(params.get("storage_id")));
            //v6设备不支持设置共享名称，会将共享名称和fs名称一致化
            if (StringUtils.isEmpty(shareName)) {
                shareName = "/"+fsName;
            }
            saveNfsInfoToDmeVmwareRelation(result, currentPortId, logicPortName, fsId, shareName, shareId, fsName,
                    storageModel, storageId);
            LOG.info("create nfs save relation success!nfsName={}", nfsName);
        } catch (Exception e) {
            //创建失败，解除共享客户端并删除共享
            if (!StringUtils.isEmpty(shareId) && !StringUtils.isEmpty(shareClientName)) {
                removeMappingHostOnShare(shareId, getOrientedShare(shareId), shareClientName);
                deleteShare(shareId);
            }
            //创建失败 删除对应fs
            if (!StringUtils.isEmpty(fsId)) {
                deleteFiLeSystem(fsId);
            }
            LOG.error("create nfs datastore error!", e);
            throw new DmeException(CODE_403, e.getMessage());
        }
    }

    @Override
    public void updateNfsDatastore(Map<String, Object> params) throws DmeException {
        if (params == null || params.size() == 0) {
            throw new DmeException(CODE_403, "params error , please check it!");
        }
        try {
            String dataStoreObjectId = (String) params.get("dataStoreObjectId");
            String nfsName = (String) params.get(NFS_NAME);
            if (StringUtils.isEmpty(dataStoreObjectId) || StringUtils.isEmpty(nfsName)) {
                LOG.error("params error , please check it! dataStoreObjectId={}", dataStoreObjectId);
                throw new DmeException(CODE_403, "params dataStoreObjectId is null, please check it!");
            }
            if (StringUtils.isEmpty(nfsName)){
                LOG.error("params error , please check it! nfsName={}",nfsName);
                throw new DmeException(CODE_403, "The name you entered is empty, please re-enter！");
            }
            // 获取表中修改对象修改前字段值
            DmeVmwareRelation dmeVmwareRelation =
                    dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dataStoreObjectId);
            dmeVmwareRelation.setStoreName(nfsName);

            Map<String, Object> fsReqBody = new HashMap<>();
            Object tuning = params.get(TUNING);
            String name = (String) params.get(NAME_FIELD);
            if (!StringUtils.isEmpty(name)) {
                fsReqBody.put(NAME_FIELD, name);
                dmeVmwareRelation.setFsName(name);
            }
            if (tuning != null) {
                Map<String, Object> tuningMap = gson.fromJson(tuning.toString(), Map.class);
                Object qosPolicy = params.get(QOS_POLICY);
                if (qosPolicy != null) {
                    tuningMap.put(QOS_POLICY, qosPolicy);
                }
                fsReqBody.put(TUNING, tuningMap);
                fsReqBody.put("capacity_threshold", 90);
                fsReqBody.put("capacity", params.get("capacity"));
            }
            String fileSystemId = (String) params.get("file_system_id");
            Object capacityAutonegotiation = params.get(CAPACITY_AUTONEGOTIATION);
            if (capacityAutonegotiation != null) {
                fsReqBody.put(CAPACITY_AUTONEGOTIATION, capacityAutonegotiation);
            }

            if (!StringUtils.isEmpty(fileSystemId)) {
                if (!updateFileSystem(fsReqBody, fileSystemId)) {
                    throw new DmeException(CODE_400, "update file system failed");
                }
            }
            String result = vcsdkUtils.renameDataStore(nfsName, dataStoreObjectId);
            if (RESULT_SUCCESS.equals(result)) {
                LOG.info("rename NFS datastore success!");
            }

            //重新查询fs share更新关系表share_name
            String shareId = params.get(NFS_SHARE_ID) == null? null: String.valueOf(params.get(NFS_SHARE_ID));
            String newShareName = getShareNameByShareId(shareId);
            if(!StringUtils.isEmpty(newShareName)){
                dmeVmwareRelation.setShareName(newShareName);
            }else {
                LOG.info("get share name error,the share name field of the relational table is not updated!shareId={}", shareId);
            }

            // 更新表中字段
            LOG.info("starting update database !");
            dmeVmwareRalationDao.updateNfs(dmeVmwareRelation);
        } catch (VcenterException e) {
            LOG.error("update nfs datastore error !", e);
            throw new DmeException(CODE_503, e.getMessage());
        }
    }

    private String getShareNameByShareId(String shareId){
        if(StringUtils.isEmpty(shareId)){
            return "";
        }
        String url = DmeConstants.DME_NFS_SHARE_DETAIL_URL.replace(NFS_SHARE_ID_STR, shareId);
        LOG.error("getNfsDatastoreShareAttr!method=get, shareId={},url={}, body=null", shareId, url);
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() / DIGIT_100 == DIGIT_2) {
               JsonObject shareDetail = gson.fromJson(responseEntity.getBody(), JsonObject.class);
               return shareDetail.get(NAME_FIELD).getAsString();
            }
        } catch (DmeException e) {
            LOG.error("get share name error!errorMsg={}",e.getMessage());
        }
        return "";
    }

    @Override
    public void changeNfsCapacity(Map<String, Object> params) throws DmeException {
        if (params == null || params.size() == 0) {
            throw new DmeException(CODE_503, "change nfs storage capacity error,params is null!");
        }
        String storeObjectId = (String) params.get("storeObjectId");
        String fileSystemId = (String) params.get("fileSystemId");
        Boolean isExpand = (Boolean) params.get("expand");
        Double capacity = Double.valueOf(params.get(CAPACITY).toString());
        if ((StringUtils.isEmpty(storeObjectId) || StringUtils.isEmpty(fileSystemId)) || StringUtils.isEmpty(isExpand)
                || StringUtils.isEmpty(capacity)) {
            throw new DmeException(CODE_400, "change nfs storage capacity error,params error!");
        }
        if (capacity == 0) {
            throw new DmeException(CODE_400, "change nfs storage capacity error,capacity params error!");
        }
        if (!StringUtils.isEmpty(storeObjectId)) {
            List<String> fsIds = dmeVmwareRalationDao.getFsIdsByStorageId(storeObjectId);
            if (fsIds.size() > 0) {
                // 如果objectid存在，则通过objectid的关系表来获取fsid
                fileSystemId = fsIds.get(0);
            }
        }
        FileSystem fileSystem = new FileSystem();
        Map<String, String> orientedFileSystem = getOrientedFs(fileSystemId);
        if ("200".equals(orientedFileSystem.get("code"))) {
            String fs = orientedFileSystem.get(DATA);
            if (!StringUtils.isEmpty(fs)) {
                fileSystem = gson.fromJson(fs, FileSystem.class);
            }
        }
        if (fileSystem == null) {
            throw new DmeException(CODE_503, "has not found the filesystem!id=" + fileSystemId);
        }
        String storageId = fileSystem.getStorageId();
        String storagePoolName = fileSystem.getStoragePoolName();
        Double minSizeFsCapacity = fileSystem.getMinSizeFsCapacity();
        Double availableCapacity = fileSystem.getAvailableCapacity();
        String allocType = fileSystem.getAllocType();
        Double currentCapacity = fileSystem.getCapacity();
        Double dataSpace = getDataspaceOfStoragepool(storagePoolName, null, storageId);
        Double exchangedCapacity = 0.0;
        Double changeCapacity = capacity;
        if (isExpand) {
            // 扩容
            if (dataSpace != null && Double.compare(changeCapacity, dataSpace) == 1) {
                LOG.info("The expansion exceeds the available storage pool capacity!");
                //所阔容量不受存储池容量大小限制
                //changeCapacity = dataSpace;
                changeCapacity = changeCapacity;
            }
            exchangedCapacity = changeCapacity + currentCapacity;
        } else if (!isExpand) {
            // 缩容
            if (!StringUtils.isEmpty(allocType) && THIN_FIELD.equals(allocType)) {
                //  该文件系统总容量-可用容量-文件系统能缩容的最小空间=实际可用缩小容量与变化量进行比较
                if (currentCapacity - changeCapacity >= minSizeFsCapacity) {
                    if (Double.compare(changeCapacity, availableCapacity) == 1) {
                        exchangedCapacity = currentCapacity - availableCapacity;
                    } else {
                        exchangedCapacity = currentCapacity - changeCapacity;
                    }
                } else {
                    exchangedCapacity = currentCapacity;
                    throw new DmeException(CODE_400,
                            "The reduced capacity shall not be less than:" + minSizeFsCapacity + "GB");
                }
            } else {
                if (Double.compare(availableCapacity, changeCapacity) == 1) {
                    exchangedCapacity = currentCapacity - changeCapacity;
                } else {
                    exchangedCapacity = currentCapacity - availableCapacity;
                }
            }
        }
        Map<String, Object> reqParam = new HashMap<>();
        reqParam.put("file_system_id", fileSystemId);
        reqParam.put(CAPACITY, exchangedCapacity);
        String url = DmeConstants.DME_NFS_FILESERVICE_DETAIL_URL.replace(FILE_SYSTEM_ID, fileSystemId);
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.PUT, gson.toJson(reqParam));

        Integer code = responseEntity.getStatusCodeValue();
        if (code != HttpStatus.ACCEPTED.value()) {
            throw new DmeException(String.valueOf(code), "expand or recycle nfs storage capacity error!");
        }
        String object = responseEntity.getBody();
        JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
        String taskId = ToolUtils.jsonToStr(jsonObject.get(TASK_ID));
        List<String> taskIds = new ArrayList<>();
        taskIds.add(taskId);
        Boolean flag = taskService.checkTaskStatus(taskIds);
        if (!flag) {
            LOG.info("changeNfsCapacity DME return wrong status!task_id={}", taskId);
            String errorMsg = taskService.queryTaskById(taskId).getDetail();
            throw new DmeException(CODE_503, errorMsg);
        }

        // 刷新datastore容量
        vcsdkUtils.refreshDatastore(storeObjectId);
    }

    // create file system
    private String createFileSystem(Map<String, Object> params, String storagePoolId) throws DmeException {
        if (params == null || params.size() == 0) {
            throw new DmeException(CODE_403, "create file system error!params is null");
        }
        String storageId = (String) params.get(STORAGE_ID);
        Map<String, Object> filesystemSpecsMap = new HashMap<>();
        List<Map<String, Object>> filesystemSpecsLists = new ArrayList<>();

        int count = 1;
        Double dataSpace = null;
        if (!StringUtils.isEmpty(storageId) || !StringUtils.isEmpty(storagePoolId)) {
            dataSpace = getDataspaceOfStoragepool(null, storagePoolId, storageId);
        }

        // 目前一个nfs 对应一个fs (一对多通用)
        String filesystemSpecs = (String) params.get(FILESYSTEM_SPECS);
        List<Map<String, Object>> filesystemSpecsList = gson.fromJson(filesystemSpecs, List.class);
        for (int index = 0; index < filesystemSpecsList.size(); index++) {
            Map<String, Object> filesystemSpec = filesystemSpecsList.get(index);
            Object objCapacity = filesystemSpec.get(CAPACITY);
            if (objCapacity != null) {
                Double capacity = Double.valueOf(objCapacity.toString());
                if (Double.compare(capacity * count, dataSpace) > 1) {
                    capacity = dataSpace / count;
                }
                filesystemSpecsMap.put(CAPACITY, capacity);
                filesystemSpecsMap.put(NAME_FIELD, filesystemSpec.get(NAME_FIELD));
                filesystemSpecsMap.put("count", count);
                filesystemSpecsMap.put("start_suffix", filesystemSpec.get("start_suffix"));
            }
            filesystemSpecsLists.add(filesystemSpecsMap);
        }
        params.put(FILESYSTEM_SPECS, filesystemSpecsLists);
        LOG.info("DME 创建NFS报文：{}", gson.toJson(params));
        String url = DmeConstants.API_FS_CREATE;
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, gson.toJson(params));
        int code = responseEntity.getStatusCodeValue();
        if (code != HttpStatus.ACCEPTED.value()) {
            throw new DmeException(CODE_503, "create file system error!" + responseEntity.getBody());
        }
        String object = responseEntity.getBody();
        JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
        return ToolUtils.jsonToStr(jsonObject.get(TASK_ID));
    }

    private boolean createNfsShare(Map<String, Object> params) throws DmeException {
        ResponseEntity<String> responseEntity = dmeAccessService.access(DmeConstants.API_NFSSHARE_CREATE,
                HttpMethod.POST, gson.toJson(params));
        int code = responseEntity.getStatusCodeValue();
        if (code != HttpStatus.ACCEPTED.value()) {
            throw new DmeException(CODE_503, "create nfs share error !");
        }
        String object = responseEntity.getBody();
        JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
        List<String> shareIds = new ArrayList<>();
        shareIds.add(ToolUtils.jsonToStr(jsonObject.get(TASK_ID)));
        return taskService.checkTaskStatus(shareIds);
    }

    private Double getDataspaceOfStoragepool(String storagePoolName, String poolId, String storageId)
            throws DmeException {
        JsonArray constraint = queryConditionInit(storagePoolName, poolId, storageId);
        JsonObject condition = new JsonObject();
        condition.add("constraint", constraint);
        String className = "SYS_StoragePool";
        String url = String.format(DmeConstants.DME_RESOURCE_INSTANCE_LIST, className)
                + "?pageSize=1000&condition={json}";
        ResponseEntity<String> responseEntity = dmeAccessService.accessByJson(url, HttpMethod.GET,
                gson.toJson(condition));
        int code = responseEntity.getStatusCodeValue();
        Double dataSpace = 0.0;
        if (code == HttpStatus.OK.value()) {
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("objList").getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();

                // 可用空间=总容量-已用容量
                Double totalCapacity = ToolUtils.jsonToDou(element.get("totalCapacity"), 0.0);
                Double consumedCapacity = ToolUtils.jsonToDou(element.get("usedCapacity"), 0.0);
                if (Double.max(totalCapacity, consumedCapacity) == totalCapacity) {
                    dataSpace = totalCapacity - consumedCapacity;
                }
            }
        }
        return dataSpace;
    }

    private JsonArray queryConditionInit(String storagePoolName, String poolId, String storageId) {
        JsonObject simple = new JsonObject();
        simple.addProperty(NAME_FIELD, "dataStatus");
        simple.addProperty(OPERATOR, EQUAL_FIELD);
        simple.addProperty(VALUE_FIELD, "normal");
        JsonObject consObj = new JsonObject();
        consObj.add(SIMPLE, simple);
        JsonArray constraint = new JsonArray();
        constraint.add(consObj);

        JsonObject simple2 = new JsonObject();
        simple2.addProperty(NAME_FIELD, "storageDeviceId");
        simple2.addProperty(OPERATOR, EQUAL_FIELD);
        simple2.addProperty(VALUE_FIELD, storageId.replace("-", "").toUpperCase());
        JsonObject consObj2 = new JsonObject();
        consObj2.add(SIMPLE, simple2);
        consObj2.addProperty(LOGOP, AND_FIELD);
        constraint.add(consObj2);
        JsonObject consObj1 = new JsonObject();
        JsonObject simple1 = new JsonObject();
        if (!StringUtils.isEmpty(poolId)) {
            simple1.addProperty(NAME_FIELD, "pool_id");
            simple1.addProperty(OPERATOR, EQUAL_FIELD);
            simple1.addProperty(VALUE_FIELD, poolId);
            consObj1.add(SIMPLE, simple1);
            consObj1.addProperty(LOGOP, AND_FIELD);
            constraint.add(consObj1);
        }
        if (!StringUtils.isEmpty(storagePoolName)) {
            simple1.addProperty(NAME_FIELD, "name");
            simple1.addProperty(OPERATOR, EQUAL_FIELD);
            simple1.addProperty(VALUE_FIELD, storagePoolName);
            consObj1.add(SIMPLE, simple1);
            consObj1.addProperty(LOGOP, AND_FIELD);
            constraint.add(consObj1);
        }
        return constraint;
    }

    private Boolean updateFileSystem(Map<String, Object> params, String fileSystemId) throws DmeException {
        if (StringUtils.isEmpty(fileSystemId)) {
            throw new DmeException(CODE_503, "updateFileSystem fileSystemId is null");
        }
        String url = DmeConstants.DME_NFS_FILESERVICE_DETAIL_URL.replace(FILE_SYSTEM_ID, fileSystemId);
        LOG.info("DME 修改NFS请求报文:{}", gson.toJson(params));
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.PUT, gson.toJson(params));
        int code = responseEntity.getStatusCodeValue();
        if (code != HttpStatus.ACCEPTED.value()) {
            throw new DmeException(CODE_503, "update nfs datastore error !" + responseEntity.getBody());
        }
        String object = responseEntity.getBody();
        JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
        String taskId = ToolUtils.jsonToStr(jsonObject.get(TASK_ID));
        if (StringUtils.isEmpty(taskId)){
            throw new DmeException("update nfs datastore error : taskid is empty!");
        }
        TaskDetailInfoNew taskinfo = taskService.queryTaskByIdReturnMainTask(taskId, longTaskTimeOut);
        if (!StringUtils.isEmpty(taskinfo)){
            if (taskinfo.getStatus() != 3){
                throw new DmeException("update nfs datastore error :"+taskinfo.getDetailEn());
            }
        }else {
            throw new DmeException("update nfs datastore error : taskinfo is empty!");
        }
        return true;
    }

    private String getFsIdByName(String fsname) throws DmeException {
        String fsId = "";
        Map<String, String> reqMap = new HashMap<>();
        if (!StringUtils.isEmpty(fsname)) {
            reqMap.put(NAME_FIELD, fsname);
            ResponseEntity<String> responseEntity = dmeAccessService.access(DmeConstants.DME_NFS_FILESERVICE_QUERY_URL,
                    HttpMethod.POST, gson.toJson(reqMap));
            if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
                String object = responseEntity.getBody();
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get(DATA).getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    String name = ToolUtils.jsonToStr(element.get(NAME_FIELD));
                    if (fsname.equals(name)) {
                        fsId = ToolUtils.jsonToStr(element.get("id"));
                        break;
                    }
                }
            }
        }
        return fsId;
    }

    private String getShareIdByName(String shareName, String fsname) throws DmeException {
        String shareId = "";
        Map<String, String> reqMap = new HashMap<>();
        if (!StringUtils.isEmpty(shareName) && !StringUtils.isEmpty(fsname)) {
            reqMap.put(NAME_FIELD, shareName);
            reqMap.put("fs_name", fsname);
            String url = DmeConstants.DME_NFS_SHARE_URL;
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, gson.toJson(reqMap));
            if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
                String object = responseEntity.getBody();
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("nfs_share_info_list").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    if (ToolUtils.jsonToStr(element.get(NAME_FIELD)).equals(shareName)) {
                        shareId = ToolUtils.jsonToStr(element.get("id"));
                        break;
                    }
                }
            }
        }
        return shareId;
    }

    private Map<String, String> getMgmtByStorageId(String storageId, String currentPortId) throws DmeException {
        Map<String, String> respMap = new HashMap<>();
        String mgmt = "";
        String logicName = "";
        List<LogicPorts> logicPorts = dmeStorageService.getLogicPorts(storageId, "all", null);
        if (logicPorts != null) {
            for (LogicPorts logicPort : logicPorts) {
                if (logicPort != null && !StringUtils.isEmpty(currentPortId) &&
                        logicPort.getId().equalsIgnoreCase(currentPortId)) {
                    mgmt = logicPort.getMgmtIp();
                    logicName = logicPort.getCurrentPortName();
                    break;
                }
            }
        }
        respMap.put("mgmt", mgmt);
        respMap.put("logicPortName", logicName);
        return respMap;
    }

    private Map<String, String> getOrientedFs(String fileSystemId) throws DmeException {
        if (StringUtils.isEmpty(fileSystemId)) {
            throw new DmeException("query filesyste param fileSystemId is null");
        }
        Map<String, String> resMap = new HashMap<>();
        resMap.put("code", "200");
        resMap.put("msg", "query filesystem success!");
        String url = DmeConstants.DME_NFS_FILESERVICE_DETAIL_URL.replace(FILE_SYSTEM_ID, fileSystemId);
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        int code = responseEntity.getStatusCodeValue();
        if (code != HttpStatus.OK.value()) {
            throw new DmeException(String.valueOf(code), "query filesystem error!");
        }
        String object = responseEntity.getBody();
        JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
        FileSystem fileSystem = new FileSystem();
        fileSystem.setCapacity(Double.valueOf(jsonObject.get(CAPACITY).getAsString()));
        fileSystem.setAllocateQuotaInPool(ToolUtils.jsonToDou(jsonObject.get("allocate_quota_in_pool")));
        fileSystem.setAvailableCapacity(ToolUtils.jsonToDou(jsonObject.get("available_capacity")));
        fileSystem.setMinSizeFsCapacity(ToolUtils.jsonToDou(jsonObject.get("min_size_fs_capacity")));
        fileSystem.setStorageId(ToolUtils.jsonToStr(jsonObject.get(STORAGE_ID)));
        fileSystem.setStoragePoolName(ToolUtils.jsonToStr(jsonObject.get("storage_pool_name")));
        fileSystem.setAllocType(ToolUtils.jsonToStr(jsonObject.get("alloc_type")));
        resMap.put(DATA, gson.toJson(fileSystem));
        return resMap;
    }

    private void saveNfsInfoToDmeVmwareRelation(String params, String currentPortId, String logicPortName, String fsId,
                                                String shareName, String shareId, String fsName, String storageModel,
                                                String storageId) throws DmeException {
        if (!StringUtils.isEmpty(params)) {
            DmeVmwareRelation datastoreInfo = gson.fromJson(params, DmeVmwareRelation.class);
            datastoreInfo.setLogicPortId(currentPortId);
            datastoreInfo.setLogicPortName(logicPortName);
            datastoreInfo.setFsId(fsId);
            datastoreInfo.setFsName(fsName);
            datastoreInfo.setShareName(shareName);
            datastoreInfo.setShareId(shareId);
            datastoreInfo.setStorageType(storageModel);
            datastoreInfo.setStorageDeviceId(storageId);
            List<DmeVmwareRelation> dmeVmwareRelations = new ArrayList<>();
            dmeVmwareRelations.add(datastoreInfo);
            dmeVmwareRalationDao.save(dmeVmwareRelations);
        } else {
            throw new DmeException("save nfs datastoreInfo error");
        }
    }

    @Override
    public Map<String, Object> getEditNfsStore(String storeObjectId) throws DmeException {
        List<String> fsIds = dmeVmwareRalationDao.getFsIdsByStorageId(storeObjectId);
        String deviceId = dmeVmwareRalationDao.getStorageIdlByObjectId(storeObjectId);
        Map<String, Object> summaryMap = vcsdkUtils.getDataStoreSummaryByObjectId(storeObjectId);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(NFS_NAME, String.valueOf(summaryMap.get(NAME_FIELD)));
        resultMap.put("deviceId", deviceId);
        String fsname = "";
        if (fsIds.size() <= 0) {
            throw new DmeException("there is no corresponding file system!storeObjectId={}", storeObjectId);
        }
        for (int index = 0; index < fsIds.size(); index++) {
            String fileSystemId = fsIds.get(index);
            if (StringUtils.isEmpty(fileSystemId)) {
                continue;
            }
            String url = DmeConstants.DME_NFS_FILESERVICE_DETAIL_URL.replace(FILE_SYSTEM_ID, fileSystemId);
            ResponseEntity<String> responseTuning = dmeAccessService.access(url, HttpMethod.GET, null);
            LOG.info(responseTuning.toString());
            if (responseTuning.getStatusCodeValue() / DIGIT_100 == DIGIT_2) {
                fsname = fsDetailPrase(resultMap, fileSystemId, responseTuning);
            }
            break;
        }

        // 根据存储ID 获取逻nfs_share_id
        String nfsShareId = dmeVmwareRalationDao.getShareIdByStorageId(storeObjectId);
        if (null == nfsShareId) {
            throw new DmeException("没有对应的共享");
        }

        resultMap.put("shareId", nfsShareId);
        String url = DmeConstants.DME_NFS_SHARE_DETAIL_URL.replace(NFS_SHARE_ID_STR, nfsShareId);
        LOG.error("deleteAuthClient!method=get, nfsShareId={},url={}, body=null", nfsShareId, url);
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        if (responseEntity.getStatusCodeValue() != HttpStatus.OK.value()) {
            LOG.error("get NFS Share error！response：{}", responseEntity.getBody());
            return Collections.emptyMap();
        }
        String resBody = responseEntity.getBody();
        JsonObject share = gson.fromJson(resBody, JsonObject.class);
        resultMap.put("shareName", ToolUtils.jsonToStr(share.get(NAME_FIELD)));
        if (String.valueOf(summaryMap.get(NAME_FIELD)).equalsIgnoreCase(fsname) && ("/" + summaryMap.get(
                NAME_FIELD)).equalsIgnoreCase(ToolUtils.jsonToStr(share.get(NAME_FIELD)))) {
            resultMap.put("sameName", true);
        } else {
            resultMap.put("sameName", false);
        }
        return resultMap;
    }

    private String fsDetailPrase(Map<String, Object> resultMap, String fileSystemId,
                                 ResponseEntity<String> responseTuning) {
        String fsname;
        //JsonObject fsDetail = gson.fromJson(responseTuning.getBody(), JsonObject.class);
        JsonObject object = new JsonParser().parse(responseTuning.getBody()).getAsJsonObject();
        resultMap.put("fsName", ToolUtils.jsonToStr(object.get(NAME_FIELD)));
        fsname = ToolUtils.jsonToStr(object.get(NAME_FIELD));
        resultMap.put("fileSystemId", fileSystemId);
        JsonObject json = object.get("capacity_auto_negotiation").getAsJsonObject();
        resultMap.put("autoSizeEnable", getAutoSizeEnableStatus(json));
        boolean isThin = (THIN_FIELD.equalsIgnoreCase(ToolUtils.jsonToStr(object.get("alloc_type")))) ? true : false;
        resultMap.put(THIN_FIELD, isThin);
        resultMap.put("capacity", ToolUtils.jsonToDou(object.get("capacity")));
        JsonObject tuning = object.get(TUNING).getAsJsonObject();
        resultMap.put("compressionEnabled", ToolUtils.jsonToBoo(tuning.get("compression_enabled")));
        resultMap.put("deduplicationEnabled", ToolUtils.jsonToBoo(tuning.get("deduplication_enabled")));
        boolean qos = tuning.get("smart_qos").isJsonNull();
        if (!qos) {
            JsonObject smartQos = tuning.get("smart_qos").getAsJsonObject();
            if (smartQos != null) {
                smartQosParse(resultMap, gson.toJson(smartQos));
            }
        } else {
            resultMap.put("qosFlag", false);
        }
        return fsname;
    }

    private void smartQosParse(Map<String, Object> resultMap, String smartQos) {
        resultMap.put("qosFlag", true);
        JsonObject qosPolicy = new JsonParser().parse(smartQos).getAsJsonObject();
        resultMap.put("maxBandwidth", ToolUtils.jsonToInt(qosPolicy.get("max_bandwidth")));
        resultMap.put("maxIops", ToolUtils.jsonToInt(qosPolicy.get("max_iops")));
        resultMap.put("latency", ToolUtils.jsonToInt(qosPolicy.get("latency")));
        resultMap.put("minBandwidth", ToolUtils.jsonToInt(qosPolicy.get("min_bandwidth")));
        resultMap.put("minIops", ToolUtils.jsonToInt(qosPolicy.get("min_iops")));
    }

    private String getStorageModel(String storageId) throws DmeException {
        StorageDetail storageDetail = dmeStorageService.getStorageDetail(storageId);
        return storageDetail.getModel() + " " + storageDetail.getProductVersion();
    }

    // 查询指定share,获取共享在存储设备上的id
    private String getOrientedShare(String shareId) throws DmeException {
        String idInStorage = "";
        String url = DmeConstants.DME_NFS_SHARE_DETAIL_URL.replace(NFS_SHARE_ID_STR, shareId);
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        LOG.info("url:" + url + "responseEntity:" + gson.toJson(responseEntity));
        if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
            String entity = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(entity).getAsJsonObject();
            idInStorage = ToolUtils.jsonToStr(jsonObject.get("id_in_storage"));
        }
        return idInStorage;
    }

    // 解决share挂载的主机
    private void removeMappingHostOnShare(String shareId, String idInStorage, String name) throws DmeException {

        JsonObject reqObject = new JsonObject();
        reqObject.addProperty("nfs_share_client_id_in_storage", idInStorage);
        reqObject.addProperty(NAME_FIELD, name);
        List<JsonObject> reqList = new ArrayList<>();
        reqList.add(reqObject);
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("nfs_share_client_deletion", reqList);
        String url = DmeConstants.DME_NFS_SHARE_DETAIL_URL.replace(NFS_SHARE_ID_STR, shareId);
        LOG.info("url:" + url + ",requestBody:" + gson.toJson(reqMap));
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.PUT, gson.toJson(reqMap));
        LOG.info("url:" + url + ",requestBody:" + gson.toJson(responseEntity));
        if (responseEntity.getStatusCodeValue() == HttpStatus.ACCEPTED.value()) {
            String body = responseEntity.getBody();
            JsonObject object = new JsonParser().parse(body).getAsJsonObject();
            String taskId = ToolUtils.jsonToStr(object.get("task_id"));
            List<String> taskIds = new ArrayList<>();
            taskIds.add(taskId);
            if (!taskService.checkTaskStatus(taskIds)) {
                LOG.info("remove client host on share fail!");
                return;
            }
            LOG.info("remove client host on share success!");
        }
    }

    // 解除共享之后删除共享
    private void deleteShare(String shareId) throws DmeException {
        List<String> reqList = new ArrayList<>();
        reqList.add(shareId);
        Map<String, List<String>> reqMap = new HashMap<>();
        reqMap.put("nfs_share_ids", reqList);
        ResponseEntity<String> responseEntity = dmeAccessService.access(DmeConstants.DME_NFS_SHARE_DELETE_URL,
                HttpMethod.POST, gson.toJson(reqMap));
        if (responseEntity.getStatusCodeValue() == HttpStatus.ACCEPTED.value()) {
            String body = responseEntity.getBody();
            JsonObject object = new JsonParser().parse(body).getAsJsonObject();
            String taskId = ToolUtils.jsonToStr(object.get("task_id"));
            List<String> taskIds = new ArrayList<>();
            taskIds.add(taskId);
            if (!taskService.checkTaskStatus(taskIds)) {
                LOG.info("delete nfs share fail!");
                return;
            }
            LOG.info("delete nfs share success!");
        }
    }

    // 删除文件系统
    private void deleteFiLeSystem(String fsId) throws DmeException {
        List<String> reqList = new ArrayList<>();
        reqList.add(fsId);
        Map<String, List<String>> reqMap = new HashMap<>();
        reqMap.put("file_system_ids", reqList);
        ResponseEntity<String> responseEntity = dmeAccessService.access(DmeConstants.DME_NFS_FS_DELETE_URL,
                HttpMethod.POST, gson.toJson(reqMap));
        if (responseEntity.getStatusCodeValue() == HttpStatus.ACCEPTED.value()) {
            String body = responseEntity.getBody();
            JsonObject object = new JsonParser().parse(body).getAsJsonObject();
            String taskId = ToolUtils.jsonToStr(object.get("task_id"));
            List<String> taskIds = new ArrayList<>();
            taskIds.add(taskId);
            if (!taskService.checkTaskStatus(taskIds)) {
                LOG.info("delete file system fail!");
                return;
            }
            LOG.info("delete file system success!");
        }
    }

    /**
     * @return @return
     * @throws
     * @Description: 根据查询结果的自动扩容策略来展示自动扩容策略
     * @Param @param null
     * @author yc
     * @Date 2021/4/15 18:13
     */
    private boolean getAutoSizeEnableStatus(JsonObject json) {
        boolean status = false;
        String capacitySelfAdjustingMode = ToolUtils.jsonToStr(json.get("capacity_self_adjusting_mode"));
        String autoSizeEnable = ToolUtils.jsonToStr(json.get("auto_size_enable"));
        if (Boolean.valueOf(autoSizeEnable)) {
            if (!"grow_shrink".equalsIgnoreCase(capacitySelfAdjustingMode)) {
                status = false;
            } else {
                status = true;
            }
        }
        return status;
    }
}
