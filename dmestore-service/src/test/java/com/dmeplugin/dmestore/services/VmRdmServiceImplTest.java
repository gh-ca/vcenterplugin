package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.model.CreateVolumesRequest;
import com.dmeplugin.dmestore.model.ServiceVolumeBasicParams;
import com.dmeplugin.dmestore.model.ServiceVolumeMapping;
import com.dmeplugin.dmestore.model.VmRdmCreateBean;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.beans.binding.When;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import springfox.documentation.spring.web.json.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * @author lianq
 * @className VmRdmServiceImplTest
 * @description TODO
 * @date 2020/11/20 10:43
 */
public class VmRdmServiceImplTest {

    @Mock
    private DmeAccessService dmeAccessService;
    @Mock
    private TaskService taskService;
    private Gson gson = new Gson();
    @Mock
    private VCSDKUtils vcsdkUtils;
    @Mock
    private VmfsAccessService vmfsAccessService;
    @InjectMocks
    VmRdmService vmRdmService = new VmRdmServiceImpl();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createRdm() throws DMEException {
        VmRdmCreateBean vmRdmCreateBean = new VmRdmCreateBean();
        CreateVolumesRequest createVolumesRequest = new CreateVolumesRequest();
        ServiceVolumeMapping serviceVolumeMapping = new ServiceVolumeMapping();
        serviceVolumeMapping.setHostgroupId("321");
        serviceVolumeMapping.setHostId("321");
        createVolumesRequest.setMapping(serviceVolumeMapping);
        createVolumesRequest.setServiceLevelId("321");
        List<ServiceVolumeBasicParams> volumes = new ArrayList<>();
        ServiceVolumeBasicParams serviceVolumeBasicParams = new ServiceVolumeBasicParams();
        serviceVolumeBasicParams.setCapacity(20);
        serviceVolumeBasicParams.setCount(20);
        serviceVolumeBasicParams.setName("321");
        serviceVolumeBasicParams.setStartSuffix(21);
        serviceVolumeBasicParams.setUnit("GB");
        volumes.add(serviceVolumeBasicParams);
        createVolumesRequest.setVolumes(volumes);
        vmRdmCreateBean.setCreateVolumesRequest(createVolumesRequest);
        String req = "{\"volumes\":[{\"name\":\"321\",\"count\":20,\"capacity\":20,\"start_suffix\":21}],\"service_level_id\":\"321\"}";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("task_id","321");
        ResponseEntity<String> responseEntity = new ResponseEntity<>(gson.toJson(jsonObject), null, HttpStatus.ACCEPTED);
        when(dmeAccessService.access("/rest/blockservice/v1/volumes", HttpMethod.POST, req)).thenReturn(responseEntity);
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("status", 3);
        when(taskService.queryTaskByIdUntilFinish("321")).thenReturn(jsonObject1);
        String res = "{\n" +
                "    \"id\": \"02bb989a-7ac2-40cd-852d-c9b26bb2ab5b\", \n" +
                "    \"name\": \"volume001\", \n" +
                "    \"description\": \"test volume\", \n" +
                "    \"status\": \"normal\", \n" +
                "    \"attached\": false, \n" +
                "    \"project_id\": \"e554f301f99044bf8175e60fc1524ebc\", \n" +
                "    \"alloctype\": \"thin\", \n" +
                "    \"capacity\": 10, \n" +
                "    \"service_level_name\": \"TEST\", \n" +
                "    \"attachments\": [\n" +
                "        {\n" +
                "            \"id\": \"98c41e3f-de68-4b0a-8298-dccf81c1ca6c\", \n" +
                "            \"volume_id\": \"98c41e3f-de68-4b0a-8298-dccf81c1ca6c\", \n" +
                "            \"host_id\": \"434f80a9-22b4-4d44-b14e-783aa41689d5\", \n" +
                "            \"attached_at\": \"2019-09-22T15:20:26.000000\", \n" +
                "            \"attached_host_group\": \"534052e9-031e-49b3-a6f5-ac93c0ffc8a2\"\n" +
                "        }\n" +
                "    ], \n" +
                "    \"volume_raw_id\": \"4631\", \n" +
                "    \"volume_wwn\": \"321\", \n" +
                "    \"storage_id\": \"02bb989a-7ac2-40cd-852d-c9b26bb2ab5b\", \n" +
                "    \"storage_sn\": \"210048435a227e94\", \n" +
                "    \"pool_raw_id\": \"0\", \n" +
                "    \"capacity_usage\": \"20\", \n" +
                "    \"protected\": false, \n" +
                "    \"updated_at\": \"2019-05-06T18:49:27.046019\", \n" +
                "    \"created_at\": \"2019-05-06T18:49:25.107161\"\n" +
                "}\n";
        JsonObject jsonObject2 = gson.fromJson(res, JsonObject.class);
        List<JsonObject> jsonObjects = new ArrayList<>();
        jsonObjects.add(jsonObject2);
        Map<String, Object> map = new HashMap<>();
        map.put("volumes", jsonObjects);
        map.put("count", 1);
        ResponseEntity<String> responseEntity1 = new ResponseEntity<>(gson.toJson(map), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/blockservice/v1/volumes?name=321", HttpMethod.GET, null)).thenReturn(responseEntity1);
        Map<String, String> map1 = new HashMap<>();
        map1.put("hostObjectId", "321");
        map1.put("hostName", "321");
        when(vcsdkUtils.getHostByVmObjectId("321")).thenReturn(map1);
        String resp1 = "{\n" +
                "    \"id\": \"319585d6-13da-4b9d-9591-33ce63b52c0a\", \n" +
                "    \"project_id\": \"319585d6-13da-4b9d-9591-33ce63b52c0a\", \n" +
                "    \"name\": \"host1\", \n" +
                "    \"ip\": \"8.46.195.61\", \n" +
                "    \"display_status\": \"NORMAL\", \n" +
                "    \"managed_status\": \"NORMAL\", \n" +
                "    \"takeover_failed_reason\": {\n" +
                "        \"error_code\": \"hostmgmt.0067\", \n" +
                "        \"error_msg\": \"Failed to take over ESXi hosts in the current ESXi cluster.\", \n" +
                "        \"error_args\": [\n" +
                "            \"host_name\"\n" +
                "        ]\n" +
                "    }, \n" +
                "    \"os_status\": \"ONLINE\", \n" +
                "    \"overall_status\": \"GREEN\", \n" +
                "    \"os_type\": \"REDHAT\", \n" +
                "    \"initiator_count\": 2, \n" +
                "    \"access_mode\": \"ACCOUNT\", \n" +
                "    \"hostGroups\": [\n" +
                "        {\n" +
                "            \"id\": \"319585d6-13da-4b9d-9591-33ce63b52c0a\", \n" +
                "            \"name\": \"hostgroup1\"\n" +
                "        }\n" +
                "    ]\n" +
                "}\n";
        JsonObject jsonObject3 = gson.fromJson(resp1, JsonObject.class);
        List<JsonObject> objects = new ArrayList<>();
        objects.add(jsonObject3);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("hosts", objects);
        map2.put("total", 1);
        ResponseEntity<String> responseEntity2 = new ResponseEntity<>(gson.toJson(map2), null, HttpStatus.OK);
        when(dmeAccessService.access("/rest/hostmgmt/v1/hosts/summary", HttpMethod.POST, "{\"ip\":\"321\"}")).thenReturn(responseEntity2);
        List<Map<String, Object>> lunlist = new ArrayList<>();
        Map<String, Object> map3 = new HashMap<>();
        map3.put("uuid", "321");
        map3.put("devicePath", "321");
        map3.put("deviceName", "321");
        map3.put("localDisk", "321");
        map3.put("block", "321");
        map3.put("blockSize", "321");
        lunlist.add(map3);
        when(vcsdkUtils.getLunsOnHost("321")).thenReturn(gson.toJson(lunlist));
        vmRdmService.createRdm("321", "321", vmRdmCreateBean);

    }

    @Test
    public void getAllDmeHost() throws DMEException {
        List<Map<String, Object>> list = new ArrayList<>();
        when(dmeAccessService.getDmeHosts(null)).thenReturn(list);
        vmRdmService.getAllDmeHost();
    }

    @Test
    public void getDatastoreMountsOnHost() throws DMEException {
        when(vcsdkUtils.getDatastoreMountsOnHost("321")).thenReturn(new ArrayList<>());
        vmRdmService.getDatastoreMountsOnHost("321");
    }
}