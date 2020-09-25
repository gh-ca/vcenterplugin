package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.dao.DmeVmwareRalationDao;
import com.dmeplugin.dmestore.entity.DmeVmwareRelation;
import com.dmeplugin.dmestore.model.*;
import com.dmeplugin.dmestore.services.bestpractice.DmeIndicatorConstants;
import com.dmeplugin.dmestore.utils.StringUtil;
import com.dmeplugin.dmestore.utils.ToolUtils;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @ClassName DmeNFSAccessServiceImpl
 * @Description TODO
 * @Author wangxiangyong
 * @Date 2020/9/4 10:33
 * @Version V1.0
 **/
public class DmeNFSAccessServiceImpl implements DmeNFSAccessService {
    private static final Logger LOG = LoggerFactory.getLogger(DmeNFSAccessServiceImpl.class);

    private Gson gson = new Gson();

    private DmeVmwareRalationDao dmeVmwareRalationDao;

    private DmeAccessService dmeAccessService;

    private DmeStorageService dmeStorageService;

    private DataStoreStatisticHistoryService dataStoreStatisticHistoryService;

    private TaskService taskService;

    private VCSDKUtils vcsdkUtils;

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
    }

    public DmeAccessService getDmeAccessService() {
        return dmeAccessService;
    }

    public void setDmeAccessService(DmeAccessService dmeAccessService) {
        this.dmeAccessService = dmeAccessService;
    }

    public DmeStorageService getDmeStorageService() {
        return dmeStorageService;
    }

    public void setDmeStorageService(DmeStorageService dmeStorageService) {
        this.dmeStorageService = dmeStorageService;
    }

    public void setDmeVmwareRalationDao(DmeVmwareRalationDao dmeVmwareRalationDao) {
        this.dmeVmwareRalationDao = dmeVmwareRalationDao;
    }

    public void setDataStoreStatisticHistoryService(DataStoreStatisticHistoryService dataStoreStatisticHistoryService) {
        this.dataStoreStatisticHistoryService = dataStoreStatisticHistoryService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public NFSDataStoreShareAttr getNFSDatastoreShareAttr(String storage_id) throws Exception {
        //根据存储ID 获取逻nfs_share_id
        String nfs_share_id = dmeVmwareRalationDao.getShareIdByStorageId(storage_id);
        String url = StringUtil.stringFormat(DmeConstants.DEFAULT_PATTERN, DmeConstants.DME_NFS_SHARE_DETAIL_URL,
                "nfs_share_id", nfs_share_id);
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        if (responseEntity.getStatusCodeValue() / 100 != 2) {
            LOG.error("获取 NFS Share 信息失败！返回信息：{}", responseEntity.getBody());
            return null;
        }
        String resBody = responseEntity.getBody();
        JsonObject share = gson.fromJson(resBody, JsonObject.class);
        NFSDataStoreShareAttr shareAttr = new NFSDataStoreShareAttr();
        shareAttr.setName(share.get("name").getAsString());
        shareAttr.setShare_path(share.get("share_path").getAsString());
        shareAttr.setDescription(share.get("description").getAsString());
        shareAttr.setOwning_dtree_name(share.get("owning_dtree_name").getAsString());
        //查询客户端列表
        List<AuthClient> authClientList = getNFSDatastoreShareAuthClients(nfs_share_id);
        if (null != authClientList && authClientList.size() > 9) {
            shareAttr.setAuth_client_list(authClientList);
        }
        return shareAttr;
    }

    //通过nfs shareId 查询关联的客户端访问列表
    private List<AuthClient> getNFSDatastoreShareAuthClients(String shareId) throws Exception {
        List<AuthClient> clientList = new ArrayList<>();
        String url = StringUtil.stringFormat(DmeConstants.DEFAULT_PATTERN, DmeConstants.DME_NFS_SHARE_AUTH_CLIENTS_URL, "nfs_share_id", shareId);
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, null);
        if (responseEntity.getStatusCodeValue() / 100 == 2) {
            String resBody = responseEntity.getBody();
            JsonObject resObject = gson.fromJson(resBody, JsonObject.class);
            int total = resObject.get("total").getAsInt();

            if (total > 0) {
                JsonArray array = resObject.getAsJsonArray("auth_client_list");
                for (int i = 0; i < total; i++) {
                    JsonObject client = array.get(i).getAsJsonObject();
                    AuthClient authClient = new AuthClient();
                    authClient.setName(client.get("name").getAsString());
                    authClient.setType(client.get("type").getAsString());
                    authClient.setAccessval(client.get("accessval").getAsString());
                    clientList.add(authClient);
                }
            }
        }
        return clientList;
    }

    @Override
    public NFSDataStoreLogicPortAttr getNFSDatastoreLogicPortAttr(String storage_id) throws Exception {
        //根据存储ID 获取逻辑端口ID
        String logic_port_id = dmeVmwareRalationDao.getLogicPortIdByStorageId(storage_id);

        String url = StringUtil.stringFormat(DmeConstants.DEFAULT_PATTERN, DmeConstants.DME_NFS_LOGICPORT_DETAIL_URL,
                "logic_port_id", logic_port_id);
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        if (responseEntity.getStatusCodeValue() / 100 != 2) {
            LOG.error("NFS 逻辑端口详细信息获取失败！logic_port_id={},返回信息：{}", logic_port_id, responseEntity.getBody());
            return null;
        }
        JsonObject logicPort = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        NFSDataStoreLogicPortAttr logicPortAttr = new NFSDataStoreLogicPortAttr();
        logicPortAttr.setName(logicPort.get("name").getAsString());
        String ipv4 = logicPort.get("mgmt_ip").getAsString();
        ipv4 = (null == ipv4 ? "" : ipv4.trim());
        String ipv6 = logicPort.get("mgmt_ipv6").getAsString();
        logicPortAttr.setIp(!StringUtils.isEmpty(ipv4) ? ipv4 : ipv6);
        String running_status = logicPort.get("running_status").getAsString();
        logicPortAttr.setStatus(running_status);
        logicPortAttr.setRunningStatus(running_status);
        logicPortAttr.setCurrentPort(logicPort.get("current_port_name").getAsString());
        logicPortAttr.setActivePort(logicPort.get("home_port_name").getAsString());
        logicPortAttr.setFailoverGroup("failover_group_name");
        return logicPortAttr;
    }

    @Override
    public List<NFSDataStoreFSAttr> getNFSDatastoreFSAttr(String storage_id) throws Exception {
        //根据存储ID获取fs
        List<String> fsIds = dmeVmwareRalationDao.getFsIdsByStorageId(storage_id);
        List<NFSDataStoreFSAttr> list = new ArrayList<>();
        for (int i = 0; i < fsIds.size(); i++) {
            NFSDataStoreFSAttr fsAttr = new NFSDataStoreFSAttr();
            String file_system_id = fsIds.get(i);
            String url = StringUtil.stringFormat(DmeConstants.DEFAULT_PATTERN, DmeConstants.DME_NFS_FILESERVICE_DETAIL_URL,
                    "file_system_id", file_system_id);
            ResponseEntity<String> responseTuning = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseTuning.getStatusCodeValue() / 100 == 2) {
                JsonObject fsDetail = gson.fromJson(responseTuning.getBody(), JsonObject.class);
                fsAttr.setName(fsDetail.get("name").getAsString());
                fsAttr.setProvisionType(fsDetail.get("alloc_type").getAsString());
                fsAttr.setDevice(fsDetail.get("storage_name").getAsString());
                fsAttr.setStoragePoolName(fsDetail.get("storage_pool_name").getAsString());
                fsAttr.setController(fsDetail.get("owning_controller").getAsString());
                //查询详情，获取tuning信息
                JsonObject tuning = fsDetail.getAsJsonObject("tuning");
                fsAttr.setApplicationScenario(tuning.get("application_scenario").getAsString());
                fsAttr.setDataDeduplication(tuning.get("deduplication_enabled").getAsBoolean());
                fsAttr.setDateCompression(tuning.get("compression_enabled").getAsBoolean());
            }
            list.add(fsAttr);
        }

        return list;
    }

    @Override
    public boolean scanNfs() throws Exception {
        // vcenter侧获取nfsStrorge信息列表 (提取shareIp,sharePath 与dem(ip)发生关联关系)
        // DEM 获取所有存储设备信息 通过ip过滤提取存储ID storageId
        // DME logicPort, 通过存储IDstroage id查询逻辑端口 (需求文档说明通过share ip,但API不支持)
        // DME share, 通过sharePath(可加storageId)查询share 提取fs_name
        // DME FileService, 通过fs_name和storageId查询fs (需求文档说明是通过share path, 但API不支持)

        String store_type = ToolUtils.STORE_TYPE_NFS;
        String listStr = vcsdkUtils.getAllVmfsDataStoreInfos(store_type);
        LOG.info("===list nfs datastore success====\n{}", listStr);
        if (StringUtils.isEmpty(listStr)) {
            return false;
        }
        Map<String, Object> storageOriginal = dmeStorageService.getStorages();
        if (null == storageOriginal || !storageOriginal.get("code").toString().equals("200")) {
            return false;
        }

        //将DME的存储设备集合转换为map key:ip value:Storage
        List<Storage> storages = (List<Storage>) storageOriginal.get("data");
        Map<String, Storage> storageMap = converStorage(storages);

        JsonArray jsonArray = new JsonParser().parse(listStr).getAsJsonArray();
        List<DmeVmwareRelation> relationList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject nfsDatastore = jsonArray.get(i).getAsJsonObject();

            //TODO 从vCenter nfsDataStore信息中提取存储id ip和path
            String nfsStorageId = nfsDatastore.get("nfsStorageId").getAsString();
            String nfsDatastoreIp = nfsDatastore.get("remoteHost").getAsString();
            String nfsDataStoreSharePath = nfsDatastore.get("remotePath").getAsString();
            Storage storageInfo = storageMap.get(nfsDatastoreIp);
            if (null == storageInfo) {
                LOG.warn("扫描NFS存储信息,share ip:{} 再DME侧没有找到对应的存储设备!!!", nfsDatastoreIp);
                continue;
            }
            String storage_id = storageInfo.getId();
            String storage_name = storageInfo.getName();
            DmeVmwareRelation relation = new DmeVmwareRelation();
            relation.setStoreId(nfsStorageId);
            relation.setStorageDeviceId(storage_id);
            relation.setStoreName(storage_name);
            relation.setStoreType(store_type);

            //获取logicPort信息
            Map<String, Object> logicPortInfo = queryLogicPortInfo(storage_id);
            if (null != logicPortInfo && logicPortInfo.size() > 0) {
                String id = ToolUtils.getStr(logicPortInfo.get("id"));
                String name = ToolUtils.getStr(logicPortInfo.get("name"));
                relation.setLogicPortId(id);
                relation.setLogicPortName(name);
            } else {
                LOG.warn("NFSDATASTORE id:" + storage_id + " contains logicport is null!");
            }

            //获取share信息 (条件:sharePath  可加 storageId)
            String fsName = "";
            Map<String, Object> shareInfo = queryShareInfo(nfsDataStoreSharePath);
            if (null != shareInfo && shareInfo.size() > 0) {
                fsName = ToolUtils.getStr(shareInfo.get("fs_name"));
                String id = ToolUtils.getStr(shareInfo.get("id"));
                String name = ToolUtils.getStr(shareInfo.get("name"));
                relation.setShareId(id);
                relation.setShareName(name);
            } else {
                LOG.warn("NFSDATASTORE id:" + storage_id + " contains share is null!");
            }

            //获取fs信息
            Map<String, Object> fsInfo = queryFsInfo(storage_id, fsName);
            if (null != fsInfo && fsInfo.size() > 0) {
                String id = ToolUtils.getStr(fsInfo.get("id"));
                String name = ToolUtils.getStr(fsInfo.get("name"));
                relation.setFsId(id);
                relation.setFsName(name);
            } else {
                LOG.warn("NFSDATASTORE id:" + storage_id + " contains fs is null!");
            }

            relationList.add(relation);
        }

        //数据库保存datastorage 与nfs的 share fs logicPort关系信息
        if (relationList.size() > 0) {
            return dmeVmwareRelationDBProcess(relationList, store_type);
        }
        return false;
    }

    private boolean dmeVmwareRelationDBProcess(List<DmeVmwareRelation> relationList, String storeType) throws Exception {
        //本地全量查询NFS
        List<String> storageIds = dmeVmwareRalationDao.getAllStorageIdByType(storeType);

        List<DmeVmwareRelation> newList = new ArrayList<>();
        List<DmeVmwareRelation> upList = new ArrayList<>();
        for (DmeVmwareRelation relation : relationList) {
            String storege_id = relation.getStoreId();
            if (storageIds.contains(storege_id)) {
                upList.add(relation);
                storageIds.remove(storege_id);
            } else {
                newList.add(relation);
            }
        }

        //更新
        if (!upList.isEmpty()) {
            dmeVmwareRalationDao.update(upList);
        }

        //新增
        if (!newList.isEmpty()) {
            dmeVmwareRalationDao.save(newList);
        }
        //删除
        if (!storageIds.isEmpty()) {
            dmeVmwareRalationDao.deleteByStorageId(storageIds);
        }
        return true;
    }

    //按条件查询share 暂认为 storage与share是一对一的关系
    private Map<String, Object> queryShareInfo(String sharePath) throws Exception {
        //ResponseEntity responseEntity = listShareByStorageId(storageId);
        ResponseEntity responseEntity = listShareBySharePath(sharePath);
        if (responseEntity.getStatusCodeValue() / 100 != 2) {
            Object object = responseEntity.getBody();
            List<Map<String, Object>> list = converShare(object);
            if (list.size() > 0) {
                return list.get(0);
            }
        }
        return null;
    }

    //通过storageId查询share信息
    private ResponseEntity listShareByStorageId(String storageId) throws Exception {
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("storage_id", storageId);
        ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_NFS_SHARE_URL, HttpMethod.POST, gson.toJson(requestbody));
        return responseEntity;
    }

    //通过share_path查询share信息
    private ResponseEntity listShareBySharePath(String sharePath) throws Exception {
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("share_path", sharePath);
        ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_NFS_SHARE_URL, HttpMethod.POST, gson.toJson(requestbody));
        return responseEntity;
    }

    //解析share信息
    private List<Map<String, Object>> converShare(Object object) {
        List<Map<String, Object>> shareList = new ArrayList<>();
        JsonArray jsonArray;
        String strObject = gson.toJson(object);
        if (strObject.indexOf("total") > -1 && strObject.indexOf("nfs_share_info_list") > -1) {
            JsonObject temp = new JsonParser().parse(object.toString()).getAsJsonObject();
            jsonArray = temp.get("nfs_share_info_list").getAsJsonArray();
        } else {
            jsonArray = new JsonParser().parse(object.toString()).getAsJsonArray();
        }
        for (JsonElement jsonElement : jsonArray) {
            Map<String, Object> shareMap = new HashMap<>();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            shareMap.put("id", ToolUtils.getStr(jsonObject.get("id")));
            shareMap.put("name", ToolUtils.getStr(jsonObject.get("name")));
            shareMap.put("share_path", ToolUtils.getStr(jsonObject.get("share_path")));
            shareMap.put("storage_id", ToolUtils.getStr(jsonObject.get("storage_id")));
            shareMap.put("device_name", ToolUtils.getStr(jsonObject.get("device_name")));
            shareMap.put("owning_dtree_id", ToolUtils.getStr(jsonObject.get("owning_dtree_id")));
            shareMap.put("owning_dtree_name", ToolUtils.getStr(jsonObject.get("owning_dtree_name")));
            shareList.add(shareMap);
        }
        return shareList;
    }

    //DME按条件获取fs
    private Map<String, Object> queryFsInfo(String storageId, String fsName) throws Exception {
        ResponseEntity responseEntity = listFsByStorageId(storageId, fsName);
        if (responseEntity.getStatusCodeValue() / 100 != 2) {
            Object object = responseEntity.getBody();
            List<Map<String, Object>> list = converFs(object);
            if (list.size() > 0) {
                return list.get(0);
            }
        }
        return null;
    }

    //DME通过storageId获取fs信息
    private ResponseEntity listFsByStorageId(String storageId, String fsName) throws Exception {
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("storage_id", storageId);
        if (!StringUtils.isEmpty(fsName)) {
            requestbody.put("name", fsName);
        }
        ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_NFS_FILESERVICE_QUERY_URL, HttpMethod.POST, gson.toJson(requestbody));
        return responseEntity;
    }

    //解析fs信息
    private List<Map<String, Object>> converFs(Object object) {
        List<Map<String, Object>> fsList = new ArrayList<>();
        JsonArray jsonArray;
        String strObject = gson.toJson(object);
        if (strObject.indexOf("total") > -1 && strObject.indexOf("data") > -1) {
            JsonObject temp = new JsonParser().parse(object.toString()).getAsJsonObject();
            jsonArray = temp.get("data").getAsJsonArray();
        } else {
            jsonArray = new JsonParser().parse(object.toString()).getAsJsonArray();
        }
        for (JsonElement jsonElement : jsonArray) {
            Map<String, Object> fsMap = new HashMap<>();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            fsMap.put("id", ToolUtils.getStr(jsonObject.get("id")));
            fsMap.put("name", ToolUtils.getStr(jsonObject.get("name")));
            fsMap.put("storage_id", ToolUtils.getStr(jsonObject.get("storage_id")));
            fsMap.put("storage_name", ToolUtils.getStr(jsonObject.get("storage_name")));
            fsMap.put("storage_pool_name", ToolUtils.getStr(jsonObject.get("storage_pool_name")));
            fsMap.put("tier_id", ToolUtils.getStr(jsonObject.get("tier_id")));
            fsMap.put("tier_name", ToolUtils.getStr(jsonObject.get("tier_name")));
            fsList.add(fsMap);
        }
        return fsList;
    }

    //按条件查询logicPort
    private Map<String, Object> queryLogicPortInfo(String storageId) throws Exception {
        ResponseEntity responseEntity = listLogicPortByStorageId(storageId);
        if (responseEntity.getStatusCodeValue() / 100 != 2) {
            Object object = responseEntity.getBody();
            List<Map<String, Object>> list = convertLogicPort(storageId, object);
            if (list.size() > 0) {
                return list.get(0);
            }
        }
        return null;
    }

    //通过storageId查询logicPort信息
    private ResponseEntity listLogicPortByStorageId(String storageId) throws Exception {
        String url = StringUtil.stringFormat(DmeConstants.DEFAULT_PATTERN, DmeConstants.DME_NFS_LOGICPORT_QUERY_URL, "storage_id", storageId);
        ResponseEntity responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        return responseEntity;
    }

    //解析logicPort信息
    private List<Map<String, Object>> convertLogicPort(String storageId, Object object) {
        List<Map<String, Object>> logicPortList = new ArrayList<>();
        JsonArray jsonArray;
        String strObject = gson.toJson(object);
        if (strObject.indexOf("total") > -1 && strObject.indexOf("logic_ports") > -1) {
            JsonObject temp = new JsonParser().parse(object.toString()).getAsJsonObject();
            jsonArray = temp.get("logic_ports").getAsJsonArray();
        } else {
            jsonArray = new JsonParser().parse(object.toString()).getAsJsonArray();
        }
        for (JsonElement jsonElement : jsonArray) {
            Map<String, Object> logicPortMap = new HashMap<>();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            logicPortMap.put("id", ToolUtils.getStr(jsonObject.get("id")));
            logicPortMap.put("name", ToolUtils.getStr(jsonObject.get("name")));
            logicPortMap.put("storage_id", storageId);
            logicPortMap.put("home_port_id", ToolUtils.getStr(jsonObject.get("home_port_id")));
            logicPortMap.put("home_port_name", ToolUtils.getStr(jsonObject.get("home_port_name")));
            logicPortMap.put("current_port_id", ToolUtils.getStr(jsonObject.get("current_port_id")));
            logicPortMap.put("current_port_id", ToolUtils.getStr(jsonObject.get("current_port_id")));
            logicPortList.add(logicPortMap);
        }
        return logicPortList;
    }

    //将storage结合转换为map key:ip
    private Map<String, Storage> converStorage(List<Storage> storages) {
        Map<String, Storage> storageMap = new HashMap<>();
        for (Storage storage : storages) {
            String ip = storage.getIp();
            storageMap.put(ip, storage);
        }
        return storageMap;
    }


    @Override
    public List<NfsDataInfo> listNfs() throws Exception {
        List<NfsDataInfo> relists = null;
        try {
            //从关系表中取得DME卷与vcenter存储的对应关系
            List<DmeVmwareRelation> dvrlist = dmeVmwareRalationDao.getDmeVmwareRelation(ToolUtils.STORE_TYPE_NFS);
            LOG.info("dvrlist==" + gson.toJson(dvrlist));
            if (dvrlist != null && dvrlist.size() > 0) {
                //整理数据
                Map<String, DmeVmwareRelation> dvrMap = getDvrMap(dvrlist);
                //取得vcenter中的所有nfs存储。
                String listStr = vcsdkUtils.getAllVmfsDataStoreInfos(ToolUtils.STORE_TYPE_NFS);
                LOG.info("nfs listStr==" + listStr);
                if (!StringUtils.isEmpty(listStr)) {
                    JsonArray jsonArray = new JsonParser().parse(listStr).getAsJsonArray();
                    if (jsonArray != null && jsonArray.size() > 0) {
                        relists = new ArrayList<>();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject jo = jsonArray.get(i).getAsJsonObject();
                            //LOG.info("jo==" + jo.toString());
                            String vmwareObjectId = ToolUtils.jsonToStr(jo.get("objectid"));
                            if (!StringUtils.isEmpty(vmwareObjectId)) {
                                //对比数据库关系表中的数据，只显示关系表中的数据
                                if (dvrMap != null && dvrMap.get(vmwareObjectId) != null) {
                                    NfsDataInfo nfsDataInfo = new NfsDataInfo();
                                    double capacity = ToolUtils.getDouble(jo.get("capacity")) / ToolUtils.GI;
                                    double freeSpace = ToolUtils.getDouble(jo.get("freeSpace")) / ToolUtils.GI;
                                    double reserveCapacity = (ToolUtils.getDouble(jo.get("capacity")) - ToolUtils.getDouble(jo.get("freeSpace"))) / ToolUtils.GI;

                                    nfsDataInfo.setName(ToolUtils.jsonToStr(jo.get("name")));

                                    nfsDataInfo.setCapacity(capacity);
                                    nfsDataInfo.setFreeSpace(freeSpace);
                                    nfsDataInfo.setReserveCapacity(reserveCapacity);

                                    nfsDataInfo.setShareIp(ToolUtils.jsonToStr(jo.get("remoteHost")));

                                    DmeVmwareRelation dvr = dvrMap.get(vmwareObjectId);

                                    nfsDataInfo.setSharePath(dvr.getVolumeShare());

                                    nfsDataInfo.setLogicPort(dvr.getLogicPortName());
                                    nfsDataInfo.setLogicPortId(dvr.getLogicPortId());
                                    nfsDataInfo.setShare(dvr.getShareName());
                                    nfsDataInfo.setShareId(dvr.getShareId());
                                    nfsDataInfo.setFs(dvr.getFsName());
                                    nfsDataInfo.setFsId(dvr.getFsId());

                                    nfsDataInfo.setObjectid(ToolUtils.jsonToStr(jo.get("objectid")));

                                    String fsUrl = "";
                                    try {
                                        fsUrl = StringUtil.stringFormat(DmeConstants.DEFAULT_PATTERN, DmeConstants.DME_NFS_FILESERVICE_DETAIL_URL,
                                                "file_system_id", dvr.getFsId());
                                        ResponseEntity<String> responseTuning = dmeAccessService.access(fsUrl, HttpMethod.GET, null);
                                        if (responseTuning.getStatusCodeValue() / 100 == 2) {
                                            JsonObject fsDetail = gson.fromJson(responseTuning.getBody(), JsonObject.class);

                                            nfsDataInfo.setStatus(ToolUtils.jsonToStr(fsDetail.get("health_status")));
                                            nfsDataInfo.setDeviceId(ToolUtils.jsonToStr(fsDetail.get("storage_id")));
                                            nfsDataInfo.setDevice(ToolUtils.jsonToStr(fsDetail.get("storage_name")));
                                        }
                                    } catch (Exception e) {
                                        LOG.error("DME link error url:" + fsUrl + ",error:" + e.getMessage());
                                    }

                                    relists.add(nfsDataInfo);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("list nfs error:", e);
            throw e;
        }
        LOG.info("relists===" + (relists == null ? "null" : (relists.size() + "==" + gson.toJson(relists))));
        return relists;
    }

    //整理关系表数据
    private Map<String, DmeVmwareRelation> getDvrMap(List<DmeVmwareRelation> dvrlist) {
        Map<String, DmeVmwareRelation> remap = null;
        try {
            if (dvrlist != null && dvrlist.size() > 0) {
                remap = new HashMap<>();
                for (DmeVmwareRelation dvr : dvrlist) {
                    remap.put(dvr.getStoreId(), dvr);
                }
            }
        } catch (Exception e) {
            LOG.error("error:", e);
        }
        return remap;
    }

    //整理存储信息
    private Map<String, String> getStorNameMap(Map<String, Object> storagemap) {
        Map<String, String> remap = null;
        try {
            if (storagemap != null && storagemap.get("data") != null) {
                List<Storage> list = (List<Storage>) storagemap.get("data");
                if (list != null && list.size() > 0) {
                    remap = new HashMap<>();
                    for (Storage sr : list) {
                        remap.put(sr.getId(), sr.getName());
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("error:", e);
        }
        return remap;
    }

    @Override
    public List<NfsDataInfo> listNfsPerformance(List<String> fsIds) throws Exception {
        List<NfsDataInfo> relists = null;
        try {
            if (fsIds != null && fsIds.size() > 0) {
                Map<String, Object> params = new HashMap<>();
                params.put("obj_ids", fsIds);
                Map<String, Object> remap = dataStoreStatisticHistoryService.queryNfsStatisticCurrent(params);
                LOG.info("remap===" + gson.toJson(remap));
                if (remap != null && remap.get("data") != null) {
                    JsonObject dataJson = (JsonObject) remap.get("data");
                    if (dataJson != null) {
                        relists = new ArrayList<>();
                        for (String fsId : fsIds) {
                            JsonObject statisticObject = dataJson.getAsJsonObject(fsId);
                            if (statisticObject != null) {
                                NfsDataInfo nfsDataInfo = new NfsDataInfo();
                                nfsDataInfo.setFsId(fsId);
                                nfsDataInfo.setOPS(ToolUtils.jsonToInt(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_VMFS_THROUGHPUT), null));
                                nfsDataInfo.setBandwidth(ToolUtils.jsonToDou(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_VMFS_BANDWIDTH), null));
                                nfsDataInfo.setReadResponseTime(ToolUtils.jsonToInt(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_VMFS_READRESPONSETIME), null));
                                nfsDataInfo.setWriteResponseTime(ToolUtils.jsonToInt(statisticObject.get(DmeIndicatorConstants.COUNTER_ID_VMFS_WRITERESPONSETIME), null));
                                relists.add(nfsDataInfo);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("list nfs performance error:", e);
            throw e;
        }
        LOG.info("listNfsPerformance relists===" + (relists == null ? "null" : (relists.size() + "==" + gson.toJson(relists))));
        return relists;
    }

    /**
     * Mount nfs,params中包含了 include:
     * dataStoreObjectId: datastore的object id
     * dataStoreName: datastore名称  必
     * list<map<str,str>> hosts: 主机hostId,主机名称hostName 必 （主机与集群二选一）
     * list<map<str,str>>  clusters: 集群clusterId,集群名称clusterName 必（主机与集群二选一）
     * str mountType: 挂载模式（只读或读写）  readOnly/readWrite
     *
     * @param params: include dataStoreName,hosts,clusters,mountType
     * @return: ResponseBodyBean
     */
    @Override
    public void mountNfs(Map<String, Object> params) throws Exception {
        if (params != null) {
            String dataStoreObjectId = ToolUtils.getStr(params.get("dataStoreObjectId"));
            LOG.info("dataStoreObjectId====" + dataStoreObjectId);
            if (!StringUtils.isEmpty(dataStoreObjectId)) {
                //查询数据库，得到对应的nfs信息
                DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dataStoreObjectId);
                if (dvr != null && dvr.getShareId() != null) {
                    params.put("shareId", dvr.getShareId());
                    //挂载卷
                    String taskId = mountnfsToHost(params);
                    if (!StringUtils.isEmpty(taskId)) {
                        List<String> taskIds = new ArrayList<>();
                        taskIds.add(taskId);
                        LOG.info("taskIds====" + taskIds);
                        //查询看挂载任务是否完成。
                        boolean mountFlag = taskService.checkTaskStatus(taskIds);
                        if (mountFlag) { //DME挂载完成
                            //调用vCenter在主机上扫描卷和Datastore，并挂载主机
                            List<Map<String, String>> clusters = null;
                            List<Map<String, String>> hosts = null;
                            String mountType = null;
                            if (params.get("hosts") != null) {
                                hosts = (List<Map<String, String>>) params.get("hosts");
                            }
                            if (params.get("clusters") != null) {
                                clusters = (List<Map<String, String>>) params.get("clusters");
                            }
                            vcsdkUtils.mountNfsOnCluster(dataStoreObjectId,
                                    clusters, hosts, ToolUtils.getStr(params.get("mountType")));

                        } else {
                            throw new Exception("DME mount nfs error(task status)!");
                        }
                    } else {
                        throw new Exception("DME mount nfs error(task is null)!");
                    }
                }
            } else {
                throw new Exception("DME dataStore ObjectId is null!");
            }
        } else {
            throw new Exception("Mount nfs parameter exception:" + params);
        }
    }

    //挂载nfs,向share中添加客户端IP
    private String mountnfsToHost(Map<String, Object> params) {
        String taskId = "";
        try {
            if (params == null) {
                LOG.info("mount nfs To Host fail: params is null");
                throw new Exception("mount nfs To Host fail: params is null");
            }
            if (StringUtils.isEmpty(params.get("shareId"))) {
                LOG.info("mount nfs To Host fail: share Id is null");
                throw new Exception("mount nfs To Host fail: share Id is null");
            }
            if (params.get("hosts") == null && params.get("clusters") == null) {
                LOG.info("mount nfs To Host fail: hosts or clusters is null");
                throw new Exception("mount nfs To Host fail: hosts or clusters is null");
            }
            //取得目标主机
            List<String> hostlist = null;
            if (params.get("hosts") != null) {
                hostlist = new ArrayList<>();
                List<Map<String, String>> hosts = (List<Map<String, String>>) params.get("hosts");
                if (hosts != null && hosts.size() > 0) {
                    for (Map<String, String> hostmap : hosts) {
                        hostlist.add(hostmap.get("hostName"));
                    }
                }
            } else if (params.get("clusters") != null) {
                //取得没有挂载这个存储的所有主机，以方便后面过滤
                hostlist = vcsdkUtils.getUnmoutHostsOnCluster(ToolUtils.getStr(params.get("dataStoreObjectId")), (List<Map<String, String>>) params.get("clusters"));
            }

            //修改dme中的share
            if (hostlist != null && hostlist.size() > 0) {
                Map<String, Object> requestbody = new HashMap<>();
                requestbody.put("id", ToolUtils.getStr(params.get("shareId")));

                List<Map<String, Object>> listAddition = new ArrayList<>();
                for (String hostIp : hostlist) {
                    Map<String, Object> addition = new HashMap<>();
                    addition.put("name", hostIp);
                    addition.put("accessval", ToolUtils.getStr(params.get("mountType")));
                    addition.put("all_squash", "all_squash");
                    addition.put("root_squash", "root_squash");
                    addition.put("sync", "synchronization");
                    listAddition.add(addition);
                }
                requestbody.put("nfs_share_client_addition", listAddition);

                LOG.info("mountnfs requestbody==" + gson.toJson(requestbody));

                String url = StringUtil.stringFormat(DmeConstants.DEFAULT_PATTERN, DmeConstants.DME_NFS_SHARE_DETAIL_URL,
                        "nfs_share_id", ToolUtils.getStr(params.get("shareId")));
                LOG.info("mountnfs URL===" + url);
                ResponseEntity responseEntity = dmeAccessService.access(url, HttpMethod.PUT, gson.toJson(requestbody));

                LOG.info("mountnfs responseEntity==" + responseEntity.toString());
                if (responseEntity.getStatusCodeValue() == 202) {
                    JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                    if (jsonObject != null && jsonObject.get("task_id") != null) {
                        taskId = ToolUtils.jsonToStr(jsonObject.get("task_id"));
                        LOG.info("mountnfs task_id====" + taskId);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("mountnfs error:", e);
        }
        return taskId;
    }

    //卸载NFS
    public void unmountNfs(Map<String, Object> params) throws Exception {
        // 过滤vm注册的主机和集群,此步骤暂未处理
        if (null != params) {
            String dataStoreObjectId = ToolUtils.getStr(params.get("dataStoreObjectId"));
            String hostId = ToolUtils.getStr(params.get("hostId"));
            String clusterId = ToolUtils.getStr(params.get("clusterId"));

            //vcenter侧主机 集群二选一
            if (!StringUtils.isEmpty(hostId)) {
                unmountNfsFromHost(dataStoreObjectId, hostId);
            } else if (!StringUtils.isEmpty(clusterId)) {
                unmountNfsFromCluster(dataStoreObjectId, clusterId);
            } else {
                throw new Exception("unmount nfs parameter exception,No host or cluster is specified :" + params);
            }
            // vcenter refresh一下?

            //***dme侧卸载
            // 1获取dme的share 2 获取share下的客户端访问列表
            DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dataStoreObjectId);
            if (null != dvr) {
                String shareId = dvr.getShareId();
                List<AuthClient> authClientList = getNFSDatastoreShareAuthClients(shareId);
                if (null != authClientList && authClientList.size() > 0) {
                    //卸载
                    List<String> authClientIds = new ArrayList<>();
                    for (AuthClient authClient : authClientList) {
                        authClientIds.add(authClient.getId());
                    }
                    String taskId = deleteAuthClient(shareId, authClientIds);
                    if (!StringUtils.isEmpty(taskId)) {
                        List<String> taskIds = new ArrayList<>();
                        taskIds.add(taskId);
                        LOG.info("taskIds====" + taskIds);
                        //查询看删除任务是否完成。
                        boolean unmountFlag = taskService.checkTaskStatus(taskIds);
                        if (unmountFlag) { //DME NFS SHAER 删除authClient完成
                            LOG.info("unmountNfs delete authClient success!");
                        } else {
                            throw new Exception("DME mount nfs error(task status)!");
                        }
                    } else {
                        throw new Exception("DME mount nfs error(task is null)!");
                    }
                }
            }
        } else {
            throw new Exception("unmount nfs parameter exception:" + params);
        }
    }

    @Override
    public void deleteNfs(Map<String, Object> params) throws Exception {
        if (null != params) {
            String dataStorageId = ToolUtils.getStr(params.get("dataStoreObjectId"));
            List<Map<String, String>> hosts = getHostsMountDataStoreByDsObjectId(dataStorageId);
            List<String> hostIds = new ArrayList<>();
            if (null != hosts && hosts.size() > 0) {
                for (Map<String, String> hostMap : hosts) {
                    String hostId = hostMap.get("hostId");
                    hostIds.add(hostId);
                }
            }
            //vcenter侧删除
            if (hostIds.size() > 0) {
                vcsdkUtils.deleteNfs(dataStorageId, hostIds);
            }
            //DME侧删除 1 获取dme侧存储的share fs(暂认为是一对一关系)
            DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dataStorageId);
            if (null != dvr) {
                String shareId = dvr.getShareId();
                String fsId = dvr.getFsId();
                List<String> taskIds = new ArrayList<>();
                if(!StringUtils.isEmpty(shareId)){
                    String deleteShareTaskId = deleteNfsShare(Arrays.asList(shareId));
                    if(!StringUtils.isEmpty(deleteShareTaskId)){
                        taskIds.add(deleteShareTaskId);
                    }
                }
                if(!StringUtils.isEmpty(fsId)){
                    String deletefsTaskId = deleteNfsFs(Arrays.asList(fsId));
                    if(!StringUtils.isEmpty(deletefsTaskId)){
                        taskIds.add(deletefsTaskId);
                    }
                }
                if(taskIds.size() > 0){
                    boolean deleteFlag = taskService.checkTaskStatus(taskIds);
                    if (deleteFlag) { //DME NFS 删除 SHAER FS
                        LOG.info("Nfs delete share fs success!");
                    } else {
                        throw new Exception("DME delete nfs error(task status)!");
                    }
                }
            }
        }
    }

    private void unmountNfsFromHost(String dataStoreObjectId, String hsotId) throws Exception {
        vcsdkUtils.unmountNfsOnHost(dataStoreObjectId, hsotId);
    }

    private void unmountNfsFromCluster(String dataStoreObjectId, String clusterId) throws Exception {
        vcsdkUtils.unmountNfsOnCluster(dataStoreObjectId, clusterId);
    }

    //dem卸载share访问客户端列表
    private String deleteAuthClient(String shareId, List<String> authClientIds) {
        String taskId = "";
        Map<String, Object> requestbody = new HashMap<>();
        List<Map<String, Object>> listAddition = new ArrayList<>();
        for (String authClientId : authClientIds) {
            Map<String, Object> addtion = new HashMap<>();
            addtion.put("nfs_share_client_id_in_storage", authClientId);
            listAddition.add(addtion);
        }
        requestbody.put("id", shareId);
        requestbody.put("nfs_share_client_deletion", listAddition);
        LOG.info("unmountnfs authClient requestbody==" + gson.toJson(requestbody));
        try {
            String url = StringUtil.stringFormat(DmeConstants.DEFAULT_PATTERN, DmeConstants.DME_NFS_SHARE_DETAIL_URL, "nfs_share_id", shareId);
            ResponseEntity responseEntity = dmeAccessService.access(url, HttpMethod.PUT, requestbody.toString());

            LOG.info("unmountnfs authClient responseEntity==" + responseEntity.toString());
            if (responseEntity.getStatusCodeValue() == 202) {
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                if (jsonObject != null && jsonObject.get("task_id") != null) {
                    taskId = ToolUtils.jsonToStr(jsonObject.get("task_id"));
                    LOG.info("unmountnfs authClient task_id====" + taskId);
                }
            }
        } catch (Exception e) {
            LOG.error("unmountnfs authClient error:", e);
        }
        return taskId;
    }

    private String deleteNfsShare(List<String> shareIds) {
        String taskId = "";
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("id_list", shareIds);
        LOG.info("delete nfs share requestbody==" + gson.toJson(requestbody));
        try {
            ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_NFS_SHARE_DELETE_URL, HttpMethod.POST, gson.toJson(requestbody));
            LOG.info("delete nfs share responseEntity==" + responseEntity.toString());
            if (responseEntity.getStatusCodeValue() == 202) {
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                if (jsonObject != null && jsonObject.get("task_id") != null) {
                    taskId = ToolUtils.jsonToStr(jsonObject.get("task_id"));
                    LOG.info("delete nfs share  task_id====" + taskId);
                }
            }
        } catch (Exception e) {
            LOG.error("delete nfs share error:", e);
        }
        return taskId;
    }

    private String deleteNfsFs(List<String> fsIds) {
        String taskId = "";
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("file_system_ids", fsIds);
        LOG.info("delete nfs fs requestbody==" + gson.toJson(requestbody));
        try {
            ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_NFS_FS_DELETE_URL, HttpMethod.POST, gson.toJson(requestbody));
            LOG.info("delete nfs fs responseEntity==" + responseEntity.toString());
            if (responseEntity.getStatusCodeValue() == 202) {
                JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                if (jsonObject != null && jsonObject.get("task_id") != null) {
                    taskId = ToolUtils.jsonToStr(jsonObject.get("task_id"));
                    LOG.info("delete nfs fs  task_id====" + taskId);
                }
            }
        } catch (Exception e) {
            LOG.error("delete nfs fs error:", e);
        }
        return taskId;
    }

    public List<Map<String, String>> getHostsMountDataStoreByDsObjectId(String dataStoreObjectId) throws Exception {
        List<Map<String, String>> lists = null;
        try {
            //取得vcenter中的所有挂载了当前存储的host
            String listStr = vcsdkUtils.getHostsByDsObjectId(dataStoreObjectId, false);
            LOG.info("host getHostsMountDataStoreByDsObjectId==" + listStr);
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() {
                }.getType());
            }
        } catch (Exception e) {
            LOG.error("get Hosts MountDataStore By DsObjectId error:", e);
            throw e;
        }
        LOG.info("getHostsMountDataStoreByDsObjectId===" + (lists == null ? "null" : (lists.size() + "==" + gson.toJson(lists))));
        return lists;
    }


}
