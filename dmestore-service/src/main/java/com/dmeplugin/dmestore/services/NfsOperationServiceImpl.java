package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.dao.DmeVmwareRalationDao;
import com.dmeplugin.dmestore.entity.DmeVmwareRelation;
import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.exception.VcenterRuntimeException;
import com.dmeplugin.dmestore.model.FileSystem;
import com.dmeplugin.dmestore.model.LogicPorts;
import com.dmeplugin.dmestore.mvc.VmfsOperationController;
import com.dmeplugin.dmestore.utils.StringUtil;
import com.dmeplugin.dmestore.utils.ToolUtils;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;


/**
 * @author lianq
 * @className NfsOperationServiceImpl
 * @description TODO
 * @date 2020/9/16 16:25
 */
public class NfsOperationServiceImpl implements NfsOperationService {

    private final String API_FS_CREATE = "/rest/fileservice/v1/filesystems/customize";
    private final String API_NFSSHARE_CREATE = "/rest/fileservice/v1/nfs-shares";
    private final String API_STORAGEPOOL_LIST = "/rest/resourcedb/v1/instances/";
    private final String API_FS_UPDATE = "/rest/fileservice/v1/filesystems";
    private final String API_FS_QUERYONE = "/rest/fileservice/v1/filesystems/query";
    private final String API_FILESYSTEMS_DETAIL = "/rest/fileservice/v1/filesystems/";
    private final String API_SHARE_QUERYONE = "/rest/fileservice/v1/nfs-shares/summary";

    private final String RESULT_FAIL = "failed";
    private final String RESULT_SUCCESS = "success";

    public static final Logger LOG = LoggerFactory.getLogger(VmfsOperationController.class);
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

    /**
     * @param params
     * @return
     */
    @Override
    public void createNfsDatastore(Map<String, Object> params) throws DMEException {

        if (params == null || params.size() == 0) {
            throw new DMEException("403", "params error , please check your params !");
        }
        try {
            //创建fs
            Map<String, Object> fsMap = new HashMap<>();
            Map<String, Object> nfsShareMap = new HashMap<>();
            Object filesystemSpecs = params.get("filesystem_specs");
            if (StringUtils.isEmpty(filesystemSpecs)) {
                LOG.error("{filesystem_specs}={" + filesystemSpecs + "}");
                throw new DMEException("403", "{filesystem_specs}={" + filesystemSpecs + "}");
            }

            String fsName = "";
            JsonArray jsonArray1 = gson.fromJson(gson.toJson(filesystemSpecs), JsonArray.class);
            for (JsonElement jsonElement : jsonArray1) {
                JsonObject element = jsonElement.getAsJsonObject();
                fsName = ToolUtils.jsonToStr(element.get("name"));
            }
            fsMap.put("filesystem_specs", gson.toJson(filesystemSpecs));
            String poolRawId = (String) params.get("pool_raw_id");
            String storageId = (String) params.get("storage_id");
            if (StringUtils.isEmpty(storageId) || StringUtils.isEmpty(storageId)) {
                LOG.error("{pool_raw_id/storage_id}={" + poolRawId + "/" + storageId + "}");
                throw new DMEException("403", "{pool_raw_id/storage_id}={" + poolRawId + "/" + storageId + "}");
            }
            fsMap.put("pool_raw_id", poolRawId);
            fsMap.put("storage_id", storageId);
            Object tuning = params.get("tuning");
            if (!StringUtils.isEmpty(tuning)) {
                Map<String, Object> tuningMap = gson.fromJson(tuning.toString(), Map.class);
                Object qoPolicy = params.get("qos_policy");
                if (qoPolicy != null) {
                    tuningMap.put("qos_policy", qoPolicy);
                }
                fsMap.put("tuning", tuningMap);
            }

            Object capacityAutonegotiation = params.get("capacity_autonegotiation");
            if (!StringUtils.isEmpty(capacityAutonegotiation)) {
                fsMap.put("capacity_autonegotiation", capacityAutonegotiation);
            }
            //可以挂载多个
            List<Map<String, String>> mounts = new ArrayList<>();
            Map<String, String> mount = new HashMap<>();
            List<Object> reqNfsShareClientArrayAdditions = new ArrayList<>();
            Object nfsShareClientAddition = params.get("nfs_share_client_addition");
            List<Map<String, Object>> nfsShareClientArrayAddition = (List<Map<String, Object>>) nfsShareClientAddition;
            String shareName = "";
            if (nfsShareClientAddition != null) {
                Map<String, Object> reqNfsShareClientArrayAddition = new HashMap<>();
                for (int i = 0; i < nfsShareClientArrayAddition.size(); i++) {
                    Map<String, Object> shareClientHostMap = nfsShareClientArrayAddition.get(i);
                    if (shareClientHostMap != null && shareClientHostMap.size() > 0 &&
                        shareClientHostMap.get("objectId") != null) {
                        reqNfsShareClientArrayAddition.put("name", shareClientHostMap.get("name"));
                        //设置创建nfs默认值
                        reqNfsShareClientArrayAddition.put("accessval", "read-only");
                        reqNfsShareClientArrayAddition.put("sync", "synchronization");
                        reqNfsShareClientArrayAddition.put("all_squash", "all_squash");
                        reqNfsShareClientArrayAddition.put("root_squash", "root_squash");
                        reqNfsShareClientArrayAddition.put("secure", "insecure");
                        mount.put((String) shareClientHostMap.get("objectId"), (String) shareClientHostMap.get("name"));
                    }
                    reqNfsShareClientArrayAdditions.add(reqNfsShareClientArrayAddition);
                }
                mounts.add(mount);
                Object createNfsShareParam = params.get("create_nfs_share_param");
                Map<String, Object> createNfsShareParams = null;
                if (!StringUtils.isEmpty(createNfsShareParam)) {
                    createNfsShareParams = gson.fromJson(gson.toJson(createNfsShareParam), Map.class);
                    if (createNfsShareParams != null && createNfsShareParams.size() > 0) {
                        createNfsShareParams.put("nfs_share_client_addition", reqNfsShareClientArrayAdditions);
                        shareName = (String) createNfsShareParams.get("name");
                        Object showSnapshotEnable = params.get("show_snapshot_enable");
                        if (!StringUtils.isEmpty(showSnapshotEnable)) {
                            createNfsShareParams.put("show_snapshot_enable", gson.toJson(showSnapshotEnable));
                        }
                        nfsShareMap.put("create_nfs_share_param", createNfsShareParams);
                    }
                }
            }
            String storagePoolId = (String) params.get("storage_pool_id");
            if (StringUtils.isEmpty(storagePoolId)) {
                LOG.error("storage_pool_id={" + storagePoolId + "}");
            }
            String taskId = createFileSystem(fsMap, storagePoolId);
            String nfsName = (String) params.get("nfsName");
            if (StringUtils.isEmpty(nfsName)) {
                LOG.error("nfsName={" + nfsName + "}");
            }
            String fsId = "";
            String shareId = "";
            List<String> taskIds = new ArrayList<>();
            if (!StringUtils.isEmpty(taskId)) {
                taskIds.add(taskId);
                if (taskService.checkTaskStatus(taskIds)) {
                    fsId = getFsIdByName(fsName);
                } else {
                    throw new DMEException("403", "create FileSystem fail");
                }
                if (!"".equals(fsId)) {
                    nfsShareMap.put("fs_id", fsId);
                    String nfsShareTaskId = createNfsShare(nfsShareMap);
                    List<String> shareIds = new ArrayList<>();
                    if (!StringUtils.isEmpty(nfsShareTaskId)) {
                        shareIds.add(nfsShareTaskId);
                        if (taskService.checkTaskStatus(shareIds)) {
                            //查询shareId
                            shareId = getShareIdByName(shareName, fsName);
                        }
                    }
                }
            }
            //query oriented logic portv by storage_id
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

            if (StringUtils.isEmpty(serverHost) || StringUtils.isEmpty(exportPath) || StringUtils.isEmpty(accessMode) ||
                mounts.size() == 0) {
                throw new DMEException("403", "params error , please check your params !");
            }
            String result =
                vcsdkUtils.createNfsDatastore(serverHost, exportPath, nfsName, accessMode, mounts, type, securityType);
            if (RESULT_FAIL.equals(result)) {
                throw new VcenterRuntimeException("403", "create nfs datastore error!");
            }
            //save datastore info to DP_DME_VMWARE_RELATION
            saveNfsInfoToDmeVmwareRelation(result, currentPortId, logicPortName, fsId, shareName, shareId, fsName);
        } catch (Exception e) {
            LOG.error("create nfs datastore error", e);
            throw new DMEException("403", e.getMessage());
        }
    }

    /**
     * @param params
     */
    @Override
    public void updateNfsDatastore(Map<String, Object> params) throws DMEException {

        if (params == null || params.size() == 0) {
            LOG.error("params error , please check it! reqParams=" + params);
            throw new DMEException("403", "params error , please check it!");
        }
        String dataStoreObjectId = (String) params.get("dataStoreObjectId");
        String nfsName = (String) params.get("nfsName");
        if (StringUtils.isEmpty(dataStoreObjectId) || StringUtils.isEmpty(nfsName)) {
            LOG.error("params error , please check it! dataStoreObjectId=" + dataStoreObjectId);
            throw new DMEException("403", "params error , please check it!");
        }
        //update fs
        Map<String, Object> fsReqBody = new HashMap<>();
        Map<String, Object> tuning = gson.fromJson(gson.toJson(params.get("tuning")), Map.class);
        Map<String, Object> qosPolicy = gson.fromJson(gson.toJson(params.get("qos_policy")), Map.class);
        String name = (String) params.get("name");
        if (!StringUtils.isEmpty(name)) {
            fsReqBody.put("name", name);
        }
        if (tuning != null && tuning.size() != 0) {
            if (qosPolicy != null && qosPolicy.size() != 0) {
                tuning.put("qos_policy", qosPolicy);
            }
            fsReqBody.put("tuning", tuning);
        }
        String fileSystemId = (String) params.get("file_system_id");
        Object capacityAutonegotiation = params.get("capacity_autonegotiation");
        if (capacityAutonegotiation != null) {
            fsReqBody.put("capacity_autonegotiation", capacityAutonegotiation);
        }
        try {
            if (!StringUtils.isEmpty(fileSystemId)) {
                String taskId = updateFileSystem(fsReqBody, fileSystemId);
                //检查任务状态
                List<String> taskIds = new ArrayList<>(10);
                taskIds.add(taskId);
                Boolean flag = taskService.checkTaskStatus(taskIds);
                if (!flag) {
                    LOG.info("{" + fileSystemId + "}update file system failed!");
                    throw new DMEException("400", "update file system failed");
                }
            }
            //update nfs datastore
            String result = vcsdkUtils.renameDataStore(nfsName, dataStoreObjectId);
            if (RESULT_SUCCESS.equals(result)) {
                LOG.info("{" + nfsName + "}rename nfs datastore success!");
            }
        } catch (Exception e) {
            LOG.error("update nfs datastore error !", e);
            throw new DMEException("503", e.getMessage());
        }
    }

    @Override
    public void changeNfsCapacity(Map<String, Object> params) throws DMEException {

        if (params == null || params.size() == 0) {
            throw new DMEException("503", "change nfs storage capacity error,params error!");
        }
        String storeObjectId = (String) params.get("storeObjectId");
        String fileSystemId = (String) params.get("fileSystemId");
        Boolean isExpand = (Boolean) params.get("expand");
        Double capacity = Double.valueOf(params.get("capacity").toString());
        if ((StringUtils.isEmpty(storeObjectId) || StringUtils.isEmpty(fileSystemId)) ||
            StringUtils.isEmpty(isExpand) || StringUtils.isEmpty(capacity)) {
            throw new DMEException("400", "change nfs storage capacity error,params error!");
        }
        if (!StringUtils.isEmpty(storeObjectId)) {
            List<String> fsIds = dmeVmwareRalationDao.getFsIdsByStorageId(storeObjectId);
            if (fsIds.size() > 0) {
                //如果objectid存在，则通过objectid的关系表来获取fsid
                fileSystemId = fsIds.get(0);
            }
        }
        String url = API_FS_UPDATE + "/" + fileSystemId;
        //查询指定fs拿对应的信息
        Integer code = 0;
        try {
            FileSystem fileSystem = null;
            Map<String, String> orientedFileSystem = getOrientedFs(fileSystemId);
            if ("200".equals(orientedFileSystem.get("code"))) {
                String fs = orientedFileSystem.get("data");
                if (!StringUtils.isEmpty(fs)) {
                    fileSystem = gson.fromJson(fs, FileSystem.class);
                }
            }
            if (fileSystem != null) {
                String storageId = fileSystem.getStorageId();
                String storagePoolName = fileSystem.getStoragePoolName();
                Double minSizeFsCapacity = fileSystem.getMinSizeFsCapacity();
                Double availableCapacity = fileSystem.getAvailableCapacity();
                String allocType = fileSystem.getAllocType();
                Double currentCapacity = fileSystem.getCapacity();
                //查询存储池可用空间
                Double dataSpace = getDataspaceOfStoragepool(storagePoolName, null, storageId);
                Double exchangedCapacity = 0.0;
                Double changeCapacity = capacity;
                if (isExpand) {
                    //扩容
                    if (dataSpace != null && Double.compare(changeCapacity, dataSpace) > 1) {
                        LOG.info("扩容量超出存储池可用容量，将当前存储池可用容量当做扩容量!");
                        changeCapacity = dataSpace;
                    }
                    exchangedCapacity = changeCapacity + currentCapacity;
                } else if (!isExpand) {
                    //缩容
                    if (!StringUtils.isEmpty(allocType) && "thin".equals(allocType)) {
                        //thin 分配策略缩容
                        // 该文件系统总容量-可用容量-文件系统能缩容的最小空间=实际可用缩小容量    与变化量进行比较
                        if (currentCapacity - changeCapacity >= minSizeFsCapacity) {
                            if (Double.compare(changeCapacity, availableCapacity) > 1) {
                                LOG.info("thin策略：nfs预计缩容到Thin文件系统能缩容的最小空间!");
                                exchangedCapacity = currentCapacity - availableCapacity;
                            } else {
                                exchangedCapacity = currentCapacity - changeCapacity;
                            }
                        } else {
                            exchangedCapacity = currentCapacity;
                            LOG.info(
                                "FileSystem:{" + fileSystemId + "}未达到能缩容条件,FileSystem缩容后条件容量不得小于:" + minSizeFsCapacity +
                                    "GB");
                            throw new DMEException("400",
                                "FileSystem:{id:" + fileSystemId + "}未达到能缩容条件,FileSystem缩容后容量不得小于:" +
                                    minSizeFsCapacity + "GB");
                        }
                    } else {
                        if (Double.compare(availableCapacity, changeCapacity) > 1) {
                            exchangedCapacity = currentCapacity - changeCapacity;
                        } else {
                            exchangedCapacity = currentCapacity - availableCapacity;
                        }
                    }
                }
                Map<String, Object> reqParam = new HashMap<>();
                reqParam.put("file_system_id", fileSystemId);
                reqParam.put("capacity", exchangedCapacity);
                ResponseEntity<String> responseEntity =
                    dmeAccessService.access(url, HttpMethod.PUT, gson.toJson(reqParam));
                code = responseEntity.getStatusCodeValue();
                if (code != HttpStatus.ACCEPTED.value()) {
                    throw new DMEException(String.valueOf(code), "expand or recycle nfs storage capacity error!");
                }
                String object = responseEntity.getBody();
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                String taskId = ToolUtils.jsonToStr(jsonObject.get("task_id"));
                List<String> taskIds = new ArrayList<>();
                taskIds.add(taskId);
                Boolean flag = taskService.checkTaskStatus(taskIds);
                if (!flag) {
                    throw new DMEException("503", "expand or recycle nfs storage capacity failed!");
                }
                //刷新datastore容量
                vcsdkUtils.refreshDatastore(storeObjectId);
            }
        } catch (Exception e) {
            LOG.error("change nfs storage capacity error!", e);
            throw new DMEException("503", "change nfs storage capacity error!" + e.getMessage());
        }
    }

    //create file system
    private String createFileSystem(Map<String, Object> params, String storagePoolId) throws Exception {
        LOG.info("{params}:" + params);

        if (params == null || params.size() == 0) {
            LOG.error("url:{" + API_FS_CREATE + "},param error,please check it!");
            throw new DMEException("403", "create file system error !");
        }
        String storageId = (String) params.get("storage_id");
        Map<String, Object> filesystemSpecsMap = new HashMap<>();
        List<Map<String, Object>> filesystemSpecsLists = new ArrayList<>();

        Double capacity = null;
        int count = 1;
        Double dataSpace = null;
        if (!StringUtils.isEmpty(storageId) || !StringUtils.isEmpty(storagePoolId)) {
            dataSpace = getDataspaceOfStoragepool(null, storagePoolId, storageId);
        }
        //目前一个nfs 对应一个fs (一对多通用)
        String filesystemSpecs = (String) params.get("filesystem_specs");
        List<Map<String, Object>> filesystemSpecsList = gson.fromJson(filesystemSpecs, List.class);
        for (int i = 0; i < filesystemSpecsList.size(); i++) {
            Map<String, Object> filesystemSpec = filesystemSpecsList.get(i);
            Object objCapacity = filesystemSpec.get("capacity");
            if (objCapacity != null) {
                capacity = Double.valueOf(objCapacity.toString());
                if (Double.compare(capacity * count, dataSpace) > 1) {
                    capacity = dataSpace / count;
                }
                filesystemSpecsMap.put("capacity", capacity);
                filesystemSpecsMap.put("name", filesystemSpec.get("name"));
                filesystemSpecsMap.put("count", count);
                filesystemSpecsMap.put("start_suffix", filesystemSpec.get("start_suffix"));
            }
            filesystemSpecsLists.add(filesystemSpecsMap);
        }
        params.put("filesystem_specs", filesystemSpecsLists);
        ResponseEntity<String> responseEntity =
            dmeAccessService.access(API_FS_CREATE, HttpMethod.POST, gson.toJson(params));
        int code = responseEntity.getStatusCodeValue();
        if (code != HttpStatus.ACCEPTED.value()) {
            throw new DMEException("503", "create file system error !");
        }
        String object = responseEntity.getBody();
        JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
        return ToolUtils.jsonToStr(jsonObject.get("task_id"));
    }

    //create nfs share
    private String createNfsShare(Map<String, Object> params) throws Exception {

        LOG.info("{" + params + "}");
        ResponseEntity<String> responseEntity =
            dmeAccessService.access(API_NFSSHARE_CREATE, HttpMethod.POST, gson.toJson(params));
        int code = responseEntity.getStatusCodeValue();
        if (code != HttpStatus.ACCEPTED.value()) {
            throw new DMEException("503", "create nfs share error !");
        }
        String object = responseEntity.getBody();
        JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
        return ToolUtils.jsonToStr(jsonObject.get("task_id"));
    }

    //get oriented storage pool data_space
    private Double getDataspaceOfStoragepool(String storagePoolName, String poolId, String storageId) throws Exception {
        Double dataSpace = 0.0;
        String className = "SYS_StoragePool";
        String url = API_STORAGEPOOL_LIST + className + "?condition={json}";
        JsonObject condition = new JsonObject();
        JsonArray constraint = new JsonArray();

        JsonObject consObj = new JsonObject();
        JsonObject simple = new JsonObject();
        simple.addProperty("name", "dataStatus");
        simple.addProperty("operator", "equal");
        simple.addProperty("value", "normal");
        consObj.add("simple", simple);
        constraint.add(consObj);

        JsonObject consObj2 = new JsonObject();
        JsonObject simple2 = new JsonObject();
        simple2.addProperty("name", "storageDeviceId");
        simple2.addProperty("operator", "storageDeviceId");
        simple2.addProperty("value", storageId);
        consObj2.add("simple", simple2);
        consObj2.addProperty("logOp", "and");
        constraint.add(consObj2);
        JsonObject consObj1 = new JsonObject();
        JsonObject simple1 = new JsonObject();
        if (!StringUtils.isEmpty(poolId)) {
            simple1.addProperty("name", "pool_id");
            simple1.addProperty("operator", "equal");
            simple1.addProperty("value", poolId);
            consObj1.add("simple", simple1);
            consObj1.addProperty("logOp", "and");
            constraint.add(consObj1);
        }
        if (!StringUtils.isEmpty(storagePoolName)) {
            simple1.addProperty("name", "storage_pool_name");
            simple1.addProperty("operator", "equal");
            simple1.addProperty("value", storagePoolName);
            consObj1.add("simple", simple1);
            consObj1.addProperty("logOp", "and");
            constraint.add(consObj1);
        }
        condition.add("constraint", constraint);
        String s = gson.toJson(condition);
        ResponseEntity<String> responseEntity =
            dmeAccessService.accessByJson(url, HttpMethod.GET, gson.toJson(condition));
        LOG.info("url:{" + url + "},responseEntity:" + responseEntity);
        int code = responseEntity.getStatusCodeValue();
        if (code == HttpStatus.OK.value()) {
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("objList").getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                //可用空间=总容量-已用容量
                Double totalCapacity = ToolUtils.jsonToDou(element.get("totalCapacity"), 0.0);
                Double consumedCapacity = ToolUtils.jsonToDou(element.get("usedCapacity"), 0.0);
                if (Double.max(totalCapacity, consumedCapacity) == totalCapacity) {
                    dataSpace = totalCapacity - consumedCapacity;
                }
            }
        }
        return dataSpace;
    }

    private String updateFileSystem(Map<String, Object> params, String fileSystemId) throws Exception {

        if (StringUtils.isEmpty(fileSystemId)) {
        }
        String url = API_FS_UPDATE + "/" + fileSystemId;

        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.PUT, gson.toJson(params));
        int code = responseEntity.getStatusCodeValue();
        if (code != HttpStatus.ACCEPTED.value()) {
            throw new DMEException("503", "update nfs datastore error !" + responseEntity.getBody());
        }
        String object = responseEntity.getBody();
        JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
        return ToolUtils.jsonToStr(jsonObject.get("task_id"));
    }

    private String getFsIdByName(String fsname) throws Exception {
        String fsId = "";
        Map<String, String> reqMap = new HashMap<>();
        if (!StringUtils.isEmpty(fsname)) {
            reqMap.put("name", fsname);
            ResponseEntity<String> responseEntity =
                dmeAccessService.access(API_FS_QUERYONE, HttpMethod.POST, gson.toJson(reqMap));
            if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
                String object = responseEntity.getBody();
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    String name = ToolUtils.jsonToStr(element.get("name"));
                    if (fsname.equals(name)) {
                        fsId = ToolUtils.jsonToStr(element.get("id"));
                        break;
                    }
                }
            }
        }
        return fsId;
    }

    private String getShareIdByName(String shareName, String fsname) throws Exception {
        String shareId = "";
        Map<String, String> reqMap = new HashMap<>();
        if (!StringUtils.isEmpty(shareName) && !StringUtils.isEmpty(fsname)) {
            reqMap.put("name", shareName);
            reqMap.put("fs_name", fsname);
            ResponseEntity<String> responseEntity =
                dmeAccessService.access(API_SHARE_QUERYONE, HttpMethod.POST, gson.toJson(reqMap));
            if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
                String object = responseEntity.getBody();
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("nfs_share_info_list").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    if (ToolUtils.jsonToStr(element.get("name")).equals(shareName)) {
                        shareId = ToolUtils.jsonToStr(element.get("id"));
                        break;
                    }
                }
            }
        }
        return shareId;
    }

    private Map<String, String> getMgmtByStorageId(String storageId, String currentPortId) throws DMEException {
        Map<String, String> respMap = new HashMap<>();
        String mgmt = "";
        String logicName = "";
        List<LogicPorts> logicPort = dmeStorageService.getLogicPorts(storageId);
        if (logicPort != null) {
            JsonArray jsonArray = new JsonParser().parse(gson.toJson(logicPort)).getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                String currentPortId1 = element.get("homePortId").getAsString();
                if (!StringUtils.isEmpty(currentPortId) && currentPortId1.equals(currentPortId)) {
                    mgmt = ToolUtils.jsonToStr(element.get("mgmtIp"));
                    logicName = ToolUtils.jsonToStr(element.get("name"));
                    break;
                }
            }
        }
        respMap.put("mgmt", mgmt);
        respMap.put("logicPortName", logicName);
        return respMap;
    }

    private Map<String, String> getOrientedFs(String fileSystemId) throws Exception {

        Map<String, String> resMap = new HashMap<>();
        resMap.put("code", "200");
        resMap.put("msg", "list filesystem success!");

        String url = API_FILESYSTEMS_DETAIL + fileSystemId;

        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        int code = responseEntity.getStatusCodeValue();
        if (code != HttpStatus.OK.value()) {
            throw new DMEException(String.valueOf(code), "list filesystem error!");
        }
        String object = responseEntity.getBody();
        JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
        FileSystem fileSystem = new FileSystem();
        fileSystem.setCapacity(Double.valueOf(jsonObject.get("capacity").getAsString()));
        fileSystem.setAllocateQuotaInPool(ToolUtils.jsonToDou(jsonObject.get("allocate_quota_in_pool")));
        fileSystem.setAvailableCapacity(ToolUtils.jsonToDou(jsonObject.get("available_capacity")));
        fileSystem.setMinSizeFsCapacity(ToolUtils.jsonToDou(jsonObject.get("min_size_fs_capacity")));
        fileSystem.setStorageId(ToolUtils.jsonToStr(jsonObject.get("storage_id")));
        fileSystem.setStoragePoolName(ToolUtils.jsonToStr(jsonObject.get("storage_pool_name")));
        fileSystem.setAllocType(ToolUtils.jsonToStr(jsonObject.get("alloc_type")));

        resMap.put("data", gson.toJson(fileSystem));
        return resMap;

    }

    private void saveNfsInfoToDmeVmwareRelation(String params, String currentPortId, String logicPortName,
                                                String fsId, String shareName, String shareId, String fsName)
        throws DMEException {

        if (!StringUtils.isEmpty(params)) {
            DmeVmwareRelation datastoreInfo = gson.fromJson(params, DmeVmwareRelation.class);
            datastoreInfo.setLogicPortId(currentPortId);
            datastoreInfo.setLogicPortName(logicPortName);
            datastoreInfo.setFsId(fsId);
            datastoreInfo.setFsName(fsName);
            datastoreInfo.setShareName(shareName);
            datastoreInfo.setShareId(shareId);
            List<DmeVmwareRelation> dmeVmwareRelations = new ArrayList<>();
            dmeVmwareRelations.add(datastoreInfo);
            dmeVmwareRalationDao.save(dmeVmwareRelations);
        } else {
            throw new DMEException("save nfs datastoreInfo error");
        }
    }

    @Override
    public Map<String, Object> getEditNfsStore(String storeObjectId) throws DMEException {
        List<String> fsIds = dmeVmwareRalationDao.getFsIdsByStorageId(storeObjectId);
        Map<String, Object> summaryMap = vcsdkUtils.getDataStoreSummaryByObjectId(storeObjectId);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("nfsName", String.valueOf(summaryMap.get("name")));
        String fsname = "";
        if (fsIds.size() <= 0) {
            throw new DMEException("没有对应的文件系统");
        }
        for (int i = 0; i < fsIds.size(); i++) {
            String fileSystemId = fsIds.get(i);
            if (StringUtils.isEmpty(fileSystemId)) {
                continue;
            }
            String url =
                StringUtil.stringFormat(DmeConstants.DEFAULT_PATTERN, DmeConstants.DME_NFS_FILESERVICE_DETAIL_URL,
                    "file_system_id", fileSystemId);
            ResponseEntity<String> responseTuning = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseTuning.getStatusCodeValue() / 100 == 2) {
                JsonObject fsDetail = gson.fromJson(responseTuning.getBody(), JsonObject.class);
                resultMap.put("fsName", ToolUtils.jsonToStr(fsDetail.get("name")));
                fsname = ToolUtils.jsonToStr(fsDetail.get("name"));
                resultMap.put("fileSystemId", fileSystemId);
                JsonObject json = fsDetail.get("capacity_auto_negotiation").getAsJsonObject();
                resultMap.put("autoSizeEnable", ToolUtils.jsonToBoo(json.get("auto_size_enable")));
                boolean isThin =
                    ("thin".equalsIgnoreCase(ToolUtils.jsonToStr(fsDetail.get("alloc_type")))) ? true : false;
                resultMap.put("thin", isThin);

                JsonObject tuning = fsDetail.get("tuning").getAsJsonObject();

                resultMap.put("compressionEnabled", ToolUtils.jsonToBoo(tuning.get("compression_enabled")));
                resultMap.put("deduplicationEnabled", ToolUtils.jsonToBoo(tuning.get("deduplication_enabled")));
                String smartQos = ToolUtils.jsonToStr(tuning.get("smart_qos"));
                if (!StringUtils.isEmpty(smartQos)) {
                    resultMap.put("qosFlag", true);
                    JsonObject qosPolicy = new JsonParser().parse(smartQos).getAsJsonObject();
                    resultMap.put("maxBandwidth", ToolUtils.jsonToInt(qosPolicy.get("max_bandwidth")));
                    resultMap.put("maxIops", ToolUtils.jsonToInt(qosPolicy.get("max_iops")));
                    resultMap.put("latency", ToolUtils.jsonToInt(qosPolicy.get("latency")));
                    resultMap.put("minBandwidth", ToolUtils.jsonToInt(qosPolicy.get("min_bandwidth")));
                    resultMap.put("minIops", ToolUtils.jsonToInt(qosPolicy.get("min_iops")));
                } else {
                    resultMap.put("qosFlag", false);
                }
            }
            break;
        }

        //根据存储ID 获取逻nfs_share_id
        String nfsShareId = dmeVmwareRalationDao.getShareIdByStorageId(storeObjectId);
        if (null == nfsShareId) {
            throw new DMEException("没有对应的共享");
        }
        String url = StringUtil.stringFormat(DmeConstants.DEFAULT_PATTERN, DmeConstants.DME_NFS_SHARE_DETAIL_URL,
            "nfs_share_id", nfsShareId);
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        if (responseEntity.getStatusCodeValue() != HttpStatus.OK.value()) {
            LOG.error("获取 NFS Share 信息失败！返回信息：{}", responseEntity.getBody());
            return null;
        }
        String resBody = responseEntity.getBody();
        JsonObject share = gson.fromJson(resBody, JsonObject.class);
        resultMap.put("shareName", ToolUtils.jsonToStr(share.get("name")));
        if (String.valueOf(summaryMap.get("name")).equalsIgnoreCase(fsname) &&
            ("/" + summaryMap.get("name")).equalsIgnoreCase(ToolUtils.jsonToStr(share.get("name")))) {
            resultMap.put("sameName", true);
        } else {
            resultMap.put("sameName", false);
        }
        return resultMap;
    }

}
