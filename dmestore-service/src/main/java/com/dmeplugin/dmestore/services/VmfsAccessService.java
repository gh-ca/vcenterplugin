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
     * List vmfs

     * @return List<VmfsDataInfo>
     * @throws Exception when error
     */
    List<VmfsDataInfo> listVmfs() throws Exception;

    /**
     * List vmfs Performance
     *
     * @param  volumeIds volumes id
     * @return List<VmfsDataInfo>
     * @throws Exception when error
     */
    List<VmfsDataInfo> listVmfsPerformance(List<String> volumeIds) throws Exception;

    /**
     * Create vmfs include:
     * ServiceVolumeBasicParams对象包含如下属性
     * param str name: 名称 必 vmfs stor的名称
     * param str volumeName: 名称 必 卷的名称
     * param int capacity: 容量，单位GB 必
     * param int count: 数量 必
     * param int start_suffix: 该规格卷的起始后缀编号
     * SchedulerHints对象包含如下属性
     * param boolean affinity: 是否开启亲和性
     * param str affinity_volume: 待亲和的卷id
     * ServiceVolumeMapping对象包含如下属性
     * param str host_id: 主机id，与hostgroup_id二选其一,不可同时存在
     * param str hostgroup_id: 主机组id，与host_id二选其一，不可同时存在

     * param str service_level_id: 服务等级id 若未选择服务等级，可选择存储设备、存储池、设置QoS、Thin、Workload
     * param str service_level_name; 服务等级名称  必
     * param str project_id: 业务群组id
     * param str availability_zone: 可用分区id
     *
     * param int version: 版本
     * param int blockSize: 块大小，单位KB
     * param int spaceReclamationGranularity   空间回收粒度 单位K
     * param str spaceReclamationPriority: 空间回收优先权
     * param str host: 主机  必
     * param str hostId: 主机
     * param str hostlun; 可用的lun 必
     * param str cluster: 集群
     * param str clusterId: 集群
     *
     * param str storage_id 存储设备id
     * param str pool_raw_id 卷所属存储池在存储设备上的id
     * param integer workload_type_id 应用类型id
     * param str alloctype 卷分配类型，取值范围 thin，thick
     *
     * 卷qos属性
     * param str  control_policy 控制策略
     * param integer  latency 时延，单位ms
     * param integer  maxbandwidth 最大带宽
     * param integer  maxiops 最大iops
     * param integer  minbandwidth 最小带宽
     * param integer  miniops 最小iops
     * param str qosname Smart QoS名称
     *
     * @param params include Parameters above
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    void createVmfs(Map<String, Object> params) throws Exception;

    /**
     * Mount vmfs include
     * param list<str> dataStoreNames: datastore名称列表 必
     * param list<str> volumeIds: 卷volumeId列表 必
     * param str host: 主机名称 必 （主机与集群二选一）
     * param str cluster: 集群名称 必（主机与集群二选一）
     *
     * @param params: include dataStoreNames,volumeIds,host,cluster
     * @return ResponseBodyBean
     * @throws Exception when error
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
