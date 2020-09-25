import {
  Component,
  OnInit,
  AfterViewInit,
  OnDestroy,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  NgZone, ViewChild,
} from '@angular/core';
import { DashboardService } from './dashboard.srevice';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ClrForm} from '@clr/angular';
import {HttpClient} from '@angular/common/http';
import {isNumber} from "util";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styles: [
    `
      .mat-raised-button {
        margin-right: 8px;
        margin-top: 8px;
      }
    `,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [DashboardService],
})
export class DashboardComponent implements OnInit, AfterViewInit, OnDestroy {

  charts = this.dashboardSrv.getCharts();
  storageNumChart = null;
  storageNum = {
    total: 0,
    normal: 0,
    abnormal: 0
  };

  storageCapacityChart= null;
  storageCapacity = {
    totalCapacity: 0,
    usedCapacity: 0,
    freeCapacity: 0,
    utilization: 0,
    capacityUnit: "TB"
  };

  popShow = false;
  connectAlertSuccess = false;
  connectAlertFail = false;
  connectModel = { hostIp: '', hostPort: '', userName: '', password: ''};
  hostModel = { hostIp: '', hostPort: ''};
  connectForm = new FormGroup({
    port: new FormControl('', [
        Validators.required,
        Validators.pattern('^[0-9]*$')]),
    ip: new FormControl('', [
        Validators.required,
        Validators.pattern('((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)')]),
    username: new FormControl('', [
        Validators.required,
        Validators.maxLength(100)]),
    password: new FormControl('', [
        Validators.required])
  });
  @ViewChild(ClrForm, {static: true}) clrForm;


  constructor(
    private dashboardSrv: DashboardService,
    private ngZone: NgZone,
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {}

  ngAfterViewInit() {
    this.refresh();
    // this.ngZone.runOutsideAngular(() => this.initChart());
  }

  ngOnDestroy() {
  }

  refresh(){
    this.http.get('accessdme/refreshaccess', {}).subscribe((result: any) => {
      console.log(result);
      if (result.code === '0' || result.code === '200'){
        this.hostModel = result.data.data;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        this.loadStorageNum();
        this.loadstorageCapacity('0');
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  storageCapacityChartInit(chart){
    this.storageCapacityChart = chart;
  }
  loadstorageCapacity(type: string){
    this.storageCapacityChart.showLoading();
    this.http.get('overview/getdatastoreoverview', { params: {type: type}}).subscribe((result: any) => {
      console.log(result);
      if (result.code === '0' || result.code === '200'){
        result.data.totalCapacity = result.data.totalCapacity.toFixed(2);
        result.data.usedCapacity = result.data.usedCapacity.toFixed(2);
        result.data.freeCapacity = result.data.freeCapacity.toFixed(2);
        result.data.utilization = result.data.utilization.toFixed(2);
        this.storageCapacity = result.data;
        console.log(this.storageCapacity);
        const os = [
          {
            name: 'Used',
            value: result.data.usedCapacity
          },
          {
            name: 'Free',
            value: result.data.freeCapacity
          }
        ];
        this.dashboardSrv.storageCapacityOption.series[0].data = os;
        this.dashboardSrv.storageCapacityOption.title.text = this.storageCapacity.utilization + ' %';
        this.storageCapacityChart.setOption(this.dashboardSrv.storageCapacityOption, true);
        this.storageCapacityChart.hideLoading();
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  storageNumChartInit(chart){
    this.storageNumChart = chart;
  }

  loadStorageNum(){
    this.storageNumChart.showLoading();
    this.http.get('overview/getstoragenum', {}).subscribe((result: any) => {
      console.log(result);
      if (result.code === '0' || result.code === '200'){
        this.storageNum = result.data;
        console.log(this.storageNum);
        const os = [
          {
            name: 'normal',
            value: result.data.normal
          },
          {
            name: 'abnormal',
            value: result.data.abnormal
          }
        ];
        this.dashboardSrv.storageNumOption.series[0].data = os;
        this.storageNumChart.setOption(this.dashboardSrv.storageNumOption, true);
        this.storageNumChart.hideLoading();
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  // 提交表单连接DME
  submit() {
    if (this.connectForm.invalid) {
      this.clrForm.markAsTouched();
    } else {
      // Do submit logic
      this.popShow = false;
      console.log(this.connectModel);
      this.http.post('accessdme/access', this.connectModel).subscribe((result: any) => {
        console.log(result);
        if (result.code !== '0' && result.code !== '200'){
             this.connectAlertFail = true;
             /*setTimeout(() => {
               this.connectAlertFail = false;
             }, 1000);*/
        } else{
          this.refresh();
          this.connectAlertSuccess = true;
          /*setTimeout(() => {
            this.connectAlertSuccess = false;
          }, 1000);*/
        }
      });
      this.resetForm();
    }
  }

  resetForm() {
    this.connectForm.reset();
  }

  showPop(){
    this.popShow = true;
    this.resetForm();
  }
}
