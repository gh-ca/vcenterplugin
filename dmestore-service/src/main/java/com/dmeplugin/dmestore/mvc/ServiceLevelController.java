package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.model.StoragePool;
import com.dmeplugin.dmestore.model.Volume;
import com.dmeplugin.dmestore.services.ServiceLevelService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @ClassName: ServiceLevelController
 * @Company: GH-CA
 * @author: liuxh
 * @create: 2020-09-02
 **/
@RestController
@RequestMapping(value = "/servicelevel")
public class ServiceLevelController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(ServiceLevelController.class);


    private Gson gson = new Gson();
    @Autowired
    private ServiceLevelService serviceLevelService;

    @RequestMapping(value = "/listservicelevel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean listServiceLevel(@RequestBody Map<String, Object> params) throws Exception {
        LOG.info("servicelevel/listservicelevel params==" + gson.toJson(params));
        Map<String, Object> resMap = serviceLevelService.listServiceLevel(params);
        if (null != resMap && null != resMap.get("code") && resMap.get("code").toString().equals("200")) {
            Object data = resMap.get("data");
            //JsonObject jsonObject = new JsonParser().parse(resMap.get("data").toString()).getAsJsonObject();//先转成json,则调用success()方法返回会报错
            return success(data);
        }
        String errMsg = resMap.get("message").toString();
        return failure(errMsg);
    }

    @RequestMapping(value = "/listStoragePoolsByServiceLevelId", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean listStoragePoolsByServiceLevelId(@RequestBody String serviceLevelId) throws Exception {
        LOG.info("servicelevel/listStoragePoolsByServiceLevelId params==" + serviceLevelId);
        String errMsg = "listStoragePoolsByServiceLevelId error, the serviceLevelId is: " + serviceLevelId;
        try {
            List<StoragePool> storagePoolList = serviceLevelService.getStoragePoolInfosByServiceLevelId(serviceLevelId);
            if (null != storagePoolList && storagePoolList.size() > 0) {
                return success(storagePoolList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return failure(errMsg);
    }

    @RequestMapping(value = "/listVolumesByServiceLevelId", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean listVolumesByServiceLevelId(@RequestBody String serviceLevelId) throws Exception {
        LOG.info("servicelevel/listVolumesByServiceLevelId params==" + serviceLevelId);
        String errMsg = "listVolumesByServiceLevelId error, the serviceLevel is: " + serviceLevelId;
        try {
            List<Volume> volumes = serviceLevelService.getVolumeInfosByServiceLevelId(serviceLevelId);
            if (null != volumes &&volumes.size()>0) {
                return success(volumes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return failure(errMsg);
    }
}
