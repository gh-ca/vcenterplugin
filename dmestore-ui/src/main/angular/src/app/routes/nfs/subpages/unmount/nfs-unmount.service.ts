import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class NfsUnmountService{
  constructor(private http: HttpClient) {}
}
