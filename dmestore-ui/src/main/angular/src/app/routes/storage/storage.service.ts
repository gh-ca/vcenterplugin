import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class StorageService {
  charts = [
    {
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
        //subtext: '234'
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
    },
    {
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
        //subtext: '234'
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
    },
    {
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
          fontSize: 18,
          color: '#63B3F7'
        },
        subtextStyle: {
          fontSize: 12,
          color: '#c2c6dc',
          align: 'center'
        },
        left: '50%',
        top: '50%',
      },

      series: [
        {
          name: '',
          type: 'pie',
          radius: ['60%', '70%'],
          center: ['50%', '50%'],
          avoidLabelOverlap: false,
          label: {
            show: false,
            position: 'center'
          },
          emphasis: {
            label: {
              show: false,
              fontSize: '20',
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
          data: [
            {value: 335, name: '直接访问'},
            {value: 310, name: '邮件营销'}
          ]
        }
      ],
      color: ['hsl(198, 100%, 32%)', 'hsl(198, 0%, 80%)']
    },
    {
      xAxis: {
        show: false,
        type: 'category'
      },
      yAxis: {
        show: false,
        type: 'value'
      },
      series: [{
        data: [120, 200, 150, 80, 70, 110, 130, 120, 200, 150, 80, 70, 110, 130, 120, 200, 150, 80, 70, 110, 130, 120, 200, 150, 80, 70, 110, 130, 120, 200, 150, 80, 70, 110, 130, 120, 200, 150, 80, 70, 110, 130],
        type: 'bar',
        barCategoryGap: 0
      }],
      color: ['hsl(198, 100%, 32%)']
    }

  ];
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

  getCharts() {
    return this.charts;
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
