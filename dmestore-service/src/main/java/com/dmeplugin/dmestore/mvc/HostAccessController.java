package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.EthPortInfo;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.DmeNFSAccessService;
import com.dmeplugin.dmestore.services.HostAccessService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @ClassName: HostAccessController
 * @Company: GH-CA
 * @author: yy
 * @create: 2020-09-02
 **/
@RestController
@RequestMapping(value = "/accesshost")
public class HostAccessController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(HostAccessController.class);


    private Gson gson = new Gson();
    @Autowired
    private HostAccessService hostAccessService;

    /**
     * Configure the host with iSCSI, params include:
     * String hostObjectId：host object id
     * Map<String, String> vmKernel：  选择的vmkernel
     * List<Map<String, Object>> ethPorts: 选择的以太网端口列表
     *
     * @param params: params include:hostObjectId,vmKernel,ethPorts
     * @return: ResponseBodyBean
     * @throws Exception when error
     */
    @RequestMapping(value = "/configureiscsi", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean configureIscsi(@RequestBody Map<String, Object> params)
            throws Exception {
        LOG.info("/configureiscsi==" + gson.toJson(params));
        String failureStr = "";
        try {
            hostAccessService.configureIscsi(params);
            return success(null, "configure iscsi success");
        } catch (Exception e) {
            LOG.error("configure iscsi failure:", e);
            failureStr = "configure iscsi failure:" + e.toString();
        }
        return failure(failureStr);
    }

    /**
     * Test Connectivity:
     * String hostObjectId：host object id
     * List<Map<String, Object>> ethPorts: 要测试的以太网端口列表
     * Map<String, String> vmKernel: 虚拟网卡信息
     *
     * @param params: params include:hostObjectId,ethPorts，vmKernel
     * @return: ResponseBodyBean
     * @throws Exception when error
     */
    @RequestMapping(value = "/testconnectivity", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean testConnectivity(@RequestBody Map<String, Object> params)
            throws Exception {
        LOG.info("/testconnectivity==" + gson.toJson(params));
        String failureStr = "";
        try {
            List<EthPortInfo> lists = hostAccessService.testConnectivity(params);
            return success(lists);
        } catch (Exception e) {
            LOG.error("Test connectivity failure:", e);
            failureStr = "Test connectivity failure:" + e.toString();
        }
        return failure(failureStr);
    }

}
