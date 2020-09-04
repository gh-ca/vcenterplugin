package com.dmeplugin.dmestore.services;


import com.dmeplugin.dmestore.model.VmfsDataInfo;
import com.dmeplugin.dmestore.model.VmfsDatastoreVolumeDetail;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VmfsAccessServiceImpl implements VmfsAccessService {

    private static final Logger LOG = LoggerFactory.getLogger(VmfsAccessServiceImpl.class);

    @Autowired
    private Gson gson = new Gson();
    @Autowired
    private DmeAccessService dmeAccessService;

    public static final String LIST_VOLUME_URL = "/rest/blockservice/v1/volumes";

    @Override
    public List<VmfsDataInfo> listVmfs() throws Exception {
        List<VmfsDataInfo> relists = null;
        try {
            //取得vcenter中的所有vmfs存储。
            String listStr = VCSDKUtils.getAllVmfsDataStores();
            if (!StringUtils.isEmpty(listStr)) {
                JsonArray jsonArray = new JsonParser().parse(listStr).getAsJsonArray();
                if (jsonArray != null && jsonArray.size() > 0) {
                    relists = new ArrayList<>();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jo = jsonArray.get(i).getAsJsonObject();
                        LOG.info("jo==" + jo.toString());

                        VmfsDataInfo vmfsDataInfo = new VmfsDataInfo();
                        double capacity = getDouble(jo.get("capacity").getAsString()) / 1024 / 1024 / 1024;
                        double freeSpace = getDouble(jo.get("freeSpace").getAsString()) / 1024 / 1024 / 1024;
                        double uncommitted = getDouble(jo.get("uncommitted").getAsString()) / 1024 / 1024 / 1024;

                        vmfsDataInfo.setName(jo.get("name").getAsString());

                        vmfsDataInfo.setCapacity(capacity);
                        vmfsDataInfo.setFreeSpace(freeSpace);
                        vmfsDataInfo.setReserveCapacity(capacity + uncommitted - freeSpace);

                        String wwn = jo.get("url").getAsString();
                        LOG.info("wwn==" + wwn);
                        //然后通过vmfs中的url值去DME系统中查询对应wwn的卷信息。
                        ///rest/blockservice/v1/volumes?volume_wwn=wwn
                        //这里由于DME系统中的卷太多。是分页查询，所以需要vmfs一个个的去查DME系统中的卷。
                        //而每次查询DME中的卷都需要调用两次，分别是查卷列表接口，查卷详细接口。
                        ResponseEntity responseEntity = dmeAccessService.access(LIST_VOLUME_URL + "?volume_wwn=" + wwn, HttpMethod.GET, null);
                        LOG.info("listVmfs responseEntity==" + responseEntity.toString());
                        if (responseEntity.getStatusCodeValue() == 200) {
                            JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                            LOG.info("listVmfs jsonObject==" + jsonObject.toString());
                            JsonObject vjson = jsonObject.getAsJsonArray("volumes").get(0).getAsJsonObject();

                            vmfsDataInfo.setStatus(vjson.get("status").getAsString());
                            vmfsDataInfo.setServiceLevelName(vjson.get("service_level_name").getAsString());
                            vmfsDataInfo.setVmfsProtected(vjson.get("protected").getAsBoolean());

                            String volid = vjson.get("id").getAsString();
                            //通过卷ID再调卷详细接口
                            responseEntity = dmeAccessService.access(LIST_VOLUME_URL + "/" + volid, HttpMethod.GET, null);
                            LOG.info("volid responseEntity==" + responseEntity.toString());
                            if (responseEntity.getStatusCodeValue() == 200) {
                                JsonObject voljson = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                                LOG.info("volid voljson==" + voljson.toString());
                                JsonObject vjson2 = voljson.getAsJsonObject("volume");


                                //通过卷ID再调卷详细接口


                            }


                        }
                        relists.add(vmfsDataInfo);

                    }
                }
            }
            //组装数据。
        } catch (Exception e) {
            LOG.error("list vmfs error:" + e);
            throw e;
        }
        LOG.info("relists===" + (relists == null ? "null" : (relists.size() + "==" + gson.toJson(relists))));
        return relists;
    }

    @Override
    public void createVmfs(Map<String, Object> params) throws Exception {

    }

    @Override
    public void mountVmfs(Map<String, Object> params) throws Exception {

    }

    /**
     * @Author wangxiangyong
     * @Description /vmfs datastore 卷详情查询
     * @Date 14:46 2020/9/3
     * @Param [volume_id]
     * @Return com.dmeplugin.dmestore.model.VmfsDatastoreVolumeDetail
     **/
    @Override
    public VmfsDatastoreVolumeDetail volumeDetail(String volume_id) throws Exception {
        //调用DME接口获取卷详情
        String url = LIST_VOLUME_URL + "/" + volume_id;
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if(responseEntity.getStatusCodeValue()/100 != 2){
                LOG.error("查询卷信息失败！错误信息:{}", responseEntity.getBody());
                return null;
            }
        }catch (Exception ex){
            LOG.error("查询卷信息异常", ex);
            return null;
        }

        String responseBody = responseEntity.getBody();
        JsonObject volume = gson.fromJson(responseBody, JsonObject.class).getAsJsonObject("volume");

        VmfsDatastoreVolumeDetail volumeDetail = new VmfsDatastoreVolumeDetail();
        //basic info
        volumeDetail.setWwn(volume.get("volume_wwn").getAsString());
        volumeDetail.setName(volume.get("name").getAsString());
        volumeDetail.setServiceLevel(volume.get("service_level_name").getAsString());
        //TODO
        volumeDetail.setStorage(volume.get("storage_id").getAsString());
        volumeDetail.setStoragePool(volume.get("pool_raw_id").getAsString());

        JsonObject tuning = volume.getAsJsonObject("tuning");
        //SmartTier
        volumeDetail.setSmartTier(tuning.get("smarttier").getAsString());
        //Tunning
        volumeDetail.setDudeplication(tuning.get("dedupe_enabled").getAsBoolean());
        volumeDetail.setProvisionType(tuning.get("alloctype").getAsString());
        volumeDetail.setCompression(tuning.get("compression_enabled").getAsBoolean());
        //TODO
        volumeDetail.setApplicationType("--");

        JsonObject smartqos = tuning.getAsJsonObject("smartqos");
        //Qos Policy
        if(null != smartqos){
            volumeDetail.setControlPolicy(smartqos.get("control_policy").getAsString());
            //TODO
            volumeDetail.setTrafficControl("--");
        }
        return volumeDetail;
    }

    private double getDouble(String obj) {
        double re = 0;
        try {
            if (!StringUtils.isEmpty(obj)) {
                re = Double.parseDouble(obj);
            }
        } catch (Exception e) {
            LOG.error("error:", e);
        }
        return re;
    }
}
