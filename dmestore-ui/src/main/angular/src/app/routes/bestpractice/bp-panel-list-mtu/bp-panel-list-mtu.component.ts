import { Component, OnInit, ChangeDetectorRef, ChangeDetectionStrategy, Input, SimpleChange, Output, EventEmitter } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';
import { CommonService } from './../../common.service';
import { Host } from '../bestpractice.component';
import { HttpClient } from '@angular/common/http';
import { handleRes, stringParseObj, valueOrDefault } from './../../../app.helpers';
import { isMockData, mockData } from 'mock/mock';


/*  */
const URL_GET_VIRTUAL_NIC_LIST = 'v1/bestpractice/virtual-nic';
const URL_UPDATE_VIRTUAL_NIC_LIST = 'v1/bestpractice/virtual-nic/update';

@Component({
  selector: 'app-bp-panel-list-mtu',
  templateUrl: './bp-panel-list-mtu.component.html',
  styleUrls: ['./bp-panel-list-mtu.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [TranslatePipe]
})


export class BpPanelListMtuComponent implements OnInit {
  @Input() hostList: Host[];
  @Input() currentItem;
  @Output() onApply: EventEmitter<any>;

  /* 外部传入的hostList 请求之后获取的需要展示的list */
  dataTableList: Host[] = [];
  hostTotal = 0;
  hostIsLoading = false;
  /* 主机选中列表 */
  hostSelected = [];


  constructor(
    private http: HttpClient,
    private $utils: CommonService,
    private translatePipe: TranslatePipe,
    private cdr: ChangeDetectorRef,
  ) {
    this.onApply = new EventEmitter();
  }

  ngOnInit(): void { }

  resetVariables() {
    this.hostSelected = [];
    this.hostTotal = 0;
  }


  async getVirtualNicList(params) {
    return new Promise((resolve, reject) => {
      this.http.post(URL_GET_VIRTUAL_NIC_LIST, params).subscribe(resolve, reject)
    })
  }
  async applyBP(params) {
    return new Promise((resolve, reject) => {
      this.http.post(URL_UPDATE_VIRTUAL_NIC_LIST, params).subscribe(resolve, reject)
    })
  }


  async handleCurrentItemChange() {

  }

  async handleHostListChange(isHostListChange, hostListChange) {
    if (isHostListChange) {
      const _hostList = hostListChange.currentValue;
      this.resetVariables()
      if (_hostList.length > 0) {
        console.log("🚀 ~ file: bp-panel-list-mtu.component.ts ~ line 37 ~ BpPanelListMtuComponent ~ ngOnChanges ~ _hostList", _hostList);
        const targetObjectIds = hostListChange?.currentValue?.map(i => i.hostObjectId);
        let res;
        if (isMockData) {
          res = mockData.BESTPRACTICE_VIRTUAL_NIC
        } else {
          this.hostIsLoading = true;
          res = await this.getVirtualNicList(targetObjectIds);
          this.hostIsLoading = false;
        }
        handleRes(res, {
          fn: data => {
            const dataTableList = stringParseObj(data, []);
            this.dataTableList = dataTableList.map(i => {
              return {
                ...i,
                host: valueOrDefault(i.hostObjIp),
                device: valueOrDefault(i.device),
                nicIp: valueOrDefault(i.nicIp),
                mtu: valueOrDefault(i.mtu),
              }
            });
            this.hostTotal = this.dataTableList.length;
            this.cdr.detectChanges();
          }
        })
      } else {
        this.dataTableList = [];
      }
      this.cdr.detectChanges();
    }

  }

  ngOnChanges(changes: { [propKey: string]: SimpleChange }) {
    const hostListChange = changes['hostList'];
    /* handle hostList changed */
    this.handleHostListChange(hostListChange && Array.isArray(hostListChange.currentValue), hostListChange);
  }

  $t(prop) {
    return this.translatePipe.transform(prop);
  }
  /**
   * @Description 实施
   * @date 2021-04-13
   * @returns {any}
   */
  async applyClick() {
    console.log(this.hostSelected);

    const paramArray = this.hostSelected.map(i => ({
      "device": i.device,
      "hostObjectId": i.hostObjId
    }))
    debugger;
    try {
      const res = await this.applyBP(paramArray);
      this.onApply.emit(res);
      console.log("🚀 ~ file: bp-panel-list-mtu.component.ts ~ line 132 ~ BpPanelListMtuComponent ~ applyClick ~ res", res);
    } catch (error) {
      this.onApply.emit(error);
      console.log("🚀 ~ file: bp-panel-list-mtu.component.ts ~ line 134 ~ BpPanelListMtuComponent ~ applyClick ~ error", error);
    }
  }

  sortFunc(obj: any) {
    debugger;
    return !obj;
  }
}
