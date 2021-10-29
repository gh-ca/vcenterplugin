import {ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {NfsExpandService} from "./nfs-expand.service";
import {ActivatedRoute, Router} from "@angular/router";
import {UpdateNfs} from "../../nfs.service";
@Component({
  selector: 'app-reduce',
  templateUrl: './nfs-expand.component.html',
  styleUrls: ['./nfs-expand.component.scss'],
  providers: [NfsExpandService]
})
export class NfsExpandComponent implements OnInit{
  newCapacity = 1;
  viewPage: string;
  pluginFlag: string;//来至插件的标记
  unit='GB';
  rowSelected = []; // 当前选中数据
  fsId:string;
  errorMsg: string;
  storeObjectId:string; //当入口为vcenter的时候需要获取此值
  modalLoading = false; // 数据加载loading
  modalHandleLoading = false; // 数据处理loading
  expandSuccessShow = false; // 扩容成功提示
  capacityErr= true;
  updateNfs=new UpdateNfs();
  constructor(private expandService: NfsExpandService, private gs: GlobalsService,
              private activatedRoute: ActivatedRoute,private router:Router, private cdr: ChangeDetectorRef){
  }
  ngOnInit(): void {
      //入口是DataSource
      this.viewPage='expand_plugin'
      this.activatedRoute.queryParams.subscribe(queryParam => {
        this.fsId = queryParam.fsId;
        this.pluginFlag =queryParam.flag;
        this.storeObjectId =queryParam.objectId;
      });
      if(this.pluginFlag==null){
        //入口来至Vcenter
        const ctx = this.gs.getClientSdk().app.getContextObjects();
        this.storeObjectId=ctx[0].id;
        // this.storeObjectId="urn:vmomi:Datastore:datastore-4072:674908e5-ab21-4079-9cb1-596358ee5dd1";
        this.viewPage='expand_vcenter';
        this.modalLoading = true;
        this.expandService.getStorageById(this.storeObjectId).subscribe((result: any) => {
          this.modalLoading = false;
          if (result.code === '200'){
            this.fsId = result.data.fsId;
            // console.log("this.fsId", this.fsId);
            // console.log("result.data", result.data);
          }
          this.cdr.detectChanges();
        });
      }

    this.modalLoading = true;
    this.expandService.getNfsDetailById(this.storeObjectId).subscribe((result: any) => {
      this.modalLoading = false;
      if (result.code === '200'){
        this.updateNfs = result.data;
        // const capacity =  this.updateNfs.capacity;
        // if (this.updateNfs.thin) {
        //   if (capacity) {
        //     if (capacity > 1) {
        //       this.minCapacity = 1;
        //     } else {
        //       this.minCapacity = capacity;
        //     }
        //   }
        // } else {
        //   if (capacity) {
        //     if (capacity > 3) {
        //       this.minCapacity = 3;
        //     } else {
        //       this.minCapacity = capacity;
        //     }
        //   }
        // }
      }
      this.cdr.detectChanges();
    });
    this.modalLoading=false
  }
  expandData(){
    let v=this.checkCapacity();
    if(!v) return;
    this.modalHandleLoading=true;
    let capacity;
    switch (this.unit) {
      case 'TB':
        capacity = this.newCapacity * 1024;
        break;
      case 'MB':
        capacity = this.newCapacity / 1024;
        break;
      case 'KB':
        capacity = this.newCapacity / (1024 * 1024);
        break;
      default: // 默认GB 不变
        capacity = this.newCapacity;
        break;
    }
    var params;
    if (this.pluginFlag=='plugin'){
      params={
        "fileSystemId": this.fsId,
        "storeObjectId": this.storeObjectId,
        "expand":true,
        "capacity": capacity
      }
    }
    if(this.pluginFlag==null){
      params={
        "storeObjectId": this.storeObjectId,
        "expand":true,
        "fileSystemId": this.fsId,
        "capacity": capacity
      }
    }
    this.expandService.changeCapacity(params).subscribe((result: any) => {
      this.modalHandleLoading=false;
      if (result.code === '200'){
        this.expandSuccessShow = true; // 扩容成功提示
      }else{
        this.errorMsg = '1';
        console.log('Expand failed:',result.description)
      }
      this.cdr.detectChanges();
    });
  }
  backToNfsList(){
    this.modalLoading=false;
    this.errorMsg=null;
    this.router.navigate(['nfs']);
  }
  closeModel(){
    this.modalLoading=false;
    this.errorMsg=null;
    this.gs.getClientSdk().modal.close();
  }

  /**
   * 确认操作结果并关闭窗口
   */
  confirmActResult() {
    if(this.pluginFlag=='plugin'){
      this.backToNfsList();
    }else{
      this.closeModel();
    }
  }
  checkCapacity(){
    let capacity;
    switch (this.unit) {
      case 'TB':
        capacity = this.newCapacity * 1024;
        break;
      default: // 默认GB 不变
        capacity = this.newCapacity;
        break;
    }
    if (capacity<0.001){
      this.newCapacity=1;
      this.capacityErr=false;
      return false;
    }
    if ((capacity+this.updateNfs.capacity)>16777216){
      this.newCapacity=1;
      this.capacityErr=false;
      return false;
    }else{
      this.capacityErr=true;
      return true;
    }

  }
}
