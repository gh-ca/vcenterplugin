import { ChangeDetectorRef, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { GlobalsService } from '@shared/globals.service';
import { VolumeInfo } from '../vmfs/volume-attribute/attribute.service';
import { transDateFormat, handlerResponseErrorSimple, is, helper } from 'app/app.helpers';
import { isMockData } from 'mock/mock';
import {
  RESPONSE_BANDWIDTH,
  RESPONSE_IOPS,
  RESPONSE_LANTENCY,
} from '../../../mock/DATASTORESTATISTICHISTRORY_VMFS';

export const URLS_NFS = {
  ACCESSNFS_LISTNFS: 'accessnfs/listnfs',
};

@Injectable()
export class NfsService {
  // 性能数据指标： indicator_ids 获取参数指标（读写）vmfs的性能数据查询的是卷的性能数据
  // VMFS IOPS 0Read 1Write
  static vmfsIOPS: Array<string> = ['1125921381744648', '1125921381744649'];
  // VMFS  bandwidth 0Read 1Write
  static vmfsBDWT: Array<string> = ['1125921381744646', '1125921381744647'];
  // VMFS  latency 0Read 1Write 2总计
  static vmfsLatency: Array<string> = ['1125921381744656', '1125921381744657', '1125921381744642'];
  static COUNTER_ID_VOLUME_RESPONSETIME: Array<string> = ['1125921381744642'];
  // VMFS  url
  static vmfsUrl = 'datastorestatistichistrory/vmfs';

  // NFS IOPS 0Read 1Write
  static nfsOPS: Array<string> = ['1126179079716865'];
  // static nfsOPS: Array<string> = ['1126179079716867', '1126179079716870'];
  // NFS  bandwidth 0Read 1Write
  static nfsBDWT: Array<string> = ['1126179079716878'];
  // static nfsBDWT: Array<string> = ['1126179079716868', '1126179079716871'];
  // NFS  latency 0Read 1Write
  static nfsLatency: Array<string> = ['1126179079716869', '1126179079716872'];
  // NFS  url
  static nfsUrl = 'datastorestatistichistrory/nfs';
  static storageUrl = 'datastorestatistichistrory/storageDevice';

  // Storage Detail 0Read 1Write
  static storageIOPS: Array<string> = ['1125904201875461', '1125904201875462'];
  // Storage bandwidth 0Read 1Write
  static storageBDWT: Array<string> = ['1125904201875459', '1125904201875460'];

  // 性能图可选range LAST_5_MINUTE LAST_1_HOUR LAST_1_DAY LAST_1_WEEK LAST_1_MONTH LAST_1_QUARTER HALF_1_YEAR LAST_1_YEAR BEGIN_END_TIME INVALID

  static perRanges = [
    { key: 'LAST_5_MINUTE', value: '最近5分钟' },
    { key: 'LAST_1_HOUR', value: '最近1小时' },
    { key: 'LAST_1_DAY', value: '最近1天' },
    { key: 'LAST_1_WEEK', value: '最近1周' },
    { key: 'LAST_1_MONTH', value: '最近1个月' },
    { key: 'LAST_1_QUARTER', value: '最近1个季度' },
    { key: 'HALF_1_YEAR', value: '最近半年' },
    { key: 'LAST_1_YEAR', value: '最近1年' },
    // {key: 'BEGIN_END_TIME', value: '开始-结束时间'},
    { key: 'INVALID', value: '无效' },
  ];
  constructor(private http: HttpClient) {}

  getData() {
    return this.http.get('accessnfs/listnfs');
  }
  getChartData(fsIds: string[]) {
    return this.http.get('accessnfs/listnfsperformance', { params: { fsIds } });
  }

  updateNfs(params = {}) {
    return this.http.post('operatenfs/updatenfsdatastore', params);
  }

  getVolsByObjId(objId: string) {
    return this.http.get('accessvmfs/volume/' + objId);
  }

  /**
   * 获取折线图
   * @param url
   * @param params
   */
  getLineChartData(url: string, params = {}) {
    return this.http.post(url, params);
  }

  /**
   * 数据集的统计：数据获取
   * @param url
   * @param params
   */
  getDataSetsData(url: string) {
    return this.http.get(url);
  }
}
export interface List {
  name: string; // 名称
  status: string; // 状态
  alarmState: number; // 告警状态
  capacity: number; // 总容量 单位GB
  freeSpace: number; // 空闲容量 单位GB
  reserveCapacity: number; // 置备容量  capacity+uncommitted-freeSpace 单位GB
  deviceId: string; // 存储设备ID
  device: string; // 存储设备名称
  logicPort: string; // 逻辑端口
  logicPortId: string; // 逻辑端口 id
  shareIp: string; // share ip
  sharePath: string; // share path
  share: string; // share 名称
  shareId: string; // share id
  fs: string; // fs
  fsId: string; // fs id
  ops: number; // OPS
  bandwidth: number; // 带宽 单位MB/s
  readResponseTime: number; // 读响应时间 单位ms
  writeResponseTime: number; // 写响应时间 单位ms
  objectid: string; //
  capacityUsage: number;
}

export class UpdateNfs {
  dataStoreObjectId: string;
  nfsName: string; //   DataStoname
  sameName: boolean; // false true 如果是false就传
  shareName: string; //  共享名称
  fsName: string; //  文件系统名称
  fileSystemId: string;
  qosFlag: boolean; // qos策略开关 false true false关闭
  contolPolicy: string; //  上下线选择标记  枚举值 up low
  control_policyUpper: string; // 上限控制flag
  control_policyLower: string; // 下限
  // up 取值如下
  maxBandwidth: string; //
  maxBandwidthChoose: boolean; //
  maxIops: string; //
  maxIopsChoose: boolean; //
  //low取值
  minBandwidth: string; //
  minBandwidthChoose: boolean; //
  minIops: string; //
  minIopsChoose: boolean; //
  latency: string; //
  latencyChoose: boolean; //
  thin: boolean; // true  代表thin false代表thick
  deduplicationEnabled: boolean; // 重删 true false
  compressionEnabled: boolean; // 压缩 true false
  autoSizeEnable: boolean; // 自动扩容 true false
  shareId: string;
  name: string;
  deviceId: string;
  capacity: number;
  constructor() {
    this.sameName = true;
    this.maxBandwidthChoose = false;
    this.maxIopsChoose = false;
    this.latencyChoose = false;
    this.minIopsChoose = false;
    this.minBandwidthChoose = false;
  }
}

export class NfsDetail {
  id: string;
  name: string;
  fileSystemTurning: FileSystemTurning;
  capacityAutonegotiation: CapacityAutonegotiation;
}
export class FileSystemTurning {
  deduplicationEnabled: boolean;
  compressionEnabled: boolean;
  allocationType: string;
  smartQos: SmartQos;
}
export class SmartQos {
  name: string;
  //控制策略,0：保护IO下限，1：控制IO上限
  latency: number;
  maxbandwidth: number;
  maxiops: number;
  minbandwidth: number;
  miniops: number;
  enabled: false;
  //for update
  controlPolicy: string;
  latencyUnit: string;
}
export class CapacityAutonegotiation {
  autoSizeEnable: boolean;
}
export class FileSystem {
  capacity: number;
  name: string;
  allocType: string;
  availableCapacity: number;
  healthStatus: string;
  count: number;
  dtreeCount: number;
  type: string;
  capacityUsageRatio: number;
  storagePoolName: string;
  nfsCount: number;
}
export class Autonegotiation {
  auto_size_enable: boolean;
}
export class Advance {
  deduplication_enabled: boolean;
  compression_enabled: boolean;
  allocation_type: string;
}
export class QosPolicy {
  max_bandwidth: number;
  max_iops: number;
  min_bandwidth: number;
  min_iops: number;
  latency: number;
}
export class Share {
  name: string;
  share_path: string;
}
export class ShareClient {
  name: string;
  accessval: string;
  sync: string;
  all_squash: string;
  root_squash: string;
  secure: string;
  objectId: string;
  hostId: string;
}
export class Vmkernel {
  portgroup: string;
  ipAddress: string;
  device: string;
  key: string;
  mac: string;
}
// =================添加NFS参数 end =========
export class Mount {
  dataStoreName: string;
  dataStoreObjectId: string;
  hosts: string[];
  clusters: string[];
  mountType: string;
}
export class Host {
  hostId: string;
  hostName: string;
  objectId: string;
}
export class Cluster {
  clusterId: string;
  clusterName: string;
}

export class ModifyNfs {
  dataStoreObjectId: string;
  nfsShareName: string;
  nfsName: string;
  file_system_id: string;
  fsName: string;
  shareName: string;
  capacity_autonegotiation = new Autonegotiation();
  name: string;
  tuning = new Advance();
  qos_policy = new QosPolicy();
  nfs_share_id: string;
  sameName = true;
  advance: boolean;
  qosPolicy: boolean;
  upLow: string;
  constructor() {
    this.advance = true;
    this.qosPolicy = true;
  }
}
export class ChartOptions {
  height: number;
  title: Title;
  xAxis: XAxis;
  yAxis: YAxis;
  tooltip: Tooltip;
  legend: Legend;
  color: string[];
  series: Serie[];
}
export class Title {
  text: string;
  subtext: string;
  textStyle: TextStyle;
  textAlign: string;
}
export class TextStyle {
  fontStyle: string;
  fontWeight: string;
}
export class XAxis {
  type: string;
  boundaryGap: boolean;
  data: string[];
}
export class YAxis {
  type: string;
  max: number;
  min: number;
  splitNumber: number;
  boundaryGap: string[];
  axisLine: AxisLine;
  splitLine: SplitLine;
}
export class SplitLine {
  show: boolean;
  lineStyle: LineStyle;
}
export class AxisLine {
  show: boolean;
}
export class Tooltip {
  trigger: string;
  formatter: string;
  axisPointer: AxisPointer;
}
export class AxisPointer {
  type: string;
  axis: string;
  snap: boolean;
}
export class Legend {
  data: LegendData[];
  selectedMode: boolean;
  y: string;
  x: string;
}
export class LegendData {
  name: string;
  icon: string;
}
export class Serie {
  name: string;
  data: SerieData[];
  type: string;
  smooth: boolean;
  itemStyle: ItemStyle;
  label: Label;
}
export class SerieData {
  value: number;
  symbol: string;
}
export class ItemStyle {
  normal: Normal;
}
export class Normal {
  lineStyle: LineStyle;
}
export class LineStyle {
  width: number;
  type: string; // 'dotted'虚线 'solid'实线
  color: string;
}
export class Label {
  show: boolean;
  formatter: string; // 标签的文字。
  constructor() {
    this.show = true;
  }
}
@Injectable()
export class MakePerformance {
  // chart:ChartOptions = new ChartOptions();
  constructor(
    private remoteSrv: NfsService,
    private cdr: ChangeDetectorRef,
    public gs: GlobalsService
  ) {}

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
    paramsInfo: typeTimeInfo,
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
        range: range,
        begin_time: startTime,
        end_time: endTime,
        ...paramsInfo,
      };
      this.remoteSrv.getLineChartData(url, params).subscribe((result: any) => {
        console.log('chartData: ', title, result);
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
          let pmaxData = this.getUpperOrLower(upperData, 'upper');
          // 下限最大值
          let lmaxData = this.getUpperOrLower(lowerData, 'upper');
          // 上限最小值
          let pminData = this.getUpperOrLower(upperData, 'lower');
          // 下限最小值
          let lminData = this.getUpperOrLower(lowerData, 'lower');
          // 上、下限数据
          const uppers: any[] = upperData.series;
          const lower: any[] = lowerData.series;
          // 平均值
          const pavgData = upperData.avg;
          const lavgData = lowerData.avg;
          // 设置X轴
          this.setXAxisData(uppers, chart);
          // 设置y轴最大值
          chart.yAxis.max = pmaxData > lmaxData ? pmaxData : lmaxData;
          console.log('chart.yAxis.pmaxData', pmaxData);
          console.log('chart.yAxis.lmaxData', lmaxData);
          // 设置上限、均值 折线图数据
          uppers.forEach(item => {
            for (const key of Object.keys(item)) {
              const value = Number(Number(item[key]).toFixed(4));
              chart.series[2].data.push({ value: value, symbol: 'none' });
            }
            for (const key of Object.keys(pavgData)) {
              const value = Number(Number(pavgData[key]).toFixed(4));
              chart.series[0].data.push({ value: value, symbol: 'none' });
            }
          });

          // 设置下限、均值 折线图数据
          lower.forEach(item => {
            for (const key of Object.keys(item)) {
              const value = Number(Number(item[key]).toFixed(4));
              chart.series[3].data.push({ value: value, symbol: 'none' });
            }
            for (const key of Object.keys(lavgData)) {
              const value = Number(Number(lavgData[key]).toFixed(4));
              chart.series[1].data.push({ value: value, symbol: 'none' });
            }
          });
        } else {
          console.log('get chartData fail: ', result.description);
        }
        resolve(chart);
      });
    });
  }

  /* vfms upper lower 问题 专用 */
  setChartVmfs(
    paramsInfo: typeTimeInfo,
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

    return new Promise((resolve, reject) => {
      const handlerGetLineChartDataSuccess = (result: any) => {
        console.log('chartData: ', title, result);
        /* 与mock数据匹配 */
        if (isMockData) {
          objIds = ['67c1cf110058934523243f0000000633'];
          /* IOPS ["1125921381744648","1125921381744649"] */
          /* 带宽 ["1125921381744646","1125921381744647"] */
          /* 时延(ms) ["1125921381744656","1125921381744657","1125921381744642"] */

          /* nfs */
          /* OPS ["1126179079716867","1126179079716870"] */
          /* 带宽 ["1126179079716868","1126179079716871"] */
          /* 时延 ["1126179079716869","1126179079716872"] */

          if (indicatorIds[0] === '1126179079716867') {
            indicatorIds = ['1125921381744648', '1125921381744649'];
          }
          if (indicatorIds[0] === '1126179079716868') {
            indicatorIds = ['1125921381744646', '1125921381744647'];
          }
          if (indicatorIds[0] === '1126179079716869') {
            indicatorIds = ['1125921381744656', '1125921381744657', '1125921381744642'];
          }
        }

        if (result.code === '200' && result.data && result.data[objIds[0]]) {
          const resData = result.data;
          let upperData, uppers, pmaxData;
          let lowerData, lower, lmaxData;
          // 上、下限数据

          try {
            // 上限对象
            upperData = resData[objIds[0]][indicatorIds[0]];
            uppers = upperData.series;
            // 上限最大值
            pmaxData = this.getUpperOrLower(upperData, 'upper');
          } catch (error) {}
          try {
            // 下限对象
            lowerData = resData[objIds[0]][indicatorIds[1]];
            lower = lowerData.series;
            // 下限最大值
            lmaxData = this.getUpperOrLower(lowerData, 'upper');
          } catch (error) {}

          const chart: ChartOptions = this.getNewChartWithResponse(
            height,
            title,
            subtext,
            resData,
            uppers,
            lower,
            objIds[0],
            indicatorIds
          );

          // 设置标题
          chart.title.text = title;
          // 设置副标题
          chart.title.subtext = subtext;
          // 平均值
          const pavgData = upperData.avg;
          const lavgData = lowerData.avg;
          // 设置X轴
          this.setXAxisData(uppers, chart);
          // 设置y轴最大值
          chart.yAxis.max = pmaxData > lmaxData ? pmaxData : lmaxData;
          console.log('chart.yAxis.pmaxData', pmaxData);
          console.log('chart.yAxis.lmaxData', lmaxData);
          resolve(chart);
        } else {
          resolve({
            series: [
              {
                data: [],
              },
            ],
          });
          console.log('get chartData fail: ', result.description);
        }
      };

      const params = {
        indicator_ids: indicatorIds,
        obj_ids: objIds,
        range: range,
        begin_time: startTime,
        end_time: endTime,
        ...paramsInfo,
      };

      if (isMockData) {
        if (['1126179079716867', '1125921381744648'].includes(indicatorIds[0])) {
          handlerGetLineChartDataSuccess(RESPONSE_IOPS);
        }
        if (['1126179079716868', '1125921381744646'].includes(indicatorIds[0])) {
          handlerGetLineChartDataSuccess(RESPONSE_BANDWIDTH);
        }
        if (['1126179079716869', '1125921381744656'].includes(indicatorIds[0])) {
          handlerGetLineChartDataSuccess(RESPONSE_LANTENCY);
        }
      } else {
        this.remoteSrv
          .getLineChartData(url, params)
          .subscribe(handlerGetLineChartDataSuccess, handlerResponseErrorSimple);
      }
    });
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
  setChartSingle(
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
    const chart: ChartOptions = this.getNewChartSingle(height, title, subtext);
    return new Promise((resolve, reject) => {
      const params = {
        indicator_ids: indicatorIds,
        obj_ids: objIds,
        range: range,
        begin_time: startTime,
        end_time: endTime,
      };
      this.remoteSrv.getLineChartData(url, params).subscribe((result: any) => {
        console.log('chartData: ', title, result);
        // 设置标题
        chart.title.text = title;
        // 设置副标题
        chart.title.subtext = subtext;
        if (result.code === '200' && result.data && result.data[objIds[0]]) {
          let resData = result.data;
          if (result.data) {
            resData = result.data;
          }
          const seriesData = resData[objIds[0]][indicatorIds[0]].series;
          // 设置X轴
          this.setXAxisData(seriesData, chart);
          seriesData.forEach(item => {
            for (const key of Object.keys(item)) {
              // chartData.value = item[key];
              chart.series[0].data.push({ value: Number(item[key]), symbol: 'none' });
            }
          });
        } else {
          console.log('get chartData fail: ', result.description);
        }
        resolve(chart);
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
    const titleInfo: Title = new Title();
    titleInfo.text = title;
    titleInfo.subtext = subtext;
    titleInfo.textAlign = 'bottom';
    const textStyle: TextStyle = new TextStyle();
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
    tooltip.formatter = '{b} <br/> {a0}: {c0}<br/>{a1}: {c1}<br/>{a2}: {c2}<br/>{a3}: {c3}';
    const axisPointer: AxisPointer = new AxisPointer();
    axisPointer.axis = 'x';
    axisPointer.type = 'line';
    tooltip.axisPointer = axisPointer;
    chart.tooltip = tooltip;

    // 指标
    const legend: Legend = new Legend();
    const legendData: LegendData[] = [];
    legendData.push(this.setLengdData('Upper Limit', 'line'));
    legendData.push(this.setLengdData('Lower Limit', 'line'));
    legendData.push(this.setLengdData('Read', 'circle'));
    legendData.push(this.setLengdData('Write', 'circle'));
    legend.x = 'right';
    legend.y = 'top';
    legend.selectedMode = true;
    legend.data = legendData;

    chart.legend = legend;
    // 指标颜色
    const colors: string[] = ['#DB2000', '#F8E082', '#6870c4', '#01bfa8'];
    chart.color = colors;

    // 数据(格式)
    const series: Serie[] = [];
    series.push(this.setSerieData('Upper Limit', 'line', true, 'dotted', '#DB2000', null));
    series.push(this.setSerieData('Lower Limit', 'line', true, 'dotted', '#F8E082', null));
    series.push(this.setSerieData('Read', 'line', true, 'solid', '#6870c4', null));
    series.push(this.setSerieData('Write', 'line', true, 'solid', '#01bfa8', null));

    chart.series = series;

    return chart;
  }

  getNewChartWithResponse(
    height: number,
    title: string,
    subtext: string,
    res,
    uppers,
    lower,
    objId,
    indicatorIds
  ) {
    const chart: ChartOptions = new ChartOptions();
    // 高度
    chart.height = height;
    // 标题
    const titleInfo: Title = new Title();
    titleInfo.text = title;
    titleInfo.subtext = subtext;
    titleInfo.textAlign = 'bottom';
    const textStyle: TextStyle = new TextStyle();
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
    const axisPointer: AxisPointer = new AxisPointer();
    axisPointer.axis = 'x';
    axisPointer.type = 'line';
    tooltip.axisPointer = axisPointer;

    // 指标
    const legend: Legend = new Legend();
    const legendData: LegendData[] = [];
    // 数据(格式)
    const series: Serie[] = [];

    const isNfsChartOpsBand = (responseData => {
      for (const key of Object.keys(responseData)) {
        if ([NfsService.nfsOPS[0], NfsService.nfsBDWT[0]].includes(key)) {
          return true;
        }
      }
      return false;
    })(res[objId]);

    if (isNfsChartOpsBand) {
      (() => {
        legendData.push(this.setLengdData('Avg', 'circle'));
        const series_avg = this.setSerieData('Avg', 'line', true, 'solid', '#6870c4', null);
        series_avg.data = res[objId][indicatorIds[0]].series.map(i => {
          const value = helper.valueFormObj(i);
          return {
            value: Number(Number(value).toFixed(4)),
            symbol: 'none',
          };
        });
        series.push(series_avg);
      })();
    } else {
      /*  */
      // 设置上限、均值 折线图数据 upper read
      // 设置下限、均值 折线图数据 lower write

      const target_upper = res[objId].upper;
      if (is.string(target_upper) && target_upper.length > 0 && target_upper !== 'null') {
        legendData.push(this.setLengdData('Upper Limit', 'line'));
        const series_upper = this.setSerieData(
          'Upper Limit',
          'line',
          true,
          'dotted',
          '#DB2000',
          null
        );
        series_upper.data = uppers.map(i => ({
          value: Number(Number(target_upper).toFixed(4)),
          symbol: 'none',
        }));
        series.push(series_upper);
      }

      const target_lower = res[objId].lower;
      if (is.string(target_lower) && target_lower.length > 0 && target_lower !== 'null') {
        legendData.push(this.setLengdData('Lower Limit', 'line'));
        const series_lower = this.setSerieData(
          'Lower Limit',
          'line',
          true,
          'dotted',
          '#F8E082',
          null
        );
        series_lower.data = lower.map(i => ({
          value: Number(Number(target_lower).toFixed(4)),
          symbol: 'none',
        }));
        series.push(series_lower);
      }

      (() => {
        legendData.push(this.setLengdData('Read', 'circle'));
        const series_read = this.setSerieData('Read', 'line', true, 'solid', '#6870c4', null);
        series_read.data = uppers.map(i => {
          const value = helper.valueFormObj(i);
          return {
            value: Number(Number(value).toFixed(4)),
            symbol: 'none',
          };
        });
        series.push(series_read);
      })();

      (() => {
        legendData.push(this.setLengdData('Write', 'circle'));
        const series_write = this.setSerieData('Write', 'line', true, 'solid', '#01bfa8', null);
        series_write.data = lower.map(i => {
          const value = helper.valueFormObj(i);
          return {
            value: Number(Number(value).toFixed(4)),
            symbol: 'none',
          };
        });
        series.push(series_write);
      })();
    }
    /*  */

    legend.x = 'right';
    legend.y = 'top';
    legend.selectedMode = true;
    legend.data = legendData;

    chart.legend = legend;
    // 指标颜色
    const colors: string[] = ['#DB2000', '#F8E082', '#6870c4', '#01bfa8'];
    chart.color = colors;
    chart.series = series;
    const formatterArray = ['{b} ', '{a0}: {c0}', '{a1}: {c1}', '{a2}: {c2}', '{a3}: {c3}'];
    formatterArray.splice(series.length + 1);
    tooltip.formatter = formatterArray.join('<br/>');
    chart.tooltip = tooltip;
    return chart;
  }

  getNewChartSingle(height: number, title: string, subtext: string) {
    const chart: ChartOptions = new ChartOptions();
    // 高度
    chart.height = height;
    // 标题
    const titleInfo: Title = new Title();
    titleInfo.text = title;
    titleInfo.subtext = subtext;
    titleInfo.textAlign = 'bottom';
    const textStyle: TextStyle = new TextStyle();
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
    tooltip.formatter = '{b} <br/> {a0}: {c0}';
    const axisPointer: AxisPointer = new AxisPointer();
    axisPointer.axis = 'x';
    axisPointer.type = 'line';
    tooltip.axisPointer = axisPointer;
    chart.tooltip = tooltip;

    // 指标
    // const legend: Legend = new Legend();
    // const legendData: LegendData[] = [];
    // legendData.push(this.setLengdData('Upper Limit', 'line'));
    // legendData.push(this.setLengdData('Lower Limit', 'line'));
    // legend.x = 'right';
    // legend.y = 'top';
    // legend.selectedMode  = true;
    // legend.data = legendData;

    //chart.legend = legend;
    // 指标颜色
    const colors: string[] = ['#DB2000'];
    chart.color = colors;

    // 数据(格式)
    const series: Serie[] = [];
    series.push(this.setSerieData('卷最大I/O相应时间', 'line', true, 'dotted', '#DB2000', null));
    //series.push(this.setSerieData('Lower Limit', 'line', true, 'dotted', '#F8E082', null))

    chart.series = series;

    return chart;
  }

  /**
   * set SerieData
   * @param name
   * @param type
   * @param smooth
   * @param lineType 线类型'dotted'虚线 'solid'实线
   * @param lineColor 线颜色
   */
  setSerieData(
    name: string,
    type: string,
    smooth: boolean,
    lineType: string,
    lineColor: string,
    labelFormatter: string
  ) {
    const serie: Serie = new Serie();
    serie.name = name;
    serie.type = type;
    const serieData: SerieData[] = [];
    serie.data = serieData;
    serie.smooth = smooth;

    const itemStyle: ItemStyle = new ItemStyle();
    const normal: Normal = new Normal();
    const lineStyle: LineStyle = new LineStyle();
    lineStyle.color = lineColor;
    lineStyle.type = lineType;
    lineStyle.width = 2;
    normal.lineStyle = lineStyle;
    itemStyle.normal = normal;
    serie.itemStyle = itemStyle;

    const lable: Label = new Label();
    lable.formatter = labelFormatter;
    lable.show = true;
    serie.label = lable;

    return serie;
  }
  /**
   * set legendData
   * @param name
   * @param icon
   */
  setLengdData(name: string, icon: string) {
    const legendData: LegendData = new LegendData();
    legendData.name = name;
    if (null !== icon) {
      legendData.icon = icon;
    }
    return legendData;
  }

  /**
   * 设置x轴
   * @param data
   * @param chart
   */
  setXAxisData(data: any[], chart: ChartOptions) {
    console.log('setXAxisData', data);
    data.forEach(item => {
      for (const key of Object.keys(item)) {
        let numKey = +key;
        const date = new Date(numKey);
        /*         
        let minutes: number | string = date.getMinutes();
        minutes = minutes < 10 ? `0${minutes}` : minutes;
        const dateStr = `${date.getFullYear()}-${(date.getMonth() + 1)}-${date.getDate()} ${date.getHours() } : ${minutes}`;
 */
        chart.xAxis.data.push(transDateFormat(date));
      }
    });
  }

  /**
   * 获取最大/小值
   * @param data
   * @param type upper 最大 lower最小
   */
  getUpperOrLower(data: any, type: string) {
    console.log('data', data);
    let result;
    if (type === 'lower') {
      for (const key of Object.keys(data.min)) {
        result = Number(data.min[key]);
      }
    } else {
      for (const key of Object.keys(data.max)) {
        result = Number(data.max[key]);
      }
    }
    return Number(result.toFixed(4));
  }

  /**
   * 通过objectId 获取卷信息并设置默认选中卷
   * @param objectId
   * @param volumeInfoList
   * @param volNames
   * @param selectVolName
   * @param selectVolume
   */
  getVolsByObjId(
    objectId: string,
    volumeInfoList: VolumeInfo[],
    volNames: string[],
    selectVolName: string,
    selectVolume: VolumeInfo
  ) {
    console.log('objectId: ' + objectId);
    this.remoteSrv.getVolsByObjId(objectId).subscribe((result: any) => {
      console.log(result);
      if (result.code === '200') {
        volumeInfoList = result.data;
        const volName: string[] = [];
        volumeInfoList.forEach(item => {
          volName.push(item.name);
        });
        volNames = volName;
        // 设置默认选中数据
        selectVolName = volNames[0];
        selectVolume = this.getVolByName(selectVolName, volumeInfoList);
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      } else {
        console.log(result.description);
      }
    });
  }
  // 通过名称获取卷信息
  getVolByName(name: string, volumeInfoList: VolumeInfo[]) {
    const volumeInfo = volumeInfoList.filter(item => item.name === name)[0];
    return volumeInfo;
  }

  /**
   * 数据集的统计: 设置折线图
   * @param height
   * @param title
   * @param subtext
   * @param range
   * @param url
   * @param serviceLevelId
   * @param chartType 类型 可选
   * @param dataNames 指标名称
   * @param dataValues 指标变量名 可选
   */
  setDataSetsChart(
    height: number,
    title: string,
    subtext: string,
    range: string,
    url: string,
    serviceLevelId: string,
    chartType: string,
    dataNames: string[],
    dataValues: string[]
  ) {
    // 生成chart optiond对象
    const chart: ChartOptions = this.getDataSetsCharts(height, title, subtext, dataNames);
    // 查询数据并设置echart
    return new Promise((resolve, reject) => {
      url += '?serviceLevelId=' + serviceLevelId + '&interval=' + range;
      this.remoteSrv.getDataSetsData(url).subscribe((result: any) => {
        console.log('chartData: ', title, result);
        // 设置标题
        chart.title.text = title;
        // 设置副标题
        chart.title.subtext = subtext;
        if (result.code == '200' && result.data) {
          let resData = result.data;
          const seriesData = resData.datas;
          // 设置X轴
          this.setDataSetsXAxis(seriesData, chart);
          // 设置数据
          this.setDataSetsDatas(seriesData, chart, chartType, dataValues);
        } else {
          console.log('get chartData fail: ', result.description);
        }
        resolve(chart);
      });
    });
  }

  /**
   * 数据集的统计: 设置折线图X轴坐标
   * @param data
   * @param chart
   */
  setDataSetsXAxis(data: any[], chart: ChartOptions) {
    data.forEach(item => {
      const timestamp = item.timestamp;
      const date = new Date(timestamp);
      /* const dateStr = date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() + ' ' + date.getHours() + ':' + date.getMinutes(); */
      const dateStr = transDateFormat(date);
      chart.xAxis.data.push(dateStr);
    });
  }

  /**
   * 数据集的统计: 数据格式化
   * @param data
   * @param chart
   * @param type 类型 statLun：LUN容量趋势、statStoragePool：存储池容量趋势、
   * responseTime：LUN最大I/O响应时间（ms）、perfDensity：LUN I/O密度（IOPS/TB）、
   * perfLUNIOPS：LUN总吞吐量（IOPS）、perfBandwidth：LUN总带宽（MB/s）、
   * perfStoragePoolDetails：存储池I/O密度(IOPS/TB)
   * @param dataValues
   */
  setDataSetsDatas(data: any[], chart: ChartOptions, type, dataValues: string[]) {
    data.forEach(item => {
      if (type) {
        switch (type) {
          case 'statStoragePool': {
            // 存储池容量趋势
            const usedCapacity = item.usedCapacity;
            const totalCapacity = item.totalCapacity;
            chart.series[0].data.push({ value: Number(usedCapacity.toFixed(3)), symbol: 'none' });
            chart.series[1].data.push({ value: Number(totalCapacity.toFixed(3)), symbol: 'none' });
            break;
          }
          case 'responseTime': {
            // LUN最大I/O响应时间（ms）
            const responseTime = item.responseTime;
            chart.series[0].data.push({ value: Number(responseTime.toFixed(3)), symbol: 'none' });
            break;
          }
          case 'perfDensity': {
            // LUN I/O密度（IOPS/TB）
            let density;
            if (item.totalCapacity != '0') {
              density = item.throughput / item.totalCapacity;
            } else {
              density = 0;
            }
            chart.series[0].data.push({ value: Number(density.toFixed(3)), symbol: 'none' });
            break;
          }
          case 'perfLUNIOPS': {
            // LUN总吞吐量（IOPS）
            const throughput = item.throughput;
            chart.series[0].data.push({ value: Number(throughput.toFixed(3)), symbol: 'none' });
            break;
          }
          case 'perfBandwidth': {
            // LUN总带宽（MB/s）
            const bandwidth = item.bandwidth;
            chart.series[0].data.push({ value: Number(bandwidth.toFixed(3)), symbol: 'none' });
            break;
          }
          case 'perfStoragePoolDetails': {
            // 存储池I/O密度(IOPS/TB)
            let density;
            if (item.totalCapacity != '0') {
              density = item.throughput / item.totalCapacity;
            } else {
              density = 0;
            }
            chart.series[0].data.push({ value: Number(density.toFixed(3)), symbol: 'none' });
            break;
          }
          default: // LUN容量趋势
          {
            const allocCapacity = item.allocCapacity;
            const totalCapacity = item.totalCapacity;
            chart.series[0].data.push({ value: Number(allocCapacity.toFixed(3)), symbol: 'none' });
            chart.series[1].data.push({ value: Number(totalCapacity.toFixed(3)), symbol: 'none' });
            break;
          }
        }
      } else {
        dataValues.forEach(value => {
          const dataValue = item[value];
          chart.series[dataValues.indexOf(value)].data.push({
            value: Number(dataValue.toFixed(3)),
            symbol: 'none',
          });
        });
      }
    });
  }

  /**
   * 数据集的统计: 生成chart并返回
   * @param height
   * @param title
   * @param subtext
   */
  getDataSetsCharts(height, title, subtext, dataNames: string[]) {
    const chart: ChartOptions = new ChartOptions();
    // 高度
    chart.height = height;
    // 标题
    const titleInfo: Title = new Title();
    titleInfo.text = title;
    titleInfo.subtext = subtext;
    titleInfo.textAlign = 'bottom';
    const textStyle: TextStyle = new TextStyle();
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

    let toolTips = '{b}';
    for (let i = 0; i < dataNames.length; i++) {
      toolTips = toolTips + '<br/>{a' + i + '}: {c' + i + '}';
    }
    tooltip.formatter = toolTips;
    const axisPointer: AxisPointer = new AxisPointer();
    axisPointer.axis = 'x';
    axisPointer.type = 'line';
    tooltip.axisPointer = axisPointer;
    chart.tooltip = tooltip;

    // 指标
    const legend: Legend = new Legend();
    const legendData: LegendData[] = [];

    // 指标颜色
    const colors: string[] = [
      '#DB2000',
      '#F8E082',
      '#6870c4',
      '#01bfa8',
      '#2f4554',
      '#61a0a8',
      '#d48265',
      '#91c7ae',
      '#749f83',
      '#ca8622',
      '#bda29a',
      '#6e7074',
      '#546570',
      '#c4ccd3',
    ];
    chart.color = colors;

    // 数据(格式)
    const series: Serie[] = [];
    legend.x = 'right';
    legend.y = 'top';
    dataNames.forEach(item => {
      legendData.push(this.setLengdData(item, 'line'));
      series.push(
        this.setSerieData(item, 'line', true, 'dotted', colors[dataNames.indexOf(item)], null)
      );
    });
    legend.selectedMode = true;
    legend.data = legendData;

    chart.series = series;

    return chart;
  }
}
