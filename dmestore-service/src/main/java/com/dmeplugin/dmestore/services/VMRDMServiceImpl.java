package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.CreateVolumesRequest;
import com.dmeplugin.dmestore.model.CustomizeVolumesRequest;
import com.dmeplugin.dmestore.model.DMEHostInfo;
import com.dmeplugin.dmestore.model.VmRDMCreateBean;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vmware.vim25.HostScsiDisk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author wangxiangyong
 * @version 1.0.0
 * @ClassName VMRDMService.java
 * @Description TODO
 * @createTime 2020年09月09日 10:58:00
 */
public class VMRDMServiceImpl implements VMRDMService {
    private static final Logger LOG = LoggerFactory.getLogger(VMRDMServiceImpl.class);

    private DmeAccessService dmeAccessService;

    private TaskService taskService;

    private Gson gson = new Gson();

    private VCSDKUtils vcsdkUtils;

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
    public void createRDM(String vm_objectId, String host_objectId, VmRDMCreateBean createBean) throws Exception {
        createDmeRDM(createBean);
        LOG.info("create DME disk succeeded!");
        String requestVolumeName = "";
        int size;
        int count;
        if (createBean.getCreateVolumesRequest() != null) {
            requestVolumeName = createBean.getCreateVolumesRequest().getVolumes().get(0).getName();
            size = createBean.getCreateVolumesRequest().getVolumes().get(0).getCapacity();
            count = createBean.getCreateVolumesRequest().getVolumes().get(0).getCount();
        } else {
            requestVolumeName = createBean.getCustomizeVolumesRequest().getCustomize_volumes().getVolume_specs().get(0).getName();
            size = createBean.getCustomizeVolumesRequest().getCustomize_volumes().getVolume_specs().get(0).getCapacity();
            count = createBean.getCustomizeVolumesRequest().getCustomize_volumes().getVolume_specs().get(0).getCount();
        }
        //根据卷名称查询已创建的卷信息
        String url = DmeConstants.DME_VOLUME_BASE_URL + "?name=" + requestVolumeName;
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        JsonObject jsonObject = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        JsonArray volumeArr = jsonObject.getAsJsonArray("volumes");
        List<String> volumeIds = new ArrayList<>();
        for (int i = 0; i < volumeArr.size(); i++) {
            volumeIds.add(volumeArr.get(i).getAsJsonObject().get("id").getAsString());
        }
        //datastore_objectId =
        //host_objectId = "urn:vmomi:HostSystem:host-224:f8e381d7-074b-4fa9-9962-9a68ab6106e1";
        String _host_id = vcsdkUtils.getVcConnectionHelper().objectID2Serverguid(host_objectId);
        LOG.info("host_objectId:{}, DME host_id:{}", host_objectId, _host_id);
        //将卷映射给主机
        hostMapping(_host_id, volumeIds);
        LOG.info("disk mapping to host succeeded!");

        //查询主机信息
        Map<String, Object> hostMap = dmeAccessService.getDmeHost(_host_id);
        String host_ip = hostMap.get("ip").toString();
        String host_name = hostMap.get("name").toString();
        //调用vcenter扫描卷
        vcsdkUtils.hostRescanVmfs(host_ip);
        LOG.info("scan vmfs succeeded!");

        //获取LUN信息
        String lunStr = vcsdkUtils.getLunsOnHost(host_ip);
        JsonArray lunArray = gson.fromJson(lunStr, JsonArray.class);
        List<JsonObject> lunObjects = new ArrayList<>();
        for (int i = 0; i < lunArray.size(); i++) {
            JsonObject lunObject = lunArray.get(i).getAsJsonObject();
            for (int j = 0; j < volumeArr.size(); j++) {
                JsonObject volume = volumeArr.get(j).getAsJsonObject();
                String wwn = volume.get("volume_wwn").getAsString();
                if (lunObject.get("deviceName").getAsString().endsWith(wwn)) {
                    lunObjects.add(lunObject);
                }
            }
        }

        for (JsonObject object : lunObjects) {
            //调用Vcenter创建磁盘
            vcsdkUtils.createDisk(vm_objectId, object.get("devicePath").getAsString(), size);
        }
    }

    public void createDmeRDM(VmRDMCreateBean vmRDMCreateBean) throws Exception {
        String taskId;
        //通过服务等级创建卷
        if (vmRDMCreateBean.getCreateVolumesRequest() != null) {
            taskId = createDMEVolumeByServiceLevel(vmRDMCreateBean.getCreateVolumesRequest());
        } else {
            //用户自定义创建卷
            taskId = createDMEVolumeByUnServiceLevel(vmRDMCreateBean.getCustomizeVolumesRequest());
        }
        JsonObject taskDetail = taskService.queryTaskByIdUntilFinish(taskId);
        if (taskDetail.get("status").getAsInt() != 3) {
            LOG.error("The DME volume is in abnormal condition!taskDetail={}", gson.toJson(taskDetail));
            throw new Exception(taskDetail.get("detail_cn").getAsString());
        }
    }

    private String createDMEVolumeByServiceLevel(CreateVolumesRequest createVolumesRequest) throws Exception {
        String url = DmeConstants.DME_VOLUME_BASE_URL;
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, gson.toJson(createVolumesRequest));
        if (responseEntity.getStatusCodeValue() / 100 != 2) {
            LOG.error("Failed to create RDM on DME!errorMsg:{}", responseEntity.getBody());
            throw new Exception("Failed to create RDM on DME!");
        }
        JsonObject task = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        String taskId = task.get("task_id").getAsString();

        return taskId;
    }

    private String createDMEVolumeByUnServiceLevel(CustomizeVolumesRequest customizeVolumesRequest) throws Exception {
        String url = DmeConstants.DME_CREATE_VOLUME_UNLEVEL_URL;
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, gson.toJson(customizeVolumesRequest));
        if (responseEntity.getStatusCodeValue() / 100 != 2) {
            LOG.error("Failed to create RDM on DME!errorMsg:{}", responseEntity.getBody());
            throw new Exception("Failed to create RDM on DME!");
        }
        JsonObject task = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        String taskId = task.get("task_id").getAsString();

        return taskId;
    }

    private void hostMapping(String hostId, List<String> volumeIds) throws Exception {
        String url = DmeConstants.DME_HOST_MAPPING_URL;
        JsonObject body = new JsonObject();
        body.addProperty("host_id", hostId);
        JsonArray volume_ids = gson.fromJson(gson.toJson(volumeIds), JsonArray.class);
        body.add("volume_ids", volume_ids);
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, body.toString());
        if (responseEntity.getStatusCodeValue() / 100 != 2) {
            LOG.error("host mapping failed!errorMsg:{}", responseEntity.getBody());
            throw new Exception(responseEntity.getBody());
        }
        JsonObject task = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        String taskId = task.get("task_id").getAsString();
        JsonObject taskDetail = taskService.queryTaskByIdUntilFinish(taskId);
        if (taskDetail.get("status").getAsInt() != 3) {
            LOG.error("host mapping failed!task status={}", task.get("status").getAsInt());
            throw new Exception(task.get("detail_cn").getAsString());
        }
    }

    @Override
    public List<DMEHostInfo> getAllDmeHost() throws Exception {
        return dmeAccessService.getAllDmeHost();
    }
}
