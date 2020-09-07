package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.*;
import com.dmeplugin.dmestore.utils.HttpRequestUtil;
import com.dmeplugin.dmestore.utils.RestUtils;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
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
    public Map<String, Object> getStorages(Map<String, String> params) {

        Map<String, Object> objMap = new HashMap<>();
        objMap.put("code", "200");
        objMap.put("msg", "list storage success!");
        List<Storage> list = new ArrayList<>();

        //TODO hostIp
        //String hostIp = "";
        String apiUrl = "/rest/storagemgmt/v1/storages";
        String param = HttpRequestUtil.concatParam(params);
        //String url = "https://" + hostIp + apiUrl + param;
        String url = apiUrl + "?" + param;

        //TODO use 26335 port to get token
        //String token = "";
        //HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
        //headers.set("X-Auth-Token", token);


        try {
            ResponseEntity<String> responseEntity = dmeAccessServiceImpl.access(url, HttpMethod.GET, null);
            Log.info("DmeStorageServiceImpl/getStorages/responseEntity==" + responseEntity);
            //ResponseEntity responseEntity = sendHttpRequest(url, HttpMethod.GET, headers);
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                objMap.put("code", code);
                objMap.put("msg", "list storage error !");
                return objMap;
            }
            Object object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
            JsonArray storage = jsonObject.get("datas").getAsJsonArray();
            for (JsonElement jsonElement : storage) {
                JsonObject jsonObj = new JsonParser().parse(jsonElement.toString()).getAsJsonObject();
                String id = jsonObj.get("id").toString();
                String name = jsonObj.get("name").toString();
                String ip = jsonObj.get("ip").toString();
                String status = jsonObj.get("status").toString();
                String synStatus = jsonObj.get("syn_status").toString();
                String vendor = jsonObj.get("vendor").toString();
                String model = jsonObj.get("model").toString();
                Long usedCapacity = Long.valueOf(jsonObj.get("used_capacity").toString());
                Long totalCapacity = Long.valueOf(jsonObj.get("total_capacity").toString());
                Long totalEffectiveCapacity = Long.valueOf(jsonObj.get("total_effective_capacity").toString());
                Long freeEffectiveCapacity = Long.valueOf(jsonObj.get("free_effective_capacity").toString());
                Long maxCpuUtilization = Long.valueOf(jsonObj.get("max_cpu_utilization").toString());
                Long maxIops = Long.valueOf(jsonObj.get("max_iops").toString());
                Long maxBandwidth = Long.valueOf(jsonObj.get("max_bandwidth").toString());
                Long maxLatency = Long.valueOf(jsonObj.get("max_Latency").toString());
                JsonArray azIds = jsonObj.get("az_ids").getAsJsonArray();
                List<JsonArray> jsonArrays = Arrays.asList(azIds);

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
                storageObj.setAzIds(jsonArrays);

                list.add(storageObj);
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
            JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
            String id = jsonObject.get("id").toString();
            String name = jsonObject.get("name").toString();
            String ip = jsonObject.get("ip").toString();
            String status = jsonObject.get("status").toString();
            String synStatus = jsonObject.get("syn_status").toString();
            String vendor = jsonObject.get("vendor").toString();
            String model = jsonObject.get("model").toString();
            Long usedCapacity = Long.valueOf(jsonObject.get("used_capacity").toString());
            Long totalCapacity = Long.valueOf(jsonObject.get("total_capacity").toString());
            Long totalEffectiveCapacity = Long.valueOf(jsonObject.get("total_effective_capacity").toString());
            Long freeEffectiveCapacity = Long.valueOf(jsonObject.get("free_effective_capacity").toString());
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

            resMap.put("data", storageObj);
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

        //String hostIp = "";
        String url = "/rest/storagemgmt/v1/storagepools/query";
        //String url = "https://" + hostIp + reqPath;

        Map<String, String> params = new HashMap<>();
        params.put("storage_id", storageId);

        //HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
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
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("datas").getAsJsonArray();
            for (JsonElement jsonElement:jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                String name = element.get("name").toString();
                Double free_capacity = Double.valueOf(element.get("free_capacity").toString());
                String health_status = element.get("health_status").toString();
                Double lun_subscribed_capacity = Double.valueOf(element.get("lun_subscribed_capacity").toString());
                String parent_type = element.get("parent_type").toString();
                String running_status = element.get("running_status").toString();
                Double total_capacity = Double.valueOf(element.get("total_capacity").toString());
                Double fs_subscribed_capacity = Double.valueOf(element.get("fs_subscribed_capacity").toString());
                Double consumed_capacity = Double.valueOf(element.get("consumed_capacity").toString());
                String consumed_capacity_percentage = element.get("consumed_capacity_percentage").toString();
                String consumed_capacity_threshold = element.get("consumed_capacity_threshold").toString();

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

        //String hostIp = "";
        String reqPath = "/rest/blockservice/v1/volumes";
        //Map<String, String> params = new HashMap<>();
        //String url = "https://" + hostIp + reqPath + "? storageId = "+ storageId;
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
            JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("volumes").getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                String id = element.get("id").toString();
                String name = element.get("name").toString();
                String status = element.get("status").toString();
                Boolean attached = Boolean.valueOf(element.get("attached").toString());
                String alloctype = element.get("alloctype").toString();
                String service_level_name = element.get("service_level_name").toString();
                String storage_id = element.get("storage_id").toString();
                String pool_raw_id = element.get("pool_raw_id").toString();
                String capacity_usage = element.get("capacity_usage").toString();
                Boolean protectionStatus = Boolean.valueOf(element.get("protectionStatus").toString());

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

        //HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

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
            JsonObject jsonObject = new JsonParser().parse(respObject).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                String id = element.get("id").toString();
                String name = element.get("name").toString();
                String health_status = element.get("health_status").toString();
                String alloc_type = element.get("alloc_type").toString();
                Double capacity = Double.valueOf(element.get("capacity").toString());
                Integer capacity_usage_ratio = Integer.valueOf(element.get("capacity_usage_ratio").toString());
                String storage_pool_name = element.get("storage_pool_name").toString();
                Integer nfs_count = Integer.valueOf(element.get("nfs_count").toString());
                Integer cifs_count = Integer.valueOf(element.get("cifs_count").toString());
                Integer dtree_count = Integer.valueOf(element.get("dtree_count").toString());

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

        //HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

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
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("dtrees").getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {

                JsonObject element = jsonElement.getAsJsonObject();
                String name = element.get("name").toString();
                String fs_name = element.get("fs_name").toString();
                Boolean quota_switch = Boolean.valueOf(element.get("quota_switch").toString());
                String security_style = element.get("security_style").toString();
                String tier_name = element.get("tier_name").toString();
                Integer nfs_count = Integer.valueOf(element.get("nfs_count").toString());

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

        //String hostIp = "";
        String url = "/rest/fileservice/v1/nfs-shares/summary";
        //String url = "https://" + hostIp + reqPath;

        Map<String, String> params = new HashMap<>();
        params.put("storage_id", storageId);
        String jsonParams = gson.toJson(params);

        //HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

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
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("nfs_share_info_list").getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                String name = element.get("name").toString();
                String share_path = element.get("share_path").toString();
                String storage_id = element.get("storage_id").toString();
                String tier_name = element.get("tier_name").toString();
                String owning_dtree_name = element.get("owning_dtree_name").toString();
                String fs_name = element.get("fs_name").toString();
                String owning_dtree_id = element.get("owning_dtree_id").toString();

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
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("bond_ports").getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                String id = element.get("id").toString();
                String name = element.get("name").toString();
                String health_status = element.get("health_status").toString();
                String running_status = element.get("running_status").toString();
                String mtu = element.get("mtu").toString();

                BandPorts bandPorts = new BandPorts();
                bandPorts.setId(id);
                bandPorts.setName(name);
                bandPorts.setHealth_status(health_status);
                bandPorts.setRunning_status(running_status);
                bandPorts.setMtu(mtu);

                resList.add(bandPorts);
            }
            resMap.put("data", resList);
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
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("logic_ports").getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                String id = element.get("id").toString();
                String name = element.get("name").toString();
                String running_status = element.get("running_status").toString();
                String operational_status = element.get("operational_status").toString();
                String mgmt_ip = element.get("mgmt_ip").toString();
                String mgmt_ipv6 = element.get("mgmt_ipv6").toString();
                String home_port_id = element.get("home_port_id").toString();
                String home_port_name = element.get("home_port_name").toString();
                String current_port_id = element.get("current_port_id").toString();
                String current_port_name = element.get("current_port_name").toString();
                String role = element.get("role").toString();
                String ddns_status = element.get("ddns_status").toString();
                String support_protocol = element.get("support_protocol").toString();
                String management_access = element.get("management_access").toString();
                String vstore_id = element.get("vstore_id").toString();
                String vstore_name = element.get("vstore_name").toString();

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
        resMap.put("msg", "list logic ports success!");
        resMap.put("storageId", className);
        List<StorageControllers> resList = new ArrayList<>();

        String url = "/rest/resourcedb/v1/instances/"+className;
        try {
            ResponseEntity<String> responseEntity = dmeAccessServiceImpl.access(url, HttpMethod.GET, null);
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                resMap.put("code", code);
                resMap.put("msg", "list logic ports error!");
                return resMap;
            }
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("objList").getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                String name = element.get("name").toString();
                String status = element.get("status").toString();
                String softVer = element.get("softVer").toString();
                String cpuInfo = element.get("cpuInfo").toString();

                StorageControllers storageControllers = new StorageControllers();
                storageControllers.setName(name);
                storageControllers.setSoftVer(softVer);
                storageControllers.setStatus(status);
                storageControllers.setCpuInfo(cpuInfo);

                resList.add(storageControllers);
            }
            resMap.put("data", resList);
            return resMap;
        } catch (Exception e) {
            Log.error("list logic ports error!");
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
