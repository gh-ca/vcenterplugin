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
    public ResponseBodyBean createRDM(@RequestParam("host_objectId") String host_id,
                                      @RequestParam("vm_objectId") String vm_objectId,
                                      @RequestBody VmRDMCreateBean createBean,
                                      @RequestParam("data_store_name") String data_store_name) throws Exception {
        try {
            vmrdmService.createRDM(data_store_name, vm_objectId, host_id, createBean);
            return success();
        }catch (Exception e){
            LOG.error(e.getMessage());
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "dmeHosts", method = RequestMethod.GET)
    public ResponseBodyBean dmeHosts() throws Exception {
        try {
            return success(vmrdmService.getAllDmeHost());
        }catch (Exception e){
            LOG.error(e.getMessage());
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "vCenter/datastoreOnHost", method = RequestMethod.GET)
    public ResponseBodyBean getDatastoreMountsOnHost(@RequestParam("host_id") String host_id) throws Exception {
        try {
            return success(vmrdmService.getDatastoreMountsOnHost(host_id));
        }catch (Exception e){
            LOG.error(e.getMessage());
            return failure(e.getMessage());
        }
    }
}
