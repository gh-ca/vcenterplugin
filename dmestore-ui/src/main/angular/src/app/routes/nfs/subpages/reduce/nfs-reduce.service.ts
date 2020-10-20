import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class NfsReduceService{
  constructor(private http: HttpClient) {}
  changeCapacity(params= {}){
    return this.http.post('operatenfs/changenfsdatastore', params);
  }
}

