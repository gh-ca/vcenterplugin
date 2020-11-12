package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.dao.DmeVmwareRalationDao;
import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.exception.DmeSqlException;
import com.dmeplugin.dmestore.model.*;
import com.dmeplugin.dmestore.services.bestpractice.DmeIndicatorConstants;
import com.dmeplugin.dmestore.utils.RestUtils;
import com.dmeplugin.dmestore.utils.ToolUtils;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.*;
import net.bytebuddy.implementation.bytecode.Throw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.util.*;

/**
 * @author lianq
 * @className DmeStorageServiceImpl
 * @description TODO
 * @date 2020/9/3 17:48
 */
public class DmeStorageServiceImpl implements DmeStorageService {

    private final String API_STORAGES = "/rest/storagemgmt/v1/storages";
    private final String API_FAILOVERGROUPS = "/rest/storagemgmt/v1/storage-port/failover-groups?storage_id=";
    private final String API_LOGICPORTS_LIST = "/rest/storagemgmt/v1/storage-port/logic-ports?storage_id=";
    private final String API_VOLUME_LIST = "/rest/blockservice/v1/volumes";
    private final String API_FILESYSTEMS_LIST = "/rest/fileservice/v1/filesystems/query";
    private final String API_DTREES_LIST = "/rest/fileservice/v1/dtrees/summary";
    private final String API_NFSSHARE_LIST = "/rest/fileservice/v1/nfs-shares/summary";
    private final String API_BANDPORTS_LIST = "/rest/storagemgmt/v1/storage-port/bond-ports?storage_id=";
    private final String API_INSTANCES_LIST = "/rest/resourcedb/v1/instances";
    private final String API_VOLUME_DETAIL = "/rest/blockservice/v1/volumes";
    private final String API_FILESYSTEM_DETAIL = "/rest/fileservice/v1/filesystems/";
    private final String DJTIERCONTAINSSTORAGEPOOL_URL = "/rest/resourcedb/v1/relations/M_DjTierContainsStoragePool/instances";
    private final String SYS_DJTIER_URL = "/rest/resourcedb/v1/instances/SYS_DjTier";


    private static final Logger LOG = LoggerFactory.getLogger(DmeStorageServiceImpl.class);

    private Gson gson = new Gson();

    private DmeAccessService dmeAccessService;

    private DmeVmwareRalationDao dmeVmwareRalationDao;

    private VCSDKUtils vcsdkUtils;

    private DataStoreStatisticHistoryService dataStoreStatisticHistoryService;

    public void setDataStoreStatisticHistoryService(DataStoreStatisticHistoryService dataStoreStatisticHistoryService) {
        this.dataStoreStatisticHistoryService = dataStoreStatisticHistoryService;
    }

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public DmeVmwareRalationDao getDmeVmwareRalationDao() {
        return dmeVmwareRalationDao;
    }

    public void setDmeVmwareRalationDao(DmeVmwareRalationDao dmeVmwareRalationDao) {
        this.dmeVmwareRalationDao = dmeVmwareRalationDao;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
    }

    public DmeAccessService getDmeAccessService() {
        return dmeAccessService;
    }

    public void setDmeAccessService(DmeAccessService dmeAccessService) {
        this.dmeAccessService = dmeAccessService;
    }

    @Override
    public List<Storage> getStorages() throws DMEException {

        List<Storage> list = new ArrayList<>();

        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(API_STORAGES, HttpMethod.GET, null);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DMEException("503", "list storage error !");
            }
            Object object = responseEntity.getBody();
            if (object != null) {
                JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
                JsonArray storage = jsonObject.get("datas").getAsJsonArray();
                for (JsonElement jsonElement : storage) {
                    JsonObject jsonObj = new JsonParser().parse(jsonElement.toString()).getAsJsonObject();
                    Storage storageObj = new Storage();
                    storageObj.setId(ToolUtils.jsonToStr(jsonObj.get("id")));
                    storageObj.setName(ToolUtils.jsonToStr(jsonObj.get("name")));
                    storageObj.setIp(ToolUtils.jsonToStr(jsonObj.get("ip")));
                    //运行状态取值包括“正常”、“离线”“故障”、“未管理”、“未知”。
                    storageObj.setStatus(ToolUtils.jsonToStr(jsonObj.get("status")));
                    //同步状态取值包括：“未同步”“同步中”“已同步”“未知”
                    storageObj.setSynStatus(ToolUtils.jsonToStr(jsonObj.get("syn_status")));
                    storageObj.setVendor(ToolUtils.jsonToStr(jsonObj.get("vendor")));
                    storageObj.setModel(ToolUtils.jsonToStr(jsonObj.get("model")));
                    storageObj.setUsedCapacity(ToolUtils.jsonToDou(jsonObj.get("used_capacity"), 0.0));
                    storageObj.setTotalCapacity(ToolUtils.jsonToDou(jsonObj.get("total_capacity"), 0.0));
                    storageObj.setTotalEffectiveCapacity(ToolUtils.jsonToDou(jsonObj.get("total_effective_capacity"), 0.0));
                    storageObj.setFreeEffectiveCapacity(ToolUtils.jsonToDou(jsonObj.get("free_effective_capacity"), 0.0));
                    storageObj.setMaxCpuUtilization(ToolUtils.jsonToDou(jsonObj.get("max_cpu_utilization"), 0.0));
                    storageObj.setMaxIops(ToolUtils.jsonToDou(jsonObj.get("max_iops"), 0.0));
                    storageObj.setMaxBandwidth(ToolUtils.jsonToDou(jsonObj.get("max_bandwidth"), 0.0));
                    storageObj.setMaxLatency(ToolUtils.jsonToDou(jsonObj.get("max_latency"), 0.0));
                    storageObj.setSn(ToolUtils.jsonToStr(jsonObj.get("sn")));
                    storageObj.setVersion(ToolUtils.jsonToStr(jsonObj.get("version")));
                    storageObj.setTotalPoolCapacity(ToolUtils.jsonToDou(jsonObj.get("total_pool_capacity"), 0.0));

                    storageObj.setLocation(ToolUtils.jsonToStr(jsonObj.get("location"), null));
                    storageObj.setPatchVersion(ToolUtils.jsonToStr(jsonObj.get("patch_version"), null));
                    storageObj.setMaintenanceStart(ToolUtils.jsonToDateStr(jsonObj.get("maintenance_start"), null));
                    storageObj.setMaintenanceOvertime(ToolUtils.jsonToDateStr(jsonObj.get("maintenance_overtime"), null));
                    storageObj.setSubscriptionCapacity(ToolUtils.jsonToDou(jsonObj.get("subscription_capacity")));

                    JsonElement jsonAzIds = jsonObj.get("az_ids");
                    if (!ToolUtils.jsonIsNull(jsonAzIds)) {
                        String azIds = ToolUtils.jsonToStr(jsonAzIds);
                        String[] azIdsArr = {azIds};
                        storageObj.setAzIds(azIdsArr);
                    } else {
                        String[] azIdsArr = {};
                        storageObj.setAzIds(azIdsArr);
                    }
                    list.add(storageObj);
                }
            }
        } catch (Exception e) {
            LOG.error("list storage error", e);
            String message = e.getMessage();
            throw new DMEException("503", message);
        }
        return list;
    }

    @Override
    public StorageDetail getStorageDetail(String storageId) throws DMEException {
        StorageDetail storageObj = null;
        String url = API_STORAGES + "/" + storageId + "/detail";
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DMEException("503", "search oriented storage error");
            }
            String object = responseEntity.getBody();
            if (object != null) {
                JsonObject element = new JsonParser().parse(object).getAsJsonObject();
                storageObj = new StorageDetail();
                storageObj.setId(ToolUtils.jsonToStr(element.get("id")));
                storageObj.setName(ToolUtils.jsonToStr(element.get("name")));
                storageObj.setIp(ToolUtils.jsonToStr(element.get("ip")));
                storageObj.setStatus(ToolUtils.jsonToStr(element.get("status")));
                storageObj.setSynStatus(ToolUtils.jsonToStr(element.get("syn_status")));
                storageObj.setVendor(ToolUtils.jsonToStr(element.get("vendor")));
                storageObj.setModel(ToolUtils.jsonToStr(element.get("model")));
                storageObj.setUsedCapacity(ToolUtils.jsonToDou(element.get("used_capacity"), 0.0) / 1024);
                storageObj.setTotalCapacity(ToolUtils.jsonToDou(element.get("total_capacity"), 0.0) / 1024);
                storageObj.setTotalEffectiveCapacity(ToolUtils.jsonToDou(element.get("total_effective_capacity"), 0.0));
                storageObj.setFreeEffectiveCapacity(ToolUtils.jsonToDou(element.get("free_effective_capacity"), 0.0));
                storageObj.setLocation(ToolUtils.jsonToStr(element.get("location"), null));
                storageObj.setPatchVersion(ToolUtils.jsonToStr(element.get("patch_version"), null));
                storageObj.setMaintenanceStart(ToolUtils.jsonToDateStr(element.get("maintenance_start"), null));
                storageObj.setMaintenanceOvertime(ToolUtils.jsonToDateStr(element.get("maintenance_overtime"), null));
                storageObj.setProductVersion(ToolUtils.jsonToStr(element.get("product_version")));
                storageObj.setSn(ToolUtils.jsonToStr(element.get("sn"), null));

                List<StoragePool> storagePools = getStoragePools(storageId, "all");
                Double totalPoolCapicity = 0.0;
                Double subscriptionCapacity = 0.0;
                Double protectionCapacity = 0.0;
                Double fileCapacity = 0.0;
                Double blockCapacity = 0.0;
                Double dedupedCapacity = 0.0;
                Double compressedCapacity = 0.0;
                for (StoragePool storagePool : storagePools) {
                    totalPoolCapicity += storagePool.getTotalCapacity();
                    subscriptionCapacity += storagePool.getSubscribedCapacity();
                    protectionCapacity += storagePool.getProtectionCapacity();
                    if ("file".equalsIgnoreCase(storagePool.getMediaType())) {
                        fileCapacity += storagePool.getConsumedCapacity();
                    }
                    if ("block".equalsIgnoreCase(storagePool.getMediaType())) {
                        blockCapacity += storagePool.getConsumedCapacity();
                    }
                    dedupedCapacity += storagePool.getDedupedCapacity();
                    compressedCapacity += storagePool.getCompressedCapacity();
                }
                storageObj.setTotalEffectiveCapacity(totalPoolCapicity);
                storageObj.setFreeEffectiveCapacity(totalPoolCapicity - fileCapacity - blockCapacity - protectionCapacity);
                storageObj.setSubscriptionCapacity(subscriptionCapacity);
                storageObj.setProtectionCapacity(protectionCapacity);
                storageObj.setFileCapacity(fileCapacity);
                storageObj.setBlockCapacity(blockCapacity);
                storageObj.setDedupedCapacity(dedupedCapacity);
                storageObj.setCompressedCapacity(compressedCapacity);
                storageObj.setOptimizeCapacity(storageObj.getUsedCapacity() - dedupedCapacity - compressedCapacity);
                JsonArray ids = element.get("az_ids").getAsJsonArray();
                if (ids.size() != 0) {
                    String[] azIdsArr = {ToolUtils.jsonToStr(ids)};
                    storageObj.setAzIds(azIdsArr);
                } else {
                    String[] azIdsArr = {};
                    storageObj.setAzIds(azIdsArr);
                }
            }
        } catch (Exception e) {
            LOG.error("search oriented storage error!", e);
            throw new DMEException("503", e.getMessage());
        }
        return storageObj;
    }

    @Override
    public List<StoragePool> getStoragePools(String storageId, String mediaType) throws DMEException {
        String replace = storageId.replace("-", "");
        String className = "SYS_StoragePool";
        List<StoragePool> resList = new ArrayList<>();
        String url = API_INSTANCES_LIST + "/" + className + "?condition={json}"+"&&pageSize=1000";
        String params = ToolUtils.getRequsetParams("storageDeviceId", replace);
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.accessByJson(url, HttpMethod.GET, params);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DMEException("503", "search oriented storage pool error");
            }
            String object = responseEntity.getBody();
            if (!StringUtils.isEmpty(object)) {
                //得到存储池与服务等级的关系
                Map<String, Object> djofspMap = getDjTierOfStoragePool();
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("objList").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    StoragePool storagePool = new StoragePool();
                    storagePool.setName(ToolUtils.jsonToStr(element.get("name")));
                    String poolId = ToolUtils.jsonToStr(element.get("poolId"));
                    storagePool.setId(poolId);
                    storagePool.setHealthStatus(ToolUtils.jsonToStr(element.get("status")));
                    storagePool.setRunningStatus(ToolUtils.jsonToStr(element.get("runningStatus")));
                    Double totalCapacity = ToolUtils.jsonToDou(element.get("totalCapacity"), 0.0);
                    storagePool.setTotalCapacity(totalCapacity);
                    Double consumedCapacity = ToolUtils.jsonToDou(element.get("usedCapacity"), 0.0);
                    storagePool.setConsumedCapacity(consumedCapacity);
                    Double dedupedCapacity = ToolUtils.jsonToDou(element.get("dedupedCapacity"), 0.0);
                    storagePool.setDedupedCapacity(dedupedCapacity);
                    Double compressedCapacity = ToolUtils.jsonToDou(element.get("compressedCapacity"), 0.0);
                    storagePool.setCompressedCapacity(compressedCapacity);
                    Double protectionCapacity = ToolUtils.jsonToDou(element.get("protectionCapacity"), 0.0);
                    storagePool.setProtectionCapacity(protectionCapacity);
                    storagePool.setStoragePoolId(ToolUtils.jsonToStr(element.get("storageDeviceId")));
                    String type = ToolUtils.jsonToStr(element.get("type"));
                    storagePool.setMediaType(type);
                    storagePool.setStorageId(ToolUtils.jsonToStr(element.get("storageDeviceId")));
                    storagePool.setTier0RaidLv(ToolUtils.jsonToStr(element.get("tier0RaidLv")));
                    storagePool.setTier1RaidLv(ToolUtils.jsonToStr(element.get("tier1RaidLv")));
                    storagePool.setTier2RaidLv(ToolUtils.jsonToStr(element.get("tier2RaidLv")));
                    storagePool.setConsumedCapacity(ToolUtils.jsonToDou(element.get("usedCapacity"), 0.0));
                    storagePool.setStorageInstanceId(ToolUtils.jsonToStr(element.get("resId")));

                    storagePool.setPoolId(ToolUtils.jsonToStr(element.get("poolId")));

                    String diskPoolId = ToolUtils.jsonToStr(element.get("diskPoolId"));
                    storagePool.setDiskPoolId(diskPoolId);
                    Double subscribedCapacity = ToolUtils.jsonToDou(element.get("subscribedCapacity"), 0.0);
                    storagePool.setSubscribedCapacity(subscribedCapacity);
                    //订阅率（lun/fs订阅率）
                    DecimalFormat df = new DecimalFormat("#.00");
                    Double subscribedCapacityRate = 0.0;
                    if (totalCapacity != 0) {
                        subscribedCapacityRate = Double.valueOf(df.format(subscribedCapacity / totalCapacity)) * 100;
                    }

                    Double freeCapacity = 0.0;
                    String consumedPercent = "0.0";
                    if (Double.max(totalCapacity, consumedCapacity) == totalCapacity) {
                        freeCapacity = totalCapacity - consumedCapacity;
                        consumedPercent = df.format(consumedCapacity / totalCapacity);
                    }

                    String diskType = getDiskType(storageId, diskPoolId, poolId);
                    storagePool.setPhysicalType(diskType);

                    storagePool.setConsumedCapacityPercentage(consumedPercent);
                    storagePool.setFreeCapacity(freeCapacity);
                    storagePool.setSubscriptionRate(subscribedCapacityRate);

                    String resId = ToolUtils.jsonToStr(element.get("resId"));
                    if (null != djofspMap && null != djofspMap.get(resId)) {
                        storagePool.setServiceLevelName(gson.toJson(djofspMap.get(resId)));
                    }

                    if (mediaType.equals(type)) {
                        resList.add(storagePool);
                    } else if ("all".equals(mediaType)) {
                        resList.add(storagePool);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("search oriented storage pool error", e);
            throw new DMEException("503", e.getMessage());
        }
        return resList;
    }

    @Override
    public List<LogicPorts> getLogicPorts(String storageId) throws DMEException {

        List<LogicPorts> resList = new ArrayList<>();
        String url = API_LOGICPORTS_LIST + storageId;
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DMEException("503", "list bandports error!");
            }
            String object = responseEntity.getBody();
            if (!StringUtils.isEmpty(object)) {
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("logic_ports").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    LogicPorts logicPorts = new LogicPorts();
                    logicPorts.setId(ToolUtils.jsonToStr(element.get("id")));
                    logicPorts.setName(ToolUtils.jsonToStr(element.get("name")));
                    logicPorts.setRunningStatus(ToolUtils.jsonToStr(element.get("running_status")));
                    logicPorts.setOperationalStatus(ToolUtils.jsonToStr(element.get("operational_status")));
                    logicPorts.setMgmtIp(ToolUtils.jsonToStr(element.get("mgmt_ip")));
                    logicPorts.setMgmtIpv6(ToolUtils.jsonToStr(element.get("mgmt_ipv6")));
                    logicPorts.setHomePortId(ToolUtils.jsonToStr(element.get("home_port_id")));
                    logicPorts.setHomePortName(ToolUtils.jsonToStr(element.get("home_port_name")));
                    logicPorts.setRole(ToolUtils.jsonToStr(element.get("role")));
                    logicPorts.setDdnsStatus(ToolUtils.jsonToStr(element.get("ddns_status")));
                    logicPorts.setCurrentPortId(ToolUtils.jsonToStr(element.get("current_port_id")));
                    logicPorts.setCurrentPortName(ToolUtils.jsonToStr(element.get("current_port_name")));
                    logicPorts.setSupportProtocol(ToolUtils.jsonToStr(element.get("support_protocol")));
                    logicPorts.setManagementAccess(ToolUtils.jsonToStr(element.get("management_access")));
                    logicPorts.setVstoreId(ToolUtils.jsonToStr(element.get("vstore_id")));
                    logicPorts.setVstoreName(ToolUtils.jsonToStr(element.get("vstore_name")));
                    resList.add(logicPorts);
                }
            }
        } catch (Exception e) {
            LOG.error("list bandports error", e);
            throw new DMEException("503", e.getMessage());
        }
        return resList;
    }


    @Override
    public VolumeListRestponse getVolumesByPage(String storageId, String pageSize, String pageNo) throws DMEException {
        
        VolumeListRestponse volumeListRestponse = new VolumeListRestponse();
        List<Volume> volumes = new ArrayList<>(10);
        String url = API_VOLUME_LIST + "?";
        if (!"".equals(storageId)) {
            url = url + "storage_id="+storageId + "&";
        }

        if(!StringUtils.isEmpty(pageSize)){
            url = url + "limit=" + pageSize + "&";
        }

        if(!StringUtils.isEmpty(pageNo)){
            url = url + "offset=" + pageNo;
        }

        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DMEException("503", "list volumes error!");
            }
            Object object = responseEntity.getBody();
            if (object != null) {
                JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
                int count = jsonObject.get("count").getAsInt();
                JsonArray jsonArray = jsonObject.get("volumes").getAsJsonArray();
                Map<String, String> poolnamecacheMap = new HashMap<>(16);
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    Volume volume = new Volume();
                    String volumeId = ToolUtils.jsonToStr(element.get("id"));
                    volume.setId(volumeId);
                    volume.setName(ToolUtils.jsonToStr(element.get("name")));
                    volume.setStatus(ToolUtils.jsonToStr(element.get("status")));
                    volume.setAttached(ToolUtils.jsonToBoo(element.get("attached")));
                    volume.setAlloctype(ToolUtils.jsonToStr(element.get("alloctype")));
                    volume.setServiceLevelName(ToolUtils.jsonToStr(element.get("service_level_name")));
                    volume.setStorageId(ToolUtils.jsonToStr(element.get("storage_id")));
                    String poolRawId = ToolUtils.jsonToStr(element.get("pool_raw_id"));
                    volume.setPoolRawId(poolRawId);
                    volume.setWwn(ToolUtils.jsonToStr(element.get("volume_wwn")));
                    volume.setCapacityUsage(ToolUtils.jsonToStr(element.get("capacity_usage")));
                    volume.setProtectionStatus(ToolUtils.jsonToBoo(element.get("protected")));
                    volume.setCapacity(ToolUtils.jsonToInt(element.get("capacity"), 0));
                    volume.setDatastores(getDataStoreOnVolume(volumeId));
                    if (null == poolnamecacheMap.get(poolRawId)) {
                        poolnamecacheMap.put(poolRawId, getStorageByPoolRawId(poolRawId));
                    }
                    volume.setStoragePoolName(poolnamecacheMap.get(poolRawId));
                    volumes.add(volume);
                }

                volumeListRestponse.setVolumeList(volumes);
                volumeListRestponse.setCount(count);
            }
        } catch (Exception e) {
            LOG.error("list volume error!", e);
            throw new DMEException("503", e.getMessage());
        }
        return volumeListRestponse;
    }

//    @Override
//    public List<Volume> getVolumes(String storageId) throws DMEException {
//        return getVolumesByPage(storageId, null, null).getVolumeList();
//    }

    @Override
    public List<FileSystem> getFileSystems(String storageId) throws DMEException {

        List<FileSystem> fileSystems = new ArrayList<>();

        Map<String, String> params = new HashMap<>();
        params.put("storage_id", storageId);
        String jsonParams = gson.toJson(params);

        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(API_FILESYSTEMS_LIST, HttpMethod.POST, jsonParams);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DMEException("503", "list filesystem error!");
            }
            String respObject = responseEntity.getBody();
            if (!StringUtils.isEmpty(respObject)) {
                JsonObject jsonObject = new JsonParser().parse(respObject).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    FileSystem fileSystem = new FileSystem();
                    fileSystem.setId(ToolUtils.jsonToStr(element.get("id")));
                    fileSystem.setName(ToolUtils.jsonToStr(element.get("name")));
                    fileSystem.setType(ToolUtils.jsonToStr(element.get("type")));
                    fileSystem.setHealthStatus(ToolUtils.jsonToStr(element.get("health_status")));
                    fileSystem.setAllocType(ToolUtils.jsonToStr(element.get("alloc_type")));
                    fileSystem.setCapacity(ToolUtils.jsonToDou(element.get("capacity"), 0.0));
                    fileSystem.setCapacityUsageRatio(ToolUtils.jsonToInt(element.get("capacity_usage_ratio"), 0));
                    fileSystem.setStoragePoolName(ToolUtils.jsonToStr(element.get("storage_pool_name")));
                    fileSystem.setNfsCount(ToolUtils.jsonToInt(element.get("nfs_count"), 0));
                    fileSystem.setCifsCount(ToolUtils.jsonToInt(element.get("cifs_count"), 0));
                    fileSystem.setDtreeCount(ToolUtils.jsonToInt(element.get("dtree_count"), 0));
                    fileSystem.setAvailableCapacity(ToolUtils.jsonToDou(element.get("available_capacity"), 0.0));
                    fileSystems.add(fileSystem);
                }
            }
        } catch (Exception e) {
            LOG.error("list filesystem error!", e);
            throw new DMEException("503", e.getMessage());
        }
        return fileSystems;
    }

    @Override
    public List<Dtrees> getDtrees(String storageId) throws DMEException {

        List<Dtrees> resList = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        params.put("storage_id", storageId);

        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(API_DTREES_LIST, HttpMethod.POST, gson.toJson(params));
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DMEException("503", "list dtree error!");
            }
            String object = responseEntity.getBody();
            if (!StringUtils.isEmpty(object)) {
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("dtrees").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    Dtrees dtrees = new Dtrees();
                    dtrees.setName(ToolUtils.jsonToStr(element.get("name")));
                    dtrees.setFsName(ToolUtils.jsonToStr(element.get("fs_name")));
                    dtrees.setQuotaSwitch(ToolUtils.jsonToBoo(element.get("quota_switch")));
                    dtrees.setSecurityStyle(ToolUtils.jsonToStr(element.get("security_style")));
                    dtrees.setTierName(ToolUtils.jsonToStr(element.get("tier_name")));
                    dtrees.setNfsCount(ToolUtils.jsonToInt(element.get("nfs_count"), 0));
                    dtrees.setCifsCount(ToolUtils.jsonToInt(element.get("cifs_count"), 0));
                    resList.add(dtrees);
                }
            }
        } catch (Exception e) {
            LOG.error("list dtree error!", e);
            throw new DMEException("503", e.getMessage());
        }
        return resList;
    }

    @Override
    public List<NfsShares> getNfsShares(String storageId) throws DMEException {

        List<NfsShares> resList = new ArrayList<>();

        Map<String, String> params = new HashMap<>();
        params.put("storage_id", storageId);
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(API_NFSSHARE_LIST, HttpMethod.POST, gson.toJson(params));
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DMEException("503", "list nfsshares error!");
            }
            String object = responseEntity.getBody();
            if (!StringUtils.isEmpty(object)) {
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("nfs_share_info_list").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    NfsShares nfsShares = new NfsShares();
                    nfsShares.setName(ToolUtils.jsonToStr(element.get("name")));
                    nfsShares.setSharePath(ToolUtils.jsonToStr(element.get("share_path")));
                    nfsShares.setStorageId(ToolUtils.jsonToStr(element.get("storage_id")));
                    nfsShares.setTierName(ToolUtils.jsonToStr(element.get("tier_name")));
                    nfsShares.setOwningDtreeName(ToolUtils.jsonToStr(element.get("owning_dtree_name")));
                    nfsShares.setOwningDtreeId(ToolUtils.jsonToStr(element.get("owning_dtree_id")));
                    nfsShares.setFsName(ToolUtils.jsonToStr(element.get("fs_name")));
                    resList.add(nfsShares);
                }
            }
        } catch (Exception e) {
            LOG.error("list nfsshares error!", e);
            throw new DMEException("503", e.getMessage());
        }
        return resList;
    }

    @Override
    public List<BandPorts> getBandPorts(String storageId) throws DMEException {

        List<BandPorts> resList = new ArrayList<>();
        String url = API_BANDPORTS_LIST + storageId;
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DMEException("503", "list bandports error!");
            }
            String object = responseEntity.getBody();
            if (!StringUtils.isEmpty(object)) {
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("bond_ports").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    BandPorts bandPorts = new BandPorts();
                    bandPorts.setId(ToolUtils.jsonToStr(element.get("id")));
                    bandPorts.setName(ToolUtils.jsonToStr(element.get("name")));
                    bandPorts.setHealthStatus(ToolUtils.jsonToStr(element.get("health_status")));
                    bandPorts.setRunningStatus(ToolUtils.jsonToStr(element.get("running_status")));
                    bandPorts.setMtu(ToolUtils.jsonToStr(element.get("mtu")));
                    resList.add(bandPorts);
                }
            }
        } catch (Exception e) {
            LOG.error("list bandports error!", e);
            throw new DMEException("503", e.getMessage());
        }
        return resList;
    }

    @Override
    public List<StorageControllers> getStorageControllers(String storageDeviceId) throws DMEException {
        String replace = storageDeviceId.replace("-", "");
        String className = "SYS_Controller";
        List<StorageControllers> resList = new ArrayList<>();
        String url = API_INSTANCES_LIST + "/" + className + "?condition={json}"+"&&pageSize=1000";
        String params = ToolUtils.getRequsetParams("storageDeviceId", replace);
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.accessByJson(url, HttpMethod.GET, params);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DMEException("503", "list storage controller error!");
            }
            Object object = responseEntity.getBody();
            if (!StringUtils.isEmpty(object)) {
                JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("objList").getAsJsonArray();

                List<String> resids = new ArrayList<>();

                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    String id = ToolUtils.jsonToStr(element.get("id"));
                    resids.add(id);
                }
                List<StorageControllers> storageControllersperf = listStorageControllerPerformance(resids);
                Map<String, StorageControllers> storageControllersMap = new HashMap<>();
                for (StorageControllers storageControllers : storageControllersperf) {
                    storageControllersMap.put(storageControllers.getId(), storageControllers);
                }
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    StorageControllers storageControllers = new StorageControllers();
                    storageControllers.setId(ToolUtils.jsonToStr(element.get("id")));
                    storageControllers.setName(ToolUtils.jsonToStr(element.get("name")));
                    storageControllers.setSoftVer(ToolUtils.jsonToStr(element.get("softVer")));
                    storageControllers.setStatus(ToolUtils.jsonToStr(element.get("status")));
                    storageControllers.setCpuInfo(ToolUtils.jsonToStr(element.get("cpuInfo")));
                    if (null != storageControllersMap.get(storageControllers.getId())) {
                        storageControllers.setLantency(storageControllersMap.get(storageControllers.getId()).getLantency());
                        storageControllers.setBandwith(storageControllersMap.get(storageControllers.getId()).getBandwith());
                        storageControllers.setIops(storageControllersMap.get(storageControllers.getId()).getIops());
                        storageControllers.setCpuUsage(storageControllersMap.get(storageControllers.getId()).getCpuUsage());
                    }
                    resList.add(storageControllers);
                }
            }
        } catch (Exception e) {
            LOG.error("list storage controller error!");
            throw new DMEException("503", e.getMessage());
        }
        return resList;
    }

    @Override
    public List<StorageDisk> getStorageDisks(String storageDeviceId) throws DMEException {
        String replace = storageDeviceId.replace("-", "");
        String className = "SYS_StorageDisk";
        List<StorageDisk> resList = new ArrayList<>();
        String url = API_INSTANCES_LIST + "/" + className + "?condition={json} "+"&&pageSize=1000";
        String params = ToolUtils.getRequsetParams("storageDeviceId", replace);
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.accessByJson(url, HttpMethod.GET, params);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DMEException("503", "list storage disk error!");
            }
            Object object = responseEntity.getBody();
            if (!StringUtils.isEmpty(object)) {
                JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("objList").getAsJsonArray();

                List<String> resids = new ArrayList<>();

                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    String id = ToolUtils.jsonToStr(element.get("id"));
                    resids.add(id);
                }
                List<StorageDisk> storageDiskperf = listStorageDiskPerformance(resids);
                Map<String, StorageDisk> storageDiskMap = new HashMap<>();
                for (StorageDisk storageDisk : storageDiskperf) {
                    storageDiskMap.put(storageDisk.getId(), storageDisk);
                }
                Map<String, String> storageDiskPool = new HashMap<>();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    StorageDisk storageDisk = new StorageDisk();
                    storageDisk.setId(ToolUtils.jsonToStr(element.get("id")));
                    storageDisk.setName(ToolUtils.jsonToStr(element.get("name")));
                    storageDisk.setStatus(ToolUtils.jsonToStr(element.get("status")));
                    storageDisk.setCapacity(ToolUtils.jsonToDou(element.get("capacity"), 0.0));
                    storageDisk.setSpeed(ToolUtils.jsonToLon(element.get("speed"), 0L));
                    storageDisk.setLogicalType(ToolUtils.jsonToStr(element.get("logicalType")));
                    storageDisk.setPhysicalType(ToolUtils.jsonToStr(element.get("physicalType")));
                    String poolId = ToolUtils.jsonToStr(element.get("poolId"));
                    storageDisk.setPoolId(poolId);
                    storageDisk.setStorageDeviceId(ToolUtils.jsonToStr(element.get("storageDeviceId")));
                    if (null==storageDiskPool.get(poolId)) {
                        storageDiskPool.put(poolId, getDiskPoolByPoolId(poolId).getName());
                        storageDisk.setDiskDomain(storageDiskPool.get(poolId));
                    }else {
                        storageDisk.setDiskDomain(storageDiskPool.get(poolId));
                    }
                    if (null != storageDiskMap.get(storageDisk.getId())) {
                        storageDisk.setLantency(storageDiskMap.get(storageDisk.getId()).getLantency());
                        storageDisk.setBandwith(storageDiskMap.get(storageDisk.getId()).getBandwith());
                        storageDisk.setIops(storageDiskMap.get(storageDisk.getId()).getIops());
                        storageDisk.setUseage(storageDiskMap.get(storageDisk.getId()).getUseage());
                    }
                    resList.add(storageDisk);
                }
            }
        } catch (Exception e) {
            LOG.error("list storage disk error!", e);
            throw new DMEException("503", e.getMessage());
        }
        return resList;
    }

    @Override
    public List<EthPortInfo> getStorageEthPorts(String storageSn) throws DMEException {
        List<EthPortInfo> relists = null;
        try {
            if (!StringUtils.isEmpty(storageSn)) {
                //通过存储设备的sn查询 存储设备的资源ID
                String dsResId = getStorageResIdBySn(storageSn);
                if (!StringUtils.isEmpty(dsResId)) {
                    relists = getEthPortsByResId(dsResId);
                } else {
                    throw new Exception("get Storage ResId By Sn error:resId is null");
                }
            }
        } catch (Exception e) {
            LOG.error("get Storage Eth Ports error:", e);
            throw new DMEException(e.getMessage());
        }
        return relists;
    }

    //通过存储设备的sn查询 存储设备的资源ID
    public String getStorageResIdBySn(String storageSn) {
        String dsResId = null;
        try {
            if (!StringUtils.isEmpty(storageSn)) {
                String stordeviceIdUrl = DmeConstants.DME_RES_STORDEVICEID_QUERY_URL;
                String params = ToolUtils.getRequsetParams("sn", storageSn);
                stordeviceIdUrl = stordeviceIdUrl + "?condition={json}";
                try {
                    ResponseEntity responseEntity = dmeAccessService.accessByJson(stordeviceIdUrl, HttpMethod.GET, params);
                    if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
                        JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                        if (jsonObject != null && jsonObject.get("objList") != null) {
                            JsonArray objList = jsonObject.getAsJsonArray("objList");
                            if (objList != null && objList.size() > 0) {
                                JsonObject vjson = objList.get(0).getAsJsonObject();
                                if (vjson != null) {
                                    dsResId = ToolUtils.jsonToStr(vjson.get("id"));
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    LOG.error("DME link error url:" + stordeviceIdUrl + ",error:" + e.toString());
                }
            }
        } catch (Exception e) {
            LOG.error("get Storage ResId By Sn error:", e);
            throw e;
        }
        LOG.info("getStorageResIdBySn dsResId===" + dsResId);
        return dsResId;
    }

    //通过资源管理API查询Eth接口
    public List<EthPortInfo> getEthPortsByResId(String dsResId) {
        List<EthPortInfo> relists = null;
        try {
            if (!StringUtils.isEmpty(dsResId)) {
                String ethPortUrl = DmeConstants.DME_STORDEVICE_ETHPORT_QUERY_URL;
                JsonObject condition = new JsonObject();

                JsonArray constraint = new JsonArray();

                JsonObject consObj = new JsonObject();
                JsonObject simple = new JsonObject();
                simple.addProperty("name", "dataStatus");
                simple.addProperty("operator", "equal");
                simple.addProperty("value", "normal");
                consObj.add("simple", simple);
                constraint.add(consObj);
                JsonObject consObj1 = new JsonObject();
                JsonObject simple1 = new JsonObject();
                simple1.addProperty("name", "portType");
                simple1.addProperty("operator", "equal");
                simple1.addProperty("value", "ETH");
                consObj1.add("simple", simple1);
                consObj1.addProperty("logOp", "and");
                constraint.add(consObj1);
                JsonObject consObj2 = new JsonObject();
                JsonObject simple2 = new JsonObject();
                simple2.addProperty("name", "storageDeviceId");
                simple2.addProperty("operator", "equal");
                simple2.addProperty("value", dsResId);
                consObj2.add("simple", simple2);
                consObj2.addProperty("logOp", "and");
                constraint.add(consObj2);
                condition.add("constraint", constraint);
                ethPortUrl = ethPortUrl + "?condition={json}";
                try {
                    ResponseEntity responseEntity = dmeAccessService.accessByJson(ethPortUrl, HttpMethod.GET, condition.toString());
                    if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
                        JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                        if (jsonObject != null && jsonObject.get("objList") != null) {
                            JsonArray objList = jsonObject.getAsJsonArray("objList");
                            if (objList != null && objList.size() > 0) {
                                relists = new ArrayList<>();
                                for (int i = 0; i < objList.size(); i++) {
                                    JsonObject vjson = objList.get(i).getAsJsonObject();
                                    if (vjson != null) {
                                        EthPortInfo ethPort = new EthPortInfo();
                                        ethPort.setOwnerType(ToolUtils.jsonToStr(vjson.get("ownerType")));
                                        ethPort.setIpv4Mask(ToolUtils.jsonToStr(vjson.get("ipv4Mask")));
                                        ethPort.setLogicalType(ToolUtils.jsonToStr(vjson.get("logicalType")));
                                        ethPort.setStorageDeviceId(ToolUtils.jsonToStr(vjson.get("storageDeviceId")));
                                        ethPort.setPortName(ToolUtils.jsonToStr(vjson.get("portName")));
                                        ethPort.setOwnerId(ToolUtils.jsonToStr(vjson.get("ownerId")));
                                        ethPort.setPortId(ToolUtils.jsonToStr(vjson.get("portId")));
                                        ethPort.setBondName(ToolUtils.jsonToStr(vjson.get("bondName")));
                                        ethPort.setMac(ToolUtils.jsonToStr(vjson.get("mac")));
                                        ethPort.setMgmtIpv6(ToolUtils.jsonToStr(vjson.get("mgmtIpv6")));
                                        ethPort.setIscsiName(ToolUtils.jsonToStr(vjson.get("iscsiName")));
                                        ethPort.setOwnerName(ToolUtils.jsonToStr(vjson.get("ownerName")));
                                        ethPort.setLastMonitorTime(ToolUtils.jsonToLon(vjson.get("lastMonitorTime"), 0L));
                                        ethPort.setMgmtIp(ToolUtils.jsonToStr(vjson.get("mgmtIp")));
                                        ethPort.setConfirmStatus(ToolUtils.jsonToStr(vjson.get("confirmStatus")));
                                        ethPort.setId(ToolUtils.jsonToStr(vjson.get("id")));
                                        ethPort.setLastModified(ToolUtils.jsonToLon(vjson.get("last_Modified"), 0L));
                                        ethPort.setConnectStatus(ToolUtils.jsonToStr(vjson.get("connectStatus")));
                                        ethPort.setClassId(ToolUtils.jsonToInt(vjson.get("classId"), 0));
                                        ethPort.setDataStatus(ToolUtils.jsonToStr(vjson.get("dataStatus")));
                                        ethPort.setMaxSpeed(ToolUtils.jsonToInt(vjson.get("maxSpeed"), 0));
                                        ethPort.setResId(ToolUtils.jsonToStr(vjson.get("resId")));
                                        ethPort.setLocal(ToolUtils.jsonToBoo(vjson.get("isLocal")));
                                        ethPort.setPortType(ToolUtils.jsonToStr(vjson.get("portType")));
                                        ethPort.setClassName(ToolUtils.jsonToStr(vjson.get("className")));
                                        ethPort.setNumberOfInitiators(ToolUtils.jsonToInt(vjson.get("numberOfInitiators"), 0));
                                        ethPort.setBondId(ToolUtils.jsonToStr(vjson.get("bondId")));
                                        ethPort.setRegionId(ToolUtils.jsonToStr(vjson.get("regionId")));
                                        ethPort.setName(ToolUtils.jsonToStr(vjson.get("name")));
                                        ethPort.setLocation(ToolUtils.jsonToStr(vjson.get("location")));
                                        ethPort.setNativeId(ToolUtils.jsonToStr(vjson.get("nativeId")));
                                        ethPort.setDataSource(ToolUtils.jsonToStr(vjson.get("dataSource")));
                                        ethPort.setIpv6Mask(ToolUtils.jsonToStr(vjson.get("ipv6Mask")));
                                        ethPort.setStatus(ToolUtils.jsonToStr(vjson.get("status")));
                                        ethPort.setSpeed(ToolUtils.jsonToInt(vjson.get("speed"), 0));
                                        ethPort.setWwn(ToolUtils.jsonToStr(vjson.get("wwn")));
                                        ethPort.setSfpStatus(ToolUtils.jsonToStr(vjson.get("sfpStatus")));

                                        relists.add(ethPort);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    LOG.error("DME link error url:" + ethPortUrl + ",error:" + e.toString());
                }
            }
        } catch (Exception e) {
            LOG.error("get EthPorts By ResId error:", e);
            throw e;
        }
        return relists;
    }

    @Override
    public Map<String, Object> getVolume(String volumeId) throws DMEException {
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", HttpStatus.OK.value());
        resMap.put("msg", "get volume success!");
        resMap.put("volume", volumeId);
        String url = API_VOLUME_DETAIL + "/" + volumeId;

        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DMEException(String.valueOf(code), "get volume error!");
            }
            Object object = responseEntity.getBody();
            if (object != null) {
                JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
                JsonObject element = jsonObject.get("volume").getAsJsonObject();
                Volume volume = new Volume();
                volume.setId(ToolUtils.jsonToStr(element.get("id")));
                volume.setName(ToolUtils.jsonToStr(element.get("name")));
                volume.setStatus(ToolUtils.jsonToStr(element.get("status")));
                volume.setAttached(ToolUtils.jsonToBoo(element.get("attached")));
                volume.setAlloctype(ToolUtils.jsonToStr(element.get("alloctype")));
                volume.setServiceLevelName(ToolUtils.jsonToStr(element.get("service_level_name")));
                volume.setStorageId(ToolUtils.jsonToStr(element.get("storage_id")));
                volume.setPoolRawId(ToolUtils.jsonToStr(element.get("pool_raw_id")));
                volume.setCapacityUsage(ToolUtils.jsonToStr(element.get("capacity_usage")));
                volume.setProtectionStatus(ToolUtils.jsonToBoo(element.get("protectionStatus")));
                JsonArray jsonArray = element.get("attachments").getAsJsonArray();
                volumeAttachments(jsonArray, volume);
                resMap.put("data", volume);
            }
        } catch (DMEException e) {
            LOG.error("get volume error!");
            throw new DMEException("503",e.getMessage());
        }
        return resMap;
    }

    @Override
    public List<StoragePort> getStoragePort(String storageDeviceId, String portType) throws DMEException {

        String replace = storageDeviceId.replace("-", "");
        if (StringUtils.isEmpty(storageDeviceId) || StringUtils.isEmpty(portType)) {
            throw new DMEException("403", "request storageDeviceId or portType error!");
        }
        List<StoragePort> storagePorts = new ArrayList<>(10);
        String className = "SYS_StoragePort";
        String url = API_INSTANCES_LIST + "/" + className + "?condition={json}" + "&&pageSize=1000";
        String params = ToolUtils.getRequsetParams("storageDeviceId", replace);
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.accessByJson(url, HttpMethod.GET, params);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DMEException("503", "get storage port failed!");
            }
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("objList").getAsJsonArray();
            List<String> resids = new ArrayList<>();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                String id = ToolUtils.jsonToStr(element.get("id"));
                resids.add(id);
            }
            List<StoragePort> storagePortperf = listStoragePortPerformance(resids);
            Map<String, StoragePort> storagePortMap = new HashMap<>();
            for (StoragePort storagePort : storagePortperf) {
                storagePortMap.put(storagePort.getId(), storagePort);
            }

            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                StoragePort storagePort = new StoragePort();
                storagePort.setId(ToolUtils.jsonToStr(element.get("id")));
                storagePort.setNativeId(ToolUtils.jsonToStr(element.get("nativeId")));
                storagePort.setLast_Modified(ToolUtils.jsonToLon(element.get("last_Modified"), 0L));
                storagePort.setLastMonitorTime(ToolUtils.jsonToLon(element.get("lastMonitorTime"), 0L));
                storagePort.setDataStatus(ToolUtils.jsonToStr(element.get("dataStatus")));
                storagePort.setName(ToolUtils.jsonToStr(element.get("name")));
                storagePort.setPortId(ToolUtils.jsonToStr(element.get("portId")));
                storagePort.setPortName(ToolUtils.jsonToStr(element.get("portName")));
                storagePort.setLocation(ToolUtils.jsonToStr(element.get("location")));
                storagePort.setConnectStatus(ToolUtils.jsonToStr(element.get("connectStatus")));
                storagePort.setStatus(ToolUtils.jsonToStr(element.get("status")));
                String type = ToolUtils.jsonToStr(element.get("portType"));
                storagePort.setPortType(type);
                storagePort.setMac(ToolUtils.jsonToStr(element.get("mac")));
                storagePort.setMgmtIp(ToolUtils.jsonToStr(element.get("mgmtIp")));
                storagePort.setIpv4Mask(ToolUtils.jsonToStr(element.get("ipv4Mask")));
                storagePort.setMgmtIpv6(ToolUtils.jsonToStr(element.get("mgmtIpv6")));
                storagePort.setIpv6Mask(ToolUtils.jsonToStr(element.get("ipv6Mask")));
                storagePort.setIscsiName(ToolUtils.jsonToStr(element.get("iscsiName")));
                storagePort.setBondId(ToolUtils.jsonToStr(element.get("bondId")));
                storagePort.setBondName(ToolUtils.jsonToStr(element.get("bondName")));
                storagePort.setWwn(ToolUtils.jsonToStr(element.get("wwn")));
                storagePort.setSfpStatus(ToolUtils.jsonToStr(element.get("sfpStatus")));
                storagePort.setLogicalType(ToolUtils.jsonToStr(element.get("logicalType")));
                storagePort.setNumOfInitiators(ToolUtils.jsonToInt(element.get("numOfInitiators")));
                storagePort.setSpeed(ToolUtils.jsonToInt(element.get("speed")));
                storagePort.setMaxSpeed(ToolUtils.jsonToInt(element.get("maxSpeed")));
                storagePort.setStorageDeviceId(ToolUtils.jsonToStr(element.get("storageDeviceId")));
                if (null != storagePortMap.get(storagePort.getId())) {
                    storagePort.setLantency(storagePortMap.get(storagePort.getId()).getLantency());
                    storagePort.setBandwith(storagePortMap.get(storagePort.getId()).getBandwith());
                    storagePort.setIops(storagePortMap.get(storagePort.getId()).getIops());
                    storagePort.setUseage(storagePortMap.get(storagePort.getId()).getUseage());
                }
                if (portType.equals(type)) {
                    storagePorts.add(storagePort);
                } else if ("ALL".equals(portType)) {
                    storagePorts.add(storagePort);
                }
            }
        } catch (Exception e) {
            LOG.error("list storage port error!");
            throw new DMEException("503", e.getMessage());
        }

        return storagePorts;
    }

    @Override
    public List<FailoverGroup> getFailoverGroups(String storageId) throws DMEException {

        if (StringUtils.isEmpty(storageId)) {
            throw new DMEException("403", "request param storage_id error!");
        }
        List<FailoverGroup> failoverGroups = new ArrayList<>(10);
        String url = API_FAILOVERGROUPS + storageId;
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DMEException("503", "list failover group failed!");
            }
            String body = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("failover_groups").getAsJsonArray();

            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                FailoverGroup failoverGroup = new FailoverGroup();
                failoverGroup.setFailoverGroupType(ToolUtils.jsonToStr(element.get("failover_group_type")));
                failoverGroup.setId(ToolUtils.jsonToStr(element.get("id")));
                failoverGroup.setName(ToolUtils.jsonToStr(element.get("name")));
                failoverGroups.add(failoverGroup);
            }
        } catch (DMEException e) {
            LOG.error("list failover group error!");
            throw new DMEException("503", e.getMessage());
        }
        return failoverGroups;
    }

    @Override
    public FileSystemDetail getFileSystemDetail(String fileSystemId) throws DMEException {


        if (StringUtils.isEmpty(fileSystemId)) {
            throw new DMEException("403", "param error!");
        }
        FileSystemDetail fileSystemDetail = new FileSystemDetail();
        String url = API_FILESYSTEM_DETAIL + fileSystemId;
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DMEException("503", "get file system detail error!");
            }

            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            fileSystemDetail.setId(ToolUtils.jsonToStr(jsonObject.get("id")));
            fileSystemDetail.setName(ToolUtils.jsonToStr(jsonObject.get("name")));
            CapacityAutonegotiation capacityAutonegotiation = new CapacityAutonegotiation();
            JsonObject json = jsonObject.get("capacity_auto_negotiation").getAsJsonObject();
            capacityAutonegotiation.setAutoSizeEnable(ToolUtils.jsonToBoo(json.get("auto_size_enable")));
            fileSystemDetail.setCapacityAutonegotiation(capacityAutonegotiation);
            JsonObject tuning = jsonObject.get("tuning").getAsJsonObject();
            FileSystemTurning fileSystemTurning = new FileSystemTurning();
            fileSystemTurning.setAllocationType(ToolUtils.jsonToStr(jsonObject.get("alloc_type")));
            fileSystemTurning.setCompressionEnabled(ToolUtils.jsonToBoo(tuning.get("compression_enabled")));
            fileSystemTurning.setDeduplicationEnabled(ToolUtils.jsonToBoo(tuning.get("deduplication_enabled")));
            SmartQos smartQos = new SmartQos();
            String smartQosStr = ToolUtils.jsonToStr(tuning.get("smart_qos"));
            if (!StringUtils.isEmpty(smartQos)) {
                JsonObject qosPolicy = new JsonParser().parse(smartQosStr).getAsJsonObject();
                smartQos.setMaxbandwidth(ToolUtils.jsonToInt(qosPolicy.get("max_bandwidth")));
                smartQos.setMaxiops(ToolUtils.jsonToInt(qosPolicy.get("max_iops")));
                smartQos.setLatency(ToolUtils.jsonToInt(qosPolicy.get("latency")));
                smartQos.setMinbandwidth(ToolUtils.jsonToInt(qosPolicy.get("min_bandwidth")));
                smartQos.setMiniops(ToolUtils.jsonToInt(qosPolicy.get("min_iops")));
            }
            fileSystemTurning.setSmartQos(smartQos);
            fileSystemDetail.setFileSystemTurning(fileSystemTurning);
        } catch (DMEException e) {
            LOG.error("get file system detail error");
            throw new DMEException("503", e.getMessage());
        }
        return fileSystemDetail;
    }

    private String getDataStoreOnVolume(String volumeId) throws DmeSqlException {
        return dmeVmwareRalationDao.getVmfsNameByVolumeId(volumeId);
    }

    private String getDiskType(String storageDeviceId, String diskPoolId, String poolId) throws DMEException {

        String replace = storageDeviceId.replace("-", "");
        String result = "";
        String className = "SYS_StorageDisk";
        String url = API_INSTANCES_LIST + "/" + className + "?condition={json}" + "&&pageSize=1000";
        String params = ToolUtils.getRequsetParams("storageDeviceId", replace);
        ResponseEntity<String> responseEntity = dmeAccessService.accessByJson(url, HttpMethod.GET, params);
        int code = responseEntity.getStatusCodeValue();
        if (code == HttpStatus.OK.value()) {
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("objList").getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                String diskId = ToolUtils.jsonToStr(element.get("diskId"));
                if (diskPoolId.equals(diskId)) {
                    result = ToolUtils.jsonToStr(element.get("physicalType"));
                    break;
                }
            }
        }
        return result;
    }

    //从卷信息中查关联的主机和主机组
    private void volumeAttachments(JsonArray array, Volume volume) {
        if (null != array && array.size() > 0) {
            List<String> hostIds = new ArrayList<>();
            List<String> hostGroupIds = new ArrayList<>();
            for (JsonElement element : array) {
                JsonObject json = element.getAsJsonObject();
                String hostId = ToolUtils.jsonToStr(json.get("host_id"));
                String hostGroupId = ToolUtils.jsonToStr(json.get("attached_host_group"));
                hostIds.add(hostId);
                hostGroupIds.add(hostGroupId);
            }
            volume.setHostIds(hostIds);
            volume.setHostGroupIds(hostGroupIds);
        }
    }

    @Override
    public List<Storage> listStoragePerformance(List<String> storageIds) throws DMEException {
        List<Storage> relists = new ArrayList<>();
        try {
            if (storageIds != null && storageIds.size() > 0) {
                Map<String, Object> params = new HashMap<>(16);
                params.put("obj_ids", storageIds);
                Map<String, Object> remap = dataStoreStatisticHistoryService.queryCurrentStatistic(DmeIndicatorConstants.RESOURCE_TYPE_NAME_STORAGEDEVICE, params);
                if (null != remap && remap.size() > 0) {
                    try {
                        JsonObject dataJson = new JsonParser().parse(remap.toString()).getAsJsonObject();
                        if (dataJson != null) {
                            relists = new ArrayList<>();
                            for (String storageId : storageIds) {
                                JsonObject statisticObject = dataJson.getAsJsonObject(storageId);
                                if (statisticObject != null) {
                                    Storage storage = new Storage();
                                    storage.setId(storageId);

                                    storage.setMaxBandwidth(ToolUtils.jsonToDou(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORDEVICE_BANDWIDTH)));
                                    storage.setMaxCpuUtilization(ToolUtils.jsonToDou(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORDEVICE_CPUUSAGE)));
                                    storage.setMaxLatency(ToolUtils.jsonToDou(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORDEVICE_RESPONSETIME)));
                                    storage.setMaxOps(ToolUtils.jsonToDou(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORDEVICE_THROUGHPUT)));
                                    relists.add(storage);
                                }
                            }
                        }
                    } catch (Exception e) {
                        LOG.warn("查询Storage实时性能数据listStoragePerformance异常", e);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("list Storage performance error:", e);
            throw new DMEException(e.getMessage());
        }
        LOG.info("listStoragePerformance relists===" + (relists == null ? "null" : relists.size()));
        return relists;
    }

    @Override
    public List<StoragePool> listStoragePoolPerformance(List<String> storagePoolIds) throws DMEException {
        List<StoragePool> relists = new ArrayList<>();
        try {
            if (storagePoolIds != null && storagePoolIds.size() > 0) {
                Map<String, Object> params = new HashMap<>(16);
                params.put("obj_ids", storagePoolIds);
                Map<String, Object> remap = dataStoreStatisticHistoryService.queryCurrentStatistic(DmeIndicatorConstants.RESOURCE_TYPE_NAME_STORAGEPOOL, params);
                if (null != remap && remap.size() > 0) {
                    try {
                        JsonObject dataJson = new JsonParser().parse(remap.toString()).getAsJsonObject();
                        if (dataJson != null) {
                            relists = new ArrayList<>();
                            for (String storagePoolId : storagePoolIds) {
                                JsonObject statisticObject = dataJson.getAsJsonObject(storagePoolId);
                                if (statisticObject != null) {
                                    StoragePool sp = new StoragePool();
                                    sp.setId(storagePoolId);
                                    sp.setMaxIops(ToolUtils.jsonToFloat(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORAGEPOOL_THROUGHPUT)));
                                    sp.setMaxBandwidth(ToolUtils.jsonToFloat(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORAGEPOOL_BANDWIDTH)));
                                    sp.setMaxLatency(ToolUtils.jsonToFloat(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORAGEPOOL_RESPONSETIME)));
                                    relists.add(sp);
                                }
                            }
                        }
                    } catch (Exception e) {
                        LOG.warn("查询StoragePool实时性能数据listStoragePoolPerformance异常", e);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("list StoragePool performance error:", e);
            throw new DMEException(e.getMessage());
        }
        LOG.info("listStoragePoolPerformance relists===" + (relists == null ? "null" : relists.size()));
        return relists;
    }

    @Override
    public List<StorageControllers> listStorageControllerPerformance(List<String> storageControllerIds) throws DMEException {
        List<StorageControllers> relists = new ArrayList<>();
        try {
            if (storageControllerIds != null && storageControllerIds.size() > 0) {
                Map<String, Object> params = new HashMap<>(16);
                params.put("obj_ids", storageControllerIds);
                Map<String, Object> remap = dataStoreStatisticHistoryService.queryCurrentStatistic(DmeIndicatorConstants.RESOURCE_TYPE_NAME_CONTROLLER, params);
                if (null != remap && remap.size() > 0) {
                    try {
                        JsonObject dataJson = new JsonParser().parse(remap.toString()).getAsJsonObject();
                        if (dataJson != null) {
                            relists = new ArrayList<>();
                            for (String storagecontrollerid : storageControllerIds) {
                                JsonObject statisticObject = dataJson.getAsJsonObject(storagecontrollerid);
                                if (statisticObject != null) {
                                    StorageControllers sp = new StorageControllers();
                                    sp.setId(storagecontrollerid);
                                    sp.setIops(ToolUtils.jsonToFloat(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_THROUGHPUT)));
                                    sp.setBandwith(ToolUtils.jsonToFloat(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_BANDWIDTH)));
                                    sp.setLantency(ToolUtils.jsonToFloat(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_RESPONSETIME)));
                                    sp.setCpuUsage(ToolUtils.jsonToFloat(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_CONTROLLER_CPUUSAGE)));
                                    relists.add(sp);
                                }
                            }
                        }
                    } catch (Exception e) {
                        LOG.warn("查询StorageController实时性能数据listStorageControllerPerformance异常", e);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("list StorageController performance error:", e);
            throw new DMEException(e.getMessage());
        }
        LOG.info("listStorageControllerPerformance relists===" + (relists == null ? "null" : relists.size()));
        return relists;
    }

    @Override
    public List<StorageDisk> listStorageDiskPerformance(List<String> storageDiskIds) throws DMEException {
        List<StorageDisk> relists = new ArrayList<>();
        try {
            if (storageDiskIds != null && storageDiskIds.size() > 0) {
                Map<String, Object> params = new HashMap<>(16);
                params.put("obj_ids", storageDiskIds);
                Map<String, Object> remap = dataStoreStatisticHistoryService.queryCurrentStatistic(DmeIndicatorConstants.RESOURCE_TYPE_NAME_STORAGEDISK, params);
                if (null != remap && remap.size() > 0) {
                    try {
                        JsonObject dataJson = new JsonParser().parse(remap.toString()).getAsJsonObject();
                        if (dataJson != null) {
                            relists = new ArrayList<>();
                            for (String storageDiskId : storageDiskIds) {
                                JsonObject statisticObject = dataJson.getAsJsonObject(storageDiskId);
                                if (statisticObject != null) {
                                    StorageDisk sp = new StorageDisk();
                                    sp.setId(storageDiskId);
                                    sp.setIops(ToolUtils.jsonToFloat(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORAGEDISK_READTHROUGHPUT)));
                                    sp.setBandwith(ToolUtils.jsonToFloat(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORAGEDISK_BANDWIDTH)));
                                    sp.setLantency(ToolUtils.jsonToFloat(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORAGEDISK_RESPONSETIME)));
                                    sp.setUseage(ToolUtils.jsonToFloat(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORAGEDISK_UTILITY)));
                                    relists.add(sp);
                                }
                            }
                        }
                    } catch (Exception e) {
                        LOG.warn("查询StorageDisk实时性能数据listStorageDiskPerformance异常", e);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("list StorageDisk performance error:", e);
            throw new DMEException(e.getMessage());
        }
        LOG.info("listStorageDiskPerformance relists===" + (relists == null ? "null" : relists.size()));
        return relists;
    }

    @Override
    public List<StoragePort> listStoragePortPerformance(List<String> storagePortIds) throws DMEException {
        List<StoragePort> relists = new ArrayList<>();
        try {
            if (storagePortIds != null && storagePortIds.size() > 0) {
                Map<String, Object> params = new HashMap<>(16);
                params.put("obj_ids", storagePortIds);
                Map<String, Object> remap = dataStoreStatisticHistoryService.queryCurrentStatistic(DmeIndicatorConstants.RESOURCE_TYPE_NAME_STORAGEPORT, params);
                if (null != remap && remap.size() > 0) {
                    try {
                        JsonObject dataJson = new JsonParser().parse(remap.toString()).getAsJsonObject();
                        if (dataJson != null) {
                            relists = new ArrayList<>();
                            for (String storagePortId : storagePortIds) {
                                JsonObject statisticObject = dataJson.getAsJsonObject(storagePortId);
                                if (statisticObject != null) {
                                    StoragePort sp = new StoragePort();
                                    sp.setId(storagePortId);
                                    sp.setIops(ToolUtils.jsonToFloat(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_THROUGHPUT)));
                                    sp.setBandwith(ToolUtils.jsonToFloat(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_BANDWIDTH)));
                                    sp.setLantency(ToolUtils.jsonToFloat(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_RESPONSETIME)));
                                    sp.setUseage(ToolUtils.jsonToFloat(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_STORAGEPORT_UTILITY)));
                                    relists.add(sp);
                                }
                            }
                        }
                    } catch (Exception e) {
                        LOG.warn("查询StorageDisk实时性能数据listStoragePortPerformance异常", e);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("list StoragePort performance error:", e);
            throw new DMEException(e.getMessage());
        }
        LOG.info("listStoragePortPerformance relists===" + (relists == null ? "null" : (relists.size())));
        return relists;
    }

    @Override
    public List<Volume> listVolumesPerformance(List<String> volumeWwns) throws DMEException {
        List<Volume> relists = new ArrayList<>();
        try {
            if (volumeWwns != null && volumeWwns.size() > 0) {
                Map<String, Object> params = new HashMap<>(16);
                params.put("obj_ids", volumeWwns);
                Map<String, Object> remap = dataStoreStatisticHistoryService.queryCurrentStatistic(DmeIndicatorConstants.RESOURCE_TYPE_NAME_LUN, params);
                if (null != remap && remap.size() > 0) {
                    try {
                        JsonObject dataJson = new JsonParser().parse(remap.toString()).getAsJsonObject();
                        if (dataJson != null) {
                            relists = new ArrayList<>();
                            for (String wwnid : volumeWwns) {
                                JsonObject statisticObject = dataJson.getAsJsonObject(wwnid);
                                if (statisticObject != null) {
                                    Volume sp = new Volume();
                                    sp.setWwn(wwnid);
                                    sp.setIops(ToolUtils.jsonToFloat(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_VOLUME_THROUGHPUT)));
                                    sp.setBandwith(ToolUtils.jsonToFloat(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_VOLUME_BANDWIDTH)));
                                    sp.setLantency(ToolUtils.jsonToFloat(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_VOLUME_RESPONSETIME)));
                                    relists.add(sp);
                                }
                            }
                        }
                    } catch (Exception e) {
                        LOG.warn("查询volume实时性能数据listVolumesPerformance异常", e);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("list volume performance error:", e);
            throw new DMEException(e.getMessage());
        }
        LOG.info("listVolumesPerformance relists===" + (relists == null ? "null" : (relists.size())));
        return relists;
    }

    @Override
    public Boolean queryVolumeByName(String name) throws DMEException {

        Boolean flag = true;
        String url = API_VOLUME_LIST + "?name=" + name;
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
            String body = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
            if (ToolUtils.jsonToInt(jsonObject.get("count"))!= 0) {
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public Boolean queryFsByName(String name, String storageId) throws DMEException {
        Boolean flag = true;
        List<FileSystem> fileSystems = getFileSystems(storageId);
        for (FileSystem fileSystem : fileSystems) {
            if (name.equals(fileSystem.getName())) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    @Override
    public Boolean queryShareByName(String name, String storageId) throws DMEException {
        Boolean flag = true;
        List<NfsShares> nfsShares = getNfsShares(storageId);
        for (NfsShares nfsShare : nfsShares) {
            if (name.equals(nfsShare.getName())) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 得到存储池与服务等级的关系信息
     **/
    public Map<String, Object> getDjTierContainsStoragePool() throws DMEException {
        Map<String, Object> map = new HashMap<>(16);
        String getDjTierContainsStoragePoolUrl = DJTIERCONTAINSSTORAGEPOOL_URL;
        try {
            ResponseEntity responseEntity = dmeAccessService.access(getDjTierContainsStoragePoolUrl, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
                JsonObject vjson = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                if (null != vjson && !vjson.isJsonNull()) {
                    if (null != vjson.get("objList") && !vjson.get("objList").isJsonNull()) {
                        JsonArray objList = vjson.getAsJsonArray("objList");
                        if (null != objList && objList.size() > 0) {
                            for (int i = 0; i < objList.size(); i++) {
                                JsonObject objJson = objList.get(i).getAsJsonObject();
                                if (!objJson.isJsonNull()) {
                                    String sourceInstanceId = ToolUtils.jsonToStr(objJson.get("source_Instance_Id"));
                                    String targetInstanceId = ToolUtils.jsonToStr(objJson.get("target_Instance_Id"));
                                    if (null != map.get(targetInstanceId)) {
                                        List<String> siIds = (List<String>) map.get(targetInstanceId);
                                        siIds.add(sourceInstanceId);
                                    } else {
                                        List<String> siIds = new ArrayList<>();
                                        siIds.add(sourceInstanceId);
                                        map.put(targetInstanceId, siIds);
                                    }

                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("DME link error url:" + getDjTierContainsStoragePoolUrl + ",error:" + e.getMessage());
            throw new DMEException(e.getMessage());
        }
        return map;
    }

    /**
     * 得到服务等级的实例信息
     **/
    public Map<String, Object> getDjtier() throws DMEException {
        Map<String, Object> map = new HashMap<>(16);
        String getDjtierUrl = SYS_DJTIER_URL;
        try {
            LOG.info("getDjtierUrl===" + getDjtierUrl);
            ResponseEntity responseEntity = dmeAccessService.access(getDjtierUrl, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
                JsonObject vjson = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                if (null != vjson && !vjson.isJsonNull()) {
                    if (null != vjson.get("objList") && !vjson.get("objList").isJsonNull()) {
                        JsonArray objList = vjson.getAsJsonArray("objList");
                        if (null != objList && objList.size() > 0) {
                            for (int i = 0; i < objList.size(); i++) {
                                JsonObject objJson = objList.get(i).getAsJsonObject();
                                if (!objJson.isJsonNull()) {
                                    String resId = ToolUtils.jsonToStr(objJson.get("resId"));
                                    String name = ToolUtils.jsonToStr(objJson.get("name"));
                                    map.put(resId, name);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("DME link error url:" + getDjtierUrl + ",error:" + e.getMessage());
            throw new DMEException(e.getMessage());
        }
        return map;
    }

    /**
     * 整理存储池与服务的关系信息
     **/
    public Map<String, Object> getDjTierOfStoragePool() {
        Map<String, Object> map = new HashMap<>(16);
        String getDjtierUrl = SYS_DJTIER_URL;
        try {
            Map<String, Object> djtierMap = getDjtier();
            Map<String, Object> djTierStoragePoolMap = getDjTierContainsStoragePool();
            Set<String> sps = djTierStoragePoolMap.keySet();
            for (String spkey : sps) {
                if (null != djTierStoragePoolMap.get(spkey)) {
                    List<String> djIds = (List<String>) djTierStoragePoolMap.get(spkey);
                    if (null != djIds && djIds.size() > 0) {
                        List<String> diNames = new ArrayList<>();
                        for (String djId : djIds) {
                            diNames.add(ToolUtils.getStr(djtierMap.get(djId)));
                        }
                        map.put(spkey, diNames);
                    }
                }
            }

        } catch (Exception e) {
            LOG.error("getDjTierOfStoragePool error:" + e.getMessage());
        }
        return map;
    }

    private String getStorageByPoolRawId(String poolRawId) throws DMEException {

        String className = "SYS_StoragePool";
        String poolName = "";
        String url = API_INSTANCES_LIST + "/" + className + "?condition={json}"+"&&pageSize=1000";
        String params = ToolUtils.getRequsetParams("poolId", poolRawId);
        ResponseEntity<String> responseEntity = dmeAccessService.accessByJson(url, HttpMethod.GET, params);
        if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("objList").getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                poolName = ToolUtils.jsonToStr(element.get("name"));
                break;
            }
        }
        return poolName;
    }

    private DiskPool getDiskPoolByPoolId(String poolId) throws DMEException {
        String className = "SYS_DiskPool";
        String url = API_INSTANCES_LIST + "/" + className + "?condition={json}"+"pageSize=1000";
        String params = ToolUtils.getRequsetParams("poolId", poolId);
        DiskPool retdiskPool = new DiskPool();
        ResponseEntity<String> responseEntity = dmeAccessService.accessByJson(url, HttpMethod.GET, params);
        if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("objList").getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                DiskPool diskPool = new DiskPool();
                diskPool.setId(ToolUtils.jsonToStr(element.get("id")));
                diskPool.setNativeId(ToolUtils.jsonToStr(element.get("nativeId")));
                diskPool.setLastModified(ToolUtils.jsonToStr(element.get("lastModified")));
                diskPool.setLastMonitorTime(ToolUtils.jsonToStr(element.get("lastMonitorTime")));
                diskPool.setDataStatus(ToolUtils.jsonToStr(element.get("dataStatus")));
                diskPool.setName(ToolUtils.jsonToStr(element.get("name")));
                diskPool.setStatus(ToolUtils.jsonToStr(element.get("status")));
                diskPool.setRunningStatus(ToolUtils.jsonToStr(element.get("runningStatus")));
                diskPool.setEncryptDiskType(ToolUtils.jsonToStr(element.get("encryptDiskType")));
                Double totalCapacity = ToolUtils.jsonToDou(element.get("totalCapacity"));
                diskPool.setTotalCapacity(totalCapacity);
                Double usedCapacity = ToolUtils.jsonToDou(element.get("usedCapacity"));
                diskPool.setUsedCapacity(usedCapacity);
                diskPool.setFreeCapacity(ToolUtils.jsonToDou(element.get("freeCapacity")));
                diskPool.setSpareCapacity(ToolUtils.jsonToDou(element.get("spareCapacity")));
                diskPool.setUsedSpareCapacity(ToolUtils.jsonToDou(element.get("usedSpareCapacity")));
                diskPool.setPoolId(ToolUtils.jsonToStr(element.get("poolId")));
                //订阅率（lun/fs订阅率）
                DecimalFormat df = new DecimalFormat("#.00");
                Double usageCapacityRate = 0.0;
                if (totalCapacity != 0) {
                    usageCapacityRate = Double.valueOf(df.format(usedCapacity / totalCapacity)) * 100;
                }
                diskPool.setUsageRate(usageCapacityRate);
                diskPool.setStorageDeviceId(ToolUtils.jsonToStr(element.get("storageDeviceId")));
                retdiskPool = diskPool;
            }
        }
        return retdiskPool;
    }

    /**
     * 判断数据存储中是否有注册的虚拟机，有则返回true，没有返回false
     *
     * @param objectid 数据存储的objectid
     * @return 是否存在vm
     */
    public boolean hasVmOnDatastore(String objectid) {
        return vcsdkUtils.hasVmOnDatastore(objectid);
    }


}
