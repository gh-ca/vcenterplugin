import { ChangeDetectorRef, Component, OnInit, AfterViewInit } from '@angular/core';
import { GlobalsService } from '../../shared/globals.service';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { CommonService } from '../common.service';
import { getTypeOf, handlerResponseErrorSimple } from 'app/app.helpers';
import { isMockData, mockData } from 'mock/mock';
import { MTU, MTU_TAG } from '../bestpractice/bestpractice.component';

@Component({
  selector: 'app-applybp',
  templateUrl: './applybp.component.html',
  styleUrls: ['./applybp.component.scss'],
  providers: [CommonService, TranslatePipe],
})
export class ApplybpComponent implements OnInit {
  hiddenTip = true;
  // 1 执行中， 2 完成， 3 出错
  status = 1;

  constructor(
    private cdr: ChangeDetectorRef,
    private http: HttpClient,
    private gs: GlobalsService,
    private activatedRoute: ActivatedRoute,
    private commonService: CommonService,
    private translatePipe: TranslatePipe
  ) {}

  ngOnInit(): void {
    this.practiceRefresh();
  }

  // ngAfterViewInit() {
  //   // /update/byCluster
  //   console.log(this.gs.getClientSdk().app.getClientInfo());
  //   console.log(this.gs.getClientSdk().app.getNavigationData());
  //
  //
  //   let type;
  //   this.activatedRoute.url.subscribe(url => {
  //     type = url[0].path;
  //   });
  //   // const objectId = 'urn:vmomi:ClusterComputeResource:domain-c2038:674908e5-ab21-4079-9cb1-596358ee5dd1';
  //   console.log("objectId", objectId)
  //   // this.http.post('v1/bestpractice/update/all', {}).subscribe((result: any) => {
  //   if (type == 'cluster') {
  //     this.http.post('v1/bestpractice/update/byCluster/' + objectId, {}).subscribe((result: any) => {
  //       if (result.code == '200'){
  //         this.status = 2;
  //         if(result.data){
  //           result.data.forEach((item)=>{
  //             if(item.needReboot){
  //               this.ips = item.hostName + ",";
  //             }
  //           });
  //         }
  //         if(this.ips != ''){
  //           this.hiddenTip = false;
  //         } else{
  //           this.hiddenTip = true;
  //         }
  //       } else{
  //         this.status = 3;
  //       }
  //     }, err => {
  //       this.status = 3;
  //       console.error('ERROR', err);
  //     });
  //   } else {
  //     let hostObjectIds = [];
  //     hostObjectIds.push(objectId);
  //     this.http.post('v1/bestpractice/update/byhosts', hostObjectIds).subscribe((result: any) => {
  //       if (result.code == '200'){
  //         this.status = 2;
  //         if(result.data){
  //           result.data.forEach((item)=>{
  //             if(item.needReboot){
  //               this.ips = item.hostName + ",";
  //             }
  //           });
  //         }
  //         if(this.ips != ''){
  //           this.hiddenTip = false;
  //         } else{
  //           this.hiddenTip = true;
  //         }
  //       } else{
  //         this.status = 3;
  //       }
  //     }, err => {
  //       this.status = 3;
  //       console.error('ERROR', err);
  //     });
  //   }
  // }

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
  isShowBPBtnTips: boolean;
  repairLogsShow=false;
  repairLogs=[];//修复日志
  reviseRecommendValueShow=false
  recommand={};
  recommandId:string='';//id
  recommandValue:string=''; //原期望值
  newRecommandValue:number //新期望值
  repairAction:string=''; //修复方式
  hostSelected = []; // 主机选中列表
  hostIsLoading = false; // table数据loading
  hostList: Host[] = []; // 数据列表
  hostTotal = 0; // 总数据数量
  currentBestpractice: Bestpractice;
  /* MTU的panel单独处理 */
  currentPanel;
  // ================END====================

  tipModal = false;
  ips = '';
  applyType = '1';
  selectedArr=['手动','自动']

  tipModalSuccess = false;
  tipModalFail = false;

  applyLoading = false;
  checkLoading = false;

  applyTips = false; // 实施最佳实践提示（违规数为0时提示）

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
        if (result.code == '200') {
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
    const ctx = this.gs.getClientSdk().app.getContextObjects();
    const objectId = ctx
      ? ctx[0].id
      : 'urn:vmomi:ClusterComputeResource:domain-c2038:674908e5-ab21-4079-9cb1-596358ee5dd1';
    let type = '';
    this.activatedRoute.url.subscribe(url => {
      type = url[0].path;
    });
    this.isLoading = true;
    const handlerGetRecords = (result: any) => {
      if (result.code === '200') {
        this.list = result.data;
        // bug修改：列表页面级别过滤 中英文问题
        if (this.list) {
          this.list.forEach(item => {
            let levelDesc;
            let levelNum;
            switch (item.level) {
              case 'Critical':
                levelNum = 4;
                levelDesc = this.translatePipe.transform('overview.critical');
                break;
              case 'Major':
                levelNum = 3;
                levelDesc = this.translatePipe.transform('overview.major');
                break;
              case 'Warning':
                levelNum = 2;
                levelDesc = this.translatePipe.transform('overview.warning');
                break;
              case 'Info':
                levelNum = 1;
                levelDesc = this.translatePipe.transform('overview.info');
                break;
              default:
                levelNum = 0;
                levelDesc = '--';
                break;
            }
            item.levelDesc = levelDesc;
            item.levelNum = levelNum;

            // 设置描述信息
            switch (item.hostSetting) {
              case 'VMFS3.UseATSForHBOnVMFS5':
                item.description = this.translatePipe.transform('bestPractice.description.vmfs5');
                break;
              case 'VMFS3.HardwareAcceleratedLocking':
                item.description = this.translatePipe.transform('bestPractice.description.locking');
                break;
              case 'DataMover.HardwareAcceleratedInit':
                item.description = this.translatePipe.transform('bestPractice.description.init');
                break;
              case 'DataMover.HardwareAcceleratedMove':
                item.description = this.translatePipe.transform('bestPractice.description.move');
                break;
              case 'VMFS3.EnableBlockDelete':
                item.description = this.translatePipe.transform('bestPractice.description.delete');
                break;
              case 'Disk.SchedQuantum':
                item.description = this.translatePipe.transform('bestPractice.description.quanTum');
                break;
              case 'Disk.DiskMaxIOSize':
                item.description = this.translatePipe.transform(
                  'bestPractice.description.diskMaxIOSize'
                );
                break;
              case 'LUN Queue Depth for Qlogic':
                item.description = this.translatePipe.transform(
                  'bestPractice.description.depthForQlogic'
                );
                break;
              case 'LUN Queue Depth for Emulex':
                item.description = this.translatePipe.transform(
                  'bestPractice.description.depthForEmulex'
                );
                break;
              case 'NMP path switch policy':
                item.description = this.translatePipe.transform(
                  'bestPractice.description.pathSwitchPolicy'
                );
                break;
              case 'Jumbo Frame (MTU)':
                item.description = this.translatePipe.transform(
                  'bestPractice.description.jumboFrame'
                );
                break;
              case 'VMFS-6 Auto-Space Reclamation':
                item.description = this.translatePipe.transform(
                  'bestPractice.description.reclamation'
                );
                break;
              case 'Number of volumes in Datastore':
                item.description = this.translatePipe.transform(
                  'bestPractice.description.numberOfVolInDatastore'
                );
                break;
              default:
                item.description = '--';
            }
            // 违规主机实际值修改
            item.hostList.forEach(hostInfo => {
              hostInfo.actualObjValue = getTypeOf(hostInfo.actualValue);
            });
          });
        }
        this.total = result.data.length;
        this.isLoading = false;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    };

    if (isMockData) {
      handlerGetRecords(mockData.BESTPRACTICE_RECORDS_ALL);
    } else {
      this.http
        .get('v1/bestpractice/records/all?objectId=' + objectId + '&type=' + type)
        .subscribe(handlerGetRecords, handlerResponseErrorSimple);
    }
  }

  checkBescpractice(bestpractice) {
    this.currentBestpractice = bestpractice;
    const { hostSetting } = bestpractice;
    this.currentPanel = hostSetting === MTU_TAG ? MTU : '';
  }

  openHostList(bestpractice: Bestpractice) {
    this.hostModalShow = true;
    this.checkBescpractice(bestpractice);
    this.hostRefresh();
  }

  async openRepairHistoryList(hostSetting){
    this.repairLogs=await this.commonService.getBestpracticeRepairLogs(hostSetting)
    this.repairLogsShow=true
  }
  async openRevise(hostSetting){
    this.recommand=await this.commonService.getBestpracticeRecommand(hostSetting)
    // console.log('recommand',this.recommand)
    this.recommandValue=(this.recommand as any).recommandValue
    this.recommandId=(this.recommand as any).id
    this.reviseRecommendValueShow=true
  }
  async modifyExpectations(){
    let code=await this.commonService.changeBestpracticeRecommand(this.recommandId,{'recommandValue':this.newRecommandValue+'%'})
    if (code==='200'){
      //  修改成功
      console.log('修改成功')
    }else {
      //  修改失败
    }
    this.reviseRecommendValueShow=false
  }
  newExpectations(){
    let value=this.newRecommandValue
    if (value<75||value>90){
      this.newRecommandValue=null
    }
  }
  changeNewRecommandValueBtn(){
    if ((this.newRecommandValue<90||this.newRecommandValue===90)&&(this.newRecommandValue>75||this.newRecommandValue===75)){
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
      this.hostList = this.currentBestpractice.hostList;
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
    return typeof obj == 'object';
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
  description: string;
  hostList: Host[];
}

export class Host {
  hostSetting: string;
  level: string;
  hostName: string;
  recommendValue: number;
  actualValue: number;
  actualObjValue: any;
  hostObjectId: string;
  needReboot: string;
  hostId: string;
  autoRepair: string;
}
