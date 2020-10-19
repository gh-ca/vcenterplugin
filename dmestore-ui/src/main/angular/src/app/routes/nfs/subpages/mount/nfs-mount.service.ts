import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class NfsMountService{
  constructor(private http: HttpClient) {}
}
