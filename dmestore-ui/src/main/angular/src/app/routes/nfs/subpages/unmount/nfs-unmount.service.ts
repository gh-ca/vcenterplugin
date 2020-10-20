import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class NfsUnmountService{
  constructor(private http: HttpClient) {}
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
