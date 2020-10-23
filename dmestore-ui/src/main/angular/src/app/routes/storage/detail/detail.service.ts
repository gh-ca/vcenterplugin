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
  listnfsperformance(storagePoolIds:string[]) {
    return this.http.get('dmestorage/liststoragepoolperformance', {params: {storagePoolIds}});
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
  getControllerList(storageDeviceId: string){
    return this.http.get('dmestorage/storagecontrollers', {params: {storageDeviceId}});
  }
  getDiskList(storageDeviceId: string){
    return this.http.get('dmestorage/storagedisks', {params: {storageDeviceId}});
  }
  getFCPortList(param: any){
    return this.http.get('dmestorage/storageport', {params: param});
  }
  getBondPortList(storage_id: string){
    return this.http.get('dmestorage/bandports', {params: {storage_id}});
  }
  getLogicPortList(storageId: string){
    return this.http.get('dmestorage/logicports', {params: {storageId}});
  }
  getFailoverGroups(storage_id: string){
    return this.http.get('dmestorage/failovergroups', {params: {storage_id}});
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
  patchVersion:string;
  maintenanceOvertime: string;
  maintenanceStart: string;
}
export class StoragePool{
  freeCapacity: number;// 空闲容量
  name: string;// 名称
  id: string;// id
  runningStatus: string;// 运行状态
  healthStatus: string;// 健康状态
  totalCapacity: number;// 总容量
  consumedCapacity: number;//已用容量
  consumedCapacityPercentage: string;// 已用容量百分比(容量利用率)
  storagePoolId: string;
  storageInstanceId: string;
  storageDeviceId: string;
  subscriptionRate: number; //订阅率
  //补充字段
  mediaType: string;//类型（块）
  tier0RaidLv: string; // RAID级别
  tier1RaidLv: string; // RAID级别
  tier2RaidLv: string; // RAID级别
  storageId: string; // 存储设备id
  dataSpace: number; // 存储池上创建LUN或者文件系统时的可用容量 单位MB
  subscribedCapacity: number; //订阅容量
  physicalType: string;//硬盘类型
  diskPoolId:string;//存储池所处硬盘id
  maxBandwidth: number;
  maxIops: number;
  maxLatency: number;
  serviceLevelName: string;//服务等级
}
export class Volume{
  id: string; //卷的唯一标识
  name: string; //名称
  status: string; //状态
  attached: boolean;// 映射状态
  alloctype: string;//分配类型
  serviceLevelName: string;//服务等级
  storageId: string;//存储设备id
  poolRawId: string;//存储池id
  capacityUsage: string;//容量利用率
  protectionStatus: boolean;//保护状态
  hostIds: string[];
  hostGroupIds: string;
  storagePoolName: string;//存储池名称
  capacity: number;//总容量 单位GB
  //关联的datastore
  datastores: string;
}
export class Dtrees{
  name: string;
  fsName: string; //所属文件系统名称
  quotaSwitch: boolean; //配额
  securityStyle: string;//安全模式
  tierName: string;//服务等级名称
  nfsCount: number;//nfs
  cifsCount: number;
}
export class NfsShare {
  name: string; //名称
  sharePath: string; //共享路径
  storageId: string; //存储设备id
  tierName: string; //服务等级
  owningDtreeName: string;//所属dtree
  fsName: string; //所属文件系统名字在
  owningDtreeId: string; //所属dtreeid
}
export class StorageDisk{
  capacity: number;
  diskDomain: string;
  logicalType:string;
  name: string;
  performance: string;
  physicalType: string;
  poolId: string;
  speed: number
  status: string;
  storageDeviceId: string;
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

