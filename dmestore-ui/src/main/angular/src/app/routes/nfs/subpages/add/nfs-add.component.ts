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
  providers: [GlobalsService,NfsAddService,StorageService]
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

    const ctx = this.gs.getClientSdk().app.getContextObjects();
    //入口是DataSource
    this.viewPage='add_plugin'
    this.activatedRoute.queryParams.subscribe(queryParam => {
      this.pluginFlag =queryParam.flag;
    });
    if(this.pluginFlag==null){
      //入口来至Vcenter
      //this.dsObjectId=ctx[0].id;
      this.viewPage='add_vcenter'
    }

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
    this.storageService.getData().subscribe((s: any) => {
      if (s.code === '200'){
        this.storageList = s.data.data;
      }
    });
    // 添加页面默认打开首页
   // this.jumpTo2(this.addPageOne, this.wizard);
  }
  jumpTo2(page: ClrWizardPage, wizard: ClrWizard) {
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
    console.log('提交参数：')
    console.log(this.addForm);
    this.addService.addNfs(this.addForm).subscribe((result: any) => {
      if (result.code === '200'){
        this.backToNfsList();
      }else{
        this.errorMsg = '添加失败！'+result.description;
      }
    });
  }
  selectStoragePool(){
    this.storagePools = null;
    this.logicPorts = null;
    // 选择存储后获取存储池
    this.storageService.getStoragePoolListByStorageId(this.addForm.storagId)
      .subscribe((r: any) => {
        if (r.code === '200'){
          this.storagePools = r.data.data;
        }
      });
    this.selectLogicPort();
  }
  selectLogicPort(){
    // 选择存储后逻辑端口
    this.storageService.getLogicPortListByStorageId(this.addForm.storagId)
      .subscribe((r: any) => {
        if (r.code === '200'){
          this.logicPorts = r.data.data;
        }
      });
  }
  checkHost(){
    this.addForm.vkernelIp=null;
    //选择主机后获取虚拟网卡
    this.addService.getVmkernelListByObjectId(this.addForm.hostObjectId)
      .subscribe((r: any) => {
        if (r.code === '200'){
          this.vmkernelList = r.data;
        }
      });
    this.selectLogicPort();
  }
  backToNfsList(){
    this.router.navigate(['nfs']);
  }

  closeModel(){
    this.gs.getClientSdk().modal.close();
  }
}
