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

    /**
     *
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

}
