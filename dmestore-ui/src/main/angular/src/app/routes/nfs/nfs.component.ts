import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {
  Host, List,NfsDetail, NfsService,UpdateNfs,

} from './nfs.service';
import {GlobalsService} from '../../shared/globals.service';
import {StorageService} from '../storage/storage.service';
import {StoragePool} from "../storage/detail/detail.service";
import {ClrWizard, ClrWizardPage} from "@clr/angular";
import {VmfsListService} from "../vmfs/list/list.service";
import {Router} from "@angular/router";
@Component({
  selector: 'app-nfs',
  templateUrl: './nfs.component.html',
  styleUrls: ['./nfs.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [NfsService, GlobalsService, StorageService,VmfsListService],
})
export class NfsComponent implements OnInit {
  list: List[] = []; // 数据列表
  radioCheck = 'list'; // 切换列表页显示
  total = 0; // 总数据数量
  isLoading = true; // table数据loading
  rowSelected = []; // 当前选中数据
  // ==========弹窗参数=======
  modifyShow = false;
  expand = false; // 扩容弹出框
  mountObj = '1';
  fsIds = [];
  unit='GB';
  hostList: Host[] = [];
  storagePools: StoragePool[] = [];
  nfsDetail: NfsDetail;
  updateNfs: UpdateNfs = new UpdateNfs();
  // 添加页面窗口
  @ViewChild('wizard') wizard: ClrWizard;
  @ViewChild('addPageOne') addPageOne: ClrWizardPage;
  @ViewChild('addPageTwo') addPageTwo: ClrWizardPage;

  errMessage = '';
  constructor(private remoteSrv: NfsService, private cdr: ChangeDetectorRef, public gs: GlobalsService ,
              private storageService: StorageService,private vmfsListService: VmfsListService,private router:Router) { }
  ngOnInit(): void {
  }
  // 获取nfs列表
  getNfsList() {
    this.isLoading = true;
    // 进行数据加载
    this.remoteSrv.getData()
      .subscribe((result: any) => {
        this.list = result.data;
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
    const flag = 'plugin';
    this.router.navigate(['nfs/add'],{
      queryParams:{
        flag
      }
    });
  }
  modifyData() {




    const flag="plugin";
    const fsId = this.rowSelected[0].fsId;
    const objectid=this.rowSelected[0].objectid;
    const shareId=this.rowSelected[0].shareId;
    const nfsName=this.rowSelected[0].name;
    this.router.navigate(['nfs/modify'],{
      queryParams:{
        fsId,objectid,shareId,nfsName,flag
      }
    });
    this.updateNfs = new UpdateNfs();
    this.updateNfs.dataStoreObjectId=this.rowSelected[0].objectid;
    this.updateNfs.shareId=this.rowSelected[0].shareId;
    this.updateNfs.fileSystemId=fsId;
    this.updateNfs.nfsName=this.rowSelected[0].name;
    this.remoteSrv.getNfsDetailById(fsId).subscribe((result: any) => {
      if (result.code === '200'){
        this.nfsDetail = result.data.data;
        if (this.nfsDetail.fileSystemTurning.smartQos.maxbandwidth==null
          &&this.nfsDetail.fileSystemTurning.smartQos.maxiops==null
          &&this.nfsDetail.fileSystemTurning.smartQos.minbandwidth==null
          &&this.nfsDetail.fileSystemTurning.smartQos.miniops==null
          &&this.nfsDetail.fileSystemTurning.smartQos.latency==null
        ){
          //qos策略没开启
          this.updateNfs.qosFlag=false;
        }else{
          //qos策略是开启的
          this.updateNfs.maxBandwidth=this.nfsDetail.fileSystemTurning.smartQos.maxbandwidth;
          this.updateNfs.maxIops=this.nfsDetail.fileSystemTurning.smartQos.miniops;
          this.updateNfs.minBandwidth=this.nfsDetail.fileSystemTurning.smartQos.minbandwidth;
          this.updateNfs.minIops=this.nfsDetail.fileSystemTurning.smartQos.miniops;
          this.updateNfs.latency=this.nfsDetail.fileSystemTurning.smartQos.latency;
          this.updateNfs.qosFlag=true;
          this.updateNfs.contolPolicy='up';
        }
        this.updateNfs.autoSizeEnable=this.nfsDetail.capacityAutonegotiation.autoSizeEnable;
        if (this.nfsDetail.fileSystemTurning.allocationType=='thin'){
          this.updateNfs.thin=true;
        }else{
          this.updateNfs.thin=false;
        }
        if (this.nfsDetail.fileSystemTurning.deduplicationEnabled==null){
          this.updateNfs.deduplicationEnabled=false;
        }else {
          this.updateNfs.deduplicationEnabled=this.nfsDetail.fileSystemTurning.deduplicationEnabled;
        }
        if (this.nfsDetail.fileSystemTurning.compressionEnabled==null){
          this.updateNfs.compressionEnabled=false;
        }else {
          this.updateNfs.compressionEnabled=this.nfsDetail.fileSystemTurning.compressionEnabled;
        }
        console.log(this.updateNfs)
      }
    });
    this.modifyShow = true;
  }
  modifyCommit(){
    console.log('提交参数：');
    this.updateNfs.name=this.updateNfs.nfsName;

    this.updateNfs.autoSizeEnable = null;
    this.updateNfs.deduplicationEnabled = null;
    this.updateNfs.compressionEnabled = null;
    this.updateNfs.thin = null;

    console.log(this.updateNfs);
    this.remoteSrv.updateNfs(this.updateNfs).subscribe((result: any) => {
      if (result.code === '200'){
        this.modifyShow = false;
      }else{
        this.modifyShow = true;
        this.errMessage = '编辑失败！'+result.description;
      }
    });
  }
  expandView(){
    const flag = 'plugin';
    const fsId=this.rowSelected[0].fsId;
    this.router.navigate(['nfs/expand'],{
      queryParams:{
        fsId,flag
      }
    });
  }
  // 弹出缩容页面
  reduceView(){
    const flag = 'plugin';
    const fsId=this.rowSelected[0].fsId;
    this.router.navigate(['nfs/reduce'],{
      queryParams:{
        fsId,flag
      }
    });
  }
  // 挂载
  mount(){
    this.jumpPage(this.rowSelected[0].objectid,"nfs/dataStore/mount");
  }
  jumpPage(objectId:string,url:string){
    const flag = 'plugin';
    this.router.navigate([url],{
      queryParams:{
        objectId,flag
      }
    });
  }
  // 卸载按钮点击事件
  unmountBtnFunc() {
    if(this.rowSelected!=null && this.rowSelected.length==1){
      this.jumpPage(this.rowSelected[0].objectid,"nfs/dataStore/unmount");
    }
  }
  // 删除按钮点击事件
  delBtnFunc() {
    const flag = 'plugin';
    const objectid=this.rowSelected[0].objectid;
    this.router.navigate(['nfs/delete'],{
      queryParams:{
        objectid,flag
      }
    })
  }
  navigateTo(objectid){
    console.log('页面跳转了');
    console.log(objectid);
    this.gs.getClientSdk().app.navigateTo({
      targetViewId: 'vsphere.core.datastore.summary',
      objectId: objectid
    });
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
  // 点刷新那个功能是分两步，一步是刷新，然后等我们这边的扫描任务，任务完成后返回你状态，任务成功后，你再刷新列表页面。
  scanDataStore() {
    this.vmfsListService.scanVMFS('nfs').subscribe((res: any) => {
      if (res.code === '200') {
        this.getNfsList();
        console.log('Scan success');
      } else {
        console.log('Scan faild');
      }
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });
  }
}
