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

/**
 * @Description: TODO
 * @ClassName: VmwareAccessController
 * @Company: GH-CA
 * @author: yy
 * @create: 2020-09-02
 **/
@RestController
@RequestMapping(value = "/accessvmware")
public class VmwareAccessController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(VmwareAccessController.class);


    private Gson gson = new Gson();
    @Autowired
    private VmwareAccessService vmwareAccessService;

    /**
     * Access hosts
     *
     * @return ResponseBodyBean List<Map<String, String>> include hostId hostName
     * @throws Exception when error
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
            failureStr = "list vmware host failure:"+e.toString();
        }
        return failure(failureStr);
    }

    /**
     * Access hosts
     *
     * @param  dataStoreObjectId dataStore ObjectId
     * @return ResponseBodyBean List<Map<String, String>> include hostId hostName
     * @throws Exception when error
     */
    @RequestMapping(value = "/gethostsbydsobjectid", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean getHostsByDsObjectId(@RequestParam("dataStoreObjectId") String dataStoreObjectId) throws Exception {
        LOG.info("accessvmware/listhostbystorageObjectId");
        String failureStr = "";
        try {
            List<Map<String, String>> lists = vmwareAccessService.getHostsByDsObjectId(dataStoreObjectId);
            LOG.info("getHostsByDsObjectId vmware lists==" + gson.toJson(lists));
            return success(lists);
        } catch (Exception e) {
            LOG.error("getHostsByDsObjectId vmware host failure:", e);
            failureStr = "getHostsByDsObjectId vmware host failure:"+e.toString();
        }
        return failure(failureStr);
    }

    /**
     * Access clusters
     *
     * @return ResponseBodyBean List<Map<String, String>> include clusterId clusterName
     * @throws Exception when error
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
            failureStr = "list vmware cluster failure:"+e.toString();
        }
        return failure(failureStr);
    }

    /**
     * Access clusters
     *
     * @param  dataStoreObjectId dataStore ObjectId
     * @return ResponseBodyBean List<Map<String, String>> include clusterId clusterName
     * @throws Exception when error
     */
    @RequestMapping(value = "/getclustersbydsobjectid", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean getClustersByDsObjectId(@RequestParam("dataStoreObjectId") String dataStoreObjectId) throws Exception {
        LOG.info("accessvmware/listhostbystorageObjectId");
        String failureStr = "";
        try {
            List<Map<String, String>> lists = vmwareAccessService.getClustersByDsObjectId(dataStoreObjectId);
            LOG.info("getClustersByDsObjectId vmware lists==" + gson.toJson(lists));
            return success(lists);
        } catch (Exception e) {
            LOG.error("getClustersByDsObjectId vmware host failure:", e);
            failureStr = "getClustersByDsObjectId vmware host failure:"+e.toString();
        }
        return failure(failureStr);
    }


    /**
     * Access datastore
     *
     * @param  hostObjectId host ObjectId
     * @param  dataStoreType dataStore Type
     * @return ResponseBodyBean List<Map<String, String>> include id name status type capacity freeSpace
     * @throws Exception when error
     */
    @RequestMapping(value = "/getdatastoresbyhostobjectid", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean getDataStoresByHostObjectId(@RequestParam("hostObjectId") String hostObjectId, @RequestParam("dataStoreType") String dataStoreType) throws Exception {
        LOG.info("accessvmware/listhostbystorageObjectId");
        String failureStr = "";
        try {
            List<Map<String, String>> lists = vmwareAccessService.getDataStoresByHostObjectId(hostObjectId, dataStoreType);
            LOG.info("getDataStoresByHostObjectId vmware lists==" + gson.toJson(lists));
            return success(lists);
        } catch (Exception e) {
            LOG.error("getDataStoresByHostObjectId vmware host failure:", e);
            failureStr = "getDataStoresByHostObjectId vmware host failure:"+e.toString();
        }
        return failure(failureStr);
    }

    /**
     * Access datastore
     *
     * @param  clusterObjectId cluster ObjectId
     * @param  dataStoreType dataStore Type
     * @return ResponseBodyBean List<Map<String, String>> include id name status type capacity freeSpace
     * @throws Exception when error
     */
    @RequestMapping(value = "/getdatastoresbyclusterobjectid", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean getDataStoresByClusterObjectId(@RequestParam("clusterObjectId") String clusterObjectId, @RequestParam("dataStoreType") String dataStoreType) throws Exception {
        LOG.info("accessvmware/listhostbystorageObjectId");
        String failureStr = "";
        try {
            List<Map<String, String>> lists = vmwareAccessService.getDataStoresByClusterObjectId(clusterObjectId, dataStoreType);
            LOG.info("getDataStoresByClusterObjectId vmware lists==" + gson.toJson(lists));
            return success(lists);
        } catch (Exception e) {
            LOG.error("getDataStoresByClusterObjectId vmware host failure:", e);
            failureStr = "getDataStoresByClusterObjectId vmware host failure:"+e.toString();
        }
        return failure(failureStr);
    }



}
