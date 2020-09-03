package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.entity.StorageInfo;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
<<<<<<< Updated upstream
import com.dmeplugin.dmestore.services.DmeAccessService;
=======
import com.dmeplugin.dmestore.utils.HttpRequestUtil;
>>>>>>> Stashed changes
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping(value = "/accessdme")
public class DmeAccessController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(DmeAccessController.class);

    @Autowired
    private Gson gson;
    @Autowired
    private DmeAccessService dmeAccessService;

    /*
    * Access DME
    * param str hostIp: Access to the IP address of the DME service
    * param str hostPort: Port to access DME service
    * param str userName: User name to access the DME service
    * param str password: Password to access the DME service
    * return: ResponseBodyBean
    */
    @RequestMapping(value = "/access", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean accessDme(@RequestBody Map<String, Object> params)
            throws Exception {
        LOG.info("accessdme/access params==" + params);
        Map<String, Object> remap = dmeAccessService.accessDme(params);
        LOG.info("accessdme/access remap==" + remap);
        if (remap != null && remap.get("code") != null && remap.get("code").equals("200")) {
            return success(remap);
        }

        return failure(gson.toJson(remap));
    }

    /*
    * Refresh connection status
    * return: ResponseBodyBean
    */
    @RequestMapping(value = "/refreshaccess", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean refreshDme()
            throws Exception {
        Map<String, Object> remap = dmeAccessService.refreshDme();
        LOG.info("accessdme/access remap==" + remap);
        if (remap != null && remap.get("code") != null && remap.get("code").equals("200")) {
            return success(remap);
        }

        return failure(gson.toJson(remap));
    }

    /**
     *  查询存储设备列表
     * @param az 可用分区 ID
     * @param start 分页查询的起始位置。
     * @param limit 分页查询的个数，默认为20
     * @return
     */

    @GetMapping("/storages")
    @ResponseBody
    public ResponseBodyBean getStorages(@RequestParam(name = "az",required = false) String az,
                                        @RequestParam(name = "start",value = "1",required = false) int start,
                                        @RequestParam(name = "limit",value = "20",required = false) int limit
    ){
        Map<String,Object> map = new HashMap();
        map.put("az",az);
        map.put("start",start);
        map.put("limit",limit);

        String storages = gson.toJson(map);
        LOG.info("storages ==" + storages );

        String url = "/rest/storagemgmt/v1/storages?start="+ start + "&limit=" + limit;
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON_UTF8);
        //ResponseEntity<String> stringResponseEntity = HttpRequestUtil.requestWithBody(url, HttpMethod.GET,
        //        header, null, String.class);
        StorageInfo storageInfo = new StorageInfo();
        return success(storageInfo);
    }

    /**
     * 注意事项：
     *  此接口必须在HTTPS通道上访问。session默认30分钟失效。北向登录成功后，访问应
     *  用的接口时，将session设置到HTTP的header中，key为X-Auth-Token，value为安全
     *  返回的值，如：X-Auth-Token:xxx。当调用该北向接口时，请使用26335端口获取
     *  token。访问此接口的用户，必须拥有北向用户组角色和存储资源查询权限。
     * @param storage_id
     * @return
     */
    @GetMapping("/storage")
    @ResponseBody
    public ResponseBodyBean getStorage(@RequestParam(name = "storage_id") String storage_id){

        LOG.info("storage_id ==" + storage_id );
        String url = "/rest/storagemgmt/v1/storages/{ "+ storage_id+ "}/detail";
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON_UTF8);
        //ResponseEntity<String> stringResponseEntity = HttpRequestUtil.requestWithBody(url, HttpMethod.GET,
        //        header, null, String.class);
        //根据storage_id 分别去查, 池 ,卷 ,fs, dt, shares ,user authentication

        return success();
    }

}
