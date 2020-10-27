import {ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NfsDeleteService} from "./nfs-delete.service";

@Component({
  selector: 'app-add',
  templateUrl: './nfs-delete.component.html',
  styleUrls: ['./nfs-delete.component.scss'],
  providers: [NfsDeleteService]
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

    //入口是DataSource
    this.viewPage='delete_plugin'
    this.activatedRoute.queryParams.subscribe(queryParam => {
      this.dataStoreObjectId = queryParam.objectid;
      this.pluginFlag =queryParam.flag;
    });
    if(this.pluginFlag==null){
      //入口来至Vcenter
      const ctx = this.gs.getClientSdk().app.getContextObjects();
      if(ctx!=null){
        this.dataStoreObjectId=ctx[0].id;
      }
      this.viewPage='delete_vcenter'
    }
  }
  delNfs(){
    this.gs.loading=true;
    var params={
      "dataStoreObjectId": this.dataStoreObjectId
    };
    this.deleteService.delNfs(params).subscribe((result: any) => {
      this.gs.loading=false;
      if (result.code === '200'){
        if(this.pluginFlag=='plugin'){
          this.backToNfsList();
        }else{
          this.closeModel();
        }
      }else{
        this.errorMsg = '1';
        console.log("Delete failed:",result.description)
      }
    });
  }

  backToNfsList(){
    this.gs.loading=false;
    this.router.navigate(['nfs']);
  }

  closeModel(){
    this.gs.loading=false;
    this.gs.getClientSdk().modal.close();
  }
}
