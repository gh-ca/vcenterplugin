import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {ActivatedRoute} from '@angular/router';
import {ReclaimService} from "./reclaim.service";

@Component({
  selector: 'app-list',
  templateUrl: './reclaim.component.html',
  styleUrls: ['./reclaim.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [ReclaimService],
})
export class ReclaimComponent implements OnInit{

  constructor(private remoteSrv: ReclaimService, private route: ActivatedRoute, private cdr: ChangeDetectorRef) {

  }

  // 服务器/集群ID
  hostOrClusterId = 'urn:vmomi:HostSystem:host-1034:674908e5-ab21-4079-9cb1-596358ee5dd1';
  // 服务器/集群名称
  hostOrClusterName = '10.143.133.17';
  //
  vmfsNames = [];
  ngOnInit(): void {
    this.initData();
    // 初始化添加数据

  }

  initData() {
    // 设备类型 操作类型初始化
    this.route.url.subscribe(url => {
      console.log('url', url);
    });

  }

  /**
   * 取消
   */
  cancel() {

  }

  // 回收空间 处理
  reclaimHandleFunc() {
    this.remoteSrv.reclaimVmfs(this.vmfsNames).subscribe((result: any) => {
      if (result.code === '200'){
        console.log('Reclaim success');
        // 空间回收完成重新请求数据
      } else {
        console.log('Reclaim fail：' + result.description);
      }
      // 关闭回收空间页面
      this.cdr.detectChanges();
    });
  }

}
