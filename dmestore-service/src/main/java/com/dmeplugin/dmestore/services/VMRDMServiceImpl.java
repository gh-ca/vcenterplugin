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

import java.util.List;


/**
 * @author wangxiangyong
 * @version 1.0.0
 * @ClassName VMRDMService.java
 * @Description TODO
 * @createTime 2020年09月09日 10:58:00
 */
public class VMRDMServiceImpl implements VMRDMService {
    private static final Logger LOG = LoggerFactory.getLogger(VMRDMServiceImpl.class);

    @Autowired
    private DmeAccessService dmeAccessService;

    @Autowired
    private Gson gson = new Gson();

    private VCSDKUtils vcsdkUtils;

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
    }

    @Override
    public String createRDM(VmRDMCreateBean vmRDMCreateBean) {
        String url = DmeConstants.DME_VOLUME_BASE_URL;
        JsonObject body = new JsonObject();
        body.addProperty("service_level_id", vmRDMCreateBean.getServiceLevelId());
        JsonArray volumes = new JsonArray();
        try {
            JsonObject base = new JsonObject();
            base.addProperty("name", vmRDMCreateBean.getName());
            base.addProperty("capacity", vmRDMCreateBean.getSize());
            base.addProperty("count", vmRDMCreateBean.getSize());
            volumes.add(base);
            body.add("volumes", volumes);
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, body.toString());
            if (responseEntity.getStatusCodeValue() / 100 != 2) {
                LOG.error("create dme rdm failed!errorMsg:{}", responseEntity.getBody());
                return null;
            }
            JsonObject task = gson.fromJson(responseEntity.getBody(), JsonObject.class);
            return task.get("task_id").getAsString();
        } catch (Exception ex) {
            LOG.error("create dme rdm error!errorMsg:{}", ex.getMessage());
        }
        return null;
    }

    @Override
    public String hostMapping(String hostId, List<String> volumeIds) {
        String url = DmeConstants.DME_HOST_MAPPING_URL;
        JsonObject body = new JsonObject();
        body.addProperty("host_id", hostId);
        JsonArray volume_ids = gson.fromJson(gson.toJson(volumeIds), JsonArray.class);
        body.add("volume_ids", volume_ids);
        try {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, body.toString());
            if (responseEntity.getStatusCodeValue() / 100 != 2) {
                LOG.error("host mapping failed!errorMsg:{}", responseEntity.getBody());
                return null;
            }
            JsonObject task = gson.fromJson(responseEntity.getBody(), JsonObject.class);
            String taskId = task.get("task_id").getAsString();
            return taskId;
        } catch (Exception ex) {
            LOG.error("host mapping error!errorMsg:{}", ex.getMessage());
        }
        return null;
    }

    @Override
    public void hostRescanVmfs(String hostIp) throws Exception {
        vcsdkUtils.hostRescanVmfs(hostIp);
    }

    @Override
    public String getLunsOnHost(String hostName) throws Exception {
        return vcsdkUtils.getLunsOnHost(hostName);
    }

}
