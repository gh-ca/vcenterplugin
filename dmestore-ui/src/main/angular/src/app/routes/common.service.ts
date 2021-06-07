import { Injectable } from '@angular/core';
import { ClrDatagridStateInterface } from '@clr/angular';
import * as moment from 'moment';
import { TranslatePipe } from '@ngx-translate/core';
import { isMockData } from 'mock/mock';
import { vmfsClusterTreeData } from './../../mock/vmfsClusterTree';
import { HttpClient } from '@angular/common/http';
import { getLodash } from '@shared/lib';
import { mockServerData } from './../app.helpers';
import {rejects} from "assert";
const _ = getLodash();

function getSelectedDateFn(label) {
  return (minuteName, exp) => {
    const time = exp.exec(label);
    const count = Number(time[1]);
    return [moment().subtract(count, minuteName).valueOf(), moment().valueOf()];
  };
}

@Injectable()
export class CommonService {
  constructor(private translatePipe: TranslatePipe, private http: HttpClient) {}

  timeSelectorRanges_type1 = [
    { value: 'LAST_5_MINUTE', key: 'chart.select.last5Minute' },
    { value: 'LAST_1_HOUR', key: 'chart.select.last1Hour' },
    { value: 'LAST_1_DAY', key: 'chart.select.last1Day' },
    { value: 'LAST_1_WEEK', key: 'chart.select.last1Week' },
    { value: 'LAST_1_MONTH', key: 'chart.select.last1Month' },
    { value: 'LAST_1_QUARTER', key: 'chart.select.last1Quarter' },
    { value: 'HALF_1_YEAR', key: 'chart.select.half1Year' },
    { value: 'LAST_1_YEAR', key: 'chart.select.last1Year' },
  ];

  /*

zh:'4小时' , range:'BEGIN_END_TIME', interval: "ONE_MINUTE" begin_time end_time
zh:'12小时' ,range:'BEGIN_END_TIME', interval: "ONE_MINUTE"  begin_time end_time
zh:'一天' ,range:'LAST_1_DAY', interval:  "ONE_MINUTE"
zh:'一周' ,range: 'LAST_1_WEEK', interval: "HALF_HOUR"
zh:'一个月' ,range:'LAST_1_MONTH',  interval:  "HALF_HOUR"
zh:'一年' ,range:'LAST_1_YEAR',   interval:  "DAY"
*/
  timeSelectorRanges_type2 = [
    { value: 'LAST_4_HOUR', key: 'chart.select.last4Hour' },
    { value: 'LAST_12_HOUR', key: 'chart.select.last12Hour' },
    { value: 'LAST_1_DAY', key: 'chart.select.last1Day' },
    { value: 'LAST_1_WEEK', key: 'chart.select.last1Week' },
    { value: 'LAST_1_MONTH', key: 'chart.select.last1Month' },
    { value: 'LAST_1_YEAR', key: 'chart.select.last1Year' },
  ];

  /**
   * @Description 通用的同步翻译工具函数
   * @date 2021-04-13
   * @param {any} ...args
   * @returns {any}
   */
  $t(prop) {
    const res = this.translatePipe.transform(prop);
    return res;
  }

  /**
   * @Description
   * @date 2021-04-01
   * @param {any} label  timeSelectorRanges的value
   * @returns {any} {range interval begin_time?: end_time?:}
   */
  getInfoFromTimeRange(label): typeTimeInfo {
    let info: typeTimeInfo = { range: label };
    const getSelectedDate = getSelectedDateFn(label);
    const hourExp = /LAST_(\d+)_HOUR/;
    if (hourExp.test(label)) {
      const [begin_time, end_time] = getSelectedDate('hour', hourExp);
      info = {
        range: 'BEGIN_END_TIME',
        interval: 'ONE_MINUTE',
        begin_time,
        end_time,
      };
    }
    const dayExp = /LAST_(\d+)_DAY/;
    if (dayExp.test(label)) {
      info.interval = 'ONE_MINUTE';
    }

    const weekExp = /LAST_(\d+)_WEEK/;
    if (weekExp.test(label)) {
      info.interval = 'HALF_HOUR';
    }

    const monthExp = /LAST_(\d+)_MONTH/;
    if (monthExp.test(label)) {
      info.interval = 'HALF_HOUR';
    }

    const yearExp = /LAST_(\d+)_YEAR/;
    if (yearExp.test(label)) {
      info.interval = 'DAY';
    }
    console.log(info);
    return info;
  }

  params(query: any = {}) {
    // 对query进行处理
    const p = Object.assign({}, query);
    return p;
  }

  // table数据处理
  refresh(state: ClrDatagridStateInterface, query: any) {
    console.log(state);
    let sort;
    sort = state.sort;
    // 排序 排序规则order:  true降序  false升序 ;   排序值by: 字符串  按照某个字段排序  在html里[clrDgSortBy]自定义
    if (sort) {
      query.order = sort.reverse ? 'desc' : 'asc';
      query.sort = sort.by;
    }
    // 过滤器   过滤内容
    if (state.filters) {
      for (const filter of state.filters) {
        const { property, value } = filter as { property: string; value: string };
        query[property] = value;
      }
    }
    // 分页器数据current: 1 当前页;    size: 5 分页大小
    if (state.page) {
      query.page = state.page.current;
      query.per_page = state.page.size;
    }
    const qq = { params: this.params(query) };
    console.log(qq);
    return qq;
  }

  /**
   * @Description vmfs 添加 获取主机、集群tree 1. vmfs add 获取所有集群/主机的接口
   * @date 2021-05-13
   * @returns {any}
   */
  async remoteGetVmfsDeviceList() {
    let data: any = [];
    try {
      if (isMockData) {
        data = await mockServerData(vmfsClusterTreeData);
      } else {
        const res: any = await new Promise((resolve, reject) => {
          this.http.post('accessvmware/listclusters', {}).subscribe(resolve, reject);
        });

        if (res.code === '200') {
          data = res.data;
        }
      }
    } catch (error) {
      console.log('vmfs 添加 获取主机、集群tree', error);
    } finally {
      return data;
    }
  }

  /**
   * @Description vmfs 添加 选择的设备是数组
   * @date 2021-05-13
   * @param {any} instance
   * @returns {any}
   */

  async remoteCreateVmfs(params) {
    if (isMockData) {
      console.log(params);
      return {
        code:"-99999",
        data:{
          successNo:2,
          failNo:1,
          connectionResult:["10.12.22.167"],
          descriptionEN:"loream....",
          descriptionCN:"这里有一段内容"
        }};
    } else {
      try {
        const res: any = await new Promise((resolve, reject) => {
          this.http.post('accessvmfs/createvmfsnew', params).subscribe(resolve, reject);
        });
        return res;
      } catch (error) {
        console.log('vmfs 创建', error);
      }
    }
  }

  /**
   * @Description vmfs 挂载
   * @date 2021-05-17
   * @param {any} params
   * @returns {any}
   */
  async remoteVmfs_Mount(params) {
    if (isMockData) {
      console.log(params);
      return {};
    } else {
      try {
        const res: any = await new Promise((resolve, reject) => {
          this.http.post('/accessvmfs/mountvmfsnew', params).subscribe(resolve, reject);
        });
        return res;
      } catch (error) {
        console.log('vmfs 挂载', error);
      }
    }
  }
  /**
   * @Description vmfs 挂载
   * @date 2021-05-17
   * @param {any} params
   * @returns {any}
   */
  async remoteVmfs_Unmount(params) {
    if (isMockData) {
      console.log(params);
      return {};
    } else {
      try {
        const res: any = await new Promise((resolve, reject) => {
          this.http.post('/accessvmfs/ummountvmfsnew', params).subscribe(resolve, reject);
        });
        return res;
      } catch (error) {
        console.log('vmfs 挂载', error);
      }
    }
  }

  /**
   * @Description 挂载 获取主机集群列表
   * @param {any} id
   * @returns {any}
   */
  async remoteGetVmfsDeviceListById_mount(id) {
    let data: any = [];
    try {
      if (isMockData) {
        data = await mockServerData(vmfsClusterTreeData);
      } else {
        const res: any = await new Promise((resolve, reject) => {
          this.http
            .get(`/accessvmware/getclustersbydsobjectidreturntree?dataStoreObjectId=${id}`)
            .subscribe(resolve, reject);
        });
        if (res.code === '200') {
          data = res.data;
        }
      }
    } catch (error) {
      console.log('vmfs 添加 获取主机、集群tree', error);
    } finally {
      return data;
    }
  }
  async remoteGetVmfsDeviceListById_unmount(id) {
    let data: any = [];
    try {
      if (isMockData) {
        data = await mockServerData(vmfsClusterTreeData);
      } else {
        const res: any = await new Promise((resolve, reject) => {
          this.http
            .get(`/accessvmfs/gethostgroupsbystorageidreturntree/${id}`)
            .subscribe(resolve, reject);
        });
        if (res.code === '200') {
          data = res.data;
        }
      }
    } catch (error) {
      console.log('vmfs 添加 获取主机、集群tree', error);
    } finally {
      return data;
    }
  }

  /**
   * @Description 挂载 获取选中的挂载类型：主机或者集群
   * @param {any} id
   * @returns {string}
   */
  async getMountTypeBySeletedId(id){
    let data:string=""
    try{
      if(isMockData){
        // console.log(id);
        data="host"
      }else{
        const res : any= await new Promise((resolve, reject)=>{
          this.http
            .get(`/accessvmfs/queryCreationMethodByDatastore？dataStoreObjectId=${id}`)
            .subscribe(resolve, reject);
        });
        if (res.code==='200'){
          data=res.data;
        }
      }
    }
    catch(error) {
      console.log('vmfs获取选中挂载类型',error)
    }
    finally {
      return data;
    }
  }
}

/*
async function getAccessvmwareListclusters(instance) {

  return new Promise((resolve,reject) => {
    instance
  })

} */
