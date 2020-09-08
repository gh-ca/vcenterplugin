package com.dmeplugin.dmestore.utils;

import com.dmeplugin.dmestore.services.EchoServiceImpl;
import com.dmeplugin.vmware.mo.*;
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
            List<Pair<ManagedObjectReference, String>> dss = rootFsMO.getAllDatastoreOnRootFs();
            if(dss!=null && dss.size()>0) {
                List<DatastoreSummary> lists = new ArrayList<>();

                for (Pair<ManagedObjectReference, String> ds : dss) {
                    DatastoreMO ds1 = new DatastoreMO(vmwareContext, ds.first());
                    if (StringUtils.isEmpty(storeType)) {
                        lists.add(ds1.getSummary());
                    } else if (ds1.getSummary().getType().equals(storeType)) {
                        lists.add(ds1.getSummary());
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
        }
        return listStr;
    }

    //得到所有主机的ID与name
    public static String getAllHosts() throws Exception {
        String listStr = "";
        try {
            VmwareContext vmwareContext= TestVmwareContextFactory.getContext("10.143.132.248","administrator@vsphere.local","Pbu4@123");

            RootFsMO rootFsMO=new RootFsMO(vmwareContext,vmwareContext.getRootFolder());
            List<Pair<ManagedObjectReference, String>> hosts = rootFsMO.getAllHostOnRootFs();
            if(hosts!=null && hosts.size()>0) {
                List<Map<String, String>> lists = new ArrayList<>();
                for (Pair<ManagedObjectReference, String> host : hosts) {
                    HostMO host1 = new HostMO(vmwareContext, host.first());

                    Map<String, String> map = new HashMap<>();
                    map.put("hostId", host1.getMor().getValue());
                    map.put("hostName", host1.getName());
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
        }
        return listStr;
    }

    //得到所有集群的id与name
    public static String getAllClusters() throws Exception {
        String listStr = "";
        try {
            VmwareContext vmwareContext= TestVmwareContextFactory.getContext("10.143.132.248","administrator@vsphere.local","Pbu4@123");

            RootFsMO rootFsMO=new RootFsMO(vmwareContext,vmwareContext.getRootFolder());
            List<Pair<ManagedObjectReference, String>> cls = rootFsMO.getAllClusterOnRootFs();
            if(cls!=null && cls.size()>0) {
                List<Map<String, String>> lists = new ArrayList<>();
                for (Pair<ManagedObjectReference, String> cl : cls) {
                    ClusterMO cl1 = new ClusterMO(vmwareContext, cl.first());

                    Map<String, String> map = new HashMap<>();
                    map.put("clusterId", cl1.getMor().getValue());
                    map.put("clusterName", cl1.getName());
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
        }
        return listStr;
    }

    //得到指定集群下的所有主机
    public static String getHostsOnCluster(String clusterName) throws Exception {
        String listStr = "";
        try {
            VmwareContext vmwareContext= TestVmwareContextFactory.getContext("10.143.132.248","administrator@vsphere.local","Pbu4@123");

            RootFsMO rootFsMO=new RootFsMO(vmwareContext,vmwareContext.getRootFolder());

            List<Pair<ManagedObjectReference, String>> cls = rootFsMO.getAllClusterOnRootFs();
            if(cls!=null && cls.size()>0) {
                for (Pair<ManagedObjectReference, String> cl : cls) {
                    ClusterMO cl1 = new ClusterMO(vmwareContext, cl.first());
                    if(cl1.getName().equals(clusterName)){
                        List<Pair<ManagedObjectReference, String>> hosts = cl1.getClusterHosts();

                        if(hosts!=null && hosts.size()>0){
                            List<Map<String, String>> lists = new ArrayList<>();
                            for (Pair<ManagedObjectReference, String> host : hosts) {
                                HostMO host1 = new HostMO(vmwareContext, host.first());

                                Map<String, String> map = new HashMap<>();
                                map.put("hostId", host1.getMor().getValue());
                                map.put("hostName", host1.getName());
                                lists.add(map);
                            }
                            if (lists.size() > 0) {
                                Gson gson = new Gson();
                                listStr = gson.toJson(lists);
                            }
                        }

                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("vmware error:", e);
            throw e;
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
//            String listStr = VCSDKUtils.getAllVmfsDataStores(null);
//            _logger.info("Vmfs listStr==" + listStr);
//            listStr = VCSDKUtils.getAllVmfsDataStores(ToolUtils.STORE_TYPE_VMFS);
//            _logger.info("Vmfs listStr==" + listStr);
//            listStr = VCSDKUtils.getAllVmfsDataStores(ToolUtils.STORE_TYPE_NFS);
//            _logger.info("Vmfs listStr==" + listStr);

            _logger.info("Vmfs getAllClusters==" + VCSDKUtils.getHostsOnCluster("pbu4test"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

