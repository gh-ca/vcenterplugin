package com.dmeplugin.dmestore.utils;

import com.dmeplugin.dmestore.model.DatastoreInfo;
import com.dmeplugin.dmestore.services.EchoServiceImpl;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.vmware.connection.BasicConnection;
import com.vmware.connection.Connection;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.Datastore;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VCSDKUtils {

    private final static Logger _logger = LoggerFactory.getLogger(EchoServiceImpl.class);

    public final static String STORE_TYPE_VMFS = "VMFS";
    public final static String STORE_TYPE_NFS = "NFS";

    public static String getAllVmfsDataStores(String storeType) throws Exception{
        String listStr = "";
        ConnectedVimServiceBase connectedVimServiceBase = null;
        try {
            connectedVimServiceBase=new ConnectedVimServiceBase();
            Connection connection=new BasicConnection();
            connection.setUrl("https://10.143.132.248:443/sdk");
            connection.setUsername("administrator@vsphere.local");
            connection.setPassword("Pbu4@123");
            connectedVimServiceBase.setConnection(connection);
            connectedVimServiceBase.connect();

            DatastoreVimBase datastoreVimBase=new DatastoreVimBase(connectedVimServiceBase);

            List<DatastoreSummary> datastoreSummaryList=datastoreVimBase.getAllDatastoreSummary();
            if(datastoreSummaryList!=null && datastoreSummaryList.size()>0) {
                List<DatastoreSummary> lists = new ArrayList<>();
                for (DatastoreSummary datastoreSummary : datastoreSummaryList) {
                    if(StringUtils.isEmpty(storeType)){
                        lists.add(datastoreSummary);
                    }else if(datastoreSummary.getType().equals(storeType)){
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
            _logger.error("vmware error:",e);
            throw e;
        }finally {
            if(connectedVimServiceBase!=null) {
                connectedVimServiceBase.disconnect();
            }
        }
        return listStr;
    }

    public static void main(String[] args) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        try {
            String listStr = VCSDKUtils.getAllVmfsDataStores(null);
            _logger.info("Vmfs listStr==" + listStr);
            listStr = VCSDKUtils.getAllVmfsDataStores(VCSDKUtils.STORE_TYPE_VMFS);
            _logger.info("Vmfs listStr==" + listStr);
            listStr = VCSDKUtils.getAllVmfsDataStores(VCSDKUtils.STORE_TYPE_NFS);
            _logger.info("Vmfs listStr==" + listStr);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
