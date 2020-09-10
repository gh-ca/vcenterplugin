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
import com.vmware.vim25.*;
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

    //得到主机对应的可用LUN
    public static Map<String,Object> getLunsOnHost(String hostName, int capacity) throws Exception {
        Map<String,Object> remap = null;
        HostScsiDisk candidateHostScsiDisk = null;
        String vmwareUrl = "10.143.132.248";
        String vmwareUserName = "administrator@vsphere.local";
        String vmwarePassword = "Pbu4@123";
        try {
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
                candidateHostScsiDisk = getObjectLuns(hostScsiDisks, capacity);
                remap = new HashMap<>();
                remap.put("host",hostMo);
                remap.put("hostScsiDisk",candidateHostScsiDisk);
            } else {
                throw new Exception("host:" + hostName + " non-existent。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("host:" + hostName + " error:", e);
            throw e;
        }
        return remap;
//
    }
    //得到集群下所有主机对应的可用LUN
    public static Map<String,Object> getLunsOnCluster(String clusterName, int capacity) throws Exception {
        Map<String,Object> remap = null;
        HostScsiDisk candidateHostScsiDisk = null;
        String vmwareUrl = "10.143.132.248";
        String vmwareUserName = "administrator@vsphere.local";
        String vmwarePassword = "Pbu4@123";
        try {
            VmwareContext vmwareContext = TestVmwareContextFactory.getContext(vmwareUrl, vmwareUserName, vmwarePassword);
            RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
            List<Pair<ManagedObjectReference, String>> cls = rootFsMO.getAllClusterOnRootFs();
            if (cls != null && cls.size() > 0) {
                for (Pair<ManagedObjectReference, String> cl : cls) {
                    ClusterMO cl1 = new ClusterMO(vmwareContext, cl.first());
                    if (cl1.getName().equals(clusterName)) {
                        List<Pair<ManagedObjectReference, String>> hosts = cl1.getClusterHosts();

                        if (hosts != null && hosts.size() > 0) {
                            Map<String,HostMO> hostMap = new HashMap<>();
                            List<HostScsiDisk> objHostScsiDisks = new ArrayList<>();
                            for (Pair<ManagedObjectReference, String> host : hosts) {
                                HostMO hostMo = new HostMO(vmwareContext, host.first());
                                HostDatastoreSystemMO hostDatastoreSystem = hostMo.getHostDatastoreSystemMO();
                                List<HostScsiDisk> hostScsiDisks = hostDatastoreSystem.queryAvailableDisksForVmfs();
                                if(hostScsiDisks!=null && hostScsiDisks.size()>0){
                                    objHostScsiDisks.addAll(hostScsiDisks);
                                    for(HostScsiDisk hsd:hostScsiDisks){
                                        hostMap.put(hsd.getDevicePath(),hostMo);
                                    }
                                }

                            }

                            if (objHostScsiDisks.size()>0) {
                                //get available LUN
                                candidateHostScsiDisk = getObjectLuns(objHostScsiDisks, capacity);
                                remap = new HashMap<>();
                                remap.put("host",hostMap.get(candidateHostScsiDisk.getDevicePath()));
                                remap.put("hostScsiDisk",candidateHostScsiDisk);
                            } else {
                                throw new Exception("cluster:" + clusterName + " non-existent LUN。");
                            }
                        }else {
                            throw new Exception("cluster:" + clusterName + " non-existent HOST。");
                        }

                        break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("cluster get lun error:", e);
            throw e;
        }
        return remap;
//
    }
    //得到主机对应的可用LUN
    public static HostScsiDisk getObjectLuns(List<HostScsiDisk> hostScsiDisks, int capacity) throws Exception {
        HostScsiDisk candidateHostScsiDisk = null;
        try {
            if (hostScsiDisks != null && hostScsiDisks.size()>0 && capacity>0) {
                long oldcapacity = capacity*1L*ToolUtils.Gi;
                long objcapacity = 0;
                HostScsiDisk objHostScsiDisk = null;
                long maxcapacity = 0;
                HostScsiDisk maxHostScsiDisk = null;
                for (HostScsiDisk hostScsiDisk : hostScsiDisks) {
                    long tmpcapacity = hostScsiDisk.getCapacity().getBlockSize()*hostScsiDisk.getCapacity().getBlock();
                    if(tmpcapacity>oldcapacity){
//                        _logger.info("tmpcapacity=="+tmpcapacity);
                        if(objcapacity==0){
                            objcapacity = tmpcapacity;
                            objHostScsiDisk = hostScsiDisk;
                        }else if(tmpcapacity<objcapacity){
                            objcapacity = tmpcapacity;
                            objHostScsiDisk = hostScsiDisk;
                        }
                    }
                    //同时也记录一个最大的
                    if(maxcapacity==0){
                        maxcapacity = tmpcapacity;
                        maxHostScsiDisk = hostScsiDisk;
                    }else if(tmpcapacity>maxcapacity){
                        maxcapacity = tmpcapacity;
                        maxHostScsiDisk = hostScsiDisk;
                    }
                }

                _logger.info("objcapacity=="+objcapacity+"=="+(objHostScsiDisk==null?"null":objHostScsiDisk.getDevicePath()));
                _logger.info("maxcapacity=="+maxcapacity+"=="+(maxHostScsiDisk==null?"null":maxHostScsiDisk.getDevicePath()));
                if(objcapacity>0){
                    candidateHostScsiDisk = objHostScsiDisk;
                }else{
                    candidateHostScsiDisk = maxHostScsiDisk;
                }

                if (candidateHostScsiDisk != null) {
                    _logger.info("Create datastore via on disk " + candidateHostScsiDisk.getDevicePath());

                } else {
                    throw new Exception("host No find available LUN。");
                }
            } else {
                throw new Exception("host No available LUN。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("get LUN error:", e);
            throw e;
        }
        return candidateHostScsiDisk;
//
    }

    //创建vmfs存储
    public static String createVmfsDataStore(Map<String,Object> hsdmap, int capacity, String datastoreName,
                                             int vmfsMajorVersion, int blockSize,
                                             int unmapGranularity, String unmapPriority) throws Exception {
        String dataStoreStr = "";
        try {
            Gson gson = new Gson();

            if(hsdmap!=null && hsdmap.get("host")!=null) {
                HostScsiDisk objhsd = (HostScsiDisk) hsdmap.get("hostScsiDisk");
                HostMO hostMo = (HostMO) hsdmap.get("host");

                _logger.info("path==" + objhsd.getDevicePath() + "==" + objhsd.getUuid() + "==" + (gson.toJson(objhsd.getCapacity())));
                _logger.info("getName==" + hostMo.getName() + "==" + hostMo.getHostDatastoreSystemMO());

                if (hostMo != null) {
                    if (objhsd != null) {
                        _logger.info("Create datastore via host " + hostMo.getName() + " on disk " + objhsd.getDevicePath());
                        long totalSectors = capacity*1L*ToolUtils.Gi/objhsd.getCapacity().getBlockSize();
                        _logger.info("Vmfs totalSectors==" + totalSectors);
                        //create vmfs
                        ManagedObjectReference datastore = null;
                        try {
                            datastore = hostMo.getHostDatastoreSystemMO().createVmfsDatastore(datastoreName, objhsd, vmfsMajorVersion, blockSize, totalSectors, unmapGranularity, unmapPriority);
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
                        throw new Exception("host:" + hostMo.getName() + ",No find available LUN。");
                    }
                } else {
                    throw new Exception("host:" + hostMo.getName() + " non-existent。");
                }
            } else {
                throw new Exception("host and LUN non-existent。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("createVmfsDataStore error:", e);
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


    //删除vmfs存储
    public static boolean deleteVmfsDataStore(String name) throws Exception {
        boolean deleteFlag = false;
        try {
            VmwareContext vmwareContext = TestVmwareContextFactory.getContext("10.143.132.248", "administrator@vsphere.local", "Pbu4@123");
            RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
            List<Pair<ManagedObjectReference, String>> hosts = rootFsMO.getAllHostOnRootFs();
            if (hosts != null && hosts.size() > 0) {
                List<Map<String, String>> lists = new ArrayList<>();
                for (Pair<ManagedObjectReference, String> host : hosts) {
                    HostMO host1 = new HostMO(vmwareContext, host.first());
                    HostDatastoreSystemMO hdsMo = host1.getHostDatastoreSystemMO();
                    if (null != hdsMo.findDatastore(name)) {
                        deleteFlag = hdsMo.deleteDatastore(name);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("vmware delete vmfs error:", e);
            throw e;
        }
        return deleteFlag;
    }

    //挂载存储
    public static void mountVmfs(String datastoreStr,String hostName) throws Exception {
        try {
            if (StringUtils.isEmpty(datastoreStr)) {
                _logger.info("datastore:"+datastoreStr+" is null");
                return;
            }
            if (StringUtils.isEmpty(hostName)) {
                _logger.info("host:"+hostName+" is null");
                return;
            }
            //查询目前未挂载的卷
            VmwareContext vmwareContext = TestVmwareContextFactory.getContext("10.143.132.248", "administrator@vsphere.local", "Pbu4@123");
            RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
            HostMO hostMo = rootFsMO.findHost(hostName);
            List<HostUnresolvedVmfsVolume> unvlist = hostMo.getHostStorageSystemMO().queryUnresolvedVmfsVolume();
            _logger.info("unvlist========"+(unvlist==null?"null":unvlist.size()));
            if(unvlist!=null && unvlist.size()>0){
                for(HostUnresolvedVmfsVolume unv:unvlist){
                    _logger.info(unv.getVmfsUuid()+"========"+unv.getVmfsLabel());
                }
            }


          _logger.info("============================");
            //查询目前未挂载的卷
            for (HostFileSystemMountInfo mount : hostMo.getHostStorageSystemMO().getHostFileSystemVolumeInfo().getMountInfo()) {
                if (mount.getVolume() instanceof HostVmfsVolume) {
                    HostVmfsVolume volume = (HostVmfsVolume) mount.getVolume();
                    _logger.info(volume.getName()+"========"+volume.getUuid());
                }

            }
          _logger.info("============================");
            //挂载卷
//            hostMo.getHostStorageSystemMO().mountVmfsVolume("4d2637f7-29519798-be16-00505684dbd6");
//
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("get LUN error:", e);
            throw e;
        }
    }

    public static void main(String[] args) {
        try {
            Gson gson = new Gson();
//            String listStr = VCSDKUtils.getAllVmfsDataStores(null);
//            _logger.info("Vmfs listStr==" + listStr);
//            listStr = VCSDKUtils.getAllVmfsDataStores(ToolUtils.STORE_TYPE_VMFS);
//            _logger.info("Vmfs listStr==" + listStr);
//            listStr = VCSDKUtils.getAllVmfsDataStores(ToolUtils.STORE_TYPE_NFS);
//            _logger.info("Vmfs listStr==" + listStr);
//            _logger.info("Vmfs getAllClusters==" + VCSDKUtils.getLunsOnHost("10.143.133.196"));
////////////////////////////////////getLunsOnHost//////////////////////////////////////////
//            Map<String,Object> hsdmap = getLunsOnHost("10.143.133.196",10);
//            if(hsdmap!=null ) {
//                HostScsiDisk objhsd = (HostScsiDisk)hsdmap.get("hostScsiDisk");
//                HostMO hostMO = (HostMO)hsdmap.get("host");
//                _logger.info("path==" + objhsd.getDevicePath() + "==" + objhsd.getUuid() + "==" + (gson.toJson(objhsd.getCapacity())));
//                _logger.info("getName==" + hostMO.getName() + "==" + hostMO.getHostDatastoreSystemMO());
//            }
//////////////////////////////////////////////////////////////////////////////////////////////
            Map<String, Object> dataStoremap = new HashMap<>();
            dataStoremap.put("name", "aaa");
            dataStoremap.put("id", "aaa");
            dataStoremap.put("type", "aaa");
            dataStoremap.put("capacity", "aaa");
            String hostName = "10.143.133.196";
            mountVmfs(gson.toJson(dataStoremap),hostName);
            _logger.info("==================over==========");
///////////////////////create vmfs/////////////////////////////////////////////////////////
//            String hostName = "10.143.133.196";
//            int capacity = 10;  //GB
//            String datastoreName = "yytestvfms007";
//            int vmfsMajorVersion = 6;
//            int blockSize = 1024;
//            int unmapGranularity = 1024;
//            String unmapPriority = "low";
//            String serviceLevelName = "dme3333";
//
//            Map<String,Object> hsdmap = getLunsOnHost(hostName,capacity);
//            if(hsdmap!=null && hsdmap.get("host")!=null) {
//
//                String dataStoreStr = VCSDKUtils.createVmfsDataStore(hsdmap, capacity, datastoreName,
//                        vmfsMajorVersion, blockSize, unmapGranularity, unmapPriority);
//                _logger.info("Vmfs dataStoreStr==" + dataStoreStr);
//
//                Map<String, Object> dsmap = gson.fromJson(dataStoreStr, new TypeToken<Map<String, Object>>() {
//                }.getType());
//                if (dsmap != null) {
//                    String attachTagStr = VCSDKUtils.attachTag(dsmap.get("type").toString(), dsmap.get("id").toString(), serviceLevelName);
//                    _logger.info("Vmfs attachTagStr==" + attachTagStr);
//                }
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

