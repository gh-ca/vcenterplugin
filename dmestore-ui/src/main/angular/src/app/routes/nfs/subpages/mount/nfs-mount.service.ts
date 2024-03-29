import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class NfsMountService {
  constructor(private http: HttpClient) {}
  getHostListByObjectId(dataStoreObjectId: string) {
    return this.http.get('accessvmware/gethostsbydsobjectid', { params: { dataStoreObjectId } });
  }
  getClusterListByObjectId(dataStoreObjectId: string) {
    return this.http.get('accessvmware/getclustersbydsobjectid', { params: { dataStoreObjectId } });
  }
  //主机获取存储
  getDatastoreListByHostObjectId(hostObjectId: string, dataStoreType: string) {
    return this.http.get('accessvmware/getdatastoresbyhostobjectid', {
      params: { hostObjectId, dataStoreType },
    });
  }
  //集群获取存储
  getDatastoreListByClusterObjectId(clusterObjectId:string,dataStoreType) {
    return this.http.get('accessvmware/getdatastoresbyclusterobjectid', {
      params:{clusterObjectId,dataStoreType}
    });
  }
  mountNfs(params = {}) {
    return this.http.post('accessnfs/mountnfs', params);
  }
  //获取主机对应的虚拟ip列表
  getVmkernelListByObjectId(hostObjectId: string,datastoreObjectId:string='') {
    return this.http.get('accessvmware/getvmkernelipbyhostobjectid', { params: { hostObjectId,datastoreObjectId} });
  }
  getLogicPortListByStorageId(storageId: string, flag) {
    if (flag) {
      return this.http.get('dmestorage/logicports', { params: { storageId, flag } });
    }
    return this.http.get('dmestorage/logicports', { params: { storageId } });
  }
}
export class DataStore {
  freeSpace: string;
  name: string;
  id: string;
  type: string;
  objectId: string;
  status: string;
  capacity: string;
}
export class Vmkernel {
  hostObjectId:string;
  portgroup: string;
  ipAddress: string;
  device: string;
  key: string;
  mac: string;
}
export class Mount {
  dataStoreObjectId: string;
  hostObjectId;
  hostVkernelIp: string; //  虚拟网卡ip
  mountType: string;
}
