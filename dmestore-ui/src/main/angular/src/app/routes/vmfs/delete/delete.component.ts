import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit, ViewChild} from "@angular/core";
import {ActivatedRoute, Router} from '@angular/router';
import {DeleteService} from "../delete/delete.service";
import {GlobalsService} from "../../../shared/globals.service";
import {ClrModal, ClrWizard} from "@clr/angular";

@Component({
  selector: 'app-list',
  templateUrl: './delete.component.html',
  styleUrls: ['./delete.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [DeleteService],
})
export class DeleteComponent implements OnInit{

  constructor(private remoteSrv: DeleteService, private route: ActivatedRoute, private cdr: ChangeDetectorRef, private router:Router, private globalsService: GlobalsService) {

  }

  // 存储ID
  objectId = 'urn:vmomi:HostSystem:host-1034:674908e5-ab21-4079-9cb1-596358ee5dd1';

  // 操作来源 list:列表页面、dataStore：在DataStore菜单页面操作
  resource;

  // 待删除VMFS的卷ID
  objectIds = [];

  delShow:boolean;

  ngOnInit(): void {
    this.initData();
  }

  initData() {
    this.delShow = true;
    // 设备类型 操作类型初始化
    this.route.url.subscribe(url => {
      console.log('url', url);
      this.route.queryParams.subscribe(queryParam => {
        this.resource = queryParam.resource;
        this.objectId = queryParam.objectId;
        let objIds = [];
        if (this.resource === 'list') { // 列表入口
          objIds = this.objectId.split(',');
        } else { // dataStore入口
          objIds.push(this.objectId);
        }
        this.objectIds = objIds;
        console.log('del vmfs objectIds:' + objIds);
      });
    });

  }

  /**
   * 取消
   */
  cancel() {
    console.log('this.resource', this.resource);
    this.delShow = false;
    if (this.resource === 'list') { // 列表入口
      this.router.navigate(['vmfs/list']);
    } else { // dataStore入口
      this.globalsService.getClientSdk().modal.close();
    }
  }

  // 删除VMFS 处理函数
  delHandleFunc() {
    const delInfos = {
      dataStoreObjectIds: this.objectIds
    }
    this.remoteSrv.delVmfs(delInfos).subscribe((result: any) => {
      // 隐藏删除提示页面
      if (result.code === '200'){
        console.log('DEL success');
      } else {
        console.log('DEL faild: ' + result.description);
      }
      this.cdr.detectChanges();
      // 关闭删除页面
      this.cancel();
    });
  }

}
