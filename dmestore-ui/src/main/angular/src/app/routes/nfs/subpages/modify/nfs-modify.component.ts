import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { GlobalsService } from '../../../../shared/globals.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NfsModifyService } from './nfs-modify.service';
import { UpdateNfs } from '../../nfs.service';
import { StorageList } from '../../../storage/storage.service';
import { VmfsListService } from '../../../vmfs/list/list.service';
import { isMockData, mockData } from 'mock/mock';
import {
  getQosCheckTipsTagInfo,
  isStringLengthByteOutRange,
  print,
  regExpCollection,
} from 'app/app.helpers';

@Component({
  selector: 'app-add',
  templateUrl: './nfs-modify.component.html',
  styleUrls: ['./nfs-modify.component.scss'],
  providers: [NfsModifyService, VmfsListService],
})
export class NfsModifyComponent implements OnInit {
  modalLoading = false; // 数据加载loading
  modalHandleLoading = false; // 数据处理loading
  viewPage: string;
  pluginFlag: string;
  objectId: string;
  updateNfs = new UpdateNfs();
  errorMsg: string;

  print = print;

  maxbandwidthChoose = false; // 最大带宽 选中
  maxiopsChoose = false; // 最大iops 选中
  minbandwidthChoose = false; // 最小带宽 选中
  miniopsChoose = false; // 最小iops 选中
  latencyChoose = false; // 时延 选中
  modifySuccessShow = false; // 编辑程功窗口

  storage: StorageList; // 设备对应存储

  hiddenLowerFlag = false; // 不支持下限 true是 false否
  deduplicationShow = false; // 重复数据删除 true 支持 false 不支持
  compressionShow = false; // 数据压缩 true 支持 false 不支持
  latencyIsSelect = false; // 时延为下拉框

  isThin = false; // true 展示重删压缩、false隐藏重删压缩

  getDataFaild = false; // 获取nfs数据异常
  dorado = false; //是否是V6设备
  bandWidthMaxErrTips = false; // 带宽上限错误提示
  bandWidthMinErrTips = false; // 带宽下限错误提示
  iopsMaxErrTips = false; // IOPS上限错误提示
  iopsMinErrTips = false; // IOPS下限错误提示
  latencyErrTips = false; // 时延错误提示
  bandwidthLimitErr = false; // v6 设备 带宽 下限大于上限
  iopsLimitErr = false; // v6 设备 IOPS 下限大于上限
  shareId;

  modifyDesc:string;//编辑失败信息

  constructor(
    private modifyService: NfsModifyService,
    private cdr: ChangeDetectorRef,
    private gs: GlobalsService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private vmfsListService: VmfsListService
  ) {}

  ngOnInit(): void {
    this.modalLoading = true;
    this.activatedRoute.queryParams.subscribe(queryParam => {
      this.pluginFlag = queryParam.flag;
      this.objectId = queryParam.objectid;
    });
    if (this.pluginFlag == null) {
      //入口来至Vcenter
      const ctx = this.gs.getClientSdk().app.getContextObjects();
      this.objectId = ctx
        ? ctx[0].id
        : 'urn:vmomi:Datastore:datastore-10020:674908e5-ab21-4079-9cb1-596358ee5dd1';
    }

    const handlerGetNfsDetailById = (result: any) => {
      if (this.pluginFlag) {
        this.viewPage = 'modify_plugin';
      } else {
        this.viewPage = 'modify_vcenter';
      }
      if (result.code === '200') {
        this.updateNfs = result.data;
        this.shareId=result.data.shareId
        // this.updateNfs.sameName=true;
        this.updateNfs.dataStoreObjectId = this.objectId;
        this.oldNfsName = this.updateNfs.nfsName;
        this.oldFsName = this.updateNfs.fsName;
        this.oldShareName = this.updateNfs.shareName;

        this.isThin = this.updateNfs.thin;
        if (!this.isThin) {
          this.updateNfs.deduplicationEnabled = null;
          this.updateNfs.compressionEnabled = null;
        }

        // this.updateNfs.deviceId = "c45cc2a4-4ce4-11eb-8b84-a28808fbcdbd";
        if (this.updateNfs.deviceId) {
          this.vmfsListService
            .getStorageDetail(this.updateNfs.deviceId)
            .subscribe((result: any) => {
              this.modalHandleLoading = false;
              if (result.code == '200') {
                this.storage = result.data;
                const storageTypeShow = this.storage.storageTypeShow;
                if (storageTypeShow) {
                  // qos策略 1 支持复选(上限、下限) 2支持单选（上限或下限） 3只支持上限
                  const qosTag = storageTypeShow.qosTag;
                  if (qosTag == 3) {
                    this.hiddenLowerFlag = true;
                  } else {
                    this.hiddenLowerFlag = false;
                  }
                  this.deduplicationShow = storageTypeShow.deduplicationShow;
                  this.compressionShow = storageTypeShow.compressionShow;
                  this.latencyIsSelect = qosTag == 1;
                  this.dorado = storageTypeShow.dorado;
                  if (this.dorado) {
                    this.updateNfs.autoSizeEnable = undefined;
                  }
                }
              }
              this.modalLoading = false;
              const upperObj = document.getElementById(
                'editControl_policyUpper'
              ) as HTMLInputElement;
              const lowerObj = document.getElementById(
                'editControl_policyLower'
              ) as HTMLInputElement;

              if (
                (this.updateNfs.maxBandwidth && this.updateNfs.maxBandwidth != '0') ||
                (this.updateNfs.maxIops && this.updateNfs.maxIops != '0')
              ) {
                if (this.updateNfs.maxBandwidth) {
                  this.updateNfs.maxBandwidthChoose = true;
                }
                if (this.updateNfs.maxIops) {
                  this.updateNfs.maxIopsChoose = true;
                }
                // 转换报错问题修改
                this.updateNfs.maxBandwidth = this.updateNfs.maxBandwidth + '';
                this.updateNfs.maxIops = this.updateNfs.maxIops + '';
                upperObj.checked = true;
                this.controlPolicyChangeFunc(
                  'editControl_policyUpper',
                  'editControl_policyLower',
                  true,
                  this.updateNfs,
                  true
                );
              }
              if (
                (this.updateNfs.latency && this.updateNfs.latency != '0') ||
                (this.updateNfs.minBandwidth && this.updateNfs.minBandwidth != '0') ||
                (this.updateNfs.minIops && this.updateNfs.minIops != '0')
              ) {
                lowerObj.checked = true;
                if (this.updateNfs.latency) {
                  this.updateNfs.latencyChoose = true;
                }
                if (this.updateNfs.minBandwidth) {
                  this.updateNfs.minBandwidthChoose = true;
                }
                if (this.updateNfs.minIops) {
                  this.updateNfs.minIopsChoose = true;
                }
                this.updateNfs.latency = this.updateNfs.latency + '';
                this.updateNfs.minBandwidth = this.updateNfs.minBandwidth + '';
                this.updateNfs.minIops = this.updateNfs.minIops + '';
                this.controlPolicyChangeFunc(
                  'editControl_policyUpper',
                  'editControl_policyLower',
                  true,
                  this.updateNfs,
                  false
                );
              }
              this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
            });
        } else {
          this.modalLoading = false;
        }
      } else {
        this.modalLoading = false;
        this.getDataFaild = true;
      }
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    };
    if (isMockData) {
      handlerGetNfsDetailById(mockData.OPERATENFS_EDITNFSSTORE);
    } else {
      this.modifyService.getNfsDetailById(this.objectId).subscribe(handlerGetNfsDetailById);
    }
  }

  modifyCommit() {
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
    this.modalHandleLoading = true;
    console.log(this.updateNfs);
    if (this.updateNfs.sameName) {
      this.updateNfs.fsName = this.updateNfs.nfsName;
    }
    const editNfsSubmitForm = new UpdateNfs();
    Object.assign(editNfsSubmitForm, this.updateNfs);
    this.qosFunc(editNfsSubmitForm);
    if (!this.deduplicationShow) {
      editNfsSubmitForm.deduplicationEnabled = null;
    }
    if (!this.compressionShow) {
      editNfsSubmitForm.compressionEnabled = null;
    }
    // 重删压缩处理
    if (!editNfsSubmitForm.thin) {
      editNfsSubmitForm.deduplicationEnabled = null;
      editNfsSubmitForm.compressionEnabled = null;
    } else {
      if (
        editNfsSubmitForm.deduplicationEnabled != null &&
        editNfsSubmitForm.deduplicationEnabled.toString()
      ) {
        // 不为空
        editNfsSubmitForm.deduplicationEnabled =
          editNfsSubmitForm.deduplicationEnabled.toString() == 'true';
      } else {
        editNfsSubmitForm.deduplicationEnabled = null;
      }
      if (
        editNfsSubmitForm.compressionEnabled != null &&
        editNfsSubmitForm.compressionEnabled.toString()
      ) {
        editNfsSubmitForm.compressionEnabled =
          editNfsSubmitForm.compressionEnabled.toString() == 'true';
      } else {
        editNfsSubmitForm.compressionEnabled = null;
      }
    }
    editNfsSubmitForm.shareId=this.shareId
    this.modifyService.updateNfs(editNfsSubmitForm).subscribe((result: any) => {
      this.modalHandleLoading = false;
      if (result.code === '200') {
        this.modifySuccessShow = true;
      } else {
        this.errorMsg = '1';
        this.modifyDesc=result.description
        console.log('Delete failed:', result.description);
      }
      this.cdr.detectChanges();
    });
  }

  backToNfsList() {
    this.modalLoading = false;
    this.router.navigate(['nfs']);
  }

  closeModel() {
    this.modalLoading = false;
    this.gs.getClientSdk().modal.close();
  }

  qosBlur(type: String, operationType: string) {
    let objVal;
    if (type === 'add') {
      switch (operationType) {
        case 'maxbandwidth':
          objVal = this.updateNfs.maxBandwidth;
          break;
        case 'maxiops':
          objVal = this.updateNfs.maxIops;
          break;
        case 'minbandwidth':
          objVal = this.updateNfs.minBandwidth;
          break;
        case 'miniops':
          objVal = this.updateNfs.minIops;
          break;
        default:
          objVal = this.updateNfs.latency;
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
          this.updateNfs.maxBandwidth = objVal;
          break;
        case 'maxiops':
          this.updateNfs.maxIops = objVal;
          break;
        case 'minbandwidth':
          this.updateNfs.minBandwidth = objVal;
          break;
        case 'miniops':
          this.updateNfs.minIops = objVal;
          break;
        default:
          this.updateNfs.latency = objVal;
          break;
      }
    }
    this.iopsErrTips(objVal, operationType);
    // 下限大于上限 检测
    this.qosV6Check('edit');
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
          if (objVal == '' && this.updateNfs.maxBandwidthChoose) {
            this.bandWidthMaxErrTips = true;
          } else {
            this.bandWidthMaxErrTips = false;
          }
          break;
        case 'maxiops':
          if (objVal == '' && this.updateNfs.maxIopsChoose) {
            this.iopsMaxErrTips = true;
          } else {
            this.iopsMaxErrTips = false;
          }
          break;
        case 'minbandwidth':
          if (objVal == '' && this.updateNfs.minBandwidthChoose) {
            this.bandWidthMinErrTips = true;
          } else {
            this.bandWidthMinErrTips = false;
          }
          break;
        case 'miniops':
          if (objVal == '' && this.updateNfs.minIopsChoose) {
            this.iopsMinErrTips = true;
          } else {
            this.iopsMinErrTips = false;
          }
          break;
        default:
          if (objVal == '' && this.updateNfs.latencyChoose) {
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

  matchErr = false;
  nfsNameRepeatErr = false;
  shareNameRepeatErr = false;
  fsNameRepeatErr = false;
  oldNfsName: string;
  oldShareName: string;
  oldFsName: string;

  setSameName() {
    if (this.updateNfs.sameName) {
      this.updateNfs.shareName = this.updateNfs.nfsName;
      this.updateNfs.fsName = this.updateNfs.nfsName;
    }
  }

  checkNfsName() {
    if (this.updateNfs.nfsName == null) {
      return false;
    }
    /*   if (this.oldNfsName == this.updateNfs.nfsName) {
      return false;
    } */
    // this.oldNfsName = this.updateNfs.nfsName;

    const inLimit = !isStringLengthByteOutRange(this.updateNfs.nfsName, 42, 'letters');
    let reg5: RegExp = regExpCollection.nfsName();
    // let reg5: RegExp = new RegExp('^[0-9a-zA-Z\u4e00-\u9fa5a"_"]*$');
    if (reg5.test(this.updateNfs.nfsName) && inLimit) {
      this.setSameName();
      //验证重复
      this.matchErr = false;
      if (this.updateNfs.sameName) {
        this.checkNfsNameExist(this.updateNfs.nfsName);
        this.checkShareNameExist(this.updateNfs.nfsName);
        this.checkFsNameExist(this.updateNfs.nfsName);
      } else {
        this.checkNfsNameExist(this.updateNfs.nfsName);
      }
    } else {
      //
      this.matchErr = true;
      //不满足的时候置空
      this.updateNfs.nfsName = null;
      console.log('验证不通过');
    }
  }

  checkFsName() {
    if (this.updateNfs.fsName == null) {
      return false;
    }
    if (this.oldFsName == this.updateNfs.fsName) {
      return false;
    }

    this.oldFsName = this.updateNfs.fsName;

    const inLimit = !isStringLengthByteOutRange(this.updateNfs.fsName, 42, 'letters');

    let reg5: RegExp = regExpCollection.nfsName();
    // let reg5: RegExp = new RegExp('^[0-9a-zA-Z\u4e00-\u9fa5a"_"]*$');
    if (reg5.test(this.updateNfs.fsName) && inLimit) {
      //验证重复
      this.matchErr = false;
      this.checkShareNameExist(this.updateNfs.fsName);
    } else {
      this.matchErr = true;
      this.updateNfs.fsName = null;
    }
  }

  checkShareName() {
    if (this.updateNfs.shareName == null) {
      return false;
    }
    if (this.oldShareName == this.updateNfs.shareName) {
      return false;
    }

    this.oldShareName = this.updateNfs.shareName;
    let reg5: RegExp = new RegExp('^[0-9a-zA-Z\u4e00-\u9fa5a"_"]*$');
    if (reg5.test(this.updateNfs.shareName)) {
      //验证重复
      this.matchErr = false;
      this.checkShareNameExist(this.updateNfs.shareName);
    } else {
      this.matchErr = true;
      this.updateNfs.shareName = null;
    }
  }

  checkNfsNameExist(name: string) {
    this.modifyService.checkNfsNameExist(name).subscribe((r: any) => {
      if (r.code == '200') {
        if (r.data) {
          this.nfsNameRepeatErr = false;
        } else {
          this.nfsNameRepeatErr = true;
          this.updateNfs.nfsName = null;
        }
      }
    });
  }

  checkShareNameExist(name: string) {
    this.modifyService.checkShareNameExist(name).subscribe((r: any) => {
      if (r.code == '200') {
        if (r.data) {
          this.shareNameRepeatErr = false;
        } else {
          this.shareNameRepeatErr = true;
          this.updateNfs.nfsName = null;
        }
      }
    });
  }

  checkFsNameExist(name: string) {
    this.modifyService.checkFsNameExist(name).subscribe((r: any) => {
      if (r.code == '200') {
        if (r.data) {
          this.shareNameRepeatErr = false;
        } else {
          this.shareNameRepeatErr = true;
          this.updateNfs.nfsName = null;
        }
      }
    });
  }

  /**
   * 确认操作结果并关闭窗口
   */
  confirmActResult() {
    if (this.pluginFlag == 'plugin') {
      this.backToNfsList();
    } else {
      this.closeModel();
    }
  }

  qosFunc(form) {
    const qosTag = this.storage.storageTypeShow.qosTag;
    if (!form.qosFlag) {
      // 关闭状态
      form.contolPolicy = null;
      this.initMinInfo(form);
      this.initMaxInfo(form);
    } else {
      if (form.control_policyUpper == '1') {
        if (qosTag == 2 || qosTag == 3) {
          this.initMinInfo(form);
        }
        if (!form.maxBandwidthChoose) {
          form.maxBandwidth = null;
        }
        if (!form.maxIopsChoose) {
          form.maxIops = null;
        }
      } else {
        this.initMaxInfo(form);
      }
      if (form.control_policyLower == '0') {
        if (qosTag == 2) {
          this.initMaxInfo(form);
        } else if (qosTag == 3) {
          this.initMinInfo(form);
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
        this.initMinInfo(form);
      }

      if (form.control_policyUpper != '1' && form.control_policyLower != '0') {
        form.contolPolicy = null;
      } else if (form.control_policyUpper == '1' && form.control_policyLower != '0') {
        form.contolPolicy = 'up';
      } else if (form.control_policyUpper != '1' && form.control_policyLower == '0') {
        form.contolPolicy = 'low';
      } else {
        // all
        form.contolPolicy = 'all';
      }
    }
  }

  initMaxInfo(form) {
    form.control_policyUpper = undefined;
    form.maxBandwidthChoose = false;
    form.maxBandwidth = null;
    form.maxIopsChoose = false;
    form.maxIops = null;
  }

  initMinInfo(form) {
    form.control_policyLower = undefined;
    form.minBandwidthChoose = false;
    form.minBandwidth = null;
    form.minIopsChoose = false;
    form.minIops = null;
    form.latencyChoose = false;
    form.latency = null;
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
    let qosTag = this.storage?.storageTypeShow.qosTag || 2;

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
    this.qosV6Check('edit');
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

  qosV6Check(type: string) {
    if (type != 'add') {
      if (this.storage) {
        const qosTag = this.storage.storageTypeShow.qosTag;
        const { bandwidthLimitErr, iopsLimitErr } = getQosCheckTipsTagInfo({
          qosTag,
          minBandwidthChoose: this.updateNfs.minBandwidthChoose,
          minBandwidth: this.updateNfs.minBandwidth,
          maxBandwidthChoose: this.updateNfs.maxBandwidthChoose,
          maxBandwidth: this.updateNfs.maxBandwidth,
          minIopsChoose: this.updateNfs.minIopsChoose,
          minIops: this.updateNfs.minIops,
          maxIopsChoose: this.updateNfs.maxIopsChoose,
          maxIops: this.updateNfs.maxIops,
          control_policyUpper: this.updateNfs.control_policyUpper,
          control_policyLower: this.updateNfs.control_policyLower,
        });
        this.bandwidthLimitErr = bandwidthLimitErr;
        this.iopsLimitErr = iopsLimitErr;
      }

      /*       if (this.storage) {
        const qosTag = this.storage.storageTypeShow.qosTag;
        if (qosTag == 1) {
          if (this.updateNfs.minBandwidthChoose && this.updateNfs.maxBandwidthChoose) {
            // 带宽上限小于下限
            if (
              this.updateNfs.minBandwidth &&
              this.updateNfs.maxBandwidth &&
              Number(this.updateNfs.minBandwidth) > Number(this.updateNfs.maxBandwidth)
            ) {
              this.bandwidthLimitErr = true;
            } else {
              this.bandwidthLimitErr = false;
            }
          } else {
            this.bandwidthLimitErr = false;
          }
          if (this.updateNfs.minIopsChoose && this.updateNfs.maxIopsChoose) {
            // iops上限小于下限
            if (
              this.updateNfs.minIops &&
              this.updateNfs.maxIops &&
              Number(this.updateNfs.minIops) > Number(this.updateNfs.maxIops)
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
          this.updateNfs.maxIopsChoose &&
          this.updateNfs.maxIops &&
          Number(this.updateNfs.maxIops) < 100
        ) {
          this.iopsLimitErr = true;
        }
        if (this.updateNfs.control_policyUpper == undefined) {
          this.iopsLimitErr = false;
          this.bandwidthLimitErr = false;
        }
        if (this.updateNfs.control_policyLower == undefined) {
          this.bandwidthLimitErr = false;
        }
      }
 */
    }
  }
}
