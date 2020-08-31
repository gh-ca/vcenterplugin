package com.dmeplugin.dmestore.utils;

import com.dmeplugin.dmestore.model.DatastoreInfo;
import com.dmeplugin.dmestore.services.EchoServiceImpl;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class VCSDKUtils {

    private final static Logger _logger = LoggerFactory.getLogger(EchoServiceImpl.class);

    public static DatastoreInfo getAllDataStores(){
        try {
            _logger.info("startme");
            URL serverUrl = new URL("https://10.143.132.248/sdk");
            ServiceInstance serviceInstance = new ServiceInstance(serverUrl, "administrator@vsphere.local", "Pbu4@123", true);

            ManagedEntity[] objs = new InventoryNavigator(serviceInstance.getRootFolder()).searchManagedEntities("Datastore");
            _logger.info("after");
            for (ManagedEntity managedEntity : objs) {
                _logger.info("in");
                _logger.info(managedEntity.getName());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
