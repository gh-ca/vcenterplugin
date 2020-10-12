package com.dmeplugin.dmestore.services.bestpractice;

/**
 * @Description: TODO
 * @ClassName: DmeIndicatorConstants
 * @Company: GH-CA
 * @author: liuxh
 * @create: 2020-09-24
 **/
public class DmeIndicatorConstants {
    public static final String COUNTER_ID_VMFS_THROUGHPUT = "1125921381744641";//IOPS
    public static final String COUNTER_ID_VMFS_BANDWIDTH = "1125921381744643";//bandwidth
    public static final String COUNTER_ID_VMFS_READTHROUGHPUT = "1125921381744648";//读IOPS
    public static final String COUNTER_ID_VMFS_WRITETHROUGHPUT = "1125921381744649";//写IOPS
    public static final String COUNTER_ID_VMFS_READBANDWIDTH = "1125921381744646";//读带宽
    public static final String COUNTER_ID_VMFS_WRITEBANDWIDTH = "1125921381744647";//写带宽
    public static final String COUNTER_ID_VMFS_READRESPONSETIME = "1125921381744656";//读响应时间
    public static final String COUNTER_ID_VMFS_WRITERESPONSETIME = "1125921381744657";//写响应时间

    public static final String COUNTER_ID_VOLUME_THROUGHPUT = "1125921381744641";//IOPS
    public static final String COUNTER_ID_VOLUME_RESPONSETIME = "1125921381744642";//平均IO响应时间
    public static final String COUNTER_ID_VOLUME_BANDWIDTH = "1125921381744643";//带宽
    public static final String COUNTER_ID_VOLUME_READTHROUGHPUT = "1125921381744648";//读IOPS
    public static final String COUNTER_ID_VOLUME_WRITETHROUGHPUT = "1125921381744649";//写IOPS
    public static final String COUNTER_ID_VOLUME_READBANDWIDTH = "1125921381744646";//读带宽
    public static final String COUNTER_ID_VOLUME_WRITEBANDWIDTH = "1125921381744647";//写带宽
    public static final String COUNTER_ID_VOLUME_READRESPONSETIME = "1125921381744656";//读响应时间
    public static final String COUNTER_ID_VOLUME_WRITERESPONSETIME = "1125921381744657";//写响应时间

    public static final String COUNTER_ID_NFS_READTHROUGHPUT = "1125904201875461";//读IOPS
    public static final String COUNTER_ID_NFS_WRITETHROUGHPUT = "1125904201875462";//写IOPS
    public static final String COUNTER_ID_NFS_READBANDWIDTH = "1125904201875459";//读带宽
    public static final String COUNTER_ID_NFS_WRITEBANDWIDTH = "1125904201875460";//写带宽
    public static final String COUNTER_ID_NFS_RESPONSETIME = "1125904201875464";//响应时间

    // FS的性能指标 文档中没找到 暂按存储设备来处理
    public static final String COUNTER_ID_FS_READTHROUGHPUT = "1125904201875461";//读IOPS
    public static final String COUNTER_ID_FS_WRITETHROUGHPUT = "1125904201875462";//写IOPS
    public static final String COUNTER_ID_FS_READBANDWIDTH = "1125904201875459";//读带宽
    public static final String COUNTER_ID_FS_WRITEBANDWIDTH = "1125904201875460";//写带宽
    public static final String COUNTER_ID_FS_RESPONSETIME = "1125904201875464";//响应时间

    // 服务等级
    public static final String COUNTER_ID_SERVICELECVEL_MAXRESPONSETIME = "1126174784815111";//最大响应时间
    public static final String COUNTER_ID_SERVICELEVEL_THROUGHPUTTIB = "1126174784815117";//卷IO密度
    public static final String COUNTER_ID_SERVICELEVEL_BANDWIDTHTIB = "1126174784815118";//总带宽

    //存储池
    public static final String COUNTER_ID_STORAGEPOOL_THROUGHPUT = "1125912791810049";//throughput IOPS
    public static final String COUNTER_ID_STORAGEPOOL_RESPONSETIME = "1125912791810050";//响应时间
    public static final String COUNTER_ID_STORAGEPOOL_BANDWIDTH = "1125912791810051";//带宽
    public static final String COUNTER_ID_STORAGEPOOL_READBANDWIDTH = "1125912791810052";//读带宽
    public static final String COUNTER_ID_STORAGEPOOL_WRITEBANDWIDTH = "1125912791810053";//写带宽
    public static final String COUNTER_ID_STORAGEPOOL_READTHROUGHPUT = "1125912791810054";//读IOPS
    public static final String COUNTER_ID_STORAGEPOOL_WRITETHROUGHPUT = "1125912791810055";//写IOPS

}
