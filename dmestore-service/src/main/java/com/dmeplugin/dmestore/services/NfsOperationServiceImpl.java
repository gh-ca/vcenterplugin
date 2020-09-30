package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.dao.DmeVmwareRalationDao;
import com.dmeplugin.dmestore.entity.DmeVmwareRelation;
import com.dmeplugin.dmestore.model.*;
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

import javax.print.DocFlavor;
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
    private final String API_STORAGEPOOL_LIST = "/rest/storagemgmt/v1/storagepools/query";
    private final String API_FS_UPDATE = "/rest/fileservice/v1/filesystems";
    //private final String API_NFS_CHANGECAPACITY = "/rest/fileservice/v1/filesystems";
    private final String API_FILESYSTEMS_DETAIL = "/rest/fileservice/v1/filesystems/";

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
    public Map<String, Object> createNfsDatastore(Map<String, Object> params) {

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
            Object filesystem_specs = params.get("filesystem_specs");
            if (StringUtils.isEmpty(filesystem_specs)) {
                LOG.error("{filesystem_specs}={"+filesystem_specs+"}");
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
            }
            fsMap.put("pool_raw_id", pool_raw_id);
            fsMap.put("storage_id", storage_id);
            Object tuning = params.get("tuning");
            if (!StringUtils.isEmpty(tuning)) {
                fsMap.put("tuning", gson.toJson(tuning));
            }
            //可以挂载多个
            List<Map<String,String>> mounts = new ArrayList<>();
            Map<String, String> mount = new HashMap<>();
            List<String> reqNfsShareClientArrayAdditions = new ArrayList<>();
            Object nfs_share_client_addition = params.get("nfs_share_client_addition");
            List<Map<String, String>> nfsShareClientArrayAddition = (List<Map<String, String>>) nfs_share_client_addition;
            String share_name = "";
            if (nfs_share_client_addition != null) {
                Map<String, String> reqNfsShareClientArrayAddition = new HashMap<>();
                for (int i = 0; i < nfsShareClientArrayAddition.size(); i++) {
                    Map<String, String> shareClientHostMap = nfsShareClientArrayAddition.get(i);
                    if (shareClientHostMap != null && shareClientHostMap.size() > 0 && shareClientHostMap.get("objectId") != null) {
                        reqNfsShareClientArrayAddition.put("name", shareClientHostMap.get("name"));
                        reqNfsShareClientArrayAddition.put("accessval", shareClientHostMap.get("accessval"));
                        mount.put(shareClientHostMap.get("objectId"), shareClientHostMap.get("name"));
                    }
                    reqNfsShareClientArrayAdditions.add(gson.toJson(reqNfsShareClientArrayAddition));
                }
                mounts.add(mount);
                Object create_nfs_share_param = params.get("create_nfs_share_param");
                Map<String, String> create_nfs_share_params = null;
                if (!StringUtils.isEmpty(create_nfs_share_param)) {
                    create_nfs_share_params = gson.fromJson(gson.toJson(create_nfs_share_param), Map.class);
                    if (create_nfs_share_params != null && create_nfs_share_params.size() > 0) {
                        create_nfs_share_params.put("nfs_share_client_addition", gson.toJson(reqNfsShareClientArrayAdditions));
                        fsMap.put("create_nfs_share_param", gson.toJson(create_nfs_share_params));
                        share_name = create_nfs_share_params.get("name");
                        Object show_snapshot_enable = params.get("show_snapshot_enable");
                        if (!StringUtils.isEmpty(show_snapshot_enable)) {
                            create_nfs_share_params.put("show_snapshot_enable", gson.toJson(show_snapshot_enable));
                        }
                        nfsShareMap.put("create_nfs_share_param", gson.toJson(create_nfs_share_params));
                    }
                }
            }
            String storage_pool_id =(String) params.get("storage_pool_id");
            if (StringUtils.isEmpty(storage_pool_id)) {
                LOG.error("storage_pool_id={"+storage_pool_id+"}");
            }
            String task_id = "";
            Map<String, Object> fileSystem = createFileSystem(fsMap, storage_pool_id);
            int fsCode = ToolUtils.getInt(fileSystem.get("code"));
            if (fsCode == 202) {
                task_id = fileSystem.get("data").toString();
            }
            String nfsName = (String)params.get("nfsName");
            if (StringUtils.isEmpty(nfsName)) {
                LOG.error("nfsName={"+nfsName+"}");
            }

            String fsId = getInstanceIdByTaskId(task_id, fs_name);
            String share_id = null;
            if (!fsId.equals("")) {
                nfsShareMap.put("fs_id", fsId);
                Map<String, Object> nfsShare = createNfsShare(nfsShareMap);
                if (ToolUtils.getInt(nfsShare.get("code"))==202) {
                    String nfsShareTaskId = (String) nfsShare.get("data");
                    share_id = getInstanceIdByTaskId(nfsShareTaskId, share_name);
                    if (!StringUtils.isEmpty(share_id)) {
                        resMap.put("share_id", share_id);
                    }
                }
            }
            //query oriented logic portv by storage_id
            String serverHost = "";
            Object current_port_id = params.get("current_port_id");
            Map<String, Object> logicPorts = dmeStorageService.getLogicPorts(storage_id);
            LogicPorts logicPort = (LogicPorts)logicPorts.get("data");
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
            String exportPath = (String)params.get("exportPath");
            String type = (String)params.get("type");
            String accessMode = (String)params.get("accessMode");

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
            //save datastore info to DP_DME_VMWARE_RELATION
            DmeVmwareRelation datastoreInfo = gson.fromJson(result, DmeVmwareRelation.class);
            datastoreInfo.setLogicPortId((String) current_port_id);
            datastoreInfo.setLogicPortName(logicPort.getName());
            datastoreInfo.setStoreId(storage_id);
            datastoreInfo.setFsId(fsId);
            datastoreInfo.setShareName(share_name);
            datastoreInfo.setFsName(fs_name);
            datastoreInfo.setShareId(share_id);

            List<DmeVmwareRelation> dmeVmwareRelations = new ArrayList<>();
            dmeVmwareRelations.add(datastoreInfo);
            dmeVmwareRalationDao.save(dmeVmwareRelations);

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
     *      String nfsShareName nfsShare的名字 （不能修改share的名字）
     *      String nfsName nfsDataStore名字 必
     *
     *      fs params:
     *      file_system_id String 文件系统唯一标识 必
     *      capacity_autonegotiation 自动扩缩容 相关属性{
     *          auto_size_enable  boolean 自动调整容量开关。 false: 关闭；true：打开。默认打开
     *       },
     *       name String fs新名字 ()
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
     * @return
     */
    @Override
    public Map<String, Object> updateNfsDatastore(Map<String, Object> params) {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "update nfs datastore success !");

        if (params == null || params.size() == 0) {
            LOG.error("params error , please check it! reqParams=" + params);
            resMap.put("code", 403);
            resMap.put("msg", "params error , please check it!");
            return resMap;
        }
        String dataStoreObjectId = (String) params.get("dataStoreObjectId");
        String nfsName = (String) params.get("nfsName");
        if (StringUtils.isEmpty(dataStoreObjectId)||StringUtils.isEmpty(nfsName)) {
            LOG.error("params error , please check it! dataStoreObjectId=" + dataStoreObjectId);
            resMap.put("code", 403);
            resMap.put("msg", "params error , please check it!");
            return resMap;
        }
        //update fs
        Map<String, Object> fsReqBody = new HashMap<>();
        Map<String,Object> tuning = gson.fromJson(gson.toJson(params.get("tuning")), Map.class);
        Map<String,Object> qos_policy = gson.fromJson(gson.toJson(params.get("qos_policy")), Map.class);
        if (tuning!=null&&tuning.size()!=0) {
            if (qos_policy!=null&&qos_policy.size()!=0) {
                tuning.put("qos_policy", qos_policy);
            }
            fsReqBody.put("tuning", gson.toJson(tuning));
        }

        String file_system_id = (String) params.get("file_system_id");
        Boolean capacity_autonegotiation = (Boolean)params.get("capacity_autonegotiation");
        String name = (String) params.get("name");
        if (!StringUtils.isEmpty(file_system_id)) {
            fsReqBody.put("file_system_id", file_system_id);
        }
        if (capacity_autonegotiation != null) {
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
        } catch (Exception e) {
            LOG.error( "update nfs datastore error !",e);
            resMap.put("code", 503);
            resMap.put("msg", e.getMessage());
        }
        return resMap;
    }

    @Override
    public ResponseBodyBean changeNfsCapacity(Map<String, Object> reqParams) {

        ResponseBodyBean responseBodyBean = new ResponseBodyBean();
        responseBodyBean.setCode("202");
        responseBodyBean.setDescription("change nfs storage capacity success!");
        if (reqParams == null || reqParams.size() == 0) {
            responseBodyBean.setCode("202");
            responseBodyBean.setDescription("change nfs storage capacity error,params error!");
            return responseBodyBean;
        }
        Map<String,String> params = gson.fromJson(gson.toJson(reqParams), Map.class);
        String file_system_id = params.get("file_system_id");
        String is_expand = params.get("is_expand");
        String capacity = params.get("capacity");
        if (StringUtils.isEmpty(file_system_id) ||StringUtils.isEmpty(is_expand)||StringUtils.isEmpty(capacity)) {
            responseBodyBean.setCode("202");
            responseBodyBean.setDescription("change nfs storage capacity error,params error!");
            return responseBodyBean;
        }
        String url = API_FS_UPDATE + "/" + file_system_id;
        //查询指定fs拿对应的信息
        Integer code = 0;
        try {
            FileSystem fileSystem = null;
            Double data_space = null;
            Map<String, String> orientedFileSystem = getOrientedFs(file_system_id);
            if (orientedFileSystem.get("code").equals("200")) {
                String fs = orientedFileSystem.get("data");
                if (!StringUtils.isEmpty(fs)) {
                    fileSystem = gson.fromJson(fs, FileSystem.class);
                }
            }
            if (fileSystem != null) {
                String storage_id = fileSystem.getStorage_id();
                String storage_pool_name = fileSystem.getStorage_pool_name();
                //Double allocate_quota_in_pool = fileSystem.getAllocate_quota_in_pool();
                Double min_size_fs_capacity = fileSystem.getMin_size_fs_capacity();
                Double available_capacity = fileSystem.getAvailable_capacity();
                String alloc_type = fileSystem.getAlloc_type();
                Double currentCapacity = fileSystem.getCapacity();
                Integer capacity_usage_ratio = fileSystem.getCapacity_usage_ratio();
                //查询存储池详情
                Map<String, Object> storagePoolMap = getDataspaceOfStoragepool(storage_pool_name, null, storage_id);
                if (storagePoolMap.get("code").toString().equals("200")) {
                    if (storagePoolMap.get("data_space") != null) {
                        data_space = Double.valueOf(storagePoolMap.get("data_space").toString());
                    }
                }
                Double exchangedCapacity = null;
                Double changeCapacity = Double.valueOf(capacity);
                if (Boolean.valueOf(is_expand)) {
                    //扩容
                    if (data_space != null && Double.compare(Double.valueOf(capacity), data_space) > 1) {
                        LOG.info("扩容量超出存储池可用容量，将当前存储池可用容量当做扩容量!");
                        changeCapacity = data_space;
                        exchangedCapacity = changeCapacity + currentCapacity;
                    }
                } else {
                    //缩容
                    if (!StringUtils.isEmpty(alloc_type) && alloc_type.equals("thin")) {
                        //thin 分配策略缩容
                        // 该文件系统总容量-可用容量-文件系统能缩容的最小空间=实际可用缩小容量与变化量进行比较
                        if (Double.compare(changeCapacity, currentCapacity - available_capacity - min_size_fs_capacity) > 1) {
                            //changeCapacity = currentCapacity - available_capacity - min_size_fs_capacity;
                            LOG.info("thin策略：nfs预计缩容到Thin文件系统能缩容的最小空间!");
                            exchangedCapacity = min_size_fs_capacity;
                        } else {
                            exchangedCapacity = currentCapacity - changeCapacity;
                        }
                    } else {
                        //thick 分配策略缩容
                        if (Double.compare(currentCapacity * capacity_usage_ratio, currentCapacity - changeCapacity) > 1) {
                            exchangedCapacity = currentCapacity * capacity_usage_ratio;
                        } else {
                            exchangedCapacity = currentCapacity - changeCapacity;
                        }
                    }
                }
                Map<String, String> reqParam = new HashMap<>();
                reqParam.put("file_system_id", file_system_id);
                reqParam.put("capacity", gson.toJson(exchangedCapacity));
                ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.PUT, gson.toJson(reqParam));
                code = responseEntity.getStatusCodeValue();
                if (code != 200) {
                    responseBodyBean.setCode(code.toString());
                    responseBodyBean.setDescription("expand nfs storage capacity error!");
                    return responseBodyBean;
                }
                String obgect = responseEntity.getBody();
                JsonObject jsonObject = new JsonParser().parse(obgect).getAsJsonObject();
                String task_id = jsonObject.get("task_id").getAsString();
                responseBodyBean.setData("task_id:"+task_id);
            }
        } catch (Exception e) {
            LOG.error("change nfs storage capacity error!",e);
            responseBodyBean.setCode(gson.toJson(code));
            responseBodyBean.setDescription("change nfs storage capacity error!"+e.getMessage());
        }
        return responseBodyBean;
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
            Map<String, Object> resBody = getDataspaceOfStoragepool(null, storage_pool_id,storage_id);
            if (ToolUtils.getInt(resBody.get("code")) == 200) {
                data_space = Double.valueOf(resBody.get("data_space").toString());
                if (freeCapacity == null && data_space != null) {
                    freeCapacity = data_space;
                }
            }
        }
        List<Map<String, Object>> filesystemSpecsList = new ArrayList<>();
        //目前一个nfs 对应一个fs (一对多通用)
        for (int i=0;i<filesystemSpecsList.size();i++) {
            Map<String, Object> filesystemSpec = filesystemSpecsList.get(i);
            Object objCapacity = filesystemSpec.get("capacity");
            Object fsNum = filesystemSpec.get("count");
            if (objCapacity != null && fsNum != null) {
                capacity = Double.valueOf(objCapacity.toString());
                count = ToolUtils.getInt(fsNum);
                if (Double.compare(capacity * count, freeCapacity) > 1) {
                    capacity = freeCapacity / count;
                } else {
                    freeCapacity -= capacity * count;
                }
                filesystem_specs.put("capacity", capacity);
                filesystem_specs.put("name", filesystemSpec.get("name"));
                filesystem_specs.put("count", filesystemSpec.get("count"));
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
    private Map<String,Object> createNfsShare(Map<String,String> params) throws Exception {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 202);
        resMap.put("msg", "create nfs share success !");
        //todo 获取taskDetailInfo中的资源id
        //注意：需要先确定任务完成状态，在完成状态之下才能拿到对应的id, 中间存在延时问题。
        ResponseEntity<String> responseEntity = dmeAccessService.access(API_NFSSHARE_CREATE, HttpMethod.POST, gson.toJson(params));
        int code = responseEntity.getStatusCodeValue();
        if (code != 202) {
            resMap.put("code", code);
            resMap.put("msg", "create nfs share error !");
            return resMap;
        }
        String object = responseEntity.getBody();
        JsonObject jsonObject = new JsonParser().parse(object).getAsJsonObject();
        resMap.put("data", ToolUtils.jsonToStr(jsonObject.get("task_id")));
        return resMap;
    }
    //get oriented storage pool data_space
    private Map<String,Object> getDataspaceOfStoragepool(String storage_pool_name,String pool_id,String storage_id) throws Exception {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "get oriented data space of storage pool success!");

        Map<String, String> reqBody = new HashMap<>();
        if (!StringUtils.isEmpty(pool_id)) {
                reqBody.put("pool_id", pool_id);
        }
        if (!StringUtils.isEmpty(storage_pool_name)) {
            reqBody.put("storage_pool_name", storage_pool_name);
        }
        reqBody.put("storage_id",storage_id);

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

    private Map<String,Object> updateFileSystem(Map<String,Object> params) throws Exception {

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 202);
        resMap.put("msg", "update nfs datastore success !");

        String file_system_id = (String) params.get("file_system_id");
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
        resMap.put("data", ToolUtils.jsonToStr(jsonObject.get("task_id")));
        return resMap;
    }

    private Map<String,Object> updateNfsShare(Map<String,String> params){

        return new HashMap<>();
    }

    private String getInstanceIdByTaskId(String task_id,String instanceName) throws Exception {

        String fsId = "";
        if (!StringUtils.isEmpty(task_id)) {
            TaskDetailInfo taskDetailInfo = taskService.queryTaskById(task_id);
            List<TaskDetailResource> resources = taskDetailInfo.getResources();
            for (TaskDetailResource taskDetail : resources) {
                if (taskDetail.getName().equals(instanceName)) {
                    fsId = taskDetail.getId();
                    break;
                }
            }
        }
        return fsId;
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
        fileSystem.setAllocate_quota_in_pool(Double.valueOf(jsonObject.get("allocate_quota_in_pool").getAsString()));
        fileSystem.setAllocate_quota_in_pool(Double.valueOf(jsonObject.get("available_capacity").getAsString()));
        fileSystem.setAllocate_quota_in_pool(Double.valueOf(jsonObject.get("min_size_fs_capacity").getAsString()));
        fileSystem.setAllocate_quota_in_pool(Double.valueOf(jsonObject.get("min_size_fs_capacity").getAsString()));
        fileSystem.setStorage_id(jsonObject.get("storage_id").getAsString());
        fileSystem.setStorage_id(jsonObject.get("storage_pool_name").getAsString());

        resMap.put("data", gson.toJson(fileSystem));
        return resMap;

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
}
