import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class ServiceLevelService {
  constructor(private http: HttpClient) {}
  /**
   * 修改服务等级
   * @param params
   */
  changeServiceLevel(params = {}) {
    return  this.http.post('operatevmfs/updatevmfsservicelevel', params);
  }

  /**
   * 获取所有的服务等级数据
   * @param params
   */
  getServiceLevelList(params = {}) {
    return this.http.put('operatevmfs/listvmfsservicelevel', params);
  }

  /**
   * 通过objectId 获取vmfs
   * @param objectId
   */
  getVmfsById(objectId) {
    return this.http.get('accessvmfs/queryvmfs?dataStoreObjectId='+objectId);
  }
}
