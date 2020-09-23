package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.model.TaskDetailInfo;
import com.dmeplugin.dmestore.model.TaskDetailResource;
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

    private final String API_FS_CREATE = "/rest/fileservice/v1/filesystems/customize";
    private final String API_NFSSHARE_CREATE = "/rest/fileservice/v1/nfs-shares";
    private final String API_STORAGEPOOL_LIST = "/rest/storagemgmt/v1/storagepools/query";
    private final String API_FS_UPDATE = "/rest/fileservice/v1/filesystems";
    private final String API_NFS_CHANGECAPACITY = "/rest/fileservice/v1/filesystems";

    public static final Logger LOG = LoggerFactory.getLogger(VmfsOperationController.class);
    private DmeAccessService dmeAccessService;
    private TaskService taskService;
    private DmeStorageService dmeStorageService;
    private Gson gson = new Gson();
    private VCSDKUtils vcsdkUtils;

    public DmeAccessService getDmeAccessService() {
        return dmeAccessService;
    }

    public void setDmeAccessService(DmeAccessService dmeAccessService) {
        this.dmeAccessService = dmeAccessService;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public DmeStorageService getDmeStorageService() {
        return dmeStorageService;
    }

    public void setDmeStorageService(DmeStorageService dmeStorageService) {
        this.dmeStorageService = dmeStorageService;
    }

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
    }

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

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "create nfs datastore success !");
        if (params == null || params.size() == 0) {
            resMap.put("code", 403);
            resMap.put("msg", "params error , please check your params !");
            return resMap;
        }

        try {
            //创建fs
            Map<String, String> fsMap = new HashMap<>();
            Map<String, String> nfsShareMap = new HashMap<>();
            String filesystem_specs = params.get("filesystem_specs");
            if (StringUtils.isEmpty(filesystem_specs)) {
                LOG.error("{filesystem_specs}={"+filesystem_specs+"}");
            }
            fsMap.put("filesystem_specs", filesystem_specs);
            String pool_raw_id = params.get("pool_raw_id");
            String storage_id = params.get("storage_id");
            if (StringUtils.isEmpty(storage_id) || StringUtils.isEmpty(storage_id)) {
                LOG.error("{pool_raw_id/storage_id}={"+pool_raw_id+"/"+storage_id+"}");
            }
            fsMap.put("pool_raw_id", pool_raw_id);
            fsMap.put("storage_id", storage_id);
            String tuning = params.get("tuning");
            if (!StringUtils.isEmpty(tuning)) {
                fsMap.put("tuning", tuning);
            }
            String create_nfs_share_param = params.get("create_nfs_share_param");
            String nfs_share_client_addition = params.get("nfs_share_client_addition");
            Map<String, String> create_nfs_share_params = null;
            if (!StringUtils.isEmpty(create_nfs_share_param) && !StringUtils.isEmpty(nfs_share_client_addition)) {
                create_nfs_share_params = gson.fromJson(create_nfs_share_param, Map.class);
                if (create_nfs_share_params != null && create_nfs_share_params.size() > 0) {
                    create_nfs_share_params.put("nfs_share_client_addition", nfs_share_client_addition);
                    fsMap.put("create_nfs_share_param", gson.toJson(create_nfs_share_params));
                    String show_snapshot_enable = params.get("show_snapshot_enable");
                    if (!StringUtils.isEmpty(show_snapshot_enable)) {
                        create_nfs_share_params.put("show_snapshot_enable", show_snapshot_enable);
                    }
                    nfsShareMap.put("create_nfs_share_param", gson.toJson(create_nfs_share_params));
                }
            }
            String storage_pool_id = params.get("storage_pool_id");
            if (StringUtils.isEmpty(storage_pool_id)) {
                LOG.error("storage_pool_id={"+storage_pool_id+"}");
            }
            String task_id = "";
            Map<String, Object> fileSystem = createFileSystem(fsMap, storage_pool_id);
            int fsCode = ToolUtils.getInt(fileSystem.get("code"));
            if (fsCode == 202) {
                task_id = fileSystem.get("task_id").toString();
            }
            String nfsName = params.get("nfsName");
            if (StringUtils.isEmpty(nfsName)) {
                LOG.error("nfsName={"+nfsName+"}");
            }
            Map<String, Object> nfsShare = createNfsShare(nfsShareMap, task_id,nfsName);
            if (ToolUtils.getInt(nfsShare.get("code"))==202) {
                String nfsShareTaskId = nfsShare.get("task_id").toString();
                resMap.put("data", nfsShareTaskId);
            }
            //query oriented logic portv by storage_id
            String serverHost = "";
            String current_port_id = params.get("current_port_id");
            Map<String, Object> logicPorts = dmeStorageService.getLogicPorts(storage_id);
            Object logicPort = logicPorts.get("data");
            if (logicPort != null) {
                JsonArray jsonArray = new JsonParser().parse(gson.toJson(logicPort)).getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    String current_port_id1 = element.get("current_port_id").getAsString();
                    if (!StringUtils.isEmpty(current_port_id) && current_port_id1.equals(current_port_id)) {
                        serverHost = element.get("mgmt_ip").getAsString();
                        break;
                    }
                }
            }
            String exportPath = params.get("exportPath");
            String type = params.get("type");
            String accessMode = params.get("accessMode");
            //可以挂载多个
            List<String> mounts = new ArrayList<>();
            List<String> nfsShareClientArrayAddition = Arrays.asList(gson.toJson(nfs_share_client_addition));
            for (String shareClientHost : nfsShareClientArrayAddition) {
                Map<String,String> shareClientHostMap = gson.fromJson(shareClientHost, Map.class);
                if (shareClientHostMap != null && shareClientHostMap.size() > 0 && shareClientHostMap.get("name")!= null) {
                    mounts.add(shareClientHostMap.get("name"));
                }
            }
            if (StringUtils.isEmpty(serverHost) || StringUtils.isEmpty(exportPath) || StringUtils.isEmpty(accessMode) || mounts.size() == 0) {
                resMap.put("code", 403);
                resMap.put("msg", "params error , please check your params !");
                return resMap;
            }
            String result = vcsdkUtils.createNfsDatastore(serverHost, exportPath, nfsName, accessMode, mounts, type);
            if ("failed".equals(result)) {
                resMap.put("code", 403);
                resMap.put("msg", "create nfs datastore error!");
                return resMap;
            }
        } catch (Exception e) {
            LOG.error("create nfs datastore error", e);
            resMap.put("code", 403);
            resMap.put("msg", e.getMessage());
            return resMap;
        }
        return resMap;
    }

    /** request params
     *  {
     *      String dataStoreObjectId 跳转用唯一id  必
     *      String nfsShareName nfsShare的名字 必
     *      String nfsName nfsDataStore名字 必
     *
     *      fs params:
     *      file_system_id String 文件系统唯一标识 必
     *      capacity_autonegotiation 自动扩缩容 相关属性{
     *          capacity_self_adjusting_mode str  自动扩容触发门限百分比，默认85%。自动扩容触发门限百分比必须大于自动缩容触发门限百分比
     *          capacity_recycle_mode str  容量回收模式。 expand_capacity：优先扩容；delete_snapshots：优先删除旧快照。默认优先扩容
     *          auto_size_enable  boolean 自动调整容量开关。 false: 关闭；true：打开。默认打开
     *          auto_grow_threshold_percent int 自动扩容触发门限百分比，默认85%。自动扩容触发门限百分比必须大于自动缩容触发门限百分比
     *          auto_shrink_threshold_percent int 自动缩容触发门限百分比，默认50%。自动扩容触发门限百分比必须大于自动缩容触发门限百分比,
     *          max_auto_size double 自动扩容下限。单位GB。默认16777216GB。自动扩容上限必须大于等于自动缩容下限
     *          min_auto_size double 自动缩容下限。单位GB。默认16777216GB。自动扩容上限必须大于等于自动缩容下限
     *          auto_size_increment int 自动扩（缩）容单次变化量。单位MB。默认1GB
     *       },
     *       name String fs新名字 (取消勾选可以没有)
     *       tuning属性 （高级属性设置）{
     *             deduplication_enabled  boolean 重复数据删除。默认关闭
     *             compression_enabled  boolean 数据压缩。默认关闭
     *             allocation_type str 文件系统分配类型，取值范围 thin，thick。默认为thin
     *        }
     *       qos_policy 属性(开启后 需要参数){
     *             max_bandwidth int 最大带宽，在控制上限的时候有效,与minbandwidth,miniops互斥
     *             max_iops int 最大iops，在控制上限的时候有效,与minbandwidth,miniops互斥
     *             min_bandwidth  int 最小带宽，在保护下限的时候有效，与maxbandwidth,maxiops互斥
     *             min_iops  int 最小iops，在保护下限的时候有效, 与maxbandwidth,maxiops互斥
     *             latency int 时延，单位ms 仅保护下限支持该参数
     *             }
     *
     *       nfs share :
     *       nfs_share_id string NFS共享的唯一标识 必  (id)
     *  }
     *
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> updateNfsDatastore(Map<String, String> params) {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "update nfs datastore success !");

        if (params == null || params.size() == 0) {
            LOG.error("params error , please check it! params=" + params);
            resMap.put("code", 403);
            resMap.put("msg", "params error , please check it!");
            return resMap;
        }

        String dataStoreObjectId = params.get("dataStoreObjectId");
        String nfsName = params.get("nfsName");
        if (StringUtils.isEmpty(dataStoreObjectId)||StringUtils.isEmpty(nfsName)) {
            LOG.error("params error , please check it! dataStoreObjectId=" + dataStoreObjectId);
            resMap.put("code", 403);
            resMap.put("msg", "params error , please check it!");
            return resMap;
        }

        //update fs
        Map<String, String> fsReqBody = new HashMap<>();
        String tuning = params.get("tuning");
        String qos_policy = params.get("qos_policy");
        Map<String, String> tuningMap = null;
        if (!StringUtils.isEmpty(tuning)) {
            if (!StringUtils.isEmpty(qos_policy)) {
                tuningMap = gson.fromJson(tuning, Map.class);
                tuningMap.put("qos_policy", qos_policy);
            }
        }
        if (tuningMap != null) {
            fsReqBody.put("tuning", gson.toJson(tuningMap));
        }
        String file_system_id = params.get("file_system_id");
        String capacity_autonegotiation = params.get("capacity_autonegotiation");
        String name = params.get("name");
        if (!StringUtils.isEmpty(file_system_id)) {
            fsReqBody.put("file_system_id", file_system_id);
        }
        if (!StringUtils.isEmpty(capacity_autonegotiation)) {
            fsReqBody.put("capacity_autonegotiation", capacity_autonegotiation);
        }
        if (!StringUtils.isEmpty(name)) {
            fsReqBody.put("name", name);
        }
        try {
            //update fs
            Map<String, Object> stringObjectMap = updateFileSystem(fsReqBody);
            int code = ToolUtils.getInt(stringObjectMap.get("code"));
            if (code == 202) {
                LOG.info("{"+name+"}"+stringObjectMap.get("msg"));
            } else {
                LOG.info("{"+name+"}"+stringObjectMap.get("msg"));
                resMap.put("msg",stringObjectMap.get("msg"));
            }
            //update nfs datastore
            String result = vcsdkUtils.renameDataStore(nfsName, dataStoreObjectId);
            if (result.equals("success")) {
                LOG.info("{"+nfsName+"}rename nfs datastore success!");
            }
            //todo update nfs share 没找到对应API
        } catch (Exception e) {
            LOG.error( "update nfs datastore error !",e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        }
        return resMap;
    }

    /**
     *  params{
     *      file_system_id String 文件系统唯一标识
     *      capacity double 该规格文件系统容量，单位GB
     *      is_expand boolean 扩容 true  缩容 false
     *  }
     * @param
     * @return
     */
    @Override
    public ResponseBodyBean changNfsCapacity(Map<String, String> params) {

        ResponseBodyBean responseBodyBean = new ResponseBodyBean();
        responseBodyBean.setCode("202");
        responseBodyBean.setDescription("change nfs capacity success!");
        String capacity = params.get("capacity");
        String is_expand = params.get("is_expand");
        if (params == null || params.size() == 0 || StringUtils.isEmpty(params.get("file_system_id"))
                || StringUtils.isEmpty(params.get("capacity")) || StringUtils.isEmpty(params.get("is_expand"))) {
            responseBodyBean.setCode("403");
            responseBodyBean.setDescription("change nfs capacity error! cause : params error");
        }
        Double changeCapacity = Double.valueOf(capacity);
        String url = API_NFS_CHANGECAPACITY + "/" + params.get("file_system_id");

      /*  if () {
        }*/





        return null;
    }

    //create file system
    private Map<String, Object> createFileSystem(Map<String, String> params, String storage_pool_id) throws Exception {
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 202);
        resMap.put("msg", "create file system success !");

        if (params == null || params.size() == 0) {
            LOG.error("url:{" + API_FS_CREATE + "},param error,please check it!");
            resMap.put("code", 403);
            resMap.put("msg", "create file system error !");
            return resMap;
        }
        String storage_id = params.get("storage_id");
        String filesystemSpecs = params.get("filesystem_specs");
        List<String> list = Arrays.asList(filesystemSpecs);
        Map<String, Object> filesystem_specs = new HashMap<>();

        Double capacity = null;
        int count = 1;
        Double freeCapacity = null;
        Double data_space = null;
        if (!StringUtils.isEmpty(storage_id) || !StringUtils.isEmpty(storage_pool_id)) {
            Map<String, Object> resBody = getDataspaceOfStoragepool(storage_id, storage_pool_id);
            if (ToolUtils.getInt(resBody.get("code")) == 200) {
                data_space = Double.valueOf(resBody.get("data_space").toString());
                if (freeCapacity == null && data_space != null) {
                    freeCapacity = data_space;
                }
            }
        }
        List<Map<String, Object>> filesystemSpecsList = new ArrayList<>();
        //目前一个nfs 对应一个fs (一对多通用)
        for (String filesystem_specss : list) {
            Map<String, Object> map = gson.fromJson(filesystem_specss, Map.class);
            Object objCapacity = map.get("capacity");
            Object fsNum = map.get("count");
            if (objCapacity != null && fsNum != null) {
                capacity = Double.valueOf(objCapacity.toString());
                count = ToolUtils.getInt(fsNum);
                if (Double.compare(capacity * count, freeCapacity) > 1) {
                    capacity = freeCapacity / count;
                } else {
                    freeCapacity -= capacity * count;
                }
                filesystem_specs.put("capacity", capacity);
                filesystem_specs.put("name", map.get("name"));
                filesystem_specs.put("count", map.get("count"));
            }
            filesystemSpecsList.add(filesystem_specs);
        }
        params.put("filesystem_specs", gson.toJson(filesystemSpecsList));
        ResponseEntity<String> responseEntity = dmeAccessService.access(API_FS_CREATE, HttpMethod.POST, gson.toJson(params));
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
    private Map<String,Object> createNfsShare(Map<String,String> params,String task_id,String dsname) throws Exception {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 202);
        resMap.put("msg", "create nfs share success !");

        //todo 获取taskDetailInfo中的资源id
        //注意：需要先确定任务完成状态，在完成状态之下才能拿到对应的id, 中间存在延时问题。
        String fsId = "";
        if (!StringUtils.isEmpty(task_id)) {
            fsId = getFsIdByTaskId(task_id, dsname);
            if (StringUtils.isEmpty(fsId)) {
                fsId = getFsIdByTaskId(task_id, dsname);
            }
            params.put("fs_id", fsId);
            ResponseEntity<String> responseEntity = access(API_NFSSHARE_CREATE, HttpMethod.POST, gson.toJson(params));
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
        } else {
            LOG.error("{/rest/fileservice/v1/nfs-shares}_param task_id=" + task_id);
        }
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
        Map<String, String> reqBody = new HashMap<>();
        reqBody.put("storage_id",storage_id);
        reqBody.put("pool_id", pool_id);

        ResponseEntity<String> responseEntity = dmeAccessService.access(API_STORAGEPOOL_LIST, HttpMethod.POST, gson.toJson(reqBody));
        LOG.info("url:{" + API_STORAGEPOOL_LIST + "},responseEntity:" + responseEntity);
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

    private Map<String,Object> updateFileSystem(Map<String,String> params) throws Exception {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 202);
        resMap.put("msg", "update nfs datastore success !");

        String file_system_id = params.get("file_system_id");
        if (StringUtils.isEmpty(file_system_id)) {
            resMap.put("code", 403);
            resMap.put("msg", "update nfs datastore error !");
            return resMap;
        }
        String url = API_FS_UPDATE + "/" + file_system_id;

        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.PUT, gson.toJson(params));
        int code = responseEntity.getStatusCodeValue();
        if (code!=202) {
            resMap.put("code", code);
            resMap.put("msg", "update nfs datastore error !");
            return resMap;
        }
        String object = responseEntity.getBody();
        JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
        String task_id = jsonObject.get("task_id").getAsString();
        resMap.put("data", task_id);
        return resMap;
    }

    private Map<String,Object> updateNfsShare(Map<String,String> params){

        return new HashMap<>();
    }

    private String getFsIdByTaskId(String task_id,String dsname) throws Exception {

        String fsId = "";
        if (!StringUtils.isEmpty(task_id)) {
            TaskDetailInfo taskDetailInfo = taskService.queryTaskById(task_id);
            List<TaskDetailResource> resources = taskDetailInfo.getResources();
            for (TaskDetailResource taskDetail : resources) {
                if (taskDetail.getName().equals(dsname)) {
                    fsId = taskDetail.getId();
                    break;
                }
            }
        }
        return fsId;
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
