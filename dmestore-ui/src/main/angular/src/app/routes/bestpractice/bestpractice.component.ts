import {
  Component,
  OnInit,
  ChangeDetectionStrategy, ChangeDetectorRef,
} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonService } from '../common.service';
import { GlobalsService } from "../../shared/globals.service";
import { TranslatePipe } from "@ngx-translate/core";
import { isMockData, mockData } from 'mock/mock';
import { handlerResponseErrorSimple } from 'app/app.helpers';

export const MTU = 'mtu';
export const MTU_TAG = 'Jumbo Frame (MTU)';

@Component({
  selector: 'app-bestpractice',
  templateUrl: './bestpractice.component.html',
  styleUrls: ['./bestpractice.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [CommonService, TranslatePipe]
})
export class BestpracticeComponent implements OnInit {

  // ================最佳实践列表=============
  rowSelected = []; // 当前选中数据
  isLoading = true; // table数据loading
  list: Bestpractice[] = []; // 数据列表
  total = 0; // 总数据数量

  query = { // 查询数据
  };
  // =================END===============

  // ================主机列表=============
  hostModalShow = false;
  isShowBPBtnTips: boolean;
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


  tipModalSuccess = false;
  tipModalFail = false;

  applyLoading = false;
  checkLoading = false;

  applyTips = false;// 实施最佳实践提示（违规数为0时提示）

  constructor(private cdr: ChangeDetectorRef,
              public gs: GlobalsService,
              private http: HttpClient,
              private commonService: CommonService,
              private translatePipe: TranslatePipe) {
  }

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
    this.rowSelected.forEach((item) => {
      const i = {hostSetting: '', hostObjectIds: []};
      i.hostSetting = item.hostSetting;
      item.hostList.forEach((s) => {
        i.hostObjectIds.push(s.hostObjectId);
        if (s.needReboot == "true") {
          this.ips += s.hostName + ",";
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
    const i = {hostSetting: '', hostObjectIds: []};
    i.hostSetting = this.currentBestpractice.hostSetting;
    let ips = '';
    this.hostSelected.forEach((s) => {
      i.hostObjectIds.push(s.hostObjectId);
      if (s.needReboot == "true") {
        ips += s.hostName + ",";
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
    this.http.post('v1/bestpractice/update/bylist', params).subscribe((result: any) => {
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
    }, err => {
      console.error('ERROR', err);
    });
  }

  openTip() {
    this.tipModal = true;
  }

  closeTip() {
    this.tipModal = false;
  }

  applyClick(type: string) {
    if (type == '1') {
      this.rowSelected.forEach((item) => {
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
    this.http.post('v1/bestpractice/check', {}).subscribe((result: any) => {
      this.checkLoading = false;
      if (result.code == '200') {
        this.tipModalSuccess = true;
        this.practiceRefresh();
      } else {
        this.tipModalFail = true;
      }
      this.cdr.detectChanges();
    }, err => {
      console.error('ERROR', err);
    });
  }

  practiceRefresh() {
    this.isLoading = true;
    const handlerGetRecordsAllSuccess = (result: any) => {
      /*TODO: 这个地方有问题：在中途切换语言的，翻译不会响应。不过不作妖应该还行。 */
      /* 最简单方案，监听语言切换，再次调用 刷新*/
      if (result.code === '200') {
        this.list = result.data;
        // bug修改：列表页面级别过滤 中英文问题
        if (this.list && Array.isArray(this.list)) {
          this.list = this.list.map(item => {
            const _item = { ...item };
            const LEVEL_MAP = {
              "Critical": [4, "overview.critical"],
              "Major": [3, "overview.major"],
              "Warning": [2, "overview.warning"],
              "Info": [1, "overview.info"],
            };
            let levelDesc = '--';
            let levelNum = 0;
            const mapLevel = LEVEL_MAP[String(item.hostSetting).trim()];

            if (Array.isArray(mapLevel)) {
              levelNum = mapLevel[0];
              levelDesc = this.translatePipe.transform(mapLevel[1]) || '--';
            }
            _item.levelDesc = levelDesc;
            _item.levelNum = levelNum;

            // 设置描述信息
            const DESCRIPTION_MAP = {
              'VMFS3.UseATSForHBOnVMFS5': ('bestPractice.description.vmfs5'),
              'VMFS3.HardwareAcceleratedLocking': ('bestPractice.description.locking'),
              'DataMover.HardwareAcceleratedInit': ('bestPractice.description.init'),
              'DataMover.HardwareAcceleratedMove': ('bestPractice.description.move'),
              'VMFS3.EnableBlockDelete': ('bestPractice.description.delete'),
              'Disk.SchedQuantum': ('bestPractice.description.quanTum'),
              'Disk.DiskMaxIOSize': ('bestPractice.description.diskMaxIOSize'),
              'LUN Queue Depth for Qlogic': ('bestPractice.description.depthForQlogic'),
              'LUN Queue Depth for Emulex': ('bestPractice.description.depthForEmulex'),
              'NMP path switch policy': ('bestPractice.description.pathSwitchPolicy'),
              'Jumbo Frame (MTU)': ('bestPractice.description.jumboFrame'),
              'VMFS-6 Auto-Space Reclamation': ('bestPractice.description.reclamation'),
              'Number of volumes in Datastore': ('bestPractice.description.numberOfVolInDatastore'),
            };
            const mapDescription = DESCRIPTION_MAP[String(item.hostSetting).trim()];
            let description = this.translatePipe.transform(mapDescription) || '--';
            _item.description = description;

            // 违规主机实际值修改
            _item.hostList = _item.hostList.map(hostInfo => ({
              ...hostInfo,
              actualObjValue: this.getTypeOf(hostInfo.actualValue)
            }));
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
      this.http.get('v1/bestpractice/records/all', {}).subscribe(handlerGetRecordsAllSuccess, handlerResponseErrorSimple);
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
    const i = {hostSetting: '', hostObjectIds: []};
    i.hostSetting = item.hostSetting;
    item.hostList.forEach((s) => {
      i.hostObjectIds.push(s.hostObjectId);
      if (s.needReboot == "true") {
        this.ips += s.hostName + ",";
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
    if (typeof obj == 'string' && obj.indexOf("[{") != -1) {
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
    return (typeof obj) == 'object';
  }

  /**
   * 获取实际值文本值
   * @param obj
   */
  getObjectTitle(obj) {
    let title = '';
    obj.forEach(item => {
      title += item.name + " | " + item.value + '\n';
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
    debugger;
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
