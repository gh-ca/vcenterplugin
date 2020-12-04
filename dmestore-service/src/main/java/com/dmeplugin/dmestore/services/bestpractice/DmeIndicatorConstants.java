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
    public static final String RESOURCE_TYPE_NAME_CONTROLLER = "SYS_Controller";
    public static final String RESOURCE_TYPE_NAME_STORAGEPORT = "SYS_StoragePort";
    public static final String RESOURCE_TYPE_NAME_STORAGEDISK= "SYS_StorageDisk";

    //SYS_Lun 1125921381679104
    public static final String COUNTER_ID_VOLUME_THROUGHPUT = "1125921381744641";//IOPS
    public static final String COUNTER_ID_VOLUME_RESPONSETIME = "1125921381744642";//平均IO响应时间
    public static final String COUNTER_ID_VOLUME_BANDWIDTH = "1125921381744643";//带宽
    public static final String COUNTER_ID_VOLUME_READBANDWIDTH = "1125921381744646";//读带宽
    public static final String COUNTER_ID_VOLUME_WRITEBANDWIDTH = "1125921381744647";//写带宽
    public static final String COUNTER_ID_VOLUME_READTHROUGHPUT = "1125921381744648";//读IOPS
    public static final String COUNTER_ID_VOLUME_WRITETHROUGHPUT = "1125921381744649";//写IOPS
    public static final String COUNTER_ID_VOLUME_READRESPONSETIME = "1125921381744656";//读响应时间
    public static final String COUNTER_ID_VOLUME_WRITERESPONSETIME = "1125921381744657";//写响应时间

    //文件系统 SYS_StorageFileSystem 1126179079716864
    public static final String COUNTER_ID_FS_THROUGHPUT = "1126179079716865";//OPS
    public static final String COUNTER_ID_FS_READTHROUGHPUT = "1126179079716867";//读OPS
    public static final String COUNTER_ID_FS_READBANDWIDTH = "1126179079716868";//读带宽
    public static final String COUNTER_ID_FS_READRESPONSETIME = "1126179079716869";//读OPS响应时间
    public static final String COUNTER_ID_FS_WRITETHROUGHPUT = "1126179079716870";//写OPS
    public static final String COUNTER_ID_FS_WRITEBANDWIDTH = "1126179079716871";//写带宽
    public static final String COUNTER_ID_FS_WRITERESPONSETIME = "1126179079716872";//写OPS响应时间
    public static final String COUNTER_ID_FS_BANDWIDTH = "1126179079716878";//带宽

    // 服务等级 SYS_DjTier 1126174784749568
    public static final String COUNTER_ID_SERVICELECVEL_MAXRESPONSETIME = "1126174784815111";//最大响应时间
    public static final String COUNTER_ID_SERVICELEVEL_THROUGHPUTTIB = "1126174784815117";//卷IO密度
    public static final String COUNTER_ID_SERVICELEVEL_BANDWIDTHTIB = "1126174784815118";//总带宽

    //存储池 SYS_StoragePool 1125912791744512
    public static final String COUNTER_ID_STORAGEPOOL_THROUGHPUT = "1125912791810049";//throughput IOPS
    public static final String COUNTER_ID_STORAGEPOOL_RESPONSETIME = "1125912791810050";//响应时间
    public static final String COUNTER_ID_STORAGEPOOL_BANDWIDTH = "1125912791810051";//带宽

    //存储 SYS_StorDevice 1125904201809920
    public static final String COUNTER_ID_STORDEVICE_CPUUSAGE = "1125904201875457";//CPU使用率
    public static final String COUNTER_ID_STORDEVICE_BANDWIDTH = "1125904201875458";//带宽
    public static final String COUNTER_ID_STORDEVICE_READBANDWIDTH = "1125904201875459";//读带宽
    public static final String COUNTER_ID_STORDEVICE_WRITEBANDWIDTH = "1125904201875460";//写带宽
    public static final String COUNTER_ID_STORDEVICE_READTHROUGHPUT = "1125904201875461";//读IOPS
    public static final String COUNTER_ID_STORDEVICE_WRITETHROUGHPUT = "1125904201875462";//写IOPS
    public static final String COUNTER_ID_STORDEVICE_RESPONSETIME = "1125904201875464";//平均IO响应时间
    public static final String COUNTER_ID_STORDEVICE_THROUGHPUT = "1125904201875465";//IOPS

    //存储控制器 SYS_Controller 1125908496777216
    public static final String COUNTER_ID_CONTROLLER_CPUUSAGE = "1125908496842753";//CPU使用率
    public static final String COUNTER_ID_CONTROLLER_BANDWIDTH = "1125908496842755";//带宽
    public static final String COUNTER_ID_CONTROLLER_READBANDWIDTH = "1125908496842756";//读带宽
    public static final String COUNTER_ID_CONTROLLER_WRITEBANDWIDTH = "1125908496842757";//写带宽
    public static final String COUNTER_ID_CONTROLLER_READTHROUGHPUT = "1125908496842758";//读IOPS
    public static final String COUNTER_ID_CONTROLLER_WRITETHROUGHPUT = "1125908496842759";//写IOPS
    public static final String COUNTER_ID_CONTROLLER_RESPONSETIME = "1125908496842762";//平均响应时间
    public static final String COUNTER_ID_CONTROLLER_THROUGHPUT = "1125908496842763";//IOPS
    public static final String COUNTER_ID_CONTROLLER_READRESPONSETIME = "1125908496842770";//平均读IO响应时间
    public static final String COUNTER_ID_CONTROLLER_WRITERESPONSETIME = "1125908496842771";//平均写IO响应时间

    //存储端口 SYS_StoragePort 1125925676646400
    public static final String COUNTER_ID_STORAGEPORT_BANDWIDTH = "1125925676711938";//带宽
    public static final String COUNTER_ID_STORAGEPORT_READBANDWIDTH = "1125925676711939";//读带宽
    public static final String COUNTER_ID_STORAGEPORT_WRITEBANDWIDTH = "1125925676711940";//写带宽
    public static final String COUNTER_ID_STORAGEPORT_READTHROUGHPUT = "1125925676711943";//读IOPS
    public static final String COUNTER_ID_STORAGEPORT_WRITETHROUGHPUT = "1125925676711944";//写IOPS
    public static final String COUNTER_ID_STORAGEPORT_RESPONSETIME = "1125925676711945";//响应时间
    public static final String COUNTER_ID_STORAGEPORT_THROUGHPUT = "1125925676711946";//IOPS
    public static final String COUNTER_ID_STORAGEPORT_UTILITY = "1125925676711951";//利用率
    public static final String COUNTER_ID_STORAGEPORT_READRESPONSETIME = "1125925676711952";//平均读IO响应时间 ms
    public static final String COUNTER_ID_STORAGEPORT_WRITERESPONSETIME = "1125925676711953";//平均写IO响应时间 ms

    //存储硬盘 SYS_StorageDisk 1125917086711808
    public static final String COUNTER_ID_STORAGEDISK_UTILITY = "1125917086777345";//利用率
    public static final String COUNTER_ID_STORAGEDISK_BANDWIDTH = "1125917086777346";//带宽
    public static final String COUNTER_ID_STORAGEDISK_READTHROUGHPUT = "1125917086777347";//读IOPS
    public static final String COUNTER_ID_STORAGEDISK_WRITETHROUGHPUT = "1125917086777348";//写IOPS
    public static final String COUNTER_ID_STORAGEDISK_RESPONSETIME = "1125917086777351";//平均IO响应时间


}
