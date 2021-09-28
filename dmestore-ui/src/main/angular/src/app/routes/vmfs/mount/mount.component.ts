import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {
  ClusterList,
  ConnFaildData,
  GetForm,
  HostList,
  HostOrCluster,
  ServiceLevelList,
  VmfsInfo,
  VmfsListService,
} from '../list/list.service';
import { DataStore, MountService } from './mount.service';
import { GlobalsService } from '../../../shared/globals.service';
import { isMockData, mockData } from '../../../../mock/mock';
import { FormGroup, FormControl } from '@angular/forms';
import { AddService } from './../add/add.service';
import { CommonService } from './../../common.service';
import { TranslatePipe,TranslateService } from '@ngx-translate/core';
import UI_TREE_CHILDREN_BY_OBJECT_IDS from 'mock/UI_TREE_CHILDREN_BY_OBJECT_IDS';
import { vmfsClusterTreeData } from './../../../../mock/vmfsClusterTree';
import { CustomValidatorFaild } from 'app/app.helpers';
import { getLodash } from '@shared/lib';
import {ClrDatagridComparatorInterface} from "@clr/angular";
const _ = getLodash();

@Component({
  selector: 'app-list',
  templateUrl: './mount.component.html',
  styleUrls: ['./mount.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [MountService, AddService, CommonService, TranslatePipe],
})
export class MountComponent implements OnInit {
  constructor(
    private commonService: CommonService,
    // private remoteService: AddService,
    private remoteSrv: MountService,
    private activatedRoute: ActivatedRoute,
    private cdr: ChangeDetectorRef,
    private router: Router,
    private globalsService: GlobalsService,
    private translateService:TranslateService
  ) {
    this.chooseDevice = [];
    this.deviceList = []; // 主机AND集群
  }
  public capacityComparator = new CapacityComparator();
  public freeSpaceComparator=new FreeSpaceComparator()
  deviceList: HostOrCluster[]; // 主机AND集群
  deviceList_list: HostOrCluster[]; // 单主机
  deviceForm;

  checkNullData=false
  //当前挂载类型：主机或集群
  selectMountType;

  /* 选择的集群主机 */
  chooseDevice;
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
  mountForm = new GetForm().getMountForm();
  // 以list/DataStore 为入口 挂载展示/隐藏
  mountShow = false;

  // 以主机集群为入口的挂载/卸载
  hostMountShow = false;

  // 服务器/集群ID
  hostOrClusterId;
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
  mountFailHost:[]; //部分成功时挂载失败的主机

  // 卸载
  unmountShow = false; // 卸载窗口
  unmountForm = new GetForm().getUnmountForm(); // 卸载form
  chooseUnmountHost: HostOrCluster = null; // 已选择卸载的主机
  chooseUnmountCluster: HostOrCluster = null; // 已选择卸载的集群
  mountedHost: HostOrCluster[] = []; // 已挂载的主机
  mountedCluster: HostOrCluster[] = []; // 已挂载的集群
  mountSuccessShow = false; // 挂载成功窗口
  unmountSuccessShow = false; // 卸载窗口
  unmountTipsShow = false; // 卸载删除提示
  // vmfs数据
  vmfsInfo = {
    name: '',
  };

  modalLoading = false; // 数据加载loading
  modalHandleLoading = false; // 数据处理loading
  isOperationErr = false; // 挂载错误信息
  isMountPartSuccess=false; //挂载部分成功
  isUnmountOperationErr = false; // 卸载错误信息

  connectivityFailure = false; // 主机联通性测试失败
  connFailData: ConnFaildData[]; //  主机联通性测试失败数据
  showDetail = false; // 展示主机联通异常数据

  //当前语言环境 CN为中文  EN为英文
  language:string;
  unmountDesc:string;//卸载失败返回信息
  mountFailOrPartSuccessDesc:string;//挂载失败和部分成功描述

  //失败提示窗口与部分成功提示窗口
  errorShow=false;
  partSuccessShow=false;
  status;
  description;
  operatingType;
  partSuccessData;

  ngOnInit(): void {
    // 初始化隐藏窗口
    this.unmountShow = false;
    this.mountShow = false;
    this.hostMountShow = false;
    this.modalLoading = true;
    this.modalHandleLoading = false;
    this.isOperationErr = false;
    this.isMountPartSuccess=false;
    this.initData();

  }

 async initData() {
    // 设备类型 操作类型初始化
    this.activatedRoute.url.subscribe(url => {
      console.log('url', url);
      if (url.length > 1) {
        this.dataType = url[0] + '';
        this.operationType = url[1] + '';
      } else {
        this.operationType = url[0] + '';
      }
      this.activatedRoute.queryParams.subscribe( queryParam => {
        this.resource = queryParam.resource;
        const ctx = this.globalsService.getClientSdk().app.getContextObjects();
        if (this.resource !== 'others') {
          // 以列表/dataStore 为入口
          if (this.resource === 'list') {
            // 以列表为入口
            this.objectId = queryParam.objectId;
          } else {
            // 以dataStore为入口
            this.objectId = ctx
              ? ctx[0].id
              : 'urn:vmomi:Datastore:datastore-10058:674908e5-ab21-4079-9cb1-596358ee5dd1';
          }
          if (this.operationType === 'mount') {
            this.mountShow = true;
          } else {
            this.unmountShow = true;
          }

          // 获取vmfs数据
          this.remoteSrv.getVmfsById(this.objectId).subscribe((result: any) => {
            if (result.code === '200' && null != result.data) {
              this.vmfsInfo = result.data.filter(item => item.objectid === this.objectId)[0];
            }
            if (isMockData) {
              this.vmfsInfo = { name: 'name_20210426101611_20210426101613_20210426101615' };
            }
            console.log('this.vmfsInfo ', this.vmfsInfo);

            this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
          });
        } else {
          /* this.resource:others */
          // 以集群为入口
          this.hostOrClusterId = ctx
            ? ctx[0].id
            : 'urn:vmomi:HostSystem:host-1034:674908e5-ab21-4079-9cb1-596358ee5dd1';
          this.hostMountShow = true;
          console.log('this.hostMountShow', this.hostMountShow);
        }

        this.cdr.detectChanges();
      });
    });
    console.log('this.objectId', this.objectId);
    console.log('this.hostOrClusterId', this.hostOrClusterId);
    this.selectMountType=await this.commonService.getMountTypeBySeletedId(this.objectId)
   console.log(this.selectMountType)
    // 数据初始化
    this.getDataStore();
   this.cdr.detectChanges();
  }

  /**
   * 挂载：初始化dataStore
   */
  async getDataStore() {
    // 初始化挂载form
    // 初始化dataStore
    this.dataStores = [];
    console.log(this.dataType, 'this.dataType', this.dataType === 'host');
    console.log(this.operationType, 'this.operationType', this.operationType === 'unmount');
    console.log(this.resource, 'this.resource', this.resource === 'others');

    // 初始化挂载/卸载 form
    if (this.operationType === 'mount') {
      this.mountForm = new GetForm().getMountForm();
      if (this.dataType === 'host') {
        // 主机
        this.mountForm.mountType = '1';
      } else {
        // 集群
        this.mountForm.mountType = '2';
      }
    } else {
      this.unmountForm = new GetForm().getUnmountForm();
      if (this.dataType === 'host') {
        // 主机
        this.unmountForm.mountType = '1';
      } else {
        // 集群
        this.unmountForm.mountType = '2';
      }
    }

    this.initDeviceForm();
    // 挂载、卸载 数据初始化
    if (this.resource === 'others') {
      // 以主机/集群为入口
      /* 以列表table的形式展示，逻辑不动 */
      if (this.operationType === 'mount') {
        this.mountDataStore();
      } else {
        this.unmountDataStore();
      }
    } else {
      // 以列表/dataStore为入口
      if (this.operationType === 'mount') {
        // 挂载

        // 初始化主机
        this.mountHostData = false;
        this.hostList = [];

        this.chooseCluster = undefined;
        this.chooseHost = undefined;
        this.deviceList = await this.commonService.remoteGetVmfsDeviceListById_mount(
          this.objectId
        );

        const loadMountHost = () => {
          const setDeviceList = (result: any) => {
            if (result.code === '200' && result.data !== null) {
              this.deviceList_list = _.map(result.data, item => {
                return {
                  clusterId: item.hostId,
                  clusterName: item.hostName,
                  deviceType: 'host',
                };
              });
            }
            this.cdr.detectChanges();
          };

          if (isMockData) {
            setDeviceList(mockData.ACCESSVMWARE_LISTHOST);
          } else {
            this.remoteSrv.getHostListByObjectId(this.objectId).subscribe(setDeviceList);
          }
        };
        loadMountHost();

        // this.initMountHost();
      } else {
        // 卸载

        this.isLoading = true;
        console.log('this.unmountShow', this.unmountShow);
        // 初始化卸载 页面未选择设备 提示数据展示
        this.notChooseUnmountDevice = false;
        // 初始话已选择数据
        this.chooseUnmountCluster = null;
        this.chooseUnmountHost = null;

        let isShowHostList = false;
        console.log('isShowHostList', isShowHostList);
        this.deviceList = await this.commonService.remoteGetVmfsDeviceListById_unmount(this.objectId);
        if(this.deviceList.length===0){
          this.checkNullData=true
        }

        const loadUnmountHost = () => {
          const setDeviceList = (result: any) => {
            if (result.code === '200' && result.data !== null) {
              this.deviceList_list = _.map(result.data, item => {
                return {
                  clusterId: item.hostId,
                  clusterName: item.hostName,
                  deviceType: 'host',
                };
              });
            }
            this.cdr.detectChanges();
          };

          if (isMockData) {
            setDeviceList(mockData.ACCESSVMWARE_LISTHOST);
          } else {
            /* 已挂在 */
            this.remoteSrv.getMountHost(this.objectId).subscribe(setDeviceList);
          }
        };
        loadUnmountHost();

        /* 2：处理获取集群的数据 */
        /*
          const handlerGetMountClusterSuccess = (result: any) => {
          console.log(result);
          if (result.code === '200' && result.data !== null && result.data.length >= 1) {
            this.unmountForm.mountType = '2';
            const mountCluster: HostOrCluster[] = [];
            result.data.forEach(item => {
              const hostInfo = {
                deviceId: item.hostGroupId,
                deviceName: item.hostGroupName,
                deviceType: 'cluster',
              };
              mountCluster.push(hostInfo);
            });
            this.mountedCluster = mountCluster;
          }
          this.modalLoading = false;
          this.isLoading = false;
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        }; */

        /* 处理获取主机的数据 */
        /*  const handlerGetMountHost = (result: any) => {
          console.log(result);
          if (result.code === '200' && result.data !== null && result.data.length >= 1) {
            this.unmountForm.mountType = '1';
            const mountHost: HostOrCluster[] = [];
            result.data.forEach(item => {
              const hostInfo = {
                deviceId: item.hostId,
                deviceName: item.hostName,
                deviceType: 'host',
              };
              mountHost.push(hostInfo);
            });
            this.mountedHost = mountHost;
          }
          // 获取集群
          if (isMockData) {
            handlerGetMountClusterSuccess(mockData.ACCESSVMFS_GETHOSTGROUPSBYSTORAGEID);
          } else {
            this.remoteSrv.getMountCluster(this.objectId).subscribe(handlerGetMountClusterSuccess);
          }
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        }; */

        /*  */
        /*TODO:*/
        /* if (isMockData) {
          handlerGetMountHost(mockData.ACCESSVMFS_GETHOSTSBYSTORAGEID);
        } else {
          // 获取主机
          // const lqHostgroupId = `urn:vmomi:Datastore:datastore-14029:674908e5-ab21-4079-9cb1-596358ee5dd1`;
          // this.remoteSrv.getMountHost(lqHostgroupId).subscribe(handlerGetMountHost);
          this.remoteSrv.getMountHost(this.objectId).subscribe(handlerGetMountHost);
        } */
      }
      this.modalLoading = false;
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    }
  }

  /**
   * 主机/集群入口 挂载数初始化
   */
  mountDataStore() {
    /*  */
    switch (this.dataType) {
      case 'host':
        // 设置主机相关参数
        this.mountForm.hostId = this.hostOrClusterId;

        // 获取dataStore
        this.remoteSrv
          .getDataStoreByHostId(this.hostOrClusterId, 'VMFS')
          .subscribe((result: any) => {
            console.log('mountHostData:', result);
            if (result.code === '200' && null != result.data) {
              this.dataStores = result.data;
            }
            this.isLoading = false;
            this.modalLoading = false;
            this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
          });
        break;
      case 'cluster':
        // 设置集群相关参数
        this.mountForm.clusterId = this.hostOrClusterId;

        // 获取dataStore
        this.remoteSrv
          .getDataStoreByClusterId(this.hostOrClusterId, 'VMFS')
          .subscribe((result: any) => {
            console.log('mountHostData:', result);
            if (result.code === '200' && null != result.data) {
              this.dataStores = result.data;
            }
            this.isLoading = false;
            this.modalLoading = false;
            this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
          });
        break;
      default:
        break;
    }
  }

  /**
   * 主机/集群入口 卸载数据初始化
   */
  unmountDataStore() {
    switch (this.dataType) {
      case 'host':
        this.isLoading = false;
        // 获取dataStore
        this.remoteSrv
          .getMountedByHostObjId(this.hostOrClusterId, 'VMFS')
          .subscribe((result: any) => {
            console.log('mountedHostData:', result);
            if (result.code === '200' && null != result.data) {
              this.dataStores = result.data;
            }
            this.isLoading = false;
            this.modalLoading = false;
            this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
          });
        break;
      case 'cluster':
        this.isLoading = false;
        this.remoteSrv
          .getMountedByClusterObjId(this.hostOrClusterId, 'VMFS')
          .subscribe((result: any) => {
            console.log('mountedClusterData:', result);
            if (result.code === '200' && null != result.data) {
              this.dataStores = result.data;
            }
            this.isLoading = false;
            this.modalLoading = false;
            this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
          });
        break;
      default:
        break;
    }
  }


  isDisableMountSubmit() {
    return this.chooseDevice?.length===0
  }

    // 卸载确认
    unMountConfirm() {
      if (this.isDisableMountSubmit()) return;
      this.unmountTipsShow = true;
  }

  /**
   * 表单提交（挂载/卸载）
   */
  mountOrUnmountFunc(isReturn=false) {
    if (isReturn) return;
    if (this.resource === 'others') {
      /* 列表保持原样 */
      if (this.operationType === 'unmount') {
        this.unMountHandleFunc_others();
      } else {
        console.log('开始挂载');
        this.mountHandleFunc_others();
      }
    } else {
      /*  */
      if (this.operationType === 'unmount') {
        this.unmountHandleFunc();
      } else {
        this.mountSubmit();
      }
    }
  }

  /**
   * 取消/关闭函数
   */
  cancel() {
    console.log('关闭页面');
    // dataStore/列表入口 窗口隐藏
    this.hostMountShow = false;
    this.mountShow = false;
    this.unmountShow = false;
    if (this.resource !== 'others') {
      if (this.operationType === 'mount') {
        this.mountShow = false;
      } else {
        this.unmountShow = false;
      }
    }
    // 父窗口关闭
    if (this.resource === 'list') {
      // 主机/集群入口
      this.router.navigate(['vmfs/list']);
    } else {
      // dataStore/列表入口
      this.globalsService.getClientSdk().modal.close();
    }
  }

  /**
   * 主机/集群入口 挂载处理
   */
  mountHandleFunc_others() {
    if (this.chooseMountDataStore.length < 1) {
      this.notChooseMountDevice = true;
    } else {
      this.notChooseMountDevice = false;
      const dataStoreObjectIds = [];
      this.chooseMountDataStore.forEach(item => {
        dataStoreObjectIds.push(item.objectId);
      });
      this.mountForm.dataStoreObjectIds = dataStoreObjectIds;

      this.modalHandleLoading = true;
      console.log('开始挂载。。。。');
      this.remoteSrv.mountVmfs(this.mountForm).subscribe((result: any) => {
        this.modalHandleLoading = false;
        console.log('result:', result);
        if (result.code === '200') {
          console.log('挂载成功');
          this.mountShow=false
          this.mountSuccessShow = true;
        } else if (result.code === '-60001') {
          this.connectivityFailure = true;
          this.showDetail = false;
          const connFailDatas: ConnFaildData[] = [];
          if (result.data) {
            result.data.forEach(item => {
              for (let key in item) {
                const conFailData = {
                  hostName: key,
                  description: item[key],
                };
                connFailDatas.push(conFailData);
              }
            });
            this.connFailData = connFailDatas;
          }
        }else if (result.code==='206'){
          console.log("挂载部分成功："+result.description)
          this.isMountPartSuccess=true
          this.mountFailHost=result.data
        } else {
          console.log('挂载异常：' + result.description);
          // this. isOperationErr = true;
          // this.mountFailOrPartSuccessDesc=result.description
          this.mountShow=false
          this.errorShow=true
          this.status='error'
          this.operatingType='vmfsMount'
          this.description=result.description
        }
        this.cdr.detectChanges();
      });
    }
  }

  /**
   * 主机/集群入口 卸载处理
   */
  unMountHandleFunc_others() {
    if (this.chooseMountDataStore.length < 1) {
      this.notChooseUnmountDevice = true;
    } else {
      this.notChooseUnmountDevice = false;

      if (this.unmountForm.mountType === '1') {
        this.unmountForm.hostId = this.hostOrClusterId;
        this.language=this.translateService.currentLang==='en-US'?'EN':'CN'
        const unmountObjIds = this.chooseMountDataStore.map(item => item.objectId);
        this.unmountForm.dataStoreObjectIds = unmountObjIds;
        this.modalHandleLoading = true;
        console.log(this.unmountForm)
        this.remoteSrv.unmountVMFS(_.merge(this.unmountForm,{ hostIds: [this.unmountForm.hostId]},{language:this.language})).subscribe((result: any) => {
          this.modalHandleLoading = false;
          if (result.code === '200') {
            // console.log('unmount ' + this.rowSelected[0].name + ' success');
            // 关闭卸载页面
            this.unmountShow = false;
            // 重新请求数据
            // this.scanDataStore();
            // 打开成功提示窗口
            this.unmountSuccessShow = true;
          }else if(result.code==='206'){
            let dmeError=[]
            let vcError=[]
            let bounded=[]
            if(result.data.dmeError&&result.data.dmeError.length>0){
              dmeError=this.unmountPartHandleFun(result.data.dmeError)
            }
            if(result.data.vcError&&result.data.vcError.length>0){
              vcError=result.data.vcError
            }
            if(result.data.bounded&&result.data.bounded.length>0){
              bounded=result.data.bounded
            }
            this.partSuccessData=this.unmountPartDataHandleFun(dmeError.concat(vcError).concat(bounded))
            this.description=result.description
            this.unmountShow=false
            this.status='partSuccess'
            this.operatingType='vmfsUnmount'
            this.partSuccessShow=true
          } else {
            // console.log('unmount ' + this.rowSelected[0].name + ' fail：' + result.description);
            if(result.data&&Object.keys(result.data).length>0){
              let dmeError=[]
              let vcError=[]
              let bounded=[]
              if(result.data.dmeError&&result.data.dmeError.length>0){
                dmeError=this.unmountPartHandleFun(result.data.dmeError)
              }
              if(result.data.vcError&&result.data.vcError.length>0){
                vcError=result.data.vcError
              }
              if(result.data.bounded&&result.data.bounded.length>0){
                bounded=result.data.bounded
              }
              this.unmountShow=false
              this.description=result.description
              this.status='partSuccess'
              this.operatingType='vmfsUnmountError'
              this.partSuccessData=this.unmountPartDataHandleFun(dmeError.concat(vcError).concat(bounded))
              this.partSuccessShow=true
            }else {
              this.unmountDesc=result.description
              // this.isOperationErr = true;
              this.unmountShow = false;
              this.status='error'
              this.partSuccessData=[]
              this.description=result.description
              this.operatingType='vmfsUnmount'
              this.errorShow=true
            }
          }
          this.cdr.detectChanges();
        });
      } else {
        this.unmountForm.clusterId = this.hostOrClusterId;
        this.language=this.translateService.currentLang==='en-US'?'EN':'CN'
        const unmountObjIds = this.chooseMountDataStore.map(item => item.objectId);
        this.unmountForm.dataStoreObjectIds = unmountObjIds;
        this.modalHandleLoading = true;
        console.log(this.unmountForm)
        this.remoteSrv.unmountVMFS(_.merge(this.unmountForm,{language:this.language})).subscribe((result: any) => {
          this.modalHandleLoading = false;
          if (result.code === '200') {
            // console.log('unmount ' + this.rowSelected[0].name + ' success');
            // 关闭卸载页面
            this.unmountShow = false;
            // 重新请求数据
            // this.scanDataStore();
            // 打开成功提示窗口
            this.unmountSuccessShow = true;
          }else if(result.code==='206'){
            let dmeError=[]
            let vcError=[]
            let bounded=[]
            if(result.data.dmeError&&result.data.dmeError.length>0){
              dmeError=this.unmountPartHandleFun(result.data.dmeError)
            }
            if(result.data.vcError&&result.data.vcError.length>0){
              vcError=result.data.vcError
            }
            if(result.data.bounded&&result.data.bounded.length>0){
              bounded=result.data.bounded
            }
            this.partSuccessData=this.unmountPartDataHandleFun(dmeError.concat(vcError).concat(bounded))
            this.description=result.description
            this.unmountShow=false
            this.status='partSuccess'
            this.operatingType='vmfsUnmount'
            this.partSuccessShow=true
          } else {
            // console.log('unmount ' + this.rowSelected[0].name + ' fail：' + result.description);
            if(result.data&&Object.keys(result.data).length>0){
              let dmeError=[]
              let vcError=[]
              let bounded=[]
              if(result.data.dmeError&&result.data.dmeError.length>0){
                dmeError=this.unmountPartHandleFun(result.data.dmeError)
              }
              if(result.data.vcError&&result.data.vcError.length>0){
                vcError=result.data.vcError
              }
              if(result.data.bounded&&result.data.bounded.length>0){
                bounded=result.data.bounded
              }
              this.unmountShow=false
              this.description=result.description
              this.status='partSuccess'
              this.operatingType='vmfsUnmountError'
              this.partSuccessData=this.unmountPartDataHandleFun(dmeError.concat(vcError).concat(bounded))
              this.partSuccessShow=true
            }else {
              this.unmountDesc=result.description
              // this.isOperationErr = true;
              this.unmountShow = false;
              this.status='error'
              this.description=result.description
              this.operatingType='vmfsUnmount'
              this.errorShow=true
              this.partSuccessData=[]
            }

          }
          this.cdr.detectChanges();
        });
      }

    }
  }

  handleChooseDeviceChange(prop: string, val: any) {
    this[prop] = val;
    this.deviceForm.patchValue({ [prop]: val });
  }

  /**
   * @Description 树形集群主机 初始化，校验规则
   * @date 2021-05-17
   * @returns {any}
   */
  initDeviceForm() {
    this.deviceForm = new FormGroup({
      chooseDevice: new FormControl(
        [],
        CustomValidatorFaild(value => {
          const isValid = _.isArray(value) && value.length > 0;
          return !isValid;
        })
      ),
    });
  }

  // 挂载 主机数据初始化
  initMountHost() {
    return new Promise((resolve, reject) => {
      // 获取服务器 通过ObjectId过滤已挂载的服务器
      this.remoteSrv.getHostListByObjectId(this.objectId).subscribe((result: any) => {
        if (result.code === '200' && result.data !== null) {
          result.data.forEach(item => {
            this.hostList.push(item);
          });
        }

        this.mountHostData = true;

        // 初始化集群
        this.mountClusterData = false;
        this.clusterList = [];

        this.initMountCluster().then(res => {
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        });

        this.cdr.detectChanges();
      });
    });
  }

  // 挂载  集群数据初始化
  initMountCluster() {
    return new Promise((resolve, reject) => {
      // 获取集群 通过ObjectId过滤已挂载的集群
      this.remoteSrv.getClusterListByObjectId(this.objectId).subscribe((result: any) => {
        if (result.code === '200' && result.data !== null) {
          result.data.forEach(item => {
            this.clusterList.push(item);
          });
        }
        this.modalLoading = false;
        this.mountClusterData = true;
        this.cdr.detectChanges();
      });
    });
  }

  // 挂载提交
  async mountSubmit() {
    // 数据封装
    if (this.mountForm.mountType === '1') {
      // 服务器
      this.mountForm.hostId = this.chooseHost?.hostId;
      this.mountForm.host = this.chooseHost?.hostName;
    } else if (this.mountForm.mountType === '2') {
      // 集群
      this.mountForm.cluster = this.chooseCluster?.clusterName;
      this.mountForm.clusterId = this.chooseCluster?.clusterId;
    }
    const objectIds = [];
    objectIds.push(this.objectId);
    this.mountForm.dataStoreObjectIds = objectIds;

    this.modalHandleLoading = true;

    const params = _.merge({ chooseDevice: this.chooseDevice }, this.mountForm);
    const handlerMountVmfsSuccess = (result: any) => {
      this.modalHandleLoading = false;
      if (result.code === '200') {
        this.mountShow=false
        console.log('挂载成功');
        this.mountSuccessShow = true;
      } else if (result.code === '-60001') {
        this.connectivityFailure = true;
        this.showDetail = false;
        const connFailDatas: ConnFaildData[] = [];
        if (result.data) {
          result.data.forEach(item => {
            for (let key in item) {
              const conFailData = {
                hostName: key,
                description: item[key],
              };
              connFailDatas.push(conFailData);
            }
          });
          this.connFailData = connFailDatas;
        }
      }else if (result.code==='206'){
        // console.log("挂载部分成功："+result.description)
        // this.isMountPartSuccess=true
        // this.mountFailOrPartSuccessDesc=result.description
        // this.mountFailHost=result.data
        this.mountShow=false
        this.status='partSuccess'
        this.description=result.description
        this.partSuccessData=result.data
        this.operatingType='vmfsMount'
        this.partSuccessShow=true
      } else {
        // console.log('挂载异常：' + result.description);
        // this.mountFailOrPartSuccessDesc=result.description
        // this.isOperationErr = true;
        this.mountShow=false
        this.status='error'
        this.description=result.description
        this.operatingType='vmfsMount'
        this.partSuccessData=result.data
        this.errorShow=true
      }
      this.cdr.detectChanges();
    };
    const res = await this.commonService.remoteVmfs_Mount(params);

    handlerMountVmfsSuccess(res);
    // this.remoteSrv.mountVmfs().subscribe(handlerMountVmfsSuccess);
  }

  // 卸载处理函数
   unmountHandleFunc() {
    console.log('this.chooseUnmountHost', this.chooseUnmountHost);
    console.log('this.chooseUnmountCluster', this.chooseUnmountCluster);
    console.log(
      'this.flag',
      (!this.chooseUnmountHost && this.unmountForm.mountType === '1') ||
        (!this.chooseUnmountCluster && this.unmountForm.mountType === '2')
    );
    // if (
    //   (!this.chooseUnmountHost && this.unmountForm?.mountType === '1') ||
    //   (!this.chooseUnmountCluster && this.unmountForm?.mountType === '2')
    // ) {
    //   this.notChooseUnmountDevice = true;
    // } else {
    const submit=()=>{
      this.language=this.translateService.currentLang==='en-US'?'EN':'CN'
      this.unmountForm.dataStoreObjectIds.push(this.objectId);
      if (this.unmountForm.mountType === '1') {
        this.unmountForm.hostId = this.chooseUnmountHost?.deviceId;
      } else {
        this.unmountForm.clusterId = this.chooseUnmountCluster?.deviceId;
      }
      console.log('this.unmountForm', this.unmountForm);
      this.notChooseUnmountDevice = false;

      this.modalHandleLoading = true;
      const handlerUnmountVmfsSuccess = (result: any) => {
        this.modalHandleLoading = false;
        if (result.code === '200') {
          // console.log('unmount ' + this.rowSelected[0].name + ' success');
          // 关闭卸载页面
          this.unmountShow = false;
          // 重新请求数据
          // this.scanDataStore();
          // 打开成功提示窗口
          this.unmountSuccessShow = true;
        }else if(result.code==='206'){
          let dmeError=[]
          let vcError=[]
          let bounded=[]
          if(result.data.dmeError&&result.data.dmeError.length>0){
            dmeError=this.unmountPartHandleFun(result.data.dmeError)
          }
          if(result.data.vcError&&result.data.vcError.length>0){
            vcError=result.data.vcError
          }
          if(result.data.bounded&&result.data.bounded.length>0){
            bounded=result.data.bounded
          }
          this.partSuccessData=this.unmountPartDataHandleFun(dmeError.concat(vcError).concat(bounded))
          this.description=result.description
          this.unmountShow=false
          this.status='partSuccess'
          this.operatingType='vmfsUnmount'
          this.partSuccessShow=true
        } else {
          // console.log('unmount ' + this.rowSelected[0].name + ' fail：' + result.description);
          if(result.data&&Object.keys(result.data).length>0){
            let dmeError=[]
            let vcError=[]
            let bounded=[]
            if(result.data.dmeError&&result.data.dmeError.length>0){
              dmeError=this.unmountPartHandleFun(result.data.dmeError)
            }
            if(result.data.vcError&&result.data.vcError.length>0){
              vcError=result.data.vcError
            }
            if(result.data.bounded&&result.data.bounded.length>0){
              bounded=result.data.bounded
            }
            this.unmountShow=false
            this.description=result.description
            this.status='partSuccess'
            this.operatingType='vmfsUnmountError'
            this.partSuccessData=this.unmountPartDataHandleFun(dmeError.concat(vcError).concat(bounded))
            this.partSuccessShow=true
          }else {
            this.unmountDesc=result.description
            // this.isOperationErr = true;
            this.unmountShow = false;
            this.status='error'
            this.description=result.description
            this.operatingType='vmfsUnmount'
            this.errorShow=true
            this.partSuccessData=[]
          }

        }
        this.cdr.detectChanges();
      };
      console.log('chooseDevice',this.chooseDevice)
      console.log('unmountForm',this.unmountForm)
      const params = _.merge(this.unmountForm,{ hostIds: this.chooseDevice.map(i=>i.deviceId) },{language:this.language});
      // const res = await this.commonService.remoteVmfs_Unmount(params);
      // handlerUnmountVmfsSuccess(res);
      console.log(params)
      this.remoteSrv.unmountVMFS(params).subscribe(handlerUnmountVmfsSuccess);
    }
    if(this.chooseDevice.length>0){
      submit()
    }
  }

  //卸载部分成功返回数据处理
  unmountPartHandleFun(arr:any){
    let obj=arr[0]
    let str=Object.keys(obj)[0]
    let arr1=str.split(",")
    let arr2=[]
    for(let item of arr1){
      item = item.trim()
      let temp = {}
      temp[item] = Object.values(obj)[0]
      arr2.push(temp)
    }
    return arr2
  }
  //卸载部分成功数据处理总和
  unmountPartDataHandleFun(arr:any){
    let temp = []
    for (let item of arr) {
      let obj = {}
      let tempStr1 = Object.values(item)
      let str = tempStr1.toString().trim()
      let tempStr2 = Object.keys(item)
      let str1 = tempStr2.toString().trim()
      // console.log(Object.keys(item));
      // console.log(Object.values(item));
      obj["key"] = str1
      obj["value"] = str
      temp.push(obj)
    }
    return temp
  }
  /**
   * 容量格式化
   * @param c 容量值
   * @param isGB true GB、false MB
   */
  formatCapacity(c: number, isGB: boolean) {
    c = Number(c);
    let cNum;
    if (c < 1024) {
      cNum = isGB ? c.toFixed(3) + 'GB' : c.toFixed(3) + 'MB';
    } else if (c >= 1024 && c < 1048576) {
      cNum = isGB ? (c / 1024).toFixed(3) + 'TB' : (c / 1024).toFixed(3) + 'GB';
    } else if (c >= 1048576) {
      cNum = isGB ? (c / 1024 / 1024).toFixed(3) + 'PB' : (c / 1024 / 1024).toFixed(3) + 'TB';
    }
    return cNum;
  }

  /**
   * 确认操作结果并关闭窗口
   */
  confirmActResult() {
    this.cancel();
  }

  sortFunc(obj: any) {
    return !obj;
  }
}
class CapacityComparator implements ClrDatagridComparatorInterface<DataStore>{
  compare(a: DataStore, b: DataStore) {
    return a.capacity - b.capacity;
  }
}
class FreeSpaceComparator implements ClrDatagridComparatorInterface<DataStore>{
  compare(a: DataStore, b: DataStore) {
    return a.freeSpace - b.freeSpace;
  }
}
