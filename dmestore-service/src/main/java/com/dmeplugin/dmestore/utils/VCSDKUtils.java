package com.dmeplugin.dmestore.utils;

import com.dmeplugin.dmestore.services.EchoServiceImpl;
import com.google.gson.Gson;
import com.vmware.connection.BasicConnection;
import com.vmware.connection.Connection;
import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.HostListSummary;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VCSDKUtils {

    private final static Logger _logger = LoggerFactory.getLogger(EchoServiceImpl.class);

    public static String getAllVmfsDataStores(String storeType) throws Exception {
        String listStr = "";
        ConnectedVimServiceBase connectedVimServiceBase = null;
        try {
            connectedVimServiceBase = new ConnectedVimServiceBase();
            Connection connection = new BasicConnection();
            connection.setUrl("https://10.143.132.248:443/sdk");
            connection.setUsername("administrator@vsphere.local");
            connection.setPassword("Pbu4@123");
            connectedVimServiceBase.setConnection(connection);
            connectedVimServiceBase.connect();

            DatastoreVimBase datastoreVimBase = new DatastoreVimBase(connectedVimServiceBase);

            List<DatastoreSummary> datastoreSummaryList = datastoreVimBase.getAllDatastoreSummary();
            if (datastoreSummaryList != null && datastoreSummaryList.size() > 0) {
                List<DatastoreSummary> lists = new ArrayList<>();
                for (DatastoreSummary datastoreSummary : datastoreSummaryList) {
                    if (StringUtils.isEmpty(storeType)) {
                        lists.add(datastoreSummary);
                    } else if (datastoreSummary.getType().equals(storeType)) {
                        lists.add(datastoreSummary);
                    }
                }
                if (lists.size() > 0) {
                    Gson gson = new Gson();
                    listStr = gson.toJson(lists);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("vmware error:", e);
            throw e;
        } finally {
            if (connectedVimServiceBase != null) {
                connectedVimServiceBase.disconnect();
            }
        }
        return listStr;
    }

    public static String getAllHosts() throws Exception {
        String listStr = "";
        ConnectedVimServiceBase connectedVimServiceBase = null;
        try {
            connectedVimServiceBase = new ConnectedVimServiceBase();
            Connection connection = new BasicConnection();
            connection.setUrl("https://10.143.132.248:443/sdk");
            connection.setUsername("administrator@vsphere.local");
            connection.setPassword("Pbu4@123");
            connectedVimServiceBase.setConnection(connection);
            connectedVimServiceBase.connect();

            HostSystemVimBase hostSystemVimBase = new HostSystemVimBase(connectedVimServiceBase);

            List<HostListSummary> hostListSummaryList = hostSystemVimBase.getAllHostSummary();
            if (hostListSummaryList != null && hostListSummaryList.size() > 0) {
                List<Map<String,String>> lists = new ArrayList<>();
                for(HostListSummary hostListSummary:hostListSummaryList){
                    Map<String,String> map = new HashMap<>();
                    map.put("hostId",hostListSummary.getHost().getValue());
                    map.put("hostName",hostListSummary.getConfig().getName());
                    lists.add(map);
                }
                if(lists.size()>0) {
                    Gson gson = new Gson();
                    listStr = gson.toJson(lists);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("vmware error:", e);
            throw e;
        } finally {
            if (connectedVimServiceBase != null) {
                connectedVimServiceBase.disconnect();
            }
        }
        return listStr;
    }



    public static void main(String[] args) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        try {
//            String listStr = VCSDKUtils.getAllVmfsDataStores(null);
//            _logger.info("Vmfs listStr==" + listStr);
//            listStr = VCSDKUtils.getAllVmfsDataStores(ToolUtils.STORE_TYPE_VMFS);
//            _logger.info("Vmfs listStr==" + listStr);
//            listStr = VCSDKUtils.getAllVmfsDataStores(ToolUtils.STORE_TYPE_NFS);
//            _logger.info("Vmfs listStr==" + listStr);

            _logger.info("Vmfs getAllHosts==" + VCSDKUtils.getAllHosts());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
