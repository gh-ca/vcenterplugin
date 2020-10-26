import {AfterViewInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, NgZone, OnInit} from '@angular/core';
import {
  CapacityDistribution, CapacitySavings, DetailService, Dtrees, NfsShare, PoolList, StorageController, StorageDetail,
  StorageDisk,
  StoragePool,
  Volume
} from './detail.service';
import { EChartOption } from 'echarts';
import {
  AxisLine, AxisPointer,
  ChartOptions,
  FileSystem, Legend, LegendData, LineStyle, MakePerformance,
  NfsService, Serie,
  SplitLine,
  TextStyle,
  Title, Tooltip,
  XAxis,
  YAxis
} from '../../nfs/nfs.service';
import {ActivatedRoute, Router} from "@angular/router";
import {CapacityChart, CapacitySerie} from "../storage.service";
import {BondPort, EthernetPort, FailoverGroup, FCoEPort, FCPort, LogicPort} from "./port.service";
import {FormControl, FormGroup} from "@angular/forms";
@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [DetailService, MakePerformance, NfsService],
})
export class DetailComponent implements OnInit, AfterViewInit {

  cd : CapacityDistribution;
  capSave:CapacitySavings;


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
  // 定时函数执行时间 默认一天
  timeInterval = 1 * 60 * 60 * 1000;
  poolRadio = 'table1'; // 存储池列表切换
  volumeRadio = 'table1'; // volume列表切换
  storageId = '1234';
  storageName= "";
  constructor(private nfsService:NfsService, private makePerformance: MakePerformance, private detailService: DetailService, private cdr: ChangeDetectorRef, private ngZone: NgZone,
              private activatedRoute: ActivatedRoute,private router:Router) { }
  detail: StorageDetail;
  storagePool: StoragePool[];
  volumes: Volume[];
  volumeTotal=0;
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
  storagePoolIds=[];
  volumeIds=[];

  selectRange = 'LAST_1_DAY';
  startTime = null;
  // endTime
  endTime = null;

  rangeTime = new FormGroup({
    start: new FormControl(),
    end: new FormControl()
  });
  // ranges
  ranges = NfsService.perRanges;

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
    // switch (this.range) {
    //   case 'LAST_1_HOUR': // 过去一小时
    //     break;
    //   case 'LAST_4_HOUR': // 过去四小时 此值目前接口没有
    //     break;
    //   case 'LAST_12_HOUR': // 过去12小时 此值目前接口没有
    //     break;
    //   default: // 默认过去24h
    //     break;
    // }
    const fsNames:string[] = [];
    fsNames.push('A7213075B5EE3AF3989D7DB938ED2CF8');
    // IOPS
    this.setChart(300,"IOPS","IO/s",
      NfsService.storageIOPS,fsNames,this.selectRange,NfsService.nfsUrl, this.startTime, this.endTime).then(res=>{
      // NfsService.nfsOPS,fsNames,this.selectRange,NfsService.nfsUrl, this.startTime, this.endTime).then(res=>{
      this.iopsChart = res;
      this.cdr.detectChanges();
    });

    // 带宽
    this.setChart(300,'Bandwidth', 'MB/s',
      NfsService.storageBDWT, fsNames, this.selectRange, NfsService.nfsUrl, this.startTime, this.endTime).then(res => {
      // NfsService.nfsBDWT, fsNames, this.selectRange, NfsService.nfsUrl, this.startTime, this.endTime).then(res => {
      this.bandwidthChart = res;
      this.cdr.detectChanges();
    });
    // 响应时间
    // this.latencyChart = echarts.init(document.querySelector('#latencyChart'));
    // this.perService.getIopsChart('Latency', 'ms', this.objTypeId, this.indicatorIdsREST, this.objIds,
    //   this.interval, this.range, this.beginTime, this.endTime).then(res => {
    //   this.latencyChart.setOption(res, true);
    //   this.cdr.detectChanges();
    // });
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
          this.detail = r.data;
          this.cdr.detectChanges();
        }else{

        }
      });
    }else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.detail === null){
        this.detailService.getStorageDetail(this.storageId).subscribe((r: any) => {
          if (r.code === '200'){
            this.detail = r.data;
            this.cdr.detectChanges();
          }
        });
      }
    }
  }
  getStoragePoolList(fresh: boolean){
    if (fresh){
      this.detailService.getStoragePoolList(this.storageId).subscribe((r: any) =>{
        if (r.code === '200'){
          this.storagePool = r.data;
          this.cdr.detectChanges();
          console.log('pool result:');
          console.log(r);
          this.liststoragepoolperformance();
        }
      });
    }else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.storagePool === null){
        this.detailService.getStoragePoolList(this.storageId).subscribe((r: any) =>{
          if (r.code === '200'){
            this.storagePool = r.data;
            this.cdr.detectChanges();
            this.liststoragepoolperformance();
          }
        });
      }
    }
  }
  liststoragepoolperformance(){
    console.log("storagePool",this.storagePool);
    if (this.storagePool === null || this.storagePool.length <= 0){ return; }
    this.storagePool.forEach(item => {
      this.storagePoolIds.push(item.storageInstanceId);
    });
    this.detailService.liststoragepoolperformance(this.storagePoolIds).subscribe((result: any) => {
      if (result.code === '200'){
        const chartList: StoragePool [] = result.data;
        if ( chartList !== null && chartList.length > 0){
          this.storagePool.forEach(item => {
            chartList.forEach(charItem => {
              if (item.storageInstanceId === charItem.id){
                item.maxBandwidth=charItem.maxBandwidth;
                item.maxIops=charItem.maxIops;
                item.maxLatency=charItem.maxLatency;
              }
            });
          });
          this.cdr.detectChanges();
        }
      }
    });
  }
  getStorageVolumeList(fresh: boolean){
    if (fresh){
      this.detailService.getVolumeListList(this.storageId).subscribe((r: any) => {
        if (r.code === '200'){
          this.volumes = r.data;
          if(this.volumes!=null){
            this.volumeTotal=this.volumes.length;
          }
          this.cdr.detectChanges();
          this.listVolumesperformance();
        }
      });
    }else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.volumes == null){
        this.detailService.getVolumeListList(this.storageId).subscribe((r: any) => {
          if (r.code === '200'){
            this.volumes = r.data;
            if(this.volumes!=null){
              this.volumeTotal=this.volumes.length;
            }
            this.cdr.detectChanges();
            this.listVolumesperformance();
          }
        });
      }
    }
  }
  listVolumesperformance(){
    if (this.volumes === null || this.volumes.length <= 0){ return; }
    this.volumes.forEach(item => {
      this.volumeIds.push(item.wwn);
    });
    this.detailService.listVolumesperformance(this.volumeIds).subscribe((result: any) => {
      if (result.code === '200'){
        const chartList: Volume [] = result.data;
        if ( chartList !== null && chartList.length > 0){
          this.volumes.forEach(item => {
            chartList.forEach(charItem => {
              if (item.wwn === charItem.wwn){
                item.bandwith=charItem.bandwith;
                item.iops=charItem.iops;
                item.lantency=charItem.lantency;
              }
            });
          });
          this.cdr.detectChanges();
        }
      }
    });
  }
  getFileSystemList(fresh: boolean){
    if (fresh){
      this.detailService.getFileSystemList(this.storageId).subscribe((r: any) => {
        if (r.code === '200'){
          this.fsList = r.data;
          this.cdr.detectChanges();
        }
      });
    }else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.fsList == null){
        this.detailService.getFileSystemList(this.storageId).subscribe((r: any) => {
          if (r.code === '200'){
            this.fsList = r.data;
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
          this.dtrees = r.data;
          this.cdr.detectChanges();
        }
      });
    }else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.dtrees == null){
        this.detailService.getDtreeList(this.storageId).subscribe((r: any) => {
          if (r.code === '200'){
            this.dtrees = r.data;
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
          this.shares = r.data;
          this.cdr.detectChanges();
        }
      });
    }else {
      // 此处防止重复切换tab每次都去后台请求数据

      if (this.shares == null){
        this.detailService.getShareList(this.storageId).subscribe((r: any) => {
          if (r.code === '200'){
            this.shares = r.data;
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
          this.controllers = r.data;
          this.cdr.detectChanges();
        }
      });
    }else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.controllers == null){
        this.detailService.getControllerList(this.storageId).subscribe((r: any) => {
          if (r.code === '200'){
            this.controllers = r.data;
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
          this.disks = r.data;
          this.cdr.detectChanges();
        }
      });
    }else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.disks == null){
        this.detailService.getDiskList(this.storageId).subscribe((r: any) => {
          if (r.code === '200'){
            this.disks = r.data;
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
        this.fcs = r.data;
        this.cdr.detectChanges();
      }
    });
  }
  getFCoEPortList(){
    this.detailService.getFCPortList({"storageDeviceId":this.storageId,"portType":"FCoE"}).subscribe((r: any) => {
      if (r.code === '200'){
        this.fcoes = r.data;
        this.cdr.detectChanges();
      }
    });
  }
  getEthernetPortList(){
    this.detailService.getFCPortList({"storageDeviceId":this.storageId,"portType":"ETH"}).subscribe((r: any) => {
      if (r.code === '200'){
        this.eths = r.data;
        this.cdr.detectChanges();
      }
    });
  }
  getBondPortList(){
    this.detailService.getBondPortList(this.storageId).subscribe((r: any) => {
      if (r.code === '200'){
        this.bonds = r.data;
        this.cdr.detectChanges();
      }
    });
  }
  getLogicPortsList(){
    this.detailService.getLogicPortList(this.storageId).subscribe((r: any) => {
      if (r.code === '200'){
        this.logicports = r.data;
        this.cdr.detectChanges();
      }
    });
  }
  getFailoverGroups(){
    this.detailService.getFailoverGroups(this.storageId).subscribe((r: any) => {
      if (r.code === '200'){
        this.fgs = r.data;
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
    this.buildCapacitySavings();
  }

  buildCapacitySavings(){
    this.capSave=new CapacitySavings();
    const usedCapacity=this.detail.usedCapacity; //默认单位是GB
    const beforeSave=Math.trunc(usedCapacity);//先取整数
    if(beforeSave< 4096){
      this.capSave.unit="GB";
      this.capSave.max=beforeSave+4-beforeSave%4;
      this.capSave.beforeSave=this.detail.usedCapacity;
      this.capSave.dedupe=this.detail.dedupedCapacity;
      this.capSave.compression=this.detail.compressedCapacity;
      this.capSave.afterSave=this.detail.optimizeCapacity;
    }else{
      this.capSave.unit="TB";

      this.capSave.beforeSave=this.detail.usedCapacity?this.detail.usedCapacity/1024:null;
      this.capSave.dedupe=this.detail.dedupedCapacity?this.detail.dedupedCapacity/1024:null;
      this.capSave.compression=this.detail.compressedCapacity?this.detail.compressedCapacity/1024:null;
      this.capSave.afterSave=this.detail.optimizeCapacity?this.detail.optimizeCapacity/1024:null;
      const max=Math.trunc(beforeSave/1024);
      this.capSave.max=max+4-max%4;
    }
    const bars=[0];
    for(var i=0;i<4;i++){
      bars.push(this.capSave.max/4*(i+1))
    }
    this.capSave.bars=bars;
    this.capSave.rate=(this.capSave.beforeSave/this.capSave.afterSave).toFixed(0) +": 1";
  }
  initCapacityDistribution(){
    this.cd = new CapacityDistribution();
    const p= 0;
    this.detail.protectionCapacity
    this.cd.protection = this.formatCapacity(this.detail.protectionCapacity);
    this.cd.fileSystem =this.formatCapacity(this.detail.fileCapacity);
    const v = 2.024;
    this.cd.volume =this.formatCapacity(this.detail.blockCapacity);
    this.cd.freeCapacity= this.getFreeCapacity(this.detail.totalEffectiveCapacity,this.detail.usedCapacity);

    const cc = new CapacityChart(this.formatCapacity(this.detail.totalEffectiveCapacity));
    const free=(this.detail.totalEffectiveCapacity-this.detail.usedCapacity)*100;
    const cs = new CapacitySerie(this.detail.usedCapacity,free);
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
  formatArry(str: string){
    if(str===null){
      return '';
    }
    str=str.replace("[","")
      .replace("]","");
    const strs=str.split(",");
    let r=""
    strs.forEach(s=>{
      r+=s.replace('"','').replace('"','')+",";
    });
    return r.substr(0,r.length-1);
  }
  getFreeCapacity(t:number,u:number){
    if(t==null ||t==0) return this.formatCapacity(0);
    if(u==null || u==0){
      return this.formatCapacity(t);
    }
    return this.formatCapacity(t-u);
  }

  /**
   * 设置折线图 ( 折线1 虚线UpperLine、折线2 虚线LowerLine、
   * 折线3Read、折线4Write)
   * @param height
   * @param title 标题
   * @param subtext 副标题
   * @param indicatorIds  获取参数指标（带宽的读写等） 0 读 1写
   * @param objIds 卷ID（vmfs）、fsId(nfs) 只能放一个值即length为1
   * @param range 时间段 LAST_5_MINUTE LAST_1_HOUR LAST_1_DAY LAST_1_WEEK LAST_1_MONTH LAST_1_QUARTER HALF_1_YEAR LAST_1_YEAR BEGIN_END_TIME INVALID
   * @param url 请求url
   */
  setChart(height: number, title: string, subtext: string, indicatorIds: any[], objIds: any[], range: string, url: string, startTime:string, endTime:string) {
    // 生成chart optiond对象
    const chart:ChartOptions = this.getNewChart(height, title, subtext);
    return new Promise((resolve, reject) => {
      const params = {
        indicator_ids: indicatorIds,
        obj_ids: objIds,
        range: range,
        begin_time: startTime,
        end_time: endTime,
      }
      this.nfsService.getLineChartData(url, params).subscribe((result: any) => {
        console.log('chartData: ', title, result);
        if (result.code === '200' && result.data !== null && result.data !== null) {
          const resData = result.data;
          // 设置标题
          chart.title.text = title;
          // 设置副标题
          chart.title.subtext = subtext;
          // 上限对象
          const upperData = resData[objIds[0]][indicatorIds[0]];
          // 下限对象
          const lowerData = resData[objIds[0]][indicatorIds[1]];
          // 上限最大值
          const pmaxData = this.makePerformance.getUpperOrLower(upperData, 'upper');
          // 下限最大值
          let lmaxData = this.makePerformance.getUpperOrLower(lowerData, 'lower');
          // 上、下限数据
          const uppers: any[] = upperData.series;
          const lower: any[] = lowerData.series;
          // 设置X轴
          this.makePerformance.setXAxisData(uppers, chart);
          // 设置y轴最大值
          chart.yAxis.max = (pmaxData > lmaxData ? pmaxData : lmaxData);
          console.log('chart.yAxis.pmaxData', pmaxData);
          console.log('chart.yAxis.lmaxData', lmaxData);
          // 设置Read 折线图数据
          uppers.forEach(item => {
            for (const key of Object.keys(item)) {
              // chartData.value = item[key];
              const value = Number(Number(item[key]).toFixed(4));
              chart.series[0].data.push({value: value, symbol: 'none'});
            }
          });
          // 设置write 折线图数据
          lower.forEach(item => {
            for (const key of Object.keys(item)) {
              const value = Number(Number(item[key]).toFixed(4));
              chart.series[1].data.push({value: value, symbol: 'none'});
            }
          });
          resolve(chart);
        } else {
          console.log('get chartData fail: ', result.description);
        }
      });
    });
  }
  /**
   * 获取一个chart的option对象 (option格式 折线1 虚线UpperLine、折线2 虚线LowerLine、
   * 折线3Read、折线4Write)
   * @param height
   * @param title
   * @param subtext
   */
  getNewChart(height: number, title: string, subtext: string) {
    const chart: ChartOptions = new ChartOptions();
    // 高度
    chart.height = height;
    // 标题
    const titleInfo:Title = new Title();
    titleInfo.text = title;
    titleInfo.subtext = subtext;
    titleInfo.textAlign = 'bottom';
    const textStyle:TextStyle  = new TextStyle();
    textStyle.fontStyle = 'normal';
    titleInfo.textStyle = textStyle;

    chart.title = titleInfo;

    // x轴
    const xAxis: XAxis = new XAxis();
    xAxis.type = 'category';
    xAxis.boundaryGap = false;
    xAxis.data = [];

    chart.xAxis = xAxis;

    // y轴
    const yAxis: YAxis = new YAxis();
    yAxis.type = 'value';
    yAxis.min = 0;
    yAxis.splitNumber = 2;
    yAxis.boundaryGap = ['50%', '50%'];
    const axisLine: AxisLine = new AxisLine();
    axisLine.show = false;
    yAxis.axisLine = axisLine;
    const splitLine = new SplitLine();
    splitLine.show = true;
    const lineStyle = new LineStyle();
    lineStyle.type = 'dashed';
    splitLine.lineStyle = lineStyle;
    yAxis.splitLine = splitLine;

    chart.yAxis = yAxis;
    // 提示框
    const tooltip: Tooltip = new Tooltip();
    tooltip.trigger = 'axis';
    tooltip.formatter = '{b} <br/> {a0}: {c0}<br/>{a1}: {c1}<br/>';
    const axisPointer: AxisPointer = new AxisPointer();
    axisPointer.axis = 'x';
    axisPointer.type = 'line';
    tooltip.axisPointer = axisPointer;
    chart.tooltip = tooltip;

    // 指标
    const legend: Legend = new Legend();
    const legendData: LegendData[] = [];
    legendData.push(this.makePerformance.setLengdData('Read', 'circle'));
    legendData.push(this.makePerformance.setLengdData('Write', 'circle'));
    legend.x = 'right';
    legend.y = 'top';
    legend.selectedMode  = true;
    legend.data = legendData;

    chart.legend = legend;
    // 指标颜色
    const colors:string[] = ['#6870c4', '#01bfa8'];
    chart.color = colors;

    // 数据(格式)
    const series: Serie[] = [];
    series.push(this.makePerformance.setSerieData('Read', 'line', true, 'solid', '#6870c4', null))
    series.push(this.makePerformance.setSerieData('Write', 'line', true, 'solid', '#01bfa8', null))

    chart.series = series;

    return chart;
  }

  /**
   * 开始结束时间触发
   */
  changeDate() {
    if (!this.rangeTime.controls.start.hasError('matStartDateInvalid')
      && !this.rangeTime.controls.end.hasError('matEndDateInvalid')
      && this.rangeTime.controls.start.value !== null && this.rangeTime.controls.end.value !== null) { // 需满足输入规范且不为空
      this.startTime = this.rangeTime.controls.start.value._d.getTime();
      this.endTime = this.rangeTime.controls.end.value._d.getTime();
      console.log('startTime', this.startTime);
      console.log('endTime', this.endTime);
      this.changeFunc();
    } else {
      return;
    }
  }

  // 切换卷函数
  changeFunc() {
    console.log(this.selectRange);
    if (this.selectRange === 'BEGIN_END_TIME') {
      if (this.startTime === null || this.endTime === null) {
        console.log('开始结束时间不能为空');
        return;
      }
    } else { // 初始化开始结束时间
      this.startTime = null;
      this.endTime = null;
    }
    if (this.selectRange) {
      console.log('this.selectVolName+this.selectRange', this.selectRange);
      // 请求后台重新加载折线图
      this.initChart();
    } else {
      console.log('未选择卷或range');
    }
  }
}
