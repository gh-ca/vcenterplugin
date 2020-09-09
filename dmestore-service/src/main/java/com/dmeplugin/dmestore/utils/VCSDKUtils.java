package com.dmeplugin.dmestore.utils;

import com.dmeplugin.dmestore.services.EchoServiceImpl;
import com.dmeplugin.vmware.autosdk.SessionHelper;
import com.dmeplugin.vmware.autosdk.TaggingWorkflow;
import com.dmeplugin.vmware.mo.*;
import com.dmeplugin.vmware.util.Pair;
import com.dmeplugin.vmware.util.TestVmwareContextFactory;
import com.dmeplugin.vmware.util.VmwareContext;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vmware.cis.tagging.TagModel;
import com.vmware.vapi.std.DynamicID;
import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.HostScsiDisk;
import com.vmware.vim25.ManagedObjectReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VCSDKUtils {

    private final static Logger _logger = LoggerFactory.getLogger(EchoServiceImpl.class);

    //得到所有存储的Summary
    public static String getAllVmfsDataStores(String storeType) throws Exception {
        String listStr = "";
        try {
            VmwareContext vmwareContext = TestVmwareContextFactory.getContext("10.143.132.248", "administrator@vsphere.local", "Pbu4@123");

            RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
            List<Pair<ManagedObjectReference, String>> dss = rootFsMO.getAllDatastoreOnRootFs();
            if (dss != null && dss.size() > 0) {
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
            VmwareContext vmwareContext = TestVmwareContextFactory.getContext("10.143.132.248", "administrator@vsphere.local", "Pbu4@123");

            RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
            List<Pair<ManagedObjectReference, String>> hosts = rootFsMO.getAllHostOnRootFs();
            if (hosts != null && hosts.size() > 0) {
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
            VmwareContext vmwareContext = TestVmwareContextFactory.getContext("10.143.132.248", "administrator@vsphere.local", "Pbu4@123");

            RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
            List<Pair<ManagedObjectReference, String>> cls = rootFsMO.getAllClusterOnRootFs();
            if (cls != null && cls.size() > 0) {
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
            VmwareContext vmwareContext = TestVmwareContextFactory.getContext("10.143.132.248", "administrator@vsphere.local", "Pbu4@123");

            RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());

            List<Pair<ManagedObjectReference, String>> cls = rootFsMO.getAllClusterOnRootFs();
            if (cls != null && cls.size() > 0) {
                for (Pair<ManagedObjectReference, String> cl : cls) {
                    ClusterMO cl1 = new ClusterMO(vmwareContext, cl.first());
                    if (cl1.getName().equals(clusterName)) {
                        List<Pair<ManagedObjectReference, String>> hosts = cl1.getClusterHosts();

                        if (hosts != null && hosts.size() > 0) {
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

    public static String renameDataStore(String oldName, String newName) throws Exception {

        String result = "success";
        _logger.info("==start rename DataStore==");
        try {
            VmwareContext vmwareContext = TestVmwareContextFactory.getContext("10.143.132.248", "administrator@vsphere.local", "Pbu4@123");
            DatastoreMO dsMo = new DatastoreMO(vmwareContext, new DatacenterMO(vmwareContext, "Datacenter").findDatastore(oldName));
            dsMo.renameDatastore(newName);
            _logger.info("==end rename DataStore==");
        } catch (Exception e) {
            result = "failed";
            _logger.error("vmware error:", e);
            throw e;
        } finally {
            return result;
        }
    }

    public static void hostRescanVmfs(String hostIp) throws Exception {
        try {
            VmwareContext vmwareContext = TestVmwareContextFactory.getContext("10.143.132.248", "administrator@vsphere.local", "Pbu4@123");
            RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
            List<Pair<ManagedObjectReference, String>> hosts = rootFsMO.getAllHostOnRootFs();
            if (hosts != null && hosts.size() > 0) {
                for (Pair<ManagedObjectReference, String> host : hosts) {
                    HostMO hostMo = new HostMO(vmwareContext, host.first());
                    String hostName = hostMo.getHostName();
                    if (hostIp.equals(hostName)) {
                        HostStorageSystemMO hssMo = hostMo.getHostStorageSystemMO();
                        hssMo.rescanVmfs();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("vmware host rescanVmfs error:", e);
            throw e;
        }
    }

    private static void hostAction() {

    }

    //得到主机对应的可用LUN
    public static String getLunsOnHost(String hostName) throws Exception {
        String lunStr = "";
        String vmwareUrl = "10.143.132.248";
        String vmwareUserName = "administrator@vsphere.local";
        String vmwarePassword = "Pbu4@123";
        try {
            Gson gson = new Gson();
            HostMO hostMo = null;

            VmwareContext vmwareContext = TestVmwareContextFactory.getContext(vmwareUrl, vmwareUserName, vmwarePassword);
            RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
            List<Pair<ManagedObjectReference, String>> hosts = rootFsMO.getAllHostOnRootFs();
            if (hosts != null && hosts.size() > 0) {
                for (Pair<ManagedObjectReference, String> host : hosts) {
                    HostMO host1 = new HostMO(vmwareContext, host.first());
                    if (host1.getName().equals(hostName)) {
                        hostMo = host1;
                        break;
                    }
                }
            }

            if (hostMo != null) {
                //get available LUN
                HostDatastoreSystemMO hostDatastoreSystem = hostMo.getHostDatastoreSystemMO();
                List<HostScsiDisk> hostScsiDisks = hostDatastoreSystem.queryAvailableDisksForVmfs();
                if (hostScsiDisks != null) {
                    List<Map<String, Object>> lunlist = new ArrayList<>();
                    for (HostScsiDisk hostScsiDisk : hostScsiDisks) {
                        System.out.println("===Found disk====" + hostScsiDisk.getDevicePath() + "====="
                                + hostScsiDisk.getUuid() + "===" + gson.toJson(hostScsiDisk.getCapacity()));
                        Map<String, Object> map = new HashMap<>();
                        map.put("uuid", hostScsiDisk.getUuid());
                        map.put("devicePath", hostScsiDisk.getDevicePath());
                        map.put("block", hostScsiDisk.getCapacity().getBlock());
                        map.put("blockSize", hostScsiDisk.getCapacity().getBlockSize());

                        lunlist.add(map);
                    }

                    if (lunlist.size() > 0) {
                        lunStr = gson.toJson(lunlist);
                    }

                } else {
                    throw new Exception("host:" + hostName + ",No available LUN。");
                }
            } else {
                throw new Exception("host:" + hostName + " non-existent。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("host:" + hostName + " error:", e);
            throw e;
        }
        return lunStr;
//
    }

    //创建vmfs存储
    public static String createVmfsDataStore(String hostName, String devicePath, String datastoreName,
                                             int vmfsMajorVersion, int blockSize, long totalSectors,
                                             int unmapGranularity, String unmapPriority) throws Exception {
        String dataStoreStr = "";
        String vmwareUrl = "10.143.132.248";
        String vmwareUserName = "administrator@vsphere.local";
        String vmwarePassword = "Pbu4@123";
        try {
            Gson gson = new Gson();
            HostMO hostMo = null;

            VmwareContext vmwareContext = TestVmwareContextFactory.getContext(vmwareUrl, vmwareUserName, vmwarePassword);
            RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
            List<Pair<ManagedObjectReference, String>> hosts = rootFsMO.getAllHostOnRootFs();
            if (hosts != null && hosts.size() > 0) {
                for (Pair<ManagedObjectReference, String> host : hosts) {
                    HostMO host1 = new HostMO(vmwareContext, host.first());
                    if (host1.getName().equals(hostName)) {
                        hostMo = host1;
                        break;
                    }
                }
            }

            if (hostMo != null) {
                //get available LUN
                HostDatastoreSystemMO hostDatastoreSystem = hostMo.getHostDatastoreSystemMO();
                List<HostScsiDisk> hostScsiDisks = hostDatastoreSystem.queryAvailableDisksForVmfs();
                if (hostScsiDisks != null) {
                    HostScsiDisk candidateHostScsiDisk = null;
                    for (HostScsiDisk hostScsiDisk : hostScsiDisks) {
                        if (devicePath.equals(hostScsiDisk.getDevicePath())) {
                            candidateHostScsiDisk = hostScsiDisk;
                            break;
                        }
                    }

                    if (candidateHostScsiDisk != null) {
                        _logger.info("Create datastore via host " + hostMo.getName() + " on disk " + devicePath);
                        //create vmfs
                        ManagedObjectReference datastore = null;
                        try {
                            datastore = hostDatastoreSystem.createVmfsDatastore(datastoreName, candidateHostScsiDisk, vmfsMajorVersion, blockSize, totalSectors, unmapGranularity, unmapPriority);
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw e;
                        }
                        //set tag
                        if (datastore != null) {

                            _logger.info("datastore===" + datastore.getValue());
                            DatastoreMO dsMo = new DatastoreMO(hostMo.getContext(), datastore);
                            Map<String, Object> dataStoremap = new HashMap<>();
                            dataStoremap.put("name", dsMo.getName());
                            dataStoremap.put("id", dsMo.getMor().getValue());
                            dataStoremap.put("type", dsMo.getMor().getType());
                            dataStoremap.put("capacity", dsMo.getSummary().getCapacity());
                            dataStoreStr = gson.toJson(dataStoremap);
                            _logger.info("dataStoreStr===" + dataStoreStr);
                        }
                    } else {
                        throw new Exception("host:" + hostName + ",No find available LUN。");
                    }
                } else {
                    throw new Exception("host:" + hostName + ",No available LUN。");
                }
            } else {
                throw new Exception("host:" + hostName + " non-existent。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("host:" + hostName + " error:", e);
            throw e;
        }
        return dataStoreStr;
//
    }

    //创建vmfs存储
    public static String attachTag(String datastoreType, String datastoreId, String serviceLevelName) throws Exception {
        String attachTagStr = "";
        String vmwareUrl = "10.143.132.248";
        String vmwareUserName = "administrator@vsphere.local";
        String vmwarePassword = "Pbu4@123";

        SessionHelper sessionHelper = null;
        try {
            if (StringUtils.isEmpty(datastoreType)) {
                _logger.info("DataStore Type is null,unable tag。");
                return null;
            }
            if (StringUtils.isEmpty(datastoreId)) {
                _logger.info("DataStore Id is null,unable tag。");
                return null;
            }
            if (StringUtils.isEmpty(serviceLevelName)) {
                _logger.info("Service Level Name is null,unable tag。");
                return null;
            }

            sessionHelper = new SessionHelper();
            sessionHelper.login(vmwareUrl, vmwareUserName, vmwarePassword);
            TaggingWorkflow taggingWorkflow = new TaggingWorkflow(sessionHelper);

            List<String> taglist = taggingWorkflow.listTags();
            for (String tagid : taglist) {
                TagModel tagModel = taggingWorkflow.getTag(tagid);
                if (tagModel.getName().equals(serviceLevelName)) {
                    DynamicID objDynamicId = new DynamicID(datastoreType, datastoreId);
                    taggingWorkflow.attachTag(tagid, objDynamicId);
                    _logger.info("Service Level:" + serviceLevelName + " Associated");
                    attachTagStr = "Associated";
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("DataStore:" + datastoreId + " error:", e);
            throw e;
        } finally {
            if (sessionHelper != null) {
                sessionHelper.logout();
            }
        }
        return attachTagStr;
//
    }


    public static void main(String[] args) {
        try {
//            String listStr = VCSDKUtils.getAllVmfsDataStores(null);
//            _logger.info("Vmfs listStr==" + listStr);
//            listStr = VCSDKUtils.getAllVmfsDataStores(ToolUtils.STORE_TYPE_VMFS);
//            _logger.info("Vmfs listStr==" + listStr);
//            listStr = VCSDKUtils.getAllVmfsDataStores(ToolUtils.STORE_TYPE_NFS);
//            _logger.info("Vmfs listStr==" + listStr);

            _logger.info("Vmfs getAllClusters==" + VCSDKUtils.getLunsOnHost("10.143.133.196"));

///////////////////////create vmfs/////////////////////////////////////////////////////////
//            String hostName = "10.143.133.196";
//            String devicePath = "/vmfs/devices/disks/mpx.vmhba1:C0:T5:L0";
//            String datastoreName = "yytestvfms007";
//            int vmfsMajorVersion = 6;
//            int blockSize = 1024;
//            long totalSectors = 16777216L;
//            int unmapGranularity = 1024;
//            String unmapPriority = "low";
//            String serviceLevelName = "dme3333";
//            String dataStoreStr = VCSDKUtils.createVmfsDataStore(hostName, devicePath, datastoreName,
//                    vmfsMajorVersion, blockSize, totalSectors, unmapGranularity, unmapPriority);
//            _logger.info("Vmfs dataStoreStr==" + dataStoreStr);
//
//            Gson gson = new Gson();
//            Map<String, Object> dsmap = gson.fromJson(dataStoreStr, new TypeToken<Map<String, Object>>() {
//            }.getType());
//            if (dsmap != null) {
//                String attachTagStr = VCSDKUtils.attachTag(dsmap.get("type").toString(), dsmap.get("id").toString(), serviceLevelName);
//                _logger.info("Vmfs attachTagStr==" + attachTagStr);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

