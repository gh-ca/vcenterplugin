import {
  Component,
  OnInit,
  ChangeDetectionStrategy, ChangeDetectorRef,
} from '@angular/core';
import {ClrDatagridStateInterface} from '@clr/angular';
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

  result = {
    items: [{hostSetting: 'Disk Max IOSize',
       recommendValue: 1,
       level: 'waning',
       violation: 49}],
     total_count: 1
  };
  // =================END===============

  // ================主机列表=============
  hostModalShow = false;
  hostSelected = []; // 主机选中列表
  hostIsLoading = false; // table数据loading
  hostList: Host[] = []; // 数据列表
  hostTotal = 0; // 总数据数量
  currentBestpractice: Bestpractice;

  hostQuery = { // 查询数据
    q: '',
    sort: 'hostName',
    order: 'desc',
    page: 0,
    per_page: 5,
  };
  hostResult = {};
  // ================END====================

  constructor(private cdr: ChangeDetectorRef, private http: HttpClient, private commonService: CommonService) { }

  ngOnInit(): void {
  }

  apply() {}

  recheck() {
    this.http.post('v1/bestpractice', {}).subscribe((result: any) => {
      console.log(result);
    }, err => {
      console.error('ERROR', err);
    });
  }

  practiceRefresh(state: ClrDatagridStateInterface){
    this.isLoading = true;
    const params = this.commonService.refresh(state, this.query);
    this.http.get('v1/bestpractice/records/all', params).subscribe((result: any) => {
          console.log(result);
          if (result.code === '0'){
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
    this.hostRefresh(null, bestpractice);
  }

  hostRefresh(state: ClrDatagridStateInterface, bestpractice: Bestpractice){
    if (this.hostModalShow === true){
      this.hostIsLoading = true;
      console.log(bestpractice.hostList.length);
      if (state){
        const params = this.commonService.refresh(state, this.hostQuery);
        // this.http.get('https://api.github.com/search/repositories', this.hostParams).subscribe((result: any) => {
        //   console.log(result)
        //   this.hostList = result.items;
        //   this.total = result.total_count;
        //   this.hostIsLoading = false;
        //   this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        // });
        this.hostList = bestpractice.hostList;
        this.hostTotal = bestpractice.hostList.length;
      } else{
        this.hostList = bestpractice.hostList;
        this.hostTotal = bestpractice.hostList.length;
      }
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
  needReboot: string;
  hostId: string;
  autoRepair: string;
}
