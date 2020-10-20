import {ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {DataStore, NfsMountService} from "./nfs-mount.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Cluster, Host, Mount} from "../../nfs.service";

@Component({
  selector: 'app-mount',
  templateUrl: './nfs-mount.component.html',
  styleUrls: ['./nfs-mount.component.scss'],
  providers: [GlobalsService,NfsMountService]
})
export class NfsMountComponent implements OnInit{
  viewPage: string;
  mountObj = '1';
  objectId:string;
  mountForm = new Mount();
  hostList: Host[] = [];
  clusterList: Cluster[] = [];
  dsObjectId: string;
  hostObjectId: string;
  clusterObjectId: string;
  routePath: string;
  rowSelected = []; // 当前选中数据
  dataStoreList: DataStore[];
  pluginFlag: string;//来至插件的标记
  errorMsg:string;

  constructor(private mountService: NfsMountService, private cdr: ChangeDetectorRef,
              private gs: GlobalsService,
              private activatedRoute: ActivatedRoute,private router:Router){
  }
  ngOnInit(): void {
    this.activatedRoute.url.subscribe(url => {
      this.routePath=url[0].path;
    });
    const ctx = this.gs.getClientSdk().app.getContextObjects();
    if ("dataStore"===this.routePath){
      //入口是DataSource
      this.viewPage='dataStore_pugin'
      this.activatedRoute.queryParams.subscribe(queryParam => {
        this.dsObjectId = queryParam.objectId;
        this.pluginFlag =queryParam.flag;
      });
      if(this.pluginFlag==null){
        //入口来至Vcenter
        //this.dsObjectId=ctx[0].id;
        this.viewPage='dataStore_vcenter'
      }
      if (this.dsObjectId!=null && this.dsObjectId!=''){
        this.initHostAndClusterList();
      }
      console.log('viewPage');
      console.log(this.viewPage);
    }else{
      //入口是主机或者集群
      if ('host'===this.routePath){
        this.hostObjectId=ctx[0].id;
        //入口是主机
        if (this.hostObjectId!=null){
          this.getDataStoreList('host');
        }
      }else if('cluster'===this.routePath){
        //入口是集群
        this.getDataStoreList('cluster');
      }
    }
    this.cdr.detectChanges();
  }
  initHostAndClusterList(){
    this.mountService.getHostListByObjectId(this.dsObjectId).subscribe((r: any) => {
      if (r.code === '200'){
        this.hostList = r.data;
        this.cdr.detectChanges();
      }
    });
    this.mountService.getClusterListByObjectId(this.dsObjectId).subscribe((r: any) => {
      if (r.code === '200'){
        this.clusterList = r.data;
        this.cdr.detectChanges();
      }
    });
  }
  getDataStoreList(type: string){
    this.dataStoreList=null;
    if ('host' == type){
      let params=
        {"hostObjectId":this.hostObjectId,
          "dataStoreType": "NFS"}
      this.mountService.getDatastoreListByHostObjectId(params).subscribe((r: any)=>{
        if (r.code==='200'){
          this.dataStoreList=r.data;
        }
      });
    }
    if ('cluster'== type){
      let params= {
        "clusterObjectId":this.clusterObjectId,
          "dataStoreType": "NFS"
      };
      this.mountService.getDatastoreListByClusterObjectId(params).subscribe((r: any)=>{
        if (r.code==='200'){
          this.dataStoreList=r.data;
        }
      });
    }
    this.cdr.detectChanges();
  }
  mountSubmit(){
    this.mountService.mountNfs(this.mountForm).subscribe((result: any) => {
      if (result.code  ===  '200'){
        this.mountForm = new Mount();
        if (this.pluginFlag=='plugin'){
          this.backToNfsList();
        }else {
         this.closeModel();
        }
      }
    });
  }
  hostClusterMount(){
    if (this.rowSelected==null || this.rowSelected.length==0){
      this.errorMsg="请选择一条或者多条数据!";
      return;
    }





  }
  backToNfsList(){
    this.router.navigate(['nfs']);
  }
  closeModel(){
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
}
