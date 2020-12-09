package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.exception.DmeException;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.DataStoreStatisticHistoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * DataStoreStatisticHistoryController
 *
 * @author liuxh
 * @since 2020-12-01
 **/
@RestController
@RequestMapping(value = "/datastorestatistichistrory")
public class DataStoreStatisticHistoryController extends BaseController {
    @Autowired
    DataStoreStatisticHistoryService dataSotreStatisticHistroyService;

    @RequestMapping(value = "/vmfs", method = RequestMethod.POST)
    public ResponseBodyBean getVmfsVolumeStatistic(@RequestBody Map<String, Object> params) {
        try {
            return success(dataSotreStatisticHistroyService.queryVmfsStatistic(params));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "/volume", method = RequestMethod.POST)
    public ResponseBodyBean getVolumeStatistic(@RequestBody Map<String, Object> params) {
        try {
            return success(dataSotreStatisticHistroyService.queryVolumeStatistic(params));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "/nfs", method = RequestMethod.POST)
    public ResponseBodyBean getNfsVolumeStatistic(@RequestBody Map<String, Object> params) {
        try {
            return success(dataSotreStatisticHistroyService.queryNfsStatistic(params));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "/fs", method = RequestMethod.POST)
    public ResponseBodyBean getFsStatistic(@RequestBody Map<String, Object> params) {
        try {
            return success(dataSotreStatisticHistroyService.queryFsStatistic(params));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "/servicelevel", method = RequestMethod.POST)
    public ResponseBodyBean getServiceLevelStatistic(@RequestBody Map<String, Object> params) {
        try {
            return success(dataSotreStatisticHistroyService.queryServiceLevelStatistic(params));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "/servicelevelLun", method = RequestMethod.POST)
    public ResponseBodyBean getServiceLevelLunStatistic(@RequestBody Map<String, Object> params) {
        try {
            return success(dataSotreStatisticHistroyService.queryServiceLevelLunStatistic(params));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "/servicelevelStoragePool", method = RequestMethod.POST)
    public ResponseBodyBean getServiceLevelStoragePoolStatistic(@RequestBody Map<String, Object> params) {
        try {
            return success(dataSotreStatisticHistroyService.queryServiceLevelStoragePoolStatistic(params));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "/storagePool", method = RequestMethod.POST)
    public ResponseBodyBean getStoragePoolStatistic(@RequestBody Map<String, Object> params) throws Exception {
        try {
            return success(dataSotreStatisticHistroyService.queryStoragePoolStatistic(params));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "/storagePoolCurrent", method = RequestMethod.POST)
    public ResponseBodyBean getStoragePoolCurrentStatistic(@RequestBody Map<String, Object> params) {
        try {
            return success(dataSotreStatisticHistroyService.queryStoragePoolCurrentStatistic(params));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "/storageDevice", method = RequestMethod.POST)
    public ResponseBodyBean getStoragDeviceStatistic(@RequestBody Map<String, Object> params) {
        try {
            return success(dataSotreStatisticHistroyService.queryStorageDevcieStatistic(params));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "/storageDeviceCurrent", method = RequestMethod.POST)
    public ResponseBodyBean getStoragDeviceCurrentStatistic(@RequestBody Map<String, Object> params) {
        try {
            return success(dataSotreStatisticHistroyService.queryStorageDevcieCurrentStatistic(params));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "/controller", method = RequestMethod.POST)
    public ResponseBodyBean getControllerStatistic(@RequestBody Map<String, Object> params) {
        try {
            return success(dataSotreStatisticHistroyService.queryControllerStatistic(params));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "/controllerCurrent", method = RequestMethod.POST)
    public ResponseBodyBean getControllerCurrenStatistic(@RequestBody Map<String, Object> params) {
        try {
            return success(dataSotreStatisticHistroyService.queryControllerCurrentStatistic(params));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "/storagePort", method = RequestMethod.POST)
    public ResponseBodyBean getStoragPortStatistic(@RequestBody Map<String, Object> params) {
        try {
            return success(dataSotreStatisticHistroyService.queryStoragePortStatistic(params));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "/storagePortCurrent", method = RequestMethod.POST)
    public ResponseBodyBean getStoragPortCurrentStatistic(@RequestBody Map<String, Object> params) {
        try {
            return success(dataSotreStatisticHistroyService.queryStoragePortCurrentStatistic(params));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "/storageDisk", method = RequestMethod.POST)
    public ResponseBodyBean getStoragDiskStatistic(@RequestBody Map<String, Object> params) {
        try {
            return success(dataSotreStatisticHistroyService.queryStorageDiskStatistic(params));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "/storageDiskCurrent", method = RequestMethod.POST)
    public ResponseBodyBean getStoragDiskCurrentStatistic(@RequestBody Map<String, Object> params) {
        try {
            return success(dataSotreStatisticHistroyService.queryStorageDiskCurrentStatistic(params));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }
}
