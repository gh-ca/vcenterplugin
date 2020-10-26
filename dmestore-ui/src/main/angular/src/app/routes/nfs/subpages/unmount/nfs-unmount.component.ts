import {Component, OnInit} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {Cluster, Host, NfsUnmountService} from "./nfs-unmount.service";
import {ActivatedRoute, Router} from "@angular/router";
import {DataStore} from "../mount/nfs-mount.service";
@Component({
  selector: 'app-unmount',
  templateUrl: './nfs-unmount.component.html',
  styleUrls: ['./nfs-unmount.component.scss'],
  providers: [NfsUnmountService]
})
export class NfsUnmountComponent implements OnInit{
  routePath: string;
  viewPage: string;
  dsObjectId: string;
  pluginFlag: string;//来至插件的标记
  rowSelected = []; // 当前选中数据
  dataStoreList: DataStore[];
  errorMsg: string;
  hostList: Host[];
  clusterList: Cluster[];
  mountType='1';
  checkHost:Host;
  checkCluster: Cluster;
  hostId:string;
  clusterId:string;
  constructor(private unmountService: NfsUnmountService, private gs: GlobalsService,
              private activatedRoute: ActivatedRoute,private router:Router){
  }
  ngOnInit(): void {
    this.gs.loading=true;

    this.activatedRoute.url.subscribe(url => {
      this.routePath=url[0].path;
    });

    if ("dataStore"===this.routePath){
      //入口是DataSource
      this.viewPage='unmount_plugin'
      this.activatedRoute.queryParams.subscribe(queryParam => {
        this.dsObjectId = queryParam.objectId;
        this.pluginFlag =queryParam.flag;
      });
      if(this.pluginFlag==null){
        //入口来至Vcenter
        const ctx = this.gs.getClientSdk().app.getContextObjects();
        if(ctx!=null){
          this.dsObjectId=ctx[0].id;
        }
        this.viewPage='unmount_datastore'
      }
      this.gs.loading=false;
    }else if("host"=== this.routePath){
      this.viewPage='unmount_host'
      const ctx = this.gs.getClientSdk().app.getContextObjects();
      if(ctx!=null){
        this.dsObjectId=ctx[0].id;
      }
      this.getMountedHostList();
      this.gs.loading=false;
    }else if( "cluster"=== this.routePath){
      this.viewPage='unmount_cluster'
      const ctx = this.gs.getClientSdk().app.getContextObjects();
      if(ctx!=null){
        this.dsObjectId=ctx[0].id;
        this.getMountedClusterList();
        this.gs.loading=false;
      }
    }
  }
  backToNfsList(){
    this.gs.loading=false;
    this.router.navigate(['nfs']);
  }
  unMount(){
    var params;
    if (this.mountType=='1'){
      if (this.checkHost==null ){
        this.errorMsg='请选择一条数据';
        return;
      }
      //卸载主机
      params={
        "dataStoreObjectId": this.dsObjectId,
        "hostId":this.checkHost.hostId
      };
    }
    if (this.mountType=='2'){
      //卸载集群
      if (this.checkCluster==null ){
        this.errorMsg='请选择一条数据';
        return;
      }
      params={
        "dataStoreObjectId": this.dsObjectId,
        "clusterId":this.checkCluster.clusterId
      };
    }
    this.gs.loading=true;
    this.unmountService.unmount(params).subscribe((result: any) => {
      this.gs.loading=false;
      if (result.code === '200'){
        if(this.pluginFlag=='plugin'){
          this.backToNfsList();
        }else{
          this.closeModel();
        }
      }else{
        this.errorMsg = '卸载失败！'+result.description;
      }
    });
  }
  closeModel(){
    this.gs.loading=false;
    this.gs.getClientSdk().modal.close();
  }
  formatCapacity(c: number){
    if (c < 1024){
      return c.toFixed(3)+" GB";
    }else if(c >= 1024 && c< 1048576){
      return (c/1024).toFixed(3) +" TB";
    }else if(c>= 1048576){
      return (c/1024/1024).toFixed(3)+" PB"
    }
  }
  getMountedHostList(){
    this.unmountService.getMountedHostList(this.hostId).subscribe((r: any) => {
      if (r.code=='200'){
        this.hostList=r.data;
      }
    });
  }
  getMountedClusterList(){
    this.unmountService.getMountedClusterList(this.clusterId).subscribe((r: any) => {
      if (r.code=='200'){
        this.clusterList=r.data;
      }
    });
  }
}
