import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class NfsReduceService{
  constructor(private http: HttpClient) {}
  changeCapacity(params= {}){
    return this.http.put('operatenfs/changenfsdatastore', params);
  }

  /**
   * 通过objectId 获取vmfs存储数据
   * @param objectId
   */
  getStorageById(objectId) {
    return this.http.get('accessvmware/relation?datastoreObjectId='+objectId);
  }

  getNfsDetailById(storeObjectId:string){
    return this.http.get('operatenfs/editnfsstore',{params: {storeObjectId}} );
  }
}

