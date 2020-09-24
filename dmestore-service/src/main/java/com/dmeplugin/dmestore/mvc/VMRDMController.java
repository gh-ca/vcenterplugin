package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.model.VmRDMCreateBean;
import com.dmeplugin.dmestore.services.VMRDMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "v1/vmrdm")
public class VMRDMController extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(VMRDMController.class);

    @Autowired
    private VMRDMService vmrdmService;

    @RequestMapping(value = "createRdm", method = RequestMethod.POST)
    public ResponseBodyBean createRDM(@RequestParam("datastore_objectId") String datastore_objectId,
                                      @RequestParam("host_objectId") String host_objectId,
                                      @RequestParam("vm_objectId") String vm_objectId,
                                      @RequestBody VmRDMCreateBean createBean) throws Exception {
        try {
            vmrdmService.createRDM(datastore_objectId, vm_objectId, host_objectId, createBean);
            return success();
        }catch (Exception e){
            LOG.error(e.getMessage());
            return failure(e.getMessage());
        }
    }
}
