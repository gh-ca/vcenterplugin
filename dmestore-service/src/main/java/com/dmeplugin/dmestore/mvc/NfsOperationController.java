package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.model.CapacityAutonegotiation;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.NfsOperationService;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/operatenfs")
@Api
public class NfsOperationController extends BaseController{

    public static final Logger LOG = LoggerFactory.getLogger(NfsOperationController.class);
    //QOS策略上限标识
    private final String CONTROL_UP = "up";
    //QOS策略下限标识
    private final String CONTROL_LOW = "low";
    private Gson gson=new Gson();

    @Autowired
    private NfsOperationService nfsOperationService;


    /**
     * {
     * storagId 存储设备id
     * storagePoolId 存储池id (storage_pool_id)
     * poolRawId 存储池在指定存储设备上的id（poolId）
     * currentPortId 逻辑端口id
     * nfsName  DataStoname
     * sameName false true 如果是false就传
     * shareName 共享名称
     * fsName 文件系统名称
     * size  ?待确认默认单位（界面可选。前端可转换）
     * type nfs协议版本   NFS以及NFS41
     * advance false true  true 是有高级选项
     * qosFlag qos策略开关 false true false关闭
     * contolPolicy 上下线选择标记  枚举值 up low
     * // up 取值如下
     * maxBandwidth
     * maxIops
     * //low取值
     * minBandwidth
     * minIops
     * latency
     * thin true  代表thin false代表thick
     * deduplicationEnabled 重删 true false
     * compressionEnabled 压缩 true false
     * autoSizeEnable 自动扩容 true false
     * vkernelIp 虚拟网卡ip
     * hostObjectId 挂载主机的Objectid
     * accessMode 挂载方式 只读 "readOnly" 读写 "readWrite
     * securityType NFS41的时候，安全类型AUTH_SYS,SEC_KRB5,SEC_KRB5I
     * }
     * @param params
     * @return
     */
    @PostMapping("/createnfsdatastore")
    @ResponseBody
    public ResponseBodyBean createNfsDatastore(@RequestBody Map<String,Object> params){

        LOG.info("url:{/operatenfs/createnfsdatastore},"+gson.toJson(params));
        //入参调整
        Map<String, Object> param = new HashMap<>(16);
        Map<String, Object> createNfsShareParam = new HashMap<>(16);
        List<Map<String, Object>> nfsShareClientAdditions = new ArrayList<>(10);
        Map<String, Object> nfsShareClientAddition = new HashMap<>(16);
        List<Map<String, Object>> filesystemSpecs = new ArrayList<>(10);
        Map<String, Object> filesystemSpec = new HashMap<>(16);
        param.put("storage_id", params.get("storagId"));
        param.put("storage_pool_id", params.get("storagePoolId"));
        param.put("pool_raw_id", params.get("poolRawId"));
        param.put("current_port_id", params.get("currentPortId"));
        param.put("accessMode", params.get("accessMode"));
        Object nfsName = params.get("nfsName");
        param.put("nfsName", nfsName);
        Boolean sameName = (Boolean)params.get("sameName");
        param.put("type", params.get("type"));
        param.put("securityType", params.get("securityType"));
        filesystemSpec.put("capacity", params.get("size"));
        filesystemSpec.put("count", 1);
        if (sameName) {
            createNfsShareParam.put("name", "/" +nfsName);
            createNfsShareParam.put("share_path", "/" + nfsName + "/");
            filesystemSpec.put("name", nfsName);
            param.put("exportPath", "/" + nfsName);
        } else {
            createNfsShareParam.put("name", "/" +params.get("shareName"));
            createNfsShareParam.put("share_path", "/" + params.get("fsName") + "/");
            filesystemSpec.put("name", params.get("fsName"));
            param.put("exportPath", "/" + params.get("fsName"));
        }
        filesystemSpecs.add(filesystemSpec);
        param.put("filesystem_specs", filesystemSpecs);
        nfsShareClientAddition.put("name", params.get("vkernelIp"));
        nfsShareClientAddition.put("objectId", params.get("hostObjectId"));
        createNfsShareParam.put("character_encoding", "utf-8");
        param.put("create_nfs_share_param", createNfsShareParam);
        Boolean advance = (Boolean)params.get("advance");
        nfsShareClientAdditions.add(nfsShareClientAddition);
        param.put("nfs_share_client_addition", nfsShareClientAdditions);
        Map<String, Object> tuning = new HashMap<>(16);
        Map<String, Object> capacityAutonegotiation = new HashMap<>(16);
        if (advance) {
            Boolean qosFlag = (Boolean) params.get("qosFlag");
            if (qosFlag) {
                Map<String, Object> qosPolicy = new HashMap<>(16);
                String contolPolicy = (String) params.get("contolPolicy");
                if (CONTROL_UP.equals(contolPolicy)) {
                    qosPolicy.put("max_bandwidth", params.get("maxBandwidth"));
                    qosPolicy.put("max_iops", params.get("maxIops"));
                } else if (CONTROL_LOW.equals(contolPolicy)) {
                    qosPolicy.put("min_bandwidth", params.get("minBandwidth"));
                    qosPolicy.put("min_iops", params.get("minIops"));
                    qosPolicy.put("latency", params.get("latency"));
                }
                param.put("qos_policy", qosPolicy);
            }
            Boolean thin = (Boolean) params.get("thin");
            if (thin) {
                tuning.put("allocation_type", "thin");
            } else {
                tuning.put("allocation_type", "thick");
            }
            tuning.put("compression_enabled", params.get("compressionEnabled"));
            tuning.put("deduplication_enabled", params.get("deduplicationEnabled"));

            String capacitymode=(Boolean.parseBoolean(String.valueOf(params.get("autoSizeEnable")))? CapacityAutonegotiation.capacitymodeauto:CapacityAutonegotiation.capacitymodeoff);
            capacityAutonegotiation.put("capacity_self_adjusting_mode", capacitymode);
            capacityAutonegotiation.put("auto_size_enable", params.get("autoSizeEnable"));
        } else {
            tuning.put("allocation_type", "thin");
            tuning.put("compression_enabled", false);
            tuning.put("deduplication_enabled", false);
            capacityAutonegotiation.put("auto_size_enable", false);
            capacityAutonegotiation.put("capacity_self_adjusting_mode", CapacityAutonegotiation.capacitymodeoff);
        }
        param.put("tuning", tuning);
        param.put("capacity_autonegotiation",capacityAutonegotiation);
        try {
            nfsOperationService.createNfsDatastore(param);
            return success();
        } catch (DMEException e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }


    }

    /**
     * {
     *      "dataStoreObjectId":"urn:vmomi:Datastore:datastore-1060:674908e5-ab21-4079-9cb1-596358ee5dd1" ,
     *      "nfsName": "fs0001",
     *      "fileSystemId":"0C9A60E0A51C3AD38567C21B6881371C",
     *      "autoSizeEnable": true,
     *      "deduplicationEnabled": "false",
     *      "compressionEnabled": "false",
     *      "thin": "thin",
     *      "maxBandwidth": "1",
     *      "maxIops": "2"
     *      "minBandwidth"
     * 	    "minIops"
     * 	    "latency"
     *      "shareId":"70C9358F595B3AA5A1DB2464F72AF0DA"
     *      "advance"false true  true 是有高级选项
     *      "qosFlag"qos策略开关 false true false关闭
     *      "contolPolicy"上下线选择标记  枚举值 up low
     *
     * }
     * @param params
     * @return
     */
    @PostMapping("/updatenfsdatastore")
    @ResponseBody
    public ResponseBodyBean updateNfsDatastore(@RequestBody Map<String,Object> params){


        Map<String, Object> param = new HashMap<>(16);
        param.put("dataStoreObjectId", params.get("dataStoreObjectId"));
        param.put("nfsName", params.get("nfsName"));
        param.put("file_system_id", params.get("fileSystemId"));
        param.put("nfs_share_id", params.get("shareId"));
        Map<String, Object> capacityAutonegotiation = new HashMap<>(16);
        Object autoSizeEnable = params.get("autoSizeEnable");
        if (autoSizeEnable!=null) {
            capacityAutonegotiation.put("auto_size_enable", params.get("autoSizeEnable"));
            String capacitymode=(Boolean.parseBoolean(String.valueOf(params.get("autoSizeEnable")))? CapacityAutonegotiation.capacitymodeauto:CapacityAutonegotiation.capacitymodeoff);
            capacityAutonegotiation.put("capacity_self_adjusting_mode", capacitymode);
            param.put("capacity_autonegotiation",capacityAutonegotiation);
        }
        Map<String, Object> tuning = new HashMap<>(16);
        tuning.put("deduplication_enabled", params.get("deduplicationEnabled"));
        tuning.put("compression_enabled", params.get("compressionEnabled"));
        param.put("name", params.get("name"));
        Object thin = params.get("thin");
        if (thin != null) {
            if ((Boolean) thin ) {
                tuning.put("allocation_type", "thin");
            } else if (!(Boolean) thin){
                tuning.put("allocation_type", "thick");
            }
            param.put("tuning",tuning);
        }
        Boolean qosFlag = (Boolean)params.get("qosFlag");
        if (qosFlag) {
            Map<String, Object> qosPolicy = new HashMap<>(16);
            String contolPolicy = (String)params.get("contolPolicy");
            if (CONTROL_LOW.equals(contolPolicy)) {
                qosPolicy.put("min_bandwidth", params.get("minBandwidth"));
                qosPolicy.put("min_iops", params.get("minIops"));
                qosPolicy.put("latency", params.get("latency"));
            } else if (CONTROL_UP.equals(contolPolicy)) {
                qosPolicy.put("max_bandwidth", params.get("maxBandwidth"));
                qosPolicy.put("max_iops", params.get("maxIops"));
            }
            param.put("qos_policy", qosPolicy);
        }
        try {
            nfsOperationService.updateNfsDatastore(param);
            return success();
        } catch (DMEException e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }

    }

    /**
     * {
     *     fileSystemId string 文件系统唯一标识 必
     *     expand boolean 扩容 is_expand=true  缩容 is_expand = false 必
     *     capacity double 该规格文件系统容量，单位GB 必
     * }
     * @param params
     * @return
     */
    @PutMapping("/changenfsdatastore")
    @ResponseBody
    public ResponseBodyBean changeNfsCapacity(@RequestBody Map<String,Object> params){
        try {
            nfsOperationService.changeNfsCapacity(params);
            return success();
        } catch (DMEException e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }

    }


    /**
     * 通过storeobjectid来获取存储编译所需要的信息
     * @param storeObjectId  存储的objectid
     * @return 根据页面参数来确定的map
     */
    @GetMapping("/editnfsstore")
    @ResponseBody
    public ResponseBodyBean getEditNfsStore(@RequestParam(name = "storeObjectId")String storeObjectId) {
        try {
            return success(nfsOperationService.getEditNfsStore(storeObjectId));
        } catch (DMEException e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }


    }
}
