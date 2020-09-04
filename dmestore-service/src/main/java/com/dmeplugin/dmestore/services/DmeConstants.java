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
}
