import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {ActivatedRoute} from '@angular/router';
import {DeleteService} from "../delete/delete.service";

@Component({
  selector: 'app-list',
  templateUrl: './delete.component.html',
  styleUrls: ['./delete.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [DeleteService],
})
export class DeleteComponent implements OnInit{

  constructor(private remoteSrv: DeleteService, private route: ActivatedRoute, private cdr: ChangeDetectorRef) {

  }

  // 服务器/集群ID
  hostOrClusterId = 'urn:vmomi:HostSystem:host-1034:674908e5-ab21-4079-9cb1-596358ee5dd1';
  // 服务器/集群名称
  hostOrClusterName = '10.143.133.17';
  //
  volumeIds = [];
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

  // 删除VMFS 处理函数
  delHandleFunc() {
    console.log('del vmfs volumeIds:' + this.volumeIds);
    this.remoteSrv.delVmfs(this.volumeIds).subscribe((result: any) => {
      // 隐藏删除提示页面
      if (result.code === '200'){
        console.log('DEL success');
      } else {
        console.log('DEL faild: ' + result.description);
      }
      // 关闭删除页面
      this.cdr.detectChanges();
    });
  }

}
