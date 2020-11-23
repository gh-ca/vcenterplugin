import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from '@angular/router';
import {ReclaimService} from "./reclaim.service";
import {GlobalsService} from "../../../shared/globals.service";

@Component({
  selector: 'app-list',
  templateUrl: './reclaim.component.html',
  styleUrls: ['./reclaim.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [ReclaimService],
})
export class ReclaimComponent implements OnInit{

  constructor(private remoteSrv: ReclaimService, private route: ActivatedRoute, private cdr: ChangeDetectorRef,
              private router:Router, private globalsService: GlobalsService) {

  }

  // 存储ID
  objectId;

  // 待回收空间的VMFS的卷ID
  objectIds = [];

  // 操作来源 list:列表页面、dataStore：在DataStore菜单页面操作
  resource;

  // 空间回收 隐藏/显示
  reclaimShow = false;
  modalHandleLoading = false; // 数据处理loading
  isOperationErr = false; // 错误信息
  modalLoading = false; // 数据加载loading
  reclaimSuccessShow = false; // 空间回收成功窗口

  ngOnInit(): void {
    this.reclaimShow = false;
    this.initData();
    // 初始化添加数据

  }

  initData() {
    this.modalHandleLoading = false;
    this.isOperationErr = false;
    this.modalLoading = true;
    // 设备类型 操作类型初始化
    this.route.url.subscribe(url => {
      this.route.queryParams.subscribe(queryParam => {
        this.resource = queryParam.resource;
        let objIds = [];
        if (this.resource === 'list') { // 列表入口
          objIds = queryParam.objectId.split(',');
        } else { // dataStore入口
          const ctx = this.globalsService.getClientSdk().app.getContextObjects();
          this.objectId = ctx[0].id;
          objIds.push(this.objectId);
        }
        this.reclaimShow = true;
        this.modalLoading = false;
        this.objectIds = objIds;
        console.log('del vmfs objectIds:' + objIds);
      });
    });

  }

  /**
   * 取消/关闭
   */
  cancel() {
    this.reclaimShow = false;
    if (this.resource === 'list') { // 列表入口
      this.router.navigate(['vmfs/list']);
    } else { // dataStore入口
      this.globalsService.getClientSdk().modal.close();
    }
  }

  // 回收空间 处理
  reclaimHandleFunc() {
    this.modalHandleLoading = true;
    this.remoteSrv.reclaimVmfs(this.objectIds).subscribe((result: any) => {
      this.modalHandleLoading = false;
      if (result.code === '200'){
        console.log('Reclaim success');

        this.reclaimSuccessShow = true;
      } else {
        console.log('Reclaim fail：' + result.description);
        this.isOperationErr = true;
      }
      // 关闭回收空间页面
      this.cdr.detectChanges();
    });
  }
  /**
   * 确认操作结果并关闭窗口
   */
  confirmActResult() {
    this.cancel();
  }
}
