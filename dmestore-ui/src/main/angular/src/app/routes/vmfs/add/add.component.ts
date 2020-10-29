import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit, ViewChild} from "@angular/core";
import {AddService} from './add.service';
import {ActivatedRoute, Router} from '@angular/router';
import {
  ClusterList,
  GetForm,
  HostList,
  HostOrCluster,
  ServiceLevelList, StorageList,
  StoragePoolList,
  VmfsListService
} from '../list/list.service';
import {ClrWizard, ClrWizardPage} from "@clr/angular";
import {GlobalsService} from "../../../shared/globals.service";

@Component({
  selector: 'app-list',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [AddService],
})
export class AddComponent implements OnInit{

  constructor(private remoteSrv: AddService, private route: ActivatedRoute, private cdr: ChangeDetectorRef,
              private router:Router, private globalsService: GlobalsService) {

  }
  // 初始化表单
  form;
  // 块大小选择
  blockSizeOptions = [];
  // 空间回收粒度初始化
  srgOptions = [];
  // 主机AND集群
  deviceList: HostOrCluster[] = [];
  // 已选择的主机/集群
  chooseDevice;

  // 服务等级列表
  serviceLevelList: ServiceLevelList[] = [];
  // 未选择服务等级true未选择 false选择 添加、服务登记变更
  serviceLevelIsNull = false;
  // 是否选择服务等级：level 选择服务器等级 customer 未选择服务等级
  levelCheck = 'level';

  // 存储池ID
  storagePoolList: StoragePoolList[] = [];
  storageList: StorageList[] = []; // 存储数据

  // 操作来源 list:列表页面、dataStore：在DataStore菜单页面操作
  resource;

  modalLoading = false; // 弹窗加载
  isOperationErr = false; // 错误信息
  capacityErr = false; // 容量错误信息


  // 添加页面窗口
  @ViewChild('wizard') wizard: ClrWizard;
  @ViewChild('addPageOne') addPageOne: ClrWizardPage;
  @ViewChild('addPageTwo') addPageTwo: ClrWizardPage;

  ngOnInit(): void {
    this.initData();
    // 初始化添加数据
    console.log(' this.wizard', this.wizard);
  }

  initData() {
    // 初始化loading
    this.modalLoading = true;
    this.isOperationErr = false;
    // 容量错误提示
    this.capacityErr = false;
    // this.globalsService.loading = true;
    // 设备类型 操作类型初始化
    this.route.url.subscribe(url => {
      console.log('url', url);
      this.route.queryParams.subscribe(queryParam => {
        this.resource = queryParam.resource;
      });
    });
    // 初始化表单
    this.form = new GetForm().getAddForm();
    // 版本、块大小、粒度下拉框初始化
    this.setBlockSizeOptions();

    // 设置主机/集群
    this.setDeviceList();

    // 初始化服务等级数据
    this.setServiceLevelList();

    // 初始化存储池
    this.storagePoolList = [];

    // 添加页面默认打开首页
    this.jumpTo(this.addPageOne);

    // 容量设置
    this.capacityOnblur();
  }
  // 页面跳转
  jumpTo(page: ClrWizardPage) {
    if (page && page.completed) {
      this.wizard.navService.currentPage = page;
    } else {
      this.wizard.navService.setLastEnabledPageCurrent();
    }
    this.wizard.open();
  }

  // 初始化块大小（修改版本触发事件）
  setBlockSizeOptions() {
    const options = [];
    // const versionVal = this.versionBtn.nativeElement.value;
    const versionVal = this.form.version + '';
    console.log('versionVal' + versionVal);
    if (versionVal === '6') {
      const option1 = {key: 1024, value : '1MB'};
      options.push(option1);
      const option2 = {key: 64, value : '64KB'};
      options.push(option2);
    } else if (versionVal === '5') {
      const option1 = {key: 1024, value : '1MB'};
      options.push(option1);
    }
    // 设置blockSize 可选值
    this.blockSizeOptions = options;
    this.form.blockSize = this.blockSizeOptions[0].key;
    // 重置空间回收粒度
    this.setSrgOptions();


  }

  /**
   * 初始化空间回收粒度
   */
  setSrgOptions() {
    const options = [];
    const blockValue = this.form.blockSize + '';
    if (blockValue === '1024') {
      const option1 = {key: 1024, value : '1MB'};
      options.push(option1);
      const option2 = {key: 8, value : '8KB'};
      options.push(option2);
    } else if (blockValue === '64') {
      const option1 = {key: 64, value : '64KB'};
      options.push(option1);
      const option2 = {key: 8, value : '8KB'};
      options.push(option2);
    }
    this.srgOptions = options;
    this.form.spaceReclamationGranularity = this.srgOptions[0].key;;

    // 容量设置
    this.capacityOnblur();
  }

  // 设置设备数据
  setDeviceList() {
    // 初始化数据
    this.deviceList = [];
    // const nullDevice =  {
    //   deviceId: '',
    //   deviceName: '',
    //   deviceType: '',
    // };
    // this.deviceList.push(nullDevice);
    this.chooseDevice = undefined;

    console.log('this.chooseDevice', this.chooseDevice);
    // 初始添加页面的主机集群信息
    this.setHostDatas().then(res => {
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });

    this.setClusterDatas().then(res => {
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });
  }

  // 设置主机数据
  setHostDatas() {
    return new Promise((resolve, reject) => {
      this.remoteSrv.getHostList().subscribe((result: any) => {
        let hostList: HostList[] = []; // 主机列表
        console.log('host', result);
        if (result.code === '200' && result.data !== null) {
          hostList = result.data;
          hostList.forEach(item => {

            const hostInfo = {
              deviceId: item.hostId,
              deviceName: item.hostName,
              deviceType: 'host',
            };
            this.deviceList.push(hostInfo);
          });
        }
        this.form.hostDataloadSuccess = true;
        resolve(this.deviceList);
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      });
    });
  }
  // 设置集群数据
  setClusterDatas() {
    return new Promise((resolve, reject) => {
      this.remoteSrv.getClusterList().subscribe((result: any) => {
        let clusterList: ClusterList [] = []; // 集群列表
        console.log('cluster', result);
        console.log('cluster', result.data !== null);
        if (result.code === '200' && result.data !== null) {
          clusterList = result.data;
          clusterList.forEach(item => {

            const clusterInfo = {
              deviceId: item.clusterId,
              deviceName: item.clusterName,
              deviceType: 'cluster',
            };
            this.deviceList.push(clusterInfo);
          });
        }
        resolve(this.deviceList);
        this.form.culDataloadSuccess = true;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        console.log('cluster', result.data !== null);
      });
    });
  }

  // 获取服务等级数据
  setServiceLevelList() {
    // 初始化服务等级选择参数
    this.serviceLevelIsNull = false;
    // 获取服务等级数据
    this.remoteSrv.getServiceLevelList().subscribe((result: any) => {
      console.log(result);
      if (result.code === '200' && result.data !== null) {
        this.serviceLevelList = result.data.filter(item => item.totalCapacity !== 0);
        console.log('this.serviceLevelList', this.serviceLevelList);
      }
      this.modalLoading = false;
      // this.globalsService.loading = false;
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });
  }

  // 选择服务等级时
  serviceLevelBtnFunc() {
    this.levelCheck = 'level';
    this.serviceLevelIsNull = false;
    this.setServiceLevelList();
  }
  // 未选择服务等级 时调用方法
  customerClickFunc() {
    this.levelCheck = 'customer';
    this.serviceLevelIsNull = false;

    this.storageList = null;
    this.storagePoolList = null;

    // loading
    this.modalLoading = true;

    this.getStorageList();
  }
  // 获取所有存储数据
  getStorageList() {
    this.remoteSrv.getStorages().subscribe((result: any) => {
      console.log(result);
      if (result.code === '200' && result.data !== null) {
        this.storageList = result.data;
        this.getStoragePoolsByStorId();
      }
      this.modalLoading = false;
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });
  }
  // 获取存储池数据
  getStoragePoolsByStorId() {
    this.form.pool_raw_id = undefined;
    console.log('selectSotrageId' + this.form.storage_id);
    if (null !== this.form.storage_id && '' !== this.form.storage_id) {
      this.remoteSrv.getStoragePoolsByStorId(this.form.storage_id, 'block').subscribe((result: any) => {
        console.log('storagePools', result);
        console.log('result.code === \'200\' && result.data !== null', result.code === '200' && result.data !== null);
        if (result.code === '200' && result.data !== null) {
          this.storagePoolList = result.data;
          console.log('this.storagePoolList', this.storagePoolList);

          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        }
      });
    }
  }

  /**
   * 取消
   */
  cancel() {
    this.wizard.close();// 关闭弹窗
    if (this.resource === 'list') { // 列表入口
      this.router.navigate(['vmfs/list']);
    } else { // dataStore入口
      this.globalsService.getClientSdk().modal.close();
    }
  }

  // 添加vmfs 处理
  addVmfsHanlde() {
    const selectResult = this.serviceLevelList.find(item => item.show === true);
    console.log('selectResult', this.levelCheck === 'level' && selectResult);
    if ((this.levelCheck === 'level' && selectResult && selectResult.totalCapacity !== 0) || this.levelCheck !== 'level') { // 选择服务等级
      if (selectResult) {
        this.form.service_level_id = selectResult.id;
        this.form.service_level_name = selectResult.name;
      }
      // 数据预处----卷名称
      if (this.form.isSameName) { // 卷名称与vmfs名称相同（PS：不同时为必填）
        this.form.volumeName = this.form.name;
      }
      // 数据预处----容量 （后端默认单位为GB）
      switch (this.form.capacityUnit) {
        case 'TB':
          this.form.capacity = this.form.capacity * 1024;
          break;
        case 'MB':
          this.form.capacity = this.form.capacity / 1024;
          break;
        case 'KB':
          this.form.capacity = this.form.capacity / (1024 * 1024);
          break;
        default: // 默认GB 不变
          break;
      }
      // 主机/集群数据处理
      if (this.chooseDevice.deviceType === 'host') {
        this.form.host = this.chooseDevice.deviceName;
        this.form.hostId = this.chooseDevice.deviceId;
      } else {
        this.form.cluster = this.chooseDevice.deviceName;
        this.form.clusterId = this.chooseDevice.deviceId;
      }
      if (this.levelCheck === 'customer') { // 未选择 服务等级 需要将服务等级数据设置为空
        this.form.service_level_id = null;
        this.form.service_level_name = null;
      }
      // 若控制策略数据为空，则将控制策略变量置为空
      if (this.form.maxbandwidth === null && this.form.maxiops === null
        && this.form.minbandwidth === null && this.form.miniops === null && this.form.latency === null) {
        this.form.control_policy = null;
      }
      console.log('addFrom', this.form);
      // 打开 loading
      // this.globalsService.loading = true;
      this.modalLoading = true;
      this.remoteSrv.createVmfs(this.form).subscribe((result: any) => {
        this.modalLoading = false;
        if (result.code === '200') {
          console.log('创建成功');
        } else {
          console.log('创建失败：' + result.description);
          // 失败信息
          this.isOperationErr = true;
        }
        // 关闭窗口
        this.cancel();
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      });
    } else {
      this.serviceLevelIsNull = true;
    }
  }
  // 容量单位转换
  capacityChange(obj: any) {
    console.log('event', obj.value === '1');
    const objValue = obj.value.match(/\d+(\.\d{0,2})?/) ? obj.value.match(/\d+(\.\d{0,2})?/)[0] : '';

    if (objValue !== '') {

      let capatityG;
      // 数据预处----容量 （后端默认单位为GB）
      switch (this.form.capacityUnit) {
        case 'TB':
          capatityG = objValue * 1024;
          break;
        case 'MB':
          capatityG = objValue / 1024;
          break;
        case 'KB':
          capatityG = objValue / (1024 * 1024);
          break;
        default: // 默认GB 不变
          capatityG = objValue;
          break;
      }

      // 版本号5 最小容量为1G 版本号6最小2G
      if (capatityG < 1 && this.form.version === '5') {
        capatityG = 1;
      } else if (capatityG < 2 && this.form.version === '6') {
        capatityG = 2;
      }
      switch (this.form.capacityUnit) {
        case 'TB':
          capatityG = capatityG / 1024;
          break;
        case 'MB':
          capatityG = capatityG * 1024;
          break;
        case 'KB':
          capatityG = capatityG * (1024 * 1024);
          break;
        default: // 默认GB 不变
          capatityG = capatityG;
          break;
      }

      obj.value = capatityG;
    } else {
      obj.value = objValue;
    }
  }
  /**
   * 容量格式化
   * @param c 容量值
   * @param isGB true GB、false MB
   */
  formatCapacity(c: number, isGB:boolean){
    let cNum;
    if (c < 1024){
      cNum = isGB ? c.toFixed(3)+'GB':c.toFixed(3)+'MB';
    }else if(c >= 1024 && c< 1048576){
      cNum = isGB ? (c/1024).toFixed(3) + 'TB' : (c/1024).toFixed(3) + 'GB';
    }else if(c>= 1048576){
      cNum = isGB ? (c/1024/1024).toFixed(3) + 'PB':(c/1024/1024).toFixed(3) + 'TB';
    }
    return cNum;
  }
  /**
   * 容量
   * @param obj
   */
  capacityOnblur() {
    // 容量
    let capacity = this.form.capacity;
    // 标准容量 单位G
    let capacityG;
    console.log('capacity', capacity)
    if (capacity && capacity !== null && capacity !== '') {

      if (capacity > 0) {
        switch (this.form.capacityUnit) {
          case "TB":
            capacityG = capacity * 1024 + '';
            console.log('capacityG2', capacityG);
            if (capacityG.indexOf(".")!==-1) { // 小数
              this.capacityErr = true;
              capacity = '';
            } else{ // 整数
              if (this.form.version === '5') {
                if (capacity < 1/1024) {
                  capacity = '';
                  this.capacityErr = true;
                } else {
                  this.capacityErr = false;
                }
              } else {
                if (capacity < 2/1024) {
                  capacity = '';
                  this.capacityErr = true;
                }else {
                  this.capacityErr = false;
                }
              }
            }
            break;
          case "MB":
            capacityG = capacity / 1024 + '';
            if (capacityG.indexOf(".")!==-1) { // 小数
              this.capacityErr = true;
              capacity = '';
            } else { // 整数
              if (this.form.version === '5') {
                if (capacity < 1*1024) {
                  capacity = '';
                  this.capacityErr = true;
                }else {
                  this.capacityErr = false;
                }
              } else {
                if (capacity < 2*1024) {
                  capacity = '';
                  this.capacityErr = true;
                }else {
                  this.capacityErr = false;
                }
              }
            }
            break;
          default:
            capacityG = capacity + '';
            if (capacityG.indexOf(".")!==-1) { // 小数
              capacity = '';
              this.capacityErr = true;
            } else {// 整数
              if (this.form.version === '5') {
                if (capacity < 1) {
                  capacity = '';
                  this.capacityErr = true;
                } else {
                  this.capacityErr = false;
                }
              } else {
                if (capacity < 2) {
                  capacity = '';
                  this.capacityErr = true;
                } else {
                  this.capacityErr = false;
                }
              }

            }
            break;
        }
      } else {
        capacity = '';
        this.capacityErr = true;
      }
      this.form.capacity = capacity;
    }
    console.log('this.form.capacityUnit', this.form.capacityUnit);
    console.log('this.form.capacity', this.form.capacity);
    console.log('this.form.count', this.form.count);
  }

  /**
   * 数量变化
   */
  countBlur() {
    let count = this.form.count;
    if (count && count !== null && count !== '') {
      if ((count+'').indexOf(".")!==-1) { // 小数
        count = '';
        this.capacityErr = true;
      } else {
        this.capacityErr = false;
      }
    }
    this.form.count =  count;
  }

  /**
   * add 下一页
   */
  addNextPage() {
    if (this.form.capacity !== '' && this.form.count !== '') {
      this.wizard.next();
    }
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
        case 'maxbandwidth':
          objVal = this.form.maxbandwidth;
          break;
        case 'maxiops':
          objVal = this.form.maxiops;
          break;
        case 'minbandwidth':
          objVal = this.form.minbandwidth;
          break;
        case 'miniops':
          objVal = this.form.miniops;
          break;
        default:
          objVal = this.form.latency;
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
      case 'maxbandwidth':
        this.form.maxbandwidth = objVal;
        break;
      case 'maxiops':
        this.form.maxiops = objVal;
        break;
      case 'minbandwidth':
        this.form.minbandwidth = objVal;
        break;
      case 'miniops':
        this.form.miniops = objVal;
        break;
      default:
        this.form.latency = objVal;
        break;
    }
  }
}
