import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

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
}

export class StorageDetail{
  id: string;
  name: string;
  ip: string;
  status: string;
  synStatus: string;
  sn: string;
  vendor: string;
  model: string;
  productVersion: string;
  usedCapacity: number;
  totalCapacity: number;
  totalEffectiveCapacity: number;
  freeEffectiveCapacity: number;
  location: number;
  azIds: string[];
}

export class StoragePool{
  free_capacity: number;
  health_status: string;
  lun_subscribed_capacity: number;
  name: string;
  parent_type: string;
  running_status: string;
  total_capacity: number;
  fs_subscribed_capacity: number;
  consumed_capacity: number;
  consumed_capacity_percentage: string;
  consumed_capacity_threshold: string;
  storage_pool_id: string;
}
export class Volume{
  id: string;
  name: string;
  status: string;
  attached: boolean;
  alloctype: string;
  service_level_name: string;
  storage_id: string;
  pool_raw_id: string;
  capacity_usage: string;
  protectionStatus: boolean;
}
export class FileSystem{
  id: string;
  name: string;
  health_status: string;
  alloc_type: string;
  capacity: number;
  capacity_usage_ratio: number;
  storage_pool_name: string;
  nfs_count: number;
  cifs_count: number;
  dtree_count: number;
}
export class Dtrees{
  name: string;
  fs_name: string;
  quota_switch: boolean;
  security_style: string;
  tier_name: string;
  nfs_count: number;
}
export class NfsShare {
  name: string;
  share_path: string;
  storage_id: string;
  tier_name: string;
  owning_dtree_name: string;
  fs_name: string;
  owning_dtree_id: string;
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

