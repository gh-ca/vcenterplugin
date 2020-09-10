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


    /**
     * DME 任务查询URL
     **/
    public static final String DME_TASK_BASE_URL = "/rest/taskmgmt/v1/tasks";


    /**
     * DME 卷映射给主机URL
     **/
    public static final String DME_HOST_MAPPING_URL = "/rest/blockservice/v1/volumes/host-mapping";

    /**
     * NFS share列表 POST
     */
    public static final String DME_NFS_SHARE_URL = "/rest/fileservice/v1/nfs-shares/summary";

    /**
     * NFS logic_port query
     */
    public static final String DME_NFS_LOGICPORT_QUERY_URL = "/rest/storagemgmt/v1/storage-port/logic-ports?storage_id=#{storage_id}";
}
