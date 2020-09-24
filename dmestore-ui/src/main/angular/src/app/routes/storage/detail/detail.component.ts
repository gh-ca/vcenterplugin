import {AfterViewInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, NgZone, OnInit} from '@angular/core';
import {DetailService, PoolList, StorageDetail} from './detail.service';
import {VmfsPerformanceService} from '../../vmfs/volume-performance/performance.service';
import * as echarts from 'echarts';
import { EChartOption } from 'echarts';
@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [DetailService, VmfsPerformanceService],
})
export class DetailComponent implements OnInit, AfterViewInit {
  options = {
    tooltip: {
      trigger: 'item',
      formatter: ' {b}: {c} ({d}%)'
    },
    title: {
      text: '123',
      textAlign: 'center',
      padding: 0,
      textVerticalAlign: 'middle',
      textStyle: {
        fontSize: 22,
        color: '#63B3F7'
      },
      subtextStyle: {
        fontSize: 12,
        color: '#c2c6dc',
        align: 'center'
      },
      left: '50%',
      top: '50%',
      //subtext: '234'
    },

    series: [
      {
        name: '',
        type: 'pie',
        radius: ['50%', '70%'],
        center: ['50%', '50%'],

        avoidLabelOverlap: false,
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: false,
            fontSize: '30',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          {value: 335, name: '直接访问'},
          {value: 310, name: '邮件营销'},
          {value: 234, name: '联盟广告'},
          {value: 135, name: '视频广告'},
          {value: 1548, name: '搜索引擎'}
        ]
      }
    ],
    color: ['#FF0000', '#FF9538', '#63B3F7']
  }
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
  // 存储池列表
  poolList: PoolList[] = [];
  poolRadio = 'table1'; // 存储池列表切换
  volumeRadio = 'table1'; // volume列表切换
  storageId = '1234';
  constructor(private detailService: DetailService, private cdr: ChangeDetectorRef, private ngZone: NgZone, private perService: VmfsPerformanceService, ) { }
  detail: StorageDetail;
  ngOnInit(): void {
    // this.getStorageDetail(this.storageId);
  }
  ngAfterViewInit() {
    this.ngZone.runOutsideAngular(() => this.initChart());
  }
  getPoolList(){
    this.detailService.getPoolList(null).subscribe((result: any) => {
      this.poolList = result.data;
    });
  }
  getStorageDetail(storageId){
    this.detailService.getStorageDetail(storageId).subscribe((r: any) => {
      if (r.code === '0'){
        this.detail = r.data.data;
        this.cdr.detectChanges();
      }
    });
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
    /* // IOPS
    this.iopsChart = echarts.init(document.querySelector('#iopsChart'));
    this.perService.getIopsChart('IOPS', 'IO/s', this.objTypeId, this.indicatorIdsIOPS, this.objIds,
      this.interval, this.range, this.beginTime, this.endTime).then(res => {
      this.iopsChart.setOption(res, true);
      this.cdr.detectChanges();
    });

    // 带宽
    this.bandwidthChart = echarts.init(document.querySelector('#bandwidthChart'));
    this.perService.getIopsChart('Bandwidth', 'MS/s', this.objTypeId, this.indicatorIdsBDWT, this.objIds,
      this.interval, this.range, this.beginTime, this.endTime).then(res => {
      this.bandwidthChart.setOption(res, true);
      this.cdr.detectChanges();
    });
    // 响应时间
    this.latencyChart = echarts.init(document.querySelector('#latencyChart'));
    this.perService.getIopsChart('Latency', 'ms', this.objTypeId, this.indicatorIdsREST, this.objIds,
      this.interval, this.range, this.beginTime, this.endTime).then(res => {
      this.latencyChart.setOption(res, true);
      this.cdr.detectChanges();
    }); */
  }
  async showChart() {
    console.log("更新图表")
  }
}
