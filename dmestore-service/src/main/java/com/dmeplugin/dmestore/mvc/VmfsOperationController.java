package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.SimpleServiceLevel;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.VmfsOperationService;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private Gson gson;
    @Autowired
    private VmfsOperationService vmfsOperationService;

    /**
     *
     * @param volume_id  required
     * @param params  {control_policy,max_iops,max_bandwidth,newVoName,oldDsName,newDsName}
     * @return
     */
    @PutMapping("/updatevmfs")
    @ResponseBody
    public ResponseBodyBean updateVMFS(@RequestParam(value = "volume_id" ) String volume_id,
                                       @RequestBody Map<String,Object> params){

        Map<String,Object> resMap = vmfsOperationService.updateVMFS(volume_id,params);
        if (null != resMap && null != resMap.get("code") && resMap.get("code").equals("200")) {
            return success(resMap);
        }
        return failure(gson.toJson(resMap));
    }

    /**
     * expand vmfs datastore
     * @param volumes
     * @return
     */
    @PostMapping("expandvmfs")
    @ResponseBody
    public ResponseBodyBean expandVMFS(@RequestBody List<Map<String,String >> volumes ){

        LOG.info("volumes=="+gson.toJson(volumes));
        Map<String, Object> resMap = vmfsOperationService.expandVMFS(volumes);
        if (null != resMap && null != resMap.get("code") && resMap.get("code").equals("200")) {
            return success(resMap);
        }
        return failure(gson.toJson(resMap));
    }



    /**
     * 调用方法不确定
     * @param volumeId
     * @return
     */
    @PostMapping("/recyclevmfs")
    @ResponseBody
    public ResponseBodyBean recycleVMFS(@RequestParam(value = "volumeId",required = true) String volumeId){
        LOG.info("recyclevmfs=="+volumeId);
        //调vcenter方法，回收空间
        return success();
    }

    /**
     * search service-level
     * @param params （name，project_id，available_zone_id，storage_array_id，start
     *               limit，sort_key，sort_dir，type）
     * @return
     */
    @GetMapping("/servicelevelvmfs")
    @ResponseBody
    public ResponseBodyBean getServiceLevelVMFS(@RequestBody Map<String,String> params){

        LOG.info("servicelevelvmfs=="+gson.toJson(params));
        //遍历参数
        //请求地址
        String url="/rest/service-policy/v1/service-levels";
        //参数拼接url
        SimpleServiceLevel simpleServiceLevel = new SimpleServiceLevel();

        return success(simpleServiceLevel);
    }

    /**
     *
     * @param serviceLevelId required
     * @param attributesAutoChange  optional
     * @param volumeIds volume list optional
     * @return
     */
    @PostMapping("changeservicelevelvmfs")
    @ResponseBody
    public ResponseBodyBean changeServiceLevelVMFS(@RequestParam(name = "serviceLevelId") String serviceLevelId,
                                                   @RequestParam(name = "attributesAutoChange",defaultValue = "true",required = false) Boolean attributesAutoChange,
                                                   @RequestBody(required = false) String[] volumeIds){

        LOG.info("changeservicelevelvmfs=="+serviceLevelId);
        String url = "/rest/blockservice/v1/volumes/update-service-level";

        String taskId = "";
        return success(taskId);
    }

    public static void main(String[] args) {

    }

}
