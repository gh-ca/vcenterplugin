package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.AuthClient;
import com.dmeplugin.dmestore.model.NFSDataStoreFSAttr;
import com.dmeplugin.dmestore.model.NFSDataStoreLogicPortAttr;
import com.dmeplugin.dmestore.model.NFSDataStoreShareAttr;
import com.dmeplugin.dmestore.utils.StringUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

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
}
