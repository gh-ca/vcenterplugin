import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {DataStore, MountService} from "./mount.service";
import {ActivatedRoute} from "@angular/router";
import {GetForm, VmfsListService} from "../list/list.service";

@Component({
  selector: 'app-list',
  templateUrl: './mount.component.html',
  styleUrls: ['./mount.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [MountService],
})
export class MountComponent implements OnInit{

  constructor(private remoteSrv: MountService, private route: ActivatedRoute, private cdr: ChangeDetectorRef) {

  }

  ngOnInit(): void {
    this.initData();
    // 初始化dataStore数据
    this.getDataStore();
  }
  // dataStore数据
  dataStores:DataStore[] = [];
  // 选择DataStore
  chooseMountDataStore: DataStore[] = [];
  // mountForm:
  // 设备类型 host 主机、cluster 集群
  dataType;
  // 操作类型 mount挂载、unmount卸载、expand扩容
  operationType;
  // 数据加载
  isLoading = true;
  // 未选择挂载的DataStore
  notChooseMountDevice = false;
  // 挂载的form
  mountForm;
  // 挂载模块展示/隐藏
  mountShow = false;
  // 卸载模块展示/隐藏
  unMountShow = false;
  // 扩容模块展示/隐藏
  expandShow = false;
  // 服务器/集群ID
  hostOrClusterId = 'urn:vmomi:HostSystem:host-1034:674908e5-ab21-4079-9cb1-596358ee5dd1';
  // 服务器/集群名称
  hostOrClusterName = '10.143.133.17';

  initData() {
    // 设备类型 操作类型初始化
    this.route.url.subscribe(url => {
      console.log('url', url);
      this.dataType = url[0] + '';
      this.operationType = url[1] + '';
    });
    // 窗口显示 form数据初始化
    switch (this.operationType) {
      case 'mount':
        this.mountShow = true;
        this.mountForm = new GetForm().getMountForm();
        break;
      case 'unmount':
        this.unMountShow = true;
        break;
      case 'expand':
        this.expandShow = true;
        break;
      default:
        break;
    }
  }
  /**
   * 初始化dataStore
   */
  getDataStore() {
    // 初始化挂载form
    // 初始化dataStore
    this.dataStores = [];
    console.log('this.dataType', this.dataType === 'host');
    this.dataType = this.dataType+'';
    switch (this.dataType) {
      case 'host':
        // 设置主机相关参数
        this.mountForm.hostId = this.hostOrClusterId;
        this.mountForm.hostName = this.hostOrClusterName;

        // 获取dataStore
        this.remoteSrv.getDataStoreByHostId(this.hostOrClusterId, 'VMFS').subscribe((result: any) => {
          console.log("mountHostData:", result);
          if (result.code === '200' && null != result.data) {
            this.dataStores = result.data;
          }
          this.isLoading = false;
        });
        break;
      case 'cluster':
        // 设置集群相关参数
        this.mountForm.hostId = this.hostOrClusterId;
        this.mountForm.hostName = this.hostOrClusterName;

        // 获取dataStore
        this.remoteSrv.getDataStoreByClusterId(this.hostOrClusterId, 'VMFS').subscribe((result: any) => {
          console.log("mountHostData:", result);
          if (result.code === '200' && null != result.data) {
            this.dataStores = result.data;
          }
        });
        break;
      default:
        break;
    }
  }

  /**
   * 挂载处理
   */
  mountHandleFunc() {
    if (this.chooseMountDataStore.length < 1) {
      this.notChooseMountDevice = true;
    } else {
      this.notChooseMountDevice = false;
      const dataStoreObjectIds = [];
      this.chooseMountDataStore.forEach(item => {
        dataStoreObjectIds.push(item.objectId);
      });
      this.mountForm.dataStoreObjectIds = dataStoreObjectIds;

      this.remoteSrv.mountVmfs(this.mountForm).subscribe((result: any) => {
        if (result.code  ===  '200'){
          console.log('挂载成功');
        } else {
          console.log('挂载异常：' + result.description);
        }
        // 隐藏挂载页面
        this.mountShow = false;
        this.cdr.detectChanges();
      });
    }
  }

  /**
   * 卸载处理
   */
  unMountHandleFunc() {

  }
  
}
