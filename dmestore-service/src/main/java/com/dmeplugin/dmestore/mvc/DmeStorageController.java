package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.model.Storage;
import com.dmeplugin.dmestore.services.DmeStorageService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author lianq
 * @className DmeStorageController
 * @description TODO
 * @date 2020/9/3 17:43
 */

@RestController
@RequestMapping("/dmestorage")
public class DmeStorageController extends BaseController{

    public static final Logger LOG = LoggerFactory.getLogger(NfsAccessController.class);

    @Autowired
    private Gson gson;

    @Autowired
    private DmeStorageService dmeStorageService;

    /**
     * query storage list
     * @param params
     * @return
     */

    @GetMapping("/storages")
    @ResponseBody
    public ResponseBodyBean getStorages(@RequestBody Map<String,String> params){

        LOG.info("storages ==" + gson.toJson(params) );
        Map<String, Object> resMap = dmeStorageService.getStorages(params);
        if (null != resMap && null != resMap.get("code") && resMap.get("code").equals("200")) {
            return success(resMap);
        }
        return failure(gson.toJson(resMap));
    }

    /**
     * search oriented storage detail
     * @param storage_id required
     * @return
     */
    @GetMapping("/storage")
    @ResponseBody
    public ResponseBodyBean getStorageDetail(@RequestParam(name = "storageId") String storageId){

        LOG.info("storage_id ==" + storageId );
        Map<String,Object> resMap=dmeStorageService.getStorageDetail(storageId);

        if (null != resMap && null != resMap.get("code") && resMap.get("code").equals("200")) {
            return success(resMap);
        }
        return failure(gson.toJson(resMap));
    }

    /**
     *  list Storage Pool
     * @param storageId required
     * @return
     */
    @GetMapping("/storagepools")
    @ResponseBody
    public ResponseBodyBean getStoragePools(@RequestParam(name = "storageId") String storageId){

        LOG.info("storage_id ==" + storageId );
        Map<String, Object> resMap = dmeStorageService.getStoragePools(storageId);
        if (null != resMap && null != resMap.get("code") && resMap.get("code").equals("200")) {
            return success(resMap);
        }
        return failure(gson.toJson(resMap));
    }
}
