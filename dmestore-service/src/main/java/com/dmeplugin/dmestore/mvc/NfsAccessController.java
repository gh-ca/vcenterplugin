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
@RequestMapping(value = "/accessnfs")
public class NfsAccessController extends BaseController{
    public static final Logger LOG = LoggerFactory.getLogger(NfsAccessController.class);

    @Autowired
    private Gson gson;

    /*
   * Access nfs
   * return: ResponseBodyBean
   */
    @RequestMapping(value = "/listnfs", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean listNfs()
            throws Exception {
        LOG.info("accessnfs/listnfs");
        String re = "";
        return success(re);
    }


    /*
   * Mount nfs
   * param list<str> volume_ids: 卷id列表 必
   * param str host_id: 主机id 必
   * return: ResponseBodyBean
   */
    @RequestMapping(value = "/mountnfs", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean mountNfs(@RequestBody Map<String, Object> params)
            throws Exception {
        LOG.info("accessnfs/mountnfs=="+gson.toJson(params));
        String re = "";
        return success(re);
    }


}
