package com.dmeplugin.dmestore.services;

/**
 * @author wangxiangyong
 **/
public class DmeConstants {
    public static final String DME_NFS_SHARE_DETAIL_URL = "/rest/fileservice/v1/nfs-shares/{nfs_share_id}";
    //public static final String DME_NFS_SHARE_AUTH_CLIENTS_URL = "/rest/fileservice/v1/nfs-shares/{nfs_share_id}/auth_clients";
    public static final String DME_NFS_SHARE_AUTH_CLIENTS_URL = "/rest/fileservice/v1/nfs-auth-clients/query";
    //public static final String DME_NFS_SHARE_URL = "/rest/fileservice/v1/nfs-shares/summary";
    public static final String DME_NFS_SHARE_URL = "/rest/fileservice/v1/nfs-shares/query";
    public static final String DME_NFS_SHARE_DELETE_URL = "/rest/fileservice/v1/nfs-shares/delete";
    // TODO 创建参数变化很大，需要重构
    public static final String API_NFSSHARE_CREATE = "/rest/fileservice/v1/nfs-shares";

    public static final String DME_NFS_FILESERVICE_QUERY_URL = "/rest/fileservice/v1/filesystems/query";
    public static final String DME_NFS_FILESERVICE_DETAIL_URL = "/rest/fileservice/v1/filesystems/{file_system_id}";
    public static final String DME_NFS_FS_DELETE_URL = "/rest/fileservice/v1/filesystems/delete";
    // TODO 需对参数
    public static final String API_FS_CREATE = "/rest/fileservice/v1/filesystems/customize";

    // TODO 未找到接口/rest/storagemgmt/v1/storage-port以下都没有找到
    public static final String DME_NFS_LOGICPORT_DETAIL_URL = "/rest/storagemgmt/v1/storage-port/logic-ports/{logic_port_id}";
    public static final String API_FAILOVERGROUPS = "/rest/storagemgmt/v1/storage-port/failover-groups?storage_id=";
    public static final String API_LOGICPORTS_LIST = "/rest/storagemgmt/v1/storage-port/logic-ports?storage_id=";
    public static final String API_BANDPORTS_LIST = "/rest/storagemgmt/v1/storage-port/bond-ports?storage_id=";

    public static final String DME_VOLUME_BASE_URL = "/rest/blockservice/v1/volumes";
    public static final String REFRES_STATE_URL = "/rest/blockservice/v1/volumes?limit=1";
    public static final String DME_VOLUME_DELETE_URL = "/rest/blockservice/v1/volumes/delete";
    //public static final String DME_CREATE_VOLUME_UNLEVEL_URL = "/rest/blockservice/v1/volumes/customize-volumes";
    // TODO 参数变更大
    public static final String DME_CREATE_VOLUME_UNLEVEL_URL = "/rest/blockservice/v1/volumes/customize";
    public static final String QUERY_SERVICE_LEVEL_VOLUME_URL = "/rest/blockservice/v1/volumes?service_level_id={serviceLevelId}";
    public static final String DME_HOST_MAPPING_URL = "/rest/blockservice/v1/volumes/host-mapping";
    public static final String DME_HOST_UNMAPPING_URL = "/rest/blockservice/v1/volumes/host-unmapping";
    public static final String HOSTGROUP_UNMAPPING = "/rest/blockservice/v1/volumes/hostgroup-unmapping";
    public static final String MOUNT_VOLUME_TO_HOSTGROUP_URL = "/rest/blockservice/v1/volumes/hostgroup-mapping";
    public static final String API_VMFS_EXPAND = "/rest/blockservice/v1/volumes/expand";
    public static final String API_SERVICELEVEL_UPDATE = "/rest/blockservice/v1/volumes/update-service-level";

    public static final String DME_TASK_BASE_URL = "/rest/taskmgmt/v1/tasks";
    public static final String QUERY_TASK_URL = "/rest/taskmgmt/v1/tasks/{task_id}";

    public static final String DME_RESOURCE_INSTANCE_LIST = "/rest/resourcedb/v1/instances/%s";
    public static final String QUERY_INSTANCE_URL = "/rest/resourcedb/v1/instances/{className}/{instanceId}";
    public static final String LIST_INSTANCE_URL = "/rest/resourcedb/v1/instances/{className}?pageSize=1000";

    public static final String API_STORAGES = "/rest/storagemgmt/v1/storages";
    public static final String DME_STORAGE_DETAIL_URL = "/rest/storagemgmt/v1/storages/{storage_id}/detail";
    public static final String GET_WORKLOADS_URL = "/rest/storagemgmt/v1/storages/{storage_id}/workloads";

    public static final String CREATE_DME_HOST_URL = "/rest/hostmgmt/v1/hosts";
    public static final String DME_HOST_SUMMARY_URL = "/rest/hostmgmt/v1/hosts/summary";
    public static final String GET_DME_HOST_URL = "/rest/hostmgmt/v1/hosts/{host_id}/summary";
    public static final String GET_DME_HOSTS_INITIATORS_URL = "/rest/hostmgmt/v1/hosts/{host_id}/initiators";

    public static final String STATISTIC_QUERY = "/rest/metrics/v1/data-svc/history-data/action/query";

    public static final String LOGIN_DME_URL = "/rest/plat/smapp/v1/sessions";

    public static final String CREATE_DME_HOSTGROUP_URL = "/rest/hostmgmt/v1/hostgroups";
    public static final String GET_DME_HOSTGROUPS_URL = "/rest/hostmgmt/v1/hostgroups/summary";
    public static final String GET_DME_HOSTGROUP_URL = "/rest/hostmgmt/v1/hostgroups/{hostgroup_id}/summary";
    public static final String GET_DME_HOSTS_IN_HOSTGROUP_URL = "/rest/hostmgmt/v1/hostgroups/{hostgroup_id}/hosts/list";

    public static final String LIST_RELATION_URL = "/rest/resourcedb/v1/relations/{relationName}/instances";
    public static final String QUERY_RELATION_URL = "/rest/resourcedb/v1/relations/{relationName}/instances/{instanceId}";

    //public static final String API_DTREES_LIST = "/rest/fileservice/v1/dtrees/summary";
    public static final String API_DTREES_LIST = "/rest/fileservice/v1/dtrees/query";

    public static final String LIST_SERVICE_LEVEL_URL = "/rest/service-policy/v1/service-levels";
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
