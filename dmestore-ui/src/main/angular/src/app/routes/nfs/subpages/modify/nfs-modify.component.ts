import {ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NfsModify, NfsModifyService} from "./nfs-modify.service";

@Component({
  selector: 'app-add',
  templateUrl: './nfs-modify.component.html',
  styleUrls: ['./nfs-modify.component.scss'],
  providers: [NfsModifyService]
})
export class NfsModifyComponent implements OnInit{

  viewPage: string;
  pluginFlag: string;
  objectId: string;
  updateNfs: NfsModify;
  errorMsg: string;
  constructor(private modifyService: NfsModifyService, private cdr: ChangeDetectorRef,
              private gs: GlobalsService,
              private activatedRoute: ActivatedRoute,private router:Router){
  }
  ngOnInit(): void {
    this.viewPage='modify_plugin'
    this.activatedRoute.queryParams.subscribe(queryParam => {
      this.pluginFlag =queryParam.flag;
      this.objectId =queryParam.objectid;
    });
    if(this.pluginFlag==null){
      //入口来至Vcenter
      const ctx = this.gs.getClientSdk().app.getContextObjects();
      if(ctx!=null){
        this.objectId=ctx[0].id;
      }
      this.viewPage='modify_vcenter'
    }
    if(this.objectId!=null){
      this.gs.loading=true;
      this.modifyService.getNfsDetailById(this.objectId).subscribe((result: any) => {
        if (result.code === '200'){
          this.updateNfs=result.data;
          this.updateNfs.sameName=true;
          this.updateNfs.dataStoreObjectId=this.objectId;
          this.gs.loading=false;
        }
      });
    }
  }
  modifyCommit(){
    this.gs.loading=true;
    console.log(this.updateNfs);
    if (this.updateNfs.sameName){
      this.updateNfs.shareName=this.updateNfs.nfsName;
      this.updateNfs.fsName=this.updateNfs.nfsName;
    }
    this.modifyService.updateNfs(this.updateNfs).subscribe((result: any) => {
      this.gs.loading=false;
      if (result.code === '200'){
        if (this.pluginFlag=='plugin'){
          this.backToNfsList();
        }else{
          this.closeModel();
        }
      }else{
        this.errorMsg = '编辑失败！'+result.description;
        this.cdr.detectChanges();
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
