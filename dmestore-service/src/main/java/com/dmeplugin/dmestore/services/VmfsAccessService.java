package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
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
    List<VmfsDataInfo> listVmfs() throws DMEException;

    /**
     * List vmfs Performance
     *
     * @param  wwns
     * @return List<VmfsDataInfo>
     * @throws Exception when error
     */
    List<VmfsDataInfo> listVmfsPerformance(List<String> wwns) throws DMEException;

    /**
     * Create vmfs include:
     * ServiceVolumeBasicParams对象包含如下属性
     * param str name: 名称 必 vmfs stor的名称
     * param str volumeName: 名称 必 卷的名称
     * param int capacity: 容量，单位GB 必
     * param int count: 数量 必
     * param str service_level_id: 服务等级id 若未选择服务等级，可选择存储设备、存储池、设置QoS、Thin、Workload
     * param str service_level_name; 服务等级名称
     * param int version: 版本
     * param int blockSize: 块大小，单位KB
     * param int spaceReclamationGranularity   空间回收粒度 单位K
     * param str spaceReclamationPriority: 空间回收优先权
     * param str host: 主机  必  与cluster二选其一,不可同时存在
     * param str hostId: 主机
     * param str cluster: 集群 必 与host二选其一,不可同时存在
     * param str clusterId: 集群
     * param str storage_id 存储设备id
     * param str pool_raw_id 卷所属存储池在存储设备上的id
     * param integer workload_type_id 应用类型id
     * param str alloctype 卷分配类型，取值范围 thin，thick
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
     * @return Return ResponseBodyBean
     * @return: ResponseBodyBean
     * @throws Exception when error
     */
    void createVmfs(Map<String, Object> params) throws DMEException;

    /**
     * Mount vmfs include
     * param list<str> dataStoreObjectIds: datastore object id列表 必
     * param str host: 主机名称 必 （主机与集群二选一）
     * param str hostId: 主机
     * param str cluster: 集群名称 必（主机与集群二选一）
     * param str clusterId: 集群
     *
     * @param params: include dataStoreObjectIds,host,hostId,cluster,clusterId
     * @return: ResponseBodyBean
     * @throws Exception when error
     */
    void mountVmfs(Map<String, Object> params) throws DMEException;

    /**
     * unmounted vmfs
     */
    void unmountVmfs(Map<String, Object> params) throws DMEException;

    /**
     * delete vmfs
     */
    void deleteVmfs(Map<String, Object> params) throws DMEException;

    /**
     * vCenter VMFS存储卷详细信息查询
     * @author wangxy
     * @date 10:05 2020/10/14
     * @param storageObjectId VMFS存储ID
     * @throws Exception always
     * @return java.util.List<com.dmeplugin.dmestore.model.VmfsDatastoreVolumeDetail>
     **/
    List<VmfsDatastoreVolumeDetail> volumeDetail(String storageObjectId) throws DMEException;

    /**
     * vCenter VMFS存储扫描发现
     * @author wangxy
     * @date 10:03 2020/10/14
     * @throws Exception always
     * @return boolean
     **/
    boolean scanVmfs() throws DMEException;

    /**
     * 通过vmfs storageId查询VC的主机 (DME侧关联的主机的启动器和VC主机的启动器要一致)
     * @return 返回VC主机列表，单个主机的信息以map方式存储属性和属性值
     * @throws Exception
     */
    List<Map<String, Object>> getHostsByStorageId(String storageId) throws DMEException;

    /**
     * 通过vmfs storageId查询vc 集群信息 （DME侧关联的主机组信息下所有主机的启动器和集群下的主机的启动器一致）
     * @param storageId
     * @return 返回集群列表，单个集群的信息以map方式存储属性和属性值
     * @throws Exception
     */
    List<Map<String, Object>> getHostGroupsByStorageId(String storageId) throws DMEException;

    /**
     * query vmfs
     * @return List<VmfsDataInfo>
     * @throws Exception when error
     */
    List<VmfsDataInfo> queryVmfs(String dataStoreObjectId) throws Exception;


    /**
     * 根据vmfs名字查询指定vmfs
     *
     * @param name
     * @return
     */
    Boolean queryDatastoreByName(String name);


    /**
     * DME侧主机检查
     * @author wangxy
     * @date 14:00 2020/10/30
     * @param hostIp
     * @param hostId
     * @throws DMEException
     * @return java.lang.String
     **/
    String checkOrCreateToHost(String hostIp, String hostId) throws DMEException;

}
