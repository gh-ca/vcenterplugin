import {Component, OnInit} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {NfsReduceService} from "./nfs-reduce.service";
import {ActivatedRoute, Router} from "@angular/router";
import {DataStore} from "../mount/nfs-mount.service";
@Component({
  selector: 'app-reduce',
  templateUrl: './nfs-reduce.component.html',
  styleUrls: ['./nfs-reduce.component.scss'],
  providers: [GlobalsService,NfsReduceService]
})
export class NfsReduceComponent implements OnInit{

  routePath: string;
  viewPage: string;
  dsObjectId: string;
  pluginFlag: string;//来至插件的标记
  rowSelected = []; // 当前选中数据
  dataStoreList: DataStore[];
  errorMsg: string;
  constructor(private reduceService: NfsReduceService, private gs: GlobalsService,
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
    }



  }

  backToNfsList(){
    this.router.navigate(['nfs']);
  }
  unMount(){

  }
  hostClusterUnmount(){

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
