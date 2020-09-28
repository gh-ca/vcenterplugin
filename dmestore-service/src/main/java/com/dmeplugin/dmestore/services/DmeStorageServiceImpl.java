package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.dao.DmeVmwareRalationDao;
import com.dmeplugin.dmestore.model.*;
import com.dmeplugin.dmestore.utils.RestUtils;
import com.dmeplugin.dmestore.utils.ToolUtils;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;
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
    private final String API_STORAGEPOOL_LIST = "/rest/storagemgmt/v1/storagepools/query";
    private final String API_LOGICPORTS_LIST = "/rest/storagemgmt/v1/storage-port/logic-ports?storage_id=";
    private final String API_VOLUME_LIST = "/rest/blockservice/v1/volumes?storageId =";
    private final String API_FILESYSTEMS_LIST = "/rest/fileservice/v1/filesystems/query";
    private final String API_DTREES_LIST = "/rest/fileservice/v1/dtrees/summary";
    private final String API_NFSSHARE_LIST = "/rest/fileservice/v1/nfs-shares/summary";
    private final String API_BANDPORTS_LIST = "/rest/storagemgmt/v1/storage-port/bond-ports?storage_id=";
    private final String API_INSTANCES_LIST = "/rest/resourcedb/v1/instances";
    private final String API_VOLUME_DETAIL = "/rest/blockservice/v1/volumes";



    private static final Logger LOG = LoggerFactory.getLogger(DmeStorageServiceImpl.class);

    private Gson gson = new Gson();

    private DmeAccessService dmeAccessService;

    private DmeVmwareRalationDao dmeVmwareRalationDao;

    private VCSDKUtils vcsdkUtils;

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
    public Map<String, Object> getStorages() {

        Map<String, Object> objMap = new HashMap<>();
        objMap.put("code", 200);
        objMap.put("msg", "list storage success!");
        List<Storage> list = new ArrayList<>();

        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(API_STORAGES, HttpMethod.GET, null);
            LOG.info("{" + API_STORAGES + "}" + responseEntity);
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                objMap.put("code", code);
                objMap.put("msg", "list storage error !");
                return objMap;
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
                    storageObj.setStatus(ToolUtils.jsonToStr(jsonObj.get("status")));
                    storageObj.setSynStatus(ToolUtils.jsonToStr(jsonObj.get("syn_status")));
                    storageObj.setVendor(ToolUtils.jsonToStr(jsonObj.get("vendor")));
                    storageObj.setModel(ToolUtils.jsonToStr(jsonObj.get("model")));
                    storageObj.setUsedCapacity(ToolUtils.jsonToDou(jsonObj.get("used_capacity"), 0.0));
                    storageObj.setTotalCapacity(ToolUtils.jsonToDou(jsonObj.get("total_capacity"), 0.0));
                    storageObj.setTotalEffectiveCapacity(ToolUtils.jsonToDou(jsonObj.get("total_effective_capacity"), 0.0));
                    storageObj.setFreeEffectiveCapacity(ToolUtils.jsonToDou(jsonObj.get("free_effective_capacity"),0.0));
                    storageObj.setMaxCpuUtilization(ToolUtils.jsonToDou(jsonObj.get("max_cpu_utilization"),0.0));
                    storageObj.setMaxIops(ToolUtils.jsonToDou(jsonObj.get("max_iops"),0.0));
                    storageObj.setMaxBandwidth(ToolUtils.jsonToDou(jsonObj.get("max_bandwidth"),0.0));
                    storageObj.setMaxLatency(ToolUtils.jsonToDou(jsonObj.get("max_latency"),0.0));
                    storageObj.setSn(ToolUtils.jsonToStr(jsonObj.get("sn")));
                    storageObj.setVersion(ToolUtils.jsonToStr(jsonObj.get("version")));
                    storageObj.setTotal_pool_capacity(ToolUtils.jsonToDou(jsonObj.get("total_pool_capacity"),0.0));

                    JsonElement jsonAzIds = jsonObj.get("az_ids");
                    if (jsonAzIds != null) {
                        String azIds =ToolUtils.jsonToStr(jsonAzIds);
                        String[] az_ids = {azIds};
                        storageObj.setAzIds(az_ids);
                    } else {
                        String[] az_ids = {};
                        storageObj.setAzIds(az_ids);
                    }
                    list.add(storageObj);
                }
            }
            objMap.put("data", list);
            return objMap;
        } catch (Exception e) {
            LOG.error("list storage error", e);
            String message = e.getMessage();
            objMap.put("code", 503);
            objMap.put("message", message);
        } finally {
            return objMap;
        }
    }

    @Override
    public Map<String, Object> getStorageDetail(String storageId) {
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "search oriented storage success");
        resMap.put("data", storageId);

        String url = API_STORAGES + "/" + storageId + "/detail";
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            LOG.info("DmeStorageServiceImpl/getStorageDetail/responseEntity==" + responseEntity);
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                resMap.put("code", code);
                resMap.put("msg", "search oriented storage error");
                return resMap;
            }
            String object = responseEntity.getBody();
            if (object != null) {
                JsonObject element = new JsonParser().parse(object).getAsJsonObject();
                StorageDetail storageObj = new StorageDetail();
                storageObj.setId(ToolUtils.jsonToStr(element.get("id")));
                storageObj.setName(ToolUtils.jsonToStr(element.get("name")));
                storageObj.setIp(ToolUtils.jsonToStr(element.get("ip")));
                storageObj.setStatus(ToolUtils.jsonToStr(element.get("status")));
                storageObj.setSynStatus(ToolUtils.jsonToStr(element.get("syn_status")));
                storageObj.setPatch_version(ToolUtils.jsonToStr(element.get("patch_version")));
                storageObj.setVendor(ToolUtils.jsonToStr(element.get("vendor")));
                storageObj.setModel(ToolUtils.jsonToStr(element.get("model")));
                storageObj.setUsedCapacity(ToolUtils.jsonToDou(element.get("used_capacity"),0.0));
                storageObj.setTotalCapacity(ToolUtils.jsonToDou(element.get("total_capacity"),0.0));
                storageObj.setTotalEffectiveCapacity(ToolUtils.jsonToDou(element.get("total_effective_capacity"),0.0));
                storageObj.setFreeEffectiveCapacity(ToolUtils.jsonToDou(element.get("free_effective_capacity"),0.0));
                storageObj.setMaintenance_start(ToolUtils.jsonToInt(element.get("maintenance_start"),0));
                storageObj.setMaintenance_overtime(ToolUtils.jsonToInt(element.get("maintenance_overtime"),0));

                JsonArray ids = element.get("az_ids").getAsJsonArray();
                if (ids.size() != 0) {
                    String[] az_ids = {ToolUtils.jsonToStr(ids)};
                    storageObj.setAzIds(az_ids);
                } else {
                    String[] az_ids = {};
                    storageObj.setAzIds(az_ids);
                }
                resMap.put("data", storageObj);
            }
            return resMap;
        } catch (Exception e) {
            LOG.error("search oriented storage error!", e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        }
        return resMap;
    }

    @Override
    public Map<String, Object> getStoragePools(String storageId) {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "search oriented storage pool success");
        resMap.put("data", storageId);

        List<StoragePool> resList = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        params.put("storage_id", storageId);

        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(API_STORAGEPOOL_LIST, HttpMethod.POST, gson.toJson(params));
            LOG.info("DmeStorageServiceImpl/getStoragePools/responseEntity==" + responseEntity);
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                resMap.put("msg", "search oriented storage pool error");
                resMap.put("code", code);
                return resMap;
            }
            String object = responseEntity.getBody();
            if (!StringUtils.isEmpty(object)) {
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("datas").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    StoragePool storagePool = new StoragePool();
                    storagePool.setName(ToolUtils.jsonToStr(element.get("name")));
                    storagePool.setFree_capacity(ToolUtils.jsonToDou(element.get("free_capacity"),0.0));
                    storagePool.setHealth_status(ToolUtils.jsonToStr(element.get("health_status")));
                    Double lun_subscribed_capacity = Double.valueOf(ToolUtils.jsonToStr(element.get("lun_subscribed_capacity")));
                    storagePool.setLun_subscribed_capacity(lun_subscribed_capacity);
                    storagePool.setParent_type(ToolUtils.jsonToStr(element.get("parent_type")));
                    storagePool.setRunning_status(ToolUtils.jsonToStr(element.get("running_status")));
                    Double total_capacity = Double.valueOf(ToolUtils.jsonToDou(element.get("total_capacity"),0.0));
                    storagePool.setTotal_capacity(ToolUtils.jsonToDou(element.get("total_capacity"),0.0));
                    Double fs_subscribed_capacity = Double.valueOf(ToolUtils.jsonToDou(element.get("fs_subscribed_capacity"),0.0));
                    storagePool.setFs_subscribed_capacity(fs_subscribed_capacity);
                    storagePool.setConsumed_capacity(Double.valueOf(ToolUtils.jsonToStr(element.get("consumed_capacity"))));
                    storagePool.setConsumed_capacity_percentage(ToolUtils.jsonToStr(element.get("consumed_capacity_percentage")));
                    storagePool.setConsumed_capacity_threshold(ToolUtils.jsonToStr(element.get("consumed_capacity_threshold")));
                    storagePool.setStorage_pool_id(ToolUtils.jsonToStr(element.get("storage_pool_id")));
                    storagePool.setStorage_name(ToolUtils.jsonToStr(element.get("storage_name")));
                    storagePool.setMedia_type(ToolUtils.jsonToStr(element.get("media_type")));
                    storagePool.setTier0_disk_type(ToolUtils.jsonToStr(element.get("tier0_disk_type")));
                    storagePool.setTier1_disk_type(ToolUtils.jsonToStr(element.get("tier1_disk_type")));
                    storagePool.setTier2_disk_type(ToolUtils.jsonToStr(element.get("tier2_disk_type")));
                    storagePool.setStorage_id(ToolUtils.jsonToStr(element.get("storage_id")));
                    storagePool.setTier0_raid_lv(ToolUtils.jsonToStr(element.get("tier0_raid_lv")));
                    storagePool.setTier1_raid_lv(ToolUtils.jsonToStr(element.get("tier1_raid_lv")));
                    storagePool.setTier2_raid_lv(ToolUtils.jsonToStr(element.get("tier2_raid_lv")));
                    storagePool.setConsumed_capacity(ToolUtils.jsonToDou(element.get("consumed_capacity"),0.0));
                    //订阅率（lun/fs订阅率）
                    DecimalFormat df = new DecimalFormat("#.00");
                    Double lun_subscription_rate = 0.0;
                    Double fs_subscription_rate = 0.0;
                    if (total_capacity!=0) {
                        lun_subscription_rate = Double.valueOf(df.format(lun_subscribed_capacity / total_capacity));
                        fs_subscription_rate = Double.valueOf(df.format(fs_subscribed_capacity / total_capacity));
                    }
                    storagePool.setLun_subscription_rate(lun_subscription_rate);
                    storagePool.setFs_subscription_rate(fs_subscription_rate);

                    resList.add(storagePool);
                }
                resMap.put("data", resList);
            }
            return resMap;
        } catch (Exception e) {
            LOG.error("search oriented storage pool error", e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        } finally {
            return resMap;
        }
    }

    @Override
    public Map<String, Object> getLogicPorts(String storageId) {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "list logic ports success!");
        resMap.put("storageId", storageId);
        List<LogicPorts> resList = new ArrayList<>();

        String url = API_LOGICPORTS_LIST + storageId;
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            LOG.info("DmeStorageServiceImpl/getLogicPorts/responseEntity==" + responseEntity);
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                resMap.put("code", code);
                resMap.put("msg", "list bandports error!");
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
                    logicPorts.setRunning_status(ToolUtils.jsonToStr(element.get("running_status")));
                    logicPorts.setOperational_status(ToolUtils.jsonToStr(element.get("operational_status")));
                    logicPorts.setMgmt_ip(ToolUtils.jsonToStr(element.get("mgmt_ip")));
                    logicPorts.setMgmt_ipv6(ToolUtils.jsonToStr(element.get("mgmt_ipv6")));
                    logicPorts.setHome_port_id(ToolUtils.jsonToStr(element.get("home_port_id")));
                    logicPorts.setHome_port_name(ToolUtils.jsonToStr(element.get("home_port_name")));
                    logicPorts.setRole(ToolUtils.jsonToStr(element.get("role")));
                    logicPorts.setDdns_status(ToolUtils.jsonToStr(element.get("ddns_status")));
                    logicPorts.setCurrent_port_id(ToolUtils.jsonToStr(element.get("current_port_id")));
                    logicPorts.setCurrent_port_name(ToolUtils.jsonToStr(element.get("current_port_name")));
                    logicPorts.setSupport_protocol(ToolUtils.jsonToStr(element.get("support_protocol")));
                    logicPorts.setManagement_access(ToolUtils.jsonToStr(element.get("management_access")));
                    logicPorts.setVstore_id(ToolUtils.jsonToStr(element.get("vstore_id")));
                    logicPorts.setVstore_name(ToolUtils.jsonToStr(element.get("vstore_name")));
                    resList.add(logicPorts);
                }
                resMap.put("data", resList);
            }
            return resMap;
        } catch (Exception e) {
            LOG.error("list bandports error", e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());

        } finally {
            return resMap;
        }
    }

    @Override
    public Map<String, Object> getVolumes(String storageId) {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "list volumes success!");
        resMap.put("storageId", storageId);

        List<Volume> volumes = new ArrayList<>();
        String url = API_VOLUME_LIST + storageId;

        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            LOG.info("DmeStorageServiceImpl/getVolumes/responseEntity==" + responseEntity);
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                resMap.put("code", code);
                resMap.put("msg", "list volumes error!");
                return resMap;
            }
            Object object = responseEntity.getBody();
            if (object != null) {
                JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("volumes").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    Volume volume = new Volume();
                    String volume_id = ToolUtils.jsonToStr(element.get("id"));
                    volume.setId(volume_id);
                    volume.setName(ToolUtils.jsonToStr(element.get("name")));
                    volume.setStatus(ToolUtils.jsonToStr(element.get("status")));
                    volume.setAttached(ToolUtils.jsonToBoo(element.get("attached")));
                    volume.setAlloctype(ToolUtils.jsonToStr(element.get("alloctype")));
                    volume.setService_level_name(ToolUtils.jsonToStr(element.get("service_level_name")));
                    volume.setStorage_id(ToolUtils.jsonToStr(element.get("storage_id")));
                    volume.setPool_raw_id(ToolUtils.jsonToStr(element.get("pool_raw_id")));
                    volume.setCapacity_usage(ToolUtils.jsonToStr(element.get("capacity_usage")));
                    volume.setProtectionStatus(ToolUtils.jsonToBoo(element.get("protected")));
                    volume.setCapacity(ToolUtils.jsonToInt(element.get("capacity"),0));
                    volume.setDatastores(getDataStoreOnVolume(volume_id));
                    volumes.add(volume);
                }
                resMap.put("data", volumes);
            }
            return resMap;
        } catch (Exception e) {
            LOG.error("list volume error!",e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        } finally {
            return resMap;
        }
    }

    @Override
    public Map<String, Object> getFileSystems(String storageId) {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "list filesystem success!");
        resMap.put("storageId", storageId);

        List<FileSystem> fileSystems = new ArrayList<>();

        Map<String, String> params = new HashMap<>();
        params.put("storage_id", storageId);
        String jsonParams = gson.toJson(params);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(API_FILESYSTEMS_LIST, HttpMethod.POST, jsonParams);
            LOG.info("DmeStorageServiceImpl/getFileSystems/responseEntity==" + responseEntity);
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                resMap.put("code", code);
                resMap.put("msg", "list filesystem error!");
                return resMap;
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
                    fileSystem.setHealth_status(ToolUtils.jsonToStr(element.get("health_status")));
                    fileSystem.setAlloc_type(ToolUtils.jsonToStr(element.get("alloc_type")));
                    fileSystem.setCapacity(ToolUtils.jsonToDou(element.get("capacity"),0.0));
                    fileSystem.setCapacity_usage_ratio(ToolUtils.jsonToInt(element.get("capacity_usage_ratio"),0));
                    fileSystem.setStorage_pool_name(ToolUtils.jsonToStr(element.get("storage_pool_name")));
                    fileSystem.setNfs_count(ToolUtils.jsonToInt(element.get("nfs_count"),0));
                    fileSystem.setCifs_count(ToolUtils.jsonToInt(element.get("cifs_count"),0));
                    fileSystem.setDtree_count(ToolUtils.jsonToInt(element.get("dtree_count"),0));
                    fileSystems.add(fileSystem);
                }
                resMap.put("data", fileSystems);
            }
            return resMap;
        } catch (Exception e) {
            LOG.error("list filesystem error!", e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        } finally {
            return resMap;
        }
    }

    @Override
    public Map<String, Object> getDTrees(String storageId) {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "list dtree success!");
        resMap.put("storageId", storageId);

        List<Dtrees> resList = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        params.put("storage_id", storageId);

        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(API_DTREES_LIST, HttpMethod.POST, gson.toJson(params));
            LOG.info("DmeStorageServiceImpl/getDTrees/responseEntity==" + responseEntity);
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                resMap.put("code", 200);
                resMap.put("msg", "list dtree error!");
                return resMap;
            }
            String object = responseEntity.getBody();
            if (!StringUtils.isEmpty(object)) {
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("dtrees").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    Dtrees dtrees = new Dtrees();
                    dtrees.setName(ToolUtils.jsonToStr(element.get("name")));
                    dtrees.setFs_name(ToolUtils.jsonToStr(element.get("fs_name")));
                    dtrees.setQuota_switch(ToolUtils.jsonToBoo(element.get("quota_switch")));
                    dtrees.setSecurity_style(ToolUtils.jsonToStr(element.get("security_style")));
                    dtrees.setTier_name(ToolUtils.jsonToStr(element.get("tier_name")));
                    dtrees.setNfs_count(ToolUtils.jsonToInt(element.get("nfs_count"),0));
                    dtrees.setCifs_count(ToolUtils.jsonToInt(element.get("cifs_count"),0));
                    resList.add(dtrees);
                }
                resMap.put("data", resList);
            }
            return resMap;
        } catch (Exception e) {
            LOG.error("list dtree error!", e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        } finally {
            return resMap;
        }
    }

    @Override
    public Map<String, Object> getNfsShares(String storageId) {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "list nfsshares success!");
        resMap.put("storageId", storageId);
        List<NfsShares> resList = new ArrayList<>();

        Map<String, String> params = new HashMap<>();
        params.put("storage_id", storageId);

        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(API_NFSSHARE_LIST, HttpMethod.POST, gson.toJson(params));
            LOG.info("DmeStorageServiceImpl/getNfsShares/responseEntity==" + responseEntity);
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                resMap.put("code", code);
                resMap.put("msg", "list nfsshares error!");
                return resMap;
            }
            String object = responseEntity.getBody();
            if (!StringUtils.isEmpty(object)) {
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("nfs_share_info_list").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    NfsShares nfsShares = new NfsShares();
                    nfsShares.setName(ToolUtils.jsonToStr(element.get("name")));
                    nfsShares.setShare_path(ToolUtils.jsonToStr(element.get("share_path")));
                    nfsShares.setStorage_id(ToolUtils.jsonToStr(element.get("storage_id")));
                    nfsShares.setTier_name(ToolUtils.jsonToStr(element.get("tier_name")));
                    nfsShares.setOwning_dtree_name(ToolUtils.jsonToStr(element.get("owning_dtree_name")));
                    nfsShares.setOwning_dtree_id(ToolUtils.jsonToStr(element.get("owning_dtree_id")));
                    nfsShares.setFs_name(ToolUtils.jsonToStr(element.get("fs_name")));
                    resList.add(nfsShares);
                }
                resMap.put("data", resList);
            }
            return resMap;
        } catch (Exception e) {
            LOG.error("list nfsshares error!", e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        } finally {
            return resMap;
        }
    }

    @Override
    public Map<String, Object> getBandPorts(String storageId) {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "list band ports success!");
        resMap.put("storageId", storageId);
        List<BandPorts> resList = new ArrayList<>();

        String url = API_BANDPORTS_LIST + storageId;

        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            LOG.info("DmeStorageServiceImpl/getBandPorts/responseEntity==" + responseEntity);
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                resMap.put("code", code);
                resMap.put("msg", "list bandports error!");
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
                    bandPorts.setHealth_status(ToolUtils.jsonToStr(element.get("health_status")));
                    bandPorts.setRunning_status(ToolUtils.jsonToStr(element.get("running_status")));
                    bandPorts.setMtu(ToolUtils.jsonToStr(element.get("mtu")));
                    resList.add(bandPorts);
                }
                resMap.put("data", resList);
            }
            return resMap;

        } catch (Exception e) {
            LOG.error("list bandports error!", e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        } finally {
            return resMap;
        }
    }

    @Override
    public Map<String, Object> getStorageControllers() {

        String className = "SYS_Controller";
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "list storage controller success!");
        resMap.put("storageId", className);
        List<StorageControllers> resList = new ArrayList<>();

        String url = API_INSTANCES_LIST + "/" + className;
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            LOG.info("DmeStorageServiceImpl/getStorageControllers/responseEntity==" + responseEntity);
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                resMap.put("code", code);
                resMap.put("msg", "list storage controller error!");
                return resMap;
            }
            Object object = responseEntity.getBody();
            if (!StringUtils.isEmpty(object)) {
                JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("objList").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    StorageControllers storageControllers = new StorageControllers();
                    storageControllers.setName(ToolUtils.jsonToStr(element.get("name")));
                    storageControllers.setSoftVer(ToolUtils.jsonToStr(element.get("name")));
                    storageControllers.setStatus(ToolUtils.jsonToStr(element.get("status")));
                    storageControllers.setCpuInfo(ToolUtils.jsonToStr(element.get("cpuInfo")));
                    resList.add(storageControllers);
                }
                resMap.put("data", resList);
            }
            return resMap;
        } catch (Exception e) {
            LOG.error("list storage controller error!");
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        } finally {
            return resMap;
        }


    }

    @Override
    public Map<String, Object> getStorageDisks() {

        String className = "SYS_StorageDisk";
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "list storage disk success!");
        resMap.put("storageId", className);
        List<StorageDisk> resList = new ArrayList<>();

        String url = API_INSTANCES_LIST + "/" + className;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            LOG.info("DmeStorageServiceImpl/getStorageDisks/responseEntity==" + responseEntity);
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                resMap.put("code", code);
                resMap.put("msg", "list storage disk error!");
                return resMap;
            }
            Object object = responseEntity.getBody();
            if (!StringUtils.isEmpty(object)) {
                JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("objList").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    StorageDisk storageDisk = new StorageDisk();
                    storageDisk.setName(ToolUtils.jsonToStr(element.get("name")));
                    storageDisk.setStatus(ToolUtils.jsonToStr(element.get("status")));
                    storageDisk.setCapacity(ToolUtils.jsonToDou(element.get("capacity"),0.0));
                    resList.add(storageDisk);
                }
                resMap.put("data", resList);
            }
            return resMap;
        } catch (Exception e) {
            LOG.error("list storage disk error!", e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        } finally {
            return resMap;
        }

    }


    private ResponseEntity<String> access(String url, HttpMethod method, String requestBody) throws Exception {

        RestUtils restUtils = new RestUtils();
        RestTemplate restTemplate = restUtils.getRestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, entity, String.class);
        LOG.info(url + "==responseEntity==" + (responseEntity == null ? "null" : responseEntity.getStatusCodeValue()));

        return responseEntity;
    }

    @Override
    public List<EthPortInfo> getStorageEthPorts(String storageSn) throws Exception {
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
            throw e;
        }
        LOG.info("getStorageEthPorts relists===" + (relists == null ? "null" : (relists.size() + "==" + gson.toJson(relists))));
        return relists;
    }

    //通过存储设备的sn查询 存储设备的资源ID
    public String getStorageResIdBySn(String storageSn) {
        String dsResId = null;
        try {
            if (!StringUtils.isEmpty(storageSn)) {
                String stordeviceIdUrl = DmeConstants.DME_RES_STORDEVICEID_QUERY_URL;
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
                simple1.addProperty("name", "sn");
                simple1.addProperty("operator", "equal");
                simple1.addProperty("value", storageSn);
                consObj1.add("simple", simple1);
                consObj1.addProperty("logOp", "and");
                constraint.add(consObj1);

                condition.add("constraint", constraint);

                stordeviceIdUrl = stordeviceIdUrl + "?condition={json}";
                LOG.info("stordeviceIdUrl===" + stordeviceIdUrl);
                try {
                    ResponseEntity responseEntity = dmeAccessService.accessByJson(stordeviceIdUrl, HttpMethod.GET, condition.toString());
                    LOG.info("stordeviceIdUrl responseEntity==" + responseEntity.toString());
                    if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
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
                LOG.info("ethPortUrl===" + ethPortUrl);
                try {
                    ResponseEntity responseEntity = dmeAccessService.accessByJson(ethPortUrl, HttpMethod.GET, condition.toString());
                    LOG.info("getWorkLoads responseEntity==" + responseEntity.toString());
                    if (responseEntity.getStatusCodeValue() == RestUtils.RES_STATE_I_200) {
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
        LOG.info("getEthPortsByResId relists===" + (relists == null ? "null" : (relists.size() + "==" + gson.toJson(relists))));
        return relists;
    }

    @Override
    public Map<String, Object> getVolume(String volumeId) {
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "get volume success!");
        resMap.put("volume", volumeId);
        String url = API_VOLUME_DETAIL + "/" + volumeId;

        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            LOG.info("DmeStorageServiceImpl/getVolume/responseEntity==" + responseEntity);
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                resMap.put("code", code);
                resMap.put("msg", "get volume error!");
                return resMap;
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
                volume.setService_level_name(ToolUtils.jsonToStr(element.get("service_level_name")));
                volume.setStorage_id(ToolUtils.jsonToStr(element.get("storage_id")));
                volume.setPool_raw_id(ToolUtils.jsonToStr(element.get("pool_raw_id")));
                volume.setCapacity_usage(ToolUtils.jsonToStr(element.get("capacity_usage")));
                volume.setProtectionStatus(ToolUtils.jsonToBoo(element.get("protectionStatus")));
                JsonArray jsonArray = element.get("attachments").getAsJsonArray();
                volumeAttachments(jsonArray, volume);
                resMap.put("data", volume);
            }
            return resMap;
        } catch (Exception e) {
            LOG.error("list volume error!");
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        } finally {
            return resMap;
        }
    }

    private String getDataStoreOnVolume(String volumeId) throws SQLException {
        return dmeVmwareRalationDao.getVmfsNameByVolumeId(volumeId);
    }

    //从卷信息中查关联的主机和主机组
    private void volumeAttachments(JsonArray array, Volume volume) {
        if (null != array && array.size() > 0) {
            List<String> hostIds = new ArrayList<>();
            List<String> hostGroupIds = new ArrayList<>();
            for (JsonElement element : array) {
                JsonObject json = element.getAsJsonObject();
                String hostId = ToolUtils.getStr(json.get("host_id"));
                String hostGroupId = ToolUtils.getStr(json.get("attached_host_group"));
                hostIds.add(hostId);
                hostGroupIds.add(hostGroupId);
            }
            volume.setHostIds(hostIds);
            volume.setHostGroupIds(hostGroupIds);
        }
    }


}
