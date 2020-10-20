import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ModifyService} from './modify.service';
import {ActivatedRoute} from '@angular/router';
import {GetForm, ServiceLevelList, VmfsListService} from '../list/list.service';

@Component({
  selector: 'app-list',
  templateUrl: './modify.component.html',
  styleUrls: ['./modify.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [ModifyService],
})
export class ModifyComponent implements OnInit{

  constructor(private remoteSrv: ModifyService, private route: ActivatedRoute, private cdr: ChangeDetectorRef) {

  }

  // 编辑form提交数据
  modifyForm = new GetForm().getEditForm();
  // 是否为服务等级
  isServiceLevelData = false;

  // 存储ID
  storageObjId = 'urn:vmomi:HostSystem:host-1034:674908e5-ab21-4079-9cb1-596358ee5dd1';
  // 服务器/集群名称
  vmfsName = '10.143.133.17';
  // 卷名称
  volumeName = '';

  ngOnInit(): void {
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
  /**
   * 修改
   */
  modifyHandleFunc() {
    // 设置修改的卷名称以及修改后的名称
    if (this.modifyForm.isSameName) {
      this.modifyForm.newVoName = this.modifyForm.name;
    } else {
      // 若不修改卷名称则将旧的卷名称设置为newVol
      this.modifyForm.newVoName = this.volumeName;
    }
    if (this.isServiceLevelData) {
      if (this.modifyForm.max_bandwidth === null && this.modifyForm.max_iops === null
        && this.modifyForm.min_bandwidth === null && this.modifyForm.min_iops === null && this.modifyForm.latency === null) {
        this.modifyForm.control_policy = null;
      }
    }
    this.modifyForm.newDsName = this.modifyForm.name;
    console.log('this.modifyForm:', this.modifyForm);
    this.remoteSrv.updateVmfs(this.modifyForm.volume_id, this.modifyForm).subscribe((result: any) => {
      if (result.code === '200') {
        console.log('modify success:' + this.modifyForm.oldDsName);
      } else {
        console.log('modify faild：' + this.modifyForm.oldDsName + result.description);
      }
      // 关闭编辑窗口
    });
  }
}
