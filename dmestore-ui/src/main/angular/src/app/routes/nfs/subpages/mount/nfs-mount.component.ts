import {ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {DataStore, Mount, NfsMountService, Vmkernel} from "./nfs-mount.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Cluster, Host} from "../../nfs.service";

@Component({
  selector: 'app-mount',
  templateUrl: './nfs-mount.component.html',
  styleUrls: ['./nfs-mount.component.scss'],
  providers: [NfsMountService]
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
  vmkernelList: Vmkernel[]=[];
  constructor(private mountService: NfsMountService, private cdr: ChangeDetectorRef,
              private gs: GlobalsService,
              private activatedRoute: ActivatedRoute,private router:Router){
  }
  ngOnInit(): void {
    this.gs.loading=true;
    this.activatedRoute.url.subscribe(url => {
      this.routePath=url[0].path;
    });
    if ("dataStore"===this.routePath){
      //入口是DataSource
      this.viewPage='mount_plugin'
      this.activatedRoute.queryParams.subscribe(queryParam => {
        this.dsObjectId = queryParam.objectId;
        this.mountForm.dataStoreObjectId=queryParam.objectId;
        this.mountForm.dataStoreName=queryParam.dsName;
        this.pluginFlag =queryParam.flag;
      });
      if(this.pluginFlag==null){
        //入口来至Vcenter
        const ctx = this.gs.getClientSdk().app.getContextObjects();
        if(ctx!=null){
          this.dsObjectId=ctx[0].id;
          this.mountForm.dataStoreObjectId=ctx[0].id;
        }
        this.dsObjectId='urn:vmomi:Datastore:datastore-1128:674908e5-ab21-4079-9cb1-596358ee5dd1';
        this.viewPage='mount_dataStore'
      }
      if (this.dsObjectId!=null && this.dsObjectId!=''){
        this.initHostAndClusterList();
      }
      this.gs.loading=false;
    }else{
      //入口是主机或者集群
      if ('host'===this.routePath){
        this.viewPage='mount_host';
        const ctx = this.gs.getClientSdk().app.getContextObjects();
        if(ctx!=null){
          this.hostObjectId=ctx[0].id;
          this.getDataStoreList('host');
        }
        this.gs.loading=false;
      }else if('cluster'===this.routePath){
        this.viewPage='mount_cluster'
        const ctx = this.gs.getClientSdk().app.getContextObjects();
        if(ctx!=null){
          this.clusterObjectId=ctx[0].id;
          //入口是集群
          this.getDataStoreList('cluster');
        }
        this.gs.loading=false;
      }
      this.gs.loading=false;
    }
    this.cdr.detectChanges();
  }
  checkHost(){
    this.gs.loading=true;
    //选择主机后获取虚拟网卡
    this.mountService.getVmkernelListByObjectId(this.mountForm.dataStoreObjectId)
      .subscribe((r: any) => {
        this.gs.loading=false;
        if (r.code === '200'){
          this.vmkernelList = r.data;
          this.cdr.detectChanges();
        }
      });
  }
  initHostAndClusterList(){
    this.mountService.getHostListByObjectId(this.dsObjectId).subscribe((r: any) => {
      if (r.code === '200'){
        this.hostList = r.data;
        this.cdr.detectChanges();
        this.gs.loading=false;
      }
    });
    // this.mountService.getClusterListByObjectId(this.dsObjectId).subscribe((r: any) => {
    //   if (r.code === '200'){
    //     this.clusterList = r.data;
    //     this.cdr.detectChanges();
    //     this.gs.loading=false;
    //   }
    // });
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
  backToNfsList(){
    this.gs.loading=false;
    this.router.navigate(['nfs']);
  }
  closeModel(){
    this.gs.getClientSdk().modal.close();
    this.gs.loading=false;
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
