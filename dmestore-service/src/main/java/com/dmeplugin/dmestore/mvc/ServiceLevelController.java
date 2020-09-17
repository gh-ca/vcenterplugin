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
 * @ClassName: ServiceLevelController
 * @Company: GH-CA
 * @author: liuxh
 * @create: 2020-09-02
 **/
@RestController
@RequestMapping(value = "/servicelevel")
public class ServiceLevelController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(ServiceLevelController.class);


    private Gson gson=new Gson();
    @Autowired
    private ServiceLevelService serviceLevelService;

    @RequestMapping(value = "/listservicelevel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean listServiceLevel(@RequestBody Map<String, Object> params) throws Exception {
        LOG.info("servicelevel/listservicelevel params==" + gson.toJson(params));
        Map<String, Object> resMap = serviceLevelService.listServiceLevel(params);
        if (null != resMap && null != resMap.get("code") && resMap.get("code").toString().equals("200")) {
            Object data = resMap.get("data");
            //JsonObject jsonObject = new JsonParser().parse(resMap.get("data").toString()).getAsJsonObject();//先转成json,则调用success()方法返回会报错
            return success(data);
        }
        String errMsg = resMap.get("message").toString();
        return failure(errMsg);
    }
}
