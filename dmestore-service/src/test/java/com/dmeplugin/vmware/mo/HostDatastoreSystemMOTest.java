package com.dmeplugin.vmware.mo;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.dmeplugin.vmware.util.VmwareClient;
import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.CustomFieldDef;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.VimPortType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName HostDatastoreSystemMOTest.java
 * @Description TODO
 * @createTime 2020年12月02日 16:01:00
 */
public class HostDatastoreSystemMOTest {
    @Mock
    private VmwareContext context;

    @Mock
    private ManagedObjectReference mor;

    private VmwareClient vmwareClient;

    private VimPortType service;

    @InjectMocks
    private HostDatastoreSystemMO hostDatastoreSystemMO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        vmwareClient = mock(VmwareClient.class);
        when(context.getVimClient()).thenReturn(vmwareClient);

        service = mock(VimPortType.class);
        when(context.getService()).thenReturn(service);
    }

    @Test
    public void findDatastore() throws Exception {
        String name = "13";
        ServiceContent serviceContent = mock(ServiceContent.class);
        when(context.getServiceContent()).thenReturn(serviceContent);
        ManagedObjectReference getCustomFieldsManager = mock(ManagedObjectReference.class);
        when(serviceContent.getCustomFieldsManager()).thenReturn(getCustomFieldsManager);
        List<CustomFieldDef> customFieldDefList = new ArrayList<>();
        CustomFieldDef customFieldDef = mock(CustomFieldDef.class);
        customFieldDefList.add(customFieldDef);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("field"))).thenReturn(customFieldDefList);
        String fieldName = "cloud.uuid";
        String morType = "Datastore";
        int key = 10;
        when(customFieldDef.getName()).thenReturn(fieldName);
        when(customFieldDef.getManagedObjectType()).thenReturn(morType);
        when(customFieldDef.getKey()).thenReturn(key);

        hostDatastoreSystemMO.findDatastore(name);
    }

    @Test
    public void queryVmfsDatastoreExpandOptions() throws Exception {
    }

    @Test
    public void expandVmfsDatastore() throws Exception {
    }

    @Test
    public void queryAvailableDisksForVmfs() throws Exception {
    }

    @Test
    public void createVmfsDatastore() throws Exception {
    }

    @Test
    public void deleteDatastore() throws Exception {
    }

    @Test
    public void createNfsDatastore() throws Exception {
    }

    @Test
    public void getDatastores() throws Exception {
    }

    @Test
    public void getDatastoreInfo() throws Exception {
    }

    @Test
    public void getNasDatastoreInfo() throws Exception {
    }

    @Test
    public void getDatastorePropertiesOnHostDatastoreSystem() throws Exception {
    }

    @Test
    public void unmapVmfsVolumeExTask() throws Exception {
    }
}