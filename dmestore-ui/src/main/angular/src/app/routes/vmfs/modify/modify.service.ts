import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class ModifyService {
  constructor(private http: HttpClient) {}

  /**
   * 修改VMFS
   * @param volumeId
   * @param params
   */
  updateVmfs(volumeId: string, params = {}) {
    return  this.http.put('operatevmfs/updatevmfs?volumeId=' + volumeId, params);
  }

  /**
   * 通过objectId 获取vmfs
   * @param objectId
   */
  getVmfsById(objectId) {
    return this.http.get('accessvmfs/queryvmfs?dataStoreObjectId='+objectId);
  }

  // 附列表数据
  getChartData(volumeIds: string[] ) {
    return this.http.get('accessvmfs/listvmfsperformance', {params: {volumeIds}});
  }
}
