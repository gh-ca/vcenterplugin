package com.dmeplugin.dmestore.utils;

import com.dmeplugin.dmestore.entity.DmeVmwareRelation;
import com.dmeplugin.dmestore.entity.VCenterInfo;
import com.dmeplugin.dmestore.services.DmeConstants;
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
import com.vmware.vim.binding.vim.HostSystem;
import com.vmware.vim.binding.vim.ServiceInstance;
import com.vmware.vim.binding.vim.ServiceInstanceContent;
import com.vmware.vim.binding.vim.SessionManager;
import com.vmware.vim.binding.vim.fault.InvalidLocale;
import com.vmware.vim.binding.vim.fault.InvalidLogin;
import com.vmware.vim.binding.vim.version.version10;
import com.vmware.vim.binding.vmodl.reflect.ManagedMethodExecuter;
import com.vmware.vim.vmomi.client.Client;
import com.vmware.vim.vmomi.client.http.HttpClientConfiguration;
import com.vmware.vim.vmomi.client.http.HttpConfiguration;
import com.vmware.vim.vmomi.client.http.impl.AllowAllThumbprintVerifier;
import com.vmware.vim.vmomi.client.http.impl.HttpConfigurationImpl;
import com.vmware.vim.vmomi.core.types.VmodlContext;
import com.vmware.vim25.*;
import com.vmware.vim25.InvalidArgumentFaultMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class VCSDKUtils {

    private static final String POLICY_DESC = "policy created by dme";

    public static final String CATEGORY_NAME = "DME Service Level";

    private VCConnectionHelper vcConnectionHelper;

    public VCConnectionHelper getVcConnectionHelper() {
        return vcConnectionHelper;
    }

    public void setVcConnectionHelper(VCConnectionHelper vcConnectionHelper) {
        this.vcConnectionHelper = vcConnectionHelper;
    }

    private final static Logger logger = LoggerFactory.getLogger(VCSDKUtils.class);

    private static final Class<?> VERSION = version10.class;

    private static VmodlContext context;

    private Gson gson = new Gson();
    /**
     *得到所有存储的info
     **/
    public String getAllVmfsDataStoreInfos(String storeType) throws Exception {
        String listStr = "";
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                RootFsMO rootFsMo = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
                List<Pair<ManagedObjectReference, String>> dss = rootFsMo.getAllDatastoreOnRootFs();
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
                        }else if(storeType.equals(ToolUtils.STORE_TYPE_VMFS) &&
                                ds1.getSummary().getType().equals(ToolUtils.STORE_TYPE_VMFS)){
                            VmfsDatastoreInfo vmfsDatastoreInfo = ds1.getVmfsDatastoreInfo();
                            List<HostScsiDiskPartition> extent = vmfsDatastoreInfo.getVmfs().getExtent();
                            List<String> wwnList = new ArrayList<>();
                            if(null != extent){
                                for(HostScsiDiskPartition hostScsiDiskPartition : extent){
                                    String wwn = hostScsiDiskPartition.getDiskName().replace("naa.", "");
                                    wwnList.add(wwn);
                                }
                                dsmap.put("vmfsWwnList", wwnList);
                            }
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
            logger.error("vmware error:", e);
            throw e;
        }
        return listStr;
    }
    /**
     *得到所有主机的ID与name
     **/
    public String getAllHosts() throws Exception {
        logger.info("get all hosts start");
        String listStr = "";
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                RootFsMO rootFsMo = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
                List<Pair<ManagedObjectReference, String>> hosts = rootFsMo.getAllHostOnRootFs();
                if (hosts != null && hosts.size() > 0) {
                    List<Map<String, String>> lists = new ArrayList<>();
                    for (Pair<ManagedObjectReference, String> host : hosts) {
                        HostMO host1 = new HostMO(vmwareContext, host.first());

                        Map<String, String> map = new HashMap<>();
                        String objectId = vcConnectionHelper.MOR2ObjectID(host1.getMor(), vmwareContext.getServerAddress());
                        map.put("hostId", objectId);
                        map.put("objectId", objectId);
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
            logger.error("vmware error:", e);
            throw e;
        }
        logger.info("get all hosts end");
        return listStr;
    }

    public String findHostById(String objectId) throws Exception {
        VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
        List<Map<String, String>> lists = new ArrayList<>();
        for (VmwareContext vmwareContext : vmwareContexts) {
            RootFsMO rootFsMo = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
            HostMO hostMo = rootFsMo.findHostById(objectId);
            Map<String, String> map = new HashMap<>();
            map.put("hostId", objectId);
            map.put("objectId", objectId);
            map.put("hostName", hostMo.getName());
            lists.add(map);
        }

        return gson.toJson(lists);
    }
    /**
     *得到所有集群的id与name
     **/
    public String getAllClusters() throws Exception {
        String listStr = "";
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                RootFsMO rootFsMo = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
                List<Pair<ManagedObjectReference, String>> cls = rootFsMo.getAllClusterOnRootFs();
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
            logger.error("vmware error:", e);
            throw e;
        }
        return listStr;
    }
    /**
    *得到所有主机的ID与name 除去已经挂载了当前存储的主机  20200918objectId
    **/
    public String getHostsByDsObjectId(String dataStoreObjectId) throws Exception {
        return getHostsByDsObjectId(dataStoreObjectId, false);
    }
    /**
     *得到所有主机的ID与name 除去没有挂载了当前存储的主机
     **/
    public String getMountHostsByDsObjectId(String dataStoreObjectId) throws Exception {
        return getHostsByDsObjectId(dataStoreObjectId, true);
    }
    /**
     *得到所有集群的ID与name 除去已经挂载了当前存储的集群  扫描集群下所有主机，只要有一个主机没挂当前存储就要显示，只有集群下所有主机都挂载了该存储就不显示
     **/
    public String getClustersByDsObjectId(String dataStoreObjectId) throws Exception {
        String listStr = "";
        try {
            String serverguid = vcConnectionHelper.objectID2Serverguid(dataStoreObjectId);
            VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

            RootFsMO rootFsMo = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
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
            List<Pair<ManagedObjectReference, String>> cls = rootFsMo.getAllClusterOnRootFs();
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
            logger.error("vmware error:", e);
            throw e;
        }
        return listStr;
    }
    /**
     *得到所有集群的ID与name 只要集群下有主机挂载了该存储就显示
     **/
    public String getMountClustersByDsObjectId(String dataStoreObjectId) throws Exception {
        String listStr = "";
        try {
            String serverguid = vcConnectionHelper.objectID2Serverguid(dataStoreObjectId);
            VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

            RootFsMO rootFsMo = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
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
            //扫描集群下所有主机，只有集群下所有主机都没有挂载了该存储就不显示
            List<Pair<ManagedObjectReference, String>> cls = rootFsMo.getAllClusterOnRootFs();
            if (cls != null && cls.size() > 0) {
                List<Map<String, String>> lists = new ArrayList<>();
                for (Pair<ManagedObjectReference, String> cl : cls) {
                    boolean isMount = false;
                    ClusterMO cl1 = new ClusterMO(vmwareContext, cl.first());

                    List<Pair<ManagedObjectReference, String>> hosts = cl1.getClusterHosts();
                    if (hosts != null && hosts.size() > 0) {
                        for (Pair<ManagedObjectReference, String> host : hosts) {
                            HostMO host1 = new HostMO(vmwareContext, host.first());
                            if (mounthostids.contains(host1.getMor().getValue())) {
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
            logger.error("vmware getMountClustersByDsObjectId error:", e);
            throw e;
        }
        return listStr;
    }
    /**
     *得到所有存储 除去已经挂载了当前主机的存储 20200918objectId
     **/
    public String getDataStoresByHostObjectId(String hostObjectId, String dataStoreType) throws Exception {
        String listStr = "";
        try {
            String serverguid = vcConnectionHelper.objectID2Serverguid(hostObjectId);
            VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

            RootFsMO rootFsMo = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
            //取得该存储下所有已经挂载的主机ID
            ManagedObjectReference objmor = vcConnectionHelper.objectID2MOR(hostObjectId);
            HostMO hostmo = new HostMO(vmwareContext, objmor);
            String objHostId = null;
            if (hostmo != null) {
                objHostId = hostmo.getMor().getValue();
            }
            logger.info("objHostId==" + objHostId);
            //取得所有主机，并通过mounthostids进行过滤，过滤掉已经挂载的主机
            List<Pair<ManagedObjectReference, String>> dss = rootFsMo.getAllDatastoreOnRootFs();
            if (dss != null && dss.size() > 0) {
                List<Map<String, Object>> lists = new ArrayList<>();
                for (Pair<ManagedObjectReference, String> ds : dss) {
                    DatastoreMO dsmo = new DatastoreMO(vmwareContext, ds.first());
                    if (dsmo != null && dataStoreType.equals(dsmo.getSummary().getType())) {
                        logger.info("dsmo.getName==" + dsmo.getName());
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
            logger.error("vmware error:", e);
            throw e;
        }
        return listStr;
    }
    /**
     *得到所有存储 除去已经挂载了当前集群的存储 扫描集群下所有主机，只要有一个主机没挂当前存储就要显示，只有集群下所有主机都挂载了该存储就不显示
     **/
    public String getDataStoresByClusterObjectId(String clusterObjectId, String dataStoreType) throws Exception {
        String listStr = "";
        try {
            String serverguid = vcConnectionHelper.objectID2Serverguid(clusterObjectId);
            VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

            RootFsMO rootFsMo = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
            //取得该存储下所有已经挂载的主机ID
            List<String> hostids = new ArrayList<>();
            ManagedObjectReference objmor = vcConnectionHelper.objectID2MOR(clusterObjectId);
            ClusterMO clusterMo = new ClusterMO(vmwareContext, objmor);
            String objHostId = null;
            if (clusterMo != null) {
                List<Pair<ManagedObjectReference, String>> hosts = clusterMo.getClusterHosts();
                if (hosts != null && hosts.size() > 0) {
                    for (Pair<ManagedObjectReference, String> host : hosts) {
                        HostMO host1 = new HostMO(vmwareContext, host.first());
                        hostids.add(host1.getMor().getValue());
                    }
                }
            }
            logger.info("objHostId==" + hostids);
            //取得所有主机，并通过mounthostids进行过滤，过滤掉已经挂载的主机
            List<Pair<ManagedObjectReference, String>> dss = rootFsMo.getAllDatastoreOnRootFs();
            if (dss != null && dss.size() > 0) {
                List<Map<String, Object>> lists = new ArrayList<>();
                for (Pair<ManagedObjectReference, String> ds : dss) {
                    DatastoreMO dsmo = new DatastoreMO(vmwareContext, ds.first());
                    if (dsmo != null && dataStoreType.equals(dsmo.getSummary().getType())) {
                        logger.info("dsmo.getName==" + dsmo.getName());
                        boolean isMount = false;
                        List<DatastoreHostMount> dhms = dsmo.getHostMounts();
                        if (dhms != null && dhms.size() > 0) {
                            //整理挂载信息
                            List<String> dsHostIds = new ArrayList<>();
                            for (DatastoreHostMount dhm : dhms) {
                                if (dhm != null) {
                                    if (dhm.getMountInfo().isMounted()) {
                                        dsHostIds.add(dhm.getKey().getValue());
                                    }
                                }
                            }
                            logger.info("dsmo.dsHostIds==" + dsHostIds);
                            for (String hostid : hostids) {
                                if (!dsHostIds.contains(hostid)) {
                                    isMount = true;
                                    break;
                                }
                            }
                            logger.info("dsmo.isMount==" + isMount);
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
            logger.error("vmware error:", e);
            throw e;
        }
        return listStr;
    }
    /**
     *得到指定集群下的所有主机 20200918objectId
     **/
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
            logger.error("vmware error:", e);
            throw e;
        }
        return listStr;
    }
    /**
     *得到集群下所有没有挂载的主机 20200918objectId
     **/
    public List<String> getUnmoutHostsOnCluster(String dataStoreObjectId, List<Map<String, String>> clusters) throws Exception {
        List<String> hostlist = null;
        try {
            String unmountHostStr = getHostsByDsObjectId(dataStoreObjectId);
            List<Map<String, String>> unmountHostlists = gson.fromJson(unmountHostStr, new TypeToken<List<Map<String, String>>>() {
            }.getType());

            if (clusters != null && clusters.size() > 0) {
                hostlist = new ArrayList<>();
                for (Map<String, String> cluster : clusters) {
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
            logger.error("vmware error:", e);
            throw e;
        }
        return hostlist;
    }
    /**
     *得到指定集群下的所有主机,以及指定主机所属集群下的所有主机 20200918objectId
     **/
    public List<Pair<ManagedObjectReference, String>> getHostsOnCluster(String clusterObjectId, String hostObjectId) throws Exception {
        List<Pair<ManagedObjectReference, String>> hosts = null;
        try {
            String serverguid = null;
            if (!StringUtils.isEmpty(clusterObjectId)) {
                serverguid = vcConnectionHelper.objectID2Serverguid(clusterObjectId);
            } else if (!StringUtils.isEmpty(hostObjectId)) {
                serverguid = vcConnectionHelper.objectID2Serverguid(hostObjectId);
            }
            if (!StringUtils.isEmpty(serverguid)) {
                VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

                //集群下的所有主机
                if (!StringUtils.isEmpty(clusterObjectId)) {
                    logger.info("object cluster Object Id:" + clusterObjectId);
                    ManagedObjectReference objmor = vcConnectionHelper.objectID2MOR(clusterObjectId);
                    ClusterMO clusterMo = new ClusterMO(vmwareContext, objmor);
                    hosts = clusterMo.getClusterHosts();
                    logger.info("Number of hosts in cluster:" + (hosts == null ? "null" : hosts.size()));
                } else if (!StringUtils.isEmpty(hostObjectId)) {  //目标主机所在集群下的其它主机
                    logger.info("object host Object Id:" + hostObjectId);
                    ManagedObjectReference objmor = vcConnectionHelper.objectID2MOR(hostObjectId);
                    HostMO hostMo = new HostMO(vmwareContext, objmor);
                    try {
                        ManagedObjectReference cluster = hostMo.getHyperHostCluster();
                        logger.info("Host cluster id:" + cluster.getValue());
                        if (cluster != null) {
                            ClusterMO clusterMo = new ClusterMO(hostMo.getContext(), cluster);
                            logger.info("Host cluster name:" + clusterMo.getName());
                            hosts = clusterMo.getClusterHosts();
                        }
                        logger.info("Number of hosts in cluster:" + (hosts == null ? "null" : hosts.size()));
                    }catch (Exception e){
                        logger.error("Number of hosts in cluster:" + (hosts == null ? "null" : hosts.size()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("get Host On Cluster error:", e);
            throw e;
        }

        return hosts;
    }
    /**
     *rename datastore name
     **/
    public String renameDataStore(String newName, String dataStoreObjectId) throws Exception {

        String result = "success";
        logger.info("==start rename DataStore==");
        try {
            String serverguid = vcConnectionHelper.objectID2Serverguid(dataStoreObjectId);
            VmwareContext serverContext = vcConnectionHelper.getServerContext(serverguid);
            ManagedObjectReference dsmor = vcConnectionHelper.objectID2MOR(dataStoreObjectId);
            //String objectId = vcConnectionHelper.MOR2ObjectID(dsMo.getMor(), dsMo.getContext().getServerAddress());
            DatastoreMO dsMo = new DatastoreMO(serverContext, dsmor);
            dsMo.renameDatastore(newName);
            logger.info("==end rename DataStore==");
        } catch (Exception e) {
            result = "failed";
            logger.error("vmware error:", e);
            throw e;
        } finally {
            return result;
        }
    }
    /**
     *get oriented datastore capacity
     **/
    public static Map<String, Object> getCapacityOnVmfs(String dsname) {
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("msg", "success");
        logger.info("==start get oriented datastore capacity==");
        try {
            VmwareContext vmwareContext = TestVmwareContextFactory.getContext("10.143.132.248", 443, "administrator@vsphere.local", "Pbu4@123");
            DatastoreMO dsMo = new DatastoreMO(vmwareContext, new DatacenterMO(vmwareContext, "Datacenter").findDatastore(dsname));
            DatastoreSummary summary = dsMo.getSummary();
            long capacity = summary.getCapacity();
            logger.info("==end get oriented datastore capacity==");
            resMap.put("capacity", capacity);
            return resMap;
        } catch (Exception e) {
            resMap.put("msg", "failed");
            logger.error("vmware error:", e);
            throw e;
        } finally {
            return resMap;
        }
    }
    /**
     *expand oriented datastore capacity
     **/
    public String expandVmfsDatastore(String dsname, Integer addCapacity,String hostObjectId) {

        String result = "success";
        logger.info("==start expand DataStore==");
        try {
            ManagedObjectReference mor = null;
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                RootFsMO rootFsMo = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
                List<Pair<ManagedObjectReference, String>> hosts = rootFsMo.getAllHostOnRootFs();
                if (hosts != null && hosts.size() > 0) {
                    HostMO host1 = null;
                    HostDatastoreSystemMO hdsMo = null;
                    for (Pair<ManagedObjectReference, String> host : hosts) {
                        host1 = new HostMO(vmwareContext, host.first());
                        hdsMo = host1.getHostDatastoreSystemMO();
                        if (null != hdsMo.findDatastore(dsname)) {
                            mor = hdsMo.findDatastore(dsname);
                            break;
                        }
                    }
                    if (mor != null && host1 != null && hdsMo != null) {
                        DatastoreMO dsMo = new DatastoreMO(vmwareContext, mor);
                        List<VmfsDatastoreOption> vmfsDatastoreOptions = hdsMo.queryVmfsDatastoreExpandOptions(dsMo);
                        VmfsDatastoreInfo datastoreInfo = (VmfsDatastoreInfo) hdsMo.getDatastoreInfo(mor);
                        if (vmfsDatastoreOptions != null && vmfsDatastoreOptions.size() > 0) {
                            VmfsDatastoreOption vmfsDatastoreOption = vmfsDatastoreOptions.get(0);
                            String diskUuid = vmfsDatastoreOption.getSpec().getDiskUuid();
                            VmfsDatastoreExpandSpec spec = (VmfsDatastoreExpandSpec) vmfsDatastoreOption.getSpec();
                            //HostDiskPartitionSpec hostDiskPartitionSpec = new HostDiskPartitionSpec();
                            //hostDiskPartitionSpec.setTotalSectors(spec.getPartition().getTotalSectors());
                            //todo 终于搞出来了 组长还是厉害
                            //spec.getPartition().setTotalSectors();
                            host1.getHostDatastoreSystemMO().expandVmfsDatastore(dsMo, spec);
                            scanDataStore(null,hostObjectId);
                        }
                    }
                }
            }
            logger.info("==end expand DataStore==");
        } catch (Exception e) {
            result = "failed";
            logger.error("vmware error:", e);
            throw e;
        } finally {
            return result;
        }
    }
    /**
     *recycle vmfs datastore capacity
     **/
    public String recycleVmfsCapacity(String dsname) throws Exception {

        String result = "success";
        List<String> uuids = new ArrayList<>();
        try {
            ManagedObjectReference mor = null;
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                RootFsMO rootFsMo = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
                List<Pair<ManagedObjectReference, String>> hosts = rootFsMo.getAllHostOnRootFs();
                if (hosts != null && hosts.size() > 0) {
                    HostMO host1 = null;
                    HostDatastoreSystemMO hdsMo = null;
                    for (Pair<ManagedObjectReference, String> host : hosts) {
                        host1 = new HostMO(vmwareContext, host.first());
                        hdsMo = host1.getHostDatastoreSystemMO();
                        if (null != hdsMo.findDatastore(dsname)) {
                            mor = hdsMo.findDatastore(dsname);
                            break;
                        }
                    }
                    if (hdsMo != null) {
                        VmfsDatastoreInfo datastoreInfo = (VmfsDatastoreInfo) hdsMo.getDatastoreInfo(mor);
                        String uuid = datastoreInfo.getVmfs().getUuid();
                        uuids.add(uuid);
                        if (uuids.size() != 0 && uuid != null) {
                            hdsMo.unmapVmfsVolumeExTask(uuids);
                        }
                    }
                }
            }
            logger.info("==end recycle DataStore==");
        } catch (Exception e) {
            result = "error";
            logger.error("vmware error:", e);
            throw e;
        }
        return result;
    }
    /**
     *create nfs datastore
     **/
    public String createNfsDatastore(String serverHost, String exportPath, String nfsName, String accessMode, List<Map<String,String>> hostObjectIds,String type) {
        String response = "";
        logger.info("start creat nfs datastore");
        accessMode = StringUtils.isEmpty(accessMode) || "readWrite".equals(accessMode) ? "readWrite" : "readOnly";
        try {
            VmwareContext vmwareContext = null;
            ManagedObjectReference managedObjectReference = null;
            DmeVmwareRelation dmeVmwareRelation = new DmeVmwareRelation();
            if (hostObjectIds != null && hostObjectIds.size()!= 0) {
                for (Map<String, String> hosts : hostObjectIds) {
                    for (Map.Entry<String, String> host : hosts.entrySet()) {
                        String serverguid = vcConnectionHelper.objectID2Serverguid(host.getKey());
                        managedObjectReference = vcConnectionHelper.objectID2MOR(host.getKey());
                        vmwareContext = vcConnectionHelper.getServerContext(serverguid);
                        if (managedObjectReference != null && vmwareContext != null) {
                            HostMO hostMo = new HostMO(vmwareContext, managedObjectReference);
                            HostDatastoreSystemMO hostDatastoreSystemMo = hostMo.getHostDatastoreSystemMO();
                            ManagedObjectReference datastore = hostDatastoreSystemMo.createNfsDatastore(serverHost, 0, exportPath, nfsName, accessMode, type);
                            String datastoreObjectId = vcConnectionHelper.MOR2ObjectID(datastore, serverguid);
                            dmeVmwareRelation.setStoreId(datastoreObjectId);
                            dmeVmwareRelation.setStoreName(nfsName);
                            dmeVmwareRelation.setStoreType(ToolUtils.STORE_TYPE_NFS);
                        } else {
                            response = "failed";
                            logger.error("can not find target host!");
                        }
                    }
                }
                response = gson.toJson(dmeVmwareRelation);
            } else {
                response = "failed";
                logger.error("{createNfsDatastore/createnfsdatastore} params error:hostObjectIds{"+hostObjectIds+"}");
            }
            logger.info("end creat nfs datastore");
        } catch (Exception e) {
            response = "failed";
            logger.error("vmware error:", e);
        }
        return response;
    }

    public void hostRescanVmfs(String hostIp) throws Exception {
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                RootFsMO rootFsMo = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
                List<Pair<ManagedObjectReference, String>> hosts = rootFsMo.getAllHostOnRootFs();
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
            logger.error("vmware host rescanVmfs error:", e);
            throw e;
        }
    }

    public void hostRescanHba(String hostIp) throws Exception {
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                RootFsMO rootFsMo = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
                List<Pair<ManagedObjectReference, String>> hosts = rootFsMo.getAllHostOnRootFs();
                if (hosts != null && hosts.size() > 0) {
                    for (Pair<ManagedObjectReference, String> host : hosts) {
                        HostMO hostMo = new HostMO(vmwareContext, host.first());
                        String hostName = hostMo.getHostName();
                        if (hostIp.equals(hostName)) {
                            //在查找可用LUN前先扫描hba，已发现新的卷
                            List<String> devices = getHbaDeviceByHost(hostMo);
                            if(devices!=null && devices.size()>0){
                                for(String device : devices){
                                    hostMo.getHostStorageSystemMO().rescanHba(device);
                                }
                            }
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("vmware host rescan HBA error:", e);
            throw e;
        }
    }

    private static void hostAction() {

    }
    /**
     *得到主机对应的可用LUN
     **/
    public String getLunsOnHost(String hostName) throws Exception {
        String lunStr = "";
        try {
            HostMO hostMo = null;

            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {

                RootFsMO rootFsMo = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
                List<Pair<ManagedObjectReference, String>> hosts = rootFsMo.getAllHostOnRootFs();
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
            logger.error("host:" + hostName + " error:", e);
            throw e;
        }
        return lunStr;
//
    }
    /**
     *得到主机对应的可用LUN 20200918objectId
     **/
    public Map<String, Object> getLunsOnHost(String hostObjectId, int capacity,String volumeWwn) throws Exception {
        Map<String, Object> remap = null;
        HostScsiDisk candidateHostScsiDisk = null;
        try {
            String serverguid = vcConnectionHelper.objectID2Serverguid(hostObjectId);
            VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

            ManagedObjectReference objmor = vcConnectionHelper.objectID2MOR(hostObjectId);
            HostMO hostMo = new HostMO(vmwareContext, objmor);

            if (hostMo != null) {
                //在查找可用LUN前先扫描hba，已发现新的卷
                List<String> devices = getHbaDeviceByHost(hostMo);
                if(devices!=null && devices.size()>0){
                    for(String device:devices){
                        hostMo.getHostStorageSystemMO().rescanHba(device);
                    }
                }

                //get available LUN
                HostDatastoreSystemMO hostDatastoreSystem = hostMo.getHostDatastoreSystemMO();
                List<HostScsiDisk> hostScsiDisks = hostDatastoreSystem.queryAvailableDisksForVmfs();
                candidateHostScsiDisk = getObjectLuns(hostScsiDisks, capacity, volumeWwn);
                remap = new HashMap<>();
                remap.put("host", hostMo);
                remap.put("hostScsiDisk", candidateHostScsiDisk);
            } else {
                throw new Exception("host:" + hostObjectId + " non-existent。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("host:" + hostObjectId + " error:", e);
            throw e;
        }
        return remap;
//
    }
    /**
     *得到主机的hba设备名
     **/
    public List<String> getHbaDeviceByHost(HostMO hostMo){
        List<String> devices = null;
        try{
            if(hostMo!=null){
                List<HostHostBusAdapter> hbas = hostMo.getHostStorageSystemMO().getStorageDeviceInfo().getHostBusAdapter();
                if(hbas!=null && hbas.size()>0) {
                    devices = new ArrayList<>();
                    for (HostHostBusAdapter hba : hbas) {
                        if (hba instanceof HostInternetScsiHba) {
                            HostInternetScsiHba iscsiHba = (HostInternetScsiHba) hba;
                            if (!StringUtils.isEmpty(iscsiHba.getDevice())) {
                                devices.add(iscsiHba.getDevice());
                            }
                        } else if (hba instanceof HostFibreChannelHba) {
                            HostFibreChannelHba fcHba = (HostFibreChannelHba) hba;
                            if (!StringUtils.isEmpty(fcHba.getDevice())) {
                                devices.add(fcHba.getDevice());
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error("get Hba Device By Host error:", e);
        }
        return devices;
    }
    /**
     *得到集群下所有主机对应的可用LUN 20200918objectId
     **/
    public Map<String, Object> getLunsOnCluster(String clusterObjectId, int capacity, String volumeWwn) throws Exception {
        Map<String, Object> remap = null;
        HostScsiDisk candidateHostScsiDisk = null;
        try {
            String serverguid = vcConnectionHelper.objectID2Serverguid(clusterObjectId);
            VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

            ManagedObjectReference objmor = vcConnectionHelper.objectID2MOR(clusterObjectId);
            ClusterMO cl1 = new ClusterMO(vmwareContext, objmor);

            List<Pair<ManagedObjectReference, String>> hosts = cl1.getClusterHosts();
            if (hosts != null && hosts.size() > 0) {
                Map<String, HostMO> hostMap = new HashMap<>();
                List<HostScsiDisk> objHostScsiDisks = new ArrayList<>();
                for (Pair<ManagedObjectReference, String> host : hosts) {
                    HostMO hostMo = new HostMO(vmwareContext, host.first());

                    //在查找可用LUN前先扫描hba，已发现新的卷
                    List<String> devices = getHbaDeviceByHost(hostMo);
                    if(devices!=null && devices.size()>0){
                        for(String device:devices){
                            hostMo.getHostStorageSystemMO().rescanHba(device);
                        }
                    }

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
                    candidateHostScsiDisk = getObjectLuns(objHostScsiDisks, capacity, volumeWwn);
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
            logger.error("cluster get lun error:", e);
            throw e;
        }
        return remap;
//
    }
    /**
     *得到主机对应的可用LUN 20200918objectId
     **/
    public HostScsiDisk getObjectLuns(List<HostScsiDisk> hostScsiDisks, int capacity, String volumeWwn) throws Exception {
        HostScsiDisk candidateHostScsiDisk = null;
        try {
            if (hostScsiDisks != null && hostScsiDisks.size() > 0 && capacity > 0) {
                for (HostScsiDisk hostScsiDisk : hostScsiDisks) {
                    if(hostScsiDisk.getCanonicalName().equals("naa."+volumeWwn)){
                        candidateHostScsiDisk = hostScsiDisk;
                        break;
                    }
                }

                if (candidateHostScsiDisk != null) {
                    logger.info("Create datastore via on disk " + candidateHostScsiDisk.getDevicePath());

                } else {
                    throw new Exception("host No find available LUN。");
                }
            } else {
                throw new Exception("host No available LUN。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("get LUN error:", e);
            throw e;
        }
        return candidateHostScsiDisk;
//
    }
    /**
     *创建vmfs存储 20200918objectId
     **/
    public String createVmfsDataStore(Map<String, Object> hsdmap, int capacity, String datastoreName,
                                      int vmfsMajorVersion, int blockSize,
                                      int unmapGranularity, String unmapPriority) throws Exception {
        String dataStoreStr = "";
        try {
            if (null != hsdmap && null != hsdmap.get(DmeConstants.HOST)) {
                HostScsiDisk objhsd = (HostScsiDisk) hsdmap.get("hostScsiDisk");
                HostMO hostMo = (HostMO) hsdmap.get("host");

                logger.info("path==" + objhsd.getDevicePath() + "==" + objhsd.getUuid() + "==" + (gson.toJson(objhsd.getCapacity())));
                logger.info("getName==" + hostMo.getName() + "==" + hostMo.getHostDatastoreSystemMO());

                if (hostMo != null) {
                    if (objhsd != null) {
                        logger.info("Create datastore via host " + hostMo.getName() + " on disk " + objhsd.getDevicePath());
                        long totalSectors = capacity * 1L * ToolUtils.GI / objhsd.getCapacity().getBlockSize();
                        logger.info("Vmfs totalSectors==" + totalSectors);
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
                            logger.info("datastore===" + datastore.getValue());
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
                            logger.info("dataStoreStr===" + dataStoreStr);
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
            logger.error("createVmfsDataStore error:", e);
            throw e;
        }
        return dataStoreStr;
//
    }
    /**
     *vmfs存储打标记 20200918objectId
     **/
    public String attachTag(String datastoreType, String datastoreId, String serviceLevelName, VCenterInfo vCenterInfo) throws Exception {
        String attachTagStr = "";

        if(vCenterInfo==null || StringUtils.isEmpty(vCenterInfo.getHostIp())){
            logger.error("vCenter Info is null");
            return null;
        }

        SessionHelper sessionHelper = null;
        try {
            if (StringUtils.isEmpty(datastoreType)) {
                logger.info("DataStore Type is null,unable tag。");
                return null;
            }
            if (StringUtils.isEmpty(datastoreId)) {
                logger.info("DataStore Id is null,unable tag。");
                return null;
            }
            if (StringUtils.isEmpty(serviceLevelName)) {
                logger.info("Service Level Name is null,unable tag。");
                return null;
            }

            sessionHelper = new SessionHelper();
            sessionHelper.login(vCenterInfo.getHostIp(), String.valueOf(vCenterInfo.getHostPort()),vCenterInfo.getUserName(), CipherUtils.decryptString(vCenterInfo.getPassword()));
            TaggingWorkflow taggingWorkflow = new TaggingWorkflow(sessionHelper);

            List<String> taglist = taggingWorkflow.listTags();
            for (String tagid : taglist) {
                TagModel tagModel = taggingWorkflow.getTag(tagid);
                if (tagModel.getName().equals(serviceLevelName)) {
                    DynamicID objDynamicId = new DynamicID(datastoreType, datastoreId);
                    taggingWorkflow.attachTag(tagid, objDynamicId);
                    logger.info("Service Level:" + serviceLevelName + " Associated");
                    attachTagStr = "Associated";
                    break;
                }
            }

        } catch (Exception e) {
            logger.error("DataStore:" + datastoreId + " error:", e);
        } finally {
            if (sessionHelper != null) {
                sessionHelper.logout();
            }
        }
        return attachTagStr;
//
    }

    /**
     *删除vmfs存储
     **/
    public boolean deleteVmfsDataStore(String name) throws Exception {
        boolean deleteFlag = false;
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                RootFsMO rootFsMo = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
                List<Pair<ManagedObjectReference, String>> hosts = rootFsMo.getAllHostOnRootFs();
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
            logger.error("vmware delete vmfs error:", e);
            throw e;
        }
        return deleteFlag;
    }
    /**
     *将存储挂载到集群下其它主机 20200918objectId
     **/
    public void mountVmfsOnCluster(String datastoreStr, String clusterObjectId, String hostObjectId) throws Exception {
        try {
            if (StringUtils.isEmpty(datastoreStr)) {
                logger.info("datastore:" + datastoreStr + " is null");
                return;
            }
            if (StringUtils.isEmpty(hostObjectId) && StringUtils.isEmpty(clusterObjectId)) {
                logger.info("host:" + hostObjectId + " and cluster:" + clusterObjectId + " is null");
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


            String serverguid = null;
            if (!StringUtils.isEmpty(clusterObjectId)) {
                serverguid = vcConnectionHelper.objectID2Serverguid(clusterObjectId);
            } else if (!StringUtils.isEmpty(hostObjectId)) {
                serverguid = vcConnectionHelper.objectID2Serverguid(hostObjectId);
            }
            if (!StringUtils.isEmpty(serverguid)) {
                VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);
                if (!StringUtils.isEmpty(clusterObjectId)) {
                    //集群下的所有主机
                    List<Pair<ManagedObjectReference, String>> hosts = getHostsOnCluster(clusterObjectId, hostObjectId);
                    if (hosts != null && hosts.size() > 0) {
                        for (Pair<ManagedObjectReference, String> host : hosts) {
                            try {
                                HostMO host1 = new HostMO(vmwareContext, host.first());
                                logger.info("Host under Cluster: " + host1.getName());
                                //只挂载其它的主机
                                if (host1 != null && !objHostName.equals(host1.getName())) {
                                    mountVmfs(objDataStoreName, host1);
                                }
                            }catch (Exception e){
                                logger.error("mount Vmfs On Cluster error:", e);
                            }
                        }
                    }
                } else if (!StringUtils.isEmpty(hostObjectId)) {
                    try {
                        ManagedObjectReference objmor = vcConnectionHelper.objectID2MOR(hostObjectId);
                        HostMO hostmo = new HostMO(vmwareContext, objmor);
                        //只挂载其它的主机
                        if (hostmo != null && !objHostName.equals(hostmo.getName())) {
                            mountVmfs(objDataStoreName, hostmo);
                        }
                    }catch (Exception e){
                        logger.error("mount Vmfs On Cluster error:", e);
                    }
                }

            }
        } catch (Exception e) {
            logger.error("mount Vmfs On Cluster error:", e);
            throw e;
        }
    }
    /**
     *挂载存储 20200918objectId
     **/
    public void mountVmfs(String datastoreName, HostMO hostMo) throws Exception {
        try {
            if (StringUtils.isEmpty(datastoreName)) {
                logger.info("datastore Name is null");
                return;
            }
            if (hostMo == null) {
                logger.info("host is null");
                return;
            }
            logger.info("Hosts that need to be mounted:" + hostMo.getName());
            //挂载前重新扫描datastore
            hostMo.getHostStorageSystemMO().rescanVmfs();
            logger.info("Rescan datastore before mounting");
            //查询目前未挂载的卷
//            List<HostUnresolvedVmfsVolume> unvlist = hostMo.getHostStorageSystemMO().queryUnresolvedVmfsVolume();
//            logger.info("unvlist========"+(unvlist==null?"null":unvlist.size()));
//            if(unvlist!=null && unvlist.size()>0){
//                for(HostUnresolvedVmfsVolume unv:unvlist){
//                    logger.info(unv.getVmfsUuid()+"========"+unv.getVmfsLabel());
//                }
//            }
//            logger.info("============================");
            //查询目前未挂载的卷
            for (HostFileSystemMountInfo mount : hostMo.getHostStorageSystemMO().getHostFileSystemVolumeInfo().getMountInfo()) {
                if (mount.getVolume() instanceof HostVmfsVolume
                        && datastoreName.equals(mount.getVolume().getName())) {
                    HostVmfsVolume volume = (HostVmfsVolume) mount.getVolume();
                    logger.info(volume.getName() + "========" + volume.getUuid());
                    //挂载卷
                    hostMo.getHostStorageSystemMO().mountVmfsVolume(volume.getUuid());
                    logger.info("mount Vmfs success:" + volume.getName() + " : " + hostMo.getName());
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error(hostMo.getName()+" mount Vmfs Volume:"+datastoreName+"  error:"+e.toString());
        }
    }
    /**
     *在主机上扫描卷和Datastore 20200918objectId
     **/
    public void scanDataStore(String clusterObjectId, String hostObjectId) throws Exception {
        try {
            String serverguid = null;
            if (!StringUtils.isEmpty(clusterObjectId)) {
                serverguid = vcConnectionHelper.objectID2Serverguid(clusterObjectId);
            } else if (!StringUtils.isEmpty(hostObjectId)) {
                serverguid = vcConnectionHelper.objectID2Serverguid(hostObjectId);
            }
            if (!StringUtils.isEmpty(serverguid)) {
                VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);
                //集群下的所有主机
                List<Pair<ManagedObjectReference, String>> hosts = null;
                if (!StringUtils.isEmpty(clusterObjectId)) {
                    hosts = getHostsOnCluster(clusterObjectId, hostObjectId);
                    if (hosts != null && hosts.size() > 0) {
                        for (Pair<ManagedObjectReference, String> host : hosts) {
                            try {
                                HostMO host1 = new HostMO(vmwareContext, host.first());
                                logger.info("Host under Cluster: " + host1.getName());
                                host1.getHostStorageSystemMO().rescanVmfs();
                            }catch (Exception ex){
                                logger.error("under Cluster scan Data Store error:"+ex.toString());
                            }
                        }
                    }
                } else if (!StringUtils.isEmpty(hostObjectId)) {
                    try {
                        ManagedObjectReference objmor = vcConnectionHelper.objectID2MOR(hostObjectId);
                        HostMO hostmo = new HostMO(vmwareContext, objmor);
                        hostmo.getHostStorageSystemMO().rescanVmfs();
                    }catch (Exception ex){
                        logger.error("scan Data Store error:"+ex.toString());
                    }
                }



            }
        } catch (Exception e) {
            logger.error("scan Data Store error:", e);
            throw e;
        }
    }

    /**
     * @Author Administrator
     * @Description 为指定的虚拟机创建磁盘
     * @Date 11:41 2020/9/14
     * @Param [dataStoreName, vmObjectId, rdmDeviceName, size]
     * @Return void
     **/
    public void createDisk(String dataStoreName, String vmObjectId, String rdmDeviceName, int size) throws Exception {
        String serverguid = vcConnectionHelper.objectID2Serverguid(vmObjectId);
        VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);
        DatastoreMO datastoreMo = new DatastoreMO(vmwareContext, dataStoreName);
        String vmdkDatastorePath = String.format("[%s]", datastoreMo.getName());
        VirtualMachineMO virtualMachineMo = new VirtualMachineMO(vmwareContext, vcConnectionHelper.objectID2MOR(vmObjectId));

        virtualMachineMo.createDisk(vmdkDatastorePath, VirtualDiskType.RDM, VirtualDiskMode.PERSISTENT,
                rdmDeviceName, size * 1024, datastoreMo.getMor(), -1);

    }

    public List<DatastoreSummary> getDatastoreMountsOnHost(String hostId, String hostIp) throws Exception {
        List<DatastoreSummary> list = new ArrayList<>();
        VmwareContext vmwareContext = vcConnectionHelper.getServerContext(hostId);
        HostMO hostMo = new HostMO(vmwareContext, hostIp);
        List<Pair<ManagedObjectReference, String>> datastoreMountsOnHost = hostMo.getDatastoreMountsOnHost();
        for(Pair<ManagedObjectReference, String> pair : datastoreMountsOnHost){
            ManagedObjectReference dsMor = pair.first();
            DatastoreMO datastoreMo = new DatastoreMO(vmwareContext, dsMor);
            DatastoreSummary summary = datastoreMo.getSummary();
            if(summary.getType().equals(ToolUtils.STORE_TYPE_VMFS)){
                list.add(summary);
            }
        }

        return list;
    }
    /**
     *将存储挂载到集群下其它主机 20200918objectId
     **/
    public void mountNfsOnCluster(String dataStoreObjectId, List<Map<String, String>> clusters, List<Map<String, String>> hosts, String mountType) throws Exception {
        try {
            if (StringUtils.isEmpty(dataStoreObjectId)) {
                logger.info("dataStore object id:" + dataStoreObjectId + " is null");
                return;
            }
            if (clusters == null && hosts == null) {
                logger.info("host:" + hosts + " and cluster:" + clusters + " is null");
                return;
            }

            List<String> hostlist = null;
            if (hosts != null) {
                hostlist = new ArrayList<>();
                if (hosts != null && hosts.size() > 0) {
                    for (Map<String, String> hostmap : hosts) {
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

                RootFsMO rootFsMo = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
                //集群下的所有主机
                ManagedObjectReference dsmor = vcConnectionHelper.objectID2MOR(dataStoreObjectId);
                DatastoreMO dsmo = new DatastoreMO(vmwareContext, dsmor);

                for (String hostName : hostlist) {

                    HostMO hostmo = rootFsMo.findHost(hostName);
                    logger.info("Host name: " + hostmo.getName());
                    //只挂载其它的主机
                    mountNfs(dsmo, hostmo, mountType);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("mount Vmfs On Cluster error:", e);
            throw e;
        }
    }
    /**
     *挂载Nfs存储 20200918objectId
     **/
    public void mountNfs(DatastoreMO dsmo, HostMO hostMo, String mountType) throws Exception {
        try {
            if (dsmo == null) {
                logger.info("datastore is null");
                return;
            }
            if (hostMo == null) {
                logger.info("host is null");
                return;
            }
            logger.info("Hosts that need to be mounted:" + hostMo.getName());
            //挂载前重新扫描datastore
            hostMo.getHostStorageSystemMO().rescanVmfs();
            logger.info("Rescan datastore before mounting");
            //挂载NFS
            NasDatastoreInfo nasdsinfo = (NasDatastoreInfo) dsmo.getInfo();
            hostMo.getHostDatastoreSystemMO().createNfsDatastore(nasdsinfo.getNas().getRemoteHost(), 0, nasdsinfo.getNas().getRemotePath(), dsmo.getMor().getValue(), mountType, null);
            logger.info("mount nfs success:" + hostMo.getName() + ":" + dsmo.getName());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("mount nfs error:", e);
        }
    }

    public void unmountNfsOnHost(String dataStoreObjectId, String hostId) throws Exception {
        try {
            if (StringUtils.isEmpty(dataStoreObjectId)) {
                logger.info("dataStore object id:" + dataStoreObjectId + " is null");
                return;
            }
            if (StringUtils.isEmpty(hostId)) {
                logger.info("host:" + hostId + " is null");
                return;
            }

            String serverguid = vcConnectionHelper.objectID2Serverguid(dataStoreObjectId);
            VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

            RootFsMO rootFsMo = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
            ManagedObjectReference dsmor = vcConnectionHelper.objectID2MOR(dataStoreObjectId);
            DatastoreMO dsmo = new DatastoreMO(vmwareContext, dsmor);

            HostMO hostmo = rootFsMo.findHostById(hostId);
            logger.info("Host name: " + hostmo.getName());
            //卸载
            unmountNfsOnHost(dsmo, hostmo, dataStoreObjectId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("unmount nfs On host error:", e);
            throw e;
        }
    }

    public void unmountNfsOnCluster(String dataStoreObjectId, String clusterId) throws Exception {
        try {
            if (StringUtils.isEmpty(dataStoreObjectId)) {
                logger.info("dataStore object id:" + dataStoreObjectId + " is null");
                return;
            }
            if (StringUtils.isEmpty(clusterId)) {
                logger.info("host:" + clusterId + " is null");
                return;
            }

            String serverguid = vcConnectionHelper.objectID2Serverguid(dataStoreObjectId);
            VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

            RootFsMO rootFsMo = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
            ManagedObjectReference dsmor = vcConnectionHelper.objectID2MOR(dataStoreObjectId);
            DatastoreMO dsmo = new DatastoreMO(vmwareContext, dsmor);

            ClusterMO clusterMo = rootFsMo.findClusterById(clusterId);
            logger.info("cluster name: " + clusterMo.getName());
            //卸载
            unmountNfsOnCluster(dsmo, clusterMo, dataStoreObjectId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("unmount nfs On cluster error:", e);
            throw e;
        }
    }
    /**
     *卸载Nfs存储 20200918objectId
     **/
    public void unmountNfsOnHost(DatastoreMO dsmo, HostMO hostMo, String nfsId) throws Exception {
        try {
            if (dsmo == null) {
                logger.info("datastore is null");
                return;
            }
            if (hostMo == null) {
                logger.info("host is null");
                return;
            }
            logger.info("Hosts that need to be unmounted:" + hostMo.getName());
            //卸载前重新扫描datastore
            hostMo.getHostStorageSystemMO().rescanVmfs();
            logger.info("Rescan datastore before unmounting");
            //卸载NFS
            hostMo.unmountDatastore(nfsId);
            logger.info("unmount nfs success:" + hostMo.getName() + ":" + dsmo.getName());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("unmount nfs error:", e);
        }
    }
    /**
     *卸载Nfs存储 20200918objectId
     **/
    public void unmountNfsOnCluster(DatastoreMO dsmo, ClusterMO clusterMo, String nfsId) throws Exception {
        try {
            if (dsmo == null) {
                logger.info("datastore is null");
                return;
            }
            if (clusterMo == null) {
                logger.info("cluster is null");
                return;
            }
            logger.info("Hosts that need to be unmounted:" + clusterMo.getName());
            //卸载前重新扫描datastore
            //hostMo.getHostStorageSystemMO().rescanVmfs();
            logger.info("Rescan datastore before unmounting");
            //卸载NFS
            clusterMo.unmountDatastore(nfsId);
            logger.info("unmount nfs success:" + clusterMo.getName() + ":" + dsmo.getName());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("unmount nfs error:", e);
        }
    }
    /**
     *Get host's vmKernel IP,only provisioning provisioning
     **/
    public String getVmKernelIpByHostObjectId(String hostObjectId) throws Exception {
        String listStr = "";
        try {
            String serverguid = vcConnectionHelper.objectID2Serverguid(hostObjectId);
            VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

            //取得该存储下所有已经挂载的主机ID
            ManagedObjectReference objmor = vcConnectionHelper.objectID2MOR(hostObjectId);
            HostMO hostmo = new HostMO(vmwareContext, objmor);
            if (hostmo != null) {
                List<VirtualNicManagerNetConfig> nics = hostmo.getHostVirtualNicManagerNetConfig();
                if (nics != null && nics.size() > 0) {
                    for (VirtualNicManagerNetConfig nic : nics) {
                        if ("vSphereProvisioning".equals(nic.getNicType())) {
                            List<Map<String, Object>> lists = new ArrayList<>();

                            List<HostVirtualNic> subnics = nic.getCandidateVnic();
                            if (subnics != null && subnics.size() > 0) {
                                for (HostVirtualNic subnic : subnics) {
                                    if (nic.getSelectedVnic().contains(subnic.getKey())) {
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
            logger.error("vmware error:", e);
            throw e;
        }
        return listStr;
    }

    public List<PbmProfile> getAllSelfPolicyInallcontext() {
        List<PbmProfile> pbmProfiles = new ArrayList<>();
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                pbmProfiles.addAll(getAllSelfPolicy(vmwareContext));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取所有虚拟机存储策略出错");
        }
        return pbmProfiles;
    }

    private List<PbmProfile> getAllSelfPolicy(VmwareContext vmwareContext) {
        List<PbmProfile> pbmProfiles = new ArrayList<>();
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
            logger.error("获取虚拟机存储策略出错");
        }
        return pbmProfiles;
    }

    public List<TagModel> getAllTagsByCategoryId(String categoryid, SessionHelper sessionHelper) {
        List<TagModel> tagList = new ArrayList<>();
        TaggingWorkflow taggingWorkflow = new TaggingWorkflow(sessionHelper);
        List<String> tags = taggingWorkflow.listTagsForCategory(categoryid);
        for (String tagid : tags) {
            tagList.add(taggingWorkflow.getTag(tagid));
        }
        return tagList;

    }

    public String getCategoryID(SessionHelper sessionHelper) throws Exception {
        String categoryid = "";

        TaggingWorkflow taggingWorkflow = new TaggingWorkflow(sessionHelper);

        List<String> categorylist = taggingWorkflow.listTagCategory();
        for (String category : categorylist) {
            CategoryModel categoryModel = taggingWorkflow.getTagCategory(category);
            if (categoryModel.getName().equalsIgnoreCase(CATEGORY_NAME)) {
                categoryid = categoryModel.getId();
            }
        }
        if ("".equalsIgnoreCase(categoryid)) {
            //创建category
            CategoryTypes.CreateSpec createSpec = new CategoryTypes.CreateSpec();
            createSpec.setName(CATEGORY_NAME);
            createSpec.setDescription(CATEGORY_NAME);

            createSpec.setCardinality(CategoryModel.Cardinality.SINGLE);

            Set<String> associableTypes = new HashSet<String>(); // empty hash set
            associableTypes.add("Datastore");
            createSpec.setAssociableTypes(associableTypes);
            categoryid = taggingWorkflow.createTagCategory(createSpec);
        }

        return categoryid;
    }

    public void createPbmProfileInAllContext(String categoryName, String tagName) {
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                createPbmProfile(vmwareContext, categoryName, tagName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("在所有实例上创建虚拟机存储策略出错");
        }
    }

    private void createPbmProfile(VmwareContext vmwareContext, String categoryName, String tagName) throws RuntimeFaultFaultMsg, InvalidArgumentFaultMsg, com.vmware.vim25.RuntimeFaultFaultMsg, com.vmware.pbm.InvalidArgumentFaultMsg, PbmFaultProfileStorageFaultFaultMsg, PbmDuplicateNameFaultMsg {
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
        if (tagCategoryInfo == null) {
            throw new InvalidArgumentFaultMsg(
                    "Specified Tag Category does not exist", null);
        }
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
        if (tagSetMeta == null || tagSetMeta.getValues().isEmpty()) {
            throw new com.vmware.vim25.RuntimeFaultFaultMsg("Specified Tag Category '"
                    + categoryName + "' does not have any associated tags", null);
        }
        // Create a New Discrete Set for holding Tag Values
        PbmCapabilityDiscreteSet tagSet = new PbmCapabilityDiscreteSet();
        for (Object obj : tagSetMeta.getValues()) {
            if (tagName.equalsIgnoreCase(((PbmCapabilityDescription) obj).getValue().toString())) {
                tagSet.getValues().add(((PbmCapabilityDescription) obj).getValue());
            }
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

        // Step 7: Create Storage Profile
        PbmProfileId profile = vmwareContext.getPbmService().pbmCreate(profileMgr, spec);
    }

    public void createTag(String tagName, SessionHelper sessionHelper) {

        try {
            TaggingWorkflow taggingWorkflow = new TaggingWorkflow(sessionHelper);
            taggingWorkflow.createTag(tagName, "", getCategoryID(sessionHelper));
            sessionHelper.logout();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("创建tag出错");
        }
    }

    private void removePbmProfile(VmwareContext vmwareContext, List<PbmProfile> pbmProfiles) throws RuntimeFaultFaultMsg {
        List<PbmProfileId> pbmProfileIds = new ArrayList<>();
        for (PbmProfile profile : pbmProfiles) {
            pbmProfileIds.add(profile.getProfileId());
        }
        if (pbmProfileIds.size()>0) {
            PbmServiceInstanceContent spbmsc;
            // Get PBM Profile Manager & Associated Capability Metadata
            spbmsc = vmwareContext.getPbmServiceContent();
            ManagedObjectReference profileMgr = spbmsc.getProfileManager();
            vmwareContext.getPbmService().pbmDelete(profileMgr,
                    pbmProfileIds);
        }
    }

    public void removePbmProfileInAllContext(List<PbmProfile> pbmProfiles) {
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelper.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                removePbmProfile(vmwareContext, pbmProfiles);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除所有虚拟机存储策略出错",e);
        }
    }

    public void removeAllTags(List<TagModel> tagModels, SessionHelper sessionHelper) {
        TaggingWorkflow taggingWorkflow = new TaggingWorkflow(sessionHelper);
        for (TagModel tagModel : tagModels) {
            taggingWorkflow.deleteTag(tagModel.getId());
        }

    }
    /**
     *主机配置iscsi
     **/
    public void configureIscsi(String hostObjectId, Map<String, String> vmKernel, List<Map<String, Object>> ethPorts) throws Exception {
        try {
            if (ethPorts == null) {
                logger.error("configure Iscsi error:ethPorts is null.");
                throw new Exception("configure Iscsi error:ethPorts is null.");
            }
            if (vmKernel == null) {
                logger.error("configure Iscsi error:vmKernel is null.");
                throw new Exception("configure Iscsi error:vmKernel is null.");
            }
            if (StringUtils.isEmpty(hostObjectId)) {
                logger.error("configure Iscsi error:host ObjectId is null.");
                throw new Exception("configure Iscsi error:host ObjectId is null.");
            }

            String serverguid = vcConnectionHelper.objectID2Serverguid(hostObjectId);
            VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

            //取得该存储下所有已经挂载的主机ID
            ManagedObjectReference objmor = vcConnectionHelper.objectID2MOR(hostObjectId);
            HostMO hostmo = new HostMO(vmwareContext, objmor);
            //查找对应的iscsi适配器
            String iscsiHbaDevice = null;
            List<HostHostBusAdapter> hbas = hostmo.getHostStorageSystemMO().getStorageDeviceInfo().getHostBusAdapter();
            for (HostHostBusAdapter hba : hbas) {
                if (hba instanceof HostInternetScsiHba) {
                    HostInternetScsiHba iscsiHba = (HostInternetScsiHba) hba;
                    if (!StringUtils.isEmpty(iscsiHba.getDevice())) {
                        iscsiHbaDevice = iscsiHba.getDevice();
                        break;
                    }
                }
            }
            if (StringUtils.isEmpty(iscsiHbaDevice)) {
                logger.error("find iscsi Hba Device error:No iSCSI adapter found");
                throw new Exception("find iscsi Hba Device error:No iSCSI adapter found");
            }
            //得到vmKernel适配器
            //网络端口邦定，将vmKernelDevice邦定到iscsiHbaDevice
            logger.info("iscsiHbaDevice:" + iscsiHbaDevice);
            boundVmKernel(hostmo, vmKernel, iscsiHbaDevice);
            //添加发现目标
            addIscsiSendTargets(hostmo, ethPorts, iscsiHbaDevice);
            logger.info("configure Iscsi success! iscsiHbaDevice:" + iscsiHbaDevice + " host:" + hostmo.getName());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("vmware error:", e);
            throw e;
        }

    }
    /**
     *网络端口邦定，将vmKernelDevice邦定到iscsiHbaDevice
     **/
    public void boundVmKernel(HostMO hostmo, Map<String, String> vmKernel, String iscsiHbaDevice) throws Exception {
        try {
            if (vmKernel == null) {
                logger.error("configure Iscsi error:vmKernel is null.");
                throw new Exception("configure Iscsi error:vmKernel is null.");
            }
            if (hostmo == null) {
                logger.error("configure Iscsi error:host is null.");
                throw new Exception("configure Iscsi error:host is null.");
            }
            if (StringUtils.isEmpty(iscsiHbaDevice)) {
                logger.error("find iscsi Hba Device error:No iSCSI adapter found");
                throw new Exception("find iscsi Hba Device error:No iSCSI adapter found");
            }
            //得到vmKernel适配器
            String vmKernelDevice = ToolUtils.getStr(vmKernel.get("device"));
            if (StringUtils.isEmpty(vmKernelDevice)) {
                logger.error("find vmKernel Device error:No vmKernel adapter found");
                throw new Exception("find vmKernel Device error:No vmKernel adapter found");
            }
            logger.info("vmKernelDevice:" + vmKernelDevice);
            //网络端口邦定，将vmKernelDevice邦定到iscsiHbaDevice
            hostmo.getIscsiManagerMO().bindVnic(iscsiHbaDevice, vmKernelDevice);
            logger.info("bind Vnic success! iscsiHbaDevice:" + iscsiHbaDevice + " vmKernelDevice:" + vmKernelDevice);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("vmware error:", e);
            throw e;
        }
    }
    /**
     *添加发现目标
     **/
    public void addIscsiSendTargets(HostMO hostmo, List<Map<String, Object>> ethPorts, String iscsiHbaDevice) throws Exception {
        try {
            if (ethPorts == null) {
                logger.error("configure Iscsi error:ethPorts is null.");
                throw new Exception("configure Iscsi error:ethPorts is null.");
            }
            if (hostmo == null) {
                logger.error("configure Iscsi error:host is null.");
                throw new Exception("configure Iscsi error:host is null.");
            }
            if (StringUtils.isEmpty(iscsiHbaDevice)) {
                logger.error("find iscsi Hba Device error:No iSCSI adapter found");
                throw new Exception("find iscsi Hba Device error:No iSCSI adapter found");
            }

            //添加发现目标
            if (ethPorts != null && ethPorts.size() > 0) {
                List<HostInternetScsiHbaSendTarget> targets = new ArrayList<>();
                for (Map<String, Object> ethPortInfo : ethPorts) {
                    //组装发现目标
                    HostInternetScsiHbaSendTarget target = new HostInternetScsiHbaSendTarget();
                    target.setAddress(ToolUtils.getStr(ethPortInfo.get("mgmtIp")));
                    targets.add(target);
                }
                if (targets.size() > 0) {
                    //向iscsi添加目标
                    hostmo.getHostStorageSystemMO().addInternetScsiSendTargets(iscsiHbaDevice, targets);
                    logger.info("add Iscsi Send Targets success! iscsiHbaDevice:" + iscsiHbaDevice + " targets:" + gson.toJson(targets));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("vmware error:", e);
            throw e;
        }

    }

    /**
     *删除NFS dataStore
     **/
    public void deleteNfs(String dataStoreObjectId, List<String> hostIds) throws Exception {
        if (StringUtils.isEmpty(dataStoreObjectId)) {
            logger.info("delete dataStore,  datasotreObject id:" + dataStoreObjectId + " is null");
            return;
        }
        if (null == hostIds || hostIds.size() == 0) {
            logger.info("delete datasotre host:" + hostIds + " is null");
            return;
        }

        String serverguid = vcConnectionHelper.objectID2Serverguid(dataStoreObjectId);
        VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

        RootFsMO rootFsMo = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());
        //存储下的所有主机
        ManagedObjectReference dsmor = vcConnectionHelper.objectID2MOR(dataStoreObjectId);
        DatastoreMO dsmo = new DatastoreMO(vmwareContext, dsmor);
        for (String hostId : hostIds) {
            HostMO hostmo = rootFsMo.findHostById(hostId);
            logger.info("Host name: " + hostmo.getName());
            //主机删除存储
            deleteNfs(dsmo, hostmo, dataStoreObjectId);
        }
    }

    public void deleteNfs(DatastoreMO dsmo, HostMO hostMo, String nfsId) {
        String hostName = null;
        try {
            //删除前重新扫描datastore
            hostMo.getHostStorageSystemMO().rescanVmfs();
            logger.info("Rescan datastore before mounting");
            //删除NFS
            NasDatastoreInfo nasdsinfo = (NasDatastoreInfo) dsmo.getInfo();
            hostMo.getHostDatastoreSystemMO().deleteDatastore(nasdsinfo.getName());
            hostName = hostMo.getName();
            logger.info("mount nfs success:" + hostMo.getName() + ":" + dsmo.getName());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("delete nfs error from host, hostId:" + hostName, e);
        }
    }
    /**
     *得到所有主机的ID与name, boolean mount 是否已经挂载了当前存储的主机
     **/
    public String getHostsByDsObjectId(String dataStoreObjectId, boolean mount) throws Exception {
        String listStr = "";
        try {
            //得到当前的context
            String serverguid = vcConnectionHelper.objectID2Serverguid(dataStoreObjectId);
            VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

            RootFsMO rootFsMo = new RootFsMO(vmwareContext, vmwareContext.getRootFolder());

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
            List<Pair<ManagedObjectReference, String>> hosts = rootFsMo.getAllHostOnRootFs();
            if (hosts != null && hosts.size() > 0) {
                List<Map<String, String>> lists = new ArrayList<>();
                for (Pair<ManagedObjectReference, String> host : hosts) {
                    HostMO host1 = new HostMO(vmwareContext, host.first());
                    if (mount) {
                        if (mounthostids.contains(host1.getMor().getValue())) {
                            Map<String, String> map = new HashMap<>();
                            String objectId = vcConnectionHelper.MOR2ObjectID(host1.getMor(), vmwareContext.getServerAddress());
                            map.put("hostId", objectId);
                            map.put("hostName", host1.getName());
                            lists.add(map);
                        }
                    } else {
                        if (!mounthostids.contains(host1.getMor().getValue())) {
                            Map<String, String> map = new HashMap<>();
                            String objectId = vcConnectionHelper.MOR2ObjectID(host1.getMor(), vmwareContext.getServerAddress());
                            map.put("hostId", objectId);
                            map.put("hostName", host1.getName());
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
            logger.error("vmware error:", e);
            throw e;
        }
        return listStr;
    }
    /**
     *得到Hba信息
     **/
    public Map<String,Object> getHbaByHostObjectId(String hostObjectId) throws Exception{
        Map<String,Object> map = new HashMap<>();
        try {
            if (StringUtils.isEmpty(hostObjectId)) {
                logger.error("get Hba error:host ObjectId is null.");
                throw new Exception("get Hba error:host ObjectId is null.");
            }

            String serverguid = vcConnectionHelper.objectID2Serverguid(hostObjectId);
            VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

            //取得该存储下所有已经挂载的主机ID
            ManagedObjectReference objmor = vcConnectionHelper.objectID2MOR(hostObjectId);
            HostMO hostmo = new HostMO(vmwareContext, objmor);
            //查找对应的iscsi适配器
            String iscsiHbaDevice = null;
            List<HostHostBusAdapter> hbas = hostmo.getHostStorageSystemMO().getStorageDeviceInfo().getHostBusAdapter();
            for (HostHostBusAdapter hba : hbas) {
                if (hba instanceof HostInternetScsiHba) {
                    HostInternetScsiHba iscsiHba = (HostInternetScsiHba) hba;
                    map.put("type","ISCSI");
                    map.put("name",iscsiHba.getIScsiName());
                }else if (hba instanceof HostFibreChannelHba) {
                    HostFibreChannelHba fcHba = (HostFibreChannelHba) hba;
                    map.put("type","FC");
                    map.put("name",fcHba.getNodeWorldWideName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("vmware error:", e);
            throw e;
        }
        return map;
    }
    /**
     *得到主机的hba
     **/
    public List<Map<String,Object>> getHbasByHostObjectId(String hostObjectId) throws Exception{
        List<Map<String,Object>> hbalist = new ArrayList<>();
        try {
            if (StringUtils.isEmpty(hostObjectId)) {
                logger.error("get Hba error:host ObjectId is null.");
                throw new Exception("get Hba error:host ObjectId is null.");
            }

            String serverguid = vcConnectionHelper.objectID2Serverguid(hostObjectId);
            VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);

            //取得该存储下所有已经挂载的主机ID
            ManagedObjectReference objmor = vcConnectionHelper.objectID2MOR(hostObjectId);
            HostMO hostmo = new HostMO(vmwareContext, objmor);
            //查找对应的iscsi适配器
            String iscsiHbaDevice = null;
            List<HostHostBusAdapter> hbas = hostmo.getHostStorageSystemMO().getStorageDeviceInfo().getHostBusAdapter();
            for (HostHostBusAdapter hba : hbas) {
                if (hba instanceof HostInternetScsiHba) {
                    Map<String,Object> map = new HashMap<>();
                    HostInternetScsiHba iscsiHba = (HostInternetScsiHba) hba;
                    map.put("type","ISCSI");
                    map.put("name",iscsiHba.getIScsiName());
                    hbalist.add(map);
                }else if (hba instanceof HostFibreChannelHba) {
                    Map<String,Object> map = new HashMap<>();
                    HostFibreChannelHba fcHba = (HostFibreChannelHba) hba;
                    map.put("type","FC");
                    map.put("name",fcHba.getNodeWorldWideName());
                    hbalist.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("vmware error:", e);
            throw e;
        }
        return hbalist;
    }
    /**
     *得到主机的hba
     **/
    public List<Map<String,Object>> getHbasByClusterObjectId(String clusterObjectId) throws Exception{
        List<Map<String,Object>> hbalist = new ArrayList<>();
        try {
            if (StringUtils.isEmpty(clusterObjectId)) {
                logger.error("get Hba error:cluster ObjectId is null.");
                throw new Exception("get Hba error:cluster ObjectId is null.");
            }

            String serverguid = vcConnectionHelper.objectID2Serverguid(clusterObjectId);
            VmwareContext vmwareContext = vcConnectionHelper.getServerContext(serverguid);
            //取得该存储下所有已经挂载的主机ID
            ManagedObjectReference objmor = vcConnectionHelper.objectID2MOR(clusterObjectId);
            ClusterMO objmo = new ClusterMO(vmwareContext, objmor);
            List<Pair<ManagedObjectReference, String>> hosts = objmo.getClusterHosts();

            if (hosts != null && hosts.size() > 0) {
                List<Map<String, String>> lists = new ArrayList<>();
                for (Pair<ManagedObjectReference, String> host : hosts) {

                    List<Map<String,Object>> subhbalist = new ArrayList<>();
                    HostMO hostmo = new HostMO(vmwareContext, host.first());
                    List<HostHostBusAdapter> hbas = hostmo.getHostStorageSystemMO().getStorageDeviceInfo().getHostBusAdapter();
                    for (HostHostBusAdapter hba : hbas) {
                        if (hba instanceof HostInternetScsiHba) {
                            Map<String,Object> map = new HashMap<>();
                            HostInternetScsiHba iscsiHba = (HostInternetScsiHba) hba;
                            map.put("type","ISCSI");
                            map.put("name",iscsiHba.getIScsiName());
                            subhbalist.add(map);
                        }else if (hba instanceof HostFibreChannelHba) {
                            Map<String,Object> map = new HashMap<>();
                            HostFibreChannelHba fcHba = (HostFibreChannelHba) hba;
                            map.put("type","FC");
                            map.put("name",fcHba.getNodeWorldWideName());
                            subhbalist.add(map);
                        }
                    }

                    if(subhbalist.size()>0){
                        hbalist.addAll(subhbalist);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("vmware error:", e);
            throw e;
        }
        return hbalist;
    }
    /**
     *使用主机测试目标机的连通性
     **/
    public String testConnectivity(String hostObjectId,List<Map<String, Object>> ethPorts,Map<String, String> vmKernel,VCenterInfo vCenterInfo) throws URISyntaxException, InvalidLogin, InvalidLocale, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        String reStr = null;
        if(StringUtils.isEmpty(hostObjectId)){
            logger.error("host object id is null");
            return null;
        }
        if(ethPorts==null || ethPorts.size()==0){
            logger.error("ethPorts is null");
            return null;
        }
        if(vCenterInfo==null || StringUtils.isEmpty(vCenterInfo.getHostIp())){
            logger.error("vCenter Info is null");
            return null;
        }
        Client vmomiClient = null;
        SessionManager sessionManager = null;
        try {
            HttpConfiguration httpConfig = new HttpConfigurationImpl();
            httpConfig.setTimeoutMs(3000);
            httpConfig.setThumbprintVerifier(new AllowAllThumbprintVerifier());

            HttpClientConfiguration clientConfig = HttpClientConfiguration.Factory.newInstance();
            clientConfig.setHttpConfiguration(httpConfig);
            try {
                context = VmodlContext.getContext();
                context.loadVmodlPackages(new String[]{"com.vmware.vim.binding.vmodl.reflect"});
            }catch (Exception e){
                logger.error("context is not ready",e);
            }
            if (context == null) {
                context = VmodlContext.initContext(new String[]{"com.vmware.vim.binding.vim", "com.vmware.vim.binding.vmodl.reflect"});
            }

            vmomiClient = Client.Factory.createClient(new URI("https://" + vCenterInfo.getHostIp() + "/sdk"), VERSION, context, clientConfig);
            com.vmware.vim.binding.vmodl.ManagedObjectReference svcRef = new com.vmware.vim.binding.vmodl.ManagedObjectReference();
            svcRef.setType("ServiceInstance");
            svcRef.setValue("ServiceInstance");
            ServiceInstance instance = vmomiClient.createStub(ServiceInstance.class, svcRef);
            ServiceInstanceContent serviceInstanceContent = instance.retrieveContent();

            sessionManager = vmomiClient.createStub(SessionManager.class,
                    serviceInstanceContent.getSessionManager());
            sessionManager.login(vCenterInfo.getUserName(), CipherUtils.decryptString(vCenterInfo.getPassword()), "en");
            ManagedObjectReference objmor = vcConnectionHelper.objectID2MOR(hostObjectId);
            com.vmware.vim.binding.vmodl.ManagedObjectReference hostmor = new com.vmware.vim.binding.vmodl.ManagedObjectReference();
            hostmor.setType(objmor.getType());
            hostmor.setValue(objmor.getValue());
            HostSystem hostSystem = vmomiClient.createStub(HostSystem.class, hostmor);
            com.vmware.vim.binding.vmodl.ManagedObjectReference methodexecutor = hostSystem.retrieveManagedMethodExecuter();

            String moid = "ha-cli-handler-network-diag";
            ManagedMethodExecuter methodExecuter = vmomiClient.createStub(ManagedMethodExecuter.class, methodexecutor);
            if (ethPorts != null && ethPorts.size() > 0) {
                String device = null;
                if (vmKernel != null) {
                    device = ToolUtils.getStr(vmKernel.get("device"));
                }

                List<Map<String, Object>> reEthPorts = new ArrayList<>();
                for (Map<String, Object> ethPort : ethPorts) {
                    String mgmtIp = ToolUtils.getStr(ethPort.get("mgmtIp"));
                    if (!StringUtils.isEmpty(mgmtIp)) {
                        try {

                            ManagedMethodExecuter.SoapArgument soapArgument0 = new ManagedMethodExecuter.SoapArgument();
                            soapArgument0.setName("host");
                            soapArgument0.setVal("<host xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns=\"urn:vim25\">" + mgmtIp + "</host>");
                            List<ManagedMethodExecuter.SoapArgument> soapArgumentList = new ArrayList<>();
                            soapArgumentList.add(soapArgument0);

                            if (!StringUtils.isEmpty(device)) {
                                ManagedMethodExecuter.SoapArgument soapArgument1 = new ManagedMethodExecuter.SoapArgument();
                                soapArgument1.setName("interface");
                                soapArgument1.setVal("<interface xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns=\"urn:vim25\">" + device + "</interface>");
                                soapArgumentList.add(soapArgument1);
                            }

                            ManagedMethodExecuter.SoapResult soapResult = methodExecuter.executeSoap(moid, "urn:vim25/6.5", "vim.EsxCLI.network.diag.ping", soapArgumentList.toArray(new ManagedMethodExecuter.SoapArgument[0]));

                            String re = new String(soapResult.getResponse().getBytes("ISO-8859-1"), "UTF-8");
                            logger.info(mgmtIp + "==re==" + re);
                            String packetLost = xmlFormat(re);
                            if (!StringUtils.isEmpty(packetLost) && !"100".equals(packetLost)) {
                                ethPort.put("connectStatus", "true");
                            } else {
                                ethPort.put("connectStatus", "false");
                            }
                        } catch (Exception e) {
                            ethPort.put("connectStatus", "false");
                            logger.error(mgmtIp + "====" + e.toString());
                        }
                        reEthPorts.add(ethPort);
                    }
                }

                if (reEthPorts.size() > 0) {
                    reStr = gson.toJson(reEthPorts);
                }
            }
        }catch (Exception ex){
            logger.error("error:",ex);
        }finally {
            if(sessionManager!=null){
                sessionManager.logout();
            }
            if(vmomiClient!=null){
                vmomiClient.shutdown();
            }
        }

        return reStr;
    }

    public String xmlFormat(String unformattedXml) {
        String packetLost = null;
        try {
            final Document document = parseXmlFile(unformattedXml);
            NodeList sms = document.getElementsByTagName("Summary");
            Element sm = (Element)sms.item(0);
            packetLost = sm.getElementsByTagName("PacketLost").item(0).getFirstChild().getNodeValue();
        } catch (Exception e) {
            logger.error("error:"+e.toString());
        }

        return packetLost;
    }

    private Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*public static void main(String[] args) {
//        try {
//            Gson gson = new Gson();
//            String listStr = VCSDKUtils.getAllVmfsDataStores(null);
//            logger.info("Vmfs listStr==" + listStr);
//            listStr = VCSDKUtils.getAllVmfsDataStores(ToolUtils.STORE_TYPE_VMFS);
//            logger.info("Vmfs listStr==" + listStr);
//            listStr = VCSDKUtils.getAllVmfsDataStores(ToolUtils.STORE_TYPE_NFS);
//            logger.info("Vmfs listStr==" + listStr);
        // logger.info("Vmfs getAllClusters==" + VCSDKUtils.getDataStoresByHostName("10.143.132.17"));
////////////////////////////////////getLunsOnHost//////////////////////////////////////////
//            Map<String,Object> hsdmap = getLunsOnHost("10.143.133.196",10);
//            if(hsdmap!=null ) {
//                HostScsiDisk objhsd = (HostScsiDisk)hsdmap.get("hostScsiDisk");
//                HostMO hostMo = (HostMO)hsdmap.get("host");
//                logger.info("path==" + objhsd.getDevicePath() + "==" + objhsd.getUuid() + "==" + (gson.toJson(objhsd.getCapacity())));
//                logger.info("getName==" + hostMo.getName() + "==" + hostMo.getHostDatastoreSystemMO());
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
//            logger.info("==================over==========");
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
//                logger.info("Vmfs dataStoreStr==" + dataStoreStr);
//
//                Map<String, Object> dsmap = gson.fromJson(dataStoreStr, new TypeToken<Map<String, Object>>() {
//                }.getType());
//                if (dsmap != null) {
//                    String attachTagStr = VCSDKUtils.attachTag(dsmap.get("type").toString(), dsmap.get("id").toString(), serviceLevelName);
//                    logger.info("Vmfs attachTagStr==" + attachTagStr);
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
        logger.info("start creat nfs datastore");
        String accessMode = "";
        accessMode = StringUtils.isEmpty(accessMode) || accessMode.equals("readWrite") ? "readWrite" : "readOnly";

        try {

            VmwareContext vmwareContext = TestVmwareContextFactory.getContext("10.143.132.248", 443, "administrator@vsphere.local", "Pbu4@123");
            DatacenterMO dcMo = new DatacenterMO(vmwareContext, "Datacenter");
            List<ManagedObjectReference> hosts = null;
            if (mountHosts != null && mountHosts.size() > 0) {
                ManagedObjectReference managedObjectReference = null;
                for (int i = 0; i < mountHosts.size(); i++) {
                    String mountHost = mountHosts.get(i);
                    hosts = dcMo.findHost(mountHost);
                    if (hosts != null && hosts.size() > 0) {
                        managedObjectReference = hosts.get(0);
                        HostMO hostMo = new HostMO(vmwareContext, managedObjectReference);
                        HostDatastoreSystemMO hostDatastoreSystemMO = hostMo.getHostDatastoreSystemMO();
                        hostDatastoreSystemMO.createNfsDatastore(serverHost, 0, exportPath, nfsName, accessMode, type);
//                        if (i == 0) {
//                            //todo logic port param needed ?
//                        }
//                        if (i > 0) {
//                            DatastoreMO dsMo = new DatastoreMO(vmwareContext, dcMo.findDatastore(nfsName));
//                            mountNfs(dsMo, hostMo, type);
//                        }
                    } else {
                        result = "failed";
                        logger.error("can not find target host!");
                    }
                }
            } else {
                result = "failed";
                logger.error("{createNfsDatastore/mountHosts} params error");
            }
            logger.info("end creat nfs datastore");
        } catch (Exception e) {
            result = "failed";
            logger.error("vmware error:", e);
        }
    }*/

}
