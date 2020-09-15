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
        LOG.info("accessvmfs/listnfs");
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
        LOG.info("accessvmfs/listnfsperformance fsIds==" + gson.toJson(fsIds));
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
     * list<str> dataStoreNames: datastore名称列表 必
     * list<str> volumeIds: 卷volumeId列表 必
     * str host: 主机名称 必 （主机与集群二选一）
     * str cluster: 集群名称 必（主机与集群二选一）
     * str logic_port: 逻辑端口
     * str mountType: 挂载模式（只读或读写）
     *
     * @param params: include dataStoreNames,volumeIds,host,cluster,logic_port,mountType
     * @return: ResponseBodyBean
     */
    @RequestMapping(value = "/mountnfs", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean mountNfs(@RequestBody Map<String, Object> params)
            throws Exception {
        LOG.info("accessnfs/mountnfs==" + gson.toJson(params));
        String re = "";
        return success(re);
    }

}
