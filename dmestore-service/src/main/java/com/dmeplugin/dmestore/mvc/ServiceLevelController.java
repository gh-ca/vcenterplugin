package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.exception.DmeException;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.model.StoragePool;
import com.dmeplugin.dmestore.model.Volume;
import com.dmeplugin.dmestore.services.ServiceLevelService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * ServiceLevelController
 *
 * @author liuxh
 * @since 2020-09-02
 **/
@RestController
@RequestMapping(value = "/servicelevel")
public class ServiceLevelController extends BaseController {
    /**
     * LOG
     **/
    public static final Logger LOG = LoggerFactory.getLogger(ServiceLevelController.class);

    @Autowired
    private ServiceLevelService serviceLevelService;

    /**
     * listServiceLevel
     *
     * @param params params
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/listservicelevel", method = RequestMethod.POST)
    public ResponseBodyBean listServiceLevel(@RequestBody Map<String, Object> params) {
        try {
            return success(serviceLevelService.listServiceLevel(params));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * listStoragePoolsByServiceLevelId
     *
     * @param serviceLevelId serviceLevelId
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/listStoragePoolsByServiceLevelId", method = RequestMethod.POST)
    public ResponseBodyBean listStoragePoolsByServiceLevelId(@RequestBody String serviceLevelId) {
        String errMsg = "";
        try {
            List<StoragePool> storagePoolList = serviceLevelService.getStoragePoolInfosByServiceLevelId(serviceLevelId);
            if (null != storagePoolList && storagePoolList.size() > 0) {
                return success(storagePoolList);
            }
        } catch (DmeException e) {
            errMsg = e.getMessage();
        }
        return failure(errMsg);
    }

    /**
     * listVolumesByServiceLevelId
     *
     * @param serviceLevelId serviceLevelId
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/listVolumesByServiceLevelId", method = RequestMethod.POST)
    public ResponseBodyBean listVolumesByServiceLevelId(@RequestBody String serviceLevelId) {
        String errMsg = "";
        try {
            List<Volume> volumes = serviceLevelService.getVolumeInfosByServiceLevelId(serviceLevelId);
            if (null != volumes && volumes.size() > 0) {
                return success(volumes);
            }
        } catch (DmeException e) {
            errMsg = e.getMessage();
        }
        return failure(errMsg);
    }

    /**
     * manual update service level
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/manualupdate", method = RequestMethod.POST)
    public ResponseBodyBean manualupdate() {
        try {
            serviceLevelService.updateVmwarePolicy();
            return success();
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }
}
