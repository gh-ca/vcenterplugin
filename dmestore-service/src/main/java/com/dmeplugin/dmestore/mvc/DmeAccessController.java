package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.DmeAccessService;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * DmeAccessController
 *
 * @author yy
 * @since 2020-12-01
 **/

@RestController
@RequestMapping(value = "/accessdme")
public class DmeAccessController extends BaseController {
    /**
     * LOG
     */
    public static final Logger LOG = LoggerFactory.getLogger(DmeAccessController.class);

    private Gson gson = new Gson();

    @Autowired
    private DmeAccessService dmeAccessService;

    @RequestMapping(value = "/access", method = RequestMethod.POST)
    public ResponseBodyBean accessDme(@RequestBody Map<String, Object> params) {
        LOG.info("accessdme/access params=={}", gson.toJson(params));
        try {
            dmeAccessService.accessDme(params);
            return success();
        } catch (DMEException e) {
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "/refreshaccess", method = RequestMethod.GET)
    public ResponseBodyBean refreshDme() {
        LOG.info("accessdme/refreshaccess==");
        try {
            return success(dmeAccessService.refreshDme());
        } catch (DMEException e) {
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "/getworkloads", method = RequestMethod.GET)
    public ResponseBodyBean getWorkLoads(@RequestParam("storageId") String storageId) {
        try {
            List<Map<String, Object>> lists = dmeAccessService.getWorkLoads(storageId);
            return success(lists);
        } catch (DMEException e) {
            return failure("get WorkLoads failure:" + e.toString());
        }
    }

    @RequestMapping(value = "/scandatastore", method = RequestMethod.GET)
    public ResponseBodyBean scanDatastore(@RequestParam("storageType") String storageType) {
        try {
            dmeAccessService.scanDatastore(storageType);
            return success(null, "scan datastore complete!");
        } catch (DMEException e) {
            return failure("scan datastore failure:" + e.toString());
        }
    }

    @RequestMapping(value = "/configuretasktime", method = RequestMethod.GET)
    public ResponseBodyBean configureTaskTime(@RequestParam("taskId") Integer taskId,
        @RequestParam("taskCron") String taskCron) {
        try {
            dmeAccessService.configureTaskTime(taskId, taskCron);
            return success(null, "configure task time complete!");
        } catch (DMEException e) {
            return failure("configure task time failure:" + e.toString());
        }
    }
}
