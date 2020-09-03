package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.Storage;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.DmeAccessService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
        LOG.info("accessdme/access params==" + params);
        Map<String, Object> remap = dmeAccessService.accessDme(params);
        LOG.info("accessdme/access remap==" + remap);
        if (remap != null && remap.get("code") != null && remap.get("code").equals("200")) {
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
        Map<String, Object> remap = dmeAccessService.refreshDme();
        LOG.info("accessdme/access remap==" + remap);
        if (remap != null && remap.get("code") != null && remap.get("code").equals("200")) {
            return success(remap);
        }

        return failure(gson.toJson(remap));
    }

    /**
     * query storage list
     * @param params
     * @return
     */

    @GetMapping("/storages")
    @ResponseBody
    public ResponseBodyBean getStorages(@RequestBody Map<String,String> params){

        LOG.info("storages ==" + gson.toJson(params) );

        //String url = "/rest/storagemgmt/v1/storages?start="+ start + "&limit=" + limit;
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON_UTF8);
        //ResponseEntity<String> stringResponseEntity = HttpRequestUtil.requestWithBody(url, HttpMethod.GET,
        //header, null, String.class);
        Storage storage = new Storage();
        return success(storage);
    }

    /**
     * search oriented storage
     * @param storage_id required
     * @return
     */
    @GetMapping("/storage")
    @ResponseBody
    public ResponseBodyBean getStorage(@RequestParam(name = "storage_id") String storage_id){

        LOG.info("storage_id ==" + storage_id );
        String url = "/rest/storagemgmt/v1/storages/{ "+ storage_id+ "}/detail";
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON_UTF8);
        //ResponseEntity<String> stringResponseEntity = HttpRequestUtil.requestWithBody(url, HttpMethod.GET,
        //        header, null, String.class);
        //根据storage_id 分别去查, 池 ,卷 ,fs, dt, shares ,user authentication

        return success();
    }

}
