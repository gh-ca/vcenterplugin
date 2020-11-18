package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.DMEServiceApplication;
import com.dmeplugin.dmestore.model.BestPracticeBean;
import com.dmeplugin.dmestore.model.BestPracticeCheckRecordBean;
import com.dmeplugin.dmestore.services.bestpractice.BestPracticeService;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
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
 * BestPracticeProcessServiceImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十一月 13, 2020</pre>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DMEServiceApplication.class)
public class BestPracticeProcessServiceImplTest {
    @Autowired
    private BestPracticeProcessServiceImpl bestPracticeProcessService;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getBestPracticeServices()
     */
    @Test
    public void testGetBestPracticeServices() throws Exception {
        List<BestPracticeService> list = bestPracticeProcessService.getBestPracticeServices();
        assert list.size() > 0;
    }

    /**
     * Method: setBestPracticeServices(List<BestPracticeService> bestPracticeServices)
     */
    @Test
    public void testSetBestPracticeServices() throws Exception {
        assert true;
    }

    /**
     * Method: getVcsdkUtils()
     */
    @Test
    public void testGetVcsdkUtils() throws Exception {
        assert null != bestPracticeProcessService.getVcsdkUtils();
    }

    /**
     * Method: setVcsdkUtils(VCSDKUtils vcsdkUtils)
     */
    @Test
    public void testSetVcsdkUtils() throws Exception {
        assert true;
    }

    /**
     * Method: getBestPracticeCheckDao()
     */
    @Test
    public void testGetBestPracticeCheckDao() throws Exception {
        assert null != bestPracticeProcessService.getBestPracticeCheckDao();
    }

    /**
     * Method: setBestPracticeCheckDao(BestPracticeCheckDao bestPracticeCheckDao)
     */
    @Test
    public void testSetBestPracticeCheckDao() throws Exception {
        assert true;
    }

    /**
     * Method: getCheckRecord()
     */
    @Test
    public void testGetCheckRecord() throws Exception {
        bestPracticeProcessService.getCheckRecord();
    }

    /**
     * Method: getCheckRecordBy(String hostSetting, int pageNo, int pageSize)
     */
    @Test
    public void testGetCheckRecordBy() throws Exception {
        String hostSetting = "Jumbo Frame (MTU)";
        bestPracticeProcessService.getCheckRecordBy(hostSetting, 0, 20);
    }


    /**
     * Method: check(String objectId)
     */
    @Test
    public void testCheck() throws Exception {
        bestPracticeProcessService.check(null);
    }

    /**
     * Method: update(List<String> objectIds)
     */
    @Test
    public void testUpdateObjectIds() throws Exception {
        bestPracticeProcessService.update(null);
        Map<String, String> map = bestPracticeProcessService.getBestPracticeCheckDao().getAllHostIds(0, 1);
        List<String> objectIds = new ArrayList<>();
        map.forEach((k, v) -> objectIds.add(k));
        bestPracticeProcessService.update(objectIds);
    }

    /**
     * Method: updateByCluster(String clusterobjectid)
     */
    @Test
    public void testUpdateByCluster() throws Exception {
        String clusterobjectid = "urn:vmomi:ClusterComputeResource:domain-c1087:674908e5-ab21-4079-9cb1-596358ee5dd1";
        bestPracticeProcessService.updateByCluster(clusterobjectid);
    }

    /**
     * Method: update(List<String> objectIds, String hostSetting)
     */
    @Test
    public void testUpdateForObjectIdsHostSetting() throws Exception {
        List<BestPracticeCheckRecordBean> checkRecordBeans = bestPracticeProcessService.getCheckRecord();
        if(checkRecordBeans.size() > 0){
            BestPracticeCheckRecordBean checkRecordBean = checkRecordBeans.get(0);
            List<String> objectIds = new ArrayList<>();
            for (BestPracticeBean bestPracticeBean : checkRecordBean.getHostList()) {
                objectIds.add(bestPracticeBean.getHostObjectId());
            }
            bestPracticeProcessService.update(objectIds, checkRecordBean.getHostSetting());
        }

        assert true;
    }


    /**
     * Method: bachDbProcess(Map<String, List<BestPracticeBean>> map)
     */
    @Test
    public void testBachDbProcess() throws Exception {
        assert true;
    }

} 
