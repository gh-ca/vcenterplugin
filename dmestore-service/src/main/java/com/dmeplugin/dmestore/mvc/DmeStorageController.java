package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.model.Storage;
import com.dmeplugin.dmestore.services.DmeStorageService;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api
public class DmeStorageController extends BaseController{

    public static final Logger LOG = LoggerFactory.getLogger(NfsAccessController.class);


    private Gson gson=new Gson();

    @Autowired
    private DmeStorageService dmeStorageService;

    /**
     * query storage list
     * @param
     * @return
     */

    @GetMapping("/storages")
    @ResponseBody
    public ResponseBodyBean getStorages(){

        Map<String, Object> resMap = dmeStorageService.getStorages();
        ResponseBodyBean responseBodyBean = new ResponseBodyBean();
        String code = resMap.get("code").toString();
        responseBodyBean.setCode(code);
        responseBodyBean.setDescription(resMap.get("msg").toString());
        responseBodyBean.setData(resMap.get("data"));
        if (null != resMap && null != code && code.equals(200)) {
            return success(responseBodyBean);
        }
        return failure(gson.toJson(responseBodyBean));
    }

    /**
     * search oriented storage detail
     * @param storageId required
     * @return
     */
    @GetMapping("/storage")
    @ResponseBody
    public ResponseBodyBean getStorageDetail(@RequestParam(name = "storageId") String storageId){

        LOG.info("storage_id ==" + storageId );
        Map<String,Object> resMap=dmeStorageService.getStorageDetail(storageId);

        if (null != resMap && null != resMap.get("code") && resMap.get("code").equals(200)) {
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
        if (null != resMap && null != resMap.get("code") && resMap.get("code").equals(200)) {
            return success(resMap);
        }
        return failure(gson.toJson(resMap));
    }

    @GetMapping("/logicports")
    @ResponseBody
    public ResponseBodyBean getLogicPorts(@RequestParam(name = "storageId") String storageId){

        Map<String, Object> resMap = dmeStorageService.getLogicPorts(storageId);
        if (null != resMap && null != resMap.get("code") && resMap.get("code").equals(200)) {
            return success(resMap);
        }
        return failure(gson.toJson(resMap));
    }
    @GetMapping("/volumes")
    @ResponseBody
    public ResponseBodyBean getVolumes(@RequestParam(name = "storageId") String storageId){

        Map<String, Object> resMap = dmeStorageService.getVolumes(storageId);
        if (null != resMap && null != resMap.get("code") && resMap.get("code").equals(200)) {
            return success(resMap);
        }
        return failure(gson.toJson(resMap));
    }
    @GetMapping("/filesystems")
    @ResponseBody
    public ResponseBodyBean getFileSystems(@RequestParam(name = "storageId") String storageId){

        Map<String, Object> resMap = dmeStorageService.getFileSystems(storageId);
        if (null != resMap && null != resMap.get("code") && resMap.get("code").equals(200)) {
            return success(resMap);
        }
        return failure(gson.toJson(resMap));
    }
    @GetMapping("/dtrees")
    @ResponseBody
    public ResponseBodyBean getDTrees(@RequestParam(name = "storageId") String storageId){

        Map<String, Object> resMap = dmeStorageService.getDTrees(storageId);
        if (null != resMap && null != resMap.get("code") && resMap.get("code").equals(200)) {
            return success(resMap);
        }
        return failure(gson.toJson(resMap));
    }
    @GetMapping("/nfsshares")
    @ResponseBody
    public ResponseBodyBean getNfsShares(@RequestParam(name = "storageId") String storageId){

        Map<String, Object> resMap = dmeStorageService.getNfsShares(storageId);
        if (null != resMap && null != resMap.get("code") && resMap.get("code").equals(200)) {
            return success(resMap);
        }
        return failure(gson.toJson(resMap));
    }
    @GetMapping("/bandports")
    @ResponseBody
    public ResponseBodyBean getBandPorts(@RequestParam(name = "storageId") String storageId){

        Map<String, Object> resMap = dmeStorageService.getBandPorts(storageId);
        if (null != resMap && null != resMap.get("code") && resMap.get("code").equals(200)) {
            return success(resMap);
        }
        return failure(gson.toJson(resMap));
    }
    @GetMapping("/storagecontrollers")
    @ResponseBody
    public ResponseBodyBean getStorageControllers(){

        Map<String, Object> resMap = dmeStorageService.getStorageControllers();
        if (null != resMap && null != resMap.get("code") && resMap.get("code").equals(200)) {
            return success(resMap);
        }
        return failure(gson.toJson(resMap));
    }
    @GetMapping("/logicports")
    @ResponseBody
    public ResponseBodyBean getStorageDisks(){

        Map<String, Object> resMap = dmeStorageService.getStorageDisks();
        if (null != resMap && null != resMap.get("code") && resMap.get("code").equals(200)) {
            return success(resMap);
        }
        return failure(gson.toJson(resMap));
    }
}
