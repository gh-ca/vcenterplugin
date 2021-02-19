import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CommonService} from '../common.service';
import {GlobalsService} from '../../shared/globals.service';
import {ClrWizard, ClrWizardPage} from "@clr/angular";

@Component({
  selector: 'app-rdm',
  templateUrl: './rdm.component.html',
  styleUrls: ['./rdm.component.scss'],
  providers: [CommonService]
})
export class RdmComponent implements OnInit {

  // 添加页面窗口
  @ViewChild('wizard') wizard: ClrWizard;
  @ViewChild('addPageOne') addPageOne: ClrWizardPage;
  @ViewChild('addPageTwo') addPageTwo: ClrWizardPage;

  serviceLevelIsNull = false;
  isOperationErr = false;
  policyEnable = {
    smartTier: false,
    qosPolicy: false,
    resourceTuning: false
  };

  configModel = new customizeVolumes();
  storageDevices:Storages[] = [];
  storagePools = [];
  ownerControllers:StorageController[] = [];
  hostList = [];
  dataStoreObjectId = '';
  levelCheck = 'level';
  dataStores = [];

  searchName = '';
  serviceLevelsRes = [];
  serviceLevels = [];
  serviceLevelId = '';

  vmObjectId = '';

  dsLoading = false;
  dsDeviceLoading = false;
  slLoading = false;
  tierLoading = false;
  submitLoading = false;
  rdmSuccess = false;
  rdmError = false;

  // 归属控制器 true 支持 false 不支持
  ownershipController = false;
  capacityInitShow=false;// 容量初始分配策略 true 支持 false 不支持
  prefetchStrategyShow=false;// 预取策略 true 支持 false 不支持
  smartTierShow=false;// SmartTier策略 true 支持 false 不支持
  allocationTypeShow = false; // 资源分配类型  true 可选thin/thick false 可选thin
  deduplicationShow = false; // 重复数据删除 true 支持 false 不支持
  compressionShow = false; // 数据压缩 true 支持 false 不支持

  compatibilityMode = 'virtualMode';// 兼容模式

  hiddenLowerFlag;// 隐藏qos下限 true隐藏、false展示
  latencyIsSelect = false; // 时延为下拉框

  // matchErr = false; // 名称校验


  constructor(private cdr: ChangeDetectorRef,
              private http: HttpClient,
              private commonService: CommonService,
              private gs: GlobalsService) { }

  ngOnInit(): void {
    this.loadStorageDevice();
    // this.loadHosts();
    this.tierFresh();
    const ctx = this.gs.getClientSdk().app.getContextObjects();
    console.log(ctx);
    if(ctx != null){
      this.vmObjectId = ctx[0].id;
    } else{
      this.vmObjectId = 'urn:vmomi:VirtualMachine:vm-1046:674908e5-ab21-4079-9cb1-596358ee5dd1';
    }
    this.loadDataStore();
  }

  // 刷新服务等级列表
  tierFresh(){
    this.tierLoading = true;
    this.http.post('servicelevel/listservicelevel', {}).subscribe((response: any) => {
      this.tierLoading = false;
      if(response.code == '200'){
        this.serviceLevelsRes = this.recursiveNullDelete(response.data);
        this.serviceLevelsRes = this.serviceLevelsRes.filter(item => item.totalCapacity !== 0);
        for (const i of this.serviceLevelsRes){
          if (i.totalCapacity == 0){
            i.usedRate = 0.0;
          } else {
            i.usedRate =  ((i.usedCapacity / i.totalCapacity * 100).toFixed(2));
          }
          i.usedCapacity = (i.usedCapacity/1024).toFixed(2);
          i.totalCapacity = (i.totalCapacity/1024).toFixed(2);
          i.freeCapacity = (i.freeCapacity/1024).toFixed(2);

          if(!i.capabilities){
            i.capabilities = {};
          }
        }
        this.search();
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  serviceLevelClick(id: string){
     this.serviceLevelId = id;
     this.serviceLevelIsNull = false;
  }

  // 服务等级列表搜索
  search(){
    if (this.searchName !== ''){
      this.serviceLevels = this.serviceLevelsRes.filter(item => item.name.indexOf(this.searchName) > -1);
    } else{
      this.serviceLevels = this.serviceLevelsRes;
    }
  }

  submit(): void {
    if (!this.ownershipController) {
      this.configModel.ownerController = '0';
    }
    const submitForm = new customizeVolumes();
    Object.assign(submitForm, this.configModel);
    // qos上下限参数处理
    this.qosFunc(submitForm);

    let body = {};

    if (submitForm.storageType == '2'){
      if(!this.policyEnable.smartTier){
        submitForm.tuning.smarttier = null;
      }
      if(!this.policyEnable.resourceTuning){
        submitForm.tuning.alloctype = null;
        submitForm.tuning.dedupeEnabled = null;
        submitForm.tuning.compressionEnabled = null;
      }
      if(!this.policyEnable.smartTier && !this.policyEnable.qosPolicy && !this.policyEnable.resourceTuning){
        submitForm.tuning = null;
      }

      body = {
        customizeVolumesRequest: {
          customizeVolumes: submitForm
        },
        compatibilityMode: this.compatibilityMode
      };
    }
    if (submitForm.storageType == '1'){
      if(this.serviceLevelId == '' || this.serviceLevelId == null){
        this.serviceLevelIsNull = true;
        return;
      }
      body = {
        createVolumesRequest: {
          serviceLevelId: this.serviceLevelId,
          volumes: submitForm.volumeSpecs
        },
        compatibilityMode: this.compatibilityMode
      };
    }
    this.submitLoading = true;
    this.http.post('v1/vmrdm/createRdm?vmObjectId='+this.vmObjectId+'&dataStoreObjectId='+this.dataStoreObjectId
      , body).subscribe((result: any) => {
        this.submitLoading = false;
        if (result.code == '200'){
          this.rdmSuccess = true;
        } else{
          this.rdmError = true;
        }
    }, err => {
      console.error('ERROR', err);
    });
  }

  recursiveNullDelete(obj: any){
    for (const property in obj){
      if (obj[property] === null){
        delete obj[property];
      } else if (obj[property] instanceof Object){
        this.recursiveNullDelete(obj[property]);
      } else if (property == 'minBandWidth' && obj[property] == 0){
        delete obj[property];
      } else if (property == 'maxBandWidth' && obj[property] == 0){
        delete obj[property];
      } else if (property == 'minIOPS' && obj[property] == 0){
        delete obj[property];
      } else if (property == 'maxIOPS' && obj[property] == 0){
        delete obj[property];
      } else if(property == 'latency' && obj[property] == 0){
        delete obj[property];
      }
    }
    return obj;
  }

  loadStorageDevice(){
    this.dsDeviceLoading = true;
    this.http.get('dmestorage/storages', {}).subscribe((result: any) => {
      this.dsDeviceLoading = false;
      if (result.code === '200'){
        this.storageDevices = result.data;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  loadStoragePool(storageId: string){
    this.slLoading = true;
    const chooseStorage = this.storageDevices.filter(item => item.id == storageId);
    // 归属控制器
    this.ownershipController = chooseStorage[0].storageTypeShow.ownershipController;
    // qos上下限
    this.addQosUpperAndLower();
    // 时延选择
    this.addLatencyChoose();
    // 容量初始分配策略
    this.capacityInitFunc();
    // 预取策略
    this.prefetchStrategyShowInit();
    // SmartTier策略
    this.smartTierShowInit();
    // 资源分配类型
    this.allocationTypeShowInit();
    // 重复数据删除
    this.deduplicationShowInit();
    // 数据压缩
    this.compressionShowInit();

    // 查询卷对应归属控制器
    if (this.ownershipController) {
      this.ownerControllers = [];
      if (storageId) {
        this.http.get('dmestorage/storagecontrollers?storageDeviceId='+storageId).subscribe((result:any) => {
          if (result.code == '200') {
            this.ownerControllers = result.data;
          }
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        });
      }
    }
    console.log("this.ownershipController", this.ownershipController);
    this.storagePools = null;
    this.configModel.poolRawId = null;
    this.http.get('dmestorage/storagepools', {params: {storageId, mediaType: "block"}}).subscribe((result: any) => {
      this.slLoading = false;
      if (result.code === '200'){
        this.storagePools = result.data;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  loadDataStore(){
    this.dsLoading = true;
    this.http.get('v1/vmrdm/vCenter/datastoreOnHost', { params: {vmObjectId : this.vmObjectId}}).subscribe((result: any) => {
      this.dsLoading = false;
      if (result.code === '200'){
        this.dataStores = JSON.parse(result.data);
      } else{
        this.dataStores = [];
      }
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    }, err => {
      console.error('ERROR', err);
    });
  }

  /**
   * 容量格式化
   * @param c 容量值
   * @param isGB true GB、false MB
   */
  formatCapacity(c: number, isGB:boolean){
    let cNum;
    if (c < 1024){
      cNum = isGB ? c.toFixed(3)+'GB':c.toFixed(3)+'MB';
    }else if(c >= 1024 && c< 1048576){
      cNum = isGB ? (c/1024).toFixed(3) + 'TB' : (c/1024).toFixed(3) + 'GB';
    }else if(c>= 1048576){
      cNum = isGB ? (c/1024/1024).toFixed(3) + 'PB':(c/1024/1024).toFixed(3) + 'TB';
    }
    return cNum;
  }

  /**
   * add 下一页
   */
  addNextPage() {
    this.wizard.next();
  }

  closeWin(){
    this.gs.getClientSdk().modal.close();
  }

  /**
   * qos开关change时间
   */
  qosChange(form){
    if(!this.policyEnable.qosPolicy) {
      form.flagInfo.control_policyLower = undefined;
      form.flagInfo.control_policyUpper = undefined;
      form.flagInfo.maxBandwidthChoose = false;
      form.flagInfo.maxIopsChoose = false;
      form.flagInfo.minBandwidthChoose = false;
      form.flagInfo.minIopsChoose = false;
      form.flagInfo.latencyChoose = false;
    }
  }

  controlPolicyChangeFunc(upperId, lowerId, form, isUpper){
    const upperObj = document.getElementById(upperId) as HTMLInputElement;
    const lowerObj = document.getElementById(lowerId) as HTMLInputElement;
    // qos策略 1 支持复选(上限、下限) 2支持单选（上限或下限） 3只支持上限
    let qosTag = this.getStorageQosTag(form.storageId);

    let upperChecked;
    if(upperObj) {
      upperChecked =  upperObj.checked;
    }
    let lowerChecked;
    if (lowerObj) {
      lowerChecked = lowerObj.checked;
    }
    if (isUpper) {
      if(upperChecked) {
        form.flagInfo.control_policyUpper = '1';
      }else {
        form.flagInfo.control_policyUpper = undefined;
      }
      if(qosTag == 2 && upperChecked) { // 单选
        form.flagInfo.control_policyLower = undefined;
        lowerObj.checked = false;
      }
    } else {
      if(lowerChecked) {
        form.flagInfo.control_policyLower = '0';
      }else {
        form.flagInfo.control_policyLower = undefined;
      }
      if (lowerChecked && qosTag == 2) {
        form.flagInfo.control_policyUpper = undefined;
        upperObj.checked = false;
      }
    }
  }
  /**
   * 获取选中的存储的 QosTag
   */
  getStorageQosTag(storageId) {
    const storageTypeShow = this.storageDevices.filter(item => item.id == storageId);
    // qos策略 1 支持复选(上限、下限) 2支持单选（上限或下限） 3只支持上限
    const qosTag = storageTypeShow[0].storageTypeShow.qosTag;
    return qosTag;
  }

  qosFunc(form) {
    console.log("form.qosFlag", form);
    if (!this.policyEnable.qosPolicy || form.storageType == '1') {// 关闭状态
      form.tuning.smartqos.controlPolicy = null;
      this.initAddMinInfo(form);
      this.initAddMaxInfo(form);
    }else {
      const qosTag = this.getStorageQosTag(form.storageId);
      if (form.flagInfo.control_policyUpper == '1') {
        if (!form.flagInfo.maxBandwidthChoose) {
          form.tuning.smartqos.maxbandwidth = null;
        }
        if (!form.flagInfo.maxIopsChoose) {
          form.tuning.smartqos.maxiops = null;
        }
        if (qosTag == 2 || qosTag == 3) {
          this.initAddMinInfo(form);
        }
      }
      if (form.flagInfo.control_policyLower == '0') {
        if(qosTag == 2){
          this.initAddMaxInfo(form);
        }else if (qosTag == 3) {
          this.initAddMinInfo(form);
        }
        if (!form.flagInfo.minBandwidthChoose) {
          form.tuning.smartqos.minbandwidth = null;
        }
        if (!form.flagInfo.minIopsChoose) {
          form.tuning.smartqos.miniops = null;
        }
        if (!form.flagInfo.latencyChoose) {
          form.tuning.smartqos.latency = null;
        }
      } else {
        this.initAddMinInfo(form);
      }
      if (form.flagInfo.control_policyUpper != '1' && form.flagInfo.control_policyLower != '0') {
        this.initAddMinInfo(form);
        this.initAddMaxInfo(form);
        form.tuning.smartqos.controlPolicy = null;
      } else if (form.flagInfo.control_policyUpper == '1' && form.flagInfo.control_policyLower != '0') {
        this.initAddMinInfo(form);
        form.tuning.smartqos.controlPolicy = '1';
      } else if (form.flagInfo.control_policyUpper != '1' && form.flagInfo.control_policyLower == '0') {
        this.initAddMaxInfo(form);
        form.tuning.smartqos.controlPolicy = '0';
      } else { // all
        form.tuning.smartqos.controlPolicy = '1';
      }
    }
  }
  initAddMinInfo(form) {
    form.flagInfo.control_policyLower = undefined;
    form.flagInfo.minBandwidthChoose = false;
    form.tuning.smartqos.minbandwidth = null;
    form.flagInfo.minIopsChoose = false;
    form.tuning.smartqos.miniops = null;
    form.flagInfo.latencyChoose = false;
    form.tuning.smartqos.latency = null;
  }
  initAddMaxInfo(form){
    form.flagInfo.control_policyUpper = undefined;
    form.flagInfo.maxBandwidthChoose = false;
    form.tuning.smartqos.maxbandwidth = null;
    form.flagInfo.maxIopsChoose = false;
    form.tuning.smartqos.maxiops = null;
  }

  /**
   * 添加页面 qos 上下限 单选、多选、隐藏
   * smartTiger 初始化
   */
  addQosUpperAndLower() {
    // qos策略 1 支持复选(上限、下限) 2支持单选（上限或下限） 3只支持上限
    const qosTag = this.getStorageQosTag(this.configModel.storageId);
    this.configModel.flagInfo.control_policyLower = undefined;
    this.configModel.flagInfo.control_policyUpper = undefined;
    const upperObj = document.getElementById("control_policyUpper") as HTMLInputElement;
    const lowerObj = document.getElementById("control_policyLower") as HTMLInputElement;
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
   * 添加页面 时延为下拉框
   */
  addLatencyChoose(){
    this.configModel.tuning.smartqos.latency = null;
    const qosTag = this.getStorageQosTag(this.configModel.storageId);
    this.latencyIsSelect = qosTag == 1;
  }

  /**
   * 切换存储：容量初始分配策略初始化
   */
  capacityInitFunc(){
    this.configModel.initialDistributePolicy = '0';
    const storageTypeShow = this.storageDevices.filter(item => item.id == this.configModel.storageId);
    this.capacityInitShow = storageTypeShow[0].storageTypeShow.capacityInitialAllocation;
  }

  /**
   * 切换存储：预取策略初始化
   */
  prefetchStrategyShowInit() {
    this.configModel.prefetchPolicy = '0';
    const storageTypeShow = this.storageDevices.filter(item => item.id == this.configModel.storageId);
    this.prefetchStrategyShow = storageTypeShow[0].storageTypeShow.prefetchStrategyShow;
  }

  /**
   * 切换存储：SmartTier策略初始化
   */
  smartTierShowInit() {
    this.configModel.tuning.smarttier = '0';
    const storageTypeShow = this.storageDevices.filter(item => item.id == this.configModel.storageId);
    this.smartTierShow =  storageTypeShow[0].storageTypeShow.smartTierShow;
  }

  /**
   * 切换存储：资源分配类型初始化
   */
  allocationTypeShowInit() {
    this.configModel.tuning.alloctype = null;
    const storageTypeShow = this.storageDevices.filter(item => item.id == this.configModel.storageId);
    this.allocationTypeShow =  storageTypeShow[0].storageTypeShow.allocationTypeShow == 1;
  }

  /**
   * 切换存储：重复数据删除初始化
   */
  deduplicationShowInit(){
    this.configModel.tuning.dedupeEnabled = null;
    const storageTypeShow = this.storageDevices.filter(item => item.id == this.configModel.storageId);
    this.deduplicationShow =  storageTypeShow[0].storageTypeShow.deduplicationShow;
  }

  /**
   * 切换存储：数据压缩初始化
   */
  compressionShowInit() {
    this.configModel.tuning.compressionEnabled = null;
    const storageTypeShow = this.storageDevices.filter(item => item.id == this.configModel.storageId);
    this.compressionShow = storageTypeShow[0].storageTypeShow.compressionShow;
  }

  /**
   * 名称校验
   */
  nameCheck() {
    const name = this.configModel.volumeSpecs[0].name;
    let reg5:RegExp = new RegExp('^[0-9a-zA-Z-\u4e00-\u9fa5a"_""."]*$');
    if(reg5.test(name)){
      //验证重复
      // this.matchErr=false;
    } else {
      //验证重复
      // this.matchErr=true;
      this.configModel.volumeSpecs[0].name = null;
    }
  }
}


class customizeVolumes{
  storageType: string;
  availabilityZone: string;
  initialDistributePolicy: string;
  ownerController: string;
  poolRawId: string;
  prefetchPolicy: string;
  prefetchValue: string;
  storageId: string;
  tuning: tuning;
  flagInfo:FlagInfo; // 复选框标志
  volumeSpecs: volumeSpecs[];
  constructor(){
    this.storageType = '1';
    this.volumeSpecs = [new volumeSpecs()];
    this.tuning = new tuning();
    this.initialDistributePolicy = '0';
    this.ownerController = '0';
    this.prefetchPolicy = '3';
    this.flagInfo = new FlagInfo();
  }
}
class FlagInfo {
  control_policyLower:string;
  minBandwidthChoose:boolean;
  minIopsChoose:boolean;
  latencyChoose:boolean;

  control_policyUpper:string;
  maxBandwidthChoose:boolean;
  maxIopsChoose:boolean;

  constructor(){
    this.control_policyUpper = undefined;
    this.control_policyLower = undefined;
    this.maxBandwidthChoose = false;
    this.maxIopsChoose = false;
    this.minBandwidthChoose = false;
    this.minIopsChoose = false;
    this.latencyChoose = false;
  }
}

class volumeSpecs{
  capacity: number;
  count: number;
  name: string;
  unit: string;
  constructor(){
     this.name = '';
     this.capacity = null;
     this.count = 1;
     this.unit = 'GB';
  }
}


class tuning{
  alloctype:string;
  compressionEnabled: boolean;
  dedupeEnabled: boolean;
  smartqos: smartqos;
  smarttier: string;
  workloadTypeId: string;
  constructor(){
    this.smartqos = new smartqos();
    this.alloctype = 'thick';
    this.smarttier = '0';
    this.compressionEnabled = null;
    this.dedupeEnabled = null;
  }
}

class smartqos{
  controlPolicy: string;
  latency: number;
  maxbandwidth: number;
  maxiops: number;
  minbandwidth: number;
  miniops: number;
  name:string;
  constructor(){
    this.controlPolicy = '1'
  }
}
// 存储
export interface Storages {
  name: string;
  id: string;
  storageTypeShow:StorageTypeShow;
}
export interface StorageTypeShow {
  dorado:boolean; // true 是dorado v6.1版本及高版本 false 是dorado v 6.0版本及更低版本
  qosTag:number;// qos策略 1 支持复选(上限、下限) 2支持单选（上限或下限） 3只支持上限
  workLoadShow:number;// 1 支持应用类型 2不支持应用类型
  ownershipController:boolean;// 归属控制器 true 支持 false 不支持
  allocationTypeShow:number;// 资源分配类型  1 可选thin/thick 2 可选thin
  deduplicationShow:boolean;// 重复数据删除 true 支持 false 不支持
  compressionShow:boolean; // 数据压缩 true 支持 false 不支持
  capacityInitialAllocation:boolean;// 容量初始分配策略 true 支持 false 不支持
  smartTierShow:boolean;// SmartTier策略 true 支持 false 不支持
  prefetchStrategyShow:boolean;// 预取策略 true 支持 false 不支持
  storageDetailTag:number;// 存储详情下展示情况 1 仅展示存储池和lun 2 展示存储池/lun/文件系统/共享/dtree
}
export class StorageController{
  name:string;
  status:string;
  softVer:string;
  cpuInfo:string;
  cpuUsage:number;
  iops:number;
  ops:number;
  bandwith:number;
  lantency:number;
}
