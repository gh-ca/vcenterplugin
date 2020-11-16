package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.DMEServiceApplication;
import com.dmeplugin.dmestore.dao.DmeInfoDao;
import com.dmeplugin.dmestore.dao.VCenterInfoDao;
import com.dmeplugin.dmestore.model.SimpleServiceLevel;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.dmeplugin.vmware.SpringBootConnectionHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ServiceLevelServiceImpl Tester.
 *
 * @author wangxiangyong
 * @version 1.0
 * @since <pre>十一月 11, 2020</pre>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DMEServiceApplication.class)
public class ServiceLevelServiceImplTest {
    @Autowired
    private ServiceLevelServiceImpl serviceLevelService;
    private String serivceLevelId = "0927dbb9-9e7a-43ee-9427-02c14963290e";
    private String dbUrl = "jdbc:h2://C:/ProgramData/VMware/vCenterServer/runtime/";
    @Before
    public void before() throws Exception {

    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getDmeAccessService()
     */
    @Test
    public void testGetDmeAccessService() throws Exception {
        serviceLevelService.getDmeAccessService();
    }

    /**
     * Method: setDmeAccessService(DmeAccessService dmeAccessService)
     */
    @Test
    public void testSetDmeAccessService() throws Exception {
        assert true;
    }

    /**
     * Method: getDmeRelationInstanceService()
     */
    @Test
    public void testGetDmeRelationInstanceService() throws Exception {
        serviceLevelService.getDmeRelationInstanceService();
    }

    /**
     * Method: setDmeRelationInstanceService(DmeRelationInstanceService dmeRelationInstanceService)
     */
    @Test
    public void testSetDmeRelationInstanceService() throws Exception {
        assert true;
    }

    /**
     * Method: getDmeStorageService()
     */
    @Test
    public void testGetDmeStorageService() throws Exception {
        serviceLevelService.getDmeStorageService();
    }

    /**
     * Method: setDmeStorageService(DmeStorageService dmeStorageService)
     */
    @Test
    public void testSetDmeStorageService() throws Exception {
        assert true;
    }

    /**
     * Method: getVcsdkUtils()
     */
    @Test
    public void testGetVcsdkUtils() throws Exception {
        serviceLevelService.getVcsdkUtils();
    }

    /**
     * Method: setVcsdkUtils(VCSDKUtils vcsdkUtils)
     */
    @Test
    public void testSetVcsdkUtils() throws Exception {
        assert true;
    }

    /**
     * Method: getvCenterInfoService()
     */
    @Test
    public void testGetvCenterInfoService() throws Exception {
        serviceLevelService.getvCenterInfoService();
    }

    /**
     * Method: setvCenterInfoService(VCenterInfoService vCenterInfoService)
     */
    @Test
    public void testSetvCenterInfoService() throws Exception {
        assert true;
    }

    /**
     * Method: listServiceLevel(Map<String, Object> params)
     */
    @Test
    public void testListServiceLevel() throws Exception {
        Map<String, Object> params = new HashMap<>();
        List<SimpleServiceLevel> list = serviceLevelService.listServiceLevel(params);
        assert list.size() > 0;
    }

    /**
     * Method: updateVmwarePolicy()
     */
    @Test
    public void testUpdateVmwarePolicy() throws Exception {
        serviceLevelService.updateVmwarePolicy();
    }

    /**
     * Method: getStoragePoolInfosByServiceLevelId(String serivceLevelId)
     */
    @Test
    public void testGetStoragePoolInfosByServiceLevelId() throws Exception {
        serviceLevelService.getStoragePoolInfosByServiceLevelId(serivceLevelId);
    }

    /**
     * Method: getVolumeInfosByServiceLevelId(String serviceLevelId)
     */
    @Test
    public void testGetVolumeInfosByServiceLevelId() throws Exception {
        serviceLevelService.getVolumeInfosByServiceLevelId(serivceLevelId);
    }

    /**
     * Method: getStoragePoolIdsByServiceLevelId(String serviceLevelId)
     */
    @Test
    public void testGetStoragePoolIdsByServiceLevelId() throws Exception {
        serviceLevelService.getStoragePoolIdsByServiceLevelId(serivceLevelId);
    }

    /**
     * Method: getVolumeIdsByServiceLivelId(String serviceLevelId)
     */
    @Test
    public void testGetVolumeIdsByServiceLivelId() throws Exception {
        serviceLevelService.getStoragePoolIdsByServiceLevelId(serivceLevelId);
    }

    /**
     * Method: getStatisticByServiceLevelId(String serviceLevelId)
     */
    @Test
    public void testGetStatisticByServiceLevelId() throws Exception {
        serviceLevelService.getStoragePoolIdsByServiceLevelId(serivceLevelId);
    }


    /**
     * Method: convertBean(Object object)
     */
    @Test
    public void testConvertBean() throws Exception {
        try {
            String s = "{\"id\":\"0927dbb9-9e7a-43ee-9427-02c14963290e\",\"name\":\"cctest\",\"description\":\"blockservice-levelfordj\",\"type\":\"BLOCK\",\"protocol\":null,\"total_capacity\":2208768.0,\"used_capacity\":225344.0,\"free_capacity\":1983424.0,\"capabilities\":{\"resource_type\":null,\"compression\":null,\"deduplication\":null,\"iopriority\":null,\"smarttier\":null,\"qos\":null}}";
            Method method = serviceLevelService.getClass().getMethod("convertBean", Object.class);
            method.setAccessible(true);
            method.invoke(serviceLevelService, s);
        } catch (NoSuchMethodException e) {

        } catch (IllegalAccessException e) {

        } catch (InvocationTargetException e) {

        }
    }

    /**
     * Method: getInstancePropertyValue(Map<String, Map<String, Object>> instanceMap, String id, String name)
     */
    @Test
    public void testGetInstancePropertyValue() throws Exception {
        try {
            Map<String, Map<String, Object>> lunInstance = new HashMap<>();
            Map<String, Object> map = new HashMap<>();
            map.put("resId", "0AE76D42D5FD3F85A4A848B035004F90");
            map.put("name", "vmfsclusterliuxh102806");
            map.put("id", "0AE76D42D5FD3F85A4A848B035004F90");
            map.put("wwn", "67c1cf11005893452d301938000000dd");
            Method method = serviceLevelService.getClass().getMethod("getInstancePropertyValue", Map.class, String.class, String.class);
            method.setAccessible(true);
            method.invoke(serviceLevelService, lunInstance, "67c1cf11005893452d301938000000dd", "wwn");
        } catch (NoSuchMethodException e) {

        } catch (IllegalAccessException e) {

        } catch (InvocationTargetException e) {

        }

    }

    /**
     * Method: getStoragePoolInfosByStorageIdStoragePoolIds(String storageDeviceId, List<String> storagePoolIds)
     */
    @Test
    public void testGetStoragePoolInfosByStorageIdStoragePoolIds() throws Exception {
        try {
            String storageDeviceId = "B94BFF9D0DFB11EBBD3D0050568491C9";
            List<String> storagePoolIds = new ArrayList<>();
            storagePoolIds.add("EED2057F2F11314AA95DC2EF0C06DCDF");
            Method method = serviceLevelService.getClass().getMethod("getStoragePoolInfosByStorageIdStoragePoolIds", String.class, List.class);
            method.setAccessible(true);
            method.invoke(serviceLevelService, storageDeviceId, storagePoolIds);
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
    }

    /**
     * Method: getContainIdsByRelationNameLevelId(String relationName, String serviceLevelId)
     */
    @Test
    public void testGetContainIdsByRelationNameLevelId() throws Exception {
        try {
            Method method = serviceLevelService.getClass().getMethod("getContainIdsByRelationNameLevelId", String.class, String.class);
            method.setAccessible(true);
            method.invoke(serviceLevelService, serivceLevelId);
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }

    }

    /**
     * Method: getStoragePoolDevcieIdRelationByRelationNameLevelId(String storageDeviceId, String storagePoolId)
     */
    @Test
    public void testGetStoragePoolDevcieIdRelationByRelationNameLevelId() throws Exception {
        try {
            String storageDeviceId = "B94BFF9D0DFB11EBBD3D0050568491C9";
            String storagePoolId = "0";
            Method method = serviceLevelService.getClass().getMethod("getStoragePoolDevcieIdRelationByRelationNameLevelId", String.class, String.class);
            method.setAccessible(true);
            method.invoke(serviceLevelService, storageDeviceId, storagePoolId);
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }

    }

    /**
     * Method: convertInstanceToStoragePool(Object instanceObj)
     */
    @Test
    public void testConvertInstanceToStoragePool() throws Exception {
        try {
            String json = "{\"ownerType\":\"eSight_Storage\",\"tier0RaidLv\":0,\"storageDeviceId\":\"B94BFF9D0DFB11EBBD3D0050568491C9\",\"ownerId\":\"3BEAFDD429EB32C2AC03EFA25608054D\",\"type\":\"file\",\"ownerName\":\"eSight_Storage\",\"lastMonitorTime\":1605237313615,\"runningStatus\":\"normal\",\"totalCapacity\":1024.0,\"tier2RaidLv\":0,\"confirmStatus\":\"unconfirmed\",\"protectionCapacity\":0.0,\"id\":\"3FB520372B753CFB8594F4C969587E5E\",\"last_Modified\":1605237313662,\"diskPoolId\":\"1\",\"usedCapacity\":77.5,\"tier0Capacity\":0.0,\"class_Id\":1029,\"dataStatus\":\"normal\",\"subscribedCapacity\":109.57,\"resId\":\"3FB520372B753CFB8594F4C969587E5E\",\"tier1RaidLv\":2,\"dedupedCapacity\":0.0,\"is_Local\":true,\"class_Name\":\"SYS_StoragePool\",\"compressedCapacity\":0.0,\"regionId\":\"C4CA4238A0B933828DCC509A6F75849B\",\"name\":\"fileStoragePool002\",\"poolId\":\"1\",\"tier1Capacity\":1024.0,\"nativeId\":\"nedn=b94bff9d-0dfb-11eb-bd3d-0050568491c9,id=1,objecttype=216\",\"dataSource\":\"auto\",\"tier2Capacity\":0.0,\"status\":\"normal\"}";
            Method method = serviceLevelService.getClass().getMethod("convertInstanceToStoragePool", Object.class);
            method.setAccessible(true);
            method.invoke( serviceLevelService, json);
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }

    }

} 
