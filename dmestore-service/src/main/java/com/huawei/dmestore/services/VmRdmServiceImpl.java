package com.huawei.dmestore.services;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.VcenterException;
import com.huawei.dmestore.model.*;
import com.huawei.dmestore.utils.StringUtil;
import com.huawei.dmestore.utils.ToolUtils;
import com.huawei.dmestore.utils.VCSDKUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

import springfox.documentation.spring.web.json.Json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * VmRdmServiceImpl
 *
 * @author wangxiangyong
 * @since 2020-09-15
 **/
public class VmRdmServiceImpl implements VmRdmService {
    private static final Logger LOG = LoggerFactory.getLogger(VmRdmServiceImpl.class);

    private static final int MAP_DEFAULT_CAPACITY = 16;

    private static final int CONSTANT_1024 = 1024;

    private static final int THOUSAND = 1000;

    private final String TASKTYPE = "Create LUN";

    /**
     * 轮询任务状态的超值时间，这里设置超长，避免创建超多的lun超时
     */
    private final long longTaskTimeOut = 30 * 60 * 1000;

    private static final String UNITTB = "TB";

    private static final String NAME_FIELD = "name";

    private static final String ID_FIELD = "id";

    private static final String CAPACITY = "capacity";

    private DmeAccessService dmeAccessService;

    private DmeStorageService dmeStorageService;

    private TaskService taskService;

    private Gson gson = new Gson();

    private VCSDKUtils vcsdkUtils;

    private VmfsAccessService vmfsAccessService;

    public VmfsAccessService getVmfsAccessService() {
        return vmfsAccessService;
    }

    public void setVmfsAccessService(VmfsAccessService vmfsAccessService) {
        this.vmfsAccessService = vmfsAccessService;
    }

    public DmeStorageService getDmeStorageService() {
        return dmeStorageService;
    }

    public void setDmeStorageService(DmeStorageService dmeStorageService) {
        this.dmeStorageService = dmeStorageService;
    }

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
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

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void createRdm(String dataStoreObjectId, String vmObjectId, VmRdmCreateBean createBean,
        String compatibilityMode) throws DmeException {
        String language =  createBean.getLanguage() == null? DmeConstants.LANGUAGE_CN : createBean.getLanguage();
        String taskId = createDmeRdm(createBean, language);

        List<String> lunNames = taskService.getSuccessNameFromCreateTask(TASKTYPE, taskId, longTaskTimeOut);
        Map<String, Object> paramMap = initParams(createBean);
        String requestVolumeName = (String) paramMap.get("requestVolumeName");
        int capacity = (int) paramMap.get(CAPACITY);
        JsonArray volumeArr = getDmeVolumeIdByName(requestVolumeName);
        List<String> volumeIds = new ArrayList<>();
        for (int index = 0; index < volumeArr.size(); index++) {
            if (lunNames.contains(volumeArr.get(index).getAsJsonObject().get(NAME_FIELD).getAsString())) {
                volumeIds.add(volumeArr.get(index).getAsJsonObject().get(ID_FIELD).getAsString());
            }
        }

        // vmware上虚拟机归属的主机查询
        Map<String, String> vcenterHostMap = vcsdkUtils.getHostByVmObjectId(vmObjectId);

        // vmware上已挂载存储的所有主机
        String hostsJsonStr = vcsdkUtils.getMountHostsByDsObjectId(dataStoreObjectId);
        List<Map<String, String>> hostListOnVmware = gson.fromJson(hostsJsonStr,
            new TypeToken<List<Map<String, String>>>() { }.getType());
        String hostIp = vcenterHostMap.get("hostName");
        String hostObjectId = vcenterHostMap.get("hostId");
        String hostId = getDmeHostId(hostIp, hostObjectId);
        boolean isFind = false;
        for (int index = 0; index < hostListOnVmware.size(); index++) {
            String tempHostObjectId = hostListOnVmware.get(index).get("hostId");
            if (hostObjectId.equals(tempHostObjectId)) {
                isFind = true;
                break;
            }
        }
        if (!isFind) {
            hostListOnVmware.add(vcenterHostMap);
        }
        List<String> dmeMapingHostIds = new ArrayList<>();
        if (paramMap.get("mapping") == null) {
            for (int index = 0; index < hostListOnVmware.size(); index++) {
                String tempHostIp = hostListOnVmware.get(index).get("hostName");
                String tempHostObjectId = hostListOnVmware.get(index).get("hostId");
                String tempDmeHostId = getDmeHostId(tempHostIp, tempHostObjectId);
                dmeMapingHostIds.add(tempDmeHostId);
                dmeAccessService.hostMapping(tempDmeHostId, volumeIds, language);
            }
        }
        vcsdkUtils.hostRescanVmfs(hostIp);
        vcsdkUtils.hostRescanHba(hostIp);
        Map<String, JsonObject> lunMap = getLunMap(volumeArr, volumeIds, hostIp, hostId, language);
        String errorMsg = "";
        int lunSize = lunMap.size();
        if (lunSize > 0) {
            List<String> failedVolumeIds = new ArrayList();
            for (Map.Entry<String, JsonObject> entry : lunMap.entrySet()) {
                String volumeId = entry.getKey();
                JsonObject object = entry.getValue();
                try {
                    vcsdkUtils.createDisk(dataStoreObjectId, vmObjectId, object.get("devicePath").getAsString(),
                        capacity, compatibilityMode);
                } catch (VcenterException ex) {
                    failedVolumeIds.add(volumeId);
                    errorMsg = ex.getMessage();
                }
            }
            if (failedVolumeIds.size() > 0) {
                for (String dmeHostId : dmeMapingHostIds) {
                    unMapping(dmeHostId, failedVolumeIds, language);
                }
                deleteVolumes(failedVolumeIds, language);
                if (failedVolumeIds.size() == lunSize) {
                    throw new DmeException(errorMsg);
                }
            }
        } else {
            for (String dmeHostId : dmeMapingHostIds) {
                unMapping(dmeHostId, volumeIds, language);
            }
            deleteVolumes(volumeIds, language);
            throw new DmeException("No matching LUN information was found on the vCenter");
        }
    }

    private Map<String, Object> initParams(VmRdmCreateBean createBean) {
        String requestVolumeName;
        int capacity;
        ServiceVolumeMapping mapping;
        ServiceVolumeBasicParams params;
        if (createBean.getCreateVolumesRequest() != null) {
            requestVolumeName = createBean.getCreateVolumesRequest().getVolumes().get(0).getName();
            params = createBean.getCreateVolumesRequest().getVolumes().get(0);
            capacity = params.getCapacity();
            if (UNITTB.equals(params.getUnit())) {
                capacity = capacity * CONSTANT_1024;
            }
            mapping = createBean.getCreateVolumesRequest().getMapping();
        } else {
            requestVolumeName = createBean.getCustomizeVolumesRequest()
                .getCustomizeVolumes()
                .getVolumeSpecs()
                .get(0)
                .getName();
            params = createBean.getCustomizeVolumesRequest().getCustomizeVolumes().getVolumeSpecs().get(0);
            capacity = params.getCapacity();
            if (UNITTB.equals(params.getUnit())) {
                capacity = capacity * CONSTANT_1024;
            }
            mapping = createBean.getCustomizeVolumesRequest().getMapping();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("requestVolumeName", requestVolumeName);
        map.put(CAPACITY, capacity);
        if (mapping != null) {
            map.put("mapping", mapping);
        }

        return map;
    }

    private JsonArray getDmeVolumeIdByName(String requestVolumeName) throws DmeException {
        String url = DmeConstants.DME_VOLUME_BASE_URL + "?name=" + requestVolumeName;
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        JsonObject jsonObject = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        JsonArray volumeArr = jsonObject.getAsJsonArray("volumes");
        return volumeArr;
    }

    private Map<String, JsonObject> getLunMap(JsonArray volumeArr, List<String> volumeIds, String hostIp, String hostId,
        String language)
        throws DmeException {
        String lunStr = vcsdkUtils.getLunsOnHost(hostIp);
        if (StringUtil.isBlank(lunStr)) {
            // 将已经创建好的卷删除
            deleteVolumes(hostId, volumeIds, language);
            throw new DmeException("Failed to obtain the target LUN!");
        }
        LOG.info("get LUN information succeeded!");
        JsonArray lunArray = gson.fromJson(lunStr, JsonArray.class);
        Map<String, JsonObject> lunMap = new HashMap<>(MAP_DEFAULT_CAPACITY);
        for (int index = 0; index < lunArray.size(); index++) {
            JsonObject lunObject = lunArray.get(index).getAsJsonObject();
            for (int indexInner = 0; indexInner < volumeArr.size(); indexInner++) {
                JsonObject volume = volumeArr.get(indexInner).getAsJsonObject();
                String wwn = volume.get("volume_wwn").getAsString();
                if (ToolUtils.jsonToStr(lunObject.get("deviceName")).endsWith(wwn)) {
                    lunMap.put(ToolUtils.jsonToStr(volume.get(ID_FIELD)), lunObject);
                }
            }
        }
        return lunMap;
    }

    private String getDmeHostId(String hostIp, String hostObjectId) throws DmeException {
        String url = DmeConstants.DME_HOST_SUMMARY_URL;
        Map<String, String> queryHostParams = new HashMap<>(MAP_DEFAULT_CAPACITY);
        queryHostParams.put("ip", hostIp);
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST,
            gson.toJson(queryHostParams));
        String hostId = null;
        if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
            JsonObject dmeHost = gson.fromJson(responseEntity.getBody(), JsonObject.class);
            if (dmeHost.get("total").getAsInt() > 0) {
                JsonObject hostObject = dmeHost.getAsJsonArray("hosts").get(0).getAsJsonObject();
                hostId = ToolUtils.jsonToStr(hostObject.get(ID_FIELD));
            }
        }
        Map<String, List<Map<String, Object>>> allinitionators = vmfsAccessService.getAllInitionator();

        if (hostId == null) {
            hostId = vmfsAccessService.checkOrCreateToHost(hostIp, hostObjectId, allinitionators);
        }
        return hostId;
    }

    private void deleteVolumes(String hostId, List<String> volumeIds, String language) {
        try {
            dmeAccessService.unMapHost(hostId, volumeIds, language);
            dmeAccessService.deleteVolumes(volumeIds, language);
        } catch (DmeException ex) {
            LOG.error("delete volumes failed!");
        }
    }

    private void deleteVolumes(List<String> volumeIds, String language) {
        try {
            dmeAccessService.deleteVolumes(volumeIds, language);
        } catch (DmeException ex) {
            LOG.error("delete volumes failed!");
        }
    }

    private void unMapping(String hostId, List<String> volumeIds, String language) {
        try {
            dmeAccessService.unMapHost(hostId, volumeIds, language);
        } catch (DmeException ex) {
            LOG.error("unMapping volumes failed!");
        }
    }

    public String createDmeRdm(VmRdmCreateBean vmRdmCreateBean, String language) throws DmeException {
        String taskId;

        // 通过服务等级创建卷
        if (vmRdmCreateBean.getCreateVolumesRequest() != null) {
            taskId = createDmeVolumeByServiceLevel(vmRdmCreateBean.getCreateVolumesRequest());
        } else {
            // 用户自定义创建卷
            taskId = createDmeVolumeByUnServiceLevelNew(vmRdmCreateBean.getCustomizeVolumesRequest());
        }
        JsonObject taskDetail = taskService.queryTaskByIdUntilFinish(taskId);
        if (taskDetail.get(DmeConstants.TASK_DETAIL_STATUS_FILE).getAsInt() != DmeConstants.TASK_SUCCESS) {
            String errMessage = DmeConstants.LANGUAGE_CN.equals(language)?
                taskDetail.get(DmeConstants.TASK_DETAIL_CN).getAsString()
                :taskDetail.get(DmeConstants.TASK_DETAIL_EN).getAsString();
            LOG.error("The DME volume is in abnormal condition!taskDetail={}", gson.toJson(taskDetail));
            throw new DmeException(errMessage);
        }
        return taskId;
    }

    private String createDmeVolumeByServiceLevel(CreateVolumesRequest createVolumesRequest) throws DmeException {
        Map<String, Object> requestbody = new HashMap<>(MAP_DEFAULT_CAPACITY);
        List<ServiceVolumeBasicParams> requestVolumes = createVolumesRequest.getVolumes();
        List<Map<String, Object>> volumes = new ArrayList<>();
        for (ServiceVolumeBasicParams volume : requestVolumes) {
            Map<String, Object> svbp = new HashMap<>(MAP_DEFAULT_CAPACITY);
            svbp.put(NAME_FIELD, volume.getName());
            int capacity = volume.getCapacity();
            if (UNITTB.equals(volume.getUnit())) {
                capacity = capacity * CONSTANT_1024;
            }
            svbp.put(CAPACITY, capacity);
            svbp.put("count", volume.getCount());
            if (volume.getStartSuffix() != null && volume.getStartSuffix() > 0) {
                svbp.put("start_suffix", volume.getStartSuffix());
            }
            volumes.add(svbp);
        }

        requestbody.put("volumes", volumes);
        requestbody.put("service_level_id", createVolumesRequest.getServiceLevelId());
        String url = DmeConstants.DME_VOLUME_BASE_URL;
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, gson.toJson(requestbody));
        if (responseEntity.getStatusCodeValue() / DmeConstants.HTTPS_STATUS_CHECK_FLAG
            != DmeConstants.HTTPS_STATUS_SUCCESS_PRE) {
            LOG.error("Failed to create RDM by service_level!errorMsg:{}", responseEntity.getBody());
            throw new DmeException(responseEntity.getBody());
        }
        JsonObject task = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        String taskId = task.get("task_id").getAsString();

        return taskId;
    }

    private String createDmeVolumeByUnServiceLevelNew(CustomizeVolumesRequest customizeVolumesRequest)
        throws DmeException {
        String ownerController = customizeVolumesRequest.getCustomizeVolumes().getOwnerController();

        // 归属控制器自动则不下发参数
        final String ownerControllerAuto = "0";
        if (ownerControllerAuto.equals(ownerController)) {
            customizeVolumesRequest.getCustomizeVolumes().setOwnerController(null);
        }

        // 判断该集群下有多少主机，如果主机在DME不存在就需要创建
        Map<String, Object> requestbody = new HashMap<>(MAP_DEFAULT_CAPACITY);
        CustomizeVolumes customizeVolumes = customizeVolumesRequest.getCustomizeVolumes();
        String storageModel = getStorageModel(customizeVolumes.getStorageId());
        StorageTypeShow storageTypeShow = ToolUtils.getStorageTypeShow(storageModel);
        if (!storageTypeShow.getDorado()) {
            putNotNull(requestbody, "initial_distribute_policy",
                DmeConstants.INITIAL_DISTRIBUTE_POLICY.get(customizeVolumes.getInitialDistributePolicy()));
        }
        putNotNull(requestbody, "prefetch_value", customizeVolumes.getPrefetchValue());
        putNotNull(requestbody, "prefetch_policy",
            DmeConstants.PREFETCH_POLICY.get(customizeVolumes.getPrefetchPolicy()));
        putNotNull(requestbody, "owner_controller", customizeVolumes.getOwnerController());
        putNotNull(requestbody, "pool_id", customizeVolumes.getPoolRawId());
        putNotNull(requestbody, "storage_id", customizeVolumes.getStorageId());
        CustomizeVolumeTuningForCreate tuningBean = customizeVolumes.getTuning();
        if (tuningBean != null) {
            Map<String, Object> tuning = tuningParse(tuningBean);
            requestbody.put("tuning", tuning);
        }

        List<ServiceVolumeBasicParams> volumeSpecList = customizeVolumes.getVolumeSpecs();
        List<Map<String, Object>> volumeSpecs = new ArrayList<>();
        for (ServiceVolumeBasicParams volumeSpec : volumeSpecList) {
            Map<String, Object> vs = new HashMap<>(MAP_DEFAULT_CAPACITY);
            putNotNull(vs, NAME_FIELD, volumeSpec.getName());
            int capacity = volumeSpec.getCapacity();
            if (UNITTB.equals(volumeSpec.getUnit())) {
                capacity = capacity * CONSTANT_1024;
            }
            vs.put(CAPACITY, capacity);
            vs.put("count", volumeSpec.getCount());
            volumeSpecs.add(vs);
        }
        requestbody.put("lun_specs", volumeSpecs);
        String url = DmeConstants.DME_CREATE_VOLUME_UNLEVEL_URL;
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, gson.toJson(requestbody));
        if (responseEntity.getStatusCodeValue() / DmeConstants.HTTPS_STATUS_CHECK_FLAG
            != DmeConstants.HTTPS_STATUS_SUCCESS_PRE) {
            LOG.error("Failed to create RDM on DME!errorMsg:{}", responseEntity.getBody());
            throw new DmeException(responseEntity.getBody());
        }
        JsonObject task = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        String taskId = task.get("task_id").getAsString();

        return taskId;
    }

    private Map<String, Object> tuningParse(CustomizeVolumeTuningForCreate tuningBean) {
        Map<String, Object> tuning = new HashMap<>();
        if (tuningBean.getAlloctype() != null) {
            putNotNull(tuning, "alloction_type", tuningBean.getAlloctype());
        } else {
            tuning.put("alloction_type", "thin");
        }
        putNotNull(tuning, "smart_tier", DmeConstants.SMART_TIER.get(tuningBean.getSmarttier()));
        putNotNull(tuning, "workload_type_raw_id", tuningBean.getWorkloadTypeId());
        putNotNull(tuning, "compression_enabled", tuningBean.getCompressionEnabled());
        putNotNull(tuning, "deduplication_enabled", tuningBean.getDedupeEnabled());

        SmartQosForRdmCreate smartqosBean = tuningBean.getSmartqos();
        if (smartqosBean != null) {
            Map<String, Object> smartqos = new HashMap<>(MAP_DEFAULT_CAPACITY);
            if (smartqosBean.getLatency() != 0) {
                putNotNull(smartqos, "latency", smartqosBean.getLatency());
            }
            if (smartqosBean.getLatency() != 0) {
                putNotNull(smartqos, "latency", smartqosBean.getLatency());
            }
            if (smartqosBean.getMaxbandwidth() != 0) {
                putNotNull(smartqos, "max_bandwidth", smartqosBean.getMaxbandwidth());
            }
            if (smartqosBean.getMaxiops() != 0) {
                putNotNull(smartqos, "max_iops", smartqosBean.getMaxiops());
            }
            if (smartqosBean.getMinbandwidth() != 0) {
                putNotNull(smartqos, "min_bandwidth", smartqosBean.getMinbandwidth());
            }
            if (smartqosBean.getMiniops() != 0) {
                putNotNull(smartqos, "min_iops", smartqosBean.getMiniops());
            }
            tuning.put("smart_qos", smartqos);
        }

        return tuning;
    }

    @Override
    public List<Map<String, Object>> getAllDmeHost() throws DmeException {
        return dmeAccessService.getDmeHosts(null);
    }

    @Override
    public List<Object> getDatastoreMountsOnHost(String vmObjectId) throws DmeException {
        return vcsdkUtils.getDatastoreMountsOnHost(vmObjectId);
    }

    private void putNotNull(Map<String, Object> map, String key, Object value) {
        if (value != null) {
            if (value instanceof String) {
                map.put(key, String.valueOf(value));
            }
            map.put(key, value);
        }
    }

    private String getStorageModel(String storageId) throws DmeException {
        StorageDetail storageDetail = dmeStorageService.getStorageDetail(storageId);
        return storageDetail.getModel() + " " + storageDetail.getProductVersion();
    }

    @Override
    public List<Map<String, String>> getVmRdmByObjectId(String vmObjectId) throws DmeException {
        List<Map<String, String>> reList = new ArrayList<>();
        try {
            List<Map<String, String>> vcRdmList = vcsdkUtils.getVmRdmByObjectId(vmObjectId);
            for (int i = 0; i < vcRdmList.size(); i++) {
                Map<String, String> vcRdmMap = vcRdmList.get(i);
                String wwn = vcRdmMap.get("lunWwn");

                // 根据wwn查询DME Lun信息
                String url = DmeConstants.DME_VOLUME_BASE_URL + "?volume_wwn=" + wwn;
                ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
                if (responseEntity.getStatusCodeValue() / 100 == 2) {
                    JsonObject object = new JsonParser().parse(responseEntity.getBody()).getAsJsonObject();
                    if (object.get("count").getAsInt() == 0) {
                        continue;
                    }
                    JsonObject dmeVolume = object.getAsJsonArray("volumes").get(0).getAsJsonObject();
                    vcRdmMap.put("lunName", dmeVolume.get("name").getAsString());
                    reList.add(vcRdmMap);
                }
            }

        } catch (Exception exception) {
            throw new DmeException(exception.getMessage());
        }

        return reList;
    }

    @Override
    public String delVmRdmByObjectId(String vmObjectId, List<DelVmRdmsRequest> disks) throws DmeException {
        JsonObject reObj = new JsonObject();
        reObj.addProperty("code", 200);
        reObj.add("data", null);
        reObj.addProperty("description", "");
        try {
            boolean isConnected = vcsdkUtils.vmIsConnected(vmObjectId);
            if(!isConnected){
                reObj.addProperty("code", 500);
                reObj.addProperty("description", "The RMD cannot be deleted in the current status!");
                return reObj.toString();
            }
            List<String> diskObjectIds = disks.stream()
                .map(DelVmRdmsRequest::getDiskObjectId)
                .collect(Collectors.toList());
            List<String> wwns = disks.stream().map(DelVmRdmsRequest::getLunWwn).collect(Collectors.toList());

            // 调用vCenter删除RDM
            vcsdkUtils.delVmRdm(vmObjectId, diskObjectIds);

            Map<String, List<String>> unMap = new HashMap<>();
            for (String wwn : wwns) {
                String url = DmeConstants.DME_VOLUME_BASE_URL + "?volume_wwn=" + wwn;
                ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
                if (responseEntity.getStatusCodeValue() / 100 == 2) {
                    JsonObject object = new JsonParser().parse(responseEntity.getBody()).getAsJsonObject();
                    if (object.get("count").getAsInt() == 0) {
                        continue;
                    }
                    JsonObject dmeVolume = object.getAsJsonArray("volumes").get(0).getAsJsonObject();
                    JsonArray attachments = dmeVolume.getAsJsonArray("attachments");
                    if (attachments == null || attachments.isJsonNull() || attachments.size() == 0) {
                        continue;
                    }
                    for (int i = 0; i < attachments.size(); i++) {
                        JsonObject attachment = attachments.get(i).getAsJsonObject();
                        String hostId = attachment.get("host_id").getAsString();
                        String volumeId = attachment.get("volume_id").getAsString();
                        if (unMap.get(hostId) == null) {
                            unMap.put(hostId, new ArrayList<>());
                        }
                        unMap.get(hostId).add(volumeId);
                    }
                }
            }
            int errCnt = 0;
            JsonArray hostArray = new JsonArray();
            JsonArray errorMsgArray = new JsonArray();
            for ( Map.Entry<String, List<String>> entry :unMap.entrySet()){
                String dmeHostId = entry.getKey();
                String hostIp = null;
                try {
                    dmeAccessService.unMapHost(dmeHostId, entry.getValue());
                    //根据主机ID获取主机IP后刷新vCenter vmfs存储
                    Map<String, Object> dmeHostMap = dmeAccessService.getDmeHost(dmeHostId);
                    hostIp = String.valueOf(dmeHostMap.get("ip"));
                    vcsdkUtils.hostRescanVmfs(hostIp);
                }catch (DmeException dmeException){
                    errCnt++;
                    hostArray.add(new JsonPrimitive(hostIp));
                    JsonObject errorObj = new JsonObject();
                    errorObj.addProperty("host_ip", hostIp);
                    errorObj.addProperty("error_message", dmeException.getMessage());
                    errorMsgArray.add(errorObj);
                    LOG.error("host unmapping error!hostIp={},errorMsg={}", hostIp, dmeException.getMessage());
                    continue;
                }
            }
            if(errCnt > 0){
                reObj.addProperty("code", errCnt == unMap.size()? 500: 206);
                reObj.add("data", hostArray);
                reObj.addProperty("description", errorMsgArray.toString());
            }
        } catch (Exception ex) {
            reObj.addProperty("code", 500);
            reObj.addProperty("description", ex.getMessage());
        }

        return reObj.toString();
    }
}
