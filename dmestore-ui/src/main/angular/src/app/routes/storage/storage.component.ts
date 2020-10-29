import {
  Component,
  OnInit,
  AfterViewInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef, NgZone
} from '@angular/core';
import {StorageService, StorageList, StorageChart, CapacityChart, CapacitySerie, PerforChart} from './storage.service';
import { Router} from "@angular/router";
@Component({
  selector: 'app-storage',
  templateUrl: './storage.component.html',
  styleUrls: ['./storage.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [StorageService],
})
export class StorageComponent implements OnInit, AfterViewInit {

  list: StorageList[] = []; // 数据列表
  total = 0; // 总数据数量
  isLoading = true; // table数据loading
  rowSelected = []; // 当前选中数据
  radioCheck = 'table1'; // 切换列表页显示
  buttonTrigger ='list'; // 切换列表页显示
  strorageCharts: StorageChart[] = [];
  obj_ids=[];
  constructor(private remoteSrv: StorageService, private cdr: ChangeDetectorRef, private ngZone: NgZone,private router:Router) {}
  // 生命周期： 初始化数据
  ngOnInit() {
   this.refresh();
  }

  ngAfterViewInit() {

  }
  // table数据处理
  refresh() {
    this.isLoading = true;
    this.remoteSrv.getData()
        .subscribe((result: any) => {
          this.list = result.data;
          // this.total = result.total_count;
          this.isLoading = false;
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
          //this.getStrageCharts();
          this.listperformance();
        });
  }
  // 性能视图列表
  listperformance(){
    if (this.list === null || this.list.length <= 0){ return; }
    this.list.forEach(item => {
      this.obj_ids.push(item.id);
    });
    this.remoteSrv.listperformance(this.obj_ids).subscribe((result: any) => {
      if (result.code === '200'){
        const chartList: StorageList [] = result.data;
        if ( chartList !== null && chartList.length > 0){
          this.list.forEach(item => {
            chartList.forEach(charItem => {
              if (item.id === charItem.id){
                item.maxOps=charItem.maxOps;
              }
            });
          });
          this.cdr.detectChanges();
        }
      }
    });
  }
  check(param){
    console.log(param);
    this.radioCheck = param;
    this.cdr.detectChanges();
  }
  //跳转详情页面的方法
  toDetailView(id,name){
    this.router.navigate(['storage/detail'],{
      queryParams:{
        id,name
      }
    });
  }
  // 容量单位转换
  formatCapacity(c: number){
    if(c==null) return '--';
    if (c < 1024){
      return c.toFixed(3)+" MB";
    }else if(c >= 1024 && c< 1048576){
      return (c/1024).toFixed(3) +" GB";
    }else if(c>= 1048576){
      return (c/1024/1024).toFixed(3)+" TB"
    }
  }
}
