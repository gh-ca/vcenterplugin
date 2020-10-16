import {
  Component,
  OnInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef, ElementRef, ViewChild
} from '@angular/core';
import {
  VmfsListService,
  List,
  StorageList,
  StoragePoolList,
  HostList,
  ClusterList,
  ServiceLevelList, HostOrCluster, GetForm,
} from './list.service';
import {ClrDatagridStateInterface, ClrWizard, ClrWizardPage} from '@clr/angular';
import {GlobalsService} from '../../../shared/globals.service';
import {Cluster, Host} from '../../nfs/nfs.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [VmfsListService],
})
export class VmfsListComponent implements OnInit {
  get params() { // 对query进行处理
    const p = Object.assign({}, this.query);
    return p;
  }
  /* get currentData() {
    const data = Object.assign({}, this.rowSelected[0]);
    return data || {}
  } */
  constructor(private remoteSrv: VmfsListService, private cdr: ChangeDetectorRef, public gs: GlobalsService) {}
  // 添加页面窗口
  @ViewChild('wizard') wizard: ClrWizard;
  @ViewChild('addPageOne') addPageOne: ClrWizardPage;
  @ViewChild('addPageTwo') addPageTwo: ClrWizardPage;

  expendActive = false; // 示例
  list: List[] = []; // 数据列表
  radioCheck = 'list'; // 切换列表页显示
  levelCheck = 'level'; // 是否选择服务等级：level 选择服务器等级 customer 未选择服务等级
  total = 0; // 总数据数量
  isLoading = true; // table数据loading
  rowSelected = []; // 当前选中数据
  wwns = []; // wwn 用来查询chart data
  query = { // 查询数据
    q: 'user:VMware',
    sort: 'stars',
    order: 'desc',
    page: 0,
    per_page: 5,
  };

  modifyShow = false;

  popShow = false; // 弹出层显示
  // 添加表单数据
  form = new GetForm().getAddForm();
  // 编辑form提交数据
  modifyForm = new GetForm().getEditForm();
  // 扩容form
  expandForm = new GetForm().getExpandForm();
  // 变更服务等级
  changeServiceLevelForm = new GetForm().getChangeLevelForm();
  storageList: StorageList[] = []; // 存储数据
  storagePoolList: StoragePoolList[] = []; // 存储池ID
  blockSizeOptions = []; // 块大小选择
  srgOptions = []; // 空间回收粒度初始化
  deviceList: HostOrCluster[] = []; // 主机AND集群
  chooseDevice; // 已选择的主机/集群
  hostRootDirectory: any[] = [
    {
      name: 'Host',
      icon: 'folder',
      expanded: false,
      files: []
    }
  ]; // 主机树结构
  clusterRootDirectory: any[] = [
    {
      name: 'Cluster',
      icon: 'folder',
      expanded: false,
      files: []
    }
  ]; // 集群树结构
  serviceLevelList: ServiceLevelList[] = []; // 服务等级列表
  mountShow = false; // 挂载窗口
  delShow = false; // 删除窗口
  unmountShow = false; // 卸载窗口
  reclaimShow = false; // 卸载窗口
  changeServiceLevelShow = false; // 变更服务等级
  expandShow = false; // 变更服务等级
  hostList: HostList[] = []; // 挂载页面 主机列表
  clusterList: ClusterList[] = []; // 挂载页面集群列表
  // 挂载form表单
  mountForm = new GetForm().getMountForm();
  chooseHost: HostList; // 已选择的主机
  chooseCluster: ClusterList; // 已选择的集群
  chooseUnmountDevice = []; // 已选择卸载的设备
  mountedDeviceList: HostOrCluster[] = []; // 已挂载的设备
  storageType = 'vmfs'; // 扫描类型（扫描接口）
  // 生命周期： 初始化数据

  isServiceLevelData = true; // 编辑页面 所选数据是否为服务等级 true:是 false:否 若为是则不显示控制策略以及交通管制对象
  mountHostData = true; // 挂载页面主机是否加载完毕 true是 false否
  mountClusterData = true; // 挂载页面集群是否加载完毕 true是 false否
  serviceLevelIsNull = false; // 未选择服务等级true未选择 false选择 添加、服务登记变更
  ngOnInit() {
    // 列表数据
    this.refresh();
  }
  // 修改
  modifyBtnClick() {
    console.log(this.rowSelected[0]);
    // 初始化form
    this.modifyForm = new GetForm().getEditForm();
    if (this.rowSelected.length === 1) {
      this.modifyForm.name = this.rowSelected[0].name;
      this.modifyForm.oldDsName = this.rowSelected[0].name;
      this.modifyForm.volume_id = this.rowSelected[0].volumeId;
      this.modifyForm.dataStoreObjectId = this.rowSelected[0].objectid;

      // 服务等级名称： 服务等级类型只能修改卷名称 非服务等级可修改卷名称+归属控制+QOS策略等
      this.modifyForm.service_level_name = this.rowSelected[0].serviceLevelName;
      if (this.rowSelected[0].serviceLevelName) { // vmfs为非服务等级类型
        this.isServiceLevelData = false;
        this.modifyForm.control_policy = '';
      } else {// vmfs为服务等级类型
        this.isServiceLevelData = true;

        this.modifyForm.max_iops = this.rowSelected[0].maxIops;
        this.modifyForm.max_bandwidth = this.rowSelected[0].maxBandwidth;
        this.modifyForm.min_iops = this.rowSelected[0].minIops;
        this.modifyForm.min_bandwidth = this.rowSelected[0].minBandwidth;
        if (this.modifyForm.max_iops !== '0') {
          this.modifyForm.control_policy = '1';
        } else {
          this.modifyForm.control_policy = '0';
        }
      }
      this.modifyShow = true;
    }
  }
  // 修改 处理
  modifyHandleFunc() {

    // 设置修改的卷名称以及修改后的名称
    if (this.modifyForm.isSameName) {
      this.modifyForm.newVoName = this.modifyForm.name;
    } else {
      // 若不修改卷名称则将旧的卷名称设置为newVol
      this.modifyForm.newVoName = this.rowSelected[0].volumeName;
    }
    this.modifyForm.newDsName = this.modifyForm.name;
    console.log('this.modifyForm:', this.modifyForm);
    this.remoteSrv.updateVmfs(this.modifyForm.volume_id, this.modifyForm).subscribe((result: any) => {
      if (result.code === '200') {
        console.log('modify success:' + this.modifyForm.oldDsName);
        // 重新请求数据
        this.scanDataStore();
      } else {
        console.log('modify faild：' + this.modifyForm.oldDsName + result.description);
      }
      // 关闭编辑窗口
      this.modifyShow = false;
      // 重新请求列表数据
      // this.refresh();
    });
  }
  // table数据处理
  refresh() {
    this.isLoading = true;
    // 进行数据加载
    this.remoteSrv.getData(this.params)
        .subscribe((result: any) => {
          console.log("result:");
          console.log(result);
          if (result.code === '200' && null != result.data ) {
            this.list = result.data;
            if (null !== this.list) {
              this.total = this.list.length;
              // 获取chart 数据
              const wwns = [];
              this.list.forEach(item => {
                wwns.push(item.wwn);
              });
              // 设置卷ID集合
              this.wwns = wwns;

              if (this.wwns.length > 0) {
                this.remoteSrv.getChartData(this.wwns).subscribe((chartResult: any) => {
                  console.log('chartResult');
                  console.log(chartResult);
                  if (chartResult.code === '200' && chartResult.data != null) {
                    const chartList: List [] = chartResult.data;
                    this.list.forEach(item => {
                      chartList.forEach(charItem => {
                        // 若属同一个卷则将chartItem的带宽、iops、读写相应时间 值赋予列表
                        if (item.wwn === charItem.volumeId) {
                          item.iops = charItem.iops;
                          item.bandwidth = charItem.bandwidth;
                          item.readResponseTime = charItem.readResponseTime;
                          item.writeResponseTime = charItem.writeResponseTime;
                        }
                      });
                    });
                    this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
                  } else {
                    console.log(chartResult.description);
                  }
                });
              }
            }
          } else {
            console.log(result.description);
          }
          this.isLoading = false;
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        });
  }
  // 点刷新那个功能是分两步，一步是刷新，然后等我们这边的扫描任务，任务完成后返回你状态，任务成功后，你再刷新列表页面。
  scanDataStore() {
    this.remoteSrv.scanVMFS(this.storageType).subscribe((res: any) => {
      console.log('res');
      console.log(res);
      if (res.code === '200') {
        this.refresh();
        console.log('Scan success');
      } else {
        console.log('Scan faild');
      }
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });
  }

  // 主机或集群数据处理 deviceType: 设备类型、deviceName 设备名称、主机/集群ID
  deviceDataHandle(deviceType: string, deviceName: string, deviceId) {
      console.log('deviceType:' + deviceType + ', deviceName: ' + deviceName + ',deviceId: ' + deviceId);
      this.form.deviceName = deviceName;
      switch (deviceType) {
        case 'Host':
          this.form.hostId = deviceId;
          this.form.host = deviceName;
          this.hostRootDirectory[0].expanded = false;
          break;
        case 'Cluster':
          this.form.cluster = deviceName;
          this.form.clusterId = deviceId;
          this.clusterRootDirectory[0].expanded = false;
          break;
        default:
          this.form.hostId = '';
          this.form.host = '';
          this.form.cluster = '';
          this.form.clusterId = '';
          break;
      }
  }
  // 获取所有存储数据
  getStorageList() {
    this.remoteSrv.getStorages().subscribe((result: any) => {
      console.log(result);
      if (result.code === '200' && result.data !== null) {
        this.storageList = result.data.data;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    });
  }

  // 获取存储池数据
  getStoragePoolsByStorId() {
    console.log('selectSotrageId' + this.form.storage_id);
    if (null !== this.form.storage_id && '' !== this.form.storage_id) {
      this.remoteSrv.getStoragePoolsByStorId(this.form.storage_id, 'block').subscribe((result: any) => {
        console.log(result);
        if (result.code === '200' && result.data !== null) {
          this.storagePoolList = result.data.data;
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        }
      });
    }
  }

  // 初始化块大小（修改版本触发事件）
  setBlockSizeOptions() {
    const options = [];
    // const versionVal = this.versionBtn.nativeElement.value;
    const versionVal = this.form.version + '';
    const option0 = {key: null, value : ''};
    options.push(option0);
    console.log('versionVal' + versionVal);
    if (versionVal === '6') {
      const option1 = {key: 1024, value : '1MB'};
      options.push(option1);
      const option2 = {key: 64, value : '64KB'};
      options.push(option2);
    } else if (versionVal === '5') {
      const option1 = {key: 1024, value : '1MB'};
      options.push(option1);
    }
    // 重置blockSize的值
    this.form.blockSize = null;
    // this.blockSizeBtn.nativeElement.value = null;
    // 设置blockSize 可选值
    this.blockSizeOptions = options;
    // 重置空间回收粒度
    // this.form.spaceReclamationGranularity = 0;
    this.setSrgOptions();
  }
  // 初始化空间回收粒度
  setSrgOptions() {
    const options = [];
    // const srgValue = this.blockSizeBtn.nativeElement.value;
    const blockValue = this.form.blockSize + '';
    const option0 = {key: null, value : ''};
    options.push(option0);
    if (blockValue === '1024') {
      const option1 = {key: 1024, value : '1MB'};
      options.push(option1);
      const option2 = {key: 8, value : '8KB'};
      options.push(option2);
    } else if (blockValue === '64') {
      const option1 = {key: 64, value : '64KB'};
      options.push(option1);
      const option2 = {key: 8, value : '8KB'};
      options.push(option2);
    }
    this.srgOptions = options;
    this.form.spaceReclamationGranularity = null;
    console.log('this.form.blockSize:' + this.form.blockSize);
    console.log('this.form.spaceReclamationGranularity:' + this.form.spaceReclamationGranularity);
  }

  // 设置设备数据
  setDeviceList() {
    // 初始化数据
    this.deviceList = [];
    const nullDevice =  {
      deviceId: '',
      deviceName: '',
      deviceType: '',
    };
    this.deviceList.push(nullDevice);
    this.chooseDevice = this.deviceList[0];

    console.log('this.chooseDevice', this.chooseDevice);
    // 初始添加页面的主机集群信息
    this.setHostDatas().then(res => {
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });

    this.setClusterDatas().then(res => {
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });
  }

  // 设置主机数据
  setHostDatas() {
    return new Promise((resolve, reject) => {
      this.remoteSrv.getHostList().subscribe((result: any) => {
        let hostList: HostList[] = []; // 主机列表
        console.log('host', result);
        if (result.code === '200' && result.data !== null) {
          hostList = result.data;
          hostList.forEach(item => {
            // const hostData = {
            //   icon: 'map',
            //   id: item.hostId,
            //   name: item.hostName,
            //   active: false
            // };
            // this.hostRootDirectory[0].files.push(hostData);

            const hostInfo = {
              deviceId: item.hostId,
              deviceName: item.hostName,
              deviceType: 'host',
            };
            this.deviceList.push(hostInfo);
          });
        }
        /*console.log('this.deviceList  host::');
        console.log(this.deviceList);*/
        this.form.hostDataloadSuccess = true;
        resolve(this.deviceList);
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      });
    });
  }
  // 设置集群数据
  setClusterDatas() {
    return new Promise((resolve, reject) => {
      this.remoteSrv.getClusterList().subscribe((result: any) => {
        let clusterList: ClusterList [] = []; // 集群列表
        console.log('cluster', result);
        console.log('cluster', result.data !== null);
        if (result.code === '200' && result.data !== null) {
          clusterList = result.data;
          clusterList.forEach(item => {
            // const culData = {
            //   icon: 'map',
            //   id: item.clusterId,
            //   name: item.clusterName,
            //   active: false
            // };
            // this.clusterRootDirectory[0].files.push(culData);

            const clusterInfo = {
              deviceId: item.clusterId,
              deviceName: item.clusterName,
              deviceType: 'cluster',
            };
            this.deviceList.push(clusterInfo);
          });
        }
        resolve(this.deviceList);
        this.form.culDataloadSuccess = true;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        console.log('cluster', result.data !== null);
      });
    });
  }
  // 点击addBtn触发事件
  addBtnClickFunc() {
    // 初始化表单
    this.form = new GetForm().getAddForm();
    // 添加页面显示
    this.popShow = true;
    // 添加页面默认打开首页
    this.jumpTo(this.addPageOne, this.wizard);
    // 版本、块大小、粒度下拉框初始化
    this.setBlockSizeOptions();

    // 设置主机/集群
    this.setDeviceList();

    // Page2默认打开服务等级也页面
    this.levelCheck = 'level';

    // 初始化服务等级数据
    this.setServiceLevelList();

    // 初始化存储池
    this.storagePoolList = [];

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
  // 获取服务等级数据
  setServiceLevelList() {
    // 初始化服务等级选择参数
    this.serviceLevelIsNull = false;
    // 获取服务等级数据
    this.remoteSrv.getServiceLevelList().subscribe((result: any) => {
      console.log(result);
      if (result.code === '200' && result.data !== null) {
        this.serviceLevelList = result.data.data;
        console.log('this.serviceLevelList',this.serviceLevelList)
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    });
  }
  showServiceLevel(obj:any, isShow:boolean) {
    console.log('obj+isShow', obj, isShow);
    if (isShow) {
      obj.hide = true;
    }
  }
  // 添加vmfs 处理
  addVmfsHanlde() {
    let selectResult = this.serviceLevelList.find(item => item.show === true);
    console.log('selectResult', this.levelCheck === 'level' && selectResult)
    if ((this.levelCheck === 'level' && selectResult) || this.levelCheck !== 'level') { // 选择服务等级
      if (selectResult) {
        this.form.service_level_id = selectResult.id;
        this.form.service_level_name = selectResult.name;
      }
      // 数据预处----卷名称
      if (this.form.isSameName) { // 卷名称与vmfs名称相同（PS：不同时为必填）
        this.form.volumeName = this.form.name;
      }
      // 数据预处----容量 （后端默认单位为GB）
      switch (this.form.capacityUnit) {
        case 'TB':
          this.form.capacity = this.form.capacity * 1024;
          break;
        case 'MB':
          this.form.capacity = this.form.capacity / 1024;
          break;
        case 'KB':
          this.form.capacity = this.form.capacity / (1024 * 1024);
          break;
        default: // 默认GB 不变
          break;
      }
      // 主机/集群数据处理
      if (this.chooseDevice.deviceType === 'host') {
        this.form.host = this.chooseDevice.deviceName;
        this.form.hostId = this.chooseDevice.deviceId;
      } else {
        this.form.cluster = this.chooseDevice.deviceName;
        this.form.clusterId = this.chooseDevice.deviceId;
      }
      if (this.levelCheck === 'customer') { // 未选择 服务等级 需要将服务等级数据设置为空
        this.form.service_level_id = null;
        this.form.service_level_name = null;
      }
      // 若控制策略数据为空，则将控制策略变量置为空
      if(this.form.maxbandwidth === null && this.form.maxiops
        && this.form.minbandwidth && this.form.miniops && this.form.latency) {
        this.form.control_policy = null;
      }
      this.form.control_policy = null;
      console.log(this.form);
      this.remoteSrv.createVmfs(this.form).subscribe((result: any) => {
        if (result.code === '200') {
          console.log('创建成功');
          // 重新请求数据
          this.scanDataStore();
        } else {
          console.log('创建失败：' + result.description);
        }
      });
    } else {
      this.serviceLevelIsNull = true;
      this.wizard.open();
    }
  }

  // 容量单位转换
  capacityChange(obj: any) {
    console.log('event', obj.value === '1')
    const objValue = obj.value.match(/\d+(\.\d{0,2})?/)? obj.value.match(/\d+(\.\d{0,2})?/)[0] : '';

    if (objValue !== '') {

      let capatityG;
      // 数据预处----容量 （后端默认单位为GB）
      switch (this.form.capacityUnit) {
        case 'TB':
          capatityG = objValue * 1024;
          break;
        case 'MB':
          capatityG = objValue / 1024;
          break;
        case 'KB':
          capatityG = objValue / (1024 * 1024);
          break;
        default: // 默认GB 不变
          capatityG = objValue;
          break;
      }

      // 版本号5 最小容量为1.3G 版本号6最小2G
      if (capatityG < 1.3 && this.form.version === '5') {
        capatityG = 1.3;
      } else if(capatityG < 2 && this.form.version === '6') {
        capatityG = 2;
      }
      switch (this.form.capacityUnit) {
        case 'TB':
          capatityG = capatityG / 1024;
          break;
        case 'MB':
          capatityG = capatityG * 1024;
          break;
        case 'KB':
          capatityG = capatityG * (1024 * 1024);
          break;
        default: // 默认GB 不变
          capatityG = capatityG;
          break;
      }

      obj.value = capatityG;
    } else {
      obj.value = objValue;
    }
  }

  // 未选择服务等级 时调用方法
  customerClickFunc() {
    this.levelCheck = 'customer';
    this.getStorageList();
  }
  // 页面跳转
  navigateTo(objectid: string){
    console.log('页面跳转了');
    console.log(objectid);
    this.gs.getClientSdk().app.navigateTo({
      targetViewId: 'vsphere.core.datastore.summary',
      objectId: objectid
    });
  }

  // 删除VMFS 处理函数
  delHandleFunc() {
    const volumeIds = this.rowSelected.map(item => item.volumeId);
    console.log('del vmfs volumeIds:' + volumeIds);
    this.remoteSrv.delVmfs(volumeIds).subscribe((result: any) => {
      // 隐藏删除提示页面
      this.delShow = false;
      if (result.code === '200'){
        console.log('DEL success' + this.rowSelected[0].name + ' success');
        // 重新请求数据
        this.scanDataStore();
      } else {
        console.log('DEL faild: ' + result.description);
      }
      // 关闭删除页面
      this.delShow = false;
      this.cdr.detectChanges();
    });
  }

  // 挂载按钮点击事件
  mountBtnFunc() {
    // 初始化表单
    this.mountForm = new GetForm().getMountForm();
    const objectIds = [];
    objectIds.push(this.rowSelected[0].objectid);
    this.mountForm.dataStoreObjectIds = objectIds;

    // 初始化主机
    this.mountHostData = false;
    this.hostList = [];
    const hostNullInfo = {
      hostId: '',
      hostName: ''
    };
    this.hostList.push(hostNullInfo);
    this.initMountHost().then(res => {
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });

    // 初始化集群
    this.mountClusterData = false;
    this.clusterList = [];
    const clusterNullInfo = {
      clusterId: '',
      clusterName: ''
    }
    this.clusterList.push(clusterNullInfo);

    this.initMountCluster().then(res => {
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });

    // 打开挂载页面
    this.mountShow = true;
  }
  // 挂载  集群数据初始化
  initMountCluster() {
    return new Promise((resolve, reject) => {
      // 获取集群 通过ObjectId过滤已挂载的集群
      this.remoteSrv.getClusterListByObjectId(this.rowSelected[0].objectid).subscribe((result: any) => {
        if (result.code === '200'){
          result.data.forEach(item => {
            this.clusterList.push(item);
          });
        }
        this.chooseCluster = this.clusterList[0];
        this.mountClusterData = true;
        resolve(this.deviceList);
        this.cdr.detectChanges();
      });
    });
  }
  // 挂载 主机数据初始化
  initMountHost() {
    return new Promise((resolve, reject) => {
      // 获取服务器 通过ObjectId过滤已挂载的服务器
      this.remoteSrv.getHostListByObjectId(this.rowSelected[0].objectid).subscribe((result: any) => {
        if (result.code === '200' && result.data !== null){
          result.data.forEach(item => {
            this.hostList.push(item);
          });
        }
        this.chooseHost =  this.hostList[0];
        this.mountHostData = true;
        resolve(this.deviceList);
        this.cdr.detectChanges();
      });
    });
  }
  // 挂载提交
  mountSubmit(){
    // 数据封装
    if (this.mountForm.mountType === '1'){ // 服务器
      this.mountForm.hostId = this.chooseHost.hostId;
      this.mountForm.host = this.chooseHost.hostName;
    }else if (this.mountForm.mountType === '2'){ // 集群
      this.mountForm.cluster = this.chooseCluster.clusterName;
      this.mountForm.clusterId = this.chooseCluster.clusterId;
    }
    this.remoteSrv.mountVmfs(this.mountForm).subscribe((result: any) => {
      if (result.code  ===  '200'){
        console.log('挂载成功');
        // 刷新数据
        this.scanDataStore();
      } else {
        console.log('挂载异常：' + result.description);
      }
      // 隐藏挂载页面
      this.mountShow = false;
      this.cdr.detectChanges();
    });
  }
  // 卸载处理函数
  unmountHandleFunc() {
    this.remoteSrv.unmountVMFS().subscribe((result: any) => {

      if (result.code === '200'){
        console.log('unmount ' + name + ' success');
        // 重新请求数据
        this.scanDataStore();
      } else {
        console.log('unmount ' + name + ' fail：' + result.description);
      }
      // 关闭卸载页面
      this.unmountShow = false;
      this.cdr.detectChanges();
    });
  }
  // 回收空间 处理
  reclaimHandleFunc() {
    const name = this.rowSelected[0].name;
    console.log('reclaim:' + name);
    const vmfsNames = this.rowSelected.map(item => item.name);
    this.remoteSrv.reclaimVmfs(vmfsNames).subscribe((result: any) => {
      if (result.code === '200'){
        console.log('Reclaim ' + name + ' success');
        // 空间回收完成重新请求数据
        this.scanDataStore();
      } else {
        console.log('Reclaim ' + name + ' fail：' + result.description);
      }
      // 关闭回收空间页面
      this.reclaimShow = false;
      this.cdr.detectChanges();
    });
  }
  // 服务等级 点击事件 serviceLevId:服务等级ID、serviceLevName：服务等级名称
  serviceLevelClickHandel(serviceLevId: string, serviceLevName: string, isoppen:any) {
    console.log('isoppen', isoppen)
    this.form.service_level_id = serviceLevId;
    this.form.service_level_name = serviceLevName;
    console.log('serviceLevId:' + serviceLevId + 'serviceLevName:' + serviceLevName);
  }
  // 变更服务等级 按钮点击事件
  changeServiceLevelBtnFunc() {

    // 初始化表单
    this.changeServiceLevelForm = new GetForm().getChangeLevelForm();
    if (this.rowSelected.length === 1) {
      // 设置表单默认参数
      const volumeIds = [];
      volumeIds.push(this.rowSelected[0].volumeId);
      this.changeServiceLevelForm.volume_ids = volumeIds;
      this.changeServiceLevelForm.ds_name = this.rowSelected[0].name;

      // 显示修改服务等级页面
      this.changeServiceLevelShow = true;
      // 初始化服务等级列表数据
      this.setServiceLevelList();
    }
  }
  // 变更服务等级 服务等级点击事件
  changeSLDataClickFunc(serviceLevId: string, serviceLevName: string) {
    this.changeServiceLevelForm.service_level_id = serviceLevId;
    this.changeServiceLevelForm.service_level_name = serviceLevName;
  }

  // 变更服务等级 处理
  changeSLHandleFunc() {
    let selectResult = this.serviceLevelList.find(item => item.show === true)
    console.log('selectResult', selectResult)
    if (selectResult) {
      this.serviceLevelIsNull = false;
      this.remoteSrv.changeServiceLevel(this.changeServiceLevelForm).subscribe((result: any) => {
        if (result.code === '200'){
          console.log('change service level success:' + name);
          // 重新请求数据
          this.scanDataStore();
        } else {
          console.log('change service level faild: ' + name  + ' Reason:' + result.description);
        }
        // 关闭修改服务等级页面
        this.changeServiceLevelShow = false;
        this.cdr.detectChanges();
      });
    } else {
      this.serviceLevelIsNull = true;
      console.log("服务等级不能为空！");
    }
  }
  // 扩容按钮点击事件
  expandBtnFunc() {
    if (this.rowSelected.length === 1) {
      // 初始化form表单
      this.expandForm = new GetForm().getExpandForm();

      this.expandShow = true;
      console.log(this.rowSelected[0]);
      this.expandForm.volume_id = this.rowSelected[0].volumeId;
      this.expandForm.ds_name = this.rowSelected[0].name;
    }
  }
  // 扩容处理
  expandHandleFunc() {
    // 容量单位转换
    switch (this.expandForm.capacityUnit) {
      case 'TB':
        this.expandForm.vo_add_capacity = this.expandForm.vo_add_capacity * 1024;
        break;
      case 'MB':
        this.expandForm.vo_add_capacity = this.expandForm.vo_add_capacity / 1024;
        break;
      case 'KB':
        this.expandForm.vo_add_capacity = this.expandForm.vo_add_capacity / (1024 * 1024);
        break;
      default: // 默认GB 不变
        break;
    }
    // 参数封装
    const params = [];
    params.push(this.expandForm);
    this.remoteSrv.expandVMFS(params).subscribe((result: any) => {
      if (result.code === '200'){
        console.log('expand success:' + name);
        // 重新请求数据
        this.scanDataStore();
      }else {
        console.log('expand: ' + name  + ' Reason:' + result.description);
      }
      // 隐藏扩容页面
      this.expandShow = false;
      this.cdr.detectChanges();
    });
  }


}
