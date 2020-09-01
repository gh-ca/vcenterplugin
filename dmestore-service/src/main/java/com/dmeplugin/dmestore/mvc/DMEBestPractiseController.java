package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    /**
     * second entry for apply best practise on host
     */
    @RequestMapping(value = "/manualapplyforhost", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean applyforhost(@RequestParam(value = "hostid", required = true) String hostid)
            throws Exception {

        return success();
    }

    /**
     * second entry for apply best practise on cluster
     */
    @RequestMapping(value = "/manualapplyforcluster", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean applyforcluster(@RequestParam(value = "clusterid", required = true) String clusterid)
            throws Exception {

        return success();
    }
}
