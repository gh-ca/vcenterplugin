package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.NfsDataInfo;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.DmeNFSAccessService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @ClassName: NfsAccessController
 * @Company: GH-CA
 * @author: yy
 * @create: 2020-09-02
 **/
@RestController
@RequestMapping(value = "/accessnfs")
public class NfsAccessController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(NfsAccessController.class);


    private Gson gson=new Gson();
    @Autowired
    private DmeNFSAccessService dmeNFSAccessService;

    /**
     * Access nfs
     *
     * @return: ResponseBodyBean
     */
    @RequestMapping(value = "/listnfs", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean listNfs()
            throws Exception {
        LOG.info("accessnfs/listnfs");
        String failureStr = "";
        try {
            List<NfsDataInfo> lists = dmeNFSAccessService.listNfs();
            LOG.info("listnfs lists==" + gson.toJson(lists));
            return success(lists);
        } catch (Exception e) {
            LOG.error("list nfs failure:", e);
            failureStr = "list nfs failure:"+e.toString();
        }
        return failure(failureStr);
    }

    /**
     * Access nfs performance
     *
     * @param fsIds fs id
     * @return: ResponseBodyBean
     */
    @RequestMapping(value = "/listnfsperformance", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean listNfsPerformance(@RequestParam("fsIds") List<String> fsIds)
            throws Exception {
        LOG.info("accessnfs/listnfsperformance fsIds==" + gson.toJson(fsIds));
        String failureStr = "";
        try {
            List<NfsDataInfo> lists = dmeNFSAccessService.listNfsPerformance(fsIds);
            LOG.info("listnfsperformance lists==" + gson.toJson(lists));
            return success(lists);
        } catch (Exception e) {
            LOG.error("get nfs performance failure:", e);
            failureStr = "get nfs performance failure:"+e.toString();
        }
        return failure(failureStr);
    }


    /**
     * Mount nfs,params中包含了 include:
     * dataStoreObjectId: datastore的object id
     * dataStoreName: datastore名称  必
     * list<map<str,str>> hosts: 主机hostId,主机名称hostName 必 （主机与集群二选一）
     * list<map<str,str>>  clusters: 集群clusterId,集群名称clusterName 必（主机与集群二选一）
     * str mountType: 挂载模式（只读或读写）  readOnly/readWrite
     *
     * @param params: include dataStoreName,hosts,clusters,mountType
     * @return: ResponseBodyBean
     */
    @RequestMapping(value = "/mountnfs", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean mountNfs(@RequestBody Map<String, Object> params)
            throws Exception {
        LOG.info("accessnfs/mountnfs=="+gson.toJson(params));
        String failureStr = "";
        try {
            dmeNFSAccessService.mountNfs(params);
            return success(null,"Mount nfs success");
        }catch (Exception e){
            LOG.error("mount nfs failure:", e);
            failureStr = "mount nfs failure:"+e.toString();
        }
        return failure(failureStr);
    }


    /**
     * Mount nfs,params中包含了 include:
     * dataStoreObjectId: datastore的object id
     * hostId: 主机hostId 必 （主机与集群二选一）
     * clusterId: 集群clusterId 必（主机与集群二选一）
     * @param params: include dataStoreName,hostId,clusterId
     * @return: ResponseBodyBean
     */
    @RequestMapping(value = "/unmountnfs", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean unmountNfs(@RequestBody Map<String, Object> params) throws Exception {
        LOG.info("accessnfs/unmountnfs=="+gson.toJson(params));
        String failureStr = "";
        try {
            dmeNFSAccessService.unmountNfs(params);
            return success(null,"unmount nfs success");
        }catch (Exception e){
            LOG.error("unmount nfs failure:", e);
            failureStr = "unmount nfs failure:"+e.toString();
        }
        return failure(failureStr);
    }


}
