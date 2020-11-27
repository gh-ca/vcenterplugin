package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.DMEServiceApplication;
import com.dmeplugin.dmestore.dao.BestPracticeCheckDao;
import com.dmeplugin.dmestore.model.BestPracticeBean;
import com.dmeplugin.dmestore.model.BestPracticeCheckRecordBean;
import com.dmeplugin.dmestore.services.bestpractice.*;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.dmeplugin.vmware.VCConnectionHelper;
import com.dmeplugin.vmware.VmwarePluginConnectionHelper;
import com.dmeplugin.vmware.mo.HostAdvanceOptionMO;
import com.dmeplugin.vmware.mo.HostKernelModuleSystemMO;
import com.dmeplugin.vmware.mo.HostMO;
import com.dmeplugin.vmware.mo.HostStorageSystemMO;
import com.dmeplugin.vmware.util.HostMOFactory;
import com.dmeplugin.vmware.util.VmwareContext;
import com.google.gson.Gson;
import com.vmware.vim25.ManagedObjectReference;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * BestPracticeProcessServiceImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十一月 13, 2020</pre>
 */
public class BestPracticeProcessServiceImplTest {
    private Gson gson = new Gson();
    @Mock
    private VCSDKUtils vcsdkUtils;
    @Mock
    private BestPracticeCheckDao bestPracticeCheckDao;
    @InjectMocks
    private BestPracticeProcessServiceImpl bestPracticeProcessService = new BestPracticeProcessServiceImpl();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        List<BestPracticeService> bestPracticeServices = new ArrayList<>();
        bestPracticeServices.add(spy(Vmfs3EnableBlockDeleteImpl.class));
        bestPracticeServices.add(spy(Vmfs3HardwareAcceleratedLockingImpl.class));
        bestPracticeServices.add(spy(VMFS3UseATSForHBOnVMFS5Impl.class));
        bestPracticeServices.add(spy(DiskSchedQuantumImpl.class));
        bestPracticeServices.add(spy(DiskDiskMaxIOSizeImpl.class));
        bestPracticeServices.add(spy(DataMoverHardwareAcceleratedInitImpl.class));
        bestPracticeServices.add(spy(DataMoverHardwareAcceleratedMoveImpl.class));
        bestPracticeServices.add(spy(LunQueueDepthForEmulexImpl.class));
        bestPracticeServices.add(spy(LunQueueDepthForQlogicImpl.class));
        bestPracticeServices.add(spy(JumboFrameMTUImpl.class));
        bestPracticeServices.add(spy(NumberOfVolumesInDatastoreImpl.class));
        bestPracticeServices.add(spy(Vmfs6AutoReclaimImpl.class));
        bestPracticeServices.add(spy(NMPPathSwitchPolicyImpl.class));
        bestPracticeProcessService.setBestPracticeServices(bestPracticeServices);
    }

    /**
     * Method: getCheckRecord()
     */
    @Test
    public void testGetCheckRecord() throws Exception {
        List<BestPracticeBean> hostBeanList = new ArrayList<>();
        BestPracticeBean bestPracticeBean = new BestPracticeBean();
        hostBeanList.add(bestPracticeBean);
        when(bestPracticeCheckDao.getRecordBeanByHostsetting(anyString())).thenReturn(hostBeanList);
        bestPracticeProcessService.getCheckRecord();
    }

    /**
     * Method: getCheckRecordBy()
     */
    @Test
    public void testGetCheckRecordBy() throws Exception{
        List<BestPracticeBean> list = new ArrayList<>();
        list.add(new BestPracticeBean());
        when(bestPracticeCheckDao.getRecordByPage("test", 0, 100)).thenReturn(list);
        bestPracticeProcessService.getCheckRecordBy("test", 0, 100);
    }

    /**
     * Method: check()
     */
    @Test
    public void testCheck() throws Exception{
        String objectId = "123";
        List<Map<String, String>> lists = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("hostId", objectId);
        map.put("objectId", "123");
        map.put("hostName", "testHost");
        lists.add(map);
        List<BestPracticeService> bestPracticeServiceList = bestPracticeProcessService.getBestPracticeServices();
        when(vcsdkUtils.findHostById(objectId)).thenReturn(gson.toJson(lists));
        when(vcsdkUtils.getAllHosts()).thenReturn(gson.toJson(lists));
        ManagedObjectReference mor = mock(ManagedObjectReference.class);
        VCConnectionHelper vcConnectionHelper = mock(VmwarePluginConnectionHelper.class);
        when(vcsdkUtils.getVcConnectionHelper()).thenReturn(vcConnectionHelper);
        when(vcConnectionHelper.objectId2Mor(objectId)).thenReturn(mor);
        VmwareContext context = mock(VmwareContext.class);
        when(vcConnectionHelper.getServerContext(objectId)).thenReturn(context);
        HostMO hostMo = mock(HostMO.class);
        HostMOFactory hostMOFactory = mock(HostMOFactory.class);
        for (BestPracticeService service : bestPracticeServiceList){
            when(service.getHostMoFactory()).thenReturn(hostMOFactory);
            when(hostMOFactory.build(context, mor)).thenReturn(hostMo);
        }
        HostAdvanceOptionMO hostAdvanceOptionMO = mock(HostAdvanceOptionMO.class);
        when(hostMo.getHostAdvanceOptionMo()).thenReturn(hostAdvanceOptionMO);
        when(hostAdvanceOptionMO.queryOptions(anyString())).thenReturn(new ArrayList<>());
        HostKernelModuleSystemMO hostKernelModuleSystemMO = mock(HostKernelModuleSystemMO.class);
        when(hostMo.getHostKernelModuleSystemMo()).thenReturn(hostKernelModuleSystemMO);
        when(hostKernelModuleSystemMO.queryConfiguredModuleOptionString(anyString())).thenReturn("aa=5");

        HostStorageSystemMO hostStorageSystemMo = mock(HostStorageSystemMO.class);
        when(hostMo.getHostStorageSystemMo()).thenReturn(hostStorageSystemMo);

        bestPracticeProcessService.check(objectId);
    }

    @Test
    public void testUpdate() throws Exception{
        String objectId = "123";
        List<Map<String, String>> lists = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("hostId", objectId);
        map.put("objectId", "123");
        map.put("hostName", "testHost");
        lists.add(map);
        when(vcsdkUtils.findHostById(objectId)).thenReturn(gson.toJson(lists));
        when(vcsdkUtils.getAllHosts()).thenReturn(gson.toJson(lists));
        bestPracticeProcessService.update(null,null);
        List<String> objectIds = new ArrayList<>();
        objectIds.add(objectId);
        bestPracticeProcessService.update(objectIds);
    }



} 
