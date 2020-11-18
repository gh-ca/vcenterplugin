package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.dao.DmeInfoDao;
import com.dmeplugin.dmestore.dao.DmeVmwareRalationDao;
import com.dmeplugin.dmestore.dao.VCenterInfoDao;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.dmeplugin.vmware.SpringBootConnectionHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * VmfsAccessServiceImpl Tester.
 *
 * @author wangxiangyong
 * @version 1.0
 * @since <pre>十一月 12, 2020</pre>
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = DMEServiceApplication.class)
public class VmfsAccessServiceImplTest {
    private VmfsAccessServiceImpl vmfsAccessService;
    private String dbUrl = "jdbc:h2://C:/ProgramData/VMware/vCenterServer/runtime/";
    @Before
    public void before() throws Exception {
        vmfsAccessService = new VmfsAccessServiceImpl();
        DmeVmwareRalationDao dmeVmwareRalationDao = new DmeVmwareRalationDao();
        dmeVmwareRalationDao.setUrl(dbUrl);
        vmfsAccessService.setDmeVmwareRalationDao(dmeVmwareRalationDao);

        DmeAccessServiceImpl dmeAccessService = new DmeAccessServiceImpl();
        DmeInfoDao dmeInfoDao = new DmeInfoDao();
        dmeInfoDao.setUrl(dbUrl);
        dmeAccessService.setDmeInfoDao(dmeInfoDao);
        DmeStorageServiceImpl dmeStorageService = new DmeStorageServiceImpl();
        dmeStorageService.setDmeAccessService(dmeAccessService);

        VCenterInfoDao vCenterInfoDao = new VCenterInfoDao();
        vCenterInfoDao.setUrl(dbUrl);
        vmfsAccessService.setDmeAccessService(dmeAccessService);
        vmfsAccessService.setDmeStorageService(dmeStorageService);
        VCSDKUtils vcsdkUtils = new VCSDKUtils();
        SpringBootConnectionHelper helper = new SpringBootConnectionHelper();
        VCenterInfoServiceImpl vCenterInfoService = new VCenterInfoServiceImpl();
        vCenterInfoService.setvCenterInfoDao(vCenterInfoDao);
        helper.setvCenterInfoService(vCenterInfoService);
        vcsdkUtils.setVcConnectionHelper(helper);
        vmfsAccessService.setVcsdkUtils(vcsdkUtils);

        DmeRelationInstanceServiceImpl relationInstanceService = new DmeRelationInstanceServiceImpl();
        DataStoreStatisticHistoryServiceImpl historyService = new DataStoreStatisticHistoryServiceImpl();
        historyService.setDmeRelationInstanceService(relationInstanceService);
        historyService.setDmeAccessService(dmeAccessService);
        vmfsAccessService.setDataStoreStatisticHistoryService(historyService);
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: setvCenterInfoService(VCenterInfoService vCenterInfoService)
     */
    @Test
    public void testSetvCenterInfoService() throws Exception {
        vmfsAccessService.setvCenterInfoService(null);
    }

    /**
     * Method: getVcsdkUtils()
     */
    @Test
    public void testGetVcsdkUtils() throws Exception {
        vmfsAccessService.getVcsdkUtils();
    }

    /**
     * Method: setVcsdkUtils(VCSDKUtils vcsdkUtils)
     */
    @Test
    public void testSetVcsdkUtils() throws Exception {
        vmfsAccessService.setVcsdkUtils(null);
    }

    /**
     * Method: listVmfs()
     */
    @Test
    public void testListVmfs() throws Exception {
        vmfsAccessService.listVmfs();
    }

    /**
     * Method: listVmfsPerformance(List<String> volumeIds)
     */
    @Test
    public void testListVmfsPerformance() throws Exception {
        List<String> volumeIds = new ArrayList<>();
        volumeIds.add("67c1cf11005893453cb206470000013d");
        vmfsAccessService.listVmfsPerformance(volumeIds);

    }

    /**
     * Method: createVmfs(Map<String, Object> params)
     */
    @Test
    public void testCreateVmfs() throws Exception {

    }

    /**
     * Method: checkOrCreateToHost(String hostIp, String hostId)
     */
    @Test
    public void testCheckOrCreateToHost() throws Exception {

    }

    /**
     * Method: mountVmfs(Map<String, Object> params)
     */
    @Test
    public void testMountVmfs() throws Exception {

    }

    /**
     * Method: volumeDetail(String storageObjectId)
     */
    @Test
    public void testVolumeDetail() throws Exception {

    }

    /**
     * Method: scanVmfs()
     */
    @Test
    public void testScanVmfs() throws Exception {

    }


    /**
     * Method: unmountVmfsAll(Map<String, Object> params)
     */
    @Test
    public void testUnmountVmfsAll() throws Exception {

    }

    /**
     * Method: deleteVmfs(Map<String, Object> params)
     */
    @Test
    public void testDeleteVmfs() throws Exception {

    }

    /**
     * Method: setDmeVmwareRalationDao(DmeVmwareRalationDao dmeVmwareRalationDao)
     */
    @Test
    public void testSetDmeVmwareRalationDao() throws Exception {

    }

    /**
     * Method: setDmeAccessService(DmeAccessService dmeAccessService)
     */
    @Test
    public void testSetDmeAccessService() throws Exception {

    }

    /**
     * Method: setDataStoreStatisticHistoryService(DataStoreStatisticHistoryService dataStoreStatisticHistoryService)
     */
    @Test
    public void testSetDataStoreStatisticHistoryService() throws Exception {

    }

    /**
     * Method: setDmeStorageService(DmeStorageService dmeStorageService)
     */
    @Test
    public void testSetDmeStorageService() throws Exception {

    }

    /**
     * Method: setTaskService(TaskService taskService)
     */
    @Test
    public void testSetTaskService() throws Exception {

    }

    /**
     * Method: getHostsByStorageId(String storageId)
     */
    @Test
    public void testGetHostsByStorageId() throws Exception {

    }

    /**
     * Method: getHostGroupsByStorageId(String storageId)
     */
    @Test
    public void testGetHostGroupsByStorageId() throws Exception {

    }

    /**
     * Method: isVmfs(String objectId)
     */
    @Test
    public void testIsVmfs() throws Exception {

    }

    /**
     * Method: queryVmfs(String dsObjId)
     */
    @Test
    public void testQueryVmfs() throws Exception {

    }

    /**
     * Method: queryDatastoreByName(String name)
     */
    @Test
    public void testQueryDatastoreByName() throws Exception {

    }


    /**
     * Method: createVmfsOnVmware(Map<String, Object> params)
     */
    @Test
    public void testCreateVmfsOnVmware() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("createVmfsOnVmware", Map<String,.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: createVmfsByServiceLevel(Map<String, Object> params, String objhostid)
     */
    @Test
    public void testCreateVmfsByServiceLevel() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("createVmfsByServiceLevel", Map<String,.class, String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: createVmfsByUnServiceLevel(Map<String, Object> params, String objhostid)
     */
    @Test
    public void testCreateVmfsByUnServiceLevel() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("createVmfsByUnServiceLevel", Map<String,.class, String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: checkToHost(String hostId)
     */
    @Test
    public void testCheckToHost() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("checkToHost", String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: checkOrCreateToHostGroup(String clusterObjectId)
     */
    @Test
    public void testCheckOrCreateToHostGroup() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("checkOrCreateToHostGroup", String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: checkToHostGroup(String clusterObjectId)
     */
    @Test
    public void testCheckToHostGroup() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("checkToHostGroup", String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: checkHostInHostGroup(String vmwareClusterObjectId, String dmeHostGroupId)
     */
    @Test
    public void testCheckHostInHostGroup() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("checkHostInHostGroup", String.class, String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: checkOrcreateToHostorHostGroup(Map<String, Object> params)
     */
    @Test
    public void testCheckOrcreateToHostorHostGroup() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("checkOrcreateToHostorHostGroup", Map<String,.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: getVolumeByName(String volumeName, String hostId, String hostGroupId, String serviceLevelId, String storageId, String poolRawId)
     */
    @Test
    public void testGetVolumeByName() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("getVolumeByName", String.class, String.class, String.class, String.class, String.class, String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: saveDmeVmwareRalation(Map<String, Object> volumeMap, Map<String, Object> dataStoreMap)
     */
    @Test
    public void testSaveDmeVmwareRalation() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("saveDmeVmwareRalation", Map<String,.class, Map<String,.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: mountVmfsToHost(Map<String, Object> params, String objhostid)
     */
    @Test
    public void testMountVmfsToHost() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("mountVmfsToHost", Map<String,.class, String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: mountVmfsToHostGroup(Map<String, Object> params, String objhostid)
     */
    @Test
    public void testMountVmfsToHostGroup() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("mountVmfsToHostGroup", Map<String,.class, String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: dmeVmwareRelationDbProcess(List<DmeVmwareRelation> relationList, String storeType)
     */
    @Test
    public void testDmeVmwareRelationDbProcess() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("dmeVmwareRelationDbProcess", List<DmeVmwareRelation>.class, String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: unmountVmfs(String dsObjId, Map<String, Object> params)
     */
    @Test
    public void testUnmountVmfs() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("unmountVmfs", String.class, Map<String,.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: queryVmfsById(String volumeId)
     */
    @Test
    public void testQueryVmfsById() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("queryVmfsById", String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: unmountHostGetTaskId(Map<String, Object> params)
     */
    @Test
    public void testUnmountHostGetTaskId() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("unmountHostGetTaskId", Map<String,.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: unmountHostGroupGetTaskId(Map<String, Object> params)
     */
    @Test
    public void testUnmountHostGroupGetTaskId() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("unmountHostGroupGetTaskId", Map<String,.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: volumeDeleteGetTaskId(Map<String, Object> params)
     */
    @Test
    public void testVolumeDeleteGetTaskId() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("volumeDeleteGetTaskId", Map<String,.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: hostUnmapping(Map<String, Object> params)
     */
    @Test
    public void testHostUnmapping() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("hostUnmapping", Map<String,.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: hostGroupUnmapping(Map<String, Object> params)
     */
    @Test
    public void testHostGroupUnmapping() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("hostGroupUnmapping", Map<String,.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: volumeDelete(Map<String, Object> params)
     */
    @Test
    public void testVolumeDelete() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("volumeDelete", Map<String,.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: getDvrMap(List<DmeVmwareRelation> dvrlist)
     */
    @Test
    public void testGetDvrMap() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("getDvrMap", List<DmeVmwareRelation>.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: getStorNameMap(List<Storage> list)
     */
    @Test
    public void testGetStorNameMap() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("getStorNameMap", List<Storage>.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: getTaskId(ResponseEntity responseEntity)
     */
    @Test
    public void testGetTaskId() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("getTaskId", ResponseEntity.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: getDmeAttachHostGroupByVolumeId(String volumeId)
     */
    @Test
    public void testGetDmeAttachHostGroupByVolumeId() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("getDmeAttachHostGroupByVolumeId", String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: getDmeStorageIdsByStorageId(String storageId)
     */
    @Test
    public void testGetDmeStorageIdsByStorageId() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("getDmeStorageIdsByStorageId", String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: getVolumesByDmeStorageIds(List<String> dmeStorageIds)
     */
    @Test
    public void testGetVolumesByDmeStorageIds() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("getVolumesByDmeStorageIds", List<String>.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: getDmeHostByHostObjeId(String hostObjId)
     */
    @Test
    public void testGetDmeHostByHostObjeId() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("getDmeHostByHostObjeId", String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: getDmeHostGroupByClusterObjId(String clusterObjId)
     */
    @Test
    public void testGetDmeHostGroupByClusterObjId() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("getDmeHostGroupByClusterObjId", String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: dataStoreVmRelateHostOrCluster(String dsObjid, String hostObjId, String clusterObjId)
     */
    @Test
    public void testDataStoreVmRelateHostOrCluster() throws Exception {
 
/* 
try { 
   Method method = VmfsAccessServiceImpl.getClass().getMethod("dataStoreVmRelateHostOrCluster", String.class, String.class, String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

} 
