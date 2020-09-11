package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.CustomizeVolumeTuning;
import com.dmeplugin.dmestore.model.QosPolicy;
import com.dmeplugin.dmestore.model.VolumeUpdate;
import com.dmeplugin.dmestore.utils.RestUtils;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
