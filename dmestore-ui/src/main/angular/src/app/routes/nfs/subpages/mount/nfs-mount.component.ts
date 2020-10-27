import {ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {DataStore, Mount, NfsMountService, Vmkernel} from "./nfs-mount.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Host} from "../../nfs.service";
import {LogicPort} from "../../../storage/storage.service";

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
  dsObjectId: string;
  hostObjectId: string;
  routePath: string;
  rowSelected = []; // 当前选中数据
  dataStoreList: DataStore[];
  pluginFlag: string;//来至插件的标记
  errorMsg:string;
  logicPorts: LogicPort[] = [];
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
        this.pluginFlag =queryParam.flag;
      });
      if(this.pluginFlag==null){
        //入口来至Vcenter
        const ctx = this.gs.getClientSdk().app.getContextObjects();
        if(ctx!=null){
          this.dsObjectId=ctx[0].id;
          this.mountForm.dataStoreObjectId=ctx[0].id;
        }
        this.viewPage='mount_dataStore'
      }
      if (this.dsObjectId!=null && this.dsObjectId!=''){
        this.initHostAndClusterList();
        this.getLogicPortByDsId(this.dsObjectId)
      }
      this.gs.loading=false;
    }else{
      //入口是主机或者集群
      if ('host'===this.routePath){
        this.viewPage='mount_host';
        const ctx = this.gs.getClientSdk().app.getContextObjects();
        if(ctx!=null){
          this.hostObjectId=ctx[0].id;
          this.getDataStoreList(this.hostObjectId);
          this.getVmkernelListByObjectId(this.hostObjectId);
          this.mountForm.hostObjectId=this.hostObjectId;
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
    this.getVmkernelListByObjectId(this.mountForm.hostObjectId);
  }
  initHostAndClusterList(){
    this.mountService.getHostListByObjectId(this.dsObjectId).subscribe((r: any) => {
      if (r.code === '200'){
        this.hostList = r.data;
        this.cdr.detectChanges();
        this.gs.loading=false;
      }
    });
  }
  getDataStoreList(hostObjectId: string){
    this.dataStoreList=null;
      let params=
        {"hostObjectId":hostObjectId,
          "dataStoreType": "NFS"}
      this.mountService.getDatastoreListByHostObjectId(params).subscribe((r: any)=>{
        if (r.code==='200'){
          this.dataStoreList=r.data;
        }
      });

    this.cdr.detectChanges();
  }
  getLogicPortByDsId(storagId: string){
    // 选择存储后逻辑端口
    this.gs.loading=true;
    this.mountService.getLogicPortListByStorageId(storagId)
      .subscribe((r: any) => {
        this.gs.loading=false;
        if (r.code === '200'){
          this.logicPorts = r.data;
          this.cdr.detectChanges();
        }
      });
  }
  getVmkernelListByObjectId(hostObjectId:string){
    this.mountService.getVmkernelListByObjectId(hostObjectId)
      .subscribe((r: any) => {
        this.gs.loading=false;
        if (r.code === '200'){
          this.vmkernelList = r.data;
          this.cdr.detectChanges();
        }
      });
  }
  mountSubmit(){
    this.gs.loading=true;
    this.mountService.mountNfs(this.mountForm).subscribe((result: any) => {
      this.gs.loading=false;
      if (result.code  ===  '200'){
        this.mountForm = new Mount();
        if (this.pluginFlag=='plugin'){
          this.backToNfsList();
        }else {
         this.closeModel();
        }
      }else{
        console.log('mount failed:',result.description);
        this.errorMsg='1';
      }
    });
  }
  backToNfsList(){
    this.gs.loading=false;
    this.errorMsg=null;
    this.router.navigate(['nfs']);
  }
  closeModel(){
    this.errorMsg=null;
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
