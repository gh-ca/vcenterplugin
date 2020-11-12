package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.dao.DmeInfoDao;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DmeAccessServiceImpl Tester.
 *
 * @author wangxiangyong
 * @version 1.0
 * @since <pre>十一月 11, 2020</pre>
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = DMEServiceApplication.class)
public class DmeAccessServiceImplTest {
    private DmeAccessServiceImpl dmeAccessService;

    @Before
    public void before() throws Exception {
        dmeAccessService = new DmeAccessServiceImpl();
        DmeInfoDao dmeInfoDao = new DmeInfoDao();
        dmeInfoDao.setUrl("jdbc:h2://C:/ProgramData/VMware/vCenterServer/runtime/");
        dmeAccessService.setDmeInfoDao(dmeInfoDao);
        VmfsAccessServiceImpl vmfsAccessService = new VmfsAccessServiceImpl();
        vmfsAccessService.setVcsdkUtils(new VCSDKUtils());
        dmeAccessService.setVmfsAccessService(vmfsAccessService);
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: accessDme(Map<String, Object> params)
     */
    @Test
    public void testAccessDme() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userName", "evuser");
        params.put("password", "Pbu4@123");
        params.put("hostIp", "10.143.133.199");
        params.put("hostPort", "26335");

        dmeAccessService.accessDme(params);
    }

    /**
     * Method: refreshDme()
     */
    @Test
    public void testRefreshDme() throws Exception {
        dmeAccessService.refreshDme();
    }

    /**
     * Method: access(String url, HttpMethod method, String requestBody)
     */
    @Test
    public void testAccess() throws Exception {
        String url = "/rest/blockservice/v1/volumes?limit=1";
        dmeAccessService.access(url, HttpMethod.GET, null);
    }

    /**
     * Method: accessByJson(String url, HttpMethod method, String jsonBody)
     */
    @Test
    public void testAccessByJson() throws Exception {
        String url = "/rest/blockservice/v1/volumes?limit=1";
        dmeAccessService.accessByJson(url, HttpMethod.GET, null);
    }

    /**
     * Method: getWorkLoads(String storageId)
     */
    @Test
    public void testGetWorkLoads() throws Exception {
        String storageId = "b94bff9d-0dfb-11eb-bd3d-0050568491c9";
        dmeAccessService.getWorkLoads(storageId);

    }

    /**
     * Method: getDmeHosts(String hostIp)
     */
    @Test
    public void testGetDmeHosts() throws Exception {
        String hostIp = "10.143.133.17";
        dmeAccessService.getDmeHosts(hostIp);
    }

    /**
     * Method: getDmeHostInitiators(String hostId)
     */
    @Test
    public void testGetDmeHostInitiators() throws Exception {
        String hostId = "9cbd24b5-fb5b-4ad9-9393-cf05b9b97339";
        dmeAccessService.getDmeHostInitiators(hostId);
    }

    /**
     * Method: getDmeHostGroups(String hostGroupName)
     */
    @Test
    public void testGetDmeHostGroups() throws Exception {
        String hostGroupName = "domain-c1087";
        dmeAccessService.getDmeHostGroups(hostGroupName);
    }

    /**
     * Method: createHost(Map<String, Object> params)
     */
    @Test
    public void testCreateHost() throws Exception {
        return;
    }

    /**
     * Method: createHostGroup(Map<String, Object> params)
     */
    @Test
    public void testCreateHostGroup() throws Exception {
        return;
    }

    /**
     * Method: setDmeInfoDao(DmeInfoDao dmeInfoDao)
     */
    @Test
    public void testSetDmeInfoDao() throws Exception {
        dmeAccessService.setDmeInfoDao(new DmeInfoDao());
    }

    /**
     * Method: setVmfsAccessService(VmfsAccessService vmfsAccessService)
     */
    @Test
    public void testSetVmfsAccessService() throws Exception {
        return;
    }

    /**
     * Method: setDmeNfsAccessService(DmeNFSAccessService dmeNfsAccessService)
     */
    @Test
    public void testSetDmeNfsAccessService() throws Exception {
        return;
    }

    /**
     * Method: setVcsdkUtils(VCSDKUtils vcsdkUtils)
     */
    @Test
    public void testSetVcsdkUtils() throws Exception {
        return;
    }

    /**
     * Method: setScheduleDao(ScheduleDao scheduleDao)
     */
    @Test
    public void testSetScheduleDao() throws Exception {
        return;
    }

    /**
     * Method: getTaskService()
     */
    @Test
    public void testGetTaskService() throws Exception {
        return;
    }

    /**
     * Method: setTaskService(TaskService taskService)
     */
    @Test
    public void testSetTaskService() throws Exception {
        return;
    }

    /**
     * Method: getDmeHost(String hostId)
     */
    @Test
    public void testGetDmeHost() throws Exception {
        String hostId = "9cbd24b5-fb5b-4ad9-9393-cf05b9b97339";
        dmeAccessService.getDmeHost(hostId);
    }

    /**
     * Method: scanDatastore(String storageType)
     */
    @Test
    public void testScanDatastore() throws Exception {
        dmeAccessService.scanDatastore("VMFS");
    }

    /**
     * Method: configureTaskTime(Integer taskId, String taskCron)
     */
    @Test
    public void testConfigureTaskTime() throws Exception {
        return;
    }

    /**
     * Method: getDmeHostGroup(String hostGroupId)
     */
    @Test
    public void testGetDmeHostGroup() throws Exception {
        String hostGroupId = "123cfad3-2ddf-4b47-9772-675c6ad770b8";
        dmeAccessService.getDmeHostGroup(hostGroupId);
    }

    /**
     * Method: getDmeHostInHostGroup(String hostGroupId)
     */
    @Test
    public void testGetDmeHostInHostGroup() throws Exception {
        String hostGroupId = "123cfad3-2ddf-4b47-9772-675c6ad770b8";
        dmeAccessService.getDmeHostInHostGroup(hostGroupId);
    }

    /**
     * Method: deleteVolumes(List<String> ids)
     */
    @Test
    public void testDeleteVolumes() throws Exception {
        List<String> ids = new ArrayList<>();
        ids.add("59582fc9-d05c-4e28-8c50-da32d2a49e1b");
        dmeAccessService.deleteVolumes(ids);
    }

    /**
     * Method: unMapHost(String hostId, List<String> ids)
     */
    @Test
    public void testUnMapHost() throws Exception {
        String hostId = "9cbd24b5-fb5b-4ad9-9393-cf05b9b97339";
        List<String> ids = new ArrayList<>();
        ids.add("59582fc9-d05c-4e28-8c50-da32d2a49e1b");
        dmeAccessService.unMapHost(hostId, ids);
    }

    /**
     * Method: hostMapping(String hostId, List<String> volumeIds)
     */
    @Test
    public void testHostMapping() throws Exception {
        String hostId = "9cbd24b5-fb5b-4ad9-9393-cf05b9b97339";
        List<String> volumeIds = new ArrayList<>();
        volumeIds.add("589e368c-6f08-45c8-a75c-b4dc28a6dcca");
        dmeAccessService.hostMapping(hostId, volumeIds);
    }


    /**
     * Method: login(Map<String, Object> params)
     */
    @Test
    public void testLogin() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userName", "evuser");
        params.put("password", "Pbu4@123");
        params.put("hostIp", "10.143.133.199");
        params.put("hostPort", "26335");
        Class class1 = dmeAccessService.getClass();
        Method format = class1.getDeclaredMethod("login", Map.class);
        //设为可见
        format.setAccessible(true);
        format.invoke(dmeAccessService, params);
    }

    /**
     * Method: getHeaders()
     */
    @Test
    public void testGetHeaders() throws Exception {
        Class class1 = dmeAccessService.getClass();
        Method format = class1.getDeclaredMethod("getHeaders");
        //设为可见
        format.setAccessible(true);
        format.invoke(dmeAccessService);
    }

    /**
     * Method: iniLogin()
     */
    @Test
    public void testIniLogin() throws Exception {
        Class class1 = dmeAccessService.getClass();
        Method format = class1.getDeclaredMethod("iniLogin");
        //设为可见
        format.setAccessible(true);
        format.invoke(dmeAccessService);
    }

} 
