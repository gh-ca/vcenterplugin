import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class ExpandService {
  constructor(private http: HttpClient) {}

  /**
   * 扩容
   * @param params
   */
  expandVMFS(params = {}) {
    return  this.http.post('operatevmfs/expandvmfs', params);
  }
}
