package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.DmeAccessService;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @ClassName: DmeAccessController
 * @Company: GH-CA
 * @author: yy
 * @create: 2020-09-02
 **/

@RestController
@RequestMapping(value = "/accessdme")
public class DmeAccessController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(DmeAccessController.class);

    private Gson gson = new Gson();
    @Autowired
    private DmeAccessService dmeAccessService;
    /**
     * Access DME
     * params include
     * hostIp: Access to the IP address of the DME service
     * hostPort: Port to access DME service
     * userName: User name to access the DME service
     * password: Password to access the DME service
     *
     * @param params: include hostIp,hostPort,userName,password
     * @return: ResponseBodyBean
     */
    @RequestMapping(value = "/access", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean accessDme(@RequestBody Map<String, Object> params) {
        LOG.info("accessdme/access params==" + gson.toJson(params));
        try {
            dmeAccessService.accessDme(params);
            return success();
        } catch (Exception e) {
            return failure(e.getMessage());
        }
    }
    /**
     * Refresh connection status
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/refreshaccess", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean refreshDme() {
        LOG.info("accessdme/refreshaccess==");
        try {
            return success(dmeAccessService.refreshDme());
        } catch (Exception e) {
            return failure(e.getMessage());
        }

    }

    /**
     * Access workload info
     *
     * @param storageId storage id
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    @RequestMapping(value = "/getworkloads", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean getWorkLoads(@RequestParam("storageId") String storageId) {
        LOG.info("accessdme/getworkloads storageId==" + storageId);
        try {
            List<Map<String, Object>> lists = dmeAccessService.getWorkLoads(storageId);
            return success(lists);
        } catch (Exception e) {
            return failure("get WorkLoads failure:" + e.toString());
        }
    }
    /**
     * scan Datastore
     *
     * @param storageType storage type:VMFS,NFS,ALL
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    @RequestMapping(value = "/scandatastore", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean scanDatastore(@RequestParam("storageType") String storageType)
        throws Exception {
        LOG.info("accessdme/scandatastore storageId==" + storageType);
        try {
            dmeAccessService.scanDatastore(storageType);
            return success(null, "scan datastore complete!");
        } catch (Exception e) {
            return failure("scan datastore failure:" + e.toString());
        }
    }
    /**
     * Configure task time
     * @param taskId   task Id
     * @param taskCron task cron
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    @RequestMapping(value = "/configuretasktime", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean configureTaskTime(@RequestParam("taskId") Integer taskId,
                                              @RequestParam("taskCron") String taskCron)
        throws Exception {
        LOG.info("accessdme/configuretasktime taskId==" + taskId + "  taskCron==" + taskCron);
        try {
            dmeAccessService.configureTaskTime(taskId, taskCron);
            return success(null, "configure task time complete!");
        } catch (Exception e) {
            return failure("configure task time failure:" + e.toString());
        }
    }
}
