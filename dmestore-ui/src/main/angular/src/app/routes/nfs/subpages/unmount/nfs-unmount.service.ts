import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class NfsUnmountService{
  constructor(private http: HttpClient) {}
  //获取ds挂载的集群和主机列表
  getMountedHostList(storageId: string){
    return this.http.get('accessnfs/gethostsbystorageid/'+storageId);
  }
  getMountedClusterList(storageId: string){
    return this.http.get('accessnfs/gethostgroupsbystorageid/'+storageId);
  }
  unmount(params={}){
    return this.http.post('accessnfs/unmountnfs', params);
  }

}
export class Host {
  hostId: string;
  hostName: string;
}
// 集群列表
export class Cluster {
  clusterId: string;
  clusterName: string;
}
