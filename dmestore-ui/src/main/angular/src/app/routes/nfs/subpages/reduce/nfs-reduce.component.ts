import {Component, OnInit} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {NfsReduceService} from "./nfs-reduce.service";
import {ActivatedRoute, Router} from "@angular/router";
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
  newCapacity = 0;
  pluginFlag: string;//来至插件的标记
  rowSelected = []; // 当前选中数据
  errorMsg: string;
  fsId:string;
  constructor(private reduceService: NfsReduceService, private gs: GlobalsService,
              private activatedRoute: ActivatedRoute,private router:Router){
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
      this.viewPage='reduce_vcenter'
    }
  }
  backToNfsList(){
    this.gs.loading=false;
    this.errorMsg=null;
    this.router.navigate(['nfs']);
  }
  closeModel(){
    this.errorMsg=null;
    this.gs.loading=false;
    this.gs.getClientSdk().modal.close();
  }
  reduceCommit(){
    this.gs.loading=true;
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
        "expand":false,
        "capacity": this.newCapacity
      }
    }
    if(this.pluginFlag==null){
      params={
        "storeObjectId": this.storeObjectId,
        "expand":false,
        "capacity": this.newCapacity
      }
    }
    this.reduceService.changeCapacity(params).subscribe((result: any) => {
      this.gs.loading=false;
      if (result.code === '200'){
        if(this.pluginFlag=='plugin'){
          this.backToNfsList();
        }else{
          this.closeModel();
        }
      }else{
        this.errorMsg = '1';
        console.log("Reduce failed:",result.description);
      }
    });
  }
}
