import {
  Component,
  OnInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef
} from '@angular/core';
import {StorageService, StorageList} from './storage.service';

@Component({
  selector: 'app-storage',
  templateUrl: './storage.component.html',
  styleUrls: ['./storage.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [StorageService],
})
export class StorageComponent implements OnInit {

  list: StorageList[] = []; // 数据列表
  total = 0; // 总数据数量
  isLoading = true; // table数据loading
  rowSelected = []; // 当前选中数据
  radioCheck = 'table1'; // 切换列表页显示
  constructor(private remoteSrv: StorageService, private cdr: ChangeDetectorRef) {}
  // 生命周期： 初始化数据
  ngOnInit() {
    this.refresh();
  }
  // table数据处理
  refresh() {
    this.isLoading = true;
    this.remoteSrv.getData()
        .subscribe((result: any) => {
          console.log(result);
          this.list = result.data.data;
          // this.total = result.total_count;
          this.isLoading = false;
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        });
  }
  check(param){
    console.log(param);
    this.radioCheck = param;
    this.cdr.detectChanges();
  }
}
