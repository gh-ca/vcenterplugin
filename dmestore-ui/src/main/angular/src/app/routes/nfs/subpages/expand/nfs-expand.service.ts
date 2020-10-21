import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class NfsExpandService{
  constructor(private http: HttpClient) {}
  changeCapacity(params= {}){
    return this.http.put('operatenfs/changenfsdatastore', params);
  }
}

