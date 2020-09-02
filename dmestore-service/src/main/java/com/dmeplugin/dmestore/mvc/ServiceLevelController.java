package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.ServiceLevelService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description: TODO
 * @ClassName: ServiceLevelAccessController
 * @Company: GH-CA
 * @author: liuxh
 * @create: 2020-09-02
 **/
@RestController
@RequestMapping(value = "/accessservicelevel")
public class ServiceLevelController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(ServiceLevelController.class);

    @Autowired
    private Gson gson;
    @Autowired
    private ServiceLevelService serviceLevelService;

    @RequestMapping(value = "/listservicelevel", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean listServiceLevel(@RequestBody Map<String, Object> params) throws Exception {
        LOG.info("accessservicelevel/listservicelevel params==" + gson.toJson(params));
        Map<String, Object> resMap = serviceLevelService.listServiceLevel(params);
        if (null != resMap && null != resMap.get("code") && resMap.get("code").equals("200")) {
            return success(resMap);
        }
        return failure(gson.toJson(resMap));
    }
}
