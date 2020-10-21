import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ExpandService} from './expand.service';
import {ActivatedRoute} from '@angular/router';
import {GetForm, ServiceLevelList, VmfsListService} from '../list/list.service';

@Component({
  selector: 'app-list',
  templateUrl: './expand.component.html',
  styleUrls: ['./expand.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [ExpandService],
})
export class ExpandComponent implements OnInit{

  constructor(private remoteSrv: ExpandService, private route: ActivatedRoute, private cdr: ChangeDetectorRef) {

  }

  // 服务器/集群ID
  storageObjId = 'urn:vmomi:HostSystem:host-1034:674908e5-ab21-4079-9cb1-596358ee5dd1';

  // 扩容form
  expandForm = new GetForm().getExpandForm();

  ngOnInit(): void {
    this.initData();
  }

  /**
   * 初始化数据
   */
  initData() {
  }

  /**
   * 取消
   */
  cancel() {

  }

  /**
   * 扩容
   */
  expandHandleFunc() {
    // 容量单位转换
    switch (this.expandForm.capacityUnit) {
      case 'TB':
        this.expandForm.vo_add_capacity = this.expandForm.vo_add_capacity * 1024;
        break;
      case 'MB':
        this.expandForm.vo_add_capacity = this.expandForm.vo_add_capacity / 1024;
        break;
      case 'KB':
        this.expandForm.vo_add_capacity = this.expandForm.vo_add_capacity / (1024 * 1024);
        break;
      default: // 默认GB 不变
        break;
    }
    // 参数封装
    const params = [];
    params.push(this.expandForm);
    this.remoteSrv.expandVMFS(params).subscribe((result: any) => {
      if (result.code === '200'){
        console.log('expand success:' + name);
        // 重新请求数据
      }else {
        console.log('expand: ' + name  + ' Reason:' + result.description);
      }
      // 隐藏扩容页面
      this.cdr.detectChanges();
    });
  }
}
