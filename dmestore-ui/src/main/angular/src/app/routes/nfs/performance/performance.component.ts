import {AfterViewInit, Component, NgZone, OnInit, ChangeDetectorRef, NgModule} from '@angular/core';
import { EChartOption } from 'echarts';
import {MakePerformance, NfsService} from "../nfs.service";
import {PerformanceService} from "./performance.service";
import { NgxEchartsModule } from 'ngx-echarts';
import {GlobalsService} from "../../../shared/globals.service";
import {FileSystemService, FsDetail} from "../file-system/file-system.service";
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-nfsperformance',
  templateUrl: './performance.component.html',
  styleUrls: ['./performance.component.scss'],
  providers: [PerformanceService, MakePerformance, NfsService,NgxEchartsModule,FileSystemService],
})
export class NfsPerformanceComponent implements OnInit, AfterViewInit {
  fsDetails: FsDetail[];
  fsNames: string[] = [];
  defaultSelect: string;

  rangeTime = new FormGroup({
    start: new FormControl(),
    end: new FormControl()
  });
  // 创建表格对象
  // OPS+QoS上下限
  opsChart: EChartOption = {};
  // 带宽+QoS上下限
  bandwidthChart: EChartOption = {};
  // 响应时间+QoS下限
  latencyChart: EChartOption = {};
  // ranges
  ranges = NfsService.perRanges;
  selectRange = 'LAST_1_DAY';
  startTime = null;
  // endTime
  endTime = null;
  constructor(private makePerformance: MakePerformance, private perService: PerformanceService,
              private ngZone: NgZone, private cdr: ChangeDetectorRef, private fsService: FileSystemService,
              private gs: GlobalsService) {
  }

  ngAfterViewInit() {
    this.ngZone.runOutsideAngular(() => this.initChart());
  }

  ngOnInit(): void {
    const ctx = this.gs.getClientSdk().app.getContextObjects();
    //this.getFsDetail(ctx[0].id);
  }
  changeFs(){}
  getFsDetail(objectId){
    this.fsService.getData(objectId).subscribe((result: any) => {
      this.fsDetails = result.data;
      this.fsDetails.forEach(f => {
        this.fsNames.push(f.name);
      });
      this.defaultSelect = this.fsNames[0];
    });
  }

  // 初始化表格对象
  async initChart() {
    const fsNames:string[] = [];
    fsNames.push('1282FFE20AA03E4EAC9A814C687B780A');
    //ops
    this.makePerformance.setChart(300,"OPS","IO/s",
      NfsService.nfsOPS,fsNames,this.selectRange,NfsService.nfsUrl, this.startTime, this.endTime).then(res=>{
        this.opsChart = res;
        this.cdr.detectChanges();
    });
    // 带宽
    this.makePerformance.setChart(300,'Bandwidth', 'MB/s', NfsService.nfsBDWT,
      fsNames, this.selectRange, NfsService.nfsUrl, this.startTime, this.endTime).then(res => {
      this.bandwidthChart = res;
      this.cdr.detectChanges();
    });
    this.makePerformance.setChart(300,'Latency', 'ms', NfsService.nfsLatency, fsNames,
      this.selectRange, NfsService.nfsUrl, this.startTime, this.endTime).then(res => {
      this.latencyChart = res;
      this.cdr.detectChanges();
    });
  }

  /**
   * 开始结束时间触发
   */
  changeDate() {
    if (!this.rangeTime.controls.start.hasError('matStartDateInvalid')
      && !this.rangeTime.controls.end.hasError('matEndDateInvalid')
      && this.rangeTime.controls.start.value !== null && this.rangeTime.controls.end.value !== null) { // 需满足输入规范且不为空
      this.startTime = this.rangeTime.controls.start.value._d.getTime();
      this.endTime = this.rangeTime.controls.end.value._d.getTime();
      console.log('startTime', this.startTime);
      console.log('endTime', this.endTime);
      this.changeFs();
    } else {
      return;
    }
  }

}
