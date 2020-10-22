import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ServiceLevelService} from './serviceLevel.service';
import {ActivatedRoute, Router} from '@angular/router';
import {GetForm, ServiceLevelList, VmfsInfo, VmfsListService} from '../list/list.service';
import {GlobalsService} from "../../../shared/globals.service";

@Component({
  selector: 'app-list',
  templateUrl: './serviceLevel.component.html',
  styleUrls: ['./serviceLevel.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [ServiceLevelService],
})
export class ServiceLevelComponent implements OnInit{

  constructor(private remoteSrv: ServiceLevelService, private route: ActivatedRoute, private cdr: ChangeDetectorRef,
              private router:Router, private globalsService: GlobalsService) {

  }

  // 未选择服务等级true未选择 false选择
  serviceLevelIsNull = false;
  // 服务等级列表
  serviceLevelList: ServiceLevelList[] = [];

  // 变更服务等级
  changeServiceLevelForm = new GetForm().getChangeLevelForm();

  // 服务器/集群ID
  objectId;

  // 操作来源 list:列表页面、dataStore：在DataStore菜单页面操作
  resource;

  // vmfs数据
  vmfsInfo: VmfsInfo;

  ngOnInit(): void {
    // 获取vmfsInfo
    // 初始化表单
    this.changeServiceLevelForm = new GetForm().getChangeLevelForm();
    this.route.url.subscribe(url => {
      console.log('url', url);
      this.route.queryParams.subscribe(queryParam => {
        this.resource = queryParam.resource;
        this.objectId = queryParam.objectId;
        // todo 获取vmfs数据
        this.remoteSrv.getVmfsById(this.objectId).subscribe((result: any) => {
          if (result.code === '200' && null != result.data) {
            this.vmfsInfo = result.data.filter(item => item.objectid === this.objectId)[0];

            // 表单数据初始化
            const volumeIds = [];
            volumeIds.push(this.vmfsInfo.volumeId);
            this.changeServiceLevelForm.volume_ids = volumeIds;
            this.changeServiceLevelForm.ds_name = this.vmfsInfo.name;
          }
        });
      });
    });
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
        this.serviceLevelList = result.data;
        console.log('this.serviceLevelList', this.serviceLevelList);
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    });
  }
  /**
   * 取消
   */
  cancel() {
    if (this.resource === 'list') { // 列表入口
      this.router.navigate(['vmfs/list']);
    } else { // dataStore入口
      this.globalsService.getClientSdk().modal.close();
    }
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
