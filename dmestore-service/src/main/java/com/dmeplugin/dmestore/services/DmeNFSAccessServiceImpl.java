package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.entity.DmeVmwareRelation;
import com.dmeplugin.dmestore.model.AuthClient;
import com.dmeplugin.dmestore.model.NFSDataStoreFSAttr;
import com.dmeplugin.dmestore.model.NFSDataStoreLogicPortAttr;
import com.dmeplugin.dmestore.model.NFSDataStoreShareAttr;
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
        // vcenter侧获取nfsStrorge信息列表 (暂通过wwn来与dem(storagetId)发生关联关系)
        String listStr = VCSDKUtils.getAllVmfsDataStores(ToolUtils.STORE_TYPE_NFS);
        LOG.info("===list nfs datastore success====\n{}", listStr);
        if (StringUtils.isEmpty(listStr)) {
            return false;
        }
        JsonArray jsonArray = new JsonParser().parse(listStr).getAsJsonArray();
        List<DmeVmwareRelation> relationList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject nfsDatastore = jsonArray.get(i).getAsJsonObject();
            String store_type = ToolUtils.STORE_TYPE_VMFS;
            //TODO 暂时认为是从url中获取到wwn信息
            String nfsDatastoreUrl = nfsDatastore.get("url").getAsString();
            String nfsDatastoreId = nfsDatastore.get("id").getAsString();
            String nfsDatastoreName = nfsDatastore.get("name").getAsString();
            //拆分wwn
            String wwn = nfsDatastoreUrl.split("volumes/")[1];

            //获取share信息
            Map<String, Object> shareInfo = queryShareInfo(wwn);
            if (null != shareInfo && shareInfo.size() > 0) {

            }

            //获取fs信息
            Map<String, Object> fsInfo = queryFsInfo(wwn);
            if (null != fsInfo && fsInfo.size() > 0) {

            }

            //获取logicPort信息
            Map<String, Object> logicPortInfo = queryLogicPortInfo(wwn);
            if (null != logicPortInfo && logicPortInfo.size() > 0) {

            }
        }

        //数据库保存datastorage 与nfs的 share fs logicPort关系信息
        //dbsave();


        return false;
    }

    //按条件查询share 暂认为 storage与share是一对一的关系
    private Map<String, Object> queryShareInfo(String storageId) throws Exception {
        ResponseEntity responseEntity = listShareByStorageId(storageId);
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
            shareMap.put("id", jsonObject.get("id").getAsString());
            shareMap.put("name", jsonObject.get("name").getAsString());
            shareMap.put("share_path", jsonObject.get("share_path").getAsString());
            shareMap.put("storage_id", jsonObject.get("storage_id").getAsString());
            shareMap.put("device_name", jsonObject.get("device_name").getAsString());
            shareMap.put("owning_dtree_id", jsonObject.get("owning_dtree_id").getAsString());
            shareMap.put("owning_dtree_name", jsonObject.get("owning_dtree_name").getAsString());
            shareList.add(shareMap);
        }
        return shareList;
    }

    //按条件查询fs
    private Map<String, Object> queryFsInfo(String storageId) throws Exception {
        ResponseEntity responseEntity = listFsByStorageId(storageId);
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
    private ResponseEntity listFsByStorageId(String storageId) throws Exception {
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("storage_id", storageId);
        ResponseEntity responseEntity = dmeAccessService.access(DmeConstants.DME_NFS_FILESERVICE_QUERY_URL, HttpMethod.POST, requestbody.toString());
        return responseEntity;
    }

    //解析fs信息
    private List<Map<String, Object>> converFs(Object object) {
        return null;
    }

    //按条件查询logicPort
    private Map<String, Object> queryLogicPortInfo(String storageId) throws Exception {
        ResponseEntity responseEntity = listLogicPortByStorageId(storageId);
        if (responseEntity.getStatusCodeValue() / 100 != 2) {
            Object object = responseEntity.getBody();
            List<Map<String, Object>> list = convertLogicPort(object);
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
    private List<Map<String, Object>> convertLogicPort(Object object) {
        return null;
    }

}
