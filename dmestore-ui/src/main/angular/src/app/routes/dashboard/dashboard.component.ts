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
    this.ngZone.runOutsideAngular(() => this.initChart());
  }

  ngOnDestroy() {
  }

  refresh(){
    this.http.get('http://localhost:8080/accessdme/refreshaccess', {}).subscribe((result: any) => {
      console.log(result);
      if (result.code === '0' || result.code === '200'){
        this.hostModel = result.data.data;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  initChart() {
  }

  // 提交表单连接DME
  submit() {
    if (this.connectForm.invalid) {
      this.clrForm.markAsTouched();
    } else {
      // Do submit logic
      this.popShow = false;
      console.log(this.connectModel);
      this.http.post('http://localhost:8080/accessdme/access', this.connectModel).subscribe((result: any) => {
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
