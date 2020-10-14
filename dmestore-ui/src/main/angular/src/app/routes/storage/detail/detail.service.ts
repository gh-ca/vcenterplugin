import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {CapacityChart} from "../storage.service";

@Injectable()
export class DetailService {
  constructor(private http: HttpClient) {}

  getPoolList(params = {}){
    return this.http.get('dmestorage/storages', { params });
  }

  getStorageDetail(storageId: string){
    return this.http.get('dmestorage/storage', {params: {storageId}});
  }

  getStoragePoolList(storageId: string){
    return this.http.get('dmestorage/storagepools', {params: {storageId}});
  }
  getVolumeListList(storageId: string){
    return this.http.get('dmestorage/volumes', {params: {storageId}});
  }
  getFileSystemList(storageId: string){
    return this.http.get('dmestorage/filesystems', {params: {storageId}});
  }
  getDtreeList(storageId: string){
    return this.http.get('dmestorage/dtrees', {params: {storageId}});
  }
  getShareList(storageId: string){
    return this.http.get('dmestorage/nfsshares', {params: {storageId}});
  }
  getControllerList(storageId: string){
    return this.http.get('dmestorage/storagecontrollers', {params: {storageId}});
  }
  getDiskList(storageId: string){
    return this.http.get('dmestorage/storagedisks', {params: {storageId}});
  }
  getPortList(storageId: string){
    return this.http.get('dmestorage/getstorageethports', {params: {storageId}});
  }
}

export class StorageDetail{
  id: string;
  name: string;//名称
  ip: string;//ip地址
  status: string;//状态
  synStatus: string;//同步状态
  sn: string;//设备序列号。
  vendor: string;//厂商
  model: string;//产品型号
  productVersion: string;//产品版本号
  usedCapacity: number;//已用容量 （单位:MB）
  totalCapacity: number; //裸容量（单位:MB）。
  totalEffectiveCapacity: number;//可得容量 （总容量）
  freeEffectiveCapacity: number;//空闲容量
  location: string;
  azIds: string[];
  storagePool: string;
  volume: string;
  fileSystem: string;
  dTrees: string;
  nfsShares: string;
  bandPorts: string;
  logicPorts: string;
  storageControllers: string;
  storageDisks: string;
}
export class StoragePool{
  free_capacity: number;// 空闲容量
  name: string;// 名称
  id: string;// id
  running_status: string;// 运行状态
  health_status: string;// 健康状态
  total_capacity: number;// 总容量
  consumed_capacity: number;//已用容量
  consumed_capacity_percentage: string;// 已用容量百分比(容量利用率)
  storage_pool_id: string;
  storage_instance_id: string;
  storage_device_id: string;
  subscription_rate: number; //订阅率
  //补充字段
  media_type: string;//类型（块）
  tier0_raid_lv: string; // RAID级别
  tier1_raid_lv: string; // RAID级别
  tier2_raid_lv: string; // RAID级别
  storage_id: string; // 存储设备id
  data_space: number; // 存储池上创建LUN或者文件系统时的可用容量 单位MB
  subscribed_capacity: number; //订阅容量
  physicalType: string;//硬盘类型
  diskPoolId:string;//存储池所处硬盘id
}
export class Volume{
  id: string; //卷的唯一标识
  name: string; //名称
  status: string; //状态
  attached: boolean;
  alloctype: string;//分配类型
  service_level_name: string;//服务等级
  storage_id: string;//存储设备id
  pool_raw_id: string;//存储池id
  capacity_usage: string;//容量利用率
  protectionStatus: boolean;//保护状态
  hostIds: string[];
  hostGroupIds: string;
  storage_pool_name: string;//存储池名称
  capacity: number;//总容量 单位GB
  //关联的datastore
  datastores: string;
}
export class FileSystem{
  id: string; //id
  name: string;//名称
  health_status: string; //状态
  alloc_type: string; //分配策略
  capacity_usage_ratio: number; //容量使用率
  storage_pool_name: string;//存储池名字
  nfs_count: number; //nfs
  cifs_count: number;//cifs
  dtree_count: number; //dtree
  capacity: number;//总容量
  allocate_quota_in_pool: number;
  available_capacity: number;
  min_size_fs_capacity: number;
  storage_id: string;
}
export class Dtrees{
  name: string;
  fs_name: string; //所属文件系统名称
  quota_switch: boolean; //配额
  security_style: string;//安全模式
  tier_name: string;//服务等级名称
  nfs_count: number;//nfs
  cifs_count: number;
}
export class NfsShare {
  name: string; //名称
  share_path: string; //共享路径
  storage_id: string; //存储设备id
  tier_name: string; //服务等级
  owning_dtree_name: string;//所属dtree
  fs_name: string; //所属文件系统名字在
  owning_dtree_id: string; //所属dtreeid
}
export class BandPorts{
  id: string;
  name: string;
  health_status: string;
  running_status: string;
  mtu: string;
}
export class LogicPorts{
  id: string;
  name: string;
  running_status: string;
  operational_status: string;
  mgmt_ip: string;
  mgmt_ipv6: string;
  home_port_id: string;
  home_port_name: string;
  current_port_id: string;
  current_port_name: string;
  role: string;
  ddns_status: string;
  support_protocol: string;
  management_access: string;
  vstore_id: string;
  vstore_name: string;
}
export class StorageControllers{
  name: string;
  status: string;
  softVer: string;
  cpuInfo: string;
}
export class StorageDisk{
  name: string;
  status: string;
  capacity: string;
}
export interface PoolList {
   name: string;
   status: string;
  diskType: string;
  type: string;
  serviceLevel: string;
  raidLevel: string;
}
export class CapacityDistribution{
  protection: string;
  fileSystem: string;
  volume: string;
  freeCapacity: string;
  chart: CapacityChart;
}
export class StorageController{
  name:string;
  status:string;
  softVer:string;
  cpuInfo:string;
}

