package com.huawei.dmestore.services;

import com.google.gson.reflect.TypeToken;
import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.constant.DpSqlFileConstants;
import com.huawei.dmestore.dao.DmeVmwareRalationDao;
import com.huawei.dmestore.entity.DmeVmwareRelation;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.VcenterException;
import com.huawei.dmestore.model.*;
import com.huawei.dmestore.utils.ToolUtils;
import com.huawei.dmestore.utils.VCSDKUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.util.*;

/**
 * VmfsOperationService
 *
 * @author lianqiangN
 * @since 2020-09-09
 **/
public class VmfsOperationServiceImpl implements VmfsOperationService {
    private static final Logger LOG = LoggerFactory.getLogger(VmfsOperationServiceImpl.class);

    private static final int DEFAULT_CAPACITY = 16;

    private static final String CODE_403 = "403";

    private static final String CODE_503 = "503";

    private static final String TASK_ID = "task_id";

    private static final String CHANGE_SECTOR = "changedSector";

    private static final String LUN_ADD_CAPACITY = "lunAddCapacity";

    private static final String HOSTID = "hostId";

    private DmeAccessService dmeAccessService;
    @Autowired
    private DmeStorageService dmeStorageService;

    private VmfsAccessServiceImpl vmfsAccessService;

    private Gson gson = new Gson();

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

    public VmfsAccessServiceImpl getVmfsAccessService() {
        return vmfsAccessService;
    }

    public void setVmfsAccessService(VmfsAccessServiceImpl vmfsAccessService) {
        this.vmfsAccessService = vmfsAccessService;
    }
    private DmeVmwareRalationDao dmeVmwareRalationDao;
    public DmeVmwareRalationDao getDmeVmwareRalationDao() {
        return dmeVmwareRalationDao;
    }

    public void setDmeVmwareRalationDao(DmeVmwareRalationDao dmeVmwareRalationDao) {
        this.dmeVmwareRalationDao = dmeVmwareRalationDao;
    }

    @Override
    public void updateVmfs(String volumeId, Map<String, Object> params) throws DmeException {
        Map<String, Object> volumeMap = new HashMap<>();
        Object serviceLevelName = params.get("service_level_name");
        if (StringUtils.isEmpty(serviceLevelName)) {
            Map<String, Object> customizeVolumeTuning = getCustomizeVolumeTuning(params);
            LOG.info("自定义方式创建vmfs{},服务等级：", serviceLevelName);
            if (customizeVolumeTuning.size() != 0 && customizeVolumeTuning != null) {
                volumeMap.put("tuning", customizeVolumeTuning);
            }
        }

        Object newVoName = params.get("newVoName");
        if (!StringUtils.isEmpty(newVoName)) {
            volumeMap.put("name", newVoName.toString());
        }

        Map<String, Object> reqMap = new HashMap<>(DEFAULT_CAPACITY);
        reqMap.put("volume", volumeMap);
        String reqBody = gson.toJson(reqMap);

        String url = DmeConstants.DME_VOLUME_BASE_URL + "/" + volumeId;
        try {
            Object oldDsName = params.get("oldDsName");
            Object newDsName = params.get("newDsName");
            if (StringUtils.isEmpty(oldDsName) || StringUtils.isEmpty(newDsName)) {
                throw new DmeException(CODE_403, "datastore params error");
            }
            Object dataStoreObjectId = params.get("dataStoreObjectId");
            String result = "";
            if (dataStoreObjectId != null) {
                result = vcsdkUtils.renameDataStore(newDsName.toString(), dataStoreObjectId.toString());
            }
            if (StringUtils.isEmpty(result) || "failed".equals(result)) {
                throw new DmeException(CODE_503, "vmware update VmfsDatastore failed!");
            }
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.PUT, reqBody);
            int code = responseEntity.getStatusCodeValue();
            LOG.info("dme update vmfs,response:code={},response body={}", code, responseEntity.getBody());
            if (code != HttpStatus.ACCEPTED.value()) {
                throw new DmeException(CODE_503, "dme update VmfsDatastore failed!");
            }
            JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody()).getAsJsonObject();
            String taskId = ToolUtils.jsonToStr(jsonObject.get(TASK_ID));
            List<String> taskIds = new ArrayList<>();
            taskIds.add(taskId);
            Boolean flag = taskService.checkTaskStatus(taskIds);
            if (!flag) {
                LOG.error("update vmfs datastore error");
                throw new DmeException("400", "update vmfs datastore error");
            }
            //刷新数据库存储数据信息
            DmeVmwareRelation dmeVmwareRelation = new DmeVmwareRelation();
            dmeVmwareRelation.setStoreId((String)params.get("dataStoreObjectId"));
            dmeVmwareRelation.setStoreName((String) params.get("name"));
            dmeVmwareRalationDao.updateVmfsByStoreId(dmeVmwareRelation);
        } catch (DmeException e) {
            LOG.error("update vmfsDatastore error", e);
            throw new DmeException(CODE_503, e.getMessage());
        }
    }

    private Map<String, Object> getCustomizeVolumeTuning(Map<String, Object> params) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> customizeVolumeTuning = new HashMap<>();
        Boolean qosFlag = (Boolean) params.get("qosFlag");
        if (qosFlag) {
            Object controlPolicy = params.get("control_policy");
            if (!StringUtils.isEmpty(controlPolicy)) {
                map.put("control_policy", controlPolicy.toString());
            }
            Object maxIops = params.get("max_iops");
            if (!StringUtils.isEmpty(maxIops)) {
                map.put("maxiops", Integer.valueOf(maxIops.toString()));
            }
            Object minIops = params.get("min_iops");
            if (!StringUtils.isEmpty(minIops)) {
                map.put("miniops", Integer.valueOf(minIops.toString()));
            }
            Object maxBandwidth = params.get("max_bandwidth");
            if (!StringUtils.isEmpty(maxBandwidth)) {
                map.put("maxbandwidth", Integer.valueOf(maxBandwidth.toString()));
            }
            Object minBandwidth = params.get("min_bandwidth");
            if (!StringUtils.isEmpty(minBandwidth)) {
                map.put("minbandwidth", Integer.valueOf(minBandwidth.toString()));
            }

            Object latency = params.get("latency");
            if (!StringUtils.isEmpty(latency)) {
                map.put("latency", Integer.valueOf(latency.toString()));
            }
            map.put("enabled", true);
        }else {
            map.put("enabled",false);
        }
        customizeVolumeTuning.put("smartqos", map);
        Boolean smartTierFlag = (Boolean) params.get("smartTierFlag");
        if (smartTierFlag) {
            customizeVolumeTuning.put("smarttier", ToolUtils.getStr(params.get("smartTier")));
        }else {
            customizeVolumeTuning.put("smarttier", null);
        }
        return customizeVolumeTuning;
    }

    @Override
    public void expandVmfs(Map<String, String> volumemap) throws DmeException {
        Map<String, Object> reqBody = new HashMap<>(DEFAULT_CAPACITY);
        List<String> volumeIds = new ArrayList<>(DEFAULT_CAPACITY);

        String volumeId = volumemap.get("volume_id");
        String datastoreobjid = volumemap.get("obj_id");
        String voAddCapacity = volumemap.get("vo_add_capacity");
        if (!StringUtils.isEmpty(volumeId) && !StringUtils.isEmpty(voAddCapacity)) {
            volumeIds.add(volumeId);
        }
        volumeIds.add(volumeId);
        reqBody.put("volume_id", volumeId);
        reqBody.put("added_capacity", Integer.valueOf(volumemap.get("vo_add_capacity")));
        List<Object> reqList = new ArrayList<>(DEFAULT_CAPACITY);
        reqList.add(reqBody);
        Map<String, Object> reqMap = new HashMap<>(DEFAULT_CAPACITY);
        reqMap.put("volumes", reqList);
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(DmeConstants.API_VMFS_EXPAND,
                HttpMethod.POST, gson.toJson(reqMap));
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.ACCEPTED.value()) {
                throw new DmeException(CODE_503, "expand vmfsDatastore failed !");
            }
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            String taskId = ToolUtils.jsonToStr(jsonObject.get(TASK_ID));
            List<String> taskIds = new ArrayList<>(DEFAULT_CAPACITY);
            taskIds.add(taskId);
            Boolean flag = taskService.checkTaskStatus(taskIds);
            if (!flag) {
                throw new DmeException(CODE_503, "expand volume failed !");
            }
            String dsName = volumemap.get("ds_name");
            String result = null;
            if (!StringUtils.isEmpty(voAddCapacity) && !StringUtils.isEmpty(datastoreobjid)) {
                result = vcsdkUtils.expandVmfsDatastore(dsName, ToolUtils.getInt(voAddCapacity), datastoreobjid);
            }
            if ("failed".equals(result)) {
                throw new DmeException(CODE_403, "expand vmfsDatastore failed !");
            }

            // 刷新vCenter存储
            vcsdkUtils.refreshDatastore(datastoreobjid);
        } catch (DmeException e) {
            LOG.error("expand vmfsDatastore error !", e);
            throw new DmeException(CODE_503, e.getMessage());
        }
    }

    @Override
    public void expandVmfs2(Map<String, String> volumemap) throws DmeException {

        String datastoreobjid = volumemap.get("obj_id");
        int addCapacity = ToolUtils.getInt(volumemap.get("vo_add_capacity"));

        try {
            //扩容条件判断。
            Map<String, Long> sectors = compareCapacityForExpandLun(addCapacity, datastoreobjid);
            if (CollectionUtils.isEmpty(sectors)) {
                LOG.error("get current vmfs datastore capacity failed!{}", datastoreobjid);
                throw new DmeException("get current vmfs datastore capacity failed!{}", datastoreobjid);
            }
            Long changedSector = sectors.get(CHANGE_SECTOR);
            Long addcapacity = sectors.get(LUN_ADD_CAPACITY);
            if (addcapacity != null) {
                String lunAddCapacity = ToolUtils.getStr(addcapacity);
                //DME Lun当前容量不满足扩容vmfs存储条件，扩容Lun
                volumemap.put("vo_add_capacity", lunAddCapacity);
                Map<String, Object> stringObjectMap = handleParam(volumemap);
                ResponseEntity<String> responseEntity = dmeAccessService.access(DmeConstants.API_VMFS_EXPAND,
                        HttpMethod.POST, gson.toJson(stringObjectMap));
                int code = responseEntity.getStatusCodeValue();
                if (code != HttpStatus.ACCEPTED.value()) {
                    throw new DmeException(CODE_503, "expand vmfsDatastore failed !");
                }
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody()).getAsJsonObject();
                String taskId = ToolUtils.jsonToStr(jsonObject.get(TASK_ID));
                List<String> taskIds = new ArrayList<>(DEFAULT_CAPACITY);
                taskIds.add(taskId);
                if (!taskService.checkTaskStatus(taskIds)) {
                    throw new DmeException(CODE_503, "expand volume failed !");
                }
                /*String listStr = vcsdkUtils.getHostsByDsObjectId(datastoreobjid, true);
                if (!StringUtils.isEmpty(listStr)) {
                    List<Map<String, String>> hosts = gson.fromJson(listStr,
                            new TypeToken<List<Map<String, String>>>() {
                            }.getType());
                    for (Map<String, String> host : hosts) {
                        String hostId = ToolUtils.getStr(host.get(HOSTID));
                        vcsdkUtils.scanDataStore(null, hostId);
                    }
                }*/
            }
            if (changedSector != null) {
                String result = vcsdkUtils.expandVmfsDatastore2(changedSector, datastoreobjid);
                if ("failed".equals(result)) {
                    throw new DmeException(CODE_403, "expand vmfsDatastore failed !");
                }
            }
            // 刷新vCenter存储
            vcsdkUtils.refreshDatastore(datastoreobjid);
        } catch (DmeException e) {
            LOG.error("expand vmfsDatastore error !", e);
            throw new DmeException(CODE_503, e.getMessage());
        }
    }

    private Map<String, Object> handleParam(Map<String, String> volumemap){
        Map<String, Object> reqBody = new HashMap<>(DEFAULT_CAPACITY);
        List<String> volumeIds = new ArrayList<>(DEFAULT_CAPACITY);
        String volumeId = volumemap.get("volume_id");
        String voAddCapacity = volumemap.get("vo_add_capacity");
        if (!StringUtils.isEmpty(volumeId) && !StringUtils.isEmpty(voAddCapacity)) {
            volumeIds.add(volumeId);
            reqBody.put("added_capacity", Integer.valueOf(volumemap.get("vo_add_capacity")));
        }
        volumeIds.add(volumeId);
        reqBody.put("volume_id", volumeId);
        List<Object> reqList = new ArrayList<>(DEFAULT_CAPACITY);
        reqList.add(reqBody);
        Map<String, Object> reqMap = new HashMap<>(DEFAULT_CAPACITY);
        reqMap.put("volumes", reqList);

        return reqMap;
    }

    private Map<String,Long> compareCapacityForExpandLun(int addCapacity,String storeId){

        //判断vmfs设备变化量与当前可用量之前的关系，满足条件 则不需要去扩容Lun
        Map<String, Long> sectors = vcsdkUtils.queryVmfsDeviceCapacity(storeId);
        if (!CollectionUtils.isEmpty(sectors)) {
            Long addSector = addCapacity * 1l * ToolUtils.GI / ToolUtils.DISK_SECTOR_SIZE;
            Long currentEndSector = sectors.get(ToolUtils.CURRENT_END_SECTOR);
            Long totalCurrentSector = sectors.get(ToolUtils.TOTAL_END_SECTOR);
            Long changedSector = addSector + currentEndSector;
            int lunAddCapacity = addCapacity;
            if (totalCurrentSector != null) {
                if (Long.compare(totalCurrentSector, changedSector) != -1) {
                    sectors.put(CHANGE_SECTOR, changedSector);
                } else if (Long.compare(totalCurrentSector, changedSector) == -1 && Long.compare(totalCurrentSector, currentEndSector) != -1) {
                    int available = (int) Math.floor((totalCurrentSector - currentEndSector) * ToolUtils.DISK_SECTOR_SIZE / ToolUtils.GI);
                    if (Long.compare(available, 1) != -1) {
                        lunAddCapacity -= available;
                    }
                    sectors.put(CHANGE_SECTOR, changedSector);
                    sectors.put(LUN_ADD_CAPACITY, Long.valueOf(lunAddCapacity));
                }
            } else {
                sectors.put(CHANGE_SECTOR, changedSector);
                sectors.put(LUN_ADD_CAPACITY, Long.valueOf(lunAddCapacity));
            }
        }
        return sectors;
    }


    @Override
    public void recycleVmfsCapacity(List<String> dsObjectIds) throws DmeException {
        try {
            String result = null;
            if (dsObjectIds != null && dsObjectIds.size() > 0) {
                for (int index = 0; index < dsObjectIds.size(); index++) {
                    result = vcsdkUtils.recycleVmfsCapacity(dsObjectIds.get(index));
                }
            }
            if (result == null || "error".equals(result)) {
                throw new DmeException(CODE_403, "recycle vmfsDatastore error");
            }
        } catch (VcenterException e) {
            LOG.error("recycle vmfsDatastore error !", e);
            throw new DmeException(CODE_503, e.getMessage());
        }
    }

    @Override
    public boolean canRecycleVmfsCapacity(List<String> dsObjectIds) throws DmeException {

        boolean isThinVmdatastore = false;
        if (dsObjectIds != null && dsObjectIds.size() > 0) {
            for (int index = 0; index < dsObjectIds.size(); index++) {
                List<VmfsDatastoreVolumeDetail> detaillists = vmfsAccessService.volumeDetail(dsObjectIds.get(index));
                for (VmfsDatastoreVolumeDetail vmfsDatastoreVolumeDetail : detaillists) {
                    if ("thin".equalsIgnoreCase(vmfsDatastoreVolumeDetail.getProvisionType())) {
                        isThinVmdatastore = true;
                        break;
                    }
                }
            }
        }
        return isThinVmdatastore;

    }

    @Override
    public void recycleVmfsCapacityByDataStoreIds(List<String> dsIds) throws DmeException {
        try {
            String result = null;
            if (dsIds != null && dsIds.size() > 0) {
                for (int index = 0; index < dsIds.size(); index++) {
                    result = vcsdkUtils.recycleVmfsCapacity(dsIds.get(index));
                }
            }
            if (result == null || "error".equals(result)) {
                throw new DmeException(CODE_403, "recycle vmfsDatastore error");
            }
        } catch (VcenterException e) {
            LOG.error("recycle vmfsDatastore error !", e);
            throw new DmeException(CODE_503, e.getMessage());
        }
    }

    @Override
    public void updateVmfsServiceLevel(Map<String, Object> params) throws DmeException {
        if (params == null || params.size() == 0) {
            throw new DmeException(CODE_403, "params error,please check your params!");
        }
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("service_level_id", params.get("service_level_id"));
        reqMap.put("volume_ids", params.get("volume_ids"));
        // 自动变更属性
        reqMap.put("attributes_auto_change", true);
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(DmeConstants.API_SERVICELEVEL_UPDATE,
                HttpMethod.POST, gson.toJson(reqMap));
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.ACCEPTED.value()) {
                throw new DmeException(CODE_503, "update vmfs service level error!");
            }
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            String taskId = jsonObject.get(TASK_ID).getAsString();
            List<String> taskIds = new ArrayList<>();
            taskIds.add(taskId);
            boolean flag = taskService.checkTaskStatus(taskIds);
            if (!flag) {
                throw new DmeException("400", "update vmfs service level failed");
            }
        } catch (DmeException e) {
            LOG.error("update vmfs service level error !", e);
            throw new DmeException(CODE_503, e.getMessage());
        }
    }

    @Override
    public List<SimpleServiceLevel> listServiceLevelVmfs(Map<String, Object> params) throws DmeException {
        List<SimpleServiceLevel> simpleServiceLevels = new ArrayList<>(DEFAULT_CAPACITY);
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(DmeConstants.LIST_SERVICE_LEVEL_URL,
                HttpMethod.GET, gson.toJson(params));
            int code = responseEntity.getStatusCodeValue();
            if (code != HttpStatus.OK.value()) {
                throw new DmeException(CODE_503, "list vmfs service level error !");
            }
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("service-levels").getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                SimpleServiceLevel simpleServiceLevel = new SimpleServiceLevel();
                parseBaseInfo(element, simpleServiceLevel);
                SimpleCapabilities capability = new SimpleCapabilities();
                JsonObject capabilities = element.get("capabilities").getAsJsonObject();
                capability.setResourceType(ToolUtils.jsonToStr(capabilities.get("resource_type")));
                capability.setCompression(ToolUtils.jsonToStr(capabilities.get("compression")));
                capability.setDeduplication(ToolUtils.jsonToStr(capabilities.get("deduplication")));
                CapabilitiesSmarttier smarttier = new CapabilitiesSmarttier();
                if (!"".equals(ToolUtils.jsonToStr(capabilities.get("smarttier")))) {
                    JsonObject smarttiers = capabilities.get("smarttier").getAsJsonObject();
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
                if (qos != null && !"".equals(ToolUtils.getStr(qos.get("qos_param")))) {
                    qosParse(qos, smartQos);
                }

                capabilitiesQos.setSmartQos(smartQos);
                capability.setQos(capabilitiesQos);
                simpleServiceLevel.setCapabilities(capability);
                simpleServiceLevels.add(simpleServiceLevel);
            }
        } catch (DmeException e) {
            LOG.error("list vmfs service level success !", e);
            throw new DmeException(CODE_503, e.getMessage());
        }
        return simpleServiceLevels;
    }

    private void qosParse(JsonObject qos, SmartQos smartQos) {
        JsonObject jsonObject1 = qos.get("qos_param").getAsJsonObject();
        smartQos.setLatency(ToolUtils.jsonToInt(jsonObject1.get("latency"), 0));
        smartQos.setMinbandwidth(ToolUtils.jsonToInt(jsonObject1.get("minBandWidth"), 0));
        smartQos.setMiniops(ToolUtils.jsonToInt(jsonObject1.get("minIOPS"), 0));
        smartQos.setLatencyUnit(ToolUtils.jsonToStr(jsonObject1.get("latencyUnit")));
    }

    private void parseBaseInfo(JsonObject element, SimpleServiceLevel simpleServiceLevel) {
        simpleServiceLevel.setId(ToolUtils.jsonToStr(element.get("id")));
        simpleServiceLevel.setName(ToolUtils.jsonToStr(element.get("name")));
        simpleServiceLevel.setDescription(ToolUtils.jsonToStr(element.get("description")));
        simpleServiceLevel.setType(ToolUtils.jsonToStr(element.get("type")));
        simpleServiceLevel.setProtocol(ToolUtils.jsonToStr(element.get("protocol")));
        simpleServiceLevel.setTotalCapacity(ToolUtils.jsonToDou(element.get("total_capacity"), 0.0));
        simpleServiceLevel.setFreeCapacity(ToolUtils.jsonToDou(element.get("free_capacity"), 0.0));
        simpleServiceLevel.setUsedCapacity(ToolUtils.jsonToDou(element.get("used_capacity"), 0.0));
    }

    @Override
    public StorageDetail getVmfsDetail(String storeId) throws DmeException {
        //首先根据storeId查询数据库，获取对应的关系信息数据
        DmeVmwareRelation vmRelations = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(storeId);
        //获取对应的storageId
        if (StringUtils.isEmpty(vmRelations)){
            throw new DmeException("500", "get dmevmware relation by storeid error ");
        }
        if(StringUtils.isEmpty(vmRelations.getStorageDeviceId())){
            throw new DmeException("500", "get dmevmware relation by storeid error ");
        }
        if(StringUtils.isEmpty(vmRelations.getVolumeId())){
            throw new DmeException("500", "get dmevmware relation by storeid error ");
        }
        String storageId = vmRelations.getStorageDeviceId();
        StorageDetail storageObj = dmeStorageService.getStorageDetail(storageId);
        /*增加查询指定lun方法，返回给前端页面，作为判断qos策略的依据
         * 1.首先根据前端页面的storageid获取volumeId
         * 2.根据volumeId查询对应卷的数据信息**/
         Map<String, Object> map = getLunDetailByVolumeId(vmRelations.getVolumeId());
            boolean qosFlag = (boolean) map.get("qosFlag");
            SmartQos smartosQ = (SmartQos) map.get("smartosQ");
            storageObj.setQosFlag(qosFlag);
            storageObj.setSmartQos(smartosQ);
        return storageObj;
    }
    /**
     * @Description: 查询知道lun信息
     * @Param String volumeId
     * @return boolean
     * @throws DmeException
     * @author yc
     * @Date 2021/4/16 15:39
     */
    private Map<String,Object> getLunDetailByVolumeId(String volumeId) throws DmeException{
        boolean flag = false;
        Map<String,Object> map  = new HashMap<>();
        if (!StringUtils.isEmpty(volumeId)){
            String url = DmeConstants.DME_VOLUME_BASE_URL + "/" + volumeId;
            try {
                ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET,
                        null);
                int code = responseEntity.getStatusCodeValue();
                if (code != HttpStatus.OK.value()) {
                    throw new DmeException(CODE_503, "get volume error !");
                }
                Object object = responseEntity.getBody();
                if (object != null) {
                    JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
                    JsonElement volume = jsonObject.get("volume");
                    JsonObject volumeJson = new JsonParser().parse(volume.toString()).getAsJsonObject();
                    JsonElement tuning = volumeJson.get("tuning");
                    JsonObject tuningJson = new JsonParser().parse(tuning.toString()).getAsJsonObject();
                    JsonElement smartqos = tuningJson.get("smartqos");
                    if(!smartqos.isJsonNull()) {
                        JsonObject smartqosJson = new JsonParser().parse(smartqos.toString()).getAsJsonObject();
                        flag = ckeckSmartQosIsEmpty(smartqosJson);
                        map.put("qosFlag", flag);
                    }else {
                        map.put("qosFlag", false);
                    }
                    SmartQos smartosQ = null;
                    if (flag){
                        smartosQ = new Gson().fromJson(new JsonParser().parse(smartqos.toString()).getAsJsonObject(),SmartQos.class);
                        map.put("smartosQ",smartosQ);
                    }

                }
            }catch (DmeException e){
                LOG.error("get volume's info  error", e);
                throw new DmeException(CODE_503, e.getMessage());
            }
        }
        return map;
    }
    /**
     * @Description: 检验对象是否为空
     * @Param JsonObject smartqosJson
     * @return boolean
     * @author yc
     * @Date 2021/4/16 15:38
     */
    private boolean ckeckSmartQosIsEmpty(JsonObject smartqosJson) {
        if (StringUtils.isEmpty(smartqosJson)||
                (StringUtils.isEmpty(smartqosJson.get("smartqosJson")) &&
                        StringUtils.isEmpty(smartqosJson.get("maxiops")) &&
                        StringUtils.isEmpty(smartqosJson.get("minbandwidth")) &&
                        StringUtils.isEmpty(smartqosJson.get("miniops")) &&
                        StringUtils.isEmpty(smartqosJson.get("maxiops")) &&
                        StringUtils.isEmpty(smartqosJson.get("latency")))){
            return false;
        }else {
            return true;
        }
    }
}
