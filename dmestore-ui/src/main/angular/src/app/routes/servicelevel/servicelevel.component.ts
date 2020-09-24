import {
  Component,
  OnInit,
  AfterViewInit,
  ChangeDetectorRef,
  OnDestroy,
  NgZone
} from '@angular/core';

import {ClrDatagridStateInterface} from '@clr/angular';
import {HttpClient} from '@angular/common/http';
import { CommonService } from '../common.service';
/* import * as echarts from 'Echarts'; */
import 'echarts-liquidfill';

@Component({
  selector: 'app-servicelevel',
  templateUrl: './servicelevel.component.html',
  styleUrls: ['./servicelevel.component.scss'],
  providers: [ CommonService ]
})
export class ServicelevelComponent implements OnInit, AfterViewInit, OnDestroy {


  // 详情页面弹出控制
  popShow = false;
  // 容量曲线图
  volumeCapacityChart = null;
  storagePoolCapacityChart = null;
  // 性能曲线图
  volumeMaxResponseTimeChart = null;
  volumeDensityChart = null;
  volumeThroughputChart = null;
  volumeBandwidthChart = null;
  storagePoolDensityChart = null;

  // 详情列表按钮控制
  storagePoolRadioCheck = 'basic';
  volumeRadioCheck = 'basic';

  // 服务等级列表搜索
  searchName = '';
  // 服务等级列表排序
  sortItem = {
    id: '',
    value: ''
  };
  sortItems = [
    {
      id: 'name',
      value: 'name'
    },
    {
      id: 'total_capacity',
      value: 'total_capacity'
    },
    {
      id: 'latency',
      value: 'latency'
    },
    {
      id: 'maxIOPS',
      value: 'maxIOPS'
    },
    {
      id: 'minIOPS',
      value: 'minIOPS'
    },
    {
      id: 'maxBandWidth',
      value: 'maxBandWidth'
    },
    {
      id: 'minBandWidth',
      value: 'minBandWidth'
    }
  ];

  volumeCapacityOption = {
    legend: {
      data: ['Allocated(GB)', 'Total(GB)'],
      left: 'right',
    },
    tooltip: {
      trigger: 'axis'
    },
    title: {
      left: 'left',
      text: 'Volume Capacity'
      // subtext:subTitle,
    },
    xAxis: {
      type: 'category',
      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    yAxis: {
      type: 'value'
    },
    series: [{
      name: 'Allocated(GB)',
      data: [820, 932, 901, 934, 1290, 1330, 1320],
      type: 'line',
      smooth: true
    }, {
      name: 'Total(GB)',
      data: [25, 932, 44, 934, 566, 1589, 558],
      type: 'line',
      smooth: true
    }]
  };

  // 选中的服务等级
  selectedModel: Servicelevel =
  {
        id: '1',
        name: '服务等级_CCC2',
        description: 'description',
        type: 'BLOCK', // FILE BLOCK
        protocol: 'iSCSI', // FC, iSCSI
        total_capacity: 1000,
        used_capacity: 600,
        free_capacity: 400,
        capabilities: {
            resource_type: 'thin', // default_type、thin、thick
            compression: 'default_type', // default_type, enabled, disabled,
            deduplication: 'enabled', // default_type, enabled, disabled,
            iopriority: {
                enabled: true,
                policy: 1, // IO优先级枚举值, 取值范围：1：低；2：中；3：高,
            },
            smarttier: {
                enabled: true,
                policy: 1, // 数据迁移等级，取值范围：0：不迁移, 1：自动迁移, 2：向低性能层迁移, 3：向高性能层迁移
            },
            qos: {
                enabled: true,
                qos_param: {
                  latency: 25,
                  latencyU: 'ms',
                  minBandWidth: 2000,
                  minIOPS: 5000,
                  maxBandWidth: 10000,
                  maxIOPS: 20000,
                }
            }
        }
  };

  // 服务等级列表
  serviceLevels = [];
  // 服务等级列表 服务器返回数据
  serviceLevelsRes = [];


  // ===============storage pool==============
  // 表格loading标志
  storeagePoolIsloading = false;
  // 数据总数
  storagePoolTotal = 0;
  // 数据列表
  storagePoolList: StoragePool[] = [];
  // 选中列表
  storagePoolSelected: StoragePool[];
  // 查询数据参数
  storagePoolQuery = { // 查询数据
      q: 'name',
      sort: 'hostSetting',
      order: 'desc',
      page: 0,
      per_page: 5
  };
  // 查询数据结果,测试用
  storagePoolReslut = {
    items: [{
        id: '123',
        name: 'storeage',
        status: 'Online',
        diskType: 'SSD',
        storageDevice: 'dertV8_160',

        totalCapacity: 1000,
        usedCapacity: 600,
        capacityUsage: 60,
        freeCapacity: 400,

        latency: 52,
        iops: 4000,
        bandwidth: 10000
      }],
    total_count: 1
  };
  // ===============storage pool end==============

  // ===============volume==============
  // 表格loading标志
  volumeIsloading = false;
  // 数据总数
  volumeTotal = 0;
  // 数据列表
  volumeList: Volume[] = [];
  // 选中列表
  volumeSelected: Volume[];
  // 查询数据参数
  volumeQuery = { // 查询数据
    q: 'name',
    sort: 'hostSetting',
    order: 'desc',
    page: 0,
    per_page: 5
  };
  // 查询数据结果,测试用
  volumeReslut = {
    items: [{
      id: '123',
      name: 'volume-001',
      status: 'Normal',
      totalCapacity: 1000,
      provisioningType: 'thin',
      capacityUsed: 500,

      latency: 52,
      iops: 4000,
      bandwidth: 10000
    }],
    total_count: 1
  };
  // ===============volume end==============

  // ===============applicationType==============
  // 表格loading标志
  applicationTypeIsloading = false;
  // 数据总数
  applicationTypeTotal = 0;
  // 数据列表
  applicationTypeList: ApplicationType[] = [];
  // 选中列表
  applicationTypeSelected: ApplicationType[];
  // 查询数据参数
  applicationTypeQuery = { // 查询数据
    q: 'name',
    sort: 'hostSetting',
    order: 'desc',
    page: 0,
    per_page: 5
  };
  // 查询数据结果,测试用
  applicationTypeReslut = {
    items: [{
      id: '123',
      storageDevice: 'applicationType-001',
      applicationType: 'SSP'
    }],
    total_count: 1
  };
  // ===============applicationType end==============

  constructor(private ngZone: NgZone,
              private cdr: ChangeDetectorRef,
              private http: HttpClient,
              private commonService: CommonService) { }

  ngOnInit(): void {
  }

  ngOnDestroy() {
    // if (this.volumeCapacityChart) {
    //   this.volumeCapacityChart.dispose();
    // }
  }

  ngAfterViewInit() {
    // this.ngZone.runOutsideAngular(() => this.initChart());
    this.refresh();
  }

  // ===============storage pool==============
  storagePoolRefresh(state: ClrDatagridStateInterface){
    this.storeagePoolIsloading = true;
    const params = this.commonService.refresh(state, this.storagePoolQuery);
    // this.http.get('https://api.github.com/search/repositories', this.practiceParams).subscribe((result: any) => {
    //       console.log(result)
    //       this.list = result.items;
    //       this.total = result.total_count;
    //       this.isLoading = false;
    //       this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    // });
    this.http.post('http://localhost:8080/datastorestatistichistrory/vmfsvolume', this.storagePoolQuery).subscribe((response: any) => {
      console.log(response);
    }, err => {
      console.error('ERROR', err);
    });
    this.storagePoolList = this.storagePoolReslut.items;
    this.storagePoolTotal = this.storagePoolReslut.total_count;
    this.storeagePoolIsloading = false;
    this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
  }
  // ===============storage pool end==============

  // ===============volume pool==============
  volumeRefresh(state: ClrDatagridStateInterface){
    this.volumeIsloading = true;
    const params = this.commonService.refresh(state, this.volumeQuery);
    // this.http.get('https://api.github.com/search/repositories', this.practiceParams).subscribe((result: any) => {
    //       console.log(result)
    //       this.list = result.items;
    //       this.total = result.total_count;
    //       this.isLoading = false;
    //       this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    // });
    this.volumeList = this.volumeReslut.items;
    this.volumeTotal = this.volumeReslut.total_count;
    this.volumeIsloading = false;
    this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
  }
  // ===============volume pool end==============

  // ===============applicationType pool==============
  applicationTypeRefresh(state: ClrDatagridStateInterface){
    this.applicationTypeIsloading = true;
    const params = this.commonService.refresh(state, this.applicationTypeQuery);
    // this.http.get('https://api.github.com/search/repositories', this.practiceParams).subscribe((result: any) => {
    //       console.log(result)
    //       this.list = result.items;
    //       this.total = result.total_count;
    //       this.isLoading = false;
    //       this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    // });
    this.applicationTypeList = this.applicationTypeReslut.items;
    this.applicationTypeTotal = this.applicationTypeReslut.total_count;
    this.applicationTypeIsloading = false;
    this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
  }
  // ===============applicationType pool end==============

  initPerformanceChart(){
    setTimeout(() => {
      const c1 = document.querySelector('#volumeMaxResponseTimeChart');
      const c2 = document.querySelector('#volumeDensityChart');
      const c3 = document.querySelector('#volumeThroughputChart');
      const c4 = document.querySelector('#volumeBandwidthChart');
      const c5 = document.querySelector('#storagePoolDensityChart');
      if (c1 == null
        || c2 == null
        || c3 == null
        || c4 == null
        || c5 == null
      ){
        this.initPerformanceChart();
      } else {
        /* this.volumeMaxResponseTimeChart = echarts.init(document.querySelector('#volumeMaxResponseTimeChart'));
        this.volumeMaxResponseTimeChart.setOption(this.volumeCapacityOption);

        this.volumeDensityChart = echarts.init(document.querySelector('#volumeDensityChart'));
        this.volumeDensityChart.setOption(this.volumeCapacityOption);

        this.volumeThroughputChart = echarts.init(document.querySelector('#volumeThroughputChart'));
        this.volumeThroughputChart.setOption(this.volumeCapacityOption);

        this.volumeBandwidthChart = echarts.init(document.querySelector('#volumeBandwidthChart'));
        this.volumeBandwidthChart.setOption(this.volumeCapacityOption);

        this.storagePoolDensityChart = echarts.init(document.querySelector('#storagePoolDensityChart'));
        this.storagePoolDensityChart.setOption(this.volumeCapacityOption); */
      }
    }, 100);
  }

  initCapacityChart(){
    setTimeout(() => {
      const c1 = document.querySelector('#volumeCapacityChart');
      const c2 = document.querySelector('#storagePoolCapacityChart');
      if (c1 == null || c2 == null){
        this.initCapacityChart();
      } else{
        /* this.volumeCapacityChart = echarts.init(document.querySelector('#volumeCapacityChart'));
        this.volumeCapacityChart.setOption(this.volumeCapacityOption);

        this.storagePoolCapacityChart = echarts.init(document.querySelector('#storagePoolCapacityChart'));
        this.storagePoolCapacityChart.setOption(this.volumeCapacityOption); */
      }
    }, 100);
  }

  // 刷新服务等级列表
  refresh(){
    this.http.post('http://localhost:8080/servicelevel/listservicelevel', {}).subscribe((response: any) => {
      response.data = response.data.replace('service-levels', 'serviceLevels');
      const r = JSON.parse(response.data);
      for (const i of r.serviceLevels){
        i.usedRate =  ((i.used_capacity / i.total_capacity * 100).toFixed(2));
      }
      this.serviceLevelsRes = r.serviceLevels;
      this.search();
      console.log(this.serviceLevels);
    }, err => {
      console.error('ERROR', err);
    });
  }

  // 服务等级列表搜索
  search(){
    if (this.searchName !== ''){
      this.serviceLevels = this.serviceLevelsRes.filter(item => item.name.indexOf(this.searchName) > -1);
    } else{
      this.serviceLevels = this.serviceLevelsRes;
    }
    this.sortItemsChange();
  }

  // 服务等级列表排序
  sortItemsChange(){
    const o = this.sortItem;
    if (o.value !== ''){
      this.serviceLevels = this.serviceLevels.sort(this.compare(this.sortItem, 'asc'));
    }
  }

  // 获取指定属性值
  recursivePropertyies(prop: string, obj: object){
    for (const property in obj){
      if (property === prop){
        return obj[prop];
      } else if (obj[property] instanceof Object){
        const r = this.recursivePropertyies(prop, obj[property]);
        if (r !== undefined){
          return r;
        }
      }
    }
  }

  // 比较函数
  compare(prop: any, sort: string){
    return (obj1: any, obj2: any) => {
      let val1 = this.recursivePropertyies(prop, obj1);
      let val2 = this.recursivePropertyies(prop, obj2);
      if (val1 === undefined) {
        val1 = '';
      }
      if (val2 === undefined) {
        val2 = '';
      }
      if (val1 < val2) {
        if (sort === 'asc'){
          return -1;
        } else {
          return 1;
        }
      } else if (val1 > val2) {
        if (sort === 'asc'){
          return 1;
        } else {
          return -1;
        }
      } else {
          return 0;
      }
    };
  }
}

class Servicelevel {
  id: string;
  name: string;
  description: string;
  type: string; // FILE BLOCK
  protocol: string; // FC, iSCSI
  total_capacity: number;
  used_capacity: number;
  free_capacity: number;
  capabilities: {
    resource_type: string; // default_type、thin、thick
    compression: string; // default_type, enabled, disabled;
    deduplication: string; // default_type, enabled, disabled;
    iopriority: {
      enabled: boolean;
      policy: number; // IO优先级枚举值, 取值范围：1：低；2：中；3：高;
    };
    smarttier: {
      enabled: boolean;
      policy: number; // 数据迁移等级，取值范围：0：不迁移; 1：自动迁移; 2：向低性能层迁移; 3：向高性能层迁移
    };
    qos: {
      enabled: boolean;
      qos_param: {
        latency: number;
        latencyU: string;
        minBandWidth: number;
        minIOPS: number;
        maxBandWidth: number;
        maxIOPS: number;
      };
    };
  };
}

interface StoragePool {
  id: string;
  name: string;
  status: string;
  diskType: string;
  storageDevice: string;

  totalCapacity: number;
  usedCapacity: number;
  capacityUsage: number;
  freeCapacity: number;

  latency: number;
  iops: number;
  bandwidth: number;
}

interface Volume {
  id: string;
  name: string;
  status: string;
  totalCapacity: number;
  provisioningType: string;
  capacityUsed: number;

  latency: number;
  iops: number;
  bandwidth: number;
}


class ApplicationType {
  id: string;
  storageDevice: string;
  applicationType;
}
