import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit, ViewChild} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AddNfs, Host, NfsAddService, Vmkernel} from "./nfs-add.service";
import {ClrWizard, ClrWizardPage} from "@clr/angular";
import {LogicPort, StorageList, StorageService} from "../../../storage/storage.service";
import {StoragePool} from "../../../storage/detail/detail.service";
@Component({
  selector: 'app-add',
  templateUrl: './nfs-add.component.html',
  styleUrls: ['./nfs-add.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [NfsAddService,StorageService]
})
export class NfsAddComponent implements OnInit{
  viewPage: string;
  pluginFlag: string;
  addForm = new AddNfs();
  errorMsg: string;
  unit='GB';
  checkedPool:any;
  storageList: StorageList[] = [];
  storagePools: StoragePool[] = [];
  logicPorts: LogicPort[] = [];
  hostList: Host[] = [];
  vmkernelList: Vmkernel[]=[];
  // 添加页面窗口
  @ViewChild('wizard') wizard: ClrWizard;
  @ViewChild('addPageOne') addPageOne: ClrWizardPage;
  @ViewChild('addPageTwo') addPageTwo: ClrWizardPage;

  constructor(private addService: NfsAddService, private cdr: ChangeDetectorRef,
              private gs: GlobalsService,private storageService: StorageService,
              private activatedRoute: ActivatedRoute,private router:Router){
  }

  ngOnInit(): void {
    this.viewPage='add_plugin'
    this.activatedRoute.queryParams.subscribe(queryParam => {
      this.pluginFlag =queryParam.flag;
    });
    if(this.pluginFlag==null){
      //入口来至Vcenter
      this.viewPage='delete_vcenter'
    }
    //入口是DataSource
    this.viewPage='add_plugin'
    this.activatedRoute.queryParams.subscribe(queryParam => {
      this.pluginFlag =queryParam.flag;
    });
    if(this.pluginFlag==null){
      //入口来至Vcenter
      this.viewPage='add_vcenter'
    }
    this.gs.loading=true;
    this.storageService.getData().subscribe((s: any) => {
      if (s.code === '200'){
        this.storageList = s.data;
        this.gs.loading=false;
      }
    });
    this.addService.getHostList().subscribe((r: any) => {
      if (r.code === '200'){
        this.hostList = r.data;
        this.cdr.detectChanges();
      }
    });
    this.addForm = new AddNfs();
    this.checkedPool= null;
    this.errorMsg='';
    // 获取存储列表
    this.cdr.detectChanges();
  }
  jumpTo(page: ClrWizardPage, wizard: ClrWizard) {
    console.log('wizard:')
    console.log(this.wizard)
    if (page && page.completed) {
      wizard.navService.currentPage = page;
    } else {
      wizard.navService.setLastEnabledPageCurrent();
    }
    this.wizard.open();
  }
  addNfs(){
    this.wizard.open();
    this.gs.loading=true;
    this.addForm.poolRawId=this.checkedPool.diskPoolId;
    this.addForm.storagePoolId= this.checkedPool.id;
    // 单位换算
    switch (this.unit) {
      case 'TB':
        this.addForm.size = this.addForm.size * 1024;
        break;
      case 'MB':
        this.addForm.size = this.addForm.size / 1024;
        break;
      case 'KB':
        this.addForm.size = this.addForm.size / (1024 * 1024);
        break;
      default: // 默认GB 不变
        break;
    }
    this.addService.addNfs(this.addForm).subscribe((result: any) => {
      this.gs.loading=false;
      if (result.code === '200'){
        if (this.pluginFlag=='plugin'){
          this.backToNfsList();
        }else{
          this.closeModel();
        }
      }else{
        this.errorMsg = '添加失败！'+result.description;
        this.cdr.detectChanges();
      }
    });
  }
  selectStoragePool(){
    this.gs.loading=true;
    this.storagePools = null;
    this.logicPorts = null;
    // 选择存储后获取存储池
    this.storageService.getStoragePoolListByStorageId(this.addForm.storagId)
      .subscribe((r: any) => {
        if (r.code === '200'){
          this.storagePools = r.data;
          this.cdr.detectChanges();
        }
      });
    this.selectLogicPort();
  }
  selectLogicPort(){
    // 选择存储后逻辑端口
    this.storageService.getLogicPortListByStorageId(this.addForm.storagId)
      .subscribe((r: any) => {
        this.gs.loading=false;
        if (r.code === '200'){
          this.logicPorts = r.data;
          this.cdr.detectChanges();
        }
      });
  }
  checkHost(){
    this.gs.loading=true;
    this.addForm.vkernelIp=null;
    //选择主机后获取虚拟网卡
    this.addService.getVmkernelListByObjectId(this.addForm.hostObjectId)
      .subscribe((r: any) => {
        this.gs.loading=false;
        if (r.code === '200'){
          this.vmkernelList = r.data;
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

