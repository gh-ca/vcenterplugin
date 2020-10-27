import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ExpandService} from './expand.service';
import {ActivatedRoute, Router} from '@angular/router';
import {GetForm, ServiceLevelList, VmfsInfo, VmfsListService} from '../list/list.service';
import {GlobalsService} from "../../../shared/globals.service";

@Component({
  selector: 'app-list',
  templateUrl: './expand.component.html',
  styleUrls: ['./expand.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [ExpandService],
})
export class ExpandComponent implements OnInit{

  constructor(private remoteSrv: ExpandService, private route: ActivatedRoute, private cdr: ChangeDetectorRef,
              private router:Router, private globalsService: GlobalsService) {

  }

  // 服务器/集群ID
  objectId;

  // 扩容form
  expandForm = new GetForm().getExpandForm();

  // 操作来源 list:列表页面、dataStore：在DataStore菜单页面操作
  resource;

  // vmfs数据
  vmfsInfo: VmfsInfo;

  // 弹窗隐藏/显示
  // expandShow = false;

  ngOnInit(): void {
    this.initData();
  }

  /**
   * 初始化数据
   */
  initData() {
    // this.expandShow = false;
    // 设备类型 操作类型初始化
    this.route.url.subscribe(url => {
      console.log('url', url);
      this.route.queryParams.subscribe(queryParam => {
        this.resource = queryParam.resource;
        if (this.resource === 'list') {
          this.objectId = queryParam.objectId;
        } else {
          const ctx = this.globalsService.getClientSdk().app.getContextObjects();
          this.objectId = ctx[0].id;
        }
        this.remoteSrv.getVmfsById(this.objectId).subscribe((result: any) => {
          console.log('VmfsInfo:', result);
          if (result.code === '200' && null != result.data) {
            this.vmfsInfo = result.data[0];
            // form表单数据初始化
            this.expandForm.volume_id = this.vmfsInfo.volumeId;
            this.expandForm.ds_name = this.vmfsInfo.name;
          }
          // this.expandShow = true;
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        });
      });
    });
    // 初始化form表单
    this.expandForm = new GetForm().getExpandForm();
  }

  /**
   * 取消/关闭页面
   */
  cancel() {
    if (this.resource === 'list') { // 列表入口
      this.router.navigate(['vmfs/list']);
    } else { // dataStore入口
      this.globalsService.getClientSdk().modal.close();
    }
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
    this.expandForm.obj_id = this.objectId;
    // 参数封装
    this.remoteSrv.expandVMFS(this.expandForm).subscribe((result: any) => {
      if (result.code === '200'){
        console.log('expand success:' + name);
        // 重新请求数据
      }else {
        console.log('expand: ' + name  + ' Reason:' + result.description);
      }
      this.cdr.detectChanges();
      // 隐藏扩容页面
      this.cancel();
    });
  }
}
