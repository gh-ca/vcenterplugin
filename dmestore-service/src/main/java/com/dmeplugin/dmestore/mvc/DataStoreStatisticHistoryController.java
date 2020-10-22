package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.DataStoreStatisticHistoryService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description: TODO
 * @ClassName: DataStoreStatisticHistoryController
 * @Company: GH-CA
 * @author: liuxh
 * @create: 2020-09-03
 **/
@RestController
@RequestMapping(value = "/datastorestatistichistrory")
public class DataStoreStatisticHistoryController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(DataStoreStatisticHistoryController.class);

    private Gson gson = new Gson();
    @Autowired
    DataStoreStatisticHistoryService dataSotreStatisticHistroyService;

    /**
     * 查询vmfs性能(实际vmfs下的volume的性能)
     *
     * @param params key required: obj_ids, indicator_ids, range (obj_ids volumeId集合? vmfs在dme侧对应资源的id集合?)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/vmfs", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean getVmfsVolumeStatistic(@RequestBody Map<String, Object> params) {
        LOG.info("datastorestatistichistrory/vmfs params==" + gson.toJson(params));
        try {
            return success(dataSotreStatisticHistroyService.queryVmfsStatistic(params));
        } catch (Exception e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }
    }

    /**
     * 查询volume性能
     *
     * @param params key required: obj_ids, indicator_ids, range (obj_ids volumeInstanceId集合,无法直接获取目前使用wwn)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/volume", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean getVolumeStatistic(@RequestBody Map<String, Object> params) {
        LOG.info("datastorestatistichistrory/volume params==" + gson.toJson(params));
        try {
            return success(dataSotreStatisticHistroyService.queryVolumeStatistic(params));
        } catch (Exception e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }

    }

    /**
     * 查询nfs性能(实际nfs下的fs的性能?)
     *
     * @param params key required: obj_ids, indicator_ids, range (obj_ids fsId集合? nfs在dme侧对应资源的id集合?)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/nfs", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean getNfsVolumeStatistic(@RequestBody Map<String, Object> params) {
        LOG.info("datastorestatistichistrory/nfs params==" + gson.toJson(params));

        try {
            return success(dataSotreStatisticHistroyService.queryNfsStatistic(params));
        } catch (Exception e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }


    }

    /**
     * 查询fs性能
     *
     * @param params key required: obj_ids, indicator_ids, range (obj_ids fsId集合)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/fs", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean getFsStatistic(@RequestBody Map<String, Object> params) {
        LOG.info("datastorestatistichistrory/fs params==" + gson.toJson(params));
        try {
            return success(dataSotreStatisticHistroyService.queryFsStatistic(params));
        } catch (Exception e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }
    }

    /**
     * 查询serviceLevel的性能(实际serviceLevel下的volume的性能)
     *
     * @param params key required: obj_ids, indicator_ids, range (obj_ids,volumeId集合)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/servicelevel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean getServiceLevelStatistic(@RequestBody Map<String, Object> params) {
        LOG.info("datastorestatistichistrory/servicelevel params==" + gson.toJson(params));

        try {
            return success(dataSotreStatisticHistroyService.queryServiceLevelStatistic(params));
        } catch (Exception e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }

    }

    /**
     * 查询serviceLevel的卷性能,对象Id为服务等级Id eg:{"obj_ids" : ["cd17eca4-3056-4ca0-9a4a-2338174c33d8"]}
     *
     * @param params key required: obj_ids, indicator_ids, range (obj_ids,serviceLevelId)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/servicelevelLun", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean getServiceLevelLunStatistic(@RequestBody Map<String, Object> params) {
        LOG.info("datastorestatistichistrory/servicelevellun params==" + gson.toJson(params));
        try {
            return success(dataSotreStatisticHistroyService.queryServiceLevelLunStatistic(params));
        } catch (Exception e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }

    }

    /**
     * 查询serviceLevel的存储池性能,对象Id为服务等级Id  eg:{"obj_ids" : ["cd17eca4-3056-4ca0-9a4a-2338174c33d8"]}
     *
     * @param params key required: obj_ids, indicator_ids, range (obj_ids,serviceLevelId)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/servicelevelStoragePool", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean getServiceLevelStoragePoolStatistic(@RequestBody Map<String, Object> params) {
        LOG.info("datastorestatistichistrory/servicelevelstoragepool params==" + gson.toJson(params));
        try {
            return success(dataSotreStatisticHistroyService.queryServiceLevelStoragePoolStatistic(params));
        } catch (Exception e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }

    }

    /**
     * 查询存储池性能,对象Id为存储池Id  eg:{"obj_ids" : ["cd17eca4-3056-4ca0-9a4a-2338174c33d8"]}
     *
     * @param params key required: obj_ids, indicator_ids, range (obj_ids, poolIds)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/storagePool", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean getStoragePoolStatistic(@RequestBody Map<String, Object> params) throws Exception {
        LOG.info("datastorestatistichistrory/storagepool params==" + gson.toJson(params));
        try {
            return success(dataSotreStatisticHistroyService.queryStoragePoolStatistic(params));
        } catch (Exception e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }
    }

    /**
     * 查询存储池当前性能,对象Id为存储池Id  eg:{"obj_ids" : ["cd17eca4-3056-4ca0-9a4a-2338174c33d8"]}
     *
     * @param params key required: obj_ids, indicator_ids, range (obj_ids, poolIds)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/storagePoolCurrent", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean getStoragePoolCurrentStatistic(@RequestBody Map<String, Object> params) {
        LOG.info("datastorestatistichistrory/storagepoolcurrent params==" + gson.toJson(params));
        try {
            return success(dataSotreStatisticHistroyService.queryStoragePoolCurrentStatistic(params));
        } catch (Exception e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }

    }

    /**
     * 查询存储设备性能,对象Id为存储设备Id  eg:{"obj_ids" : ["cd17eca4-3056-4ca0-9a4a-2338174c33d8"]}
     *
     * @param params key required: obj_ids, indicator_ids, range (obj_ids, storagedeviceIds)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/storageDevice", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean getStoragDeviceStatistic(@RequestBody Map<String, Object> params) {
        LOG.info("datastorestatistichistrory/storagedevice params==" + gson.toJson(params));
        try {
            return success(dataSotreStatisticHistroyService.queryStorageDevcieStatistic(params));
        } catch (Exception e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }
    }

    /**
     * 查询存储设备当前性能,对象Id为存储设备Id  eg:{"obj_ids" : ["cd17eca4-3056-4ca0-9a4a-2338174c33d8"]}
     *
     * @param params key required: obj_ids, indicator_ids, range (obj_ids, storagedeviceIds)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/storageDeviceCurrent", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean getStoragDeviceCurrentStatistic(@RequestBody Map<String, Object> params) {
        LOG.info("datastorestatistichistrory/storagedevicecurrent params==" + gson.toJson(params));
        try {
            return success(dataSotreStatisticHistroyService.queryStorageDevcieCurrentStatistic(params));
        } catch (Exception e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }
    }

    /**
     * 查询控制器历史性能,对象Id为控制器设备Id  eg:{"obj_ids" : ["cd17eca4-3056-4ca0-9a4a-2338174c33d8"]}
     *
     * @param params key required: obj_ids, indicator_ids, range (obj_ids, contorllerIds)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/controller", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean getControllerStatistic(@RequestBody Map<String, Object> params) {
        LOG.info("datastorestatistichistrory/controllercurrent params==" + gson.toJson(params));
        try {
            return success(dataSotreStatisticHistroyService.queryControllerStatistic(params));
        } catch (Exception e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }
    }

    /**
     * 查询控制器当前性能,对象Id为控制器设备Id  eg:{"obj_ids" : ["cd17eca4-3056-4ca0-9a4a-2338174c33d8"]}
     *
     * @param params key required: obj_ids, indicator_ids, range (obj_ids, contorllerIds)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/controllerCurrent", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean getControllerCurrenStatistic(@RequestBody Map<String, Object> params) {
        LOG.info("datastorestatistichistrory/controllercurrent params==" + gson.toJson(params));
        try {
            return success(dataSotreStatisticHistroyService.queryControllerCurrentStatistic(params));
        } catch (Exception e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }

    }

    /**
     * 查询存储端口历史性能,对象Id为存储端口Id  eg:{"obj_ids" : ["cd17eca4-3056-4ca0-9a4a-2338174c33d8"]}
     *
     * @param params key required: obj_ids, indicator_ids, range (obj_ids, storagedeviceIds)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/storagePort", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean getStoragPortStatistic(@RequestBody Map<String, Object> params) {
        LOG.info("datastorestatistichistrory/storageport params==" + gson.toJson(params));
        try {
            return success(dataSotreStatisticHistroyService.queryStoragePortStatistic(params));
        } catch (Exception e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }
    }

    /**
     * 查询存储端口当前性能,对象Id为存储端口Id  eg:{"obj_ids" : ["cd17eca4-3056-4ca0-9a4a-2338174c33d8"]}
     *
     * @param params key required: obj_ids, indicator_ids, range (obj_ids, storagedeviceIds)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/storagePortCurrent", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean getStoragPortCurrentStatistic(@RequestBody Map<String, Object> params) {
        LOG.info("datastorestatistichistrory/storageportcurrent params==" + gson.toJson(params));
        try {
            return success(dataSotreStatisticHistroyService.queryStoragePortCurrentStatistic(params));
        } catch (Exception e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }
    }


    /**
     * 查询存储磁盘历史性能,对象Id为存储磁盘Id  eg:{"obj_ids" : ["cd17eca4-3056-4ca0-9a4a-2338174c33d8"]}
     *
     * @param params key required: obj_ids, indicator_ids, range (obj_ids, storagedeviceIds)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/storageDisk", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean getStoragDiskStatistic(@RequestBody Map<String, Object> params) {
        LOG.info("datastorestatistichistrory/storagedisk params==" + gson.toJson(params));
        try {
            return success(dataSotreStatisticHistroyService.queryStorageDiskStatistic(params));
        } catch (Exception e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }
    }

    /**
     * 查询存储磁盘当前性能,对象Id为存储磁盘Id  eg:{"obj_ids" : ["cd17eca4-3056-4ca0-9a4a-2338174c33d8"]}
     *
     * @param params key required: obj_ids, indicator_ids, range (obj_ids, storagedeviceIds)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/storageDiskCurrent", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean getStoragDiskCurrentStatistic(@RequestBody Map<String, Object> params) {
        LOG.info("datastorestatistichistrory/storagediskcurrent params==" + gson.toJson(params));
        try {
            return success(dataSotreStatisticHistroyService.queryStorageDiskCurrentStatistic(params));
        } catch (Exception e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }
    }

}
