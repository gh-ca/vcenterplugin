import {Component, OnInit} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {NfsExpandService} from "./nfs-expand.service";
import {ActivatedRoute, Router} from "@angular/router";
import {DataStore} from "../mount/nfs-mount.service";
@Component({
  selector: 'app-reduce',
  templateUrl: './nfs-expand.component.html',
  styleUrls: ['./nfs-expand.component.scss'],
  providers: [GlobalsService,NfsExpandService]
})
export class NfsExpandComponent implements OnInit{
  newCapacity = 0;
  viewPage: string;
  pluginFlag: string;//来至插件的标记
  unit='GB';
  rowSelected = []; // 当前选中数据
  fsId:string;
  errorMsg: string;
  constructor(private expandService: NfsExpandService, private gs: GlobalsService,
              private activatedRoute: ActivatedRoute,private router:Router){
  }
  ngOnInit(): void {
      const ctx = this.gs.getClientSdk().app.getContextObjects();
      //入口是DataSource
      this.viewPage='expand_plugin'
      this.activatedRoute.queryParams.subscribe(queryParam => {
        this.fsId = queryParam.fsId;
        this.pluginFlag =queryParam.flag;
      });
      if(this.pluginFlag==null){
        //入口来至Vcenter
        //this.dsObjectId=ctx[0].id;
        this.viewPage='expand_vcenter'
      }
  }
  expandData(){
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
    var param={
      "fileSystemId": this.fsId,
      "expand":true,
      "capacity": this.newCapacity
    }
    this.expandService.changeCapacity(param).subscribe((result: any) => {
      if (result.code === '200'){
        this.backToNfsList();
      }else{
        this.errorMsg = '编辑失败！'+result.description;
      }
    });
  }
  backToNfsList(){
    this.router.navigate(['nfs']);
  }
  closeModel(){
    this.gs.getClientSdk().modal.close();
  }

}
