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
        if (null != resMap && null != resMap.get("code") && resMap.get("code").toString().equals("200")) {
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
     * @param params key required: obj_ids, indicator_ids, range (obj_ids volumeId集合)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/volume", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean getVolumeStatistic(@RequestBody Map<String, Object> params) throws Exception {
        LOG.info("datastorestatistichistrory/volume params==" + gson.toJson(params));
        Map<String, Object> resMap = dataSotreStatisticHistroyService.queryVolumeStatistic(params);
        if (null != resMap && null != resMap.get("code") && resMap.get("code").toString().equals("200")) {
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
        if (null != resMap && null != resMap.get("code") && resMap.get("code").toString().equals("200")) {
            return success(resMap);
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
        if (null != resMap && null != resMap.get("code") && resMap.get("code").toString().equals("200")) {
            return success(resMap);
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
        if (null != resMap && null != resMap.get("code") && resMap.get("code").toString().equals("200")) {
            return success(resMap);
        }
        return failure(gson.toJson(resMap));
    }

    //queryServiceLevelLunStatistic
    /**
     * 查询serviceLevel的卷性能
     *
     * @param params key required: obj_ids, indicator_ids, range (obj_ids,serviceLevelId)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/servicelevelLun", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean getServiceLevelLunStatistic(@RequestBody Map<String, Object> params) throws Exception {
        LOG.info("datastorestatistichistrory/servicelevelLun params==" + gson.toJson(params));

        Map<String, Object> resMap = dataSotreStatisticHistroyService.queryServiceLevelLunStatistic(params);
        if (null != resMap && null != resMap.get("code") && resMap.get("code").toString().equals("200")) {
            return success(resMap);
        }
        return failure(gson.toJson(resMap));
    }
}
