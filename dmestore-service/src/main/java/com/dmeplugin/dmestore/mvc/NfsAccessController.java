package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.NfsDataInfo;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.model.VmfsDataInfo;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        LOG.info("accessvmfs/listnfs");
        String failureStr = "";
        try {
            List<NfsDataInfo> lists = new ArrayList<>(); //vmfsAccessService.listVmfs();
            NfsDataInfo ndi = new NfsDataInfo();
            ndi.setName("nfstest2");    //名称
            ndi.setStatus("Normal");  //状态
            ndi.setCapacity(2000D);  //总容量 单位GB
            ndi.setFreeSpace(200D); //空闲容量 单位GB
            ndi.setReserveCapacity(3000D); //置备容量  capacity+uncommitted-freeSpace 单位GB
            ndi.setDevice("Device02"); //设备
            ndi.setLogicPort("8022"); //逻辑端口
            ndi.setShareIp("10.143.132.231"); //share ip
            ndi.setSharePath("/D/filepath02"); //share path
            ndi.setFs("FileSystem02"); //fs

            //列表字段（性能视图）：
            ndi.setOPS(2000); //OPS
            ndi.setBandwidth(123.33);   //带宽 单位MB/s

            ndi.setReadResponseTime(2);   //读响应时间 单位ms
            ndi.setWriteResponseTime(2); //写响应时间 单位ms
            lists.add(ndi);
            ndi = new NfsDataInfo();
            ndi.setName("nfstest1");    //名称
            ndi.setStatus("Normal");  //状态
            ndi.setCapacity(1000D);  //总容量 单位GB
            ndi.setFreeSpace(200D); //空闲容量 单位GB
            ndi.setReserveCapacity(3000D); //置备容量  capacity+uncommitted-freeSpace 单位GB
            ndi.setDevice("Device01"); //设备
            ndi.setLogicPort("8022"); //逻辑端口
            ndi.setShareIp("10.143.132.231"); //share ip
            ndi.setSharePath("/D/filepath01"); //share path
            ndi.setFs("FileSystem01"); //fs

            //列表字段（性能视图）：
            ndi.setOPS(1000); //OPS
            ndi.setBandwidth(23.33);   //带宽 单位MB/s

            ndi.setReadResponseTime(2);   //读响应时间 单位ms
            ndi.setWriteResponseTime(2); //写响应时间 单位ms
            lists.add(ndi);


            LOG.info("listvmfs remap==" + gson.toJson(lists));
            return success(lists);
        }catch (Exception e){
            LOG.error("list vmfs failure:", e);
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }


    /*
   * Mount nfs
   * param list<str> dataStoreNames: datastore名称列表 必
   * param list<str> volumeIds: 卷volumeId列表 必
   * param str host: 主机名称 必 （主机与集群二选一）
   * param str cluster: 集群名称 必（主机与集群二选一）
   * param str logic_port: 逻辑端口
   * param str mountType: 挂载模式（只读或读写）
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

}
