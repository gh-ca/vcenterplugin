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
    public ResponseBodyBean getVmfsVolumeStatistic(@RequestBody Map<String, Object> params) throws Exception {
        LOG.info("datastorestatistichistrory/vmfs params==" + gson.toJson(params));
        Map<String, Object> resMap = dataSotreStatisticHistroyService.queryVmfsStatistic(params);
        if (null != resMap && null != resMap.get("code") && "200".equals(resMap.get("code").toString())) {
            //Object data = resMap.get("data");
            //JsonObject dataJson = new JsonParser().parse(data.toString()).getAsJsonObject();
            Map<String, Object> data = (Map<String, Object>) resMap.get("data");
            return success(data);
            //return success(gson.toJson(data));
        }
        return failure(gson.toJson(resMap));
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
    public ResponseBodyBean getVolumeStatistic(@RequestBody Map<String, Object> params) throws Exception {
        LOG.info("datastorestatistichistrory/volume params==" + gson.toJson(params));
        Map<String, Object> resMap = dataSotreStatisticHistroyService.queryVolumeStatistic(params);
        if (null != resMap && null != resMap.get("code") && "200".equals(resMap.get("code").toString())) {
            Map<String, Object> data = (Map<String, Object>) resMap.get("data");
            return success(data);
        }
        return failure(gson.toJson(resMap));
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
    public ResponseBodyBean getNfsVolumeStatistic(@RequestBody Map<String, Object> params) throws Exception {
        LOG.info("datastorestatistichistrory/nfs params==" + gson.toJson(params));
        Map<String, Object> resMap = dataSotreStatisticHistroyService.queryNfsStatistic(params);
        if (null != resMap && null != resMap.get("code") && "200".equals(resMap.get("code").toString())) {
            Map<String, Object> data = (Map<String, Object>) resMap.get("data");
            return success(data);
        }
        return failure(gson.toJson(resMap));
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
    public ResponseBodyBean getFsStatistic(@RequestBody Map<String, Object> params) throws Exception {
        LOG.info("datastorestatistichistrory/fs params==" + gson.toJson(params));
        Map<String, Object> resMap = dataSotreStatisticHistroyService.queryFsStatistic(params);
        if (null != resMap && null != resMap.get("code") && "200".equals(resMap.get("code").toString())) {
            Map<String, Object> data = (Map<String, Object>) resMap.get("data");
            return success(data);
        }
        return failure(gson.toJson(resMap));
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
    public ResponseBodyBean getServiceLevelStatistic(@RequestBody Map<String, Object> params) throws Exception {
        LOG.info("datastorestatistichistrory/servicelevel params==" + gson.toJson(params));

        Map<String, Object> resMap = dataSotreStatisticHistroyService.queryServiceLevelStatistic(params);
        if (null != resMap && null != resMap.get("code") && "200".equals(resMap.get("code").toString())) {
            Map<String, Object> data = (Map<String, Object>) resMap.get("data");
            return success(data);
        }
        return failure(gson.toJson(resMap));
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
    public ResponseBodyBean getServiceLevelLunStatistic(@RequestBody Map<String, Object> params) throws Exception {
        LOG.info("datastorestatistichistrory/servicelevellun params==" + gson.toJson(params));
        Map<String, Object> resMap = dataSotreStatisticHistroyService.queryServiceLevelLunStatistic(params);
        if (null != resMap && null != resMap.get("code") && "200".equals(resMap.get("code").toString())) {
            Map<String, Object> data = (Map<String, Object>) resMap.get("data");
            return success(data);
        }
        return failure(gson.toJson(resMap));
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
    public ResponseBodyBean getServiceLevelStoragePoolStatistic(@RequestBody Map<String, Object> params) throws Exception {
        LOG.info("datastorestatistichistrory/servicelevelstoragepool params==" + gson.toJson(params));
        Map<String, Object> resMap = dataSotreStatisticHistroyService.queryServiceLevelStoragePoolStatistic(params);
        if (null != resMap && null != resMap.get("code") && "200".equals(resMap.get("code").toString())) {
            Map<String, Object> data = (Map<String, Object>) resMap.get("data");
            return success(data);
        }
        return failure(gson.toJson(resMap));
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
       /* Map<String, Object> resMap = dataSotreStatisticHistroyService.queryStoragePoolStatistic(params);
        if (null != resMap && null != resMap.get("code") && "200".equals(resMap.get("code").toString())) {
            Map<String, Object> data = (Map<String, Object>) resMap.get("data");
            return success(data);
        }
        return failure(gson.toJson(resMap));*/
        return null;
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
    public ResponseBodyBean getStoragePoolCurrentStatistic(@RequestBody Map<String, Object> params) throws Exception {
        LOG.info("datastorestatistichistrory/storagepoolcurrent params==" + gson.toJson(params));
        Map<String, Object> resMap = dataSotreStatisticHistroyService.queryStoragePoolCurrentStatistic(params);
        if (null != resMap && null != resMap.get("code") && "200".equals(resMap.get("code").toString())) {
            Map<String, Object> data = (Map<String, Object>) resMap.get("data");
            return success(data);
        }
        return failure(gson.toJson(resMap));
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
    public ResponseBodyBean getStoragDeviceStatistic(@RequestBody Map<String, Object> params) throws Exception {
        LOG.info("datastorestatistichistrory/storagedevice params==" + gson.toJson(params));
        Map<String, Object> resMap = dataSotreStatisticHistroyService.queryStorageDevcieStatistic(params);
        if (null != resMap && null != resMap.get("code") && "200".equals(resMap.get("code").toString())) {
            Map<String, Object> data = (Map<String, Object>) resMap.get("data");
            return success(data);
        }
        return failure(gson.toJson(resMap));
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
    public ResponseBodyBean getStoragDeviceCurrenStatistic(@RequestBody Map<String, Object> params) throws Exception {
        LOG.info("datastorestatistichistrory/storagedevicecurrent params==" + gson.toJson(params));
        Map<String, Object> resMap = dataSotreStatisticHistroyService.queryStorageDevcieCurrentStatistic(params);
        if (null != resMap && null != resMap.get("code") && "200".equals(resMap.get("code").toString())) {
            Map<String, Object> data = (Map<String, Object>) resMap.get("data");
            return success(data);
        }
        return failure(gson.toJson(resMap));
    }



}
