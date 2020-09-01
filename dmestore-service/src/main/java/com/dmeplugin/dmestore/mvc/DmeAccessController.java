package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/accessdme")
public class DmeAccessController extends BaseController{
    public static final Logger LOG = LoggerFactory.getLogger(DmeAccessController.class);

    @Autowired
    private Gson gson;

    /*
    * Access DME
    * param str ip: Access to the IP address of the DME service
    * param str port: Port to access DME service
    * param str username: User name to access the DME service
    * param str password: Password to access the DME service
    * return: ResponseBodyBean
    */
    @RequestMapping(value = "/access", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean accessDme(@RequestBody Map<String, Object> params)
            throws Exception {
        LOG.info("accessdme/access params==" + gson.toJson(params));
        String re = "";
        //service

        return success(re);
    }
    /*
    * Refresh connection status
    * return: ResponseBodyBean
    */
    @RequestMapping(value = "/refreshaccess", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean refreshDme()
            throws Exception {
        String re = "";

        return success(re);
    }


}
