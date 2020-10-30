package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.model.VmfsDataInfo;
import com.dmeplugin.dmestore.model.VmfsDatastoreVolumeDetail;
import com.dmeplugin.dmestore.services.VmfsAccessService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @ClassName: VmfsAccessController
 * @Company: GH-CA
 * @author: yy
 * @create: 2020-09-02
 **/
@RestController
@RequestMapping(value = "/accessvmfs")
public class VmfsAccessController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(VmfsAccessController.class);


    private Gson gson = new Gson();
    @Autowired
    private VmfsAccessService vmfsAccessService;


    /**
     * Access vmfs
     *
     * @return: ResponseBodyBean
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
        } catch (Exception e) {
            LOG.error("list vmfs failure:", e);
            failureStr = "list vmfs failure:" + e.toString();
        }
        return failure(failureStr);
    }

    /**
     * Access vmfs performance
     *
     * @param volumeIds volumes id
     * @return: ResponseBodyBean
     */
    @RequestMapping(value = "/listvmfsperformance", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean listVmfsPerformance(@RequestParam("volumeIds") List<String> volumeIds)
            throws Exception {
        LOG.info("accessvmfs/listvmfsperformance volumeIds==" + gson.toJson(volumeIds));
        String failureStr = "";
        try {
            List<VmfsDataInfo> lists = vmfsAccessService.listVmfsPerformance(volumeIds);
            LOG.info("listvmfsperformance lists==" + gson.toJson(lists));
            return success(lists);
        } catch (Exception e) {
            LOG.error("get vmfs performance failure:", e);
            failureStr = "get vmfs performance failure:" + e.toString();
        }
        return failure(failureStr);
    }

    /**
     * Create vmfs include:
     * ServiceVolumeBasicParams对象包含如下属性
     * param str name: 名称 必 vmfs stor的名称
     * param str volumeName: 名称 必 卷的名称
     * param int capacity: 容量，单位GB 必
     * param int count: 数量 必
     * param str service_level_id: 服务等级id 若未选择服务等级，可选择存储设备、存储池、设置QoS、Thin、Workload
     * param str service_level_name; 服务等级名称
     * param int version: 版本
     * param int blockSize: 块大小，单位KB
     * param int spaceReclamationGranularity   空间回收粒度 单位K
     * param str spaceReclamationPriority: 空间回收优先权 low->低
     * param str host: 主机  必  与cluster二选其一,不可同时存在
     * param str hostId: 主机
     * param str cluster: 集群 必 与host二选其一,不可同时存在
     * param str clusterId: 集群
     * param str storage_id 存储设备id
     * param str pool_raw_id 卷所属存储池在存储设备上的id
     * param integer workload_type_id 应用类型id
     * param str alloctype 卷分配类型，取值范围 thin，thick
     * 卷qos属性
     * param str  control_policy 控制策略  0：保护IO下限，1：控制IO上限
     * param integer  latency 时延，单位ms
     * param integer  maxbandwidth 最大带宽
     * param integer  maxiops 最大iops
     * param integer  minbandwidth 最小带宽
     * param integer  miniops 最小iops
     * param str qosname Smart QoS名称
     *
     * @param params include Parameters above
     * @return Return ResponseBodyBean
     * @return: ResponseBodyBean
     */
    @RequestMapping(value = "/createvmfs", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean createVmfs(@RequestBody Map<String, Object> params)
            throws Exception {
        LOG.info("accessvmfs/createvmfs==" + gson.toJson(params));
        String failureStr = "";
        try {
            vmfsAccessService.createVmfs(params);
            return success(null, "Create vmfs success");
        } catch (Exception e) {
            LOG.error("create vmfs failure:", e);
            failureStr = "create vmfs failure:" + e.toString();
        }
        return failure(failureStr);
    }

    /**
     * Mount vmfs include
     * param list<str> dataStoreObjectIds: datastore object id列表 必
     * param str host: 主机名称 必 （主机与集群二选一）
     * param str hostId: 主机
     * param str cluster: 集群名称 必（主机与集群二选一）
     * param str clusterId: 集群
     *
     * @param params: include dataStoreObjectIds,host,hostId,cluster,clusterId
     * @return: ResponseBodyBean
     */
    @RequestMapping(value = "/mountvmfs", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean mountVmfs(@RequestBody Map<String, Object> params)
            throws Exception {
        LOG.info("accessvmfs/mountvmfs==" + gson.toJson(params));
        String failureStr = "";
        try {
            vmfsAccessService.mountVmfs(params);
            return success(null, "Mount vmfs success");
        } catch (Exception e) {
            LOG.error("mount vmfs failure:", e);
            failureStr = "mount vmfs failure:" + e.toString();
        }
        return failure(failureStr);
    }

    /**
     * unmount vmfs include
     * param list<str> dataStoreObjectIds: datastore object id列表 必
     * param str hostId: 主机ID （主机ID与集群ID二选一）
     * param str clusterId: 集群ID（主机ID与集群ID二选一）
     *
     * @param params: include volumeIds,host_id,cluster_id
     * @return: ResponseBodyBean
     */
    @RequestMapping(value = "/ummountvmfs", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean unmountVmfs(@RequestBody Map<String, Object> params)
            throws Exception {
        LOG.info("accessvmfs/unmountvmfs==" + gson.toJson(params));
        String failureStr = "";
        try {
            vmfsAccessService.unmountVmfs(params);
            return success(null, "unmount vmfs success");
        } catch (Exception e) {
            LOG.error("unmount vmfs failure:", e);
            failureStr = "unmount vmfs failure:" + e.toString();
        }
        return failure(failureStr);
    }


    /**
     * Delete vmfs
     * param list<str> dataStoreObjectIds: datastore object id列表 必
     * return: Return execution status and information
     * code:Status code 202 or 503
     * message:Information
     * data: Data，including task_id
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

    /**
     * 根据存储ID查询关联的DME卷详细信息
     * @author wangxy
     * @date 10:01 2020/10/14
     * @param storageObjectId vCenter存储ID
     * @return com.dmeplugin.dmestore.model.ResponseBodyBean
     **/
    @RequestMapping(value = "/volume/{storageObjectId}", method = RequestMethod.GET)
    public ResponseBodyBean volumeDetail(@PathVariable(value = "storageObjectId") String storageObjectId) throws Exception {
        try {
            List<VmfsDatastoreVolumeDetail> detail = vmfsAccessService.volumeDetail(storageObjectId);
            return success(detail);
        } catch (Exception e) {
            return failure(e.getMessage());
        }
    }

    /**
     * VMFS扫描发现
     * @author wangxy
     * @date 10:25 2020/10/14
     * @return com.dmeplugin.dmestore.model.ResponseBodyBean
     **/
    @RequestMapping(value = "/scanvmfs", method = RequestMethod.GET)
    public ResponseBodyBean scanvmfs() throws Exception {
        boolean flag = vmfsAccessService.scanVmfs();
        if (flag) {
            return success();
        }

        return failure();
    }

    @RequestMapping(value = "/gethostsbystorageid/{storageId}", method = RequestMethod.GET)
    public ResponseBodyBean getHostsByStorageId(@PathVariable(value = "storageId") String storageId) throws Exception {
        try {
            List<Map<String, Object>> hosts = vmfsAccessService.getHostsByStorageId(storageId);
            return success(hosts);
        } catch (Exception e) {
            return failure(e.getMessage());
        }
    }

    /**
     * 获取已经挂载的存储
     * @param storageId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/gethostgroupsbystorageid/{storageId}", method = RequestMethod.GET)
    public ResponseBodyBean getHostGroupsByStorageId(@PathVariable(value = "storageId") String storageId) throws Exception {
        try {
            List<Map<String, Object>> hosts = vmfsAccessService.getHostGroupsByStorageId(storageId);
            return success(hosts);
        } catch (Exception e) {
            return failure(e.getMessage());
        }
    }

    /**
     * Access vmfs
     *
     * @return: ResponseBodyBean
     */
    @RequestMapping(value = "/queryvmfs", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean queryVmfs(@RequestParam("dataStoreObjectId") String dataStoreObjectId) throws Exception {
        LOG.info("accessvmfs/queryvmfs");
        String failureStr = "";
        try {
            List<VmfsDataInfo> lists = vmfsAccessService.queryVmfs(dataStoreObjectId);
            LOG.info("listvmfs lists==" + gson.toJson(lists));
            return success(lists);
        } catch (Exception e) {
            LOG.error("list vmfs failure:", e);
            failureStr = "list vmfs failure:" + e.toString();
        }
        return failure(failureStr);
    }

}
