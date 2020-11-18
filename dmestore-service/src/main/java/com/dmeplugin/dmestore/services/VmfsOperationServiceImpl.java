package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DMEException;
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

    private DmeAccessService dmeAccessService;

    private Gson gson=new Gson();

    private VCSDKUtils vcsdkUtils;

    private TaskService taskService;

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

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
    public void updateVmfs(String volumeId, Map<String, Object> params) throws DMEException {

        VolumeUpdate volume = new VolumeUpdate();

        Object serviceLevelName = params.get("service_level_name");
        //TODO if (!StringUtils.isEmpty(serviceLevelName)) { 有高级选项环境需要开启这个
        if (StringUtils.isEmpty(serviceLevelName)) {
            SmartQos smartQos = new SmartQos();
            Object controlPolicy = params.get("control_policy");
            if (!StringUtils.isEmpty(controlPolicy)) {
                smartQos.setControlPolicy(controlPolicy.toString());
            }
            Object maxIops = params.get("max_iops");
            if (!StringUtils.isEmpty(maxIops)) {
                smartQos.setMaxiops(Integer.valueOf(maxIops.toString()));
            }
            Object minIops = params.get("min_iops");
            if (!StringUtils.isEmpty(minIops)) {
                smartQos.setMiniops(Integer.valueOf(minIops.toString()));
            }
            Object maxBandwidth = params.get("max_bandwidth");
            if (!StringUtils.isEmpty(maxBandwidth)) {
                smartQos.setMaxbandwidth(Integer.valueOf(maxBandwidth.toString()));
            }
            Object minBandwidth = params.get("min_bandwidth");
            if (!StringUtils.isEmpty(minBandwidth)) {
                smartQos.setMinbandwidth(Integer.valueOf(minBandwidth.toString()));
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

        String url = API_VMFS_UPDATE + "/" + volumeId;
        try {
            Object oldDsName = params.get("oldDsName");
            Object newDsName = params.get("newDsName");
            if (StringUtils.isEmpty(oldDsName) || StringUtils.isEmpty(newDsName)) {
                throw new DMEException("403","datastore params error");
            }
            //vcenter renameDatastore
            Object dataStoreObjectId = params.get("dataStoreObjectId");
            String result = "";
            if (dataStoreObjectId != null) {
                result = vcsdkUtils.renameDataStore(newDsName.toString(), dataStoreObjectId.toString());
            }
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.PUT, reqBody);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.ACCEPTED.value() ||StringUtils.isEmpty(result)||"failed".equals(result)) {
                throw new DMEException("503", "update VmfsDatastore failed");
            }
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            String taskId = ToolUtils.jsonToStr(jsonObject.get("task_id"));
            List<String> taskIds = new ArrayList<>(10);
            taskIds.add(taskId);
            Boolean flag = taskService.checkTaskStatus(taskIds);
            if (!flag) {
                LOG.error("update vmfs datastore error");
                throw new DMEException("400", "update vmfs datastore error");
            }

        } catch (Exception e) {
            LOG.error("update vmfsDatastore error", e);
            throw new DMEException("503",e.getMessage());
        }
    }

    @Override
    public void expandVmfs(Map<String, String> volumemap) throws DMEException {

        Map<String, Object> reqBody = new HashMap<>(16);
        List<Object> reqList = new ArrayList<>(10);
        Map<String, Object> reqMap = new HashMap<>(16);
        List<String> volumeIds = new ArrayList<>(10);

        String volumeId = volumemap.get("volume_id");
        String datastoreobjid = volumemap.get("obj_id");
        String voAddCapacity = volumemap.get("vo_add_capacity");
        if (!StringUtils.isEmpty(volumeId) && !StringUtils.isEmpty(voAddCapacity)) {
            volumeIds.add(volumeId);
        }
        volumeIds.add(volumeId);
        reqBody.put("volume_id", volumeId);
        reqBody.put("added_capacity", Integer.valueOf(volumemap.get("vo_add_capacity")));
        reqList.add(reqBody);
        reqMap.put("volumes", reqList);
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(API_VMFS_EXPAND, HttpMethod.POST, gson.toJson(reqMap));
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.ACCEPTED.value()) {
                throw new DMEException("503","expand vmfsDatastore failed !");
            }
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            String taskId = ToolUtils.jsonToStr(jsonObject.get("task_id"));
            List<String> taskIds = new ArrayList<>(10);
            taskIds.add(taskId);
            Boolean flag = taskService.checkTaskStatus(taskIds);
            if (!flag) {
                throw new DMEException("503","expand volume failed !");
            }

            String dsName = volumemap.get("ds_name");
            String result = null;
            if (!StringUtils.isEmpty(voAddCapacity)&&!StringUtils.isEmpty(datastoreobjid)) {
                result = vcsdkUtils.expandVmfsDatastore(dsName, ToolUtils.getInt(voAddCapacity),datastoreobjid);
            }
            if ("failed".equals(result)) {
                throw new DMEException("403","expand vmfsDatastore failed !");
            }


        } catch (Exception e) {
            LOG.error("expand vmfsDatastore error !", e);
            throw new DMEException("503",e.getMessage());
        }
    }

    @Override
    public void recycleVmfsCapacity(List<String> dsname) throws DMEException {

        try {
            String result = null;
            if (dsname != null && dsname.size() > 0) {
                for (int i = 0; i < dsname.size(); i++) {
                    result = vcsdkUtils.recycleVmfsCapacity(dsname.get(i));
                }
            }
            if (result == null || "error".equals(result)) {

                throw new DMEException("403","recycle vmfsDatastore error");
            }
        } catch (Exception e) {
            LOG.error("recycle vmfsDatastore error !", e);

            throw new DMEException("503",e.getMessage());
        }
    }

    @Override
    public void recycleVmfsCapacityByDataStoreIds(List<String> dsIds) throws DMEException {
        try {
            String result = null;
            if (dsIds != null && dsIds.size() > 0) {
                for (int i = 0; i < dsIds.size(); i++) {
                    String dsName = vcsdkUtils.getDataStoreName(dsIds.get(i));
                    result = vcsdkUtils.recycleVmfsCapacity(dsName);
                }
            }
            if (result == null || "error".equals(result)) {
                throw new DMEException("403","recycle vmfsDatastore error");
            }
        } catch (Exception e) {
            LOG.error("recycle vmfsDatastore error !", e);
            throw new DMEException("503",e.getMessage());
        }
    }

    @Override
    public void updateVmfsServiceLevel(Map<String, Object> params) throws DMEException {

        if (params == null || params.size() == 0) {
            throw new DMEException("403","params error,please check your params!");
        }

        Map<String, Object> reqMap = new HashMap<>(16);
        reqMap.put("service_level_id", params.get("service_level_id"));
        reqMap.put("volume_ids", params.get("volume_ids"));
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(API_SERVICELEVEL_UPDATE, HttpMethod.POST, gson.toJson(reqMap));
            LOG.info("url:{" + API_SERVICELEVEL_UPDATE + "},响应信息：" + responseEntity);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.ACCEPTED.value()) {
                throw new DMEException("503","update vmfs service level error!");
            }
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            String taskId = jsonObject.get("task_id").getAsString();
            List<String> taskIds = new ArrayList<>(10);
            taskIds.add(taskId);
            Boolean flag = taskService.checkTaskStatus(taskIds);
            if (!flag) {
                throw new DMEException("400","update vmfs service level failed");
            }
        } catch (Exception e) {
            LOG.error("update vmfs service level error !", e);
            throw new DMEException("503",e.getMessage());
        }
    }

    @Override
    public List<SimpleServiceLevel> listServiceLevelVmfs(Map<String, Object> params) throws DMEException {

        List<SimpleServiceLevel> simpleServiceLevels = new ArrayList<>();
        String s = gson.toJson(params);
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(API_SERVICELEVEL_LIST, HttpMethod.GET, gson.toJson(params));
            LOG.info("{"+API_SERVICELEVEL_LIST+"}"+responseEntity);
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
            
                throw new DMEException("503","list vmfs service level error !");
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
                simpleServiceLevel.setTotalCapacity(ToolUtils.jsonToDou(element.get("total_capacity"),0.0));
                simpleServiceLevel.setFreeCapacity(ToolUtils.jsonToDou(element.get("free_capacity"),0.0));
                simpleServiceLevel.setUsedCapacity(ToolUtils.jsonToDou(element.get("used_capacity"),0.0));

                SimpleCapabilities capability = new SimpleCapabilities();
                JsonObject capabilities = element.get("capabilities").getAsJsonObject();
                capability.setResourceType(ToolUtils.jsonToStr(capabilities.get("resource_type")));
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
                if (!"".equals(ToolUtils.getStr(capabilities.get("qos")))) {
                    qos = capabilities.get("qos").getAsJsonObject();
                    capabilitiesQos.setEnabled(ToolUtils.jsonToBoo(qos.get("enabled")));
                }
                SmartQos smartQos = new SmartQos();
                JsonObject jsonObject1 = null;
                if (qos != null && !"".equals(ToolUtils.getStr(qos.get("qos_param")))) {
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
        } catch (Exception e) {
            LOG.error("list vmfs service level success !", e);
            throw new DMEException("503",e.getMessage());
        }
        return simpleServiceLevels;
    }

}
