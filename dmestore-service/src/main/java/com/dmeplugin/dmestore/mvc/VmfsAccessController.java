package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.model.VmfsDataInfo;
import com.dmeplugin.dmestore.services.VmfsAccessService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/accessvmfs")
public class VmfsAccessController extends BaseController{
    public static final Logger LOG = LoggerFactory.getLogger(VmfsAccessController.class);

    @Autowired
    private Gson gson = new Gson();
    @Autowired
    private VmfsAccessService vmfsAccessService;

    /*
   * Access vmfs
   * return: Return execution status and information
   *         code:Status code 200 or 503
   *         message:Information
   *         data: List<VmfsDataInfo>，including vmfs's data infos
   */
    @RequestMapping(value = "/listvmfs", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean listVmfs()
            throws Exception {
        LOG.info("accessvmfs/listvmfs");
        String failureStr = "";
        try {
            List<VmfsDataInfo> lists = vmfsAccessService.listVmfs();
            LOG.info("listvmfs lists==" + gson.toJson(lists));
            return success(lists);
        }catch (Exception e){
            LOG.error("list vmfs failure:", e);
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }

    /*
  * Access vmfs performance"
  * return: Return execution status and information
  *         code:Status code 200 or 503
  *         message:Information
  *         data: List<VmfsDataInfo>，including vmfs's data infos
  */
    @RequestMapping(value = "/listvmfsperformance", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean listVmfsPerformance(@RequestParam("volumeIds") List<String> volumeIds)
            throws Exception {
        LOG.info("accessvmfs/listvmfsperformance volumeIds=="+gson.toJson(volumeIds));
        String failureStr = "";
        try {
            List<VmfsDataInfo> lists = vmfsAccessService.listVmfsPerformance(volumeIds);
            LOG.info("listvmfsperformance lists==" + gson.toJson(lists));
            return success(lists);
        }catch (Exception e){
            LOG.error("get vmfs performance failure:", e);
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }

    /*
   * Create vmfs
   * ServiceVolumeBasicParams对象包含如下属性
   * param str name: 名称 必
   * param int capacity: 容量，单位GB 必
   * param int count: 数量 必
   * param int start_suffix: 该规格卷的起始后缀编号
   * SchedulerHints对象包含如下属性
   * param boolean affinity: 是否开启亲和性
   * param str affinity_volume: 待亲和的卷id
   * ServiceVolumeMapping对象包含如下属性
   * param str host_id: 主机id，与hostgroup_id二选其一,不可同时存在
   * param str hostgroup_id: 主机组id，与host_id二选其一，不可同时存在

   * param str service_level_id: 服务等级id 必
   * param str project_id: 业务群组id
   * param str availability_zone: 可用分区id
   *
   * param str version: 版本
   * param int blockSize: 块大小，单位MB
   * param int spaceReclamationGranularity   空间回收粒度 单位K
   * param str spaceReclamationPriority: 空间回收优先权
   * param str host: 主机
   * param str cluster: 集群
   * return: Return execution status and information
   *         code:Status code 200 or 503
   *         message:Information
   *         data: Data，including task_id
   */
    @RequestMapping(value = "/createvmfs", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean createVmfs(@RequestBody Map<String, Object> params)
            throws Exception {
        LOG.info("accessvmfs/createvmfs=="+gson.toJson(params));
        String failureStr = "";
        try {
            vmfsAccessService.createVmfs(params);
            return success();
        }catch (Exception e){
            LOG.error("create vmfs failure:", e);
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }

    /*
   * Mount vmfs
   * param list<str> volume_ids: 卷id列表 必
   * param str host_id: 主机id 必
   * return: Return execution status and information
   *         code:Status code 200 or 503
   *         message:Information
   *         data: Data，including task_id
   */
    @RequestMapping(value = "/mountvmfs", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean mountVmfs(@RequestBody Map<String, Object> params)
            throws Exception {
        LOG.info("accessvmfs/mountvmfs=="+gson.toJson(params));
        String failureStr = "";
        try {
            vmfsAccessService.mountVmfs(params);
            return success();
        }catch (Exception e){
            LOG.error("mount vmfs failure:", e);
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }

    /*
   * Delete vmfs
   * param list<str> volume_ids: 卷id列表 必
   * param str host_id: 主机id 必
   * param str hostGroup_id: 主机id 必
   * return: Return execution status and information
   *         code:Status code 202 or 503
   *         message:Information
   *         data: Data，including task_id
   */
    @RequestMapping(value = "/deletevmfs", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean deleteVmfs(@RequestBody Map<String, Object> params) throws Exception {
        LOG.info("accessvmfs/deletevmfs==" + gson.toJson(params));
        String failureStr = "";
        try {
            vmfsAccessService.deleteVmfs(params);
            return success();
        } catch (Exception e) {
            LOG.error("delete vmfs failure:" + e);
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }
}
