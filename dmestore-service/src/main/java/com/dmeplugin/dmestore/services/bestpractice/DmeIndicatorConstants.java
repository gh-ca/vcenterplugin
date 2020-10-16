package com.dmeplugin.dmestore.services.bestpractice;

/**
 * @Description: TODO
 * @ClassName: DmeIndicatorConstants
 * @Company: GH-CA
 * @author: liuxh
 * @create: 2020-09-24
 **/
public class DmeIndicatorConstants {

    public static final String RESOURCE_TYPE_NAME_STORAGEDEVICE = "SYS_StorDevice";
    public static final String RESOURCE_TYPE_NAME_STORAGEPOOL = "SYS_StoragePool";
    public static final String RESOURCE_TYPE_NAME_SERVICELEVEL = "SYS_DjTier";
    public static final String RESOURCE_TYPE_NAME_LUN = "SYS_Lun";
    public static final String RESOURCE_TYPE_NAME_FILESYSTEM = "SYS_StorageFileSystem";

    //SYS_Lun 1125921381679104
    public static final String COUNTER_ID_VOLUME_THROUGHPUT = "1125921381744641";//IOPS
    public static final String COUNTER_ID_VOLUME_RESPONSETIME = "1125921381744642";//平均IO响应时间
    public static final String COUNTER_ID_VOLUME_BANDWIDTH = "1125921381744643";//带宽
    public static final String COUNTER_ID_VOLUME_READHITRATIO = "1125921381744644";//读缓存命中率
    public static final String COUNTER_ID_VOLUME_WRITEHITRATIO = "1125921381744645";//写缓存命中率
    public static final String COUNTER_ID_VOLUME_READBANDWIDTH = "1125921381744646";//读带宽
    public static final String COUNTER_ID_VOLUME_WRITEBANDWIDTH = "1125921381744647";//写带宽
    public static final String COUNTER_ID_VOLUME_READTHROUGHPUT = "1125921381744648";//读IOPS
    public static final String COUNTER_ID_VOLUME_WRITETHROUGHPUT = "1125921381744649";//写IOPS
    public static final String COUNTER_ID_VOLUME_QUEUELENGTH = "1125921381744650";//队列长度
    public static final String COUNTER_ID_VOLUME_UTILITY = "1125921381744651";//利用率
    public static final String COUNTER_ID_VOLUME_READSIZE = "1125921381744652";//平均读I/O大小
    public static final String COUNTER_ID_VOLUME_WRITESIZE = "1125921381744653";//平均写I/O大小
    public static final String COUNTER_ID_VOLUME_SERVICETIME = "1125921381744654";//平均I/O服务时间
    public static final String COUNTER_ID_VOLUME_MAXRESPONSETIME = "1125921381744655";//最大响应时间
    public static final String COUNTER_ID_VOLUME_READRESPONSETIME = "1125921381744656";//读响应时间
    public static final String COUNTER_ID_VOLUME_WRITERESPONSETIME = "1125921381744657";//写响应时间
    public static final String COUNTER_ID_VOLUME_READRATIO = "1125921381744658";//读I/O百分比
    public static final String COUNTER_ID_VOLUME_WRITERATIO = "1125921381744659";//写I/O百分百
    public static final String COUNTER_ID_VOLUME_HITRATIO = "1125921381744660";//缓存命中率

    //文件系统 SYS_StorageFileSystem 1126179079716864
    public static final String COUNTER_ID_FS_THROUGHPUT = "1126179079716865";//OPS
    public static final String COUNTER_ID_FS_SERVICETIME = "1126179079716866";//服务时间
    public static final String COUNTER_ID_FS_READTHROUGHPUT = "1126179079716867";//读OPS
    public static final String COUNTER_ID_FS_READBANDWIDTH = "1126179079716868";//读带宽
    public static final String COUNTER_ID_FS_READRESPONSETIME = "1126179079716869";//读OPS响应时间
    public static final String COUNTER_ID_FS_WRITETHROUGHPUT = "1126179079716870";//写OPS
    public static final String COUNTER_ID_FS_WRITEBANDWIDTH = "1126179079716871";//写带宽
    public static final String COUNTER_ID_FS_WRITERESPONSETIME = "1126179079716872";//写OPS响应时间
    public static final String COUNTER_ID_FS_READSIZE = "1126179079716873";//读I/O大小
    public static final String COUNTER_ID_FS_WRITESIZE = "1126179079716874";//写I/O大小
    public static final String COUNTER_ID_FS_IOSIZE = "1126179079716875";//平均IO大小
    public static final String COUNTER_ID_FS_CACHEPAGESIZE = "1126179079716876";//Cache page保有量
    public static final String COUNTER_ID_FS_CACHECHUNKSIZE = "1126179079716877";//Cache chunk保有量
    public static final String COUNTER_ID_FS_BANDWIDTH = "1126179079716878";//带宽

    // 服务等级 SYS_DjTier 1126174784749568
    public static final String COUNTER_ID_SERVICELECVEL_MAXRESPONSETIME = "1126174784815111";//最大响应时间
    public static final String COUNTER_ID_SERVICELEVEL_THROUGHPUTTIB = "1126174784815117";//卷IO密度
    public static final String COUNTER_ID_SERVICELEVEL_BANDWIDTHTIB = "1126174784815118";//总带宽

    //存储池 SYS_StoragePool 1125912791744512
    public static final String COUNTER_ID_STORAGEPOOL_THROUGHPUT = "1125912791810049";//throughput IOPS
    public static final String COUNTER_ID_STORAGEPOOL_RESPONSETIME = "1125912791810050";//响应时间
    public static final String COUNTER_ID_STORAGEPOOL_BANDWIDTH = "1125912791810051";//带宽
    public static final String COUNTER_ID_STORAGEPOOL_READBANDWIDTH = "1125912791810052";//读带宽
    public static final String COUNTER_ID_STORAGEPOOL_WRITEBANDWIDTH = "1125912791810053";//写带宽
    public static final String COUNTER_ID_STORAGEPOOL_READTHROUGHPUT = "1125912791810054";//读IOPS
    public static final String COUNTER_ID_STORAGEPOOL_WRITETHROUGHPUT = "1125912791810055";//写IOPS

    //存储 SYS_StorDevice 1125904201809920
    public static final String COUNTER_ID_STORDEVICE_CPUUSAGE = "1125904201875457";//CPU使用率
    public static final String COUNTER_ID_STORDEVICE_BANDWIDTH = "1125904201875458";//带宽
    public static final String COUNTER_ID_STORDEVICE_READBANDWIDTH = "1125904201875459";//读带宽
    public static final String COUNTER_ID_STORDEVICE_WRITEBANDWIDTH = "1125904201875460";//写带宽
    public static final String COUNTER_ID_STORDEVICE_READTHROUGHPUT = "1125904201875461";//读IOPS
    public static final String COUNTER_ID_STORDEVICE_WRITETHROUGHPUT = "1125904201875462";//写IOPS
    public static final String COUNTER_ID_STORDEVICE_MEMORYUSAGE = "1125904201875463";//内存占用率
    public static final String COUNTER_ID_STORDEVICE_RESPONSETIME = "1125904201875464";//平均IO响应时间
    public static final String COUNTER_ID_STORDEVICE_THROUGHPUT = "1125904201875465";//IOPS
}
