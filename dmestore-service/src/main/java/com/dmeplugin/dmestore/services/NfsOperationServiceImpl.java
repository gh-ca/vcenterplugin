package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.dao.DmeVmwareRalationDao;
import com.dmeplugin.dmestore.entity.DmeVmwareRelation;
import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.exception.VcenterRuntimeException;
import com.dmeplugin.dmestore.model.*;
import com.dmeplugin.dmestore.mvc.VmfsOperationController;
import com.dmeplugin.dmestore.utils.RestUtils;
import com.dmeplugin.dmestore.utils.StringUtil;
import com.dmeplugin.dmestore.utils.ToolUtils;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final String API_NFSSHARE_CREATE = "/rest/fileservice/v1/nfs-shares";
    private final String API_STORAGEPOOL_LIST = "/rest/resourcedb/v1/instances/";
    private final String API_FS_UPDATE = "/rest/fileservice/v1/filesystems";
    private final String API_FS_QUERYONE = "/rest/fileservice/v1/filesystems/query";
    private final String API_FILESYSTEMS_DETAIL = "/rest/fileservice/v1/filesystems/";
    private final String API_SHARE_QUERYONE = "/rest/fileservice/v1/nfs-shares/summary";

    public static final Logger LOG = LoggerFactory.getLogger(VmfsOperationController.class);
    private DmeAccessService dmeAccessService;
    private TaskService taskService;
    private DmeStorageService dmeStorageService;
    private Gson gson = new Gson();
    private VCSDKUtils vcsdkUtils;
    private DmeVmwareRalationDao dmeVmwareRalationDao;

    public DmeVmwareRalationDao getDmeVmwareRalationDao() {
        return dmeVmwareRalationDao;
    }

    public void setDmeVmwareRalationDao(DmeVmwareRalationDao dmeVmwareRalationDao) {
        this.dmeVmwareRalationDao = dmeVmwareRalationDao;
    }

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
     * @param params
     * @return
     */
    @Override
    public void createNfsDatastore(Map<String, Object> params) throws DMEException {

        //Map<String, Object> resMap = new HashMap<>();
        //resMap.put("code", 200);
        //resMap.put("msg", "create nfs datastore success !");
        if (params == null || params.size() == 0) {
            //resMap.put("code", 403);
            //resMap.put("msg", "params error , please check your params !");
            //return resMap;
            throw new DMEException("403","params error , please check your params !");
        }
        try {
            //创建fs
            Map<String, Object> fsMap = new HashMap<>();
            Map<String, Object> nfsShareMap = new HashMap<>();
            Object filesystem_specs = params.get("filesystem_specs");
            if (StringUtils.isEmpty(filesystem_specs)) {
                LOG.error("{filesystem_specs}={"+filesystem_specs+"}");
                throw new DMEException("403","{filesystem_specs}={"+filesystem_specs+"}");
            }

            String fs_name = "";
            JsonArray jsonArray1 = gson.fromJson(gson.toJson(filesystem_specs), JsonArray.class);
            for (JsonElement jsonElement : jsonArray1) {
                JsonObject element = jsonElement.getAsJsonObject();
                fs_name = ToolUtils.jsonToStr(element.get("name"));
            }
            fsMap.put("filesystem_specs", gson.toJson(filesystem_specs));
            String pool_raw_id = (String)params.get("pool_raw_id");
            String storage_id = (String)params.get("storage_id");
            if (StringUtils.isEmpty(storage_id) || StringUtils.isEmpty(storage_id)) {
                LOG.error("{pool_raw_id/storage_id}={"+pool_raw_id+"/"+storage_id+"}");
                throw new DMEException("403","{pool_raw_id/storage_id}={"+pool_raw_id+"/"+storage_id+"}");
            }
            fsMap.put("pool_raw_id", pool_raw_id);
            fsMap.put("storage_id", storage_id);
            //todo 存储池不具备smartQut功能
            Object tuning = params.get("tuning");
            if (!StringUtils.isEmpty(tuning)) {
                fsMap.put("tuning", tuning);
            }
            //可以挂载多个
            List<Map<String,String>> mounts = new ArrayList<>();
            Map<String, String> mount = new HashMap<>();
            List<Object> reqNfsShareClientArrayAdditions = new ArrayList<>();
            Object nfs_share_client_addition = params.get("nfs_share_client_addition");
            List<Map<String, Object>> nfsShareClientArrayAddition = (List<Map<String, Object>>) nfs_share_client_addition;
            String share_name = "";
            if (nfs_share_client_addition != null) {
                Map<String, Object> reqNfsShareClientArrayAddition = new HashMap<>();
                for (int i = 0; i < nfsShareClientArrayAddition.size(); i++) {
                    Map<String, Object> shareClientHostMap = nfsShareClientArrayAddition.get(i);
                    if (shareClientHostMap != null && shareClientHostMap.size() > 0 && shareClientHostMap.get("objectId") != null) {
                        reqNfsShareClientArrayAddition.put("name", shareClientHostMap.get("name"));
                        //设置创建nfs默认值
                        reqNfsShareClientArrayAddition.put("accessval", "read-only");
                        reqNfsShareClientArrayAddition.put("sync", "synchronization");
                        reqNfsShareClientArrayAddition.put("all_squash", "all_squash");
                        reqNfsShareClientArrayAddition.put("root_squash", "root_squash");
                        reqNfsShareClientArrayAddition.put("secure", "insecure");
                        mount.put((String) shareClientHostMap.get("objectId"), (String) shareClientHostMap.get("name"));
                    }
                    reqNfsShareClientArrayAdditions.add(reqNfsShareClientArrayAddition);
                }
                mounts.add(mount);
                Object create_nfs_share_param = params.get("create_nfs_share_param");
                Map<String, Object> create_nfs_share_params = null;
                if (!StringUtils.isEmpty(create_nfs_share_param)) {
                    create_nfs_share_params = gson.fromJson(gson.toJson(create_nfs_share_param), Map.class);
                    if (create_nfs_share_params != null && create_nfs_share_params.size() > 0) {
                        create_nfs_share_params.put("nfs_share_client_addition", reqNfsShareClientArrayAdditions);
                        share_name = (String) create_nfs_share_params.get("name");
                        Object show_snapshot_enable = params.get("show_snapshot_enable");
                        if (!StringUtils.isEmpty(show_snapshot_enable)) {
                            create_nfs_share_params.put("show_snapshot_enable", gson.toJson(show_snapshot_enable));
                        }
                        nfsShareMap.put("create_nfs_share_param", create_nfs_share_params);
                    }
                }
            }
            String storage_pool_id =(String) params.get("storage_pool_id");
            if (StringUtils.isEmpty(storage_pool_id)) {
                LOG.error("storage_pool_id={"+storage_pool_id+"}");
            }
            String task_id = "";
            task_id= createFileSystem(fsMap, storage_pool_id);

            String nfsName = (String)params.get("nfsName");
            if (StringUtils.isEmpty(nfsName)) {
                LOG.error("nfsName={"+nfsName+"}");
            }
            String fsId = "";
            String share_id = "";
            List<String> task_ids = new ArrayList<>();
            if (!StringUtils.isEmpty(task_id)) {
                task_ids.add(task_id);
                Boolean flag = taskService.checkTaskStatus(task_ids);
                if (flag) {
                    fsId = getFsIdByName(fs_name);
                }else {
                    //resMap.put("code", 403);
                    //resMap.put("msg", "create FileSystem fail");
                    //return resMap;
                    throw new DMEException("403","create FileSystem fail");
                }
                if (!"".equals(fsId)) {
                    nfsShareMap.put("fs_id", fsId);
                    String nfsShareTaskId = createNfsShare(nfsShareMap);

                        List<String> shareIds = new ArrayList<>();
                        if (!StringUtils.isEmpty(nfsShareTaskId)) {
                            shareIds.add(nfsShareTaskId);
                            Boolean share_flag = taskService.checkTaskStatus(shareIds);
                            if (share_flag) {
                                //查询shareId
                                share_id = getShareIdByName(share_name, fs_name);
                            }
                        }

                }
            }
            //query oriented logic portv by storage_id
            String serverHost = "";
            String logicPortName = "";
            String current_port_id = String.valueOf(params.get("current_port_id"));
            Map<String, String> logicMap = getMgmtByStorageId(storage_id, current_port_id);
            if (logicMap != null && logicMap.size() != 0) {
                serverHost = logicMap.get("mgmt");
                logicPortName = logicMap.get("logicPortName");
            }
            String exportPath = (String)params.get("exportPath");
            String type = (String)params.get("type");
            String accessMode = (String)params.get("accessMode");

            if (StringUtils.isEmpty(serverHost) || StringUtils.isEmpty(exportPath) || StringUtils.isEmpty(accessMode) || mounts.size() == 0) {
                //resMap.put("code", 403);
                //resMap.put("msg", "params error , please check your params !");
                //return resMap;
                throw new DMEException("403","params error , please check your params !");
            }
            String result = vcsdkUtils.createNfsDatastore(serverHost, exportPath, nfsName, accessMode, mounts, type);
            if ("failed".equals(result)) {
                //resMap.put("code", 403);
                //resMap.put("msg", "create nfs datastore error!");
                //return resMap;
                throw new VcenterRuntimeException("403","create nfs datastore error!");
            }
            //save datastore info to DP_DME_VMWARE_RELATION
            saveNfsInfoToDmeVmwareRelation(result,current_port_id,logicPortName,storage_id,fsId,share_name,share_id,fs_name);
        } catch (Exception e) {
            LOG.error("create nfs datastore error", e);
            //resMap.put("code", 403);
            //resMap.put("msg", e.getMessage());
            //return resMap;
            throw new DMEException("403",e.getMessage());
        }
    }

    /** request params
     *  {
     *      String dataStoreObjectId 跳转用唯一id  必
     *      String nfsShareName nfsShare的名字 （不能修改share的名字）
     *      String nfsName nfsDataStore名字 必
     *
     *      fs params:
     *      file_system_id String 文件系统唯一标识 必
     *      capacity_autonegotiation 自动扩缩容 相关属性{
     *          auto_size_enable  boolean 自动调整容量开关。 false: 关闭；true：打开。默认打开
     *       },
     *       name String fs名字
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
     *       nfs_share_id string NFS共享的唯一标识 必  (id) 暂时不能确定是否需要，需要等dme能创建share时候才能确定是否需要修改share
     *  }
     *
     * @param params
     */
    @Override
    public void updateNfsDatastore(Map<String, Object> params) throws DMEException {

        //Map<String, Object> resMap = new HashMap<>();
        //resMap.put("code", 202);
        //resMap.put("msg", "update nfs datastore success !");

        if (params == null || params.size() == 0) {
            LOG.error("params error , please check it! reqParams=" + params);
            //resMap.put("code", 403);
            //resMap.put("msg", "params error , please check it!");
            //return resMap;
            throw new DMEException("403","params error , please check it!");
        }
        String dataStoreObjectId = (String) params.get("dataStoreObjectId");
        String nfsName = (String) params.get("nfsName");
        if (StringUtils.isEmpty(dataStoreObjectId)||StringUtils.isEmpty(nfsName)) {
            LOG.error("params error , please check it! dataStoreObjectId=" + dataStoreObjectId);
            //resMap.put("code", 403);
            //resMap.put("msg", "params error , please check it!");
            //return resMap;
            throw new DMEException("403", "params error , please check it!");
        }
        //update fs
        Map<String, Object> fsReqBody = new HashMap<>();
        Map<String,Object> tuning = gson.fromJson(gson.toJson(params.get("tuning")), Map.class);
        Map<String,Object> qos_policy = gson.fromJson(gson.toJson(params.get("qos_policy")), Map.class);
        String name = (String)params.get("name");
        if (!StringUtils.isEmpty(name)) {
            fsReqBody.put("name", name);
        }

        if (tuning!=null&&tuning.size()!=0) {
            if (qos_policy!=null&&qos_policy.size()!=0) {
                tuning.put("qos_policy", qos_policy);
            }
            fsReqBody.put("tuning", tuning);
        }

        String file_system_id = (String) params.get("file_system_id");
        Object capacity_autonegotiation = params.get("capacity_autonegotiation");
        if (capacity_autonegotiation != null) {
            fsReqBody.put("capacity_autonegotiation", capacity_autonegotiation);
        }
        try {
            if (!StringUtils.isEmpty(file_system_id)) {
                String taskId = updateFileSystem(fsReqBody,file_system_id);
                //todo 是否还要检测任务状态
            }
            //update nfs datastore
            String result = vcsdkUtils.renameDataStore(nfsName, dataStoreObjectId);
            if ("success".equals(result)) {
                LOG.info("{"+nfsName+"}rename nfs datastore success!");
            }
        } catch (Exception e) {
            LOG.error( "update nfs datastore error !",e);
            //resMap.put("code", 503);
            //resMap.put("msg", e.getMessage());
            throw new DMEException("503",e.getMessage());
        }
        //return resMap;
    }

    @Override
    public void changeNfsCapacity(Map<String, Object> params) throws DMEException {

        //ResponseBodyBean responseBodyBean = new ResponseBodyBean();
        //responseBodyBean.setCode("202");
        //responseBodyBean.setDescription("change nfs storage capacity success!");
        if (params == null || params.size() == 0) {
            //responseBodyBean.setCode("202");
            ////responseBodyBean.setDescription("change nfs storage capacity error,params error!");
           // return responseBodyBean;
            throw new DMEException("503","change nfs storage capacity error,params error!");
        }
        String storeObjectId = (String) params.get("storeObjectId");
        String file_system_id = (String) params.get("fileSystemId");
        Boolean is_expand = (Boolean) params.get("expand");
        Double capacity = Double.valueOf(params.get("capacity").toString());
        if ((StringUtils.isEmpty(storeObjectId)&&StringUtils.isEmpty(file_system_id)) ||StringUtils.isEmpty(is_expand)||StringUtils.isEmpty(capacity)) {
            //responseBodyBean.setCode("202");
            //responseBodyBean.setDescription("change nfs storage capacity error,params error!");
            //return responseBodyBean;
            throw new DMEException("202","change nfs storage capacity error,params error!");
        }

        if (!StringUtils.isEmpty(storeObjectId)){
            List<String> fsIds = dmeVmwareRalationDao.getFsIdsByStorageId(storeObjectId);
            if (fsIds.size()>0){
                //如果objectid存在，则通过objectid的关系表来获取fsid
                file_system_id=fsIds.get(0);
            }
        }

        String url = API_FS_UPDATE + "/" + file_system_id;
        //查询指定fs拿对应的信息
        Integer code = 0;
        try {
            FileSystem fileSystem = null;
            Map<String, String> orientedFileSystem = getOrientedFs(file_system_id);
            if ("200".equals(orientedFileSystem.get("code"))) {
                String fs = orientedFileSystem.get("data");
                if (!StringUtils.isEmpty(fs)) {
                    fileSystem = gson.fromJson(fs, FileSystem.class);
                }
            }
            if (fileSystem != null) {
                String storage_id = fileSystem.getStorageId();
                String storage_pool_name = fileSystem.getStoragePoolName();
                Double min_size_fs_capacity = fileSystem.getMinSizeFsCapacity();
                Double available_capacity = fileSystem.getAvailableCapacity();
                String alloc_type = fileSystem.getAllocType();
                Double currentCapacity = fileSystem.getCapacity();
                //查询存储池可用空间
                Double data_space = getDataspaceOfStoragepool(storage_pool_name, null, storage_id);
                Double exchangedCapacity = 0.0;
                Double changeCapacity = capacity;
                if (is_expand) {
                    //扩容
                    if (data_space != null && Double.compare(changeCapacity, data_space) > 1) {
                        LOG.info("扩容量超出存储池可用容量，将当前存储池可用容量当做扩容量!");
                        changeCapacity = data_space;
                    }
                    exchangedCapacity = changeCapacity + currentCapacity;
                } else if(!is_expand) {
                    //缩容
                    if (!StringUtils.isEmpty(alloc_type) && "thin".equals(alloc_type)) {
                        //thin 分配策略缩容
                        // 该文件系统总容量-可用容量-文件系统能缩容的最小空间=实际可用缩小容量    与变化量进行比较
                        if (currentCapacity - changeCapacity >= min_size_fs_capacity) {
                            if (Double.compare(changeCapacity, available_capacity) > 1) {
                                LOG.info("thin策略：nfs预计缩容到Thin文件系统能缩容的最小空间!");
                                exchangedCapacity = currentCapacity - available_capacity;
                            } else {
                                exchangedCapacity = currentCapacity - changeCapacity;
                            }
                        }else {
                            exchangedCapacity = currentCapacity;
                            //responseBodyBean.setCode("400");
                            //responseBodyBean.setDescription("FileSystem:{id:"+file_system_id+"}未达到能缩容条件,FileSystem缩容后容量不得小于:"+min_size_fs_capacity+"GB");
                            LOG.info("FileSystem:{"+file_system_id+"}未达到能缩容条件,FileSystem缩容后条件容量不得小于:"+min_size_fs_capacity+"GB");
                            throw new DMEException("400","FileSystem:{id:"+file_system_id+"}未达到能缩容条件,FileSystem缩容后容量不得小于:"+min_size_fs_capacity+"GB");

                        }
                    } else {
                        if (Double.compare(available_capacity, changeCapacity) > 1) {
                            exchangedCapacity = currentCapacity - changeCapacity;
                        } else {
                            exchangedCapacity = currentCapacity - available_capacity;
                        }
                    }
                }
                Map<String, Object> reqParam = new HashMap<>();
                reqParam.put("file_system_id", file_system_id);
                reqParam.put("capacity",exchangedCapacity);
                ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.PUT, gson.toJson(reqParam));
                code = responseEntity.getStatusCodeValue();
                if (code != 202) {
                    //responseBodyBean.setCode(code.toString());
                    //responseBodyBean.setDescription("expand or recycle nfs storage capacity error!");
                    //return responseBodyBean;
                    throw new DMEException("202","expand or recycle nfs storage capacity error!");
                }
                String object = responseEntity.getBody();
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                String task_id = jsonObject.get("task_id").getAsString();
                List<String> task_ids = new ArrayList<>();
                task_ids.add(task_id);
                Boolean flag = taskService.checkTaskStatus(task_ids);
                if (!flag) {
                   // responseBodyBean.setDescription("expand or recycle nfs storage capacity failed!");
                    throw new DMEException("503","expand or recycle nfs storage capacity failed!");
                }
                //刷新datastore容量
                vcsdkUtils.refreshDatastore(storeObjectId);
                //responseBodyBean.setData("task_id:"+task_id);
            }
        } catch (Exception e) {
            LOG.error("change nfs storage capacity error!",e);
            //responseBodyBean.setCode(code.toString());
            //responseBodyBean.setDescription("change nfs storage capacity error!"+e.getMessage());
            throw new DMEException("503","change nfs storage capacity error!"+e.getMessage());
        }
        //return responseBodyBean;
    }

    //create file system
    private String createFileSystem(Map<String, Object> params, String storage_pool_id) throws Exception {
        //Map<String, Object> resMap = new HashMap<>();
        ////resMap.put("code", 202);
        //resMap.put("msg", "create file system success !");
        LOG.info("{params}:" + params);

        if (params == null || params.size() == 0) {
            LOG.error("url:{" + API_FS_CREATE + "},param error,please check it!");
            //resMap.put("code", 403);
            //resMap.put("msg", "create file system error !");
            //return resMap;
            throw new DMEException("403","create file system error !");
        }
        String storage_id = (String) params.get("storage_id");
        Map<String, Object> filesystem_specs = new HashMap<>();
        List<Map<String, Object>> filesystemSpecsLists = new ArrayList<>();

        Double capacity = null;
        int count = 1;
        Double data_space = null;
        if (!StringUtils.isEmpty(storage_id) || !StringUtils.isEmpty(storage_pool_id)) {
            data_space = getDataspaceOfStoragepool(null, storage_pool_id, storage_id);
        }
        //目前一个nfs 对应一个fs (一对多通用)
        String filesystemSpecs = (String) params.get("filesystem_specs");
        List<Map<String, Object>> filesystemSpecsList = gson.fromJson(filesystemSpecs, List.class);
        for (int i = 0; i < filesystemSpecsList.size(); i++) {
            Map<String, Object> filesystemSpec = filesystemSpecsList.get(i);
            Object objCapacity = filesystemSpec.get("capacity");
            if (objCapacity != null) {
                capacity = Double.valueOf(objCapacity.toString());
                if (Double.compare(capacity * count, data_space) > 1) {
                    capacity = data_space / count;
                }
                filesystem_specs.put("capacity", capacity);
                filesystem_specs.put("name", filesystemSpec.get("name"));
                filesystem_specs.put("count", count);
                filesystem_specs.put("start_suffix", filesystemSpec.get("start_suffix"));
            }
            filesystemSpecsLists.add(filesystem_specs);
        }
        params.put("filesystem_specs",filesystemSpecsLists);
        ResponseEntity<String> responseEntity = dmeAccessService.access(API_FS_CREATE, HttpMethod.POST , gson.toJson(params));
        int code = responseEntity.getStatusCodeValue();
        if (code != 202) {
            //resMap.put("code", code);
            //resMap.put("msg", "create file system error !");
            throw new DMEException("503","create file system error !");
        }
        String object = responseEntity.getBody();
        JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
        String task_id = jsonObject.get("task_id").getAsString();
        //resMap.put("data", task_id);
        return task_id;
    }
    //create nfs share
    private String createNfsShare(Map<String,Object> params) throws Exception {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 202);
        resMap.put("msg", "create nfs share success !");
        LOG.info("{"+params+"}");
        ResponseEntity<String> responseEntity = dmeAccessService.access(API_NFSSHARE_CREATE, HttpMethod.POST, gson.toJson(params));
        int code = responseEntity.getStatusCodeValue();
        if (code != 202) {
            //resMap.put("code", code);
            //resMap.put("msg", "create nfs share error !");
            //return resMap;
            throw new DMEException("503","create nfs share error !");
        }
        String object = responseEntity.getBody();
        JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
        //resMap.put("data", ToolUtils.jsonToStr(jsonObject.get("task_id")));
        return ToolUtils.jsonToStr(jsonObject.get("task_id"));
    }
    //get oriented storage pool data_space
    private Double getDataspaceOfStoragepool(String storage_pool_name,String pool_id,String storage_id) throws Exception {
        Double data_space = 0.0;
        String className = "SYS_StoragePool";
        String url = API_STORAGEPOOL_LIST + className + "?storageDeviceId=" + storage_id;

        Map<String, String> reqBody = new HashMap<>();
        if (!StringUtils.isEmpty(pool_id)) {
            reqBody.put("pool_id", pool_id);
            url = url + " and pool_id=" + pool_id;
        }
        if (!StringUtils.isEmpty(storage_pool_name)) {
            reqBody.put("storage_pool_name", storage_pool_name);
            url = url + " and storage_pool_name=" + storage_pool_name;
        }
        reqBody.put("storage_id",storage_id);
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET,null);
        LOG.info("url:{" + API_STORAGEPOOL_LIST + "},responseEntity:" + responseEntity);
        int code = responseEntity.getStatusCodeValue();
        if (code == 200) {
            String object = responseEntity.getBody();
            JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("objList").getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                //可用空间=总容量-已用容量
                Double total_capacity =ToolUtils.jsonToDou(element.get("totalCapacity"),0.0);
                Double consumed_capacity = ToolUtils.jsonToDou(element.get("usedCapacity"), 0.0);
                if (Double.max(total_capacity,consumed_capacity)==total_capacity) {
                    data_space = total_capacity - consumed_capacity;
                }
            }
        }
        return data_space;
    }

    private String updateFileSystem(Map<String,Object> params,String file_system_id) throws Exception {

        //Map<String, Object> resMap = new HashMap<>();
       // resMap.put("code", 202);
        //resMap.put("msg", "update nfs datastore success !");

        if (StringUtils.isEmpty(file_system_id)) {
           // resMap.put("code", 403);
            //resMap.put("msg", "update nfs datastore error !");
            //return resMap;
        }
        String url = API_FS_UPDATE + "/" + file_system_id;

        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.PUT, gson.toJson(params));
        int code = responseEntity.getStatusCodeValue();
        if (code!=202) {
            //resMap.put("code", code);
            //resMap.put("msg", "update nfs datastore error !");
            //return resMap;
            throw new DMEException("503","update nfs datastore error !");
        }
        String object = responseEntity.getBody();
        JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
        //resMap.put("data", ToolUtils.jsonToStr(jsonObject.get("task_id")));
        return ToolUtils.jsonToStr(jsonObject.get("task_id"));
    }

    private String getFsIdByName(String fsname) throws Exception {
        String fsId = "";
        Map<String, String> reqMap = new HashMap<>();
        if (!StringUtils.isEmpty(fsname)) {
            reqMap.put("name", fsname);
            ResponseEntity<String> responseEntity = dmeAccessService.access(API_FS_QUERYONE, HttpMethod.POST, gson.toJson(reqMap));
            if (responseEntity.getStatusCodeValue() == 200) {
                String object = responseEntity.getBody();
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    String name = ToolUtils.jsonToStr(element.get("name"));
                    if (fsname.equals(name)) {
                        fsId = ToolUtils.jsonToStr(element.get("id"));
                        break;
                    }
                }
            }
        }
        return fsId;
    }

    private String getShareIdByName(String shareName,String fsname) throws Exception {
        String shareId = "";
        Map<String, String> reqMap = new HashMap<>();
        if (!StringUtils.isEmpty(shareName) && !StringUtils.isEmpty(fsname)) {
            reqMap.put("name", shareName);
            reqMap.put("fs_name", fsname);
            ResponseEntity<String> responseEntity = dmeAccessService.access(API_SHARE_QUERYONE, HttpMethod.POST, gson.toJson(reqMap));
            if (responseEntity.getStatusCodeValue() == 200) {
                String object = responseEntity.getBody();
                JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("nfs_share_info_list").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject element = jsonElement.getAsJsonObject();
                    if (ToolUtils.jsonToStr(element.get("name")).equals(shareName)) {
                        shareId = ToolUtils.jsonToStr(element.get("id"));
                        break;
                    }
                }

            }
        }
        return shareId;
    }

    private Map<String,String> getMgmtByStorageId(String storage_id,String current_port_id) throws DMEException {
        Map<String, String> respMap = new HashMap<>();
        String mgmt = "";
        String logicName = "";
        List<LogicPorts> logicPort = dmeStorageService.getLogicPorts(storage_id);
       // List<LogicPorts> logicPort = (List)logicPorts.get("data");
        if (logicPort != null) {
            JsonArray jsonArray = new JsonParser().parse(gson.toJson(logicPort)).getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject element = jsonElement.getAsJsonObject();
                String current_port_id1 = element.get("homePortId").getAsString();
                if (!StringUtils.isEmpty(current_port_id) && current_port_id1.equals(current_port_id)) {
                    mgmt = ToolUtils.jsonToStr(element.get("mgmtIp"));
                    logicName = ToolUtils.jsonToStr(element.get("name"));
                    break;
                }
            }
        }
        respMap.put("mgmt", mgmt);
        respMap.put("logicPortName", logicName);
        return respMap;
    }

    private Map<String, String> getOrientedFs(String file_system_id) throws Exception {

        Map<String, String> resMap = new HashMap<>();
        resMap.put("code", "200");
        resMap.put("msg", "list filesystem success!");

        String url = API_FILESYSTEMS_DETAIL + "/" + file_system_id;

        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        int code = responseEntity.getStatusCodeValue();
        if (code != 200) {
            resMap.put("code", String.valueOf(code));
            resMap.put("msg", "list filesystem success!");
            return resMap;
        }
        String object = responseEntity.getBody();
        JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
        FileSystem fileSystem = new FileSystem();
        fileSystem.setCapacity(Double.valueOf(jsonObject.get("capacity").getAsString()));
        fileSystem.setAllocateQuotaInPool(ToolUtils.jsonToDou(jsonObject.get("allocate_quota_in_pool")));
        fileSystem.setAvailableCapacity(ToolUtils.jsonToDou(jsonObject.get("available_capacity")));
        fileSystem.setMinSizeFsCapacity(ToolUtils.jsonToDou(jsonObject.get("min_size_fs_capacity")));
        fileSystem.setStorageId(ToolUtils.jsonToStr(jsonObject.get("storage_id")));
        fileSystem.setStoragePoolName(ToolUtils.jsonToStr(jsonObject.get("storage_pool_name")));
        fileSystem.setAllocType(ToolUtils.jsonToStr(jsonObject.get("alloc_type")));

        resMap.put("data", gson.toJson(fileSystem));
        return resMap;

    }

    private void saveNfsInfoToDmeVmwareRelation(String params, String current_port_id, String logicPortName, String storage_id,
                                                String fsId, String share_name, String share_id, String fs_name) {

        DmeVmwareRelation datastoreInfo = gson.fromJson(params, DmeVmwareRelation.class);
        datastoreInfo.setLogicPortId(current_port_id);
        datastoreInfo.setLogicPortName(logicPortName);
        datastoreInfo.setFsId(fsId);
        datastoreInfo.setFsName(fs_name);
        datastoreInfo.setShareName(share_name);
        datastoreInfo.setShareId(share_id);
        List<DmeVmwareRelation> dmeVmwareRelations = new ArrayList<>();
        dmeVmwareRelations.add(datastoreInfo);
        dmeVmwareRalationDao.save(dmeVmwareRelations);
    }

    private static ResponseEntity<String> access(String url, HttpMethod method, String requestBody) throws Exception {

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

    @Override
    public Map<String, Object> getEditNfsStore(String storeObjectId) throws DMEException {
        List<String> fsIds = dmeVmwareRalationDao.getFsIdsByStorageId(storeObjectId);
        Map<String, Object> summaryMap=vcsdkUtils.getDataStoreSummaryByObjectId(storeObjectId);
        Map<String, Object> resultMap=new HashMap<>();
        resultMap.put("nfsName", String.valueOf(summaryMap.get("name")));
        String fsname = "";
        if (fsIds.size()<=0){
            throw new DMEException("没有对应的文件系统");
        }
        for (int i = 0; i < fsIds.size(); i++) {
            String file_system_id = fsIds.get(i);
            if(StringUtils.isEmpty(file_system_id)){
                continue;
            }
            String url = StringUtil.stringFormat(DmeConstants.DEFAULT_PATTERN, DmeConstants.DME_NFS_FILESERVICE_DETAIL_URL,
                    "file_system_id", file_system_id);
            ResponseEntity<String> responseTuning = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseTuning.getStatusCodeValue() / 100 == 2) {
                JsonObject fsDetail = gson.fromJson(responseTuning.getBody(), JsonObject.class);
                resultMap.put("fsName",ToolUtils.jsonToStr(fsDetail.get("name")));
                fsname=ToolUtils.jsonToStr(fsDetail.get("name"));
                resultMap.put("fileSystemId",file_system_id);
                JsonObject json = fsDetail.get("capacity_auto_negotiation").getAsJsonObject();
                //fileSystemDetail.setCapacityAutonegotiation(capacityAutonegotiation);
                resultMap.put("autoSizeEnable",ToolUtils.jsonToBoo(json.get("auto_size_enable")));
                boolean isThin=("thin".equalsIgnoreCase(ToolUtils.jsonToStr(fsDetail.get("alloc_type"))))?true:false;
                resultMap.put("thin",isThin);

                JsonObject tuning = fsDetail.get("tuning").getAsJsonObject();

                resultMap.put("compressionEnabled",ToolUtils.jsonToBoo(tuning.get("compression_enabled")));
                resultMap.put("deduplicationEnabled",ToolUtils.jsonToBoo(tuning.get("deduplication_enabled")));
                String smart_qos = ToolUtils.jsonToStr(tuning.get("smart_qos"));
                if (!StringUtils.isEmpty(smart_qos)) {
                    resultMap.put("qosFlag",true);
                    JsonObject qos_policy = new JsonParser().parse(smart_qos).getAsJsonObject();
                    resultMap.put("maxBandwidth",ToolUtils.jsonToInt(qos_policy.get("max_bandwidth")));
                    resultMap.put("maxIops",ToolUtils.jsonToInt(qos_policy.get("max_iops")));
                    resultMap.put("latency",ToolUtils.jsonToInt(qos_policy.get("latency")));
                    resultMap.put("minBandwidth",ToolUtils.jsonToInt(qos_policy.get("min_bandwidth")));
                    resultMap.put("minIops",ToolUtils.jsonToInt(qos_policy.get("min_iops")));
                }else
                {
                    resultMap.put("qosFlag",false);
                }
            }
            break;
        }

        //根据存储ID 获取逻nfs_share_id
        String nfsShareId = dmeVmwareRalationDao.getShareIdByStorageId(storeObjectId);
        if (null==nfsShareId){
            throw new DMEException("没有对应的共享");
        }
        String url = StringUtil.stringFormat(DmeConstants.DEFAULT_PATTERN, DmeConstants.DME_NFS_SHARE_DETAIL_URL,
                "nfs_share_id", nfsShareId);
        ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
        if (responseEntity.getStatusCodeValue() / 100 != 2) {
            LOG.error("获取 NFS Share 信息失败！返回信息：{}", responseEntity.getBody());
            return null;
        }
        String resBody = responseEntity.getBody();
        JsonObject share = gson.fromJson(resBody, JsonObject.class);
        resultMap.put("shareName",ToolUtils.jsonToStr(share.get("name")));
        if ( String.valueOf(summaryMap.get("name")).equalsIgnoreCase(fsname)&&  ("/"+ summaryMap.get("name")).equalsIgnoreCase(ToolUtils.jsonToStr(share.get("name")))){
            resultMap.put("sameName",true);
        }else
        {
            resultMap.put("sameName",false);
        }


        return resultMap;
    }

}
