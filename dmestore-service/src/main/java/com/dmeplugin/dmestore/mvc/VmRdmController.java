package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.model.VmRdmCreateBean;
import com.dmeplugin.dmestore.services.VmRdmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wangxiangyong
 * @version 1.0.0
 * @ClassName VmRdmController.java
 * @Description TODO
 * @createTime 2020年09月09日 10:58:00
 */
@RestController
@RequestMapping(value = "v1/vmrdm")
public class VmRdmController extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(VmRdmController.class);

    @Autowired
    private VmRdmService vmRdmService;

    @RequestMapping(value = "createRdm", method = RequestMethod.POST)
    public ResponseBodyBean createRdm(@RequestParam("hostId") String hostId,
                                      @RequestParam("vmObjectId") String vmObjectId,
                                      @RequestBody VmRdmCreateBean createBean,
                                      @RequestParam("dataStoreName") String dataStoreName) throws Exception {
        try {
            vmRdmService.createRdm(dataStoreName, vmObjectId, hostId, createBean);
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
    public ResponseBodyBean getDatastoreMountsOnHost(@RequestParam("hostId") String hostId) throws Exception {
        try {
            return success(vmRdmService.getDatastoreMountsOnHost(hostId));
        }catch (Exception e){
            LOG.error(e.getMessage());
            return failure(e.getMessage());
        }
    }
}
