import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ModifyService } from './modify.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import {
  GetForm,
  ServiceLevelList,
  StorageList,
  VmfsInfo,
  VmfsListService,
} from '../list/list.service';
import { GlobalsService } from '../../../shared/globals.service';
import {
  getQosCheckTipsTagInfo,
  getQueryParams,
  getURL,
  handlerResponseErrorSimple,
  isStringLengthByteOutRange,
  regExpCollection,
} from 'app/app.helpers';
import { isMockData, mockData } from 'mock/mock';
import { getVmfsDmestorageStorageByTag } from 'mock/VMFS_DMESTORAGE_STORAGE';

@Component({
  selector: 'app-list',
  templateUrl: './modify.component.html',
  styleUrls: ['./modify.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [ModifyService, VmfsListService],
})
export class ModifyComponent implements OnInit {
  constructor(
    private vmfsListServece: VmfsListService,
    private remoteSrv: ModifyService,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef,
    private router: Router,
    private globalsService: GlobalsService
  ) {}

  dorado = false; //是否是V6设备

  // 编辑form提交数据
  modifyForm = new GetForm().getEditForm();
  // 是否为服务等级 true:是 false:否 若为是则不显示控制策略以及交通管制对象
  isServiceLevelData = true;

  // 存储ID
  objectId;
  // vmfs数据
  vmfsInfo: VmfsInfo;

  // 操作来源 list:列表页面、dataStore：在DataStore菜单页面操作
  resource;

  // 编辑窗口隐藏于展示
  modifyShow = false;
  modalHandleLoading = false; // 数据处理loading
  modalLoading = false; // 数据加载loading
  nameChecking = false; // 名称校验
  isOperationErr = false; // 错误信息
  modifySuccessShow = false; // 编辑程功窗口

  matchErr = false; // 名称校验 是否只由字母与数字组成 true：是 false 否
  vmfsNameRepeatErr = false; // vmfs名称是否重复 true：是 false 否
  volNameRepeatErr = false; // Vol名称是否重复 true：是 false 否
  modifyNameChanged = false;
  storage: StorageList; // 存储详情（编辑页面使用参数）

  showLowerFlag = false;
  showSmartTierFlag = false;
  showAlloctypeThick = false; // 资源调优option全部展示
  showWorkLoadFlag = false; // 应用类型展示
  latencyIsSelect = false; // 时延为下拉框

  bandWidthMaxErrTips = false; // 带宽上限错误提示
  bandWidthMinErrTips = false; // 带宽下限错误提示
  iopsMaxErrTips = false; // IOPS上限错误提示
  iopsMinErrTips = false; // IOPS下限错误提示
  latencyErrTips = false; // 时延错误提示

  bandwidthLimitErr = false; // v6 设备 带宽 下限大于上限
  iopsLimitErr = false; // v6 设备 IOPS 下限大于上限

  ngOnInit(): void {
    this.initData();
  }

  async initData() {
    this.modifyShow = true;
    this.modalLoading = true;
    this.modalHandleLoading = false;
    this.isOperationErr = false;

    // 名称错误提示初始化
    this.vmfsNameRepeatErr = false;
    this.volNameRepeatErr = false;
    this.matchErr = false;

    // 设备类型 操作类型初始化
    const queryParam: Params = await getQueryParams(this);
    this.resource = queryParam.resource;
    if (this.resource === 'list') {
      this.objectId = queryParam.objectId;
    } else {
      const ctx = this.globalsService.getClientSdk().app.getContextObjects();
      this.objectId = ctx
        ? ctx[0].id
        : 'urn:vmomi:Datastore:datastore-10076:674908e5-ab21-4079-9cb1-596358ee5dd1';
    }

    // 获取vmfs数据
    const handlerGetVmfsByIdSuccess = (result: any) => {
      console.log('result:', result);
      if (result.code === '200' && null != result.data) {
        this.vmfsInfo = result.data.filter(item => item.objectid === this.objectId)[0];
        if (isMockData) {
          this.vmfsInfo = result.data[0];
        }
        // 初始化form表单
        this.modifyForm = new GetForm().getEditForm();
        this.modifyForm.name = this.vmfsInfo?.name;
        this.modifyForm.volumeId = this.vmfsInfo.volumeId;
        this.modifyForm.oldDsName = this.vmfsInfo?.name;
        this.modifyForm.dataStoreObjectId = this.vmfsInfo.objectid;
        this.modifyForm.service_level_name = this.vmfsInfo.serviceLevelName;
        if (this.vmfsInfo.serviceLevelName === '') {
          // 非服务等级
          this.isServiceLevelData = false;
          this.modifyForm.control_policy = '';
          const wwns = [];
          wwns.push(this.vmfsInfo.wwn);

          this.modifyForm.max_bandwidth = this.vmfsInfo.maxBandwidth;
          this.modifyForm.max_iops = this.vmfsInfo.maxIops;
          this.modifyForm.min_iops = this.vmfsInfo.minIops;
          this.modifyForm.min_bandwidth = this.vmfsInfo.minBandwidth;
          this.modifyForm.latency = this.vmfsInfo.latency;
          if (
            this.modifyForm.latency ||
            this.modifyForm.min_bandwidth ||
            this.modifyForm.min_iops ||
            this.modifyForm.max_bandwidth ||
            this.modifyForm.max_iops
          ) {
            this.modifyForm.qosFlag = true;
          }
          this.modifyGetStorage(this.objectId);

          this.remoteSrv.getChartData(wwns).subscribe((chartResult: any) => {
            console.log('chartResult', chartResult);
            console.log('chartResult', chartResult.code === '200' && chartResult.data != null);
            if (chartResult.code === '200' && chartResult.data != null) {
              const chartList: VmfsInfo = chartResult.data[0];
            }
          });
        } else {
          this.isServiceLevelData = true;
        }
      }
      this.modalLoading = false;
      console.log('this.modifyForm:', this.modifyForm);
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    };
    if (isMockData) {
      handlerGetVmfsByIdSuccess(mockData.VMFS_ACCESSVMFS_QUERYVMFS);
    } else {
      this.remoteSrv.getVmfsById(this.objectId).subscribe(handlerGetVmfsByIdSuccess);
    }
    this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
  }

  /**
   * 取消
   */
  cancel() {
    console.log('this.resource', this.resource);
    this.modifyShow = false;
    if (this.resource === 'list') {
      // 列表入口
      this.router.navigate(['vmfs/list']);
    } else {
      // dataStore入口
      this.globalsService.getClientSdk().modal.close();
    }
  }

  /**
   * 修改
   */
  modifyHandleFunc() {
    if (
      this.bandWidthMaxErrTips ||
      this.iopsMaxErrTips ||
      this.bandWidthMinErrTips ||
      this.iopsMinErrTips ||
      this.latencyErrTips
    ) {
      return;
    }
    // 设置修改的卷名称以及修改后的名称
    if (this.modifyForm.isSameName) {
      this.modifyForm.newVoName = this.modifyForm.name;
    } else {
      // 若不修改卷名称则将旧的卷名称设置为newVol
      this.modifyForm.newVoName = this.vmfsInfo.volumeName;
    }
    if (!this.isServiceLevelData) {
      // 控制策略若未选清空数据
      this.qosEditFunc(this.modifyForm);
      if (this.modifyForm.control_policyUpper == '1') {
        // 上限+全选（上下限）
        this.modifyForm.control_policy = '1';
      } else if (this.modifyForm.control_policyLower == '0') {
        // 下限
        this.modifyForm.control_policy = '0';
      } else {
        this.modifyForm.control_policy = null;
      }
    }
    this.modifyForm.newDsName = this.modifyForm.name;
    console.log('this.modifyForm:', this.modifyForm);
    this.modalHandleLoading = true;
    this.remoteSrv
      .updateVmfs(this.modifyForm.volumeId, this.modifyForm)
      .subscribe((result: any) => {
        this.modalHandleLoading = false;
        if (result.code === '200') {
          console.log('modify success:' + this.modifyForm.oldDsName);
          this.modifySuccessShow = true;
        } else {
          console.log('modify faild：' + this.modifyForm.oldDsName + result.description);
          this.isOperationErr = true;
        }
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      });
  }

  /**
   * 带宽 blur
   * @param type
   * @param operationType add modify
   * @param valType
   */
  qosBlur(type: String, operationType: string) {
    let objVal;
    switch (operationType) {
      case 'max_bandwidth':
        objVal = this.modifyForm.max_bandwidth;
        break;
      case 'max_iops':
        objVal = this.modifyForm.max_iops;
        break;
      case 'min_bandwidth':
        objVal = this.modifyForm.min_bandwidth;
        break;
      case 'min_iops':
        objVal = this.modifyForm.min_iops;
        break;
      default:
        objVal = this.modifyForm.latency;
        break;
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
    switch (operationType) {
      case 'max_bandwidth':
        this.modifyForm.max_bandwidth = objVal;
        break;
      case 'max_iops':
        this.modifyForm.max_iops = objVal;
        break;
      case 'min_bandwidth':
        this.modifyForm.min_bandwidth = objVal;
        break;
      case 'min_iops':
        this.modifyForm.min_iops = objVal;
        break;
      default:
        this.modifyForm.latency = objVal;
        break;
    }
    this.iopsErrTips(objVal, operationType);
    // 下限大于上限 检测
    this.qosV6Check('edit');
  }

  /**
   * 确认操作结果并关闭窗口
   */
  confirmActResult() {
    this.cancel();
  }

  /**
   * 编辑功能名称校验
   */
  modifyNameCheck() {
    this.vmfsNameRepeatErr = false;
    this.volNameRepeatErr = false;
    this.matchErr = false;

    let reg5: RegExp = regExpCollection.vmfsName();
    // let reg5: RegExp = new RegExp('^[0-9a-zA-Z-\u4e00-\u9fa5a"_""."]*$');
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

    if (this.modifyForm.name) {
      const inLimit = !isStringLengthByteOutRange(this.modifyForm.name, 27);
      if (reg5.test(this.modifyForm.name) && inLimit) {
        // 校验VMFS名称重复
        if (this.modifyNameChanged) {
          // this.modalHandleLoading = true;
          this.nameChecking = true;
          this.remoteSrv.checkVmfsName(this.modifyForm.name).subscribe((result: any) => {
            // this.modalHandleLoading = false;
            if (result.code === '200') {
              // result.data true 不重复 false 重复
              this.vmfsNameRepeatErr = !result.data;
              if (this.vmfsNameRepeatErr) {
                // 名称重复
                this.modifyForm.name = null;
                this.volNameRepeatErr = false;
                this.matchErr = false;
                this.nameChecking = false;
              } else {
                if (this.modifyForm.isSameName) {
                  // this.modalHandleLoading = true;
                  // 校验VMFS名称重复
                  this.remoteSrv.checkVolName(this.modifyForm.name).subscribe((result: any) => {
                    // this.modalHandleLoading = false;
                    if (result.code === '200') {
                      // result.data true 不重复 false 重复
                      this.volNameRepeatErr = !result.data;
                      if (!this.vmfsNameRepeatErr && this.volNameRepeatErr) {
                        this.modifyForm.name = null;
                      }
                      if (this.volNameRepeatErr) {
                        this.modifyForm.name = null;
                        this.vmfsNameRepeatErr = false;
                        this.matchErr = false;
                      } else {
                        // 数据提交
                        this.modifyHandleFunc();
                      }
                    }
                    this.nameChecking = false;
                    this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
                  });
                } else {
                  // 数据提交
                  this.modifyHandleFunc();
                }
              }
            }
            this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
          });
        } else {
          this.modifyHandleFunc();
        }
      } else {
        this.matchErr = true;
        this.modifyForm.name = null;
      }
    }
    console.log(
      'this.vmfsNameRepeatErr, this.volNameRepeatErr',
      this.vmfsNameRepeatErr,
      this.volNameRepeatErr
    );
  }

  /**
   * 编辑页面  名称变化Func
   */
  modifyNameChange() {
    console.log('this.modifyForm.name', this.modifyForm.name);
    const inLimit = !isStringLengthByteOutRange(this.modifyForm.name, 27);
    if (!(regExpCollection.vmfsName().test(this.modifyForm.name) && inLimit)) {
      this.matchErr = true;
      this.modifyForm.name = null;
    }
    const oldName = this.vmfsInfo.volumeName;
    if (oldName !== this.modifyForm.name) {
      this.modifyNameChanged = true;
    } else {
      this.modifyNameChanged = false;
    }
  }

  qosEditFunc(form) {
    console.log('editform.qosFlag', form.qosFlag);
    const qosTag = this.storage.storageTypeShow.qosTag;
    if (!form.qosFlag) {
      // 关闭状态
      this.initEditMinInfo(form);
      this.initEditMaxInfo(form);
    } else {
      if (form.control_policyUpper == '1') {
        if (!form.maxbandwidthChoose) {
          form.max_bandwidth = null;
        }
        if (!form.maxiopsChoose) {
          form.max_iops = null;
        }
        if (qosTag == 2 || qosTag == 3) {
          this.initEditMinInfo(form);
        }
      } else {
        this.initEditMaxInfo(form);
      }
      if (form.control_policyLower == '0') {
        if (qosTag == 2) {
          this.initEditMaxInfo(form);
        } else if (qosTag == 3) {
          this.initEditMinInfo(form);
        }
        if (!form.minbandwidthChoose) {
          form.min_bandwidth = null;
        }
        if (!form.miniopsChoose) {
          form.min_iops = null;
        }
        if (!form.latencyChoose) {
          form.latency = null;
        }
      } else {
        this.initEditMinInfo(form);
      }
      if (form.control_policyUpper != '1' && form.control_policyLower != '0') {
        this.initEditMinInfo(form);
        this.initEditMaxInfo(form);
        form.control_policy = null;
      }
    }
  }

  initEditMinInfo(form) {
    form.control_policyLower = undefined;
    form.minbandwidthChoose = false;
    form.min_bandwidth = null;
    form.miniopsChoose = false;
    form.min_iops = null;
    form.latencyChoose = false;
    form.latency = null;
  }

  initEditMaxInfo(form) {
    form.control_policyUpper = undefined;
    form.maxiopsChoose = false;
    form.max_iops = null;
    form.maxbandwidthChoose = false;
    form.max_bandwidth = null;
  }

  /**
   * edit qos开关
   * @param form
   */
  qoSEditFlagChange(form) {
    if (form.qosFlag) {
      form.control_policyUpper = undefined;
      form.maxbandwidthChoose = false;
      form.maxiopsChoose = false;

      form.control_policyLower = undefined;
      form.minbandwidthChoose = false;
      form.miniopsChoose = false;
      form.latencyChoose = false;
    }
  }

  /**
   * 修改页面获取存储数据
   * @param objectId
   */
  async modifyGetStorage(objectId) {
    this.modalHandleLoading = true;
    if (objectId) {
      const handlerGetStorageDetailSuccess = (result: any) => {
        this.modalHandleLoading = false;
        if (result.code == '200') {
          this.storage = result.data;
          const storageTypeShow = this.storage.storageTypeShow;
          /* 是否打开qos switch qosFlog */
          this.modifyForm.qosFlag = this.storage.qosFlag;
          if (storageTypeShow) {
            // smartTier 展示与隐藏
            const smartTierShow = storageTypeShow.smartTierShow;
            this.showSmartTierFlag = smartTierShow;
            // qos策略 1 支持复选(上限、下限) 2支持单选（上限或下限） 3只支持上限
            const qosTag = storageTypeShow.qosTag;
            if (qosTag == 3) {
              this.showLowerFlag = true;
            } else {
              this.showLowerFlag = false;
            }
            this.latencyIsSelect = qosTag == 1;
            this.modifyForm.isCheckedUpper = false;
            this.modifyForm.isCheckedlower = false;
            const upperObj = document.getElementById('editControl_policyUpper') as HTMLInputElement;
            const lowerObj = document.getElementById('editControl_policyLower') as HTMLInputElement;
            /* 根据返回的数据判断是否需要展示 */
            const smartQos = this.storage.smartQos || {};
            const { latency, maxbandwidth, maxiops, minbandwidth, miniops } = smartQos as any;

            this.modifyForm.max_iops = maxiops;
            this.modifyForm.max_bandwidth = maxbandwidth;
            this.modifyForm.min_iops = miniops;
            this.modifyForm.min_bandwidth = minbandwidth;
            this.modifyForm.latency = latency;
            if (this.modifyForm.max_iops || this.modifyForm.max_bandwidth) {
              if (this.modifyForm.max_iops) {
                this.modifyForm.maxiopsChoose = true;
              }
              if (this.modifyForm.max_bandwidth) {
                this.modifyForm.maxbandwidthChoose = true;
              }

              this.modifyForm.isCheckedUpper = true;
              if (upperObj) {
                upperObj.checked = true;
              }
              this.handleModifyControlPolicyChange(true, this.modifyForm, true);
            }

            if (
              this.modifyForm.min_iops ||
              this.modifyForm.min_bandwidth ||
              this.modifyForm.latency
            ) {
              if (this.modifyForm.min_iops) {
                this.modifyForm.miniopsChoose = true;
              }
              if (this.modifyForm.min_bandwidth) {
                this.modifyForm.minbandwidthChoose = true;
              }
              if (this.modifyForm.latency) {
                this.modifyForm.latencyChoose = true;
              }

              this.modifyForm.isCheckedlower = true;
              if (lowerObj) {
                lowerObj.checked = true;
              }

              this.handleModifyControlPolicyChange(true, this.modifyForm, false);
            }
            if (smartTierShow) {
              if (this.vmfsInfo.smartTier) {
                this.modifyForm.smartTierFlag = true;
                this.modifyForm.smartTier = this.vmfsInfo.smartTier;
              }
            }
          }
        }
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      };

      if (isMockData) {
        /* 每次动态切换 qoSFlag qosTag  */
        const a: any = this.modifyGetStorage;
        a.count = a.count || 1;
        const count = a.count++ % 4;
        const res = getVmfsDmestorageStorageByTag(count);
        console.log(res);
        handlerGetStorageDetailSuccess(res);
      } else {
        try {
          const res = await this.vmfsListServece.asyncGetStoragesVmfsInfo(objectId);
          handlerGetStorageDetailSuccess(res);
        } catch (error) {
          handlerResponseErrorSimple(error);
        }
      }
    }
  }

  /**
   * 控制策略变更
   * @param upperObj
   * @param lowerObj
   * @param isUpper true:upper、false:lower
   */

  handleModifyControlPolicyChange(isEdit, form, isUpper) {
    // qos策略 1 支持复选(上限、下限) 2支持单选（上限或下限） 3只支持上限
    let qosTag = this.storage.storageTypeShow.qosTag;

    let upperChecked = this.modifyForm.isCheckedUpper;
    let lowerChecked = this.modifyForm.isCheckedlower;

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
        this.modifyForm.isCheckedlower = false;
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
        this.modifyForm.isCheckedUpper = false;
      }
    }
    if (form.control_policyUpper == undefined) {
      form.maxbandwidthChoose = false;
      form.maxiopsChoose = false;
    }
    if (form.control_policyLower == undefined) {
      form.minbandwidthChoose = false;
      form.miniopsChoose = false;
      form.latencyChoose = false;
    }
    this.qosV6Check('edit');
  }

  controlPolicyChangeFunc(upperId, lowerId, isEdit, form, isUpper) {
    const upperObj = document.getElementById(upperId) as HTMLInputElement;
    const lowerObj = document.getElementById(lowerId) as HTMLInputElement;
    // qos策略 1 支持复选(上限、下限) 2支持单选（上限或下限） 3只支持上限
    let qosTag = this.storage.storageTypeShow.qosTag;

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
    if (form.control_policyUpper == undefined) {
      form.maxbandwidthChoose = false;
      form.maxiopsChoose = false;
    }
    if (form.control_policyLower == undefined) {
      form.minbandwidthChoose = false;
      form.miniopsChoose = false;
      form.latencyChoose = false;
    }
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
        case 'max_bandwidth':
          if (objVal == '' && this.modifyForm.maxbandwidthChoose) {
            this.bandWidthMaxErrTips = true;
          } else {
            this.bandWidthMaxErrTips = false;
          }
          break;
        case 'max_iops':
          if (objVal == '' && this.modifyForm.maxiopsChoose) {
            this.iopsMaxErrTips = true;
          } else {
            this.iopsMaxErrTips = false;
          }
          break;
        case 'min_bandwidth':
          if (objVal == '' && this.modifyForm.minbandwidthChoose) {
            this.bandWidthMinErrTips = true;
          } else {
            this.bandWidthMinErrTips = false;
          }
          break;
        case 'min_iops':
          if (objVal == '' && this.modifyForm.miniopsChoose) {
            this.iopsMinErrTips = true;
          } else {
            this.iopsMinErrTips = false;
          }
          break;
        default:
          if (objVal == '' && this.modifyForm.latencyChoose) {
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
        this.dorado = String(qosTag) === '1';
        const { bandwidthLimitErr, iopsLimitErr } = getQosCheckTipsTagInfo({
          qosTag,
          minBandwidthChoose: this.modifyForm.minbandwidthChoose,
          minBandwidth: this.modifyForm.min_bandwidth,
          maxBandwidthChoose: this.modifyForm.maxbandwidthChoose,
          maxBandwidth: this.modifyForm.max_bandwidth,
          minIopsChoose: this.modifyForm.miniopsChoose,
          minIops: this.modifyForm.min_iops,
          maxIopsChoose: this.modifyForm.maxiopsChoose,
          maxIops: this.modifyForm.max_iops,
          control_policyUpper: this.modifyForm.control_policyUpper,
          control_policyLower: this.modifyForm.control_policyLower,
        });
        this.bandwidthLimitErr = bandwidthLimitErr;
        this.iopsLimitErr = iopsLimitErr;

        /* 
        
        
        
        const qosTag = this.storage.storageTypeShow.qosTag;
        if (qosTag == 1) {
          if (this.modifyForm.minbandwidthChoose && this.modifyForm.maxbandwidthChoose) {
            // 带宽上限小于下限
            if (
              this.modifyForm.min_bandwidth &&
              this.modifyForm.max_bandwidth &&
              Number(this.modifyForm.min_bandwidth) > Number(this.modifyForm.max_bandwidth)
            ) {
              this.bandwidthLimitErr = true;
            } else {
              this.bandwidthLimitErr = false;
            }
          } else {
            this.bandwidthLimitErr = false;
          }
          if (this.modifyForm.miniopsChoose && this.modifyForm.maxiopsChoose) {
            // iops上限小于下限
            if (
              this.modifyForm.min_iops &&
              this.modifyForm.max_iops &&
              Number(this.modifyForm.min_iops) > Number(this.modifyForm.max_iops)
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
          this.modifyForm.maxiopsChoose &&
          this.modifyForm.max_iops &&
          Number(this.modifyForm.max_iops) < 100
        ) {
          this.iopsLimitErr = true;
        }
        if (this.modifyForm.control_policyUpper == undefined) {
          this.iopsLimitErr = false;
          this.bandwidthLimitErr = false;
        }
        if (this.modifyForm.control_policyLower == undefined) {
          this.bandwidthLimitErr = false;
        }
      
      
      
        */
      }
    }
  }
}
