import {ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NfsModify, NfsModifyService} from "./nfs-modify.service";
import {ModifyNfs, UpdateNfs} from "../../nfs.service";

@Component({
  selector: 'app-add',
  templateUrl: './nfs-modify.component.html',
  styleUrls: ['./nfs-modify.component.scss'],
  providers: [NfsModifyService]
})
export class NfsModifyComponent implements OnInit{
  modalLoading = false; // 数据加载loading
  modalHandleLoading = false; // 数据处理loading
  viewPage: string;
  pluginFlag: string;
  objectId: string;
  updateNfs=new UpdateNfs();
  errorMsg: string;
  constructor(private modifyService: NfsModifyService, private cdr: ChangeDetectorRef,
              private gs: GlobalsService,
              private activatedRoute: ActivatedRoute,private router:Router){
  }
  ngOnInit(): void {
    this.modalLoading=true;
    this.activatedRoute.queryParams.subscribe(queryParam => {
      this.pluginFlag =queryParam.flag;
      this.objectId =queryParam.objectid;
    });
    if(this.pluginFlag==null){
      //入口来至Vcenter
      const ctx = this.gs.getClientSdk().app.getContextObjects();
      this.objectId=ctx[0].id;
    }
    this.modifyService.getNfsDetailById(this.objectId).subscribe((result: any) => {
      if (this.pluginFlag){
        this.viewPage='modify_plugin'
      }else{
        this.viewPage='modify_vcenter'
      }
      if (result.code === '200'){
        this.updateNfs=result.data;
        this.updateNfs.sameName=true;
        this.updateNfs.dataStoreObjectId=this.objectId;
        this.modalLoading=false;
      }
    });
  }
  modifyCommit(){
    this.modalHandleLoading=true;
    console.log(this.updateNfs);
    if (this.updateNfs.sameName){
      this.updateNfs.shareName=this.updateNfs.nfsName;
      this.updateNfs.fsName=this.updateNfs.nfsName;
    }
    this.modifyService.updateNfs(this.updateNfs).subscribe((result: any) => {
      this.modalHandleLoading=false;
      if (result.code === '200'){
        if (this.pluginFlag=='plugin'){
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
    this.modalLoading=false;
    this.router.navigate(['nfs']);
  }
  closeModel(){
    this.modalLoading=false;
    this.gs.getClientSdk().modal.close();
  }
}
