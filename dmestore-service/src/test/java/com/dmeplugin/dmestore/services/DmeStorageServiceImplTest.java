package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.model.Storage;
import com.dmeplugin.dmestore.model.StoragePool;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.beans.binding.When;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.omg.CORBA.Any;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.IsInstanceOf.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

/**
 * @author lianq
 * @className DmeStorageServiceImplTest
 * @description TODO
 * @date 2020/11/12 10:09
 */
public class DmeStorageServiceImplTest {

    Gson gson = new Gson();
    String url;

    @Mock
    DmeAccessService dmeAccessService;

    @InjectMocks
    DmeStorageService dmeStorageService = new DmeStorageServiceImpl();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void getStorages() throws DMEException {
        url = "/rest/storagemgmt/v1/storages";
        List<JsonObject> reqList = new ArrayList<>();
        //更换设定返回的数据
        String data = " {\n" +
                "    \"id\": \"b94bff9d-0dfb-11eb-bd3d-0050568491c9\",\n" +
                "    \"name\": \"Huawei.Storage\",\n" +
                "    \"ip\": \"10.143.133.201\",\n" +
                "    \"status\": \"1\",\n" +
                "    \"synStatus\": \"2\",\n" +
                "    \"sn\": \"2102351QLH9WK5800028\",\n" +
                "    \"vendor\": \"Huawei\",\n" +
                "    \"model\": \"5300 V5\",\n" +
                "    \"usedCapacity\": 297.5625,\n" +
                "    \"totalCapacity\": 6542.693389892578,\n" +
                "    \"totalEffectiveCapacity\": 3181,\n" +
                "    \"freeEffectiveCapacity\": 2883.44,\n" +
                "    \"subscriptionCapacity\": 309.57,\n" +
                "    \"protectionCapacity\": 0,\n" +
                "    \"fileCapacity\": 77.5,\n" +
                "    \"blockCapacity\": 220.06,\n" +
                "    \"dedupedCapacity\": 0,\n" +
                "    \"compressedCapacity\": 0,\n" +
                "    \"optimizeCapacity\": 297.5625,\n" +
                "    \"azIds\": [],\n" +
                "    \"storagePool\": null,\n" +
                "    \"volume\": null,\n" +
                "    \"fileSystem\": null,\n" +
                "    \"dTrees\": null,\n" +
                "    \"nfsShares\": null,\n" +
                "    \"bandPorts\": null,\n" +
                "    \"logicPorts\": null,\n" +
                "    \"storageControllers\": null,\n" +
                "    \"storageDisks\": null,\n" +
                "    \"productVersion\": \"V500R007C10\",\n" +
                "    \"warning\": null,\n" +
                "    \"event\": null,\n" +
                "    \"location\": \"\",\n" +
                "    \"patchVersion\": \"SPH013\",\n" +
                "    \"maintenanceStart\": null,\n" +
                "    \"maintenanceOvertime\": null\n" +
                "  }";
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        reqList.add(jsonObject);
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("datas", reqList);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(gson.toJson(reqMap),null,HttpStatus.OK);
        System.out.println("================="+responseEntity+"=================");
        when(dmeAccessService.access(url, HttpMethod.GET, null)).thenReturn(responseEntity);
        dmeStorageService.getStorages();
    }

    @Test
    public void getStorageDetail() throws DMEException {
        String storageId = "123";
        url = "/rest/storagemgmt/v1/storages/123/detail";
        //更换设定返回的数据
        String data = " {\n" +
                "  \"id\" : \"string\",\n" +
                "  \"name\" : \"string\",\n" +
                "  \"ip\" : \"string\",\n" +
                "  \"status\" : \"string\",\n" +
                "  \"syn_status\" : \"string\",\n" +
                "  \"sn\" : \"string\",\n" +
                "  \"vendor\" : \"string\",\n" +
                "  \"model\" : \"string\",\n" +
                "  \"product_version\" : \"string\",\n" +
                "  \"patch_version\" : \"string\",\n" +
                "  \"used_capacity\" : 0.0,\n" +
                "  \"total_capacity\" : 0.0,\n" +
                "  \"total_effective_capacity\" : 0.0,\n" +
                "  \"free_effective_capacity\" : 0.0,\n" +
                "  \"location\" : \"string\",\n" +
                "  \"az_ids\" : [ \"string\" ],\n" +
                "  \"maintenance_start\" : 1564211245111,\n" +
                "  \"maintenance_overtime\" : 1564211245111\n" +
                "}";
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        ResponseEntity<String> responseEntity = new ResponseEntity<>(gson.toJson(jsonObject),null,HttpStatus.OK);
        System.out.println("================= dmeAccessService.access:"+responseEntity+"=================");
        when(dmeAccessService.access(url, HttpMethod.GET, null)).thenReturn(responseEntity);

        List<JsonObject> reqPool = new ArrayList<>();
        String pool = "{\n" +
                " \"ownerType\": \"eSight_Storage\",\n" +
                " \"storageDeviceId\": \"98BBDFB0579E11EAA20D005056ADF850\",\n" +
                " \"ownerId\": \"3BEAFDD429EB32C2AC03EFA25608054D\",\n" +
                " \"wwn\": \"648435a10072df427691fabd00000013\",\n" +
                " \"ownerName\": \"eSight_Storage\",\n" +
                " \"lastMonitorTime\": 1582614988093,\n" +
                " \"totalCapacity\": 1.0,\n" +
                " \"protectionCapacity\": 0.0,\n" +
                " \"id\": \"3F6CC81D8C4C358F8888303479544ACB\",\n" +
                " \"lunType\": \"normal\",\n" +
                " \"class_Id\": 1053,\n" +
                " \"lunId\": \"19\",\n" +
                " \"dataStatus\": \"normal\",\n" +
                " \"resId\": \"3F6CC81D8C4C358F8888303479544ACB\",\n" +
                " \"dedupedCapacity\": 0.0,\n" +
                " \"class_Name\": \"SYS_Lun\",\n" +
                " \"compressedCapacity\": 0.0,\n" +
                " \"regionId\": \"C4CA4238A0B933828DCC509A6F75849B\",\n" +
                " \"name\": \"SPEC-1877\",\n" +
                " \"mapped\": false,\n" +
                " \"poolId\": \"30\",\n" +
                " \"nativeId\": \"nedn=98bbdfb0-579e-11ea-a20d-005056adf850,id=19,objecttype=11\",\n" +
                " \"dataSource\": \"auto\",\n" +
                " \"allocCapacity\": 1.19\n" +
                " }";
        JsonObject jsonPool = new JsonParser().parse(pool).getAsJsonObject();
        reqPool.add(jsonPool);
        Map<String, Object> mapPool = new HashMap<>();
        mapPool.put("objList", reqPool);
        ResponseEntity<String> responseEntity2 = new ResponseEntity<>(gson.toJson(mapPool),null,HttpStatus.OK);
        String poolUrl = "/rest/resourcedb/v1/instances/SYS_StoragePool?condition={json}&&pageSize=1000";
        String params = "{\"constraint\":[{\"simple\":{\"name\":\"dataStatus\",\"operator\":\"equal\",\"value\":\"normal\"}},{\"simple\":{\"name\":\"storageDeviceId\",\"operator\":\"equal\",\"value\":\"123\"},\"logOp\":\"and\"}]}";
        System.out.println("================= dmeAccessService.access:"+responseEntity2+"=================");
        when(dmeAccessService.accessByJson(poolUrl, HttpMethod.GET, params)).thenReturn(responseEntity2);
        System.out.println("================= dmeAccessService.access:"+responseEntity2+"=================");
        String diskUrl = "/rest/resourcedb/v1/instances/SYS_StorageDisk?condition={json}&&pageSize=1000";
            when(dmeAccessService.accessByJson(diskUrl, HttpMethod.GET, params)).thenReturn(responseEntity2);
        dmeStorageService.getStorageDetail(storageId);
    }

    @Test
    public void getStoragePools() {
    }

    @Test
    public void getLogicPorts() {
    }

    @Test
    public void getVolumesByPage() {
    }

    @Test
    public void getFileSystems() {
    }

    @Test
    public void getDtrees() {
    }

    @Test
    public void getNfsShares() {
    }

    @Test
    public void getBandPorts() {
    }

    @Test
    public void getStorageControllers() {
    }

    @Test
    public void getStorageDisks() {
    }

    @Test
    public void getStorageEthPorts() {
    }

    @Test
    public void getStorageResIdBySn() {
    }

    @Test
    public void getEthPortsByResId() {
    }

    @Test
    public void getVolume() {
    }

    @Test
    public void getStoragePort() {
    }

    @Test
    public void getFailoverGroups() {
    }

    @Test
    public void getFileSystemDetail() {
    }

    @Test
    public void listStoragePerformance() {
    }

    @Test
    public void listStoragePoolPerformance() {
    }

    @Test
    public void listStorageControllerPerformance() {
    }

    @Test
    public void listStorageDiskPerformance() {
    }

    @Test
    public void listStoragePortPerformance() {
    }

    @Test
    public void listVolumesPerformance() {
    }

    @Test
    public void queryVolumeByName() {
    }

    @Test
    public void queryFsByName() {
    }

    @Test
    public void queryShareByName() {
    }
}