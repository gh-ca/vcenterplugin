package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DMEException;
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
    private final String LIST_INSTANCE_URL = "/rest/resourcedb/v1/instances/{className}?pageSize=1000";

    private static Map<String, Map<String, Object>> serviceLevelInstance = new HashMap<>();
    private static Map<String, Map<String, Object>> lunInstance = new HashMap<>();
    private static Map<String, Map<String, Object>> storagePoolInstance = new HashMap<>();
    private static Map<String, Map<String, Object>> storageDevcieInstance = new HashMap<>();

    private DmeAccessService dmeAccessService;

    public DmeAccessService getDmeAccessService() {
        return dmeAccessService;
    }

    public void setDmeAccessService(DmeAccessService dmeAccessService) {
        this.dmeAccessService = dmeAccessService;
    }

    @Override
    public List<RelationInstance> queryRelationByRelationName(String relationName) throws DMEException {
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
            throw new DMEException(e.getMessage());
        }
        return ris;
    }

    @Override
    public List<RelationInstance> queryRelationByRelationNameConditionSourceInstanceId(String relationName, String sourceInstanceId) throws DMEException {
        List<RelationInstance> ris = new ArrayList<>();
        Map<String, Object> remap = new HashMap<>();
        remap.put("code", 200);
        remap.put("message", "queryRelationByRelationName success!");
        //remap.put("data", params);

        String url = LIST_RELATION_URL;
        url = url.replace("{relationName}", relationName);
        url = url + "?condition={json}";

        JsonArray constraint = new JsonArray();
        JsonObject consObj2 = new JsonObject();
        JsonObject simple2 = new JsonObject();
        simple2.addProperty("name", "source_Instance_Id");
        simple2.addProperty("value", sourceInstanceId);
        consObj2.add("simple", simple2);
        constraint.add(consObj2);

        ResponseEntity responseEntity;
        try {
            responseEntity = dmeAccessService.accessByJson(url, HttpMethod.GET, constraint.toString());
            if (responseEntity.getStatusCodeValue() == 200) {
                ris = converRelations(responseEntity.getBody());
            }
        } catch (Exception e) {
            LOG.warn("通过关系类型名称和源实例ID查询对应关系异常,url:{},condition:{},relationName:{},sourceInstancId:{}!", url, constraint.toString(), relationName, sourceInstanceId);
            throw new DMEException(e.getMessage());
        }
        return ris;
    }

    @Override
    public RelationInstance queryRelationByRelationNameInstanceId(String relationName, String instanceId) throws DMEException {
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
            LOG.warn("通过关系类型名称和源实例ID查询对应关系异常,url:{},relationName:{},InstancId:{}!", url, relationName, instanceId);
            throw new DMEException(e.getMessage());
        }
        return ri;
    }

    @Override
    public Object queryInstanceByInstanceNameId(String instanceName, String instanceId) throws DMEException {
        Object obj = new Object();
        Map<String, Object> remap = new HashMap<>();
        remap.put("code", 200);
        remap.put("message", "queryInstanceByInstanceNameId success!");
        //remap.put("data", params);

        String url = QUERY_INSTANCE_URL;
        url = url.replace("{className}", instanceName);
        url = url.replace("{instanceId}", instanceId);

        ResponseEntity responseEntity;
        try {
            responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == 200) {
                obj = responseEntity.getBody();
            }
        } catch (Exception e) {
            LOG.warn("通过资源类型名称和资源实例ID查询对应资源实例异常,className:{},instancId:{}!", instanceName, instanceId);
            throw new DMEException(e.getMessage());
        }
        return obj;
    }

    public void listInstanceServiceLevel() {
        String instanceName = "SYS_DjTier";
        JsonObject jsonObject = listInstancdByInstanceName(instanceName);
        if (null != jsonObject) {
            JsonArray jsonArray = jsonObject.get("objList").getAsJsonArray();
            Map<String, Map<String, Object>> map = new HashMap<>();
            for (JsonElement element : jsonArray) {
                Map<String, Object> slMap = new HashMap<>();
                JsonObject slJson = element.getAsJsonObject();
                slMap.put("resId", ToolUtils.jsonToOriginalStr(slJson.get("resId")));
                slMap.put("name", ToolUtils.jsonToOriginalStr(slJson.get("name")));
                slMap.put("id", ToolUtils.jsonToOriginalStr(slJson.get("id")));
                slMap.put("nativeId", ToolUtils.jsonToOriginalStr(slJson.get("nativeId")));
                map.put(ToolUtils.jsonToOriginalStr(slJson.get("nativeId")), slMap);
            }
            if (map.size() > 0) {
                serviceLevelInstance.clear();
                serviceLevelInstance.putAll(map);
            }
        }
    }

    public void listInstanceLun() {
        String instanceName = "SYS_Lun";
        JsonObject jsonObject = listInstancdByInstanceName(instanceName);
        if (null != jsonObject) {
            JsonArray jsonArray = jsonObject.get("objList").getAsJsonArray();
            Map<String, Map<String, Object>> map = new HashMap<>();
            for (JsonElement element : jsonArray) {
                Map<String, Object> lunMap = new HashMap<>();
                JsonObject slJson = element.getAsJsonObject();
                lunMap.put("resId", ToolUtils.jsonToOriginalStr(slJson.get("resId")));
                lunMap.put("name", ToolUtils.jsonToOriginalStr(slJson.get("name")));
                lunMap.put("id", ToolUtils.jsonToOriginalStr(slJson.get("id")));
                lunMap.put("wwn", ToolUtils.jsonToOriginalStr(slJson.get("wwn")));
                map.put(ToolUtils.jsonToOriginalStr(slJson.get("wwn")), lunMap);
            }
            if (map.size() > 0) {
                lunInstance.clear();
                lunInstance.putAll(map);
            }
        }
    }

    public void listInstanceStoragePool() {
        String instanceName = "SYS_StoragePool";
        JsonObject jsonObject = listInstancdByInstanceName(instanceName);
        if (null != jsonObject) {
            JsonArray jsonArray = jsonObject.get("objList").getAsJsonArray();
            Map<String, Map<String, Object>> map = new HashMap<>();
            for (JsonElement element : jsonArray) {
                Map<String, Object> lunMap = new HashMap<>();
                JsonObject slJson = element.getAsJsonObject();
                lunMap.put("resId", ToolUtils.jsonToOriginalStr(slJson.get("resId")));
                lunMap.put("name", ToolUtils.jsonToOriginalStr(slJson.get("name")));
                lunMap.put("id", ToolUtils.jsonToOriginalStr(slJson.get("id")));
                map.put(ToolUtils.jsonToOriginalStr(slJson.get("id")), lunMap);
            }
            if (map.size() > 0) {
                storagePoolInstance.clear();
                storagePoolInstance.putAll(map);
            }
        }
    }

    public void listInstanceStorageDevcie() {
        String instanceName = "SYS_StorDevice";
        JsonObject jsonObject = listInstancdByInstanceName(instanceName);
        if (null != jsonObject) {
            JsonArray jsonArray = jsonObject.get("objList").getAsJsonArray();
            Map<String, Map<String, Object>> map = new HashMap<>();
            for (JsonElement element : jsonArray) {
                Map<String, Object> lunMap = new HashMap<>();
                JsonObject slJson = element.getAsJsonObject();
                lunMap.put("resId", ToolUtils.jsonToOriginalStr(slJson.get("resId")));
                lunMap.put("name", ToolUtils.jsonToOriginalStr(slJson.get("name")));
                lunMap.put("id", ToolUtils.jsonToOriginalStr(slJson.get("id")));
                lunMap.put("nativeId", ToolUtils.jsonToOriginalStr(slJson.get("nativeId")));
                lunMap.put("sn", ToolUtils.jsonToOriginalStr(slJson.get("sn")));
                map.put(ToolUtils.jsonToOriginalStr(slJson.get("nativeId")), lunMap);
            }
            if (map.size() > 0) {
                storageDevcieInstance.clear();
                storageDevcieInstance.putAll(map);
            }
        }
    }

    @Override
    public Map<String, Map<String, Object>> getServiceLevelInstance() {
        if (serviceLevelInstance.size() == 0) {
            listInstanceServiceLevel();
        }
        return serviceLevelInstance;
    }

    @Override
    public Map<String, Map<String, Object>> getLunInstance() {
        if (lunInstance.size() == 0) {
            listInstanceLun();
        }
        return lunInstance;
    }

    @Override
    public Map<String, Map<String, Object>> getStoragePoolInstance() {
        if (storagePoolInstance.size() == 0) {
            listInstanceStoragePool();
        }
        return storagePoolInstance;
    }

    @Override
    public Map<String, Map<String, Object>> getStorageDeviceInstance() {
        if (storageDevcieInstance.size() == 0) {
            listInstanceStorageDevcie();
        }
        return storageDevcieInstance;
    }

    @Override
    public void refreshResourceInstance() {
        listInstanceStorageDevcie();
        listInstanceStoragePool();
        listInstanceLun();
        listInstanceServiceLevel();
    }

    private JsonObject listInstancdByInstanceName(String instanceName) {
        JsonObject jsonObject = null;
        String url = LIST_INSTANCE_URL.replace("{className}", instanceName);
        try {
            ResponseEntity responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (null != responseEntity && 200 == responseEntity.getStatusCodeValue()) {
                Object object = responseEntity.getBody();
                jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
            }
        } catch (Exception e) {
            LOG.warn("List instance error by instanceName:" + instanceName, e);
        }
        return jsonObject;
    }

    private List<RelationInstance> converRelations(Object object) {
        List<RelationInstance> ris = new ArrayList<>();
        JsonObject bodyJson = new JsonParser().parse(object.toString()).getAsJsonObject();
        JsonArray objList = bodyJson.get("objList").getAsJsonArray();
        for (JsonElement element : objList) {
            RelationInstance ri = new RelationInstance();
            JsonObject objJson = element.getAsJsonObject();
            ri.setSourceInstanceId(ToolUtils.jsonToStr(objJson.get("source_Instance_Id")));
            ri.setTargetInstanceId(ToolUtils.jsonToStr(objJson.get("target_Instance_Id")));
            ri.setRelationName(ToolUtils.jsonToStr(objJson.get("relation_Name")));
            ri.setId(ToolUtils.jsonToStr(objJson.get("id")));
            ri.setLastModified(ToolUtils.jsonToLon(objJson.get("last_Modified"), null));
            ri.setResId(ToolUtils.jsonToStr(objJson.get("resId")));
            ri.setRelationId(ToolUtils.jsonToInt(objJson.get("relation_Id"), 0));

            ris.add(ri);
        }
        return ris;
    }

    private RelationInstance converRelation(Object object) {
        RelationInstance ri = new RelationInstance();
        JsonObject objJson = new JsonParser().parse(object.toString()).getAsJsonObject();
        if (null != objJson) {
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
