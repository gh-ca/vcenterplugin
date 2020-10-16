package com.dmeplugin.dmestore.mvc;

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
    private final String API_RESP_CODE = "code";
    private Gson gson=new Gson();

    @Autowired
    private NfsOperationService nfsOperationService;


    /**
     *      fs_param:
     *      storage_pool_id string 存储池id
     *      current_port_id String 当前逻辑端口id
     *      storage_id  str  存储设备id 必
     *      pool_raw_id str  存储池在指定存储设备上的id 必
     *      exportPath str 文件路径（和共享路径相同） 必
     *      String nfsName 名称  必
     *      String accessMode  读写权限 必 （ "readWrite" : "readOnly"）
     *      String type  NFS版本 （标准：NFS / NFS41） 必
     *      filesystem_specs  array  文件系统规格属性[{
     *           capacity double 该规格文件系统容量，单位GB  必
     *           name str  文件系统名称 必
     *           count int 该规格文件系统数量  必 （count 固定：1）
     *      }]
     *       capacity_autonegotiation : {
     *           auto_size_enable  boolean 自动调整容量开关。 false: 关闭；true：打开。默认打开
     *        },
     *      tuning属性 （高级属性设置）{
     *           deduplication_enabled  boolean 重复数据删除。默认关闭
     *           compression_enabled  boolean 数据压缩。默认关闭
     *           allocation_type str 文件系统分配类型，取值范围 thin，thick。默认为thin
     *           }
     *      qos_policy 属性(开启后 需要参数){
     *           max_bandwidth int 最大带宽，在控制上限的时候有效,与minbandwidth,miniops互斥
     *           max_iops int 最大iops，在控制上限的时候有效,与minbandwidth,miniops互斥
     *           min_bandwidth  int 最小带宽，在保护下限的时候有效，与maxbandwidth,maxiops互斥
     *           min_iops  int 最小iops，在保护下限的时候有效, 与maxbandwidth,maxiops互斥
     *           latency int 时延，单位ms 仅保护下限支持该参数
     *           }
     *      create_nfs_share_param   创建NFS共享参数 必 {
     *           name Str  共享别名
     *           share_path str 共享路径 必
     *         }
     *      nfs_share_client_addition  array  NFS共享客户端 [{
     *          name str   客户端IP或主机名或网络组名(vkernel ip) 必
     *          objectId String  主机objectId
     *          }]
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
        filesystemSpec.put("capacity", params.get("size"));
        filesystemSpec.put("count", 1);
        if (sameName) {
            createNfsShareParam.put("name", "/"+nfsName);
            createNfsShareParam.put("share_path", "/" + nfsName + "/");
            filesystemSpec.put("name", nfsName);
            param.put("exportPath", "/" + nfsName);
        } else {
            createNfsShareParam.put("name", "/"+params.get("shareName"));
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
                if ("up".equals(contolPolicy)) {
                    qosPolicy.put("max_bandwidth", params.get("maxBandwidth"));
                    qosPolicy.put("max_iops", params.get("maxIops"));
                } else if ("low".equals(contolPolicy)) {
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
            capacityAutonegotiation.put("auto_size_enable", params.get("autoSizeEnable"));
        } else {
            tuning.put("allocation_type", "thin");
            tuning.put("compression_enabled", false);
            tuning.put("deduplication_enabled", false);
            capacityAutonegotiation.put("auto_size_enable", false);
        }
        param.put("tuning", tuning);
        Map<String,Object> resMap = nfsOperationService.createNfsDatastore(param);
        if (null != resMap && null != resMap.get(API_RESP_CODE) && resMap.get(API_RESP_CODE).equals(HttpStatus.OK.value())) {
            return success(resMap);
        }
        return failure(gson.toJson(resMap));
    }

    /** request params
     *  {
     *      String dataStoreObjectId
     *      String nfsShareName nfsShare的名字 必
     *      String nfsName nfsDataStore名字 必
     *
     *      fs params:
     *      file_system_id String 文件系统唯一标识 必
     *      capacity_autonegotiation 自动扩缩容 相关属性{
     *          auto_size_enable  boolean 自动调整容量开关。 false: 关闭；true：打开。默认false
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
     *       nfs_share_id string NFS共享的唯一标识 必  (id)
     *  }
     *
     * @param params
     * @return
     */
    @PostMapping("/updatenfsdatastore")
    @ResponseBody
    public ResponseBodyBean updateNfsDatastore(@RequestBody Map<String,Object> params){

        Map<String,Object> resMap = nfsOperationService.updateNfsDatastore(params);
        if (null != resMap && null != resMap.get(API_RESP_CODE) && resMap.get(API_RESP_CODE).equals(HttpStatus.ACCEPTED.value())) {
            return success(resMap);
        }
        return failure(gson.toJson(resMap));
    }

    /**
     * {
     *     file_system_id string 文件系统唯一标识 必
     *     is_expand boolean 扩容 is_expand=true  缩容 is_expand = false 必
     *     capacity double 该规格文件系统容量，单位GB 必
     * }
     * @param params
     * @return
     */
    @PutMapping("/changenfsdatastore")
    @ResponseBody
    public ResponseBodyBean changeNfsCapacity(@RequestBody Map<String,Object> params){

        ResponseBodyBean responseBodyBean = nfsOperationService.changeNfsCapacity(params);
        if (null != responseBodyBean && null != responseBodyBean.getCode() && "202".equals(responseBodyBean.getCode())) {
            return success(responseBodyBean);
        }
        return failure(gson.toJson(responseBodyBean));
    }
}
