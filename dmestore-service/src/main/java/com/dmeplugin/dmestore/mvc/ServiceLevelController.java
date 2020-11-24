package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.exception.DMEException;
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
    public ResponseBodyBean listServiceLevel(@RequestBody Map<String, Object> params)   {
        LOG.info("servicelevel/listservicelevel params==" + gson.toJson(params));
        try {
            return success(serviceLevelService.listServiceLevel(params));
        } catch (DMEException e) {
            return failure(e.getMessage());
        }

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

    //查询服务等级下的卷
    @RequestMapping(value = "/listVolumesByServiceLevelId", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean listVolumesByServiceLevelId(@RequestBody String serviceLevelId) throws DMEException {
        LOG.info("servicelevel/listVolumesByServiceLevelId params==" + serviceLevelId);
        String errMsg = "listVolumesByServiceLevelId error, the serviceLevel is: " + serviceLevelId;
        try {
            List<Volume> volumes = serviceLevelService.getVolumeInfosByServiceLevelId(serviceLevelId);
            if (null != volumes && volumes.size() > 0) {
                return success(volumes);
            }
        } catch (Exception e) {
            throw new DMEException(e.getMessage());
        }
        return failure(errMsg);
    }

    /**
     * manual update service level
     */
    @RequestMapping(value = "/manualupdate", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean manualupdate() {
        try {
            serviceLevelService.updateVmwarePolicy();
            return success();
        } catch (DMEException e) {
            return failure(e.getMessage());
        }
    }
    //查询服务等级下的存储池
}
