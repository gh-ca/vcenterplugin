package com.dmeplugin.dmestore.utils;

import com.dmeplugin.dmestore.model.EthPortInfo;
import com.dmeplugin.dmestore.services.EchoServiceImpl;
import com.dmeplugin.vmware.VCConnectionHelper;
import com.dmeplugin.vmware.autosdk.SessionHelper;
import com.dmeplugin.vmware.autosdk.TaggingWorkflow;
import com.dmeplugin.vmware.mo.*;
import com.dmeplugin.vmware.util.Pair;
import com.dmeplugin.vmware.util.PbmUtil;
import com.dmeplugin.vmware.util.TestVmwareContextFactory;
import com.dmeplugin.vmware.util.VmwareContext;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vmware.cis.tagging.CategoryModel;
import com.vmware.cis.tagging.CategoryTypes;
import com.vmware.cis.tagging.TagModel;
import com.vmware.pbm.*;
import com.vmware.pbm.RuntimeFaultFaultMsg;
import com.vmware.vapi.std.DynamicID;
import com.vmware.vim25.*;
import com.vmware.vim25.InvalidArgumentFaultMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.*;

public class VCSDKUtils {

    private static final String POLICY_DESC="policy created by dme";

    public static final String CATEGORY_NAME="DME Service Level";

    private VCConnectionHelper vcConnectionHelper;

    public VCConnectionHelper getVcConnectionHelper() {
        return vcConnectionHelper;
    }

    public void setVcConnectionHelper(VCConnectionHelper vcConnectionHelper) {
        this.vcConnectionHelper = vcConnectionHelper;
    }

    private final static Logger _logger = LoggerFactory.getLogger(VCSDKUtils.class);

    private Gson gson = new Gson();

    //得到所有存储的info 20200918objectId
    public String getAllVmfsDataStoreInfos(String storeType) throws Exception {
        String listStr = "";
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
                List<Pair<ManagedObjectReference, String>> dss = rootFsMO.getAllDatastoreOnRootFs();
                if (dss != null && dss.size() > 0) {
                    List<Map<String, Object>> lists = new ArrayList<>();

                    for (Pair<ManagedObjectReference, String> ds : dss) {
                        DatastoreMO ds1 = new DatastoreMO(vmwareContext, ds.first());
                        Map<String, Object> dsmap = gson.fromJson(gson.toJson(ds1.getSummary()), new TypeToken<Map<String, Object>>() {
                        }.getType());
                        String objectid = vcConnectionHelper.MOR2ObjectID(ds1.getMor(), vmwareContext.getServerAddress());
                        dsmap.put("objectid", objectid);
                        if (storeType.equals(ToolUtils.STORE_TYPE_NFS) &&
                                ds1.getSummary().getType().equals(ToolUtils.STORE_TYPE_NFS)) {
                            NasDatastoreInfo nasinfo = (NasDatastoreInfo) ds1.getInfo();

                            dsmap.put("remoteHost", nasinfo.getNas().getRemoteHost());
                            dsmap.put("remotePath", nasinfo.getNas().getRemotePath());
                            dsmap.put("nfsStorageId", ds1.getMor().getValue());
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

    //得到所有主机的ID与name  20200918objectId
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
                        String objectId = vcConnectionHelper.MOR2ObjectID(host1.getMor(), vmwareContext.getServerAddress());
                        map.put("hostId", objectId);
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
            String objectId = vcConnectionHelper.MOR2ObjectID(hostMO.getMor(), vmwareContext.getServerAddress());
            map.put("hostId", objectId);
            map.put("hostName", hostMO.getName());
            lists.add(map);
        }

        return gson.toJson(lists);
    }

    //得到所有集群的id与name 20200918objectId
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
                        String objectId = vcConnectionHelper.MOR2ObjectID(cl1.getMor(), vmwareContext.getServerAddress());
                        map.put("clusterId", objectId);
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

    //得到所有主机的ID与name 除去已经挂载了当前存储的主机  20200918objectId
    public String getHostsByDsObjectId(String dataStoreObjectId) throws Exception {
        String listStr = "";
        try {
            //得到当前的context
            String serverguid = vcConnectionHelper.objectID2Serverguid(dataStoreObjectId);
            VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

            RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());

            ManagedObjectReference dsmor = vcConnectionHelper.objectID2MOR(dataStoreObjectId);
            //取得该存储下所有已经挂载的主机ID
            List<String> mounthostids = new ArrayList<>();
            DatastoreMO dsmo = new DatastoreMO(vmwareContext, dsmor);
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
                        String objectId = vcConnectionHelper.MOR2ObjectID(host1.getMor(), vmwareContext.getServerAddress());
                        map.put("hostId", objectId);
                        map.put("hostName", host1.getName());
                        lists.add(map);
                    }
                }
                if (lists.size() > 0) {
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

    //得到所有集群的ID与name 除去已经挂载了当前存储的集群  扫描集群下所有主机，只要有一个主机没挂当前存储就要显示，只有集群下所有主机都挂载了该存储就不显示
    //20200918objectId
    public String getClustersByDsObjectId(String dataStoreObjectId) throws Exception {
        String listStr = "";
        try {
            String serverguid = vcConnectionHelper.objectID2Serverguid(dataStoreObjectId);
            VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

            RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
            //取得该存储下所有已经挂载的主机ID
            List<String> mounthostids = new ArrayList<>();
            ManagedObjectReference dsmor = vcConnectionHelper.objectID2MOR(dataStoreObjectId);
            DatastoreMO dsmo = new DatastoreMO(vmwareContext, dsmor);
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
                        String objectId = vcConnectionHelper.MOR2ObjectID(cl1.getMor(), vmwareContext.getServerAddress());
                        map.put("clusterId", objectId);
                        map.put("clusterName", cl1.getName());
                        lists.add(map);
                    }
                }
                if (lists.size() > 0) {
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

    //得到所有存储 除去已经挂载了当前主机的存储 20200918objectId
    public String getDataStoresByHostObjectId(String hostObjectId, String dataStoreType) throws Exception {
        String listStr = "";
        try {
            String serverguid = vcConnectionHelper.objectID2Serverguid(hostObjectId);
            VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

            RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
            //取得该存储下所有已经挂载的主机ID
            ManagedObjectReference objmor = vcConnectionHelper.objectID2MOR(hostObjectId);
            HostMO hostmo = new HostMO(vmwareContext, objmor);
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
                            String objectId = vcConnectionHelper.MOR2ObjectID(dsmo.getMor(), vmwareContext.getServerAddress());
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", dsmo.getMor().getValue());
                            map.put("name", dsmo.getName());
                            map.put("objectId", objectId);
                            map.put("status", dsmo.getSummary().isAccessible());
                            map.put("type", dsmo.getSummary().getType());
                            map.put("capacity", dsmo.getSummary().getCapacity() / ToolUtils.GI);
                            map.put("freeSpace", dsmo.getSummary().getFreeSpace() / ToolUtils.GI);

                            lists.add(map);
                        }

                    }

                }
                if (lists.size() > 0) {
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

    //得到所有存储 除去已经挂载了当前集群的存储 扫描集群下所有主机，只要有一个主机没挂当前存储就要显示，只有集群下所有主机都挂载了该存储就不显示
    //20200918objectId
    public String getDataStoresByClusterObjectId(String clusterObjectId, String dataStoreType) throws Exception {
        String listStr = "";
        try {
            String serverguid = vcConnectionHelper.objectID2Serverguid(clusterObjectId);
            VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

            RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
            //取得该存储下所有已经挂载的主机ID
            List<String> hostids = new ArrayList<>();
            ManagedObjectReference objmor = vcConnectionHelper.objectID2MOR(clusterObjectId);
            ClusterMO clusterMO = new ClusterMO(vmwareContext, objmor);
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
                            String objectId = vcConnectionHelper.MOR2ObjectID(dsmo.getMor(), vmwareContext.getServerAddress());
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", dsmo.getMor().getValue());
                            map.put("name", dsmo.getName());
                            map.put("objectId", objectId);
                            map.put("status", dsmo.getSummary().isAccessible());
                            map.put("type", dsmo.getSummary().getType());
                            map.put("capacity", dsmo.getSummary().getCapacity() / ToolUtils.GI);
                            map.put("freeSpace", dsmo.getSummary().getFreeSpace() / ToolUtils.GI);

                            lists.add(map);
                        }

                    }

                }
                if (lists.size() > 0) {
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

    //得到指定集群下的所有主机 20200918objectId
    public String getHostsOnCluster(String clusterObjectId) throws Exception {
        String listStr = "";
        try {
            //得到当前的context
            String serverguid = vcConnectionHelper.objectID2Serverguid(clusterObjectId);
            VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

            ManagedObjectReference clmor = vcConnectionHelper.objectID2MOR(clusterObjectId);
            ClusterMO cl1 = new ClusterMO(vmwareContext, clmor);
            List<Pair<ManagedObjectReference, String>> hosts = cl1.getClusterHosts();

            if (hosts != null && hosts.size() > 0) {
                List<Map<String, String>> lists = new ArrayList<>();
                for (Pair<ManagedObjectReference, String> host : hosts) {
                    HostMO host1 = new HostMO(vmwareContext, host.first());

                    Map<String, String> map = new HashMap<>();
                    String objectId = vcConnectionHelper.MOR2ObjectID(host1.getMor(), vmwareContext.getServerAddress());
                    map.put("hostId", objectId);
                    map.put("hostName", host1.getName());
                    lists.add(map);
                }
                if (lists.size() > 0) {
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

    //得到集群下所有没有挂载的主机 20200918objectId
    public List<String> getUnmoutHostsOnCluster(String dataStoreObjectId, List<Map<String,String>> clusters) throws Exception {
        List<String> hostlist = null;
        try {
            String unmountHostStr = getHostsByDsObjectId(dataStoreObjectId);
            List<Map<String, String>> unmountHostlists = gson.fromJson(unmountHostStr, new TypeToken<List<Map<String, String>>>() {
            }.getType());

            if (clusters != null && clusters.size() > 0) {
                hostlist = new ArrayList<>();
                for (Map<String,String> cluster : clusters) {
                    String hostStr = getHostsOnCluster(cluster.get("clusterId"));
                    List<Map<String, String>> hostStrlists = gson.fromJson(hostStr, new TypeToken<List<Map<String, String>>>() {
                    }.getType());
                    if (hostStrlists != null && hostStrlists.size() > 0) {
                        for (Map<String, String> hostmap : hostStrlists) {
                            if (unmountHostlists.contains(hostmap)) {
                                hostlist.add(ToolUtils.getStr(hostmap.get("hostName")));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("vmware error:", e);
            throw e;
        }
        return hostlist;
    }

    //得到指定集群下的所有主机,以及指定主机所属集群下的所有主机 20200918objectId
    public List<Pair<ManagedObjectReference, String>> getHostsOnCluster(String clusterObjectId, String hostObjectId) throws Exception {
        List<Pair<ManagedObjectReference, String>> hosts = null;
        try {
            String serverguid = null;
            if(!StringUtils.isEmpty(clusterObjectId)) {
                serverguid = vcConnectionHelper.objectID2Serverguid(clusterObjectId);
            }else if(!StringUtils.isEmpty(hostObjectId)) {
                serverguid = vcConnectionHelper.objectID2Serverguid(hostObjectId);
            }
            if(!StringUtils.isEmpty(serverguid)) {
                VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

                RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());

                //集群下的所有主机
                if (!StringUtils.isEmpty(clusterObjectId)) {
                    _logger.info("object cluster Object Id:" + clusterObjectId);
                    ManagedObjectReference objmor = vcConnectionHelper.objectID2MOR(clusterObjectId);
                    ClusterMO clusterMO = new ClusterMO(vmwareContext, objmor);
                    hosts = clusterMO.getClusterHosts();
                    _logger.info("Number of hosts in cluster:" + (hosts == null ? "null" : hosts.size()));
                } else if (!StringUtils.isEmpty(hostObjectId)) {  //目标主机所在集群下的其它主机
                    _logger.info("object host Object Id:" + hostObjectId);
                    ManagedObjectReference objmor = vcConnectionHelper.objectID2MOR(hostObjectId);
                    HostMO hostMO = new HostMO(vmwareContext, objmor);
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

    //rename datastore name
    public String renameDataStore(String newName,String dataStoreObjectId) throws Exception {

        String result = "success";
        _logger.info("==start rename DataStore==");
        try {
            String serverguid = vcConnectionHelper.objectID2Serverguid(dataStoreObjectId);
            VmwareContext serverContext = vcConnectionHelper.getServerContext(serverguid);
            ManagedObjectReference dsmor = vcConnectionHelper.objectID2MOR(dataStoreObjectId);
            //todo 测试需要生成使用
            //String objectId = vcConnectionHelper.MOR2ObjectID(dsMo.getMor(), dsMo.getContext().getServerAddress());
            DatastoreMO dsMo = new DatastoreMO(serverContext, dsmor);
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

    //get oriented datastore capacity
    public static Map<String, Object> getCapacityOnVmfs(String dsname) {
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("msg", "success");
        _logger.info("==start get oriented datastore capacity==");
        try {
            VmwareContext vmwareContext = TestVmwareContextFactory.getContext("10.143.132.248", 443,"administrator@vsphere.local", "Pbu4@123");
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
            VmwareContext vmwareContext = TestVmwareContextFactory.getContext("10.143.132.248", 443,"administrator@vsphere.local", "Pbu4@123");
            DatacenterMO dcMo = new DatacenterMO(vmwareContext, "Datacenter");
            DatastoreMO dsMo = new DatastoreMO(vmwareContext, dcMo.findDatastore(dsname));
            List<ManagedObjectReference> hosts = dcMo.findHost("10.143.132.17");
            ManagedObjectReference managedObjectReference = null;
            if (hosts != null && hosts.size() > 0) {
                managedObjectReference = hosts.get(0);
            }
            HostMO hostMo = new HostMO(vmwareContext, managedObjectReference);
            //todo get oriented LUN  (参数设置存在问题)
            String devicePath = "/vmfs/devices/disks/t10.ATA_____WDC_WD1003FBYX2D01Y7B1________________________WD2DWCAW35431438";
            Long totalSectors = add_capacity * 1L * ToolUtils.GI;
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
            VmwareContext vmwareContext = TestVmwareContextFactory.getContext("10.143.132.248",443, "administrator@vsphere.local", "Pbu4@123");
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
    public String createNfsDatastore(String serverHost, String exportPath, String nfsName, String accessMode, List<String> mountHosts,String type) {
        String result = "success";
        //exportPath = "/volume1/TESTNFS";
        //需要判断nfs版本，如果是v4.1要将 Kerberos 安全功能与 NFS 4.1 结合使用，请启用 Kerberos 并选择适当的 Kerberos 模型。
        //nfsName = "lqnfsv3.1";
        //remoteHostNames equal remoteHost(v3)
        //serverHost = "10.143.132.187";
        //mountHost = "10.143.132.17";
        //logicPort = 0;
        _logger.info("start creat nfs datastore");
        accessMode = StringUtils.isEmpty(accessMode) || accessMode.equals("readWrite") ? "readWrite" : "readOnly";
        try {
            VmwareContext vmwareContext = TestVmwareContextFactory.getContext("10.143.132.248", 443,"administrator@vsphere.local", "Pbu4@123");
            DatacenterMO dcMo = new DatacenterMO(vmwareContext, "Datacenter");
            List<ManagedObjectReference> hosts = null;
            if (mountHosts != null && mountHosts.size() > 0) {
                ManagedObjectReference managedObjectReference = null;
                for (int i = 0; i < mountHosts.size(); i++) {
                    String mountHost = mountHosts.get(i);
                    hosts = dcMo.findHost(mountHost);
                    if (hosts != null && hosts.size() > 0) {
                        managedObjectReference = hosts.get(0);
                        HostMO hostMO = new HostMO(vmwareContext, managedObjectReference);
                        HostDatastoreSystemMO hostDatastoreSystemMO = hostMO.getHostDatastoreSystemMO();
                        //todo logic port param needed ?
                        hostDatastoreSystemMO.createNfsDatastore(serverHost, 0, exportPath, nfsName, accessMode, type);
                    } else {
                        result = "failed";
                        _logger.error("can not find target host!");
                    }
                }
            } else {
                result = "failed";
                _logger.error("{createNfsDatastore/mountHosts} params error");
            }
            _logger.info("end creat nfs datastore");
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

    //得到主机对应的可用LUN 20200918objectId
    public Map<String, Object> getLunsOnHost(String hostObjectId, int capacity) throws Exception {
        Map<String, Object> remap = null;
        HostScsiDisk candidateHostScsiDisk = null;
        try {
            String serverguid = vcConnectionHelper.objectID2Serverguid(hostObjectId);
            VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

            RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
            ManagedObjectReference objmor = vcConnectionHelper.objectID2MOR(hostObjectId);
            HostMO hostMo = new HostMO(vmwareContext, objmor);

            if (hostMo != null) {
                //get available LUN
                HostDatastoreSystemMO hostDatastoreSystem = hostMo.getHostDatastoreSystemMO();
                List<HostScsiDisk> hostScsiDisks = hostDatastoreSystem.queryAvailableDisksForVmfs();
                candidateHostScsiDisk = getObjectLuns(hostScsiDisks, capacity);
                remap = new HashMap<>();
                remap.put("host", hostMo);
                remap.put("hostScsiDisk", candidateHostScsiDisk);
            } else {
                throw new Exception("host:" + hostObjectId + " non-existent。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("host:" + hostObjectId + " error:", e);
            throw e;
        }
        return remap;
//
    }

    //得到集群下所有主机对应的可用LUN 20200918objectId
    public Map<String, Object> getLunsOnCluster(String clusterObjectId, int capacity) throws Exception {
        Map<String, Object> remap = null;
        HostScsiDisk candidateHostScsiDisk = null;
        try {
            String serverguid = vcConnectionHelper.objectID2Serverguid(clusterObjectId);
            VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

            RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());

            ManagedObjectReference objmor = vcConnectionHelper.objectID2MOR(clusterObjectId);
            ClusterMO cl1 = new ClusterMO(vmwareContext, objmor);

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
                    throw new Exception("cluster:" + clusterObjectId + " non-existent LUN。");
                }
            } else {
                throw new Exception("cluster:" + clusterObjectId + " non-existent HOST。");
            }

        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("cluster get lun error:", e);
            throw e;
        }
        return remap;
//
    }

    //得到主机对应的可用LUN 20200918objectId
    public HostScsiDisk getObjectLuns(List<HostScsiDisk> hostScsiDisks, int capacity) throws Exception {
        HostScsiDisk candidateHostScsiDisk = null;
        try {
            if (hostScsiDisks != null && hostScsiDisks.size() > 0 && capacity > 0) {
                long oldcapacity = capacity * 1L * ToolUtils.GI;
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

    //创建vmfs存储 20200918objectId
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
                        long totalSectors = capacity * 1L * ToolUtils.GI / objhsd.getCapacity().getBlockSize();
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

                            String objectId = vcConnectionHelper.MOR2ObjectID(dsMo.getMor(), dsMo.getContext().getServerAddress());

                            dataStoremap.put("name", dsMo.getName());
                            dataStoremap.put("id", dsMo.getMor().getValue());
                            dataStoremap.put("objectId", objectId);
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

    //vmfs存储打标记 20200918objectId
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

    //将存储挂载到集群下其它主机 20200918objectId
    public void mountVmfsOnCluster(String datastoreStr, String clusterObjectId, String hostObjectId) throws Exception {
        try {
            if (StringUtils.isEmpty(datastoreStr)) {
                _logger.info("datastore:" + datastoreStr + " is null");
                return;
            }
            if (StringUtils.isEmpty(hostObjectId) && StringUtils.isEmpty(clusterObjectId)) {
                _logger.info("host:" + hostObjectId + " and cluster:" + clusterObjectId + " is null");
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

            //集群下的所有主机
            List<Pair<ManagedObjectReference, String>> hosts = getHostsOnCluster(clusterObjectId, hostObjectId);

            String serverguid = null;
            if(!StringUtils.isEmpty(clusterObjectId)) {
                serverguid = vcConnectionHelper.objectID2Serverguid(clusterObjectId);
            }else if(!StringUtils.isEmpty(hostObjectId)) {
                serverguid = vcConnectionHelper.objectID2Serverguid(hostObjectId);
            }
            if(!StringUtils.isEmpty(serverguid)) {
                VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);
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

    //挂载存储 20200918objectId
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
                    _logger.info("mount Vmfs success:"+volume.getName() + " : " + hostMO.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("mount Vmfs Volume error:", e);
        }
    }

    //在主机上扫描卷和Datastore 20200918objectId
    public void scanDataStore(String clusterObjectId, String hostObjectId) throws Exception {
        try {
            String serverguid = null;
            if(!StringUtils.isEmpty(clusterObjectId)) {
                serverguid = vcConnectionHelper.objectID2Serverguid(clusterObjectId);
            }else if(!StringUtils.isEmpty(hostObjectId)) {
                serverguid = vcConnectionHelper.objectID2Serverguid(hostObjectId);
            }
            if(!StringUtils.isEmpty(serverguid)) {
                VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);
                //集群下的所有主机
                List<Pair<ManagedObjectReference, String>> hosts = getHostsOnCluster(clusterObjectId, hostObjectId);

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
     * @Param [datastore_name, vm_name, rdmdevicename, size]
     * @Return void
     **/
    public void createDisk(String datastore_name, String vm_name, String rdmdevicename, int size) throws Exception {
        VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
        for (VmwareContext vmwareContext : vmwareContexts) {
            DatastoreMO dsMo = new DatastoreMO(vmwareContext, datastore_name);
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

    //将存储挂载到集群下其它主机 20200918objectId
    public void mountNfsOnCluster(String dataStoreObjectId, List<Map<String,String>> clusters, List<Map<String,String>> hosts, String mountType) throws Exception {
        try {
            if (StringUtils.isEmpty(dataStoreObjectId)) {
                _logger.info("dataStore object id:" + dataStoreObjectId + " is null");
                return;
            }
            if (clusters == null && hosts == null) {
                _logger.info("host:" + hosts + " and cluster:" + clusters + " is null");
                return;
            }

            List<String> hostlist = null;
            if (hosts != null) {
                hostlist = new ArrayList<>();
                if(hosts!=null && hosts.size()>0){
                    for(Map<String,String> hostmap:hosts){
                        hostlist.add(hostmap.get("hostName"));
                    }
                }
            } else if (clusters != null) {
                //取得没有挂载这个存储的所有主机，以方便后面过滤
                hostlist = getUnmoutHostsOnCluster(dataStoreObjectId, clusters);
            }

            if (hostlist != null && hostlist.size() > 0) {
                String serverguid = vcConnectionHelper.objectID2Serverguid(dataStoreObjectId);
                VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

                RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
                //集群下的所有主机
                ManagedObjectReference dsmor = vcConnectionHelper.objectID2MOR(dataStoreObjectId);
                DatastoreMO dsmo = new DatastoreMO(vmwareContext, dsmor);

                for (String hostName : hostlist) {

                    HostMO hostmo = rootFsMO.findHost(hostName);
                    _logger.info("Host name: " + hostmo.getName());
                    //只挂载其它的主机
                    mountNfs(dsmo, hostmo, mountType);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("mount Vmfs On Cluster error:", e);
            throw e;
        }
    }

    //挂载Nfs存储 20200918objectId
    public void mountNfs(DatastoreMO dsmo, HostMO hostMO, String mountType) throws Exception {
        try {
            if (dsmo == null) {
                _logger.info("datastore is null");
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
            //挂载NFS
            NasDatastoreInfo nasdsinfo = (NasDatastoreInfo) dsmo.getInfo();
            hostMO.getHostDatastoreSystemMO().createNfsDatastore(nasdsinfo.getNas().getRemoteHost(), 0, nasdsinfo.getNas().getRemotePath(), dsmo.getMor().getValue(), mountType,null);
            _logger.info("mount nfs success:" + hostMO.getName() + ":" + dsmo.getName());
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("mount nfs error:", e);
        }
    }

    //Get host's vmKernel IP,only provisioning provisioning
    public String getVmKernelIpByHostObjectId(String hostObjectId) throws Exception {
        String listStr = "";
        try {
            String serverguid = vcConnectionHelper.objectID2Serverguid(hostObjectId);
            VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

            RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
            //取得该存储下所有已经挂载的主机ID
            ManagedObjectReference objmor = vcConnectionHelper.objectID2MOR(hostObjectId);
            HostMO hostmo = new HostMO(vmwareContext, objmor);
            if (hostmo != null) {
                List<VirtualNicManagerNetConfig> nics = hostmo.getHostVirtualNicManagerNetConfig();
                if(nics!=null && nics.size()>0){
                    for(VirtualNicManagerNetConfig nic:nics){
                        if(nic.getNicType().equals("vSphereProvisioning")){
                            List<Map<String, Object>> lists = new ArrayList<>();

                            List<HostVirtualNic> subnics = nic.getCandidateVnic();
                            if(subnics!=null && subnics.size()>0){
                                for(HostVirtualNic subnic:subnics){
                                    if(nic.getSelectedVnic().contains(subnic.getKey())){
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("device", subnic.getDevice());
                                        map.put("key", subnic.getKey());
                                        map.put("portgroup", subnic.getPortgroup());
                                        map.put("ipAddress", subnic.getSpec().getIp().getIpAddress());
                                        map.put("mac", subnic.getSpec().getMac());
                                        map.put("port", subnic.getPort());

                                        lists.add(map);
                                    }
                                }
                            }

                            if (lists.size() > 0) {
                                listStr = gson.toJson(lists);
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

    public List<PbmProfile> getAllSelfPolicyInallcontext(){
        List<PbmProfile> pbmProfiles=new ArrayList<>();
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                pbmProfiles.addAll(getAllSelfPolicy(vmwareContext));
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("获取所有虚拟机存储策略出错");
        }
        return pbmProfiles;
    }
    private List<PbmProfile> getAllSelfPolicy(VmwareContext vmwareContext){
        List<PbmProfile> pbmProfiles=new ArrayList<>();
        try {
            PbmServiceInstanceContent spbmsc;
            // Get PBM Profile Manager & Associated Capability Metadata
            spbmsc = vmwareContext.getPbmServiceContent();
            ManagedObjectReference profileMgr = spbmsc.getProfileManager();
            //query profiles
            List<PbmProfileId> pbmProfileIds = vmwareContext.getPbmService().pbmQueryProfile(profileMgr, PbmUtil.getStorageResourceType(), null);

            List<PbmProfile> pbmprofiles = vmwareContext.getPbmService().pbmRetrieveContent(profileMgr, pbmProfileIds);

            //获取通过插件创建的策略
            for (PbmProfile profile : pbmprofiles) {
                if (POLICY_DESC.equalsIgnoreCase(profile.getDescription())) {
                    pbmProfiles.add(profile);
                }
            }
        } catch (com.vmware.pbm.InvalidArgumentFaultMsg | RuntimeFaultFaultMsg invalidArgumentFaultMsg) {
            invalidArgumentFaultMsg.printStackTrace();
            _logger.error("获取虚拟机存储策略出错");
        }
        return pbmProfiles;
    }

    public List<TagModel> getAllTagsByCategoryId(String categoryid,SessionHelper sessionHelper){
        List<TagModel> tagList=new ArrayList<>();
        TaggingWorkflow taggingWorkflow=new TaggingWorkflow(sessionHelper);
        List<String> tags=taggingWorkflow.listTagsForCategory(categoryid);
        for (String tagid:tags){
            tagList.add(taggingWorkflow.getTag(tagid));
        }
        return tagList;

    }

    public String getCategoryID(SessionHelper sessionHelper) throws Exception {
        String categoryid="";

        TaggingWorkflow taggingWorkflow=new TaggingWorkflow(sessionHelper);

        List<String> categorylist=taggingWorkflow.listTagCategory();
        for (String category:categorylist){
            CategoryModel categoryModel=taggingWorkflow.getTagCategory(category);
            if(categoryModel.getName().equalsIgnoreCase(CATEGORY_NAME)){
                categoryid=categoryModel.getId();
            }
        }
        if ("".equalsIgnoreCase(categoryid)){
            //创建category
            CategoryTypes.CreateSpec createSpec = new CategoryTypes.CreateSpec();
            createSpec.setName(CATEGORY_NAME);

            createSpec.setCardinality(CategoryModel.Cardinality.SINGLE);

            Set<String> associableTypes = new HashSet<String>(); // empty hash set
            associableTypes.add("Datastore");
            createSpec.setAssociableTypes(associableTypes);
            categoryid=taggingWorkflow.createTagCategory(createSpec);
        }

        return categoryid;
    }

    public void createPbmProfileInAllContext(String categoryName,String tagName){
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                createPbmProfile(vmwareContext, categoryName, tagName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("在所有实例上创建虚拟机存储策略出错");
        }
    }

    private void createPbmProfile(VmwareContext vmwareContext,String categoryName,String tagName) throws RuntimeFaultFaultMsg, InvalidArgumentFaultMsg, com.vmware.vim25.RuntimeFaultFaultMsg {
        //String tagCategoryName="dme";
        //String profileName="mytestprofile";
        PbmServiceInstanceContent spbmsc;
        // Get PBM Profile Manager & Associated Capability Metadata
        spbmsc = vmwareContext.getPbmServiceContent();
        ManagedObjectReference profileMgr = spbmsc.getProfileManager();
        // Get PBM Supported Capability Metadata
        List<PbmCapabilityMetadataPerCategory> metadata =
                vmwareContext.getPbmService().pbmFetchCapabilityMetadata(profileMgr,
                        PbmUtil.getStorageResourceType(), null);

        // Step 1: Create Property Instance with tags from the specified Category
        PbmCapabilityMetadata tagCategoryInfo =
                PbmUtil.getTagCategoryMeta(categoryName, metadata);
        if (tagCategoryInfo == null)
            throw new InvalidArgumentFaultMsg(
                    "Specified Tag Category does not exist", null);
        // Fetch Property Metadata of the Tag Category
        List<PbmCapabilityPropertyMetadata> propMetaList =
                tagCategoryInfo.getPropertyMetadata();
        PbmCapabilityPropertyMetadata propMeta = propMetaList.get(0);
        // Create a New Property Instance based on the Tag Category ID
        PbmCapabilityPropertyInstance prop = new PbmCapabilityPropertyInstance();
        prop.setId(propMeta.getId());
        // Fetch Allowed Tag Values Metadata
        PbmCapabilityDiscreteSet tagSetMeta =
                (PbmCapabilityDiscreteSet) propMeta.getAllowedValue();
        if (tagSetMeta == null || tagSetMeta.getValues().isEmpty())
            throw new com.vmware.vim25.RuntimeFaultFaultMsg("Specified Tag Category '"
                    + categoryName + "' does not have any associated tags", null);
        // Create a New Discrete Set for holding Tag Values
        PbmCapabilityDiscreteSet tagSet = new PbmCapabilityDiscreteSet();
        for (Object obj : tagSetMeta.getValues()) {
            if (tagName.equalsIgnoreCase(((PbmCapabilityDescription) obj).getValue().toString()))
            tagSet.getValues().add(((PbmCapabilityDescription) obj).getValue());
        }
        prop.setValue(tagSet);


        // Step 2: Associate Property Instance with a Rule
        PbmCapabilityConstraintInstance rule =
                new PbmCapabilityConstraintInstance();
        rule.getPropertyInstance().add(prop);

        // Step 3: Associate Rule with a Capability Instance
        PbmCapabilityInstance capability = new PbmCapabilityInstance();
        capability.setId(tagCategoryInfo.getId());
        capability.getConstraint().add(rule);

        // Step 4: Add Rule to a RuleSet
        PbmCapabilitySubProfile ruleSet = new PbmCapabilitySubProfile();
        ruleSet.getCapability().add(capability);

        // Step 5: Add Rule-Set to Capability Constraints
        PbmCapabilitySubProfileConstraints constraints =
                new PbmCapabilitySubProfileConstraints();
        ruleSet.setName("Rule-Set" + (constraints.getSubProfiles().size() + 1));
        constraints.getSubProfiles().add(ruleSet);

        // Step 6: Build Capability-Based Profile
        PbmCapabilityProfileCreateSpec spec =
                new PbmCapabilityProfileCreateSpec();
        spec.setName(tagName);
        spec.setDescription(POLICY_DESC);
        spec.setResourceType(PbmUtil.getStorageResourceType());
        spec.setConstraints(constraints);
    }

    public void createTag(String tagName,SessionHelper sessionHelper){

        try {
            TaggingWorkflow taggingWorkflow = new TaggingWorkflow(sessionHelper);
            taggingWorkflow.createTag(tagName, "", getCategoryID(sessionHelper));
            sessionHelper.logout();
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("创建tag出错");
        }
    }

    private void removePbmProfile(VmwareContext vmwareContext,List<PbmProfile> pbmProfiles) throws RuntimeFaultFaultMsg {
        List<PbmProfileId> pbmProfileIds=new ArrayList<>();
        for (PbmProfile profile:pbmProfiles){
            pbmProfileIds.add(profile.getProfileId());
        }
        PbmServiceInstanceContent spbmsc;
        // Get PBM Profile Manager & Associated Capability Metadata
        spbmsc = vmwareContext.getPbmServiceContent();
        ManagedObjectReference profileMgr = spbmsc.getProfileManager();
                vmwareContext.getPbmService().pbmDelete(profileMgr,
                        pbmProfileIds);
    }

    public void removePbmProfileInAllContext(List<PbmProfile> pbmProfiles){
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                removePbmProfile(vmwareContext, pbmProfiles);
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("删除所有虚拟机存储策略出错");
        }
    }

    public void removeAllTags(List<TagModel> tagModels,SessionHelper sessionHelper){
        TaggingWorkflow taggingWorkflow=new TaggingWorkflow(sessionHelper);
        for (TagModel tagModel:tagModels){
            taggingWorkflow.deleteTag(tagModel.getId());
        }

    }

    //主机配置iscsi
    public void configureIscsi(String hostObjectId,Map<String, String> vmKernel,List<Map<String, Object>> ethPorts) throws Exception{
        try {
            if(ethPorts==null){
                _logger.error("configure Iscsi error:ethPorts is null.");
                throw new Exception("configure Iscsi error:ethPorts is null.");
            }
            if(vmKernel==null){
                _logger.error("configure Iscsi error:vmKernel is null.");
                throw new Exception("configure Iscsi error:vmKernel is null.");
            }
            if(StringUtils.isEmpty(hostObjectId)){
                _logger.error("configure Iscsi error:host ObjectId is null.");
                throw new Exception("configure Iscsi error:host ObjectId is null.");
            }

            String serverguid = vcConnectionHelper.objectID2Serverguid(hostObjectId);
            VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

            RootFsMO rootFsMO = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
            //取得该存储下所有已经挂载的主机ID
            ManagedObjectReference objmor = vcConnectionHelper.objectID2MOR(hostObjectId);
            HostMO hostmo = new HostMO(vmwareContext, objmor);
            //查找对应的iscsi适配器
            String iscsiHbaDevice = null;
            List<HostHostBusAdapter> hbas = hostmo.getHostStorageSystemMO().getStorageDeviceInfo().getHostBusAdapter();
            for (HostHostBusAdapter hba : hbas) {
                if (hba instanceof HostInternetScsiHba) {
                    HostInternetScsiHba iscsiHba = (HostInternetScsiHba) hba;
                    if(!StringUtils.isEmpty(iscsiHba.getDevice())){
                        iscsiHbaDevice = iscsiHba.getDevice();
                        break;
                    }
                }
            }
            if(StringUtils.isEmpty(iscsiHbaDevice)){
                _logger.error("find iscsi Hba Device error:No iSCSI adapter found");
                throw new Exception("find iscsi Hba Device error:No iSCSI adapter found");
            }
            //得到vmKernel适配器
            //网络端口邦定，将vmKernelDevice邦定到iscsiHbaDevice
            _logger.info("iscsiHbaDevice:"+iscsiHbaDevice);
            boundVmKernel(hostmo, vmKernel, iscsiHbaDevice);
            //添加发现目标
            addIscsiSendTargets(hostmo, ethPorts, iscsiHbaDevice);
            _logger.info("configure Iscsi success! iscsiHbaDevice:"+iscsiHbaDevice+" host:"+hostmo.getName());
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("vmware error:", e);
            throw e;
        }

    }
    //网络端口邦定，将vmKernelDevice邦定到iscsiHbaDevice
    public void boundVmKernel(HostMO hostmo,Map<String, String> vmKernel,String iscsiHbaDevice) throws Exception{
        try {
            if(vmKernel==null){
                _logger.error("configure Iscsi error:vmKernel is null.");
                throw new Exception("configure Iscsi error:vmKernel is null.");
            }
            if(hostmo==null){
                _logger.error("configure Iscsi error:host is null.");
                throw new Exception("configure Iscsi error:host is null.");
            }
            if(StringUtils.isEmpty(iscsiHbaDevice)){
                _logger.error("find iscsi Hba Device error:No iSCSI adapter found");
                throw new Exception("find iscsi Hba Device error:No iSCSI adapter found");
            }
            //得到vmKernel适配器
            String vmKernelDevice = ToolUtils.getStr(vmKernel.get("device"));
            if(StringUtils.isEmpty(vmKernelDevice)){
                _logger.error("find vmKernel Device error:No vmKernel adapter found");
                throw new Exception("find vmKernel Device error:No vmKernel adapter found");
            }
            _logger.info("vmKernelDevice:"+vmKernelDevice);
            //网络端口邦定，将vmKernelDevice邦定到iscsiHbaDevice
            hostmo.getIscsiManagerMO().bindVnic(iscsiHbaDevice,vmKernelDevice);
            _logger.info("bind Vnic success! iscsiHbaDevice:"+iscsiHbaDevice+" vmKernelDevice:"+vmKernelDevice);
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("vmware error:", e);
            throw e;
        }
    }
    //添加发现目标
    public void addIscsiSendTargets(HostMO hostmo,List<Map<String, Object>> ethPorts,String iscsiHbaDevice) throws Exception{
        try {
            if(ethPorts==null){
                _logger.error("configure Iscsi error:ethPorts is null.");
                throw new Exception("configure Iscsi error:ethPorts is null.");
            }
            if(hostmo==null){
                _logger.error("configure Iscsi error:host is null.");
                throw new Exception("configure Iscsi error:host is null.");
            }
            if(StringUtils.isEmpty(iscsiHbaDevice)){
                _logger.error("find iscsi Hba Device error:No iSCSI adapter found");
                throw new Exception("find iscsi Hba Device error:No iSCSI adapter found");
            }

            //添加发现目标
            if(ethPorts!=null && ethPorts.size()>0){
                List<HostInternetScsiHbaSendTarget> targets = new ArrayList<>();
                for(Map<String, Object> ethPortInfo:ethPorts){
                    //组装发现目标
                    HostInternetScsiHbaSendTarget target = new HostInternetScsiHbaSendTarget();
                    target.setAddress(ToolUtils.getStr(ethPortInfo.get("mgmtIp")));
                    targets.add(target);
                }
                if(targets.size()>0){
                    //向iscsi添加目标
                    hostmo.getHostStorageSystemMO().addInternetScsiSendTargets(iscsiHbaDevice,targets);
                    _logger.info("add Iscsi Send Targets success! iscsiHbaDevice:"+iscsiHbaDevice+" targets:"+gson.toJson(targets));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("vmware error:", e);
            throw e;
        }

    }

    public static void main(String[] args) {
//        try {
//            Gson gson = new Gson();
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

//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        String result = "success";
        String exportPath = "/volume1/TESTNFS";
        //需要判断nfs版本，如果是v4.1要将 Kerberos 安全功能与 NFS 4.1 结合使用，请启用 Kerberos 并选择适当的 Kerberos 模型。
        String nfsName = "lqnfsv3.1";
        //remoteHostNames equal remoteHost(v3)
        String serverHost = "10.143.132.187";
        String mountHost1 = "10.143.132.17";
        String mountHost2 = "10.143.132.20";
        List<String> mountHosts = new ArrayList<>();
        mountHosts.add(mountHost1);
        mountHosts.add(mountHost2);
        String type = "NFS";
        //logicPort = 0;
        _logger.info("start creat nfs datastore");
        String accessMode = "";
        accessMode = StringUtils.isEmpty(accessMode) || accessMode.equals("readWrite") ? "readWrite" : "readOnly";
        try {
            VmwareContext vmwareContext = TestVmwareContextFactory.getContext("10.143.132.248", 443,"administrator@vsphere.local", "Pbu4@123");
            DatacenterMO dcMo = new DatacenterMO(vmwareContext, "Datacenter");
            List<ManagedObjectReference> hosts = null;
            if (mountHosts != null && mountHosts.size() > 0) {
                ManagedObjectReference managedObjectReference = null;
                for (int i = 0; i < mountHosts.size(); i++) {
                    String mountHost = mountHosts.get(i);
                    hosts = dcMo.findHost(mountHost);
                    if (hosts != null && hosts.size() > 0) {
                        managedObjectReference = hosts.get(0);
                        HostMO hostMO = new HostMO(vmwareContext, managedObjectReference);
                        HostDatastoreSystemMO hostDatastoreSystemMO = hostMO.getHostDatastoreSystemMO();
                        hostDatastoreSystemMO.createNfsDatastore(serverHost, 0, exportPath, nfsName, accessMode, type);
//                        if (i == 0) {
//                            //todo logic port param needed ?
//                        }
//                        if (i > 0) {
//                            DatastoreMO dsMo = new DatastoreMO(vmwareContext, dcMo.findDatastore(nfsName));
//                            mountNfs(dsMo, hostMO, type);
//                        }
                    } else {
                        result = "failed";
                        _logger.error("can not find target host!");
                    }
                }
            } else {
                result = "failed";
                _logger.error("{createNfsDatastore/mountHosts} params error");
            }
            _logger.info("end creat nfs datastore");
        } catch (Exception e) {
            result = "failed";
            _logger.error("vmware error:", e);
        }
    }
}

