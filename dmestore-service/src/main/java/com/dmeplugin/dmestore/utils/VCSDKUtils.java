package com.dmeplugin.dmestore.utils;

import com.dmeplugin.dmestore.services.EchoServiceImpl;
import com.dmeplugin.vmware.VCConnectionHelper;
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

    private VCConnectionHelper vcConnectionHelper;

    public VCConnectionHelper getVcConnectionHelper() {
        return vcConnectionHelper;
    }

    public void setVcConnectionHelper(VCConnectionHelper vcConnectionHelper) {
        this.vcConnectionHelper = vcConnectionHelper;
    }

    private final static Logger _logger = LoggerFactory.getLogger(EchoServiceImpl.class);

    private Gson gson = new Gson();

    //得到所有存储的Summary
    public String getAllVmfsDataStores(String storeType) throws Exception {
        String listStr = "";
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
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
                        listStr = gson.toJson(lists);
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

    //得到所有存储的info
    public  String getAllVmfsDataStoreInfos(String storeType) throws Exception {
        String listStr = "";
        try {
            VmwareContext[] vmwareContexts =vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext:vmwareContexts) {
                RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
                List<Pair<ManagedObjectReference, String>> dss = rootFsMO.getAllDatastoreOnRootFs();
                if (dss != null && dss.size() > 0) {
                    List<Map<String,Object>> lists = new ArrayList<>();

                    for (Pair<ManagedObjectReference, String> ds : dss) {
                        DatastoreMO ds1 = new DatastoreMO(vmwareContext, ds.first());
                        Map<String,Object> dsmap = gson.fromJson(gson.toJson(ds1.getSummary()), new TypeToken<Map<String,Object>>() {}.getType());
                        if(storeType.equals(ToolUtils.STORE_TYPE_NFS) &&
                                ds1.getSummary().getType().equals(ToolUtils.STORE_TYPE_NFS)){
                            NasDatastoreInfo nasinfo = (NasDatastoreInfo)ds1.getInfo();
                            dsmap.put("remoteHost",nasinfo.getNas().getRemoteHost());
                            dsmap.put("remotePath",nasinfo.getNas().getRemotePath());
                        }

                        if (StringUtils.isEmpty(storeType)) {
                            lists.add(dsmap);
                        } else if (ds1.getSummary().getType().equals(storeType)) {
                            lists.add(dsmap);
                        }

                    }
                    if (lists.size() > 0) {
                        listStr = gson.toJson(lists);
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

    //得到所有主机的ID与name
    public String getAllHosts() throws Exception {
        String listStr = "";
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
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
                        listStr = gson.toJson(lists);
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

    public String getHostByName(String hostName) throws Exception {
        VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
        List<Map<String, String>> lists = new ArrayList<>();
        for (VmwareContext vmwareContext : vmwareContexts) {
            RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
            HostMO hostMO = rootFsMO.findHost(hostName);
            Map<String, String> map = new HashMap<>();
            map.put("hostId", hostMO.getMor().getValue());
            map.put("hostName", hostMO.getName());
            lists.add(map);
        }

        return gson.toJson(lists);
    }

    //得到所有集群的id与name
    public String getAllClusters() throws Exception {
        String listStr = "";
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
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
                        listStr = gson.toJson(lists);
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

    //得到所有主机的ID与name 除去已经挂载了当前存储的主机
    public String getHostsByDsName(String dataStoreName) throws Exception {
        String listStr = "";
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
                //取得该存储下所有已经挂载的主机ID
                List<String> mounthostids = new ArrayList<>();
                DatastoreMO dsmo = rootFsMO.findDataStore(dataStoreName);
                if (dsmo != null) {
                    List<DatastoreHostMount> dhms = dsmo.getHostMounts();
                    if (dhms != null && dhms.size() > 0) {
                        for (DatastoreHostMount dhm : dhms) {
                            if (dhm != null) {
                                if (dhm.getMountInfo() != null && dhm.getMountInfo().isMounted()) {
                                    mounthostids.add(dhm.getKey().getValue());
                                }
                            }
                        }
                    }
                }
                //取得所有主机，并通过mounthostids进行过滤，过滤掉已经挂载的主机
                List<Pair<ManagedObjectReference, String>> hosts = rootFsMO.getAllHostOnRootFs();
                if (hosts != null && hosts.size() > 0) {
                    List<Map<String, String>> lists = new ArrayList<>();
                    for (Pair<ManagedObjectReference, String> host : hosts) {
                        HostMO host1 = new HostMO(vmwareContext, host.first());

                        if (!mounthostids.contains(host1.getMor().getValue())) {
                            Map<String, String> map = new HashMap<>();
                            map.put("hostId", host1.getMor().getValue());
                            map.put("hostName", host1.getName());
                            lists.add(map);
                        }
                    }
                    if (lists.size() > 0) {
                        listStr = gson.toJson(lists);
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

    //得到所有集群的ID与name 除去已经挂载了当前存储的集群  扫描集群下所有主机，只要有一个主机没挂当前存储就要显示，只有集群下所有主机都挂载了该存储就不显示
    public String getClustersByDsName(String dataStoreName) throws Exception {
        String listStr = "";
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
                //取得该存储下所有已经挂载的主机ID
                List<String> mounthostids = new ArrayList<>();
                DatastoreMO dsmo = rootFsMO.findDataStore(dataStoreName);
                if (dsmo != null) {
                    List<DatastoreHostMount> dhms = dsmo.getHostMounts();
                    if (dhms != null && dhms.size() > 0) {
                        for (DatastoreHostMount dhm : dhms) {
                            if (dhm != null) {
                                if (dhm.getMountInfo() != null && dhm.getMountInfo().isMounted()) {
                                    mounthostids.add(dhm.getKey().getValue());
                                }
                            }
                        }
                    }
                }
                //取得所有集群，并通过mounthostids进行过滤，过滤掉已经挂载的主机
                //扫描集群下所有主机，只要有一个主机没挂当前存储就要显示，只有集群下所有主机都挂载了该存储就不显示
                List<Pair<ManagedObjectReference, String>> cls = rootFsMO.getAllClusterOnRootFs();
                if (cls != null && cls.size() > 0) {
                    List<Map<String, String>> lists = new ArrayList<>();
                    for (Pair<ManagedObjectReference, String> cl : cls) {
                        boolean isMount = false;
                        ClusterMO cl1 = new ClusterMO(vmwareContext, cl.first());

                        List<Pair<ManagedObjectReference, String>> hosts = cl1.getClusterHosts();
                        if (hosts != null && hosts.size() > 0) {
                            for (Pair<ManagedObjectReference, String> host : hosts) {
                                HostMO host1 = new HostMO(vmwareContext, host.first());
                                if (!mounthostids.contains(host1.getMor().getValue())) {
                                    isMount = true;
                                    break;
                                }
                            }
                        }

                        if (isMount) {
                            Map<String, String> map = new HashMap<>();
                            map.put("clusterId", cl1.getMor().getValue());
                            map.put("clusterName", cl1.getName());
                            lists.add(map);
                        }
                    }
                    if (lists.size() > 0) {
                        listStr = gson.toJson(lists);
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

    //得到所有存储 除去已经挂载了当前主机的存储
    public String getDataStoresByHostName(String hostName, String dataStoreType) throws Exception {
        String listStr = "";
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
                //取得该存储下所有已经挂载的主机ID
                HostMO hostmo = rootFsMO.findHost(hostName);
                String objHostId = null;
                if (hostmo != null) {
                    objHostId = hostmo.getMor().getValue();
                }
                _logger.info("objHostId==" + objHostId);
                //取得所有主机，并通过mounthostids进行过滤，过滤掉已经挂载的主机
                List<Pair<ManagedObjectReference, String>> dss = rootFsMO.getAllDatastoreOnRootFs();
                if (dss != null && dss.size() > 0) {
                    List<Map<String, Object>> lists = new ArrayList<>();
                    for (Pair<ManagedObjectReference, String> ds : dss) {
                        DatastoreMO dsmo = new DatastoreMO(vmwareContext, ds.first());
                        if (dsmo != null && dataStoreType.equals(dsmo.getSummary().getType())) {
                            _logger.info("dsmo.getName==" + dsmo.getName());
                            boolean isMount = true;
                            List<DatastoreHostMount> dhms = dsmo.getHostMounts();
                            if (dhms != null && dhms.size() > 0) {
                                for (DatastoreHostMount dhm : dhms) {
                                    if (dhm != null) {
                                        if (dhm.getMountInfo() != null && dhm.getMountInfo().isMounted() && dhm.getKey().getValue().equals(objHostId)) {
                                            isMount = false;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (isMount) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("id", dsmo.getMor().getValue());
                                map.put("name", dsmo.getName());
                                map.put("status", dsmo.getSummary().isAccessible());
                                map.put("type", dsmo.getSummary().getType());
                                map.put("capacity", dsmo.getSummary().getCapacity() / ToolUtils.Gi);
                                map.put("freeSpace", dsmo.getSummary().getFreeSpace() / ToolUtils.Gi);

                                lists.add(map);
                            }

                        }

                    }
                    if (lists.size() > 0) {
                        listStr = gson.toJson(lists);
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

    //得到所有存储 除去已经挂载了当前集群的存储 扫描集群下所有主机，只要有一个主机没挂当前存储就要显示，只有集群下所有主机都挂载了该存储就不显示
    public String getDataStoresByClusterName(String clusterName, String dataStoreType) throws Exception {
        String listStr = "";
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
                //取得该存储下所有已经挂载的主机ID
                List<String> hostids = new ArrayList<>();
                ClusterMO clusterMO = rootFsMO.findCluster(clusterName);
                String objHostId = null;
                if (clusterMO != null) {
                    List<Pair<ManagedObjectReference, String>> hosts = clusterMO.getClusterHosts();
                    if (hosts != null && hosts.size() > 0) {
                        for (Pair<ManagedObjectReference, String> host : hosts) {
                            HostMO host1 = new HostMO(vmwareContext, host.first());
                            hostids.add(host1.getMor().getValue());
                        }
                    }
                }
                _logger.info("objHostId==" + hostids);
                //取得所有主机，并通过mounthostids进行过滤，过滤掉已经挂载的主机
                List<Pair<ManagedObjectReference, String>> dss = rootFsMO.getAllDatastoreOnRootFs();
                if (dss != null && dss.size() > 0) {
                    List<Map<String, Object>> lists = new ArrayList<>();
                    for (Pair<ManagedObjectReference, String> ds : dss) {
                        DatastoreMO dsmo = new DatastoreMO(vmwareContext, ds.first());
                        if (dsmo != null && dataStoreType.equals(dsmo.getSummary().getType())) {
                            _logger.info("dsmo.getName==" + dsmo.getName());
                            boolean isMount = false;
                            List<DatastoreHostMount> dhms = dsmo.getHostMounts();
                            if (dhms != null && dhms.size() > 0) {
                                //整理挂载信息
                                List<String> ds_hostids = new ArrayList<>();
                                for (DatastoreHostMount dhm : dhms) {
                                    if (dhm != null) {
                                        if (dhm.getMountInfo().isMounted()) {
                                            ds_hostids.add(dhm.getKey().getValue());
                                        }
                                    }
                                }
                                _logger.info("dsmo.ds_hostids==" + ds_hostids);
                                for (String hostid : hostids) {
                                    if (!ds_hostids.contains(hostid)) {
                                        isMount = true;
                                        break;
                                    }
                                }
                                _logger.info("dsmo.isMount==" + isMount);
                            }
                            if (isMount) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("id", dsmo.getMor().getValue());
                                map.put("name", dsmo.getName());
                                map.put("status", dsmo.getSummary().isAccessible());
                                map.put("type", dsmo.getSummary().getType());
                                map.put("capacity", dsmo.getSummary().getCapacity() / ToolUtils.Gi);
                                map.put("freeSpace", dsmo.getSummary().getFreeSpace() / ToolUtils.Gi);

                                lists.add(map);
                            }

                        }

                    }
                    if (lists.size() > 0) {
                        listStr = gson.toJson(lists);
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

    //得到指定集群下的所有主机
    public String getHostsOnCluster(String clusterName) throws Exception {
        String listStr = "";
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
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
                                    listStr = gson.toJson(lists);
                                }
                            }

                            break;
                        }
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

    //得到指定集群下的所有主机,以及指定主机所属集群下的所有主机
    public List<Pair<ManagedObjectReference, String>> getHostsOnCluster(String clusterName, String hostName) throws Exception {
        List<Pair<ManagedObjectReference, String>> hosts = null;
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());

                //集群下的所有主机
                if (!StringUtils.isEmpty(clusterName)) {
                    _logger.info("object cluster:" + clusterName);
                    ClusterMO clusterMO = rootFsMO.findCluster(clusterName);
                    hosts = clusterMO.getClusterHosts();
                    _logger.info("Number of hosts in cluster:" + (hosts == null ? "null" : hosts.size()));
                } else if (!StringUtils.isEmpty(hostName)) {  //目标主机所在集群下的其它主机
                    _logger.info("object host:" + hostName);
                    HostMO hostMO = rootFsMO.findHost(hostName);
                    ManagedObjectReference cluster = hostMO.getHyperHostCluster();
                    _logger.info("Host cluster id:" + cluster.getValue());
                    if (cluster != null) {
                        ClusterMO clusterMO = new ClusterMO(hostMO.getContext(), cluster);
                        _logger.info("Host cluster name:" + clusterMO.getName());
                        hosts = clusterMO.getClusterHosts();
                    }
                    _logger.info("Number of hosts in cluster:" + (hosts == null ? "null" : hosts.size()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("get Host On Cluster error:", e);
            throw e;
        }

        return hosts;
    }

    public String renameDataStore(String oldName, String newName) throws Exception {

        String result = "success";
        _logger.info("==start rename DataStore==");
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                DatastoreMO dsMo = new DatastoreMO(vmwareContext, new DatacenterMO(vmwareContext, "Datacenter").findDatastore(oldName));
                dsMo.renameDatastore(newName);
                _logger.info("==end rename DataStore==");
            }
        } catch (Exception e) {
            result = "failed";
            _logger.error("vmware error:", e);
            throw e;
        } finally {
            return result;
        }
    }

    //get oriented datastore capacity
    public static Map<String, Object> getCapacityOnVmfs(String dsname) {
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("msg", "success");
        _logger.info("==start get oriented datastore capacity==");
        try {
            VmwareContext vmwareContext = TestVmwareContextFactory.getContext("10.143.132.248", "administrator@vsphere.local", "Pbu4@123");
            DatastoreMO dsMo = new DatastoreMO(vmwareContext, new DatacenterMO(vmwareContext, "Datacenter").findDatastore(dsname));
            DatastoreSummary summary = dsMo.getSummary();
            long capacity = summary.getCapacity();
            _logger.info("==end get oriented datastore capacity==");
            resMap.put("capacity", capacity);
            return resMap;
        } catch (Exception e) {
            resMap.put("msg", "failed");
            _logger.error("vmware error:", e);
            throw e;
        } finally {
            return resMap;
        }
    }

    //expand oriented datastore capacity
    public String expandVmfsDatastore(String dsname, Integer add_capacity) {
        String result = "success";
        _logger.info("==start expand DataStore==");
        try {
            VmwareContext vmwareContext = TestVmwareContextFactory.getContext("10.143.132.248", "administrator@vsphere.local", "Pbu4@123");
            DatastoreMO dsMo = new DatastoreMO(vmwareContext, new DatacenterMO(vmwareContext, "Datacenter").findDatastore(dsname));
            DatacenterMO dcMo = new DatacenterMO(vmwareContext, "Datacenter");
            List<ManagedObjectReference> hosts = dcMo.findHost("10.143.132.17");
            ManagedObjectReference managedObjectReference = null;
            if (hosts != null && hosts.size() > 0) {
                managedObjectReference = hosts.get(0);
            }
            HostMO hostMo = new HostMO(vmwareContext, managedObjectReference);
            //todo get oriented LUN  (参数设置存在问题)
            String devicePath = "/vmfs/devices/disks/t10.ATA_____WDC_WD1003FBYX2D01Y7B1________________________WD2DWCAW35431438";
            Long totalSectors = add_capacity * 1L * ToolUtils.Gi;
            HostScsiDisk candidateHostScsiDisk = null;
            if (hostMo != null) {
                HostDatastoreSystemMO hostDatastoreSystemMO = hostMo.getHostDatastoreSystemMO();
                List<HostScsiDisk> hostScsiDisks = hostDatastoreSystemMO.queryAvailableDisksForVmfs();
                if (hostScsiDisks != null) {
                    for (HostScsiDisk hostScsiDisk : hostScsiDisks) {
                        if (hostScsiDisk.getDevicePath().equals(devicePath)) {
                            candidateHostScsiDisk = hostScsiDisk;
                            break;
                        }
                    }
                }
                int blockSize = 0;
                if (candidateHostScsiDisk != null) {
                    blockSize = candidateHostScsiDisk.getCapacity().getBlockSize();
                    totalSectors = totalSectors / blockSize;
                }
                List<VmfsDatastoreOption> vmfsDatastoreOptions = hostDatastoreSystemMO.queryVmfsDatastoreExpandOptions(dsMo);
                if (vmfsDatastoreOptions != null && vmfsDatastoreOptions.size() > 0) {
                    VmfsDatastoreOption vmfsDatastoreOption = vmfsDatastoreOptions.get(0);
                    VmfsDatastoreExpandSpec spec = (VmfsDatastoreExpandSpec) vmfsDatastoreOption.getSpec();
                    if (candidateHostScsiDisk != null) {
                        HostDiskPartitionSpec hostDiskPartitionSpec = new HostDiskPartitionSpec();
                        hostDiskPartitionSpec.setTotalSectors(totalSectors);
                        spec.setPartition(hostDiskPartitionSpec);
                        VmfsDatastoreSpec vmfsDatastoreSpec = new VmfsDatastoreSpec();
                        vmfsDatastoreSpec.setDiskUuid(candidateHostScsiDisk.getUuid());
                        vmfsDatastoreOption.setSpec(vmfsDatastoreSpec);

                    }
                    hostMo.getHostDatastoreSystemMO().expandVmfsDatastore(dsMo, spec);
                }
            }
            _logger.info("==end expand DataStore==");
        } catch (Exception e) {
            result = "failed";
            _logger.error("vmware error:", e);
            throw e;
        } finally {
            return result;
        }
    }

    //recycle vmfs datastore capacity
    public String recycleVmfsCapacity(String dsname) throws Exception {

        String result = "success";
        List<String> uuids = new ArrayList<>();
        try {
            VmwareContext vmwareContext = TestVmwareContextFactory.getContext("10.143.132.248", "administrator@vsphere.local", "Pbu4@123");
            DatacenterMO dcMo = new DatacenterMO(vmwareContext, "Datacenter");
            List<ManagedObjectReference> hosts = dcMo.findHost("10.143.133.196");
            ManagedObjectReference managedObjectReference = null;
            if (hosts != null && hosts.size() > 0) {
                managedObjectReference = hosts.get(0);
            }

            HostMO hostMo = new HostMO(vmwareContext, managedObjectReference);
            HostDatastoreSystemMO hostDatastoreSystemMO = hostMo.getHostDatastoreSystemMO();
            HostStorageSystemMO hostStorageSystemMO = hostMo.getHostStorageSystemMO();
            List<HostFileSystemMountInfo> mountInfo = hostStorageSystemMO.getHostFileSystemVolumeInfo().getMountInfo();
            if (mountInfo != null && mountInfo.size() > 0) {
                for (HostFileSystemMountInfo mount : mountInfo) {
                    if (mount.getVolume() instanceof HostVmfsVolume
                            && dsname.equals(mount.getVolume().getName())) {
                        HostVmfsVolume volume = (HostVmfsVolume) mount.getVolume();
                        _logger.info(volume.getName() + "========" + volume.getUuid());
                        uuids.add(volume.getUuid());
                    }
                }
            }
            hostDatastoreSystemMO.unmapVmfsVolumeExTask(uuids);
        } catch (Exception e) {
            result = "error";
            _logger.error("vmware error:", e);
            throw e;
        }
        return result;
    }

    //create nfs datastore
    public String createNfsDatastore(String serverHost, Integer logicPort, String exportPath, String nfsName ,String accessMode,String mountHost) {
        //todo
        //NfsDataInfo nfsDataInfo = new NfsDataInfo(); 用于详情和列表展示的自定义model
        //NasDatastoreInfo-->DataStoreInfo-->(aliasOf, containerId, freeSpace, maxFileSize, maxMemoryFileSize, maxVirtualDiskCapacity, name, timestamp, url)
        //如何拿到DataStoreInfo对象？去设置其中关于指定容量大小的问题
        //如何让NFS和fs/share/logicport产生关系？（指定的主机上面是否绑定对应的fs/share/logicport列表?）
        //String accessMode = "";
        String result = "success";
        accessMode = StringUtils.isEmpty(accessMode) || accessMode.equals("readWrite") ? "readWrite" : "readOnly";
        //exportPath = "/volume1/TESTNFS";
        //需要判断nfs版本，如果是v4.1要将 Kerberos 安全功能与 NFS 4.1 结合使用，请启用 Kerberos 并选择适当的 Kerberos 模型。
        //nfsName = "lqnfsv3.1";
        //remoteHostNames equal remoteHost(v3)
        //serverHost = "10.143.132.187";
        _logger.info("start creat nfs datastore");
        //mountHost = "10.143.132.17";
        //logicPort = 0;
        try {
            VmwareContext vmwareContext = TestVmwareContextFactory.getContext("10.143.132.248", "administrator@vsphere.local", "Pbu4@123");
            DatacenterMO dcMo = new DatacenterMO(vmwareContext, "Datacenter");
            List<ManagedObjectReference> hosts = dcMo.findHost(mountHost);
            ManagedObjectReference managedObjectReference = null;
            if (hosts != null && hosts.size() > 0) {
                managedObjectReference = hosts.get(0);
                HostMO hostMO = new HostMO(vmwareContext, managedObjectReference);
                HostDatastoreSystemMO hostDatastoreSystemMO = hostMO.getHostDatastoreSystemMO();
                //todo 存在返回值 需要处理
                hostDatastoreSystemMO.createNfsDatastore(serverHost, logicPort, exportPath, nfsName,accessMode);
                _logger.info("end creat nfs datastore");
            } else {
                result = "failed";
                _logger.error("can not find target host!");
            }
        } catch (Exception e) {
            result = "failed";
            _logger.error("vmware error:", e);
        }
        return result;
    }

    public void hostRescanVmfs(String hostIp) throws Exception {
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
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
    public String getLunsOnHost(String hostName) throws Exception {
        String lunStr = "";
        try {
            HostMO hostMo = null;

            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {

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
                            map.put("deviceName", hostScsiDisk.getDeviceName());
                            map.put("localDisk", hostScsiDisk.isLocalDisk());
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
    public Map<String, Object> getLunsOnHost(String hostName, int capacity) throws Exception {
        Map<String, Object> remap = null;
        HostScsiDisk candidateHostScsiDisk = null;
        try {
            HostMO hostMo = null;

            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
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
                    remap.put("host", hostMo);
                    remap.put("hostScsiDisk", candidateHostScsiDisk);
                } else {
                    throw new Exception("host:" + hostName + " non-existent。");
                }
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
    public Map<String, Object> getLunsOnCluster(String clusterName, int capacity) throws Exception {
        Map<String, Object> remap = null;
        HostScsiDisk candidateHostScsiDisk = null;
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
                List<Pair<ManagedObjectReference, String>> cls = rootFsMO.getAllClusterOnRootFs();
                if (cls != null && cls.size() > 0) {
                    for (Pair<ManagedObjectReference, String> cl : cls) {
                        ClusterMO cl1 = new ClusterMO(vmwareContext, cl.first());
                        if (cl1.getName().equals(clusterName)) {
                            List<Pair<ManagedObjectReference, String>> hosts = cl1.getClusterHosts();

                            if (hosts != null && hosts.size() > 0) {
                                Map<String, HostMO> hostMap = new HashMap<>();
                                List<HostScsiDisk> objHostScsiDisks = new ArrayList<>();
                                for (Pair<ManagedObjectReference, String> host : hosts) {
                                    HostMO hostMo = new HostMO(vmwareContext, host.first());
                                    HostDatastoreSystemMO hostDatastoreSystem = hostMo.getHostDatastoreSystemMO();
                                    List<HostScsiDisk> hostScsiDisks = hostDatastoreSystem.queryAvailableDisksForVmfs();
                                    if (hostScsiDisks != null && hostScsiDisks.size() > 0) {
                                        objHostScsiDisks.addAll(hostScsiDisks);
                                        for (HostScsiDisk hsd : hostScsiDisks) {
                                            hostMap.put(hsd.getDevicePath(), hostMo);
                                        }
                                    }

                                }

                                if (objHostScsiDisks.size() > 0) {
                                    //get available LUN
                                    candidateHostScsiDisk = getObjectLuns(objHostScsiDisks, capacity);
                                    remap = new HashMap<>();
                                    remap.put("host", hostMap.get(candidateHostScsiDisk.getDevicePath()));
                                    remap.put("hostScsiDisk", candidateHostScsiDisk);
                                } else {
                                    throw new Exception("cluster:" + clusterName + " non-existent LUN。");
                                }
                            } else {
                                throw new Exception("cluster:" + clusterName + " non-existent HOST。");
                            }

                            break;
                        }
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
    public HostScsiDisk getObjectLuns(List<HostScsiDisk> hostScsiDisks, int capacity) throws Exception {
        HostScsiDisk candidateHostScsiDisk = null;
        try {
            if (hostScsiDisks != null && hostScsiDisks.size() > 0 && capacity > 0) {
                long oldcapacity = capacity * 1L * ToolUtils.Gi;
                long objcapacity = 0;
                HostScsiDisk objHostScsiDisk = null;
                long maxcapacity = 0;
                HostScsiDisk maxHostScsiDisk = null;
                for (HostScsiDisk hostScsiDisk : hostScsiDisks) {
                    long tmpcapacity = hostScsiDisk.getCapacity().getBlockSize() * hostScsiDisk.getCapacity().getBlock();
                    if (tmpcapacity > oldcapacity) {
//                        _logger.info("tmpcapacity=="+tmpcapacity);
                        if (objcapacity == 0) {
                            objcapacity = tmpcapacity;
                            objHostScsiDisk = hostScsiDisk;
                        } else if (tmpcapacity < objcapacity) {
                            objcapacity = tmpcapacity;
                            objHostScsiDisk = hostScsiDisk;
                        }
                    }
                    //同时也记录一个最大的
                    if (maxcapacity == 0) {
                        maxcapacity = tmpcapacity;
                        maxHostScsiDisk = hostScsiDisk;
                    } else if (tmpcapacity > maxcapacity) {
                        maxcapacity = tmpcapacity;
                        maxHostScsiDisk = hostScsiDisk;
                    }
                }

                _logger.info("objcapacity==" + objcapacity + "==" + (objHostScsiDisk == null ? "null" : objHostScsiDisk.getDevicePath()));
                _logger.info("maxcapacity==" + maxcapacity + "==" + (maxHostScsiDisk == null ? "null" : maxHostScsiDisk.getDevicePath()));
                if (objcapacity > 0) {
                    candidateHostScsiDisk = objHostScsiDisk;
                } else {
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
    public String createVmfsDataStore(Map<String, Object> hsdmap, int capacity, String datastoreName,
                                      int vmfsMajorVersion, int blockSize,
                                      int unmapGranularity, String unmapPriority) throws Exception {
        String dataStoreStr = "";
        try {
            if (hsdmap != null && hsdmap.get("host") != null) {
                HostScsiDisk objhsd = (HostScsiDisk) hsdmap.get("hostScsiDisk");
                HostMO hostMo = (HostMO) hsdmap.get("host");

                _logger.info("path==" + objhsd.getDevicePath() + "==" + objhsd.getUuid() + "==" + (gson.toJson(objhsd.getCapacity())));
                _logger.info("getName==" + hostMo.getName() + "==" + hostMo.getHostDatastoreSystemMO());

                if (hostMo != null) {
                    if (objhsd != null) {
                        _logger.info("Create datastore via host " + hostMo.getName() + " on disk " + objhsd.getDevicePath());
                        long totalSectors = capacity * 1L * ToolUtils.Gi / objhsd.getCapacity().getBlockSize();
                        _logger.info("Vmfs totalSectors==" + totalSectors);
                        //create vmfs
                        ManagedObjectReference datastore = null;
                        try {
                            datastore = hostMo.getHostDatastoreSystemMO().createVmfsDatastore(datastoreName, objhsd, vmfsMajorVersion, blockSize, totalSectors, unmapGranularity, unmapPriority);
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw e;
                        }
                        //return datastore info
                        if (datastore != null) {
                            _logger.info("datastore===" + datastore.getValue());
                            DatastoreMO dsMo = new DatastoreMO(hostMo.getContext(), datastore);
                            Map<String, Object> dataStoremap = new HashMap<>();
                            dataStoremap.put("name", dsMo.getName());
                            dataStoremap.put("id", dsMo.getMor().getValue());
                            dataStoremap.put("type", dsMo.getMor().getType());
                            dataStoremap.put("capacity", dsMo.getSummary().getCapacity());
                            dataStoremap.put("hostName", hostMo.getName());
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

    //vmfs存储打标记
    public String attachTag(String datastoreType, String datastoreId, String serviceLevelName) throws Exception {
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
    public boolean deleteVmfsDataStore(String name) throws Exception {
        boolean deleteFlag = false;
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("vmware delete vmfs error:", e);
            throw e;
        }
        return deleteFlag;
    }

    //将存储挂载到集群下其它主机
    public void mountVmfsOnCluster(String datastoreStr, String clusterName, String hostName) throws Exception {
        try {
            if (StringUtils.isEmpty(datastoreStr)) {
                _logger.info("datastore:" + datastoreStr + " is null");
                return;
            }
            if (StringUtils.isEmpty(hostName) && StringUtils.isEmpty(clusterName)) {
                _logger.info("host:" + hostName + " and cluster:" + clusterName + " is null");
                return;
            }

            Map<String, Object> dsmap = gson.fromJson(datastoreStr, new TypeToken<Map<String, Object>>() {
            }.getType());
            String objHostName = "";
            String objDataStoreName = "";
            if (dsmap != null) {
                objHostName = ToolUtils.getStr(dsmap.get("hostName"));
                objHostName = objHostName == null ? "" : objHostName;
                objDataStoreName = ToolUtils.getStr(dsmap.get("name"));
            }

            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                //集群下的所有主机
                List<Pair<ManagedObjectReference, String>> hosts = getHostsOnCluster(clusterName, hostName);

                if (hosts != null && hosts.size() > 0) {
                    for (Pair<ManagedObjectReference, String> host : hosts) {
                        HostMO host1 = new HostMO(vmwareContext, host.first());
                        _logger.info("Host under Cluster: " + host1.getName());
                        //只挂载其它的主机
                        if (host1 != null && !objHostName.equals(host1.getName())) {
                            mountVmfs(objDataStoreName, host1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("mount Vmfs On Cluster error:", e);
            throw e;
        }
    }

    //挂载存储
    public void mountVmfs(String datastoreName, HostMO hostMO) throws Exception {
        try {
            if (StringUtils.isEmpty(datastoreName)) {
                _logger.info("datastore Name is null");
                return;
            }
            if (hostMO == null) {
                _logger.info("host is null");
                return;
            }
            _logger.info("Hosts that need to be mounted:" + hostMO.getName());
            //挂载前重新扫描datastore
            hostMO.getHostStorageSystemMO().rescanVmfs();
            _logger.info("Rescan datastore before mounting");
            //查询目前未挂载的卷
//            List<HostUnresolvedVmfsVolume> unvlist = hostMO.getHostStorageSystemMO().queryUnresolvedVmfsVolume();
//            _logger.info("unvlist========"+(unvlist==null?"null":unvlist.size()));
//            if(unvlist!=null && unvlist.size()>0){
//                for(HostUnresolvedVmfsVolume unv:unvlist){
//                    _logger.info(unv.getVmfsUuid()+"========"+unv.getVmfsLabel());
//                }
//            }
//            _logger.info("============================");
            //查询目前未挂载的卷
            for (HostFileSystemMountInfo mount : hostMO.getHostStorageSystemMO().getHostFileSystemVolumeInfo().getMountInfo()) {
                if (mount.getVolume() instanceof HostVmfsVolume
                        && datastoreName.equals(mount.getVolume().getName())) {
                    HostVmfsVolume volume = (HostVmfsVolume) mount.getVolume();
                    _logger.info(volume.getName() + "========" + volume.getUuid());
                    //挂载卷
                    hostMO.getHostStorageSystemMO().mountVmfsVolume(volume.getUuid());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("mount Vmfs Volume error:", e);
            throw e;
        }
    }

    //在主机上扫描卷和Datastore
    public void scanDataStore(String clusterName, String hostName) throws Exception {
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                //集群下的所有主机
                List<Pair<ManagedObjectReference, String>> hosts = getHostsOnCluster(clusterName, hostName);

                if (hosts != null && hosts.size() > 0) {
                    for (Pair<ManagedObjectReference, String> host : hosts) {
                        HostMO host1 = new HostMO(vmwareContext, host.first());
                        _logger.info("Host under Cluster: " + host1.getName());
                        host1.getHostStorageSystemMO().rescanVmfs();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("scan Data Store error:", e);
            throw e;
        }
    }

    /**
     * @Author Administrator
     * @Description 为指定的虚拟机创建磁盘
     * @Date 11:41 2020/9/14
     * @Param [datacenter_name, datastore_name, vm_name, rdmdevicename, size]
     * @Return void
     **/
    public void createDisk(String datacenter_name, String datastore_name, String vm_name, String rdmdevicename, int size) throws Exception {
        VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
        for (VmwareContext vmwareContext : vmwareContexts) {
            DatacenterMO dcMo = new DatacenterMO(vmwareContext, datacenter_name);
            DatastoreMO dsMo = new DatastoreMO(vmwareContext, dcMo.findDatastore(datastore_name));
            VirtualMachineMO virtualMachineMO = new VirtualMachineMO(vmwareContext, vm_name);
            String vmdkDatastorePath = dsMo.getDatastorePath(datastore_name);
            int sizeInMb = size;
            try {
                virtualMachineMO.createDisk(vmdkDatastorePath, VirtualDiskType.RDM, VirtualDiskMode.PERSISTENT,
                        rdmdevicename, sizeInMb, dsMo.getMor(), -1);
            } catch (Exception ex) {
                _logger.error("create vcenter disk rdm failed!errorMsg:{}", ex.getMessage());
                throw new Exception(ex.getMessage());
            }
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
            // _logger.info("Vmfs getAllClusters==" + VCSDKUtils.getDataStoresByHostName("10.143.132.17"));
////////////////////////////////////getLunsOnHost//////////////////////////////////////////
//            Map<String,Object> hsdmap = getLunsOnHost("10.143.133.196",10);
//            if(hsdmap!=null ) {
//                HostScsiDisk objhsd = (HostScsiDisk)hsdmap.get("hostScsiDisk");
//                HostMO hostMO = (HostMO)hsdmap.get("host");
//                _logger.info("path==" + objhsd.getDevicePath() + "==" + objhsd.getUuid() + "==" + (gson.toJson(objhsd.getCapacity())));
//                _logger.info("getName==" + hostMO.getName() + "==" + hostMO.getHostDatastoreSystemMO());
//            }
//////////////////////////////////////////////////////////////////////////////////////////////
//            Map<String, Object> dataStoremap = new HashMap<>();
//            dataStoremap.put("name", "aaa");
//            dataStoremap.put("id", "aaa");
//            dataStoremap.put("type", "aaa");
//            dataStoremap.put("capacity", "aaa");
//            dataStoremap.put("hostName", "10.143.133.196");
//            String hostName = "10.143.133.196";
//            String clusterName = "";
//            mountVmfsOnCluster(gson.toJson(dataStoremap), clusterName, hostName);
//            _logger.info("==================over==========");
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

