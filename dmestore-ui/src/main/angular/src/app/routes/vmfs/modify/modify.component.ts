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
  objectId;
  // vmfs数据
  vmfsInfo: VmfsInfo;

  // 操作来源 list:列表页面、dataStore：在DataStore菜单页面操作
  resource;

  // 编辑窗口隐藏于展示
  modifyShow = false;
  modalHandleLoading = false; // 数据处理loading
  modalLoading = false; // 数据加载loading
  isOperationErr = false; // 错误信息
  modifySuccessShow = false; // 编辑程功窗口

  ngOnInit(): void {
    this.initData();
  }

  initData() {
    this.modifyShow = true;
    this.modalLoading = true;
    this.modalHandleLoading = false;
    this.isOperationErr = false;
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
              this.modifyForm.service_level_name = this.vmfsInfo.serviceLevelName;
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
            this.modalLoading = false;
            console.log('this.modifyForm:', this.modifyForm);
            this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
          });
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
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
    this.modalHandleLoading = true;
    this.remoteSrv.updateVmfs(this.modifyForm.volumeId, this.modifyForm).subscribe((result: any) => {
      this.modalHandleLoading = false;
      if (result.code === '200') {
        console.log('modify success:' + this.modifyForm.oldDsName);
        // 关闭编辑窗口
        this.cancel();
        this.modifySuccessShow = true;
      } else {
        console.log('modify faild：' + this.modifyForm.oldDsName + result.description);
        this.isOperationErr = true;
      }
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });
  }
  /**
   * 带宽 blur
   * @param type
   * @param operationType add modify
   * @param valType
   */
  qosBlur(type:String, operationType:string) {

    let objVal;
    switch (operationType) {
      case 'max_bandwidth':
        objVal = this.modifyForm.max_bandwidth;
        break;
      case 'max_iops':
        objVal = this.modifyForm.max_iops;
        break;
      case 'min_bandwidth':
        objVal = this.modifyForm.min_bandwidth;
        break;
      case 'min_iops':
        objVal = this.modifyForm.min_iops;
        break;
      default:
        objVal = this.modifyForm.latency;
        break;
    }
    if (objVal && objVal !== '') {
      if (objVal.toString().match(/\d+(\.\d{0,2})?/)) {
        objVal = objVal.toString().match(/\d+(\.\d{0,2})?/)[0];
      } else {
        objVal = '';
      }
    }
    switch (operationType) {
      case 'max_bandwidth':
        this.modifyForm.max_bandwidth = objVal;
        break;
      case 'max_iops':
        this.modifyForm.max_iops = objVal;
        break;
      case 'min_bandwidth':
        this.modifyForm.min_bandwidth = objVal;
        break;
      case 'min_iops':
        this.modifyForm.min_iops = objVal;
        break;
      default:
        this.modifyForm.latency = objVal;
        break;
    }
  }
}
