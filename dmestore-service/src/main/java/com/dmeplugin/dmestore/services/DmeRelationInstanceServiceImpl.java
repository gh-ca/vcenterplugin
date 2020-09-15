package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.RelationInstance;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @ClassName: DmeRelationInstanceServiceImpl
 * @Company: GH-CA
 * @author: liuxh
 * @create: 2020-09-15
 **/
public class DmeRelationInstanceServiceImpl implements DmeRelationInstanceService {
    private static final Logger LOG = LoggerFactory.getLogger(DmeRelationInstanceServiceImpl.class);

    Gson gson = new Gson();

    private final String LIST_RELATION_URL = "/rest/resourcedb/v1/relations/{relationName}/instances";
    private final String QUERY_RELATION_URL = "/rest/resourcedb/v1/relations/{relationName}/instances/{instanceId}";

    private DmeAccessService dmeAccessService;


    @Override
    public List<RelationInstance> queryRelationByRelationName(String relationName) throws Exception {
        List<RelationInstance> ris = new ArrayList<>();
        Map<String, Object> remap = new HashMap<>();
        remap.put("code", 200);
        remap.put("message", "queryRelationByRelationName success!");
        //remap.put("data", params);

        String url = LIST_RELATION_URL;
        url = url.replace("{relationName}", relationName);
        url = "https://localhost:26335" + url;//上线时删除此行代码

        ResponseEntity responseEntity;
        try {
            responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == 200) {
                Object body = responseEntity.getBody();
                JsonObject bodyJson = new JsonParser().parse(body.toString()).getAsJsonObject();
                JsonArray objList = bodyJson.get("objList").getAsJsonArray();
                for(JsonElement element : objList){
                    RelationInstance ri = new RelationInstance();
                    JsonObject objJson = element.getAsJsonObject();
                    String sourceInstanceId = objJson.get("source_Instance_Id").getAsString();
                    String targetInstanceId = objJson.get("target_Instance_Id").getAsString();
                    //String relationName = objJson.get("relation_Name").getAsString();
                    String id = objJson.get("id").getAsString();
                    long lastModified = objJson.get("last_Modified").getAsLong();
                    String resId = objJson.get("resId").getAsString();
                    int relationId = objJson.get("relation_Id").getAsInt();

                    ri.setSourceInstanceId(sourceInstanceId);
                    ri.setTargetInstanceId(targetInstanceId);
                    ri.setRelationName(relationName);
                    ri.setId(id);
                    ri.setLastModified(lastModified);
                    ri.setResId(resId);
                    ri.setRelationId(relationId);

                    ris.add(ri);
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return ris;
    }

    @Override
    public RelationInstance queryRelationByRelationNameInstanceId(String relationName, String instanceId) throws Exception{
        return null;
    }
}
