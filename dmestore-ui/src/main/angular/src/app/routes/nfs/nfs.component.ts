import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {AddNfs, Cluster, FileSystem, Host, List, ModifyNfs, Mount, NfsService, Share, ShareClient} from './nfs.service';
import {GlobalsService} from '../../shared/globals.service';
import {LogicPort, StorageList, StorageService} from '../storage/storage.service';
import {StoragePool} from "../storage/detail/detail.service";
import {ClrWizard, ClrWizardPage} from "@clr/angular";
@Component({
  selector: 'app-nfs',
  templateUrl: './nfs.component.html',
  styleUrls: ['./nfs.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [NfsService, GlobalsService, StorageService],
})
export class NfsComponent implements OnInit {
  list: List[] = []; // 数据列表
  radioCheck = 'list'; // 切换列表页显示
  total = 0; // 总数据数量
  isLoading = true; // table数据loading
  rowSelected = []; // 当前选中数据
  oldCapacity = 0;
  // ==========弹窗参数=======
  modifyShow = false;
  popShow = false; // 弹出层显示
  expand = false; // 扩容弹出框
  reduceOpen = false; // 缩容
  mountShow = false; // 挂载
  delShow = false; // 删除提示
  unmountShow = false; // 卸载提示
  mountObj = '1';
  fsIds = [];
  addForm = new AddNfs();
  selectHost = [];
  hostLoading = true;
  currentData = null;
  hostList: Host[] = [];
  clusterList: Cluster[] = [];
  mountForm = new Mount();
  storageList: StorageList[] = [];
  storagePools: StoragePool[] = [];
  logicPorts: LogicPort[] = [];

  // 添加页面窗口
  @ViewChild('wizard') wizard: ClrWizard;
  @ViewChild('addPageOne') addPageOne: ClrWizardPage;
  @ViewChild('addPageTwo') addPageTwo: ClrWizardPage;

  errMessage = '';
  constructor(private remoteSrv: NfsService, private cdr: ChangeDetectorRef, public gs: GlobalsService , private storageService: StorageService) { }
  ngOnInit(): void {
  }
  changeData() {
    this.rowSelected[0] = this.currentData;
    this.modifyShow = false;
  }

  // 获取nfs列表
  getNfsList() {
    this.isLoading = true;
    // 进行数据加载
    this.remoteSrv.getData()
      .subscribe((result: any) => {
        this.list = result.data;
        console.log(result);
        this.isLoading = false;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        // 获取性能列表
        this.listnfsperformance();
      });
  }
// 性能视图列表
  listnfsperformance(){
    if (this.list === null || this.list.length <= 0){ return; }
    this.list.forEach(item => {
      this.fsIds.push(item.fsId);
    });
    console.log('fsIds');
    console.log(this.fsIds);
    this.remoteSrv.getChartData(this.fsIds).subscribe((result: any) => {
      if (result.code === '200'){
        const chartList: List [] = result.data;
        if ( chartList !== null && chartList.length > 0){
         this.list.forEach(item => {
           chartList.forEach(charItem => {
             if (item.fsId === charItem.fsId){
               item.ops = charItem.ops;
               item.bandwidth = charItem.bandwidth;
               item.readResponseTime = charItem.readResponseTime;
               item.writeResponseTime = charItem.writeResponseTime;
             }
           });
         });
         this.cdr.detectChanges();
        }
      }
    });
  }

  addView(){
    this.addForm = new AddNfs();
    // 添加页面默认打开首页
    this.jumpTo(this.addPageOne, this.wizard);
    this.errMessage='';
    // 获取存储列表
    this.storageService.getData().subscribe((s: any) => {
       if (s.code === '200'){
        this.storageList = s.data.data;
        }
    });
    this.remoteSrv.getHostList().subscribe((r: any) => {
      if (r.code === '200'){
        this.hostList = r.data;
        this.hostLoading = false;
        this.cdr.detectChanges();
      }
    });
    this.popShow = true;
  }
  // 页面跳转
  jumpTo(page: ClrWizardPage, wizard: ClrWizard) {
    if (page && page.completed) {
      wizard.navService.currentPage = page;
    } else {
      wizard.navService.setLastEnabledPageCurrent();
    }
    this.wizard.open();
  }
  // 添加提交方法
  addNfs(){
    // 单位换算
    switch (this.addForm.unit) {
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
    // 构建share 和fs对象
    if (this.addForm.sameName){
     const share = new Share();
     share.name = this.addForm.nfsName;
     share.share_path = this.addForm.nfsName;
     this.addForm.create_nfs_share_param = share;
     const fs = new FileSystem();
     fs.name = this.addForm.nfsName;
     fs.capacity = this.addForm.size;
     fs.count = 1;
     this.addForm.filesystem_specs.push(fs);
    }else {
      const share = new Share();
      share.name = this.addForm.shareName;
      share.share_path = this.addForm.shareName;
      this.addForm.create_nfs_share_param = share;
      const fs = new FileSystem();
      fs.name = this.addForm.fsName;
      fs.capacity = this.addForm.size;
      fs.count = 1;
      this.addForm.filesystem_specs.push(fs);
    }
    // nfs_share_client_addition 构建挂载的主机列表
    if (this.selectHost !== null && this.selectHost.length > 0){
      this.selectHost.forEach(h => {
        const cli = new ShareClient();
        cli.name = h.hostName;
        cli.accessval = this.addForm.accessMode;
        this.addForm.nfs_share_client_addition.push(cli);
      });
    }
    // thin属性构造
    if (this.addForm.thin){
      this.addForm.tuning.allocation_type = 'thin';
    }else{
      this.addForm.tuning.allocation_type = 'thick';
    }
    // 构建exportPath路径
    this.addForm.exportPath = this.addForm.nfsName;
    this.addForm.pool_raw_id = this.addForm.storage_pool_id;
    this.remoteSrv.addNfs(this.addForm).subscribe((result: any) => {
      if (result.code === '200'){
        this.popShow = false;
      }else{
        this.popShow = true;
        this.errMessage = '添加失败！'+result.description;
      }
     });
  }
  selectStoragePool(){
    this.storagePools = null;
    this.logicPorts = null;
    // 选择存储后获取存储池
    this.storageService.getStoragePoolListByStorageId(this.addForm.storage_id)
      .subscribe((r: any) => {
        if (r.code === '200'){
          this.storagePools = r.data.data;
        }
      });
    this.selectLogicPort();
  }
  selectLogicPort(){
    // 选择存储后逻辑端口
    this.storageService.getLogicPortListByStorageId(this.addForm.storage_id)
      .subscribe((r: any) => {
        if (r.code === '200'){
        this.logicPorts = r.data.data;
      }
      });
  }
  modifyData() {

    this.currentData = new ModifyNfs();
    this.currentData.dataStoreObjectId = this.rowSelected[0].objectId;
    this.currentData.nfsShareName = this.rowSelected[0].share;
    this.currentData.nfsName = this.rowSelected[0].name;
    this.currentData.file_system_id = this.rowSelected[0].fsId;
    this.currentData.capacity_autonegotiation.auto_size_enable = false;
    this.currentData.tuning.deduplication_enabled = false;
    this.currentData.tuning.compression_enabled = false;
    this.currentData.tuning.allocation_type = 'thin';
    this.currentData.nfs_share_id = this.rowSelected[0].shareId;
    console.log('modify');
    console.log(this.currentData);
    this.modifyShow = true;
  }
  expandData(){
    console.log('扩容提交.....');
    // 弹窗关闭
    this.expand = false;
  }
  // 弹出缩容页面
  reduceView(){
    console.log(this.rowSelected[0]);
    this.oldCapacity = this.rowSelected[0].capacity;
    this.reduceOpen = true;
  }
  // 缩容提交
  reduceCommit(){

  }
  // 挂载
  mount(){
    this.mountForm = new Mount();
    this.mountForm.dataStoreName = this.rowSelected[0].name;
    this.mountForm.dataStoreObjectId = this.rowSelected[0].objectid;
    this.remoteSrv.getHostListByObjectId(this.rowSelected[0].objectid).subscribe((r: any) => {
      if (r.code === '200'){
        this.hostList = r.data;
        this.cdr.detectChanges();
      }
    });
    this.remoteSrv.getClusterListByObjectId(this.rowSelected[0].objectid).subscribe((r: any) => {
      if (r.code === '200'){
        this.clusterList = r.data;
        this.cdr.detectChanges();
      }
    });
    this.mountShow = true;
  }
  // 挂载提交
  mountSubmit(){
    this.remoteSrv.mountNfs(this.mountForm).subscribe((result: any) => {
      if (result.code  ===  '200'){
        this.mountShow = false;
        this.mountForm = new Mount();
      }
    });
  }
  // 卸载
  unmount(){
    const name = this.rowSelected[0].name;
    console.log(name);
    this.unmountShow = false;
  }
  // 删除NFS
  delNfs(){
    const name = this.rowSelected[0].name;
    console.log(name);
    this.remoteSrv.delNfs(name).subscribe((result: any) => {
      this.delShow = false;
    });
  }
  navigateTo(objectid){
    console.log('页面跳转了');
    console.log(objectid);
    this.gs.getClientSdk().app.navigateTo({
      targetViewId: 'vsphere.core.datastore.summary',
      objectId: objectid
    });
  }
}
