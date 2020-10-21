import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class MountService {
  constructor(private http: HttpClient) {}

  /**
   * 通过hostId 获取未挂载的VMFS
   * @param hostObjectId
   * @param dataStoreType
   */
  getDataStoreByHostId(hostObjectId:string, dataStoreType:string) {
    return this.http.get('accessvmware/getdatastoresbyhostobjectid?hostObjectId=' + hostObjectId + '&dataStoreType=' + dataStoreType);
  }

  /**
   * 通过clusterId 获取未挂载的VMFS
   * @param hostObjectId
   * @param dataStoreType
   */
  getDataStoreByClusterId(clusterObjectId:string, dataStoreType:string) {
    return this.http.get('accessvmware/getdatastoresbyhostobjectid?clusterObjectId=' + clusterObjectId + '&dataStoreType=' + dataStoreType);
  }

  /**
   * 挂载
   * @param params
   */
  mountVmfs(params = {}) {
    return  this.http.post('accessvmfs/mountvmfs', params);
  }

  // 通过objectId获取主机
  getHostListByObjectId(objectId: string){
    return this.http.get('accessvmware/gethostsbydsobjectid?dataStoreObjectId=' + objectId);
  }
  // 通过objectId获取集群
  getClusterListByObjectId(objectId: string){
    return this.http.get('accessvmware/getclustersbydsobjectid?dataStoreObjectId=' + objectId);
  }

  // 卸载
  unmountVMFS(params = {}) {
    return  this.http.post('/accessvmfs/ummountvmfs', params);
  }
}

export interface DataStore {
  'freeSpace': string;
  'name': string;
  'id': string;
  'type': string;
  'objectId': string;
  'status': boolean;
  'capacity': number;
}
