import {Component, OnInit} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {NfsReduceService} from "./nfs-reduce.service";
import {ActivatedRoute, Router} from "@angular/router";
@Component({
  selector: 'app-reduce',
  templateUrl: './nfs-reduce.component.html',
  styleUrls: ['./nfs-reduce.component.scss'],
  providers: [GlobalsService,NfsReduceService]
})
export class NfsReduceComponent implements OnInit{

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
    const ctx = this.gs.getClientSdk().app.getContextObjects();
    //入口是DataSource
    this.viewPage='reduce_plugin'
    this.activatedRoute.queryParams.subscribe(queryParam => {
      this.fsId = queryParam.fsId;
      this.pluginFlag =queryParam.flag;
    });
    if(this.pluginFlag==null){
      //入口来至Vcenter
      //this.dsObjectId=ctx[0].id;
      this.viewPage='reduce_vcenter'
    }
  }

  backToNfsList(){
    this.router.navigate(['nfs']);
  }

  closeModel(){
    this.gs.getClientSdk().modal.close();
  }
  reduceCommit(){
// 弹窗关闭
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
    }//
    const fsId = this.rowSelected[0].fsId;
    var param={
      "fileSystemId": fsId,
      "expand":false,
      "capacity": this.newCapacity
    }
    this.reduceService.changeCapacity(param).subscribe((result: any) => {
      if (result.code === '200'){
        this.closeModel();
      }else{
        this.errorMsg = '编辑失败！'+result.description;
      }
    });
  }

}
