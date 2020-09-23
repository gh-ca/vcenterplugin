package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.*;
import com.dmeplugin.dmestore.utils.RestUtils;
import com.dmeplugin.dmestore.utils.ToolUtils;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
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
 * @className VmfsOperationServiceImpl
 * @description TODO
 * @date 2020/9/9 10:23
 */
public class VmfsOperationServiceImpl implements VmfsOperationService {

    private final String API_VMFS_UPDATE = "/rest/blockservice/v1/volumes";
    private final String API_VMFS_EXPAND = "/rest/blockservice/v1/volumes/expand";
    private final String API_SERVICELEVEL_UPDATE = "/rest/blockservice/v1/volumes/update-service-level";
    private final String API_SERVICELEVEL_LIST = "/rest/service-policy/v1/service-levels";

    private DmeAccessService dmeAccessService;

    private Gson gson=new Gson();

    private VCSDKUtils vcsdkUtils;

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
    }

    private static final Logger LOG = LoggerFactory.getLogger(VmfsOperationServiceImpl.class);

    @Override
    public Map<String, Object> updateVMFS(String volume_id, Map<String, Object> params) {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 202);
        resMap.put("msg", "update vmfsDatastore success !");
        resMap.put("task_id", volume_id);

        QosPolicy qosPolicy = new QosPolicy();
        Object control_policy = params.get("control_policy");
        if (!StringUtils.isEmpty(control_policy)) {
            qosPolicy.setControl_policy(control_policy);
        }
        Object max_iops = params.get("max_iops");
        if (!StringUtils.isEmpty(max_iops)) {
            qosPolicy.setMax_iops(max_iops.toString());
        }
        Object min_iops = params.get("min_iops");
        if (!StringUtils.isEmpty(min_iops)) {
            qosPolicy.setMin_iops(min_iops.toString());
        }
        Object max_bandwidth = params.get("max_bandwidth");
        if (!StringUtils.isEmpty(max_bandwidth)) {
            qosPolicy.setMax_bandwidth(max_bandwidth.toString());
        }
        Object min_bandwidth = params.get("min_bandwidth");
        if (!StringUtils.isEmpty(min_bandwidth)) {
            qosPolicy.setMin_bandwidth(min_bandwidth.toString());
        }
        CustomizeVolumeTuning customizeVolumeTuning = new CustomizeVolumeTuning();
        customizeVolumeTuning.setSmartQos(qosPolicy);

        VolumeUpdate volume = new VolumeUpdate();
        Object newVoName = params.get("newVoName");

        if (!StringUtils.isEmpty(newVoName)) {
            volume.setName(newVoName.toString());
        }
        volume.setTuning(customizeVolumeTuning);
        Map<String, Object> reqMap = new HashMap<>();
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
            if (code != 202 ||StringUtils.isEmpty(result)||result.equals("failed")) {
                resMap.put("code", code);
                resMap.put("msg", "update VmfsDatastore failed");
                return resMap;
            }
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            resMap.put("task_id", jsonObject.get("task_id").getAsString());
            return resMap;
        } catch (Exception e) {
            LOG.error("update vmfsDatastore error", e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        } finally {
            return resMap;
        }
    }

    @Override
    public Map<String, Object> expandVMFS(List<Map<String, String>> volumes) {

        //volumes{vo_add_capacity,volume_id,ds_name}
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 202);
        resMap.put("msg", "expand vmfsDatastore and volumes success !");

        Map<String, Object> reqBody = new HashMap<>();
        Map<String, Map<String, Object>> reqMap = new HashMap<>();
        List<String> volume_ids = new ArrayList<>();
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
                String volume_id = map.get("volume_id");
                String ds_name = map.get("ds_name");
                String ds_add_capacity = map.get("ds_add_capacity");
                Map<String, Object> deviceByVolume = getStorageDeviceByVolume(volume_id);
                int deviceCode = ToolUtils.getInt(deviceByVolume.get("code"));
                if (deviceCode != 200) {
                    resMap.put("code", deviceCode);
                    resMap.put("msg", "search storage by volume error");
                    return resMap;
                }
                Storage storage = (Storage) deviceByVolume.get("data");
                String result = null;
                if (!StringUtils.isEmpty(ds_add_capacity)) {
                    result = vcsdkUtils.expandVmfsDatastore(ds_name, ToolUtils.getInt(ds_add_capacity));
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
        } finally {
            return resMap;
        }
    }

    @Override
    public Map<String, Object> recycleVmfsCapacity(List<String> dsname) {
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "recycle vmfsDatastore success !");
        try {
            String result = null;
            VCSDKUtils vcsdkUtils = new VCSDKUtils();
            if (dsname != null && dsname.size() > 0) {
                for (int i = 0; i < dsname.size(); i++) {
                    result = vcsdkUtils.recycleVmfsCapacity(dsname.get(i));
                }
            }
            if (result == null || result.equals("error")) {
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

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 202);
        resMap.put("msg", "update vmfs service level success !");
        if (params == null || params.size() == 0) {
            resMap.put("msg", "params error,please check your params!");
            resMap.put("code", 403);
        }
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(API_SERVICELEVEL_UPDATE, HttpMethod.PUT, gson.toJson(params));
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
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "list vmfs service level success !");

        List<SimpleServiceLevel> simpleServiceLevels = new ArrayList<>();

        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(API_SERVICELEVEL_LIST, HttpMethod.GET, gson.toJson(params));
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                resMap.put("code", code);
                resMap.put("msg", "list vmfs service level error !");
                return resMap;
            }
            SimpleServiceLevel simpleServiceLevel = new SimpleServiceLevel();
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("service-levels").getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                simpleServiceLevel.setId(element.get("id").getAsString());
                simpleServiceLevel.setName(element.get("name").getAsString());
                simpleServiceLevel.setDescription(element.get("description").getAsString());
                simpleServiceLevel.setType(element.get("type").getAsString());
                simpleServiceLevel.setProtocol(element.get("protocol").getAsString());
                simpleServiceLevel.setTotal_capacity(Double.valueOf(element.get("total_capacity").getAsString()));
                simpleServiceLevel.setFree_capacity(Double.valueOf(element.get("free_capacity").getAsString()));
                simpleServiceLevel.setUsed_capacity(Double.valueOf(element.get("used_capacity").getAsString()));

                SimpleCapabilities capability = new SimpleCapabilities();
                JsonObject capabilities = element.get("capabilities").getAsJsonObject();
                capability.setResource_type(capabilities.get("resource_type").getAsString());
                capability.setCompression(Boolean.valueOf(capabilities.get("compression").getAsString()));
                capability.setDeduplication(Boolean.valueOf(capabilities.get("deduplication").getAsString()));

                CapabilitiesSmarttier smarttier = new CapabilitiesSmarttier();
                JsonObject smarttiers = capabilities.get("smarttier").getAsJsonObject();
                smarttier.setPolicy(Integer.valueOf(smarttiers.get("policy").getAsString()));
                smarttier.setEnabled(Boolean.valueOf(smarttiers.get("enabled").getAsString()));
                capability.setSmarttier(smarttier);

                QosParam qosParam = new QosParam();
                JsonObject qos = capabilities.get("qos").getAsJsonObject();
                qosParam.setEnabled(Boolean.valueOf(qos.get("enabled").getAsString()));

                QosPolicy qosPolicy = new QosPolicy();
                JsonObject jsonObject1 = qos.get("qos_param").getAsJsonObject();
                qosPolicy.setLatency(jsonObject1.get("latency").getAsString());
                qosPolicy.setMin_bandwidth(jsonObject1.get("minBandWidth").getAsString());
                qosPolicy.setMin_iops(jsonObject1.get("minIOPS").getAsString());
                qosPolicy.setLatencyUnit(jsonObject1.get("latencyUnit").getAsString());

                qosParam.setQosPolicy(qosPolicy);
                capability.setQosParam(qosParam);
                simpleServiceLevel.setSimpleCapabilities(capability);
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
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "search host id success ");
        List<String> host_ids = new ArrayList<>();
        for (int i = 0; i < volume_ids.size(); i++) {
            String volume_id = volume_ids.get(i);
            url = "https://localhost:26335/rest/blockservice/v1/volumes/" + volume_id;
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            //ResponseEntity<String> responseEntity = access(url, HttpMethod.GET, null);
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
                String host_id = element.get("host_id").getAsString();
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
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "search host ip success ");

        List<String> host_ips = new ArrayList<>();
        String url;
        for (int i = 0; i < params.size(); i++) {
            String host_id = params.get(i);
            url = "https://localhost:26335/rest/hostmgmt/v1/hosts/" + host_id + "/summary";
            ResponseEntity<String> responseEntity = access(url, HttpMethod.GET, null);
            int code = responseEntity.getStatusCodeValue();
            if (code != 200) {
                resMap.put("code", code);
                resMap.put("msg", "search host ip error ");
            }
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            String ip = jsonObject.get("ip").getAsString();
            host_ips.add(ip);
        }
        resMap.put("host_ips", host_ips);
        return resMap;
    }

    private Map<String, Object> getVmfsServicLevel(Map<String, String> params) {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "list vmfs service level success !");


        return resMap;
    }

    private Map<String, Object> getVolumeCapacity(List<Map<String, String>> volumes) throws Exception {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "search volume capacity success ");
        String url;
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < volumes.size(); i++) {
            Map<String, String> reqmap = volumes.get(i);
            String volume_id = reqmap.get("volume_id");
            url = "https://localhost:26335/rest/blockservice/v1/volumes/" + volume_id;
            //ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            ResponseEntity<String> responseEntity = access(url, HttpMethod.GET, null);

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
            Map<String, String> map = new HashMap<>();
            map.put(volume_id, capacity);
            list.add(map);
        }
        resMap.put("data", list);
        return resMap;
    }

    private Map<String, Object> getStorageDeviceByVolume(String volume_id) throws Exception {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "search storage device success ");
        String url = "https://localhost:26335/rest/storagemgmt/v1/storages/" + volume_id + "/detail";
        ResponseEntity<String> responseEntity = access(url, HttpMethod.GET, null);
        int code = responseEntity.getStatusCodeValue();
        if (code != 200) {
            resMap.put("code", 200);
            resMap.put("msg", "search storage device error ");
        }
        String object = responseEntity.getBody();
        JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
        Storage storage = new Storage();
        storage.setId(jsonObject.get("id").getAsString());
        storage.setName(jsonObject.get("name").getAsString());
        storage.setIp(jsonObject.get("ip").getAsString());
        storage.setStatus(jsonObject.get("status").getAsString());
        storage.setVendor(jsonObject.get("vendor").getAsString());
        storage.setProductVersion(jsonObject.get("product_version").getAsString());
        storage.setUsedCapacity(Double.valueOf(jsonObject.get("used_capacity").getAsString()));
        storage.setTotalCapacity(Double.valueOf(jsonObject.get("total_capacity").getAsString()));
        storage.setTotalEffectiveCapacity(Double.valueOf(jsonObject.get("total_effective_capacity").getAsString()));
        storage.setFreeEffectiveCapacity(Double.valueOf(jsonObject.get("free_effective_capacity").getAsString()));
        JsonElement jsonAzIds = jsonObject.get("az_ids");
        if (jsonAzIds != null) {
            String azIds = jsonAzIds.getAsString();
            String[] az_ids = {azIds};
            storage.setAzIds(az_ids);
        } else {
            String[] az_ids = {};
            storage.setAzIds(az_ids);
        }
        resMap.put("data", storage);
        return resMap;
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
}
