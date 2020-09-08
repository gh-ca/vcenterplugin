package com.dmeplugin.dmestore.services;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.dmeplugin.dmestore.model.*;
import com.dmeplugin.dmestore.utils.HttpRequestUtil;
import com.dmeplugin.dmestore.utils.RestUtils;
import com.dmeplugin.dmestore.utils.StringUtil;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final Logger LOG = LoggerFactory.getLogger(DmeStorageServiceImpl.class);

    @Autowired
    private Gson gson;

    @Autowired
    private DmeAccessService dmeAccessServiceImpl;

    private static final Logger Log = LoggerFactory.getLogger(DmeStorageServiceImpl.class);

    @Override
    public Map<String, Object> getStorages() {

        Map<String, Object> objMap = new HashMap<>();
        objMap.put("code", "200");
        objMap.put("msg", "list storage success!");
        List<Storage> list = new ArrayList<>();

        //String apiUrl = "/rest/storagemgmt/v1/storages";
        String apiUrl = "https://localhost:26335/rest/storagemgmt/v1/storages";
        //String param = HttpRequestUtil.concatParam(params);
        String url = apiUrl ;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        try {
            //ResponseEntity<String> responseEntity = dmeAccessServiceImpl.access(url, HttpMethod.GET, null);
            //Log.info("DmeStorageServiceImpl/getStorages/responseEntity==" + responseEntity);
            ResponseEntity responseEntity = sendHttpRequest(url, HttpMethod.GET, headers);
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
                    String id = jsonObj.get("id").getAsString();
                    String name = jsonObj.get("name").getAsString();
                    String ip = jsonObj.get("ip").getAsString();
                    String status = jsonObj.get("status").getAsString();
                    String synStatus = jsonObj.get("syn_status").getAsString();
                    String vendor = jsonObj.get("vendor").getAsString();
                    String model = jsonObj.get("model").getAsString();
                    Double usedCapacity = Double.valueOf(jsonObj.get("used_capacity").getAsString());
                    Double totalCapacity = Double.valueOf(jsonObj.get("total_capacity").getAsString());
                    Double totalEffectiveCapacity = Double.valueOf(jsonObj.get("total_effective_capacity").getAsString());
                    Double freeEffectiveCapacity = Double.valueOf(jsonObj.get("free_effective_capacity").getAsString());
                    Double maxCpuUtilization = Double.valueOf(jsonObj.get("max_cpu_utilization").getAsString());
                    Double maxIops = Double.valueOf(jsonObj.get("max_iops").getAsString());
                    Double maxBandwidth = Double.valueOf(jsonObj.get("max_bandwidth").getAsString());
                    Double maxLatency = Double.valueOf(jsonObj.get("max_latency").getAsString());
                    String zaIds = jsonObj.get("az_ids").getAsString();
                    String[] az_ids = {zaIds};

                    Storage storageObj = new Storage();
                    storageObj.setId(id);
                    storageObj.setName(name);
                    storageObj.setIp(ip);
                    storageObj.setStatus(status);
                    storageObj.setSynStatus(synStatus);
                    storageObj.setVendor(vendor);
                    storageObj.setModel(model);
                    storageObj.setUsedCapacity(usedCapacity);
                    storageObj.setTotalCapacity(totalCapacity);
                    storageObj.setTotalEffectiveCapacity(totalEffectiveCapacity);
                    storageObj.setFreeEffectiveCapacity(freeEffectiveCapacity);
                    storageObj.setMaxCpuUtilization(maxCpuUtilization);
                    storageObj.setMaxIops(maxIops);
                    storageObj.setMaxBandwidth(maxBandwidth);
                    storageObj.setMaxLatency(maxLatency);
                    storageObj.setAzIds(az_ids);

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
    //    "description": "Could not write JSON: For input string: \"2f84a672-48e7-4892-a7b4-96c4c9d403c8\"; nested exception is com.fasterxml.jackson.databind.JsonMappingException: For input string: \"2f84a672-48e7-4892-a7b4-96c4c9d403c8\" (through reference chain: com.dmeplugin.dmestore.model.ResponseBodyBean[\"data\"]->java.util.HashMap[\"data\"]->java.util.ArrayList[0]->com.dmeplugin.dmestore.model.Storage[\"azIds\"]->java.util.Arrays$ArrayList[0]->com.google.gson.JsonArray[\"asInt\"])"
    }

    @Override
    public Map<String, Object> getStorageDetail(String storageId) {
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "search oriented storage success");
        resMap.put("data", storageId);

        String url = "/rest/storagemgmt/v1/storages/" + storageId + "/detail";
        try {
            ResponseEntity<String> responseEntity = dmeAccessServiceImpl.access(url, HttpMethod.GET, null);
            Log.info("DmeStorageServiceImpl/getStorageDetail/responseEntity==" + responseEntity);
            //ResponseEntity responseEntity = sendHttpRequest(url, HttpMethod.GET, headers);
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                resMap.put("code", code);
                resMap.put("msg", "search oriented storage error");
                return resMap;
            }
            Object object = responseEntity.getBody();
            if (object != null) {
                JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
                String id = jsonObject.get("id").getAsString();
                String name = jsonObject.get("name").getAsString();
                String ip = jsonObject.get("ip").getAsString();
                String status = jsonObject.get("status").getAsString();
                String synStatus = jsonObject.get("syn_status").getAsString();
                String vendor = jsonObject.get("vendor").getAsString();
                String model = jsonObject.get("model").getAsString();
                Long usedCapacity = Long.valueOf(jsonObject.get("used_capacity").getAsString());
                Long totalCapacity = Long.valueOf(jsonObject.get("total_capacity").getAsString());
                Long totalEffectiveCapacity = Long.valueOf(jsonObject.get("total_effective_capacity").getAsString());
                Long freeEffectiveCapacity = Long.valueOf(jsonObject.get("free_effective_capacity").getAsString());
                JsonArray azIds = jsonObject.get("azIds").getAsJsonArray();
                List<JsonArray> jsonArrays = Arrays.asList(azIds);

                StorageDetail storageObj = new StorageDetail();
                storageObj.setId(id);
                storageObj.setName(name);
                storageObj.setIp(ip);
                storageObj.setStatus(status);
                storageObj.setSynStatus(synStatus);
                storageObj.setVendor(vendor);
                storageObj.setModel(model);
                storageObj.setUsedCapacity(usedCapacity);
                storageObj.setTotalCapacity(totalCapacity);
                storageObj.setTotalEffectiveCapacity(totalEffectiveCapacity);
                storageObj.setFreeEffectiveCapacity(freeEffectiveCapacity);
                storageObj.setAzIds(jsonArrays);

                Map<String, Object> storagePool = getStoragePools(storageId);
                String jsonStoragePool = gson.toJson(storagePool.get("data"));
                storageObj.setStoragePool(jsonStoragePool);

                Map<String, Object> volume = getVolumes(storageId);
                String jsonVolume = gson.toJson(volume.get("data"));
                storageObj.setVolume(jsonVolume);

                Map<String, Object> fileSystem = getFileSystems(storageId);
                String jsonFileSystem = gson.toJson(fileSystem.get("data"));
                storageObj.setFileSystem(jsonFileSystem);

                Map<String, Object> dTrees = getDTrees(storageId);
                String jsonDtress = gson.toJson(dTrees.get("data"));
                storageObj.setdTrees(jsonDtress);

                Map<String, Object> nfsShare = getNfsShares(storageId);
                String jsonNfsShares = gson.toJson(nfsShare.get("data"));
                storageObj.setNfsShares(jsonNfsShares);

                Map<String, Object> bandPorts = getBandPorts(storageId);
                String jsonBandPorts = gson.toJson(bandPorts.get("data"));
                storageObj.setBandPorts(jsonBandPorts);

                Map<String, Object> logicPorts = getLogicPorts(storageId);
                String jsonLogicPorts = gson.toJson(logicPorts.get("data"));
                storageObj.setLogicPorts(jsonLogicPorts);

                Map<String, Object> storageControllers = getStorageControllers();
                String jsonStorageControllers = gson.toJson(storageControllers.get("data"));
                storageObj.setStorageControllers(jsonStorageControllers);

                Map<String, Object> storageDisks = getStorageDisks();
                String jsonStorageDisks = gson.toJson(storageDisks.get("data"));
                storageObj.setStorageDisks(jsonStorageDisks);

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

        String url = "/rest/storagemgmt/v1/storagepools/query";

        Map<String, String> params = new HashMap<>();
        params.put("storage_id", storageId);

        try {
            //ResponseEntity<String> responseEntity = HttpRequestUtil.requestWithBody(url, HttpMethod.POST, headers, gson.toJson(params), String.class);
            ResponseEntity<String> responseEntity = dmeAccessServiceImpl.access(url, HttpMethod.POST, gson.toJson(params));
            Log.info("DmeStorageServiceImpl/getStoragePools/responseEntity==" + responseEntity);
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
                    String name = element.get("name").getAsString();
                    Double free_capacity = Double.valueOf(element.get("free_capacity").getAsString());
                    String health_status = element.get("health_status").getAsString();
                    Double lun_subscribed_capacity = Double.valueOf(element.get("lun_subscribed_capacity").getAsString());
                    String parent_type = element.get("parent_type").getAsString();
                    String running_status = element.get("running_status").getAsString();
                    Double total_capacity = Double.valueOf(element.get("total_capacity").getAsString());
                    Double fs_subscribed_capacity = Double.valueOf(element.get("fs_subscribed_capacity").getAsString());
                    Double consumed_capacity = Double.valueOf(element.get("consumed_capacity").getAsString());
                    String consumed_capacity_percentage = element.get("consumed_capacity_percentage").getAsString();
                    String consumed_capacity_threshold = element.get("consumed_capacity_threshold").getAsString();

                    StoragePool storagePool = new StoragePool();
                    storagePool.setName(name);
                    storagePool.setFree_capacity(free_capacity);
                    storagePool.setHealth_status(health_status);
                    storagePool.setLun_subscribed_capacity(lun_subscribed_capacity);
                    storagePool.setParent_type(parent_type);
                    storagePool.setRunning_status(running_status);
                    storagePool.setTotal_capacity(total_capacity);
                    storagePool.setFs_subscribed_capacity(fs_subscribed_capacity);
                    storagePool.setConsumed_capacity(consumed_capacity);
                    storagePool.setConsumed_capacity_percentage(consumed_capacity_percentage);
                    storagePool.setConsumed_capacity_threshold(consumed_capacity_threshold);

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

    private Map<String, Object> getVolumes(String storageId) {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "list volumes success!");
        resMap.put("storageId", storageId);

        List<Volume> volumes = new ArrayList<>();

        String reqPath = "/rest/blockservice/v1/volumes";
        String url = reqPath + "? storageId = "+ storageId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        try {
            ResponseEntity<String> responseEntity = dmeAccessServiceImpl.access(url, HttpMethod.GET, null);
            //ResponseEntity responseEntity = sendHttpRequest(url, HttpMethod.GET, headers);
            Log.info("DmeStorageServiceImpl/getVolumes/responseEntity==" + responseEntity);
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
                    String id = element.get("id").getAsString();
                    String name = element.get("name").getAsString();
                    String status = element.get("status").getAsString();
                    Boolean attached = Boolean.valueOf(element.get("attached").getAsString());
                    String alloctype = element.get("alloctype").getAsString();
                    String service_level_name = element.get("service_level_name").getAsString();
                    String storage_id = element.get("storage_id").getAsString();
                    String pool_raw_id = element.get("pool_raw_id").getAsString();
                    String capacity_usage = element.get("capacity_usage").getAsString();
                    Boolean protectionStatus = Boolean.valueOf(element.get("protectionStatus").getAsString());

                    Volume volume = new Volume();
                    volume.setId(id);
                    volume.setName(name);
                    volume.setStatus(status);
                    volume.setAttached(attached);
                    volume.setAlloctype(alloctype);
                    volume.setService_level_name(service_level_name);
                    volume.setStorage_id(storage_id);
                    volume.setPool_raw_id(pool_raw_id);
                    volume.setCapacity_usage(capacity_usage);
                    volume.setProtectionStatus(protectionStatus);

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

    private Map<String, Object> getFileSystems(String storageId) {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "list filesystem success!");
        resMap.put("storageId", storageId);

        List<FileSystem> fileSystems = new ArrayList<>();

        String hostIp = "";
        String reqPath = "/rest/fileservice/v1/filesystems/query";
        String url = "https://" + hostIp + reqPath;

        Map<String, String> params = new HashMap<>();
        params.put("storage_id", storageId);
        String jsonParams = gson.toJson(params);

        try {
            //ResponseEntity<String> responseEntity = HttpRequestUtil.requestWithBody(url, HttpMethod.POST, headers, jsonParams, String.class);
            ResponseEntity<String> responseEntity = dmeAccessServiceImpl.access(url, HttpMethod.POST, jsonParams);
            Log.info("DmeStorageServiceImpl/getFileSystems/responseEntity==" + responseEntity);
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
                    String id = element.get("id").getAsString();
                    String name = element.get("name").getAsString();
                    String health_status = element.get("health_status").getAsString();
                    String alloc_type = element.get("alloc_type").getAsString();
                    Double capacity = Double.valueOf(element.get("capacity").getAsString());
                    Integer capacity_usage_ratio = Integer.valueOf(element.get("capacity_usage_ratio").getAsString());
                    String storage_pool_name = element.get("storage_pool_name").getAsString();
                    Integer nfs_count = Integer.valueOf(element.get("nfs_count").getAsString());
                    Integer cifs_count = Integer.valueOf(element.get("cifs_count").getAsString());
                    Integer dtree_count = Integer.valueOf(element.get("dtree_count").getAsString());

                    FileSystem fileSystem = new FileSystem();
                    fileSystem.setId(id);
                    fileSystem.setName(name);
                    fileSystem.setHealth_status(health_status);
                    fileSystem.setAlloc_type(alloc_type);
                    fileSystem.setCapacity(capacity);
                    fileSystem.setCapacity_usage_ratio(capacity_usage_ratio);
                    fileSystem.setStorage_pool_name(storage_pool_name);
                    fileSystem.setNfs_count(nfs_count);
                    fileSystem.setCifs_count(cifs_count);
                    fileSystem.setDtree_count(dtree_count);

                    fileSystems.add(fileSystem);
                }
                resMap.put("data", fileSystems);
            }
            return resMap;
        } catch (Exception e) {
            Log.error("list filesystem error!",e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        }finally {
            return resMap;
        }
    }

    private Map<String, Object> getDTrees(String storageId) {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "list dtree success!");
        resMap.put("storageId", storageId);

        //String hostIp = "";
        String url = "/rest/fileservice/v1/dtrees/summary";
        //String url = "https://" + hostIp + reqPath;

        List<Dtrees> resList = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        params.put("storage_id", storageId);
        String jsonParams = gson.toJson(params);

        try {
            //ResponseEntity<String> responseEntity = HttpRequestUtil.requestWithBody(url, HttpMethod.POST, headers, jsonParams, String.class);
            ResponseEntity<String> responseEntity = dmeAccessServiceImpl.access(url, HttpMethod.POST, jsonParams);
            Log.info("DmeStorageServiceImpl/getDTrees/responseEntity==" + responseEntity);
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
                    String name = element.get("name").getAsString();
                    String fs_name = element.get("fs_name").getAsString();
                    Boolean quota_switch = Boolean.valueOf(element.get("quota_switch").getAsString());
                    String security_style = element.get("security_style").getAsString();
                    String tier_name = element.get("tier_name").getAsString();
                    Integer nfs_count = Integer.valueOf(element.get("nfs_count").getAsString());

                    Dtrees dtrees = new Dtrees();
                    dtrees.setName(name);
                    dtrees.setFs_name(fs_name);
                    dtrees.setQuota_switch(quota_switch);
                    dtrees.setSecurity_style(security_style);
                    dtrees.setTier_name(tier_name);
                    dtrees.setNfs_count(nfs_count);

                    resList.add(dtrees);
                }
                resMap.put("data", resList);
            }
            return resMap;
        } catch (Exception e) {
            Log.error("list dtree error!",e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        }finally {
            return resMap;
        }
    }

    private Map<String, Object> getNfsShares(String storageId) {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "list nfsshares success!");
        resMap.put("storageId", storageId);
        List<NfsShares> resList = new ArrayList<>();

        String url = "/rest/fileservice/v1/nfs-shares/summary";

        Map<String, String> params = new HashMap<>();
        params.put("storage_id", storageId);
        String jsonParams = gson.toJson(params);

        try {
            //ResponseEntity<String> responseEntity = HttpRequestUtil.requestWithBody(url, HttpMethod.POST, headers, jsonParams, String.class);
            ResponseEntity<String> responseEntity = dmeAccessServiceImpl.access(url, HttpMethod.POST, jsonParams);
            Log.info("DmeStorageServiceImpl/getNfsShares/responseEntity==" + responseEntity);
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
                    String name = element.get("name").getAsString();
                    String share_path = element.get("share_path").getAsString();
                    String storage_id = element.get("storage_id").getAsString();
                    String tier_name = element.get("tier_name").getAsString();
                    String owning_dtree_name = element.get("owning_dtree_name").getAsString();
                    String fs_name = element.get("fs_name").getAsString();
                    String owning_dtree_id = element.get("owning_dtree_id").getAsString();

                    NfsShares nfsShares = new NfsShares();
                    nfsShares.setName(name);
                    nfsShares.setShare_path(share_path);
                    nfsShares.setStorage_id(storage_id);
                    nfsShares.setTier_name(tier_name);
                    nfsShares.setOwning_dtree_name(owning_dtree_name);
                    nfsShares.setOwning_dtree_id(owning_dtree_id);
                    nfsShares.setFs_name(fs_name);

                    resList.add(nfsShares);
                }
                resMap.put("data", resList);
            }
            return resMap;
        } catch (Exception e) {
            Log.error("list nfsshares error!",e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        }finally {
            return resMap;
        }
    }

    private Map<String,Object> getBandPorts(String storageId){

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "list band ports success!");
        resMap.put("storageId", storageId);
        List<BandPorts> resList = new ArrayList<>();

        String url = "/rest/storagemgmt/v1/storage-port/bond-ports?storage_id"+storageId;
        try {
            ResponseEntity<String> responseEntity = dmeAccessServiceImpl.access(url, HttpMethod.GET, null);
            Log.info("DmeStorageServiceImpl/getBandPorts/responseEntity==" + responseEntity);
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
                    String id = element.get("id").getAsString();
                    String name = element.get("name").getAsString();
                    String health_status = element.get("health_status").getAsString();
                    String running_status = element.get("running_status").getAsString();
                    String mtu = element.get("mtu").getAsString();

                    BandPorts bandPorts = new BandPorts();
                    bandPorts.setId(id);
                    bandPorts.setName(name);
                    bandPorts.setHealth_status(health_status);
                    bandPorts.setRunning_status(running_status);
                    bandPorts.setMtu(mtu);

                    resList.add(bandPorts);
                }
                resMap.put("data", resList);
            }
            return resMap;

        } catch (Exception e) {
            Log.error("list bandports error!",e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        }finally {
            return resMap;
        }
    }

    private Map<String,Object> getLogicPorts(String storageId){

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "list logic ports success!");
        resMap.put("storageId", storageId);
        List<LogicPorts> resList = new ArrayList<>();

        String url = "/rest/storagemgmt/v1/storage-port/logic-ports?storage_id"+storageId;
        try {
            ResponseEntity<String> responseEntity = dmeAccessServiceImpl.access(url, HttpMethod.GET, null);
            Log.info("DmeStorageServiceImpl/getLogicPorts/responseEntity==" + responseEntity);
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
                    String id = element.get("id").getAsString();
                    String name = element.get("name").getAsString();
                    String running_status = element.get("running_status").getAsString();
                    String operational_status = element.get("operational_status").getAsString();
                    String mgmt_ip = element.get("mgmt_ip").getAsString();
                    String mgmt_ipv6 = element.get("mgmt_ipv6").getAsString();
                    String home_port_id = element.get("home_port_id").getAsString();
                    String home_port_name = element.get("home_port_name").getAsString();
                    String current_port_id = element.get("current_port_id").getAsString();
                    String current_port_name = element.get("current_port_name").getAsString();
                    String role = element.get("role").getAsString();
                    String ddns_status = element.get("ddns_status").getAsString();
                    String support_protocol = element.get("support_protocol").getAsString();
                    String management_access = element.get("management_access").getAsString();
                    String vstore_id = element.get("vstore_id").getAsString();
                    String vstore_name = element.get("vstore_name").getAsString();

                    LogicPorts logicPorts = new LogicPorts();
                    logicPorts.setId(id);
                    logicPorts.setName(name);
                    logicPorts.setRunning_status(running_status);
                    logicPorts.setOperational_status(operational_status);
                    logicPorts.setMgmt_ip(mgmt_ip);
                    logicPorts.setMgmt_ipv6(mgmt_ipv6);
                    logicPorts.setHome_port_id(home_port_id);
                    logicPorts.setHome_port_name(home_port_name);
                    logicPorts.setRole(role);
                    logicPorts.setDdns_status(ddns_status);
                    logicPorts.setCurrent_port_id(current_port_id);
                    logicPorts.setCurrent_port_name(current_port_name);
                    logicPorts.setSupport_protocol(support_protocol);
                    logicPorts.setManagement_access(management_access);
                    logicPorts.setVstore_id(vstore_id);
                    logicPorts.setVstore_name(vstore_name);

                    resList.add(logicPorts);
                }
                resMap.put("data", resList);
            }
            return resMap;
        } catch (Exception e) {
            Log.error("list bandports error", e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());

        }finally {
            return resMap;
        }
    }

    private Map<String,Object> getStorageControllers(){

        String className = "SYS_Controller";
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "list storage controller success!");
        resMap.put("storageId", className);
        List<StorageControllers> resList = new ArrayList<>();

        String url = "/rest/resourcedb/v1/instances/"+className;
        try {
            ResponseEntity<String> responseEntity = dmeAccessServiceImpl.access(url, HttpMethod.GET, null);
            Log.info("DmeStorageServiceImpl/getStorageControllers/responseEntity==" + responseEntity);
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                resMap.put("code", code);
                resMap.put("msg", "list storage controller error!");
                return resMap;
            }
            String object = responseEntity.getBody();
            if (!StringUtils.isEmpty(object)) {
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("objList").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    String name = element.get("name").getAsString();
                    String status = element.get("status").getAsString();
                    String softVer = element.get("softVer").getAsString();
                    String cpuInfo = element.get("cpuInfo").getAsString();

                    StorageControllers storageControllers = new StorageControllers();
                    storageControllers.setName(name);
                    storageControllers.setSoftVer(softVer);
                    storageControllers.setStatus(status);
                    storageControllers.setCpuInfo(cpuInfo);

                    resList.add(storageControllers);
                }
                resMap.put("data", resList);
            }
            return resMap;
        } catch (Exception e) {
            Log.error("list storage controller error!");
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        }finally {
            return resMap;
        }


    }

    private Map<String,Object> getStorageDisks(){

        String className = "SYS_StorageDisk";
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "list storage disk success!");
        resMap.put("storageId", className);
        List<StorageDisk> resList = new ArrayList<>();

        String url = "/rest/resourcedb/v1/instances/"+className;

        try {
            ResponseEntity<String> responseEntity = dmeAccessServiceImpl.access(url, HttpMethod.GET, null);
            Log.info("DmeStorageServiceImpl/getStorageDisks/responseEntity==" + responseEntity);
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                resMap.put("code", code);
                resMap.put("msg", "list storage disk error!");
                return resMap;
            }
            String object = responseEntity.getBody();
            if (!StringUtils.isEmpty(object)) {
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("objList").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    String name = element.get("name").getAsString();
                    String status = element.get("status").getAsString();
                    String capacity = element.get("capacity").getAsString();

                    StorageDisk storageDisk = new StorageDisk();
                    storageDisk.setName(name);
                    storageDisk.setStatus(status);
                    storageDisk.setCapacity(capacity);

                    resList.add(storageDisk);
                }
                resMap.put("data", resList);
            }
            return resMap;
        } catch (Exception e) {
            Log.error( "list storage disk error!",e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        }finally {
            return resMap;
        }

    }

    private ResponseEntity sendHttpRequest(String url, HttpMethod method, HttpHeaders header) throws Exception {

        RestUtils restUtils = new RestUtils();
        RestTemplate restTemplate = restUtils.getRestTemplate();

        HttpEntity<String> entity = new HttpEntity<>(header);
        ResponseEntity responseEntity = restTemplate.exchange(url, method, entity, String.class);

        if (403 == responseEntity.getStatusCodeValue() || 401 == responseEntity.getStatusCodeValue()) {
            responseEntity = restTemplate.exchange(url, method, entity, String.class);
        }
        return responseEntity;
    }
}
