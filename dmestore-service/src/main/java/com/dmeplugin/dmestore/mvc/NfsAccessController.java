package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.entity.SimpleServiceLevel;
import com.dmeplugin.dmestore.entity.Volume;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.utils.HttpRequestUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/accessnfs")
public class NfsAccessController extends BaseController{
    public static final Logger LOG = LoggerFactory.getLogger(NfsAccessController.class);

    @Autowired
    private Gson gson;

    /*
   * Access nfs
   * return: ResponseBodyBean
   */
    @RequestMapping(value = "/listnfs", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean listNfs()
            throws Exception {
        LOG.info("accessnfs/listnfs");
        String re = "";
        return success(re);
    }


    /*
   * Mount nfs
   * param list<str> volume_ids: 卷id列表 必
   * param str host_id: 主机id 必
   * return: ResponseBodyBean
   */
    @RequestMapping(value = "/mountnfs", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean mountNfs(@RequestBody Map<String, Object> params)
            throws Exception {
        LOG.info("accessnfs/mountnfs=="+gson.toJson(params));
        String re = "";
        return success(re);
    }

    //服务化卷和非服务化卷的衡量标准？
    //修改vmfs名字 调用vcenter接口
    /**
     *
     * @param volumeId 卷的唯一标识 required
     * @param volume 指定卷的请求结构体  required
     * @return
     */
    @PutMapping("/updatevmfs")
    @ResponseBody
    public ResponseBodyBean updateVMFS(@RequestParam(value = "volumeId" ,required = true) String volumeId,
                                       @RequestBody Volume volume){
        String url ="/rest/blockservice/v1/volumes/"+ volumeId;
        LOG.info("updatevmfs==volumeId="+volumeId+"volume=="+gson.toJson(volume));
        //String taskId="";
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

    //调用方法不确定

    /**
     *
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
     * 查询可变更服务等级
     * @param params 包括多种可选参数（name，project_id，available_zone_id，storage_array_id，start
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
     * @param serviceLevelId 服务等级id required
     * @param attributesAutoChange 根据服务等级参数刷新卷属性 optional
     * @param volume_ids 卷列表 optional
     * @return
     */
    @PostMapping("changeservicelevelvmfs")
    @ResponseBody
    public ResponseBodyBean changeServiceLevelVMFS(@RequestParam(name = "serviceLevelId",required = true) String serviceLevelId,
                                                   @RequestParam(name = "attributesAutoChange",value = "true",required = false) Boolean attributesAutoChange,
                                                   @RequestBody(required = false) String[] volumeIds){

        LOG.info("changeservicelevelvmfs=="+serviceLevelId);
        String url = "/rest/blockservice/v1/volumes/update-service-level";

        String taskId = "";
        return success(taskId);
    }







}
