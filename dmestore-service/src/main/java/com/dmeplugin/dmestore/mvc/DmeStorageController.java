package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.model.*;
import com.dmeplugin.dmestore.services.DmeStorageService;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lianq
 * @className DmeStorageController
 * @description TODO
 * @date 2020/9/3 17:43
 */

@RestController
@RequestMapping("/dmestorage")
public class DmeStorageController extends BaseController {


    public static final Logger LOG = LoggerFactory.getLogger(DmeStorageController.class);
    private Gson gson = new Gson();

    @Autowired
    private DmeStorageService dmeStorageService;

    /**
     * query storage list
     *
     * @param
     * @return
     */

    @GetMapping("/storages")
    @ResponseBody
    public ResponseBodyBean getStorages() {

        try {
            return success(dmeStorageService.getStorages());
        } catch (DMEException e) {
            return failure(e.getMessage());
        }


    }

    /**
     * search oriented storage detail
     *
     * @param storageId required
     * @return
     */
    @GetMapping("/storage")
    @ResponseBody
    public ResponseBodyBean getStorageDetail(@RequestParam(name = "storageId") String storageId) {

        LOG.info("storage_id ==" + storageId);

        try {
            return success(dmeStorageService.getStorageDetail(storageId));
        } catch (DMEException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * @param storageId
     * @param mediaType file,block,all
     * @return
     */
    @GetMapping("/storagepools")
    @ResponseBody
    public ResponseBodyBean getStoragePools(@RequestParam(name = "storageId") String storageId,
                                            @RequestParam(name = "mediaType", defaultValue = "all", required = false) String mediaType) {

        LOG.info("storage_id ==" + storageId);
        try {
            return success(dmeStorageService.getStoragePools(storageId, mediaType));
        } catch (DMEException e) {
            return failure(e.getMessage());
        }

    }

    @GetMapping("/logicports")
    @ResponseBody
    public ResponseBodyBean getLogicPorts(@RequestParam(name = "storageId") String storageId) {

        try {
            return success(dmeStorageService.getLogicPorts(storageId));
        } catch (DMEException e) {
            return failure(e.getMessage());
        }
    }

    @GetMapping("/volumes/byPage")
    @ResponseBody
    public ResponseBodyBean getVolumesByPage(@RequestParam(name = "storageId", required = false, defaultValue = "") String storageId,
                                             @RequestParam(name = "pageSize", required = false, defaultValue = "20") String pageSize,
                                             @RequestParam(name = "pageNo", required = false, defaultValue = "0") String pageNo) {
        try {
            return success(dmeStorageService.getVolumesByPage(storageId, pageSize, pageNo));
        } catch (DMEException e) {
            return failure(e.getMessage());
        }
    }

    @GetMapping("/filesystems")
    @ResponseBody
    public ResponseBodyBean getFileSystems(@RequestParam(name = "storageId") String storageId) {

        try {
            return success(dmeStorageService.getFileSystems(storageId));
        } catch (DMEException e) {
            return failure(e.getMessage());
        }
    }

    @GetMapping("/dtrees")
    @ResponseBody
    public ResponseBodyBean getDtrees(@RequestParam(name = "storageId") String storageId) {

        try {
            return success(dmeStorageService.getDtrees(storageId));
        } catch (DMEException e) {
            return failure(e.getMessage());
        }
    }

    @GetMapping("/nfsshares")
    @ResponseBody
    public ResponseBodyBean getNfsShares(@RequestParam(name = "storageId") String storageId) {

        try {
            return success(dmeStorageService.getNfsShares(storageId));
        } catch (DMEException e) {
            return failure(e.getMessage());
        }
    }

    @GetMapping("/bandports")
    @ResponseBody
    public ResponseBodyBean getBandPorts(@RequestParam(name = "storageId") String storageId) {

        try {
            return success(dmeStorageService.getBandPorts(storageId));
        } catch (DMEException e) {
            return failure(e.getMessage());
        }
    }

    @GetMapping("/storagecontrollers")
    @ResponseBody
    public ResponseBodyBean getStorageControllers(@RequestParam(name = "storageDeviceId") String storageDeviceId) {

        try {
            return success(dmeStorageService.getStorageControllers(storageDeviceId));
        } catch (DMEException e) {
            return failure(e.getMessage());
        }


    }

    @GetMapping("/storagedisks")
    @ResponseBody
    public ResponseBodyBean getStorageDisks(@RequestParam(name = "storageDeviceId") String storageDeviceId) {

        try {
            return success(dmeStorageService.getStorageDisks(storageDeviceId));
        } catch (DMEException e) {
            return failure(e.getMessage());
        }


    }

    /**
     * Get ETH ports
     *
     * @param storageSn storage's Sn
     * @return ResponseBodyBean List<EthPortInfo> include EthPortInfo
     * @throws Exception when error
     */
    @GetMapping("/getstorageethports")
    @ResponseBody
    public ResponseBodyBean getStorageEthPorts(@RequestParam(name = "storageSn") String storageSn) throws Exception {

        LOG.info("getStorageEthPorts storageSn==" + storageSn);
        String failureStr = "";
        try {
            List<EthPortInfo> lists = dmeStorageService.getStorageEthPorts(storageSn);
            return success(lists);
        } catch (Exception e) {
            LOG.error("get Storage Eth Ports failure:", e);
            return failure("get Storage Eth Ports failure:" + e.toString());
        }
    }

    /**
     * @param portType FC FCoE ETH  默认 ALL 所有端口类型
     * @return
     */
    @GetMapping("/storageport")
    @ResponseBody
    public ResponseBodyBean getStoragePort(@RequestParam(name = "storageDeviceId") String storageDeviceId,
                                           @RequestParam(name = "portType", defaultValue = "ALL", required = false) String portType) {
        try {
            return success(dmeStorageService.getStoragePort(storageDeviceId, portType));
        } catch (DMEException e) {
            return failure(e.getMessage());
        }
    }

    @GetMapping("/failovergroups")
    @ResponseBody
    public ResponseBodyBean getFailoverGroups(@RequestParam(name = "storageId") String storageId) {
        try {
            return success(dmeStorageService.getFailoverGroups(storageId));
        } catch (DMEException e) {
            return failure(e.getMessage());
        }

    }

    /**
     * Access storage performance
     *
     * @param storageIds storage id
     * @return: ResponseBodyBean
     */
    @RequestMapping(value = "/liststorageperformance", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean listStoragePerformance(@RequestParam("storageIds") List<String> storageIds)
            throws Exception {
        LOG.info("accessvmfs/liststorageperformance storageIds==" + gson.toJson(storageIds));
        String failureStr = "";
        try {
            List<Storage> lists = dmeStorageService.listStoragePerformance(storageIds);
            return success(lists);
        } catch (Exception e) {
            LOG.error("get Storage performance failure:", e);
            return failure("get Storage performance failure:" + e.toString());
        }
    }

    /**
     * Access storage pool performance
     * @param storagePoolIds storage pool res Id
     * @return: ResponseBodyBean
     */
    @RequestMapping(value = "/liststoragepoolperformance", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean listStoragePoolPerformance(@RequestParam("storagePoolIds") List<String> storagePoolIds)
            throws Exception {
        LOG.info("accessvmfs/listStoragePoolPerformance storagePoolIds==" + gson.toJson(storagePoolIds));
        String failureStr = "";
        try {
            List<StoragePool> lists = dmeStorageService.listStoragePoolPerformance(storagePoolIds);
            return success(lists);
        } catch (Exception e) {
            LOG.error("get Storage Pool performance failure:", e);
            return failure("get Storage Pool performance failure:" + e.toString());
        }
    }

    /**
     * Access storage controller performance
     *
     * @param storageControllerIds controller res Id
     * @return: ResponseBodyBean
     */
    @RequestMapping(value = "/listStorageControllerPerformance", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean listStorageControllerPerformance(@RequestParam("storageControllerIds") List<String> storageControllerIds)
            throws Exception {
        LOG.info("accessvmfs/listStorageControllerPerformance storagePoolIds==" + gson.toJson(storageControllerIds));
        String failureStr = "";
        try {
            List<StorageControllers> lists = dmeStorageService.listStorageControllerPerformance(storageControllerIds);
            return success(lists);
        } catch (Exception e) {
            LOG.error("get Storage controller performance failure:", e);
            failureStr = "get Storage controller performance failure:" + e.toString();
        }
        return failure(failureStr);
    }

    /**
     * Access storage disk performance
     *
     * @param storageDiskIds storage disk res Id
     * @return: ResponseBodyBean
     */
    @RequestMapping(value = "/listStorageDiskPerformance", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean listStorageDiskPerformance(@RequestParam("storageDiskIds") List<String> storageDiskIds)
            throws Exception {
        LOG.info("accessvmfs/listStorageDiskPerformance storagePoolIds==" + gson.toJson(storageDiskIds));
        String failureStr = "";
        try {
            List<StorageDisk> lists = dmeStorageService.listStorageDiskPerformance(storageDiskIds);
            return success(lists);
        } catch (Exception e) {
            LOG.error("get Storage disk performance failure:", e);
            return failure("get Storage disk performance failure:" + e.toString());
        }
    }

    /**
     * Access storage port performance
     *
     * @param storagePortIds storage port res Id
     * @return: ResponseBodyBean
     */
    @RequestMapping(value = "/listStoragePortPerformance", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean listStoragePortPerformance(@RequestParam("storagePortIds") List<String> storagePortIds)
            throws Exception {
        LOG.info("accessvmfs/listStoragePortPerformance storagePoolIds==" + gson.toJson(storagePortIds));
        String failureStr = "";
        try {
            List<StoragePort> lists = dmeStorageService.listStoragePortPerformance(storagePortIds);
            return success(lists);
        } catch (Exception e) {
            LOG.error("get Storage port performance failure:", e);
            return failure("get Storage port performance failure:" + e.toString());
        }
    }

    /**
     * Access storage volume performance
     *
     * @param volumeId storage volume res Id
     * @return: ResponseBodyBean
     */
    @RequestMapping(value = "/listVolumesPerformance", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean listVolumesPerformance(@RequestParam("volumeId") List<String> volumeId)
            throws Exception {
        LOG.info("accessvmfs/listVolumesPerformance storagePoolIds==" + gson.toJson(volumeId));
        String failureStr = "";
        try {
            List<Volume> lists = dmeStorageService.listVolumesPerformance(volumeId);
            return success(lists);
        } catch (Exception e) {
            LOG.error("get Storage volume performance failure:", e);
            return failure("get Storage volume performance failure:" + e.toString());
        }
    }

    /**
     * 通过名字查询DME中是否存在指定volume
     *
     * @param name volume name
     * @return
     */
    @GetMapping("/queryvolumebyname")
    public ResponseBodyBean queryVolumeByName(@RequestParam("name") String name) {
        try {
            return success(dmeStorageService.queryVolumeByName(name));
        } catch (DMEException e) {
            return failure(e.getMessage());
        }
    }

    @GetMapping("/queryfsbyname")
    public ResponseBodyBean queryFsByName(@RequestParam("name") String name, @RequestParam("storageId") String storageId) {
        try {
            return success(dmeStorageService.queryFsByName(name, storageId));
        } catch (DMEException e) {
            return failure(e.getMessage());
        }
    }

    @GetMapping("/querysharebyname")
    public ResponseBodyBean queryShareByName(@RequestParam("name") String name, @RequestParam("storageId") String storageId) {
        try {
            return success(dmeStorageService.queryShareByName("/" + name, storageId));
        } catch (DMEException e) {
            return failure(e.getMessage());
        }
    }
}
