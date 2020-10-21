import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ServiceLevelService} from './serviceLevel.service';
import {ActivatedRoute} from '@angular/router';
import {GetForm, ServiceLevelList, VmfsListService} from '../list/list.service';

@Component({
  selector: 'app-list',
  templateUrl: './serviceLevel.component.html',
  styleUrls: ['./serviceLevel.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [ServiceLevelService],
})
export class ServiceLevelComponent implements OnInit{

  constructor(private remoteSrv: ServiceLevelService, private route: ActivatedRoute, private cdr: ChangeDetectorRef) {

  }

  // 未选择服务等级true未选择 false选择
  serviceLevelIsNull = false;
  // 服务等级列表
  serviceLevelList: ServiceLevelList[] = [];

  // 变更服务等级
  changeServiceLevelForm = new GetForm().getChangeLevelForm();

  // 服务器/集群ID
  hostOrClusterId = 'urn:vmomi:HostSystem:host-1034:674908e5-ab21-4079-9cb1-596358ee5dd1';

  ngOnInit(): void {
    // 初始化dataStore数据
    this.getServiceLevels();
  }

  /**
   * 获取服务等级数据
   */
  getServiceLevels() {
    // 初始化服务等级选择参数
    this.serviceLevelIsNull = false;
    // 获取服务等级数据
    this.remoteSrv.getServiceLevelList().subscribe((result: any) => {
      console.log(result);
      if (result.code === '200' && result.data !== null) {
        this.serviceLevelList = result.data.data;
        console.log('this.serviceLevelList', this.serviceLevelList);
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    });
  }
  /**
   * 取消
   */
  cancel() {

  }

  /**
   * 服务登记变更处理
   */
  changeSLHandleFunc() {
    const selectResult = this.serviceLevelList.find(item => item.show === true);
    console.log('selectResult', selectResult);
    if (selectResult) {
      this.serviceLevelIsNull = false;
      this.changeServiceLevelForm.service_level_id = selectResult.id;
      this.changeServiceLevelForm.service_level_name = selectResult.name;


      this.remoteSrv.changeServiceLevel(this.changeServiceLevelForm).subscribe((result: any) => {
        if (result.code === '200'){
          console.log('change service level success:' + name);
          // 重新请求数据
        } else {
          console.log('change service level faild: ' + name  + ' Reason:' + result.description);
        }
        // 关闭修改服务等级页面
        this.cdr.detectChanges();
      });
    } else {
      this.serviceLevelIsNull = true;
      console.log('服务等级不能为空！');
    }
  }
}
