import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class NfsMountService{
  constructor(private http: HttpClient) {}
  getHostListByObjectId(dataStoreObjectId: string){
    return this.http.get('accessvmware/gethostsbydsobjectid', {params: {dataStoreObjectId}});
  }
  getClusterListByObjectId(dataStoreObjectId: string){
    return this.http.get('accessvmware/getclustersbydsobjectid', {params: {dataStoreObjectId}});
  }

  getDatastoreListByHostObjectId(params:{}){
    return this.http.get('accessvmware/getdatastoresbyhostobjectid', params);
  }
  getDatastoreListByClusterObjectId(params:{}){
    return this.http.get('accessvmware/getdatastoresbyclusterobjectid', params);
  }
  mountNfs(params= {}){
    return this.http.post('accessnfs/mountnfs', params);
  }
}
export class DataStore{
  freeSpace:string;
  name: string;
  id : string;
  type: string;
  objectId: string;
  status: string;
  capacity: string;
}
