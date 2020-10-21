package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.model.CreateVolumesRequest;
import com.dmeplugin.dmestore.model.CustomizeVolumesRequest;
import com.dmeplugin.dmestore.model.ServiceVolumeMapping;
import com.dmeplugin.dmestore.model.VmRdmCreateBean;
import com.dmeplugin.dmestore.utils.StringUtil;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.*;
import com.vmware.vim25.DatastoreSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


/**
 * @author wangxiangyong
 */
public class VmRdmServiceImpl implements VmRdmService {
    private static final Logger LOG = LoggerFactory.getLogger(VmRdmServiceImpl.class);

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
    public void createRdm(String dataStoreName, String vmObjectId, String hostId, VmRdmCreateBean createBean) throws DMEException {
        createDmeRdm(createBean);
        LOG.info("create DME disk succeeded!");
        String requestVolumeName;
        int size;
        ServiceVolumeMapping mapping;
        if (createBean.getCreateVolumesRequest() != null) {
            requestVolumeName = createBean.getCreateVolumesRequest().getVolumes().get(0).getName();
            size = createBean.getCreateVolumesRequest().getVolumes().get(0).getCapacity();
            mapping = createBean.getCreateVolumesRequest().getMapping();
        } else {
            requestVolumeName = createBean.getCustomizeVolumesRequest().getCustomizeVolumes().getVolumeSpecs().get(0).getName();
            size = createBean.getCustomizeVolumesRequest().getCustomizeVolumes().getVolumeSpecs().get(0).getCapacity();
            mapping = createBean.getCustomizeVolumesRequest().getMapping();
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

        if (null == mapping) {
            //将卷映射给主机
            dmeAccessService.hostMapping(hostId, volumeIds);
        }
        LOG.info("disk mapping to host succeeded!");

        //查询主机信息
        Map<String, Object> hostMap = dmeAccessService.getDmeHost(hostId);
        String hostIp = hostMap.get("ip").toString();
        //调用vCenter扫描卷
        vcsdkUtils.hostRescanVmfs(hostIp);
        LOG.info("scan vmfs succeeded!");

        //扫描hba，已发现新的卷
        vcsdkUtils.hostRescanHba(hostIp);

        //获取LUN信息.有扫描需要一定的时间才能发现得了LUN信息，这里等待两分钟
        String lunStr = "";
        int times = 0;
        //获取LUN信息重试次数
        final int retryTimes = 60;
        while (times++ < retryTimes) {
            lunStr = vcsdkUtils.getLunsOnHost(hostIp);
            if (StringUtil.isNotBlank(lunStr)) {
                break;
            } else {
                try {
                    Thread.sleep(2 * 1000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new DMEException(e.getMessage());
                }
            }
        }

        if (StringUtil.isBlank(lunStr)) {
            LOG.error("获取目标LUN失败！");
            //将已经创建好的卷删除
            deleteVolumes(hostId, volumeIds);
            throw new DMEException("Failed to obtain the target LUN!");
        }
        LOG.info("get LUN information succeeded!");
        JsonArray lunArray = gson.fromJson(lunStr, JsonArray.class);
        Map<String, JsonObject> lunMap = new HashMap<>();
        for (int i = 0; i < lunArray.size(); i++) {
            JsonObject lunObject = lunArray.get(i).getAsJsonObject();
            for (int j = 0; j < volumeArr.size(); j++) {
                JsonObject volume = volumeArr.get(j).getAsJsonObject();
                String wwn = volume.get("volume_wwn").getAsString();
                if (lunObject.get("deviceName").getAsString().endsWith(wwn)) {
                    lunMap.put(volume.get("id").getAsString(), lunObject);
                }
            }
        }

        String errorMsg = "";
        int lunSize = lunMap.size();
        if (lunSize > 0) {
            List<String> failList = new ArrayList();
            for (Map.Entry<String, JsonObject> entry : lunMap.entrySet()) {
                String volumeId = entry.getKey();
                JsonObject object = entry.getValue();
                //调用vCenter创建磁盘
                try {
                    vcsdkUtils.createDisk(dataStoreName, vmObjectId, object.get("devicePath").getAsString(), size);
                } catch (Exception ex) {
                    failList.add(volumeId);
                    errorMsg = ex.getMessage();
                }
            }

            if (failList.size() > 0) {
                deleteVolumes(hostId, failList);
                //完全失败
                if (failList.size() == lunSize) {
                    throw new DMEException(errorMsg);
                }
            }
        } else {
            throw new DMEException("No matching LUN information was found on the vCenter");
        }
    }

    /**
     * DME卷先解除映射后删除
     * @author wangxy
     * @date 11:04 2020/10/14
     * @param hostId 主机ID
     * @param ids    卷ID列表
     * @throws Exception always
     * @return 
     **/
    private void deleteVolumes(String hostId, List<String> ids)  {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("volumeDelete-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        singleThreadPool.execute(() -> {
            try {
                dmeAccessService.unMapHost(hostId, ids);
                dmeAccessService.deleteVolumes(ids);
            } catch (Exception ex) {
                LOG.error("deleteVolumes error!{}", ex.getMessage());
            }
        });
        singleThreadPool.shutdown();
    }

    public void createDmeRdm(VmRdmCreateBean vmRdmCreateBean) throws DMEException {
        String taskId;
        //通过服务等级创建卷
        if (vmRdmCreateBean.getCreateVolumesRequest() != null) {
            taskId = createDmeVolumeByServiceLevel(vmRdmCreateBean.getCreateVolumesRequest());
        } else {
            //用户自定义创建卷
            taskId = createDmeVolumeByUnServiceLevel(vmRdmCreateBean.getCustomizeVolumesRequest());
        }
        JsonObject taskDetail = taskService.queryTaskByIdUntilFinish(taskId);
        if (taskDetail.get(DmeConstants.TASK_DETAIL_STATUS_FILE).getAsInt() != DmeConstants.TASK_SUCCESS) {
            LOG.error("The DME volume is in abnormal condition!taskDetail={}", gson.toJson(taskDetail));
            throw new DMEException(taskDetail.get("detail_cn").getAsString());
        }
    }

    private String createDmeVolumeByServiceLevel(CreateVolumesRequest createVolumesRequest) throws DMEException {
        String url = DmeConstants.DME_VOLUME_BASE_URL;
        Gson gsonTemp = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, gsonTemp.toJson(createVolumesRequest));
        if (responseEntity.getStatusCodeValue() / DmeConstants.HTTPS_STATUS_CHECK_FLAG != DmeConstants.HTTPS_STATUS_SUCCESS_PRE) {
            LOG.error("Failed to create RDM on DME!errorMsg:{}", responseEntity.getBody());
            throw new DMEException("Failed to create RDM on DME!");
        }
        JsonObject task = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        String taskId = task.get("task_id").getAsString();

        return taskId;
    }

    private String createDmeVolumeByUnServiceLevel(CustomizeVolumesRequest customizeVolumesRequest) throws DMEException {
        String url = DmeConstants.DME_CREATE_VOLUME_UNLEVEL_URL;
        String  ownerController = customizeVolumesRequest.getCustomizeVolumes().getOwnerController();
        //归属控制器自动则不下发参数
        final String ownerControllerAuto = "0";
        if( ownerControllerAuto.equals(ownerController)){
            customizeVolumesRequest.getCustomizeVolumes().setOwnerController(null);
        }
        Gson gsonTemp = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, gsonTemp.toJson(customizeVolumesRequest));
        if (responseEntity.getStatusCodeValue() / DmeConstants.HTTPS_STATUS_CHECK_FLAG != DmeConstants.HTTPS_STATUS_SUCCESS_PRE) {
            LOG.error("Failed to create RDM on DME!errorMsg:{}", responseEntity.getBody());
            throw new DMEException("Failed to create RDM on DME!");
        }
        JsonObject task = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        String taskId = task.get("task_id").getAsString();

        return taskId;
    }

    @Override
    public List<Map<String, Object>> getAllDmeHost() throws DMEException {
        return dmeAccessService.getDmeHosts(null);
    }

    @Override
    public List<DatastoreSummary> getDatastoreMountsOnHost(String hostId) throws DMEException {
        //查询主机信息
        Map<String, Object> hostMap = dmeAccessService.getDmeHost(hostId);
        String hostIp = hostMap.get("ip").toString();
        return vcsdkUtils.getDatastoreMountsOnHost(hostId, hostIp);
    }
}
