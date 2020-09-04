package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.model.Storage;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author lianq
 * @className DmeStoragesController
 * @description TODO
 * @date 2020/9/3 17:43
 */

@RestController
@RequestMapping("/dmestorage")
public class DmeStorageController extends BaseController{

    public static final Logger LOG = LoggerFactory.getLogger(NfsAccessController.class);

    @Autowired
    private Gson gson;

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
