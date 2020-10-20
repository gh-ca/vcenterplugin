import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ClusterList, GetForm, HostList, HostOrCluster, ServiceLevelList, VmfsListService} from '../list/list.service';
import {DataStore, MountService} from "./mount.service";
import {GlobalsService} from "../../../shared/globals.service";

@Component({
  selector: 'app-list',
  templateUrl: './mount.component.html',
  styleUrls: ['./mount.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [MountService],
})
export class MountComponent implements OnInit{

  constructor(private remoteSrv: MountService, private activatedRoute: ActivatedRoute, private cdr: ChangeDetectorRef,
              private router:Router, private globalsService: GlobalsService,) {

  }
  // dataStore数据
  dataStores: DataStore[] = [];
  // 选择DataStore
  chooseMountDataStore: DataStore[] = [];
  // mountForm:
  // 设备类型 host 主机、cluster 集群
  dataType;
  // 操作类型 mount挂载、unmount卸载
  operationType;
  // 数据加载
  isLoading = true;
  // 未选择挂载的DataStore
  notChooseMountDevice = false;
  // 未选择卸载的DataStore
  notChooseUnmountDevice = false;
  // 挂载的form
  mountForm;
  // 挂载模块展示/隐藏
  mountShow = false;
  // 卸载模块展示/隐藏
  unMountShow = false;
  // 扩容模块展示/隐藏
  expandShow = false;

  // 服务器/集群ID
  hostOrClusterId = 'urn:vmomi:HostSystem:host-1034:674908e5-ab21-4079-9cb1-596358ee5dd1';
  // 服务器/集群名称
  hostOrClusterName = '10.143.133.17';
  // objectid
  objectId;

  // 操作来源 list:列表页面、dataStore：在DataStore菜单页面操作、others:在主机或集群为入口
  resource;

  // 以列表或者dataStore为入口
  chooseHost: HostList; // 已选择的主机
  mountHostData = true; // 挂载页面主机是否加载完毕 true是 false否
  hostList: HostList[] = []; // 挂载页面 主机列表
  mountClusterData = true; // 挂载页面集群是否加载完毕 true是 false否
  clusterList: ClusterList[] = []; // 挂载页面集群列表
  chooseCluster: ClusterList; // 已选择的集群

  // 卸载
  unmountShow = false; // 卸载窗口
  unmountForm = new GetForm().getUnmountForm(); // 卸载form
  chooseUnmountHost: HostOrCluster = null; // 已选择卸载的主机
  chooseUnmountCluster: HostOrCluster = null; // 已选择卸载的集群
  mountedHost: HostOrCluster[] = []; // 已挂载的主机
  mountedCluster: HostOrCluster[] = []; // 已挂载的集群
  ngOnInit(): void {
    this.initData();
  }

  initData() {
    // 设备类型 操作类型初始化
    this.activatedRoute.url.subscribe(url => {
      console.log('url', url);
      this.dataType = url[0] + '';
      this.operationType = url[1] + '';
      this.activatedRoute.queryParams.subscribe(queryParam => {
        this.resource = queryParam.resource;
        if (this.resource !== 'others') {
          this.objectId = queryParam.objectId;
        }
      });

    });
    console.log('this.objectId', this.objectId);
    // 数据初始化
    this.getDataStore();
  }
  /**
   * 挂载：初始化dataStore
   */
  getDataStore() {
    // 初始化挂载form
    // 初始化dataStore
    this.dataStores = [];
    console.log('this.dataType', this.dataType === 'host');

    if (this.resource === 'others') { // 以主机/集群为入口
      if (this.operationType === 'mount') {
        this.mountDataStore();
      } else {
        this.unmountDataStore();
      }
    } else { // 以列表/dataStore为入口
      if (this.operationType === 'mount') {

        this.mountForm =  new GetForm().getMountForm();

        this.mountShow = true;
        // 初始化主机
        this.mountHostData = false;
        this.hostList = [];
        const hostNullInfo = {
          hostId: '',
          hostName: ''
        };
        this.hostList.push(hostNullInfo);
        this.initMountHost();

        // 初始化集群
        this.mountClusterData = false;
        this.clusterList = [];
        const clusterNullInfo = {
          clusterId: '',
          clusterName: ''
        };
        this.clusterList.push(clusterNullInfo);

        this.initMountCluster().then(res => {
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        });
      } else {
        // this.unmountDataStore();
      }
    }
  }

  /**
   * 挂载的DataStore
   */
  mountDataStore() {
    switch (this.dataType) {
      case 'host':
        // 设置主机相关参数
        this.mountForm.hostId = this.hostOrClusterId;
        this.mountForm.host = this.hostOrClusterName;

        // 获取dataStore
        this.remoteSrv.getDataStoreByHostId(this.hostOrClusterId, 'VMFS').subscribe((result: any) => {
          console.log('mountHostData:', result);
          if (result.code === '200' && null != result.data) {
            this.dataStores = result.data;
          }
          this.isLoading = false;
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        });
        break;
      case 'cluster':
        // 设置集群相关参数
        this.mountForm.clusterId = this.hostOrClusterId;
        this.mountForm.cluster = this.hostOrClusterName;

        // 获取dataStore
        this.remoteSrv.getDataStoreByClusterId(this.hostOrClusterId, 'VMFS').subscribe((result: any) => {
          console.log('mountHostData:', result);
          if (result.code === '200' && null != result.data) {
            this.dataStores = result.data;
          }
          this.isLoading = false;
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        });
        break;
      default:
        break;
    }
  }

  /**
   * 卸载的DataStore
   */
  unmountDataStore() {
    switch (this.dataType) {
      case 'host':
        this.isLoading = false;
        break;
      case 'cluster':
        this.isLoading = false;
        break;
      default:
        break;
    }
  }

  /**
   * 挂载Or卸载
   */
  mountOrUnmountFunc() {
    if (this.resource === 'others') {
      if (this.operationType === 'unmount') {
        this.unMountHandleFunc();
      } else {
        this.mountHandleFunc();
      }
    } else {
      if (this.operationType === 'unmount') {
        this.unmountHandleFunc();
      } else {
        this.mountSubmit();
      }
    }
    // 关闭窗口
    this.cancel();
  }

  /**
   * 取消/关闭函数
   */
  cancel() {
    if (this.resource === 'list') { // 主机/集群入口
      this.router.navigate(['vmfs/list']);
    } else { // dataStore/列表入口
      this.globalsService.getClientSdk().modal.close();
    }
  }

  /**
   * 主机/集群入口 挂载处理
   */
  mountHandleFunc() {
    if (this.chooseMountDataStore.length < 1) {
      this.notChooseMountDevice = true;
    } else {
      this.notChooseMountDevice = false;
      const dataStoreObjectIds = [];
      this.chooseMountDataStore.forEach(item => {
        dataStoreObjectIds.push(item.objectId);
      });
      this.mountForm.dataStoreObjectIds = dataStoreObjectIds;

      this.remoteSrv.mountVmfs(this.mountForm).subscribe((result: any) => {
        if (result.code  ===  '200'){
          console.log('挂载成功');
        } else {
          console.log('挂载异常：' + result.description);
        }
        // 隐藏挂载页面
        this.mountShow = false;
        this.cdr.detectChanges();
      });
    }
  }

  /**
   * 主机/集群入口 卸载处理
   */
  unMountHandleFunc() {
    if (this.chooseMountDataStore.length < 1) {
      this.notChooseUnmountDevice = true;
    } else {
      this.notChooseUnmountDevice = false;
    }
  }

  // 挂载 主机数据初始化
  initMountHost() {
    return new Promise((resolve, reject) => {
      // 获取服务器 通过ObjectId过滤已挂载的服务器
      this.remoteSrv.getHostListByObjectId(this.objectId).subscribe((result: any) => {
        if (result.code === '200' && result.data !== null){
          result.data.forEach(item => {
            this.hostList.push(item);
          });
        }
        this.chooseHost =  this.hostList[0];
        this.mountHostData = true;
        this.cdr.detectChanges();
      });
    });
  }
  // 挂载  集群数据初始化
  initMountCluster() {
    return new Promise((resolve, reject) => {
      // 获取集群 通过ObjectId过滤已挂载的集群
      this.remoteSrv.getClusterListByObjectId(this.objectId).subscribe((result: any) => {
        if (result.code === '200'){
          result.data.forEach(item => {
            this.clusterList.push(item);
          });
        }
        this.chooseCluster = this.clusterList[0];
        this.mountClusterData = true;
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
    const objectIds = [];
    objectIds.push(this.objectId);
    this.mountForm.dataStoreObjectIds = objectIds;
    this.remoteSrv.mountVmfs(this.mountForm).subscribe((result: any) => {
      if (result.code  ===  '200'){
        console.log('挂载成功');
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
    console.log('this.chooseUnmountHost', this.chooseUnmountHost);
    console.log('this.chooseUnmountCluster', this.chooseUnmountCluster);
    console.log('this.flag', (!this.chooseUnmountHost && this.unmountForm.mountType === '1') || (!this.chooseUnmountCluster && this.unmountForm.mountType === '2'));
    if ((!this.chooseUnmountHost && this.unmountForm.mountType === '1') || (!this.chooseUnmountCluster && this.unmountForm.mountType === '2')) {
      this.notChooseUnmountDevice = true;
    } else {
      this.unmountForm.dataStoreObjectIds.push(this.objectId);
      if (this.unmountForm.mountType === '1') {
        this.unmountForm.hostId = this.chooseUnmountHost.deviceId;
      } else {
        this.unmountForm.hostGroupId = this.chooseUnmountCluster.deviceId;
      }
      console.log('this.unmountForm', this.unmountForm);
      this.notChooseUnmountDevice = false;
      this.remoteSrv.unmountVMFS(this.unmountForm).subscribe((result: any) => {

        if (result.code === '200'){
          console.log('unmount  success');
        } else {
          console.log('unmount  fail：' + result.description);
        }
        // 关闭卸载页面
        this.unmountShow = false;
        this.cdr.detectChanges();
      });
    }
  }
}
