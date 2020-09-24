import {AfterViewInit, Component, NgZone, OnInit, ChangeDetectorRef} from '@angular/core';
import { EChartOption } from 'echarts';
import { VmfsPerformanceService } from './performance.service';
import {VmfsListService} from '../list/list.service';


@Component({
  selector: 'app-performance',
  templateUrl: './performance.component.html',
  styleUrls: ['./performance.component.scss'],
  providers: [VmfsPerformanceService],
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
  objIds: Array<string> = ['47FEBD5002AB344D90EC6CFCD6127BA3'];
  // interval 时间间隔单位 ONE_MINUTE MINUTE HALF_HOUR HOUR DAY WEEK MONTH
  interval;
  // range 时间段 LAST_5_MINUTE LAST_1_HOUR LAST_1_DAY LAST_1_WEEK LAST_1_MONTH LAST_1_QUARTER HALF_1_YEAR LAST_1_YEAR BEGIN_END_TIME INVALID
  range;
  // begin_time 开始时间 时间戳(例：1552477343834)
  beginTime = 1552477343834;
  // end_time 结束时间 时间戳
  endTime = 1552567343000;
  // 定时函数执行时间 默认一天
  timeInterval = 1 * 60 * 60 * 1000;
  // 卷ID
  constructor( private perService: VmfsPerformanceService, private ngZone: NgZone, private cdr: ChangeDetectorRef) {
  }

  ngAfterViewInit() {
    this.ngZone.runOutsideAngular(() => this.initChart());
  }

  ngOnInit(): void {
  }
  // 初始化表格对象
  async initChart() {
    switch (this.range) {
      case 'LAST_1_HOUR': // 过去一小时
        break;
      case 'LAST_4_HOUR': // 过去四小时 此值目前接口没有
        break;
      case 'LAST_12_HOUR': // 过去12小时 此值目前接口没有
        break;
      default: // 默认过去24h
        break;
    }
    // IOPS
    this.perService.getIopsChart('IOPS', 'IO/s', this.objTypeId, this.indicatorIdsIOPS, this.objIds,
      this.interval, this.range, this.beginTime, this.endTime).then(res => {
      this.iopsChart = res;
      this.cdr.detectChanges();
    });

    // 带宽
    this.perService.getIopsChart('Bandwidth', 'MS/s', this.objTypeId, this.indicatorIdsBDWT, this.objIds,
      this.interval, this.range, this.beginTime, this.endTime).then(res => {
      this.bandwidthChart = res;
      this.cdr.detectChanges();
    });
    // 响应时间
    this.perService.getIopsChart('Latency', 'ms', this.objTypeId, this.indicatorIdsREST, this.objIds,
      this.interval, this.range, this.beginTime, this.endTime).then(res => {
      this.latencyChart = res;
      this.cdr.detectChanges();
    });
  }
}
