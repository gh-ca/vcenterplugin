package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.dao.DmeVmwareRalationDao;
import com.dmeplugin.dmestore.entity.DmeVmwareRelation;
import com.dmeplugin.dmestore.model.Storage;
import com.dmeplugin.dmestore.utils.ToolUtils;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
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
    String url;
    @Mock
    private DmeVmwareRalationDao dmeVmwareRalationDao;
    @Mock
    private DmeAccessService dmeAccessService;
    @Mock
    private DmeStorageService dmeStorageService;
    @Mock
    private VCSDKUtils vcsdkUtils;
    @InjectMocks
    private VmfsAccessService vmfsAccessService = new VmfsAccessServiceImpl();
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void listVmfs() throws Exception{
        List<DmeVmwareRelation> dvrlist = new ArrayList<>();
        DmeVmwareRelation dmeVmwareRelation = new DmeVmwareRelation();
        dmeVmwareRelation.setStoreId("13232");
        dvrlist.add(dmeVmwareRelation);
        when(dmeVmwareRalationDao.getDmeVmwareRelation(ToolUtils.STORE_TYPE_VMFS)).thenReturn(dvrlist);
        List<Storage> list = new ArrayList<>();
        Storage storageObj = new Storage();
        storageObj.setName("Huawei.Storage");
        list.add(storageObj);
        when(dmeStorageService.getStorages()).thenReturn(list);
        String listStr = "";
        when(vcsdkUtils.getAllVmfsDataStoreInfos(ToolUtils.STORE_TYPE_VMFS)).thenReturn(listStr);
        vcsdkUtils.getAllVmfsDataStoreInfos(ToolUtils.STORE_TYPE_VMFS);

        vmfsAccessService.listVmfs();
    }

    @Test
    public void listVmfsPerformance() {
    }

    @Test
    public void createVmfs() {
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