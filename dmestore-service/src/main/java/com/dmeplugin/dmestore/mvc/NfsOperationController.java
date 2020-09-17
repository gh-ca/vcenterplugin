package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.NfsOperationService;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/operatenfs")
@Api
public class NfsOperationController extends BaseController{

    public static final Logger LOG = LoggerFactory.getLogger(NfsOperationController.class);


    private Gson gson=new Gson();

    @Autowired
    private NfsOperationService nfsOperationService;



    /**
     *
     * @param params (String serverHost, int logicPort, String exportPath, String nfsName ,String accessMode,String mountHost )
     * @return
     */
    @PostMapping("/createnfsdatastore")
    @ResponseBody
    public ResponseBodyBean createNfsDatastore(@RequestBody Map<String,String> params){

        LOG.info("url:{/operatenfs/createnfsdatastore},"+gson.toJson(params));
        Map<String,Object> resMap = nfsOperationService.createNfsDatastore(params);
        if (null != resMap && null != resMap.get("code") && resMap.get("code").equals(200)) {
            return success(resMap);
        }
        return failure(gson.toJson(resMap));
    }

}
