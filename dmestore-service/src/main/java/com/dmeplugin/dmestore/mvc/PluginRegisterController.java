package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.PluginRegisterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * OverviewController
 *
 * @author liugq
 * @since 2020-09-02
 **/
@RestController
@RequestMapping(value = "/registerservice")
public class PluginRegisterController extends BaseController {
    private static final Object LOCK = new Object();

    private static final String ACTION_INSTALL = "install";

    private static final String ACTION_UNINSTALL = "uninstall";

    @Autowired
    private PluginRegisterService pluginRegisterService;

    @RequestMapping(value = "/pluginaction", method = RequestMethod.POST)
    public ResponseBodyBean pluginaction(HttpServletRequest request, @RequestParam(name = "vcenterIP") String vcenterIp,
        @RequestParam String vcenterPort, @RequestParam String vcenterUsername, @RequestParam String vcenterPassword,
        @RequestParam(required = false) String action, @RequestParam(required = false) String removeData,
        @RequestParam String dmeIp, @RequestParam String dmePort, @RequestParam String dmeUsername,
        @RequestParam String dmePassword) {
        boolean isRemoveData;
        synchronized (LOCK) {
            try {
                if (ACTION_INSTALL.equals(action)) {
                    pluginRegisterService.installService(vcenterIp, vcenterPort, vcenterUsername, vcenterPassword,
                        dmeIp, dmePort, dmeUsername, dmePassword);
                }
                if (ACTION_UNINSTALL.equals(action)) {
                    // 调用接口，删除数据
                    isRemoveData = removeData != null && ("1".equals(removeData) || Boolean.valueOf(removeData));
                    if (isRemoveData) {
                        pluginRegisterService.uninstallService();
                    }
                }
                return success();
            } catch (DMEException e) {
                return failure(e.getMessage());
            }
        }
    }
}
