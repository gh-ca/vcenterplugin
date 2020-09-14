package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.dao.DmeVmwareRalationDao;
import com.dmeplugin.dmestore.entity.DmeVmwareRelation;
import com.dmeplugin.dmestore.model.*;
import com.dmeplugin.dmestore.utils.StringUtil;
import com.dmeplugin.dmestore.utils.ToolUtils;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DmeNFSAccessServiceImpl
 * @Description TODO
 * @Author wangxiangyong
 * @Date 2020/9/4 10:33
 * @Version V1.0
 **/
public class DmeNFSAccessServiceImpl implements DmeNFSAccessService {
    private static final Logger LOG = LoggerFactory.getLogger(DmeNFSAccessServiceImpl.class);

    @Autowired
    private Gson gson;

    @Autowired
    private DmeAccessService dmeAccessService;
    @Autowired
    private DmeStorageService dmeStorageService;
    @Autowired
    private DmeVmwareRalationDao dmeVmwareRalationDao;


    private VCSDKUtils vcsdkUtils;

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
    }

    @Override
    public NFSDataStoreShareAttr getNFSDatastoreShareAttr(String nfs_share_id) throws Exception {
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
        url = StringUtil.stringFormat(DmeConstants.DEFAULT_PATTERN, DmeConstants.DME_NFS_SHARE_AUTH_CLIENTS_URL,
                "nfs_share_id", nfs_share_id);
        responseEntity = dmeAccessService.access(url, HttpMethod.POST, null);
        if (responseEntity.getStatusCodeValue() / 100 == 2) {
            resBody = responseEntity.getBody();
            JsonObject resObject = gson.fromJson(resBody, JsonObject.class);
            int total = resObject.get("total").getAsInt();
            List<AuthClient> clientList = new ArrayList<>();
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
                shareAttr.setAuth_client_list(clientList);
            }
        }

        return shareAttr;
    }

    @Override
    public NFSDataStoreLogicPortAttr getNFSDatastoreLogicPortAttr(String logic_port_id) throws Exception {
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
        String url = DmeConstants.DME_NFS_FILESERVICE_QUERY_URL;
        JsonObject queryParam = new JsonObject();
        int page_no = 0;
        int page_size = 100;
        queryParam.addProperty("storage_id", storage_id);
        queryParam.addProperty("page_size", page_size);
        boolean loopFlag = true;
        JsonArray totalArray = new JsonArray();
        while (loopFlag) {
            queryParam.addProperty("page_no", page_no++);
            try {
                ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.POST, queryParam.toString());
                if (responseEntity.getStatusCodeValue() / 100 == 2) {
                    String resBody = responseEntity.getBody();
                    JsonObject response = gson.fromJson(resBody, JsonObject.class);
                    JsonArray array = response.getAsJsonArray("data");
                    totalArray.addAll(array);

                    if (array.size() != page_size) {
                        loopFlag = false;
                    }
                }
            } catch (Exception ex) {
                loopFlag = false;
            }
        }

        List<NFSDataStoreFSAttr> list = new ArrayList<>();
        for (int i = 0; i < totalArray.size(); i++) {
            JsonObject object = totalArray.get(i).getAsJsonObject();
            NFSDataStoreFSAttr fsAttr = new NFSDataStoreFSAttr();
            fsAttr.setName(object.get("name").getAsString());
            fsAttr.setProvisionType(object.get("alloc_type").getAsString());
            fsAttr.setDevice(object.get("storage_name").getAsString());
            fsAttr.setStoragePoolName(object.get("storage_pool_name").getAsString());
            //TODO 控制器暂未找到对应的值。
            fsAttr.setController("--");
            //查询详情，获取tuning信息
            String file_system_id = object.get("id").getAsString();
            url = StringUtil.stringFormat(DmeConstants.DEFAULT_PATTERN, DmeConstants.DME_NFS_FILESERVICE_DETAIL_URL,
                    "file_system_id", file_system_id);
            ResponseEntity<String> responseTuning = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseTuning.getStatusCodeValue() / 100 == 2) {
                JsonObject fsDetail = gson.fromJson(responseTuning.getBody(), JsonObject.class);
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
        String listStr = vcsdkUtils.getAllVmfsDataStores(store_type);
        LOG.info("===list nfs datastore success====\n{}", listStr);
        if (StringUtils.isEmpty(listStr)) {
            return false;
        }
        Map<String, Object> storageOriginal = dmeStorageService.getStorages();
        if (null == storageOriginal || !storageOriginal.get("200").toString().equals("200")) {
            return false;
        }

        //将DME的存储设备集合转换为map key:ip value:Storage
        List<Storage> storages = (List<Storage>) storageOriginal.get("data");
        Map<String, Storage> storageMap = converStorage(storages);

        JsonArray jsonArray = new JsonParser().parse(listStr).getAsJsonArray();
        List<DmeVmwareRelation> relationList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject nfsDatastore = jsonArray.get(i).getAsJsonObject();

            //TODO 暂时认为是从url中获取到wwn信息
            String nfsDatastoreUrl = nfsDatastore.get("url").getAsString();
            String nfsDatastoreId = nfsDatastore.get("id").getAsString();
            String nfsDatastoreName = nfsDatastore.get("name").getAsString();
            String nfsDatastoreIp = "";//nfsDatastore.get("XXX").getAsString();
            String nfsDataStoreSharePath = "";//nfsDatastore.get("XXX").getAsString();
            //拆分wwn
            //String wwn = nfsDatastoreUrl.split("volumes/")[1];
            Storage storageInfo = storageMap.get(nfsDatastoreIp);
            if (null == storageInfo) {
                LOG.warn("扫描NFS存储信息,share ip:{} 再DME侧没有找到对应的存储设备!!!", nfsDatastoreIp);
                continue;
            }
            String storage_id = storageInfo.getId();
            String storage_name = storageInfo.getName();
            DmeVmwareRelation relation = new DmeVmwareRelation();
            relation.setStoreId(storage_id);
            relation.setStoreName(storage_name);
            relation.setStoreType(store_type);

            //获取logicPort信息
            Map<String, Object> logicPortInfo = queryLogicPortInfo(storage_id);
            if (null != logicPortInfo && logicPortInfo.size() > 0) {
                String id = ToolUtils.getStr(logicPortInfo.get("id"));
                String name = ToolUtils.getStr(logicPortInfo.get("name"));
                relation.setLogicPortId(id);
                relation.setLogicPortName(name);
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
            }

            //获取fs信息
            Map<String, Object> fsInfo = queryFsInfo(storage_id, fsName);
            if (null != fsInfo && fsInfo.size() > 0) {
                String id = ToolUtils.getStr(fsInfo.get("id"));
                String name = ToolUtils.getStr(fsInfo.get("name"));
                relation.setFsId(id);
                relation.setFsName(name);
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
        ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_NFS_SHARE_URL, HttpMethod.POST, requestbody.toString());
        return responseEntity;
    }

    //通过share_path查询share信息
    private ResponseEntity listShareBySharePath(String sharePath) throws Exception {
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("share_path", sharePath);
        ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_NFS_SHARE_URL, HttpMethod.POST, requestbody.toString());
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

    //按条件查询fs
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

    //通过storageId查询fs信息
    private ResponseEntity listFsByStorageId(String storageId, String fsName) throws Exception {
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("storage_id", storageId);
        if (!StringUtils.isEmpty(fsName)) {
            requestbody.put("name", fsName);
        }
        ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_NFS_FILESERVICE_QUERY_URL, HttpMethod.POST, requestbody.toString());
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

}
