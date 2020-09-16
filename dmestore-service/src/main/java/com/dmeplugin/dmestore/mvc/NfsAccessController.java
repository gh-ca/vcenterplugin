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

    @Autowired
    private Gson gson;
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
            failureStr = e.getMessage();
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
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }


    /**
     * Mount nfs include:
     * list<Map<String,String>> dataStoreInfos: datastore信息列表 必
     * Map<String,String>中包含了
     * dataStoreName: datastore名称  必
     * fsId: fs id 必
     * shareId: share Id 必
     * logicPortId: logicPort Id 必
     * str host: 主机名称 必 （主机与集群二选一）
     * str cluster: 集群名称 必（主机与集群二选一）
     * str logic_port: 逻辑端口
     * str mountType: 挂载模式（只读或读写）  readOnly/readWrite
     *
     * @param params: include dataStoreInfos,host,cluster,logic_port,mountType
     * @return: ResponseBodyBean
     */
    @RequestMapping(value = "/mountnfs", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean mountNfs(@RequestBody Map<String, Object> params)
            throws Exception {
        LOG.info("accessnfs/mountvmfs=="+gson.toJson(params));
        String failureStr = "";
        try {
//            dmeNFSAccessService.mountVmfs(params);
            return success();
        }catch (Exception e){
            LOG.error("mount vmfs failure:", e);
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }

}
