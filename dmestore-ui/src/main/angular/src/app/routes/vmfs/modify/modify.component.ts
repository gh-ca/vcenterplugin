import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ModifyService} from './modify.service';
import {ActivatedRoute, Router} from '@angular/router';
import {GetForm, ServiceLevelList, VmfsInfo, VmfsListService} from '../list/list.service';
import {GlobalsService} from "../../../shared/globals.service";

@Component({
  selector: 'app-list',
  templateUrl: './modify.component.html',
  styleUrls: ['./modify.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [ModifyService],
})
export class ModifyComponent implements OnInit{

  constructor(private remoteSrv: ModifyService, private route: ActivatedRoute, private cdr: ChangeDetectorRef,
              private router:Router, private globalsService: GlobalsService) {

  }

  // 编辑form提交数据
  modifyForm = new GetForm().getEditForm();
  // 是否为服务等级 true:是 false:否 若为是则不显示控制策略以及交通管制对象
  isServiceLevelData = true;

  // 存储ID
  objectId = 'urn:vmomi:HostSystem:host-1034:674908e5-ab21-4079-9cb1-596358ee5dd1';
  // vmfs数据
  vmfsInfo: VmfsInfo;

  // 操作来源 list:列表页面、dataStore：在DataStore菜单页面操作
  resource;

  // 编辑窗口隐藏于展示
  modifyShow:boolean;

  ngOnInit(): void {
    this.initData();
  }

  initData() {
    this.modifyShow = true;
    // 设备类型 操作类型初始化
    this.route.url.subscribe(url => {
      console.log('url', url);
      this.route.queryParams.subscribe(queryParam => {
        this.resource = queryParam.resource;
        this.objectId = queryParam.objectId;
        // 获取vmfs数据
        this.remoteSrv.getVmfsById(this.objectId)
          .subscribe((result: any) => {
            console.log('result:', result);

            if (result.code === '200' && null != result.data) {
              this.vmfsInfo = result.data.filter(item => item.objectid === this.objectId)[0];
              // 初始化form表单
              this.modifyForm = new GetForm().getEditForm();
              this.modifyForm.name = this.vmfsInfo.name;
              this.modifyForm.volumeId = this.vmfsInfo.volumeId;
              this.modifyForm.oldDsName = this.vmfsInfo.name;
              this.modifyForm.dataStoreObjectId = this.vmfsInfo.objectid;
              if (this.vmfsInfo.serviceLevelName === '') { // 非服务等级
                this.isServiceLevelData = false;
                const wwns = [];
                wwns.push(this.vmfsInfo.wwn)
                this.remoteSrv.getChartData(wwns).subscribe((chartResult: any) => {
                  console.log('chartResult', chartResult);
                  console.log('chartResult', chartResult.code === '200' && chartResult.data != null);
                  if (chartResult.code === '200' && chartResult.data != null) {
                    const chartList: VmfsInfo  = chartResult.data[0];
                    this.modifyForm.max_bandwidth = chartList.maxBandwidth;
                    this.modifyForm.max_iops = chartList.maxIops;
                    this.modifyForm.min_iops = chartList.minIops;
                    this.modifyForm.min_bandwidth = chartList.minBandwidth;
                    this.modifyForm.latency = chartList.latency;
                  }
                });
              } else {
                this.isServiceLevelData = true;
              }
            }
            console.log('this.modifyForm.:', this.modifyForm);
            this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
          });
      });
    });

  }


  /**
   * 取消
   */
  cancel() {
    console.log('this.resource', this.resource);
    this.modifyShow = false;
    if (this.resource === 'list') { // 列表入口
      this.router.navigate(['vmfs/list']);
    } else { // dataStore入口
      this.globalsService.getClientSdk().modal.close();
    }
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
      this.modifyForm.newVoName = this.vmfsInfo.volumeName;
    }
    if (this.isServiceLevelData) {
      if (this.modifyForm.max_bandwidth === null && this.modifyForm.max_iops === null
        && this.modifyForm.min_bandwidth === null && this.modifyForm.min_iops === null && this.modifyForm.latency === null) {
        this.modifyForm.control_policy = null;
      }
    }
    this.modifyForm.newDsName = this.modifyForm.name;
    console.log('this.modifyForm:', this.modifyForm);
    this.remoteSrv.updateVmfs(this.modifyForm.volumeId, this.modifyForm).subscribe((result: any) => {
      if (result.code === '200') {
        console.log('modify success:' + this.modifyForm.oldDsName);
      } else {
        console.log('modify faild：' + this.modifyForm.oldDsName + result.description);
      }
      // 关闭编辑窗口
      this.cancel();
    });
  }
}
