package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.DmeAccessService;
import com.dmeplugin.dmestore.services.SystemService;
import com.dmeplugin.dmestore.services.VmwareAccessService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/registerservice")
public class PluginRegisterController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(PluginRegisterController.class);
    private static Object lock=new Object();

    private Gson gson = new Gson();

    @Autowired
    private DmeAccessService dmeAccessService;

    @Autowired
    private SystemService systemService;


    @RequestMapping(value = "/pluginaction", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean pluginaction(HttpServletRequest request,
                                          @RequestParam String vcenterUsername, @RequestParam String vcenterPassword,
                                          @RequestParam(required = false) String action,
                                          @RequestParam(required = false) String removeData,
                                         @RequestParam String dmeIp,
                                         @RequestParam String dmePort,
                                         @RequestParam String dmeUsername,
                                         @RequestParam String dmePassword) throws Exception {
        LOG.info("registerservice/pluginaction");
        String failureStr = "";
        boolean isRemoveData = false;
        synchronized (lock) {
            try {
                if ("install".equals(action)) {
                    //调用接口，创建dme连接信息
                    Map params = new HashMap();
                    params.put("hostIp", dmeIp);
                    params.put("hostPort", dmePort);
                    params.put("userName", dmeUsername);
                    params.put("password", dmePassword);
                    dmeAccessService.accessDme(params);
                }
                if ("uninstall".equals(action)) {
                    //调用接口，删除数据
                    isRemoveData = (removeData != null && ("1".equals(removeData) || Boolean
                            .valueOf(removeData)));
                    if (isRemoveData)
                        systemService.cleanData();
                }
                return success();
            } catch (Exception e) {
                LOG.error("installplugin for dme failure:", e);
                failureStr = e.getMessage();
            }
        }
        return failure(failureStr);
    }



}
