import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class StorageService {
  constructor(private http: HttpClient) {}

  getData(){
    return this.http.get('http://localhost:8080/dmestorage/storages');
  }
  getStoragePoolListByStorageId(storageId: string){
    return this.http.get('http://localhost:8080/dmestorage/storagepools', {params: {storageId}});
  }
  getLogicPortListByStorageId(storageId: string){
    return this.http.get('http://localhost:8080/dmestorage/logicports', {params: {storageId}});
  }
}
export interface StorageList {
   id: string;
   name: string;
   ip: string;
   status: string;
   synStatus: string;
   vendor: string;
   model: string;
   productVersion: string;
   usedCapacity: number;
   totalCapacity: number;
   totalEffectiveCapacity: number;
   freeEffectiveCapacity: number;
   maxCpuUtilization: number;
   maxIops: number;
   maxBandwidth: number;
   maxLatency: number;
   azIds: string[];
}
export class StoragePool{
  free_capacity: number;
  health_status:string;
  lun_subscribed_capacity: number;
  name:string;
  parent_type:string;
  running_status:string;
  total_capacity: number;
  fs_subscribed_capacity;
  consumed_capacity: number;
  consumed_capacity_percentage:string;
  consumed_capacity_threshold:string;
  storage_pool_id:string;
}
export class LogicPort{
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
