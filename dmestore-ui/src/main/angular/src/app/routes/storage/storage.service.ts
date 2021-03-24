import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {StorageTypeShow} from "../vmfs/list/list.service";
import {TranslatePipe} from "@ngx-translate/core";

@Injectable()
export class StorageService {
  constructor(private http: HttpClient) {}

  getData(){
    return this.http.get('dmestorage/storages');
  }
  getStoragePoolListByStorageId(mediaType:string,storageId:string){
    return this.http.get('dmestorage/storagepools', {params: {storageId,mediaType}});
  }
  getLogicPortListByStorageId(storageId: string){
    return this.http.get('dmestorage/logicports', {params: {storageId}});
  }
  listperformance(storageIds:string[]) {
    return this.http.get('dmestorage/liststorageperformance', {params: {storageIds}});
  }
}
export interface StorageList {
   id: string;
   name: string;
   ip: string;
   status: string;
   synStatus: string;
   vendor: string;
   model: string;
   version: string;
   productVersion: string;
   usedCapacity: number;
   totalCapacity: number;
   freeCap: number;
   totalEffectiveCapacity: number;
   freeEffectiveCapacity: number;
   maxCpuUtilization: number;
   maxIops: number;
   maxBandwidth: number;
   maxLatency: number;
   maxOps:number;
   azIds: string[];
   totalPoolCapacity: number;
   subscriptionCapacity: number;
   maintenanceStart: string;
   maintenanceOvertime: string;
   location: string;
   patchVersion: string;
   storageTypeShow:StorageTypeShow;
}
export class LogicPort{
  id: string;
  name: string;
  runningStatus: string;
  operationalStatus: string;
  mgmtIp: string;
  mgmtIpv6: string;
  homePortId: string;
  homePortName: string;
  currentPortId: string;
  currentPortName: string;
  role: string;
  ddnsStatus: string;
  supportProtocol: string;
  managementAccess: string;
  vstoreId: string;
  vstoreName: string;
}
export class StorageChart{
  id: string;
  name: string;
  ip: string;
  model: string; // 5300 V5(OceanStor)
  capacity: string;// 总容量
  usedCapacity: string;// 已使用容量
  freeCapacity: string;// 空闲容量
  alarms: number;// 告警数
  events: number;// 事件数
  chart: CapacityChart;
  iops: PerforChart;
  bandwidth: PerforChart;
}
export class CapacityChart{
  tooltip: any;
  title: any;
  series: CapacitySerie[] =[];
  constructor(title: string){
    this.tooltip = {trigger: 'item', formatter: ' {b}: {d}%'};
    this.title = {
      text: title,
      textAlign: 'center',
      padding: 0,
      textVerticalAlign: 'middle',
      textStyle: {
        fontSize: 15,
        color: '#6C92FA'
      },
      subtextStyle: {
        fontSize: 10,
        color: '#c2c6dc',
        align: 'center'
      },
      left: '50%',
      top: '50%',
      //subtext: '234'
    };

  }
}
export class CapacitySerie{
  name:string;
  type: string;
  radius: string[];
  center: any;
  avoidLabelOverlap: boolean;
  label: any;
  emphasis: any;
  labelLine: any;
  color: any;
  data: D[]=[];
  constructor(protection:number,fs:number,volume:number,free:number, blockFile:number,isDorado:boolean, translatePipe:TranslatePipe){
    this.name= "";
    this.type="pie";
    this.radius=['60%', '70%'];
    this.center=['50%', '50%'];
    this.avoidLabelOverlap=false;
    this.label = {
      show: false,
      position: 'center'
    };
    this.emphasis = {
      label: {
        show: false,
        fontSize: '20',
        fontWeight: 'bold'
      }
    };
    // 保护
    const p = new D();
    p.value=protection;
    p.name=translatePipe.transform("storage.chart.protection");
    this.data.push(p);

    if (isDorado) {
      this.color=['hsl(164,58%, 52%)', 'hsl(48, 77%, 55%)', 'hsl(0, 0%, 90%)'];
      // 块/文件
      const bf = new D();
      bf.value = blockFile;
      bf.name = translatePipe.transform('storage.chart.blockFile');
      this.data.push(bf);
    } else {
      this.color=['hsl(164,58%, 52%)', 'hsl(48, 77%, 55%)', 'hsl(224, 93%, 70%)', 'hsl(0, 0%, 90%)'];
      // 文件系统
      const f= new D();
      f.value=fs;
      f.name = translatePipe.transform('storage.chart.file');
      this.data.push(f);
      // LUN
      const v= new D();
      v.value=volume;
      v.name = translatePipe.transform('storage.chart.volume');
      this.data.push(v);
    }
    this.labelLine = {
      show: false
    };
    // 空闲
    const fr= new D();
    fr.value=free;
    fr.name = translatePipe.transform('storage.chart.free');
    this.data.push(fr);

  }
}
export class D{
  value: number;
  name: string;
}

export class PerforChart{
  xAxis: any;
  yAxis: any;
  series: any;
  color: any;
  constructor(d: number[]){
    this.xAxis={
      show: true,
      type: 'category'
    };
    this.yAxis={
      show: false,
      type: 'value'
    };
    this.color=['hsl(198, 100%, 32%)'];
    this.series=[
      {
        data: d,
        type: 'line',
        barCategoryGap: 0
      }];
  }
}
