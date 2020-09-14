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
    public ResponseBodyBean createRDM(@RequestParam("datacenter_name") String datacenter_name,
                                      @RequestParam("datastore_name") String datastore_name,
                                      @RequestParam("vm_name") String vm_name,
                                      @RequestParam("host_id") String host_id,
                                      @RequestBody VmRDMCreateBean createBean) throws Exception {
        try {
            vmrdmService.createRDM(datacenter_name, datastore_name, vm_name, host_id, createBean);
            return success();
        }catch (Exception e){
            LOG.error(e.getMessage());
            return failure(e.getMessage());
        }
    }
}
