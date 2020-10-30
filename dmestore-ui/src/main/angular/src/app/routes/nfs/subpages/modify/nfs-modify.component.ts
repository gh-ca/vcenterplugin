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

  maxbandwidthChoose=false; // 最大带宽 选中
  maxiopsChoose=false; // 最大iops 选中
  minbandwidthChoose=false; // 最小带宽 选中
  miniopsChoose=false; // 最小iops 选中
  latencyChoose=false; // 时延 选中

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
  qosBlur(type:String, operationType:string) {

    let objVal;
    if (type === 'add') {
      switch (operationType) {
        case 'maxbandwidth':
          objVal = this.updateNfs.maxBandwidth;
          break;
        case 'maxiops':
          objVal = this.updateNfs.maxIops;
          break;
        case 'minbandwidth':
          objVal = this.updateNfs.minBandwidth;
          break;
        case 'miniops':
          objVal = this.updateNfs.minIops;
          break;
        default:
          objVal = this.updateNfs.latency;
          break;
      }
    }
    if (objVal && objVal !== '') {
      if (objVal.toString().match(/\d+(\.\d{0,2})?/)) {
        objVal = objVal.toString().match(/\d+(\.\d{0,2})?/)[0];
      } else {
        objVal = '';
      }
    }
    if (type === 'add') {
      switch (operationType) {
        case 'maxbandwidth':
          this.updateNfs.maxBandwidth = objVal;
          break;
        case 'maxiops':
          this.updateNfs.maxIops = objVal;
          break;
        case 'minbandwidth':
          this.updateNfs.minBandwidth = objVal;
          break;
        case 'miniops':
          this.updateNfs.minIops = objVal;
          break;
        default:
          this.updateNfs.latency = objVal;
          break;
      }
    }
  }
}
