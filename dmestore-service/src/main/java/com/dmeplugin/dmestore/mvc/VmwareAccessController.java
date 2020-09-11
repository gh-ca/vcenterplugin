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
   * Access hosts
   * return: host info
   */
    @RequestMapping(value = "/gethostsbydsname", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean getHostsByDsName(@RequestParam("dataStoreName") String dataStoreName) throws Exception {
        LOG.info("accessvmware/listhostbystoragename");
        String failureStr = "";
        try {
            List<Map<String, String>> lists = vmwareAccessService.getHostsByDsName(dataStoreName);
            LOG.info("getHostsByDsName vmware lists==" + gson.toJson(lists));
            return success(lists);
        } catch (Exception e) {
            LOG.error("getHostsByDsName vmware host failure:", e);
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
    @RequestMapping(value = "/getclustersbydsname", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean getClustersByDsName(@RequestParam("dataStoreName") String dataStoreName) throws Exception {
        LOG.info("accessvmware/listhostbystoragename");
        String failureStr = "";
        try {
            List<Map<String, String>> lists = vmwareAccessService.getClustersByDsName(dataStoreName);
            LOG.info("getClustersByDsName vmware lists==" + gson.toJson(lists));
            return success(lists);
        } catch (Exception e) {
            LOG.error("getClustersByDsName vmware host failure:", e);
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }


    /*
   * Access datastore
   * return: datastore info
   */
    @RequestMapping(value = "/getdatastoresbyhostname", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean getDataStoresByHostName(@RequestParam("hostName") String hostName) throws Exception {
        LOG.info("accessvmware/listhostbystoragename");
        String failureStr = "";
        try {
            List<Map<String, String>> lists = vmwareAccessService.getDataStoresByHostName(hostName);
            LOG.info("getDataStoresByHostName vmware lists==" + gson.toJson(lists));
            return success(lists);
        } catch (Exception e) {
            LOG.error("getDataStoresByHostName vmware host failure:", e);
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }

    /*
   * Access datastore
   * return: datastore info
   */
    @RequestMapping(value = "/getdatastoresbyclustername", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean getDataStoresByClusterName(@RequestParam("clusterName") String clusterName) throws Exception {
        LOG.info("accessvmware/listhostbystoragename");
        String failureStr = "";
        try {
            List<Map<String, String>> lists = vmwareAccessService.getDataStoresByClusterName(clusterName);
            LOG.info("getDataStoresByClusterName vmware lists==" + gson.toJson(lists));
            return success(lists);
        } catch (Exception e) {
            LOG.error("getDataStoresByClusterName vmware host failure:", e);
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }



}
