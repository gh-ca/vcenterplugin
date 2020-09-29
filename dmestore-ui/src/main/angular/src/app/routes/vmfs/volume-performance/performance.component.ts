import {AfterViewInit, Component, NgZone, OnInit, ChangeDetectorRef} from '@angular/core';
import { EChartOption } from 'echarts';
import { VmfsPerformanceService } from './performance.service';
import {VmfsListService} from '../list/list.service';
import {ChartOptions, NfsService, MakePerformance} from "../../nfs/nfs.service";
import {VolumeInfo} from "../volume-attribute/attribute.service";


@Component({
  selector: 'app-performance',
  templateUrl: './performance.component.html',
  styleUrls: ['./performance.component.scss'],
  providers: [VmfsPerformanceService, MakePerformance, NfsService],
})
export class PerformanceComponent implements OnInit, AfterViewInit {

  // 创建表格对象
  // IOPS+QoS上下限
  iopsChart: EChartOption = {};
  // 带宽+QoS上下限
  bandwidthChart: EChartOption = {};
  // 响应时间+QoS下限
  latencyChart: EChartOption = {};

  // obj_type_id  (卷类型ID)
  objTypeId;
  // indicator_ids 获取参数指标（上下限等） 0 上限 1下限
  indicatorIdsIOPS: Array<string> = ['1407379178651656', '1407379178586113'];
  indicatorIdsBDWT: Array<string> = ['1407379178651656', '1407379178586113'];
  indicatorIdsREST: Array<string> = ['1407379178651656', '1407379178586113'];
  // obj_ids 卷ID
  // objIds: Array<string> = ['1282FFE20AA03E4EAC9A814C687B780A'];
  /*{
    "obj_type_id" : "1125921381679104",
    "indicator_ids" : ["1125921381744648","1125921381744649"],
    "obj_ids" : ["1282FFE20AA03E4EAC9A814C687B780A"],
    "interval" : "ONE_MINUTE",
    "range" : "LAST_1_DAY"
  }*/
  // interval 时间间隔单位 ONE_MINUTE MINUTE HALF_HOUR HOUR DAY WEEK MONTH
  interval;
  // range 时间段 LAST_5_MINUTE LAST_1_HOUR LAST_1_DAY LAST_1_WEEK LAST_1_MONTH LAST_1_QUARTER HALF_1_YEAR LAST_1_YEAR BEGIN_END_TIME INVALID
  range = 'LAST_1_DAY';
  // begin_time 开始时间 时间戳(例：1552477343834)
  // beginTime = 1552477343834;
  // end_time 结束时间 时间戳
  // endTime = 1552567343000;
  // 定时函数执行时间 默认一天
  timeInterval = 1 * 60 * 60 * 1000;
  // 卷ID
  // 卷信息
  volumeInfoList: VolumeInfo[];
  // 选中的卷数据
  selectVolume: VolumeInfo;
  // 选中的卷名称
  selectVolName: string;
  // 卷名称集合
  volNames: string[] = [];

  // ranges
  ranges = NfsService.perRanges;
  // select range
  selectRange = 'LAST_1_DAY';

  constructor(private nfsService: NfsService, private makePerformance: MakePerformance, private perService: VmfsPerformanceService, private ngZone: NgZone, private cdr: ChangeDetectorRef) {
  }

  ngAfterViewInit() {
    this.ngZone.runOutsideAngular(() => this.initChart());
  }

  ngOnInit(): void {
    // 初始化卷信息
    // const objectId=ctx[0].id;
    const objectId = 'urn:vmomi:Datastore:datastore-183:f8e381d7-074b-4fa9-9962-9a68ab6106e1';
    this.makePerformance.getVolsByObjId(objectId, this.volumeInfoList, this.volNames, this.selectVolName, this.selectVolume);
  }
  // 初始化表格对象
  async initChart() {

    console.log('this.rang', this.range)
    const volIds:string[] = [];
    // 后续需注释掉此代码
    // volIds.push(this.selectVolume.wwn);
    volIds.push('1282FFE20AA03E4EAC9A814C687B780A');
    // IOPS
    this.makePerformance.setChart(300,'IOPS', 'IO/s', NfsService.nfsIOPS, volIds, this.selectRange, NfsService.vmfsUrl).then(res => {
      this.iopsChart = res;
      this.cdr.detectChanges();
    });

    // 带宽
    this.perService.getIopsChart('Bandwidth', 'MS/s', null,  NfsService.nfsIOPS, volIds,
      this.interval, this.selectRange, null, null).then(res => {
      this.bandwidthChart = res;
      this.cdr.detectChanges();
    });
    // // 响应时间
    // this.perService.getIopsChart('Latency', 'ms', this.objTypeId, this.indicatorIdsREST, this.objIds,
    //   this.interval, this.range, this.beginTime, this.endTime).then(res => {
    //   this.latencyChart = res;
    //   this.cdr.detectChanges();
    // });
  }
  // 切换卷函数
  changeVolFunc() {
    console.log(this.selectVolName && this.selectRange);
    // if (this.selectVolName && this.selectRange) {
    if (this.selectRange) {
      console.log('this.selectVolName+this.selectRange', this.selectVolName, this.selectRange);
      // 获取已选择的卷
      // this.selectVolume = this.makePerformance.getVolByName(this.selectVolName, this.volumeInfoList);
      // 请求后台重新加载折线图
      this.initChart();
    } else {
      console.log('未选择卷或range');
    }
  }

}
