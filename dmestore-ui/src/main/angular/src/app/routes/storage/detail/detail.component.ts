import {AfterViewInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, NgZone, OnInit} from '@angular/core';
import {
  CapacityDistribution, DetailService, Dtrees, NfsShare, PoolList, StorageController, StorageDetail, StorageDisk,
  StoragePool,
  Volume
} from './detail.service';
import {VmfsPerformanceService} from '../../vmfs/volume-performance/performance.service';
import { EChartOption } from 'echarts';
import {FileSystem} from '../../nfs/nfs.service';
import {ActivatedRoute, Router} from "@angular/router";
import {CapacityChart, CapacitySerie} from "../storage.service";
import {BondPort, EthernetPort, FailoverGroup, FCoEPort, FCPort, LogicPort} from "./port.service";
@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [DetailService, VmfsPerformanceService],
})
export class DetailComponent implements OnInit, AfterViewInit {

  cd : CapacityDistribution;



  options = {
    tooltip: {
      trigger: 'item',
      formatter: ' {b}: {c} ({d}%)'
    },
    title: {
      text: '123',
      textAlign: 'center',
      padding: 0,
      textVerticalAlign: 'middle',
      textStyle: {
        fontSize: 22,
        color: '#63B3F7'
      },
      subtextStyle: {
        fontSize: 12,
        color: '#c2c6dc',
        align: 'center'
      },
      left: '50%',
      top: '50%',
      // subtext: '234'
    },

    series: [
      {
        name: '',
        type: 'pie',
        radius: ['50%', '70%'],
        center: ['50%', '50%'],

        avoidLabelOverlap: false,
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: false,
            fontSize: '30',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          {value: 335, name: '直接访问'},
          {value: 310, name: '邮件营销'},
          {value: 234, name: '联盟广告'},
          {value: 135, name: '视频广告'},
          {value: 1548, name: '搜索引擎'}
        ]
      }
    ],
    color: ['#FF0000', '#FF9538', '#63B3F7']
  };
  mychart = {
    height: 300,
    title: {
      text: 'IOPS',
      subtext: 'IO/s',
      textStyle: {
        fontStyle: 'normal' // y轴线消失
      },
      textAlign: 'bottom',
      // left: '120px'
    },
    xAxis: {
      type: 'category',
      data: [
         '4:00', '6:00', '8:00', '10:00', '12:00', '14:00', '16:00', '18:00', '20:00', '22:00', '01/20'
      ]
    },
    yAxis: {
      type: 'value',
      max: 8,    // 设置最大值
      min: 0,
      splitNumber: 2,
      boundaryGap: ['50%', '50%'],
      axisLine: {
        show: false // y轴线消失
      },
    },
    tooltip: {},
    legend: {
      data: [
        {
          name: 'Upper Limit',  // 强制设置图形为圆。
          // icon: 'dotted',
        },
        {
          name: 'Lower Limit',  // 强制设置图形为圆。
          // icon: 'dottedLine',
        },
        {
          name: 'Read',  // 强制设置图形为圆。
          icon: 'triangle',
        },
        {
          name: 'Write',  // 强制设置图形为圆。
          icon: 'triangle',
        }
      ],
      y: 'top',    // 延Y轴居中
      x: 'right' // 居右显示
    },
    // dataZoom: [
    //   {   // 这个dataZoom组件，默认控制x轴。显示滑动框
    //     type: 'slider', // 这个 dataZoom 组件是 slider 型 dataZoom 组件
    //     xAxisIndex: 0, // x轴
    //     start: 10,      // 左边在 10% 的位置。
    //     end: 60         // 右边在 60% 的位置。
    //   },
    //   {   // 这个dataZoom组件，也控制x轴。 页面拖拽
    //     type: 'inside', // 这个 dataZoom 组件是 inside 型 dataZoom 组件
    //     xAxisIndex: 0, // x轴
    //     start: 10,      // 左边在 10% 的位置。
    //     end: 60         // 右边在 60% 的位置。
    //   },
    //   {
    //     type: 'slider',
    //     yAxisIndex: 0,
    //     start: 10,
    //     end: 80
    //   },
    //   {
    //     type: 'inside',
    //     yAxisIndex: 0,
    //     start: 30,
    //     end: 80
    //   }
    // ],
    series: [
      {
        name: 'Upper Limit',
        data: [
          {value: 4, symbol: 'none'},
          {value: 4, symbol: 'none'},
          {value: 4, symbol: 'none'},
          {value: 4, symbol: 'none'},
          {value: 4, symbol: 'none'},
          {value: 4, symbol: 'none'},
          {value: 4, symbol: 'none'},
          {value: 4, symbol: 'none'},
          {value: 4, symbol: 'none'},
          {value: 4, symbol: 'none'},
          {value: 4, symbol: 'none'}],
        type: 'line',
        smooth: true,
        // 普通样式。
        itemStyle: {
          normal: {
            lineStyle: {
              width: 2,
              type: 'dotted',  // 'dotted'虚线 'solid'实线
              color: '#DB2000' // 线条颜色
            }
          }
        },
        label: {
          show: true,
          // 标签的文字。
          formatter: 'This is a normal label.'
        },
        //
        //   // 高亮样式。
        //   emphasis: {
        //     itemStyle: {
        //       // 高亮时点的颜色。
        //       color: 'blue'
        //     },
        //     label: {
        //       show: true,
        //       // 高亮时标签的文字。
        //       formatter: 'This is a emphasis label.'
        //     }
        //   }
      },
      {
        name: 'Lower Limit',
        data: [
          {value: 3, symbol: 'none'},
          {value: 3, symbol: 'none'},
          {value: 3, symbol: 'none'},
          {value: 3, symbol: 'none'},
          {value: 3, symbol: 'none'},
          {value: 3, symbol: 'none'},
          {value: 3, symbol: 'none'},
          {value: 3, symbol: 'none'}],
        type: 'line',
        smooth: true,
        itemStyle: {
          normal: {
            lineStyle: {
              width: 2,
              type: 'dotted',  // 'dotted'虚线 'solid'实线
              color: '#F8E082'
            }
          }
        },
      },
      {
        name: 'Read',
        data: [
          {value: 7, symbol: 'none'},
          {value: 7, symbol: 'none'},
          {value: 7, symbol: 'none'},
          {value: 7, symbol: 'none'},
          {value: 7, symbol: 'none'},
          {value: 7, symbol: 'none'},
          {value: 7, symbol: 'none'},
          {value: 7, symbol: 'none'},
          {value: 7, symbol: 'none'},
          {value: 7, symbol: 'none'},
          {value: 7, symbol: 'none'},
          {value: 7, symbol: 'none'}],
        type: 'line',
        smooth: true,
        itemStyle: {
          normal: {
            lineStyle: {
              width: 2,
              type: 'solid',  // 'dotted'虚线 'solid'实线
              color: '#6870c4'
            }
          }
        },
      },
      {
        name: 'Write',
        data: [
          {value: 5, symbol: 'none'},
          {value: 5, symbol: 'none'},
          {value: 5, symbol: 'none'},
          {value: 5, symbol: 'none'},
          {value: 5, symbol: 'none'},
          {value: 5, symbol: 'none'},
          {value: 5, symbol: 'none'},
          {value: 5, symbol: 'none'},
          {value: 5, symbol: 'none'}],
        type: 'line',
        smooth: true,
        itemStyle: {
          normal: {
            lineStyle: {
              width: 2,
              type: 'solid',  // 'dotted'虚线 'solid'实线
              color: '#01bfa8'
            }
          }
        },
      }
    ]
  };
  // 创建表格对象
  // IOPS+QoS上下限
  iopsChart: EChartOption = {};
  // 带宽+QoS上下限
  bandwidthChart: EChartOption = {};
  // 响应时间+QoS下限
  latencyChart: EChartOption = {};
  // obj_type_id  (卷类型ID)
  objTypeId;
  // indicator_ids 获取参数指标（上下限等） 0 上限 1下限
  indicatorIdsIOPS: Array<string> = ['1407379178651656', '1407379178586113'];
  indicatorIdsBDWT: Array<string> = ['1407379178651656', '1407379178586113'];
  indicatorIdsREST: Array<string> = ['1407379178651656', '1407379178586113'];
  // obj_ids 卷ID
  objIds: Array<string> = ['47FEBD5002AB344D90EC6CFCD6127BA3'];
  // interval 时间间隔单位 ONE_MINUTE MINUTE HALF_HOUR HOUR DAY WEEK MONTH
  interval;
  // range 时间段 LAST_5_MINUTE LAST_1_HOUR LAST_1_DAY LAST_1_WEEK LAST_1_MONTH LAST_1_QUARTER HALF_1_YEAR LAST_1_YEAR BEGIN_END_TIME INVALID
  range;
  // begin_time 开始时间 时间戳(例：1552477343834)
  beginTime = 1552477343834;
  // end_time 结束时间 时间戳
  endTime = 1552567343000;
  // 定时函数执行时间 默认一天
  timeInterval = 1 * 60 * 60 * 1000;
  // 存储池列表
  poolList: PoolList[] = [];
  poolRadio = 'table1'; // 存储池列表切换
  volumeRadio = 'table1'; // volume列表切换
  storageId = '1234';
  storageName= "";
  constructor(private detailService: DetailService, private cdr: ChangeDetectorRef, private ngZone: NgZone,
              private activatedRoute: ActivatedRoute,private router:Router) { }
  detail: StorageDetail;
  storagePool: StoragePool[];
  volumes: Volume[];
  volumeSelect = [];
  fsList: FileSystem[];
  dtrees: Dtrees[];
  shares: NfsShare[];
  controllers:StorageController[];
  disks: StorageDisk[];

  fcs:FCPort[];
  eths: EthernetPort[];
  fcoes: FCoEPort[];
  bonds: BondPort[];
  logicports:LogicPort[];
  fgs:FailoverGroup[];
  //portList:
  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(queryParam => {
      this.storageId = queryParam.id;
      this.storageName = queryParam.name;
    });

    this.getStorageDetail(true);
    this.getStoragePoolList(true);
    this.initCapacity();
  }
  ngAfterViewInit() {
    this.ngZone.runOutsideAngular(() => this.initChart());
  }

  // 初始化表格对象
  async initChart() {
    switch (this.range) {
      case 'LAST_1_HOUR': // 过去一小时
        break;
      case 'LAST_4_HOUR': // 过去四小时 此值目前接口没有
        break;
      case 'LAST_12_HOUR': // 过去12小时 此值目前接口没有
        break;
      default: // 默认过去24h
        break;
    }
    /* // IOPS
    this.iopsChart = echarts.init(document.querySelector('#iopsChart'));
    this.perService.getIopsChart('IOPS', 'IO/s', this.objTypeId, this.indicatorIdsIOPS, this.objIds,
      this.interval, this.range, this.beginTime, this.endTime).then(res => {
      this.iopsChart.setOption(res, true);
      this.cdr.detectChanges();
    });

    // 带宽
    this.bandwidthChart = echarts.init(document.querySelector('#bandwidthChart'));
    this.perService.getIopsChart('Bandwidth', 'MS/s', this.objTypeId, this.indicatorIdsBDWT, this.objIds,
      this.interval, this.range, this.beginTime, this.endTime).then(res => {
      this.bandwidthChart.setOption(res, true);
      this.cdr.detectChanges();
    });
    // 响应时间
    this.latencyChart = echarts.init(document.querySelector('#latencyChart'));
    this.perService.getIopsChart('Latency', 'ms', this.objTypeId, this.indicatorIdsREST, this.objIds,
      this.interval, this.range, this.beginTime, this.endTime).then(res => {
      this.latencyChart.setOption(res, true);
      this.cdr.detectChanges();
    }); */
  }
  async showChart() {
    console.log("更新图表")
  }

  changeTab(page: string){
    console.log('点击：'+page);
    if (page === 'conf'){
      this.getStorageDetail(false);
    }
    if (page === 'pool'){
      this.getStoragePoolList(false);
    }
    if (page === 'volume'){
      this.getStorageVolumeList(false);
    }
    if (page === 'fs'){
      this.getFileSystemList(false);
    }
    if (page === 'dtrees'){
      this.getDtreeList(false);
    }
    if (page === 'shares'){
      this.getShareList(false);
    }
    if (page === 'capacity'){
      this.initCapacity();
    }
    if (page === 'hardware'){
      this.getControllerList(false);
    }
    if (page === 'controller'){
      this.getControllerList(false);
    }
    if (page === 'disk'){
      this.getDisksList(false);
    }
    if (page === 'port'){
      this.getPortsList();
    }

  }
  getStorageDetail(fresh: boolean){
    if (fresh){
      this.detailService.getStorageDetail(this.storageId).subscribe((r: any) => {
        if (r.code === '200'){
          this.detail = r.data.data;
          this.cdr.detectChanges();
        }else{

        }
      });
    }else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.detail === null){
        this.detailService.getStorageDetail(this.storageId).subscribe((r: any) => {
          if (r.code === '200'){
            this.detail = r.data.data;
            this.cdr.detectChanges();
          }
        });
      }
    }
  }
  getStoragePoolList(fresh: boolean){
    if (fresh){
      this.detailService.getStoragePoolList(this.storageId).subscribe((r: any) =>{
        console.log('pool result:');
        console.log(r);
        if (r.code === '200'){
          this.storagePool = r.data.data;
          this.cdr.detectChanges();
        }
      });
    }else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.poolList === null){
        this.detailService.getStoragePoolList(this.storageId).subscribe((r: any) =>{
          if (r.code === '200'){
            this.storagePool = r.data.data;
            this.cdr.detectChanges();
          }
        });
      }
    }

  }
  getStorageVolumeList(fresh: boolean){
    console.log("is null?")
    console.log(this.volumes)
    console.log(this.volumes==null)
    if (fresh){
      this.detailService.getVolumeListList(this.storageId).subscribe((r: any) => {
        if (r.code === '200'){
          this.volumes = r.data.data;
          this.cdr.detectChanges();
        }
      });
    }else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.volumes == null){
        this.detailService.getVolumeListList(this.storageId).subscribe((r: any) => {
          if (r.code === '200'){
            this.volumes = r.data.data;
            this.cdr.detectChanges();
          }
        });
      }
    }
  }
  getFileSystemList(fresh: boolean){
    if (fresh){
      this.detailService.getFileSystemList(this.storageId).subscribe((r: any) => {
        if (r.code === '200'){
          this.fsList = r.data.data;
          this.cdr.detectChanges();
        }
      });
    }else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.fsList == null){
        this.detailService.getFileSystemList(this.storageId).subscribe((r: any) => {
          if (r.code === '200'){
            this.fsList = r.data.data;
            this.cdr.detectChanges();
          }
        });
      }
    }
  }
  getDtreeList(fresh: boolean){
    if (fresh){
      this.detailService.getDtreeList(this.storageId).subscribe((r: any) => {
        if (r.code === '200'){
          this.dtrees = r.data.data;
          this.cdr.detectChanges();
        }
      });
    }else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.dtrees == null){
        this.detailService.getDtreeList(this.storageId).subscribe((r: any) => {
          if (r.code === '200'){
            this.dtrees = r.data.data;
            this.cdr.detectChanges();
          }
        });
      }
    }
  }
  getShareList(fresh: boolean){
    if (fresh){
      this.detailService.getShareList(this.storageId).subscribe((r: any) => {
        if (r.code === '200'){
          this.shares = r.data.data;
          this.cdr.detectChanges();
        }
      });
    }else {
      // 此处防止重复切换tab每次都去后台请求数据

      if (this.shares == null){
        this.detailService.getShareList(this.storageId).subscribe((r: any) => {
          if (r.code === '200'){
            this.shares = r.data.data;
            this.cdr.detectChanges();
          }
        });
      }
    }
  }
  getControllerList(fresh: boolean){
    if (fresh){
      this.detailService.getControllerList(this.storageId).subscribe((r: any) => {
        if (r.code === '200'){
          this.controllers = r.data.data;
          this.cdr.detectChanges();
        }
      });
    }else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.controllers == null){
        this.detailService.getControllerList(this.storageId).subscribe((r: any) => {
          if (r.code === '200'){
            this.controllers = r.data.data;
            this.cdr.detectChanges();
          }
        });
      }
    }
  }
  getDisksList(fresh: boolean){
    if (fresh){
      this.detailService.getDiskList(this.storageId).subscribe((r: any) => {
        if (r.code === '200'){
          this.disks = r.data.data;
          this.cdr.detectChanges();
        }
      });
    }else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.disks == null){
        this.detailService.getDiskList(this.storageId).subscribe((r: any) => {
          if (r.code === '200'){
            this.disks = r.data.data;
            this.cdr.detectChanges();
          }
        });
      }
    }
  }
  getPortsList(){
    this.getFCPortList();
    this.getFCoEPortList();
    this.getEthernetPortList();
    this.getBondPortList();
    this.getLogicPortsList();
    this.getFailoverGroups();
  }
  getFCPortList(){
      this.detailService.getFCPortList({"storageDeviceId":this.storageId,"portType":"FC"}).subscribe((r: any) => {
        if (r.code === '200'){
          this.fcs = r.data.data;
          this.cdr.detectChanges();
        }
      });
  }
  getFCoEPortList(){
    this.detailService.getFCPortList({"storageDeviceId":this.storageId,"portType":"FCoE"}).subscribe((r: any) => {
      if (r.code === '200'){
        this.fcoes = r.data.data;
        this.cdr.detectChanges();
      }
    });
  }
  getEthernetPortList(){
    this.detailService.getFCPortList({"storageDeviceId":this.storageId,"portType":"ETH"}).subscribe((r: any) => {
      if (r.code === '200'){
        this.eths = r.data.data;
        this.cdr.detectChanges();
      }
    });
  }
  getBondPortList(){
    this.detailService.getBondPortList(this.storageId).subscribe((r: any) => {
      if (r.code === '200'){
        this.bonds = r.data.data;
        this.cdr.detectChanges();
      }
    });
  }
  getLogicPortsList(){
    this.detailService.getLogicPortList(this.storageId).subscribe((r: any) => {
      if (r.code === '200'){
        this.logicports = r.data.data;
        this.cdr.detectChanges();
      }
    });
  }
  getFailoverGroups(){
    this.detailService.getFailoverGroups(this.storageId).subscribe((r: any) => {
      if (r.code === '200'){
        this.fgs = r.data.data;
        this.cdr.detectChanges();
      }
    });
  }

  formatCapacity(c: number){
    if (c < 1024){
      return c.toFixed(3)+" GB";
    }else if(c >= 1024 && c< 1048576){
      return (c/1024).toFixed(3) +" TB";
    }else if(c>= 1048576){
      return (c/1024/1024).toFixed(3)+" PB"
    }
  }
  initCapacity(){
    this.initCapacityDistribution();
  }
  initCapacityDistribution(){
    this.cd = new CapacityDistribution();
    const p= 0;
    this.cd.protection = p.toFixed(3);
    const fs = 4;
    this.cd.fileSystem = fs.toFixed(3);
    const v = 2.024;
    this.cd.volume = v.toFixed(3);
    const fc =1.078;
     this.cd.freeCapacity= fc.toFixed(3);

    const cc = new CapacityChart('3.016 TB');
    const cs = new CapacitySerie(2.024,1.078);
    cc.series.push(cs);
    this.cd.chart = cc;
  }
  backToList(){
    this.router.navigate(['storage']);
  }
  parseUsage(c:string){
    if (c===null || c==='') return 0;
    return Number.parseFloat(c).toFixed(2);
  }
}
