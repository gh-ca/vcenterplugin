package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.VmRDMCreateBean;

import java.util.List;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName VMRDMService.java
 * @Description TODO
 * @createTime 2020年09月09日 10:58:00
 */
public interface VMRDMService {
    /**
     * @Author Administrator
     * @Description //TODO
     * @Date 14:51 2020/9/9
     * @Param [vmRDMCreateBean]
     * @Return java.lang.String  返回任务ID
     **/
    String createRDM(VmRDMCreateBean vmRDMCreateBean);

    /**
     * @Author Administrator
     * @Description //TODO
     * @Date 14:51 2020/9/9
     * @Param [hostId, volumeIds]
     * @Return java.lang.String 返回任务ID
     **/
    String hostMapping(String hostId, List<String> volumeIds);

    public  void hostRescanVmfs(String hostIp) throws Exception;

    public  String getLunsOnHost(String hostName) throws Exception ;
}
