package com.dmeplugin.dmestore.utils;

import com.dmeplugin.dmestore.services.EchoServiceImpl;
import com.dmeplugin.vmware.mo.DatacenterMO;
import com.dmeplugin.vmware.mo.DatastoreMO;
import com.dmeplugin.vmware.mo.HostMO;
import com.dmeplugin.vmware.mo.RootFsMO;
import com.dmeplugin.vmware.util.Pair;
import com.dmeplugin.vmware.util.TestVmwareContextFactory;
import com.dmeplugin.vmware.util.VmwareContext;
import com.google.gson.Gson;
import com.vmware.connection.BasicConnection;
import com.vmware.connection.Connection;
import com.vmware.vim25.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.*;

public class VCSDKUtils {

    private final static Logger _logger = LoggerFactory.getLogger(EchoServiceImpl.class);

    //得到所有存储的Summary
    public static String getAllVmfsDataStores(String storeType) throws Exception {
        String listStr = "";
        try {
            VmwareContext vmwareContext= TestVmwareContextFactory.getContext("10.143.132.248","administrator@vsphere.local","Pbu4@123");

            RootFsMO rootFsMO=new RootFsMO(vmwareContext,vmwareContext.getRootFolder());
            List<Pair<ManagedObjectReference, String>> dcs = rootFsMO.getAllDatacenterOnRootFs();
            if(dcs!=null && dcs.size()>0) {
                List<DatastoreSummary> lists = new ArrayList<>();

                for (Pair<ManagedObjectReference, String> dc : dcs) {
                    DatacenterMO dc1 = new DatacenterMO(vmwareContext, dc.first());
                    System.out.println(dc1.getName());

                    List<Pair<ManagedObjectReference, String>> dss = dc1.getAllDatastoreOnDatacenter();
                    if (dss != null && dss.size() > 0) {
                        for (Pair<ManagedObjectReference, String> ds : dss) {
                            DatastoreMO ds1 = new DatastoreMO(vmwareContext, ds.first());
                            if (StringUtils.isEmpty(storeType)) {
                                lists.add(ds1.getSummary());
                            } else if (ds1.getSummary().getType().equals(storeType)) {
                                lists.add(ds1.getSummary());
                            }

                        }
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
//            if (connectedVimServiceBase != null) {
//                connectedVimServiceBase.disconnect();
//            }
        }
        return listStr;
    }

    //得到所有主机的ID与name
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
                List<Map<String, String>> lists = new ArrayList<>();
                for (HostListSummary hostListSummary : hostListSummaryList) {
                    Map<String, String> map = new HashMap<>();
                    map.put("hostId", hostListSummary.getHost().getValue());
                    map.put("hostName", hostListSummary.getConfig().getName());
                    lists.add(map);
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

    //得到所有集群的id与name
    public static String getAllClusters() throws Exception {
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

            ClusterVimBase clusterVimBase = new ClusterVimBase(connectedVimServiceBase);

            Map<String, ManagedObjectReference> clusters = clusterVimBase.getClusters();
            List<ManagedObjectReference> clusterlist = new ArrayList<>(clusters.values());
            if (clusterlist != null && clusterlist.size() > 0) {
                List<Map<String, String>> lists = new ArrayList<>();
                for (ManagedObjectReference mr : clusterlist) {
                    List<ManagedObjectReference> tmp_clusterlist = new ArrayList<>();
                    tmp_clusterlist.add(mr);
                    Collection<Map<String, Object>> restcollection = connectedVimServiceBase.getMOREFs.entityProps(tmp_clusterlist, new String[]{"name"}).values();

                    for (Map<String, Object> clustermap : restcollection) {
                        String clusterName = (String) clustermap.get("name");

                        Map<String, String> map = new HashMap<>();
                        map.put("clusterId", mr.getValue());
                        map.put("clusterName", clusterName);
                        lists.add(map);
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

    public static void renameDataStore(String newName) {
        //拿到对应的datastore 然后修改名字
        //如何获取指定的dataStore
        _logger.info("start rename DataStore");
    }

    public static void main(String[] args) {
        try {
            String listStr = VCSDKUtils.getAllVmfsDataStores(null);
            _logger.info("Vmfs listStr==" + listStr);
            listStr = VCSDKUtils.getAllVmfsDataStores(ToolUtils.STORE_TYPE_VMFS);
            _logger.info("Vmfs listStr==" + listStr);
            listStr = VCSDKUtils.getAllVmfsDataStores(ToolUtils.STORE_TYPE_NFS);
            _logger.info("Vmfs listStr==" + listStr);

//            _logger.info("Vmfs getAllClusters==" + VCSDKUtils.getAllClusters());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

