package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.RelationInstance;
import com.dmeplugin.dmestore.utils.ToolUtils;
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
    private final String QUERY_INSTANCE_URL = "/rest/resourcedb/v1/instances/{className}/{instanceId}";

    private DmeAccessService dmeAccessService;

    public DmeAccessService getDmeAccessService() {
        return dmeAccessService;
    }

    public void setDmeAccessService(DmeAccessService dmeAccessService) {
        this.dmeAccessService = dmeAccessService;
    }

    @Override
    public List<RelationInstance> queryRelationByRelationName(String relationName) throws Exception {
        List<RelationInstance> ris = new ArrayList<>();
        Map<String, Object> remap = new HashMap<>();
        remap.put("code", 200);
        remap.put("message", "queryRelationByRelationName success!");
        //remap.put("data", params);

        String url = LIST_RELATION_URL;
        url = url.replace("{relationName}", relationName);

        ResponseEntity responseEntity;
        try {
            responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == 200) {
                ris = converRelations(responseEntity.getBody());
            }
        } catch (Exception e) {
            throw e;
        }
        return ris;
    }

    @Override
    public List<RelationInstance> queryRelationByRelationNameConditionSourceInstanceId(String relationName, String sourceInstanceId) throws Exception {
        List<RelationInstance> ris = new ArrayList<>();
        Map<String, Object> remap = new HashMap<>();
        remap.put("code", 200);
        remap.put("message", "queryRelationByRelationName success!");
        //remap.put("data", params);

        String url = LIST_RELATION_URL;
        url = url.replace("{relationName}", relationName);
        url = url + "?condition=[{\"simple\":{\"name\":\"source_Instance_Id\",\"value\":\"" + sourceInstanceId + "\"}}]";


        ResponseEntity responseEntity;
        try {
            responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == 200) {
                ris = converRelations(responseEntity.getBody());
            }
        } catch (Exception e) {
            LOG.warn("通过关系类型名称和源实例ID查询对应关系异常,relationName:{},sourceInstancId:{}!", relationName, sourceInstanceId);
            throw e;
        }
        return ris;
    }

    @Override
    public RelationInstance queryRelationByRelationNameInstanceId(String relationName, String instanceId) throws Exception {
        RelationInstance ri = new RelationInstance();
        Map<String, Object> remap = new HashMap<>();
        remap.put("code", 200);
        remap.put("message", "queryRelationByRelationName success!");

        String url = QUERY_RELATION_URL;
        url = url.replace("{relationName}", relationName);
        url = url.replace("{instanceId}", relationName);

        ResponseEntity responseEntity;
        try {
            responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == 200) {
                ri = converRelation(responseEntity.getBody());
            }
        } catch (Exception e) {
            LOG.warn("通过关系类型名称和源实例ID查询对应关系异常,relationName:{},InstancId:{}!", relationName, instanceId);
            throw e;
        }
        return ri;
    }

    @Override
    public Object queryInstanceByInstanceNameId(String instanceName, String instanceId) throws Exception {
        Object obj = new Object();
        Map<String, Object> remap = new HashMap<>();
        remap.put("code", 200);
        remap.put("message", "queryInstanceByInstanceNameId success!");
        //remap.put("data", params);

        String url = QUERY_INSTANCE_URL;
        url = url.replace("{className}", instanceName);
        url = url.replace("{instanceId}", instanceId);
        url = "https://localhost:26335" + url;//上线时删除此行代码

        ResponseEntity responseEntity;
        try {
            responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == 200) {
                obj = responseEntity.getBody();
            }
        } catch (Exception e) {
            LOG.warn("通过资源类型名称和资源实例ID查询对应资源实例异常,className:{},instancId:{}!", instanceName, instanceId);
            throw e;
        }
        return obj;
    }

    private List<RelationInstance> converRelations(Object object) {
        List<RelationInstance> ris = new ArrayList<>();
        JsonObject bodyJson = new JsonParser().parse(object.toString()).getAsJsonObject();
        JsonArray objList = bodyJson.get("objList").getAsJsonArray();
        for (JsonElement element : objList) {
            RelationInstance ri = new RelationInstance();
            JsonObject objJson = element.getAsJsonObject();
            ri.setSourceInstanceId(ToolUtils.getStr(objJson.get("source_Instance_Id")));
            ri.setTargetInstanceId(ToolUtils.getStr(objJson.get("target_Instance_Id")));
            ri.setRelationName(ToolUtils.getStr(objJson.get("relation_Name")));
            ri.setId(ToolUtils.getStr(objJson.get("id")));
            ri.setLastModified(ToolUtils.getLong(objJson.get("last_Modified")));
            ri.setResId(ToolUtils.getStr(objJson.get("resId")));
            ri.setRelationId(ToolUtils.getInt(objJson.get("relation_Id")));

            ris.add(ri);
        }
        return ris;
    }

    private RelationInstance converRelation(Object object) {
        RelationInstance ri = new RelationInstance();
        JsonObject objJson = new JsonParser().parse(object.toString()).getAsJsonObject();
        if(null != objJson){
            ri.setSourceInstanceId(ToolUtils.getStr(objJson.get("source_Instance_Id")));
            ri.setTargetInstanceId(ToolUtils.getStr(objJson.get("target_Instance_Id")));
            ri.setRelationName(ToolUtils.getStr(objJson.get("relation_Name")));
            ri.setId(ToolUtils.getStr(objJson.get("id")));
            ri.setLastModified(ToolUtils.getLong(objJson.get("last_Modified")));
            ri.setResId(ToolUtils.getStr(objJson.get("resId")));
            ri.setRelationId(ToolUtils.getInt(objJson.get("relation_Id")));
        }
        return ri;
    }

}
