import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class NfsDeleteService{
  constructor(private http: HttpClient) {}
  delNfs(dataStoreObjectId: string){
    return this.http.post('accessnfs/delnfs', {params: {dataStoreObjectId}});
  }
}
