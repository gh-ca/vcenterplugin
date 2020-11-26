package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.dao.DmeVmwareRalationDao;
import com.dmeplugin.dmestore.entity.DmeVmwareRelation;
import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.exception.DmeSqlException;
import com.dmeplugin.dmestore.exception.VcenterException;
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
 * @author wangxiangyong
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
    public NfsDataStoreShareAttr getNfsDatastoreShareAttr(String storageObjectId) throws DMEException {
        //根据存储ID 获取逻nfs_share_id
        String nfsShareId = dmeVmwareRalationDao.getShareIdByStorageId(storageObjectId);
        String url = DmeConstants.DME_NFS_SHARE_DETAIL_URL.replace("{nfs_share_id}", nfsShareId);
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        if (responseEntity.getStatusCodeValue() / 100 != 2) {
            LOG.error("获取 NFS Share 信息失败！返回信息：{}", responseEntity.getBody());
            return null;
        }
        String resBody = responseEntity.getBody();
        JsonObject share = gson.fromJson(resBody, JsonObject.class);
        NfsDataStoreShareAttr shareAttr = new NfsDataStoreShareAttr();
        shareAttr.setName(share.get("name").getAsString());
        shareAttr.setFsName(ToolUtils.jsonToStr(share.get("fs_name"), null));
        shareAttr.setSharePath(ToolUtils.jsonToStr(share.get("share_path")));
        shareAttr.setDescription(ToolUtils.jsonToStr(share.get("description")));
        shareAttr.setDeviceName(ToolUtils.jsonToStr(share.get("device_name"), null));
        shareAttr.setOwningDtreeName(ToolUtils.jsonToStr(share.get("owning_dtree_name"), null));
        shareAttr.setOwningDtreeId(ToolUtils.jsonToStr(share.get("owning_dtree_id"), null));
        //查询客户端列表
        List<AuthClient> authClientList = getNfsDatastoreShareAuthClients(nfsShareId);
        if (null != authClientList && authClientList.size() > 0) {
            shareAttr.setAuthClientList(authClientList);
        }
        return shareAttr;
    }

    //通过nfs shareId 查询关联的客户端访问列表
    private List<AuthClient> getNfsDatastoreShareAuthClients(String shareId) throws DMEException {
        List<AuthClient> clientList = new ArrayList<>();
        String url = DmeConstants.DME_NFS_SHARE_AUTH_CLIENTS_URL.replace("{nfs_share_id}", shareId);
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, gson.toJson(new HashMap<>()));
        if (responseEntity.getStatusCodeValue() / 100 == 2) {
            String resBody = responseEntity.getBody();
            JsonObject resObject = gson.fromJson(resBody, JsonObject.class);
            int total = resObject.get("total").getAsInt();

            if (total > 0) {
                JsonArray array = resObject.getAsJsonArray("auth_client_list");
                for (int i = 0; i < total; i++) {
                    JsonObject client = array.get(i).getAsJsonObject();
                    AuthClient authClient = new AuthClient();
                    authClient.setName(ToolUtils.jsonToStr(client.get("name")));
                    authClient.setType(ToolUtils.jsonToStr(client.get("type")));
                    authClient.setAccessval(ToolUtils.jsonToStr(client.get("accessval")));
                    authClient.setId(ToolUtils.jsonToStr(client.get("id")));
                    clientList.add(authClient);
                }
            }
        }
        return clientList;
    }

    @Override
    public NfsDataStoreLogicPortAttr getNfsDatastoreLogicPortAttr(String storageObjectId) throws DMEException {
        //根据存储ID 获取逻辑端口ID
        String logicPortId = dmeVmwareRalationDao.getLogicPortIdByStorageId(storageObjectId);

        String url = DmeConstants.DME_NFS_LOGICPORT_DETAIL_URL.replace("{logic_port_id}", logicPortId);
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        if (responseEntity.getStatusCodeValue() / 100 != 2) {
            LOG.error("NFS 逻辑端口详细信息获取失败！logic_port_id={},返回信息：{}", logicPortId, responseEntity.getBody());
            return null;
        }
        JsonObject logicPort = gson.fromJson(responseEntity.getBody(), JsonObject.class);
        NfsDataStoreLogicPortAttr logicPortAttr = new NfsDataStoreLogicPortAttr();
        logicPortAttr.setName(ToolUtils.jsonToStr(logicPort.get("name")));
        String ipv4 = ToolUtils.jsonToStr(logicPort.get("mgmt_ip"), null);
        ipv4 = (null == ipv4 ? "" : ipv4.trim());
        String ipv6 = ToolUtils.jsonToStr(logicPort.get("mgmt_ipv6"), null);
        logicPortAttr.setIp(!StringUtils.isEmpty(ipv4) ? ipv4 : ipv6);
        String runningStatus = ToolUtils.jsonToStr(logicPort.get("running_status"));
        logicPortAttr.setStatus(ToolUtils.jsonToStr(logicPort.get("operational_status"), null));
        logicPortAttr.setRunningStatus(runningStatus);
        logicPortAttr.setCurrentPort(ToolUtils.jsonToStr(logicPort.get("current_port_name")));
        logicPortAttr.setActivePort(ToolUtils.jsonToStr(logicPort.get("home_port_name")));
        logicPortAttr.setFailoverGroup(ToolUtils.jsonToStr(logicPort.get("failover_group_name"), null));
        return logicPortAttr;
    }

    @Override
    public List<NfsDataStoreFsAttr> getNfsDatastoreFsAttr(String storageObjectId) throws DMEException {
        //根据存储ID获取fs
        List<String> fsIds = dmeVmwareRalationDao.getFsIdsByStorageId(storageObjectId);
        List<NfsDataStoreFsAttr> list = new ArrayList<>();
        for (int i = 0; i < fsIds.size(); i++) {
            NfsDataStoreFsAttr fsAttr = new NfsDataStoreFsAttr();
            String fileSystemId = fsIds.get(i);
            if (StringUtils.isEmpty(fileSystemId)) {
                continue;
            }
            String url = DmeConstants.DME_NFS_FILESERVICE_DETAIL_URL.replace("{file_system_id}", fileSystemId);
            ResponseEntity<String> responseTuning = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseTuning.getStatusCodeValue() / 100 == 2) {
                fsAttr.setFileSystemId(fileSystemId);
                JsonObject fsDetail = gson.fromJson(responseTuning.getBody(), JsonObject.class);
                fsAttr.setName(fsDetail.get("name").getAsString());
                fsAttr.setProvisionType(fsDetail.get("alloc_type").getAsString());
                fsAttr.setDevice(fsDetail.get("storage_name").getAsString());
                fsAttr.setStoragePoolName(fsDetail.get("storage_pool_name").getAsString());
                fsAttr.setController(fsDetail.get("owning_controller").getAsString());
                //查询详情，获取tuning信息
                JsonObject tuning = fsDetail.getAsJsonObject("tuning");
                if (!tuning.isJsonNull()) {
                    fsAttr.setApplicationScenario(ToolUtils.jsonToStr(tuning.get("application_scenario"), null));
                    fsAttr.setDataDeduplication(ToolUtils.jsonToBoo(tuning.get("deduplication_enabled"), null));
                    fsAttr.setDateCompression(ToolUtils.jsonToBoo(tuning.get("compression_enabled"), null));
                }
                list.add(fsAttr);
            }
        }

        if (list.size() > 0) {
            return list;
        }

        return null;
    }

    @Override
    public boolean scanNfs() throws DMEException {
        // vcenter侧获取nfsStrorge信息列表 (提取shareIp,sharePath 与dem(ip)发生关联关系)
        // DEM 获取所有存储设备信息 通过ip过滤提取存储ID storageId
        // DME logicPort, 通过存储IDstroage id查询逻辑端口 (需求文档说明通过share ip,但API不支持)
        // DME share, 通过sharePath(可加storageId)查询share 提取fs_name
        // DME FileService, 通过fs_name和storageId查询fs (需求文档说明是通过share path, 但API不支持)

        String storeType = ToolUtils.STORE_TYPE_NFS;
        String listStr = vcsdkUtils.getAllVmfsDataStoreInfos(storeType);
        if (StringUtils.isEmpty(listStr)) {
            return false;
        }

        //将DME的存储设备集合转换为map key:ip value:Storage
        List<Storage> storages = dmeStorageService.getStorages();
        Map<String, Storage> storageMap = converStorage(storages);

        JsonArray jsonArray = new JsonParser().parse(listStr).getAsJsonArray();
        List<DmeVmwareRelation> relationList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject nfsDatastore = jsonArray.get(i).getAsJsonObject();

            //TODO 从vCenter nfsDataStore信息中提取存储id ip和path
            String nfsStorageId = ToolUtils.jsonToStr(nfsDatastore.get("objectid"));
            String nfsDatastoreIp = ToolUtils.jsonToStr(nfsDatastore.get("remoteHost"));
            String nfsDataStoreSharePath = ToolUtils.jsonToStr(nfsDatastore.get("remotePath"));
            String nfsDataStorageName = ToolUtils.jsonToStr(nfsDatastore.get("name"));
           /* Storage storageInfo = storageMap.get(nfsDatastoreIp);
            if (null == storageInfo) {
                LOG.warn("扫描NFS存储信息,share ip:{} 再DME侧没有找到对应的存储设备!!!", nfsDatastoreIp);
                continue;
                //storageInfo = storageMap.get("10.143.133.201");
            }*/
            if (null != storageMap && storageMap.size() > 0) {
                for (Map.Entry<String, Storage> entry : storageMap.entrySet()) {
                    Storage storageInfo = entry.getValue();
                    String storageId = storageInfo.getId();
                    DmeVmwareRelation relation = new DmeVmwareRelation();
                    relation.setStoreId(nfsStorageId);
                    relation.setStorageDeviceId(storageId);
                    relation.setStoreName(nfsDataStorageName);
                    relation.setStoreType(storeType);

                    //获取logicPort信息
                    boolean withLogicPort = false;
                    List<Map<String, Object>> logicPortInfos = queryLogicPortInfo(storageId);
                    if (null != logicPortInfos && logicPortInfos.size() > 0) {
                        for (Map<String, Object> logicPortInfo : logicPortInfos) {
                            String id = ToolUtils.getStr(logicPortInfo.get("id"));
                            String name = ToolUtils.getStr(logicPortInfo.get("home_port_name"));
                            String mgmtIp = ToolUtils.getStr(logicPortInfo.get("mgmt_ip"));
                            if (nfsDatastoreIp.equals(mgmtIp)) {
                                relation.setLogicPortId(id);
                                relation.setLogicPortName(name);
                                withLogicPort = true;
                                break;
                                //此处若匹配到了多个IP,属于异常场景,处理方法见需求说明文档
                            }
                        }
                    } else {
                        LOG.warn("NFSDATASTORE id:" + storageId + " contains logicport is null!");
                    }

                    //获取share信息 (条件:sharePath  可加 storageId)
                    String fsName = "";
                    boolean withShare = false;
                    Map<String, Object> shareInfo = queryShareInfo(nfsDataStoreSharePath);
                    if (null != shareInfo && shareInfo.size() > 0) {
                        fsName = ToolUtils.getStr(shareInfo.get("fs_name"));
                        String id = ToolUtils.getStr(shareInfo.get("id"));
                        String name = ToolUtils.getStr(shareInfo.get("name"));
                        relation.setShareId(id);
                        relation.setShareName(name);
                        withShare = true;
                    } else {
                        LOG.warn("NFSDATASTORE id:" + storageId + " contains share is null!");
                    }

                    //获取fs信息
                    boolean withFs = false;
                    if (!StringUtils.isEmpty(fsName)) {
                        Map<String, Object> fsInfo = queryFsInfo(storageId, fsName);
                        if (null != fsInfo && fsInfo.size() > 0) {
                            String id = ToolUtils.getStr(fsInfo.get("id"));
                            String name = ToolUtils.getStr(fsInfo.get("name"));
                            relation.setFsId(id);
                            relation.setFsName(name);
                            withFs = true;
                        } else {
                            LOG.warn("NFSDATASTORE id:" + storageId + " contains fs is null!");
                        }
                    }

                    if (withFs || withShare || withLogicPort) {
                        relationList.add(relation);
                    }
                }
            }
        }

        //数据库保存datastorage 与nfs的 share fs logicPort关系信息
        if (relationList.size() > 0) {
            return dmeVmWareRelationDbProcess(relationList, storeType);
        }
        return false;
    }

    private boolean dmeVmWareRelationDbProcess(List<DmeVmwareRelation> relationList, String storeType) throws DmeSqlException {
        //本地全量查询NFS
        List<String> storageIds = dmeVmwareRalationDao.getAllStorageIdByType(storeType);
        List<DmeVmwareRelation> newList = new ArrayList<>();
        List<DmeVmwareRelation> upList = new ArrayList<>();
        for (DmeVmwareRelation relation : relationList) {
            String storegeId = relation.getStoreId();
            if (storageIds.contains(storegeId)) {
                upList.add(relation);
                storageIds.remove(storegeId);
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
    private Map<String, Object> queryShareInfo(String sharePath) throws DMEException {
        //ResponseEntity responseEntity = listShareByStorageId(storageId);
        ResponseEntity responseEntity = listShareBySharePath(sharePath);
        if (responseEntity.getStatusCodeValue() / 100 == 2) {
            Object object = responseEntity.getBody();
            List<Map<String, Object>> list = converShare(object);
            if (list.size() > 0) {
                return list.get(0);
            }
        }
        return null;
    }

    //通过share_path查询share信息
    private ResponseEntity listShareBySharePath(String sharePath) throws DMEException {
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
            shareMap.put("id", ToolUtils.jsonToStr(jsonObject.get("id")));
            shareMap.put("name", ToolUtils.jsonToStr(jsonObject.get("name")));
            shareMap.put("share_path", ToolUtils.jsonToStr(jsonObject.get("share_path")));
            shareMap.put("storage_id", ToolUtils.jsonToStr(jsonObject.get("storage_id")));
            shareMap.put("device_name", ToolUtils.jsonToStr(jsonObject.get("device_name")));
            shareMap.put("owning_dtree_id", ToolUtils.jsonToStr(jsonObject.get("owning_dtree_id")));
            shareMap.put("owning_dtree_name", ToolUtils.jsonToStr(jsonObject.get("owning_dtree_name")));
            shareMap.put("fs_name", ToolUtils.jsonToStr(jsonObject.get("fs_name")));
            shareList.add(shareMap);
        }
        return shareList;
    }

    //DME按条件获取fs 暂只考虑一对一关系 存储设备下多个FS只取第一个
    private Map<String, Object> queryFsInfo(String storageId, String fsName) throws DMEException {
        ResponseEntity responseEntity = listFsByStorageId(storageId, fsName);
        if (responseEntity.getStatusCodeValue() / 100 == 2) {
            Object object = responseEntity.getBody();
            List<Map<String, Object>> list = converFs(object);
            if (list.size() > 0) {
                return list.get(0);
            }
        }
        return null;
    }

    //DME通过storageId获取fs信息
    private ResponseEntity listFsByStorageId(String storageId, String fsName) throws DMEException {
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
            fsMap.put("id", ToolUtils.jsonToStr(jsonObject.get("id")));
            fsMap.put("name", ToolUtils.jsonToStr(jsonObject.get("name")));
            fsMap.put("storage_id", ToolUtils.jsonToStr(jsonObject.get("storage_id")));
            fsMap.put("storage_name", ToolUtils.jsonToStr(jsonObject.get("storage_name")));
            fsMap.put("storage_pool_name", ToolUtils.jsonToStr(jsonObject.get("storage_pool_name")));
            fsMap.put("tier_id", ToolUtils.jsonToStr(jsonObject.get("tier_id")));
            fsMap.put("tier_name", ToolUtils.jsonToStr(jsonObject.get("tier_name")));
            fsList.add(fsMap);
        }
        return fsList;
    }

    //按条件查询logicPort
    private List<Map<String, Object>> queryLogicPortInfo(String storageId) throws DMEException {
        ResponseEntity responseEntity = listLogicPortByStorageId(storageId);
        if (responseEntity.getStatusCodeValue() / 100 == 2) {
            Object object = responseEntity.getBody();
            List<Map<String, Object>> list = convertLogicPort(storageId, object);
            if (list.size() > 0) {
                return list;
            }
        }
        return null;
    }

    //通过storageId查询logicPort信息
    private ResponseEntity listLogicPortByStorageId(String storageId) throws DMEException {
        String url = DmeConstants.API_LOGICPORTS_LIST + storageId;
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
            logicPortMap.put("id", ToolUtils.jsonToStr(jsonObject.get("id")));
            logicPortMap.put("name", ToolUtils.jsonToStr(jsonObject.get("name")));
            logicPortMap.put("storage_id", storageId);
            logicPortMap.put("home_port_id", ToolUtils.jsonToStr(jsonObject.get("home_port_id")));
            logicPortMap.put("home_port_name", ToolUtils.jsonToStr(jsonObject.get("home_port_name")));
            logicPortMap.put("current_port_id", ToolUtils.jsonToStr(jsonObject.get("current_port_id")));
            logicPortMap.put("current_port_id", ToolUtils.jsonToStr(jsonObject.get("current_port_id")));
            logicPortMap.put("mgmt_ip", ToolUtils.jsonToStr(jsonObject.get("mgmt_ip")));
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
    public List<NfsDataInfo> listNfs() throws DMEException {
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
                                    //nfsDataInfo.setSharePath(dvr.getVolumeShare());
                                    nfsDataInfo.setSharePath(dvr.getShareName());
                                    nfsDataInfo.setLogicPort(dvr.getLogicPortName());
                                    nfsDataInfo.setLogicPortId(dvr.getLogicPortId());
                                    nfsDataInfo.setShare(dvr.getShareName());
                                    nfsDataInfo.setShareId(dvr.getShareId());
                                    nfsDataInfo.setFs(dvr.getFsName());
                                    nfsDataInfo.setFsId(dvr.getFsId());
                                    nfsDataInfo.setObjectid(ToolUtils.jsonToStr(jo.get("objectid")));

                                    String fsUrl = "";
                                    try {
                                        fsUrl = DmeConstants.DME_NFS_FILESERVICE_DETAIL_URL.replace("{file_system_id}", dvr.getFsId());
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
            throw new DMEException(e.getMessage());
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
    public List<NfsDataInfo> listNfsPerformance(List<String> fsIds) throws DMEException {
        List<NfsDataInfo> relists = null;
        try {
            if (fsIds != null && fsIds.size() > 0) {
                Map<String, Object> params = new HashMap<>();
                params.put("obj_ids", fsIds);
                Map<String, Object> remap = dataStoreStatisticHistoryService.queryNfsStatisticCurrent(params);
                LOG.info("remap===" + gson.toJson(remap));
                if (remap != null && remap.size() > 0) {
                    JsonObject dataJson = new JsonParser().parse(remap.toString()).getAsJsonObject();
                    if (dataJson != null) {
                        relists = new ArrayList<>();
                        for (String fsId : fsIds) {
                            JsonObject statisticObject = dataJson.getAsJsonObject(fsId);
                            if (statisticObject != null) {
                                NfsDataInfo nfsDataInfo = new NfsDataInfo();
                                nfsDataInfo.setFsId(fsId);
                                nfsDataInfo.setOps(ToolUtils.jsonToFloat(ToolUtils.getStatistcValue(statisticObject, DmeIndicatorConstants.COUNTER_ID_FS_THROUGHPUT, "max"), null));
                                nfsDataInfo.setBandwidth(ToolUtils.jsonToFloat(ToolUtils.getStatistcValue(statisticObject, DmeIndicatorConstants.COUNTER_ID_FS_BANDWIDTH, "max"), null));
                                nfsDataInfo.setReadResponseTime(ToolUtils.jsonToFloat(ToolUtils.getStatistcValue(statisticObject, DmeIndicatorConstants.COUNTER_ID_FS_READRESPONSETIME, "max"), null));
                                nfsDataInfo.setWriteResponseTime(ToolUtils.jsonToFloat(ToolUtils.getStatistcValue(statisticObject, DmeIndicatorConstants.COUNTER_ID_FS_WRITERESPONSETIME, "max"), null));
                                relists.add(nfsDataInfo);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("list nfs performance error:", e);
            throw new DMEException(e.getMessage());
        }
        //LOG.info("listNfsPerformance relists===" + (relists == null ? "null" : (relists.size() + "==" + gson.toJson(relists))));
        return relists;
    }

    /**
     * Mount nfs,params中包含了 include:
     * dataStoreObjectId: datastore的object id
     * logicPortIp:存储逻辑端口IP  暂时不需要
     * hostObjectId:主机objectid
     * hostVkernelIp:主机vkernelip
     * str mountType: 挂载模式（只读或读写）  readOnly/readWrite
     *
     * @param params: include dataStoreObjectId,hosts,mountType
     * @return: ResponseBodyBean
     */
    @Override
    public void mountNfs(Map<String, Object> params) throws DMEException {
        if (params != null && null != params.get("dataStoreObjectId")) {
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
                            //List<Map<String, String>> clusters = null;
                            //List<Map<String, String>> hosts = null;
                            String hostObjectId = null;
                            String logicPortIp = null;
                            //String mountType = null;
                            if (params.get("hostObjectId") != null) {
                                hostObjectId = ToolUtils.getStr(params.get("hostObjectId"));
                                logicPortIp= ToolUtils.getStr( params.get("hostVkernelIp"));
                            }
                        /*if (params.get("clusters") != null) {
                            clusters = (List<Map<String, String>>) params.get("clusters");
                        }*/
                            vcsdkUtils.mountNfs(dataStoreObjectId,
                                    hostObjectId, logicPortIp, ToolUtils.getStr(params.get("mountType")));

                        } else {
                            throw new DMEException("DME mount nfs error(task status)!");
                        }
                    } else {
                        throw new DMEException("DME mount nfs error(task is null)!");
                    }
                }
            } else {
                throw new DMEException("DME dataStore ObjectId is null!");
            }

        } else {
            throw new DMEException("Mount nfs parameter exception:" + params);
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
            if (params.get("hostVkernelIp") == null) {
                LOG.info("mount nfs To Host fail: vkernelIp is null");
                throw new Exception("mount nfs To Host fail: vkernelIp is null");
            }

            //修改dme中的share

            Map<String, Object> requestbody = new HashMap<>();
            requestbody.put("id", ToolUtils.getStr(params.get("shareId")));

            List<Map<String, Object>> listAddition = new ArrayList<>();
            String vkernelIp = ToolUtils.getStr(params.get("hostVkernelIp"));

            Map<String, Object> addition = new HashMap<>();
            addition.put("name", vkernelIp);
            String accessval = ("readOnly".equalsIgnoreCase(ToolUtils.getStr(params.get("mountType")))) ? "read-only" : "read/write";
            addition.put("accessval", accessval);
            addition.put("all_squash", "no_all_squash");
            addition.put("root_squash", "root_squash");
            addition.put("sync", "synchronization");
            addition.put("secure", "insecure");
            listAddition.add(addition);

            requestbody.put("nfs_share_client_addition", listAddition);

            LOG.info("mountnfs requestbody==" + gson.toJson(requestbody));

            String url = DmeConstants.DME_NFS_SHARE_DETAIL_URL.replace("{nfs_share_id}", ToolUtils.getStr(params.get("shareId")));
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

        } catch (Exception e) {
            LOG.error("mountnfs error:", e);
        }
        return taskId;
    }

    //卸载NFS
    @Override
    public void unmountNfs(Map<String, Object> params) throws DMEException {
        // 过滤vm注册的主机和集群,此步骤暂未处理
        if (null != params) {
            String dataStoreObjectId = ToolUtils.getStr(params.get("dataStoreObjectId"));
            String hostObjId = ToolUtils.getStr(params.get("hostId"));
            String clusterObjId = ToolUtils.getStr(params.get("clusterId"));
            //查询Datastore关联主机、集群、VM，过滤掉VM注册的主机和集群
            vcsdkUtils.hasVmOnDatastore(dataStoreObjectId);
            // 1获取dme的share 2 获取share下的客户端访问列表
            DmeVmwareRelation dvr = dmeVmwareRalationDao.getDmeVmwareRelationByDsId(dataStoreObjectId);
            if (null != dvr) {
                String dsName = dvr.getStoreName();
                if (!StringUtils.isEmpty(dsName)) {
                    Map<String, Object> dsmap = new HashMap<>();
                    dsmap.put("name", dsName);
                    //vcenter侧主机 集群二选一
                    if (!StringUtils.isEmpty(hostObjId)) {
                        unmountNfsFromHost(dataStoreObjectId, hostObjId);
                    } else if (!StringUtils.isEmpty(clusterObjId)) {
                        //unmountNfsFromCluster(dataStoreObjectId, clusterObjId);
                    } else {
                        //throw new Exception("unmount nfs parameter exception,No host or cluster is specified :" + params);
                    }
                    // vcenter refresh一下?
                }
            }

            //***dme侧卸载
            if (null != dvr) {
                String shareId = dvr.getShareId();
                List<AuthClient> authClientList = getNfsDatastoreShareAuthClients(shareId);
                if (null != authClientList && authClientList.size() > 0) {
                    //卸载
                    Map<String, String> authIdIpMap = new HashMap<>();
                    for (AuthClient authClient : authClientList) {
                        String authId = authClient.getId();
                        String ip = authClient.getName();
                        if (!StringUtils.isEmpty(ip) && !StringUtils.isEmpty(authId)) {
                            authIdIpMap.put(authId, ip);
                        }
                    }
                    if (authIdIpMap.size() > 0) {
                        String taskId = deleteAuthClient(shareId, authIdIpMap);
                        if (!StringUtils.isEmpty(taskId)) {
                            List<String> taskIds = new ArrayList<>();
                            taskIds.add(taskId);
                            LOG.info("taskIds====" + taskIds);
                            //查询看删除任务是否完成。
                            boolean unmountFlag = taskService.checkTaskStatus(taskIds);
                            if (unmountFlag) { //DME NFS SHAER 删除authClient完成
                                LOG.info("unmountNfs delete authClient success!");
                            } else {
                                throw new DMEException("DME mount nfs error(task status)!");
                            }
                        } else {
                            throw new DMEException("DME mount nfs error(task is null)!");
                        }
                    } else {
                        LOG.info("unmount nfs dme fs authClient is null!!!");
                    }

                }
            }
        } else {
            throw new DMEException("unmount nfs parameter exception:" + params);
        }
    }

    @Override
    public void deleteNfs(Map<String, Object> params) throws DMEException {
        if (null != params) {
            String dataStorageId = ToolUtils.getStr(params.get("dataStoreObjectId"));
            List<Map<String, Object>> hosts = getHostsMountDataStoreByDsObjectId(dataStorageId);
            List<String> hostIds = new ArrayList<>();
            if (null != hosts && hosts.size() > 0) {
                for (Map<String, Object> hostMap : hosts) {
                    String hostId = ToolUtils.getStr(hostMap.get("hostId"));
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
                if (!StringUtils.isEmpty(shareId)) {
                    String deleteShareTaskId = deleteNfsShare(Arrays.asList(shareId));
                    if (!StringUtils.isEmpty(deleteShareTaskId)) {
                        taskIds.add(deleteShareTaskId);
                    }
                }
                if (!StringUtils.isEmpty(fsId)) {
                    String deletefsTaskId = deleteNfsFs(Arrays.asList(fsId));
                    if (!StringUtils.isEmpty(deletefsTaskId)) {
                        taskIds.add(deletefsTaskId);
                    }
                }
                if (taskIds.size() > 0) {
                    boolean deleteFlag = taskService.checkTaskStatus(taskIds);
                    if (deleteFlag) { //DME NFS 删除 SHAER FS
                        LOG.info("Nfs delete share fs success!");
                    } else {
                        throw new DMEException("DME delete nfs error(task status)!");
                    }
                }
            }
        }
    }

    private void unmountNfsFromHost(String dataStoreObjectId, String hostId) throws VcenterException {
        vcsdkUtils.unmountNfsOnHost(dataStoreObjectId, hostId);
        //vcsdkUtils.unmountVmfsOnHostOrCluster(dataStoreObjectId,null,hostId);
    }

    private void unmountNfsFromCluster(String dataStoreObjectId, String clusterId) throws VcenterException {
        vcsdkUtils.unmountNfsOnCluster(dataStoreObjectId, clusterId);
    }

    //dem卸载share访问客户端列表
    private String deleteAuthClient(String shareId, Map<String, String> authClientIdIpMap) {
        String taskId = "";
        Map<String, Object> requestbody = new HashMap<>();
        List<Map<String, Object>> listAddition = new ArrayList<>();
        for (Map.Entry<String, String> authClient : authClientIdIpMap.entrySet()) {
            Map<String, Object> addtion = new HashMap<>();
            addtion.put("nfs_share_client_id_in_storage", authClient.getKey());
            addtion.put("name", authClient.getValue());
            listAddition.add(addtion);
        }
        requestbody.put("id", shareId);
        requestbody.put("nfs_share_client_deletion", listAddition);
        LOG.info("unmountnfs authClient requestbody==" + gson.toJson(requestbody));
        try {
            String url = DmeConstants.DME_NFS_SHARE_DETAIL_URL.replace("{nfs_share_id}", shareId);
            ResponseEntity responseEntity = dmeAccessService.access(url, HttpMethod.PUT, gson.toJson(requestbody));

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

    @Override
    public List<Map<String, Object>> getHostsMountDataStoreByDsObjectId(String dataStoreObjectId) throws DMEException {
        List<Map<String, Object>> lists = null;
        try {
            //取得vcenter中的所有挂载了当前存储的host
            String listStr = vcsdkUtils.getHostsByDsObjectId(dataStoreObjectId, true);
            LOG.info("host getHostsMountDataStoreByDsObjectId==" + listStr);
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() {
                }.getType());
            }
        } catch (Exception e) {
            LOG.error("get Hosts MountDataStore By DsObjectId error:", e);
            throw new DMEException(e.getMessage());
        }
        LOG.info("getHostsMountDataStoreByDsObjectId===" + (lists == null ? "null" : (lists.size() + "==" + gson.toJson(lists))));
        return lists;
    }

    @Override
    public List<Map<String, Object>> getClusterMountDataStoreByDsObjectId(String dataStoreObjectId) throws DMEException {
        List<Map<String, Object>> lists = null;
        try {
            //取得vcenter中的所有挂载了当前存储的host
            String listStr = vcsdkUtils.getClustersByDsObjectId(dataStoreObjectId);
            LOG.info("host getClustersMountDataStoreByDsObjectId==" + listStr);
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() {
                }.getType());
            }
        } catch (Exception e) {
            LOG.error("get Clusters MountDataStore By DsObjectId error:", e);
            throw new DMEException(e.getMessage());
        }
        LOG.info("getClustersMountDataStoreByDsObjectId===" + (lists == null ? "null" : (lists.size() + "==" + gson.toJson(lists))));
        return lists;
    }


    public boolean isNfs(String objectId) throws Exception {
        List<DmeVmwareRelation> dvrlist = dmeVmwareRalationDao.getDmeVmwareRelation(ToolUtils.STORE_TYPE_NFS);
        for (DmeVmwareRelation dmeVmwareRelation : dvrlist) {
            if (dmeVmwareRelation.getStoreId().equalsIgnoreCase(objectId)) {
                return true;
            }

        }
        return false;
    }
}
