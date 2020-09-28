import {ChangeDetectorRef, Injectable} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {GlobalsService} from "@shared/globals.service";

@Injectable()
export class NfsService {

  // 性能数据指标： indicator_ids 获取参数指标（读写）
  // VMFS IOPS 0Read 1Write
  static vmfsIOPS: Array<string> = ['1125921381744648', '1125921381744649'];
  // VMFS  bandwidth 0Read 1Write
  static vmfsBDWT: Array<string> = ['1125921381744646', '1125921381744647'];
  // VMFS  latency 0Read 1Write
  static vmfsLatency: Array<string> = ['1125921381744656', '1125921381744657'];
  // VMFS  url
  static vmfsUrl = 'datastorestatistichistrory/vmfs';

  // NFS IOPS 0Read 1Write
  static nfsIOPS: Array<string> = ['1125921381744648', '1125921381744649'];
  // NFS  bandwidth 0Read 1Write
  static nfsBDWT: Array<string> = ['1125921381744646', '1125921381744647'];
  // NFS  latency 0Read 1Write
  static nfsLatency: Array<string> = ['1125921381744656', '1125921381744657'];
  // NFS  url
  static nfsUrl = 'datastorestatistichistrory/nfs';
  constructor(private http: HttpClient) {}

  getData() {
    return this.http.get('accessnfs/listnfs', );
  }
  getChartData(fsIds: string[] ) {
    return this.http.get('accessnfs/listnfsperformance', {params: {fsIds}});
  }
  addNfs(params= {}){
    return this.http.post('operatenfs/createnfsdatastore', params);
  }
  updateNfs(params= {}){
    return this.http.post('operatenfs/updatenfsdatastore', params);
  }
  mountNfs(params= {}){
    return this.http.post('accessnfs/mountnfs', params);
  }
  delNfs(param: string){
    return this.http.post('accessnfs/delnfs', param);
  }
  getHostListByObjectId(dataStoreObjectId: string){
    return this.http.get('accessvmware/gethostsbydsobjectid', {params: {dataStoreObjectId}});
  }
  getHostList(){
    return this.http.get('accessvmware/listhost');
  }
  getClusterListByObjectId(dataStoreObjectId: string){
    return this.http.get('accessvmware/getclustersbydsobjectid', {params: {dataStoreObjectId}});
  }

  /**
   * 获取折线图
   * @param url
   * @param params
   */
  getLineChartData(url:string, params = {}) {
    return this.http.post(url, params);
  }
}
export interface List {
   name: string;    // 名称
   status: string;  // 状态
  capacity: number;  // 总容量 单位GB
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
  bandwidth: number;   // 带宽 单位MB/s
  readResponseTime: number;   // 读响应时间 单位ms
  writeResponseTime: number; // 写响应时间 单位ms
  objectid: string; //
}
// =================添加NFS参数 start=========
export class AddNfs{
  storage_pool_id: string;
  current_port_id: string;
  storage_id: string;
  pool_raw_id: string;
  exportPath: string;
  nfsName: string;
  accessMode: string;
  type: string;
  sameName: boolean;
  filesystem_specs: FileSystem[];
  capacity_autonegotiation: Autonegotiation;
  tuning: Advance;
  qos_policy: QosPolicy;
  create_nfs_share_param: Share;
  nfs_share_client_addition: ShareClient[];
  fsName: string;
  shareName: string;
  size: number;
  unit: string;
  qosPolicy: boolean;
  upLow: string;
  thin: boolean;
  auto: boolean;
  constructor(){
    this.sameName = true;
    this.qosPolicy = false;
    this.thin = true;
    this.auto = false;
    this.unit = 'GB';
    this.accessMode = 'readWrite';
    this.tuning = new Advance();
    this.tuning.deduplication_enabled = true;
    this.tuning.compression_enabled = false;
    this.capacity_autonegotiation = new Autonegotiation();
    this.capacity_autonegotiation.auto_size_enable = false;
    this.qos_policy = new QosPolicy();
    this.filesystem_specs = [];
    this.nfs_share_client_addition = [];
  }
}
export class FileSystem{
  capacity: number;
  name: string;
  count: number;
}
export class Autonegotiation{
  capacity_self_adjusting_mode: string;
  capacity_recycle_mode: string;
  auto_size_enable: boolean;
  auto_grow_threshold_percent: number;
  auto_shrink_threshold_percent: number;
  max_auto_size: number;
  min_auto_size: number;
  auto_size_increment: number;
}
export class Advance{
  deduplication_enabled: boolean;
  compression_enabled: boolean;
  application_scenario: string;
  block_size: number;
  allocation_type: string;
}
export class QosPolicy{
  max_bandwidth: number;
  max_iops: number;
  min_bandwidth: number;
  min_iops: number;
  latency: number;
}
export class Share{
  name: string;
  share_path: string;
}
export class ShareClient{
  name: string;
  accessval: string;
  sync: string;
  all_squash: string;
  root_squash: string;
}

// =================添加NFS参数 end =========
export class Mount{
  dataStoreName: string;
  dataStoreObjectId: string;
  hosts: string[];
  clusters: string[];
  mountType: string;
}
export class Host{
  hostId: string;
  hostName: string;
  vkernelIP: string;
}
export class Cluster{
  clusterId: string;
  clusterName: string;
}

export class ModifyNfs{
  dataStoreObjectId: string;
  nfsShareName: string;
  nfsName: string;
  file_system_id: string;
  fsName: string;
  shareName: string;
  capacity_autonegotiation = new Autonegotiation();
  name: string;
  tuning = new Advance();
  qos_policy = new  QosPolicy();
  nfs_share_id: string;
  sameName = true;
  advance: boolean;
  qosPolicy: boolean;
  upLow: string;
  constructor(){
    this.advance = true;
    this.qosPolicy = true;
  }
}
export class ChartOptions{
  height: number;
  title: Title;
  xAxis: XAxis;
  yAxis: YAxis;
  tooltip: Tooltip;
  legend: Legend;
  series: Serie[];

}
export class Title{
  text: string;
  subtext: string;
  textStyle: TextStyle;
  textAlign: string;
}
export class TextStyle{
  fontStyle: string;
}
export class XAxis{
  type: string;
  boundaryGap: boolean;
  data: string[];
}
export class YAxis{
  type: string;
  max: number;
  min: number;
  splitNumber: number;
  boundaryGap: string[];
  axisLine: AxisLine;
}
export class AxisLine{
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
  snap: boolean
}
export class Legend{
  data: LegendData[];
  selectedMode: boolean;
  y: string;
  x: string;
}
export class LegendData{
  name: string;
  icon: string;
}
export class Serie{
  name: string;
  data: SerieData[];
  type: string;
  smooth: boolean;
  itemStyle: ItemStyle;
  label: Label;
}
export class SerieData{
  value: number;
  symbol: string;
}
export class ItemStyle{
  normal: Normal;
}
export class Normal {
  lineStyle: LineStyle;
}
export class LineStyle{
  width: number;
  type: string; // 'dotted'虚线 'solid'实线
  color: string;
}
export class Label{
  show: boolean;
  formatter: string; // 标签的文字。
  constructor(){
    this.show = true;
  }
}
@Injectable()
export class MakePerformance {
  // chart:ChartOptions = new ChartOptions();
  constructor(private remoteSrv: NfsService, private cdr: ChangeDetectorRef, public gs: GlobalsService) {}

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
  setChart(height: number, title: string, subtext: string, indicatorIds: any[], objIds: any[], range: string, url: string) {
    // 生成chart optiond对象
    const chart:ChartOptions = this.getNewChart(height, title, subtext);
    return new Promise((resolve, reject) => {
      const params = {
        indicator_ids: indicatorIds, // 指标
        obj_ids: objIds, // 指标
        range: range, // 指标
      }
      this.remoteSrv.getLineChartData(url, params).subscribe((result: any) => {
        console.log('chartData: ', result);
        if (result.code === '200' && result.data !== null && result.data.data !== null) {
          const resData = result.data.data;
            // 设置标题
          chart.title.text = title;
            // 设置副标题
          chart.title.subtext = subtext;
          // 上限对象
          const upperData = resData[objIds[0]][indicatorIds[0]];
          // 下限对象
          const lowerData = resData[objIds[0]][indicatorIds[1]];
          // 上限最大值
          const pmaxData = this.getUpperOrLower(upperData, 'upper');
          // 下限最大值
          let lmaxData = this.getUpperOrLower(lowerData, 'lower');
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
          chart.yAxis.max = (pmaxData > lmaxData ? pmaxData : lmaxData) + 2;
          console.log('chart.yAxis.pmaxData', pmaxData);
          console.log('chart.yAxis.lmaxData', lmaxData);
          // 设置上限、均值 折线图数据
          uppers.forEach(item => {
            for (const key of Object.keys(item)) {
              // chartData.value = item[key];
              chart.series[2].data.push({value: Number(item[key]), symbol: 'none'});
            }
            for (const key of Object.keys(pavgData)) {
              chart.series[0].data.push({value:  Number(pavgData[key]), symbol: 'none'});
            }
          });
          // 设置下限、均值 折线图数据
          lower.forEach(item => {
            for (const key of Object.keys(item)) {
              chart.series[3].data.push({value: Number(item[key]), symbol: 'none'});
            }
            for (const key of Object.keys(lavgData)) {
              chart.series[1].data.push({value: Number(lavgData[key]), symbol: 'none'});
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

    // 提示数据
    const legend: Legend = new Legend();
    const legendData: LegendData[] = [];
    legendData.push(this.setLengdData('Upper Limit', null));
    legendData.push(this.setLengdData('Lower Limit', null));
    legendData.push(this.setLengdData('Read', 'triangle'));
    legendData.push(this.setLengdData('Write', 'triangle'));
    legend.x = 'right';
    legend.y = 'top';
    legend.selectedMode  = true;
    legend.data = legendData;

    chart.legend = legend;

    // 数据(格式)
    const series: Serie[] = [];
    series.push(this.setSerieData('Upper Limit', 'line', true, 'dotted', '#DB2000', null))
    series.push(this.setSerieData('Lower Limit', 'line', true, 'dotted', '#F8E082', null))
    series.push(this.setSerieData('Read', 'line', true, 'solid', '#6870c4', null))
    series.push(this.setSerieData('Write', 'line', true, 'solid', '#01bfa8', null))

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
  setSerieData(name: string, type:string, smooth:boolean, lineType:string, lineColor:string, labelFormatter: string) {
    const serie:Serie = new Serie();
    serie.name = name;
    serie.type = type;
    const serieData: SerieData[] = [];
    serie.data = serieData;
    serie.smooth = smooth;

    const itemStyle: ItemStyle = new ItemStyle();
    const normal:Normal = new Normal();
    const lineStyle: LineStyle = new LineStyle();
    lineStyle.color = lineColor;
    lineStyle.type = lineType;
    lineStyle.width = 2;
    normal.lineStyle = lineStyle;
    itemStyle.normal = normal;
    serie.itemStyle = itemStyle;

    const lable:Label = new Label();
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
  setXAxisData(data: any[], chart:ChartOptions) {
    console.log('data', data);
    data.forEach(item => {
      for (const key of Object.keys(item)) {
        let numKey = + key;
        const date = new Date(numKey);
        console.log('date', date);
        console.log('key', key);
        const dateStr = date.getFullYear() + '-' + date.getMonth() + '-' + date.getDay() + ' ' + date.getHours() + ':' + date.getMinutes();
        console.log('dateStr', dateStr);
        chart.xAxis.data.push(dateStr);
      }
    });
  }

  /**
   * 获取最大/小值
   * @param data
   * @param type upper 最大 lower最小
   */
  getUpperOrLower(data: any, type: string) {
    let result;
    if (type === 'lower') {
      for (const key of Object.keys(data.max)) {
        result = Number(data.max[key]);
      }
    } else {
      for (const key of Object.keys(data.max)) {
        result = Number(data.min[key]);
      }
    }
    return result;
  }
}
