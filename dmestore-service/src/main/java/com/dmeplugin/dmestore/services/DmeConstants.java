package com.dmeplugin.dmestore.services;

/**
 * @ClassName DmeConstants
 * @Description Dme 常量
 * @Author wangxiangyong
 * @Date 2020/9/4 10:40
 * @Version V1.0
 **/
public class DmeConstants {
    public static final String DEFAULT_PATTERN = "#\\{.*?\\}";
    /**
     * NFS Share详情
     **/
    public static final String DME_NFS_SHARE_DETAIL_URL = "/rest/fileservice/v1/nfs-shares/#{nfs_share_id}";

    /**
     * DTREE 列表
     **/
    public static final String DME_NFS_DTREE_LIST_URL = "/rest/fileservice/v1/dtrees/summary";

    /**
     * 查询指定nfs-share下的客户端访问列表
     **/
    public static final String DME_NFS_SHARE_AUTH_CLIENTS_URL = "/rest/fileservice/v1/nfs-shares/#{nfs_share_id}/auth_clients";

    /**
     * NFS Datastore FileService列表
     **/
    public static final String DME_NFS_FILESERVICE_QUERY_URL = "/rest/fileservice/v1/filesystems/query";

    /**
     * NFS Datastore FileService详情
     **/
    public static final String DME_NFS_FILESERVICE_DETAIL_URL = "/rest/fileservice/v1/filesystems/#{file_system_id}";

    /**
     * NFS Datastore 逻辑端口详情
     **/
    public static final String DME_NFS_LOGICPORT_DETAIL_URL = "/rest/storagemgmt/v1/storage-port/logic-ports/#{logic_port_id}";

    /**
     * DME 磁盘操作基础URL
     **/
    public static final String DME_VOLUME_BASE_URL = "/rest/blockservice/v1/volumes";
    public static final String DME_VOLUME_DELETE_URL = "/rest/blockservice/v1/volumes/delete";

    /**
     * DME 磁盘操作基础URL
     **/
    public static final String DME_CREATE_VOLUME_UNLEVEL_URL = "/rest/blockservice/v1/volumes/customize-volumes";


    /**
     * DME 任务查询URL
     **/
    public static final String DME_TASK_BASE_URL = "/rest/taskmgmt/v1/tasks";


    /**
     * DME 卷映射给主机URL
     **/
    public static final String DME_HOST_MAPPING_URL = "/rest/blockservice/v1/volumes/host-mapping";

    /**
     * 解除主机卷映射
     **/
    public static final String DME_HOST_UNMAPPING_URL = "/rest/blockservice/v1/volumes/host-unmapping";

    /**
     * NFS share列表 POST
     */
    public static final String DME_NFS_SHARE_URL = "/rest/fileservice/v1/nfs-shares/summary";

    /**
     * NFS logic_port query
     */
    public static final String DME_NFS_LOGICPORT_QUERY_URL = "/rest/storagemgmt/v1/storage-port/logic-ports?storage_id=#{storage_id}";

    /**
     * The resource ID of the storage device can be obtained through device Sn filtering
     */
    public static final String DME_RES_STORDEVICEID_QUERY_URL = "/rest/resourcedb/v1/instances/SYS_StorDevice";

    /**
     * Get Storage eth ports
     */
    public static final String DME_STORDEVICE_ETHPORT_QUERY_URL = "/rest/resourcedb/v1/instances/SYS_StoragePort";

    /**
     * delete share
     */
    public static final String DME_NFS_SHARE_DELETE_URL = "/rest/fileservice/v1/nfs-shares/delete";

    /**
     * delete fs
     */
    public static final String DME_NFS_FS_DELETE_URL = "/rest/fileservice/v1/filesystems/delete";

    /**
     *
     */
    public static final String DME_RESOURCE_INSTANCE_LIST = "/rest/resourcedb/v1/instances/#{className}";

    /**
     * Constant definition
     */
    public static final int MAXLEN = 255;
    public static final String HTTP = "http";
    public static final String HOSTIP = "hostIp";
    public static final String ACCESSSESSION = "accessSession";
    public static final String DATAS = "datas";
    public static final String DATA = "data";
    public static final String HOSTS = "hosts";
    public static final String INITIATORS = "initiators";
    public static final String HOSTGROUPS = "hostgroups";
    public static final String HOST = "host";
    public static final String ID = "id";
    public static final String CLUSTER = "cluster";
    public static final String HOSTIDS = "hostids";
    public static final String SERVICELEVELID = "service_level_id";
    public static final String TASKID = "task_id";
    public static final String STORAGEID = "storage_id";
    public static final String HOSTID = "host_id";
    public static final String HOSTGROUPID = "hostgroup_id";
    public static final String VOLUMEID = "volume_id";
    public static final String VOLUMEIDS = "volumeIds";
    public static final String CONTROLPOLICY = "control_policy";
    public static final String ALLOCTYPE = "alloctype";
    public static final String VOLUMES = "volumes";
    public static final String DATASTOREOBJECTIDS = "dataStoreObjectIds";
    public static final String DATASTORENAMES = "dataStoreNames";
    public static final String ETHPORTS = "ethPorts";
    public static final String VMKERNEL = "vmKernel";
    public static final String HOSTOBJECTID = "hostObjectId";


    public static final String TASK_DETAIL_STATUS_FILE = "status";
    public static final int TASK_SUCCESS = 3;
    public static final int HTTPS_STATUS_CHECK_FLAG = 100;
    public static final int HTTPS_STATUS_SUCCESS_PRE = 2;
}
