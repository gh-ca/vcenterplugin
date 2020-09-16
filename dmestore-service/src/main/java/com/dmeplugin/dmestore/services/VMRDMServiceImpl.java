package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.VmRDMCreateBean;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

    //@Autowired
    private DmeAccessService dmeAccessService;

    //@Autowired
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
    public void createRDM(String datacenter_name, String datastore_name, String vm_name, String host_id, VmRDMCreateBean createBean) throws Exception {
        List<String> volumeIds = createDmeRDM(createBean);
        LOG.info("create DME disk succeeded!");
        //将卷映射给主机
        hostMapping(host_id, volumeIds);
        LOG.info("disk mapping to host succeeded!");
        //查询主机信息
        Map<String, Object> hostMap = dmeAccessService.getDmeHost(host_id);
        String host_ip = hostMap.get("ip").toString();
        String host_name = hostMap.get("name").toString();
        //调用vcenter扫描卷
        vcsdkUtils.hostRescanVmfs(host_ip);
        LOG.info("scan vmfs succeeded!");
        //获取卷信息
        String lunStr = vcsdkUtils.getLunsOnHost(host_name);
        JsonArray lunArray = gson.fromJson(lunStr, JsonArray.class);
        JsonObject lunObject = null;
        boolean flag = false;
        for (int i = 0; i < lunArray.size(); i++) {
            lunObject = lunArray.get(i).getAsJsonObject();
            LOG.info("Lun deviceName={}, requested name={}", lunObject.get("deviceName").getAsString(), createBean.getName());
            //TODO 根据实际情况优化
            if (lunObject.get("deviceName").getAsString().equals(createBean.getName())) {
                flag = true;
                break;
            }
        }

        if (!flag) {
            LOG.error("no matching Lun information was found!");
            throw new Exception("no matching Lun information was found!");
        }
        //调用Vcenter创建磁盘
        vcsdkUtils.createDisk(datacenter_name, datastore_name, vm_name, lunObject.get("deviceName").getAsString(), createBean.getSize());
    }

    public List<String> createDmeRDM(VmRDMCreateBean vmRDMCreateBean) throws Exception {
        String url = DmeConstants.DME_VOLUME_BASE_URL;
        JsonObject body = new JsonObject();
        body.addProperty("service_level_id", vmRDMCreateBean.getServiceLevelId());
        JsonArray volumes = new JsonArray();
        JsonObject base = new JsonObject();
        base.addProperty("name", vmRDMCreateBean.getName());
        base.addProperty("capacity", vmRDMCreateBean.getSize());
        base.addProperty("count", vmRDMCreateBean.getSize());
        volumes.add(base);
        body.add("volumes", volumes);
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, body.toString());
        if (responseEntity.getStatusCodeValue() / 100 != 2) {
            LOG.error("Failed to create RDM on DME!errorMsg:{}", responseEntity.getBody());
            throw new Exception("Failed to create RDM on DME!");
        }
        JsonObject task = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        String taskId = task.get("task_id").getAsString();
        JsonObject taskDetail = taskService.queryTaskByIdUntilFinish(taskId);
        if (null != taskDetail && taskDetail.get("status").getAsInt() != 3) {
            LOG.error("The DME volume is in abnormal condition!taskDetail={}", gson.toJson(taskDetail));
            throw new Exception("The DME volume is in abnormal condition!");
        }
        List<String> volumeIds = new ArrayList();
        //获取卷资源
        JsonArray resources = taskDetail.getAsJsonArray("resources");
        for (int i = 0; i < resources.size(); i++) {
            JsonObject resource = resources.get(i).getAsJsonObject();
            String id = resource.get("id").getAsString();
            volumeIds.add(id);
        }
        return volumeIds;
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
            throw new Exception("");
        }
        JsonObject task = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        String taskId = task.get("task_id").getAsString();
        JsonObject taskDetail = taskService.queryTaskByIdUntilFinish(taskId);
        if (null != taskDetail && taskDetail.get("status").getAsInt() == 3) {
            LOG.info("Disk mapping to host succeeded!");
        } else {
            LOG.error("Disk mapping to host failed!taskDetail={}", gson.toJson(taskDetail));
        }
    }

}
