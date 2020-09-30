import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class StorageService {
  constructor(private http: HttpClient) {}

  getData(){
    return this.http.get('dmestorage/storages');
  }
  getStoragePoolListByStorageId(storageId: string){
    return this.http.get('dmestorage/storagepools', {params: {storageId}});
  }
  getLogicPortListByStorageId(storageId: string){
    return this.http.get('dmestorage/logicports', {params: {storageId}});
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
   totalEffectiveCapacity: number;
   freeEffectiveCapacity: number;
   maxCpuUtilization: number;
   maxIops: number;
   maxBandwidth: number;
   maxLatency: number;
   azIds: string[];
  total_pool_capacity: number;
  subscription_capacity: number;
}
export class LogicPort{
  id: string;
  name: string;
  running_status: string;
  operational_status: string;
  mgmt_ip: string;
  mgmt_ipv6: string;
  home_port_id: string;
  home_port_name: string;
  current_port_id: string;
  current_port_name: string;
  role: string;
  ddns_status: string;
  support_protocol: string;
  management_access: string;
  vstore_id: string;
  vstore_name: string;
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
    this.tooltip = {trigger: 'item', formatter: ' {b}: {c} ({d}%)'};
    this.title = {
      text: title,
      textAlign: 'center',
      padding: 0,
      textVerticalAlign: 'middle',
      textStyle: {
        fontSize: 15,
        color: '#63B3F7'
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
  constructor(use: number,free:number){
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
    this.color=['hsl(198, 100%, 32%)', 'hsl(198, 0%, 80%)'];
    this.labelLine = {
      show: false
    };
    const u = new D();
    u.value=use;
    u.name="已使用";
    this.data.push(u);
    const f= new D();
    f.value=free;
    f.name="空闲";
    this.data.push(f);
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
