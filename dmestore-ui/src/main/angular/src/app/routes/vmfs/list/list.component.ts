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
import {ClrDatagridStateInterface} from '@clr/angular';
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
  expendActive = false // 示例
  list: List[] = []; // 数据列表
  radioCheck = 'list'; // 切换列表页显示
  levelCheck = 'level'; // 是否选择服务等级：level 选择服务器等级 customer 未选择服务等级
  total = 0; // 总数据数量
  isLoading = true; // table数据loading
  rowSelected = []; // 当前选中数据
  volumnIds = []; // 卷ID
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
  chooseDevice: HostOrCluster; // 已选择的主机/集群
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
  // 生命周期： 初始化数据
  ngOnInit() {
    // 列表数据
    this.refresh();
  }
  // 修改
  modifyData() {
    console.log(this.rowSelected[0]);
    // 初始化form
    this.modifyForm = new GetForm().getEditForm();
    if (this.rowSelected.length === 1) {
      this.modifyForm.name = this.rowSelected[0].name;
      this.modifyForm.oldDsName = this.rowSelected[0].name;
      this.modifyForm.volume_id = this.rowSelected[0].volumeId;
      this.modifyForm.max_iops = this.rowSelected[0].maxIops;
      this.modifyForm.max_bandwidth = this.rowSelected[0].maxBandwidth;
      this.modifyForm.min_iops = this.rowSelected[0].minIops;
      this.modifyForm.min_bandwidth = this.rowSelected[0].minBandwidth;
      this.modifyForm.dataStoreObjectId = this.rowSelected[0].objectid;
      if (this.modifyForm.max_iops !== '0') {
        this.modifyForm.control_policy = '1';
      } else {
        this.modifyForm.control_policy = '0';
      }
      this.modifyShow = true;
    }
  }
  // 修改 处理
  modifyHandleFunc() {
    console.log(this.modifyForm);
    // 设置修改的卷名称以及修改后的名称
    if (this.modifyForm.isSameName) {
      this.modifyForm.newVoName = this.modifyForm.name;
    }
    this.modifyForm.newDsName = this.modifyForm.name;
    this.remoteSrv.updateVmfs(this.modifyForm.volume_id, this.modifyForm).subscribe((result: any) => {
      if (result.code === '0') {
        console.log('modify success:' + this.modifyForm.oldDsName);
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
    // We convert the filters from an array to a map,
    // because that's what our backend-calling service is expecting
    // let sort
    // <{order: Boolean, sort: String}> sort
    // sort = state.sort
    // // 排序 排序规则order:  true降序  false升序 ;   排序值by: 字符串  按照某个字段排序  在html里[clrDgSortBy]自定义
    // if (sort) {
    //   this.query.order = sort.reverse ? 'desc' : 'asc'
    //   this.query.sort = sort.by
    // }
    // // 过滤器   过滤内容
    // let filters: {[prop:string]: any[]} = {};
    // if (state.filters) {
    //   for (let filter of state.filters) {
    //     let {property, value} = <{property: string, value: string}> filter;
    //     filters[property] = [value];
    //   }
    // }
    // 分页器数据current: 1 当前页;    size: 5 分页大小
    // if (state.page) {
    //   this.query.page = state.page.current;
    //   this.query.per_page = state.page.size;
    // }
    // 进行数据加载
    this.remoteSrv.getData(this.params)
        .subscribe((result: any) => {
          console.log(result);
          if (result.code === '0' && null != result.data ) {
            this.list = result.data;
            if (null !== this.list) {
              this.total = this.list.length;
              this.isLoading = false;
              // 获取chart 数据
              const volumeIds = [];
              this.list.forEach(item => {
                volumeIds.push(item.volumeId);
              });
              // 设置卷ID集合
              this.volumnIds = volumeIds;

              if (this.volumnIds.length > 0) {
                this.remoteSrv.getChartData(this.volumnIds).subscribe((chartResult: any) => {
                  console.log('chartResult');
                  console.log(chartResult);
                  if (chartResult.code === '0' && chartResult.data != null) {
                    const chartList: List [] = chartResult.data;
                    this.list.forEach(item => {
                      chartList.forEach(charItem => {
                        // 若属同一个卷则将chartItem的带宽、iops、读写相应时间 值赋予列表
                        if (item.volumeId === charItem.volumeId) {
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
              this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
            }
          } else {
            console.log(result.description);
          }
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
      if (result.code === '0' && result.data !== null) {
        this.storageList = result.data.data;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    });
  }

  // 获取存储池数据
  getStoragePoolsByStorId() {
    console.log('selectSotrageId' + this.form.storage_id);
    if (null !== this.form.storage_id && '' !== this.form.storage_id) {
      this.remoteSrv.getStoragePoolsByStorId(this.form.storage_id).subscribe((result: any) => {
        console.log(result);
        if (result.code === '0' && result.data !== null) {
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

  // 设置主机数据
  setHostDatas() {
    return new Promise((resolve, reject) => {
      this.remoteSrv.getHostList().subscribe((result: any) => {
        let hostList: HostList[] = []; // 主机列表
        /*console.log(result);*/
        if (result.code === '0' && result.data !== null) {
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
          /*console.log('this.deviceList  host::');
          console.log(this.deviceList);*/
          this.form.hostDataloadSuccess = true;
          resolve(this.deviceList);
        }
      });
    });
  }
  // 设置集群数据
  setClusterDatas() {
    // return new Promise((resolve, reject) => {
      this.remoteSrv.getClusterList().subscribe((result: any) => {
        let clusterList: ClusterList [] = []; // 集群列表
        console.log(result);
        if (result.code === '0' && result.data !== null) {
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
          // resolve(this.deviceList);
          this.form.culDataloadSuccess = true;
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        }
      });
    // });
  }
  // 点击addBtn触发事件
  addBtnClickFunc() {
    // 初始化表单
    this.form = new GetForm().getAddForm();
    // 添加页面显示
    this.popShow = true;
    // 版本、块大小、粒度下拉框初始化
    this.setBlockSizeOptions();
    // 初始化数据
    this.deviceList = [];
    // 初始添加页面的主机集群信息
    this.setHostDatas().then(res => {
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });

    this.setClusterDatas();
    // 初始化服务等级数据
    this.setServiceLevelList();
  }
  // 获取服务等级数据
  setServiceLevelList() {
    this.remoteSrv.getServiceLevelList().subscribe((result: any) => {
      console.log(result);
      if (result.code === '0' && result.data !== null) {
        this.serviceLevelList = result.data.data;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    });
  }

  // 添加vmfs 处理
  addVmfsHanlde() {
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

    console.log(this.form);
    this.remoteSrv.createVmfs(this.form).subscribe((result: any) => {
      if (result.code === '0') {
        console.log('创建成功');
      } else {
        console.log('创建失败：' + result.description);
      }
    });
  }
  // 服务等级 点击事件 serviceLevId:服务等级ID、serviceLevName：服务等级名称
  serviceLevelClickHandel(serviceLevId: string, serviceLevName: string) {
    this.form.service_level_id = serviceLevId;
    this.form.service_level_name = serviceLevName;
    console.log('serviceLevId:' + serviceLevId + 'serviceLevName:' + serviceLevName);
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
      if (result.code === '0'){
        console.log('DEL success' + this.rowSelected[0].name + ' success');
        // 空间回收完成重新请求数据
        this.refresh();
      } else {
        console.log('DEL faild: ' + result.description);
      }
      // 关闭删除页面
      this.delShow = false;
    });
  }
  // 挂载按钮点击事件
  mountBtnFunc() {
    // 初始化表单
    this.mountForm = new GetForm().getMountForm();
    const objectIds = [];
    objectIds.push(this.rowSelected[0].objectid);
    this.mountForm.dataStoreObjectIds = objectIds;
    // 获取服务器 通过ObjectId过滤已挂载的服务器
    this.remoteSrv.getHostListByObjectId(this.rowSelected[0].objectid).subscribe((result: any) => {
    // this.remoteSrv.getHostList().subscribe((result: any) => {
      if (result.code === '0' && result.data !== null){
        this.hostList = result.data;
        this.cdr.detectChanges();
      }
    });
    // 获取集群 通过ObjectId过滤已挂载的集群
    this.remoteSrv.getClusterListByObjectId(this.rowSelected[0].objectid).subscribe((result: any) => {
    // this.remoteSrv.getClusterList().subscribe((result: any) => {
      if (result.code === '0'){
        this.clusterList = result.data;
        this.cdr.detectChanges();
      }
    });
    // 打开挂载页面
    this.mountShow = true;
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
      if (result.code  ===  '0'){
        console.log('挂载成功');
      } else {
        console.log('挂载异常：' + result.description);
      }
      // 隐藏挂载页面
      this.mountShow = false;
    });
  }
  // 卸载处理函数
  unmountHandleFunc() {
    this.remoteSrv.unmountVMFS().subscribe((result: any) => {

      if (result.code === '0'){
        console.log('unmount ' + name + ' success');
        // 空间回收完成重新请求数据
        this.refresh();
      } else {
        console.log('unmount ' + name + ' fail：' + result.description);
      }
      // 关闭卸载页面
      this.unmountShow = false;
    });
  }
  // 回收空间 处理
  reclaimHandleFunc() {
    const name = this.rowSelected[0].name;
    console.log('reclaim:' + name);
    const vmfsNames = this.rowSelected.map(item => item.name);
    this.remoteSrv.reclaimVmfs(vmfsNames).subscribe((result: any) => {
      if (result.code === '0'){
        console.log('Reclaim ' + name + ' success');
        // 空间回收完成重新请求数据
        this.refresh();
      } else {
        console.log('Reclaim ' + name + ' fail：' + result.description);
      }
      // 关闭回收空间页面
      this.reclaimShow = false;
    });
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
      this.changeServiceLevelForm.service_level_name = this.rowSelected[0].serviceLevelName;

      // 显示修改服务等级页面
      this.changeServiceLevelShow = true;
      // 初始化服务等级列表数据
      this.setServiceLevelList();
    }
  }
  // 变更服务等级 处理
  changeSLHandleFunc() {
    this.remoteSrv.changeServiceLevel(this.changeServiceLevelForm).subscribe((result: any) => {
      if (result.code === '0'){
        console.log('change service level success:' + name);
        // 空间回收完成重新请求数据
        this.refresh();
      } else {
        console.log('change service level faild: ' + name  + ' Reason:' + result.description);
      }
      // 关闭修改服务等级页面
      this.changeServiceLevelShow = false;
    });
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
      if (result.code === '0'){
        console.log('expand success:' + name);
      }else {
        console.log('expand: ' + name  + ' Reason:' + result.description);
      }
      // 隐藏扩容页面
      this.expandShow = false;
    });
  }
}
