package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.VmwareAccessService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/accessvmware")
public class VmwareAccessController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(VmwareAccessController.class);

    @Autowired
    private Gson gson = new Gson();
    @Autowired
    private VmwareAccessService vmwareAccessService;

    /*
   * Access hosts
   * return: host info
   */
    @RequestMapping(value = "/listhost", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean listHosts() throws Exception {
        LOG.info("accessvmware/listhost");
        String failureStr = "";
        try {
            List<Map<String, String>> lists = vmwareAccessService.listHosts();
            LOG.info("listhost vmware lists==" + gson.toJson(lists));
            return success(lists);
        } catch (Exception e) {
            LOG.error("list vmware host failure:", e);
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }

    /*
   * Access cluster
   * return: cluster info
   */
    @RequestMapping(value = "/listcluster", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean listClusters() throws Exception {
        LOG.info("accessvmware/listcluster");
        String failureStr = "";
        try {
            List<Map<String, String>> lists = vmwareAccessService.listClusters();
            LOG.info("listcluster vmware lists==" + gson.toJson(lists));
            return success(lists);
        } catch (Exception e) {
            LOG.error("list vmware cluster failure:", e);
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }

    /*
   * Access cluster
   * return: cluster info
   */
    @RequestMapping(value = "/getlunsonhost", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean getLunsOnHost(@RequestParam("hostName") String hostName) throws Exception {
        LOG.info("accessvmware/getlunsonhost");
        String failureStr = "";
        try {
            List<Map<String, String>> lists = vmwareAccessService.getLunsOnHost(hostName);
            LOG.info("getlunsonhost vmware lists==" + gson.toJson(lists));
            return success(lists);
        } catch (Exception e) {
            LOG.error("get luns failure:", e);
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }


}
