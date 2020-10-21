import {ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NfsModifyService, UpdateNfs} from "./nfs-modify.service";

@Component({
  selector: 'app-add',
  templateUrl: './nfs-modify.component.html',
  styleUrls: ['./nfs-modify.component.scss'],
  providers: [GlobalsService,NfsModifyService]
})
export class NfsModifyComponent implements OnInit{

  viewPage: string;
  pluginFlag: string;
  updateNfs: UpdateNfs = new UpdateNfs();
  constructor(private deleteService: NfsModifyService, private cdr: ChangeDetectorRef,
              private gs: GlobalsService,
              private activatedRoute: ActivatedRoute,private router:Router){
  }
  ngOnInit(): void {
    this.viewPage='modify_plugin'
    this.activatedRoute.queryParams.subscribe(queryParam => {
      this.pluginFlag =queryParam.flag;
    });
    if(this.pluginFlag==null){
      //入口来至Vcenter
      this.viewPage='modify_vcenter'
    }
    const ctx = this.gs.getClientSdk().app.getContextObjects();

  }
  modifyCommit(){

  }
  backToNfsList(){
    this.router.navigate(['nfs']);
  }
  closeModel(){
    this.gs.getClientSdk().modal.close();
  }
}
