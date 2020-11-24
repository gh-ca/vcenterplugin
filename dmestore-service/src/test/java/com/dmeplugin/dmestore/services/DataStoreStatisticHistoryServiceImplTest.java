package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DMEException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * @author lianq
 * @className DataStoreStatisticHistoryServiceImplTest
 * @description TODO
 * @date 2020/11/18 10:43
 */
public class DataStoreStatisticHistoryServiceImplTest {

    @Mock
    private DmeAccessService dmeAccessService;
    @Mock
    private DmeRelationInstanceService dmeRelationInstanceService;
    private Gson gson = new Gson();
    private List<String> list = new ArrayList<>();
    private List<String> list2 = new ArrayList<>();
    private List<String> list3 = new ArrayList<>();
    private Map<String, Object> map = new HashMap<>();
    private Map<String, Object> map1 = new HashMap<>();
    Map<String, Map<String, Object>> map2 = new HashMap<>();
    Map<String, Object> map3 = new HashMap<>();
    private JsonObject jsonObject;
    private String param2;

    @InjectMocks
    DataStoreStatisticHistoryService dataStoreStatisticHistoryService = new DataStoreStatisticHistoryServiceImpl();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        list.add("321");
        list2.add("321");
        list3.add("321");
        map.put("obj_ids", list);
        map.put("indicator_ids", list2);
        map.put("range", list3);
        map.put("interval", "321");
        map.put("begin_time", "321");
        map.put("end_time", "321");
        map1.put("resId", "321");
        map2.put("321", map1);
        param2 = "{\n" +
                "    \"47FEBD5002AB344D90EC6CFCD6127BA3\": {\n" +
                "        \"1407379178651656\": {\n" +
                "            \"series\": [\n" +
                "                {\n" +
                "                    \"1552480500000\": \"80.0\"\n" +
                "                }, \n" +
                "                {\n" +
                "                    \"1552480800000\": \"80.0\"\n" +
                "                }\n" +
                "            ], \n" +
                "            \"min\": {\n" +
                "                \"1552480740000\": \"80.0\"\n" +
                "            }, \n" +
                "            \"max\": {\n" +
                "                \"1552480740000\": \"80.0\"\n" +
                "            }, \n" +
                "            \"avg\": {\n" +
                "                \"1552480943834\": \"80.0\"\n" +
                "            }\n" +
                "        }, \n" +
                "        \"1407379178586113\": {\n" +
                "            \"series\": [\n" +
                "                {\n" +
                "                    \"1552480500000\": \"49.0\"\n" +
                "                }, \n" +
                "                {\n" +
                "                    \"1552480800000\": \"48.42857142857143\"\n" +
                "                }\n" +
                "            ], \n" +
                "            \"min\": {\n" +
                "                \"1552480890000\": \"48.0\"\n" +
                "            }, \n" +
                "            \"max\": {\n" +
                "                \"1552480740000\": \"49.0\"\n" +
                "            }, \n" +
                "            \"avg\": {\n" +
                "                \"1552480943834\": \"48.55555555555556\"\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
        jsonObject = gson.fromJson(param2, JsonObject.class);
        map3.put("data", jsonObject);
    }

    @Test
    public void queryVmfsStatistic() throws DMEException {
        when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"321\"],\"range\":\"[321]\",\"interval\":\"321\",\"obj_type_id\":\"1125921381679104\"}";
        ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/metrics/v1/data-svc/history-data/action/query", HttpMethod.POST, param)).thenReturn(responseEntity);
        Map<String, Object> map = dataStoreStatisticHistoryService.queryVmfsStatistic(this.map);
        System.out.println(gson.toJson(map));
    }

    @Test
    public void queryVmfsStatisticCurrent() throws DMEException {
        when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"321\"],\"range\":\"[321]\",\"interval\":\"321\",\"obj_type_id\":\"1125921381679104\"}";
        ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/metrics/v1/data-svc/history-data/action/query", HttpMethod.POST, param)).thenReturn(responseEntity);
        dataStoreStatisticHistoryService.queryVmfsStatisticCurrent(map);
    }

    @Test
    public void queryNfsStatistic() throws DMEException {
        when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"321\"],\"range\":\"[321]\",\"interval\":\"321\",\"obj_type_id\":\"1126179079716864\"}";
        ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/metrics/v1/data-svc/history-data/action/query", HttpMethod.POST, param)).thenReturn(responseEntity);
        dataStoreStatisticHistoryService.queryNfsStatistic(map);
    }

    @Test
    public void queryNfsStatisticCurrent() throws DMEException {
        when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"321\"],\"range\":\"[321]\",\"interval\":\"321\",\"obj_type_id\":\"1126179079716864\"}";
        ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/metrics/v1/data-svc/history-data/action/query", HttpMethod.POST, param)).thenReturn(responseEntity);
        dataStoreStatisticHistoryService.queryNfsStatisticCurrent(map);
    }

    @Test
    public void queryVolumeStatistic() throws DMEException {
        when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"321\"],\"range\":\"[321]\",\"interval\":\"321\",\"obj_type_id\":\"1125921381679104\"}";
        ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/metrics/v1/data-svc/history-data/action/query", HttpMethod.POST, param)).thenReturn(responseEntity);
        dataStoreStatisticHistoryService.queryVolumeStatistic(map);
    }

    @Test
    public void queryControllerStatistic() throws DMEException {
        when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"321\"],\"range\":\"[321]\",\"interval\":\"321\",\"obj_type_id\":\"1125908496777216\"}";
        ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/metrics/v1/data-svc/history-data/action/query", HttpMethod.POST, param)).thenReturn(responseEntity);
        dataStoreStatisticHistoryService.queryControllerStatistic(map);
    }

    @Test
    public void queryStoragePortStatistic() throws DMEException {
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"321\"],\"range\":\"[321]\",\"interval\":\"321\",\"obj_type_id\":\"1125925676646400\"}";
        ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/metrics/v1/data-svc/history-data/action/query", HttpMethod.POST, param)).thenReturn(responseEntity);
        dataStoreStatisticHistoryService.queryStoragePortStatistic(map);
    }

    @Test
    public void queryStorageDiskStatistic() throws DMEException {
        when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"321\"],\"range\":\"[321]\",\"interval\":\"321\",\"obj_type_id\":\"1125917086711808\"}";
        ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/metrics/v1/data-svc/history-data/action/query", HttpMethod.POST, param)).thenReturn(responseEntity);
        dataStoreStatisticHistoryService.queryStorageDiskStatistic(map);
    }

    @Test
    public void queryFsStatistic() throws DMEException {
        when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"321\"],\"range\":\"[321]\",\"interval\":\"321\",\"obj_type_id\":\"1126179079716864\"}";
        ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/metrics/v1/data-svc/history-data/action/query", HttpMethod.POST, param)).thenReturn(responseEntity);
        dataStoreStatisticHistoryService.queryFsStatistic(map);
    }

    @Test
    public void queryServiceLevelStatistic() throws DMEException {
        when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"321\"],\"range\":\"[321]\",\"interval\":\"321\",\"obj_type_id\":\"1126174784749568\"}";
        ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/metrics/v1/data-svc/history-data/action/query", HttpMethod.POST, param)).thenReturn(responseEntity);
        dataStoreStatisticHistoryService.queryServiceLevelStatistic(map);
    }

    @Test
    public void queryServiceLevelLunStatistic() throws DMEException {
        when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"321\"],\"range\":\"[321]\",\"interval\":\"321\",\"obj_type_id\":\"1125921381679104\"}";
        ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/metrics/v1/data-svc/history-data/action/query", HttpMethod.POST, param)).thenReturn(responseEntity);
        dataStoreStatisticHistoryService.queryServiceLevelLunStatistic(map);
    }

    @Test
    public void queryServiceLevelStoragePoolStatistic() throws DMEException {
        when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"321\"],\"range\":\"[321]\",\"interval\":\"321\",\"obj_type_id\":\"1125912791744512\"}";
        ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/metrics/v1/data-svc/history-data/action/query", HttpMethod.POST, param)).thenReturn(responseEntity);
        dataStoreStatisticHistoryService.queryServiceLevelStoragePoolStatistic(map);
    }

    @Test
    public void queryStoragePoolStatistic() throws DMEException {
        when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"321\"],\"range\":\"[321]\",\"interval\":\"321\",\"obj_type_id\":\"1125912791744512\"}";
        ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/metrics/v1/data-svc/history-data/action/query", HttpMethod.POST, param)).thenReturn(responseEntity);
        dataStoreStatisticHistoryService.queryStoragePoolStatistic(map);
    }

    @Test
    public void queryStorageDevcieStatistic() throws DMEException {
        when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"321\"],\"range\":\"[321]\",\"interval\":\"321\",\"obj_type_id\":\"1125904201809920\"}";
        ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/metrics/v1/data-svc/history-data/action/query", HttpMethod.POST, param)).thenReturn(responseEntity);
        dataStoreStatisticHistoryService.queryStorageDevcieStatistic(map);
    }

    @Test
    public void queryStorageDevcieCurrentStatistic() throws DMEException {
        when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"321\"],\"range\":\"[321]\",\"interval\":\"321\",\"obj_type_id\":\"1125904201809920\"}";
        ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/metrics/v1/data-svc/history-data/action/query", HttpMethod.POST, param)).thenReturn(responseEntity);
        dataStoreStatisticHistoryService.queryStorageDevcieCurrentStatistic(map);
    }

    @Test
    public void queryStoragePoolCurrentStatistic() throws DMEException {
        when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"321\"],\"range\":\"[321]\",\"interval\":\"321\",\"obj_type_id\":\"1125912791744512\"}";
        ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/metrics/v1/data-svc/history-data/action/query", HttpMethod.POST, param)).thenReturn(responseEntity);
        dataStoreStatisticHistoryService.queryStoragePoolCurrentStatistic(map);
    }

    @Test
    public void queryControllerCurrentStatistic() throws DMEException {
        when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"321\"],\"range\":\"[321]\",\"interval\":\"321\",\"obj_type_id\":\"1125908496777216\"}";
        ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/metrics/v1/data-svc/history-data/action/query", HttpMethod.POST, param)).thenReturn(responseEntity);
        dataStoreStatisticHistoryService.queryControllerCurrentStatistic(map);
    }

    @Test
    public void queryStoragePortCurrentStatistic() throws DMEException {
        when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"321\"],\"range\":\"[321]\",\"interval\":\"321\",\"obj_type_id\":\"1125925676646400\"}";
        ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/metrics/v1/data-svc/history-data/action/query", HttpMethod.POST, param)).thenReturn(responseEntity);
        dataStoreStatisticHistoryService.queryStoragePortCurrentStatistic(map);
    }

    @Test
    public void queryStorageDiskCurrentStatistic() throws DMEException {
        when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"321\"],\"range\":\"[321]\",\"interval\":\"321\",\"obj_type_id\":\"1125917086711808\"}";
        ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/metrics/v1/data-svc/history-data/action/query", HttpMethod.POST, param)).thenReturn(responseEntity);
        dataStoreStatisticHistoryService.queryStorageDiskCurrentStatistic(map);
    }

    @Test
    public void queryHistoryStatistic() throws DMEException {
        String relationOrInstance = "SYS_StoragePool";
        when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
        String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"321\"],\"range\":\"[321]\",\"interval\":\"321\",\"obj_type_id\":\"1125912791744512\"}";
        ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/metrics/v1/data-svc/history-data/action/query", HttpMethod.POST, param)).thenReturn(responseEntity);
        dataStoreStatisticHistoryService.queryHistoryStatistic(relationOrInstance,map);

    }

    @Test
    public void queryCurrentStatistic() throws DMEException {
        String[] relationOrInstances = {"SYS_StorageDisk","SYS_StoragePool","SYS_DjTier","SYS_Lun","SYS_StorageFileSystem","SYS_Controller","SYS_StoragePort","SYS_StorDevice"};
        for (int i = 0; i < 7; i++) {
            when(dmeRelationInstanceService.getLunInstance()).thenReturn(map2);
            String param = "{\"obj_ids\":[\"321\"],\"indicator_ids\":[\"321\"],\"range\":\"[321]\",\"interval\":\"321\",\"obj_type_id\":\"1125904201809920\"}";
            ResponseEntity responseEntity = new ResponseEntity(gson.toJson(map3), null, HttpStatus.OK);
            when(dmeAccessService.access("/rest/metrics/v1/data-svc/history-data/action/query", HttpMethod.POST, param)).thenReturn(responseEntity);
            dataStoreStatisticHistoryService.queryCurrentStatistic(relationOrInstances[i], map);
        }
    }
}