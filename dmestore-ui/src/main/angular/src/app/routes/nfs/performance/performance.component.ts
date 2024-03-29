import {
  AfterViewInit,
  Component,
  NgZone,
  OnInit,
  ChangeDetectorRef,
  NgModule,
} from '@angular/core';
import { EChartOption } from 'echarts';
import { MakePerformance, NfsService } from '../nfs.service';
import { PerformanceService } from './performance.service';

import { NgxEchartsModule } from 'ngx-echarts';
import { GlobalsService } from '../../../shared/globals.service';
import { FileSystemService, FsDetail } from '../file-system/file-system.service';
import { FormControl, FormGroup } from '@angular/forms';
import { TranslatePipe } from '@ngx-translate/core';
import { CommonService } from './../../common.service';

@Component({
  selector: 'app-nfsperformance',
  templateUrl: './performance.component.html',
  styleUrls: ['./performance.component.scss'],
  providers: [
    CommonService,
    PerformanceService,
    TranslatePipe,
    MakePerformance,
    NfsService,
    NgxEchartsModule,
    FileSystemService,
  ],
})
export class NfsPerformanceComponent implements OnInit, AfterViewInit {
  fsDetails: FsDetail[];
  chooseFs: FsDetail;
  fsNames: string[] = [];
  defaultSelect: string;

  rangeTime = new FormGroup({
    start: new FormControl(),
    end: new FormControl(),
  });
  // 创建表格对象
  // OPS+QoS上下限
  opsChart: EChartOption = {};
  opsChartDataIsNull = false;
  // 带宽+QoS上下限
  bandwidthChart: EChartOption = {};
  bandwidthChartDataIsNull = false;
  // 响应时间+QoS下限
  latencyChart: EChartOption = {};
  latencyChartDataIsNull = false;
  /* DTS202103270EB3F3P0G00 */
  timeSelectorRanges = [];
  selectRange = 'LAST_1_DAY';
  startTime = null;
  // endTime
  endTime = null;
  constructor(
    private makePerformance: MakePerformance,
    private perService: PerformanceService,
    private ngZone: NgZone,
    private cdr: ChangeDetectorRef,
    private fsService: FileSystemService,
    private gs: GlobalsService,
    private translatePipe: TranslatePipe,
    private commonService: CommonService
  ) {
    /* DTS202103270EB3F3P0G00 */
    this.timeSelectorRanges = this.commonService.timeSelectorRanges_type2;
  }

  ngAfterViewInit() {
    //this.ngZone.runOutsideAngular(() => this.initChart());
  }

  ngOnInit(): void {
    const ctx = this.gs.getClientSdk().app.getContextObjects();
    const objectId = ctx
      ? ctx[0].id
      : 'urn:vmomi:Datastore:datastore-12024:674908e5-ab21-4079-9cb1-596358ee5dd1';
    this.getFsDetail(objectId);
  }
  changeFs() {
    if (this.selectRange === 'BEGIN_END_TIME') {
      if (this.startTime === null || this.endTime === null) {
        console.log('开始结束时间不能为空');
        return;
      }
    } else {
      // 初始化开始结束时间
      this.startTime = null;
      this.endTime = null;
    }
    if (this.selectRange) {
      const paramsInfo = this.commonService.getInfoFromTimeRange(this.selectRange);
      this.startTime = paramsInfo.begin_time || '';
      this.endTime = paramsInfo.end_time || '';
      console.log('this.selectVolName+this.selectRange', this.defaultSelect, this.selectRange);
      // 获取已选择的卷
      this.chooseFs = this.getVolByName(this.defaultSelect);
      // 请求后台重新加载折线图
      this.initChart(paramsInfo);
    } else {
      console.log('未选择卷或range');
    }
  }
  getFsDetail(objectId) {
    this.fsService.getData(objectId).subscribe((result: any) => {
      this.fsDetails = result.data;
      this.fsDetails.forEach(f => {
        this.fsNames.push(f.name);
      });
      this.defaultSelect = this.fsNames[0];
      this.chooseFs = this.getVolByName(this.defaultSelect);
      this.ngZone.runOutsideAngular(() => this.initChart());
    });
  }

  // 初始化表格对象
  async initChart(paramsInfo = {}) {
    const fsNames: string[] = [];
    fsNames.push(this.chooseFs?.fileSystemId || 'A7213075B5EE3AF3989D7DB938ED2CF8');
    //ops
    this.makePerformance
      .setChartVmfs(
        paramsInfo,
        300,
        this.translatePipe.transform('nfs.ops'),
        'IO/s',
        NfsService.nfsOPS,
        fsNames,
        this.selectRange,
        NfsService.nfsUrl,
        this.startTime,
        this.endTime
      )
      .then(res => {
        this.opsChart = res;
        this.opsChartDataIsNull = res['series'][0].data.length < 1;
        this.cdr.detectChanges();
      });
    // 带宽
    this.makePerformance
      .setChartVmfs(
        paramsInfo,
        300,
        this.translatePipe.transform('nfs.qos_bandwidth'),
        'MB/s',
        NfsService.nfsBDWT,
        fsNames,
        this.selectRange,
        NfsService.nfsUrl,
        this.startTime,
        this.endTime
      )
      .then(res => {
        this.bandwidthChart = res;
        this.bandwidthChartDataIsNull = res['series'][0].data.length < 1;
        this.cdr.detectChanges();
      });
    /* 时延 */
    this.makePerformance
      .setChartVmfs(
        paramsInfo,
        300,
        this.translatePipe.transform('nfs.qos_latency'),
        'ms',
        NfsService.nfsLatency,
        fsNames,
        this.selectRange,
        NfsService.nfsUrl,
        this.startTime,
        this.endTime
      )
      .then(res => {
        this.latencyChart = res;
        this.latencyChartDataIsNull = res['series'][0].data.length < 1;
        this.cdr.detectChanges();
      });
  }

  /**
   * 开始结束时间触发
   */
  changeDate() {
    if (
      !this.rangeTime.controls.start.hasError('matStartDateInvalid') &&
      !this.rangeTime.controls.end.hasError('matEndDateInvalid') &&
      this.rangeTime.controls.start.value !== null &&
      this.rangeTime.controls.end.value !== null
    ) {
      // 需满足输入规范且不为空
      this.startTime = this.rangeTime.controls.start.value._d.getTime();
      this.endTime = this.rangeTime.controls.end.value._d.getTime();
      console.log('startTime', this.startTime);
      console.log('endTime', this.endTime);
      this.changeFs();
    } else {
      return;
    }
  }

  // 通过名称获取卷信息
  getVolByName(name): any {
    const volumeInfo = this.fsDetails.filter(item => item.name === name)[0];
    return volumeInfo;
  }
}
