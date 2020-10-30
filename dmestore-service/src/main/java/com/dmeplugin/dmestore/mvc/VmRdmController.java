package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.model.VmRdmCreateBean;
import com.dmeplugin.dmestore.services.VmRdmService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangxiangyong
 */
@RestController
@RequestMapping(value = "v1/vmrdm")
public class VmRdmController extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(VmRdmController.class);

    @Autowired
    private VmRdmService vmRdmService;

    @RequestMapping(value = "createRdm", method = RequestMethod.POST)
    public ResponseBodyBean createRdm(@RequestParam("vmObjectId") String vmObjectId,
                                      @RequestBody VmRdmCreateBean createBean,
                                      @RequestParam("dataStoreObjectId") String dataStoreObjectId) throws Exception {
        try {
            vmRdmService.createRdm(dataStoreObjectId, vmObjectId, createBean);
            return success();
        }catch (Exception e){
            LOG.error(e.getMessage());
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "dmeHosts", method = RequestMethod.GET)
    public ResponseBodyBean dmeHosts() throws Exception {
        try {
            return success(vmRdmService.getAllDmeHost());
        }catch (Exception e){
            LOG.error(e.getMessage());
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "vCenter/datastoreOnHost", method = RequestMethod.GET)
    public ResponseBodyBean getDatastoreMountsOnHost(@RequestParam("vmObjectId") String vmObjectId) throws Exception {
        try {
            List<Object> objects = vmRdmService.getDatastoreMountsOnHost(vmObjectId);

            return success(new Gson().toJson(objects));
        }catch (Exception e){
            LOG.error(e.getMessage());
            return failure(e.getMessage());
        }
    }
}
