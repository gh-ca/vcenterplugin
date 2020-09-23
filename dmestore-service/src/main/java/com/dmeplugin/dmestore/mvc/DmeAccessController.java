package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.DmeAccessService;
import com.dmeplugin.dmestore.utils.RestUtils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    private Gson gson=new Gson();
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
    public ResponseBodyBean accessDme(@RequestBody Map<String, Object> params)
            throws Exception {
        LOG.info("accessdme/access params==" + gson.toJson(params));
        Map<String, Object> remap = dmeAccessService.accessDme(params);
        LOG.info("accessdme/access remap==" + gson.toJson(remap));
        if (remap != null && remap.get(RestUtils.RESPONSE_STATE_CODE) != null
                && RestUtils.RESPONSE_STATE_200.equals(remap.get(RestUtils.RESPONSE_STATE_CODE).toString())) {
            return success(remap);
        }

        return failure(gson.toJson(remap));
    }


    /**
     * Refresh connection status
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/refreshaccess", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean refreshDme()
            throws Exception {
        LOG.info("accessdme/refreshaccess==");
        Map<String, Object> remap = dmeAccessService.refreshDme();
        LOG.info("accessdme/refreshaccess remap==" + gson.toJson(remap));
        if (remap != null && remap.get(RestUtils.RESPONSE_STATE_CODE) != null
                && RestUtils.RESPONSE_STATE_200.equals(remap.get(RestUtils.RESPONSE_STATE_CODE).toString())) {
            return success(remap);
        }

        return failure(gson.toJson(remap));
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
    public ResponseBodyBean getWorkLoads(@RequestParam("storageId") String storageId)
            throws Exception {
        LOG.info("accessdme/getworkloads storageId==" + storageId);
        String failureStr = "";
        try {
            List<Map<String, Object>> lists = dmeAccessService.getWorkLoads(storageId);
            LOG.info("getWorkLoads lists==" + gson.toJson(lists));
            return success(lists);
        } catch (Exception e) {
            LOG.error("get WorkLoads failure:", e);
            failureStr = "get WorkLoads failure:"+e.toString();
        }
        return failure(failureStr);
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
        String failureStr = "";
        try {
            dmeAccessService.scanDatastore(storageType);
            return success(null,"scan datastore complete!");
        } catch (Exception e) {
            LOG.error("scan datastore failure:", e);
            failureStr = "scan datastore failure:"+e.toString();
        }
        return failure(failureStr);
    }


}
