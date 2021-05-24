package com.huawei.dmestore.services;

import com.google.gson.*;
import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.VcenterException;
import com.huawei.dmestore.utils.JsonUtil;
import com.huawei.dmestore.utils.VCSDKUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DmePerformanceServiceImpl implements DmePerformanceService {
    private static final Logger log = LoggerFactory.getLogger(DmePerformanceServiceImpl.class);

    private static final String INDICATOR_IDS_FIELD = "indicator_ids";
    private static final String OBJ_IDS_FIELD = "obj_ids";

    private VCSDKUtils vcsdkUtils;
    private Gson gson = new Gson();

    @Override
    public String getPerformanceDataStore(String storageId) throws DmeException {
        return vcsdkUtils.queryPerf(storageId);
    }

    @Override
    public Map<String, Object> getPerformance(Map<String, Object> params) throws DmeException {
        return null;
    }

    @Override
    public Map<String, Object> getPerformanceCurrent(Map<String, Object> params) throws DmeException {
        Map<String, Object> result = new HashMap<>();
        //Map<String, String> idInstancdIdMap = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        List<String> instanceIds = new ArrayList<>();
        Object indicatorIds = params.get(INDICATOR_IDS_FIELD);
        List<String> ids = (List<String>) params.get(OBJ_IDS_FIELD);
        if( null == ids || 0 == ids.size()){
            String msg = "getPerformanceCurrent no objectids";
            throw new DmeException(msg);
        }
        for(String id : ids){
            try {
                String perResult = vcsdkUtils.queryPerfAllCount(id);
                Map<String, String> countValueMap = parsePerf(perResult);
                result.put(id, countValueMap);
            } catch (VcenterException e) {
                String msg = "the objectid " + id + "getPerformacne error!";
                log.warn(msg);
            }
        }
        return result;
    }

    public Map<String, String> parsePerf(String perfResult){
        Map<String, String> countValueMap = new HashMap<>();
        try {
            JsonArray array = gson.fromJson(perfResult, JsonArray.class);
            JsonElement element = array.get(0);
            JsonObject jsonObject = element.getAsJsonObject();
            JsonArray valueArray = jsonObject.get("value").getAsJsonArray();
            for(JsonElement valueElement : valueArray){
                int counterId = valueElement.getAsJsonObject().get("id").getAsJsonObject().get("counterId").getAsInt();
                String value = valueElement.getAsJsonObject().get("value").getAsString();
                value = stringCut(value, ",", 1);
                countValueMap.put(String.valueOf(counterId), value);
            }
        } catch (JsonSyntaxException e) {
            log.error("parsePerf error!");
        }
        return countValueMap;
    }

    public String stringCut(String value, String separator, int length){
        String result = "";
        if(0 >= length){
            length = 1;
        }
        String[] strs = value.split(separator);
        int length_t = strs.length;
        if(length_t <= length){
            result =  value;
        }else{
            StringBuilder sb = new StringBuilder();
            for(int i= 0; i < length ; i++){
                sb.append(separator).append(strs[i]);
            }
            result = sb.substring(separator.length()).toString();
        }
        return result;
    }

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
    }
}