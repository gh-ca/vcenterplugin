package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.model.EthPortInfo;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.model.Storage;
import com.dmeplugin.dmestore.model.StoragePool;
import com.dmeplugin.dmestore.services.DmeStorageService;
import com.dmeplugin.dmestore.utils.ToolUtils;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author lianq
 * @className DmeStorageController
 * @description TODO
 * @date 2020/9/3 17:43
 */

@RestController
@RequestMapping("/dmestorage")
@Api
public class DmeStorageController extends BaseController{


    public static final Logger LOG = LoggerFactory.getLogger(DmeStorageController.class);
    private final String API_RESP_CODE = "code";
    private Gson gson=new Gson();

    @Autowired
    private DmeStorageService dmeStorageService;

    /**
     * query storage list
     * @param
     * @return
     */

    @GetMapping("/storages")
    @ResponseBody
    public ResponseBodyBean getStorages(){

        try {
            return success(dmeStorageService.getStorages());
        } catch (DMEException e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }


    }

    /**
     * search oriented storage detail
     * @param storageId required
     * @return
     */
    @GetMapping("/storage")
    @ResponseBody
    public ResponseBodyBean getStorageDetail(@RequestParam(name = "storageId") String storageId){

        LOG.info("storage_id ==" + storageId );

        try {
            return success(dmeStorageService.getStorageDetail(storageId));
        } catch (DMEException e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }



    }

    /**
     *
     * @param storageId
     * @param mediaType file,block,all
     * @return
     */
    @GetMapping("/storagepools")
    @ResponseBody
    public ResponseBodyBean getStoragePools(@RequestParam(name = "storageId") String storageId,
                                            @RequestParam(name = "mediaType",defaultValue = "all",required = false) String mediaType){

        LOG.info("storage_id ==" + storageId );
        try {
            return success(dmeStorageService.getStoragePools(storageId,mediaType));
        } catch (DMEException e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }


    }

    @GetMapping("/logicports")
    @ResponseBody
    public ResponseBodyBean getLogicPorts(@RequestParam(name = "storageId") String storageId){

        try {
            return success(dmeStorageService.getLogicPorts(storageId));
        } catch (DMEException e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }


    }
    @GetMapping("/volumes")
    @ResponseBody
    public ResponseBodyBean getVolumes(@RequestParam(name = "storageId") String storageId){

        try {
            return success(dmeStorageService.getVolumes(storageId));
        } catch (DMEException e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }


    }
    @GetMapping("/filesystems")
    @ResponseBody
    public ResponseBodyBean getFileSystems(@RequestParam(name = "storageId") String storageId){

        try {
            return success(dmeStorageService.getFileSystems(storageId));
        } catch (DMEException e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }


    }
    @GetMapping("/dtrees")
    @ResponseBody
    public ResponseBodyBean getDTrees(@RequestParam(name = "storageId") String storageId){

        try {
            return success(dmeStorageService.getDTrees(storageId));
        } catch (DMEException e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }


    }
    @GetMapping("/nfsshares")
    @ResponseBody
    public ResponseBodyBean getNfsShares(@RequestParam(name = "storageId") String storageId){

        try {
            return success(dmeStorageService.getNfsShares(storageId));
        } catch (DMEException e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }


    }
    @GetMapping("/bandports")
    @ResponseBody
    public ResponseBodyBean getBandPorts(@RequestParam(name = "storageId") String storageId){

        try {
            return success(dmeStorageService.getBandPorts(storageId));
        } catch (DMEException e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }


    }
    @GetMapping("/storagecontrollers")
    @ResponseBody
    public ResponseBodyBean getStorageControllers(@RequestParam(name = "storageDeviceId")String storageDeviceId){

        try {
            return success(dmeStorageService.getStorageControllers(storageDeviceId));
        } catch (DMEException e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }


    }
    @GetMapping("/storagedisks")
    @ResponseBody
    public ResponseBodyBean getStorageDisks(@RequestParam(name = "storageDeviceId") String storageDeviceId){

        try {
            return success(dmeStorageService.getStorageDisks(storageDeviceId));
        } catch (DMEException e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }


    }

    /**
     * Get ETH ports
     *
     * @param  storageSn storage's Sn
     * @return ResponseBodyBean List<EthPortInfo> include EthPortInfo
     * @throws Exception when error
     */
    @GetMapping("/getstorageethports")
    @ResponseBody
    public ResponseBodyBean getStorageEthPorts(@RequestParam(name = "storageSn") String storageSn) throws Exception{

        LOG.info("getStorageEthPorts storageSn==" + storageSn);
        String failureStr = "";
        try {
            List<EthPortInfo> lists = dmeStorageService.getStorageEthPorts(storageSn);
            LOG.info("getStorageEthPorts lists==" + gson.toJson(lists));
            return success(lists);
        } catch (Exception e) {
            LOG.error("get Storage Eth Ports failure:", e);
            failureStr = "get Storage Eth Ports failure:"+e.toString();
        }
        return failure(failureStr);
    }

    /**
     *
     * @param portType FC FCoE ETH  默认 ALL 所有端口类型
     * @return
     */
    @GetMapping("/storageport")
    @ResponseBody
    public ResponseBodyBean getStoragePort(@RequestParam(name = "storageDeviceId")String storageDeviceId,
                                           @RequestParam(name = "portType", defaultValue = "ALL", required = false) String portType) {
        try {
            return success(dmeStorageService.getStoragePort(storageDeviceId,portType));
        } catch (DMEException e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }


    }

    @GetMapping("/failovergroups")
    @ResponseBody
    public ResponseBodyBean getFailoverGroups(@RequestParam(name = "storageId")String storageId) {
        try {
            return success(dmeStorageService.getFailoverGroups(storageId));
        } catch (DMEException e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }

    }

    /**
     * Access storage performance
     *
     * @param storageIds storage id
     * @return: ResponseBodyBean
     */
    @RequestMapping(value = "/liststorageperformance", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean listStoragePerformance(@RequestParam("storageIds") List<String> storageIds)
            throws Exception {
        LOG.info("accessvmfs/liststorageperformance storageIds==" + gson.toJson(storageIds));
        String failureStr = "";
        try {
            List<Storage> lists = dmeStorageService.listStoragePerformance(storageIds);
            LOG.info("liststorageperformance lists==" + gson.toJson(lists));
            return success(lists);
        } catch (Exception e) {
            LOG.error("get Storage performance failure:", e);
            failureStr = "get Storage performance failure:" + e.toString();
        }
        return failure(failureStr);
    }

    /**
     * Access storage pool performance
     *
     * @param storagePoolIds storage pool res Id
     * @return: ResponseBodyBean
     */
    @RequestMapping(value = "/liststoragepoolperformance", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean listStoragePoolPerformance(@RequestParam("storagePoolIds") List<String> storagePoolIds)
            throws Exception {
        LOG.info("accessvmfs/listStoragePoolPerformance storagePoolIds==" + gson.toJson(storagePoolIds));
        String failureStr = "";
        try {
            List<StoragePool> lists = dmeStorageService.listStoragePoolPerformance(storagePoolIds);
            LOG.info("liststorageperformance lists==" + gson.toJson(lists));
            return success(lists);
        } catch (Exception e) {
            LOG.error("get Storage Pool performance failure:", e);
            failureStr = "get Storage Pool performance failure:" + e.toString();
        }
        return failure(failureStr);
    }
    /*@GetMapping("/filesystemdetail")
    @ResponseBody
    public ResponseBodyBean getFileSystemDetail(@RequestParam(name = "fileSystemId")String fileSystemId) {
        try {
            return success(dmeStorageService.getFileSystemDetail(fileSystemId));
        } catch (DMEException e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }


    }*/
}
