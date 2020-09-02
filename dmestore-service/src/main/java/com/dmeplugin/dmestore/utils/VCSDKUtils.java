package com.dmeplugin.dmestore.utils;

import com.dmeplugin.dmestore.model.DatastoreInfo;
import com.dmeplugin.dmestore.services.EchoServiceImpl;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.mo.Datastore;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VCSDKUtils {

    private final static Logger _logger = LoggerFactory.getLogger(EchoServiceImpl.class);

    private final static String STORE_TYPE_VMFS = "VMFS";
    private final static String STORE_TYPE_NFS = "NFS";

    public static String getAllVmfsDataStores(){
        String listStr = "";
        try {
            _logger.info("startme");
            URL serverUrl = new URL("https://10.143.132.248/sdk");
            ServiceInstance serviceInstance = new ServiceInstance(serverUrl, "administrator@vsphere.local", "Pbu4@123", true);

            ManagedEntity[] objs = new InventoryNavigator(serviceInstance.getRootFolder()).searchManagedEntities("Datastore");
            _logger.info("after");
            if(objs!=null && objs.length>0) {
                List<DatastoreSummary> lists = new ArrayList<>();
                for (ManagedEntity managedEntity : objs) {
                    _logger.info(managedEntity.getName());
                    Datastore da = (Datastore) managedEntity;
                    DatastoreSummary datastoreSummary = da.getSummary();
                    if(datastoreSummary.getType().equals(STORE_TYPE_VMFS)){
                        lists.add(datastoreSummary);
                    }
                }
                if(lists.size()>0){
                    Gson gson = new Gson();
                    listStr = gson.toJson(lists);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listStr;
    }
}
