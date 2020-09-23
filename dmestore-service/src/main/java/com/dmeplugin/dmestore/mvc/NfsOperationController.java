package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.NfsOperationService;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/operatenfs")
@Api
public class NfsOperationController extends BaseController{

    public static final Logger LOG = LoggerFactory.getLogger(NfsOperationController.class);

    private Gson gson=new Gson();

    @Autowired
    private NfsOperationService nfsOperationService;


    /**
     *  fs_param:
     *      current_port_id String 当前逻辑端口id
     *      storage_id  str  存储设备id 必
     *      pool_raw_id str  存储池在指定存储设备上的id 必
     *      exportPath str 文件路径（和共享路径相同） 必
     *      String nfsName 名称  必
     *      String accessMode  读写权限 必
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
     *           application_scenario str 应用场景。database： 数据库；VM：虚拟机；user_defined：自定义。默认自定义
     *           block_size int 文件系统块大小，单位KB
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
     *          name str   客户端IP或主机名或网络组名 必
     *          accessval str 权限：read-only：只读， read/write：读写 必
     *          sync str 写入模式：synchronization：同步， asynchronization：异步 必
     *          all_squash str 权限限制：all_squash，no_all_squash 必
     *          root_squash str root权限限制：root_squash，no_root_squash 必
     *          }]
     * @param params
     * @return
     */
    @PostMapping("/createnfsdatastore")
    @ResponseBody
    public ResponseBodyBean createNfsDatastore(@RequestBody Map<String,String> params){

        LOG.info("url:{/operatenfs/createnfsdatastore},"+gson.toJson(params));
        Map<String,Object> resMap = nfsOperationService.createNfsDatastore(params);
        if (null != resMap && null != resMap.get("code") && resMap.get("code").equals(200)) {
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
    @PostMapping("/updatenfsdatastore")
    @ResponseBody
    public ResponseBodyBean updateNfsDatastore(@RequestBody Map<String,String> params){

        Map<String,Object> resMap = nfsOperationService.updateNfsDatastore(params);
        if (null != resMap && null != resMap.get("code") && resMap.get("code").equals(200)) {
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
    public ResponseBodyBean changeNfsCapacity(@RequestBody Map<String,String> params){

        ResponseBodyBean responseBodyBean = nfsOperationService.changeNfsCapacity(params);
        if (null != responseBodyBean && null != responseBodyBean.getCode() && responseBodyBean.getCode().equals("200")) {
            return success(responseBodyBean);
        }
        return failure(gson.toJson(responseBodyBean));
    }
}
