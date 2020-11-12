package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.dao.DmeInfoDao;
import com.dmeplugin.dmestore.model.SimpleServiceLevel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
public class ServiceLevelServiceImplTest {
    private ServiceLevelServiceImpl serviceLevelService;
    private String serivceLevelId = "0927dbb9-9e7a-43ee-9427-02c14963290e";

    @Before
    public void before() throws Exception {
        serviceLevelService = new ServiceLevelServiceImpl();
        DmeAccessServiceImpl dmeAccessService = new DmeAccessServiceImpl();
        DmeInfoDao dmeInfoDao = new DmeInfoDao();
        dmeInfoDao.setUrl("jdbc:h2://C:/ProgramData/VMware/vCenterServer/runtime/");
        dmeAccessService.setDmeInfoDao(dmeInfoDao);
        serviceLevelService.setDmeAccessService(dmeAccessService);

        VCenterInfoService vCenterInfoService = new VCenterInfoServiceImpl();
        serviceLevelService.setvCenterInfoService(vCenterInfoService);
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
        serviceLevelService.setDmeAccessService(null);
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
        serviceLevelService.setDmeRelationInstanceService(null);
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
        serviceLevelService.setDmeStorageService(null);
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
        serviceLevelService.setVcsdkUtils(null);
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
        serviceLevelService.setvCenterInfoService(null);
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
        serviceLevelService.getStatisticByServiceLevelId(serivceLevelId);
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
 
/* 
try { 
   Method method = ServiceLevelServiceImpl.getClass().getMethod("getInstancePropertyValue", Map<String,.class, String.class, String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: getStoragePoolInfosByStorageIdStoragePoolIds(String storageDeviceId, List<String> storagePoolIds)
     */
    @Test
    public void testGetStoragePoolInfosByStorageIdStoragePoolIds() throws Exception {
 
/* 
try { 
   Method method = ServiceLevelServiceImpl.getClass().getMethod("getStoragePoolInfosByStorageIdStoragePoolIds", String.class, List<String>.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: getContainIdsByRelationNameLevelId(String relationName, String serviceLevelId)
     */
    @Test
    public void testGetContainIdsByRelationNameLevelId() throws Exception {
 
/* 
try { 
   Method method = ServiceLevelServiceImpl.getClass().getMethod("getContainIdsByRelationNameLevelId", String.class, String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: getStoragePoolDevcieIdRelationByRelationNameLevelId(String storageDeviceId, String storagePoolId)
     */
    @Test
    public void testGetStoragePoolDevcieIdRelationByRelationNameLevelId() throws Exception {
 
/* 
try { 
   Method method = ServiceLevelServiceImpl.getClass().getMethod("getStoragePoolDevcieIdRelationByRelationNameLevelId", String.class, String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: convertInstanceToStoragePool(Object instanceObj)
     */
    @Test
    public void testConvertInstanceToStoragePool() throws Exception {
 
/* 
try { 
   Method method = ServiceLevelServiceImpl.getClass().getMethod("convertInstanceToStoragePool", Object.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

} 
