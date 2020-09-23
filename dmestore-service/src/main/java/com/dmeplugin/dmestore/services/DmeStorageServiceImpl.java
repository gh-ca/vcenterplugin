package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.*;
import com.dmeplugin.dmestore.utils.RestUtils;
import com.dmeplugin.dmestore.utils.ToolUtils;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

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


    private static final Logger LOG = LoggerFactory.getLogger(DmeStorageServiceImpl.class);

    private Gson gson=new Gson();

    private DmeAccessService dmeAccessService;

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
            //ResponseEntity responseEntity = access( API_STORAGES_LIST, HttpMethod.GET, null);
            LOG.info("{"+API_STORAGES+"}" + responseEntity);
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
                    storageObj.setId(jsonObj.get("id").getAsString());
                    storageObj.setName(jsonObj.get("name").getAsString());
                    storageObj.setIp(jsonObj.get("ip").getAsString());
                    storageObj.setStatus(jsonObj.get("status").getAsString());
                    storageObj.setSynStatus(jsonObj.get("syn_status").getAsString());
                    storageObj.setVendor(jsonObj.get("vendor").getAsString());
                    storageObj.setModel(jsonObj.get("model").getAsString());
                    storageObj.setUsedCapacity(Double.valueOf(jsonObj.get("used_capacity").getAsString()));
                    storageObj.setTotalCapacity(Double.valueOf(jsonObj.get("total_capacity").getAsString()));
                    storageObj.setTotalEffectiveCapacity(Double.valueOf(jsonObj.get("total_effective_capacity").getAsString()));
                    storageObj.setFreeEffectiveCapacity(Double.valueOf(jsonObj.get("free_effective_capacity").getAsString()));
                    storageObj.setMaxCpuUtilization(Double.valueOf(jsonObj.get("max_cpu_utilization").getAsString()));
                    storageObj.setMaxIops(Double.valueOf(jsonObj.get("max_iops").getAsString()));
                    storageObj.setMaxBandwidth(Double.valueOf(jsonObj.get("max_bandwidth").getAsString()));
                    storageObj.setMaxLatency(Double.valueOf(jsonObj.get("max_latency").getAsString()));
                    JsonElement jsonAzIds = jsonObj.get("az_ids");
                    if (jsonAzIds != null) {
                        String azIds = jsonAzIds.getAsString();
                        String[] az_ids = {azIds};
                        storageObj.setAzIds(az_ids);
                    } else{
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
        }finally {
            return objMap;
        }
    }

    @Override
    public Map<String, Object> getStorageDetail(String storageId) {
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "search oriented storage success");
        resMap.put("data", storageId);

        String url =  API_STORAGES + "/" + storageId + "/detail";
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            //ResponseEntity responseEntity = access(url, HttpMethod.GET, null);
            LOG.info("DmeStorageServiceImpl/getStorageDetail/responseEntity==" + responseEntity);
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                resMap.put("code", code);
                resMap.put("msg", "search oriented storage error");
                return resMap;
            }
            Object object = responseEntity.getBody();
            if (object != null) {
                JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
                StorageDetail storageObj = new StorageDetail();
                storageObj.setId(jsonObject.get("id").getAsString());
                storageObj.setName(jsonObject.get("name").getAsString());
                storageObj.setIp(jsonObject.get("ip").getAsString());
                storageObj.setStatus(jsonObject.get("status").getAsString());
                storageObj.setSynStatus(jsonObject.get("syn_status").getAsString());
                storageObj.setVendor(jsonObject.get("vendor").getAsString());
                storageObj.setModel(jsonObject.get("model").getAsString());
                storageObj.setUsedCapacity(Double.valueOf(jsonObject.get("used_capacity").getAsString()));
                storageObj.setTotalCapacity(Double.valueOf(jsonObject.get("total_capacity").getAsString()));
                storageObj.setTotalEffectiveCapacity(Double.valueOf(jsonObject.get("total_effective_capacity").getAsString()));
                storageObj.setFreeEffectiveCapacity(Double.valueOf(jsonObject.get("free_effective_capacity").getAsString()));
                JsonArray ids = jsonObject.get("az_ids").getAsJsonArray();
                if (ids.size() != 0) {
                    String[] az_ids = {ids.getAsString()};
                    storageObj.setAzIds(az_ids);
                } else {
                    String[] az_ids = {};
                    storageObj.setAzIds(az_ids);
                }
                resMap.put("data", storageObj);
            }
            return resMap;
        } catch (Exception e) {
            LOG.error("search oriented storage error!");
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
            //ResponseEntity<String> responseEntity = access(url, HttpMethod.POST, gson.toJson(params));
            ResponseEntity<String> responseEntity = dmeAccessService.access( API_STORAGEPOOL_LIST, HttpMethod.POST, gson.toJson(params));
            LOG.info("DmeStorageServiceImpl/getStoragePools/responseEntity==" + responseEntity);
            int code = responseEntity.getStatusCodeValue();
            if (code!=200) {
                resMap.put("msg", "search oriented storage pool error");
                resMap.put("code", code);
                return resMap;
            }
            String object = responseEntity.getBody();
            if (!StringUtils.isEmpty(object)) {
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("datas").getAsJsonArray();
                for (JsonElement jsonElement:jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    StoragePool storagePool = new StoragePool();
                    storagePool.setName(element.get("name").getAsString());
                    storagePool.setFree_capacity(Double.valueOf(element.get("free_capacity").getAsString()));
                    storagePool.setHealth_status(element.get("health_status").getAsString());
                    storagePool.setLun_subscribed_capacity(Double.valueOf(element.get("lun_subscribed_capacity").getAsString()));
                    storagePool.setParent_type(element.get("parent_type").getAsString());
                    storagePool.setRunning_status(element.get("running_status").getAsString());
                    storagePool.setTotal_capacity(Double.valueOf(element.get("total_capacity").getAsString()));
                    storagePool.setFs_subscribed_capacity(Double.valueOf(element.get("fs_subscribed_capacity").getAsString()));
                    storagePool.setConsumed_capacity(Double.valueOf(element.get("consumed_capacity").getAsString()));
                    storagePool.setConsumed_capacity_percentage(element.get("consumed_capacity_percentage").getAsString());
                    storagePool.setConsumed_capacity_threshold(element.get("consumed_capacity_threshold").getAsString());
                    storagePool.setStorage_pool_id(element.get("storage_pool_id").getAsString());
                    storagePool.setStorage_name(element.get("storage_name").getAsString());
                    storagePool.setMedia_type(element.get("media_type").getAsString());
                    resList.add(storagePool);
                }
                resMap.put("data", resList);
            }
            return resMap;
        } catch (Exception e) {
            LOG.error("search oriented storage pool error", e);
            resMap.put("code", 503);
            resMap.put("msg",e.getMessage());
        }finally {
            return resMap;
        }
    }

    @Override
    public Map<String,Object> getLogicPorts(String storageId){

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "list logic ports success!");
        resMap.put("storageId", storageId);
        List<LogicPorts> resList = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        String url =  API_LOGICPORTS_LIST + storageId;
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            LOG.info("DmeStorageServiceImpl/getLogicPorts/responseEntity==" + responseEntity);
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                resMap.put("code",code);
                resMap.put("msg", "list bandports error!");
            }
            String object = responseEntity.getBody();
            if (!StringUtils.isEmpty(object)) {
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("logic_ports").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    LogicPorts logicPorts = new LogicPorts();
                    logicPorts.setId(element.get("id").getAsString());
                    logicPorts.setName(element.get("name").getAsString());
                    logicPorts.setRunning_status(element.get("running_status").getAsString());
                    logicPorts.setOperational_status(element.get("operational_status").getAsString());
                    logicPorts.setMgmt_ip(element.get("mgmt_ip").getAsString());
                    logicPorts.setMgmt_ipv6(element.get("mgmt_ipv6").getAsString());
                    logicPorts.setHome_port_id(element.get("home_port_id").getAsString());
                    logicPorts.setHome_port_name(element.get("home_port_name").getAsString());
                    logicPorts.setRole(element.get("role").getAsString());
                    logicPorts.setDdns_status(element.get("ddns_status").getAsString());
                    logicPorts.setCurrent_port_id(element.get("current_port_id").getAsString());
                    logicPorts.setCurrent_port_name(element.get("current_port_name").getAsString());
                    logicPorts.setSupport_protocol(element.get("support_protocol").getAsString());
                    logicPorts.setManagement_access(element.get("management_access").getAsString());
                    logicPorts.setVstore_id( element.get("vstore_id").getAsString());
                    logicPorts.setVstore_name(element.get("vstore_name").getAsString());
                    resList.add(logicPorts);
                }
                resMap.put("data", resList);
            }
            return resMap;
        } catch (Exception e) {
            LOG.error("list bandports error", e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());

        }finally {
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
        String url =  API_VOLUME_LIST + storageId;

        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            //ResponseEntity responseEntity = access(url, HttpMethod.GET,null);
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
                    volume.setId(element.get("id").getAsString());
                    volume.setName( element.get("name").getAsString());
                    volume.setStatus(element.get("status").getAsString());
                    volume.setAttached(Boolean.valueOf(element.get("attached").getAsString()));
                    volume.setAlloctype(element.get("alloctype").getAsString());
                    volume.setService_level_name(element.get("service_level_name").getAsString());
                    volume.setStorage_id(element.get("storage_id").getAsString());
                    volume.setPool_raw_id(element.get("pool_raw_id").getAsString());
                    volume.setCapacity_usage(element.get("capacity_usage").getAsString());
                    volume.setProtectionStatus( Boolean.valueOf(element.get("protectionStatus").getAsString()));
                    volumes.add(volume);
                }
                resMap.put("data", volumes);
            }
            return resMap;
        } catch (Exception e) {
            LOG.error("list volume error!");
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        }finally {
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
            ResponseEntity<String> responseEntity = dmeAccessService.access(API_FILESYSTEMS_LIST , HttpMethod.POST, jsonParams);
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
                    fileSystem.setId(element.get("id").getAsString());
                    fileSystem.setName(element.get("name").getAsString());
                    fileSystem.setHealth_status(element.get("health_status").getAsString());
                    fileSystem.setAlloc_type(element.get("alloc_type").getAsString());
                    fileSystem.setCapacity(Double.valueOf(element.get("capacity").getAsString()));
                    fileSystem.setCapacity_usage_ratio(Integer.valueOf(element.get("capacity_usage_ratio").getAsString()));
                    fileSystem.setStorage_pool_name(element.get("storage_pool_name").getAsString());
                    fileSystem.setNfs_count(Integer.valueOf(element.get("nfs_count").getAsString()));
                    fileSystem.setCifs_count(Integer.valueOf(element.get("cifs_count").getAsString()));
                    fileSystem.setDtree_count(Integer.valueOf(element.get("dtree_count").getAsString()));
                    fileSystems.add(fileSystem);
                }
                resMap.put("data", fileSystems);
            }
            return resMap;
        } catch (Exception e) {
            LOG.error("list filesystem error!",e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        }finally {
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
            if (code!=200) {
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
                    dtrees.setName(element.get("name").getAsString());
                    dtrees.setFs_name(element.get("fs_name").getAsString());
                    dtrees.setQuota_switch(Boolean.valueOf(element.get("quota_switch").getAsString()));
                    dtrees.setSecurity_style(element.get("security_style").getAsString());
                    dtrees.setTier_name(element.get("tier_name").getAsString());
                    dtrees.setNfs_count(Integer.valueOf(element.get("nfs_count").getAsString()));
                    resList.add(dtrees);
                }
                resMap.put("data", resList);
            }
            return resMap;
        } catch (Exception e) {
            LOG.error("list dtree error!",e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        }finally {
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
                    nfsShares.setName(element.get("name").getAsString());
                    nfsShares.setShare_path(element.get("share_path").getAsString());
                    nfsShares.setStorage_id(element.get("storage_id").getAsString());
                    nfsShares.setTier_name(element.get("tier_name").getAsString());
                    nfsShares.setOwning_dtree_name(element.get("owning_dtree_name").getAsString());
                    nfsShares.setOwning_dtree_id(element.get("owning_dtree_id").getAsString());
                    nfsShares.setFs_name(element.get("fs_name").getAsString());
                    resList.add(nfsShares);
                }
                resMap.put("data", resList);
            }
            return resMap;
        } catch (Exception e) {
            LOG.error("list nfsshares error!",e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        }finally {
            return resMap;
        }
    }

    @Override
    public Map<String,Object> getBandPorts(String storageId){

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "list band ports success!");
        resMap.put("storageId", storageId);
        List<BandPorts> resList = new ArrayList<>();

        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(API_BANDPORTS_LIST, HttpMethod.GET, null);
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
                    bandPorts.setId(element.get("id").getAsString());
                    bandPorts.setName(element.get("name").getAsString());
                    bandPorts.setHealth_status(element.get("health_status").getAsString());
                    bandPorts.setRunning_status(element.get("running_status").getAsString());
                    bandPorts.setMtu(element.get("mtu").getAsString());
                    resList.add(bandPorts);
                }
                resMap.put("data", resList);
            }
            return resMap;

        } catch (Exception e) {
            LOG.error("list bandports error!",e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        }finally {
            return resMap;
        }
    }

    @Override
    public Map<String,Object> getStorageControllers(){

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
                    storageControllers.setName(element.get("name").getAsString());
                    storageControllers.setSoftVer(element.get("softVer").getAsString());
                    storageControllers.setStatus(element.get("status").getAsString());
                    storageControllers.setCpuInfo( element.get("cpuInfo").getAsString());
                    resList.add(storageControllers);
                }
                resMap.put("data", resList);
            }
            return resMap;
        } catch (Exception e) {
            LOG.error("list storage controller error!");
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        }finally {
            return resMap;
        }


    }

    @Override
    public Map<String,Object> getStorageDisks(){

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
                    storageDisk.setName(element.get("name").getAsString());
                    storageDisk.setStatus(element.get("status").getAsString());
                    storageDisk.setCapacity(element.get("capacity").getAsString());
                    resList.add(storageDisk);
                }
                resMap.put("data", resList);
            }
            return resMap;
        } catch (Exception e) {
            LOG.error( "list storage disk error!",e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        }finally {
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
    public List<EthPortInfo> getStorageEthPorts(String storageSn) throws Exception{
        List<EthPortInfo> relists = null;
        try {
            if (!StringUtils.isEmpty(storageSn)) {
                //通过存储设备的sn查询 存储设备的资源ID
                String dsResId = getStorageResIdBySn(storageSn);
                if(!StringUtils.isEmpty(dsResId)){
                    relists = getEthPortsByResId(dsResId);
                }else{
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
    public String getStorageResIdBySn(String storageSn){
        String dsResId = null;
        try {
            if (!StringUtils.isEmpty(storageSn)) {
                String stordeviceIdUrl = DmeConstants.DME_RES_STORDEVICEID_QUERY_URL;
                JsonObject condition = new JsonObject();

                JsonArray constraint = new JsonArray();

                JsonObject consObj = new JsonObject();
                JsonObject simple = new JsonObject();
                simple.addProperty("name","dataStatus");
                simple.addProperty("operator","equal");
                simple.addProperty("value","normal");
                consObj.add("simple",simple);
                constraint.add(consObj);

                JsonObject consObj1 = new JsonObject();
                JsonObject simple1 = new JsonObject();
                simple1.addProperty("name","sn");
                simple1.addProperty("operator","equal");
                simple1.addProperty("value",storageSn);
                consObj1.add("simple",simple1);
                consObj1.addProperty("logOp","and");
                constraint.add(consObj1);

                condition.add("constraint",constraint);

                stordeviceIdUrl = stordeviceIdUrl+"?condition={json}";
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
    public List<EthPortInfo> getEthPortsByResId(String dsResId){
        List<EthPortInfo> relists = null;
        try {
            if (!StringUtils.isEmpty(dsResId)) {
                String ethPortUrl = DmeConstants.DME_STORDEVICE_ETHPORT_QUERY_URL;
                JsonObject condition = new JsonObject();

                JsonArray constraint = new JsonArray();

                JsonObject consObj = new JsonObject();
                JsonObject simple = new JsonObject();
                simple.addProperty("name","dataStatus");
                simple.addProperty("operator","equal");
                simple.addProperty("value","normal");
                consObj.add("simple",simple);
                constraint.add(consObj);

                JsonObject consObj1 = new JsonObject();
                JsonObject simple1 = new JsonObject();
                simple1.addProperty("name","portType");
                simple1.addProperty("operator","equal");
                simple1.addProperty("value","ETH");
                consObj1.add("simple",simple1);
                consObj1.addProperty("logOp","and");
                constraint.add(consObj1);

                JsonObject consObj2 = new JsonObject();
                JsonObject simple2 = new JsonObject();
                simple2.addProperty("name","storageDeviceId");
                simple2.addProperty("operator","equal");
                simple2.addProperty("value",dsResId);
                consObj2.add("simple",simple2);
                consObj2.addProperty("logOp","and");
                constraint.add(consObj2);

                condition.add("constraint",constraint);

                ethPortUrl = ethPortUrl+"?condition={json}";
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
                                        ethPort.setLastMonitorTime(ToolUtils.jsonToLon(vjson.get("lastMonitorTime"),0L));
                                        ethPort.setMgmtIp(ToolUtils.jsonToStr(vjson.get("mgmtIp")));
                                        ethPort.setConfirmStatus(ToolUtils.jsonToStr(vjson.get("confirmStatus")));
                                        ethPort.setId(ToolUtils.jsonToStr(vjson.get("id")));
                                        ethPort.setLastModified(ToolUtils.jsonToLon(vjson.get("last_Modified"),0L));
                                        ethPort.setConnectStatus(ToolUtils.jsonToStr(vjson.get("connectStatus")));
                                        ethPort.setClassId(ToolUtils.jsonToInt(vjson.get("classId"),0));
                                        ethPort.setDataStatus(ToolUtils.jsonToStr(vjson.get("dataStatus")));
                                        ethPort.setMaxSpeed(ToolUtils.jsonToInt(vjson.get("maxSpeed"),0));
                                        ethPort.setResId(ToolUtils.jsonToStr(vjson.get("resId")));
                                        ethPort.setLocal(ToolUtils.jsonToBoo(vjson.get("isLocal")));
                                        ethPort.setPortType(ToolUtils.jsonToStr(vjson.get("portType")));
                                        ethPort.setClassName(ToolUtils.jsonToStr(vjson.get("className")));
                                        ethPort.setNumberOfInitiators(ToolUtils.jsonToInt(vjson.get("numberOfInitiators"),0));
                                        ethPort.setBondId(ToolUtils.jsonToStr(vjson.get("bondId")));
                                        ethPort.setRegionId(ToolUtils.jsonToStr(vjson.get("regionId")));
                                        ethPort.setName(ToolUtils.jsonToStr(vjson.get("name")));
                                        ethPort.setLocation(ToolUtils.jsonToStr(vjson.get("location")));
                                        ethPort.setNativeId(ToolUtils.jsonToStr(vjson.get("nativeId")));
                                        ethPort.setDataSource(ToolUtils.jsonToStr(vjson.get("dataSource")));
                                        ethPort.setIpv6Mask(ToolUtils.jsonToStr(vjson.get("ipv6Mask")));
                                        ethPort.setStatus(ToolUtils.jsonToStr(vjson.get("status")));
                                        ethPort.setSpeed(ToolUtils.jsonToInt(vjson.get("speed"),0));
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

        List<Volume> volumes = new ArrayList<>();

        //String reqPath = "/rest/blockservice/v1/volumes/{volume_id}";
        String reqPath = "https://localhost:26335/rest/blockservice/v1/volumes";
        String url = reqPath + "/" + volumeId;

        try {
            //ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            ResponseEntity responseEntity = access(url, HttpMethod.GET, null);
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
                volume.setId(element.get("id").getAsString());
                volume.setName(element.get("name").getAsString());
                volume.setStatus(element.get("status").getAsString());
                volume.setAttached(Boolean.valueOf(element.get("attached").getAsString()));
                volume.setAlloctype(element.get("alloctype").getAsString());
                volume.setService_level_name(element.get("service_level_name").getAsString());
                volume.setStorage_id(element.get("storage_id").getAsString());
                volume.setPool_raw_id(element.get("pool_raw_id").getAsString());
                volume.setCapacity_usage(element.get("capacity_usage").getAsString());
                volume.setProtectionStatus(Boolean.valueOf(element.get("protectionStatus").getAsString()));
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
}
