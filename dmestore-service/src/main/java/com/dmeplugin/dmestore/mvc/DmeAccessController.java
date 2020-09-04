package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.DmeAccessService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping(value = "/accessdme")
public class DmeAccessController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(DmeAccessController.class);

    @Autowired
    private Gson gson;
    @Autowired
    private DmeAccessService dmeAccessService;

    /*
    * Access DME
    * param str hostIp: Access to the IP address of the DME service
    * param str hostPort: Port to access DME service
    * param str userName: User name to access the DME service
    * param str password: Password to access the DME service
    * return: ResponseBodyBean
    */
    @RequestMapping(value = "/access", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean accessDme(@RequestBody Map<String, Object> params)
            throws Exception {
        LOG.info("accessdme/access params==" + gson.toJson(params));
        Map<String, Object> remap = dmeAccessService.accessDme(params);
        LOG.info("accessdme/access remap==" + gson.toJson(remap));
        if (remap != null && remap.get("code") != null && remap.get("code").toString().equals("200")) {
            return success(remap);
        }

        return failure(gson.toJson(remap));
    }

    /*
    * Refresh connection status
    * return: ResponseBodyBean
    */
    @RequestMapping(value = "/refreshaccess", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean refreshDme()
            throws Exception {
        LOG.info("accessdme/refreshaccess==");
        Map<String, Object> remap = dmeAccessService.refreshDme();
        LOG.info("accessdme/refreshaccess remap==" + gson.toJson(remap));
        if (remap != null && remap.get("code") != null && remap.get("code").toString().equals("200")) {
            return success(remap);
        }

        return failure(gson.toJson(remap));
    }


}
