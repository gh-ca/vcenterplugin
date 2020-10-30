import {Component, OnInit} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {NfsExpandService} from "./nfs-expand.service";
import {ActivatedRoute, Router} from "@angular/router";
@Component({
  selector: 'app-reduce',
  templateUrl: './nfs-expand.component.html',
  styleUrls: ['./nfs-expand.component.scss'],
  providers: [NfsExpandService]
})
export class NfsExpandComponent implements OnInit{
  newCapacity = 0;
  viewPage: string;
  pluginFlag: string;//来至插件的标记
  unit='GB';
  rowSelected = []; // 当前选中数据
  fsId:string;
  errorMsg: string;
  storeObjectId:string; //当入口为vcenter的时候需要获取此值
  modalLoading = false; // 数据加载loading
  modalHandleLoading = false; // 数据处理loading
  constructor(private expandService: NfsExpandService, private gs: GlobalsService,
              private activatedRoute: ActivatedRoute,private router:Router){
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
        this.viewPage='expand_vcenter'
      }
  }
  expandData(){
    this.modalHandleLoading=true;
    switch (this.unit) {
      case 'TB':
        this.newCapacity = this.newCapacity * 1024;
        break;
      case 'MB':
        this.newCapacity = this.newCapacity / 1024;
        break;
      case 'KB':
        this.newCapacity = this.newCapacity / (1024 * 1024);
        break;
      default: // 默认GB 不变
        break;
    }
    var params;
    if (this.pluginFlag=='plugin'){
      params={
        "fileSystemId": this.fsId,
        "storeObjectId": this.storeObjectId,
        "expand":true,
        "capacity": this.newCapacity
      }
    }
    if(this.pluginFlag==null){
      params={
        "storeObjectId": this.storeObjectId,
        "expand":true,
        "capacity": this.newCapacity
      }
    }
    this.expandService.changeCapacity(params).subscribe((result: any) => {
      this.modalHandleLoading=false;
      if (result.code === '200'){
        if(this.pluginFlag=='plugin'){
          this.backToNfsList();
        }else{
          this.closeModel();
        }
      }else{
        this.errorMsg = '1';
        console.log('Expand failed:',result.description)
      }
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
}
