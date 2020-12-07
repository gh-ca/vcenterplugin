package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.DmeStorageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * DmeStorageController
 *
 * @author lianqiang
 * @since 2020-12-01
 **/
@RestController
@RequestMapping("/dmestorage")
public class DmeStorageController extends BaseController {
    /**
     * LOG
     */
    public static final Logger LOG = LoggerFactory.getLogger(DmeStorageController.class);

    @Autowired
    private DmeStorageService dmeStorageService;

    @GetMapping("/storages")
    public ResponseBodyBean getStorages() {
        try {
            return success(dmeStorageService.getStorages());
        } catch (DMEException e) {
            return failure(e.getMessage());
        }
    }

    @GetMapping("/storage")
    public ResponseBodyBean getStorageDetail(@RequestParam(name = "storageId") String storageId) {
        try {
            return success(dmeStorageService.getStorageDetail(storageId));
        } catch (DMEException e) {
            return failure(e.getMessage());
        }
    }

    @GetMapping("/storagepools")
    public ResponseBodyBean getStoragePools(@RequestParam(name = "storageId") String storageId,
        @RequestParam(name = "mediaType", defaultValue = "all", required = false) String mediaType) {
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
    public ResponseBodyBean getVolumesByPage(
        @RequestParam(name = "storageId", required = false, defaultValue = "") String storageId,
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
    public ResponseBodyBean getStorageControllers(@RequestParam(name = "storageDeviceId") String storageDeviceId) {
        try {
            return success(dmeStorageService.getStorageControllers(storageDeviceId));
        } catch (DMEException e) {
            return failure(e.getMessage());
        }
    }

    @GetMapping("/storagedisks")
    public ResponseBodyBean getStorageDisks(@RequestParam(name = "storageDeviceId") String storageDeviceId) {
        try {
            return success(dmeStorageService.getStorageDisks(storageDeviceId));
        } catch (DMEException e) {
            return failure(e.getMessage());
        }
    }

    @GetMapping("/getstorageethports")
    public ResponseBodyBean getStorageEthPorts(@RequestParam(name = "storageSn") String storageSn) throws Exception {
        try {
            return success(dmeStorageService.getStorageEthPorts(storageSn));
        } catch (DMEException e) {
            return failure("get Storage Eth Ports failure:" + e.toString());
        }
    }

    @GetMapping("/storageport")
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

    @RequestMapping(value = "/liststorageperformance", method = RequestMethod.GET)
    public ResponseBodyBean listStoragePerformance(@RequestParam("storageIds") List<String> storageIds) {
        try {
            return success(dmeStorageService.listStoragePerformance(storageIds));
        } catch (DMEException e) {
            LOG.error("get Storage performance failure:", e);
            return failure("get Storage performance failure:" + e.toString());
        }
    }

    @RequestMapping(value = "/liststoragepoolperformance", method = RequestMethod.GET)
    public ResponseBodyBean listStoragePoolPerformance(@RequestParam("storagePoolIds") List<String> storagePoolIds) {
        try {
            return success(dmeStorageService.listStoragePoolPerformance(storagePoolIds));
        } catch (DMEException e) {
            LOG.error("get Storage Pool performance failure:", e);
            return failure("get Storage Pool performance failure:" + e.toString());
        }
    }

    @RequestMapping(value = "/listStorageControllerPerformance", method = RequestMethod.GET)
    public ResponseBodyBean listStorageControllerPerformance(
        @RequestParam("storageControllerIds") List<String> storageControllerIds) throws Exception {
        try {
            return success(dmeStorageService.listStorageControllerPerformance(storageControllerIds));
        } catch (DMEException e) {
            LOG.error("get Storage controller performance failure:", e);
            return failure("get Storage controller performance failure:" + e.toString());
        }
    }

    @RequestMapping(value = "/listStorageDiskPerformance", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean listStorageDiskPerformance(@RequestParam("storageDiskIds") List<String> storageDiskIds) {
        try {
            return success(dmeStorageService.listStorageDiskPerformance(storageDiskIds));
        } catch (DMEException e) {
            LOG.error("get Storage disk performance failure:", e);
            return failure("get Storage disk performance failure:" + e.toString());
        }
    }

    @RequestMapping(value = "/listStoragePortPerformance", method = RequestMethod.POST)
    public ResponseBodyBean listStoragePortPerformance(@RequestParam("storagePortIds") List<String> storagePortIds) {
        try {
            return success(dmeStorageService.listStoragePortPerformance(storagePortIds));
        } catch (DMEException e) {
            LOG.error("get Storage port performance failure:", e);
            return failure("get Storage port performance failure:" + e.toString());
        }
    }

    @RequestMapping(value = "/listVolumesPerformance", method = RequestMethod.GET)
    public ResponseBodyBean listVolumesPerformance(@RequestParam("volumeId") List<String> volumeId) {
        try {
            return success(dmeStorageService.listVolumesPerformance(volumeId));
        } catch (DMEException e) {
            LOG.error("get Storage volume performance failure:", e);
            return failure("get Storage volume performance failure:" + e.toString());
        }
    }

    @GetMapping("/queryvolumebyname")
    public ResponseBodyBean queryVolumeByName(@RequestParam("name") String name) {
        try {
            return success(dmeStorageService.queryVolumeByName(name));
        } catch (DMEException e) {
            return failure(e.getMessage());
        }
    }

    @GetMapping("/queryfsbyname")
    public ResponseBodyBean queryFsByName(@RequestParam("name") String name,
        @RequestParam("storageId") String storageId) {
        try {
            return success(dmeStorageService.queryFsByName(name, storageId));
        } catch (DMEException e) {
            return failure(e.getMessage());
        }
    }

    @GetMapping("/querysharebyname")
    public ResponseBodyBean queryShareByName(@RequestParam("name") String name,
        @RequestParam("storageId") String storageId) {
        try {
            return success(dmeStorageService.queryShareByName("/" + name, storageId));
        } catch (DMEException e) {
            return failure(e.getMessage());
        }
    }
}
