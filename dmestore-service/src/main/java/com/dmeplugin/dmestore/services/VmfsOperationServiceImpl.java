package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.CustomizeVolumeTuning;
import com.dmeplugin.dmestore.model.QosPolicy;
import com.dmeplugin.dmestore.model.Storage;
import com.dmeplugin.dmestore.model.VolumeUpdate;
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

    @Autowired
    private DmeAccessService dmeAccessService;

    @Autowired
    private Gson gson;

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
        Object max_bandwidth = params.get("max_bandwidth");
        if (!StringUtils.isEmpty(max_bandwidth)) {
            qosPolicy.setMax_bandwidth(max_bandwidth.toString());
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

        String url = "https://localhost:26335/rest/blockservice/v1/volumes/" + volume_id;
        try {
            Object oldDsName = params.get("oldDsName");
            Object newDsName = params.get("newDsName");
            if (StringUtils.isEmpty(oldDsName) || StringUtils.isEmpty(newDsName)) {
                resMap.put("code", 403);
                resMap.put("msg", "datastore params error");
                return resMap;
            }
            //vcenter renameDatastore
            String result = vcsdkUtils.renameDataStore(oldDsName.toString(), newDsName.toString());
            //ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.PUT, reqBody);
            ResponseEntity<String> responseEntity = access(url, HttpMethod.PUT, reqBody);
            int code = responseEntity.getStatusCodeValue();
            if (code != 202 || result.equals("failed")) {
                resMap.put("code", code);
                resMap.put("msg", "update VmfsDatastore failed");
                return resMap;
            }
            String object = responseEntity.getBody();
            JsonObject jsonObject=new JsonParser().parse(object).getAsJsonObject();
            resMap.put("task_id", jsonObject.get("task_id").getAsString());
            return resMap;
        } catch (Exception e) {
            LOG.error("update vmfsDatastore error", e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        }finally {
            return resMap;
        }
    }

    @Override
    public Map<String, Object> expandVMFS(List<Map<String, String>> volumes) {

        //volumes{vo_add_capacity,ds_add_capacity,volume_id,ds_name}
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
        String url = "/rest/blockservice/v1/volumes/expand";
        try {
            //ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, gson.toJson(reqMap));
            //ResponseEntity<String> responseEntity = access(url, HttpMethod.POST, gson.toJson(reqMap));
            /*int code = responseEntity.getStatusCodeValue();
            if (code != 202) {
                resMap.put("code", code);
                resMap.put("msg", "expand vmfsDatastore failed !");
                return resMap;
            }
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            resMap.put("task_id", jsonObject.get("task_id").getAsString());*/

            //scan volume of host
           /* Map<String, Object> hostMap = getHostIpByVolume(volume_ids);
            Integer rescode = Integer.valueOf(hostMap.get("code").toString());
            if (rescode == 200) {
                List<Object> ips = Arrays.asList(hostMap.get("host_ips"));
               for (int i = 0; i < ips.size(); i++) {
                    VCSDKUtils.hostRescanVmfs(ips.get(i).toString());
                }
             }*/
            //expand vmfs datastore
            for (int i = 0; i < volumes.size(); i++) {
                Map<String, String> map = volumes.get(i);
                String volume_id = map.get("volume_id");
                String ds_name = map.get("ds_name");
                Map<String, Object> deviceByVolume = getStorageDeviceByVolume(volume_id);
                int deviceCode = ToolUtils.getInt(deviceByVolume.get("code"));
                if (deviceCode != 200) {
                    resMap.put("code", deviceCode);
                    resMap.put("msg", "search storage by volume error");
                    return resMap;
                }
                Storage storage = (Storage) deviceByVolume.get("data");
                String result = VCSDKUtils.expandVmfsDatastore(ds_name, 1);

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

    private Map<String, Object> getHostIpByVolume(List<String> volume_ids) throws Exception {

        String url;
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "search host id success ");
        List<String> host_ids = new ArrayList<>();
        for (int i = 0; i < volume_ids.size(); i++) {
            String volume_id = volume_ids.get(i);
            url = "https://localhost:26335/rest/blockservice/v1/volumes/" + volume_id;
            //ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            ResponseEntity<String> responseEntity = access(url, HttpMethod.GET, null);
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
        } else{
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
        LOG.info(url + "==responseEntity==" + (responseEntity==null?"null":responseEntity.getStatusCodeValue()));

        return responseEntity;
    }
}
