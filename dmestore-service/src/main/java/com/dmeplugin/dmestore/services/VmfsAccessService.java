package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.VmfsDataInfo;
import com.dmeplugin.dmestore.model.VmfsDatastoreVolumeDetail;

import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @ClassName: VmfsAccessService
 * @Company: GH-CA
 * @author: yy
 * @create: 2020-09-02
 **/
public interface VmfsAccessService {
    /**
     * vmfs list
     */
    List<VmfsDataInfo> listVmfs() throws Exception;

    /**
     * vmfs list Performance
     */
    List<VmfsDataInfo> listVmfsPerformance(List<String> volumeIds) throws Exception;

    /**
     * create vmfs
     */
    void createVmfs(Map<String, Object> params) throws Exception;

    /**
     * mount vmfs
     */
    void mountVmfs(Map<String, Object> params) throws Exception;

    /**
     * unmounted vmfs
     */
    void unmountVmfs(Map<String, Object> params) throws Exception;

    /**
     * delete vmfs
     */
    void deleteVmfs(Map<String, Object> params) throws Exception;

    /**
     * vmfs 指定卷详细信息查询
     */
    VmfsDatastoreVolumeDetail volumeDetail(String volumeId) throws Exception;

    boolean scanVmfs() throws Exception;

}
