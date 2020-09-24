import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Cluster, Host, List, Mount, NfsService} from './nfs.service';
import {GlobalsService} from '../../shared/globals.service';
import {LogicPort, StorageList, StoragePool, StorageService} from '../storage/storage.service';
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
  // 添加form提交数据
  form = {
    storageDevice: '',
    storagePool: '',
    logicPort: 0,
    datastoreName: 0,
    fsname: '',
    shareName: '',
    size: 0,
    unit: 'GB',
    nfsProtocol: 'v3',
    name: '',
    favorite: '',
    sameName: true,
    qosPolicy: false,
    thin: true,
    control: 'up',
    deduplication: 'disable',
    compression: 'disable',
    capAuto: false
  };
  currentData = {
    name: '',
    sameName: true,

  };
  hostList: Host[] = [];
  clusterList: Cluster[] = [];
  mountForm = new Mount();
  select1 = [];
  select2 = [];

  storageList: StorageList[] = [];
  storagePools: StoragePool[] = [];
  logicPorts: LogicPort[] = [];

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
      if (result.code === '0'){
        const chartList: List [] = result.data;
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
    });
  }

  addView(){
    // 获取存储列表
    this.storageService.getData().subscribe((s: any) => {
       if (s.code === '0'){
        this.storageList = s.data.data;
        }
    });
    this.popShow = true;
  }
  selectStoragePool(){
    this.storagePools = null;
    this.logicPorts = null;
    console.log('查询存储池....');
    // 选择存储后获取存储池
    this.storageService.getStoragePoolListByStorageId(this.form.storageDevice)
      .subscribe((r: any) => {
        if (r.code === '0'){
          this.storagePools = r.data.data;
        }
      });
    this.selectLogicPort();
  }
  selectLogicPort(){
    console.log('查询逻辑端口....');
    // 选择存储后逻辑端口
    this.storageService.getLogicPortListByStorageId(this.form.storageDevice)
      .subscribe((r: any) => {
        if (r.code === '0'){
        this.logicPorts = r.data.data;
      }
      });
  }
  modifyData() {
    console.log(this.rowSelected[0]);
    this.currentData = Object.assign({}, this.rowSelected[0]);
    this.modifyShow = true;
  }
  // 添加提交方法
  addNfs(){
    console.log('form');
    console.log(this.form);
    this.remoteSrv.addNfs(this.form).subscribe((result: any) => {
      const data = result.data;
      console.log('result');
      console.log(data);
    });
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
    this.mountForm.dataStoreName = this.rowSelected[0].name;
    this.remoteSrv.getHostList(this.mountForm.dataStoreName).subscribe((r: any) => {
      if (r.code === '0'){
        this.hostList = r.data;
        this.cdr.detectChanges();
      }
    });
    this.remoteSrv.getClusterList(this.mountForm.dataStoreName).subscribe((r: any) => {
      if (r.code === '0'){
        this.clusterList = r.data;
        this.cdr.detectChanges();
      }
    });
    this.mountShow = true;
  }
  // 挂载提交
  mountSubmit(){
    if (this.mountObj === '1'){
      const strs: string[] = [];
      this.select1.forEach(e => {
        strs.push(e.hostId);
      });
      this.mountForm.hosts = strs;
    }else if (this.mountObj === '2'){
      const strs: string[] = [];
      this.select2.forEach(e => {
        strs.push(e.clusterId);
      });
      this.mountForm.clusters = strs;
    }
    this.remoteSrv.mountNfs(this.mountForm).subscribe((result: any) => {
      if (result.code  ===  '0'){
        this.mountShow = false;
        this.mountForm = new Mount();
        this.select1 = [];
        this.select2 = [];
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
