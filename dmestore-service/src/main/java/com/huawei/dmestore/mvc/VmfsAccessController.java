package com.huawei.dmestore.mvc;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.*;
import com.huawei.dmestore.services.VmfsAccessService;
import com.google.gson.Gson;

import com.huawei.dmestore.utils.ToolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * ServiceLevelController
 *
 * @author yy
 * @since 2020-09-02
 **/
@RestController
@RequestMapping(value = "/accessvmfs")
public class VmfsAccessController extends BaseController {
    /**
     * LOG
     **/
    public static final Logger LOG = LoggerFactory.getLogger(VmfsAccessController.class);

    private Gson gson = new Gson();

    private Boolean PARTIAL_SUCCESS = false;

    @Autowired
    private VmfsAccessService vmfsAccessService;

    /**
     * listVmfs
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/listvmfs", method = RequestMethod.GET)
    public ResponseBodyBean listVmfs() {
        String failureStr = "";
        try {
            List<VmfsDataInfo> lists = vmfsAccessService.listVmfs();
            return success(lists);
        } catch (DmeException e) {
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }

    /**
     * get lun by volume
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/getLunByVmfs/{storeId}", method = RequestMethod.GET)
    public ResponseBodyBean getLunByVmfs(@PathVariable(value = "storeId") String storeId ) {
        String failureStr = "";
        try {
            Volume volume = vmfsAccessService.getLunCapacityByVmfsId(storeId);
            return success(volume);
        } catch (DmeException e) {
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }

    /**
     * listVmfsPerformance
     *
     * @param wwns wwns
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/listvmfsperformance", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean listVmfsPerformance(@RequestParam("wwns") List<String> wwns) {
        LOG.info("accessvmfs/listvmfsperformance volumeIds=={}", gson.toJson(wwns));
        String failureStr = "";
        try {
            List<VmfsDataInfo> lists = vmfsAccessService.listVmfsPerformance(wwns);
            return success(lists);
        } catch (DmeException e) {
            LOG.error("get vmfs performance failure:", e);
            failureStr = "get vmfs performance failure:" + e.getMessage();
        }
        return failure(failureStr);
    }

    /**
     * createVmfs
     *
     * @param params params
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/createvmfs", method = RequestMethod.POST)
    public ResponseBodyBean createVmfs(@RequestBody Map<String, Object> params) {
        LOG.info("accessvmfs/createvmfs=={}", gson.toJson(params));
        String failureStr = "";
        try {
            List<Map<String, String>> vmfs = vmfsAccessService.createVmfs(params);
            if (vmfs.size() != 0) {
                return failure(DmeConstants.CODE_CONNECTIVITY_FAILURE,
                    "create vmfs failure,connectivity of host or hostgroup on dme error!", vmfs);
            }
            return success(null, "Create vmfs success");
        } catch (DmeException e) {
            failureStr = "create vmfs failure:" + e.getMessage();
        }
        return failure(failureStr);
    }

    /**
     * mountVmfs
     *
     * @param params params
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/mountvmfs", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean mountVmfs(@RequestBody Map<String, Object> params) {
        LOG.info("accessvmfs/mountvmfs=={}", gson.toJson(params));
        String failureStr = "";
        try {
            List<Map<String, String>> list = vmfsAccessService.mountVmfs(params);
            if (list.size() != 0) {
                return failure(DmeConstants.CODE_CONNECTIVITY_FAILURE,
                    "mount vmfs failure,connectivity of host or hostgroup on dme error!", list);
            }
            return success(null, "Mount vmfs success");
        } catch (DmeException e) {
            LOG.error("mount vmfs failure:", e);
            failureStr = "mount vmfs failure:" + e.getMessage();
        }
        return failure(failureStr);
    }

    /**
     * unmountVmfs
     *
     * @param params params
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/ummountvmfs", method = RequestMethod.POST)
    public ResponseBodyBean unmountVmfs(@RequestBody Map<String, Object> params) {
        LOG.info("accessvmfs/unmountvmfs=={}", gson.toJson(params));
        String failureStr = "";
        try {
            //用于衡量部分成功情况返回结果
            params.put("success", PARTIAL_SUCCESS);
            Map<String, Object> map = vmfsAccessService.unmountVmfsNew(params);
            if (map.size() == 0) {
                return success(null, "unmount vmfs success!");
            } else if (Boolean.valueOf(params.get("success").toString())) {
                return failure(CODE_PARTIALSUCCESS, "unmount vmfs partial success!", map);
            } else {
                String msg = "unmount vmfs failed.";
                return failure(msg,map);
            }
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * deleteVmfs
     *
     * @param params params
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/deletevmfs", method = RequestMethod.POST)
    public ResponseBodyBean deleteVmfs(@RequestBody Map<String, Object> params) {
        LOG.info("accessvmfs/deletevmfs=={}", gson.toJson(params));
        String failureStr = "";
        try {
            vmfsAccessService.deleteVmfs(params);
            return success("delete vmfs success!");
        } catch (DmeException e) {
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }

    /**
     * 根据存储ID查询关联的DME卷详细信息
     *
     * @param storageObjectId vCenter存储ID
     * @return com.dmeplugin.dmestore.model.ResponseBodyBean
     * @author wangxy
     * @date 10:01 2020/10/14
     **/
    @RequestMapping(value = "/volume/{storageObjectId}", method = RequestMethod.GET)
    public ResponseBodyBean volumeDetail(@PathVariable(value = "storageObjectId") String storageObjectId) {
        try {
            List<VmfsDatastoreVolumeDetail> detail = vmfsAccessService.volumeDetail(storageObjectId);
            return success(detail);
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * VMFS扫描发现
     *
     * @return com.dmeplugin.dmestore.model.ResponseBodyBean
     * @author wangxy
     * @date 10:25 2020/10/14
     **/
    @RequestMapping(value = "/scanvmfs", method = RequestMethod.GET)
    public ResponseBodyBean scanvmfs() {
        try {
            vmfsAccessService.scanVmfs();
            return success();
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * 获取指定存储下的主机信息
     *
     * @param storageId 存储ID
     * @return com.dmeplugin.dmestore.model.ResponseBodyBean
     * @author wangxy
     * @date 10:25 2020/10/14
     */
    @RequestMapping(value = "/gethostsbystorageid/{storageId}", method = RequestMethod.GET)
    public ResponseBodyBean getHostsByStorageId(@PathVariable(value = "storageId") String storageId) {
        try {
            List<Map<String, Object>> hosts = vmfsAccessService.getHostsByStorageId2(storageId);
            return success(hosts);
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * getHostGroupsByStorageId
     *
     * @param storageId storageId
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/gethostgroupsbystorageid/{storageId}", method = RequestMethod.GET)
    public ResponseBodyBean getHostGroupsByStorageId(@PathVariable(value = "storageId") String storageId) {
        try {
            List<Map<String, Object>> hosts = vmfsAccessService.getHostGroupsByStorageId2(storageId);
            return success(hosts);
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * queryVmfs
     *
     * @param dataStoreObjectId dataStoreObjectId
     * @return ResponseBodyBean
     * @throws Exception Exception
     */
    @RequestMapping(value = "/queryvmfs", method = RequestMethod.GET)
    public ResponseBodyBean queryVmfs(@RequestParam("dataStoreObjectId") String dataStoreObjectId) throws Exception {
        String failureStr = "";
        try {
            List<VmfsDataInfo> lists = vmfsAccessService.queryVmfs(dataStoreObjectId);
            LOG.info("listvmfs lists=={}", gson.toJson(lists));
            return success(lists);
        } catch (DmeException e) {
            LOG.error("list vmfs failure:", e);
            failureStr = "list vmfs failure:" + e.toString();
        }
        return failure(failureStr);
    }

    /**
     * queryDatastoreByName
     *
     * @param name name
     * @return ResponseBodyBean
     */
    @GetMapping("/querydatastorebyname")
    public ResponseBodyBean queryDatastoreByName(@RequestParam("name") String name) {
        return success(vmfsAccessService.queryDatastoreByName(name));
    }

    /**
     * createVmfs
     *
     * @param params params
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/createvmfsnew", method = RequestMethod.POST)
    public ResponseBodyBean createvmfsNew(@RequestBody Map<String, Object> params) {
        LOG.info("accessvmfs/createvmfsnew=={}", gson.toJson(params));
        String failureStr = "";
        CreateVmfsResponse02 result = new CreateVmfsResponse02();
        try {
            //CreateVmfsResponse result = vmfsAccessService.createVmfsNew(params);

             result = vmfsAccessService.createVmfsNew1(params);
            if (result.getSuccessNo() != 0 && result.getFailNo()==0 && result.getPartialSuccess() == 0
                    && CollectionUtils.isEmpty(result.getConnectionResult())){
                return success(result, "");
            }else if (result.getSuccessNo() == 0 && result.getPartialSuccess() == 0) {
                if (!CollectionUtils.isEmpty(result.getDesc())) {
                    return failure(result.getDesc().toString(), result);
                }else {
                    return failure("", result);
                }
            }else {
                if (!CollectionUtils.isEmpty(result.getDesc())) {
                    return partialSuccess(result,result.getDesc().toString());
                }else {
                    return partialSuccess(result,"");
                }
            }
        } catch (Exception e) {
            failureStr = e.getMessage();
            result.setFailNo(ToolUtils.getInt(params.get("count")));
        }
        return failure(failureStr,result);
    }

    /**
     * mountVmfs
     *
     * @param params params
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/mountvmfsnew", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean mountVmfsNew(@RequestBody Map<String, Object> params) {
        LOG.info("accessvmfs/mountvmfs=={}", gson.toJson(params));
        String failureStr = "";
        try {
            MountVmfsReturn res = vmfsAccessService.mountVmfsNew(params);
            if (!res.isFlag()) {
                if (StringUtils.isEmpty(res.getDescription())) {
                    return failure("");
                } else {
                    return failure(res.getDescription());
                }
            } else if (res.isFlag() && CollectionUtils.isEmpty(res.getFailedHost())) {
                return success(null, "");
            } else {
                if (StringUtils.isEmpty(res.getDescription())) {
                    return partialSuccess(res.getFailedHost(), "");
                } else {
                    return partialSuccess(res.getFailedHost(), res.getDescription());
                }
            }
        } catch (Exception e) {
            LOG.error("mount vmfs failure:", e);
            failureStr = e.getMessage();
        }
        return failure(failureStr);

    }

   /**
     * @Description: 卸载页面以树的方式展示可卸载的主机和集群（以过滤集群下未挂载的主机）
     * @Param @param null
     * @return @return 
     * @throws 
     * @author yc
     * @Date 2021/6/8 15:17
    */
    @RequestMapping(value = "/getMountedHostGroupsAndHostReturnTree/{storageId}", method = RequestMethod.GET)
    public ResponseBodyBean getHostGroupsByStorageIdNew(@PathVariable(value = "storageId") String storageId) {
        try {
            List<ClusterTree> hosts = vmfsAccessService.getMountedHostGroupsAndHostReturnTree(storageId);
            return success(hosts);
        } catch (Exception e) {
            return failure(e.getMessage());
        }
    }

    /**
     * queryCreationMethodByDatastore  查询存储的创建方式
     *
     * @param dataStoreObjectId
     * @return ResponseBodyBean
     */
    @GetMapping("/queryCreationMethodByDatastore")
    public ResponseBodyBean queryCreationMethodByDatastore(@RequestParam("dataStoreObjectId") String dataStoreObjectId) {
        try {
            String createmethod = vmfsAccessService.queryCreationMethodByDatastore(dataStoreObjectId);
            return success(createmethod);
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }
    /**
     * queryVmfs
     *
     * @param clusterObjectId clusterObjectId
     * @return ResponseBodyBean
     * @throws Exception Exception
     */
    @RequestMapping(value = "/queryMountableVmfsByClusterId", method = RequestMethod.GET)
    public ResponseBodyBean queryMountableVmfsByClusterId(@RequestParam("clusterObjectId") String clusterObjectId,
                                                          @RequestParam("dataStoreType") String dataStoreType) {
        String failureStr = "";
        try {
            List<Map<String, String>> lists = vmfsAccessService.queryMountableVmfsByClusterId(clusterObjectId,dataStoreType);
            LOG.info("listvmfs lists=={}", gson.toJson(lists));
            return success(lists);
        } catch (DmeException e) {
            LOG.error("list vmfs failure:", e);
            failureStr = "list vmfs failure:" + e.toString();
        }
        return failure(failureStr);
    }
}
