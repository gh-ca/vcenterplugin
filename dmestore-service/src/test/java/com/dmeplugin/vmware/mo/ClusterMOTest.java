package com.dmeplugin.vmware.mo;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import com.dmeplugin.vmware.util.VmwareContext;
import com.dmeplugin.vmware.util.VmwareContextPool;
import com.vmware.vim25.ManagedObjectReference;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className ClusterMOTest
 * @description TODO
 * @date 2020/11/25 15:28
 */
public class ClusterMOTest {

    @InjectMocks
    ClusterMO clusterMO;

    @Before
    public void setUp() throws Exception {
        VmwareContext context = mock(VmwareContext.class);
        context.setPoolInfo(new VmwareContextPool(),"321");
        ManagedObjectReference managedObjectReference = mock(ManagedObjectReference.class);
        managedObjectReference.setType("321");
        managedObjectReference.setValue("321");
        clusterMO = new ClusterMO(context,managedObjectReference);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getHyperHostName() throws Exception {
        clusterMO.getHyperHostName();
    }

    @Test
    public void getDasConfig() {
    }

    @Test
    public void isHaEnabled() {
    }

    @Test
    public void setRestartPriorityForVm() {
    }

    @Test
    public void getHyperHostDatacenter() {
    }

    @Test
    public void getHyperHostOwnerResourcePool() {
    }

    @Test
    public void getHyperHostCluster() {
    }

    @Test
    public void listVmsOnHyperHost() {
    }

    @Test
    public void findVmOnHyperHost() {
    }

    @Test
    public void findVmOnPeerHyperHost() {
    }

    @Test
    public void getVmPropertiesOnHyperHost() {
    }

    @Test
    public void getDatastorePropertiesOnHyperHost() {
    }

    @Test
    public void createVm() {
    }

    @Test
    public void createBlankVm() {
    }

    @Test
    public void mountDatastore() {
    }

    @Test
    public void unmountDatastore() {
    }

    @Test
    public void findDatastore() {
    }

    @Test
    public void findDatastoreByName() {
    }

    @Test
    public void findDatastoreByExportPath() {
    }

    @Test
    public void findMigrationTarget() {
    }

    @Test
    public void isHyperHostConnected() {
    }

    @Test
    public void getHyperHostDefaultGateway() {
    }

    @Test
    public void getHyperHostResourceSummary() {
    }

    @Test
    public void getHyperHostNetworkSummary() {
    }

    @Test
    public void getHyperHostHardwareSummary() {
    }

    @Test
    public void getLicenseAssignmentManager() {
    }

    @Test
    public void getRecommendedDiskController() {
    }
}