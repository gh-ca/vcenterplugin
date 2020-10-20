import {ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NfsDeleteService} from "./nfs-delete.service";

@Component({
  selector: 'app-add',
  templateUrl: './nfs-delete.component.html',
  styleUrls: ['./nfs-delete.component.scss'],
  providers: [GlobalsService,NfsDeleteService]
})
export class NfsDeleteComponent implements OnInit{
  viewPage: string;
  pluginFlag: string;//来至插件的标记
  dataStoreObjectId:string;
  errorMsg: string;
  constructor(private deleteService: NfsDeleteService, private cdr: ChangeDetectorRef,
              private gs: GlobalsService,
              private activatedRoute: ActivatedRoute,private router:Router){
  }
  ngOnInit(): void {
    const ctx = this.gs.getClientSdk().app.getContextObjects();
    //入口是DataSource
    this.viewPage='add_plugin'
    this.activatedRoute.queryParams.subscribe(queryParam => {
      this.dataStoreObjectId = queryParam.objectid;
      this.pluginFlag =queryParam.flag;
    });
    if(this.pluginFlag==null){
      //入口来至Vcenter
      //this.dsObjectId=ctx[0].id;
      this.viewPage='delete_vcenter'
    }
  }
  delNfs(){
    this.deleteService.delNfs(this.dataStoreObjectId).subscribe((result: any) => {
      if (result.code === '200'){
        if(this.pluginFlag=='plugin'){
          this.backToNfsList();
        }else{
          this.closeModel();
        }
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
