package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/bestpractise")
public class DMEBestPractiseController extends BaseController{
    /**
     * manual update best practise
     */
    @RequestMapping(value = "/manualupdate", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean manualupdate()
            throws Exception {

        return success();
    }
}
