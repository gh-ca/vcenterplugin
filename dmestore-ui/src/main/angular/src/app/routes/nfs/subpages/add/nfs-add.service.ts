import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class NfsAddService{
  constructor(private http: HttpClient) {}
}
