package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.dao.DmeVmwareRalationDao;
import com.dmeplugin.dmestore.entity.DmeVmwareRelation;
import com.dmeplugin.dmestore.entity.VCenterInfo;
import com.dmeplugin.dmestore.model.Storage;
import com.dmeplugin.dmestore.utils.ToolUtils;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName VmfsAccessServiceTest.java
 * @Description TODO
 * @createTime 2020年11月19日 17:21:00
 */
public class VmfsAccessServiceTest {
    Gson gson = new Gson();
    @Mock
    private DmeVmwareRalationDao dmeVmwareRalationDao;
    @Mock
    private DmeAccessService dmeAccessService;
    @Mock
    private DmeStorageService dmeStorageService;
    @Mock
    private DataStoreStatisticHistoryService dataStoreStatisticHistoryService;
    @Mock
    private VCSDKUtils vcsdkUtils;
    @Mock
    private TaskService taskService;
    @Mock
    private VCenterInfoService vCenterInfoService;

    @InjectMocks
    private VmfsAccessService vmfsAccessService = new VmfsAccessServiceImpl();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void listVmfs() throws Exception {
        List<DmeVmwareRelation> dvrlist = new ArrayList<>();
        DmeVmwareRelation dmeVmwareRelation = new DmeVmwareRelation();
        String volumeId = "8f6d93f1-4214-46bc-ae7a-85f8349ebbd2";
        String storeObjectId = "13232";
        dmeVmwareRelation.setStoreId(storeObjectId);
        dmeVmwareRelation.setVolumeId(volumeId);
        dvrlist.add(dmeVmwareRelation);
        when(dmeVmwareRalationDao.getDmeVmwareRelation(ToolUtils.STORE_TYPE_VMFS)).thenReturn(dvrlist);
        List<Storage> list = new ArrayList<>();
        Storage storageObj = new Storage();
        storageObj.setName("Huawei.Storage");
        list.add(storageObj);
        when(dmeStorageService.getStorages()).thenReturn(list);
        List<Map<String, Object>> lists = new ArrayList<>();
        Map<String, Object> dsmap = new HashMap<>();
        dsmap.put("objectid", storeObjectId);
        dsmap.put("capacity", 4.75);
        dsmap.put("freeSpace", 4.052734375);
        dsmap.put("uncommitted", 4.15);
        List<String> wwnList = new ArrayList(16);
        wwnList.add(volumeId);
        dsmap.put("vmfsWwnList", wwnList);
        lists.add(dsmap);
        String listStr = gson.toJson(lists);
        when(vcsdkUtils.getAllVmfsDataStoreInfos(ToolUtils.STORE_TYPE_VMFS)).thenReturn(listStr);
        String url = "/rest/blockservice/v1/volumes" + "/" + volumeId;
        String jsonData = "{\"volume\": {\"id\": \"955a0632-c309-4471-a116-6a059d84ade3\",\"name\": \"VMFSTest20201026\",\"description\": null,\"status\": \"normal\",\"attached\": true,\"project_id\": null,\"alloctype\": \"thick\",\"capacity\": 1,\"service_level_name\": \"cctest\",\"attachments\": [{\"id\": \"8b561dd2-03bb-4f20-98c4-8092e75fe951\",\"volume_id\": \"955a0632-c309-4471-a116-6a059d84ade3\",\"host_id\": \"9cbd24b5-fb5b-4ad9-9393-cf05b9b97339\",\"attached_at\": \"2020-10-26T06:50:20.000000\",\"attached_host_group\": null}],\"volume_raw_id\": \"174\",\"volume_wwn\": \"67c1cf11005893452a7c7314000000ae\",\"storage_id\": \"b94bff9d-0dfb-11eb-bd3d-0050568491c9\",\"storage_sn\": \"2102351QLH9WK5800028\",\"pool_raw_id\": \"0\",\"capacity_usage\": null,\"protected\": false,\"updated_at\": \"2020-10-26T06:50:20.000000\",\"created_at\": \"2020-10-26T06:50:15.000000\",\"tuning\": {\"smarttier\": \"0\",\"dedupe_enabled\": null,\"compression_enabled\": null,\"workload_type_id\": null,\"smartqos\": {\"maxiops\": 123,\"miniops\": 2134,\"maxbandwidth\": 123413,\"minbandwidth\": 1234,\"latency\": 0.48},\"alloctype\": \"thick\"},\"initial_distribute_policy\": \"0\",\"prefetch_policy\": \"3\",\"owner_controller\": \"0B\",\"prefetch_value\": \"0\"}}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonData, null, HttpStatus.OK);
        when(dmeAccessService.access(url, HttpMethod.GET, null)).thenReturn(responseEntity);
        vmfsAccessService.listVmfs();
    }

    @Test
    public void listVmfsPerformance() throws Exception {
        String jsonStr = "{\"67c1cf1100589345402376ae00000143\": {\"1125921381744657\": {\"min\": {\"1605755940000\": 0.0},\"avg\": {\"0\": 0.0},\"max\": {\"1605755940000\": 0.0},\"series\": [{\"1605755940000\": 0.0}]}}}";
        Map<String, Object> remap = gson.fromJson(jsonStr, Map.class);
        List<String> wwns = new ArrayList<>();
        wwns.add("67c1cf1100589345402376ae00000143");
        Map<String, Object> params = new HashMap<>(16);
        params.put("obj_ids", wwns);
        when(dataStoreStatisticHistoryService.queryVmfsStatisticCurrent(params)).thenReturn(remap);

        vmfsAccessService.listVmfsPerformance(wwns);
    }

    @Test
    public void createVmfsByLevel() throws Exception {
        String requestParams = "{\"name\":\"testvmfsWxy\",\"volumeName\":\"testvmfsWxy\",\"isSameName\":true,\"capacity\":1,\"capacityUnit\":\"GB\",\"count\":1,\"service_level_id\":\"0927dbb9-9e7a-43ee-9427-02c14963290e\",\"service_level_name\":\"cctest\",\"version\":\"5\",\"blockSize\":1024,\"spaceReclamationGranularity\":1024,\"spaceReclamationPriority\":\"low\",\"host\":\"10.143.133.17\",\"hostId\":\"urn:vmomi:HostSystem:host-1034:674908e5-ab21-4079-9cb1-596358ee5dd1\",\"cluster\":null,\"clusterId\":null,\"storage_id\":null,\"pool_raw_id\":null,\"workload_type_id\":null,\"alloctype\":null,\"control_policy\":null,\"latencyChoose\":false,\"latency\":null,\"maxbandwidth\":null,\"maxbandwidthChoose\":false,\"maxiops\":null,\"maxiopsChoose\":false,\"minbandwidth\":null,\"minbandwidthChoose\":false,\"miniops\":null,\"miniopsChoose\":false,\"qosname\":null,\"deviceName\":null,\"hostDataloadSuccess\":true,\"culDataloadSuccess\":true}";
        Map<String, Object> params = gson.fromJson(requestParams, Map.class);
        String hostId = "urn:vmomi:HostSystem:host-1034:674908e5-ab21-4079-9cb1-596358ee5dd1";
        String hostIp = "10.143.133.17";
        String demHostId = "";

        String hbasStr = "";
        //gson.fromJson(dataStoreStr, new TypeToken<Map<String, Object>>() {}.getType());
        List<Map<String, Object>> hbas = gson.fromJson(hbasStr, new TypeToken<List<Map<String, Object>>>(){}.getType());
        when(vcsdkUtils.getHbasByHostObjectId(hostId)).thenReturn(hbas);

        String hostListStr = "";
        List<Map<String, Object>> hostList = gson.fromJson(hostListStr, new TypeToken<List<Map<String, Object>>>(){}.getType());
        when(dmeAccessService.getDmeHosts(null)).thenReturn(hostList);

        String initiatorStr = "";
        List<Map<String, Object>> initiators = gson.fromJson(initiatorStr, new TypeToken<List<Map<String, Object>>>(){}.getType());
        when(dmeAccessService.getDmeHostInitiators(demHostId)).thenReturn(initiators);

        String hostMapStr = "";
        Map<String, Object> hostMap = gson.fromJson(hostMapStr, new TypeToken<Map<String, Object>>(){}.getType());
        Map<String, Object> param = new HashMap<>();
        param.put("host", hostIp);
        param.put("hostId", hostId);
        when(dmeAccessService.createHost(param)).thenReturn(hostMap);

        String url = "/rest/blockservice/v1/volumes";
        String createBody = "";
        Map<String, Object> requestbody = gson.fromJson(createBody, new TypeToken<Map<String, Object>>(){}.getType());
        String s = "";
        ResponseEntity responseEntity = new ResponseEntity<>(s, null, HttpStatus.ACCEPTED);
        when(dmeAccessService.access(url, HttpMethod.POST, gson.toJson(requestbody))).thenReturn(responseEntity);

        when(taskService.checkTaskStatus(anyList())).thenReturn(true);

        String listVolumeUrl = "";
        String listVolumeStr = "";
        ResponseEntity responseEntity1 = new ResponseEntity<>(listVolumeStr, null, HttpStatus.OK);
        when(dmeAccessService.access(listVolumeUrl, HttpMethod.GET, null)).thenReturn(responseEntity1);

        VCenterInfo vCenterInfo = mock(VCenterInfo.class);
        when(vCenterInfoService.getVcenterInfo()).thenReturn(vCenterInfo);

        doNothing().when(vcsdkUtils).getLunsOnHost(anyString(), anyInt(), anyString());
        doNothing().when(vcsdkUtils).getLunsOnCluster(anyString(), anyInt(), anyString());
        String dataStoreStr = "";
        when(vcsdkUtils.createVmfsDataStore(anyMap(), anyInt(), anyString(),
                anyInt(), anyInt(), anyInt(), anyString())).thenReturn(dataStoreStr);

        doNothing().when(vcsdkUtils).scanDataStore(anyString(), null);
        doNothing().when(dmeVmwareRalationDao).save(anyList());
        doNothing().when(vcsdkUtils).attachTag(anyString(), anyString(), anyString(), vCenterInfo);

        vmfsAccessService.createVmfs(params);
    }

    @Test
    public void createVmfsUnLevel() {

    }

    @Test
    public void mountVmfs() {
    }

    @Test
    public void unmountVmfs() {
    }

    @Test
    public void deleteVmfs() {
    }

    @Test
    public void volumeDetail() {
    }

    @Test
    public void scanVmfs() {
    }

    @Test
    public void getHostsByStorageId() {
    }

    @Test
    public void getHostGroupsByStorageId() {
    }

    @Test
    public void queryVmfs() {
    }

    @Test
    public void queryDatastoreByName() {
    }

    @Test
    public void checkOrCreateToHost() {
    }
}