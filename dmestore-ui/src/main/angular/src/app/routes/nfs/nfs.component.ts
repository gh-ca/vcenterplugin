import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  Inject,
  OnInit,
  Optional,
  ViewChild,
} from '@angular/core';
import { Host, List, NfsService, UpdateNfs, URLS_NFS } from './nfs.service';
import { GlobalsService } from '../../shared/globals.service';
import { LogicPort, StorageList, StorageService } from '../storage/storage.service';
import { StoragePool, StoragePoolMap } from '../storage/detail/detail.service';
import { ClrDatagridSortOrder, ClrWizard, ClrWizardPage } from '@clr/angular';
import { VmfsListService } from '../vmfs/list/list.service';
import { Router } from '@angular/router';
import { AddNfs, NfsAddService, Vmkernel } from './subpages/add/nfs-add.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { TokenService } from '@core';
import { DOCUMENT } from '@angular/common';
import { isMockData, mockData } from './../../../mock/mock';
import { getColorByType, getLabelByValue, getQosCheckTipsTagInfo } from './../../app.helpers';
import { handlerResponseErrorSimple } from 'app/app.helpers';
import { SimpleChange } from '@angular/core';
import debounce from 'just-debounce';
import { NfsComponentCommon } from './NfsComponentCommon';

@Component({
  selector: 'app-nfs',
  templateUrl: './nfs.component.html',
  styleUrls: ['./nfs.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [NfsService, StorageService, VmfsListService, NfsAddService],
})
export class NfsComponent extends NfsComponentCommon implements OnInit {
  getColor;
  getLabelByValue;
  isAddPageOneNextDisabled: boolean;
  /*  */

  descSort = ClrDatagridSortOrder.DESC;
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
  unit = 'GB';
  hostList: Host[] = [];
  addForm = new AddNfs();
  addFormGroup = new FormGroup({
    storagId: new FormControl('', Validators.required),
    storagePoolId: new FormControl(true, Validators.required),
    currentPortId: new FormControl('', Validators.required),
    nfsName: new FormControl('', Validators.required),
    size: new FormControl('', Validators.required),
    hostObjectId: new FormControl('', Validators.required),
    vkernelIp: new FormControl('', Validators.required),
    accessMode: new FormControl('', Validators.required),
    sameName: new FormControl(true, Validators.required),
    fsName: new FormControl('', Validators.required),
    shareName: new FormControl('', Validators.required),
    type: new FormControl(''),
    securityType: new FormControl(false),
    unit: new FormControl('GB', Validators.required),
    characterEncoding: new FormControl('utf-8', Validators.required),
  });
  storageList: StorageList[] = [];
  storagePools: StoragePool[] = [];
  storagePoolMap: StoragePoolMap[] = [];

  updateNfs: UpdateNfs = new UpdateNfs();
  addModelShow = false; // 添加窗口
  errorMsg: string;
  modalLoading = false; // 数据加载loading
  modalHandleLoading = false; // 数据处理loading
  checkedPool: any;
  // 添加页面窗口
  @ViewChild('wizard') wizard: ClrWizard;
  @ViewChild('addPageOne') addPageOne: ClrWizardPage;
  @ViewChild('addPageTwo') addPageTwo: ClrWizardPage;
  addSuccessShow = false; // 添加成功提示
  modifySuccessShow = false; // 添加成功提示
  syncSuccessTips = false; // 同步成功提

  logicPorts: LogicPort[] = [];
  oldNfsName: string;
  oldShareName: string;
  oldFsName: string;
  matchErr = false;
  nfsNameRepeatErr = false;
  shareNameRepeatErr = false;
  fsNameRepeatErr = false;

  vmkernelList: Vmkernel[] = [];

  maxbandwidthChoose = false; // 最大带宽 选中
  maxiopsChoose = false; // 最大iops 选中
  minbandwidthChoose = false; // 最小带宽 选中
  miniopsChoose = false; // 最小iops 选中
  latencyChoose = false; // 时延 选中

  hiddenLowerFlag = false; // 不支持下限 true是 false否
  deduplicationShow = false; // 重复数据删除 true 支持 false 不支持
  compressionShow = false; // 数据压缩 true 支持 false 不支持
  latencyIsSelect = false; // 时延为下拉框
  dorado = false; //是否是V6设备
  shareNameContainsCN = false; // 共享名称包含中文

  errMessage = '';

  bandWidthMaxErrTips = false; // 带宽上限错误提示
  bandWidthMinErrTips = false; // 带宽下限错误提示
  iopsMaxErrTips = false; // IOPS上限错误提示
  iopsMinErrTips = false; // IOPS下限错误提示
  latencyErrTips = false; // 时延错误提示

  bandwidthLimitErr = false; // v6 设备 带宽 下限大于上限
  iopsLimitErr = false; // v6 设备 IOPS 下限大于上限

  constructor(
    private addService: NfsAddService,
    private remoteSrv: NfsService,
    public cdr: ChangeDetectorRef,
    public gs: GlobalsService,
    private storageService: StorageService,
    private vmfsListService: VmfsListService,
    private router: Router,
    private token: TokenService,
    @Optional()
    @Inject(DOCUMENT)
    private document: any
  ) {
    super();
    this.getColor = getColorByType;
    this.getLabelByValue = getLabelByValue;
    this.isAddPageOneNextDisabled = true;
    this.checkAddForm = debounce(this.checkAddForm.bind(this), 300);
  }

  ngOnInit(): void {
    this.process();
    this.getNfsList();
  }
  // 获取nfs列表
  getNfsList() {
    this.isLoading = true;
    // 进行数据加载
    const nfsListResHandler = (result: any) => {
      this.list = result.data;
      if (this.list != null) {
        this.total = this.list.length;
      }
      //处理利用率排序问题
      this.handleSortingFeild();
      this.isLoading = false;
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      // 获取性能列表
      this.listnfsperformance();
    };

    /* TODO: */
    if (isMockData) {
      nfsListResHandler(mockData.ACCESSNFS_LISTNFS);
    } else {
      this.remoteSrv.getData().subscribe(nfsListResHandler);
    }
  }

  handleSortingFeild() {
    if (this.list != null) {
      this.list.forEach(n => {
        if (n.capacity && n.freeSpace) {
          n.capacityUsage = (n.capacity - n.freeSpace) / n.capacity;
        }
      });
    }
  }
  // 性能视图列表
  listnfsperformance() {
    if (this.list === null || this.list.length <= 0) {
      return;
    }
    const fsIds = [];
    this.list.forEach(item => {
      fsIds.push(item.fsId);
    });
    this.fsIds = fsIds;
    this.remoteSrv.getChartData(this.fsIds).subscribe((result: any) => {
      if (result.code === '200') {
        const chartList: List[] = result.data;
        if (chartList !== null && chartList.length > 0) {
          this.list.forEach(item => {
            chartList.forEach(charItem => {
              if (item.fsId === charItem.fsId) {
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

  // 页面跳转
  jumpTo(page: ClrWizardPage) {
    if (page && page.completed) {
      this.wizard.navService.currentPage = page;
    } else {
      this.wizard.navService.setLastEnabledPageCurrent();
    }
    this.wizard.open();
  }

  print(val) {
    return JSON.stringify(val, null, 2);
  }

  addView() {
    // const flag = 'plugin';
    // this.router.navigate(['nfs/add'],{
    //   queryParams:{
    //     flag
    //   }
    // });
    /* 打开添加弹窗 */
    this.addModelShow = true;
    this.storageList = null;
    this.storagePoolMap = [];
    this.shareNameContainsCN = false;
    // qos错误提示初始化
    this.iopsLimitErr = false;
    this.bandwidthLimitErr = false;
    // 添加页面默认打开首页
    this.jumpTo(this.addPageOne);

    const successGetData = (s: any) => {
      this.modalLoading = false;
      if (s.code === '200') {
        this.storageList = s.data;
        this.modalLoading = false;
        const allPoolMap: StoragePoolMap[] = [];
        s.data.forEach(item => {
          const poolMap: StoragePoolMap = {
            storageId: item.id,
            storagePoolList: null,
            logicPort: null,
          };
          allPoolMap.push(poolMap);
        });

        this.storagePoolMap = allPoolMap;
      }
    };
    /* 1.获取存储设备 */
    if (isMockData) {
      successGetData(mockData.DMESTORAGE_STORAGES);
    } else {
      this.storageService.getData().subscribe(successGetData);
    }

    this.hostList = null;
    const handlerGetHostListSuccess = (r: any) => {
      this.modalLoading = false;
      if (r.code === '200') {
        this.hostList = r.data;
        this.cdr.detectChanges();
      }
    };
    /* 2.获取挂载主机List */
    if (isMockData) {
      handlerGetHostListSuccess(mockData.NFS_ACCESSVMWARE_LISTHOST);
    } else {
      this.addService
        .getHostList()
        .subscribe(handlerGetHostListSuccess, handlerResponseErrorSimple);
    }

    this.createAddFormAndWatchFormChange();

    // 初始化form
    this.addFormGroup.reset(this.addForm);
    this.checkedPool = null;
    this.errorMsg = '';

    this.hiddenLowerFlag = false;
    this.deduplicationShow = false;
    this.compressionShow = false;
    this.initIopsErrTips(true, true);
    // 获取存储列表
    this.cdr.detectChanges();
  }

  modifyData() {
    const flag = 'plugin';
    const objectid = this.rowSelected[0].objectid;
    this.router.navigate(['nfs/modify'], {
      queryParams: {
        objectid,
        flag,
      },
    });
  }

  modifyCommit() {
    this.updateNfs.name = this.updateNfs.nfsName;
    this.remoteSrv.updateNfs(this.updateNfs).subscribe((result: any) => {
      if (result.code === '200') {
        this.modifyShow = false;
      } else {
        this.modifyShow = true;
        this.errMessage = '编辑失败！' + result.description;
      }
    });
  }
  expandView() {
    const flag = 'plugin';
    const fsId = this.rowSelected[0].fsId;
    const objectId = this.rowSelected[0].objectid;
    this.router.navigate(['nfs/expand'], {
      queryParams: {
        objectId,
        fsId,
        flag,
      },
    });
  }

  addNfs() {
    if (
      this.bandWidthMaxErrTips ||
      this.iopsMaxErrTips ||
      this.bandWidthMinErrTips ||
      this.iopsMinErrTips ||
      this.latencyErrTips ||
      this.bandwidthLimitErr ||
      this.iopsLimitErr
    ) {
      return;
    }
    //
    this.modalHandleLoading = true;

    this.checkedPool = this.storagePools.filter(item => item.id === this.addForm.storagePoolId)[0];

    this.addForm.poolRawId = this.checkedPool.poolId;
    this.addForm.storagePoolId = this.checkedPool.id;
    const addSubmitForm = new AddNfs();
    Object.assign(addSubmitForm, this.addForm);
    // 单位换算
    switch (this.unit) {
      case 'TB':
        addSubmitForm.size = addSubmitForm.size * 1024;
        break;
      case 'MB':
        addSubmitForm.size = addSubmitForm.size / 1024;
        break;
      case 'KB':
        addSubmitForm.size = addSubmitForm.size / (1024 * 1024);
        break;
      default:
        // 默认GB 不变
        break;
    }
    //  控制策略若未选清空数据
    this.qosFunc(addSubmitForm);
    // 重删压缩处理
    if (!addSubmitForm.thin) {
      addSubmitForm.deduplicationEnabled = null;
      addSubmitForm.compressionEnabled = null;
    } else {
      if (
        addSubmitForm.deduplicationEnabled != null &&
        addSubmitForm.deduplicationEnabled.toString()
      ) {
        addSubmitForm.deduplicationEnabled =
          addSubmitForm.deduplicationEnabled.toString() == 'true';
      } else {
        addSubmitForm.deduplicationEnabled = null;
      }
      if (addSubmitForm.compressionEnabled != null && addSubmitForm.compressionEnabled.toString()) {
        addSubmitForm.compressionEnabled = addSubmitForm.compressionEnabled.toString() == 'true';
      } else {
        addSubmitForm.compressionEnabled = null;
      }
    }
    this.addService.addNfs(addSubmitForm).subscribe((result: any) => {
      this.modalHandleLoading = false;
      if (result.code === '200') {
        // 打开成功提示窗口
        this.addSuccessShow = true;
        // 添加成功后刷新数据
        this.getNfsList();
      } else {
        this.errorMsg = '1';
        console.log('Delete failed:', result.description);
      }
      this.cdr.detectChanges();
    });
  }

  /* 存储设备change */
  selectStoragePool() {
    this.modalLoading = true;
    this.storagePools = [];
    this.addForm.storagePoolId = undefined;
    this.logicPorts = [];
    this.addForm.currentPortId = undefined;
    /*  */
    if (this.addForm.storagId) {
      this.addQosUpperAndLower();
      this.addCompressionShow();
      this.addDeduplicationShow();
      this.addLatencyChoose();
      const storages = this.storageList.filter(item => item.id == this.addForm.storagId);
      this.dorado = storages[0].storageTypeShow.dorado;

      // const storagePoolMap = this.storagePoolMap.filter(item => item.storageId == this.addForm.storagId);

      // const storagePoolList = storagePoolMap[0].storagePoolList;
      // const logicPorts = storagePoolMap[0].logicPort;
      // 选择存储后获取存储池
      // if (!storagePoolList) {
      let mediaType;
      //如果是v6就不显示自动扩容选项 mediaType 为BlockAndFile
      if (this.dorado) {
        this.addForm.autoSizeEnable = undefined;
        mediaType = 'block-and-file';
      } else {
        mediaType = 'file';
      }
      const handlerGetStoragePoolListByStorageIdSuccess = (r: any) => {
        this.modalLoading = false;
        if (r.code === '200') {
          this.storagePools = r.data;
          this.storagePoolMap.filter(
            item => item.storageId == this.addForm.storagId
          )[0].storagePoolList = r.data;
        }
        this.cdr.detectChanges();
      };
      /*  */
      if (isMockData) {
        handlerGetStoragePoolListByStorageIdSuccess(mockData.DMESTORAGE_STORAGES);
      } else {
        this.storageService
          .getStoragePoolListByStorageId(mediaType, this.addForm.storagId)
          .subscribe(handlerGetStoragePoolListByStorageIdSuccess, handlerResponseErrorSimple);
      }

      // } else {
      //   console.log("storagePoolList exists")
      //   this.storagePools = storagePoolList;
      // }
      // if (!logicPorts) {
      this.selectLogicPort();
      // } else {
      //   console.log("logicPorts exists")
      //   this.logicPorts = logicPorts;
      //   this.modalLoading=false;
      // }
    }
  }

  /**
   * 添加页面 qos 上下限 单选、多选、隐藏
   * smartTiger 初始化
   */
  addQosUpperAndLower() {
    // qos策略 1 支持复选(上限、下限) 2支持单选（上限或下限） 3只支持上限
    const qosTag = this.getStorageQosTag(this.addForm.storagId);
    this.addForm.control_policyLower = undefined;
    this.addForm.control_policyUpper = undefined;
    const upperObj = document.getElementById('control_policyUpper') as HTMLInputElement;
    const lowerObj = document.getElementById('control_policyLower') as HTMLInputElement;
    if (upperObj && upperObj.checked) {
      upperObj.checked = false;
    }
    if (lowerObj && lowerObj.checked) {
      lowerObj.checked = false;
    }
    if (qosTag == 3) {
      this.hiddenLowerFlag = true;
    } else {
      this.hiddenLowerFlag = false;
    }
  }

  /**
   * 添加页面 重复数据删除
   */
  addDeduplicationShow() {
    this.addForm.deduplicationEnabled = null;
    this.deduplicationShow = this.getDeduplicationShow(this.addForm.storagId);
  }

  /**
   * 添加页面 获取 重复数据删除
   * @param storageId
   */
  getDeduplicationShow(storageId) {
    const deduplicationShow = this.storageList.filter(item => item.id == storageId);
    return deduplicationShow[0].storageTypeShow.deduplicationShow;
  }

  /**
   * 添加页面数据压缩
   */
  addCompressionShow() {
    this.addForm.compressionEnabled = null;
    this.compressionShow = this.getCompressionShow(this.addForm.storagId);
  }

  /**
   * 添加页面 时延为下拉框
   */
  addLatencyChoose() {
    this.addForm.latency = null;
    const qosTag = this.getStorageQosTag(this.addForm.storagId);
    this.latencyIsSelect = qosTag == 1;
  }

  /**
   * 添加页面 获取数据压缩
   * @param storageId
   */
  getCompressionShow(storageId) {
    const compressionshow = this.storageList.filter(item => item.id == storageId);
    return compressionshow[0].storageTypeShow.compressionShow;
  }
  // 选择存储后逻辑端口
  selectLogicPort() {
    this.modalLoading = true;
    /*  */
    const handlerGetLogicPortListByStorageIdSuccess = (r: any) => {
      this.modalLoading = false;
      if (r.code === '200') {
        this.logicPorts = r.data;
        this.storagePoolMap.filter(item => item.storageId == this.addForm.storagId)[0].logicPort =
          r.data;
      }
      this.cdr.detectChanges();
    };

    if (isMockData) {
      handlerGetLogicPortListByStorageIdSuccess(mockData.NFS_DMESTORAGE_LOGICPORTS);
    } else {
      this.storageService
        .getLogicPortListByStorageId(this.addForm.storagId)
        .subscribe(handlerGetLogicPortListByStorageIdSuccess);
    }
  }

  // 弹出缩容页面
  reduceView() {
    const flag = 'plugin';
    const fsId = this.rowSelected[0].fsId;
    const objectId = this.rowSelected[0].objectid;
    this.router.navigate(['nfs/reduce'], {
      queryParams: {
        objectId,
        fsId,
        flag,
      },
    });
  }
  // 挂载
  mount() {
    // this.jumpPage(this.rowSelected[0].objectid,"nfs/dataStore/mount");
    const flag = 'plugin';
    const objectId = this.rowSelected[0].objectid;
    const dsName = this.rowSelected[0].name;
    this.router.navigate(['nfs/dataStore/mount'], {
      queryParams: {
        objectId,
        flag,
        dsName,
      },
    });
  }
  jumpPage(objectId: string, url: string) {
    const flag = 'plugin';
    this.router.navigate([url], {
      queryParams: {
        objectId,
        flag,
      },
    });
  }
  // 卸载按钮点击事件
  unmountBtnFunc() {
    if (this.rowSelected != null && this.rowSelected.length == 1) {
      this.jumpPage(this.rowSelected[0].objectid, 'nfs/dataStore/unmount');
    }
  }
  // 删除按钮点击事件
  delBtnFunc() {
    const flag = 'plugin';
    const objectid = this.rowSelected.map(item => item.objectid);
    this.router.navigate(['nfs/delete'], {
      queryParams: {
        objectid,
        flag,
      },
    });
  }
  navigateTo(objectid) {
    console.log('页面跳转了');
    console.log(objectid);
    this.gs.getClientSdk().app.navigateTo({
      targetViewId: 'vsphere.core.datastore.summary',
      objectId: objectid,
    });
  }

  formatCapacity(c: number) {
    if (c < 1024) {
      return c.toFixed(3) + ' GB';
    } else if (c >= 1024 && c < 1048576) {
      return (c / 1024).toFixed(3) + ' TB';
    } else if (c >= 1048576) {
      return (c / 1024 / 1024).toFixed(3) + ' PB';
    }
  }
  // 点刷新那个功能是分两步，一步是刷新，然后等我们这边的扫描任务，任务完成后返回你状态，任务成功后，你再刷新列表页面。
  scanDataStore() {
    this.isLoading = true;
    this.vmfsListService.scanVMFS('nfs').subscribe((res: any) => {
      this.isLoading = false;
      this.syncSuccessTips = true;
      if (res.code === '200') {
        // this.getNfsList();
        console.log('Scan success');
        this.router.navigate(['nfs'], {
          queryParams: { t: new Date().getTime() },
        });
      } else {
        console.log('Scan faild');
        this.router.navigate(['nfs'], {
          queryParams: { t: new Date().getTime() },
        });
      }
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });
  }

  checkNfsName() {
    if (this.addForm.nfsName == null) return false;
    if (this.oldNfsName == this.addForm.nfsName) return false;
    this.oldNfsName = this.addForm.nfsName;
    let reg5: RegExp = new RegExp('^[0-9a-zA-Z\u4e00-\u9fa5a"_"]*$');
    if (reg5.test(this.addForm.nfsName)) {
      // 共享名称不能包含中文
      let reg5Two: RegExp = new RegExp('[\u4e00-\u9fa5]');
      //验证重复
      this.matchErr = false;
      if (this.addForm.sameName) {
        this.addForm.shareName = this.addForm.nfsName;
        this.addForm.fsName = this.addForm.nfsName;
        if (reg5Two.test(this.addForm.nfsName)) {
          this.addForm.sameName = false;
          this.addForm.shareName = '';
          this.shareNameContainsCN = true;
        } else {
          this.shareNameContainsCN = false;
          this.checkShareNameExist(this.addForm.nfsName);
        }
        this.checkNfsNameExist(this.addForm.nfsName);
        this.checkFsNameExist(this.addForm.nfsName);
      } else {
        this.checkNfsNameExist(this.addForm.nfsName);
      }
    } else {
      //
      this.matchErr = true;
      //不满足的时候置空
      this.addForm.nfsName = null;
      console.log('验证不通过');
    }
  }

  checkShareName() {
    if (this.addForm.shareName == null) return false;
    if (this.oldShareName == this.addForm.shareName) return false;
    this.oldShareName = this.addForm.shareName;
    let reg5: RegExp = new RegExp('^[0-9a-zA-Z\u4e00-\u9fa5a"_"]*$');
    if (reg5.test(this.addForm.shareName)) {
      // 共享名称不能包含中文
      let reg5Two: RegExp = new RegExp('[\u4e00-\u9fa5]');
      if (reg5Two.test(this.addForm.shareName)) {
        this.addForm.shareName = '';
        this.shareNameContainsCN = true;
      } else {
        this.shareNameContainsCN = false;
        //验证重复
        this.matchErr = false;
        this.checkShareNameExist(this.addForm.shareName);
      }
    } else {
      this.matchErr = true;
      this.addForm.shareName = null;
    }
  }

  /**
   * 添加页面可点击 true 可点击 false 不可点击
   */
  isCheckSameName() {
    let reg5: RegExp = new RegExp('[\u4e00-\u9fa5]');
    if (reg5.test(this.addForm.nfsName)) {
      // 名称有中文
      return false;
    } else {
      // 无中文
      return true;
    }
  }
  setSameName() {
    if (this.addForm.sameName) {
      this.addForm.shareName = this.addForm.nfsName;
      this.addForm.fsName = this.addForm.nfsName;
    }
  }

  checkFsName() {
    if (this.addForm.fsName == null) return false;
    if (this.oldFsName == this.addForm.fsName) return false;

    this.oldFsName = this.addForm.fsName;
    let reg5: RegExp = new RegExp('^[0-9a-zA-Z\u4e00-\u9fa5a"_"]*$');
    if (reg5.test(this.addForm.fsName)) {
      //验证重复
      this.matchErr = false;
      this.checkShareNameExist(this.addForm.fsName);
    } else {
      this.matchErr = true;
      this.addForm.fsName = null;
    }
  }
  checkNfsNameExist(name: string) {
    this.addService.checkNfsNameExist(name).subscribe((r: any) => {
      if (r.code == '200') {
        if (r.data) {
          this.nfsNameRepeatErr = false;
        } else {
          this.nfsNameRepeatErr = true;
          this.addForm.nfsName = null;
        }
      }
    });
  }
  checkShareNameExist(name: string) {
    this.addService.checkShareNameExist(name).subscribe((r: any) => {
      if (r.code == '200') {
        if (r.data) {
          this.shareNameRepeatErr = false;
        } else {
          this.shareNameRepeatErr = true;
          this.addForm.nfsName = null;
        }
      }
    });
  }
  checkFsNameExist(name: string) {
    this.addService.checkFsNameExist(name).subscribe((r: any) => {
      if (r.code == '200') {
        if (r.data) {
          this.shareNameRepeatErr = false;
        } else {
          this.shareNameRepeatErr = true;
          this.addForm.nfsName = null;
        }
      }
    });
  }

  backToNfsList() {
    this.modalLoading = false;
    this.confirmActResult();
  }

  checkHost() {
    this.modalLoading = true;
    this.addForm.vkernelIp = undefined;
    //选择主机后获取虚拟网卡
    const handlerGetVmkernelListByObjectIdSuccess = (r: any) => {
      this.modalLoading = false;
      if (r.code === '200') {
        this.vmkernelList = r.data;
        if (this.vmkernelList && this.vmkernelList.length > 0) {
          this.addForm.vkernelIp = this.vmkernelList[0].ipAddress;
        }
      }
      this.cdr.detectChanges();
    };
    if (isMockData) {
      handlerGetVmkernelListByObjectIdSuccess(
        mockData.NFS_ACCESSVMWARE_GETVMKERNELIPBYHOSTOBJECTID
      );
    } else {
      this.addService
        .getVmkernelListByObjectId(this.addForm.hostObjectId)
        .subscribe(handlerGetVmkernelListByObjectIdSuccess, handlerResponseErrorSimple);
    }
  }
  qosBlur(type: String, operationType: string) {
    let objVal;
    if (type === 'add') {
      switch (operationType) {
        case 'maxbandwidth':
          objVal = this.addForm.maxBandwidth;
          break;
        case 'maxiops':
          objVal = this.addForm.maxIops;
          break;
        case 'minbandwidth':
          objVal = this.addForm.minBandwidth;
          break;
        case 'miniops':
          objVal = this.addForm.minIops;
          break;
        default:
          objVal = this.addForm.latency;
          break;
      }
    }
    if (objVal && objVal !== '') {
      if (objVal.toString().match(/\d+(\.\d{0,2})?/)) {
        objVal = objVal.toString().match(/\d+(\.\d{0,2})?/)[0];
      } else {
        objVal = '';
      }
    }
    if (objVal > 999999999) {
      objVal = '';
    } else if (objVal < 1) {
      objVal = '';
    }
    if (type === 'add') {
      switch (operationType) {
        case 'maxbandwidth':
          this.addForm.maxBandwidth = objVal;
          break;
        case 'maxiops':
          this.addForm.maxIops = objVal;
          break;
        case 'minbandwidth':
          this.addForm.minBandwidth = objVal;
          break;
        case 'miniops':
          this.addForm.minIops = objVal;
          break;
        default:
          this.addForm.latency = objVal;
          break;
      }
    }
    this.iopsErrTips(objVal, operationType);

    // 下限大于上限 检测
    this.qosV6Check('add');
  }

  /**
   * iops错误提示
   * @param objVal
   * @param operationType
   */
  iopsErrTips(objVal: string, operationType: string) {
    if (operationType) {
      switch (operationType) {
        case 'maxbandwidth':
          if (objVal == '' && this.addForm.maxBandwidthChoose) {
            this.bandWidthMaxErrTips = true;
          } else {
            this.bandWidthMaxErrTips = false;
          }
          break;
        case 'maxiops':
          if (objVal == '' && this.addForm.maxIopsChoose) {
            this.iopsMaxErrTips = true;
          } else {
            this.iopsMaxErrTips = false;
          }
          break;
        case 'minbandwidth':
          if (objVal == '' && this.addForm.minBandwidthChoose) {
            this.bandWidthMinErrTips = true;
          } else {
            this.bandWidthMinErrTips = false;
          }
          break;
        case 'miniops':
          if (objVal == '' && this.addForm.minIopsChoose) {
            this.iopsMinErrTips = true;
          } else {
            this.iopsMinErrTips = false;
          }
          break;
        default:
          if (objVal == '' && this.addForm.latencyChoose) {
            this.latencyErrTips = true;
          } else {
            this.latencyErrTips = false;
          }
          break;
      }
    }
  }

  /**
   * 初始化IOPS错误提示
   */
  initIopsErrTips(upper: boolean, lower: boolean) {
    if (upper) {
      this.bandWidthMaxErrTips = false;
      this.iopsMaxErrTips = false;
    }
    if (lower) {
      this.bandWidthMinErrTips = false;
      this.iopsMinErrTips = false;
      this.latencyErrTips = false;
    }
  }

  /**
   * 确认关闭窗口
   */
  confirmActResult() {
    this.addModelShow = false;
  }

  qosFunc(form) {
    console.log('form.qosFlag', form.qosFlag);
    const qosTag = this.getStorageQosTag(form.storagId);
    if (!form.qosFlag) {
      // 关闭状态
      form.contolPolicy = null;
      this.initAddMinInfo(form);
      this.initAddMaxInfo(form);
    } else {
      if (form.control_policyUpper == '1') {
        if (!form.maxBandwidthChoose) {
          form.maxBandwidth = null;
        }
        if (!form.maxIopsChoose) {
          form.maxIops = null;
        }
        if (qosTag == 2 || qosTag == 3) {
          this.initAddMinInfo(form);
        }
      }
      if (form.control_policyLower == '0') {
        if (qosTag == 2) {
          this.initAddMaxInfo(form);
        } else if (qosTag == 3) {
          this.initAddMinInfo(form);
        }
        if (!form.minBandwidthChoose) {
          form.minBandwidth = null;
        }
        if (!form.minIopsChoose) {
          form.minIops = null;
        }
        if (!form.latencyChoose) {
          form.latency = null;
        }
      } else {
        this.initAddMinInfo(form);
      }
      if (form.control_policyUpper != '1' && form.control_policyLower != '0') {
        this.initAddMinInfo(form);
        this.initAddMaxInfo(form);
        form.contolPolicy = null;
      } else if (form.control_policyUpper == '1' && form.control_policyLower != '0') {
        this.initAddMinInfo(form);
        form.contolPolicy = 'up';
      } else if (form.control_policyUpper != '1' && form.control_policyLower == '0') {
        this.initAddMaxInfo(form);
        form.contolPolicy = 'low';
      } else {
        // all
        form.contolPolicy = 'all';
      }
    }
  }
  initAddMinInfo(form) {
    form.control_policyLower = undefined;
    form.minBandwidthChoose = false;
    form.minBandwidth = null;
    form.minIopsChoose = false;
    form.minIops = null;
    form.latencyChoose = false;
    form.latency = null;
  }
  initAddMaxInfo(form) {
    form.control_policyUpper = undefined;
    form.maxBandwidthChoose = false;
    form.maxBandwidth = null;
    form.maxIopsChoose = false;
    form.maxIops = null;
  }

  ngOnChanges(changes: { [propKey: string]: SimpleChange }) {
    debugger;
    console.log(changes);
  }

  /**
   * qos开关
   * @param form
   */
  qoSFlagChange(form) {
    if (form.qosFlag) {
      form.control_policyUpper = undefined;
      form.maxBandwidthChoose = false;
      form.maxIopsChoose = false;

      form.control_policyLower = undefined;
      form.minBandwidthChoose = false;
      form.minIopsChoose = false;
      form.latencyChoose = false;
    }
  }
  /**
   * 控制策略变更
   * @param upperObj
   * @param lowerObj
   * @param isUpper true:upper、false:lower
   */
  controlPolicyChangeFunc(upperId, lowerId, isEdit, form, isUpper) {
    const upperObj = document.getElementById(upperId) as HTMLInputElement;
    const lowerObj = document.getElementById(lowerId) as HTMLInputElement;
    // qos策略 1 支持复选(上限、下限) 2支持单选（上限或下限） 3只支持上限
    let qosTag;
    if (isEdit) {
      // qosTag = this.storage.storageTypeShow.qosTag;
    } else {
      qosTag = this.getStorageQosTag(form.storagId);
    }

    let upperChecked;
    if (upperObj) {
      upperChecked = upperObj.checked;
    }
    let lowerChecked;
    if (lowerObj) {
      lowerChecked = lowerObj.checked;
    }
    this.initIopsErrTips(upperChecked, lowerChecked);
    if (isUpper) {
      if (upperChecked) {
        form.control_policyUpper = '1';
      } else {
        form.control_policyUpper = undefined;
      }
      if (qosTag == 2 && upperChecked) {
        // 单选
        console.log('单选1', qosTag);
        form.control_policyLower = undefined;
        lowerObj.checked = false;
      }
    } else {
      if (lowerChecked) {
        form.control_policyLower = '0';
      } else {
        form.control_policyLower = undefined;
      }
      if (lowerChecked && qosTag == 2) {
        console.log('单选2', qosTag);
        form.control_policyUpper = undefined;
        upperObj.checked = false;
      }
    }
    console.log('lowerChecked', form);
    if (form.control_policyUpper == undefined) {
      form.maxBandwidthChoose = false;
      form.maxIopsChoose = false;
    }
    if (form.control_policyLower == undefined) {
      form.minBandwidthChoose = false;
      form.minIopsChoose = false;
      form.latencyChoose = false;
    }
    this.qosV6Check('add');
  }

  /**
   * 获取选中的存储的 QosTag
   */
  getStorageQosTag(storageId) {
    const storageTypeShow = this.storageList.filter(item => item.id == storageId);
    // qos策略 1 支持复选(上限、下限) 2支持单选（上限或下限） 3只支持上限
    const qosTag = storageTypeShow[0].storageTypeShow.qosTag;
    return qosTag;
  }
  private process(): boolean {
    const tourl = this.getQueryString('view');
    let res = this.checkJWT(this.token.get<any>(), 1000);
    res = true;
    if (tourl) {
      // 如果带有?view=storage则跳转到当前页面
      var newURL = location.href.split('?')[0];
      console.log('newURL=', newURL);
      window.history.pushState('object', document.title, newURL); // 去除多余参数  避免二次内部跳转失败
      this.gotoUrl('/' + tourl);
    }
    return res;
  }
  private gotoUrl(url?: string) {
    setTimeout(() => {
      if (/^https?:\/\//g.test(url!)) {
        this.document.location.href = url as string;
      } else {
        this.router.navigateByUrl(url);
      }
    });
  }
  private checkJWT(model: any, offset?: number): boolean {
    return !!model?.token;
  }
  getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
      return unescape(r[2]);
    }
    return null;
  }

  resetQosFlag(objValue: boolean, operationType: string) {
    switch (operationType) {
      case 'maxbandwidth':
        if (!objValue) {
          this.bandWidthMaxErrTips = false;
        }
        break;
      case 'maxiops':
        if (!objValue) {
          this.iopsMaxErrTips = false;
        }
        break;
      case 'minbandwidth':
        if (!objValue) {
          this.bandWidthMinErrTips = false;
        }
        break;
      case 'miniops':
        if (!objValue) {
          this.iopsMinErrTips = false;
        }
        break;
      default:
        if (!objValue) {
          this.latencyErrTips = false;
        }
        break;
    }
  }
  setType() {
    if (this.addForm.type == 'NFS41') {
      this.addForm.securityType = 'AUTH_SYS';
    } else {
      this.addForm.securityType = '';
    }
  }
  sortFunc(obj: any) {
    return !obj;
  }

  qosV6Check(type: string) {
    if (type == 'add') {
      if (this.addForm.storagId) {
        const chooseStorage = this.storageList.filter(item => item.id == this.addForm.storagId)[0];
        if (chooseStorage) {
          const qosTag = chooseStorage.storageTypeShow.qosTag;
          const { bandwidthLimitErr, iopsLimitErr } = getQosCheckTipsTagInfo({
            qosTag,
            minBandwidthChoose: this.addForm.minBandwidthChoose,
            minBandwidth: this.addForm.minBandwidth,
            maxBandwidthChoose: this.addForm.maxBandwidthChoose,
            maxBandwidth: this.addForm.maxBandwidth,
            minIopsChoose: this.addForm.minIopsChoose,
            minIops: this.addForm.minIops,
            maxIopsChoose: this.addForm.maxIopsChoose,
            maxIops: this.addForm.maxIops,
            control_policyUpper: this.addForm.control_policyUpper,
            control_policyLower: this.addForm.control_policyLower,
          });
          this.bandwidthLimitErr = bandwidthLimitErr;
          this.iopsLimitErr = iopsLimitErr;

          /* 
          
          
          const qosTag = chooseStorage.storageTypeShow.qosTag;
          if (qosTag == 1) {
            if (this.addForm.minBandwidthChoose && this.addForm.maxBandwidthChoose) {
              // 带宽上限小于下限
              if (
                this.addForm.minBandwidth &&
                this.addForm.maxBandwidth &&
                Number(this.addForm.minBandwidth) > Number(this.addForm.maxBandwidth)
              ) {
                this.bandwidthLimitErr = true;
              } else {
                this.bandwidthLimitErr = false;
              }
            } else {
              this.bandwidthLimitErr = false;
            }
            if (this.addForm.minIopsChoose && this.addForm.maxIopsChoose) {
              // iops上限小于下限
              if (
                this.addForm.minIops &&
                this.addForm.maxIops &&
                Number(this.addForm.minIops) > Number(this.addForm.maxIops)
              ) {
                this.iopsLimitErr = true;
              } else {
                this.iopsLimitErr = false;
              }
            } else {
              this.iopsLimitErr = false;
            }
          } else {
            this.iopsLimitErr = false;
            this.bandwidthLimitErr = false;
          }
          if (
            this.addForm.maxIopsChoose &&
            this.addForm.maxIops &&
            Number(this.addForm.maxIops) < 100
          ) {
            this.iopsLimitErr = true;
          }
          if (this.addForm.control_policyUpper == undefined) {
            this.iopsLimitErr = false;
            this.bandwidthLimitErr = false;
          }
          if (this.addForm.control_policyLower == undefined) {
            this.bandwidthLimitErr = false;
          }
        
          */
        }
      }
    }
  }
}
