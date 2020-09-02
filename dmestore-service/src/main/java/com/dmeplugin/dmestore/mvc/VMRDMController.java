package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.model.VmRDMCreateBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "v1/vmrdm")
public class VMRDMController extends BaseController{
    @RequestMapping(method = RequestMethod.POST)
    public ResponseBodyBean createRDM(@RequestBody VmRDMCreateBean createBean) throws Exception {

        return null;
    }
}
