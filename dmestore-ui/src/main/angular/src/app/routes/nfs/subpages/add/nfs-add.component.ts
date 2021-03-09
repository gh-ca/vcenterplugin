import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit, ViewChild} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AddNfs, Host, NfsAddService, Vmkernel} from "./nfs-add.service";
import {ClrWizard, ClrWizardPage} from "@clr/angular";
import {LogicPort, StorageList, StorageService} from "../../../storage/storage.service";
import {StoragePool, StoragePoolMap} from "../../../storage/detail/detail.service";
@Component({
  selector: 'app-add',
  templateUrl: './nfs-add.component.html',
  styleUrls: ['./nfs-add.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [NfsAddService,StorageService]
})
export class NfsAddComponent implements OnInit{
  viewPage: string;
  pluginFlag: string;
  addForm = new AddNfs();
  errorMsg: string;
  modalLoading = false; // 数据加载loading
  modalHandleLoading = false; // 数据处理loading
  unit='GB';
  checkedPool:any;
  storageList: StorageList[] = [];
  storagePools: StoragePool[] = [];
  storagePoolMap:StoragePoolMap[] = [];

  logicPorts: LogicPort[] = [];
  hostList: Host[] = [];
  vmkernelList: Vmkernel[]=[];
  capacityErr=true;


  maxbandwidthChoose=false; // 最大带宽 选中
  maxiopsChoose=false; // 最大iops 选中
  minbandwidthChoose=false; // 最小带宽 选中
  miniopsChoose=false; // 最小iops 选中
  latencyChoose=false; // 时延 选中
  addSuccessShow = false; // 添加成功提示

  hiddenLowerFlag = false; // 不支持下限 true是 false否
  deduplicationShow = false; // 重复数据删除 true 支持 false 不支持
  compressionShow = false; // 数据压缩 true 支持 false 不支持
  latencyIsSelect = false; // 时延为下拉框
  dorado= false;
  shareNameContainsCN = false; // 共享名称包含中文

  bandWidthMaxErrTips = false;// 带宽上限错误提示
  bandWidthMinErrTips = false;// 带宽下限错误提示
  iopsMaxErrTips = false;// IOPS上限错误提示
  iopsMinErrTips = false;// IOPS下限错误提示
  latencyErrTips = false;// 时延错误提示

  // 添加页面窗口
  @ViewChild('wizard') wizard: ClrWizard;
  @ViewChild('addPageOne') addPageOne: ClrWizardPage;
  @ViewChild('addPageTwo') addPageTwo: ClrWizardPage;

  constructor(private addService: NfsAddService, private cdr: ChangeDetectorRef,
              private gs: GlobalsService,private storageService: StorageService,
              private activatedRoute: ActivatedRoute,private router:Router){
  }

  ngOnInit(): void {
    this.modalLoading=true;
    this.viewPage='add_plugin'
    this.activatedRoute.queryParams.subscribe(queryParam => {
      this.pluginFlag =queryParam.flag;
    });
    if(this.pluginFlag==null){
      //入口来至Vcenter
      this.viewPage='delete_vcenter'
    }

    this.storagePoolMap = [];

    //入口是DataSource
    this.viewPage='add_plugin'
    this.activatedRoute.queryParams.subscribe(queryParam => {
      this.pluginFlag =queryParam.flag;
    });
    if(this.pluginFlag==null){
      //入口来至Vcenter
      this.viewPage='add_vcenter'
    }
    this.storageService.getData().subscribe((s: any) => {
      this.modalLoading=false;
      if (s.code === '200'){
        this.storageList = s.data;

        const allPoolMap:StoragePoolMap[] = []

        s.data.forEach(item  => {
          const poolMap:StoragePoolMap = {
            storageId:item.id,
            storagePoolList:null,
            logicPort:null
          }
          allPoolMap.push(poolMap);
        });

        this.storagePoolMap = allPoolMap;

        this.modalLoading=false;
      }
    });
    this.addService.getHostList().subscribe((r: any) => {
      this.modalLoading=false;
      if (r.code === '200'){
        this.hostList = r.data;
        this.cdr.detectChanges();
      }
    });
    this.addForm = new AddNfs();
    this.checkedPool= null;
    this.errorMsg='';
    // 获取存储列表
    this.cdr.detectChanges();
  }
  jumpTo(page: ClrWizardPage, wizard: ClrWizard) {
    console.log('wizard:')
    console.log(this.wizard)
    if (page && page.completed) {
      wizard.navService.currentPage = page;
    } else {
      wizard.navService.setLastEnabledPageCurrent();
    }
    this.wizard.open();
  }
  addNfs(){
    //
    this.modalHandleLoading=true;

    this.checkedPool = this.storagePools.filter(item => item.id === this.addForm.storagePoolId)[0];

    this.addForm.poolRawId=this.checkedPool.poolId;
    this.addForm.storagePoolId= this.checkedPool.id;
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
      default: // 默认GB 不变
        break;
    }
    this.qosFunc(addSubmitForm);
    // 重删压缩处理
    if (!addSubmitForm.thin) {
      addSubmitForm.deduplicationEnabled = null;
      addSubmitForm.compressionEnabled = null;
    } else {
      if (addSubmitForm.deduplicationEnabled != null && addSubmitForm.deduplicationEnabled.toString()) {
        addSubmitForm.deduplicationEnabled = addSubmitForm.deduplicationEnabled.toString() == 'true';
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
      this.modalHandleLoading=false;
      if (result.code === '200'){
        // 打开成功提示窗口
        this.addSuccessShow = true;
      }else{
        this.errorMsg = '1';
        console.log("Delete failed:",result.description)
      }
      this.cdr.detectChanges();
    });
  }
  selectStoragePool(){
    this.modalLoading=true;
    this.storagePools = [];
    this.addForm.storagePoolId = undefined;
    this.logicPorts = [];
    this.addForm.currentPortId = undefined;
    if (this.addForm.storagId) {

      this.addQosUpperAndLower();
      this.addCompressionShow();
      this.addDeduplicationShow();
      this.addLatencyChoose();
      const storages=this.storageList.filter(item=>item.id==this.addForm.storagId);
      this.dorado=storages[0].storageTypeShow.dorado;
      if (this.dorado){
        this.addForm.autoSizeEnable=undefined;
      }
      const storagePoolMap = this.storagePoolMap.filter(item => item.storageId == this.addForm.storagId);

      const storagePoolList = storagePoolMap[0].storagePoolList;
      const logicPorts = storagePoolMap[0].logicPort;

      // 选择存储后获取存储池
      // if (!storagePoolList) {
        this.storageService.getStoragePoolListByStorageId("file",this.addForm.storagId)
          .subscribe((r: any) => {
            if (r.code === '200'){
              this.storagePools = r.data;
            }
            this.cdr.detectChanges();
          });
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
  selectLogicPort(){
    // 选择存储后逻辑端口
    this.storageService.getLogicPortListByStorageId(this.addForm.storagId)
      .subscribe((r: any) => {
        this.modalLoading=false;
        if (r.code === '200'){
          this.logicPorts = r.data;
          this.storagePoolMap.filter(item => item.storageId == this.addForm.storagId)[0].logicPort = r.data;
        }
        this.cdr.detectChanges();
      });
  }
  checkHost(){
    this.modalLoading=true;

    //选择主机后获取虚拟网卡
    this.addService.getVmkernelListByObjectId(this.addForm.hostObjectId)
      .subscribe((r: any) => {
        this.modalLoading=false;
        if (r.code === '200'){
          this.vmkernelList = r.data;
          this.addForm.vkernelIp=this.vmkernelList[0].ipAddress;
        }
        this.cdr.detectChanges();
      });
  }
  backToNfsList(){
    this.modalLoading=false;
    this.router.navigate(['nfs']);
  }
  closeModel(){
    this.modalLoading=false;
    this.gs.getClientSdk().modal.close();
  }
  qosBlur(type:String, operationType:string) {

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
    if (objVal > 999999999){
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
  }
  /**
   * iops错误提示
   * @param objVal
   * @param operationType
   */
  iopsErrTips(objVal:string, operationType:string) {
    if (operationType) {
      switch (operationType) {
        case 'maxbandwidth':
          if (objVal == '' && this.addForm.maxBandwidthChoose) {
            this.bandWidthMaxErrTips = true;
          }else {
            this.bandWidthMaxErrTips = false;
          }
          break;
        case 'maxiops':
          if (objVal == '' && this.addForm.maxIopsChoose) {
            this.iopsMaxErrTips = true;
          }else {
            this.iopsMaxErrTips = false;
          }
          break;
        case 'minbandwidth':
          if (objVal == '' && this.addForm.minBandwidthChoose) {
            this.bandWidthMinErrTips = true;
          }else {
            this.bandWidthMinErrTips = false;
          }
          break;
        case 'miniops':
          if (objVal == '' && this.addForm.minIopsChoose) {
            this.iopsMinErrTips = true;
          }else {
            this.iopsMinErrTips = false;
          }
          break;
        default:
          if (objVal == '' && this.addForm.latencyChoose) {
            this.latencyErrTips = true;
          }else {
            this.latencyErrTips = false;
          }
          break;
      }
    }
  }

  /**
   * 初始化IOPS错误提示
   */
  initIopsErrTips(upper:boolean, lower:boolean) {
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
  matchErr=false;
  nfsNameRepeatErr=false;
  shareNameRepeatErr=false;
  fsNameRepeatErr=false;
  oldNfsName:string;
  oldShareName:string;
  oldFsName:string;
  checkNfsName(){
    if(this.addForm.nfsName==null) return false;
    if(this.oldNfsName==this.addForm.nfsName) return false;
    this.oldNfsName=this.addForm.nfsName;
    let reg5:RegExp = new RegExp('^[0-9a-zA-Z\u4e00-\u9fa5a"_"]*$');
    if(reg5.test(this.addForm.nfsName)){
      // 共享名称不能包含中文
      let reg5Two:RegExp = new RegExp('[\u4e00-\u9fa5]');
      //验证重复
      this.matchErr=false;
      if (this.addForm.sameName){
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
      }else{
        this.checkNfsNameExist(this.addForm.nfsName);
      }
    }else{
      //
      this.matchErr=true;
      //不满足的时候置空
      this.addForm.nfsName=null;
      console.log('验证不通过');
    }
  }
  checkShareName(){
    if(this.addForm.shareName==null) return false;
    if(this.oldShareName==this.addForm.shareName) return false;

    this.oldShareName=this.addForm.shareName;
    let reg5:RegExp = new RegExp('^[0-9a-zA-Z\u4e00-\u9fa5a"_"]*$');
    if(reg5.test(this.addForm.shareName)){
      // 共享名称不能包含中文
      let reg5Two:RegExp = new RegExp('[\u4e00-\u9fa5]');
      if (reg5Two.test(this.addForm.shareName)) {
        this.addForm.shareName = '';
        this.shareNameContainsCN = true;
      } else {
        this.shareNameContainsCN = false;
        //验证重复
        this.matchErr=false;
        this.checkShareNameExist(this.addForm.shareName);
      }
    }else{
      this.matchErr=true;
      this.addForm.shareName=null;
    }
  }
  /**
   * 添加页面可点击 true 可点击 false 不可点击
   */
  isCheckSameName() {
    let reg5:RegExp = new RegExp('[\u4e00-\u9fa5]');
    if (reg5.test(this.addForm.nfsName)) { // 名称有中文
      return false;
    } else { // 无中文
      return true;
    }
  }
  setSameName(){
    if (this.addForm.sameName) {
      this.addForm.shareName = this.addForm.nfsName;
      this.addForm.fsName = this.addForm.nfsName;
    }
  }
  checkFsName(){
    if(this.addForm.fsName==null) return false;
    if(this.oldFsName==this.addForm.fsName) return false;

    this.oldFsName=this.addForm.fsName;
    let reg5:RegExp = new RegExp('^[0-9a-zA-Z\u4e00-\u9fa5a"_"]*$');
    if(reg5.test(this.addForm.fsName)){
      //验证重复
      this.matchErr=false;
      this.checkShareNameExist(this.addForm.fsName);
    }else{
      this.matchErr=true;
      this.addForm.fsName=null;
    }
  }
  checkNfsNameExist(name:string){
    this.addService.checkNfsNameExist(name).subscribe((r:any)=>{
        if (r.code=='200'){
          if(r.data){
            this.nfsNameRepeatErr=false;
          }else{
            this.nfsNameRepeatErr=true;
            this.addForm.nfsName=null;
          }
        }
    });
  }
  checkShareNameExist(name:string){
    this.addService.checkShareNameExist(name).subscribe((r:any)=>{
      if (r.code=='200'){
        if(r.data){
          this.shareNameRepeatErr=false;
        }else{
          this.shareNameRepeatErr=true;
          this.addForm.nfsName=null;
        }
      }
    });
  }
  checkFsNameExist(name:string){
    this.addService.checkFsNameExist(name).subscribe((r:any)=>{
      if (r.code=='200'){
        if(r.data){
          this.shareNameRepeatErr=false;
        }else{
          this.shareNameRepeatErr=true;
          this.addForm.nfsName=null;
        }
      }
    });
  }


  /**
   * 确认关闭窗口
   */
  confirmActResult() {
    this.wizard.close();// 关闭弹窗
    if (this.pluginFlag=='plugin'){
      this.backToNfsList();
    }else{
      this.closeModel();
    }
  }
  qosFunc(form) {
    console.log("form.qosFlag", form.qosFlag);
    const qosTag = this.getStorageQosTag(form.storagId);
    if (!form.qosFlag) {// 关闭状态
      form.contolPolicy = null;
      this.initAddMinInfo(form);
      this.initAddMaxInfo(form);
    }else {
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
        if(qosTag == 2){
          this.initAddMaxInfo(form);
        }else if (qosTag == 3) {
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
      } else { // all
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
  initAddMaxInfo(form){
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
  qoSFlagChange(form){
    if(form.qosFlag) {
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
    if(upperObj) {
      upperChecked =  upperObj.checked;
    }
    let lowerChecked;
    if (lowerObj) {
      lowerChecked = lowerObj.checked;
    }
    this.initIopsErrTips(upperChecked, lowerChecked);
    if (isUpper) {
      if(upperChecked) {
        form.control_policyUpper = '1';
      }else {
        form.control_policyUpper = undefined;
      }
      if(qosTag == 2 && upperChecked) { // 单选
        console.log("单选1", qosTag)
        form.control_policyLower = undefined;
        lowerObj.checked = false;
      }
    } else {
      if(lowerChecked) {
        form.control_policyLower = '0';
      }else {
        form.control_policyLower = undefined;
      }
      if (lowerChecked && qosTag == 2) {
        console.log("单选2", qosTag)
        form.control_policyUpper = undefined;
        upperObj.checked = false;
      }
    }
    console.log("lowerChecked", form)
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
  addLatencyChoose(){
    this.addForm.latency = null;
    const qosTag = this.getStorageQosTag(this.addForm.storagId);
    this.latencyIsSelect = qosTag == 1;
  }
}

