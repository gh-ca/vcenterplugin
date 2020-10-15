package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.*;
import com.dmeplugin.dmestore.utils.ToolUtils;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author lianq
 * @className VmfsOperationServiceImpl
 * @description TODO
 * @date 2020/9/9 10:23
 */
public class VmfsOperationServiceImpl implements VmfsOperationService {

    private final String API_VMFS_UPDATE = "/rest/blockservice/v1/volumes";
    private final String API_VMFS_EXPAND = "/rest/blockservice/v1/volumes/expand";
    private final String API_SERVICELEVEL_UPDATE = "/rest/blockservice/v1/volumes/update-service-level";
    private final String API_SERVICELEVEL_LIST = "/rest/service-policy/v1/service-levels";
    private final String API_VOLUME_DETAIL = "/rest/blockservice/v1/volumes/";
    private final String API_HOST_DETAIL = "/rest/hostmgmt/v1/hosts/";
    private final String API_STORAGE_LIST = "/rest/storagemgmt/v1/storages/";

    private DmeAccessService dmeAccessService;

    private Gson gson=new Gson();

    private VCSDKUtils vcsdkUtils;

    public DmeAccessService getDmeAccessService() {
        return dmeAccessService;
    }

    public void setDmeAccessService(DmeAccessService dmeAccessService) {
        this.dmeAccessService = dmeAccessService;
    }

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
    }

    private static final Logger LOG = LoggerFactory.getLogger(VmfsOperationServiceImpl.class);

    @Override
    public Map<String, Object> updateVMFS(String volume_id, Map<String, Object> params) {

        Map<String, Object> resMap = new HashMap<>(6);
        resMap.put("code", 202);
        resMap.put("msg", "update vmfsDatastore success !");
        resMap.put("task_id", volume_id);

        VolumeUpdate volume = new VolumeUpdate();

        Object service_level_name = params.get("service_level_name");
        if (StringUtils.isEmpty(service_level_name)) {
            SmartQos smartQos = new SmartQos();
            Object control_policy = params.get("control_policy");
            if (!StringUtils.isEmpty(control_policy)) {
                smartQos.setControl_policy(control_policy.toString());
            }
            Object max_iops = params.get("max_iops");
            if (!StringUtils.isEmpty(max_iops)) {
                smartQos.setMaxiops(Integer.valueOf(max_iops.toString()));
            }
            Object min_iops = params.get("min_iops");
            if (!StringUtils.isEmpty(min_iops)) {
                smartQos.setMiniops(Integer.valueOf(min_iops.toString()));
            }
            Object max_bandwidth = params.get("max_bandwidth");
            if (!StringUtils.isEmpty(max_bandwidth)) {
                smartQos.setMaxbandwidth(Integer.valueOf(max_bandwidth.toString()));
            }
            Object min_bandwidth = params.get("min_bandwidth");
            if (!StringUtils.isEmpty(min_bandwidth)) {
                smartQos.setMinbandwidth(Integer.valueOf(min_bandwidth.toString()));
            }
            CustomizeVolumeTuning customizeVolumeTuning = new CustomizeVolumeTuning();
            customizeVolumeTuning.setSmartQos(smartQos);
            volume.setTuning(customizeVolumeTuning);
        }
        Object newVoName = params.get("newVoName");
        if (!StringUtils.isEmpty(newVoName)) {
            volume.setName(newVoName.toString());
        }
        Map<String, Object> reqMap = new HashMap<>(16);
        reqMap.put("volume", volume);
        String reqBody = gson.toJson(reqMap);

        String url = API_VMFS_UPDATE + "/" + volume_id;
        try {
            Object oldDsName = params.get("oldDsName");
            Object newDsName = params.get("newDsName");
            if (StringUtils.isEmpty(oldDsName) || StringUtils.isEmpty(newDsName)) {
                resMap.put("code", 403);
                resMap.put("msg", "datastore params error");
                return resMap;
            }
            //vcenter renameDatastore
            Object dataStoreObjectId = params.get("dataStoreObjectId");
            String result = "";
            if (dataStoreObjectId != null) {
                result = vcsdkUtils.renameDataStore(newDsName.toString(), dataStoreObjectId.toString());
            }
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.PUT, reqBody);
            int code = responseEntity.getStatusCodeValue();
            if (code != 202 ||StringUtils.isEmpty(result)||"failed".equals(result)) {
                resMap.put("code", code);
                resMap.put("msg", "update VmfsDatastore failed");
                return resMap;
            }
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            resMap.put("task_id", jsonObject.get("task_id").getAsString());
        } catch (Exception e) {
            LOG.error("update vmfsDatastore error", e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        }
    return resMap;
    }

    @Override
    public Map<String, Object> expandVMFS(List<Map<String, String>> volumes) {

        //volumes{vo_add_capacity,volume_id,ds_name}
        Map<String, Object> resMap = new HashMap<>(16);
        resMap.put("code", 202);
        resMap.put("msg", "expand vmfsDatastore and volumes success !");

        Map<String, Object> reqBody = new HashMap<>(16);
        Map<String, Map<String, Object>> reqMap = new HashMap<>(16);
        List<String> volume_ids = new ArrayList<>(10);
        for (int i = 0; i < volumes.size(); i++) {
            Map<String, String> map = volumes.get(i);
            String volume_id = map.get("volume_id");
            volume_ids.add(volume_id);
            reqBody.put("volume_id", volume_id);
            reqBody.put("added_capacity", Integer.valueOf(map.get("vo_add_capacity")));
        }
        reqMap.put("volumes", reqBody);
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(API_VMFS_EXPAND, HttpMethod.POST, gson.toJson(reqMap));
            int code = responseEntity.getStatusCodeValue();
            if (code != 202) {
                resMap.put("code", code);
                resMap.put("msg", "expand vmfsDatastore failed !");
                return resMap;
            }
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            resMap.put("task_id", jsonObject.get("task_id").getAsString());

            //scan volume of host
            Map<String, Object> hostMap = getHostIpByVolume(volume_ids);
            Integer rescode = Integer.valueOf(hostMap.get("code").toString());
            if (rescode == 200) {
                List<Object> ips = Arrays.asList(hostMap.get("host_ips"));
                for (int i = 0; i < ips.size(); i++) {
                    vcsdkUtils.hostRescanVmfs(ips.get(i).toString());
                }
            }
            //expand vmfs datastore
            for (int i = 0; i < volumes.size(); i++) {
                Map<String, String> map = volumes.get(i);
                String ds_name = map.get("ds_name");
                String vo_add_capacity = map.get("vo_add_capacity");
                String hostObjectId = map.get("hostObjectId");
                String result = null;
                if (!StringUtils.isEmpty(vo_add_capacity)&&!StringUtils.isEmpty(hostObjectId)) {
                    result = vcsdkUtils.expandVmfsDatastore(ds_name, ToolUtils.getInt(vo_add_capacity),hostObjectId);
                }
                if ("failed".equals(result)) {
                    resMap.put("code", 403);
                    resMap.put("msg", "expand vmfsDatastore failed !");
                    return resMap;
                }
            }
        } catch (Exception e) {
            LOG.error("expand vmfsDatastore error !", e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        }
        return resMap;
    }

    @Override
    public Map<String, Object> recycleVmfsCapacity(List<String> dsname) {
        Map<String, Object> resMap = new HashMap<>(16);
        resMap.put("code", 200);
        resMap.put("msg", "recycle vmfsDatastore success !");
        try {
            String result = null;
            if (dsname != null && dsname.size() > 0) {
                for (int i = 0; i < dsname.size(); i++) {
                    result = vcsdkUtils.recycleVmfsCapacity(dsname.get(i));
                }
            }
            if (result == null || "error".equals(result)) {
                resMap.put("code", 403);
                resMap.put("msg", "recycle vmfsDatastore error");
                return resMap;
            }
        } catch (Exception e) {
            LOG.error("recycle vmfsDatastore error !", e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        }
        return resMap;
    }

    @Override
    public Map<String, Object> updateVmfsServiceLevel(Map<String, Object> params) {

        Map<String, Object> resMap = new HashMap<>(16);
        resMap.put("code", 202);
        resMap.put("msg", "update vmfs service level success !");
        if (params == null || params.size() == 0) {
            resMap.put("msg", "params error,please check your params!");
            resMap.put("code", 403);
        }
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(API_SERVICELEVEL_UPDATE, HttpMethod.POST, gson.toJson(params));
            LOG.info("url:{" + API_SERVICELEVEL_UPDATE + "},响应信息：" + responseEntity);
            int code = responseEntity.getStatusCodeValue();
            if (code != 202) {
                resMap.put("msg", "update vmfs service level error!");
                resMap.put("code", code);
                return resMap;
            }
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            String task_id = jsonObject.get("task_id").getAsString();
            resMap.put("task_id", task_id);

        } catch (Exception e) {
            LOG.error("update vmfs service level error !", e);
            resMap.put("msg", e.getMessage());
            resMap.put("code", 503);
        }
        return resMap;
    }

    @Override
    public Map<String, Object> listServiceLevelVMFS(Map<String, Object> params) {
        //
        Map<String, Object> resMap = new HashMap<>(16);
        resMap.put("code", 200);
        resMap.put("msg", "list vmfs service level success !");

        List<SimpleServiceLevel> simpleServiceLevels = new ArrayList<>();

        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(API_SERVICELEVEL_LIST, HttpMethod.GET, gson.toJson(params));
            LOG.info("{"+API_SERVICELEVEL_LIST+"}"+responseEntity);
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                resMap.put("code", code);
                resMap.put("msg", "list vmfs service level error !");
                return resMap;
            }

            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("service-levels").getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                SimpleServiceLevel simpleServiceLevel = new SimpleServiceLevel();
                simpleServiceLevel.setId(ToolUtils.jsonToStr(element.get("id")));
                simpleServiceLevel.setName(ToolUtils.jsonToStr(element.get("name")));
                simpleServiceLevel.setDescription(ToolUtils.jsonToStr(element.get("description")));
                simpleServiceLevel.setType(ToolUtils.jsonToStr(element.get("type")));
                simpleServiceLevel.setProtocol(ToolUtils.jsonToStr(element.get("protocol")));
                simpleServiceLevel.setTotal_capacity(ToolUtils.jsonToDou(element.get("total_capacity"),0.0));
                simpleServiceLevel.setFree_capacity(ToolUtils.jsonToDou(element.get("free_capacity"),0.0));
                simpleServiceLevel.setUsed_capacity(ToolUtils.jsonToDou(element.get("used_capacity"),0.0));

                SimpleCapabilities capability = new SimpleCapabilities();
                JsonObject capabilities = element.get("capabilities").getAsJsonObject();
                capability.setResource_type(ToolUtils.jsonToStr(capabilities.get("resource_type")));
                capability.setCompression(ToolUtils.jsonToBoo(capabilities.get("compression")));
                capability.setDeduplication(ToolUtils.jsonToBoo(capabilities.get("deduplication")));

                CapabilitiesSmarttier smarttier = new CapabilitiesSmarttier();
                JsonObject smarttiers = null;
                if (!"".equals(ToolUtils.jsonToStr(capabilities.get("smarttier")))) {
                    smarttiers = capabilities.get("smarttier").getAsJsonObject();
                    smarttier.setPolicy(ToolUtils.jsonToInt(smarttiers.get("policy"), 0));
                    smarttier.setEnabled(ToolUtils.jsonToBoo(smarttiers.get("enabled")));
                }
                capability.setSmarttier(smarttier);

                CapabilitiesQos capabilitiesQos = new CapabilitiesQos();
                JsonObject qos = null;
                    if (!"".equals(ToolUtils.jsonToStr(capabilities.get("qos")))) {
                    qos = capabilities.get("qos").getAsJsonObject();
                    capabilitiesQos.setEnabled(ToolUtils.jsonToBoo(qos.get("enabled")));
                }
                SmartQos smartQos = new SmartQos();
                JsonObject jsonObject1 = null;
                if (qos != null && !"".equals(ToolUtils.jsonToStr(qos.get("qos_param")))) {
                    jsonObject1 = qos.get("qos_param").getAsJsonObject();
                    smartQos.setLatency(ToolUtils.jsonToInt(jsonObject1.get("latency"), 0));
                    smartQos.setMinbandwidth(ToolUtils.jsonToInt(jsonObject1.get("minBandWidth"), 0));
                    smartQos.setMiniops(ToolUtils.jsonToInt(jsonObject1.get("minIOPS"), 0));
                    smartQos.setLatencyUnit(ToolUtils.jsonToStr(jsonObject1.get("latencyUnit")));
                }

                capabilitiesQos.setSmartQos(smartQos);
                capability.setQos(capabilitiesQos);
                simpleServiceLevel.setCapabilities(capability);
                simpleServiceLevels.add(simpleServiceLevel);
            }
            resMap.put("data", simpleServiceLevels);
        } catch (Exception e) {
            LOG.error("list vmfs service level success !", e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        }
        return resMap;
    }

    private Map<String, Object> getHostIpByVolume(List<String> volume_ids) throws Exception {

        String url;
        Map<String, Object> resMap = new HashMap<>(16);
        resMap.put("code", 200);
        resMap.put("msg", "search host id success ");
        List<String> host_ids = new ArrayList<>();
        for (int i = 0; i < volume_ids.size(); i++) {
            String volume_id = volume_ids.get(i);
            url = API_VOLUME_DETAIL + volume_id;
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                resMap.put("code", code);
                resMap.put("msg", "search host id error");
                return resMap;
            }
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            JsonObject volume = jsonObject.get("volume").getAsJsonObject();
            JsonArray attachments = volume.get("attachments").getAsJsonArray();
            //int length = attachments.getAsString().length();
            for (JsonElement jsonElement : attachments) {
                JsonObject element = jsonElement.getAsJsonObject();
                String host_id = ToolUtils.jsonToStr(element.get("host_id"));
                if (!host_ids.contains(host_id)) {
                    host_ids.add(host_id);
                }
            }
        }
        Map<String, Object> hostIp = getHostIp(host_ids);
        int resCode = ToolUtils.getInt(hostIp.get("code"));
        if (resCode != 200) {
            resMap.put("code", resCode);
            resMap.put("msg", "search host ip error");
            return resMap;
        }
        resMap.put("msg", "search host ip success");
        resMap.put("host_ips", hostIp.get("host_ips"));
        return resMap;
    }

    private Map<String, Object> getHostIp(List<String> params) throws Exception {
        Map<String, Object> resMap = new HashMap<>(16);
        resMap.put("code", 200);
        resMap.put("msg", "search host ip success ");

        List<String> host_ips = new ArrayList<>();
        String url;
        for (int i = 0; i < params.size(); i++) {
            String host_id = params.get(i);
            url = API_HOST_DETAIL + host_id + "/summary";
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                resMap.put("code", code);
                resMap.put("msg", "search host ip error ");
            }
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            String ip = ToolUtils.jsonToStr(jsonObject.get("ip"));
            host_ips.add(ip);
        }
        resMap.put("host_ips", host_ips);
        return resMap;
    }

    private Map<String, Object> getVolumeCapacity(List<Map<String, String>> volumes) throws Exception {

        Map<String, Object> resMap = new HashMap<>(16);
        resMap.put("code", 200);
        resMap.put("msg", "search volume capacity success ");
        String url;
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < volumes.size(); i++) {
            Map<String, String> reqmap = volumes.get(i);
            String volume_id = reqmap.get("volume_id");
            url = API_VMFS_UPDATE + volume_id;
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                resMap.put("code", code);
                resMap.put("msg", "search volume capacity error ");
                return resMap;
            }
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            JsonObject volume = jsonObject.get("volume").getAsJsonObject();
            String capacity = volume.get("capacity").getAsString();
            Map<String, String> map = new HashMap<>(16);
            map.put(volume_id, capacity);
            list.add(map);
        }
        resMap.put("data", list);
        return resMap;
    }

    private Map<String, Object> getStorageDeviceByVolume(String volume_id) throws Exception {

        Map<String, Object> resMap = new HashMap<>(16);
        resMap.put("code", 200);
        resMap.put("msg", "search storage device success ");
        String url = API_STORAGE_LIST + volume_id + "/detail";
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        int code = responseEntity.getStatusCodeValue();
        if (code != 200) {
            resMap.put("code", 200);
            resMap.put("msg", "search storage device error ");
        }
        String object = responseEntity.getBody();
        JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
        Storage storage = new Storage();
        storage.setId(ToolUtils.jsonToStr(jsonObject.get("id")));
        storage.setName(ToolUtils.jsonToStr(jsonObject.get("name")));
        storage.setIp(ToolUtils.jsonToStr(jsonObject.get("ip")));
        storage.setStatus(ToolUtils.jsonToStr(jsonObject.get("status")));
        storage.setVendor(ToolUtils.jsonToStr(jsonObject.get("vendor")));
        storage.setProductVersion(ToolUtils.jsonToStr(jsonObject.get("product_version")));
        Double used_capacity = ToolUtils.jsonToDou(jsonObject.get("used_capacity"), 0.0);
        storage.setUsedCapacity(used_capacity);
        Double total_capacity = ToolUtils.jsonToDou(jsonObject.get("total_capacity"), 0.0);
        storage.setTotalCapacity(total_capacity);
        storage.setTotalEffectiveCapacity(ToolUtils.jsonToDou(jsonObject.get("total_effective_capacity"),0.0));
        storage.setFreeEffectiveCapacity(ToolUtils.jsonToDou(jsonObject.get("free_effective_capacity"),0.0));
        //容量利用率

        resMap.put("data", storage);
        return resMap;
    }

}
