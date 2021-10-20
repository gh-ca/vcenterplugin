import {
  AfterViewInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  NgZone,
  OnInit,
  ViewChild,
} from '@angular/core';
import {
  CapacityDistribution,
  CapacitySavings,
  DetailService,
  Dtrees,
  NfsShare,
  PoolList,
  StorageController,
  StorageDetail,
  StorageDisk,
  StoragePool,
  Volume,
} from './detail.service';
import { EChartOption } from 'echarts';
import {
  AxisLine,
  AxisPointer,
  ChartOptions,
  FileSystem,
  Legend,
  LegendData,
  LineStyle,
  MakePerformance,
  NfsService,
  Serie,
  SplitLine,
  TextStyle,
  Title,
  Tooltip,
  XAxis,
  YAxis,
} from '../../nfs/nfs.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CapacityChart, CapacitySerie } from '../storage.service';
import { BondPort, EthernetPort, FailoverGroup, FCoEPort, FCPort, LogicPort } from './port.service';
import { FormControl, FormGroup } from '@angular/forms';
import { GlobalsService } from '../../../shared/globals.service';
import { ClrDatagridPagination } from '@clr/angular';
import { TranslatePipe } from '@ngx-translate/core';
import { DeviceFilter } from '../../vmfs/list/filter.component';
import {
  DtreeQuotaFilter,
  DtreeSecModFilter,
  FsStatusFilter,
  FsTypeFilter,
  MapStatusFilter,
  ProTypeFilter,
  StoragePoolStatusFilter,
  StoragePoolTypeFilter,
  VolProtectionStatusFilter,
  VolServiceLevelFilter,
  VolStatusFilter,
  VolStoragePoolFilter,
} from '../filter.component';
import { ServiceLevelList, VmfsListService } from '../../vmfs/list/list.service';
import { CommonService } from './../../common.service';
import { isMockData, mockData } from 'mock/mock';
import { handlerResponseErrorSimple, print } from 'app/app.helpers';
import { getDmestorageStoragedisks } from './../../../../mock/DMESTORAGE_STORAGEDISKS';
import { getDmestorageStoragepools } from 'mock/DMESTORAGE_STORAGEPOOLS';

@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [
    DetailService,
    TranslatePipe,
    MakePerformance,
    NfsService,
    VmfsListService,
    CommonService,
  ],
})
export class DetailComponent implements OnInit, AfterViewInit {
  print;
  /* 时间下拉 */
  timeSelectorRanges: any[];
  cd: CapacityDistribution;
  capSave: CapacitySavings;
  options = {
    tooltip: {
      trigger: 'item',
      formatter: ' {b}: {c} ({d}%)',
    },
    title: {
      text: '123',
      textAlign: 'center',
      padding: 0,
      textVerticalAlign: 'middle',
      textStyle: {
        fontSize: 22,
        color: '#63B3F7',
      },
      subtextStyle: {
        fontSize: 12,
        color: '#c2c6dc',
        align: 'center',
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
          position: 'center',
        },
        emphasis: {
          label: {
            show: false,
            fontSize: '30',
            fontWeight: 'bold',
          },
        },
        labelLine: {
          show: false,
        },
        data: [
          { value: 335, name: '直接访问' },
          { value: 310, name: '邮件营销' },
          { value: 234, name: '联盟广告' },
          { value: 135, name: '视频广告' },
          { value: 1548, name: '搜索引擎' },
        ],
      },
    ],
    color: ['#FF0000', '#FF9538', '#63B3F7'],
  };
  // 创建表格对象
  // IOPS+QoS上下限
  iopsChart: EChartOption = {};
  iopsChartDataIsNull = false;
  // 带宽+QoS上下限
  bandwidthChart: EChartOption = {};
  bdwtChartDataIsNull = false;
  range;
  // 定时函数执行时间 默认一天
  poolRadio = 'table1'; // 存储池列表切换
  volumeRadio = 'table1'; // volume列表切换
  storageId = '1234';
  storageName = '';
  detail: StorageDetail;
  storagePool: StoragePool[];
  volumes: Volume[];
  volumeTotal = 0;
  poolTotal = 0;
  fsTotal = 0;
  dtreeTotal = 0;
  shareTotal = 0;
  conTotal = 0;
  diskTotal = 0;
  fcTotal = 0;
  ethTotal = 0;
  fcoeTotal = 0;
  bondTotal = 0;
  logicTotal = 0;
  failTotal = 0;

  fsList: FileSystem[];
  dtrees: Dtrees[];
  shares: NfsShare[];
  controllers: StorageController[];
  disks: StorageDisk[];

  fcs: FCPort[];
  eths: EthernetPort[];
  fcoes: FCoEPort[];
  bonds: BondPort[];
  logicports: LogicPort[];
  fgs: FailoverGroup[];
  storagePoolIds = [];
  volumeIds = [];

  selectRange = 'LAST_1_DAY';
  startTime = null;
  // endTime
  endTime = null;

  totalPageNum = '/' + ' ' + '0'; // 卷信息分页个数

  rangeTime = new FormGroup({
    start: new FormControl(),
    end: new FormControl(),
  });

  isLoading = false;
  volIsLoading = false;
  fsIsLoading = false;
  dtreeIsLoading = false;
  shareIsLoading = false;
  controIsLoading = false;
  diskIsLoading = false;
  fcportIsLoading = false;
  fcoeportIsLoading = false;
  ethsIsLoading = false;
  bondIsLoading = false;
  logicPortIsLoading = false;
  fgsIsLoading = false;
  @ViewChild('paginationVolume') pagination: ClrDatagridPagination;

  volumeFooter = '1 - 10'; // 卷信息当前页数据
  volumeTotalPage = 10; // 卷信息总页数
  volumeCurrentPage = 1; // 卷信息当前页
  volumePageSize = 10; // 当前页数据数

  @ViewChild('storagePoolTypeFilter') storagePoolTypeFilter: StoragePoolTypeFilter;
  @ViewChild('storagePoolStatusFilter') storagePoolStatusFilter: StoragePoolStatusFilter;
  @ViewChild('volStatusFilter') volStatusFilter: VolStatusFilter;
  @ViewChild('portTypeFilter') portTypeFilter: ProTypeFilter;
  @ViewChild('mapStatusFilter') mapStatusFilter: MapStatusFilter;
  @ViewChild('volStoragePoolFilter') volStoragePoolFilter: VolStoragePoolFilter;
  @ViewChild('volServiceLevelFilter') volServiceLevelFilter: VolServiceLevelFilter;
  @ViewChild('volProtectionStatusFilter') volProtectionStatusFilter: VolProtectionStatusFilter;
  @ViewChild('fsStatusFilter') fsStatusFilter: FsStatusFilter;
  @ViewChild('fsTypeFilter') fsTypeFilter: FsTypeFilter;
  @ViewChild('dtreeQuotaFilter') dtreeQuotaFilter: DtreeQuotaFilter;
  @ViewChild('dtreeQuotaFilter') dtreeSecModFilter: DtreeSecModFilter;

  serviceLevelList: ServiceLevelList[] = []; // 服务等级列表
  volName: string; // 卷名称 筛选
  volSortDir: any; // 卷 排序方式-1 降序 1 升序
  volSortKey: string; //卷 排序字段名称 capacity_usage/capacity
  volReqParam; // 卷请求参数

  storageDetailTag: number;

  detailLoading = false;

  constructor(
    private nfsService: NfsService,
    private makePerformance: MakePerformance,
    private detailService: DetailService,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone,
    /*private gs: GlobalsService,*/
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private translatePipe: TranslatePipe,
    private vmfsListService: VmfsListService,
    private commonService: CommonService
  ) {
    this.timeSelectorRanges = this.commonService.timeSelectorRanges_type2;
    this.print = print;
  }

  //portList:
  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(queryParam => {
      this.storageId = queryParam.id;
      this.storageName = queryParam.name;
    });
    this.getStorageDetail(true);

    // 初始化服务等级数据
    this.setServiceLevelList();
  }

  // 获取服务等级数据
  setServiceLevelList() {
    // 初始化服务等级选择参数
    // this.gs.loading=true;
    // 获取服务等级数据
    this.vmfsListService.getServiceLevelList().subscribe((result: any) => {
      if (result.code === '200' && result.data !== null) {
        this.serviceLevelList = result.data.filter(item => item.totalCapacity !== 0);
      }
      // 隐藏loading
      // this.gs.loading=false;
      // this.gs.loading = false;
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });
  }

  ngAfterViewInit() {
    this.ngZone.runOutsideAngular(() => this.initChart());
  }

  // 初始化表格对象
  async initChart(paramsInfo = {}) {
    // this.gs.loading = false;=true;
    const fsNames: string[] = [];
    fsNames.push(this.storageId);
    // IOPS
    this.setChart(
      paramsInfo,
      150,
      this.translatePipe.transform('vmfs.iops') + '(IO/s)',
      null,
      NfsService.storageIOPS,
      fsNames,
      this.selectRange,
      NfsService.storageUrl,
      this.startTime,
      this.endTime
    ).then(res => {
      // this.gs.loading = false;
      this.iopsChart = res;
      this.iopsChartDataIsNull = res['series'][0].data.length < 1;
      this.cdr.detectChanges();
    });

    // 带宽
    this.setChart(
      paramsInfo,
      150,
      this.translatePipe.transform('nfs.qos_bandwidth') + '(MB/s)',
      '',
      NfsService.storageBDWT,
      fsNames,
      this.selectRange,
      NfsService.storageUrl,
      this.startTime,
      this.endTime
    ).then(res => {
      // this.gs.loading = false;
      this.bandwidthChart = res;
      this.bdwtChartDataIsNull = res['series'][0].data.length < 1;
      this.cdr.detectChanges();
    });
  }

  changeTab(page: string) {
    console.log('点击：', page);
    if (page === 'conf') {
      this.poolRadio = 'table1';
      this.getStorageDetail(false);
    }
    if (page === 'pool') {
      this.poolRadio = 'table1';
      this.cdr.detectChanges();
      this.getStoragePoolList(false);
    }
    if (page === 'volume') {
      //this.volumeRadio='table1';
      this.getStorageVolumeList(false);
    }
    if (page === 'fs') {
      this.getFileSystemList(false);
    }
    if (page === 'dtrees') {
      this.getDtreeList(false);
    }
    if (page === 'shares') {
      this.getShareList(false);
    }
    if (page === 'capacity') {
      this.initCapacity();
    }
    if (page === 'hardware') {
      this.getControllerList(false);
    }
    if (page === 'controller') {
      this.getControllerList(false);
    }
    if (page === 'disk') {
      this.getDisksList(false);
    }
    if (page === 'port') {
      this.getPortsList();
    }
  }

  getStorageDetail(fresh: boolean) {
    if (fresh) {
      // this.gs.loading=true;
      this.detailLoading = true;
      const handlerGetStorageDetailSuccess = (r: any) => {
        // this.gs.loading = false; = false;
        this.detailLoading = false;
        if(isMockData){
          r={
            "code":"200",
            "data":{
              "id":"40ed4f57-0d98-4592-884e-3c044a64172f",
              "name":"Dorado5600-203",
              "ip":"51.10.132.203",
              "status":"0",
              "synStatus":"2",
              "sn":"2102353GTG10L6000003",
              "vendor":"Huawei",
              "model":"OceanStor Dorado 5600 V6",
              "usedCapacity":399.1181640625,
              "usableCapacity":44584.928,
              "totalCapacity":0,
              "totalEffectiveCapacity":209715200,
              "freeEffectiveCapacity":170003.94434,
              "subscriptionCapacity":22317,
              "protectionCapacity":0.08,
              "fileCapacity":0,
              "blockCapacity":0,
              "blockFileCapacity":399.109,
              "dedupedCapacity":1263.04,
              "compressedCapacity":30.020000000000003,
              "optimizeCapacity":-893.9418359375,
              "azIds":[

              ],
              "storagePool":null,
              "volume":null,
              "fileSystem":null,
              "dTrees":null,
              "nfsShares":null,
              "bandPorts":null,
              "logicPorts":null,
              "storageControllers":null,
              "storageDisks":null,
              "productVersion":"6.1.0.SPH2",
              "warning":null,
              "event":null,
              "location":"Q",
              "patchVersion":"SPH2",
              "maintenanceStart":null,
              "maintenanceOvertime":null,
              "qosFlag":false,
              "storageTypeShow":{
                "qosTag":1,
                "workLoadShow":1,
                "ownershipController":false,
                "allocationTypeShow":2,
                "deduplicationShow":false,
                "compressionShow":false,
                "capacityInitialAllocation":false,
                "smartTierShow":false,
                "prefetchStrategyShow":false,
                "storageDetailTag":2,
                "dorado":true
              },
              "smartQos":null
            },
            "description":null
          }
        }
        if (r.code === '200') {
          this.detail = r.data;
          this.detail.location = this.HTMLDecode(this.detail.location);
          // 如果是V6设备可得容量单位为M，此处修改为G
          /* if (this.detail.storageTypeShow.dorado) {
            this.detail.totalEffectiveCapacity = this.detail.totalEffectiveCapacity / 1024;
          } */
          this.storageDetailTag = this.detail.storageTypeShow.storageDetailTag;
        }
        this.getStoragePoolList(true);
        this.cdr.detectChanges();
      };
      if (isMockData) {
        handlerGetStorageDetailSuccess(mockData.DMESTORAGE_STORAGE);
      } else {
        this.detailService
          .getStorageDetail(this.storageId)
          .subscribe(handlerGetStorageDetailSuccess, handlerResponseErrorSimple);
      }
    } else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.detail === null) {
        // this.gs.loading = false;=true;
        this.detailLoading = true;
        const handlerGetStorageDetailSuccess2 = (r: any) => {
          // this.gs.loading = false;
          this.detailLoading = false;
          if (r.code === '200') {
            this.detail = r.data;
            this.detail.location = this.HTMLDecode(this.detail.location);
            // 如果是V6设备可得容量单位为M，此处修改为G
            /* if (this.detail.storageTypeShow.dorado) {
              this.detail.totalEffectiveCapacity = this.detail.totalEffectiveCapacity / 1024;
            } */
            this.storageDetailTag = this.detail.storageTypeShow.storageDetailTag;
            this.getStoragePoolList(true);
          }
          this.cdr.detectChanges();
        };

        if (isMockData) {
          handlerGetStorageDetailSuccess2(mockData.DMESTORAGE_STORAGE);
        } else {
          this.detailService
            .getStorageDetail(this.storageId)
            .subscribe(handlerGetStorageDetailSuccess2, handlerResponseErrorSimple);
        }
      }
    }
  }

  /**
   * location处理
   * @param strParam
   * @constructor
   */
  HTMLDecode(strParam) {
    if (!strParam) {
      return strParam;
    }

    // 避免嵌套转义的情况, e.g.&amp;#39
    let str = strParam;
    while (str.indexOf('&amp;') > -1) {
      str = str.replace(/&amp;/g, '&');
    }

    // 判断是否存在HTML字符实体
    if (/&[a-zA-Z]{2,5};/.test(str) || /&#\d{2,5};/.test(str)) {
      const div = document.createElement('div');
      div.innerHTML = str;
      str = div.innerText;
    }
    return str;
  }

  refreshStoragePool() {
    if (this.storagePoolStatusFilter) {
      this.storagePoolStatusFilter.initStatus();
    }
    if (this.storagePoolTypeFilter) {
      this.storagePoolTypeFilter.initType();
    }
    this.getStoragePoolList(true);
  }

  getStoragePoolList(fresh: boolean) {
    if (fresh) {
      // this.gs.loading = false;=true;
      this.isLoading = true;
      const handlerGetStoragePoolListSuccess = (r: any) => {
        // this.gs.loading = false; = false;
        this.isLoading = false;
        if (r.code === '200') {
          this.storagePool = r.data;
          // 设置容量个利用率
          this.storagePool.forEach(item => {
            item.capUsage = (item?.consumedCapacity / item?.totalCapacity) * 100 || 0;
            item.supRate = (item?.subscribedCapacity / item?.totalCapacity) * 100 || 0;
          });
          // 如果是v6设备存储池的类型为Block/File
          if (this.detail.storageTypeShow.dorado) {
            this.storagePool.forEach(item => (item.mediaType = 'block/file'));
          }
          const allName = this.storagePool.map(item => item.name);
          if (this.volStoragePoolFilter) {
            this.volStoragePoolFilter.initAllName(allName);
          }
          this.poolTotal = this.storagePool.length == null ? 0 : this.storagePool.length;
          this.storageListInitHandle();
          this.initCapacity();
          this.cdr.detectChanges();
          this.liststoragepoolperformance();
        }
      };

      if (isMockData) {
        handlerGetStoragePoolListSuccess(getDmestorageStoragepools(20));
      } else {
        this.detailService
          .getStoragePoolList(this.storageId)
          .subscribe(handlerGetStoragePoolListSuccess, handlerResponseErrorSimple);
      }
    } else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.storagePool === null) {
        // this.gs.loading = false;=true;
        this.detailService.getStoragePoolList(this.storageId).subscribe((r: any) => {
          // this.gs.loading = false;
          if (r.code === '200') {
            this.storagePool = r.data;
            // 设置容量个利用率
            this.storagePool.forEach(item => {
              item.capUsage = (item.consumedCapacity / item.totalCapacity) * 100;
              item.supRate = (item.subscribedCapacity / item.totalCapacity) * 100;
            });
            // 如果是v6设备存储池的类型为Block/File
            if (this.detail.storageTypeShow.dorado) {
              this.storagePool.forEach(item => (item.mediaType = 'block/file'));
            }
            const allName = this.storagePool.map(item => item.name);
            if (this.volStoragePoolFilter) {
              this.volStoragePoolFilter.initAllName(allName);
            }
            this.poolTotal = this.storagePool.length == null ? 0 : this.storagePool.length;
            this.storageListInitHandle();
            this.initCapacity();
            this.cdr.detectChanges();
            this.liststoragepoolperformance();
          }
        });
      }
    }
  }

  /**
   * 存储池数据初始化
   */
  storageListInitHandle() {
    if (this.storagePool) {
      this.storagePool.forEach(item => {
        // switch (item.tier1RaidLv) {
        //   case "1":
        //     item.tier1RaidLvDesc = "RAID10";
        //     break;
        //   case "2":
        //     item.tier1RaidLvDesc = "RAID5";
        //     break;
        //   case "3":
        //     item.tier1RaidLvDesc = "RAID0";
        //     break;
        //   case "4":
        //     item.tier1RaidLvDesc = "RAID1";
        //     break;
        //   case "5":
        //     item.tier1RaidLvDesc = "RAID6";
        //     break;
        //   case "6":
        //     item.tier1RaidLvDesc = "RAID50";
        //     break;
        //   case "7":
        //     item.tier1RaidLvDesc = "RAID3";
        //     break;
        //   case "11":
        //     item.tier1RaidLvDesc = "RAIDTP";
        //     break;
        //   default:
        //     item.tier1RaidLvDesc = item.tier1RaidLv;
        //     break;
        // }
        // switch (item.physicalType) {
        //   case "sata":
        //     item.physicalTypeDesc = "SATA";
        //     break;
        //   case "sas":
        //     item.physicalTypeDesc = "SAS";
        //     break;
        //   case "ssd":
        //     item.physicalTypeDesc = "SSD";
        //     break;
        //   case "nl-sas":
        //     item.physicalTypeDesc = "NL-SAS";
        //     break;
        //   case "unknown":
        //     item.physicalTypeDesc = this.translatePipe.transform('storage.detail.storagePool.unknown');
        //     break;
        //   default:
        //     item.physicalTypeDesc = item.physicalType;
        //     break;
        // }
        switch (item.mediaType) {
          case 'file':
            item.mediaTypeDesc = this.translatePipe.transform('enum.type.file');
            break;
          case 'block':
            item.mediaTypeDesc = this.translatePipe.transform('enum.type.block');
            break;
          default:
            item.mediaTypeDesc = item.mediaType;
            break;
        }
      });
    }
  }

  liststoragepoolperformance() {
    if (this.storagePool === null || this.storagePool.length <= 0) {
      return;
    }
    const storagePoolIds = [];
    this.storagePool.forEach(item => {
      storagePoolIds.push(item.storageInstanceId);
    });
    this.storagePoolIds = storagePoolIds;
    this.detailService.liststoragepoolperformance(this.storagePoolIds).subscribe((result: any) => {
      if (result.code === '200') {
        const chartList: StoragePool[] = result.data;
        if (chartList !== null && chartList.length > 0) {
          this.storagePool.forEach(item => {
            chartList.forEach(charItem => {
              if (item.storageInstanceId === charItem.id) {
                item.maxBandwidth = charItem.maxBandwidth;
                item.maxIops = charItem.maxIops;
                item.maxLatency = charItem.maxLatency;
              }
            });
          });
          this.cdr.detectChanges();
        }
      }
    });
  }

  refreshVolList() {
    if (this.volStatusFilter) {
      this.volStatusFilter.initStatus();
    }
    if (this.portTypeFilter) {
      this.portTypeFilter.initType();
    }
    if (this.mapStatusFilter) {
      this.mapStatusFilter.initStatus();
    }
    if (this.volStoragePoolFilter) {
      this.volStoragePoolFilter.initPoolNameStatus();
    }
    if (this.volServiceLevelFilter) {
      this.volServiceLevelFilter.initServiceLevel();
    }

    // this.volProtectionStatusFilter.initProtectionStatus();
    // 清空查询条件
    this.clearVolSearchInfo();
    this.getStorageVolumeList(true);
  }

  getStorageVolumeList(fresh: boolean) {
    const handlerGetVolumeListListByPageSuccess = (r: any) => {
      this.volIsLoading = false;
      if (r.code === '200') {
        this.volumes = r.data.volumeList;
        this.volumeTotal = r.data.count;
        this.volumes.forEach(vol => {
          vol.capacityUsageNum = vol.capacityUsage ? Number.parseFloat(vol.capacityUsage) : 0;
        });
        // 设置总页数、footer
        this.setVolumeTotalAndFooter();

        // this.totalPageNum = '/' + ' ' + Math.ceil(this.volumeTotal/(query ? query.pageSize:10));
        if (this.volumeRadio === 'table2') {
          this.listVolumesperformance();
        }
      }
      this.cdr.detectChanges();
    };
    let reqParams;
    if (this.volReqParam != null) {
      reqParams = this.volReqParam;
      this.volReqParam.pageSize = this.volumePageSize;
      this.volReqParam.pageNo = (this.volumeCurrentPage - 1) * this.volumePageSize;
    } else {
      reqParams = {
        storageId: this.storageId,
        pageSize: this.volumePageSize,
        pageNo: (this.volumeCurrentPage - 1) * this.volumePageSize,
      };
    }
    if (fresh) {
      this.volIsLoading = true;
      if (isMockData) {
        handlerGetVolumeListListByPageSuccess(mockData.DMESTORAGE_VOLUMES_BY_PAGE);
      } else {
        this.detailService
          .getVolumeListListByPage(reqParams)
          .subscribe(handlerGetVolumeListListByPageSuccess, handlerResponseErrorSimple);
      }
    } else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.volumes == null) {
        this.volIsLoading = true;
        if (isMockData) {
          handlerGetVolumeListListByPageSuccess(mockData.DMESTORAGE_VOLUMES_BY_PAGE);
        } else {
          this.detailService
            .getVolumeListListByPage(reqParams)
            .subscribe(handlerGetVolumeListListByPageSuccess, handlerResponseErrorSimple);
        }
      }
    }
  }

  listVolumesperformance() {
    if (this.volumes === null || this.volumes.length <= 0) {
      return;
    }

    const volumeIds = [];
    this.volumes.forEach(item => {
      volumeIds.push(item.wwn);
    });
    this.volumeIds = volumeIds;
    this.volIsLoading = true;
    this.detailService.listVolumesperformance(this.volumeIds).subscribe((result: any) => {
      if (result.code === '200') {
        const chartList: Volume[] = result.data;
        if (chartList !== null && chartList.length > 0) {
          this.volumes.forEach(item => {
            chartList.forEach(charItem => {
              if (item.wwn === charItem.wwn) {
                item.bandwith = charItem.bandwith;
                item.iops = charItem.iops;
                item.lantency = charItem.lantency;
              }
            });
          });
        }
      }
      this.volIsLoading = false;
      this.cdr.detectChanges();
    });
  }

  refreshFileSystem() {
    if (this.fsStatusFilter) {
      this.fsStatusFilter.initStatus();
    }
    if (this.fsTypeFilter) {
      this.fsTypeFilter.initType();
    }

    this.getFileSystemList(true);
  }

  getFileSystemList(fresh: boolean) {
    const handlerGetFileSystemListSuccess = (r: any) => {
      this.fsIsLoading = false;
      if (r.code === '200') {
        this.fsList = r.data;
        this.fsTotal = this.fsList == null ? 0 : this.fsList.length;
      }
      this.cdr.detectChanges();
    };
    if (fresh) {
      this.fsIsLoading = true;
      if (isMockData) {
        handlerGetFileSystemListSuccess(mockData.DMESTORAGE_FILESYSTEMS);
      } else {
        this.detailService
          .getFileSystemList(this.storageId)
          .subscribe(handlerGetFileSystemListSuccess, handlerResponseErrorSimple);
      }
    } else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.fsList == null) {
        this.fsIsLoading = true;
        if (isMockData) {
          handlerGetFileSystemListSuccess(mockData.DMESTORAGE_FILESYSTEMS);
        } else {
          this.detailService
            .getFileSystemList(this.storageId)
            .subscribe(handlerGetFileSystemListSuccess, handlerResponseErrorSimple);
        }
      }
    }
  }

  refreshDtree() {
    if (this.dtreeSecModFilter) {
      this.dtreeSecModFilter.initSecMod();
    }
    if (this.dtreeQuotaFilter) {
      this.dtreeQuotaFilter.initQuota();
    }
    this.getDtreeList(true);
  }

  getDtreeList(fresh: boolean) {
    const handlerGetDtreeListSuccess = (r: any) => {
      this.dtreeIsLoading = false;
      if (r.code === '200') {
        this.dtrees = r.data;
        this.dtreeTotal = this.dtrees == null ? 0 : this.dtrees.length;
      }
      this.cdr.detectChanges();
    };

    if (fresh) {
      this.dtreeIsLoading = true;
      if (isMockData) {
        handlerGetDtreeListSuccess(mockData.DMESTORAGE_DTREES);
      } else {
        this.detailService
          .getDtreeList(this.storageId)
          .subscribe(handlerGetDtreeListSuccess, handlerResponseErrorSimple);
      }
    } else {
      if (this.dtrees == null) {
        // 此处防止重复切换tab每次都去后台请求数据
        this.dtreeIsLoading = true;
        if (isMockData) {
          handlerGetDtreeListSuccess(mockData.DMESTORAGE_DTREES);
        } else {
          this.detailService
            .getDtreeList(this.storageId)
            .subscribe(handlerGetDtreeListSuccess, handlerResponseErrorSimple);
        }
      }
    }
  }

  getShareList(fresh: boolean) {
    const handlerGetShareListSuccess = (r: any) => {
      this.shareIsLoading = false;
      if (r.code === '200') {
        this.shares = r.data;
        this.shareTotal = this.shares == null ? 0 : this.shares.length;
      }
      this.cdr.detectChanges();
    };

    if (fresh) {
      this.shareIsLoading = true;
      if (isMockData) {
        handlerGetShareListSuccess(mockData.DMESTORAGE_NFSSHARES);
      } else {
        this.detailService
          .getShareList(this.storageId)
          .subscribe(handlerGetShareListSuccess, handlerResponseErrorSimple);
      }
    } else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.shares == null) {
        this.shareIsLoading = true;
        if (isMockData) {
          handlerGetShareListSuccess(mockData.DMESTORAGE_NFSSHARES);
        } else {
          this.detailService
            .getShareList(this.storageId)
            .subscribe(handlerGetShareListSuccess, handlerResponseErrorSimple);
        }
      }
    }
  }

  getControllerList(fresh: boolean) {
    if (fresh) {
      this.controIsLoading = true;
      this.detailService.getControllerList(this.storageId).subscribe((r: any) => {
        this.controIsLoading = false;
        if (r.code === '200') {
          this.controllers = r.data;
          this.conTotal = this.controllers == null ? 0 : this.controllers.length;
          this.cdr.detectChanges();
        }
      });
    } else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.controllers == null) {
        this.controIsLoading = true;
        this.detailService.getControllerList(this.storageId).subscribe((r: any) => {
          this.controIsLoading = false;
          if (r.code === '200') {
            this.controllers = r.data;
            this.conTotal = this.controllers == null ? 0 : this.controllers.length;
            this.cdr.detectChanges();
          }
        });
      }
    }
  }

  getDisksList(fresh: boolean) {
    const handleGetDiskListSuccess = (r: any) => {
      this.diskIsLoading = false;
      if (r.code === '200') {
        this.disks = r.data;
        this.diskTotal = this.disks == null ? 0 : this.disks.length;
        this.cdr.detectChanges();
      }
    };

    if (fresh) {
      this.diskIsLoading = true;
      if (isMockData) {
        handleGetDiskListSuccess(getDmestorageStoragedisks(100));
      } else {
        this.detailService
          .getDiskList(this.storageId)
          .subscribe(handleGetDiskListSuccess, handlerResponseErrorSimple);
      }
    } else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.disks == null) {
        this.diskIsLoading = true;
        if (isMockData) {
          handleGetDiskListSuccess(getDmestorageStoragedisks(100));
        } else {
          this.detailService
            .getDiskList(this.storageId)
            .subscribe(handleGetDiskListSuccess, handlerResponseErrorSimple);
        }
      }
    }
  }

  getPortsList() {
    this.getFCPortList();
    this.getFCoEPortList();
    this.getEthernetPortList();
    this.getBondPortList();
    this.getLogicPortsList();
    this.getFailoverGroups();
  }

  getFCPortList() {
    this.fcportIsLoading = true;
    const handlerGetFCSuccess = (r: any) => {
      this.fcportIsLoading = false;
      if (r.code === '200') {
        this.fcs = r.data;
        this.fcTotal = this.fcs == null ? 0 : this.fcs.length;
        this.cdr.detectChanges();
      }
    };

    if (isMockData) {
      handlerGetFCSuccess(mockData.DMESTORAGE_STORAGEPORT_FC);
    } else {
      this.detailService
        .getFCPortList({ storageDeviceId: this.storageId, portType: 'FC' })
        .subscribe(handlerGetFCSuccess);
    }
  }

  getFCoEPortList() {
    this.fcoeportIsLoading = true;
    this.detailService
      .getFCPortList({ storageDeviceId: this.storageId, portType: 'FCoE' })
      .subscribe((r: any) => {
        this.fcoeportIsLoading = false;
        if (r.code === '200') {
          this.fcoes = r.data;
          this.fcoeTotal = this.fcoes == null ? 0 : this.fcoes.length;
          this.cdr.detectChanges();
        }
      });
  }

  getEthernetPortList() {
    this.ethsIsLoading = true;
    this.detailService
      .getFCPortList({ storageDeviceId: this.storageId, portType: 'ETH' })
      .subscribe((r: any) => {
        this.ethsIsLoading = false;
        if (r.code === '200') {
          this.eths = r.data;
          this.ethTotal = this.eths == null ? 0 : this.eths.length;
          this.cdr.detectChanges();
        }
      });
  }

  getBondPortList() {
    this.bondIsLoading = true;
    this.detailService.getBondPortList(this.storageId).subscribe((r: any) => {
      this.bondIsLoading = false;
      if (r.code === '200') {
        this.bonds = r.data;
        this.bondTotal = this.bonds == null ? 0 : this.bonds.length;
        this.cdr.detectChanges();
      }
    });
  }

  getLogicPortsList() {
    this.logicPortIsLoading = true;
    this.detailService.getLogicPortList(this.storageId).subscribe((r: any) => {
      this.logicPortIsLoading = false;
      if (r.code === '200') {
        this.logicports = r.data;
        this.logicTotal = this.logicports == null ? 0 : this.logicports.length;
        this.cdr.detectChanges();
      }
    });
  }

  getFailoverGroups() {
    this.fgsIsLoading = true;
    this.detailService.getFailoverGroups(this.storageId).subscribe((r: any) => {
      this.fgsIsLoading = false;
      if (r.code === '200') {
        this.fgs = r.data;
        this.failTotal = this.fgs == null ? 0 : this.fgs.length;
        this.cdr.detectChanges();
      }
    });
  }

  formatCapacity(c: number) {
    if ((c < 1024&&c>1)||c===0) {
      return c.toFixed(3) + ' GB';
    } else if (c >= 1024 && c < 1048576) {
      return (c / 1024).toFixed(3) + ' TB';
    } else if (c >= 1048576) {
      return (c / 1024 / 1024).toFixed(3) + ' PB';
    }else if(c<1&&c>0){
      return (c*1024).toFixed(3)+'MB'
    }
  }

  /**
   * @Description v6 （单位 mb : 小于1换KB 大于1024换Gb大于1024GB换Tb）
   * @date 2021-04-16
   * @param {any} c:number
   * @returns {any}
   */
  formatCapacityV6(c: number) {
    const p = pow => Math.pow(1024, pow);

    const u = p(1);
    const uu = p(2);
    const uuu = p(3);

    if (c < 1) {
      return c * u + ' KB';
    }

    if (c >= 1 && c < u) {
      return c.toFixed(3) + ' MB';
    }

    if (c >= u && c < uu) {
      return (c / u).toFixed(3) + ' GB';
    }

    if (c >= uu && c < uuu) {
      return (c / uu).toFixed(3) + ' TB';
    }

    if (c >= uuu) {
      return (c / uuu).toFixed(3) + ' PB';
    }
  }

  initCapacity() {
    this.initCapacityDistribution();
    this.buildCapacitySavings();
  }

  buildCapacitySavings() {
    this.capSave = new CapacitySavings();
    const usedCapacity = this.detail.usedCapacity; //默认单位是GB
    const beforeSave = Math.trunc(usedCapacity); //先取整数
    if (beforeSave < 4096) {
      this.capSave.unit = 'GB';
      this.capSave.max = beforeSave + 4 - (beforeSave % 4);
      this.capSave.beforeSave = this.detail.usedCapacity;
      this.capSave.dedupe = this.detail.dedupedCapacity;
      this.capSave.compression = this.detail.compressedCapacity;
      this.capSave.afterSave = this.detail.optimizeCapacity;
    } else {
      this.capSave.unit = 'TB';

      this.capSave.beforeSave = this.detail.usedCapacity ? this.detail.usedCapacity / 1024 : null;
      this.capSave.dedupe = this.detail.dedupedCapacity ? this.detail.dedupedCapacity / 1024 : null;
      this.capSave.compression = this.detail.compressedCapacity
        ? this.detail.compressedCapacity / 1024
        : null;
      this.capSave.afterSave = this.detail.optimizeCapacity
        ? this.detail.optimizeCapacity / 1024
        : null;
      const max = Math.trunc(beforeSave / 1024);
      this.capSave.max = max + 4 - (max % 4);
    }
    const bars = [0];
    for (var i = 0; i < 4; i++) {
      bars.push((this.capSave.max / 4) * (i + 1));
    }
    this.capSave.bars = bars;
    this.capSave.rate = (this.capSave.beforeSave / this.capSave.afterSave).toFixed(0) + ': 1';
  }

  initCapacityDistribution() {
    this.cd = new CapacityDistribution();
    const p = 0;
    this.detail.protectionCapacity;
    /* 保护容量 */
    this.cd.protection = this.formatCapacity(this.detail.protectionCapacity);
    let storagePoolAllUsedCap;
    let freeCapacity;
    let title;

    // dorado v6.1版本及高版本
    const handleDoradoV6 = () => {
      // storagePoolAllUsedCap = this.storagePool.map(item => item.consumedCapacity).reduce(this.getSum, 0).toFixed(3);
      storagePoolAllUsedCap = this.formatCapacity(this.detail.blockFileCapacity);
      this.cd.blockFile = storagePoolAllUsedCap;
      /* 总容量-保护容量-块/文件容量 */
      // freeCapacity = (this.detail.totalEffectiveCapacity - this.detail.protectionCapacity - this.detail.blockFileCapacity).toFixed(3);
      /* FIX: v6直接使用提供的字段 */
      freeCapacity = this.detail.usableCapacity-this.detail.usedCapacity;
      /*  */
      this.cd.freeCapacity = this.formatCapacity(freeCapacity);
      title =
        this.formatCapacity(this.detail.usableCapacity) +
        '\n' +
        this.translatePipe.transform('storage.chart.total');
    };

    // dorado v 6.0版本及更低版本
    const handleOtherVersion = () => {
      this.cd.fileSystem = this.formatCapacity(this.detail.fileCapacity);
      this.cd.volume = this.formatCapacity(this.detail.blockCapacity);
      freeCapacity = (this.detail.totalEffectiveCapacity - this.detail.usedCapacity).toFixed(3);
      /*  */
      this.cd.freeCapacity = this.formatCapacity(freeCapacity);
      title =
        this.formatCapacity(this.detail.totalEffectiveCapacity) +
        '\n' +
        this.translatePipe.transform('storage.chart.total');
    };

    if (this.detail.storageTypeShow.dorado) {
      handleDoradoV6();
    } else {
      handleOtherVersion();
    }

    const cc = new CapacityChart(title);
    console.log(
      this.detail.protectionCapacity,
      this.detail.fileCapacity,
      this.detail.blockCapacity,
      Number(freeCapacity),
      Number(this.detail.blockFileCapacity),
      this.detail.storageTypeShow.dorado,
      this.translatePipe
    );
    let cs = new CapacitySerie(
      this.detail.protectionCapacity,
      this.detail.fileCapacity,
      this.detail.blockCapacity,
      Number(freeCapacity),
      Number(this.detail.blockFileCapacity),
      this.detail.storageTypeShow.dorado,
      this.translatePipe
    );
    cc.series.push(cs);
    this.cd.chart = cc;
  }

  getSum(total, num) {
    return total + Math.round(num);
  }

  backToList() {
    this.router.navigate(['storage']);
  }

  parseUsage(c: string) {
    if (c === null || c === '') {
      return 0;
    }
    return Number.parseFloat(c).toFixed(2);
  }

  formatArry(str: string) {
    if (!str) {
      return '';
    }
    str = str?.replace('[', '').replace(']', '');
    const strs = str?.split(',');
    let r = '';
    strs.forEach(s => {
      r += s.replace('"', '').replace('"', '') + ',';
    });
    return r.substr(0, r.length - 1);
  }

  getFreeCapacity(t: number, u: number) {
    if (t == null || t == 0) {
      return this.formatCapacity(0);
    }
    if (u == null || u == 0) {
      return this.formatCapacity(t);
    }
    return this.formatCapacity(t - u);
  }
  getFreeCapacityV6(t: number, u: number) {
    if (t == null || t == 0) {
      return this.formatCapacityV6(0);
    }
    if (u == null || u == 0) {
      return this.formatCapacityV6(t);
    }
    return this.formatCapacityV6(t - u);
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
  setChart(
    paramsInfo: any,
    height: number,
    title: string,
    subtext: string,
    indicatorIds: any[],
    objIds: any[],
    range: string,
    url: string,
    startTime: string,
    endTime: string
  ) {
    // 生成chart optiond对象
    const chart: ChartOptions = this.getNewChart(height, title, subtext);
    return new Promise((resolve, reject) => {
      const params = {
        indicator_ids: indicatorIds,
        obj_ids: objIds,
        ...paramsInfo,
      };
      const handlerGetLineChartDataSuccess = (result: any) => {
        if (isMockData) {
          objIds[0] = '9e1a6ffa-a278-11eb-994b-16eea383cc74';
        }
        if (result.code === '200' && result.data && result.data[objIds[0]]) {
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
          chart.yAxis.max = pmaxData > lmaxData ? pmaxData : lmaxData;
          // 设置Read 折线图数据
          uppers.forEach(item => {
            for (const key of Object.keys(item)) {
              // chartData.value = item[key];
              chart.series[0].data.push({ value: Number(item[key]), symbol: 'none' });
            }
          });
          // 设置write 折线图数据
          lower.forEach(item => {
            for (const key of Object.keys(item)) {
              chart.series[1].data.push({ value: Number(item[key]), symbol: 'none' });
            }
          });
        } else {
          console.log('get chartData fail: ', result.description);
        }
        resolve(chart);
      };

      if (isMockData) {
        handlerGetLineChartDataSuccess(mockData.ATASTORESTATISTICHISTRORY_STORAGE_DEVICE);
      } else {
        this.nfsService
          .getLineChartData(url, params)
          .subscribe(handlerGetLineChartDataSuccess, handlerResponseErrorSimple);
      }
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
    const titleInfo: Title = new Title();
    titleInfo.text = title;
    if (subtext) {
      titleInfo.subtext = subtext;
    }
    titleInfo.textAlign = 'bottom';
    const textStyle: TextStyle = new TextStyle();
    textStyle.fontStyle = 'normal';
    textStyle.fontWeight = 'normal';
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
    legend.selectedMode = true;
    legend.data = legendData;

    chart.legend = legend;
    // 指标颜色
    const colors: string[] = ['#6870c4', '#01bfa8'];
    chart.color = colors;

    // 数据(格式)
    const series: Serie[] = [];
    series.push(this.makePerformance.setSerieData('Read', 'line', true, 'solid', '#6870c4', null));
    series.push(this.makePerformance.setSerieData('Write', 'line', true, 'solid', '#01bfa8', null));

    chart.series = series;

    return chart;
  }

  /**
   * 开始结束时间触发
   */
  changeDate() {
    if (
      !this.rangeTime.controls.start.hasError('matStartDateInvalid') &&
      !this.rangeTime.controls.end.hasError('matEndDateInvalid') &&
      this.rangeTime.controls.start.value !== null &&
      this.rangeTime.controls.end.value !== null
    ) {
      // 需满足输入规范且不为空
      this.startTime = this.rangeTime.controls.start.value._d.getTime();
      this.endTime = this.rangeTime.controls.end.value._d.getTime();
      this.changeFunc();
    } else {
      return;
    }
  }

  // 切换卷函数
  changeFunc() {
    if (this.selectRange === 'BEGIN_END_TIME') {
      if (this.startTime === null || this.endTime === null) {
        console.log('开始结束时间不能为空');
        return;
      }
    } else {
      // 初始化开始结束时间
      this.startTime = null;
      this.endTime = null;
    }
    if (this.selectRange) {
      console.log('this.selectVolName+this.selectRange', this.selectRange);
      const paramsInfo = this.commonService.getInfoFromTimeRange(this.selectRange);
      this.startTime = paramsInfo.begin_time || '';
      this.endTime = paramsInfo.end_time || '';
      // 请求后台重新加载折线图
      this.initChart(paramsInfo);
    } else {
      console.log('未选择卷或range');
    }
  }

  params(query: any = {}) {
    // 对query进行处理
    const p = Object.assign({}, query);
    return p;
  }

  getVolDataByPage(pagination: any) {
    this.getStorageVolumeList(true);
    return pagination;
  }

  /**
   * 卷翻页
   * @param object
   * @param pageObj
   */
  jumpPage(object: any) {
    const obj = object.target;
    if (object.keyCode === 13 || object.keyCode === 108 || object.type === 'blur') {
      // 按下enter键触发
      if (obj.value && obj.value > 0 && obj.value != this.volumeCurrentPage) {
        obj.value = obj.value.replace(/[^1-9]/g, '');

        if (obj.value <= this.volumeTotalPage) {
          this.volumeCurrentPage = obj.value;
        } else {
          this.volumeCurrentPage = this.volumeTotalPage;
          obj.value = this.volumeCurrentPage;
        }
        this.getStorageVolumeList(true);
      } else {
        obj.value = this.volumeCurrentPage;
      }
    }
  }

  /**
   * 卷信息列表 当前页显示数据change函数
   * @param object
   */
  pageSizeChange(object: any) {
    this.volumeCurrentPage = 1;
    this.getStorageVolumeList(true);
  }

  /**
   * 卷信息列表 首页
   * @param object
   */
  volmeFirstPage() {
    if (this.volumeCurrentPage !== 1) {
      this.volumeCurrentPage = 1;
      this.getStorageVolumeList(true);
    }
  }

  /**
   * 卷信息列表 尾页
   * @param object
   */
  volmeLastPage() {
    if (this.volumeCurrentPage !== this.volumeTotalPage) {
      this.volumeCurrentPage = this.volumeTotalPage;
      this.getStorageVolumeList(true);
    }
  }

  /**
   * 卷信息列表 上一页
   */
  volumePreviousPage() {
    if (this.volumeCurrentPage !== 1) {
      this.volumeCurrentPage = this.volumeCurrentPage - 1;
      this.getStorageVolumeList(true);
    }
  }

  /**
   * 卷信息列表 下一页
   */
  volumeNextPage() {
    if (this.volumeCurrentPage !== this.volumeTotalPage) {
      this.volumeCurrentPage = this.volumeCurrentPage + 1;
      this.getStorageVolumeList(true);
    }
  }

  /**
   * 卷信息列表 设置页码总数、footer
   */
  setVolumeTotalAndFooter() {
    this.volumeTotalPage = Math.ceil(this.volumeTotal / this.volumePageSize);
    const last =
      this.volumeTotalPage === this.volumeCurrentPage
        ? this.volumeTotal
        : this.volumeCurrentPage * this.volumePageSize;
    const first = (this.volumeCurrentPage - 1) * this.volumePageSize + 1;
    this.volumeFooter = first + ' - ' + last;
  }

  /**
   * vol 条件查询
   */
  volQueryChange(name, caObject, caUObject) {
    let serviceLevelId;
    if (this.volServiceLevelFilter.serviceLevel) {
      serviceLevelId = this.serviceLevelList.filter(
        item => item.name == this.volServiceLevelFilter.serviceLevel
      )[0].id;
    }
    if (name != null) {
      this.volName = name;
    }
    if (caObject != null || caUObject != null) {
      if (caObject != null) {
        this.volSortKey = 'size';
      } else if (caUObject != null) {
        // this.volSortKey = 'capacity_usage';
      }
      if (caObject == 1 || caUObject == 1) {
        this.volSortDir = 'asc';
      } else if (caObject == -1 || caUObject == -1) {
        this.volSortDir = 'desc';
      } else {
        this.volSortDir = null;
        return !caObject;
      }
    }
    this.volumeCurrentPage = 1;
    const requestParams = {
      name: this.volName ? this.volName : '', //名称
      status: this.volStatusFilter.status ? this.volStatusFilter.status : '', //状态
      allocateType: this.portTypeFilter.type ? this.portTypeFilter.type : '', //分配类型
      attached: this.mapStatusFilter.status ? this.mapStatusFilter.status : '', // 映射状态
      servicelevelId: serviceLevelId ? serviceLevelId : '', //服务等级ID
      sortDir: this.volSortDir ? this.volSortDir : '', // 排序方式 desc 降序 asc 升序
      sortKey: this.volSortKey ? this.volSortKey : '', // 排序字段名称
      storageId: this.storageId,
      pageSize: this.volumePageSize,
      pageNo: (this.volumeCurrentPage - 1) * this.volumePageSize,
    };
    this.volReqParam = requestParams;
    this.getStorageVolumeList(true);
    return !caObject;
  }

  /**
   * vol 查询条件清空
   */
  clearVolSearchInfo() {
    this.volName = null;
    if (this.volStatusFilter) {
      this.volStatusFilter.initStatus();
    }
    if (this.portTypeFilter) {
      this.portTypeFilter.initType();
    }
    if (this.mapStatusFilter) {
      this.mapStatusFilter.initStatus();
    }

    this.volSortDir = null;
    this.volSortKey = null;
    this.volReqParam = null;
  }

  sortFunc(obj: any) {
    return !obj;
  }
}
