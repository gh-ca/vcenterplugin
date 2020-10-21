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
    return  this.http.put('operatevmfs/updatevmfs?volume_id=' + volumeId, params);
  }
}
