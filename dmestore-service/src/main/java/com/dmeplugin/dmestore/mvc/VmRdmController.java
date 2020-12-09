package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.model.VmRdmCreateBean;
import com.dmeplugin.dmestore.services.VmRdmService;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * VmRdmController
 *
 * @author wangxiangyong
 * @since 2020-09-03
 **/
@RestController
@RequestMapping(value = "v1/vmrdm")
public class VmRdmController extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(VmRdmController.class);

    @Autowired
    private VmRdmService vmRdmService;

    @RequestMapping(value = "createRdm", method = RequestMethod.POST)
    public ResponseBodyBean createRdm(@RequestParam("vmObjectId") String vmObjectId,
        @RequestBody VmRdmCreateBean createBean, @RequestParam("dataStoreObjectId") String dataStoreObjectId) {
        try {
            vmRdmService.createRdm(dataStoreObjectId, vmObjectId, createBean);
            return success();
        } catch (DMEException e) {
            LOG.error(e.getMessage());
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "dmeHosts", method = RequestMethod.GET)
    public ResponseBodyBean dmeHosts() {
        try {
            return success(vmRdmService.getAllDmeHost());
        } catch (DMEException e) {
            LOG.error(e.getMessage());
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "vCenter/datastoreOnHost", method = RequestMethod.GET)
    public ResponseBodyBean getDatastoreMountsOnHost(@RequestParam("vmObjectId") String vmObjectId) {
        try {
            List<Object> objects = vmRdmService.getDatastoreMountsOnHost(vmObjectId);
            return success(new Gson().toJson(objects));
        } catch (DMEException e) {
            LOG.error(e.getMessage());
            return failure(e.getMessage());
        }
    }
}
