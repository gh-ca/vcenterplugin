package com.dmeplugin.dmestore.services;


import com.dmeplugin.dmestore.dao.DmeInfoDao;
import com.dmeplugin.dmestore.entity.DmeInfo;
import com.dmeplugin.dmestore.model.VmfsDataInfo;
import com.dmeplugin.dmestore.utils.RestUtils;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
import java.util.List;
import java.util.Map;

public class VmfsAccessServiceImpl implements VmfsAccessService {

    private static final Logger LOG = LoggerFactory.getLogger(VmfsAccessServiceImpl.class);

    @Autowired
    private Gson gson = new Gson();
    @Autowired
    private DmeAccessService dmeAccessService;

    private final String LIST_VOLUME_URL = "/rest/blockservice/v1/volumes";

    @Override
    public List<VmfsDataInfo> listVmfs() throws Exception {
        try{
            //取得vcenter中的所有vmfs存储。
            String listStr = VCSDKUtils.getAllVmfsDataStores();
            if(!StringUtils.isEmpty(listStr)) {
                JsonArray jsonArray = new JsonParser().parse(listStr).getAsJsonArray();
                if(jsonArray!=null && jsonArray.size()>0) {
                    for(int i=0;i<jsonArray.size();i++) {
                        JsonObject jo = jsonArray.get(i).getAsJsonObject();
                        LOG.info("jo=="+jo.toString());
                        //然后通过vmfs中的url值去DME系统中查询对应的卷信息。
                        //这里由于DME系统中的卷太多。是分页查询，所以需要vmfs一个个的去查DME系统中的卷。
                        //而每次查询DME中的卷都需要调用两次，分别是查卷列表接口，查卷详细接口。
//                        ResponseEntity responseEntity = dmeAccessService.access(LIST_VOLUME_URL, HttpMethod.GET, null);
//                        LOG.info("listVmfs responseEntity==" + responseEntity.toString());
//                        if (responseEntity.getStatusCodeValue() == 200) {
//                            JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
//                            LOG.info("listVmfs jsonObject==" + jsonObject.toString());
//
//                        }
                    }
                }
            }
            //组装数据。
        }catch (Exception e){
            LOG.error("list vmfs error:"+e);
            throw e;
        }
        return null;
    }

    @Override
    public void createVmfs(Map<String, Object> params) throws Exception {

    }

    @Override
    public void mountVmfs(Map<String, Object> params) throws Exception {

    }
}
