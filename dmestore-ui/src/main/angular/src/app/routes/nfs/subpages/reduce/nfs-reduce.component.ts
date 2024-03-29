import {ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {NfsReduceService} from "./nfs-reduce.service";
import {ActivatedRoute, Router} from "@angular/router";
import {UpdateNfs} from "../../nfs.service";
@Component({
  selector: 'app-reduce',
  templateUrl: './nfs-reduce.component.html',
  styleUrls: ['./nfs-reduce.component.scss'],
  providers: [NfsReduceService]
})
export class NfsReduceComponent implements OnInit{
  storeObjectId:string;
  viewPage: string;
  unit='GB';
  newCapacity =1;
  pluginFlag: string;//来至插件的标记
  rowSelected = []; // 当前选中数据
  errorMsg: string;
  modalLoading = false; // 数据加载loading
  modalHandleLoading = false; // 数据处理loading
  fsId:string;
  reduceSuccessShow = false;// 缩容成功窗口
  capacityErr= true;
  updateNfs=new UpdateNfs();

  minCapacity;
  maxOpCapacity;
  reduceErrorShow=false
  newCapacityError=false

  constructor(private reduceService: NfsReduceService, private gs: GlobalsService,
              private activatedRoute: ActivatedRoute,private router:Router, private cdr: ChangeDetectorRef){
  }
  ngOnInit(): void {
    //入口是DataSource
    this.viewPage='reduce_plugin'
    this.activatedRoute.queryParams.subscribe(queryParam => {
      this.fsId = queryParam.fsId;
      this.pluginFlag =queryParam.flag;
      this.storeObjectId =queryParam.objectId;
    });
    if(this.pluginFlag==null){
      //入口来至Vcenter
      const ctx = this.gs.getClientSdk().app.getContextObjects();
      if(ctx!=null){
        this.storeObjectId=ctx[0].id;
      }
      // this.storeObjectId="urn:vmomi:Datastore:datastore-4072:674908e5-ab21-4079-9cb1-596358ee5dd1";
      console.log("this.storeObjectId", this.storeObjectId);
      console.log("ctx!=null", ctx!=null);
      this.viewPage='reduce_vcenter'
      this.modalLoading = true;
      this.reduceService.getStorageById(this.storeObjectId).subscribe((result: any) => {
        this.modalLoading = false;
        if (result.code === '200'){
          this.fsId = result.data.fsId;
        }
        this.cdr.detectChanges();
      });
    }
    this.modalLoading = true;
    this.reduceService.getNfsDetailById(this.storeObjectId).subscribe((result: any) => {
      this.modalLoading = false;
      if (result.code === '200'){
        this.updateNfs = result.data;
        const capacity =  this.updateNfs.capacity;
        if (capacity>3){
          this.maxOpCapacity=(capacity-3).toFixed(3)
        }else {
          this.reduceErrorShow=true
        }
      }
      this.cdr.detectChanges();
    });
    this.modalLoading=false
  }
  backToNfsList(){
    this.modalLoading=false;
    this.errorMsg=null;
    this.router.navigate(['nfs']);
  }
  closeModel(){
    this.errorMsg=null;
    this.modalLoading=false;
    this.gs.getClientSdk().modal.close();
  }
  reduceCommit(){
    let v=this.checkNewCapacity();
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
        "expand":false,
        "capacity": capacity
      }
    }
    if(this.pluginFlag==null){
      params={
        "storeObjectId": this.storeObjectId,
        "expand":false,
        "fileSystemId": this.fsId,
        "capacity": capacity
      }
    }
    this.reduceService.changeCapacity(params).subscribe((result: any) => {
      this.modalHandleLoading=false;
      if (result.code === '200'){
        this.reduceSuccessShow = true;
      }else{
        this.errorMsg = '1';
        console.log("Reduce failed:",result.description);
      }
      this.cdr.detectChanges();
    });
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
  /* checkCapacity(){
     let capacity;
     switch (this.unit) {
       case 'TB':
         capacity = this.newCapacity * 1024;
         break;
       default: // 默认GB 不变
         capacity = this.newCapacity;
         break;
     }
     const lastCapacity = this.updateNfs.capacity - capacity;
     if (lastCapacity<parseFloat(this.maxOpCapacity)){
       this.newCapacity=0;
       this.capacityErr=false;
       return false;
     }else {
       this.capacityErr=true;
       return true;
     }
   }*/
  checkNewCapacity(){
    console.log('reduce',this.newCapacity)
    console.log(typeof this.newCapacity)
    let capacity
    switch (this.unit) {
      case 'TB':
        capacity = this.newCapacity * 1024;
        break;
      default: // 默认GB 不变
        capacity = this.newCapacity;
        break;
    }
    if (capacity>parseFloat(this.maxOpCapacity)||capacity<0.001){
      this.newCapacity=1
      this.newCapacityError=true
      return false
    }else {
      this.newCapacityError=false
      return true
    }
  }
}
