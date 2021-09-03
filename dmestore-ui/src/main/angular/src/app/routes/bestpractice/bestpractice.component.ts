import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonService } from '../common.service';
import { GlobalsService } from '../../shared/globals.service';
import { TranslatePipe ,TranslateService} from '@ngx-translate/core';
import { isMockData, mockData } from 'mock/mock';
import { getTypeOf, handlerResponseErrorSimple } from 'app/app.helpers';
import {element} from "protractor";
import set = Reflect.set;

export const MTU = 'mtu';
export const MTU_TAG = 'Jumbo Frame (MTU)';

@Component({
  selector: 'app-bestpractice',
  templateUrl: './bestpractice.component.html',
  styleUrls: ['./bestpractice.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [CommonService, TranslatePipe],
})
export class BestpracticeComponent implements OnInit {
  // ================最佳实践列表=============
  rowSelected = []; // 当前选中数据
  isLoading = true; // table数据loading
  list: Bestpractice[] = []; // 数据列表
  total = 0; // 总数据数量

  query = {
    // 查询数据
  };
  // =================END===============

  // ================主机列表=============
  hostModalShow = false;
  repairLogsShow=false;
  repairLogs=[];//修复日志
  reviseRecommendValueShow=false
  recommand={};
  recommandId:string='';//id
  recommandValue:string=''; //原期望值
  newRecommandValue:number //新期望值
  repairAction:string=''; //修复方式
  isShowBPBtnTips: boolean;
  hostSelected = []; // 主机选中列表
  hostIsLoading = false; // table数据loading
  hostList: Host[] = []; // 数据列表
  hostTotal = 0; // 总数据数量
  currentBestpractice: Bestpractice;
  /* MTU的panel单独处理 */
  currentPanel;
  dataLoading=false;
  // ================END====================

  tipModal = false;
  ips = '';
  applyType = '1';

  tipModalSuccess = false;
  tipModalFail = false;

  applyLoading = false;
  checkLoading = false;
  selectedArr=[{value:'0',label:'bestPractice.repairHistory.manual'},{value:'1',label: 'bestPractice.repairHistory.automatic'}]

  applyTips = false; // 实施最佳实践提示（违规数为0时提示）

  constructor(
    private cdr: ChangeDetectorRef,
    public gs: GlobalsService,
    private http: HttpClient,
    private commonService: CommonService,
    private translatePipe: TranslatePipe,
    private translateService:TranslateService
  ) {}

  ngOnInit(): void {
    this.practiceRefresh();
  }

  /**
   * 列表实时最佳实践 参数封装
   * @returns {any[]}
   */
  packApplyPracticeParams() {
    const params = [];
    this.ips = '';
    this.rowSelected.forEach(item => {
      const i = { hostSetting: '', hostObjectIds: [] };
      i.hostSetting = item.hostSetting;
      item.hostList.forEach(s => {
        i.hostObjectIds.push(s.hostObjectId);
        if (s.needReboot == 'true') {
          this.ips += s.hostName + ',';
        }
      });
      params.push(i);
    });
    return params;
  }

  /**
   * 主机列表实时最佳实践 参数封装
   * @returns {any[]}
   */
  packApplyPracticeParamsByHost() {
    const params = [];
    const i = { hostSetting: '', hostObjectIds: [] };
    i.hostSetting = this.currentBestpractice.hostSetting;
    let ips = '';
    this.hostSelected.forEach(s => {
      i.hostObjectIds.push(s.hostObjectId);
      if (s.needReboot == 'true') {
        ips += s.hostName + ',';
      }
    });
    params.push(i);

    return params;
  }

  /**
   * 实时最佳实践
   * @param params
   */
  applyPractice(params) {
    this.applyLoading = true;
    this.http.post('v1/bestpractice/update/bylist', params).subscribe(
      (result: any) => {
        this.applyLoading = false;
        if (result?.code == '200') {
          this.tipModalSuccess = true;
          if (this.applyType != '1') {
            this.hostModalShow = false;
          }
          this.practiceRefresh();
        } else {
          this.tipModalFail = true;
        }
        this.cdr.detectChanges();
      },
      err => {
        console.error('ERROR', err);
      }
    );
  }

  openTip() {
    this.tipModal = true;
  }

  closeTip() {
    this.tipModal = false;
  }

  applyClick(type: string) {
    if (type == '1') {
      this.rowSelected.forEach(item => {
        if (item.count == 0) {
          this.applyTips = true;
          return;
        }
      });
      if (this.applyTips) {
        return;
      }
    }
    this.applyType = type;
    let params;
    if (this.applyType == '1') {
      params = this.packApplyPracticeParams();
    } else {
      params = this.packApplyPracticeParamsByHost();
    }
    if (this.ips.length != 0) {
      this.openTip();
    } else {
      this.applyPractice(params);
    }
  }

  tipOk() {
    this.closeTip();
    let params;
    if (this.applyType == '1') {
      params = this.packApplyPracticeParams();
    } else {
      params = this.packApplyPracticeParamsByHost();
    }
    this.applyPractice(params);
  }

  recheck() {
    this.checkLoading = true;
    this.http.post('v1/bestpractice/check', {}).subscribe(
      (result: any) => {
        this.checkLoading = false;
        if (result.code == '200') {
          this.tipModalSuccess = true;
          this.practiceRefresh();
        } else {
          this.tipModalFail = true;
        }
        this.cdr.detectChanges();
      },
      err => {
        console.error('ERROR', err);
      }
    );
  }

  practiceRefresh() {
    this.isLoading = true;
    const handlerGetRecordsAllSuccess = (result: any) => {
      if (result.code === '200') {
        this.list = result.data;
        // bug修改：列表页面级别过滤 中英文问题
        if (this.list && Array.isArray(this.list)) {
          this.list = this.list.map(item => {
            const _item = { ...item };
            const LEVEL_MAP = {
              Critical: [4, 'overview.critical'],
              Major: [3, 'overview.major'],
              Warning: [2, 'overview.warning'],
              Info: [1, 'overview.info'],
            };
            let levelDesc = '--';
            let levelNum = 0;
            const mapLevel = LEVEL_MAP[String(item.level).trim()];
            if (Array.isArray(mapLevel)) {
              levelNum = mapLevel[0];
              levelDesc = this.translatePipe.transform(mapLevel[1]) || '--';
            }
            _item.levelDesc = levelDesc;
            _item.levelNum = levelNum;

            // 设置描述信息
            const DESCRIPTION_MAP = {
              'VMFS3.UseATSForHBOnVMFS5': 'bestPractice.description.vmfs5',
              'VMFS3.HardwareAcceleratedLocking': 'bestPractice.description.locking',
              'DataMover.HardwareAcceleratedInit': 'bestPractice.description.init',
              'DataMover.HardwareAcceleratedMove': 'bestPractice.description.move',
              'VMFS3.EnableBlockDelete': 'bestPractice.description.delete',
              'Disk.SchedQuantum': 'bestPractice.description.quanTum',
              'Disk.DiskMaxIOSize': 'bestPractice.description.diskMaxIOSize',
              'LUN Queue Depth for Qlogic': 'bestPractice.description.depthForQlogic',
              'LUN Queue Depth for Emulex': 'bestPractice.description.depthForEmulex',
              'NMP path switch policy': 'bestPractice.description.pathSwitchPolicy',
              'Jumbo Frame (MTU)': 'bestPractice.description.jumboFrame',
              'VMFS-6 Auto-Space Reclamation': 'bestPractice.description.reclamation',
              'Number of volumes in Datastore': 'bestPractice.description.numberOfVolInDatastore',
              'VMFS Datastore Space Utilization':'bestPractice.description.spaceUtilization'
            };
            const mapDescription = DESCRIPTION_MAP[String(item.hostSetting).trim()];
            let description = this.translatePipe.transform(mapDescription) || '--';
            _item.description = description;

            // 违规主机实际值修改
            _item.hostList = _item.hostList.map(hostInfo => ({
              ...hostInfo,
              actualObjValue: getTypeOf(hostInfo.actualValue),
            }));
            //修复动作
              _item.repairAction=item.repairAction
            // console.log(item.repairAction,_item.repairAction)
            return _item;
          });
        }
        this.total = result.data.length;
        this.isLoading = false;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    };

    if (isMockData) {
      handlerGetRecordsAllSuccess(mockData.BESTPRACTICE_RECORDS_ALL);
    } else {
      this.http
        .get('v1/bestpractice/records/all', {})
        .subscribe(handlerGetRecordsAllSuccess, handlerResponseErrorSimple);
    }
  }

  checkBescpractice(bestpractice) {
    this.currentBestpractice = bestpractice;
    this.currentBestpractice.hostList.forEach(i=>{
      i.recommendValue=this.currentBestpractice.recommendValue+''
    })
    const { hostSetting } = bestpractice;
    this.currentPanel = hostSetting === MTU_TAG ? MTU : '';
    this.cdr.detectChanges()
  }
  openHostList(bestpractice: Bestpractice) {
    this.hostModalShow = true;
    this.checkBescpractice(bestpractice);
    this.hostRefresh();
    // console.log(bestpractice)
  }
  async openRepairHistoryList(hostSetting){
    this.dataLoading=true
    this.repairLogs=await this.commonService.getBestpracticeRepairLogs(hostSetting)
    this.repairLogs.forEach(i=>{
      i.repairTime=this.transFormatOfDate(i.repairTime)
    })
    this.repairLogsShow=true
    this.cdr.detectChanges()
    this.dataLoading=false
  }
  async openRevise(hostSetting){
    this.dataLoading=true
    this.recommand=await this.commonService.getBestpracticeRecommand(hostSetting)
    // console.log('recommand',this.recommand)
    this.recommandValue=(this.recommand as any).recommandValue
    this.recommandId=(this.recommand as any).id
    this.newRecommandValue=null
    this.reviseRecommendValueShow=true;
    this.cdr.detectChanges();
    this.dataLoading=false
  }
  transFormatOfDate(date){
    const year=new Date(date).getFullYear()
    const month=(new Date(date).getMonth()+1)<10?'0'+(new Date(date).getMonth()+1):new Date(date).getMonth()+1
    const day=new Date(date).getDate()<10?'0'+new Date(date).getDate():new Date(date).getDate()
    const hour=new Date(date).getHours()<10?'0'+new Date(date).getHours():new Date(date).getHours()
    const min=new Date(date).getMinutes()<10?'0'+new Date(date).getMinutes():new Date(date).getMinutes()
    const second=new Date(date).getSeconds()<10?'0'+new Date(date).getSeconds():new Date(date).getSeconds()
    const str=year+'-'+month+'-'+day+' '+hour+':'+min+':'+second
    return str
  }
 // 修改期望值
 async modifyExpectations(){
    this.dataLoading=true
    let code=await this.commonService.changeBestpracticeRecommand(this.recommandId,{'recommandValue':this.newRecommandValue+'%'})
    let resData:any= await this.commonService.checkVmfsBestpractice([])
    if (code==='200'&&resData.code==='200'){
     this.dataLoading=false
     this.reviseRecommendValueShow=false
     this.practiceRefresh()
   }
   this.cdr.detectChanges()
  }
  newExpectations(){
    let value=this.newRecommandValue
    if (value<1||value>90){
      this.newRecommandValue=null
    }
  }
  changeNewRecommandValueBtn(){
    if ((this.newRecommandValue<90||this.newRecommandValue===90)&&(this.newRecommandValue>1||this.newRecommandValue===1)){
      return false
    }else {
      return true
    }
  }
  async changeRepairAction(item){
    console.log(item)
    let temp:any=await this.commonService.getBestpracticeRecommand(item.hostSetting)
    let id=temp.id
    let code=await this.commonService.changeBestpracticeRecommand(id,{'repairAction':item.repairAction})
    if (code==='200'){
    //  修改成功
    }else {
    //  修改失败
    }
  }
  hostRefresh() {
    if (this.hostModalShow === true) {
      this.hostIsLoading = true;
      this.hostList=this.currentBestpractice.hostList
      // console.log(this.hostList)
      /*根据autoRepair 判断是否禁用执行最佳实践 */
      /*显示tips就不显示按钮*/
      if (this.hostList.length > 0) {
        this.isShowBPBtnTips = this.hostList[0].autoRepair === 'false' ? true : false;
      } else {
        this.isShowBPBtnTips = false;
      }
      this.hostTotal = this.currentBestpractice.hostList.length;
      this.hostIsLoading = false;
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    }
  }

  /**
   * 单行实施
   * @param item
   */
  applyOperation(item: Bestpractice) {
    const params = [];
    this.ips = '';
    const i = { hostSetting: '', hostObjectIds: [] };
    i.hostSetting = item.hostSetting;
    item.hostList.forEach(s => {
      i.hostObjectIds.push(s.hostObjectId);
      if (s.needReboot == 'true') {
        this.ips += s.hostName + ',';
      }
    });
    params.push(i);
    this.applyPractice(params);
  }

  /**
   * 将字符串转JSON数组
   * @param obj
   */
  getTypeOf(obj) {
    let object: object;
    if (typeof obj == 'string' && obj.indexOf('[{') != -1) {
      object = JSON.parse(obj);
    } else {
      object = obj;
    }
    return object;
  }

  /**
   * 实际值 是否为数组 true 数组 false 字符串
   * @param obj
   */
  isObjectValue(obj) {
    return Array.isArray(obj) && obj.length > 0;
  }

  /**
   * 获取实际值文本值
   * @param obj
   */
  getObjectTitle(obj) {
    let title = '';
    obj.forEach(item => {
      title += item.name + ' | ' + item.value + '\n';
    });
    return title;
  }

  sortFunc(obj: any) {
    // let object;
    // if (obj.target.type && obj.target.type == 'button') {
    //   object = obj.target;
    // } else {
    //   if ((obj.target.attributes.shape && obj.target.attributes.shape.nodeValue == 'filter-grid')
    //     || (obj.target.attributes.class && obj.target.attributes.class.value == 'datagrid-column ng-star-inserted')) {
    //     return;
    //   } else {
    //     object = obj.target.parentElement;
    //   }
    // }
    // const sortValue = object.children[1].attributes.shape.nodeValue;
    // if (sortValue == 'arrow down' || sortValue == 'arrow') {
    //   object.children[0].hidden = true;
    // } else {
    //   object.children[0].hidden = false;
    // }
    return !obj;
  }

  afterApply(result) {
    this.applyLoading = false;
    if (result?.code == '200') {
      this.tipModalSuccess = true;
      if (this.applyType != '1') {
        this.hostModalShow = false;
      }
      this.practiceRefresh();
    } else {
      this.tipModalFail = true;
    }
    this.cdr.detectChanges();
  }
}

class Bestpractice {
  hostSetting: string;
  recommendValue: number;
  level: string;
  levelNum: number;
  levelDesc: string;
  count: number;
  repairAction:string;
  description: string;
  hostList: Host[];
}

export class Host {
  hostSetting: string;
  level: string;
  hostName: string;
  recommendValue: string;
  actualValue: string;
  actualObjValue: any;
  hostObjectId: string;
  needReboot: string;
  hostId: string;
  autoRepair: string;
}
