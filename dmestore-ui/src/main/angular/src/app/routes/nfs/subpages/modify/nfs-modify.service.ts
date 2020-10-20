import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class NfsModifyService{
  constructor(private http: HttpClient) {}
}
