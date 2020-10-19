import {
  Component,
  OnInit,
  ChangeDetectionStrategy, ChangeDetectorRef,
} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { CommonService } from '../common.service';

@Component({
  selector: 'app-bestpractice',
  templateUrl: './bestpractice.component.html',
  styleUrls: ['./bestpractice.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [CommonService]
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
  hostSelected = []; // 主机选中列表
  hostIsLoading = false; // table数据loading
  hostList: Host[] = []; // 数据列表
  hostTotal = 0; // 总数据数量
  currentBestpractice: Bestpractice;
  // ================END====================

  constructor(private cdr: ChangeDetectorRef, private http: HttpClient, private commonService: CommonService) { }

  ngOnInit(): void {
  }

  applySelectedPractice() {
    const params = [];
    let ips = '';
    this.rowSelected.forEach((item) => {
      const i = {hostSetting:'', hostObjectIds: []};
      i.hostSetting = item.hostSetting;
      item.hostList.forEach((s) => {
        i.hostObjectIds.push(s.hostObjectId);
        if (s.needReboot == "true"){
          ips += s.hostName+",";
        }
      });
      params.push(i);
    });

    if (ips.length != 0){
      alert('modify config, you need reboot ' + ips);
    }

    console.log(params);
    this.http.post('v1/bestpractice/update/bylist', params).subscribe((result: any) => {
      if (result.code == '200'){
        this.practiceRefresh();
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  applyByHosts(){
    const params = [];
    const i = {hostSetting:'', hostObjectIds: []};
    i.hostSetting = this.currentBestpractice.hostSetting;
    let ips = '';
    this.hostSelected.forEach((s) => {
      i.hostObjectIds.push(s.hostObjectId);
      if (s.needReboot == "true"){
        ips += s.hostName+",";
      }
    });
    params.push(i);

    if (ips.length != 0){
      alert('modify config, you need reboot ' + ips);
    }

    console.log(params);
    this.http.post('v1/bestpractice/update/bylist', params).subscribe((result: any) => {
      if (result.code == '200'){
        this.practiceRefresh();
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  recheck() {
    this.http.post('v1/bestpractice/check', {}).subscribe((result: any) => {
    }, err => {
      console.error('ERROR', err);
    });
  }

  practiceRefresh(){
    this.isLoading = true;
    //const params = this.commonService.refresh(state, this.query);
    this.http.get('v1/bestpractice/records/all', {}).subscribe((result: any) => {
          if (result.code === '200'){
            this.list = result.data;
            this.total = result.data.length;
            this.isLoading = false;
            this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
          }
    }, err => {
      console.error('ERROR', err);
    });
  }

  openHostList(bestpractice: Bestpractice){
    this.hostModalShow = true;
    this.currentBestpractice = bestpractice;
    this.hostRefresh();
  }

  hostRefresh(){
    if (this.hostModalShow === true){
      this.hostIsLoading = true;
      this.hostList = this.currentBestpractice.hostList;
      this.hostTotal = this.currentBestpractice.hostList.length;
      this.hostIsLoading = false;
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    }
  }
}

class Bestpractice {
  hostSetting: string;
  recommendValue: number;
  level: string;
  count: number;
  hostList: Host[];
}

class Host {
  hostSetting: string;
  level: string;
  hostName: string;
  recommendValue: number;
  actualValue: number;
  hostObjectId: string;
  needReboot: string;
  hostId: string;
  autoRepair: string;
}
