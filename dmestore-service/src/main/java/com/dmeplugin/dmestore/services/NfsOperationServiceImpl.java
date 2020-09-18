package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.TaskDetailInfo;
import com.dmeplugin.dmestore.mvc.VmfsOperationController;
import com.dmeplugin.dmestore.utils.RestUtils;
import com.dmeplugin.dmestore.utils.ToolUtils;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private TaskService taskService;

    private Gson gson = new Gson();

    @Autowired
    private VCSDKUtils vcsdkUtils;

    /**
     * NFS 数据存储的准则和最佳做法包括以下项：
     *
     *     不能使用不同的 NFS 版本在不同主机上挂载同一数据存储。NFS 3 和 NFS 4.1 客户端是不兼容的，并且使用不同的锁定协议。因此，从两个不兼容的客户端访问同一虚拟磁盘可能导致不正确的行为，并导致数据损坏。
     *     NFS 3 和 NFS 4.1 数据存储可以在同一主机上共存。
     *     当在不同主机上挂载相同 NFS 3 卷时，确保各主机之间的服务器名称和文件夹名称相同。如果名称不匹配，则主机会将同一 NFS 版本 3 卷视为两个不同的数据存储。该错误可能导致诸如 vMotion 之类的功能运行失败。
     *     例如，如果在一台主机上输入 filer 作为服务器名称，而在另一台主机上输入 filer.domain.com 作为服务器名称，就出现了这种名称不匹配的情况。此准则不适用于 NFS 版本 4.1。
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> createNfsDatastore(Map<String, String> params) {

        /**
         * fs_param:
         * storage_id  str  存储设备id 必
         * pool_raw_id str  存储池在指定存储设备上的id 必
         * exportPath str 文件路径（和共享路径相同） 必
         * String nfsName 名称  必
         * String accessMode  读写权限 必
         * String type  NFS版本 （标准：NFS / NFS41） 必
         * filesystem_specs  array  文件系统规格属性[{
         *      capacity double 该规格文件系统容量，单位GB  必
         *      name str  文件系统名称 必
         *      count int 该规格文件系统数量  必
         *      description str   描述
         *      start_suffix int 该规格文件系统的起始后缀编号
         * }]
         * tuning属性 （高级属性设置）{
         *      deduplication_enabled  boolean 重复数据删除。默认关闭
         *      compression_enabled  boolean 数据压缩。默认关闭
         *      application_scenario str 应用场景。database： 数据库；VM：虚拟机；user_defined：自定义。默认自定义
         *      block_size int 文件系统块大小，单位KB
         *      allocation_type str 文件系统分配类型，取值范围 thin，thick。默认为thin
         *      }
         * qos_policy 属性{
         *      max_bandwidth int 最大带宽，在控制上限的时候有效,与minbandwidth,miniops互斥
         *      max_iops int 最大iops，在控制上限的时候有效,与minbandwidth,miniops互斥
         *      min_bandwidth  int 最小带宽，在保护下限的时候有效，与maxbandwidth,maxiops互斥
         *      min_iops  int 最小iops，在保护下限的时候有效, 与maxbandwidth,maxiops互斥
         *      latency int 时延，单位ms 仅保护下限支持该参数
         *      }
         * create_nfs_share_param   创建NFS共享参数 必{
         *      share_path str 共享路径 必
         *      description  str 描述
         *      name  str 共享别名
         *     character_encoding str 当前共享使用的字符编码
         *     audit_items array 支持审计的事件列表[ {
         *                 audititem str 支持审计的事件：none：无操作，all：所有操作，open：打开，create：创建，read：读，write：写，close：关闭，delete：删除，rename：重命名，get_security：获取安全属性，set_security：设置安全属性，get_attr：获取属性，set_attr：设置属性
         *               } ],
         *    }
         * nfs_share_client_addition  array  NFS共享客户端 [{
         *     name str   客户端IP或主机名或网络组名 必
         *     accessval str 权限：read-only：只读， read/write：读写 必
         *     sync str 写入模式：synchronization：同步， asynchronization：异步 必
         *     all_squash str 权限限制：all_squash，no_all_squash 必
         *     root_squash str root权限限制：root_squash，no_root_squash 必
         *     secure str  源端口校验限制：secure，insecure
         *     }]
         * nfs share param:
         * show_snapshot_enable boolean 是否开启显示Snapshot的功能
         * logic port:
         * mgmt_ip  Str ipv4地址
         */

        //todo nfs 容量 = fs data_space?

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "create nfs datastore success !");

        if (params == null || params.size() == 0) {
            resMap.put("code", 403);
            resMap.put("msg", "params error , please check your params !");
            return resMap;
        }

        //创建fs

        //创建nfs share

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
         *       storage_pool_id
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
        //todo 多个文件夹 总capacity <=storage pool data_space?
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

    //create nfs share
    private Map<String,Object> createNfsShare(Map<String,Object> params,String task_id) throws Exception {

        /**
         * CreateNfsShareRequest{
         *     fs_id  文件系统在DJ的id 必  通过创建fs返回的task_id 去查询监听任务 通过task_id去拿对应的资源id
         *     create_nfs_share_param 创建NFS共享参数 必
         *     {
         *         share_path str  共享路径 必 （exportPath）
         *         name  str  共享别名
         *         description  str 描述
         *         character_encoding" : "utf-8",
         *         audit_items array 支持审计的事件列表[ {
         *           audititem str 支持审计的事件：none：无操作，all：所有操作，open：打开，create：创建，read：读，write：写，close：关闭，delete：删除，rename：重命名，get_security：获取安全属性，set_security：设置安全属性，get_attr：获取属性，set_attr：设置属性
         *         } ],
         *         show_snapshot_enable boolean 是否开启显示Snapshot的功能
         *         NfsShareClientAddition array NFS共享客户端 （选） [{
         *             accessval str 权限：read-only：只读， read/write：读写 必
         *             all_squash str  权限限制：all_squash，no_all_squash 必
         *             anonymous_id str 匿名用户id
         *             name str 客户端IP或主机名或网络组名 必
         *             root_squash str root权限限制：root_squash，no_root_squash 必
         *             secure str 源端口校验限制：secure，insecure
         *             sync str  写入模式：synchronization：同步， asynchronization：异步 必
         *         }],
         * }
         */

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 202);
        resMap.put("msg", "create nfs share success !");
        String url = "/rest/fileservice/v1/nfs-shares";

        //todo 获取taskDetailInfo中的资源id
        //注意：需要先确定任务完成状态，在完成状态之下才能拿到对应的id, 中间存在延时问题。
        TaskDetailInfo taskDetailInfo = taskService.queryTaskById(task_id);
        String fsId = "taskDetailInfo.getFsId()";
        params.put("fs_id", fsId);

        ResponseEntity<String> responseEntity = access(url, HttpMethod.POST, gson.toJson(params));
        int code = responseEntity.getStatusCodeValue();
        if (code != 202) {
            resMap.put("code", code);
            resMap.put("msg", "create nfs share error !");
            return resMap;
        }
        String object = responseEntity.getBody();
        JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
        String task_id1 = jsonObject.get("task_id").getAsString();
        resMap.put("data", task_id1);
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

    private ResponseEntity<String> access(String url, HttpMethod method, String requestBody) throws Exception {

        RestUtils restUtils = new RestUtils();
        RestTemplate restTemplate = restUtils.getRestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, entity, String.class);
        LOG.info(url + "==responseEntity==" + (responseEntity == null ? "null" : responseEntity.getStatusCodeValue()));

        return responseEntity;
    }
}
