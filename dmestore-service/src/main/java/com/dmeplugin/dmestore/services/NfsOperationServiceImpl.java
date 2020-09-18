package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.mvc.VmfsOperationController;
import com.dmeplugin.dmestore.utils.ToolUtils;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author lianq
 * @className NfsOperationServiceImpl
 * @description TODO
 * @date 2020/9/16 16:25
 */
public class NfsOperationServiceImpl implements NfsOperationService {

    public static final Logger LOG = LoggerFactory.getLogger(VmfsOperationController.class);

    @Autowired
    private DmeAccessService dmeAccessServiceImpl;

    private Gson gson = new Gson();

    @Autowired
    private VCSDKUtils vcsdkUtils;

    @Override
    public Map<String, Object> createNfsDatastore(Map<String, String> params) {
        /**
         * params (String serverHost, Integer logicPort, String exportPath, String nfsName ,String accessMode,String type,String mountHost,String storage_id)
         */

        /**
         *   存储池  pool_id/storage_id  拿指定存储设备的存储池的 free_capacity？data_space
         *   逻辑端口  storage_id 返回逻辑端口列表
         *             指定逻辑端口  current_port_id  查询指定的逻辑端口详情
         */

        /**
         * 创建share
         *          POST /rest/fileservice/v1/nfs-shares
         *          share_path = exportPath
         *          默认 nfsName = shareName
         *          fs_id  DmeVmwareRalationDao /getFsIdByStorageId  根据存储id 获取fs_id
         *          nfs_share_client_addition NFS共享客户端
         *                  accessval 权限  = accessMode
         *                  all_squash  权限限制 （all_squash，no_all_squash）
         *                  name 客户端IP或主机名或网络组名 =  mountHost
         *                  root_squash  root权限限制：root_squash，no_root_squash
         *                  sync  写入模式：synchronization：同步， asynchronization：异步
         */

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "create nfs datastore success !");

        if (params == null || params.size() == 0) {
            resMap.put("code", 403);
            resMap.put("msg", "params error , please check your params !");
            return resMap;
        }
        String serverHost = params.get("serverHost");
        String port = params.get("logicPort");
        String exportPath = params.get("exportPath");
        String nfsName = params.get("nfsName");
        String accessMode = params.get("accessMode");
        String mountHost = params.get("mountHost");
        String type = params.get("type");
        if (StringUtils.isEmpty(serverHost)||StringUtils.isEmpty(port)||StringUtils.isEmpty(exportPath)
                ||StringUtils.isEmpty(nfsName)||StringUtils.isEmpty(accessMode)||StringUtils.isEmpty(mountHost)) {
            resMap.put("code", 403);
            resMap.put("msg", "params error , please check your params !");
            return resMap;
        }
        //VCSDKUtils vcsdkUtils = new VCSDKUtils();
        Integer logicPort = Integer.valueOf(port);
        String result = vcsdkUtils.createNfsDatastore(serverHost, logicPort, exportPath, nfsName, accessMode, mountHost, type);
        if ("failed".equals(result)) {
            resMap.put("code", 403);
            resMap.put("msg", "create nfs datastore error!");
            return resMap;
        }
        return resMap;
    }

    //create file system
    //todo 挂载一个  挂载多个时候需要修改参数列表
    private Map<String, Object> createFileSystem(Map<String, Object> params, String storage_pool_id) throws Exception {
        /**
         * 创建fs 参数结构：
         *      {
         *          {
         *              "filesystem_specs" : [ {
         *                      "name" : "FileSystem001",= nfsName
         *                      "capacity" : 2.0,
         *                      "count" : 20
         *               } ],
         *               "pool_raw_id":"String",
         *               "storage_id":"String",
         *               "tuning" : {
         *                  deduplication_enabled" : true,
         *                  compression_enabled" : false,
         *                  application_scenario" : "user_defined",
         *                  qos_policy" : {
         *                      "max_bandwidth" : 100,
         *                      "max_iops" : 100,
         *                   }
         *                },
         *                "create_nfs_share_param" : {
         *                      "share_path" : "/system/dtree" = exportPath
         *                      "nfs_share_client_addition" : [ {
         *                          "name" : "192.168.0.1",
         *                          "accessval" : "read-only",
         *                          "sync" : "synchronization",
         *                          "all_squash" : "all_squash",
         *                          "root_squash" : "root_squash",
         *                       } ]
         *                   }
         *                }
         */
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 202);
        resMap.put("msg", "create file system success !");

        String url = "/rest/fileservice/v1/filesystems/customize";

        if (params == null || params.size() == 0) {
            LOG.error("url:{" + url + "},param error,please check it!");
            resMap.put("code", 403);
            resMap.put("msg", "create file system error !");
            return resMap;
        }
        String storage_id = params.get("storage_id").toString();
        String filesystemSpecs = params.get("filesystem_specs").toString();
        List<String> list = Arrays.asList(filesystemSpecs);
        Map<String, Object> filesystem_specs = new HashMap<>();
        Double capacity = null;
        if (!StringUtils.isEmpty(storage_id) || !StringUtils.isEmpty(storage_pool_id)) {
            List<Map<String, Object>> filesystemSpecsList = new ArrayList<>();
            for (String filesystem_specss : list) {
                Map<String, Object> map = gson.fromJson(filesystem_specss, Map.class);
                Object objCapacity = map.get("capacity");
                if (objCapacity != null) {
                    capacity = Double.valueOf(objCapacity.toString());
                    Map<String, Object> resBody = getDataspaceOfStoragepool(storage_id, storage_pool_id);
                    if (ToolUtils.getInt(resBody.get("code")) == 200) {
                        Double data_space = Double.valueOf(resBody.get("data_space").toString());
                        if (Double.compare(capacity, data_space) > 1) {
                            capacity = data_space;
                            filesystem_specs.put("capacity", capacity);
                            filesystem_specs.put("name", map.get("name"));
                            filesystem_specs.put("count", map.get("count"));
                        }
                    }
                }
                filesystemSpecsList.add(filesystem_specs);
            }
            params.put("filesystem_specs", filesystemSpecsList);
        }
        ResponseEntity<String> responseEntity = dmeAccessServiceImpl.access(url, HttpMethod.POST, gson.toJson(params));
        int code = responseEntity.getStatusCodeValue();
        if (code != 202) {
            resMap.put("code", code);
            resMap.put("msg", "create file system error !");
        }
        String object = responseEntity.getBody();
        JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
        String task_id = jsonObject.get("task_id").getAsString();
        resMap.put("data", task_id);
        return resMap;
    }

    //get oriented storage pool data_space
    private Map<String,Object> getDataspaceOfStoragepool(String pool_id,String storage_id) throws Exception {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "get oriented data space of storage pool success!");

        if (StringUtils.isEmpty(pool_id) || StringUtils.isEmpty(storage_id)) {
            resMap.put("code", 403);
            resMap.put("msg", "params error ,please check your params!");
            return resMap;
        }
        String url = "/rest/storagemgmt/v1/storagepools/query";
        Map<String, String> reqBody = new HashMap<>();
        reqBody.put("storage_id",storage_id);
        reqBody.put("pool_id", pool_id);

        ResponseEntity<String> responseEntity = dmeAccessServiceImpl.access(url, HttpMethod.POST, gson.toJson(reqBody));
        LOG.info("url:{" + url + "},responseEntity:" + responseEntity);
        int code = responseEntity.getStatusCodeValue();
        if (code != 200) {
            resMap.put("code", code);
            resMap.put("msg", "get oriented data space of storage pool error!");
            return resMap;
        }
        String object = responseEntity.getBody();
        JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
        JsonArray jsonArray = jsonObject.get("datas").getAsJsonArray();
        Double data_space = null;
        for (JsonElement jsonElement : jsonArray) {
            JsonObject element = jsonElement.getAsJsonObject();
            data_space = Double.valueOf(element.get("data_space").getAsString());
        }
        if (data_space != null) {
            resMap.put("data_space", data_space);
        }else {
            LOG.error("get oriented data space of storage pool error");
        }
        return resMap;
    }
}
