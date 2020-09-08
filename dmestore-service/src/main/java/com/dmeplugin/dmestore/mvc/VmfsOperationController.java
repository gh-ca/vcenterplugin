package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.SimpleServiceLevel;
import com.dmeplugin.dmestore.model.Volume;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
//@Api(value = "operation Vmfs Datastore")
public class VmfsOperationController extends BaseController{

    public static final Logger LOG = LoggerFactory.getLogger(VmfsOperationController.class);

    @Autowired
    private Gson gson;

    //服务化卷和非服务化卷的衡量标准？
    //修改vmfs名字 调用vcenter接口
    /**
     *
     * @param volumeId  required
     * @param volume requestbody  required
     * @return
     */
    @PutMapping("/updatevmfs")
    @ResponseBody
    //@ApiOperation(value = "updatevmfs",httpMethod = "put")
    public ResponseBodyBean updateVMFS(@RequestParam(value = "volumeId" ) String volumeId,
                                       @RequestBody Volume volume){
        String url ="/rest/blockservice/v1/volumes/"+ volumeId;
        LOG.info("updatevmfs==volumeId="+volumeId+"volume=="+gson.toJson(volume));
        String taskId="";
        return success();
    }

    /**
     *  入参：
     *  "volumes" : [ {
     *     "volume_id" : "02bb989a-7ac2-40cd-852d-c9b26bb2ab5b", 必须
     *     "added_capacity" : 2 必须
     *   } ]
     * @param volumes 批量扩容vmfs
     * @return
     */
    @PostMapping("expandvmfs")
    @ResponseBody
    public ResponseBodyBean expandVMFS(@RequestBody List<Map<String,Object>> volumes ){
        LOG.info("volumes=="+gson.toJson(volumes));
        String url = "/rest/blockservice/v1/volumes/expand";
        //调vcenter端口
        //String taskId="";
        return success();
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

}
