import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
  ViewChild,
} from '@angular/core';
import { GlobalsService } from '../../../../shared/globals.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AddNfs, Host, NfsAddService, Vmkernel } from './nfs-add.service';
import { ClrWizard, ClrWizardPage } from '@clr/angular';
import { LogicPort, StorageList, StorageService } from '../../../storage/storage.service';
import { StoragePool, StoragePoolMap } from '../../../storage/detail/detail.service';
import { isMockData, mockData } from 'mock/mock';
import {
  getQosCheckTipsTagInfo,
  handlerResponseErrorSimple,
  isStringLengthByteOutRange,
  regExpCollection,
} from 'app/app.helpers';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import debounce from 'just-debounce';
import { NfsComponentCommon } from '../../NfsComponentCommon';
import { NfsMountService } from './../mount/nfs-mount.service';

@Component({
  selector: 'app-add',
  templateUrl: './nfs-add.component.html',
  styleUrls: ['./nfs-add.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [NfsAddService, StorageService, NfsMountService],
})
export class NfsAddComponent extends NfsComponentCommon implements OnInit {
  isAddPageOneNextDisabled: boolean;

  viewPage: string;
  pluginFlag: string;
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

  errorMsg: string;
  modalLoading = false; // 数据加载loading
  modalHandleLoading = false; // 数据处理loading
  unit = 'GB';
  checkedPool: any;
  storageList: StorageList[] = [];
  storagePools: StoragePool[] = [];
  storagePoolMap: StoragePoolMap[] = [];

  logicPorts: LogicPort[] = [];
  hostList: Host[] = [];

  oldNfsName: string;
  oldShareName: string;
  oldFsName: string;
  matchErr = false;
  nfsNameRepeatErr = false;
  shareNameRepeatErr = false;
  fsNameRepeatErr = false;
  vmkernelList: Vmkernel[] = [];
  capacityErr = true;

  maxbandwidthChoose = false; // 最大带宽 选中
  maxiopsChoose = false; // 最大iops 选中
  minbandwidthChoose = false; // 最小带宽 选中
  miniopsChoose = false; // 最小iops 选中
  latencyChoose = false; // 时延 选中
  addSuccessShow = false; // 添加成功提示

  hiddenLowerFlag = false; // 不支持下限 true是 false否
  deduplicationShow = false; // 重复数据删除 true 支持 false 不支持
  compressionShow = false; // 数据压缩 true 支持 false 不支持
  latencyIsSelect = false; // 时延为下拉框
  dorado = false;

  shareNameContainsCN = false; // 共享名称包含中文

  bandWidthMaxErrTips = false; // 带宽上限错误提示
  bandWidthMinErrTips = false; // 带宽下限错误提示
  iopsMaxErrTips = false; // IOPS上限错误提示
  iopsMinErrTips = false; // IOPS下限错误提示
  latencyErrTips = false; // 时延错误提示

  bandwidthLimitErr = false; // v6 设备 带宽 下限大于上限
  iopsLimitErr = false; // v6 设备 IOPS 下限大于上限

  isCheckUpper:boolean;
  isCheckLower:boolean;
  overSizeTB:boolean
  overSizeGB:boolean

  // 添加页面窗口
  @ViewChild('wizard') wizard: ClrWizard;
  @ViewChild('addPageOne') addPageOne: ClrWizardPage;
  @ViewChild('addPageTwo') addPageTwo: ClrWizardPage;

  constructor(
    private mountService: NfsMountService,
    private addService: NfsAddService,
    public cdr: ChangeDetectorRef,
    private gs: GlobalsService,
    private storageService: StorageService,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) {
    super();
    this.isAddPageOneNextDisabled = true;
    (this.checkAddForm as any) = debounce(this.checkAddForm.bind(this), 600);
  }

  ngOnInit(): void {
    // this.modalLoading = true;
    this.viewPage = 'add_plugin';
    this.activatedRoute.queryParams.subscribe(queryParam => {
      this.pluginFlag = queryParam.flag;
    });
    if (this.pluginFlag == null) {
      //入口来至Vcenter
      this.viewPage = 'delete_vcenter';
    }

    this.storagePoolMap = [];

    //入口是DataSource
    this.viewPage = 'add_plugin';
    this.activatedRoute.queryParams.subscribe(queryParam => {
      this.pluginFlag = queryParam.flag;
    });
    if (this.pluginFlag == null) {
      //入口来至Vcenter
      this.viewPage = 'add_vcenter';
    }

    const successGetData = (s: any) => {
      this.modalLoading = false;
      if (s.code === '200') {
        this.storageList = s.data;

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

        this.modalLoading = false;
      }
    };

    /* 1.获取存储设备 */
    if (isMockData) {
      successGetData(mockData.DMESTORAGE_STORAGES);
    } else {
      this.storageService.getData(1).subscribe(successGetData);
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

    this.checkedPool = null;
    this.errorMsg = '';
    // 获取存储列表
    this.cdr.detectChanges();
  }

  jumpTo(page: ClrWizardPage, wizard: ClrWizard) {
    console.log('wizard:');
    console.log(this.wizard);
    if (page && page.completed) {
      wizard.navService.currentPage = page;
    } else {
      wizard.navService.setLastEnabledPageCurrent();
    }
    this.wizard.open();
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
      } else {
        this.errorMsg = '1';
        console.log('Delete failed:', result.description);
      }
      this.cdr.detectChanges();
    });
  }

  selectStoragePool() {
    this.modalLoading = true;
    this.storagePools = [];
    this.addForm.storagePoolId = undefined;
    this.logicPorts = [];
    this.addForm.currentPortId = undefined;
    if (this.addForm.storagId) {
      this.addQosUpperAndLower();
      this.addCompressionShow();
      this.addDeduplicationShow();
      this.addLatencyChoose();
      const storages = this.storageList.filter(item => item.id == this.addForm.storagId);
      this.dorado = storages[0].storageTypeShow.dorado;
      let mediaType;
      if (this.dorado) {
        this.addForm.autoSizeEnable = undefined;
        mediaType = 'block-and-file';
      } else {
        mediaType = 'file';
      }
      const storagePoolMap = this.storagePoolMap.filter(
        item => item.storageId == this.addForm.storagId
      );

      const storagePoolList = storagePoolMap[0].storagePoolList;
      const logicPorts = storagePoolMap[0].logicPort;

      // 选择存储后获取存储池
      // if (!storagePoolList) {

      const handlerGetStoragePoolListByStorageIdSuccess = (r: any) => {
        if (r.code === '200') {
          this.storagePools = r.data;
        }
        this.cdr.detectChanges();
      };

      if (isMockData) {
        handlerGetStoragePoolListByStorageIdSuccess(mockData.DMESTORAGE_STORAGES);
      } else {
        this.storageService
          .getStoragePoolListByStorageId(mediaType, this.addForm.storagId)
          .subscribe(handlerGetStoragePoolListByStorageIdSuccess);
      }
      // } else {
      //   this.storagePools = storagePoolList;
      // }
      // if (!logicPorts) {
      this.selectLogicPort();
      // } else {
      //   this.logicPorts = logicPorts;
      //   this.modalLoading=false;
      // }
    }
  }

  selectLogicPort() {
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
      // 选择存储后逻辑端口
      this.mountService
        .getLogicPortListByStorageId(this.addForm.storagId, 1)
        .subscribe(handlerGetLogicPortListByStorageIdSuccess);
    }
  }

  checkHost() {
    this.modalLoading = true;

    const handlerGetVmkernelListByObjectIdSuccess = (r: any) => {
      this.modalLoading = false;
      if (r.code === '200') {
        this.vmkernelList = r.data;
        this.addForm.vkernelIp = this.vmkernelList[0].ipAddress;
      }
      this.cdr.detectChanges();
    };
    //选择主机后获取虚拟网卡
    if (isMockData) {
      handlerGetVmkernelListByObjectIdSuccess(
        mockData.NFS_ACCESSVMWARE_GETVMKERNELIPBYHOSTOBJECTID
      );
    } else {
      this.addService
        .getVmkernelListByObjectId(this.addForm.hostObjectId)
        .subscribe(handlerGetVmkernelListByObjectIdSuccess);
    }
  }

  backToNfsList() {
    this.modalLoading = false;
    this.router.navigate(['nfs']);
  }

  closeModel() {
    this.modalLoading = false;
    this.gs.getClientSdk().modal.close();
  }
  checkSize(){
    this.overSizeGB=false
    this.overSizeTB=false
    if (this.unit==='GB'){
      if (this.addForm.size>16777216){
        this.overSizeGB=true
        this.addForm.size=null
      }else if (!this.addForm.size){
        this.overSizeGB=true
      }
      else {
        this.overSizeGB=false
      }
    }else if (this.unit==='TB'){
      if (this.addForm.size>16384){
        this.overSizeTB=true
        this.addForm.size=null
      }else if (!this.addForm.size){
        this.overSizeTB=true
      }
      else {
        this.overSizeTB=false
      }
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
   * 添加页面可点击 true 可点击 false 不可点击
   */
  isCheckSameName() {
    // let reg5: RegExp = new RegExp('[\u4e00-\u9fa5]');
    if (this.addForm.nfsName === '') return true;
    let reg5: RegExp = regExpCollection.shareFsName();
    if (!reg5.test(this.addForm.nfsName)) {
      // 名称有中文
      return false;
    } else {
      // 无中文
      return true;
    }
  }

  setSameName() {
    if (this.addForm.sameName) {
      this.setShareName(this.addForm.nfsName);
      this.addForm.fsName = this.addForm.nfsName;
    }
  }

  whenChoseSameName() {
    this.setShareName(this.addForm.nfsName);
    this.addForm.fsName = this.addForm.nfsName;
    // 共享名称不能包含中文
    /* 取值范围为1到254个字符，只能由字母、数字、字符!\"#&%$'()*+-·.;<=>?@[]^_`{|}~,:和空格组成。 */
    /* 如果有中文，不可以相同，清空并打开 */
    if (!regExpCollection.shareFsName().test(this.addForm.nfsName)) {
      this.addForm.sameName = false;
      this.setShareName('');
      this.shareNameContainsCN = true;
    } else {
      this.shareNameContainsCN = false;
      this.checkShareNameExist(this.addForm.shareName);
    }
    this.checkNfsNameExist(this.addForm.nfsName);
    this.checkFsNameExist(this.addForm.fsName);
  }

  checkNfsName() {
    if (this.addForm.nfsName == null) return false; // let reg5: RegExp = new RegExp('^[0-9a-zA-Z\u4e00-\u9fa5a"_"]*$');
    /*     if (this.oldNfsName == this.addForm.nfsName) return false;
     */

    this.oldNfsName = this.addForm.nfsName;
    const inLimit = !isStringLengthByteOutRange(this.addForm.nfsName, 42, 'letters');

    if (regExpCollection.nfsName().test(this.addForm.nfsName) && inLimit) {
      //验证重复
      this.matchErr = false;
      /* 如果选择名字相同 */
      if (this.addForm.sameName) {
        this.whenChoseSameName();
      } else {
        this.checkNfsNameExist(this.addForm.nfsName);
      }
    } else {
      //不满足的时候置空，触发错误提示
      this.matchErr = true;
      this.addForm.nfsName = null;
      console.log('验证不通过');
    }
  }

  checkFsName() {
    if (this.addForm.fsName == null) return false;
    /*     if (this.oldFsName == this.addForm.fsName) return false;
    this.oldFsName = this.addForm.fsName; */
    // let reg5: RegExp = new RegExp('^[0-9a-zA-Z\u4e00-\u9fa5a"_"]*$');
    const inLimit = !isStringLengthByteOutRange(this.addForm.fsName, 42, 'letters');
    /*  */
    if (regExpCollection.nfsName().test(this.addForm.fsName) && inLimit) {
      //验证重复
      this.matchErr = false;
      this.checkFsNameExist(this.addForm.fsName);
    } else {
      this.matchErr = true;
      this.addForm.fsName = null;
    }
  }

  checkShareName() {
    if (this.addForm.shareName == null) {
      return false;
    }
    /*     if (this.oldShareName == this.addForm.shareName) {
      return false;
    }

    this.oldShareName = this.addForm.shareName;
 */
    // let reg5: RegExp = new RegExp('^[0-9a-zA-Z\u4e00-\u9fa5a"_"]*$');
    const inLimit = !isStringLengthByteOutRange(this.addForm.shareName, 254, 'letters');
    if (regExpCollection.shareFsName().test(this.addForm.shareName) && inLimit) {
      this.shareNameContainsCN = false;
      this.matchErr = false;
      //验证重复
      this.checkShareNameExist(this.addForm.shareName);
    } else {
      this.matchErr = true;
      this.addForm.shareName = null;
    }
  }

  checkNfsNameExist(name: string) {
    if ((this as any).checkNfsNameExist_oldName === name) return;
    (this as any).checkNfsNameExist_oldName = name;
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
    /* v6 不需要 */
    if (this.dorado) return;
    if ((this as any).checkShareNameExist_oldName === name) return;
    (this as any).checkShareNameExist_oldName = name;
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
    if ((this as any).checkFsNameExist_oldName === name) return;
    (this as any).checkFsNameExist_oldName = name;
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

  /**
   * 确认关闭窗口
   */
  confirmActResult() {
    this.wizard.close(); // 关闭弹窗
    if (this.pluginFlag == 'plugin') {
      this.backToNfsList();
    } else {
      this.closeModel();
    }
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

  /**
   * qos开关
   * @param form
   */
  qoSFlagChange(form) {
    if (form.qosFlag) {
      form.control_policyUpper = '1';
      this.isCheckUpper=true;
      this.isCheckLower=false;
      form.maxBandwidthChoose = true;
      form.maxIopsChoose = true;

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
    console.log('lowerChecked', form);
    if (form.control_policyUpper == undefined) {
      form.maxBandwidthChoose = false;
      form.maxIopsChoose = false;
    }else {
      form.maxBandwidthChoose=true;
      form.maxIopsChoose=true;
    }
    if (form.control_policyLower == undefined) {
      form.minBandwidthChoose = false;
      form.minIopsChoose = false;
      form.latencyChoose = false;
    }else {
      form.minBandwidthChoose=true;
      form.minIopsChoose=true;
      form.latencyChoose=false
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
    this.addForm.deduplicationEnabled = '';
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
    this.addForm.compressionEnabled = '';
    this.compressionShow = this.getCompressionShow(this.addForm.storagId);
  }

  /**
   * 添加页面 获取数据压缩
   * @param storageId
   */
  getCompressionShow(storageId) {
    const compressionshow = this.storageList.filter(item => item.id == storageId);
    return compressionshow[0].storageTypeShow.compressionShow;
  }

  /**
   * 添加页面 时延为下拉框
   */
  addLatencyChoose() {
    this.addForm.latency = null;
    const qosTag = this.getStorageQosTag(this.addForm.storagId);
    this.latencyIsSelect = qosTag == 1;
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
        }
      }
    }
  }
}
