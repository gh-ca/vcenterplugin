import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class NfsService {
  constructor(private http: HttpClient) {}

  getData() {
    return this.http.get('http://localhost:8080/accessnfs/listnfs', );
  }
  getChartData(fsIds: string[] ) {
    return this.http.get('http://localhost:8080/accessnfs/listnfsperformance', {params: {fsIds}});
  }
  addNfs(params= {}){
    return this.http.post('http://localhost:8080/operatenfs/operatenfs', { params });
  }

  mountNfs(params= {}){
    return this.http.post('http://localhost:8080/accessnfs/mountnfs', params);
  }
  delNfs(param: string){
    return this.http.post('http://localhost:8080/accessnfs/delnfs', param);
  }
  getHostList(dataStoreName: string){
    return this.http.get('http://localhost:8080/accessvmware/gethostsbydsname', {params: {dataStoreName}});
  }
  getClusterList(dataStoreName: string){
    return this.http.get('http://localhost:8080/accessvmware/getclustersbydsname', {params: {dataStoreName}});
  }
}
export interface List {
   name: string;    // 名称
   status: string;  // 状态
  capacity: number;  // 总容量 单位GB
  freeSpace: number; // 空闲容量 单位GB
  reserveCapacity: number; // 置备容量  capacity+uncommitted-freeSpace 单位GB
   deviceId: string; // 存储设备ID
   device: string; // 存储设备名称
   logicPort: string; // 逻辑端口
   logicPortId: string; // 逻辑端口 id
   shareIp: string; // share ip
   sharePath: string; // share path
   share: string; // share 名称
   shareId: string; // share id
   fs: string; // fs
   fsId: string; // fs id
  ops: number; // OPS
  bandwidth: number;   // 带宽 单位MB/s
  readResponseTime: number;   // 读响应时间 单位ms
  writeResponseTime: number; // 写响应时间 单位ms
  objectid: string; //
}
// =================添加NFS参数 start=========
export class AddNfs{
  storage_id: string;
  pool_raw_id: number;
  exportPath: string;
  nfsName: string;
  accessMode: string;
  type: string;
  filesystem_specs: FileSystem[];
  capacity_autonegotiation: Autonegotiation;
  tuning: Advance;
  qos_policy: QosPolicy;
  create_nfs_share_param: Share;
  nfs_share_client_addition: ShareClient[];
}
export class FileSystem{
  capacity: number;
  name: string;
  count: number;
}
export class Autonegotiation{
  capacity_self_adjusting_mode: string;
  capacity_recycle_mode: string;
  auto_size_enable: boolean;
  auto_grow_threshold_percent: number;
  auto_shrink_threshold_percent: number;
  max_auto_size: number;
  min_auto_size: number;
  auto_size_increment: number;
}
export class Advance{
  deduplication_enabled: boolean;
  compression_enabled: boolean;
  application_scenario: string;
  block_size: number;
  allocation_type: string;
}
export class QosPolicy{
  max_bandwidth: number;
  max_iops: number;
  min_bandwidth: number;
  min_iops: number;
  latency: number;
}
export class Share{
  name: string;
  share_path: string;
}
export class ShareClient{
  name: string;
  accessval: string;
  sync: string;
  all_squash: string;
  root_squash: string;
}

// =================添加NFS参数 end =========

export class Mount{
  dataStoreName: string;
  hosts: string[];
  clusters: string[];
  mountType: string;
}

export class Host{
  hostId: string;
  hostName: string;
}
export class Cluster{
  clusterId: string;
  clusterName: string;
}

