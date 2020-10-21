package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.VmfsOperationService;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
/**
 * @author lianq
 * @ClassName: VmfsOperationController
 * @Company: GH-CA
 * @create 2020-09-03
 */
@RestController
@RequestMapping("/operatevmfs")
@Api
public class VmfsOperationController extends BaseController{

    public static final Logger LOG = LoggerFactory.getLogger(VmfsOperationController.class);
    private final String API_RESP_CODE = "code";

    private Gson gson=new Gson();
    @Autowired
    private VmfsOperationService vmfsOperationService;

    /**
     *
     * @param volumeId  required
     * @param params  {control_policy,max_iops,max_bandwidth,newVoName,newDsName,min_iops,min_bandwidth,String dataStoreObjectId,String service_level_name}
     * @return
     */
    @PutMapping("/updatevmfs")
    @ResponseBody
    public ResponseBodyBean updateVMFS(@RequestParam(value = "volumeId" ) String volumeId,
                                       @RequestBody Map<String,Object> params){

        try {
            vmfsOperationService.updateVMFS(volumeId,params);
            return success();
        } catch (DMEException e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }


    }

    /**
     * expand vmfs datastore
     *
     * @param volumes  volumes{int vo_add_capacity 扩容量,String hostObjectId 主机,String ds_name 存储名,String volume_id vmfs所在卷id}
     * @return
     */
    @PostMapping("/expandvmfs")
    @ResponseBody
    public ResponseBodyBean expandVMFS(@RequestBody List<Map<String,String >> volumes ){

        LOG.info("volumes=="+gson.toJson(volumes));
        try {
            vmfsOperationService.expandVMFS(volumes);
            return success();
        } catch (DMEException e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }


    }



    @PostMapping("/recyclevmfs")
    @ResponseBody
    public ResponseBodyBean recycleVMFS(@RequestBody List<String> datastoreName){
        LOG.info("recyclevmfs=="+gson.toJson(datastoreName));
        try {
            vmfsOperationService.recycleVmfsCapacity(datastoreName);
            return success();
        } catch (DMEException e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }

    }

    @PutMapping("/listvmfsservicelevel")
    @ResponseBody
    public ResponseBodyBean listServiceLevelVMFS(@RequestBody(required = false) Map<String,Object> params){

        LOG.info("recyclevmfs=="+gson.toJson(params));
        try {
            return success(vmfsOperationService.listServiceLevelVMFS(params));
        } catch (DMEException e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }

    }

    /**
     * {
     *   "service_level_id" : "b1da0725-2456-4d37-9caf-ad0a4644d10e",
     *   "attributes_auto_change" : true,
     *   "volume_ids" : [ "a0da0725-2456-4d37-9caf-ad0a4644d10e" ]
     * }
     * @param params {service_level_id,attributes_auto_change,volume_ids}
     * @return
     */
    @PostMapping("updatevmfsservicelevel")
    @ResponseBody
    public ResponseBodyBean updateServiceLevelVMFS(@RequestBody Map<String,Object> params){

        LOG.info("servicelevelvmfs=="+gson.toJson(params));
        try {
            vmfsOperationService.updateVmfsServiceLevel(params);
            return success();
        } catch (DMEException e) {
            e.printStackTrace();
            return failure(e.getMessage());
        }


    }

}
