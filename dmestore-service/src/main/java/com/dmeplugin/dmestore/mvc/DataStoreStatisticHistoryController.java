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

    @Autowired
    private Gson gson;
    @Autowired
    DataStoreStatisticHistoryService dataSotreStatisticHistroyService;

    @RequestMapping(value = "/vmfsvolume", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean getNfsVolumeStatistic(@RequestBody Map<String, Object> params) throws Exception {
        LOG.info("datastorestatistichistrory/vmfsvolume params==" + gson.toJson(params));

        Map<String, Object> resMap = dataSotreStatisticHistroyService.queryVmfsStatistic(params);
        if (null != resMap && null != resMap.get("code") && resMap.get("code").equals("200")) {
            return success(resMap);
        }
        return failure(gson.toJson(resMap));
    }
}
