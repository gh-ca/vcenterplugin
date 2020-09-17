package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.*;
import com.dmeplugin.dmestore.utils.HttpRequestUtil;
import com.dmeplugin.dmestore.utils.RestUtils;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


    private Gson gson=new Gson();

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
        //test url
        String url = "https://localhost:26335/rest/storagemgmt/v1/storages";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        try {
            //ResponseEntity<String> responseEntity = dmeAccessServiceImpl.access(url, HttpMethod.GET, null);
            ResponseEntity responseEntity = sendHttpRequest(url, HttpMethod.GET, headers);
            Log.info("DmeStorageServiceImpl/getStorages/responseEntity==" + responseEntity);
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

        //String url = "/rest/storagemgmt/v1/storages/" + storageId + "/detail";
        String url = "https://localhost:26335/rest/storagemgmt/v1/storages/storage_id/detail";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        try {
            //ResponseEntity<String> responseEntity = dmeAccessServiceImpl.access(url, HttpMethod.GET, null);
            ResponseEntity responseEntity = sendHttpRequest(url, HttpMethod.GET, headers);
            Log.info("DmeStorageServiceImpl/getStorageDetail/responseEntity==" + responseEntity);
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

        //String url = "/rest/storagemgmt/v1/storagepools/query";
        String url = "https://localhost:26335/rest/storagemgmt/v1/storagepools/query";

        Map<String, String> params = new HashMap<>();
        params.put("storage_id", storageId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        try {
            ResponseEntity<String> responseEntity = HttpRequestUtil.requestWithBody(url, HttpMethod.POST, headers, gson.toJson(params), String.class);
            //ResponseEntity<String> responseEntity = dmeAccessServiceImpl.access(url, HttpMethod.POST, gson.toJson(params));
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

        //String reqPath = "/rest/blockservice/v1/volumes";
        String reqPath = "https://localhost:26335/rest/blockservice/v1/volumes";
        String url = reqPath + "? storageId = "+ storageId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        try {
            //ResponseEntity<String> responseEntity = dmeAccessServiceImpl.access(url, HttpMethod.GET, null);
            ResponseEntity responseEntity = sendHttpRequest(url, HttpMethod.GET, headers);
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

    private Map<String, Object> getFileSystems(String storageId) {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "list filesystem success!");
        resMap.put("storageId", storageId);

        List<FileSystem> fileSystems = new ArrayList<>();

        String hostIp = "";
        String reqPath = "https://localhost:26335/rest/fileservice/v1/filesystems/query";
        String url = "https://" + hostIp + reqPath;

        Map<String, String> params = new HashMap<>();
        params.put("storage_id", storageId);
        String jsonParams = gson.toJson(params);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        try {
            ResponseEntity<String> responseEntity = HttpRequestUtil.requestWithBody(url, HttpMethod.POST, headers, jsonParams, String.class);
            //ResponseEntity<String> responseEntity = dmeAccessServiceImpl.access(url, HttpMethod.POST, jsonParams);
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
        String url = "https://localhost:26335/rest/fileservice/v1/dtrees/summary";
        //String url = "https://" + hostIp + reqPath;

        List<Dtrees> resList = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        params.put("storage_id", storageId);
        String jsonParams = gson.toJson(params);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        try {
            ResponseEntity<String> responseEntity = HttpRequestUtil.requestWithBody(url, HttpMethod.POST, headers, jsonParams, String.class);
            //ResponseEntity<String> responseEntity = dmeAccessServiceImpl.access(url, HttpMethod.POST, jsonParams);
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
        String url = "https://localhost:26335/rest/fileservice/v1/nfs-shares/summary";

        Map<String, String> params = new HashMap<>();
        params.put("storage_id", storageId);
        String jsonParams = gson.toJson(params);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        String url = "https://localhost:26335/rest/storagemgmt/v1/storage-port/bond-ports?storage_id"+storageId;
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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        String url = "https://localhost:26335/rest/storagemgmt/v1/storage-port/logic-ports?storage_id="+storageId;
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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        String url = "https://localhost:26335/rest/resourcedb/v1/instances/"+className;
        try {
            //ResponseEntity<String> responseEntity = dmeAccessServiceImpl.access(url, HttpMethod.GET, null);
            ResponseEntity responseEntity = sendHttpRequest(url, HttpMethod.GET, headers);
            Log.info("DmeStorageServiceImpl/getStorageControllers/responseEntity==" + responseEntity);
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

        String url = "https://localhost:26335/rest/resourcedb/v1/instances/"+className;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        try {
            //ResponseEntity<String> responseEntity = dmeAccessServiceImpl.access(url, HttpMethod.GET, null);
            ResponseEntity responseEntity = sendHttpRequest(url, HttpMethod.GET, headers);
            Log.info("DmeStorageServiceImpl/getStorageDisks/responseEntity==" + responseEntity);
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
    private ResponseEntity<String> access(String url, HttpMethod method, String requestBody) throws Exception {

        RestUtils restUtils = new RestUtils();
        RestTemplate restTemplate = restUtils.getRestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, entity, String.class);
        LOG.info(url + "==responseEntity==" + (responseEntity==null?"null":responseEntity.getStatusCodeValue()));

        return responseEntity;
    }
}
